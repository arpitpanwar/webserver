package edu.upenn.cis.cis455.webserver.exception;

public class ForbiddenException extends Exception {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = -8967085263934189857L;
	public ForbiddenException() { super(); }
	  public ForbiddenException(String message) { super(message); }
	  public ForbiddenException(String message, Throwable cause) { super(message, cause); }
	  public ForbiddenException(Throwable cause) { super(cause); }

}
