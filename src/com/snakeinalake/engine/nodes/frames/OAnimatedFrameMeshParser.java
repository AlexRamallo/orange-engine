package com.snakeinalake.engine.nodes.frames;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.OTileset;
import com.snakeinalake.engine.mesh.OMesh;
import com.snakeinalake.engine.nodes.OAnimatedMeshSceneNode;
import com.snakeinalake.engine.ortho.ONode2DAnimatedTile;
import com.snakeinalake.engine.ortho.ONode2DTile;
import com.snakeinalake.engine.utilities.OUtilClock.IFC_UNIT;

import android.content.Context;
import android.util.Log;

public class OAnimatedFrameMeshParser{
	public static OAnimatedFrameMeshParser pInstance;
	public OrangeDevice device;
	
	public static OAnimatedFrameMeshParser getInstance(OrangeDevice Device){
		if(pInstance == null)
			pInstance = new OAnimatedFrameMeshParser(Device);
		return pInstance;
	}
	public OAnimatedFrameMeshParser(OrangeDevice Device){
		device = Device;
	}
	public OAnimatedMeshSceneNode loadAnimation(Context context, String file){
		OAnimatedMeshSceneNode ret = null;
		
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			TMXMLHandler handler = new TMXMLHandler();			
			xr.setContentHandler(handler);
			
			ret = new OAnimatedMeshSceneNode(device.getClock());
			handler.node = ret;
			
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
	
	private class TMXMLHandler implements ContentHandler{
		public OAnimatedMeshSceneNode node;
		OFrameSet build;
		//String frameBuild;
		//boolean buildingFrame;
		@Override
		public void characters(char[] arg0, int start, int length)
				throws SAXException {
			/*if(buildingFrame){
				for(int i=start; i<length; i++){
					frameBuild+=arg0[i];
				}
			}*/
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(localName.equalsIgnoreCase("frameset")){
				node.FrameSets.add(build);
				build = null;
			}/*else if(localName.equalsIgnoreCase("frame")){
				Log.d("OAFMP","\t\t\tDone Building; Inserting...");
				node.stash.add(
						new OMesh(device.getOBJParser().loadMesh(device.getContext(),
						new ByteArrayInputStream(frameBuild.getBytes()))));
				buildingFrame = false;
				frameBuild="";
			}*/
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
			
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			if(localName.equalsIgnoreCase("frame")){
				node.stash.add(
					new OMesh(device.getOBJParser().loadMesh(device.getContext(), atts.getValue("file"))));
				Log.d("OAFMP","\t\t\tFound frame..");
				//buildingFrame = true;
			}else if(localName.equalsIgnoreCase("frameset")){
				build = new OFrameSet(atts.getValue("name"));
			}else if(localName.equalsIgnoreCase("atf")){
				build.frameInfo.add(
						new OFrameInfo(
								Integer.parseInt(atts.getValue("id")),
								Float.parseFloat(atts.getValue("time")),
								getIFC(atts.getValue("u")),
								"OAnimatedFrameMeshTimer["+build.name+":"+build.frameInfo.size()+"]"+Double.toString(Math.random()*999999999))
				);
			}
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	} 
	private IFC_UNIT getIFC(String unit){
		if(unit.equalsIgnoreCase("miliseconds")){
			return IFC_UNIT.IFC_MILI;
		}else if(unit.equalsIgnoreCase("seconds")){
			return IFC_UNIT.IFC_SECONDS;
		}else if(unit.equalsIgnoreCase("minutes")){
			return IFC_UNIT.IFC_MINUTES;
		}else if(unit.equalsIgnoreCase("hours")){
			return IFC_UNIT.IFC_HOURS;
		}else return null;
	}
}













