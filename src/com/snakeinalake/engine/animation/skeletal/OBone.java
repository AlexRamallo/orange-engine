package com.snakeinalake.engine.animation.skeletal;

import java.util.Vector;

import com.snakeinalake.engine.core.vector3df;

/**
 * Each bone only has knowledge of its father and first-generation children
 * */
public class OBone{
	public String name;
	public OBone father;
	public Vector<OBone> children;
	public Vector<Integer> indices;
	
	public vector3df Origin,Tip;
	
	public OBone(){
		indices = new Vector<Integer>();
	}
	public float getRotation(int axis){
		float ret = 0;
		
		switch(axis){
		case 0:
			ret = (float)Math.asin(
			(Tip.y-Origin.y)
			/		
			Math.sqrt(
					((Tip.x-Origin.x)*(Tip.x-Origin.x))+((Tip.y-Origin.y)*(Tip.y-Origin.y))		
			));
			break;
		case 1:
			ret = (float)Math.asin(
			(Tip.z-Origin.z)
			/		
			Math.sqrt(
					((Tip.y-Origin.y)*(Tip.y-Origin.y))+((Tip.z-Origin.z)*(Tip.z-Origin.z))		
			));
			break;
		case 2:
			ret = (float)Math.asin(
			(Tip.x-Origin.x)
			/		
			Math.sqrt(
					((Tip.z-Origin.z)*(Tip.z-Origin.z))+((Tip.x-Origin.x)*(Tip.x-Origin.x))		
			));
			break;
		}		
		return ret;
	}
}
