package com.snakeinalake.engine.nodes;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.snakeinalake.engine.core.vector3df;

public class OCameraSceneNode extends OSceneNode{
	vector3df target,upvec;
	private float DOF = 45.0f;

	public OCameraSceneNode(int ID) {
		super(ID);
		target = new vector3df(0,0,0);
		upvec = new vector3df(0,1,0);
	}
	/**
	 * Set the camera's Depth of Field value
	 * 
	 * Default value: 45.0f
	 * */
	public void setDepthOfField(float set){
		DOF = set;
	}
	/**
	 * Get the camera's Depth of Field value
	 * */
	public float getDepthOfField(){
		return DOF;
	}
	/**
	 * Get the target position of the camera
	 * */
	public vector3df getTarget(){
		return target;
	}
	/**
	 * Set the target of the camera
	 * */
	public void setTarget(vector3df set){
		target.x = set.x;
		target.y = set.y;
		target.z = set.z;
	}
	/**
	 * Set the target of the camera
	 * */
	public void setTarget(float X, float Y, float Z){
		target.x = X;
		target.y = Y;
		target.z = Z;
	}
	/**
	 * Get the up vector of the camera
	 * */
	public vector3df getUpVector(){
		return upvec;
	}
	/**
	 * Set the up vector of the camera
	 * */
	public void setUpVector(vector3df set){
		upvec = set;		
	}
	@Override
	public void render(GL10 gl){
		GLU.gluLookAt(gl, position.x, position.y, position.z,
										target.x, target.y, target.z,
													upvec.x, upvec.y, upvec.z);
	}
}
