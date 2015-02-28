package edu.upenn.cis.cis455.webserver.exception;

public class SocketCloseException extends Exception {

	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 4088141708061883392L;
	public 	SocketCloseException() { super(); }
	  public SocketCloseException(String message) { super(message); }
	  public SocketCloseException(String message, Throwable cause) { super(message, cause); }
	  public SocketCloseException(Throwable cause) { super(cause); }

	
}
