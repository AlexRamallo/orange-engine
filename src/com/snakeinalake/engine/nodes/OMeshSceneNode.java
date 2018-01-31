package com.snakeinalake.engine.nodes;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.mesh.OMesh;

public class OMeshSceneNode extends OSceneNode{
	OMesh mesh;
	public OMeshSceneNode(int ID, OMesh Mesh){
		super(ID);
		mesh = Mesh;
	}
	public OMeshSceneNode(OMeshSceneNode copy){
		super(copy.getId());
		this.mesh = copy.getMesh(); 
	}
	
	public OMesh getMesh(){
		return mesh;
	}
	public void setMesh(OMesh set){
		mesh = set;
	}
	@Override
	public void render(GL10 gl){
		if(parent!=null){
			mesh.render(gl,
					parent.getPosition().x+position.x,
							parent.getPosition().y+position.y,
								parent.getPosition().z+position.z,
					parent.getRotation().x+rotation.x,
							parent.getRotation().y+rotation.y,
							parent.getRotation().z+rotation.z,
					parent.getScale().x*scale.x,
							parent.getScale().y*scale.y,parent.getScale().z*scale.z);
		}else{
			mesh.render(gl,position,rotation,scale);
		}
	}
	
	@Override
	public OSceneNode clone() {
		OMeshSceneNode ret = (OMeshSceneNode)super.clone();
		ret.mesh = mesh.clone();
		return ret;
	}
}
