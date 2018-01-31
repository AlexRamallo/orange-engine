package com.snakeinalake.engine.animators;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.nodes.OSceneNode;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.utilities.OUtilClock;

public class ORotationAnimator extends ONodeAnimator{
	private vector3df rot;
	public boolean active = true;
	public ORotationAnimator(OrangeDevice Device, vector3df vec){
		super(Device);
		rot = vec;
		reqVis = false;
	}
	public ORotationAnimator(OrangeDevice Device, float rx, float ry, float rz){
		super(Device);
		rot = new vector3df(rx,ry,rz);
		reqVis = false;
	}
	
	@Override
	public void run(OSceneNode node){
		if(!active)return;
		node.setRotation(
				node.getRotation().x+rot.x,
				node.getRotation().y+rot.y,
				node.getRotation().z+rot.z);
	}
	@Override
	public void run(OSceneNode node, OUtilClock clock, float delay){
		if(!active)return;
		node.setRotation(
					node.getRotation().x+(rot.x*(float)clock.getElapsed()/delay),
					node.getRotation().y+(rot.y*(float)clock.getElapsed()/delay),
					node.getRotation().z+(rot.z*(float)clock.getElapsed()/delay)
				);
	}
	@Override
	public void run(ONode2D node){
		if(!active)return;
		node.setRotation(
				node.getRotation().x+rot.x,
				node.getRotation().y+rot.y,
				node.getRotation().z+rot.z);
	}
	@Override
	public void run(ONode2D node, OUtilClock clock, float delay){
		if(!active)return;
		node.setRotation(
					node.getRotation().x+(rot.x*(float)clock.getElapsed()/delay),
					node.getRotation().y+(rot.y*(float)clock.getElapsed()/delay),
					node.getRotation().z+(rot.z*(float)clock.getElapsed()/delay)
				);
	}
}
