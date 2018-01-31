package com.snakeinalake.engine.ortho;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class ONodeCompound extends ONode2D{
	private static final long serialVersionUID = 3912417557933557107L;
	public Vector<ONode2D> nodes;
	
	public ONodeCompound(int ID) {
		super(ID);
		nodes = new Vector<ONode2D>();
	}
	
	public void addNode(ONode2D add){
		add.setParent(this);
		bounds.combine(add.getBounds());
		nodes.add(add);
	}
	
	public void setNodeAtPoint(int loc, ONode2D set){
		if(nodes.size()>loc){
			nodes.set(loc, set);
		}
	}
	
	public ONode2D getNodeAtPoint(int loc){
		if(nodes.size()>loc){
			return nodes.get(loc);
		}
		return null;
	}
	@Override
	public void setDepth(float set) {
		for(ONode2D node: nodes)
			node.setDepth(set);
	}
	@Override
	public void setVisible(boolean set) {
		for(ONode2D node: nodes)
			node.setVisible(set);
	}
	
	public int getNodeCount(){
		return nodes.size();
	}
	@Override
	public void setPreRenderPass(ORenderPass set) {
		for(ONode2D n: nodes)
			n.setPreRenderPass(set);
	}
	@Override
	public void setPostRenderPass(ORenderPass set) {
		for(ONode2D n: nodes)
			n.setPostRenderPass(set);
	}
	@Override
	public void render(GL10 gl) {
	}
}
