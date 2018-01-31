package com.snakeinalake.engine.animators;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.utilities.OBasicCollision;
import com.snakeinalake.engine.utilities.OUtilClock;

public class FadeAwayAnimator extends ONodeAnimator{
	private float speed;
	private String offset;
	private boolean offdone;
	public FadeAwayAnimator(OrangeDevice Device, float Speed){
		super(Device);
		offset = null;
		offdone = false;
		speed=Speed;
		reqVis = true;
	}
	public FadeAwayAnimator(OrangeDevice Device, float Speed, String Offset){
		super(Device);
		offset = Offset;
		offdone = false;
		speed=Speed;
		reqVis = true;
	}
	public void setSpeed(float set){
		speed = set;
	}
	@Override
	public void run(ONode2D node){
		if((offset==null)?false:((device.getClock().isSetTimer(offset))?true:false)){
			if(device.getClock().getTimer(offset)<=0){
				offdone = true;
			}
			if(offdone){
				if(node.getAlpha()>0){
					node.setAlpha(OBasicCollision.clamp(node.getAlpha()-speed, 0, 1.0f));
				}else{
					device.getClock().removeTimer(offset);
					offdone = false;
				}
			}
		}else{
			if(node.getAlpha()>0){
				node.setAlpha(OBasicCollision.clamp(node.getAlpha()-speed, 0, 1.0f));
			}
		}
	}
	@Override
	public void run(ONode2D node, OUtilClock clock, float delay){
		if((offset==null)?false:((device.getClock().isSetTimer(offset))?true:false)){
			if(device.getClock().getTimer(offset)<=0){
				offdone = true;
			}
			if(offdone){
				if(node.getAlpha()>0){
					node.setAlpha(OBasicCollision.clamp(node.getAlpha()-speed, 0, 1.0f));
				}else{
					device.getClock().removeTimer(offset);
					offdone = false;
				}
			}
		}else{
			if(node.getAlpha()>0){
				node.setAlpha(OBasicCollision.clamp(node.getAlpha()-speed, 0, 1.0f));
			}
		}
	}
}
