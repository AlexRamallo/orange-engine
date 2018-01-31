package com.snakeinalake.engine.tools.wsds;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.snakeinalake.engine.core.vector3df;

import android.content.Context;

public class OWSDSFile{
	public Vector<OWSDSBlock> Blocks;
	
	public OWSDSFile(Context context, String file){
		int data = 0,wscnt = 0;
		boolean counting = false,building = false;;
		Blocks = new Vector<OWSDSBlock>();
		String buildWord = "";
		OWSDSBlock buffer = null;
		try {
			InputStream is = context.getAssets().open(file);
			InputStreamReader rd = new InputStreamReader(is);
			
			while(data!=-1){
				char c = (char)data;
				wscnt = (counting)?wscnt:0;
					do{
						data = rd.read();
						c=(char)data;
						if(c == ' '){
							wscnt++;	
							counting = true;
						}else
							counting=false;
					}while(counting);
					
					if(building == true){
						Blocks.add(buffer);
						buffer = new OWSDSBlock();
						data = rd.read();
						building = false;
					}
					if(c=='&'){
						do{
							data = rd.read();
							c = (char)data;				
							buildWord+=c;
							}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
							counting = false;
							buffer = new OWSDSBlock();
							buffer.ID = Integer.parseInt(buildWord.trim());
					}else{
						buildWord="";
						switch(wscnt){
							default:
								break;
							case 1:	//Mesh file
								do{
									c = (char)data;				
									buildWord+=c;
									data = rd.read();
									}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
									buffer.MeshFile = buildWord;
									counting = false;
								break;
							case 2:	//Texture file
								do{
									c = (char)data;				
									buildWord+=c;
									data = rd.read();
									}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
									buffer.TexFile = buildWord;
									counting = false;
								break;
							case 3:	//Position
								vector3df buildpos = new vector3df(0,0,0);
								do{
									c = (char)data;				
									buildWord+=c;
									data = rd.read();
									}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
									buildpos.x = Float.parseFloat(buildWord.trim());
									buildWord="";
									do{
										c = (char)data;				
										buildWord+=c;
										data = rd.read();
										}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
										buildpos.y = Float.parseFloat(buildWord.trim());
										buildWord="";
										do{
											c = (char)data;				
											buildWord+=c;
											data = rd.read();
											}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
											buildpos.z = Float.parseFloat(buildWord.trim());
											buildWord="";
									buffer.Position = buildpos;
									counting = false;
								break;
							case 4:	//Rotation
								vector3df buildrot = new vector3df(0,0,0);
								do{
									c = (char)data;				
									buildWord+=c;
									data = rd.read();
									}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
								buildrot.x = Float.parseFloat(buildWord.trim());
								buildWord="";
									do{
										c = (char)data;				
										buildWord+=c;
										data = rd.read();
										}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
									buildrot.y = Float.parseFloat(buildWord.trim());
									buildWord="";
										do{
											c = (char)data;
											buildWord+=c;
											data = rd.read();
											}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
										buildrot.z = Float.parseFloat(buildWord.trim());
										buildWord="";
									buffer.Rotation = buildrot;
									counting = false;
								break;
							case 5:	//Scale
								vector3df buildscal = new vector3df(0,0,0);
								do{
									c = (char)data;				
									buildWord+=c;
									data = rd.read();
									}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
								buildscal.x = Float.parseFloat(buildWord.trim());
								buildWord="";
									do{
										c = (char)data;				
										buildWord+=c;
										data = rd.read();
										}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
									buildscal.y = Float.parseFloat(buildWord.trim());
									buildWord="";
										do{
											c = (char)data;				
											buildWord+=c;
											data = rd.read();
											}while((char)data!=' ' && (char)data!='\n' && (char)data!='#');
										buildscal.z = Float.parseFloat(buildWord.trim());
										buildWord="";
									buffer.Scale = buildscal;
									counting = false;
									building = true;
								break;
						}
					}
			}
			rd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}