package com.snakeinalake.engine.ortho;

import java.util.Vector;

import com.snakeinalake.engine.OOrthoEnvironment;
import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.text.OFont;

public class ONode2DDynamicText extends ONode2D{
	private static final long serialVersionUID = -5656231021548170601L;
	private Vector<ONode2DChar> nodes;
	public OOrthoEnvironment env;
	private OrangeDevice device;
	private String text;
	private OFont font;
	private float size;
	private float margin;
	private boolean smooth;
	private boundbox2df area;

	public ONode2DDynamicText(int ID, OFont Font, String txt, OrangeDevice Device){
		super(ID);
		device = Device;
		text = txt;
		font = Font;
		nodes = new Vector<ONode2DChar>();
		size = 1;
		margin = 1;
		smooth = false;
	}
	public void insertChar(ONode2DChar add){
		add.setParent(this);
		add.setPosition((bounds.w*size),0);
		add.setInheritanceTrait(InheritanceList.IN_VISIBILITY, true);
		bounds.combine(add.getBounds());
		nodes.add(add);
	}
	public void setFont(OFont set){
		font = set;
		for(int i=0; i<nodes.size(); i++){
			nodes.get(i).setFont(font);
		}
		setSize(size);
	}
	/**
	 * Check whether or not smooth scaling is active for this node
	 * */
	public boolean isSmooth(){
		return smooth;
	}
	/**
	 * Get the list of characters in this text node
	 * */
	public Vector<ONode2DChar> getChars(){
		return nodes;
	}
	/**
	 * Set whether or not to use smooth scaling
	 * */
	public void setSmooth(boolean set){
		smooth = set;
	}
	/**
	 * Set the text area of this text node. Used for wordwrapping and such
	 * */
	public void setArea(boundbox2df set){
		area = set;
	}
	/**
	 * Returns the vector containing all the characters in this node.
	 * </br>**BE CAREFUL!**
	 * */
	public Vector<ONode2DChar> getCharArray(){
		return nodes;
	}
	
	public float getHeight(){
		return getHeight(true);
	}
	public float getHeight(boolean invisible){
		float h = bounds.h;
		for(int i=0; i<nodes.size(); i++){
			ONode2D c = nodes.get(i);
			if(invisible || (!invisible && c.isVisible())){
				float ch = c.getHeight()*size;
				if(ch>h)
					h=ch;
			}
		}
		return h;
	}
	
	/**
	 * @return The width of the node in pixels, including the size of invisible characters
	 * 
	 * @see getWidth(boolean invisible)
	 * */
	public float getWidth(){
		return getWidth(true);
	}
	
	/**
	 * @param invisible Whether or not to count nodes that aren't visible into the width
	 * */
	public float getWidth(boolean invisible){
		//return bounds.w * size;
		float w = bounds.w;
		for(int i=0; i<nodes.size(); i++){
			ONode2D c = nodes.get(i);
			if(invisible || (!invisible && c.isVisible()))
				w+=c.getWidth()*size;
		}
		return w;
	}

	/**
	 * Returns the "Size" needed so that that each line will render at the given height regardless of font bitmap
	 * Returns -1 if this node has not been initialized with a font
	 *
	 * For example, to make sure MyTxtNode is at least 150 pixels tall, call:
	 *
	 * MyTxtNode.setSize(sizeToSetLineHeight(150));
	 * */
	public float sizeToSetLineHeight(float H){
		if(nodes.size()>0)
			return H/nodes.get(0).getHeight();
		return -1;
	}
	/**
	 * Get the appropriate size to guarantee that there will be no horizontal overflow.
	 * @Param W the maximum width
	 * @return The size that you need to set with setSize() to prevent overflow
	 * */
	public float sizeToFitInHorizontalSpace(float W){
		if(nodes.size()<=0)
			return 0;
		float TW=0;
		float nullsize = nodes.get(0).getWidth();
		for(int i=0; i<nodes.size(); i++){
			ONode2DChar Ch = nodes.get(i);
			float ww = Ch.getWidth();
			if(ww<nullsize)
				nullsize = ww;
		}
		for(int c=0; c<nodes.size(); c++){
			ONode2DChar Ch = nodes.get(c);
			float ww = 0;
			if(Ch.Char != '\0')
				ww = (Ch.getWidth());
			else
				ww = nullsize;
			
			TW+=ww + (margin*size)*(c!=nodes.size()-1?1:0);
		}
		return W/TW;
	}
	/**
	 * Will wrap the text to fit inside the area horizontally. Will still flow out vertically<br>
	 * <strong>WARNING!</strong><br>This function is expensive!
	 * */
	public void Area_wrap(){
		//ONode2DChar[][] words;
		//Count the number of words (separated by space or \n)
		Vector<String> words = new Vector<String>();
		String build = "";
		for(int i=0; i<nodes.size(); i++){
			if(build.equals(""))
				build = Integer.toString(i);
			if(nodes.get(i).Char==' ' || nodes.get(i).Char == '\n'){
				build+="-"+Integer.toString(i);
				words.add(build);
				build="";
			}
		}
		int lWidth=0,tmpWidth=0,lCount=1;
		int wend, wstart;
		Vector<ONode2DChar> buf = new Vector<ONode2DChar>();
		for(int i=0; i<words.size(); i++){
			String[] split = words.get(i).split("-");
			wstart = Integer.parseInt(split[0].substring(0, split[0].length()));
			wend = Integer.parseInt(split[1]);
			for(int c=wstart; c<wend; c++){
				ONode2DChar cH = nodes.get(c);
				cH.setPosition(cH.getPosition().x, area.y);
				buf.add(cH);
				tmpWidth+=cH.getBoundsScaled().w;
				lWidth+=tmpWidth;
			}
			if(lWidth>area.w){
				lCount++;
			}
			for(int c=0; c<buf.size(); c++){
				buf.get(c).setPosition(
					((c==0)?area.x:((buf.get(c-1).getAbsolutePosition().x)+(margin*size)+(size*buf.get(c-1).getBounds().w)))
					,area.y*lCount);
			}
		}
		
	}
	/**
	 * Sets the size of the text. If Area_wrap() was previously called, this call will fuck up the formatting
	 * again, so make sure you call this BEFORE Area_wrap()
	 * */
	public void setSize(float set){
		float factor = set/size;
		bounds.w*=factor;
		bounds.h*=factor;
		size=set;
		for(int i=0; i<nodes.size(); i++){
			ONode2DChar N = nodes.get(i);
			N.setPosition(
					((i==0)?0:((nodes.get(i-1).getAbsolutePosition().x)+(margin*size)+(size*nodes.get(i-1).getBounds().w)))					
			,0);
		}
		setScale(size,size);
	}
	public void setNodeAtPoint(int loc, ONode2DChar set){
		if(nodes.size()>loc){
			nodes.set(loc, set);
		}
	}
	public void setAllVisible(boolean set){
		for(int i=0; i<nodes.size(); i++){
			ONode2DChar c = nodes.get(i);
			if(c.Char!='\0')
				c.setVisible(set);
		}
	}
	/**
	 *	Automatically changes/removes/creates new nodes to display new text<br/>
	 *	if the new text is longer than the old text, new nodes will automatically be added.<br/>
	 *	if the new text is shorter than the old text, existing nodes will be overwritten and unused nodes will be made invisible(to avoid the GC)<br/>
	 *	if the new text is the exact same length than the old text, then existing nodes will be changed
	 * */
	public void setText(String set){
		//int diff = set.length()-text.length();
		int diff = set.length()-nodes.size();
		char[] ar = set.toCharArray();
		//Make all nodes invisible, and set characters to a null ('\0')
		for(int i=0; i<nodes.size(); i++){
			ONode2DChar c = nodes.get(i);
			nodes.get(i).setVisible(false);
			c.setCharacter('\0');
		}
		//If we need more characters...
		if(diff>0){
			//Make all existing nodes visible, and set their characters appropriately
			for(int i=0; i<text.length(); i++){
				ONode2DChar n = nodes.get(i); 
				if(n.Char!=ar[i])
					n.setCharacter(ar[i]);
				n.setVisible(true);
			}
			//Create new nodes as needed
			for(int i=0; i<diff; i++){
				ONode2DChar c = device.getOrthoEnvironment().addNode2DChar(font, ar[text.length()+i]);
				/*if(c==null)
					continue;*/
				insertChar(c); 
			}
		}else{
			//Set visible only what is needed...
			for(int i=0; i<ar.length; i++){
				ONode2DChar n = nodes.get(i); 
				if(n.Char!=ar[i])
					n.setCharacter(ar[i]);
				n.setVisible(true);
			}
		}
		setSize(size);
		text = set;
	}
	@Override
	public void setPreRenderPass(ORenderPass set) {
		for(int i=0; i<nodes.size(); i++){
			nodes.get(i).setPreRenderPass(set);
		}
	}
	@Override
	public void setPostRenderPass(ORenderPass set) {
		for(int i=0; i<nodes.size(); i++){
			nodes.get(i).setPostRenderPass(set);
		}
	}
	public ONode2DChar getNodeAtPoint(int loc){
		if(nodes.size()>loc){
			return (ONode2DChar)nodes.get(loc);
		}
		return null;
	}
	public float getSize() {
		return size;
	}
	public String getText() {
		return text;
	}
}
