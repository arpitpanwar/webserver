package edu.upenn.cis.cis455.webserver.server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.model.servlet.ServletConfigImpl;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Parser;
import edu.upenn.cis.cis455.webserver.util.Utils;


/**
 * Class for loading the servlet
 * @author cis455
 *
 */
public class ServletLoader {
	
	static final Logger LOG = Logger.getLogger(ServletLoader.class); 
	
	private String filepath;
	
	public ServletLoader(String path){
		this.filepath = path;
	}
	
		
	private HttpServlet load(String path,ServletConfig config){
		URLClassLoader classLoader;
		try{
			String basePath = Utils.getParentFolder(this.filepath);
			String classPath = generateClassPath(path);
			URL u = new URL("file://"+basePath+File.separator+"classes"+File.separator);
			URL[] classUrls = {u};
			classLoader = new URLClassLoader(classUrls);
			Class tempClass = classLoader.loadClass(path);
			HttpServlet servlet = (HttpServlet)tempClass.newInstance();
			servlet.init(config);
			classLoader.close();
			return servlet;
		}catch(ClassNotFoundException cnfe){
			LOG.debug("Unable to load class"+cnfe.getMessage());
		}catch(IllegalAccessException iae){
			LOG.debug("IlLegal Access: "+iae.getMessage());
		}catch(InstantiationException ie){
			LOG.debug("Unable to instantiate the servlet: "+ie.getMessage());
		}catch(ServletException se){
			LOG.debug("Servlet Exception while initialization: "+se.getMessage());
		}catch(MalformedURLException mfe){
			LOG.debug("Unable to load URL "+mfe.getMessage());
		}catch(IOException ioe){
			LOG.debug("Exception closing class loader "+ioe.getMessage());
		}
				
		return null;
		
	}
	
	public HashMap<String,HttpServlet> getServlets(ServletContext context,Parser parser){
		
		HashMap<String,HttpServlet> servlets = new HashMap<>();
		
		HashMap<String,String> servletMap = parser.getServlets();
		HashMap<String,HashMap<String, String>> servletParamsMap = parser.getServletParams();
		HashMap<String,String> servletMappingMap = parser.getServletMapping();
		
		for(Map.Entry<String, String> servlet: servletMap.entrySet()){
			
			String servletName = servlet.getKey();
			ServletConfig config = generateServletConfig(context, servletParamsMap.get(servletName), servletName);
			HttpServlet tempServlet = this.load(servlet.getValue().trim(),config);
			String servletPath=servletMappingMap.get(servletName);
			if(!servletPath.startsWith("/"))
				servletPath = "/"+servletPath;
			//if(servletPath.endsWith(Constants.MATCH_ALL))
			//	servletPath = servletPath.substring(0, servletPath.length()-2);
			if(tempServlet!=null)
				servlets.put(servletPath, tempServlet);
			
		}
				
		return servlets;
		
		
	}
	
	private ServletConfig generateServletConfig(ServletContext context,HashMap<String,String> servletParams,String servletName){
		
		ServletConfigImpl config = new ServletConfigImpl(context, servletParams, servletName);
		return config;
	}
	
	private String generateClassPath(String path){
		//edu.upenn.cis.cis455.CalculatorServlet
		StringBuilder sb = new StringBuilder();
		String[] tokens = path.split("\\.");
		for(int i=0;i<tokens.length-1;i++){
			sb.append(tokens[i]+File.separator);
		}
		return sb.toString();
	}
	
	private String getClassName(String path){
		String[] tokens = path.split("\\.");
		return tokens[tokens.length-1];
	}
	
}
