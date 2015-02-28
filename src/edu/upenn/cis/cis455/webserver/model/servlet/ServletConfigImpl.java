package edu.upenn.cis.cis455.webserver.model.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import edu.upenn.cis.cis455.webserver.util.Parser;

public class ServletConfigImpl implements ServletConfig {
	
	private HashMap<String,String> servletParams;
	private String servletName;
	private ServletContext context;
	
	public ServletConfigImpl(ServletContext context,HashMap<String,String> servletParams,String servletName){
		this.context = context;
		this.servletName = servletName;
		this.servletParams = servletParams;
		
	}

	@Override
	public String getInitParameter(String arg0) {
		return servletParams.get(arg0);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(servletParams.keySet());
	}

	@Override
	public ServletContext getServletContext() {
		return this.context;
	}

	@Override
	public String getServletName() {
		return this.servletName;
	}

}
