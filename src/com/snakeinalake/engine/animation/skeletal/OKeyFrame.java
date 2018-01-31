package com.snakeinalake.engine.animation.skeletal;

import java.util.Vector;

public class OKeyFrame{
	public int time;
	public Vector<transformPacket> transforms;
	
	public OKeyFrame(int Time){
		time = Time;
		transforms = new Vector<transformPacket>();
	}
	
	public class transformPacket{
		public OBone target;
		public boolean[] absolute;
		public float pX, pY, pZ;
		public float rX, rY, rZ;
		public float sX, sY, sZ;
	}
}
