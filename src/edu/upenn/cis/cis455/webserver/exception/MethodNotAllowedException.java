package edu.upenn.cis.cis455.webserver.exception;

public class MethodNotAllowedException extends Exception {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 2745961871455834453L;
	public MethodNotAllowedException() { super(); }
	  public MethodNotAllowedException(String message) { super(message); }
	  public MethodNotAllowedException(String message, Throwable cause) { super(message, cause); }
	  public MethodNotAllowedException(Throwable cause) { super(cause); }

}
