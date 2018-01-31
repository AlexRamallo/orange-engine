package com.snakeinalake.engine.core;

import java.io.Serializable;


public class OTriangle implements Serializable{
	private static final long serialVersionUID = 8412117671898892031L;
	public vector3df A,B,C;
	public float Av[],Bv[],Cv[];
	public int Apos,Bpos,Cpos;
	public int[] indices;
	
	public OTriangle(){
		this(
			new vector3df(0,0,0),
			new vector3df(0,0,0),
			new vector3df(0,0,0)
		);
	}	
	public OTriangle(vector3df a, vector3df b, vector3df c){
		A = a;
		B = b;
		C = c;
		indices = new int[]{0,0,0};
		Av = new float[]{0.0f, 0.0f, 0.0f};
		Bv = new float[]{0.0f, 0.0f, 0.0f};
		Cv = new float[]{0.0f, 0.0f, 0.0f};
	}
}
