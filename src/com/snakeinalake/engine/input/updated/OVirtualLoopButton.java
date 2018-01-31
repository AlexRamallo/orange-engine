package com.snakeinalake.engine.input.updated;

import java.util.Vector;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.ortho.ONode2D.InheritanceList;

public class OVirtualLoopButton extends OVirtualPeripheral{
	private Vector<ONode2D> options;
	private Vector<boundbox2df> box;
	private int cur;
	
	public OVirtualLoopButton(OInputManager Manager, int id, vector3df position, Vector<ONode2D> Options){
		super(Manager, id);
		options = Options;
		Active = false;
		cur = 0;
		root.setPosition(position);
		box = new Vector<boundbox2df>();
		for(int i=0; i<options.size(); i++){
			options.get(i).setParent(root);
			options.get(i).setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
			box.add(
				new boundbox2df(position.x,position.y,options.get(i).getWidth(),options.get(i).getHeight()));
		}

	}
	public OVirtualLoopButton(OInputManager Manager, int id, float px, float py, Vector<ONode2D> Options){
		super(Manager, id);
		options = Options;
		Active = false;
		cur = 0;
		root.setPosition(px, py);
		box = new Vector<boundbox2df>();
		for(int i=0; i<options.size(); i++){
			options.get(i).setParent(root);
			options.get(i).setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
			box.add(
				new boundbox2df(px,py,options.get(i).getWidth(),options.get(i).getHeight()));
		}

	}
	/**
	 * Get the currently selected choice
	 * */
	public int getCurrent(){
		return cur;
	}
	/**
	 * Set the currently selected choice
	 * */
	public void setCurrent(int set){
		cur = set;
	}
	@Override
	public vector2df getPosition() {
		vector2df ret;
		try{
			ret = box.get(cur).asVector2df();
		}catch(ArrayIndexOutOfBoundsException e){
			ret = new vector2df(0,0);
		}
		return ret;
	}
	@Override
	public void setPosition(vector2df set) {
		root.setPosition(set);
		for(boundbox2df bx: box){
			bx.setPosition(set);
		}
	}
	@Override
	public void setPosition(float X, float Y) {
		root.setPosition(X,Y);
		for(boundbox2df bx: box){
			bx.setPosition(X,Y);
		}
	}
	/**
	 * Called AFTER every touch event
	 * */
	@Override
	public void update(MotionEvent event){
		if(cur>=options.size()){
			cur = 0;
		}
		for(int i=0; i<options.size(); i++){
			options.get(i).setVisible((cur == i)?true:false);
		}
	}
	@Override
	protected boolean actionDown(MotionEvent ev){
		int pid = 0;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);		
		if(box.get(cur).intersectsWith(xx,yy)){
			if(mngr.reserve(ID, ev.getPointerId(pid))){
				Active = true;
				PointerID = ev.getPointerId(pid);
				cur++;
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
			if(box.get(cur).intersectsWith(xx,yy)){
				if(mngr.reserve(ID, ev.getPointerId(pid))){
					Active = true;
					PointerID = ev.getPointerId(pid);
					cur++;
					return true;
				}
			}		
		}
		return false;
	}
}









