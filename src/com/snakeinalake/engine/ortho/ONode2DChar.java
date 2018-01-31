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
import com.snakeinalake.engine.text.OFont;
import com.snakeinalake.engine.text.OFont.OFCharacter;
import com.snakeinalake.engine.utilities.TextureLoader;

/**
 * Class that renders a single text character. Shouldn't really be used manually, its main purpose is to
 * draw full text using the Font Manager.
 * */
public class ONode2DChar extends ONode2D{
	private static final long serialVersionUID = 6942960177966313611L;
	private OFont font;
	private OTexture texture;
	private Grid mGrid;
	public FloatBuffer uvBuff;
	public char Char;
	private float gWidth,gHeight;
	private boolean smooth;
	
	public ONode2DChar(int ID,float Width, float Height, Grid grid, char C, OFont Font, OrangeDevice Device){
		super(ID,Device);
		mGrid = grid;
		font = Font;
		texture = font.tex;
		gWidth = Width;
		gHeight = Height;
		bounds = new boundbox2df(0,0,Width,Height);
		Char = C;
		ByteBuffer ubb = ByteBuffer.allocateDirect(2 * 2 * 3 * 4); //4 byte float, 3 vert per face, 2 float per vert, 2 faces total
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
		setCharacter(C);
		smooth = false;
	}
	public void setFont(OFont set){
		font = set;
		texture = font.tex;
		setCharacter(Char);
	}
	public void setCharacter(char set){
		Char = set;
		OFCharacter ofc = font.getCharacters().get(Char);
		if(ofc==null)
			return;
		rect2df mapping = ofc.mapping;
		setScale(mapping.W/gWidth,mapping.H/gHeight);
		bounds.w = mapping.W;
		bounds.h = mapping.H;
		float tw = font.getBWidth();
		float th = font.getBHeight();
		
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
	public void setSmooth(boolean set){
		smooth = set;
	}
	public boolean isSmooth(){
		return smooth;
	}
	@Override
	public void render(GL10 gl) {
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
        
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuff);
		boolean s;
			try{
				s = ((ONode2DDynamicText)parent).isSmooth();
			}catch(ClassCastException e){
				s = smooth;
			}catch(NullPointerException e){
				s = false;
			}
			gl.glTexParameterf(GL10.GL_TEXTURE_2D,
	                GL10.GL_TEXTURE_MAG_FILTER,
	                (!s)?GL10.GL_NEAREST:GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D,
	                GL10.GL_TEXTURE_MIN_FILTER,
	                GL10.GL_LINEAR);
		
		gl.glPushMatrix();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		gl.glEnable(GL10.GL_TEXTURE_2D);
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
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glPopMatrix();
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}

}
