package edu.upenn.cis.cis455.webserver.model.servlet;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Session store for storing the sessions in the context
 */
public class HttpSessionStore {
	
	private ConcurrentHashMap<String,HttpSession> sessionStore;
	
	
	public HttpSessionStore(){
		
		sessionStore = new ConcurrentHashMap<String, HttpSession>();
		
	}
	
	public void addSession(HttpSession session){
		
		String id = session.getId();
		sessionStore.put(id, session);
		
	}
	
	public HttpSession getSession(String id){
		
		return sessionStore.get(id);
		
	}
	
	public void removeSession(String id){
		
		sessionStore.remove(id);
	}
	

}
