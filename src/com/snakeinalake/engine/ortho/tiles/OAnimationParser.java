package com.snakeinalake.engine.ortho.tiles;

import java.io.FileInputStream;
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

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.utilities.OUtilClock.IFC_UNIT;

import android.content.Context;

public class OAnimationParser{
	public static OAnimationParser pInstance;
	public OrangeDevice device;
	private String workdir;
	
	public static OAnimationParser getInstance(OrangeDevice Device){
		if(pInstance == null)
			pInstance = new OAnimationParser(Device);
		if(pInstance.device == null)
			pInstance.device = Device;
		return pInstance;
	}
	public static OAnimationParser getInstance(){
		if(pInstance == null)
			pInstance = new OAnimationParser(null);
		return pInstance;
	}
	public OAnimationParser(OrangeDevice Device){
		device = Device;
		workdir="";
	}
	/**Set the working directory*/
	public void setWorkDir(String set){
		workdir = set;
		if(workdir.charAt(workdir.length()-1)!='/'){
			workdir+="/";
		}
	}
	/**Load an animation from internal*/
	public ONode2DAnimatedTile_helper_importinfo loadAnimation(Context context, String file){
		Vector<OAnimSet> vecret;
		ONode2DAnimatedTile_helper_importinfo ret = null;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			TMXMLHandler handler = new TMXMLHandler();			
			xr.setContentHandler(handler);
			
			ret = new ONode2DAnimatedTile_helper_importinfo();
			vecret = new Vector<OAnimSet>();
			handler.Bframes = vecret;
			xr.parse(new InputSource(context.getAssets().open(file)));
			ret.frames = vecret; 
			ret.repeat = handler.repeat;
			ret.dframes = handler.Bdframes;
			
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
	/**Load an animation from external*/
	public ONode2DAnimatedTile_helper_importinfo loadAnimation(String file){
		Vector<OAnimSet> vecret;
		ONode2DAnimatedTile_helper_importinfo ret = null;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			TMXMLHandler handler = new TMXMLHandler();			
			xr.setContentHandler(handler);
			
			ret = new ONode2DAnimatedTile_helper_importinfo();
			vecret = new Vector<OAnimSet>();
			handler.Bframes = vecret;
			xr.parse(new InputSource(new FileInputStream(workdir+file)));
			ret.frames = vecret;
			ret.repeat = handler.repeat;
			ret.dframes = handler.Bdframes;
			
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
		Vector<OAnimSet> Bframes;
		Vector<OAnimatedTileFrame> Bdframes;
		boolean repeat;
		OAnimSet asBuild,asDefault;
		public TMXMLHandler(){
			asDefault=new OAnimSet();
			asDefault.loop = false;
			asDefault.name = "default";
		}
		@Override
		public void characters(char[] arg0, int arg1, int arg2)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			
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
			if(localName.equalsIgnoreCase("Animation")){
				String rep=atts.getValue("repeat");
				if(rep!=null){
					repeat = rep.equalsIgnoreCase("true") || rep.equalsIgnoreCase("yes");
				}
			}else
			if(localName.equalsIgnoreCase("animset")){
				asBuild = new OAnimSet();
				if(atts.getValue("loop")!=null)
					asBuild.loop = Boolean.parseBoolean(atts.getValue("loop"));
				else
					asBuild.loop = false;
				asBuild.name = atts.getValue("name");
			}else
			if(localName.equalsIgnoreCase("atf")){
				if(Bframes == null)
					Bframes = new Vector<OAnimSet>();
				if(Bdframes == null)
					Bdframes = new Vector<OAnimatedTileFrame>();
				OAnimatedTileFrame f = new OAnimatedTileFrame();
				int TileX = 0, TileY = 0;				
				char[] mapdat = atts.getValue("id").toCharArray();
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
							TileX = Integer.parseInt(build);
						else if(stage == 1)
							TileY = Integer.parseInt(build);
						stage++;
						build = "";
					}
				}
				f.fX = TileX;
				f.fY = TileY;
				f.time = Float.parseFloat(atts.getValue("time"));
				f.timer = "AnimatedTileFrameTimer["+Bframes.size()+"]#"+Double.toString(Math.random()*5467);
				String u = atts.getValue("u");
				if(u.equalsIgnoreCase("seconds")){
					f.unit = IFC_UNIT.IFC_SECONDS;
				}else if(u.equalsIgnoreCase("miliseconds")){
					f.unit = IFC_UNIT.IFC_MILI;
				}else if(u.equalsIgnoreCase("minutes")){
					f.unit = IFC_UNIT.IFC_MINUTES;
				}else if(u.equalsIgnoreCase("hours")){
					f.unit = IFC_UNIT.IFC_HOURS;
				}
				Bdframes.add(f);
				if(asBuild==null)
					asBuild = asDefault;
				asBuild.frames.add(f);
			}
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(localName.equalsIgnoreCase("animset")){
				Bframes.add(asBuild);
				asBuild=null;
			}
		}
		
	}
}













