package com.snakeinalake.engine.ortho;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.utilities.TextureLoader;
/**
 * A 9 Slice that splits an image into 9 slices, so when the dimensions are changed, it actually resizes the 9 slice appropriately
 * 
 * Requires to numbers for the slice, which are the first corner width and height.
 * It is assumed that all corner slices are identical to each other, and all border slices are identical to each other
 * The remaining space will make up the middle slice
 * 
 * */
public class ONode2D9Slice extends ONode2D{
	private static final long serialVersionUID = 8699540435713962949L;
	private Grid mGrid[];
	private OTexture texture;
	private int sliceWidth, sliceHeight;
	private float NodeWidth, NodeHeight; //"Artificial" width and height of 9-slice
	private FloatBuffer uvBuff;
	
	public ONode2D9Slice(int ID, int Width, int Height, int SliceWidth, int SliceHeight, Grid grid[], int Texture, OrangeDevice Device){
		super(ID,Device);
		mGrid = grid;		
		texture = new OTexture();
		texture.setId(Texture);
		bounds = new boundbox2df(0,0,Width,Height);
		sliceWidth = SliceWidth;
		sliceHeight = SliceHeight;
		NodeWidth = Width;
		NodeHeight = Height;
		ByteBuffer ubb = ByteBuffer.allocateDirect(2 * 2 * 3 * 4); //4 byte float, 3 vert per face, 2 float per vert, 2 faces total
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
	}
	public void setWidth(float set){
		NodeWidth = set;
	}
	public void setHeight(float set){
		NodeHeight = set;
	}
	public float getWidth(){
		return NodeWidth;
	}
	public float getHeight(){
		return NodeHeight;
	}	
	public synchronized void changeSprite(int tex){
		texture = new OTexture();
		texture.setId(tex);
	}	
	@Override
	public void setCenter(){
		float w=bounds.w;float h=bounds.h;
		for(int i=0; i<9; i++){
			mGrid[i].set(0, 0,  	-w/2, -h/2, 0.0f,	0, 0, null);
        	mGrid[i].set(1, 0, 	w/2, -h/2, 0.0f,		0, 0, null);
        	mGrid[i].set(0, 1, 	-w/2, h/2, 0.0f,		0, 0, null);
        	mGrid[i].set(1, 1, 	w/2, h/2, 0.0f,			0, 0, null);
		}
	}
	private void updateMapping(int i){		
		float 	TLx = 0, TLy = 0,				TRx = 1, TRy = 0,				
				BLx = 0, BLy = 1,				BRx = 1, BRy = 1;		
		
		float 	rsw = (sliceWidth) / bounds.w,
				rsh = (sliceHeight) / bounds.h,
				rcw = (bounds.w - sliceWidth) / bounds.w,
				rch = (bounds.h - sliceHeight) / bounds.h;
				
		switch(i){
			default:
				break;
			case 1:
				TLx = 0; TLy = 0;		TRx = rsw; TRy = 0;
				BLx = 0; BLy = rsh;		BRx = rsw; BRy = rsh;
				break;
			case 2:
				TLx = rsw; TLy = 0;		TRx = rcw; TRy = 0;
				BLx = rsw; BLy = rsh;	BRx = rcw; BRy = rsh;
				break;
			case 3:
				TLx = rcw; TLy = 0;		TRx = 1; TRy = 0;
				BLx = rcw; BLy = rsh;	BRx = 1; BRy = rsh;
				break;
				
			case 4:
				TLx = 0; TLy = rsh;		TRx = rsw; TRy = rsh;
				BLx = 0; BLy = rch;		BRx = rsw; BRy = rch;
				break;
			case 5:
				TLx = rsw; TLy = rsh;	TRx = rcw; TRy = rsh;
				BLx = rsw; BLy = rch;	BRx = rcw; BRy = rch;
				break;
			case 6:
				TLx = rcw; TLy = rsh;	TRx = 1; TRy = rsh;
				BLx = rcw; BLy = rch;	BRx = 1; BRy = rch;
				break;
				
			case 7:
				TLx = 0; TLy = rch;		TRx = rsw; TRy = rch;
				BLx = 0; BLy = 1;		BRx = rsw; BRy = 1;
				break;
			case 8:
				TLx = rsw; TLy = rch;	TRx = rcw; TRy = rch;
				BLx = rsw; BLy = 1;		BRx = rcw; BRy = 1;
				break;
			case 9:
				TLx = rcw; TLy = rch;	TRx = 1; TRy = rch;
				BLx = rcw; BLy = 1;		BRx = 1; BRy = 1;
				break;
		}
		
		uvBuff.put(0,TLx);  uvBuff.put(1,TLy);		
		uvBuff.put(2,TRx); 	uvBuff.put(3,TRy);		
		uvBuff.put(4,BLx);  uvBuff.put(5,BLy);
		//////////////////////////////////////
		uvBuff.put(6,BRx);  uvBuff.put(7,BRy);		
		uvBuff.put(8,BLx);  uvBuff.put(9,BLy);		
		uvBuff.put(10,TRx); uvBuff.put(11,TRy);

	}
	@Override
	public synchronized void render(GL10 gl){
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
		
		for(int i=1; i<=9; i++){
		gl.glPushMatrix();
			float offx = 0, offy = 0;
			
			int column = ((i-1)%3)+1;
			if(column != 1)
				offx = (column==2)?sliceWidth:NodeWidth - sliceWidth;
			
			int rowidx = i-3;
			//int row = 1;
			if(rowidx>0){
				if(rowidx>=1 && rowidx<=3){
					offy = sliceHeight;
				//	row = 2;
				}else{
					offy = NodeHeight - sliceHeight;
				//	row = 3;
				} 
			}
			
			float sx = 1, sy = 1;
			boolean allowSX = false, allowSY = false;
			if(i%2 == 0){
				if(i==2 || i==8)
					allowSX = true;
				else if(i==4 || i==6)
					allowSY = true;
			}else if(i==5){
				allowSX = allowSY = true;
			}
			
			if(allowSX){
				sx = (NodeWidth - (2*sliceWidth)) / bounds.w;
			}else{
				sx = sliceWidth / bounds.w;
			}
			if(allowSY){
				sy = (NodeHeight - (2*sliceHeight)) / bounds.h;
			}else{
				sy = sliceHeight / bounds.h;
			}
			
			updateMapping(i);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuff);
			gl.glTranslatef(position.x+offx, position.y+offy, position.z);
			gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(rotation.y, 0f, 1.0f, 0.0f);
			gl.glRotatef(rotation.z, 0f, 0.0f, 1.0f);
			gl.glScalef(scale.x*sx, scale.y*sy, scale.z);
			gl.glColor4f(col.r,col.g,col.b,col.a);
			
			mGrid[i-1].draw(gl, false, false);
			gl.glPopMatrix();
		}
		gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}
}
