package com.snakeinalake.engine;


import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.util.Log;

import com.snakeinalake.engine.animation.skeletal.OEAInfo;
import com.snakeinalake.engine.core.OLight;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.mesh.OAnimatedMesh;
import com.snakeinalake.engine.mesh.OMesh;
import com.snakeinalake.engine.mesh.OMeshInfo;
import com.snakeinalake.engine.nodes.OAnimatedMeshSceneNode;
import com.snakeinalake.engine.nodes.OBillboardSceneNode;
import com.snakeinalake.engine.nodes.OCameraSceneNode;
import com.snakeinalake.engine.nodes.OMeshSceneNode;
import com.snakeinalake.engine.nodes.OSceneNode;
import com.snakeinalake.engine.nodes.frames.OAnimatedFrameMeshParser;
import com.snakeinalake.engine.parser.OEAParser;
import com.snakeinalake.engine.parser.OMeshParserOBJ;
import com.snakeinalake.engine.tools.wsds.OWSDSFile;


public class OSceneManager{
	private Vector<OSceneNode> nodes,quarantine;
	private Vector<OLight> lights;
	private Vector<OCameraSceneNode> cameras;
	private OCameraSceneNode activeCamera;
	private int counter;
	private static final int salt = 100000000;
	private OrangeDevice device;
	private float sWidth,sHeight;
	private boolean isquarantine;
	private boolean OMeshCaching;
	private float DOF = 45.0f;
	/**
	 * Empties the scene by clearing everything
	 * */
	public synchronized void empty(){
		counter = 0;
		cameras.clear();
		nodes.clear();
		quarantine.clear();
		lights.clear();
		activeCamera = null;
	}
	public OSceneManager(OrangeDevice Device){
		counter = 0;
		device = Device;
		nodes = new Vector<OSceneNode>();
		quarantine = new Vector<OSceneNode>();
		cameras = new Vector<OCameraSceneNode>();
		lights = new Vector<OLight>();
		activeCamera = null;
		isquarantine = false;
		//clock = new OUtilClock(device);
		OMeshCaching = false;
	}
	public OLight addLight(vector3df pos, vector3df dir, float[] specular, float[] ambient, float[] diffuse){
		if(lights.size()>7)
			return null;
		OLight add = new OLight(GL10.GL_LIGHT0+lights.size(), pos, dir, specular, ambient, diffuse);
		lights.add(add);
		return add;
	}
	public void setQuarantine(boolean set){
		isquarantine = set;
	}
	public synchronized void clearQuarantine(){
		if(isquarantine)
			isquarantine = false;
		for(int i=0; i<quarantine.size(); i++){
			nodes.add(quarantine.get(i));
		}
	}
	/**
	 * Enable or Disable mesh caching\n
	 * Mesh Caching is a great way to speed up load times. It works by reusing meshes that were already
	 * loaded in the past to prevent from having to load the same mesh multiple times.\n
	 * It is currently in an experimental stage, so if you are having some trouble you might want to
	 * disable it when debugging just in case.
	 * @param parser String indicating the type of file parser (e.g. "OBJ")
	 * @param set Boolean value to set mesh caching to
	 * \n\n
	 * Note: Mesh caching is enabled by default
	 * */
	public void setMeshCaching(String parser, boolean set){
		((OMeshParserOBJ)device.getParser(parser)).setCaching(set);
	}
	/**
	 * Get the vertical dimensions of the viewport
	 * */
	public float getHeight(){
		return sHeight;
	}
	/**
	 * Get the horizontal dimensions of the viewport
	 * */
	public float getWidth(){
		return sWidth;
	}
	private synchronized void enterNode(OSceneNode node){
		if(!isquarantine){
			nodes.add(node);
		}else{
			quarantine.add(node);
		}
	}
	private OMesh getMesh(OMeshInfo info){
		HashMap<OMeshInfo, OMesh> meshCache = device.getSceneMeshCache();
		if(!OMeshCaching){
			return new OMesh(info);
		}else
		if(meshCache.containsKey(info)){
			return meshCache.get(info);
		}
		OMesh m = new OMesh(info);
		meshCache.put(info, m);
		return m;
	}
	private OAnimatedMesh getAnimatedMesh(OMeshInfo info, OEAInfo OEA){
		HashMap<OMeshInfo, OMesh> meshCache = device.getSceneMeshCache();
		if(!OMeshCaching){
			return new OAnimatedMesh(info, OEA);
		}else
		if(meshCache.containsKey(info)){
			return (OAnimatedMesh) meshCache.get(info);
		}
		OAnimatedMesh m = new OAnimatedMesh(info, OEA);
		meshCache.put(info, m);
		return m;
	}
	/**
	 * Add a custom-created node to the scene
	 * 
	 * @param node The initialized and ready-to-render node you want to add
	 * */
	public void addCustomNode(OSceneNode node){
		enterNode(node);
	}
	/** Creates a new instance of OMeshSceneNode
	 * @param Mesh String filename
	 * */
	public OMeshSceneNode createMeshSceneNode(String Mesh){
		Context ctxt = device.getContext();
		OMeshParserOBJ parser = (OMeshParserOBJ)device.getParser("OBJ");
		OMeshInfo info = parser.loadMesh(ctxt, Mesh);
		OMesh mesh = getMesh(info);
		OMeshSceneNode ret = new OMeshSceneNode(salt+counter,mesh);
		counter++;
		ret.setDeviceReference(device);
		enterNode(ret);
		return ret;
	}
	/** Creates a new instance of OAnimatedMeshSceneNode
	 * @param File The XML File which defines all frames and framesets of the node
	 * */
	public OAnimatedMeshSceneNode createAnimatedMeshSceneNode(String File){
		Context ctxt = device.getContext();		
		OAnimatedMeshSceneNode ret = OAnimatedFrameMeshParser.getInstance(device).loadAnimation(ctxt, File);
		ret.setId(salt+counter);
		counter++;
		ret.setDeviceReference(device);
		enterNode(ret);
		return ret;
	}
	/** Creates a new instance of OMeshSceneNode WITHOUT a mesh
	 * @param Mesh String filename
	 * */
	public OMeshSceneNode createEmptyMeshSceneNode(){
		OMeshSceneNode ret = new OMeshSceneNode(salt+counter,null);
		counter++;
		ret.setDeviceReference(device);
		enterNode(ret);
		return ret;
	}
	public OBillboardSceneNode createBillboardSceneNode(String texture){
		OMeshParserOBJ parser = (OMeshParserOBJ)device.getParser("OBJ");
		OMeshInfo info = parser.loadMesh(device.getContext(), "primitives/plane.obj");
		OMesh mesh = getMesh(info);
		mesh.loadTexture(device.getContext(),texture);
        OBillboardSceneNode ret = new OBillboardSceneNode(salt+counter, mesh);
        counter++;
        ret.setDeviceReference(device);
        enterNode(ret);
		return ret;
	}
	/** Creates a new instance of OMeshSceneNode
	 * @param Mesh Integer resource id
	 * */
	public OMeshSceneNode createMeshSceneNode(int Mesh){
		Context ctxt = device.getContext();
		OMeshParserOBJ parser = (OMeshParserOBJ)device.getParser("OBJ");
		OMeshInfo info = parser.loadMesh(ctxt, Mesh);
		OMesh mesh = getMesh(info);
		OMeshSceneNode ret = new OMeshSceneNode(salt+counter,mesh);
		counter++;
		ret.setDeviceReference(device);
		enterNode(ret);
		return ret;
	}
	/**
	 * Load a scene from a WSDS file
	 * @param instance of OWSDSFile data structure
	 * */
	public void loadWSDS(OWSDSFile scene){
		for(int i=0; i<scene.Blocks.size(); i++){
			OMeshSceneNode add = createMeshSceneNode(scene.Blocks.get(i).MeshFile);
			add.getMesh().loadTexture(device.getContext(), scene.Blocks.get(i).TexFile);
			add.setPosition(scene.Blocks.get(i).Position);
			add.setRotation(scene.Blocks.get(i).Rotation);
			add.setScale(scene.Blocks.get(i).Scale);
			add.setId(scene.Blocks.get(i).ID);
			add.setDeviceReference(device);
			Log.d("ORANGE", "Loaded WSDS Block: "+add.getId());
		}
	}
	/**
	 * Search through list of nodes to look for the one with the requested ID
	 * */
	public synchronized OSceneNode findNodeById(int id){
		for(int i=0; i<nodes.size(); i++){
			if(nodes.get(i).getId() == id)
				return nodes.get(i);
		}
		return null;
	}
	public OCameraSceneNode createCameraSceneNode(float px, float py, float pz, float tx, float ty, float tz){
		OCameraSceneNode ret = new OCameraSceneNode(salt+counter);
		ret.setPosition(px,py,pz);
		ret.setTarget(tx,ty,tz);
		counter++;
		if(activeCamera == null)
			activeCamera = ret;
		cameras.add(ret);
		return ret;		
	}
	/**
	 * Notify the scene manager of a surface change. Should be called on glSurfaceViews overridden onSurfaceChange method
	 * @param width the width given as a parameter to glSurfaceView.onSurfaceChange
	 * @param height the height given as a parameter to glSurfaceView.onSurfaceChange
	 * */
	public void notifySurfaceChange(GL10 gl, int width, int height){
		sWidth = width;
		sHeight = height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f,sWidth / sHeight,0.1f, 100.0f);
		gl.glPushMatrix();		
	}
	/**
	 * Set the active camera used for rendering
	 * @param set The camera to use
	 * */
	public void setActiveCamera(OCameraSceneNode set){
		activeCamera = set;
	}
	/**
	 * Get the current active camera
	 * */
	public OCameraSceneNode getActiveCamera(){
		return activeCamera;
	}
	/**
	 * Set the active camera used for rendering
	 * @param The ID of the camera to use
	 * */
	public void setActiveCamera(int set){
		for(int i=0; i<cameras.size(); i++){
			if(cameras.get(i).getId() == set){
				activeCamera = cameras.get(i);
				break;
			}
		}
	}
	/**
	 * Render all the scene nodes that were created by the scene manager
	 * */
	public synchronized void renderAll(GL10 Gl){
		//clock.count();
		Gl.glEnable(GL10.GL_DEPTH_TEST);
		Gl.glMatrixMode(GL10.GL_PROJECTION);
		Gl.glEnable(GL10.GL_CULL_FACE);
		Gl.glFrontFace(GL10.GL_CCW);
		Gl.glCullFace(GL10.GL_BACK);
		Gl.glLoadIdentity();
		if(activeCamera!=null)
			GLU.gluPerspective(Gl, activeCamera.getDepthOfField(),sWidth / sHeight,0.1f, 200.0f);
		else
			GLU.gluPerspective(Gl, 70, sWidth / sHeight,0.1f, 200.0f);
		Gl.glPushMatrix();
		if(activeCamera!=null){
			activeCamera.render(Gl);
			activeCamera.animate();
		}
		for(int i=0; i<nodes.size(); i++){
			OSceneNode node = nodes.get(i);
				node.animate();
			if(node.isVisible()){
				Gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				node.render(Gl);
			}
		}
	}
	/**
	 * Render a single scene node
	 * */
	public synchronized void renderSingle(GL10 Gl, OSceneNode node){
		//clock.count();
		Gl.glEnable(GL10.GL_DEPTH_TEST);
		Gl.glMatrixMode(GL10.GL_PROJECTION);
		Gl.glEnable(GL10.GL_CULL_FACE);
		Gl.glFrontFace(GL10.GL_CCW);
		Gl.glCullFace(GL10.GL_BACK);
		Gl.glLoadIdentity();
		GLU.gluPerspective(Gl, activeCamera.getDepthOfField(),sWidth / sHeight,0.1f, 200.0f);
		Gl.glPushMatrix();
		if(activeCamera!=null){
			activeCamera.render(Gl);
			activeCamera.animate();
		}
		
		node.animate();
		if(node.isVisible()){
			Gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			node.render(Gl);
		}
		
	}
}







