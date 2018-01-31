package com.snakeinalake.engine.ortho;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.TextureLoader;
import com.snakeinalake.engine.core.boundbox2df;

public class ONode2DSprite extends ONode2D{
	private static final long serialVersionUID = 8699540435713962949L;
	private Grid mGrid;
	private OTexture texture;
	
	public ONode2DSprite(int ID,int Width, int Height, Grid grid, int Texture, OrangeDevice Device){
		super(ID,Device);
		mGrid = grid;
		texture = new OTexture();
		texture.setId(Texture);
		bounds = new boundbox2df(0,0,Width,Height);
	}
	
	public synchronized void changeSprite(int tex){
		texture = new OTexture();
		texture.setId(tex);
	}	
	@Override
	public void setCenter(){
		float w=bounds.w;float h=bounds.h;
        mGrid.set(0, 0,  	-w/2, -h/2, 0.0f,	 0, 0, null);
        mGrid.set(1, 0, 	w/2, -h/2, 0.0f,		 0, 0, null);
        mGrid.set(0, 1, 	-w/2, h/2, 0.0f,		 0, 0, null);
        mGrid.set(1, 1, 	w/2, h/2, 0.0f,		 0, 0, null);
	}
	@Override
	public synchronized void render(GL10 gl){
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);		
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glPushMatrix();
		if(parent!=null){
			vector3df ppos = parent.getPosition();
			gl.glTranslatef(ppos.x+position.x, ppos.y+position.y, ppos.z+position.z);
			gl.glRotatef(parent.rotation.x+rotation.x, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(parent.rotation.y+rotation.y, 0f, 1.0f, 0.0f);
			gl.glRotatef(parent.rotation.z+rotation.z, 0f, 0.0f, 1.0f);
			gl.glScalef(parent.scale.x*scale.x, parent.scale.y*scale.y, parent.scale.z*scale.z);
			gl.glColor4f(col.r,col.g,col.b,col.a);
			//gl.glColor4f(parent.col.r*col.r,parent.col.g*col.g,parent.col.b*col.b,parent.col.a*col.a);
		}else{
			gl.glTranslatef(position.x, position.y, position.z);
			gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(rotation.y, 0f, 1.0f, 0.0f);
			gl.glRotatef(rotation.z, 0f, 0.0f, 1.0f);
			gl.glScalef(scale.x, scale.y, scale.z);
			gl.glColor4f(col.r,col.g,col.b,col.a);
		}
		mGrid.draw(gl, true, false);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}
}
