package edu.upenn.cis.cis455.webserver.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Not used as of now
public class ResourceContext {

	private Map<String, String> mimeMap;
	private static ResourceContext context = null;
	
	private ResourceContext(){
		mimeMap = new HashMap<String, String>();
		initializeMime();
	}
	
	public static ResourceContext getInstance(){
		if(context == null)
			context = new ResourceContext();
		
		return context;
	}
	
	private void initializeMime(){
		
		Map<String,String> tempMap = new HashMap<>();
		
		tempMap.put(".xhtml", "application/xhtml+xml");
		tempMap.put(".html", "text/html");
		tempMap.put(".gif","image/gif");
		tempMap.put(".jpeg", "image/jpeg");
		tempMap.put(".jpg", "image/jpeg");
		tempMap.put(".js", "application/javascript");
		tempMap.put(".json","application/json");
		tempMap.put(".java", "text/x-java-source,java");
		tempMap.put(".pdf", "application/pdf");
		tempMap.put(".swf", "application/x-shockwave-flash");
		tempMap.put(".flv", "video/x-flv");	
		
		mimeMap = Collections.unmodifiableMap(tempMap);
		
	}
	
	public Map<String,String> getMimeMap(){
		
		return mimeMap;
	}
	
	
}
