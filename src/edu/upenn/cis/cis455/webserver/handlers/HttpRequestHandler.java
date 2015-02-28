package edu.upenn.cis.cis455.webserver.handlers;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.exception.BadRequestException;
import edu.upenn.cis.cis455.webserver.exception.ForbiddenException;
import edu.upenn.cis.cis455.webserver.exception.MethodNotAllowedException;
import edu.upenn.cis.cis455.webserver.exception.ShutdownException;
import edu.upenn.cis.cis455.webserver.model.HeaderConstants;
import edu.upenn.cis.cis455.webserver.model.Request;
import edu.upenn.cis.cis455.webserver.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class HttpRequestHandler extends RequestHandler {
	private static final Logger LOG = Logger.getLogger(HttpRequestHandler.class);
	private Socket request;
	private ServerContext context;
	private boolean supportNewFeatures = false;
	private Request initialReq = null;
	private Object obj;
	private HttpServlet servlet;
	
	
	public HttpRequestHandler(Socket request,ServerContext context)throws IOException {
				this.request = request;
				this.context = context;
				this.initialReq = new Request(this.request);
				obj = new String();
	}
	
	
	
	@Override
	public Object handle() throws ShutdownException {
		try{
			
			setInitialParams(this.initialReq);
			
			if(!validateAndSendResponse(initialReq))
				this.request.close();
			if(obj instanceof String){
				if(((String) obj).equalsIgnoreCase(Constants.SHUTDOWN))
					return obj;
			}
			
		}catch(IOException ioe){
			LOG.debug("Error processing the received request. Closing the connection\n"+ioe.getMessage());
		}finally{
			try{
				this.request.close();
				return obj;
			}catch(IOException io){
				LOG.debug("Exception closing the socket\n"+io.getMessage());
			}
		}
		return obj;
	}

	public Request getInitialReq() {
		return initialReq;
	}



	public void setInitialReq(Request initialReq) {
		this.initialReq = initialReq;
	}



	private void setInitialParams(Request req){
		
		setSupportNewFeatures(req.isNewHttpVersion());
		
	}
	/*
	 * Validates the incoming request and sends the required response
	 */
	private boolean validateAndSendResponse(Request req){
		ResponseSender sender = new ResponseSender();
		boolean isValid = false;
		boolean isSpecial = false;
		try{
			
			isValid = isValidRequest(req);	
			isSpecial = isSpecialRequest(req);
			if(isSpecial)
			{
				sender.sendSpecialResponse(req,context);
				
			}else
			{	
				if(isServlet(req.getContextPath())){
					ServletRequestHandler servHandler =	new ServletRequestHandler(req, context,this.servlet);
					servHandler.handle();
				}else{
					sender.sendFirstResponse(req,context.getRootFolder());			
				}
			}
		}catch(BadRequestException bre){
			sender.sendErrorResponse(req, Constants.BAD_REQUEST_STRING);
		}catch(FileNotFoundException fnfe){
			sender.sendErrorResponse(req, Constants.NOT_FOUND_STRING);
		}catch(MethodNotAllowedException mna){
			sender.sendErrorResponse(req, Constants.METHOD_NOT_SUPPORTED_STRING);
		}catch(ForbiddenException fbe){
			sender.sendErrorResponse(req, Constants.FORBIDDEN_STRING);
		}catch(Exception e){
			if(e instanceof ShutdownException)
			{	
				obj = new String(Constants.SHUTDOWN);
			}else{
				LOG.debug("Unexpected error during response creation: "+e.getStackTrace()[0].toString());
				sender.sendErrorResponse(req, Constants.SERVER_ERROR_STRING);
			}
		}
				
		return isValid;
	}
	/*
	 * Check if the request is for Control or Shutdown
	 */
	
	private boolean isSpecialRequest(Request req){
		boolean isSpecial = false;
		
		String path = req.getRequestPath();
		
		isSpecial = (path.equalsIgnoreCase(Constants.SPECIAL_PATH_CONTROl) 
					|| path.equalsIgnoreCase(Constants.SPECIAL_PATH_SHUTDOWN));
		
		return isSpecial;
		
	}
	/*
	 * Validate the incoming request and throw exceptions based on the type of error in the request
	 * Returns a boolean true/false indicating validity
	 */
	private boolean isValidRequest(Request req) throws BadRequestException,
	FileNotFoundException, MethodNotAllowedException, ForbiddenException
	{
		
		if((req.isEmptyPath()) || (req.isNewHttpVersion() && !req.isValidHostHeader()))
			throw new BadRequestException();
		
		if(!Arrays.asList(HeaderConstants.SUPPORTED_METHODS).contains(req.getRequestMethod()))
			throw new MethodNotAllowedException();
		
		if(!isChild(context.getRootFolder()+File.separator+req.getRequestPath(), context.getRootFolder()))
			throw new ForbiddenException();
		
		if(!isSpecialRequest(req) && !isResourcePresent(context.getRootFolder()+File.separator+req.getRequestPath())
				&& !isServlet(req.getContextPath()) )
			throw new FileNotFoundException();
				
			
		return true;
	}
	
	/*
	 * Utility function to check if resource exists
	 */
	private boolean isResourcePresent(String childPath){
		return new File(childPath).exists();
	}
	
	/*
	 * Check if the request was for a servlet and not for a web page
	 */
	
	private boolean isServlet(String path){
		
		List<String> contextList = new ArrayList<>(context.getServletMap().keySet());
		
		for(String context:contextList){
			Pattern pattern = Pattern.compile(context);
			if(pattern.matcher(path).matches()){
				this.servlet = this.context.getServletMap().get(context);
				return true;
			}
		}
		return false;
		//return contextList.contains(path) || contextList.contains(Constants.MATCH_ALL);
						
	}
	
	/*
	 * Utility function to check if the hierarchy is followed
	 */
	private boolean isChild(String childPath, String parentPath) {
		Path child = Paths.get(childPath).toAbsolutePath();
	    Path parent = Paths.get(parentPath).toAbsolutePath();
	    return child.startsWith(parent);
	}
	
	

	public boolean isSupportNewFeatures() {
		return supportNewFeatures;
	}



	public void setSupportNewFeatures(boolean supportNewFeatures) {
		this.supportNewFeatures = supportNewFeatures;
	}


	@Override
	public Object call() throws Exception {
		this.initialReq.parseRequest();
		return handle();
		
	}
	


}
