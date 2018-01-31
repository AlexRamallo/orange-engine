package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.OBasicCollision;

public class OVirtualSlideGesture extends OVirtualPeripheral{
	private float slidexdist,slideydist,slidedir;
	private vector3df gestorig;
	private boundbox2df box;

	public OVirtualSlideGesture(OInputManager Manager, int id, boundbox2df Box){
		super(Manager, id);
		slidexdist = 0;
		slideydist = 0;
		slidedir = 0;
		box = Box;
		gestorig = new vector3df(0,0,0);
	}
	public float getXDist(){
		return slidexdist;
	}
	public float getYDist(){
		return slideydist;
	}
	public float getDir(){
		return slidedir;
	}
	@Override
	public void update(MotionEvent event){
		if(Active){
			slidedir = OBasicCollision.vectorDirection(
					gestorig.x, gestorig.y,
					event.getX(event.findPointerIndex(PointerID)),
					event.getY(event.findPointerIndex(PointerID)));
			
			slidexdist = event.getX(event.findPointerIndex(PointerID))-gestorig.x;
			slideydist = event.getY(event.findPointerIndex(PointerID))-gestorig.y;
		}
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
				gestorig.x = ev.getX(ev.findPointerIndex(PointerID));
				gestorig.y = ev.getY(ev.findPointerIndex(PointerID));
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
					gestorig.x = ev.getX(ev.findPointerIndex(PointerID));
					gestorig.y = ev.getY(ev.findPointerIndex(PointerID));
					return true;
				}
			}		
		}
		return false;
	}
}
