package edu.upenn.cis.cis455.webserver.exception;

public class BadRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8077659985894466312L;
	public BadRequestException() { super(); }
	  public BadRequestException(String message) { super(message); }
	  public BadRequestException(String message, Throwable cause) { super(message, cause); }
	  public BadRequestException(Throwable cause) { super(cause); }

	
	
}
