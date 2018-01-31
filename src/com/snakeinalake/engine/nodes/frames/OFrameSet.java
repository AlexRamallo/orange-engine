package com.snakeinalake.engine.nodes.frames;

import java.util.Vector;

import com.snakeinalake.engine.mesh.OMesh;

public class OFrameSet {
	/**
	 * Name to identify this frameset
	 * */
	public String name;
	/**
	 * List of frames associated to this frameset
	 * */
	public Vector<OFrameInfo> frameInfo;
	/**
	 * Whether or not this frameset loops by default
	 * */
	public boolean loop;
	
	public OFrameSet(String Name){
		name=Name;
		frameInfo = new Vector<OFrameInfo>();
		loop = true;
	}
	public OFrameSet(){
		this("default");
	}
}
