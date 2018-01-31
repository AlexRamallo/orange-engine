package com.snakeinalake.engine.mesh;

public class OMeshBone{
	private OMeshInfo mesh;
	private OMeshBone parent;
	
	public OMeshBone(String Name){
		this(null, Name, null);
	}
	public OMeshBone(OMeshBone Parent, String Name, OMeshInfo Mesh){
		mesh = Mesh;
	}
	public void setMesh(OMeshInfo set){
		mesh = set;
	}
	public void setParent(OMeshBone set){
		parent = set;
	}
	/**
	 * Get this bones mesh
	 * */
	public OMeshInfo getMesh(){
		return mesh;
	}
	/**
	 * Get this bones parent.
	 * */
	public OMeshBone getParent(){
		return parent;
	}
}
