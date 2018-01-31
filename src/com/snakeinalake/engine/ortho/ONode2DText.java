package com.snakeinalake.engine.ortho;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.opengl.GLUtils;

import com.snakeinalake.engine.OOrthoEnvironment;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.vector3df;

public class ONode2DText extends ONode2D{
	private static final long serialVersionUID = 8779867516427965188L;
	private Grid mGrid;
	private String text; 
	private OTexture texture;
	private boolean retrieved, aa;
	public int bgColor,txtColor,fontSize;
	public Typeface tFace;
	public Align tAlign;
	
	public ONode2DText(int ID, String txt, OOrthoEnvironment Enviro){
		super(ID);
		texture = new OTexture();
		fontSize = 48;
		bgColor = Color.TRANSPARENT;
		txtColor = Color.BLACK;
		aa = true;
		tFace = Typeface.MONOSPACE;
		tAlign = Align.LEFT; 
		setText(txt);
	}
	/**
	 * Set the font size of the node
	 * */
	public void setFontSize(int set){
		fontSize = set;
		retrieved = false;
	}
	/**
	 * Enable or Disable anti-aliasing
	 * */
	public void setAA(boolean set){
		aa = set;
		retrieved = false;
	}
	/**
	 * set the background color of the node
	 * */
	public void setBgColor(int set){
		bgColor = set;
		retrieved = false;
	}
	/**
	 * set the text color of the node
	 * */
	public void setTxtColor(int set){
		txtColor = set;
		retrieved = false;
	}
	/**
	 * get the font size of the node
	 * */
	public int getFontSize(){
		return fontSize;
	}
	/**
	 * check whether or not Anti-Aliasing is enabled on this node
	 * */
	public boolean getAA(){
		return aa;
	}
	/**
	 * get the background color of the node
	 * */
	public int getBgColor(){
		return bgColor;
	}
	/**
	 * get the text color of the node
	 * */
	public int getTxtColor(){
		return txtColor;
	}
	public int roundPowerOf2(int x){
		--x;
		x |= x >> 1;
		x |= x >> 2;
		x |= x >> 4;
		x |= x >> 8;
		x |= x >> 16;
		return ++x;		
	}
	/**
	 * Sets the text of this node. Should not be called frequently as it may cause lag.\n\n
	 * Calling this requires a new texture to be created
	 * */
	public void setText(String set){
		text = set;
		retrieved = false;
	}
	/**
	 * Sets the text of this node. Should not be called frequently as it may cause lag.\n\n
	 * Calling this requires a new texture to be created
	 * */
	protected void updateTexure(GL10 gl){
		int l = text.length()+(((text.length()%2)!=0)?1:0);
		int Width = l * (fontSize);
		int Height = fontSize;
		int roundw = roundPowerOf2(Width);
		int roundh = roundPowerOf2(Height);
		
		Bitmap bmp = Bitmap.createBitmap(roundw, roundh, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		//bmp.eraseColor(bgColor);
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		paint.setColor(txtColor);
		paint.setAntiAlias(aa);
		paint.setTextAlign(tAlign);
		paint.setTypeface(tFace);
		c.scale(1.0f, -1.0f);
		c.drawText(text, 0, -fontSize/4, paint);
		int[] tex = new int[1];
		gl.glGenTextures(1, tex, 0);
		texture.setName(tex[0]);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		
        mGrid = new Grid(2, 2, false);
        mGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
        mGrid.set(1, 0, roundw, 0.0f, 0.0f, 1.0f, 1.0f, null);
        mGrid.set(0, 1, 0.0f, roundh, 0.0f, 0.0f, 0.0f, null);
        mGrid.set(1, 1, roundw, roundh, 0.0f, 1.0f, 0.0f, null);
        texture.setBitmap(bmp);
	}
	/**
	 * Get this text nodes text
	 * */
	public String getText(){
		return text;
	}
	@Override
	public void render(GL10 gl){
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		if(!retrieved){
			updateTexure(gl);
			retrieved = true;
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
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
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}
}
