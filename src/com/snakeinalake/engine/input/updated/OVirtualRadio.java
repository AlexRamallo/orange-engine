package com.snakeinalake.engine.input.updated;

import android.view.MotionEvent;

import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.ortho.ONode2D.InheritanceList;

public class OVirtualRadio extends OVirtualPeripheral{
	private ONode2D unchecked, checked, label, label2;
	private boundbox2df box;
	private boolean state;
	private float padding;
	
	public OVirtualRadio(OInputManager Manager, int id, vector3df position, ONode2D Checked, ONode2D UnChecked, ONode2D Label, ONode2D Label2){
		super(Manager, id);
		checked = Checked;
		unchecked = UnChecked;
		label = Label;
		label2 = Label2;
		if(label == null){
			label = new ONode2D(-1);
		}
		if(label2 == null){
			label2 = new ONode2D(-1);
		}
		Active = false;
		state = false;
		checked.setParent(root);
		unchecked.setParent(root);
		label.setParent(root);
		label2.setParent(root);
		root.setPosition(position);
		
		checked.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		unchecked.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		label.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		label2.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		
		label.setPosition(label.getAbsolutePosition().x+padding+checked.getWidth(),(checked.getHeight()/2)-(label.getHeight()/2));
		label2.setPosition(label2.getAbsolutePosition().x+padding+unchecked.getWidth(),(unchecked.getHeight()/2)-(label2.getHeight()/2));
		
		checked.setVisible(false);
		unchecked.setVisible(true);

		box = new boundbox2df(position.x,position.y,checked.getWidth(),checked.getHeight());
	}
	public OVirtualRadio(OInputManager Manager, int id, float px, float py, ONode2D Checked, ONode2D UnChecked, ONode2D Label, ONode2D Label2){
		super(Manager, id);
		checked = Checked;
		unchecked = UnChecked;
		label = Label;
		label2 = Label2;
		if(label == null){
			label = new ONode2D(-1);
		}
		if(label2 == null){
			label2 = new ONode2D(-1);
		}
		Active = false;
		state = false;
		checked.setParent(root);
		unchecked.setParent(root);
		label.setParent(root);
		label2.setParent(root);
		root.setPosition(px,py);
		
		checked.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		unchecked.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		label.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		label2.setInheritanceTrait(InheritanceList.IN_VISIBILITY, false);
		
		label.setPosition(label.getAbsolutePosition().x+padding+checked.getWidth(),(checked.getHeight()/2)-(label.getHeight()/2));
		label2.setPosition(label2.getAbsolutePosition().x+padding+unchecked.getWidth(),(unchecked.getHeight()/2)-(label2.getHeight()/2));
		
		checked.setVisible(false);
		unchecked.setVisible(true);

		box = new boundbox2df(px,py,checked.getWidth(),checked.getHeight());
	}
	/**
	 * @param set the horizontal padding (in pixels) between the label and radio sprites
	 * */
	public void setPadding(float set){
		padding = set;
		label.setPosition(padding+checked.getWidth(),(checked.getHeight()/2)-(label.getHeight()/2));
		label2.setPosition(padding+unchecked.getWidth(),(unchecked.getHeight()/2)-(label2.getHeight()/2));
	}
	public float getPadding(){
		return padding;
	}
	public void setState(boolean set){
		state = set;
	}
	@Override
	public vector2df getPosition() {
		return box.asVector2df();
	}
	@Override
	public void setPosition(vector2df set) {
		root.setPosition(set);
		box.setPosition(set);
	}
	@Override
	public void setPosition(float X, float Y) {
		root.setPosition(X,Y);
		box.setPosition(X,Y);
	}
	
	public boolean getState(){
		return state;
	}
	
	@Override
	public void update(MotionEvent event){
		if(!state){
			checked.setVisible(false);
			label.setVisible(false);
			
			unchecked.setVisible(true);
			label2.setVisible(true);
		}else{
			unchecked.setVisible(false);
			label2.setVisible(false);
			
			checked.setVisible(true);
			label.setVisible(true);
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
	
	@Override
	protected boolean actionUp(MotionEvent ev) {
		int pid = 0;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);
		if(box.intersectsWith(xx,yy)){
			state=(state)?false:true;
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean actionPointerUp(MotionEvent ev) {
		int pid = ev.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		float xx = ev.getX(pid);
		float yy = ev.getY(pid);
		if(box.intersectsWith(xx,yy)){
			state=(state)?false:true;
			return true;
		}
		return false;
	}
}









