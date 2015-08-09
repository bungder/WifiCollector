package com.gordon.exp.test.as.service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class ConfigGenerator {
	
	String path = "/mnt/sdcard/001Hello/conf.xml" ;
	
	public Map<String,String> getXMLContent(InputStream xml) throws Exception{
		Map<String, String> map = new LinkedHashMap<String, String>() ;
		XmlPullParser pull = Xml.newPullParser() ;
		pull.setInput(xml, "UTF-8") ;
		int event = pull.getEventType() ;
		while(event != XmlPullParser.END_DOCUMENT){
			switch(event){
			case XmlPullParser.START_DOCUMENT :
				break ;
			case XmlPullParser.START_TAG:
				map.put(pull.getName(), pull.nextText()) ;
				break ;
			case XmlPullParser.END_TAG :
				break ;
			}
			event = pull.next() ;
		}
		return map ;
	}

}
