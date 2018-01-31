package com.snakeinalake.engine.ortho;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.OTileset;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.rect2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.utilities.TextureLoader;

/**
 * Class that renders a a single tile from a tileset
 * (basically a modified ONode2DChar)
 * */
public class ONode2DTile extends ONode2D{
	private static final long serialVersionUID = -5240021186807303095L;
	private OTileset tileset;
	private OTexture texture;
	private Grid mGrid;
	public FloatBuffer uvBuff;
	public rect2df mapping;
	public float tilex,tiley;
	
	public ONode2DTile(int ID,float Width, float Height, int Tilex, int Tiley, Grid grid, OTileset Tileset, OrangeDevice Device){
		super(ID,Device);
		mGrid = grid;
		tileset = Tileset;
		tilex = Tilex;
		tiley = Tiley; 
		texture = tileset.tex;
		bounds = new boundbox2df(0,0,Width,Height);
		ByteBuffer ubb = ByteBuffer.allocateDirect(2 * 2 * 3 * 4); //4 byte float, 3 vert per face, 2 float per vert, 2 faces total
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
		mapping = new rect2df();
		setTile(Tilex, Tiley);
		inheritance[0] = false;
	}
	@Override
	public void setCenter(){
		float w=bounds.w;float h=bounds.h;
        mGrid.set(0, 0,  	-w/2, -h/2, 0.0f,	 0, 0, null);
        mGrid.set(1, 0, 	w/2, -h/2, 0.0f,		 0, 0, null);
        mGrid.set(0, 1, 	-w/2, h/2, 0.0f,		 0, 0, null);
        mGrid.set(1, 1, 	w/2, h/2, 0.0f,		 0, 0, null);
	}
	public void setTile(int setx, int sety){
		tilex = setx;
		tiley = sety;
		mapping.X = ((tilex*tileset.Tw)+((tilex+1)*tileset.TmL));
		mapping.Y = ((tiley*tileset.Th)+((tiley+1)*tileset.TmT));
		mapping.W = tileset.Tw;
		mapping.H = tileset.Th;
		float tw = tileset.Width;
		float th = tileset.Height;
		
		//top left
		uvBuff.put(0,
			mapping.X/tw
		);
		uvBuff.put(1,
			mapping.Y/th
		);
		
		//top right
		uvBuff.put(2,
			(mapping.X+mapping.W)/tw
		);
		uvBuff.put(3,
			mapping.Y/th
		);

		//bottom left
		uvBuff.put(4,
			mapping.X/tw
		);
		uvBuff.put(5,
			(mapping.Y+mapping.H)/th
		);
////////////////////////////////////
		
		//bottom right
		uvBuff.put(6,
			(mapping.X+mapping.W)/tw
		);
		uvBuff.put(7,
			(mapping.Y+mapping.H)/th
		);
		
		//bottom left
		uvBuff.put(8,
			mapping.X/tw
		);
		uvBuff.put(9,
			(mapping.Y+mapping.H)/th
		);
		
		
		//top right
		uvBuff.put(10,
			(mapping.X+mapping.W)/tw
		);
		uvBuff.put(11,
			mapping.Y/th
		);
		
		uvBuff.position(0);
	}
	
	@Override
	public void render(GL10 gl) {
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
        
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuff);
        
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
		
		gl.glPushMatrix();
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}

}
