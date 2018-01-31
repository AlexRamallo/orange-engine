package com.snakeinalake.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.snakeinalake.engine.core.OTileset;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.Grid;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.ortho.ONode2D.InheritanceList;
import com.snakeinalake.engine.ortho.ONode2D9Slice;
import com.snakeinalake.engine.ortho.ONode2DAnimatedTile;
import com.snakeinalake.engine.ortho.ONode2DCamera;
import com.snakeinalake.engine.ortho.ONode2DChar;
import com.snakeinalake.engine.ortho.ONode2DDynamicText;
import com.snakeinalake.engine.ortho.ONode2DRect;
import com.snakeinalake.engine.ortho.ONode2DScrolling;
import com.snakeinalake.engine.ortho.ONode2DSprite;
import com.snakeinalake.engine.ortho.ONode2DText;
import com.snakeinalake.engine.ortho.ONode2DTile;
import com.snakeinalake.engine.ortho.ONodeCompound;
import com.snakeinalake.engine.ortho.tiles.OAnimatedTileFrame;
import com.snakeinalake.engine.ortho.tiles.ONode2DAnimatedTile_helper_importinfo;
import com.snakeinalake.engine.text.OFont;
import com.snakeinalake.engine.text.OFont.OFCharacter;
import com.snakeinalake.engine.utilities.OUtilClock;

public class OOrthoEnvironment {
	private OrangeDevice device;
	private ONode2D[] nodes,nodes_tmp, ui, ui_tmp;
	private int nodes_size,ui_size;
	private HashMap<Integer, ArrayList<ONode2D>> postRenderList,preRenderList;
	private int PreRenderSpecialRange, PostRenderSpecialRange;
	private Vector<ONode2DCamera> cameras;
	private ONode2DCamera activeCamera;
	private float sWidth, sHeight;
	private int counter;
	private static final int salt = 200000000;
	private static final boolean uvflip = true;
	private boolean opSearch,opSearch2;
	private OUtilClock clock;
	/**
	 * Whether or not to render nodes when they leave the screen
	 * */
	private boolean clipNodes;

	public OOrthoEnvironment(OrangeDevice Device) {
		device = Device;
		nodes = new ONode2D[0];
		ui = new ONode2D[0];
		cameras = new Vector<ONode2DCamera>();
		postRenderList = new HashMap<Integer, ArrayList<ONode2D>>();
		preRenderList = new HashMap<Integer, ArrayList<ONode2D>>();
		PreRenderSpecialRange = 0;
		PostRenderSpecialRange = 0;
		counter = 0;
		device.getMainClock().setDelay(0);
		sWidth = 0;
		sHeight = 0;
		nodes_size=0;
		ui_size=0;
		opSearch = true;
		opSearch2 = true;
		clipNodes = true;
		clock = Device.getMainClock();
	}
	/**
	 * Set whether or not to render nodes that are outside of the screen.
	 * @param set TRUE means they will NOT be rendered when outside of the screen, FALSE means they will
	 * */
	public void setClipNodes(boolean set){
		clipNodes = !set;
	}
	/**
	 * Insert a node into the Nodes array.
	 * @param add The node to insert
	 * @param search whether or not to search for existing null entries to fill (slow)
	 * */
	private void insertArrayNode(ONode2D add, boolean search){
		if(search){
			try{
			for(int i=0; i<nodes_size; i++){
				if(nodes[i]==null){
					nodes[i] = add;
					return;
				}
			}
			}catch(ArrayIndexOutOfBoundsException e){
				Log.e("insertArrayNode", "Error: "+e.getMessage());
			}
		}
		nodes_size++;
		nodes_tmp = (ONode2D[])nodes.clone();
		nodes = new ONode2D[nodes_size];
		System.arraycopy(nodes_tmp, 0, nodes, 0, nodes_size-1);
		/*for(int i=0; i<nodes_size-1; i++){
			nodes[i] = nodes_tmp[i];
		}*/
		nodes[nodes_size-1] = add;
		nodes_tmp=null;
		return;
	}
	/**
	 * Insert a node into the UI array.
	 * @param add The node to insert
	 * @param search whether or not to search for existing null entries to fill (slow)
	 * */
	private void insertArrayUI(ONode2D add, boolean search){
		if(search){
			for(int i=0; i<ui_size; i++){
				if(ui[i]==null){
					ui[i] = add;
					return;
				}
			}
		}
		ui_size++;
		ui_tmp = (ONode2D[])ui.clone();
		ui = new ONode2D[ui_size];
		System.arraycopy(ui_tmp, 0, ui, 0, ui_size-1);
		/*for(int i=0; i<ui_size-1; i++){
			ui[i] = ui_tmp[i];
		}*/
		ui[ui_size-1] = add;
	}

	/**
	 * Set the dimensions of the screen. They will be overridden when
	 * onSurfaceChange is called on the renderer, but this is useful for setting
	 * up the interface before the surface gets created.
	 * */
	public void setDimensions(float width, float height) {
		sWidth = width;
		sHeight = height;
	}

	/**
	 * Get the OrangeDevice instance from this ortho environment.
	 * */
	public OrangeDevice getDevice() {
		return device;
	}

	/**
	 * Notify the ortho environment of a surface change. Should be called on
	 * glSurfaceViews onSurfaceChange method
	 * 
	 * @param width
	 *            the width given as a parameter to
	 *            glSurfaceView.onSurfaceChange
	 * @param height
	 *            the height given as a parameter to
	 *            glSurfaceView.onSurfaceChange
	 * */
	public void notifySurfaceChange(GL10 gl, int width, int height) {
		sWidth = (float) width;
		sHeight = (float) height;
	}

	/**
	 * Anything with a SpecialListID less than this will be rendered before the
	 * whole scene
	 * */
	public void setPreRenderSpecialRange(int set) {
		PreRenderSpecialRange = set;
	}

	/**
	 * Anything with a SpecialListID less than or equal to this and greater than
	 * or equal to PreRenderSpecialRange will be rendered after the whole scene
	 * */
	public void setPostRenderSpecialRange(int set) {
		PostRenderSpecialRange = set;
	}

	/**
	 * Moves node to the special render list. \nNode must be part of normal list
	 * and not already be in special list
	 * 
	 * @param move
	 *            Node to move into list
	 * @param SpecialListID
	 *            special ID to assign to node
	 * @see setPreRenderSpecialRange
	 * @see setPostRenderSpecialRange
	 * */
	public void moveToSpecialList(ONode2D move, int SpecialListID) {
		HashMap<Integer, ArrayList<ONode2D>> list;
		if(SpecialListID<=PreRenderSpecialRange){
			list = preRenderList;
		}else{			
			list = postRenderList;
		}
		for (int i = 0; i < nodes_size; i++) {
			if (nodes[i] == move) {
				if(list.containsKey(SpecialListID)){
					list.get(SpecialListID).add(nodes[i]);
					nodes[i]=null;
				}else{
					ArrayList<ONode2D> add = new ArrayList<ONode2D>();
					add.add(nodes[i]);
					list.put(SpecialListID, add);
					nodes[i]=null;
				}
				return;
			}
		}
		for (int i = 0; i < ui_size; i++) {
			if (ui[i] == move) { 
				if(list.containsKey(SpecialListID)){
					list.get(SpecialListID).add(ui[i]);
					ui[i]=null;
				}else{
					ArrayList<ONode2D> add = new ArrayList<ONode2D>();
					add.add(ui[i]);
					list.put(SpecialListID, add);
					ui[i]=null;
				}
				return;
			}
		}
	}
	/**
	 * Create a Dynamic Text node and set the text
	 * @param font The font to use
	 * @param length The default text to set
	 * */
	public ONode2DDynamicText addNode2DDynamicText(OFont font, String text) {
		ONode2DDynamicText ret = new ONode2DDynamicText(salt + counter, font,
				text, device);
		char[] ar = text.toCharArray();
		for (int i = 0; i < ar.length; i++) {
			ret.insertChar(addNode2DChar(font, ar[i]));
		}
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}
	/**
	 * Create a Dynamic Text node with a given length
	 * @param font The font to use
	 * @param length The maximum amount of characters allocated for this node
	 * */
	public ONode2DDynamicText addNode2DDynamicText(OFont font, int length) {
		String text = "";
		for(int i=0; i<length; i++){
			text+='\0';
		}
		ONode2DDynamicText ret = new ONode2DDynamicText(salt + counter, font,
				text, device);
		char[] ar = text.toCharArray();
		for (int i = 0; i < ar.length; i++) {
			ret.insertChar(addNode2DChar(font, ar[i]));
		}
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create a Tile node
	 * 
	 * @param tileset
	 *            loaded instance of OTileset to use
	 * @param TileX
	 *            the column of the tile to use
	 * @param TileY
	 *            the row of the tile to use
	 * @param width
	 *            The width to render the tile
	 * @param height
	 *            The height to render the tile
	 * */
	public ONode2DTile addNode2DTile(OTileset tileset, int TileX, int TileY,
			float width, float height) {
		float tw = width;
		float th = height;

		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0, 0, null);
		m.set(1, 0, tw, 0.0f, 0.0f, 0, 0, null);
		m.set(0, 1, 0.0f, th, 0.0f, 0, 0, null);
		m.set(1, 1, tw, th, 0.0f, 0, 0, null);

		ONode2DTile ret = new ONode2DTile(salt + counter, tw, th, TileX, TileY,
				m, tileset, device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create an Animated Tile node
	 * 
	 * @param tileset
	 *            loaded instance of OTileset to use
	 * @param frames
	 *            Vector array of all frames in the animation
	 * @param width
	 *            The width to render the tile
	 * @param height
	 *            The height to render the tile
	 * */
	public ONode2DAnimatedTile addNode2DAnimatedTile(OTileset tileset,
			Vector<OAnimatedTileFrame> frames, float width, float height) {
		float tw = width;
		float th = height;

		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0, 0, null);
		m.set(1, 0, tw, 0.0f, 0.0f, 0, 0, null);
		m.set(0, 1, 0.0f, th, 0.0f, 0, 0, null);
		m.set(1, 1, tw, th, 0.0f, 0, 0, null);
		Vector<OAnimatedTileFrame> Fr = new Vector<OAnimatedTileFrame>();
		for (OAnimatedTileFrame F : frames)
			Fr.add(new OAnimatedTileFrame(F));
		ONode2DAnimatedTile ret = null;
		ret = new ONode2DAnimatedTile(salt + counter, tw, th, Fr, m, tileset,
				device);

		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create an Animated Tile node
	 * 
	 * @param tileset
	 *            loaded instance of OTileset to use
	 * @param info
	 *            Data returned by the Animation parser
	 * @param width
	 *            The width to render the tile
	 * @param height
	 *            The height to render the tile
	 * */
	public ONode2DAnimatedTile addNode2DAnimatedTile(OTileset tileset,
			ONode2DAnimatedTile_helper_importinfo Info, float width,
			float height) {
		float tw = width;
		float th = height;

		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0, 0, null);
		m.set(1, 0, tw, 0.0f, 0.0f, 0, 0, null);
		m.set(0, 1, 0.0f, th, 0.0f, 0, 0, null);
		m.set(1, 1, tw, th, 0.0f, 0, 0, null);

		ONode2DAnimatedTile ret = new ONode2DAnimatedTile(salt + counter, tw,
				th, Info, m, tileset, device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}
	
	/**
	 * Same as calling setCenter except it can be used inline since it returns the node
	 * after its been centered
	 * */
	public ONode2D centerAndReturn(ONode2D node){
		node.setCenter();
		return node;
	}

	/**
	 * Create a camera node for manipulating the projection(zooming, panning,
	 * etc)
	 * 
	 * @param W
	 *            the Width of the camera projection (in pixels)
	 * @param H
	 *            the Height of the camera projection (in pixels)
	 * */
	public ONode2DCamera addNode2DCamera(float W, float H) {
		ONode2DCamera ret = new ONode2DCamera(cameras.size(), W, H, device);
		cameras.add(ret);
		if (activeCamera == null)
			activeCamera = ret;
		return ret;
	}

	/**
	 * Set the active camera (the camera that is currently manipulating the
	 * projection)
	 * 
	 * @param set
	 *            The camera to set
	 * */
	public void setActiveCamera(ONode2DCamera set) {
		activeCamera = set;
	}

	/**
	 * Get the active camera (the camera that is currently manipulating the
	 * projection)
	 * 
	 * @return The active camera or NULL if no camera has been set
	 * */
	public ONode2DCamera getActiveCamera() {
		return activeCamera;
	}

	/**
	 * Create a Character node
	 * 
	 * @param OFont
	 *            the font to use (loaded through OFontManager)
	 * */
	public ONode2DChar addNode2DChar(OFont font, char C) {
		OFCharacter character = font.getCharacters().get(C);
		if(character == null) {
			Log.d("OORTHO", "Could not retrieve character from font: "+C);
			return null;
		}
		float W = character.mapping.W;
		float H = character.mapping.H;
		float X = character.mapping.X;
		float Y = character.mapping.Y;
		float tw = font.getBWidth();
		float th = font.getBHeight();

		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, X / tw, (Y + H) / th, null);
		m.set(1, 0, W, 0.0f, 0.0f, (X + W) / tw, (Y + H) / th, null);
		m.set(0, 1, 0.0f, H, 0.0f, X / tw, Y / th, null);
		m.set(1, 1, W, H, 0.0f, (X + W) / tw, Y / th, null);

		ONode2DChar ret = new ONode2DChar(salt + counter, W, H, m, C, font,
				device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create a 2D sprite node
	 * 
	 * @param image
	 *            The file of the image you wish to use
	 * */
	public ONode2DSprite addNode2DSprite(String image, int Width, int Height) {
		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(1, 0, Width, 0.0f, 0.0f, 1.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(0, 1, 0.0f, Height, 0.0f, 0.0f, (uvflip) ? 1.0f : 0.0f, null);
		m.set(1, 1, Width, Height, 0.0f, 1.0f, (uvflip) ? 1.0f : 0.0f, null);

		ONode2DSprite ret = new ONode2DSprite(salt + counter, Width, Height, m,
				device.getTexLoader().loadTex(image), device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}
	
	/**
	 * Create a 2D 9-slice node
	 * 
	 * @param image
	 * 				The file of the image you wish to use
	 * @param Width
	 * 				Width of Node
	 * @param Height
	 * 				Height of Node
	 * @param SliceWidth
	 * 				Width of the top-left slice
	 * @param SliceHeight
	 * 				Height of the top-left slice
	 * */
	public ONode2D9Slice addNode2D9Slice(String image, int Width, int Height, int SliceWidth, int SliceHeight) {
		Grid m[] = new Grid[9];
		for(int i=0; i<9; i++){
			m[i] = new Grid(2, 2, false);
			m[i].set(0, 0, 0.0f, 0.0f, 0.0f, 0.0f, (uvflip) ? 0.0f : 1.0f, null);
			m[i].set(1, 0, Width, 0.0f, 0.0f, 1.0f, (uvflip) ? 0.0f : 1.0f, null);
			m[i].set(0, 1, 0.0f, Height, 0.0f, 0.0f, (uvflip) ? 1.0f : 0.0f, null);
			m[i].set(1, 1, Width, Height, 0.0f, 1.0f, (uvflip) ? 1.0f : 0.0f, null);
		}
		ONode2D9Slice ret = new ONode2D9Slice(salt + counter, Width, Height, SliceWidth, SliceHeight, m,
				device.getTexLoader().loadTex(image), device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create a 2D rectangle node No texture but allows colors, alpha, rotation
	 * and all the usual stuff
	 * */
	public ONode2DRect addNode2DRect(int Width, int Height) {
		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(1, 0, Width, 0.0f, 0.0f, 1.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(0, 1, 0.0f, Height, 0.0f, 0.0f, (uvflip) ? 1.0f : 0.0f, null);
		m.set(1, 1, Width, Height, 0.0f, 1.0f, (uvflip) ? 1.0f : 0.0f, null);

		ONode2DRect ret = new ONode2DRect(salt + counter, Width, Height, m,
				device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create a 2D sprite node
	 * 
	 * @param image
	 *            The file of the image you wish to use
	 * @param Width
	 * 			  The Width of the node in pixels
	 * @param Height
	 * 			  The Height of the node in pixels
	 * @param ScrollX
	 * 			  The Speed at which to scroll horizontally every frame
	 * @param ScrollY
	 * 			  The Speed at which to scroll vertically every frame
	 * */
	public ONode2DScrolling addNode2DScrolling(String image, int Width,
			int Height, float ScrollX, float ScrollY) {
		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(1, 0, Width, 0.0f, 0.0f, 1.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(0, 1, 0.0f, Height, 0.0f, 0.0f, (uvflip) ? 1.0f : 0.0f, null);
		m.set(1, 1, Width, Height, 0.0f, 1.0f, (uvflip) ? 1.0f : 0.0f, null);
		ONode2DScrolling ret = new ONode2DScrolling(salt + counter, Width,
				Height, m, device.getTexLoader().loadTex(image), ScrollX,
				ScrollY, device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}
	/**
	 * Create a 2D sprite node
	 * 
	 * @param image
	 *            The file of the image you wish to use
	 * @param Width
	 * 			  The Width of the node in pixels
	 * @param Height
	 * 			  The Height of the node in pixels
	 * */
	public ONode2DScrolling addNode2DScrolling(String image, int Width,	int Height) {
		Grid m = new Grid(2, 2, false);
		m.set(0, 0, 0.0f, 0.0f, 0.0f, 0.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(1, 0, Width, 0.0f, 0.0f, 1.0f, (uvflip) ? 0.0f : 1.0f, null);
		m.set(0, 1, 0.0f, Height, 0.0f, 0.0f, (uvflip) ? 1.0f : 0.0f, null);
		m.set(1, 1, Width, Height, 0.0f, 1.0f, (uvflip) ? 1.0f : 0.0f, null);
		ONode2DScrolling ret = new ONode2DScrolling(salt + counter, Width,
				Height, m, device.getTexLoader().loadTex(image), 0,	0, device);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}
	
	/**
	 * Adds a custom node to the scene
	 * */
	public void addCustomNode2D(ONode2D add){
		insertArrayNode(add, opSearch);
	}

	/**
	 * Attach a node to the UI layer, this will prevent it from going off-screen
	 * when the active camera is moved
	 * 
	 * @param node
	 *            Reference to the node you want to attach. it MUST be in the
	 *            node list (e.g. no custom instantiation or already in UI)
	 * */
	public void attachToUI(ONode2D node) {
		//Recursive hack for text nodes
		try{
			ONode2DDynamicText txt = ((ONode2DDynamicText)node);
			Vector<ONode2DChar> chars = txt.getChars();
			for(int i=0; i<chars.size(); i++){
				attachToUI(chars.get(i));
			}				
		}catch(ClassCastException e){
			//--
		}
		for (int i = 0; i < nodes_size; i++) {
			if (nodes[i] == node) {
				nodes[i]=null;
				insertArrayUI(node,opSearch2);
			}
		}
	}

	/**
	 * Create a 2D text node
	 * 
	 * @param text
	 *            The text for the node to render
	 * */
	public ONode2DText addNode2DText(String text) {
		ONode2DText ret = new ONode2DText(salt + counter, text, this);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * Create a compound node
	 * 
	 * @param text
	 *            The text for the node to render
	 * */
	public ONodeCompound addONodeCompound() {
		ONodeCompound ret = new ONodeCompound(salt + counter);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}
	
	public ONode2D findNodeById(int ID){
		for(int i=0; i<nodes.length; i++){
			if(nodes[i].getId() == ID)
				return nodes[i];
		}
		return null;
	}

	/**
	 * Create a compound node
	 * 
	 * @param text
	 *            The text for the node to render
	 * */
	public ONodeCompound addONodeCompound(ONode2D parent) {
		ONodeCompound ret = new ONodeCompound(salt + counter);
		ret.addNode(parent);
		counter++;
		insertArrayNode(ret, opSearch);
		return ret;
	}

	/**
	 * empties the environment by clearing everything
	 * */
	public void empty() {
		for(int i=0; i<nodes_size; i++){
			nodes[i]=null;
		}
		for(int i=0; i<ui_size; i++){
			ui[i]=null;	
		}
	}
	
	/**
	 * Removes a node from the regular, special, and ui lists. useful for manual rendering
	 * @param node Node to remove.
	 * */
	public void remove(ONode2D node){
		for(int i=0; i<nodes.length; i++){
			if(nodes[i]==node){
				nodes[i] = null;
				return;
			}
		}
		for(int i=0; i<ui.length; i++){
			if(ui[i]==node){
				ui[i] = null;
				return;
			}
		}		
		/*for(int i=0; i<specialList.size(); i++){
			if(specialList.get(i)==node){
				specialList.remove(i);
				return;
			}
		}*/
	}

	/**
	 * Re-Add a node to the scene. Useful for when you want to return nodes you
	 * kept a reference of after calling OOrthoEnvironment.empty()
	 * 
	 * @param add
	 *            Node to return to the scene
	 * */
	public void readdNode(ONode2D add) {
		insertArrayNode(add, opSearch);
	}

	public int getWidth() {
		return (int) sWidth;
	}

	public int getHeight() {
		return (int) sHeight;
	}

	/**
	 * Render a single node
	 * */
	public void renderSingle(GL10 gl, ONode2D node){
		renderSingle(gl, node, true, true);
	}
	public void renderSingle(GL10 gl, ONode2D node, boolean animate, boolean count) {
		if(!node.isVisible()) return;
		if(count)
			device.getMainClock().count();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0, sWidth, sHeight, 0, -100000, 100000);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		Grid.beginDrawing(gl, true, false);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		if(animate)
			node.animate();
		node.render(gl);

		gl.glPopMatrix();
		Grid.endDrawing(gl);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_DEPTH_TEST);
	}

	/**
	 * Render all the elements of the GUI
	 * */
	public synchronized void renderAll(GL10 gl) {
		device.getMainClock().count();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		if (activeCamera != null)
			activeCamera.render(gl);
		else
			gl.glOrthof(0, sWidth, sHeight, 0, -100000, 100000);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		Grid.beginDrawing(gl, true, false);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		/*Iterator it = preRenderList.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			ArrayList<ONode2D> list = (ArrayList<ONode2D>)pairs.getValue();
			for(int v=0; v<list.size(); v++){
				ONode2D node = list.get(v);
			if(node==null)
				continue;
			vector3df pos = node.getPosition();
			boundbox2df cp = null;
			if(activeCamera!=null)
				cp = activeCamera.getBounds();
			node.animate();
			if((cp!=null?node.getBoundsTranslatedz().intersectsWith(cp):true) || clipNodes){
					if (((node.getParent() == null) ? (node.isVisible())
							: ((node.getInheritanceTrait(InheritanceList.IN_VISIBILITY) ? 
									((node.getParent().isVisible())?node.isVisible():false)
									: node.isVisible())))
							&& node.isRenderable()) {
						node.render(gl);
					}
				}
			}
		}*/
		for (int i = 0; i < nodes_size; i++) {
			ONode2D node;
			try{
				node = nodes[i];
			}catch(ArrayIndexOutOfBoundsException e){
				node = null;
			}
			if(node==null)
				continue;
			vector3df pos = node.getPosition();
			boundbox2df cp = null;
			if(activeCamera!=null)
				cp = activeCamera.getBoundsTranslatedz();
			node.animate();
			if((cp!=null?node.getBoundsTranslatedz().intersectsWith(cp):true) || clipNodes){
				if (((node.getParent() == null) ? (node.isVisible())
						: ((node.getInheritanceTrait(InheritanceList.IN_VISIBILITY) ?
								((node.getParent().isVisible())?node.isVisible():false)
								: node.isVisible())))
						&& node.isRenderable()) {
					node.render(gl);
				}
				if (node.isRemoved())
					nodes[i]=null;
			}
		}
		gl.glPopMatrix();
		Grid.endDrawing(gl);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0, sWidth, sHeight, 0, -10, 100000);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		Grid.beginDrawing(gl, true, false);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPushMatrix(); 
		gl.glLoadIdentity();
		for (int i = 0; i < ui_size; i++) {
			ONode2D node;
			try{
				node = ui[i];
			}catch(ArrayIndexOutOfBoundsException e){
				node = null;
			}
			if(node==null)
				continue;
			node.animate();
			if (((node.getParent() == null) ? (node.isVisible())
					: ((node.getInheritanceTrait(InheritanceList.IN_VISIBILITY) ? 
							((node.getParent().isVisible())?node.isVisible():false)
							: node.isVisible())))
					&& node.isRenderable()) {
				node.render(gl);
			}
			if (node.isRemoved())
				nodes[i]=null;
		}
		
		/*it = postRenderList.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			ArrayList<ONode2D> list = (ArrayList<ONode2D>)pairs.getValue();
			for(int v=0; v<list.size(); v++){
				ONode2D node = list.get(v);
			if(node==null)
				continue;
			vector3df pos = node.getPosition();
			boundbox2df cp = null;
			if(activeCamera!=null)
				cp = activeCamera.getBounds();
			node.animate();
			if((cp!=null?node.getBoundsTranslatedz().intersectsWith(cp):true) || clipNodes){
					if (((node.getParent() == null) ? (node.isVisible())
							: ((node.getInheritanceTrait(InheritanceList.IN_VISIBILITY) ? 
									((node.getParent().isVisible())?node.isVisible():false)
									: node.isVisible())))
							&& node.isRenderable()) {
						node.render(gl);
					}
				}
			}
		}
		*/
		gl.glPopMatrix();
		Grid.endDrawing(gl);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_DEPTH_TEST);

	}
}
