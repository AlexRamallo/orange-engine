package com.snakeinalake.engine.ortho;

import java.io.Serializable;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.animators.ONodeAnimator;
import com.snakeinalake.engine.core.OColor4f;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.OUtilClock;

public class ONode2D implements Serializable{
	private static final long serialVersionUID = -3059005104659337115L;
	//POSITION	-	Location of the node
	//BOUNDS	-	rectangle surrounding visual node (eg sprite)
	public vector3df position;
	public vector3df rotation;
	public vector3df scale;
	public vector3df rel_position,rel_rotation,rel_scale;
	public boolean visible;
	public ONode2D parent;
	private boolean remove;
	public int Id;
	public volatile Vector<ONodeAnimator> animators;
	private Vector<ONode2D> children;
	private int childid;
	public OColor4f col;
	public boundbox2df bounds,rbounds;
	public boolean renderable;
	public int blend[] = {GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA};
	public ORenderPass PreRenderPass,PostRenderPass;
	
	public Object user;
	
	public boolean inheritance[];
	public enum InheritanceList{
		IN_VISIBILITY,
		IN_POSITION,
		IN_ROTATION,
		IN_SCALE,
		IN_ALPHA,
		IN_REMOVE
	}
	public ONode2D(int ID, OrangeDevice Device){
		Id = ID;
		visible = true;
		position = new vector3df(0.0f,0.0f,0.0f);
		rotation = new vector3df(0.0f,0.0f,0.0f);
		scale = new vector3df(1.0f,1.0f,1.0f);
		rel_position = new vector3df(0f,0f,0f);
		rel_rotation = new vector3df(0f,0f,0f);
		rel_scale = new vector3df(1f,1f,1f);
		remove = false;
		parent = null;
		animators = new Vector<ONodeAnimator>();
		children = new Vector<ONode2D>();
		childid = 0;
		col = new OColor4f();
		bounds = new boundbox2df(0,0,0,0);
		rbounds = new boundbox2df(0,0,0,0);
		renderable = true;
		inheritance = new boolean[]{
				true,	//Visibility
				true,	//Position
				true,	//Rotation
				true,	//Scale
				true,	//Alpha
				true,	//Remove
		};
	}
	public void setPreRenderPass(ORenderPass set){
		PreRenderPass = set;
	}
	public void setPostRenderPass(ORenderPass set){
		PostRenderPass = set;
	}
	/**
	 * Set this node's glBlendFunction and its children's 
	 * */
	public ONode2D setBlendFunction(int src, int dest){
		blend[0] = src;
		blend[1] = dest;
		for(int i=0; i<children.size(); i++)
			children.get(i).setBlendFunction(src, dest);
		return this;
	}
	public void setDepth(float set){
		position.z = set;
	}
	public float getDepth(){
		return position.z;
	}
	public ONode2D(int ID){
		this(ID,null);
	}
	public Vector<ONode2D> getChildren() {
		return children;
	}
	/**
	 * Set whether or not this node is rendered by the OOrthoEnvironment
	 * @param set True means renderable false means otherwise
	 * */
	public ONode2D setRenderable(boolean set){
		renderable = set;
		return this;
	}
	/**
	 * Whether or not this node is renderable by the OOrthoEnvironment
	 * @return boolean with true meaning renderable false meaning not
	 * */
	public boolean isRenderable(){
		return renderable;
	}
	/**
	 * Changes the vertices so that the origin is 'moved' to the
	 * middle of the node for perfectly centered transformations
	 * */
	public void setCenter(){
		//TODO Implement
	}
	/**
	 * set which values to inherit from the parent(if any).
	 * By default, all are set to true
	 * @param attribute enum representing the type of attribute
	 * @param value true to accept inheritance, false to deny it
	 * */
	public ONode2D setInheritanceTrait(InheritanceList attribute, boolean value){
		switch(attribute){
			default:
				break;
			case IN_VISIBILITY:
				inheritance[0] = value;
			break;
			case IN_POSITION:
				inheritance[1] = value;
			break;
			case IN_ROTATION:
				inheritance[2] = value;
			break;
			case IN_SCALE:
				inheritance[3] = value;
			break;
			case IN_ALPHA:
				inheritance[4] = value;
			break;
			case IN_REMOVE:
				inheritance[5] = value;
			break;
		}
		return this;
	}
	
	/**
	 * get the boolean associated with the attribute
	 * @param attribute enum representing the type of attribute
	 * @return true if allow inheritance, false if deny
	 * */
	public boolean getInheritanceTrait(InheritanceList attribute){
		switch(attribute){
			default:
				break;
			case IN_VISIBILITY:
				return inheritance[0];
			case IN_POSITION:
				return inheritance[1];
			case IN_ROTATION:
				return inheritance[2];
			case IN_SCALE:
				return inheritance[3];
			case IN_ALPHA:
				return inheritance[4];
			case IN_REMOVE:
				return inheritance[5];
		}
		return false;
	}
	
	public float getWidth(){
		return bounds.w;
	}
	public float getHeight(){
		return bounds.h;
	}
	/**
	 * Set the alpha value of this node and its children
	 * */
	public ONode2D setAlpha(float set){
		col.a=set;
		//for(int i=0; i<children.size(); i++)
		//	children.get(i).setAlpha(set);
		return this;
	}
	public float getAlpha(){
		return col.a;
	}
	/**
	 * Set the color of this node and its children
	 * */
	public ONode2D setColor(OColor4f set){
		col = set;
		//for(int i=0; i<children.size(); i++)
		//	children.get(i).setColor(set);
		return this;
	}
	/**
	 * Get the color of this node
	 * */
	public OColor4f getColor(){
		return col;
	}
	/**
	 * Set the color of this node
	 * */
	public ONode2D setColor(float R, float G, float B, float A){
		col.r = R;
		col.g = G;
		col.b = B;
		col.a = A;
		for(int i=0; i<children.size(); i++)
			children.get(i).setColor(R,G,B,A);
		return this;
	}
	/**
	 * Set the parent of this node
	 * */
	public ONode2D setParent(ONode2D set){
		if(set == null){
			parent.children.remove(childid);
			childid = -1;
		}else{
			parent = set;
			childid = parent.children.size();
			parent.children.add(this);
			setAlpha(parent.getAlpha());
			setColor(parent.getColor());
			setBlendFunction(parent.blend[0], parent.blend[1]);
		}
		return this;
	}
	/**
	 * Add an animator to this node
	 * */
	public ONode2D addAnimator(ONodeAnimator add){
		animators.add(add);
		return this;
	}
	/**
	 * Animate the animators of this node
	 * */
	public ONode2D animate(){
		for(int i=0; i<animators.size(); i++){
			ONodeAnimator anim = animators.get(i);
			if(anim.reqVis){
				if(visible)
					anim.run(this);
			}else
				anim.run(this);
		}
		return this;
	}
	public ONodeAnimator getAnimator(int pos){
		return animators.get(pos);
	}
	/**
	 * Animate the animators of this node
	 * */
	public ONode2D animate(OUtilClock clock, float delay){
		for(ONodeAnimator anim: animators){
			if(anim.reqVis){
				if(visible)
					anim.run(this, clock, delay);
			}else
				anim.run(this, clock, delay);
		}
		return this;
	}
	/**
	 * Get the parent of this node
	 * */
	public ONode2D getParent(){
		return parent;
	}
	/**
	 * Returns true if remove() was called previously
	 * */
	public boolean isRemoved(){
		return remove;
	}
	/**
	 * Set the node for removal and makes it invisible, will be deleted asap
	 * */
	public ONode2D remove(){
		visible = false;
		remove = true;
		return this;
	}
	/**
	 * Set whether this node should be visible or not
	 * */
	public void setVisible(boolean set){
		visible = set;
	}
	/**
	 * Check if the node is visible
	 * */
	public boolean isVisible(){
		return visible;
	}
	/**
	 * Get the ID of the node
	 * */
	public int getId(){
		return Id;
	}
	/**
	 * Set the ID of the node
	 * */
	public ONode2D setId(int set){
		Id = set;
		return this;
	}
	/**
	 * Get the position of the node
	 * */
	public vector3df getPosition(){
		if(parent!=null){
			rel_position.x = parent.getPosition().x+position.x;
			rel_position.y = parent.getPosition().y+position.y;
			rel_position.z = parent.getPosition().z+position.z;
			return rel_position;
		}else
		return (vector3df)position;
	}
	/**
	 * Get the actual position of the node, disregarding the parent
	 * */
	public vector3df getAbsolutePosition(){
		return (vector3df)position;
	}
	/**
	 * get the bounds of the node
	 * */
	public boundbox2df getBounds(){
		return bounds;
	}
	/**
	 * Get the bounds of this node translated to the nodes position
	 * */
	public boundbox2df getBoundsTranslated(){
		rbounds.x = bounds.x;//+position.x;
		rbounds.y = bounds.y;//+position.y;
		rbounds.w = bounds.getW();
		rbounds.h = bounds.getH();
		return rbounds;
	}
	/**
	 * Get the bounds of this node scaled appropriately
	 * */
	public boundbox2df getBoundsScaled(){
		rbounds.x = bounds.x*scale.x;//+position.x;
		rbounds.y = bounds.y*scale.y;//+position.y;
		rbounds.w = bounds.w*scale.x;
		rbounds.h = bounds.h*scale.y;
		return rbounds;
	}
	/**
	 * Get the bounds of this node translated to the nodes position with scaled dimensions
	 * */
	public boundbox2df getBoundsTranslatedz(){
		rbounds.x = bounds.x+getPosition().x;
		rbounds.y = bounds.y+getPosition().y;
		rbounds.w = bounds.w+getPosition().x;
		rbounds.h = bounds.h+getPosition().y;
		return rbounds;
	}
	/**
	 * Get the bounds of this node translated to the nodes position
	 * */
	@Deprecated
	public boundbox2df getBoundsTranslatedx(){
		rbounds.x = position.x;
		rbounds.y = position.y;
		rbounds.w = bounds.getW();
		rbounds.h = bounds.getH();
		return rbounds;
	}
	/**
	 * get the rotation of the node
	 * */
	public vector3df getRotation(){
		if(parent!=null){
			rel_rotation.x = parent.getRotation().x+rotation.x;
			rel_rotation.y = parent.getRotation().y+rotation.y;
			rel_rotation.z = parent.getRotation().z+rotation.z;
			return rel_rotation;
		}else
		return (vector3df)rotation;
	}
	/**
	 * get the scale of the node
	 * */
	public vector3df getScale(){
		if(parent!=null){
			rel_scale.x = parent.getScale().x+scale.x;
			rel_scale.y = parent.getScale().y+scale.y;
			rel_scale.z = parent.getScale().z+scale.z;
			return rel_scale;
		}else
		return (vector3df)scale;
	}

	/**
	 * Set the bounds of the node
	 * */
	public ONode2D setBounds(boundbox2df set){
		bounds = set;
		return this;
	}
	/**
	 * Set the position of the node
	 * */
	public ONode2D setPosition(vector3df set){
		//bounds.x = set.x;
		//bounds.y = set.y;
		position = set;
		return this;
	}
	/**
	 * Set the position of the node
	 * */
	public ONode2D setPosition(int X, int Y){
		//bounds.x = X;
		//bounds.y = Y;
		position.x = X;
		position.y = Y;
		return this;
	}
	/**
	 * Set the position of the node
	 * */
	public ONode2D setPosition(vector2df set){
		///bounds.x = set.x;
		//bounds.y = set.y;
		position.x = set.x;
		position.y = set.y;
		position.z = 0;
		return this;
	}
	/**
	 * Set the position of the node
	 * */
	public ONode2D setPosition(float X, float Y){
		//bounds.x = X;
		//bounds.y = Y;
		position.x = X;
		position.y = Y;
		return this;
	}
	/**
	 * Set the rotation of the node
	 * */
	public ONode2D setRotation(vector3df set){
		rotation = set;
		return this;
	}
	/**
	 * Set the rotation of the node
	 * */
	public ONode2D setRotation(float X, float Y, float Z){
		rotation.x = X;
		rotation.y = Y;
		rotation.z = Z;
		return this;
	}
	/**
	 * Set the scale of the node
	 * */
	public ONode2D setScale(vector3df set){
		scale = set;
		return this;
	}	
	/**
	 * Set the scale of the node
	 * */
	public ONode2D setScale(float X, float Y){
		scale.x = X;
		scale.y = Y;
		return this;
	}	
	/**
	 * Render this node
	 * */
	public void render(GL10 gl){
		//--
	}
}
