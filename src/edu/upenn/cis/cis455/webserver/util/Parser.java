package edu.upenn.cis.cis455.webserver.util;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class Parser {
	Logger LOG = Logger.getLogger(Parser.class);
	private Handler handler;
	
	class Handler extends DefaultHandler {
		
		
		public HashMap<String,String> getContextParams(){
			return m_contextParams;
		}
		
		public HashMap<String, String> getServlets(){
			return m_servlets;
		}
		
		public HashMap<String, HashMap<String,String>> getServletParams(){
			return m_servletParams;
		}
		
		public HashMap<String,String> getServletMapping(){
			return m_servlet_mapping;
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.compareTo("servlet-name") == 0) {
				m_state = 1;
			} else if (qName.compareTo("servlet-class") == 0) {
				m_state = 2;
			} else if (qName.compareTo("context-param") == 0) {
				m_state = 3;
			} else if (qName.compareTo("init-param") == 0) {
				m_state = 4;
			} else if (qName.compareTo("param-name") == 0) {
				m_state = (m_state == 3) ? 10 : 20;
			} else if (qName.compareTo("param-value") == 0) {
				m_state = (m_state == 10) ? 11 : 21;
			}else if (qName.compareTo("url-pattern")==0){
				m_state = 5;
			}
		}
		public void characters(char[] ch, int start, int length) {
			String value = new String(ch, start, length).trim();
			if(value.length()==0)
					return;
			if (m_state == 1) {
				m_servletName = value;
				m_state = 0;
			} else if (m_state == 2) {
				m_servlets.put(m_servletName, value);
				m_state = 0;
			} else if (m_state == 10 || m_state == 20) {
				m_paramName = value;
			} else if (m_state == 11) {
				if (m_paramName == null) {
					System.err.println("Context parameter value '" + value + "' without name");
					System.exit(-1);
				}
				m_contextParams.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			} else if (m_state == 21) {
				if (m_paramName == null) {
					System.err.println("Servlet parameter value '" + value + "' without name");
					System.exit(-1);
				}
				HashMap<String,String> p = m_servletParams.get(m_servletName);
				if (p == null) {
					p = new HashMap<String,String>();
					m_servletParams.put(m_servletName, p);
				}
				p.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			}else if (m_state ==5){
				m_servlet_mapping.put(m_servletName, value);
				m_state = 0;
			}
		}

		private int m_state = 0;
		private String m_servletName;
		private String m_paramName;
		 HashMap<String,String> m_servlets = new HashMap<String,String>();
		 HashMap<String,String> m_contextParams = new HashMap<String,String>();
		 HashMap<String,HashMap<String,String>> m_servletParams = new HashMap<String,HashMap<String,String>>();
		HashMap<String,String> m_servlet_mapping = new HashMap<String, String>();
		
	}
		
	private Handler parseWebdotxml(String webdotxml) throws Exception {
		Handler h  = new Handler();
		File file = new File(webdotxml);
		if (file.exists() == false) {
			System.err.println("error: cannot find " + file.getPath());
			System.exit(-1);
		}
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(file, h);
		
		return h;
	}
	
	
	public void parse(String webXml){
		
		try{
			
			this.handler = parseWebdotxml(webXml);
				
		}catch(Exception e){
			LOG.debug("Exception while parsing: "+e.getMessage());
		}
	}
	
	public HashMap<String,String> getContextParams(){
		return handler.getContextParams();
	}
	
	public HashMap<String, String> getServlets(){
		return handler.getServlets();
	}
	
	public HashMap<String, HashMap<String,String>> getServletParams(){
		return handler.getServletParams();
	}
	
	public HashMap<String,String> getServletMapping(){
		return handler.getServletMapping();
	}
	
}
