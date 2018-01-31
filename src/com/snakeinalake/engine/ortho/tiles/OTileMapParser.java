package com.snakeinalake.engine.ortho.tiles;

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
import com.snakeinalake.engine.core.OTileset;
import com.snakeinalake.engine.ortho.ONode2DAnimatedTile;
import com.snakeinalake.engine.ortho.ONode2DTile;
import com.snakeinalake.engine.utilities.OUtilClock.IFC_UNIT;

import android.content.Context;
import android.util.Log;

public class OTileMapParser{
	public static OTileMapParser pInstance;
	public OrangeDevice device;
	
	public static OTileMapParser getInstance(OrangeDevice Device){
		if(pInstance == null)
			pInstance = new OTileMapParser(Device);
		return pInstance;
	}
	public OTileMapParser(OrangeDevice Device){
		device = Device;
	}
	public OTileMap loadTileMap(Context context, String file){
		OTileMap ret = null;
		
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			TMXMLHandler handler = new TMXMLHandler();			
			xr.setContentHandler(handler);
			
			ret = new OTileMap(device);
			handler.map = ret;
			ret.info = ret.new Information();
			
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
		public OTileMap map;
		Vector<OAnimatedTileFrame> Bframes;
		float Bx,By,Bw,Bh;
		String Bts;
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
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(localName.equals("at")){
				Log.d("OrangeEngine", "ONode2DAnimatedTile:: Bx:"+Bx+",By:"+By+",Bw:"+Bw+",Bh"+Bh);
				ONode2DAnimatedTile at = device.getOrthoEnvironment().addNode2DAnimatedTile(
						map.tilesets.get(Bts), Bframes, Bw, Bh);
				at.setPosition(Bx,By);
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
			
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			if(localName.equals("MapName")){
				map.info.name = atts.getValue("value");
			}else if(localName.equals("CreationDate")){
				map.info.date = atts.getValue("date");
			}else if(localName.equals("Editor")){
				map.info.editor = atts.getValue("value");
				map.info.edver = atts.getValue("version");
			}else if(localName.equals("License")){
				map.info.license = atts.getValue("type");
			}else if(localName.equals("Author")){
				map.info.author = atts.getValue("name");
				map.info.org = atts.getValue("organization");
			}else if(localName.equals("Email")){
				map.info.contact.add(atts.getValue("value"));
			}else if(localName.equals("Phone")){
				map.info.contact.add(atts.getValue("value"));
			}else if(localName.equals("Website")){
				map.info.contact.add(atts.getValue("value"));
			}else if(localName.equals("Tileset")){
				map.tilesets.put(atts.getValue("id"),new OTileset(atts.getValue("file")));
				map.tilesets.get(atts.getValue("id")).load(device);
			}else if(localName.equals("t")){
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
				Log.d("OrangeEngine", "loadtilemap; TileX: "+TileX+", TileY: "+TileY);
				float width = Float.parseFloat(atts.getValue("w")), height = Float.parseFloat(atts.getValue("h"));
				ONode2DTile tile = device.getOrthoEnvironment().addNode2DTile(
						map.tilesets.get(atts.getValue("ts")), TileX, TileY, width, height);
				tile.setPosition(
						Float.parseFloat(atts.getValue("x")),
						Float.parseFloat(atts.getValue("y"))
				);
				map.tiles.add(tile);
			}else if(localName.equals("at")){
				Bts = atts.getValue("ts");
				Bx = Float.parseFloat(atts.getValue("x"));
				By = Float.parseFloat(atts.getValue("y"));
				Bw = Float.parseFloat(atts.getValue("w"));
				Bh = Float.parseFloat(atts.getValue("h"));
			}else if(localName.equals("atf")){
				if(Bframes == null)
					Bframes = new Vector<OAnimatedTileFrame>();
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
				f.timer = "34637867AnimatedTileFrameTimer["+Bframes.size()+"]#"+Double.toString(Math.random()*5467);
				String u = atts.getValue("u");
				if(u.equals("seconds")){
					f.unit = IFC_UNIT.IFC_SECONDS;
				}else if(u.equals("miliseconds")){
					f.unit = IFC_UNIT.IFC_MILI;
				}else if(u.equals("minutes")){
					f.unit = IFC_UNIT.IFC_MINUTES;
				}else if(u.equals("hours")){
					f.unit = IFC_UNIT.IFC_HOURS;
				}
				Bframes.add(f);
			}
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
}













