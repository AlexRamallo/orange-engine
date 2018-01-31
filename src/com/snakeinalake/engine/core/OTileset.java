package com.snakeinalake.engine.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.snakeinalake.engine.OrangeDevice;


public class OTileset implements Serializable{
	private static final long serialVersionUID = -2564743849508536891L;
	public float Width,Height,TileW,TileH;
	public String file,image;
	public float Tw,Th,TmL,TmT;
	public OTexture tex;
	private boolean loaded;
	
	public OTileset(String filename){
		file = filename;
		tex = new OTexture();
		loaded = false;
	}
	/**
	 * Loads the tile information required for rendering separate tiles
	 * */
	public void load(OrangeDevice device){
		if(loaded)
			return;
		loaded = true;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			OTilesetXMLHandler handler = new OTilesetXMLHandler();			
			xr.setContentHandler(handler);
			
			handler.tileset = this;
			
			xr.parse(new InputSource(device.getContext().getAssets().open(file)));
			
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
		loadTexture(device);
	}
	/**
	 * Loads the tile information required for rendering separate tiles
	 * */
	public void loadSC(OrangeDevice device){this.loadSC("",device);}
	public void loadSC(String workdir, OrangeDevice device){
		if(loaded)
			return;
		loaded = true;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			OTilesetXMLHandler handler = new OTilesetXMLHandler();			
			xr.setContentHandler(handler);
			
			handler.tileset = this;
			Log.d("OTileset","loadSC::FILE->"+workdir+file);
			xr.parse(new InputSource(new FileInputStream(workdir+file)));
			
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
		loadTexture(device);
	}
	
	private void loadTexture(OrangeDevice device){
		tex.setId(device.getTexLoader().loadTex(image));
	}
	
	private class OTilesetXMLHandler implements ContentHandler{
		public OTileset tileset;
		@Override
		public void characters(char[] ch, int start, int length)
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
			if(localName.equals("file")){
				tileset.Width = Float.parseFloat(atts.getValue("width"));
				tileset.Height = Float.parseFloat(atts.getValue("height"));
				tileset.TileW = Float.parseFloat(atts.getValue("tw"));
				tileset.TileH = Float.parseFloat(atts.getValue("th"));
				tileset.image = atts.getValue("name");
			}else if(localName.equals("tiles")){
				tileset.Tw = Float.parseFloat(atts.getValue("w"));
				tileset.Th = Float.parseFloat(atts.getValue("h"));
				tileset.TmL = Float.parseFloat(atts.getValue("left"));
				tileset.TmT = Float.parseFloat(atts.getValue("top"));
			}
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
}
