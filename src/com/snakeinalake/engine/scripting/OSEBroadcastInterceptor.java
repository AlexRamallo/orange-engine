package com.snakeinalake.engine.scripting;

import java.util.Vector;

public class OSEBroadcastInterceptor {
	public String name;
	public Vector<OSEInstruction> instructions;
	
	public OSEBroadcastInterceptor(){
		instructions = new Vector<OSEInstruction>();
	}
}
