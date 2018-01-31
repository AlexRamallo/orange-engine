package com.snakeinalake.engine.animators;


import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.nodes.OSceneNode;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.utilities.OUtilClock;

public class ONodeAnimator{
	public boolean reqVis;
	protected OrangeDevice device;
	public ONodeAnimator(OrangeDevice Device){
		device = Device;
		reqVis = false;
	}
	public void run(OSceneNode node){
		//--3D NODES--
	}
	public void run(ONode2D node){
		//--GUI NODES--
	}
	public void run(OSceneNode node, OUtilClock clock, float delay){
		//--3D NODES--
	}
	public void run(ONode2D node, OUtilClock clock, float delay){
		//--GUI NODES--
	}
}
