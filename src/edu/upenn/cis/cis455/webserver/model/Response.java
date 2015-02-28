package edu.upenn.cis.cis455.webserver.model;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.util.Constants;

/**
 * Creates a response object which is passed around to send the response back to the user
 * It is also used to initialize HttpServletResponse
 * @author cis455
 *
 */
public class Response {
	private static final Logger LOG = Logger.getLogger(Response.class);
	
	private int bufferSize=Constants.DEFAULT_OUTPUT_BUFFER_SIZE;
	
	private Header headers;
	private InputStream dataStream;
	private OutputStream outStream; 
	private Locale defaultLocale = Locale.getDefault();
	
	private String httpVersion=Constants.HTTP_VERSION_1_1;
	private String statusCode=HeaderConstants.STATUSCODE_OK;
	private String statusMessage=HeaderConstants.STATUSCODE_OK_MESSAGE;
	private String requestProtocol=Constants.PROTOCOL_GET;
	
	public Response(){
				
		headers = new Header();
	}
	

	public String getRequestProtocol() {
		return requestProtocol;
	}

	public void setRequestProtocol(String requestProtocol) {
		this.requestProtocol = requestProtocol;
	}
	
	public OutputStream getOutStream() {
		return outStream;
	}

	public void setOutStream(OutputStream outStream) {
		this.outStream = outStream;
	}

	public String getProtocol() {
		return httpVersion;
	}


	public void setProtocol(String protocol) {
		this.httpVersion = protocol;
	}


	public String getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}


	public String getStatusMessage() {
		return statusMessage;
	}


	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}


	public InputStream getDataStream() {
		return dataStream;
	}


	public void setDataStream(InputStream dataStream) {
		this.dataStream = dataStream;
	}

	
	public Header getHeaders() {
		if(headers == null)
			headers = new Header();
		return headers;
	}

	public void setHeaders(Header headers) {
		this.headers = headers;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}


	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}


	public Locale getDefaultLocale() {
		return defaultLocale;
	}


	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
		this.headers.setHeader(HeaderConstants.HEADER_CONTENT_LANGUAGE, this.defaultLocale.toString());
	}


	public boolean sendResponse(){
		
		return sendResponse(false);
		
	}
	
	public boolean sendHeaders(boolean isPersistent){
		DataOutputStream writer = null;
		try{ 
			 writer = new DataOutputStream(getOutStream());
			 writer.writeBytes(generateResponseString(isPersistent));
			 writer.writeBytes(generateHeaderString());
			// writer.flush();
			return true;
		}catch(IOException ioe){
			LOG.debug("Error sending response to the client"+ioe.getMessage());
			
		}
		return false;
	}
	/**
	 * Sends the response back to the user
	 * @param isPersistent
	 * @return
	 */
		
	public boolean sendResponse(boolean isPersistent){
		InputStream stream = null;
		//BufferedWriter writer = null;
		DataOutputStream writer = null;
		try{ 
			 stream = this.dataStream;
			 writer = new DataOutputStream(getOutStream());
		//	 writer = new BufferedWriter(new OutputStreamWriter(getOutStream()));
			 
			 writer.writeBytes(generateResponseString(isPersistent));
			 writer.writeBytes(generateHeaderString());
			 writer.flush();
			 sendMessageBody(writer, stream);
			return true;
		}catch(IOException ioe){
			LOG.debug("Error sending response to the client"+ioe.getMessage());
			
		}finally{
			try{
				if(stream!=null)
					stream.close();
				if(writer!=null)
					writer.close();
			}catch(IOException io){
				LOG.debug("Error closing stream for response");
			}
		} 
		return false;
	}
	/**
	 * Utility method to send the message body
	 * @param writer
	 * @param stream
	 * @throws IOException
	 */
	
	private void sendMessageBody(OutputStream writer,InputStream stream)throws IOException{
		
		if(requestProtocol.equalsIgnoreCase(Constants.PROTOCOL_HEAD))
		{
			writer.write(0);
			writer.flush();
			
		}else{
			
			DataInputStream dis = new DataInputStream(stream);
			
			int read;
			
			int size = this.bufferSize;
			byte[] buf = new byte[size];
			while((read=dis.read(buf)) != -1){
				
				if(read<=size-1){
					for(int i=0;i<read;i++)
						writer.write(buf[i]);
				}else{
					writer.write(buf);
				}
			}
			
			writer.flush();
			stream.close();
		}	
			
	}
	
	private String generateResponseString(boolean isPersistent){
		
		String protocol = getProtocol();
		String status = getStatusCode();
		String statusMessage = getStatusMessage();
		StringBuilder sb = new StringBuilder();
		
		if(isPersistent)
			sb.append(Constants.RESPONSE_PERSISTENT_STRING+"\r\n\r\n");
		
		if(protocol!=null)
			sb.append(protocol+" ");
		if(status!=null)
			sb.append(status+" ");
		if(statusMessage!=null)
			sb.append(statusMessage+"\r\n");
		
		if (!sb.toString().endsWith("\r\n"))
			sb.append("\r\n");
		
		return sb.toString();
		
	}
	
	private String generateHeaderString(){
		
		Header headers = getHeaders();
		StringBuilder sb = new StringBuilder();
		if(headers!=null){
			Iterator<Map.Entry<String,ArrayList<String>>> it = headers.getIterator();
			
			while(it.hasNext()){
				
				Map.Entry<String, ArrayList<String>> curr = it.next();
				ArrayList<String> currList = curr.getValue();
				sb.append(curr.getKey()+": ");
				for(String val : currList){
					sb.append(val+";");
				}
				sb.deleteCharAt(sb.lastIndexOf(";"));
				sb.append("\r\n");
				it.remove();
				
			}
		}
		sb.append("\r\n");

		return sb.toString();
	}
	
	public void reset(){
		setHeaders(new Header());
		statusCode=HeaderConstants.STATUSCODE_OK;
		statusMessage=HeaderConstants.STATUSCODE_OK_MESSAGE;
		requestProtocol=Constants.PROTOCOL_GET;
	}

}
