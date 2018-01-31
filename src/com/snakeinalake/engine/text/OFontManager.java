package com.snakeinalake.engine.text;

import java.util.HashMap;
import java.util.Iterator;

import com.snakeinalake.engine.OrangeDevice;


public class OFontManager {
	/**
	 * Cache of all the fonts that have been loaded
	 * */
	private HashMap<String, OFont> fonts;
	private OFontXMLParser parser;
	private OrangeDevice device;
	
	public OFontManager(OrangeDevice Device){
		device = Device;
		fonts = new HashMap<String, OFont>();
		parser = OFontXMLParser.getInstance();
	}
	
	/**
	 * Load a font into memory
	 * */
	public void loadFont(String filename){
		OFont load = parser.loadFont(device.getContext(), filename);
		fonts.put(load.name, load);
	}
	/**
	 * Loads all loaded fonts' bitmaps into memory
	 * */
	@SuppressWarnings("rawtypes")
	public void loadAllFontBitmaps(){
		Iterator it = fonts.entrySet().iterator();
	    while (it.hasNext()) {
			HashMap.Entry pairs = (HashMap.Entry)it.next();
	        ((OFont)pairs.getValue()).loadTexture(device);
	    }
	}
	/**
	 * get a font by its name
	 * @return the instance of OFont
	 * */
	public OFont getFont(String name){
		return fonts.get(name);
	}
}
