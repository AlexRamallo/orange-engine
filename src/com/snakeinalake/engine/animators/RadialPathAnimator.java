package com.snakeinalake.engine.animators;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.nodes.OSceneNode;
import com.snakeinalake.engine.utilities.OBasicCollision;
import com.snakeinalake.engine.utilities.OUtilClock;

public class RadialPathAnimator extends ONodeAnimator{
	private OSceneNode target;
	private float speed;
	public RadialPathAnimator(OrangeDevice Device, OSceneNode Target, float Speed){
		super(Device);
		speed = Speed;
		target = Target;
		reqVis = true;
	}
	
	@Override
	public void run(OSceneNode node){
		vector3df pos = node.getPosition();
		vector3df tpos = target.getPosition();
		float dir = OBasicCollision.vectorDirection(pos.x, pos.z, tpos.x, tpos.z);
		float r90 = (float) (90f*Math.PI/180f);
		pos.x+=(float)(Math.cos(dir+r90)*speed);
		pos.z-=(float)(Math.sin(dir+r90)*speed);
	}
	@Override
	public void run(OSceneNode node, OUtilClock clock, float delay){
		vector3df pos = node.getPosition();
		vector3df tpos = target.getPosition();
		float dir = OBasicCollision.vectorDirection(pos.x, pos.z, tpos.x, tpos.z);
		float r90 = (float) (90f*Math.PI/180f);
		pos.x+=(float)(Math.cos(dir+r90)*(speed*((float)clock.getElapsed()/delay)));
		pos.z-=(float)(Math.sin(dir+r90)*(speed*((float)clock.getElapsed()/delay)));
	}
}
