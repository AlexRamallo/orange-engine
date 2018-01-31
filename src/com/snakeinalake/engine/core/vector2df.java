package com.snakeinalake.engine.core;

import java.io.Serializable;

public class vector2df implements Serializable{
	private static final long serialVersionUID = -2390794830962466418L;
	public float x,y;
	
	public vector2df(){
		x=0; y=0;
	}
	public vector2df(vector3df set){
		x=set.x; y=set.y;
	}
	public vector2df(vector3df set, boolean switchYaxis){
		x=set.x; y=(switchYaxis)?set.z:set.y;
	}
	public vector2df(float X, float Y){
		x = X;
		y = Y;
	}
}
