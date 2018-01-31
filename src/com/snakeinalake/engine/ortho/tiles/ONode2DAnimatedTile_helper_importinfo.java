package com.snakeinalake.engine.ortho.tiles;

import java.util.Vector;

public class ONode2DAnimatedTile_helper_importinfo{
	public Vector<OAnimatedTileFrame> dframes;
	public Vector<OAnimSet> frames;
	public boolean repeat;
	public ONode2DAnimatedTile_helper_importinfo(){
		//--
	}
	//public ONode2DAnimatedTile_helper_importinfo(Vector<OAnimatedTileFrame> Frames, boolean Repeat){
	public ONode2DAnimatedTile_helper_importinfo(Vector<OAnimSet> Frames, Vector<OAnimatedTileFrame> dFrames, boolean Repeat){
		frames = Frames;
		dframes = dFrames;
		repeat = Repeat;
	}
}