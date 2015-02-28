package edu.upenn.cis.cis455.webserver.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.exception.ShutdownException;
import edu.upenn.cis.cis455.webserver.model.Header;
import edu.upenn.cis.cis455.webserver.model.HeaderConstants;
import edu.upenn.cis.cis455.webserver.model.Request;
import edu.upenn.cis.cis455.webserver.model.Response;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Utils;

/*
 * Class handles the response sending for Web pages and static resources
 */

public class ResponseSender {
	static final Logger LOG = Logger.getLogger(ResponseSender.class);
	
	public ResponseSender(){
		
	}
	
	/*
	 * Send the static response based on the request received 
	 * 
	 */
	
	public void sendFirstResponse(Request req,String root){
		
		Response res = new Response();
		Header header = new Header();
		boolean ifModified = false,ifUnmodified=false,responseSet=false;
		try{
			String date ="";
			
			if(req.getHeaders().contains(HeaderConstants.HEADER_IF_MODIFIED_SINCE)){
				ifModified = true; 
				date = req.getHeaders().getHeader(HeaderConstants.HEADER_IF_MODIFIED_SINCE);
			}
			if(req.getHeaders().contains(HeaderConstants.HEADER_IF_UNMODIFIED_SINCE)){
				ifUnmodified = true; 
				date = req.getHeaders().getHeader(HeaderConstants.HEADER_IF_UNMODIFIED_SINCE);
			}
			
			if(!date.equalsIgnoreCase("")&&ifModified){
				if(!Utils.isResourceModified(root+File.separator+req.getRequestPath(), date)){
					header.setDate(Utils.getFormattedDate());
					res.setRequestProtocol(Constants.PROTOCOL_HEAD);
					res.setStatusCode(HeaderConstants.STATUSCODE_NOT_MODIFIED);
					res.setStatusMessage(HeaderConstants.STATUSCODE_NOT_MODIFIED_MESSAGE);
					responseSet = true;
				}
				
			}
			
			if(!date.equalsIgnoreCase("")&&ifUnmodified){
				
				if(Utils.isResourceModified(root+File.separator+req.getRequestPath(), date)){
					res.setRequestProtocol(Constants.PROTOCOL_HEAD);
					res.setStatusCode(HeaderConstants.STATUSCODE_PRECONDITION_FAILED);
					res.setStatusMessage(HeaderConstants.STATUSCODE_PRECONDITION_FAILED_MESSAGE);
					responseSet = true;
				}
				
			}
			
			if(!responseSet){
				HashMap<String,Object> streamInfo = Utils.getStreamFromResource(root+File.separator+req.getRequestPath());
				InputStream stream = (InputStream)streamInfo.get(Constants.CONTENT_STREAM);
				header.setContentLength((long)streamInfo.get(Constants.CONTENT_LENGTH));
				header.setContentType((String)streamInfo.get(Constants.CONTENT_TYPE));
				header.setDate(Utils.getFormattedDate());
				res.setDataStream(stream);
				res.setStatusCode(HeaderConstants.STATUSCODE_OK);
				res.setStatusMessage(HeaderConstants.STATUSCODE_OK_MESSAGE);
				res.setRequestProtocol(req.getRequestMethod());
			}
			res.setHeaders(header);
			res.setOutStream(req.getOutputStream());
			res.setProtocol(req.getProtocol());
			if(req.getHeaders().contains(HeaderConstants.HEADER_EXPECT))
				if(req.getHeaders().getHeader(HeaderConstants.HEADER_EXPECT).equalsIgnoreCase(HeaderConstants.HEADER_EXPECT_MESSAGE))
					res.sendResponse(true);
				else
					res.sendResponse();
			else
				res.sendResponse();
		}catch(IOException ioe){
			LOG.debug("Unable to send response "+ioe.getMessage());
		}		
		
	}
	
	
	
	public void sendErrorResponse(Request req,String error){
		HashMap<String, Object> streamInfo = new HashMap<String, Object>();
		Response res = new Response();
		Header header = new Header();
		try{	
		switch(error){
			case Constants.BAD_REQUEST_STRING:
				streamInfo = Utils.getErrorPageInfo(Constants.BAD_REQUEST_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_BADREQ);
				res.setStatusMessage(HeaderConstants.STATUSCODE_BADREQ_MESSAGE);
				break;
			case Constants.NOT_FOUND_STRING:
				streamInfo = Utils.getErrorPageInfo(Constants.NOT_FOUND_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_NOTFOUND);
				res.setStatusMessage(HeaderConstants.STATUSCODE_NOTFOUND_MESSAGE);
				break;
			case Constants.FORBIDDEN_STRING:
				streamInfo = Utils.getErrorPageInfo(Constants.FORBIDDEN_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_FORBIDDEN);
				res.setStatusMessage(HeaderConstants.STATUSCODE_FORBIDDEN_MESSAGE);
				break;
			case Constants.SERVER_ERROR_STRING:
				streamInfo =  Utils.getErrorPageInfo(Constants.SERVER_ERROR_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_SERVER_ERROR);
				res.setStatusMessage(HeaderConstants.STATUSCODE_SERVER_ERROR_MESSAGE);
				break;
			case Constants.METHOD_NOT_SUPPORTED_STRING:
				streamInfo =  Utils.getErrorPageInfo(Constants.METHOD_NOT_SUPPORTED_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_NOT_SUPPORTED);
				res.setStatusMessage(HeaderConstants.STATUSCODE_NOT_SUPPORTED_MESSAGE);
				break;
			default:
				streamInfo =  Utils.getErrorPageInfo(Constants.SERVER_ERROR_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_SERVER_ERROR);
				res.setStatusMessage(HeaderConstants.STATUSCODE_SERVER_ERROR_MESSAGE);
			}
			if(streamInfo == null)
				return;
			InputStream stream = (InputStream)streamInfo.get(Constants.CONTENT_STREAM);
			header.setContentLength((long)streamInfo.get(Constants.CONTENT_LENGTH));
			header.setContentType((String)streamInfo.get(Constants.CONTENT_TYPE));
			header.setDate(Utils.getFormattedDate());
			res.setDataStream(stream);
			res.setOutStream(req.getOutputStream());
			res.setProtocol(req.getProtocol());
			res.setHeaders(header);
			res.setRequestProtocol(req.getRequestMethod());
			res.sendResponse();
		}catch(IOException ioe){
			LOG.debug("IOException while sending error response."+ioe.getMessage());
		}
	}
	
	public void sendErrorResponse(Response res,String error){
		HashMap<String, Object> streamInfo = new HashMap<String, Object>();
		Header header = new Header();
		try{	
		switch(error){
			case Constants.BAD_REQUEST_STRING:
				streamInfo = Utils.getErrorPageInfo(Constants.BAD_REQUEST_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_BADREQ);
				res.setStatusMessage(HeaderConstants.STATUSCODE_BADREQ_MESSAGE);
				break;
			case Constants.NOT_FOUND_STRING:
				streamInfo = Utils.getErrorPageInfo(Constants.NOT_FOUND_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_NOTFOUND);
				res.setStatusMessage(HeaderConstants.STATUSCODE_NOTFOUND_MESSAGE);
				break;
			case Constants.FORBIDDEN_STRING:
				streamInfo = Utils.getErrorPageInfo(Constants.FORBIDDEN_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_FORBIDDEN);
				res.setStatusMessage(HeaderConstants.STATUSCODE_FORBIDDEN_MESSAGE);
				break;
			case Constants.SERVER_ERROR_STRING:
				streamInfo =  Utils.getErrorPageInfo(Constants.SERVER_ERROR_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_SERVER_ERROR);
				res.setStatusMessage(HeaderConstants.STATUSCODE_SERVER_ERROR_MESSAGE);
				break;
			case Constants.METHOD_NOT_SUPPORTED_STRING:
				streamInfo =  Utils.getErrorPageInfo(Constants.METHOD_NOT_SUPPORTED_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_NOT_SUPPORTED);
				res.setStatusMessage(HeaderConstants.STATUSCODE_NOT_SUPPORTED_MESSAGE);
				break;
			default:
				streamInfo =  Utils.getErrorPageInfo(Constants.SERVER_ERROR_STRING);
				res.setStatusCode(HeaderConstants.STATUSCODE_SERVER_ERROR);
				res.setStatusMessage(HeaderConstants.STATUSCODE_SERVER_ERROR_MESSAGE);
			}
			if(streamInfo == null)
				return;
			InputStream stream = (InputStream)streamInfo.get(Constants.CONTENT_STREAM);
			header.setContentLength((long)streamInfo.get(Constants.CONTENT_LENGTH));
			header.setContentType((String)streamInfo.get(Constants.CONTENT_TYPE));
			header.setDate(Utils.getFormattedDate());
			res.setDataStream(stream);
			res.setHeaders(header);
			res.sendResponse();
		}catch(IOException ioe){
			LOG.debug("IOException while sending error response."+ioe.getMessage());
		}
	}
	
	public void sendSpecialResponse(Request req,ServerContext context){
		
		String path = req.getRequestPath();
		if(path.equalsIgnoreCase(Constants.SPECIAL_PATH_SHUTDOWN))
			{
				sendShutdownResponse(req);
				throw new ShutdownException();
			}
		else
			sendControlResponse(req,context);
		
	}
	private void sendShutdownResponse(Request req){
		
		Response res = new Response();
		Header header = new Header();
		try{
			HashMap<String,Object> streamInfo = Utils.getStreamFromResource(Constants.SHUTDOWN_FILE);
			InputStream stream = (InputStream)streamInfo.get(Constants.CONTENT_STREAM);
			header.setContentLength((long)streamInfo.get(Constants.CONTENT_LENGTH));
			header.setContentType((String)streamInfo.get(Constants.CONTENT_TYPE));
			header.setDate(Utils.getFormattedDate());
			res.setOutStream(req.getOutputStream());
			res.setDataStream(stream);
			res.setProtocol(req.getProtocol());
			res.setStatusCode(HeaderConstants.STATUSCODE_OK);
			res.setStatusMessage(HeaderConstants.STATUSCODE_OK_MESSAGE);
			res.setRequestProtocol(req.getRequestMethod());
			res.setHeaders(header);
			res.sendResponse();
		}catch(Exception e){
			LOG.debug("Unable to send the shutdown response: "+e.getMessage());
		}
		
	}
	private void sendControlResponse(Request req,ServerContext context){
		
		Response res = new Response();
		Header header = new Header();
		try{
			HashMap<String, Object> streamInfo = Utils.generateControlData(context.getThreadPools().get(0));
			InputStream stream = (InputStream)streamInfo.get(Constants.CONTENT_STREAM);
			header.setContentLength((long)streamInfo.get(Constants.CONTENT_LENGTH));
			header.setContentType((String)streamInfo.get(Constants.CONTENT_TYPE));
			header.setDate(Utils.getFormattedDate());
			res.setDataStream(stream);
			res.setOutStream(req.getOutputStream());
			res.setProtocol(req.getProtocol());
			res.setStatusCode(HeaderConstants.STATUSCODE_OK);
			res.setStatusMessage(HeaderConstants.STATUSCODE_OK_MESSAGE);
			res.setHeaders(header);
			res.setRequestProtocol(req.getRequestMethod());
			res.sendResponse();
		}catch(IOException ioe){
			LOG.debug("Unable to send response "+ioe.getMessage());
		}		
		
		
	}

}
