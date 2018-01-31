package com.snakeinalake.engine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTriangle;
import com.snakeinalake.engine.core.boundbox3df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.mesh.OMeshInfo;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class OMeshParserAOBJ extends OMeshParser{
	public boolean cachingEnabled;
	private objparsercache_packet curload;
	private OrangeDevice device;
	public class objparsercache_packet{
		public int FileId;
		public String filename;		
		public objparsercache_packet(int Fid, String fName){
			FileId = Fid;
			filename = fName;
		}
	};
	
	public OMeshParserAOBJ(OrangeDevice Device){
		type = "AOBJ";
		device = Device;
		cachingEnabled = true;
	}
	public void setCaching(boolean set){
		cachingEnabled = set;
	}
	private synchronized OMeshInfo checkCache(objparsercache_packet packet){
		if(!cachingEnabled)
			return null;
		OMeshInfo chk = null;
		if(packet.FileId!=0){
			chk = device.getOBJParserCache().get(String.valueOf(packet.FileId));
		}else{
			chk = device.getOBJParserCache().get(packet.filename);
		}
		if(chk!=null)
			return chk;
		else
			return null;
	}
	@Override
	public synchronized OMeshInfo loadMesh(Context context, int FileID){
		OMeshInfo chk = checkCache(new objparsercache_packet(FileID,null));
		if(chk!=null){
			return chk;
		}else{
			curload = new objparsercache_packet(FileID,null);
		}
		return loadMesh(context,context.getResources().openRawResource(FileID));
	}
	public synchronized OMeshInfo loadMesh(Context context, String Filename){
		OMeshInfo chk = checkCache(new objparsercache_packet(0,Filename));
		if(chk!=null){
			return chk;
		}else{
			curload = new objparsercache_packet(0,Filename);
		}
		AssetManager am = context.getAssets();
		try {
			return loadMesh(context, am.open(Filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public synchronized OMeshInfo loadMesh(Context context, InputStream is){
		OMeshInfo ret = new OMeshInfo(type);
		
		String buildWord = "";
		int data;
		int found = -1;
		int buildtri = 0;
		OTriangle tribuffer = null;
		Vector<Float> vert = new Vector<Float>();
		Vector<Float> norm = new Vector<Float>();
		Vector<Float> uv = new Vector<Float>();
		Vector<OTriangle> tris = new Vector<OTriangle>();
		Vector<cmp_objfvn> idlist = new Vector<cmp_objfvn>();
		boundbox3df size = new boundbox3df(0,0,0,
											0,0,0);

		InputStreamReader rd = new InputStreamReader(is);
		try {
			data = rd.read();
			while(data!=-1){	
				char c = (char)data;
				
					if(found == -1){
						if(c == 'v'){				//found vertex data
							found = -1;
							data = rd.read();
								if((char)data == ' '){
									found = 1;
								}else if((char)data == 't'){	//found uv map data
									data = rd.read();
									if((char)data == ' '){
										found = 3;
									}
								}else if((char)data == 'n'){	//found normal data
									data = rd.read();
									if((char)data == ' '){
										found = 4;
									}
								}							
						}else if(c == 'f'){			//found index data (faces)
							data = rd.read();
							if((char)data == ' '){
								found = 2;
								if(tribuffer == null)
									tribuffer = new OTriangle();
							}else{
									found = -1;							
								}
							}
					}
					if(found == 1){			//vertex
						float x = 0,y = 0,z = 0;					
						for(int repeat = 0; repeat<3; repeat++){
							buildWord="";
							do{
								data = rd.read();
								c = (char)data;				
								buildWord+=c;
							}while((char)data!=' ' && (char)data!='\n');
							if(repeat==0)
								x = Float.parseFloat(buildWord.trim());
							else if(repeat==1)
								y = Float.parseFloat(buildWord.trim());
							else if(repeat==2)
								z = Float.parseFloat(buildWord.trim());
						}
						if(vert.size() == 0){
							size.x=x;size.w=x;
							size.y=y;size.h=y;
							size.z=z;size.d=z;
						}else{
							if(x<size.x)
								size.x=x;
							else if(x>size.w)
								size.w=x;
							
							if(y<size.y)
								size.y=y;
							else if(y>size.h)
								size.h=y;
							
							if(z<size.z)
								size.z=z;
							else if(z>size.d)
								size.d=z;
						}
						vert.add(x);
						vert.add(y);
						vert.add(z);
						found = -1;
					}
					
					if(found == 2){			//face
						short v1 = 0, v2 = 0, v3 = 0;
						short n1 = 0, n2 = 0, n3 = 0;
						int map1 = 0, map2 = 0, map3 = 0;
						boolean uvdata = false,normdata = false;
						for(int repeat = 0; repeat<3; repeat++){
							buildWord="";
							do{
								data = rd.read();
								if(!uvdata && (char)data == '/'){
									uvdata = true;								
								}else{
									c = (char)data;				
									buildWord+=c;
								}
							}while((char)data!=' ' && (char)data!='\n' && (char)data!='/');
							if(repeat==0){
								v1 = Short.parseShort(buildWord.trim());
								v1--;
							}else if(repeat==1){
								v2 = Short.parseShort(buildWord.trim());
								v2--;
							}else if(repeat==2){
								v3 = Short.parseShort(buildWord.trim());
								v3--;
							}
							
							if(uvdata){		//Face UV mapping
								uvdata = false;
								buildWord = "";
								do{
									data = rd.read();
									c = (char)data;
									if(!normdata && (char)data == '/'){
										normdata = true;								
									}else{
										buildWord+=c;
									}
								}while((char)data!=' ' && (char)data!='\n' && (char)data!='/');
								if(repeat == 0){
									map1 = Integer.parseInt(buildWord.trim());
									map1--;
								}else if(repeat == 1){
									map2 = Integer.parseInt(buildWord.trim());
									map2--;
								}else if(repeat == 2){
									map3 = Integer.parseInt(buildWord.trim());
									map3--;
								}else{
									map1 = 0; map2  = 0; map3 = 0;					
								}
							}
							if(normdata){		//Face UV mapping
								normdata = false;
								buildWord = "";
								do{
									data = rd.read();
									c = (char)data;
									buildWord+=c;
								}while((char)data!=' ' && (char)data!='\n');
								if(repeat == 0){
									n1 = Short.parseShort(buildWord.trim());
									n1--;
								}else if(repeat == 1){
									n2 = Short.parseShort(buildWord.trim());
									n2--;
								}else if(repeat == 2){
									n3 = Short.parseShort(buildWord.trim());
									n3--;
								}else{
									n1 = 0; n2  = 0; n3 = 0;					
								}
							}
						}		
						
						idlist.add(new cmp_objfvn(v1,map1,n1));
						idlist.add(new cmp_objfvn(v2,map2,n2));
						idlist.add(new cmp_objfvn(v3,map3,n3));
						
						switch(buildtri){
							case 0:
								tribuffer.A = new vector3df(vert.get((v1*3))
										,vert.get((v1*3)+1),vert.get((v1*3)+2));
								buildtri = 1;
								break;
							case 1:
								tribuffer.B = new vector3df(vert.get((v2*3))
										,vert.get((v2*3)+1),vert.get((v2*3)+2));
								buildtri = 2;
								break;
							case 2:
								tribuffer.C = new vector3df(vert.get((v3*3))
										,vert.get((v3*3)+1),vert.get((v3*3)+2));
								buildtri = 0;
								tris.add(tribuffer);
								tribuffer = null;
								break;
						}
						
						found = -1;
					}
					if(found == 3){		//UV coordinates
						float uvx = 0, uvy = 0;					
						for(int repeat = 0; repeat<2; repeat++){
							buildWord="";
							do{
								data = rd.read();
								c = (char)data;				
								buildWord+=c;
							}while((char)data!=' ' && (char)data!='\n');
							if(repeat==0)
								uvx = Float.parseFloat(buildWord.trim());
							else if(repeat==1)
								uvy = Float.parseFloat(buildWord.trim());
						}
						uv.add(uvx);
						uv.add(uvy);
						found = -1;
					}
					if(found == 4){			//normal
						float x = 0,y = 0,z = 0;					
						for(int repeat = 0; repeat<3; repeat++){
							buildWord="";
							do{
								data = rd.read();
								c = (char)data;				
								buildWord+=c;
							}while((char)data!=' ' && (char)data!='\n');
							if(repeat==0)
								x = Float.parseFloat(buildWord.trim());
							else if(repeat==1)
								y = Float.parseFloat(buildWord.trim());
							else if(repeat==2)
								z = Float.parseFloat(buildWord.trim());
						}
						norm.add(x);
						norm.add(y);
						norm.add(z);
						found = -1;
					}
				
				data = rd.read();
			}
			rd.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ret.vertices = new float[vert.size()];
		for(int i=0; i<vert.size(); i++){
			ret.vertices[i] = vert.get(i);
			Log.d("CATCH", Float.toString(ret.vertices[i]));
		}
		
		ret.normals = new float[norm.size()];
		for(int i=0; i<norm.size(); i++){
			ret.normals[i] = norm.get(i);
			Log.d("CATCH", Float.toString(ret.normals[i]));
		}
		/**
		 * make sure normal array isn't empty for meshes without normal data
		 * */
		if(norm.size()<=0){
			ret.normals = new float[]{
				0, 0, 0
			};
		}
		
		ret.uvtex = uv;				//Assign UV coordinates
		ret.indexlist = idlist;	//Assign indices
		ret.size = size;
		ret.fillBuffers();
		
		if(curload.FileId!=0){
			device.getOBJParserCache().put(String.valueOf(curload.FileId), ret);
		}else{
			device.getOBJParserCache().put(curload.filename, ret);
		}
		
		return ret;
	}
}