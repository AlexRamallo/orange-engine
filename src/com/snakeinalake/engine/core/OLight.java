package com.snakeinalake.engine.core;

import javax.microedition.khronos.opengles.GL10;


public class OLight{
	public float[] pos,dir;
	public float[] specular, diffuse, ambient;
	private int LightID;
	public OLight(){
		this(-1, null, null, null, null, null);
	}
	public OLight(int ID, vector3df pos){
		this(ID, pos, null, null, null, null);
	}
	public OLight(int ID, vector3df pos, vector3df dir){
		this(ID, pos, dir, null, null, null);
	}
	public OLight(int ID, vector3df position, vector3df direction, float[] spec, float[] diff, float[] amb){
		LightID = ID;
		specular = spec;
		diffuse = diff;
		ambient = amb;
		pos = new float[]{position.x,position.y,position.z};
		dir = new float[]{direction.x,direction.y,direction.z};
		/*ByteBuffer Bp = ByteBuffer.allocateDirect(3*4);
		Bp.position(0);
		pos = Bp.asFloatBuffer();
		pos.put(new float[]{position.x, position.y, position.z});
		pos.position(0);
		
		ByteBuffer Bv = ByteBuffer.allocateDirect(3*4);
		Bv.position(0);
		dir = Bv.asFloatBuffer();
		dir.put(new float[]{direction.x, direction.y, direction.z});
		dir.position(0);
		
		ByteBuffer Bs = ByteBuffer.allocateDirect(4*4);
		Bs.position(0);
		specular = Bs.asFloatBuffer();
		specular.put(spec);
		specular.position(0);
		
		ByteBuffer Ba = ByteBuffer.allocateDirect(4*4);
		Ba.position(0);
		ambient = Ba.asFloatBuffer();
		ambient.put(amb);
		ambient.position(0);
		
		ByteBuffer Bd = ByteBuffer.allocateDirect(4*4);
		Bd.position(0);
		diffuse = Bd.asFloatBuffer();
		diffuse.put(diff);
		diffuse.position(0);*/
	}
	public void enable(GL10 gl){
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_SPECULAR, specular, 0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);
		//gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, dir, 0);
		//gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 180.0f);
	}
	public void disable(GL10 gl){
		gl.glDisable(LightID);
	}
}
