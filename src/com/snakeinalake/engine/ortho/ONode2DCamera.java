package com.snakeinalake.engine.ortho;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.boundbox2df;

public class ONode2DCamera extends ONode2D{
	private static final long serialVersionUID = 1694756772066829405L;
	private int Zrange;
	public ONode2DCamera(int ID, float Width, float Height, OrangeDevice Device){
		super(ID,Device);
		bounds = new boundbox2df(0,0,Width,Height);
		Zrange = 100000;
	}
	public void setZRange(int set){
		Zrange = set;
	}
	@Override
	public boundbox2df getBoundsTranslatedz() {
		rbounds.x = bounds.x;
		rbounds.y = bounds.y;
		rbounds.w = bounds.w+bounds.x;
		rbounds.h = bounds.h+bounds.y;		
		return rbounds;
	}
	@Override
	public synchronized void render(GL10 gl){	
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotation.y, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(rotation.z, 0.0f, 0.0f, 1.0f);
		gl.glOrthof(bounds.x, bounds.w+bounds.x, bounds.h+bounds.y, bounds.y, -Zrange, Zrange);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}
}
