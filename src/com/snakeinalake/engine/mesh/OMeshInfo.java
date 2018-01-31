package com.snakeinalake.engine.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import com.snakeinalake.engine.core.OTriangle;
import com.snakeinalake.engine.core.boundbox3df;
import com.snakeinalake.engine.parser.cmp_objfvn;
import com.snakeinalake.engine.parser.OMeshParserOBJ.objparsercache_packet;

/*
 * The point of this class is to add flexibility when trying to allow support for multiple file types.
 * 
 * The only thing required for a loader to work is to make it fill in the data in this class and passed to
 * an instance of CMesh.
 * 
 * for example, to implement a loader for skeletons I can either add a bones array variable to this class,or 
 * create a child that extends this class with the new bones array, then I can just pass the new class as an argument to 
 * the constructor of a NEW class which extends CMesh to allow support for controlling bones etc.
 * 
 * The info in this class is what is required for basic rendering. Extra stuff can come later in the future
 * 
 * The rendering data is requested by the CMesh class through its 'public void render()' function which is called
 * by the scene manager during rendering.
 * 
 * */

public class OMeshInfo {
	public objparsercache_packet hash;
	public FloatBuffer vertBuff;
	public FloatBuffer normBuff;
	public FloatBuffer uvBuff;
	public ShortBuffer IndBuff;
	public Vector<OTriangle> tris;
	public boundbox3df size;
	/**Vertex data*/
	public float vertices[];
	/**Normal data*/
	public float normals[];
	/**UV coordinates*/
	public Vector<Float> uvtex;				
	/**UV indices*/
	//public Vector<int[]> uvMappings;	
	/**Face indices*/
	public Vector<cmp_objfvn> indexlist;
	private String type;
	public int numFaces;
	
	public OMeshInfo(){
		type = "unknown";
	}
	public OMeshInfo(String Type){
		type = Type;
	}
	public String getType(){
		return type;		
	}
	/**
	 * This method will use the data you've passed to it to create buffers and prepare them for passing to a mesh.<br/>
	 * Before calling this method, the following data must be filled by you:<br/>
	 * <ul>
	 * 		<li><b>numFaces</b> - The total number of faces in the whole mesh</li>
	 * 		<li><b>vertices</b> - Float array of vertices</li>
	 * 		<li><b>normals</b> - Float array of vertex normals</li>
	 * 		<li><b>uvtex</b> - Vector&lt;Float&gt; of UV Coordinates</li>
	 * 		<li><b>indexlist</b> - Face indices</li>
	 * </ul>
	 * */
	public void fillBuffers(){
		numFaces = indexlist.size();
		ByteBuffer vbb = ByteBuffer.allocateDirect(numFaces * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertBuff = vbb.asFloatBuffer();
		

		ByteBuffer nbb = ByteBuffer.allocateDirect(numFaces * 3 * 4);
		nbb.order(ByteOrder.nativeOrder());
		normBuff = nbb.asFloatBuffer();
		
		ByteBuffer ubb = ByteBuffer.allocateDirect(numFaces * 2 * 3 * 4);
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
		
		uvBuff.position(0);
		vertBuff.position(0);
		normBuff.position(0);
		OTriangle tbuf = new OTriangle();
		tris = new Vector<OTriangle>();
		int tbIter = 0;
		for(int i=0; i<numFaces; i++){
			//Build first point...
			if(tbIter == 0){
				tbuf.Av[0] = vertices[(int) (indexlist.get(i).Vi * 3)];
				tbuf.Av[1] = vertices[(int) (indexlist.get(i).Vi * 3 + 1)];
				tbuf.Av[2] = vertices[(int) (indexlist.get(i).Vi * 3 + 2)];
				vertBuff.put(tbuf.Av[0]);
				vertBuff.put(tbuf.Av[1]);
				vertBuff.put(tbuf.Av[2]);
				tbuf.indices[0] = (int) indexlist.get(i).Vi;
				tbuf.Apos = vertBuff.position()-3;
			}
			//Build second point...
			if(tbIter == 1){
				tbuf.Bv[0] = vertices[(int) (indexlist.get(i).Vi * 3)];
				tbuf.Bv[1] = vertices[(int) (indexlist.get(i).Vi * 3 + 1)];
				tbuf.Bv[2] = vertices[(int) (indexlist.get(i).Vi * 3 + 2)];
				vertBuff.put(tbuf.Bv[0]);
				vertBuff.put(tbuf.Bv[1]);
				vertBuff.put(tbuf.Bv[2]);
				tbuf.indices[1] = (int) indexlist.get(i).Vi;
				tbuf.Bpos = vertBuff.position()-3;
			}
			//Build third point...
			if(tbIter == 2){
				tbuf.Cv[0] = vertices[(int) (indexlist.get(i).Vi * 3)];
				tbuf.Cv[1] = vertices[(int) (indexlist.get(i).Vi * 3 + 1)];
				tbuf.Cv[2] = vertices[(int) (indexlist.get(i).Vi * 3 + 2)];
				vertBuff.put(tbuf.Cv[0]);
				vertBuff.put(tbuf.Cv[1]);
				vertBuff.put(tbuf.Cv[2]);
				tbuf.indices[2] = (int) indexlist.get(i).Vi;
				tbuf.Cpos = vertBuff.position()-3;
			}		
			//increment face counter
			tbIter++;
			
			//If we've built 3 points already
			if(tbIter == 3){
				//push new face to list
				tris.add(tbuf);
				//prepare new face for building
				tbuf = new OTriangle();
				//reset point counter
				tbIter = 0;
			}
			//Put normal's X,Y,Z values into the normal buffer
			normBuff.put(normals[(int) (indexlist.get(i).Ni * 3)]);
			normBuff.put(normals[(int) (indexlist.get(i).Ni * 3 + 1)]);
			normBuff.put(normals[(int) (indexlist.get(i).Ni * 3 + 2)]);
			
			//Put UV X,Y coords into the UV buffer
			uvBuff.put(uvtex.get(indexlist.get(i).Ti*2));
			uvBuff.put(1.0f - uvtex.get(indexlist.get(i).Ti*2+1));
		}
		ByteBuffer ibb = ByteBuffer.allocateDirect(indexlist.size()*3*2);
		ibb.order(ByteOrder.nativeOrder());
		IndBuff = ibb.asShortBuffer();
		
		for(int i=0; i<indexlist.size(); i++){
			IndBuff.put((short)i);
		}
		
		IndBuff.position(0);
		uvBuff.position(0);
		vertBuff.position(0);
		normBuff.position(0);
		
	}
}
