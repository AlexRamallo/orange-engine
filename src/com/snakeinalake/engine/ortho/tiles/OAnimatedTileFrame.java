package com.snakeinalake.engine.ortho.tiles;

import com.snakeinalake.engine.utilities.OUtilClock.IFC_UNIT;

public class OAnimatedTileFrame {
	public int fX,fY;
	public String timer;
	public float time;
	public IFC_UNIT unit;
	public OAnimatedTileFrame(){
		//--
	}
	/**
	 * Copy
	 * */
	public OAnimatedTileFrame(OAnimatedTileFrame copy){
		fX = copy.fX;
		fY = copy.fY;
		timer = copy.timer+"[NEWCOPY+ID:"+12+Math.random()*13343+"]";
		time = copy.time;
		unit = copy.unit;
	}
}
