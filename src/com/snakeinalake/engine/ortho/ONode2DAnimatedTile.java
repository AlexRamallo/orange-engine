package com.snakeinalake.engine.ortho;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTexture;
import com.snakeinalake.engine.core.OTileset;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.rect2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.ortho.tiles.OAnimSet;
import com.snakeinalake.engine.ortho.tiles.OAnimatedTileFrame;
import com.snakeinalake.engine.ortho.tiles.OAnimationCallback;
import com.snakeinalake.engine.ortho.tiles.ONode2DAnimatedTile_helper_importinfo;
import com.snakeinalake.engine.utilities.OUtilClock;
import com.snakeinalake.engine.utilities.TextureLoader;

/**
 * Class that renders a a single tile from a tileset
 * (basically a modified ONode2DChar)
 * */
public class ONode2DAnimatedTile extends ONode2D{
	private static final long serialVersionUID = -6026231868350583955L;
	private OTileset tileset;
	private OTexture texture;
	private Grid mGrid;
	public FloatBuffer uvBuff;
	public rect2df mapping;
	public int tilex,tiley;	
	public OUtilClock clock;	
	private int curFrame,numFrames;
	private int start,end;
	public Vector<OAnimatedTileFrame> frames,hook;
	ONode2DAnimatedTile_helper_importinfo Info;
	public boolean repeat;
	private OAnimationCallback instructions;
	private boolean firstLoop, paused;
	private Vector<OAnimSet> animSets;
	OAnimSet asPlayQueue;
	private String currentanimset;
	
	public ONode2DAnimatedTile(int ID,float Width, float Height, ONode2DAnimatedTile_helper_importinfo info, Grid grid, OTileset Tileset, OrangeDevice Device){
		super(ID,Device);
		paused = false;
		mGrid = grid;
		tileset = Tileset;
		curFrame = 0;
		Info = info;
		frames = info.dframes;
		animSets = info.frames;
		
		//Set initial hook
		OAnimSet gi = animSets.get(0);
		hook = gi.frames;
		setFrames(0,hook.size()-1);
		curFrame = 0;
		repeat = gi.loop;
		currentanimset=gi.name;
		
		repeat = info.repeat;
		clock = Device.getMainClock();
		numFrames = hook.size();
		tilex = hook.get(curFrame).fX;
		tiley = hook.get(curFrame).fY; 
		texture = tileset.tex;
		bounds = new boundbox2df(0,0,Width,Height);
		ByteBuffer ubb = ByteBuffer.allocateDirect(2 * 2 * 3 * 4); //4 byte float, 3 vert per face, 2 float per vert, 2 faces total
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
		mapping = new rect2df();
		setTile(tilex, tiley);

		firstLoop = false;
		if(!clock.isSetTimer(hook.get(curFrame).timer)){
			clock.setTimer(hook.get(curFrame).timer, hook.get(curFrame).time, hook.get(curFrame).unit);
		}
	}
	public ONode2DAnimatedTile(int ID,float Width, float Height, Vector<OAnimatedTileFrame> Frames, Grid grid, OTileset Tileset, OrangeDevice Device){
		super(ID,Device);
		paused = false;
		mGrid = grid;
		tileset = Tileset;
		curFrame = 0;
		Info = null;
		frames = Frames;
		hook = frames;
		clock = Device.getClock();
		numFrames = hook.size();
		setFrames(0,Frames.size()-1);
		repeat = false;		
		tilex = hook.get(curFrame).fX;
		tiley = hook.get(curFrame).fY; 
		texture = tileset.tex;
		firstLoop = false;
		bounds = new boundbox2df(0,0,Width,Height);
		ByteBuffer ubb = ByteBuffer.allocateDirect(2 * 2 * 3 * 4); //4 byte float, 3 vert per face, 2 float per vert, 2 faces total
		ubb.order(ByteOrder.nativeOrder());
		uvBuff = ubb.asFloatBuffer();
		mapping = new rect2df();
		setTile(tilex, tiley);
		if(!clock.isSetTimer(hook.get(curFrame).timer)){
			clock.setTimer(hook.get(curFrame).timer, hook.get(curFrame).time, hook.get(curFrame).unit);
		}
		currentanimset="-1";
		
	}
	/**
	 * Set the instructions of this node
	 * @param set The instructions to set
	 * @see OAnimationCallback
	 * */
	public void setInstructions(OAnimationCallback set){
		instructions = set;
	}
	/**
	 * Check whether this node has been set instructions
	 * */
	public boolean hasInstructions(){
		return instructions!=null;
	}
	/**
	 * Remove the set instructions
	 * */
	public void removeInstructions(){
		instructions = null;
	}
	/**
	 * Moves the origin to the center of the bounding box
	 * */
	@Override
	public void setCenter(){
		float w=bounds.w;float h=bounds.h;
        mGrid.set(0, 0,  	-w/2, -h/2, 0.0f,	0, 0, null);
        mGrid.set(1, 0, 	w/2, -h/2, 0.0f,	0, 0, null);
        mGrid.set(0, 1, 	-w/2, h/2, 0.0f,	0, 0, null);
        mGrid.set(1, 1, 	w/2, h/2, 0.0f,		0, 0, null);
        bounds.x-=w/2;
        bounds.y-=h/2;
	}
	/**
	 * Set origin
	 * @param X the origins new x value (ranges are between bounds.x and bounds.w)
	 * @param Y the origins new Y value (ranges are between bounds.y and bounds.h)
	 * */
	public void setOrigin(float X, float Y){
		float w=bounds.w;float h=bounds.h;
        mGrid.set(0, 0,  	-X, -Y, 0.0f,	0, 0, null);
        mGrid.set(1, 0, 	w-X, -Y, 0.0f,	0, 0, null);
        mGrid.set(0, 1, 	-X, h-Y, 0.0f,	0, 0, null);
        mGrid.set(1, 1, 	w-X, h-Y, 0.0f,		0, 0, null);
        bounds.x-=w/2;
        bounds.y-=h/2;
	}
	/**
	 * Sets the current tile from X/Y coordinates inside of the tileset
	 * Not recommended for use in animated tiles
	 * */
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
	/**
	 * Empties the play queue. If it is currently playing, then it will freeze at the current frame.
	 * Also initializes the play queue if it wasn't done so before, so it is important to call this
	 * at least once before using the queue at all
	 * */
	public void clearPlayQueue(){
		if(asPlayQueue == null){
			asPlayQueue = new OAnimSet();
			asPlayQueue.name = "play_queue";
			if(asPlayQueue.frames == null)
				asPlayQueue.frames = new Vector<OAnimatedTileFrame>();
		}
		asPlayQueue.frames.clear();
		paused=true;
	}
	/**
	 * Add an animation to the play queue. returns false if the animation set with the given name does not exist, true otherwise
	 * */
	public boolean addToPlayQueue(String add){
		for(int i=0; i<animSets.size(); i++){
			if(animSets.get(i).name.equalsIgnoreCase(add)){
				asPlayQueue.frames.addAll(animSets.get(i).frames);
				return true;
			}
		}
		return false;
	}
	/**
	 * Plays the animation queue
	 * */
	public void playAnimationQueue(boolean loop){
		if(hook==asPlayQueue.frames)return;
		hook = asPlayQueue.frames;
		setFrames(0, hook.size()-1);
		curFrame = 0;
		paused=false;
		repeat = loop;
	}
	/**
	 * Plays an animation set. returns false if animation set with given name doesn't exist, true otherwise
	 * if name is null, it will hook to the default frame vector
	 * */
	public boolean playAnimSet(String name){
		if(currentanimset.equalsIgnoreCase(name))return true;
		if(name==null){
			hook=frames;
			return true;
		}
		for(int i=0; i<animSets.size(); i++){
			OAnimSet gi = animSets.get(i);
			if(gi.name.equalsIgnoreCase(name)){
				hook = gi.frames;
				setFrames(start,hook.size()-1);
				curFrame = 0;
				repeat = gi.loop;
				currentanimset=name;
				return true;
			}
		}
		hook = frames;
		return false;
	}
	/**
	 * Sets the Y coordinate for EVERY single frame in the tileset. cannot be reversed easily!
	 * */
	public void setAllfY(int set){
		for(OAnimatedTileFrame f: frames)
			f.fY = set;
	}
	/**
	 * Sets the X coordinate for EVERY single frame in the tileset. cannot be reversed easily!
	 * */
	public void setAllfX(int set){
		for(OAnimatedTileFrame f: frames)
			f.fX = set;
	}
	/**
	 * Set the range of frames to play (indices are local to currently hooked frame vector)
	 * */
	@Deprecated
	public void setFrames(int Start, int End){
		start = Start;
		end = End;
	}
	/**
	 * Sets the currently visible frame index (indices are local to currently hooked frame vector)
	 * */
	public void setCurFrame(int set){
		curFrame = set;
	}
	/**
	 * Gets the currently visible frame index (indices are local to currently hooked frame vector)
	 * */
	public int getCurFrame(){
		return curFrame;
	}
	/**
	 * Pauses/Un Pauses the currently playing animation
	 * */
	public void setPause(boolean set){
		paused = set;
	}
	/**
	 * Checks if the currently playing animation is paused
	 * */
	public boolean isPaused(){
		return paused;
	}
	@Override
	public void render(GL10 gl) {
		if(PreRenderPass!=null)PreRenderPass.onRender(gl);
		if(!firstLoop && instructions!=null){
			instructions.onAnimationStart();
			firstLoop = true;
		}
		//try{
			if(clock.getTimer(hook.get(curFrame).timer)<=0 && !paused){
				curFrame++;
				if(curFrame>end){
					if(repeat)
						curFrame = start;
					else
						curFrame = end;
					if(instructions!=null)
						instructions.onAnimationEnd();
				}
				if(instructions!=null)instructions.onFrameStep(curFrame);
				clock.setTimer(hook.get(curFrame).timer, hook.get(curFrame).time, hook.get(curFrame).unit);
			}
			setTile(hook.get(curFrame).fX,hook.get(curFrame).fY);
		/*}catch(ArrayIndexOutOfBoundsException e){
			Log.e("ONode2DAnimatedTile", "Caught Exception: ArrayIndexOutOfBoundsException\n[");
			Log.e("ONode2DAnimatedTile", "\thook:\t\t\t"+hook.toString());
			Log.e("ONode2DAnimatedTile", "\tcurFrame:\t\t\t"+curFrame);
			Log.e("ONode2DAnimatedTile", "\tend:\t\t\t"+end);
			Log.e("ONode2DAnimatedTile", "\thook.size():\t\t\t"+hook.size());
			Log.e("ONode2DAnimatedTile", "\trepeat:\t\t\t"+repeat);
			Log.e("ONode2DAnimatedTile", "]");
			curFrame = 0;
		}*/
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		gl.glAlphaFunc(GL10.GL_GREATER, 0.1f);
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
        
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuff);
        
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		if(PostRenderPass!=null)PostRenderPass.onRender(gl);
	}
}
