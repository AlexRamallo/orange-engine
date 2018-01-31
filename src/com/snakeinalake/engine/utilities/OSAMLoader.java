package com.snakeinalake.engine.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.snakeinalake.engine.OSceneManager;
import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.nodes.OMeshSceneNode;

public class OSAMLoader {
	private static OSAMLoader pInstance = null;
	
	public static OSAMLoader getInstance(){
		if(pInstance == null){
			pInstance = new OSAMLoader();
		}
		return pInstance;
	}
	
	public void loadSAM(Context context, OSceneManager smgr, String filename){
		vector2df grid = new vector2df(0,0);
		vector3df cursor = new vector3df(0,0,0);
		AssetManager am = context.getAssets();
		String mesh = "models/block.obj",tex = "textures/blocktex.png";
		try {
			InputStream is = am.open(filename);
			InputStreamReader rd = new InputStreamReader(is);
			
			int data = 0;
			int found = 0;
			String buildWord = "";
			while(data!=-1){	
				char c = (char)data;
				if(c == 's'){
					found = 1;
					Log.d("OSAMLoader", "FOUND 'S'");
					data = rd.read();
				}
				if(c == '.'){		
					cursor.x+=grid.x;
					found = 2;
				}
				if(c == '\n'){
					cursor.y+=grid.y;
					cursor.x = 0;
				}
				if(found == 2){
					OMeshSceneNode node = smgr.createMeshSceneNode(mesh);
					node.getMesh().loadTexture(am.open(tex));
					node.setPosition(cursor);
				}
				if(found == 1){
					buildWord = "";
					do{
						c = (char)data;
						data = rd.read();
						buildWord+=c;
					}while(c!='\n' && c!='x');
					grid.x=Integer.parseInt(buildWord.trim());
					buildWord = "";
					do{
						c = (char)data;
						data = rd.read();
						buildWord+=c;
					}while(c!='\n');
					grid.y=Integer.parseInt(buildWord.trim());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("OSAMLoader", "ERROR LOADING SAM FILE");
		}
	}
}
