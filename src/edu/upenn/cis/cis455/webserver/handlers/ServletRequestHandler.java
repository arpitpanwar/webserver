package edu.upenn.cis.cis455.webserver.handlers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.model.Request;
import edu.upenn.cis.cis455.webserver.model.Response;
import edu.upenn.cis.cis455.webserver.model.servlet.HttpServletRequestImpl;
import edu.upenn.cis.cis455.webserver.model.servlet.HttpServletResponseImpl;
import edu.upenn.cis.cis455.webserver.util.Constants;

/**
 * Handles the requests for servlets
 * @author Arpit
 *
 */
public class ServletRequestHandler implements Handler {
	static final Logger LOG = Logger.getLogger(ServletRequestHandler.class); 
	
	
	private HttpServlet servlet;
	private Object result;
	private Request request;
	private ServerContext context;
	private ServletContext servletContext;

	public ServletRequestHandler(Request request, ServerContext context,HttpServlet servlet){
		this.request = request;
		this.context = context;
		this.servletContext = context.getServletContext(Constants.DEFAULT_CONTEXT);
		this.servlet = servlet;
		result = new Object();
	}
	
	public Object handle(){
		
		sendResponse();
		
		
		return result;
	}
	/**
	 * Sends the response to the user and closes the connections
	 */
	private void sendResponse(){
		ResponseSender sender = new ResponseSender();
		try{
			this.servlet.service(generateServletRequest(), generateServletResponse());
		}catch(IOException ioe){
			LOG.debug("IOException during servlet invocation: "+ioe.getMessage());
			sender.sendErrorResponse(request, Constants.SERVER_ERROR_STRING);
		}catch(ServletException se){
			LOG.debug("Servlet Exception during servlet invocation: "+se.getMessage());
			sender.sendErrorResponse(request, Constants.SERVER_ERROR_STRING);
		}catch(NullPointerException npe){
			LOG.debug("Null pointer"+npe.getMessage());
			sender.sendErrorResponse(request, Constants.SERVER_ERROR_STRING);
			npe.printStackTrace();
		}catch(Exception e){
			LOG.debug("Unexpected Exception");
			sender.sendErrorResponse(request, Constants.SERVER_ERROR_STRING);
		}
		
	}
	
	private HttpServletRequest generateServletRequest(){
		HttpServletRequest request = new HttpServletRequestImpl(this.request, this.context, this.request.getContextPath());
		return request;
	}
	
	private HttpServletResponse generateServletResponse()throws IOException{
		Response res = new Response();
		res.setOutStream(request.getOutputStream());
		HttpServletResponse response = new HttpServletResponseImpl(res);
		return response;
	}
	
	
	
	
}
