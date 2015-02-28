package edu.upenn.cis.cis455.webserver.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.model.Header;
import edu.upenn.cis.cis455.webserver.model.HeaderConstants;
import edu.upenn.cis.cis455.webserver.model.Response;
/**
 * Extension of printwriter for writing to socket output stream.
 * @author cis455
 *
 */
public class PrintWriterImpl extends PrintWriter {
	static final Logger LOG = Logger.getLogger(PrintWriterImpl.class);
	
	private Response response;
	private DataOutputStream stream;
	
	private boolean headersSent=false;
	private boolean autoflush=false; 
	private boolean isCommited=false;
	private int bufSize=Constants.DEFAULT_OUTPUT_BUFFER_SIZE;
	private int index=0;
	private char[] charBuf;
	
	public PrintWriterImpl(OutputStream stream,boolean autoflush,Response response){
		super(stream,autoflush);
		charBuf = new char[bufSize];
		this.stream = new DataOutputStream(stream);
		this.autoflush= autoflush;
		this.response = response;
	}
	
	@Override
	public void flush(){
		try{
			checkAndSendHeaders();
			writeToSocket();
			this.stream.flush();
			super.flush();
		}catch(IOException ioe){
			LOG.debug("Unable to flush "+ioe.getMessage());
			
		}
	}
	
	@Override
	public void write(String s, int off, int len){
		
		checkAndSendHeaders();
		bufferAndSend(s.toCharArray(),off,len);
	}
	
	@Override
	public void write(char buf[],int off,int len){
		
		bufferAndSend(buf,off,len);
	}
	
	@Override
	public void write(int c){
		bufferAndSend(c);
	}
	
	@Override
	public void println(){
		checkAndSendHeaders();
		super.println();
	}
	@Override
	public void close(){
		try{
			if(this.stream !=null){
				writeToSocket();
				flush();
				this.stream.close();
				super.close();
			}
		}catch(IOException ioe){
			LOG.debug("exception while closing stream");
		}finally{
			try{
				if(this.stream !=null)
					this.stream.close();
			}catch(IOException ioe){}
			
		}
	}
		
	
	
	public void setResponse(Response res){
		this.response = res;
	}
	
	public Response getResponse(){
		return this.response;
	}
	
	private void checkAndSendHeaders(){
		
		if(!this.headersSent){
			this.response.sendHeaders(false);
			this.headersSent = true;
			this.isCommited = true;
		}
		
	}
	
	public void reset(){
		if(isCommited)
			throw new IllegalStateException();
		else{
			this.charBuf = new char[this.bufSize];
			response.reset();
		}
	}
	
	public void resetBuffer(){
		this.charBuf = new char[this.bufSize];
	}
	
	public void setBufSize(int size){
		writeToSocket();
		this.bufSize = size;
		this.charBuf = new char[this.bufSize];
	}
	
	public int getBufSize(){
		return this.bufSize;
	}
	
	public boolean isCommited() {
		return isCommited;
	}

	private boolean bufferAndSend(char[] buf,int off , int len){
		
		boolean isFull = false;
		
		if(this.bufSize>len+this.index){
			for(int i=0;i<len;i++){
				this.charBuf[this.index] = buf[off];
				off++;
				this.index++;
			}
			if(this.autoflush)
				writeToSocket();
		}else{
			if(this.index!=0){
				writeToSocket();
				bufferAndSend(buf, off, len);
			}else{
				
				this.charBuf = Arrays.copyOfRange(buf, off, this.bufSize);
				writeToSocket();
				bufferAndSend(buf, off+this.bufSize-1, len-this.bufSize);
			}
		}
		
		
		return isFull;
	}
	
	private boolean bufferAndSend(int c){
		boolean isFull = false;
		
		if(this.index>=this.bufSize-1){
			writeToSocket();
		}
		this.charBuf[this.index] =(char)c;
		
		return isFull;
	}
	
	private void writeToSocket(){
		checkAndSendHeaders();
		try{
			if(this.charBuf.length==0)
				return;
			this.stream.write(new String(this.charBuf).getBytes());
			if(this.autoflush)
				this.stream.flush();
			this.charBuf = new char[getBufSize()];
			this.index =0;
		
		}catch(IOException ioe){
			LOG.debug("Unable to write to socket "+ioe.getMessage());
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
			//res.sendHeaders(false);
			res.sendResponse();
		}catch(IOException ioe){
			LOG.debug("IOException while sending error response."+ioe.getMessage());
		}
	}
	
	public void sendRedirect(String arg0){
		
		Response res = this.getResponse();
		res.setStatusCode(HeaderConstants.STATUSCODE_FOUND);
		res.setStatusMessage(HeaderConstants.STATUSCODE_FOUND_MESSAGE);
		res.setRequestProtocol(Constants.PROTOCOL_HEAD);
		res.getHeaders().setHeader(HeaderConstants.HEADER_LOCATION, arg0);
		res.sendHeaders(false);
	}
	

}
