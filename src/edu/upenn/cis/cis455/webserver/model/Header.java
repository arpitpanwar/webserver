package edu.upenn.cis.cis455.webserver.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
public class Header {
	private HashMap<String,ArrayList<String>> headers;
	
	public Header(){
		headers = new HashMap<>();
		setConnection("close");
		setContentType("text/html");
	}
	
	public Iterator<Map.Entry<String, ArrayList<String>>> getIterator(){
		return headers.entrySet().iterator();
	}
	
	public void addHeader(String name,String value){
		ArrayList<String> tempList;
		if(headers.containsKey(name))
			tempList = headers.get(name);
		else
			tempList = new ArrayList<String>();
		
		tempList.add(value);
			
		headers.put(name, tempList);
	}
	
	public void addCookie(Cookie c){
		
		ArrayList<String> tempList;
		if(headers.containsKey(HeaderConstants.HEADER_SET_COOKIE))
			tempList = headers.get(HeaderConstants.HEADER_SET_COOKIE);
		else
			tempList = new ArrayList<String>();
		
		String val = c.getName()+"="+c.getValue();
		tempList.add(val);
		
		headers.put(HeaderConstants.HEADER_SET_COOKIE, tempList);
		
	}
	
	public void setHeader(String name,String value){
		ArrayList<String> tempList = new  ArrayList<String>();
		tempList.add(value);
		
		headers.put(name, tempList);
	}
	
	public String getHeader(String name){
		String value=null;
		if(headers.containsKey(name))
			value = headers.get(name).get(0);
		
		return value;
	}
	
	public List<String> getHeaders(String name){
		List<String> value=null;
		if(headers.containsKey(name))
			value = headers.get(name);
		
		return value;
	}
	
	public void setContentType(String type){
		ArrayList<String> tempList = new  ArrayList<String>();
		tempList.add(type);
		headers.put(HeaderConstants.HEADER_CONTENT_TYPE, tempList);
		
	}
	
	public void setContentLength(long length){
		
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add(String.valueOf(length));
				
		headers.put(HeaderConstants.HEADER_CONTENT_LENGTH, tempList);
		
	}
	
	public void setConnection(String connection){
		
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add(connection);
		
		headers.put(HeaderConstants.HEADER_CONNECTION, tempList);
		
	}
	
	public void setDate(String date){
		
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add(date);
		
		headers.put(HeaderConstants.HEADER_DATE, tempList);
		
	}
	
	public boolean contains(String header){
		
		return headers.containsKey(header);
	
	}
	
	public Enumeration<String> getHeaderNames(){
		
		return Collections.enumeration(headers.keySet());
		
	}
	

}
