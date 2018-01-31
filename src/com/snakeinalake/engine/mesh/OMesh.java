package com.snakeinalake.engine.mesh;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.snakeinalake.engine.core.OColor4f;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.boundbox3df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.TextureLoader;

/*
 * this class contains information for the renderer to be able to draw it as well as extra functions for optional
 * functionality which can later be implemented by creating new classes that inherit from it.
 * 
 * An example for this would be to add a function like getBone("head") if I want to add support for meshes with bones
 * in the future. Instead of adding that function to this class I can just create a child with the extra functionality
 * to keep everything clean and organized
 * */

public class OMesh implements Cloneable{
	public OMeshInfo info;
	public boolean wire;
	public boolean debug;
	protected OTexture texture;
	protected OColor4f color;
	protected int blend[] = {GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA};
	public void setBlendFunction(int src, int dest){
		blend[0] = src;
		blend[1] = dest;
	}
	public void setColor(float r, float g, float b, float a){
		color.r = r;
		color.g = g;
		color.b = b;
		color.a = a;
	}
	public void setColor(OColor4f set){
		color = set;
	}
	public OColor4f getColor(){
		return color;
	}
	public void setAlpha(float set){
		color.a = set;
	}
	public float getAlpha(){
		return color.a;
	}
	public OMesh(){
		info = new OMeshInfo();
		info.vertBuff = null;
		info.IndBuff = null;
		info.vertices = null;
		info.indexlist = null;
		wire = false;
		debug = true;
		color = new OColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	public boundbox3df getSize(){
		return info.size;
	}
	public OMesh(OMeshInfo MeshInfo){
		info = MeshInfo;
		color = new OColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	/**
	 * Should only be used for animation
	 * */
	public synchronized void setInfo(OMeshInfo set){
		info = set;
	}
	public synchronized void loadTexture(InputStream is){
		if(texture == null){
			texture = new OTexture();			
		}
		texture.setId(TextureLoader.getInstance().loadTex(is));
		texture.unretrieve();
	}
	public synchronized void loadTexture(Bitmap bmp){
		if(texture == null){
			texture = new OTexture();			
		}
		texture.setId(TextureLoader.getInstance().loadTex(bmp));
	}
	public void setTexture(int iD) {
		if(texture == null){
			texture = new OTexture();			
		}
		texture.setId(iD);
		texture.unretrieve();
	}
	public synchronized void loadTexture(Context context, String Filename){
		AssetManager am = context.getAssets();
		try {
			loadTexture(am.open(Filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void render(GL10 gl, vector3df pos, vector3df rot, vector3df scale){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
		
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		
		gl.glLoadIdentity();
		gl.glTranslatef(pos.x, pos.y, pos.z);
		gl.glRotatef(rot.x, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rot.y, 0f, 1.0f, 0.0f);
		gl.glRotatef(rot.z, 0f, 0.0f, 1.0f);
		gl.glScalef(scale.x, scale.y, scale.z);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);		
		if(texture!=null)
			if(!texture.isRetrieved()){
				texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
			}
		if(texture != null){
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,texture.getName());
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, info.uvBuff);
		}
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, info.vertBuff);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, info.normBuff);
		
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glDrawElements((wire)?GL10.GL_LINE_LOOP:GL10.GL_TRIANGLES, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		/*if(debug == true){
			gl.glScalef(scale.x+0.5f, scale.y+0.5f, scale.z+0.5f);
			gl.glColor4f(1.0f, 0, 0, 1.0f);
			gl.glDrawElements(GL10.GL_LINE_LOOP, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		}*/
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void render(GL10 gl, float px, float py, float pz, float rx, float ry, float rz, float sx, float sy, float sz){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		
		gl.glLoadIdentity();
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ry, 0f, 1.0f, 0.0f);
		gl.glRotatef(rz, 0f, 0.0f, 1.0f);
		gl.glScalef(sx, sy, sz);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);		
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		if(texture != null){
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,texture.getName());
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, info.uvBuff);
		}
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, info.vertBuff);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, info.normBuff);
		
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glDrawElements((wire)?GL10.GL_LINE_LOOP:GL10.GL_TRIANGLES, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		/*if(debug == true){
			gl.glScalef(sx+0.5f, sy+0.5f, sz+0.5f);
			gl.glColor4f(1.0f, 0, 0, 1.0f);
			gl.glDrawElements(GL10.GL_LINE_LOOP, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		}*/
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	@Override
	public OMesh clone(){
		// TODO Auto-generated method stub
		try{
			return (OMesh)super.clone();
		}catch(CloneNotSupportedException e){
			return null;
		}
	}

}














