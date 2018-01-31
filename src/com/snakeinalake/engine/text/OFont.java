package com.snakeinalake.engine.text;

import java.util.HashMap;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.rect2df;

public class OFont {
	private HashMap<Character, OFCharacter> characters;
	public boolean hasAlpha;
	public String name,texture;
	public OTexture tex;
	private float bmwidth,bmheight;
	/*
	 * The following variables are records of the biggest and smallest characters in the tilesheet
	 * **/
	private float chMaxHeight,chMinHeight;
	private float chMaxWidth,chMinWidth;
	
	public OFont(float BMW, float BMH, float MaxHeight, float MinHeight, float MaxWidth, float MinWidth){
		characters = new HashMap<Character, OFCharacter>();
		tex = new OTexture();
		bmwidth=BMW;
		bmheight=BMH;
		chMaxHeight = MaxHeight;
		chMinHeight = MinHeight;
		chMaxWidth = MaxWidth;
		chMinWidth = MinWidth;		
	}
	/**
	 * Get the map of characters in this font. Should only be used for loading
	 * @return Actual HashMap of characters, not a copy
	 * */
	public HashMap<Character, OFCharacter> getCharacters(){
		return characters;
	}
	/**
	 * Loads the fonts texture(s) into memory
	 * */
	public void loadTexture(OrangeDevice device){
		tex.setId(device.getTexLoader().loadTex(texture));
	}
	/**
	 * Get the biggest recorded character height in the sheet
	 * */
	public float getMaxHeight(){
		return chMaxHeight;
	}
	/**
	 * Get the smallest recorded character height in the sheet
	 * */
	public float getMinHeight(){
		return chMinHeight;
	} 
	/**
	 * Get the bigger recorded character width in the sheet
	 * */
	public float getMaxWidth(){
		return chMaxWidth;
	}
	/**
	 * Get the smallest recorded character width in the sheet
	 * */
	public float getMinWidth(){
		return chMinWidth;
	} 
	/*********************************************************/
	/**
	 * Set the biggest recorded character height in the sheet
	 * */
	public void setMaxHeight(float set){
		chMaxHeight = set;
	}
	/**
	 * Set the smallest recorded character height in the sheet
	 * */
	public void setMinHeight(float set){
		chMinHeight = set;
	} 
	/**
	 * Set the bigger recorded character width in the sheet
	 * */
	public void setMaxWidth(float set){
		chMaxWidth = set;
	}
	/**
	 * Set the smallest recorded character width in the sheet
	 * */
	public void setMinWidth(float set){
		chMinWidth = set;
	} 
	
	
	
	public float getBWidth(){
		return bmwidth;

	}
	public float getBHeight(){
		return bmheight;
	}
	public void setBWidth(float set){
		bmwidth = set;
	}
	public void setBHeight(float set){
		bmheight = set;
	}
	public class OFCharacter{
		public char c;
		public rect2df mapping;
		
		public OFCharacter(char C, rect2df Mapping){
			c = C;
			mapping = Mapping;
		}
	}
}
