package com.snakeinalake.engine.nodes;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OOrthoEnvironment;
import com.snakeinalake.engine.OSceneManager;
import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.OUtilClock;
import com.snakeinalake.engine.animators.ONodeAnimator;

public class OSceneNode implements Cloneable{
	protected volatile vector3df position;
	protected volatile vector3df rotation;
	protected volatile vector3df scale;
	protected volatile boolean visible;
	protected volatile OSceneNode parent;
	private volatile OrangeDevice device;
	protected volatile Vector<ONodeAnimator> animators;
	protected volatile int Id;
	
	public OSceneNode(int ID){
		Id = ID;
		parent = null;
		position = new vector3df(0.0f,0.0f,0.0f);
		rotation = new vector3df(0.0f,0.0f,0.0f);
		scale = new vector3df(1.0f,1.0f,1.0f);
		visible = true;
		animators = new Vector<ONodeAnimator>();
	}
	protected OrangeDevice getDevice(){
		return device;
	}
	public void setDeviceReference(OrangeDevice set){
		device = set;
	}
	protected OSceneManager getSceneManager(){
		return device.getSceneManager();
	}
	protected OOrthoEnvironment getEnvironment(){
		return device.getOrthoEnvironment();
	}
	/**
	 * Sets the ID of the node. When the scene manager creates a node with one
	 * of its functions, it automatically assigns a unique Id to it, so generally
	 * this function won't be used very often.
	 * */
	public void setId(int set){
		Id = set;
	}
	/**
	 * Add an animator to this node
	 * */
	public void addAnimator(ONodeAnimator add){
		animators.add(add);
	}
	public OSceneNode clone(){
		try {
			return (OSceneNode)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Animate the animators of this node
	 * */
	public void animate(){
		for(ONodeAnimator anim: animators){
			if(anim.reqVis){
				if(visible)
					anim.run(this);
			}else
				anim.run(this);
		}
	}
	/**
	 * Animate the animators of this node
	 * */
	public void animate(OUtilClock clock, float delay){
		for(ONodeAnimator anim: animators){
			if(anim.reqVis){
				if(visible)
					anim.run(this, clock, delay);
			}else
				anim.run(this, clock, delay);
		}
	}
	/**
	 * Get the nodes unique id
	 * */
	public int getId(){
		return Id;
	}
	/**
	 * 	Set the nodes parent
	 * */
	public void setParent(OSceneNode Parent){
		parent = Parent;
	}
	/**
	 * 	Get the nodes parent
	 * */
	public OSceneNode getParent(){
		return parent;
	}
	/**
	 * Get the current position of the node
	 * */
	public vector3df getPosition(){
		return position;
	}
	/**
	 * Get the current rotation of the node
	 * */	
	public vector3df getRotation(){
		return rotation;
	}
	/**
	 * Get the current scale of the node
	 * */
	public vector3df getScale(){
		return scale;
	}
	/**
	 * Set the position of the node
	 * @param set new Position
	 * */
	public void setPosition(vector3df set){
		position.x = set.x;
		position.y = set.y;
		position.z = set.z;
	}
	/**
	 * Set the position of the node
	 * @param set new Position
	 * */
	public void setPosition(float X, float Y, float Z){
		position.x=X;
		position.y=Y;
		position.z=Z;
	}
	/**
	 * Set the rotation of the node
	 * */
	public void setRotation(vector3df set){
		rotation.x = set.x;
		rotation.y = set.y;
		rotation.z = set.z;
	}
	/**
	 * Set the rotation of the node
	 * */
	public void setRotation(float X, float Y, float Z){
		rotation.x=X;
		rotation.y=Y;
		rotation.z=Z;
	}
	/**
	 * Set the scale of the node
	 * */
	public void setScale(vector3df set){
		scale.x = set.x;
		scale.y = set.y;
		scale.z = set.z;
	}
	/**
	 * Set the scale of the node
	 * */
	public void setScale(float X, float Y, float Z){
		scale.x=X;
		scale.y=Y;
		scale.z=Z;
	}
	/**
	 * Render the node
	 * It is automatically done by the scene manager but may be called manually.
	*/
	public void render(GL10 gl){
		return;
	}
	/**
	 * Check if the node is visible
	 * */
	public boolean isVisible(){
		return visible;
	}
	/**
	 * Make the node visible or invisible
	 * */
	public void setVisible(boolean set){
		visible = set;
	}
}
