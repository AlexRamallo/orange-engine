package com.snakeinalake.engine.core;

public class boundbox3df extends vector3df{
	private static final long serialVersionUID = -544554589795096471L;
	/**
	 * Depth of the bounding box
	 * */
	public float w;
	public float h;
	public float d;
	
	public boundbox3df(){
		super(0,0,0);
		w=0;
		h=0;
		d=0;
	}
	public boundbox3df(vector3df set){
		super(set);
		w=0;
		h=0;
		d=0;
	}
	public boundbox3df(float X, float Y, float Z, float Width, float Height, float Depth){
		super(X,Y,Z);
		w=Width;
		h=Height;
		d=Depth;
	}
	/**
	 * Relative width, as in, the X position + Width
	 * */
	public float relw(){
		return x+w;
	}
	/**
	 * Relative height, as in, the Y position + Height
	 * */
	public float relh(){
		return y+h;
	}
	/**
	 * Relative depth, as in, the Z position + Depth
	 * */
	public float reld(){
		return z+d;
	}
	/**
	 * Simple algorithm to check if this bounding box intersects with another one
	 * @param box The other bounding box to check against
	 * @return true if it intersects, false otherwise
	 * */
	public boolean intersectsWith(boundbox3df box){
		return (((x<box.relw()) && (y<box.relh()) && (z<box.reld()))
				&& ((relw()>box.x) && (relh()>box.y) && (reld()>box.d)));
	}
	/**
	 * Create a new vector3df with the data from this bounding box
	 * */
	public vector3df asVector3df(){
		return new vector3df(x,y,z);
	}
}
