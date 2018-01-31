package com.snakeinalake.engine.parser;

import java.io.InputStream;

import com.snakeinalake.engine.mesh.OMeshInfo;

import android.content.Context;

/*
 * The base class for future file parsers/readers
 * 
 * All that is required(for now, anyways) is a function that returns an instance of CMeshInfo
 * */

public class OMeshParser{
	protected String type;
	
	public OMeshParser(){
		type = "NULL";
	}	
	public OMeshInfo loadMesh(Context context, InputStream is){
		return null;
	}
	public OMeshInfo loadMesh(Context context, int FileID){
		return null;
	}
	public OMeshInfo loadMesh(Context context, String Filename){
		return null;
	}
	public String getType(){
		return type;		
	}
}
