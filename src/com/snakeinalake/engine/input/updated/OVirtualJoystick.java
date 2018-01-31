package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2DSprite;
import com.snakeinalake.engine.utilities.OBasicCollision;

public class OVirtualJoystick extends OVirtualPeripheral{
	private ONode2DSprite sprite1, sprite2;
	private boundbox2df box;
	private vector3df joystickCenter;
	private vector3df position;
	public float dir, dist;
	private boolean Enabled = true;
	
	public OVirtualJoystick(OInputManager Manager, int id, vector3df Position, ONode2DSprite sp1, ONode2DSprite sp2){
		super(Manager, id);
		sprite1 = sp1;
		sprite2 = sp2;
		Active = false;
		position = Position;
		sprite1.setPosition(position);
		
		joystickCenter = new vector3df();
		joystickCenter.x = position.x+(sprite1.getWidth()/2);
		joystickCenter.y = position.y+(sprite1.getHeight()/2);
		dir = 0;
		dist = 0;
		sprite2.setPosition(joystickCenter.x-(sprite2.getWidth()/2),joystickCenter.y-(sprite2.getHeight()/2));

		box = new boundbox2df(position.x,position.y,sprite1.getWidth(),sprite1.getHeight());
	}
	public OVirtualJoystick(OInputManager Manager, int id, float px, float py, ONode2DSprite sp1, ONode2DSprite sp2){
		super(Manager, id);
		sprite1 = sp1;
		sprite2 = sp2;
		Active = false;
		position = new vector3df(px,py,0);
		sprite1.setPosition(position);
		
		joystickCenter = new vector3df();
		joystickCenter.x = position.x+(sprite1.getWidth()/2);
		joystickCenter.y = position.y+(sprite1.getHeight()/2);
		dir = 0;
		dist = 0;
		sprite2.setPosition(joystickCenter.x-(sprite2.getWidth()/2),joystickCenter.y-(sprite2.getHeight()/2));

		box = new boundbox2df(position.x,position.y,sprite1.getWidth(),sprite1.getHeight());
	}
	@Override
	public vector2df getPosition() {
		return box.asVector2df();
	}
	public void setEnabled(boolean set){
		Enabled = set;
	}
	public boolean isEnabled(){
		return Enabled;
	}
	@Override
	public void update(MotionEvent event){
		if(!Enabled)return;
		if(!Active){
			dir = 0;
			dist = 0;
		}else{
			dir = OBasicCollision.vectorDirection(
					joystickCenter.x, joystickCenter.y,
					event.getX(event.findPointerIndex(PointerID)),
					event.getY(event.findPointerIndex(PointerID)));
			
			dist = OBasicCollision.distance2D(joystickCenter.x,joystickCenter.y,
							event.getX(event.findPointerIndex(PointerID)),
							event.getY(event.findPointerIndex(PointerID)));
		}
		dist = OBasicCollision.clamp(dist, -sprite1.getWidth()/2, sprite1.getWidth()/2);
		sprite2.setPosition(
				((float)(joystickCenter.x+Math.cos(dir)*dist))-(sprite2.getWidth()/2),
				((float)(joystickCenter.y-Math.sin(dir)*dist))-(sprite2.getHeight()/2));
	}
	@Override
	protected boolean actionDown(MotionEvent ev){
		int pid = 0;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);		
		if(box.intersectsWith(xx,yy)){
			if(mngr.reserve(ID, ev.getPointerId(pid))){
				Active = true;
				PointerID = ev.getPointerId(pid);
				return true;
			}
		}		
		return false;
	}
	@Override
	protected boolean actionPointerDown(MotionEvent ev){
		int pid = ev.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		for(int i=0; i<ev.getPointerCount(); i++){
			pid = ev.getPointerId(i);
			float xx = ev.getX(pid);
			float yy = ev.getY(pid);
			if(box.intersectsWith(xx,yy)){
				if(mngr.reserve(ID, ev.getPointerId(pid))){
					Active = true;
					PointerID = ev.getPointerId(pid);
					return true;
				}
			}		
		}
		return false;
	}
}
