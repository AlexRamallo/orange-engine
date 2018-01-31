package com.snakeinalake.engine.ortho.tiles;

import java.util.Vector;

public class OAnimSet{
	public Vector<OAnimatedTileFrame> frames;
	public boolean loop;
	public String name;
	
	public OAnimSet(){
		name="default";
		loop=false;
		frames=new Vector<OAnimatedTileFrame>();
	}
}
