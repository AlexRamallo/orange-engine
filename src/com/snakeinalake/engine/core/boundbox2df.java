package com.snakeinalake.engine.core;

public class boundbox2df extends vector3df{
	private static final long serialVersionUID = 3395728708879496687L;
	/**
	 * Width of the bounding box
	 * */
	public float w;
	/**
	 * Height of the bounding box
	 * */
	public float h;
	
	public boundbox2df(){
		this(0,0,0,0);
	}
	public boundbox2df(vector2df pos){
		this(pos,0,0);
	}
	public boundbox2df(vector2df pos, float Width, float Height){
		this(pos.x,pos.y,Width,Height);
	}
	/**
	 * Change the X and Y values of this box with a vector3df. Z value is ignored
	 * */
	public void setPosition(vector3df set){
		x = set.x;
		y = set.y;
	}
	/**
	 * Change the X and Y values of this box with a vector3df. Z value is ignored
	 * */
	public void setPosition(float X, float Y){
		x = X;
		y = Y;
	}
	/**
	 * Change the X and Y values of this box with a vector3df. Y value is ignored
	 * */
	public void setPositionz(vector3df set){
		x = set.x;
		y = set.z;
	}
	/**
	 * Change the X and Y values of this box with a vector2df
	 * */
	public void setPosition(vector2df set){
		x = set.x;
		y = set.y;
	}
	public boundbox2df(float X, float Y, float Width, float Height){
		super(X,Y,0);
		w = Width;
		h = Height;
	}
	/**
	 * Get the X value of this box
	 * */
	public float getX(){
		return x;
	}
	/**
	 * Get the Y value of this box
	 * */
	public float getY(){
		return y;
	}
	/**
	 * Get the Width value of this box
	 * */
	public float getW(){
		return w;
	}
	/**
	 * Get the Height value of this box
	 * */
	public float getH(){
		return h;
	}
	/**
	 * Combine another boundbox with this one by recreating it so that both are contained in one big box
	 * *Important Note*: Only this boundboxes values are changed, the target is not touched
	 * @param target the 2D boundbox to combine with this one
	 * */
	public void combine(boundbox2df target){
		if(target.x<x){
			x = target.x;
		}
		if(target.y<y){
			y = target.y;
		}
		if(target.w+target.x>relw()){
			w = target.w+target.x;
		}
		if(target.h+target.y>relh()){
			h = target.h+target.y;
		}
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
	 * Simple algorithm to check if this bounding box intersects with another one
	 * @param box The other bounding box to check against
	 * @return true if it intersects, false otherwise
	 * */
	public boolean intersectsWith(boundbox2df box){
		/*
		return (((x<=box.relw()) && (y<=box.relh()))
				&& ((relw()>=box.x) && (relh()>=box.y)));
		 */
		return (((x<=box.w) && (y<=box.h))
				&& ((w>=box.x) && (h>=box.y)));
	}
	/**
	 * Simple algorithm to check if this bounding box intersects with another one; relative dimensions
	 * @param box The other bounding box to check against
	 * @return true if it intersects, false otherwise
	 * */
	public boolean intersectsWithRelative(boundbox2df box){
		return (((x<=box.relw()) && (y<=box.relh()))
				&& ((relw()>=box.x) && (relh()>=box.y)));
	}
	/**
	 * Simple algorithm to check if the given point is within the bounds of the bounding box
	 * @param point The point to check
	 * @return true if it is inside, false otherwise
	 * */
	public boolean intersectsWith(vector2df point){
		return (((x<point.x) && (y<point.y))
				&& ((relw()>point.x) && (relh()>point.y)));
	}
	/**
	 * Simple algorithm to check if the given point is within the bounds of the bounding box
	 * @param X The X position of the point to check
	 * @param Y The Y position of the point to check
	 * @return true if it is inside, false otherwise
	 * */
	public boolean intersectsWith(float X, float Y){
		return (((x<X) && (y<Y))
				&& ((relw()>X) && (relh()>Y)));
	}
	/**
	 * Return a new bounding box with the same dimensions relative to the given vector
	 * */
	public boundbox2df relativeTo(vector2df location){
		return new boundbox2df(x+location.x,y+location.y,w+location.x,h+location.y);
	}
	/**
	 * Create a new vector2df with the data from this bounding box
	 * */
	public vector2df asVector2df(){
		//return (vector2df)this;
		return new vector2df(x,y);
	}
}
