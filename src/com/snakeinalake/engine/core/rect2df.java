package com.snakeinalake.engine.core;

import java.io.Serializable;

public class rect2df implements Serializable{
	private static final long serialVersionUID = -1883704859175732801L;
	public float X,Y,W,H;
	
	public rect2df(float x, float y, float w, float h){
		X = x;
		Y = y;
		W = w;
		H = h;
	}
	public rect2df(){
		X = 0;
		Y = 0;
		W = 0;
		H = 0;
	}
	public rect2df(vector2df tl, vector2df br){
		X = tl.x;
		Y = tl.y;
		W = br.x;
		H = br.y;
	}
}
