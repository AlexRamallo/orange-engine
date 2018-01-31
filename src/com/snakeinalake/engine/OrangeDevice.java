package com.snakeinalake.engine;

import java.util.HashMap;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;

import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.input.updated.OInputManager;
import com.snakeinalake.engine.mesh.OMesh;
import com.snakeinalake.engine.mesh.OMeshInfo;
import com.snakeinalake.engine.parser.OMeshParser;
import com.snakeinalake.engine.parser.OMeshParserOBJ;
import com.snakeinalake.engine.text.OFontManager;
import com.snakeinalake.engine.utilities.OUtilClock;
import com.snakeinalake.engine.utilities.TextureLoader;

public class OrangeDevice{
	private HashMap<String, OMeshParser> parsers;
	private OSceneManager smgr;
	private OOrthoEnvironment env;
	private OFontManager fmgr;
	private TextureLoader textureLoader;
	public static final String EngineVersion = "OrangeEngine v1.75";
	private boolean showsplash;
	private Context context;
	private GL10 gl10;
	private COglRenderer renderer;
	private OInputManager input;
	/** Thread-safe clocks*/
	private OUtilClock clock,clock2;
	private Activity activity;
	private HashMap<Integer, OTexture> TextureLoaderCache;
	private HashMap<String, OMeshInfo> OBJParserCache;
	private HashMap<OMeshInfo, OMesh> SceneMeshCache;
	private boolean paused;
	private static OrangeDevice pInstance;
	/**
	 * Get singleton instance of the device
	 * */
	public static OrangeDevice getInstance(){
		if(pInstance == null){
			pInstance = new OrangeDevice(null,null,null);
		}
		return pInstance;
	}
	
	private OrangeDevice(Context cntxt, Activity mActivity, GL10 gl){
		activity = mActivity;
		parsers = new HashMap<String, OMeshParser>();
		OBJParserCache = new HashMap<String, OMeshInfo>();
		parsers.put("OBJ", new OMeshParserOBJ(this));
		gl10 = gl;
		paused = false;
		SceneMeshCache = new HashMap<OMeshInfo, OMesh>();
		clock = new OUtilClock(this);
		clock2 = new OUtilClock(this);
		smgr = new OSceneManager(this);
		env = new OOrthoEnvironment(this);
		fmgr = new OFontManager(this);
		textureLoader = TextureLoader.getInstance();
		TextureLoaderCache = new HashMap<Integer, OTexture>();
		textureLoader.setDevice(this);
		context = cntxt;
		showsplash = true;
	} 
	/**
	 * Set the reference to the Application Context
	 * */
	public void setContext(Context cntxt){
		context = cntxt;
	}
	/**
	 * Set the reference to the activity
	 * */
	public void setActivity(Activity mActivity){
		activity = mActivity;
	}
	/**
	 * Set pause to true or false
	 * */
	public void setPaused(boolean set){
		paused = set;
	}
	/**
	 * Check whether pause is true or false
	 * */
	public boolean getPaused(){
		return paused;
	}
	/**
	 * Get the OBJParser cache
	 * */
	public HashMap<String, OMeshInfo> getOBJParserCache(){
		return OBJParserCache;
	}
	/**
	 * Get the SceneMesh Cache
	 * */
	public HashMap<OMeshInfo, OMesh> getSceneMeshCache(){
		return SceneMeshCache;
	}
	/**
	 * Get the texture loader cahce
	 * */
	public HashMap<Integer, OTexture> getTextureLoaderCache(){
		return TextureLoaderCache;
	}
	/**
	 * Get the game clock
	 * */
	public OUtilClock getClock(){
		return clock;
	}
	/**
	 * Returns the second clock used in the rendering thread
	 * SHOULD NOT be used in anything other than the main rendering. Infact, don't use it at all
	 * */
	public OUtilClock getMainClock(){
		return clock2;
	}
	/**
	 * Get the reference to the activity
	 * */
	public Activity getActivity(){
		return activity;
	}
	/**
	 * Set the reference to the renderer
	 * */
	public void setRenderer(COglRenderer Render){
		renderer = Render;
	}
	/**
	 * Set the reference to the input handler
	 * */
	public void setInputHandler(OInputManager set){ 
		input = set;
	}
	/**
	 * Get the reference to the input handler
	 * */
	public OInputManager getInput(){
		return input;
	}
	/**
	 * Get the reference to the renderer
	 * */
	public COglRenderer getRenderer(){
		return renderer;
	}
	public OrangeDevice(Context cntxt){
		this(cntxt,null,null);
	}
	/**
	 * Get the font manager used as a high-level interface for loading fonts and drawing text
	 * */
	public OFontManager getFontManager(){
		return fmgr;
	}
	/**
	 * Return the instance of TextureLoader for loading textures and getting opengl texture names
	 * */
	public TextureLoader getTexLoader(){
		return textureLoader;
	}
	/**
	 * Sets the devices Gl10 instance. Should only be used in special cases
	 * @param gl instance to set
	 * */
	public void setGl10(GL10 gl){
		gl10 = gl;
	}	
	/**
	 * Get the scene manager used for creating, modifying, and loading nodes and meshes
	 * */
	public OSceneManager getSceneManager(){
		return smgr;
	}	
	/**
	 * Get the OrthoEnvironment for creating, managing, and rendering 2D graphics
	 * */
	public OOrthoEnvironment getOrthoEnvironment(){
		return env;
	}	
	/**
	 * Get an OMeshParser
	 * @param type type of file parser(e.g. "OBJ")
	 */
	public OMeshParser getParser(String type){
		return parsers.get(type);
	}
	/**
	 * Get an OMeshParserOBJ
	 */
	public OMeshParserOBJ getOBJParser(){
		return (OMeshParserOBJ)parsers.get("OBJ");
	}
	/**
	 * Get the devices current instance of GL10
	 * */
	public GL10 getGL10(){
		return gl10;
	}
	/**
	 * Get application context
	 * */
	public Context getContext(){
		return context;
	}
}
