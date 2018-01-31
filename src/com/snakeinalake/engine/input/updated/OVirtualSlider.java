package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;

public class OVirtualSlider extends OVirtualPeripheral{
	private ONode2D sprite;
	private boundbox2df box;
	private float pct;
	private boolean vert;
	//Whether or not to restrain slide gesture to sprite dimensions
	private boolean restrain = true;
	
	public OVirtualSlider(OInputManager Manager, int id, vector3df position, ONode2D sp1, boolean Vert){
		super(Manager, id);
		sprite = sp1;
		Active = false;
		pct = 0;
		vert = Vert;
		sprite.setPosition(position);

		box = new boundbox2df(position.x,position.y,sprite.getWidth(),sprite.getHeight());
	}
	public OVirtualSlider(OInputManager Manager, int id, float px, float py, ONode2D sp1, boolean Vert){
		super(Manager, id);
		sprite = sp1;
		Active = false;
		pct = 0;
		vert = Vert;
		sprite.setPosition(px,py);

		box = new boundbox2df(px,py,sprite.getWidth(),sprite.getHeight());
	}
	/**
	 * Whether or not to restrain the sliding gesture to the dimensions of the sprite.
	 * 
	 * If FALSE, user will be able to drag finger outside of the sprite, and will be able to achieve values higher than 100%
	 * */
	public void setRestrainToDimensions(boolean set){
		restrain = set;
	}
	public boolean getRestrainToDimensions(){
		return restrain;
	}
	public ONode2D getNode(){
		return sprite;
	}
	@Override
	public vector2df getPosition() {
		return box.asVector2df();
	}
	@Override
	public void setPosition(vector2df set) {
		sprite.setPosition(set);
		box.setPosition(set);		
	}
	@Override
	public void setPosition(float X, float Y) {
		sprite.setPosition(X, Y);
		box.setPosition(X, Y);		
	}
	public float getPercent(){
		if(restrain){
			if(pct>1)
				return 1;
			if(pct<0)
				return 0;
		}
		return pct;
	}
	public void setPercent(float set){
		pct = set;
	}
	@Override
	public void update(MotionEvent event){
		if(!Active)return;
		if(vert)
			pct = event.getY(event.findPointerIndex(PointerID))/sprite.getHeight();
		else
			pct = event.getX(event.findPointerIndex(PointerID))/sprite.getWidth();
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









