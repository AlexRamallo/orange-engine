package com.snakeinalake.engine.parser;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.snakeinalake.engine.animation.skeletal.OAnimationList;
import com.snakeinalake.engine.animation.skeletal.OBone;
import com.snakeinalake.engine.animation.skeletal.OEAInfo;
import com.snakeinalake.engine.animation.skeletal.OKeyFrame;
import com.snakeinalake.engine.animation.skeletal.OSkeleton;
import com.snakeinalake.engine.animation.skeletal.OKeyFrame.transformPacket;
import com.snakeinalake.engine.core.vector3df;

import android.content.Context;

public class OEAParser{
	private static OEAParser pInstance;
	public static OEAParser getInstance(){
		if(pInstance == null){
			pInstance = new OEAParser();
		}
		return pInstance;
	}
	private OEAParser(){
		//--
	}
	
	private class OEAXMLHandler implements ContentHandler{
		public OEAInfo info;
		Vector<OBone> tree;
		OKeyFrame keybuild;
		transformPacket pBuild;
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			tree.clear();
			keybuild = null;
			pBuild = null;
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(localName.equals("Bone")){
				tree.remove(tree.size()-1);
			}else if(localName.equals("KeyFrame")){
				info.animation.frames.add(keybuild);
			}
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDocumentLocator(Locator locator) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			tree = new Vector<OBone>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			if(localName.equals("Mesh")){
				info.skeleton = new OSkeleton();
				info.meshname = atts.getValue("filename");
			}else if(localName.equals("Bone")){
				OBone add = new OBone();
				add.name = atts.getValue("name");
				/*LOAD ORIGIN INFO*/
				String sO = atts.getValue("O");
				if(sO == null){
					OBone endoflist =tree.get(tree.size()-1);
					add.Origin = new vector3df();
					add.Origin.x = endoflist.Tip.x;
					add.Origin.y = endoflist.Tip.y;
					add.Origin.z = endoflist.Tip.z;
					add.Origin.setParent(endoflist.Tip);
				}else{
					add.Origin = new vector3df();
					char[] mapdat = atts.getValue("O").toCharArray();
					int stage = 0;
					String build = "";
					for(int i=0; i<mapdat.length; i++){
						boolean done;
						if(mapdat[i] != ',' && mapdat[i]!=' '){
							build+=mapdat[i];
							done = (i+1==mapdat.length);
						}else
							done = true;
						if(done && build.length()>0){
							if(stage == 0)
								add.Origin.x = Integer.parseInt(build);
							if(stage == 1)
								add.Origin.y = Integer.parseInt(build);
							if(stage == 2)
								add.Origin.z = Integer.parseInt(build);
							stage++;
							build = "";
						}
					}
				}
				/*LOAD TIP INFO*/		
					add.Tip = new vector3df();
					char[] mapdat = atts.getValue("T").toCharArray();
					int stage = 0;
					String build = "";
					for(int i=0; i<mapdat.length; i++){
						boolean done;
						if(mapdat[i] != ',' && mapdat[i]!=' '){
							build+=mapdat[i];
							done = (i+1==mapdat.length);
						}else
							done = true;
						if(done && build.length()>0){
							if(stage == 0)
								add.Tip.x = Integer.parseInt(build);
							if(stage == 1)
								add.Tip.y = Integer.parseInt(build);
							if(stage == 2)
								add.Tip.z = Integer.parseInt(build);
							stage++;
							build = "";
						}
					}				
				info.skeleton.bones.add(add);
				if(tree.size() == 0){
					add.father = null;
					add.Tip.setParent(add.Origin);
					tree.add(add);
				}else{
					add.father = tree.get(tree.size()-1);
					if(add.father.children == null)
						add.father.children = new Vector<OBone>();
					add.Origin.setParent(add.father.Tip);
					add.Tip.setParent(add.Origin);
					add.father.children.add(add);
					tree.add(add);
				}
			}else if(localName.equals("v")){
				tree.get(tree.size()-1).indices.add(Integer.parseInt(atts.getValue("v")));
			}else if(localName.equals("Animation")){
				info.animation = new OAnimationList();
				if(atts.getValue("interpolation").equals("linear"))
					info.animation.interpolation = 1;
			}else if(localName.equals("KeyFrame")){
				keybuild = new OKeyFrame(Integer.parseInt(atts.getValue("time")));
				pBuild = keybuild.new transformPacket();
				pBuild.absolute = new boolean[]{false, false, false};
			}else if(localName.equals("Position")){
				if(pBuild == null){
					pBuild = keybuild.new transformPacket();
					pBuild.absolute = new boolean[]{false, false, false};
				}
				
				String searchName = atts.getValue("targ");
				for(int i=0; i<info.skeleton.bones.size(); i++){
					if(info.skeleton.bones.get(i).name == searchName){
						pBuild.target = info.skeleton.bones.get(i);
					}
				}	
				char[] mapdat = atts.getValue("v").toCharArray();
				int stage = 0;
				String build = "";
				for(int i=0; i<mapdat.length; i++){
					boolean done;
					if(mapdat[i] != ',' && mapdat[i]!=' '){
						build+=mapdat[i];
						done = (i+1==mapdat.length);
					}else
						done = true;
					if(done && build.length()>0){
						if(stage == 0)
							pBuild.pX = Float.parseFloat(build);
						if(stage == 1)
							pBuild.pY = Float.parseFloat(build);
						if(stage == 2)
							pBuild.pZ = Float.parseFloat(build);
						stage++;
						build = "";
					}
				}
				pBuild.absolute[0] = atts.getValue("abs").equals("true");
				keybuild.transforms.add(pBuild);
				pBuild = null;
			}else if(localName.equals("Rotation")){
				if(pBuild == null){
					pBuild = keybuild.new transformPacket();
					pBuild.absolute = new boolean[]{false, false, false};
				}
				String searchName = atts.getValue("targ");
				for(int i=0; i<info.skeleton.bones.size(); i++){
					if(info.skeleton.bones.get(i).name == searchName){
						pBuild.target = info.skeleton.bones.get(i);
					}
				}
				char[] mapdat = atts.getValue("v").toCharArray();
				int stage = 0;
				String build = "";
				for(int i=0; i<mapdat.length; i++){
					boolean done;
					if(mapdat[i] != ',' && mapdat[i]!=' '){
						build+=mapdat[i];
						done = (i+1==mapdat.length);
					}else
						done = true;
					if(done && build.length()>0){
						if(stage == 0)
							pBuild.rX = Float.parseFloat(build);
						if(stage == 1)
							pBuild.rY = Float.parseFloat(build);
						if(stage == 2)
							pBuild.rZ = Float.parseFloat(build);
						stage++;
						build = "";
					}
				}
				pBuild.absolute[1] = atts.getValue("abs").equals("true");
				keybuild.transforms.add(pBuild);
				pBuild = null;
			}else if(localName.equals("Scale")){
				if(pBuild == null){
					pBuild = keybuild.new transformPacket();
					pBuild.absolute = new boolean[]{false, false, false};
				}
				String searchName = atts.getValue("targ");
				for(int i=0; i<info.skeleton.bones.size(); i++){
					if(info.skeleton.bones.get(i).name == searchName){
						pBuild.target = info.skeleton.bones.get(i);
					}
				}
				char[] mapdat = atts.getValue("v").toCharArray();
				int stage = 0;
				String build = "";
				for(int i=0; i<mapdat.length; i++){
					boolean done;
					if(mapdat[i] != ',' && mapdat[i]!=' '){
						build+=mapdat[i];
						done = (i+1==mapdat.length);
					}else
						done = true;
					if(done && build.length()>0){
						if(stage == 0)
							pBuild.sX = Float.parseFloat(build);
						if(stage == 1)
							pBuild.sY = Float.parseFloat(build);
						if(stage == 2)
							pBuild.sZ = Float.parseFloat(build);
						stage++;
						build = "";
					}
				}
				pBuild.absolute[2] = atts.getValue("abs").equals("true");
				keybuild.transforms.add(pBuild);
				pBuild = null;
			}
			
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public OEAInfo loadOEAFile(Context context, String file){
		OEAInfo ret = null;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			OEAXMLHandler handler = new OEAXMLHandler();			
			xr.setContentHandler(handler);
			
			ret = new OEAInfo();
			handler.info = ret;
			
			xr.parse(new InputSource(context.getAssets().open(file)));
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return ret;
	}
}
