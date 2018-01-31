package com.snakeinalake.engine.mesh;

import javax.microedition.khronos.opengles.GL10;

import com.snakeinalake.engine.animation.skeletal.OBone;
import com.snakeinalake.engine.animation.skeletal.OEAInfo;
import com.snakeinalake.engine.core.OColor4f;
import com.snakeinalake.engine.core.OMatrix4f;
import com.snakeinalake.engine.core.OTriangle;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.utilities.TextureLoader;

/*
 * this class contains information for the renderer to be able to draw it as well as extra functions for optional
 * functionality which can later be implemented by creating new classes that inherit from it.
 * 
 * An example for this would be to add a function like getBone("head") if I want to add support for meshes with bones
 * in the future. Instead of adding that function to this class I can just create a child with the extra functionality
 * to keep everything clean and organized
 * */

public class OAnimatedMesh extends OMesh{
	public OEAInfo ainfo;
	public float frameloopStart,frameloopEnd,curFrame;
	public OAnimatedMesh(){
		info.vertBuff = null;
		info.IndBuff = null;
		info.vertices = null;
		info.indexlist = null;
		wire = false;
		debug = true;
		color = new OColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		frameloopStart=0;
		frameloopEnd=0;
		curFrame = 0;
	}
	/**
	 * Moves vertices in correspondence with the bones
	 * */
	public void skin(){
		//if(info.vertBuff.hasArray()){
			for(int i=0; i<ainfo.skeleton.bones.size(); i++){
				OBone b = ainfo.skeleton.bones.get(i);
				int numTris = info.tris.size();
				OTriangle chk = null;
					for(int u=0; u<numTris; u++){
						chk = info.tris.get(u);
						for(int p=0; p<b.indices.size(); p++){
							for(int v=0; v<3; v++){
								if(chk.indices[v] == b.indices.get(p)-1){
									int pos = 0;
									if(v == 0){
										pos = chk.Apos;
										OMatrix4f.transvec(chk.Av[0], chk.Av[1], chk.Av[2],
												b.Tip.getX(), b.Tip.getY(), b.Tip.getZ());
										info.vertBuff.put(pos,OMatrix4f.results[0]);
										info.vertBuff.put(pos+1,OMatrix4f.results[1]);
										info.vertBuff.put(pos+2,OMatrix4f.results[2]);
									}else if(v == 1){
										pos = chk.Bpos; 
										OMatrix4f.transvec(chk.Bv[0], chk.Bv[1], chk.Bv[2],
												b.Tip.getX(), b.Tip.getY(), b.Tip.getZ());
										info.vertBuff.put(pos,OMatrix4f.results[0]);
										info.vertBuff.put(pos+1,OMatrix4f.results[1]);
										info.vertBuff.put(pos+2,OMatrix4f.results[2]);
									}else if(v == 2){
										pos = chk.Cpos;
										OMatrix4f.transvec(chk.Cv[0], chk.Cv[1], chk.Cv[2],
												b.Tip.getX(), b.Tip.getY(), b.Tip.getZ());
										info.vertBuff.put(pos,OMatrix4f.results[0]);
										info.vertBuff.put(pos+1,OMatrix4f.results[1]);
										info.vertBuff.put(pos+2,OMatrix4f.results[2]);
									}

								}
									
							}
						}
					}
			}
		//}
	}
	public OAnimatedMesh(OMeshInfo MeshInfo, OEAInfo OEA){
		super(MeshInfo);
		ainfo = OEA;
		color = new OColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	@Override
	public void render(GL10 gl, vector3df pos, vector3df rot, vector3df scale){
		skin();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	   // gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, 
               // GL10.GL_REPLACE 
               // GL10.GL_MODULATE
               // );
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
		
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		
		gl.glLoadIdentity();
		gl.glTranslatef(pos.x, pos.y, pos.z);
		gl.glRotatef(rot.x, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rot.y, 0f, 1.0f, 0.0f);
		gl.glRotatef(rot.z, 0f, 0.0f, 1.0f);
		gl.glScalef(scale.x, scale.y, scale.z);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);		
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		if(texture != null){
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,texture.getName());
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, info.uvBuff);
		}
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, info.vertBuff);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, info.normBuff);
		
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glDrawElements((wire)?GL10.GL_LINE_LOOP:GL10.GL_TRIANGLES, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		/*if(debug == true){
			gl.glScalef(scale.x+0.5f, scale.y+0.5f, scale.z+0.5f);
			gl.glColor4f(1.0f, 0, 0, 1.0f);
			gl.glDrawElements(GL10.GL_LINE_LOOP, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		}*/
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	@Override
	public void render(GL10 gl, float px, float py, float pz, float rx, float ry, float rz, float sx, float sy, float sz){
		skin();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(blend[0], blend[1]);
		
		gl.glLoadIdentity();
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ry, 0f, 1.0f, 0.0f);
		gl.glRotatef(rz, 0f, 0.0f, 1.0f);
		gl.glScalef(sx, sy, sz);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);		
		if(!texture.isRetrieved()){
			texture = TextureLoader.getInstance().retrieve(texture.getId(), gl);
		}
		if(texture != null){
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,texture.getName());
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, info.uvBuff);
		}
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, info.vertBuff);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, info.normBuff);
		
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glDrawElements((wire)?GL10.GL_LINE_LOOP:GL10.GL_TRIANGLES, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		/*if(debug == true){
			gl.glScalef(sx+0.5f, sy+0.5f, sz+0.5f);
			gl.glColor4f(1.0f, 0, 0, 1.0f);
			gl.glDrawElements(GL10.GL_LINE_LOOP, info.numFaces * 3, GL10.GL_UNSIGNED_SHORT, info.IndBuff);
		}*/
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

}














