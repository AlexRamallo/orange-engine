package com.snakeinalake.engine.core;

import java.io.Serializable;

import com.snakeinalake.engine.utilities.TextureLoader;

import android.graphics.Bitmap;

public class OTexture implements Serializable{
	private static final long serialVersionUID = -8326174829213222914L;
	private Bitmap bmp;
	private int name,cacheId;
	
	public OTexture(){
		this(null, 0);
	}
	public OTexture(Bitmap Bmp){
		this(Bmp, 0);
	}
	public OTexture(int Name){
		this(null, Name);
	}
	public OTexture(Bitmap Bmp, int Name){
		bmp = Bmp;
		name = Name;
		cacheId = -1;
		TextureLoader.getInstance().references.add(this);
	}
	public void unretrieve(){
		name = 0;
	}
	public synchronized void setBitmap(Bitmap Bmp){
		bmp = Bmp;
	}
	public synchronized void setName(int Name){
		name = Name;		
	}
	public synchronized void setId(int ID){
		cacheId = ID;
	}
	
	public Bitmap getBitmap(){
		return bmp;
	}
	public int getName(){
		return name;
	}
	public int getId(){
		return cacheId;
	}
	
	public synchronized boolean isRetrieved(){
		if(name == 0){
			return false;
		}
		return true;
	}
}
