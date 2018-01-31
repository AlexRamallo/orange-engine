package com.snakeinalake.engine.text;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.snakeinalake.engine.core.rect2df;
import com.snakeinalake.engine.text.OFont.OFCharacter;

import android.content.Context;

public class OFontXMLParser{	
	private static OFontXMLParser pInstance;
	/**
	 * get the instance of OFontXMLParser. Since it is a singleton it cannot be instantiated manually
	 * */
	public static OFontXMLParser getInstance(){
		if(pInstance == null){
			pInstance = new OFontXMLParser();
		}
		return pInstance;
	}
	
	private OFontXMLParser(){
		//--
	}
	
	private class fontXMLHandler implements ContentHandler{
		public OFont font;
		private HashMap<Character, OFCharacter> characters;
		
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
			characters = font.getCharacters();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			if(localName.equalsIgnoreCase("font")){
				font.name = atts.getValue("name");
				
			}else if(localName.equalsIgnoreCase("texture")){
				font.hasAlpha = (atts.getValue("hasAlpha").equalsIgnoreCase("true"))?true:false;
				font.texture = atts.getValue("filename");
				font.setBWidth(Float.parseFloat(atts.getValue("width")));
				font.setBHeight(Float.parseFloat(atts.getValue("height")));
			}else if(localName.equalsIgnoreCase("c")){
				rect2df mapping = new rect2df();
				char[] mapdat = atts.getValue("r").toCharArray();
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
							mapping.X = Integer.parseInt(build);
						else if(stage == 1)
							mapping.Y = Integer.parseInt(build);
						else if(stage == 2)
							mapping.W = Integer.parseInt(build)-mapping.X;
						else if(stage == 3){
							mapping.H = Integer.parseInt(build)-mapping.Y;
							
							/*
							 * Record maximum and minimum character sizes
							 * */
							float height = mapping.H;
							float width = mapping.W;
							
							if(font.getMinHeight() == -1 || font.getMinHeight()>height)
								font.setMinHeight(height);
							
							if(font.getMinWidth() == -1 || font.getMinWidth()>width)
								font.setMinWidth(width);
							
							if(font.getMaxHeight()<height)
								font.setMaxHeight(height);
							
							if(font.getMaxWidth()<width)
								font.setMaxWidth(width);						
							/*****//*****//*****//*****//*****//*****//*****/
						}
						
						stage++;
						build = "";
					}
				}
				String C = atts.getValue("c");
				if(C.equalsIgnoreCase("NULL"))
					characters.put('\0', font.new OFCharacter(C.toCharArray()[0],mapping));
				else
					characters.put(C.toCharArray()[0], font.new OFCharacter(C.toCharArray()[0],mapping));
			}
			
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * Load a font from a font XML file
	 * @param file The file to load
	 * @return an instance of OFont
	 * */
	public OFont loadFont(Context context, String file){
		OFont ret = null;
		
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
		try {
			sp = spf.newSAXParser();			
			XMLReader xr = sp.getXMLReader();			
			fontXMLHandler handler = new fontXMLHandler();			
			xr.setContentHandler(handler);
			
			ret = new OFont(0,0,0,-1,0,-1);
			handler.font = ret;
			
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
