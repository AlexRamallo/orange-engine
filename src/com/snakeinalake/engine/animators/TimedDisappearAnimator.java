package com.snakeinalake.engine.animators;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.utilities.OUtilClock;

public class TimedDisappearAnimator extends ONodeAnimator{
	private String timer;
	public TimedDisappearAnimator(OrangeDevice Device, String timername){
		super(Device);
		timer=timername;
		reqVis = true;
	}
	
	@Override
	public void run(ONode2D node){
		if(!device.getClock().isSetTimer(timer))
			return;
		if(device.getClock().getTimer(timer)<=0){
			node.setVisible(false);
			device.getClock().removeTimer(timer);
		}else{
			node.setVisible(true);
		}
	}
	@Override
	public void run(ONode2D node, OUtilClock clock, float delay){
		if(!clock.isSetTimer(timer))
			return;
		if(clock.getTimer(timer)<=0){
			node.setVisible(false);
			clock.removeTimer(timer);
		}else{
			node.setVisible(true);
		}
	}
}
