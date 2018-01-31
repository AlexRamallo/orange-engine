package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;

public class OVirtualToggleButton extends OVirtualPeripheral{
	private ONode2D sprite1, sprite2;
	private boundbox2df box;
	private boolean released,on;
	
	public OVirtualToggleButton(OInputManager Manager, int id, float px, float py, ONode2D sp1, ONode2D sp2){
		super(Manager, id);
		sprite1 = sp1;
		sprite2 = sp2;
		Active = false;
		released = false;
		
		sprite1.setPosition(px,py);
		sprite2.setPosition(px,py);
		on = false;
		sprite1.setVisible(!on);
		sprite2.setVisible(on);

		box = new boundbox2df(px,py,sprite1.getWidth(),sprite1.getHeight());
	}
	public OVirtualToggleButton(OInputManager Manager, int id, vector3df position, ONode2D sp1, ONode2D sp2){
		this(Manager,id,position.x,position.y,sp1,sp2);
	}
	public void setToggle(boolean set){
		on=set;
	}
	public boolean isToggled(){
		return on;
	}
	@Override
	public vector2df getPosition() {
		return box.asVector2df();
	}
	@Override
	public void setPosition(vector2df set) {
		sprite1.setPosition(set);
		sprite2.setPosition(set);
		box.setPosition(set);		
	}
	@Override
	public void setPosition(float X, float Y) {
		sprite1.setPosition(X, Y);
		sprite2.setPosition(X, Y);
		box.setPosition(X, Y);		
	}
	/**
	 * Check if the user released the finger while still in the node
	 * */
	public boolean isReleased(){
		boolean ret = released;
		released = false;
		return ret;
	}
	public void toggleSwitch(){
		ONode2D tmp = sprite1;
		sprite1 = sprite2;
		sprite2 = tmp;
		tmp = null;
	}
	@Override
	public void update(MotionEvent event){
		sprite1.setVisible(!on);
		sprite2.setVisible(on);
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
				if(released){
					on = !on;
					released = false;
				}
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
					if(released){
						on = !on;
						released = false;
					}
					return true;
				}
			}		
		}
		return false;
	}
	
	@Override
	protected boolean actionUp(MotionEvent ev) {
		int pid = 0;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);
		if(box.intersectsWith(xx,yy)){
			released = true;
			return true;
		}
		released = false;
		return false;
	}
	
	@Override
	protected boolean actionPointerUp(MotionEvent ev) {
		int pid = ev.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);
		if(box.intersectsWith(xx,yy)){
			released = true;
			return true;
		}
		released = false;
		return false;
	}
}









