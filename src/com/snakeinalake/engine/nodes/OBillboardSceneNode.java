package com.snakeinalake.engine.nodes;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.mesh.OMesh;
import com.snakeinalake.engine.utilities.OBasicCollision;

public class OBillboardSceneNode extends OSceneNode{
	OMesh mesh;
	
	public OBillboardSceneNode(int ID, OMesh Mesh){
		super(ID);
		mesh = Mesh;
	}
	public OBillboardSceneNode(OBillboardSceneNode copy){
		super(copy.getId());
		this.mesh = copy.getMesh(); 
	}
	
	public OMesh getMesh(){
		return mesh;
	}
	@Override
	public void render(GL10 gl){
		if(parent!=null){
			mesh.render(gl,parent.getPosition().x+position.x,
							parent.getPosition().y+position.y,
								parent.getPosition().z+position.z,
					parent.getRotation().x+rotation.x,
							parent.getRotation().y+rotation.y,
							parent.getRotation().z+rotation.z,
					parent.getScale().x*scale.x,
							parent.getScale().y*scale.y,parent.getScale().z*scale.z);
		}else{
			float hd = OBasicCollision.vectorDirection(position.x, position.z,
					getSceneManager().getActiveCamera().getPosition().x,
					getSceneManager().getActiveCamera().getPosition().z);
			
			float vd = OBasicCollision.vectorDirection(position.x, position.y,
					getSceneManager().getActiveCamera().getPosition().x,
					getSceneManager().getActiveCamera().getPosition().y);
			mesh.render(gl,position.x,position.y, position.z,
					(float)((vd-90)/Math.PI*180), (float)((hd-90)/Math.PI*180),0
					,scale.x,scale.y,scale.z);
		}
	}
}
