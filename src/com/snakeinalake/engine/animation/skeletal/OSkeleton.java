package com.snakeinalake.engine.animation.skeletal;

import java.util.Vector;

public class OSkeleton {
	public Vector<OBone> bones;
	
	public OSkeleton(){
		bones = new Vector<OBone>();
	}
	
	public OBone getBoneByName(String name){
		for(OBone b: bones){
			if(b.name.equals(name))
				return b;
		}
		return null;
	}
}
