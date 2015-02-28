package edu.upenn.cis.cis455.webserver.model.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Parser;

public class ServletContextImpl implements ServletContext {
	static final Logger LOG = Logger.getLogger(ServletContextImpl.class);
	 
	private String basePath;
	private HashMap<String,Object> attributeMap;
	private HashMap<String,String> contextParams;
	
	public ServletContextImpl(Parser parser,String basePath) {
			attributeMap = new HashMap<String, Object>();
			contextParams = parser.getContextParams();
			this.basePath = basePath;
	}
	

	@Override
	public Object getAttribute(String arg0) {
		
		return attributeMap.get(arg0);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attributeMap.keySet());
	}

	@Override
	public ServletContext getContext(String arg0) {
		return this;
	}

	@Override
	public String getInitParameter(String arg0) {
		return contextParams.get(arg0);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(contextParams.keySet());
	}

	@Override
	public int getMajorVersion() {
		return 2;
	}

	@Override
	public String getMimeType(String arg0) {
		String mimeType=null;
		try{
			mimeType = Files.probeContentType(new File(arg0).toPath());
		}catch(IOException ioe){
			LOG.debug("IOException during getting mime type: "+ioe.getMessage());
		}
		return mimeType;
	}

	@Override
	public int getMinorVersion() {
		return 4;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		
		return this.basePath+File.separator+arg0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		return null;
	}

	@Override
	public Set getResourcePaths(String arg0) {
		return null;
	}

	@Override
	public String getServerInfo() {
		return Constants.SERVER_INFO;
	}

	@Override
	@Deprecated
	public Servlet getServlet(String arg0) throws ServletException {
		return null;
	}

	@Override
	public String getServletContextName() {
		return Constants.DEFAULT_CONTEXT;
	}

	@Override
	@Deprecated
	public Enumeration getServletNames() {
		return null;
	}

	@Override
	public Enumeration getServlets() {
		return null;
	}

	@Override
	public void log(String arg0) {
			
	}

	@Override
	public void log(Exception arg0, String arg1) {

	}

	@Override
	public void log(String arg0, Throwable arg1) {

	}

	@Override
	public void removeAttribute(String arg0) {
		attributeMap.remove(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributeMap.put(arg0, arg1);
	}

}
