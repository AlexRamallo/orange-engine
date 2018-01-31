package com.snakeinalake.engine.ortho;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector3df;

public class ONode2DRect extends ONode2D{
	private static final long serialVersionUID = 997314280242190015L;
	private Grid mGrid;
	
	public ONode2DRect(int ID,int Width, int Height, Grid grid, OrangeDevice Device){
		super(ID,Device);
		mGrid = grid;
		bounds = new boundbox2df(0,0,Width,Height);
		blend[0] = GL10.GL_SRC_ALPHA;
		blend[1] = GL10.GL_DST_COLOR;
	}
	@Override
	public synchronized void render(GL10 gl){
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		gl.glDisable(GL10.GL_TEXTURE_2D);
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
		mGrid.draw(gl, true, false);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_BLEND);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}
}
