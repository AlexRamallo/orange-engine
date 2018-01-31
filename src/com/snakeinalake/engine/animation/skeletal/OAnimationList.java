package com.snakeinalake.engine.animation.skeletal;

import java.util.Vector;

public class OAnimationList{
	/** Type of interpolation to use */
	public int interpolation;
	public Vector<OKeyFrame> frames;
	
	public OAnimationList(){
		frames = new Vector<OKeyFrame>();
	}
}
