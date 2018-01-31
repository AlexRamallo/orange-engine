package com.snakeinalake.engine.nodes.frames;

import com.snakeinalake.engine.utilities.OUtilClock.IFC_UNIT;

public class OFrameInfo {
	/**
	 * ID of the mesh to render while this frame is visible
	 * */
	public int MID;
	/**
	 * Amount of time that this frame is visible
	 * */
	public float Time;
	/**
	 * Unit for Time
	 * */
	public IFC_UNIT Unit;
	public String Timer;
	
	public OFrameInfo(int meshid, float time, IFC_UNIT unit, String timer){
		MID = meshid;
		Time = time;
		Unit = unit;
		Timer = timer;
	}
}
