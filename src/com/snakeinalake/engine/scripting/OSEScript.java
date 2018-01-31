package com.snakeinalake.engine.scripting;

import java.util.HashMap;
import java.util.Vector;

public class OSEScript{
	public Vector<OSEBroadcastInterceptor> bInt;
	public HashMap<String, OSEType> constants;
	
	public OSEScript(){
		bInt = new Vector<OSEBroadcastInterceptor>();
		constants = new HashMap<String, OSEType>();
	}
	
	/**
	 * Check if this script has an intercepter for the given broadcast, name
	 * */
	public boolean hasEvent(String name){
		for(int i=0; i<bInt.size(); i++){
			if(bInt.get(i).name.compareTo(name)==0){
				return true;
			}
		}
		return false;
	}
	
	public void dispatch(OScriptEngine SE, String name){
		Vector<OSEInstruction> instructions = null;
		for(int i=0; i<bInt.size(); i++){
			if(bInt.get(i).name.compareTo(name)==0){
				instructions = bInt.get(i).instructions;
			}
		}
		for(int i=0; i<instructions.size(); i++){
			SE.primitives.get(instructions.get(i).ID).execute(SE, instructions.get(i).param);
		}
	}
}
