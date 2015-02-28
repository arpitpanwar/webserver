package edu.upenn.cis.cis455.webserver.model.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.handlers.ResponseSender;
import edu.upenn.cis.cis455.webserver.model.HeaderConstants;
import edu.upenn.cis.cis455.webserver.model.Response;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.PrintWriterImpl;
import edu.upenn.cis.cis455.webserver.util.Utils;

/**
 * Implementation of HttpServletResponse Interface
 * @author cis455
 *
 */
public class HttpServletResponseImpl implements HttpServletResponse {
	static final Logger LOG = Logger.getLogger(HttpServletResponseImpl.class);
	
	private Response response;
	private PrintWriterImpl writer;
	
	private String characterEncoding = Constants.DEFAULT_ENCODING;
	
	 
	public HttpServletResponseImpl(Response response) {
		
		this.response = response;
		
		writer = new PrintWriterImpl(response.getOutStream(),true,response);
		
		
	}
	
	
	@Override
	public void flushBuffer() throws IOException {
		
		writer.flush();
	}

	@Override
	public int getBufferSize() {
		
		return this.writer.getBufSize();
	}

	@Override
	public String getCharacterEncoding() {
		
		return this.characterEncoding;
	}

	@Override
	public String getContentType() {
		return this.writer.getResponse().getHeaders().getHeader(HeaderConstants.HEADER_CONTENT_TYPE);
	}

	@Override
	public Locale getLocale() {

		return this.writer.getResponse().getDefaultLocale();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	@Override
	public boolean isCommitted() {
		return this.writer.isCommited();
	}

	@Override
	public void reset() {
		if(this.isCommitted())
			throw new IllegalStateException();
		 this.writer.reset();
	}

	@Override
	public void resetBuffer() {
		this.writer.resetBuffer();
	}

	@Override
	public void setBufferSize(int arg0) {
		this.writer.setBufSize(arg0);
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		this.characterEncoding = arg0;
	}

	@Override
	public void setContentLength(int arg0) {
		this.writer.getResponse().getHeaders().setContentLength(arg0);
	}

	@Override
	public void setContentType(String arg0) {
		if(this.writer==null){
			System.out.println("writer null");
		}
		if(this.writer.getResponse() == null){
			System.out.println("response null");
		}
		if(this.writer.getResponse().getHeaders() == null){
			System.out.println("Header null");
		}
		this.writer.getResponse().getHeaders().setContentType(arg0);
	}

	@Override
	public void setLocale(Locale arg0) {
		this.writer.getResponse().setDefaultLocale(arg0);
	}

	@Override
	public void addCookie(Cookie arg0) {
		this.writer.getResponse().getHeaders().addCookie(arg0);
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		this.writer.getResponse().getHeaders().addHeader(arg0, Utils.getFormattedDate(arg1));

	}

	@Override
	public void addHeader(String arg0, String arg1) {
		this.writer.getResponse().getHeaders().addHeader(arg0, arg1);
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		this.writer.getResponse().getHeaders().addHeader(arg0, String.valueOf(arg1));

	}

	@Override
	public boolean containsHeader(String arg0) {
		
		return this.writer.getResponse().getHeaders().contains(arg0);
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		try{
			String encoded = URLEncoder.encode(arg0, "UTF-8");
			return encoded;
		}catch(UnsupportedEncodingException ue){
			LOG.debug("Unsupported scheme "+ue.getMessage());
		}
		return null;
	}

	@Override
	@Deprecated
	public String encodeRedirectUrl(String arg0) {
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		try{
			String encoded = URLEncoder.encode(arg0, "UTF-8");
			return encoded;
		}catch(UnsupportedEncodingException ue){
			LOG.debug("Unsupported scheme "+ue.getMessage());
		}
		return null;
	}

	@Override
	@Deprecated
	public String encodeUrl(String arg0) {
		return null;
	}

	@Override
	public void sendError(int arg0) throws IOException {
		
		String errorCode;
		
		
		switch(arg0){
			case(HeaderConstants.STATUSCODE_BADREQ_VAL):
			{
				errorCode = Constants.BAD_REQUEST_STRING;
				break;
			}
			case(HeaderConstants.STATUSCODE_FORBIDDEN_VAL):
				errorCode = Constants.FORBIDDEN_STRING;
				break;
			case(HeaderConstants.STATUSCODE_NOT_SUPPORTED_VAL):
				errorCode = Constants.METHOD_NOT_SUPPORTED_STRING;
				break;
			case(HeaderConstants.STATUSCODE_NOTFOUND_VAL):
				errorCode = Constants.NOT_FOUND_STRING;
				break;
			case(HeaderConstants.STATUSCODE_SERVER_ERROR_VAL):
				errorCode = Constants.SERVER_ERROR_STRING;
				break;
			default:
				errorCode = Constants.SERVER_ERROR_STRING;
		}
		
		this.writer.sendErrorResponse(this.writer.getResponse(), errorCode);
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
			sendError(arg0);
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
			this.writer.sendRedirect(arg0);
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		this.writer.getResponse().getHeaders().setHeader(arg0, Utils.getFormattedDate(arg1));

	}

	@Override
	public void setHeader(String arg0, String arg1) {
		this.writer.getResponse().getHeaders().setHeader(arg0, arg1);

	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		this.writer.getResponse().getHeaders().setHeader(arg0, String.valueOf(arg1));
	}

	@Override
	public void setStatus(int arg0) {
		this.writer.getResponse().setStatusCode(String.valueOf(arg0));
	}

	@Override
	@Deprecated
	public void setStatus(int arg0, String arg1) {
		this.writer.getResponse().setStatusCode(String.valueOf(arg0));
		this.writer.getResponse().setStatusMessage(arg1);
	}

}
