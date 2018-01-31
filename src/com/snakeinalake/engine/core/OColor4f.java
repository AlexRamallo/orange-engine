package com.snakeinalake.engine.core;

import java.io.Serializable;

public class OColor4f implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7260241450069313911L;
	public float r,g,b,a;
	
	public OColor4f(float R, float G, float B, float A){
		r=R;
		g=G;
		b=B;
		a=A;
	}
	public OColor4f(float R, float G, float B){
		this(R,G,B,1.0f);
	}
	public OColor4f(){
		this(1.0f,1.0f,1.0f,1.0f);
	}
	public OColor4f invert(){
		return new OColor4f(
				Math.abs(1-r),
				Math.abs(1-g),
				Math.abs(1-b));
	}
}
