package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;

public class OVirtualButton extends OVirtualPeripheral{
	private ONode2D sprite1, sprite2;
	private boundbox2df box;
	private boolean released,enabled;
	
	public OVirtualButton(OInputManager Manager, int id, vector3df position, ONode2D sp1, ONode2D sp2){
		super(Manager, id);
		sprite1 = sp1;
		sprite2 = sp2;
		Active = false;
		released = false;
		
		sprite1.setPosition(position);
		sprite2.setPosition(position);
		
		sprite1.setVisible(false);
		sprite2.setVisible(true);
		enabled = true;
		box = new boundbox2df(position.x,position.y,sprite1.getWidth(),sprite1.getHeight());
	}
	public OVirtualButton(OInputManager Manager, int id, float px, float py, ONode2D sp1, ONode2D sp2){
		super(Manager, id);
		sprite1 = sp1;
		sprite2 = sp2;
		Active = false;
		released = false;
		
		sprite1.setPosition(px,py);
		sprite2.setPosition(px,py);
		
		sprite1.setVisible(false);
		sprite2.setVisible(true);
		enabled = true;
		box = new boundbox2df(px,py,sprite1.getWidth(),sprite1.getHeight());
	}
	@Override
	public vector2df getPosition() {
		return box.asVector2df();
	}
	@Override
	public void setPosition(vector2df set) {
		root.setPosition(set);
		sprite1.setPosition(set);
		sprite2.setPosition(set);
		box.setPosition(set);		
	}
	@Override
	public void setPosition(float X, float Y) {
		root.setPosition(X, Y);
		sprite1.setPosition(X, Y);
		sprite2.setPosition(X, Y);
		box.setPosition(X, Y);		
	}
	public void setEnabled(boolean set){
		enabled = set;
		sprite1.setVisible(!enabled);
		sprite2.setVisible(enabled);
	}
	public boolean isEnabled(){
		return enabled;
	}
	public void setVisible(boolean set){
		sprite1.setVisible(set);
		sprite2.setVisible(set);
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
		if(enabled){
			if(!Active){
				sprite1.setVisible(false);
				sprite2.setVisible(true);
			}else{
				sprite1.setVisible(true);
				sprite2.setVisible(false);
			}
		}
	}
	@Override
	protected boolean actionDown(MotionEvent ev){
		if(!enabled)return false;
		int pid = 0;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);		
		if(box.intersectsWith(xx,yy)){
			if(mngr.reserve(ID, ev.getPointerId(pid))){
				Active = true;
				PointerID = ev.getPointerId(pid);
				released = false;
				return true;
			}
		}		
		return false;
	}
	@Override
	protected boolean actionPointerDown(MotionEvent ev){
		if(!enabled)return false;
		int pid = ev.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		for(int i=0; i<ev.getPointerCount(); i++){
			pid = ev.getPointerId(i);
				float xx = ev.getX(pid);
				float yy = ev.getY(pid);
				if(box.intersectsWith(xx,yy)){
					if(mngr.reserve(ID, ev.getPointerId(pid))){
						Active = true;
						PointerID = ev.getPointerId(pid);
						released = false;
						return true;
					}
				}		
		}
		return false;
	}
	
	@Override
	protected boolean actionUp(MotionEvent ev) {
		if(!enabled)return false;
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
		if(!enabled)return false;
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









