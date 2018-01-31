package com.snakeinalake.engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.snakeinalake.engine.parser.OMeshParserOBJ;
import android.app.Activity;
import android.opengl.GLSurfaceView.Renderer;

public class COglRenderer implements Renderer{
	protected OMeshParserOBJ parser;	//OBJ file parser
	protected OrangeDevice device;	//OrangeEngine OrangeDevice
	protected OSceneManager smgr;		//3D scene manager
	protected OOrthoEnvironment env;	//2D gui environment
	protected Activity act;			//Reference to the current activity as an Activty
	public COglRenderer(OrangeDevice Device, Activity Act){
		act = Act;
		device = Device;
		smgr = device.getSceneManager();
		env = device.getOrthoEnvironment();
	}
	public COglRenderer(OrangeDevice Device){
		device = Device;
		smgr = device.getSceneManager();
		env = device.getOrthoEnvironment();
	}
	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		device.setGl10(gl);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST); 
		gl.glEnable(GL10.GL_TEXTURE_2D);                              
		gl.glEnable(GL10.GL_BLEND);       
		gl.glDepthFunc(GL10.GL_LEQUAL);                               
		gl.glEnable(GL10.GL_CULL_FACE);                               
		gl.glCullFace(GL10.GL_BACK);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_NICEST);
	}
	public void onDrawFrame(GL10 gl){
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0f, 0f, 0f, 1.0f);
		smgr.renderAll(gl);
		env.renderAll(gl);
	}
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		smgr.notifySurfaceChange(gl, width, height);
		env.notifySurfaceChange(gl, width, height);
	}
	
}
