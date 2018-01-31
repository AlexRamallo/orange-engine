package com.snakeinalake.engine.scripting;

import java.util.Vector;

import com.snakeinalake.engine.scripting.OScriptEngine.InstructionInterface;

public class OSEInstruction{
	public String ID;
	public Vector<OSEType> param;
	public InstructionInterface inter;
	
	public OSEInstruction(){
		param = new Vector<OSEType>();
	}
}
