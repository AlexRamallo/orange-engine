package com.snakeinalake.engine.ortho;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.rect2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.OUtilClock;
import com.snakeinalake.engine.utilities.TextureLoader;


public class ONode2DScrolling extends ONode2D{
	private static final long serialVersionUID = 1401468457095025850L;
	private OTexture texture;
	public Grid mGrid;
	public FloatBuffer uvBuff;
	private float scrollX,scrollY;
	OUtilClock clock;
	rect2df mapping;
	float W,H;
	private boolean cthread;
	
	public ONode2DScrolling(int ID,float Width, float Height, Grid grid, int Texture, float ScrollX, float ScrollY, OrangeDevice Device){
		super(ID,Device);
		clock = Device.getClock();
		mGrid = grid;
		texture = new OTexture();
		texture.setId(Texture);
		bounds = new boundbox2df(0,0,Width,Height);
		ByteBuffer ubb = ByteBuffer.allocateDirect(2 * 2 * 3 * 4); //4 byte float, 3 vert per face, 2 float per vert, 2 faces total
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
		scrollX = ScrollX;
		scrollY = ScrollY;
		inheritance[0] = false;
		mapping = new rect2df(0.0f,0.0f,Width,Height);
		W = Width;
		H = Height;
		cthread = false;
		scroll();
	}
	/**
	 * Check whether or not this nodes scrolling is being controlled by the rendering thread or a custom one.
	 * TRUE means that you are manually invoking this classes 'scroll()' method in your own thread
	 * FALSE means that it is automatically scrolled()'ed in the rendering thread during every frame
	 * */
	public boolean iscthread(){
		return cthread;
	}
	/**
	 * Set whether or not to control the scrolling manually or automatically.
	 * TRUE means that you will manually invoke this classes 'scroll()' method to scroll the layer whenever necessary
	 * FALSE means that it will automatically be done in the rendering thread during every frame
	 * */
	public void setcthread(boolean set){
		cthread  = set;
	}
	public float getScrollX(){
		return scrollX;
	}
	public float getScrollY(){
		return scrollY;
	}
	/**
	 * Set the scrolling offsets for this node
	 * */
	public void setScroll(float X, float Y){
		scrollX = X;
		scrollY = Y;
	}
	public synchronized void scroll(){
		double elapsed = clock.getElapsed();
		mapping.X+=(scrollX*elapsed);
		mapping.Y+=(scrollY*elapsed);
		updateMapping();
	}
	public synchronized void scroll(float sX, float sY){
		mapping.X=sX;
		mapping.Y=sY;
		updateMapping();
	}
	private void updateMapping(){
		uvBuff.put(0,
				mapping.X/W
			);
			uvBuff.put(1,
				mapping.Y/H
			);
			uvBuff.put(2,
				(mapping.X+mapping.W)/W
			);
			uvBuff.put(3,
				mapping.Y/H
			);
			uvBuff.put(4,
				mapping.X/W
			);
			uvBuff.put(5,
				(mapping.Y+mapping.H)/H
			);
			uvBuff.put(6,
				(mapping.X+mapping.W)/W
			);
			uvBuff.put(7,
				(mapping.Y+mapping.H)/H
			);
			uvBuff.put(8,
				mapping.X/W
			);
			uvBuff.put(9,
				(mapping.Y+mapping.H)/H
			);
			uvBuff.put(10,
				(mapping.X+mapping.W)/W
			);
			uvBuff.put(11,
				mapping.Y/H
			);
	}
	@Override
	public synchronized void render(GL10 gl) {
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		gl.glAlphaFunc(GL10.GL_GREATER, 0f);
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST);	
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);	
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());        
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuff);
		
		gl.glPushMatrix();
		if(parent!=null){
			vector3df ppos = parent.getPosition();
			gl.glTranslatef(ppos.x+position.x, ppos.y+position.y, ppos.z+position.z);
			gl.glRotatef(parent.rotation.x+rotation.x, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(parent.rotation.y+rotation.y, 0f, 1.0f, 0.0f);
			gl.glRotatef(parent.rotation.z+rotation.z, 0f, 0.0f, 1.0f);
			gl.glScalef(parent.scale.x*scale.x, parent.scale.y*scale.y, parent.scale.z*scale.z);
			gl.glColor4f(parent.col.r*col.r,parent.col.g*col.g,parent.col.b*col.b,parent.col.a*col.a);
		}else{
			gl.glTranslatef(position.x, position.y, position.z);
			gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(rotation.y, 0f, 1.0f, 0.0f);
			gl.glRotatef(rotation.z, 0f, 0.0f, 1.0f);
			gl.glScalef(scale.x, scale.y, scale.z);
			gl.glColor4f(col.r,col.g,col.b,col.a);
		}
		mGrid.draw(gl, false, false);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}

}
