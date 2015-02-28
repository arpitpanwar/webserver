package edu.upenn.cis.cis455.webserver.context;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import edu.upenn.cis.cis455.webserver.model.servlet.HttpSessionStore;
import edu.upenn.cis.cis455.webserver.pool.ThreadPool;

public class ServerContext {
	
	private int portNum;
	
	private List<ThreadPool> threadPools;
	
	private String serverName;
	private String rootFolder;
	private InetAddress localAddress;
	private HttpSessionStore sessionStore;
	private ConcurrentHashMap<String,ServletContext> contextMap;
	private HashMap<String,HttpServlet> servletMap;
	
	private ResourceContext resourceContext = null;
	private static ServerContext context = null;
	
	private ServerContext(){
		threadPools = new ArrayList<ThreadPool>();
		sessionStore = new HttpSessionStore();
		contextMap = new ConcurrentHashMap<>();
		servletMap = new HashMap<String, HttpServlet>();
		setResourceContext(ResourceContext.getInstance());
	}
	
	public static ServerContext getInstance(){
		
		if(context == null)
			context = new ServerContext();
		
		return context;
	}
	
	public void addServletContext(String path,ServletContext context){
		this.contextMap.put(path, context);
	}
	
	public boolean isContextPathPresent(String path){
		return this.contextMap.containsKey(path);
	}
	
	public List<String> getContextList(){
		return new ArrayList<String>(this.contextMap.keySet());
	}
	
	public ServletContext getServletContext(String path){
		return this.contextMap.get(path);
	}
	
	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}
	
	public InetAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}

	public void setThreadPools(List<ThreadPool> threadPools) {
		this.threadPools = threadPools;
	}
	public List<ThreadPool> getThreadPools() {
		return threadPools;
	}
	
	public void addThreadPool(ThreadPool pool) {
		if(this.threadPools == null)
			threadPools = new ArrayList<ThreadPool>();
		
		threadPools.add(pool);
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public int getPortNum() {
		return portNum;
	}
	
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
	
	public ResourceContext getResourceContext() {
		return resourceContext;
	}
	
	private void setResourceContext(ResourceContext resourceContext) {
		this.resourceContext = resourceContext;
	}

	public HttpSessionStore getSessionStore() {
		return sessionStore;
	}

	public void setSessionStore(HttpSessionStore sessionStore) {
		this.sessionStore = sessionStore;
	}

	public HashMap<String, HttpServlet> getServletMap() {
		return servletMap;
	}

	public void setServletMap(HashMap<String, HttpServlet> servletMap) {
		
		for(String key:servletMap.keySet()){
			this.servletMap.put(key, servletMap.get(key));
		}
				
	}
			

}
