package com.snakeinalake.engine.scripting;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

import com.snakeinalake.engine.*;
import com.snakeinalake.engine.core.vector3df;

public class OScriptEngine {
	private Vector<OSEScript> scripts;
	public OrangeDevice device;
	public OSceneManager smgr;
	public OOrthoEnvironment env;
	
	public HashMap<String, OSEType> globalConstants;
	//PRIMITIVE INSTRUCTIONS
	public HashMap<String, InstructionInterface> primitives;
	/**
	 * Interface for script instructions
	 * */
	public interface InstructionInterface{
		public boolean checkParams(Vector<OSEType> param);
		public void execute (OScriptEngine SE, Vector<OSEType> param);
	}
	public OScriptEngine(OrangeDevice Device){
		device = Device;
		smgr = device.getSceneManager();
		env = device.getOrthoEnvironment();
		globalConstants = new HashMap<String, OSEType>();
		scripts = new Vector<OSEScript>();
		initFunctionality();
	}
	
	public enum DataTypes{
		DT_INTEGER,
		DT_STRING,
		DT_VECTOR
	};
	
	/**
	 * Broadcasts the given event to the given script
	 * */
	public boolean callEvent(int id, String name){
		OSEScript s = scripts.get(id);
		if(!s.hasEvent(name)){
			return false;
		}
		s.dispatch(this, name);		
		return true;
	}		
	/**
	 * Loads a script
	 * */
	public int loadScript(InputStream is){
		OSEScript ret = new OSEScript();
		int retid = -1;
		InputStreamReader rd = new InputStreamReader(is);
		int data = 0;
		char c = 0;
		int job = 0;	/*0 = nothing, 1 = build instruction name, 2 = read params*/
		try{
			String buildWord = "";
			OSEBroadcastInterceptor obi = null;
			OSEInstruction osi = null;
			do{
				data = rd.read();
				c = (char)data;
				
				if(c == '('){
					obi = new OSEBroadcastInterceptor();
					do{
					data = rd.read();
					c = (char)data;
					if(c!=')'){
						buildWord+=c;
					}					
					}while(c!=')' && c!='\n' && data!=-1);
					obi.name = buildWord.trim();
					buildWord = "";
				}
				if(c =='{'){
					job = 1;		//found instruction name
				}
				
				if(job == 1){ //Building instruction name
					if(osi!=null){
						obi.instructions.add(osi);
					}
					osi = new OSEInstruction();
					if(c == '{'){data = rd.read();c = (char)data;}
					if(c == '}'){job = 4;}else{
						buildWord = "";
						do{
							if(c!=' '){
								buildWord+=c;
							}
							data = rd.read();
							c = (char)data;
						}while(c!=' ' && data!=-1);					
						osi.ID = buildWord.trim();
						job = 3;
					}
				}
				if(job == 3){
					buildWord = "";
					OSEType type = new OSEType();
					boolean finish = false, start = false,end = false;
					do{
						if(c=='-'){
							start = true;
							data = rd.read();
							c = (char)data;
						}
						if(c==';'){
							finish = true;
							start = false;
						} 
						if(start){
							if(c=='['){
								vector3df vbuf = new vector3df();
								buildWord = "";
								do{
									data = rd.read();
									c = (char)data;
									if(c!=',')
									buildWord+=c;
								}while(c!=',');
								vbuf.x = Float.parseFloat(buildWord.trim());
								buildWord = "";
								do{
									data = rd.read();
									c = (char)data;
									if(c!=',')
									buildWord+=c;
								}while(c!=',');
								vbuf.y = Float.parseFloat(buildWord.trim());
								buildWord = "";
								do{
									data = rd.read();
									c = (char)data;
									if(c!=']')
									buildWord+=c;
								}while(c!=']');
								vbuf.z = Float.parseFloat(buildWord.trim());
								type.Type = DataTypes.DT_VECTOR;
								type.data = vbuf;
								buildWord = "";
								type.Type = DataTypes.DT_VECTOR;
								start = false;
							}else if(c=='\"'){
								//STRING
							}else if(c!=']' && c!='\n'){
								if(c!=';'){
									buildWord+=c;
									type.Type = DataTypes.DT_INTEGER;
								}
							}
						}
						data = rd.read();
						c = (char)data;
					}while(!finish);
					if(c == '\n'){
						end = true;
					}
					if(end == true){
						job = 1;
					}
					
					if(type.Type == DataTypes.DT_INTEGER){
						type.data = Integer.parseInt(buildWord.trim());
						osi.param.add(type);
						buildWord="";
					}
					if(type.Type == DataTypes.DT_VECTOR){
						osi.param.add(type);
					}
					
					if(c == '}'){
						job = 4;
					}
				}
				if(job == 4){
					ret.bInt.add(obi);
					job = 0;
				}
			}while(data!=-1);
			retid = scripts.size();
			scripts.add(ret);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return retid;
	}
	
	public void initFunctionality(){
		primitives = new HashMap<String, InstructionInterface>();
		primitives.put("mv",new InstructionInterface(){
			@Override
			public boolean checkParams(Vector<OSEType> param) {
				if(param.size()!=2)
					return false;
				if(param.get(0).Type != DataTypes.DT_INTEGER)
					return false;
				if(param.get(1).Type != DataTypes.DT_VECTOR)
					return false;
				return true;
			}
			@Override
			public void execute(OScriptEngine SE, Vector<OSEType> param){
				if(checkParams(param)){
					SE.smgr.findNodeById((Integer)param.get(0).data).setPosition(
							(vector3df)param.get(1).data);
				}
			}				
		});
		primitives.put("rt",new InstructionInterface(){
					@Override
					public boolean checkParams(Vector<OSEType> param) {
						if(param.size()<=0)
							return false;
						if(param.get(0).Type != DataTypes.DT_INTEGER)
							return false;
						if(param.get(1).Type != DataTypes.DT_VECTOR)
							return false;
						return true;
					}
					@Override
					public void execute(OScriptEngine SE, Vector<OSEType> param) {
						SE.smgr.findNodeById((Integer)param.get(0).data).setRotation(
								(vector3df)param.get(1).data);
					}				
				});
		primitives.put("sc", new InstructionInterface(){
					@Override
					public boolean checkParams(Vector<OSEType> param) {
						if(param.size()<=0)
							return false;
						if(param.get(0).Type != DataTypes.DT_INTEGER)
							return false;
						if(param.get(1).Type != DataTypes.DT_VECTOR)
							return false;
						return true;
					}
					@Override
					public void execute(OScriptEngine SE, Vector<OSEType> param) {
						SE.smgr.findNodeById((Integer)param.get(0).data).setScale(
								(vector3df)param.get(1).data);
					}				
				});
	}


}
