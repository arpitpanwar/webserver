package edu.upenn.cis.cis455.webserver.model.servlet;

import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import edu.upenn.cis.cis455.webserver.util.Utils;

/**
 * Implementation of HttpSession interface
 * @author cis455
 *
 */

@SuppressWarnings("deprecation")
public class HttpSessionImpl implements HttpSession {
	
	private ServletContext context;
	private String id;
	private HashMap<String,Object> attributeMap;

	private long creationTime;
	private long lastAccessedTime=0;

	private int maxIntervalTime=30;
	
	private boolean isValid=true;
	private boolean isNew = true;
	
	public HttpSessionImpl(ServletContext context) {
		attributeMap = new HashMap<>();
		this.context = context;
		this.initializeSession();
	}
	
	private void initializeSession(){
		
		this.setId(Utils.generateUniqieId());
		this.setCreationTime(new Date().getTime());
		this.setLastAccessedTime(this.getCreationTime());
	}
	
	private void setIsValid(boolean val){
		this.isValid = val;
	}
	
	private void setId(String id){
		this.id = id;
	}
	
	private void setCreationTime(long time){
		this.creationTime = time;
	}
	
	private void setLastAccessedTime(long time){
		this.lastAccessedTime = time;
	}
	
	private void updateLastAccessedTime(){
		this.setLastAccessedTime(new Date().getTime());
	}
	
	public void setIsNew(boolean isNew){
		this.isNew = isNew;
	}

	@Override
	public Object getAttribute(String arg0) {
		this.updateLastAccessedTime();
		return attributeMap.get(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		this.updateLastAccessedTime();
		return Collections.enumeration(this.attributeMap.keySet());
	}

	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxIntervalTime;
	}

	@Override
	public ServletContext getServletContext() {
		updateLastAccessedTime();
		return this.context;
	}

	@Override
	@Deprecated
	public HttpSessionContext getSessionContext() {
		
		return null;
	}

	@Override
	@Deprecated
	public Object getValue(String arg0) {
		
		return this.getAttribute(arg0);
	}

	@Override
	@Deprecated
	public String[] getValueNames() {
		
		this.updateLastAccessedTime();
		return (String[])this.attributeMap.keySet().toArray();
	}

	@Override
	public void invalidate() {
		this.setIsValid(false);
	}

	@Override
	public boolean isNew() {
		return this.isNew;
	}

	@Override
	@Deprecated
	public void putValue(String arg0, Object arg1) {
		this.setAttribute(arg0, arg1);

	}

	@Override
	public void removeAttribute(String arg0) {
		this.updateLastAccessedTime();
		this.attributeMap.remove(arg0);
	}

	@Override
	@Deprecated
	public void removeValue(String arg0) {
		 this.removeAttribute(arg0);
				
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		this.updateLastAccessedTime();
		this.attributeMap.put(arg0, arg1);

	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		this.maxIntervalTime = arg0;

	}
	
	public boolean isSessionValid(){
		return this.isValid;
	}

}
