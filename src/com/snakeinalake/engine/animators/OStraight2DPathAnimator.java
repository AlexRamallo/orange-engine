package com.snakeinalake.engine.animators;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.nodes.OSceneNode;
import com.snakeinalake.engine.utilities.OUtilClock;

public class OStraight2DPathAnimator extends ONodeAnimator{
	//private float speed;
	public OStraight2DPathAnimator(OrangeDevice Device, float Speed){
		super(Device);
		//speed = Speed;
		reqVis = true;
	}
	
	@Override
	public void run(OSceneNode node){
		/*float dir = (float)((node.getRotation().y-90)/180*Math.PI);
		node.setPosition(node.getPosition().add(new vector3df(
				(float)(Math.cos(dir)*speed),
				0f,
				(float)(-Math.sin(dir)*speed)))
		);*/
	}
	@Override
	public void run(OSceneNode node, OUtilClock clock, float delay){
		/*float dir = (float)(node.getRotation().y*Math.PI/180);
		node.setPosition(node.getPosition().add(new vector3df(
				(float)(Math.cos(dir)*(speed*((float)clock.getElapsed()/delay))),
				0f,
				(float)(-Math.sin(dir)*(speed*((float)clock.getElapsed()/delay)))))
		);*/
	}
}
