package com.snakeinalake.engine.nodes;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

import com.snakeinalake.engine.mesh.OAnimatedMesh;
import com.snakeinalake.engine.mesh.OMesh;
import com.snakeinalake.engine.nodes.frames.OFrameInfo;
import com.snakeinalake.engine.nodes.frames.OFrameSet;
import com.snakeinalake.engine.utilities.OUtilClock;
/**
 * Scene Node that support 3D frame based animation
 * */
public class OAnimatedMeshSceneNode extends OSceneNode{
	public Vector<OMesh> stash;
	OMesh mesh;
	public Vector<OFrameSet> FrameSets;
	OUtilClock clock;
	
	int curSet,curFrame;
	
	public OAnimatedMeshSceneNode(int ID, OMesh Mesh){
		super(ID);
		mesh = Mesh;
	}
	public OAnimatedMeshSceneNode(OUtilClock Clock){
		super(-1);
		clock = Clock;
		mesh = null;
		FrameSets = new Vector<OFrameSet>();
		stash = new Vector<OMesh>();
		curSet = 0;
		curFrame = 0;
	}
	public void setTexture(int ID){
		for(int i=0; i<stash.size(); i++){
			stash.get(i).setTexture(ID);
		}
	}
	public OAnimatedMeshSceneNode(OAnimatedMeshSceneNode copy){
		super(copy.getId());
		this.mesh = copy.getMesh(); 
	}
	public void playSet(String name){
		for(int i=0; i<FrameSets.size(); i++){
			if(FrameSets.get(i).name.equals(name)){
				if(curSet == i)
					return; //Avoid resetting the animation
				curSet = i;curFrame = 0;
			}
		}
	}
	public OMesh getMesh(){
		return mesh;
	}
	public synchronized void setMesh(OMesh set){
		mesh = set;
	}
	@Override
	public synchronized void render(GL10 gl){
		OFrameSet Cfr = FrameSets.get(curSet);
		OFrameInfo Fr = Cfr.frameInfo.get(curFrame);
		//String timerName = Cfr.name+Id+"_"+curFrame;
		if(!clock.isSetTimer(Fr.Timer)){
			clock.setTimer(Fr.Timer, Fr.Time, Fr.Unit);
		}
		float tmr = clock.getTimer(Fr.Timer);
		if(tmr<=0){
			clock.removeTimer(Fr.Timer);
			curFrame=(curFrame+1>=Cfr.frameInfo.size())?((Cfr.loop)?0:curFrame):curFrame+1;
			clock.setTimer(Cfr.frameInfo.get(curFrame).Timer, Cfr.frameInfo.get(curFrame).Time, Cfr.frameInfo.get(curFrame).Unit);
		}
		mesh = stash.get(Cfr.frameInfo.get(curFrame).MID);
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
}
