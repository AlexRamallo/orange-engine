package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2DSprite;

public class OVirtualDPad extends OVirtualPeripheral{
	private ONode2DSprite sprite1;
	private boundbox2df box;
	private vector3df position;
	public boolean left,right,up,down;
	/**
	 * Accept diagonal input
	 * */
	public boolean diag;
	
	public OVirtualDPad(OInputManager Manager, int id, vector3df Position, ONode2DSprite sp1){
		super(Manager, id);
		sprite1 = sp1;
		Active = false;
		position = Position;
		sprite1.setPosition(position);
		left=right=up=down=false;		
		box = new boundbox2df(position.x,position.y,sprite1.getWidth(),sprite1.getHeight());
	}
	public OVirtualDPad(OInputManager Manager, int id, float px, float py, ONode2DSprite sp1){
		super(Manager, id);
		sprite1 = sp1;
		Active = false;
		position = new vector3df(px,py,0);
		sprite1.setPosition(position);
		left=right=up=down=false;		
		box = new boundbox2df(position.x,position.y,sprite1.getWidth(),sprite1.getHeight());
	}
	@Override
	public vector2df getPosition() {
		return box.asVector2df();
	}
	@Override
	public void update(MotionEvent event){
		if(!Active){
			left=right=up=down=false;
		}else{
			float cx = event.getX(event.findPointerIndex(PointerID));
			float cy = event.getY(event.findPointerIndex(PointerID));
			
			left=right=up=down=false;
			if(cy<=position.y+(box.h/3))
				up = true;
			else if(cy>=position.y+(2*(box.h/3)))
				down = true;
			else if(cx<=position.x+(box.w/3))
				left = true;
			else if(cx>=position.x+(2*(box.w/3)))
				right = true;
			//left = !hemi1;	right = hemi1;	up = !hemi2;	down = hemi2;
		}
	}
	public void setVisible(boolean set){
		sprite1.setVisible(set);
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
