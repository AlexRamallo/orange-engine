package com.snakeinalake.engine.core;

public class vector3df extends vector2df{
	private static final long serialVersionUID = 8099118480780958629L;
	public float z;
	private vector3df parent;
	
	public vector3df(){
		super(0,0);
		//parent = null;
		z=0;
	}
	public vector3df(vector2df set){
		this(set,0);
	}
	public vector3df(vector2df set, float Z){
		this(set.x,set.y,Z);
	}
	public vector3df(float X, float Y, float Z){
		super(X,Y);
		z = Z;
		//parent = null;
	}
	public void setParent(vector3df Parent){
		parent = Parent;
	}
	public float getX(){
		if(parent == null || parent == this)
			return x;
		return x+parent.getX();
	}
	public float getY(){
		if(parent == null || parent == this)
			return x;
		return y+parent.getY();
	}
	public float getZ(){
		if(parent == null || parent == this)
			return x;
		return z+parent.getZ();
	}	
	/*public vector3df add(vector3df vec){
		return new vector3df(
				x+vec.x,
				y+vec.y,
				z+vec.z);
	}
	public vector3df subtract(vector3df vec){
		return new vector3df(
				x-vec.x,
				y-vec.y,
				z-vec.z);
	}
	public vector3df multiply(vector3df vec){
		return new vector3df(
				x*vec.x,
				y*vec.y,
				z*vec.z);
	}
	public vector3df divide(vector3df vec){
		return new vector3df(
				x/vec.x,
				y/vec.y,
				z/vec.z);
	}
	
	public vector3df add(float xx, float yy, float zz){
		return new vector3df(
				x+xx,
				y+yy,
				z+zz);
	}
	public vector3df subtract(float xx, float yy, float zz){
		return new vector3df(
				x-xx,
				y-yy,
				z-zz);
	}
	public vector3df multiply(float xx, float yy, float zz){
		return new vector3df(
				x*xx,
				y*yy,
				z*zz);
	}
	public vector3df divide(float xx, float yy, float zz){
		return new vector3df(
				x/xx,
				y/yy,
				z/zz);
	}*/
}
