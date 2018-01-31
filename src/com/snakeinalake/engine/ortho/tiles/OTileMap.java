package com.snakeinalake.engine.ortho.tiles;

import java.util.HashMap;
import java.util.Vector;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTileset;
import com.snakeinalake.engine.ortho.ONode2D;

public class OTileMap extends ONode2D{
	private static final long serialVersionUID = 7146416629796581498L;
	public Vector<ONode2D> tiles;
	public HashMap<String, OTileset> tilesets;
	public Information info;
	public class Information{
		public String name;
		public String date;
		public String editor,edver;
		public String license;
		public String author,org;
		public Vector<String> contact;
		
		public Information(){
			contact = new Vector<String>();
		}
	}
	
	public OTileMap(OrangeDevice device){
		super(-1,device);
		info = new Information();
		tiles = new Vector<ONode2D>();
		tilesets = new HashMap<String, OTileset>();
	}
	
	/**
	 * Get the Vector array of tiles in this map
	 * */
	public Vector<ONode2D> getTiles(){
		return tiles;
	}
}