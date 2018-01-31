package com.snakeinalake.engine.input.updated;

import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.ortho.ONode2D;

import android.view.MotionEvent;

public class OVirtualPeripheral{
	protected OInputManager mngr;
	protected ONode2D root;
	public int PointerID;
	public boolean Active;
	public int ID;
	
	protected OVirtualPeripheral(OInputManager Manager, int id){
		mngr = Manager;
		ID = id;
		PointerID = -1;
		root = new ONode2D(id);
	}
	/**
	 * Get the event data from the input manager
	 * */
	public boolean event(MotionEvent event)  throws IllegalArgumentException{
		int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
		boolean ret;
		switch(actionCode){
			default:
				ret =  false;
				break;
			case MotionEvent.ACTION_DOWN:
				ret = actionDown(event);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				ret = actionPointerDown(event);
				break;
			case MotionEvent.ACTION_UP:
				ret = actionUp(event);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				ret = actionPointerUp(event);
				break;
			case MotionEvent.ACTION_MOVE:
				ret = actionMove(event);
				break;
			case MotionEvent.ACTION_CANCEL:
				ret = actionCancel(event);
				break;
		}
		return ret;
	};
	
	public void update(MotionEvent ev) throws IllegalArgumentException{};
	
	protected boolean actionDown (MotionEvent ev) throws IllegalArgumentException{return false;};
	protected boolean actionPointerDown(MotionEvent ev) throws IllegalArgumentException{return false;};
	
	protected boolean actionUp(MotionEvent ev) throws IllegalArgumentException{return false;};
	protected boolean actionPointerUp(MotionEvent ev) throws IllegalArgumentException{return false;};
	
	protected boolean actionMove(MotionEvent ev) throws IllegalArgumentException{return false;};
	protected boolean actionCancel(MotionEvent ev) throws IllegalArgumentException{return false;};
	
	/**
	 * Move this peripherals boundbox and associated ONode2Ds 
	 * */
	public void setPosition(vector2df set){};
	public void setPosition(float X, float Y){};
	/**
	 * Get this peripherals position 
	 * */
	public vector2df getPosition(){return null;};
	
	/**
	 * Get the Root node of this peripheral
	 * */
	public ONode2D getRoot() {
		return root;
	}
	
	/**
	 * Return whether or not this peripheral should recieve information about new, unreserved, pointers
	 * */
	public boolean isAcceptingPointers(){
		if(PointerID==-1)
			return true;
		return false;
	}
}
