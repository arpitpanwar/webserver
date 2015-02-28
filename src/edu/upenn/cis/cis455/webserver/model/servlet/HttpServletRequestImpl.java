package edu.upenn.cis.cis455.webserver.model.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.model.HeaderConstants;
import edu.upenn.cis.cis455.webserver.model.Request;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.ServletInputStreamImpl;
import edu.upenn.cis.cis455.webserver.util.Utils;

public class HttpServletRequestImpl implements HttpServletRequest {
	
	private Request request;
	private String characterEncoding = Constants.DEFAULT_ENCODING;
	private String contextPath;
	private HashMap<String,Object> attributeMap;
	private HashMap<String,String> paramMap;
	private ServerContext context;

	public HttpServletRequestImpl(Request request,ServerContext context,String contextPath) {
		this.request = request;
		this.context = context;
		this.contextPath = contextPath;
		this.paramMap = request.getParameterMap();
		this.attributeMap = new HashMap<String, Object>();
	}
	
	@Override
	public Object getAttribute(String arg0) {
		return attributeMap.get(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(this.attributeMap.keySet());
	}

	@Override
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	@Override
	public int getContentLength() {
		return -1;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStreamImpl(request.getInputStream());
	}

	@Override
	public String getLocalAddr() {
		
		return request.getLocalAddress().getAddress().getHostAddress();
	}

	@Override
	public String getLocalName() {
		return request.getLocalAddress().getAddress().getHostName();
	}

	@Override
	public int getLocalPort() {
		return request.getLocalAddress().getPort();
	}

	@Override
	public Locale getLocale() {
		
		Locale loc = Locale.getDefault();
		
		if(request.getHeaders().contains(HeaderConstants.HEADER_ACCEPT_LANGUAGE)){
			String locale = request.getHeaders().getHeader(HeaderConstants.HEADER_ACCEPT_LANGUAGE);
			locale = locale.split(";")[0];
			loc = new Locale(locale.split("-")[0],locale.split("-")[1]);
		}
		
		return loc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getLocales() {
		List<Locale> locales = new ArrayList<Locale>();
		locales.add(Locale.getDefault());
		return Collections.enumeration(locales);
	}

	@Override
	public String getParameter(String arg0) {
	
		return this.paramMap.get(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getParameterMap() {
		return this.paramMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getParameterNames() {
		return Collections.enumeration(this.paramMap.keySet());
	}

	@Override
	public String[] getParameterValues(String arg0) {
		
		return (String[])this.paramMap.values().toArray();
	}

	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(request.getInputStream()));
	}

	@Override
	@Deprecated
	public String getRealPath(String arg0) {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		
		return request.getRemoteAddress().getAddress().getHostAddress();
	}

	@Override
	public String getRemoteHost() {
		return request.getRemoteAddress().getHostName();
	}

	@Override
	public int getRemotePort() {
		return request.getRemoteAddress().getPort();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {

		return null;
	}

	@Override
	public String getScheme() {
		
		return Constants.DEFAULT_SCHEME;
	}

	@Override
	public String getServerName() {
		return context.getLocalAddress().getHostName();
	}

	@Override
	public int getServerPort() {
		
		return this.request.getLocalAddress().getPort();
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		
		if(attributeMap.containsKey(arg0))
			attributeMap.remove(arg0);
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributeMap.put(arg0,arg1);
		
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		if(!Utils.isCharsetSupported(arg0))
			throw new UnsupportedEncodingException();
		this.characterEncoding = arg0;
		
	}

	@Override
	public String getAuthType() {
		return BASIC_AUTH;
	}

	@Override
	public String getContextPath() {
		return Constants.DEFAULT_CONTEXT;
	}

	@Override
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	@Override
	public long getDateHeader(String arg0) {
		
		return Utils.getTimeFromString(request.getHeaders().getHeader(arg0));
	}

	@Override
	public String getHeader(String arg0) {
		
		return request.getHeaders().getHeader(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaderNames() {
		return request.getHeaders().getHeaderNames();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaders(String arg0) {
		return Collections.enumeration(request.getHeaders().getHeaders(arg0));
	}

	@Override
	public int getIntHeader(String arg0) {
		String val = request.getHeaders().getHeader(arg0);
		return Integer.parseInt(val);
	}

	@Override
	public String getMethod() {
		return request.getRequestMethod();
	}

	@Override
	public String getPathInfo() {
		
		return this.request.getRequestPath();
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		
		return null;
	}

	@Override
	public String getRequestURI() {
		return request.getRequestPath();
	}

	@Override
	public StringBuffer getRequestURL() {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(request.getLocalAddress().getHostName());
		sb.append(":");
		sb.append(request.getLocalAddress().getPort());
		sb.append("/");
		sb.append(request.getRequestPath());
		
		return sb;
	}

	@Override
	public String getRequestedSessionId() {
		return request.getSessionId();
	}

	@Override
	public String getServletPath() {
		
		return this.request.getContextPath();
	}

	@Override
	public HttpSession getSession() {
		HttpSession session;
		if(request.getSessionId()==null){
			session = new HttpSessionImpl(context.getServletContext(this.contextPath));
			context.getSessionStore().addSession(session);
		}else{
			session = context.getSessionStore().getSession(request.getSessionId());
			
			if(session == null )
			{
				session = new HttpSessionImpl(context.getServletContext(this.contextPath));
				context.getSessionStore().addSession(session);
			}else{
				if(!Utils.isSessionValid(session)){
					context.getSessionStore().removeSession(session.getId());
					session = new HttpSessionImpl(context.getServletContext(this.contextPath));
					context.getSessionStore().addSession(session);
				}
			}	
		}
		return session;
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		HttpSession session = null;
		
		if(request.getSessionId()!=null){
			session = context.getSessionStore().getSession(request.getSessionId());
			if(session == null)
			{
				session = new HttpSessionImpl(context.getServletContext(this.contextPath));
				context.getSessionStore().addSession(session);
			}else{
				if(!Utils.isSessionValid(session)){
					context.getSessionStore().removeSession(session.getId());
					session = new HttpSessionImpl(context.getServletContext(this.contextPath));
					context.getSessionStore().addSession(session);
				}
			}	
		}else{
			if(arg0){
				session = new HttpSessionImpl(context.getServletContext(this.contextPath));
				context.getSessionStore().addSession(session);
			}
		}
		
		
		return session;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		
		boolean isValid = false;
		HttpSession session=null;
		
		if(request.getSessionId()!=null){
			session = context.getSessionStore().getSession(request.getSessionId());
		}
		if(session!=null){
			isValid = true;
		}
		
		return isValid;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return false;
	}

	

}
