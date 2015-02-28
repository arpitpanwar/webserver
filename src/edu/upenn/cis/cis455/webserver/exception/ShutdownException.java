package edu.upenn.cis.cis455.webserver.exception;

public class ShutdownException extends RuntimeException {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2558627093941417625L;
	
	public ShutdownException() { super(); }
	  public ShutdownException(String message) { super(message); }
	  public ShutdownException(String message, Throwable cause) { super(message, cause); }
	  public ShutdownException(Throwable cause) { super(cause); }

}
