package com.snakeinalake.engine.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES11;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureLoader{
	private static TextureLoader pInstance = null;	
	private OrangeDevice device;
	private Vector<Bitmap> bmp;
	public Vector<OTexture> references;
	Bitmap defbmp;
	/** Flag determining whether to search external or internal directories * */
	private boolean external;
	/**Working Directory (starting location)*/
	public String workdir;
	/**
	 * Get instance of TextureLoader
	 * */
	public static TextureLoader getInstance(){
		if(pInstance == null){
			pInstance = new TextureLoader();
		}
		return pInstance;
	}
	/**Set whether to search external or internal directories * */
	public void setExternal(boolean set){
		external = set;
	}
	/**Set the working directory*/
	public void setWorkDir(String set){
		workdir = set;
		if(workdir.charAt(workdir.length()-1)!='/'){
			workdir+="/";
		}
	}
	/** Delete all Bitmaps, clear all references, and clear the cache * */
	public void clear(){
		for(Bitmap Bmp: bmp){
			Bmp.recycle();
		}
		references.clear();
		bmp.clear();
		device.getTextureLoaderCache().clear();
	}
	/**
	 * Force all texture to be recreated (not reloaded from the texture file)
	 * */
	public synchronized void reload(){
		for(OTexture tex: references){
			if(!GLES11.glIsTexture(tex.getName())){
				tex.unretrieve();
			}
		}
	}
	/**
	 * Retrieve the opengl texture name using the ID number returned by loadTex()
	 * */
	public synchronized OTexture retrieve(int id, GL10 gl){
		HashMap<Integer, OTexture> cache = device.getTextureLoaderCache();
		if(cache.containsKey(id)){
			return cache.get(id);
		}
		OTexture ret = null;
		int[] textures = new int[1];
		Bitmap Bmp;
		try{
			Bmp = bmp.get(id);
			gl.glEnable(GL10.GL_BLEND);
		    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glGenTextures(1, textures, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, Bmp, 0);
			ret = new OTexture(Bmp,textures[0]); 
		}catch(ArrayIndexOutOfBoundsException e){
			Log.e("TextureLoader", "Uh Oh! something went wrong in TextureLoader.retrieve");
			if(defbmp == null){
				defbmp = Bitmap.createBitmap(16,16,Bitmap.Config.RGB_565);
				Canvas c = new Canvas();
				c.setBitmap(defbmp);
				Paint p = new Paint();
				c.drawColor(Color.rgb(255, 0, 255));
				p.setColor(Color.BLACK);
				c.drawRect(0, 0, 8, 8, p);
				c.drawRect(8, 8, 16, 16, p);
			}
			gl.glEnable(GL10.GL_BLEND);
		    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glGenTextures(1, textures, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, defbmp, 0);
			ret = new OTexture(defbmp,textures[0]);
		}
		cache.put(id, ret);
		return ret;
	}
	/**
	 * Should not be called manually.
	 * */
	public void setDevice(OrangeDevice Device){
		getInstance().device = Device;
	}
	protected TextureLoader(){
		bmp = new Vector<Bitmap>();
		references = new Vector<OTexture>();
		external = false;
		workdir = "";
	}
	/**
	 * Load a texture from assets
	 * @param Filename filename of the asset
	 * @return OpenGL Texture name
	 * */
	public synchronized int loadTex(String Filename){
		String fn = (external)?workdir+Filename:Filename;
		Context context = device.getContext();
		AssetManager am = context.getAssets();
			try {
				InputStream fis = (external)?new FileInputStream(fn):am.open(fn);							
				int id = this.loadTex(fis);
				return id; 
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
	}
	/**
	 * Load a texture
	 * @param is InputStream to read from
	 * @return OpenGL Texture name
	 * */
	public synchronized int loadTex(InputStream is){
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeStream(is);
		}finally{
			try{
				is.close();
				is = null;
			}catch (IOException e){}
		}
		return this.loadTex(bitmap);
	}
	/**
	 * Load a Bitmap as a texture
	 * @param bitmap The Bitmap to use as a texture
	 * @return OpenGL texture name 
	 * */
	public synchronized int loadTex(Bitmap bitmap){
		bmp.add(bitmap);
		return bmp.size()-1;
	}
}
