package edu.upenn.cis.cis455.webserver.handlers;


import edu.upenn.cis.cis455.webserver.exception.ShutdownException;
/*
 * Generic interface implemented by all the handlers
 */
public interface Handler {

	public Object handle() throws ShutdownException;
	
		
}
