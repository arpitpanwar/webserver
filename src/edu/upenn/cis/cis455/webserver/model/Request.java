package edu.upenn.cis.cis455.webserver.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Utils;
 
public class Request {
	static final Logger LOG = Logger.getLogger(Request.class);

	private Socket socket = null;
	
	private Cookie[] cookies = null;
	
	private Header headers;
	private HashMap<String,String> parameterMap;
	
	private InetSocketAddress remoteAddress;
	private InetSocketAddress localAddress;

	private String hostname=null;
	private String protocol=null;
	private String requestMethod = null;
	private String requestPath=null;
	private String scheme = null;
	private String sessionId=null;
	private String contextPath = null;
	private String queryString = null;
	
	private boolean hasTransferEncoding = false;
	private boolean isNewHttpVersion = false;
	
	
	public Request(Socket socket) throws IOException{
		
		this.socket = socket;
		headers = new Header();
		parameterMap = new HashMap<String, String>();
	}
	
	public InputStream getInputStream()throws IOException{
		return socket.getInputStream();
	}
	
	public boolean getIsNewProtocol(){
		return isNewHttpVersion;
	}
	
	private void setIsNewProtocol(boolean isNew){
		this.isNewHttpVersion = isNew;
	}
	
	public boolean isHasTransferEncoding() {
		return hasTransferEncoding;
	}

	public void setHasTransferEncoding(boolean hasTransferEncoding) {
		this.hasTransferEncoding = hasTransferEncoding;
	}
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public Header getHeaders() {
		return headers;
	}
	
	private void setHeaders(Header headers){
		this.headers = headers;
	}
	
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetSocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public Cookie[] getCookies() {
		return cookies;
	}

	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
		updateParameters();
	}

	public HashMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(HashMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	//TODO Provide support for Transfer-Encoding
	public void parseRequest() throws IOException,NullPointerException{
		
		if(socket == null)
			throw new NullPointerException();
		this.setRemoteAddress(new InetSocketAddress(socket.getInetAddress(), socket.getPort()));
		this.setLocalAddress(new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort()));
		InputStream stream = null;
		BufferedReader br = null;
		try{
		stream = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(stream));
		String read="";
		
		StringBuffer sb = new StringBuffer();
		
		while((read=br.readLine())!=null){
			if(read.length()==0 || read.equalsIgnoreCase("\r\n\r\n"))
				break;
			sb.append(read+"\n");
		}
		
		
		String tokens[] = sb.toString().split("\n");
		if(tokens.length>0)
		{	parseRequestString(tokens[0]);
			for(int j =1;j<tokens.length;j++){
				parseAndAddHeader(tokens[j]);
			}
		}
		
		if(getRequestMethod().equalsIgnoreCase(Constants.PROTOCOL_POST)){
			sb = new StringBuffer();
			int len = Integer.parseInt(this.headers.getHeader(HeaderConstants.HEADER_CONTENT_LENGTH));
			int val;
			while(len!=0){
				val = br.read();
				sb.append((char)val);
				len--;
			}
			parsePostParams(sb.toString());
		}
		
		}catch(IOException io){
			throw new IOException("Error parsing the request");
		}finally{
		}		
	}
	
	private void parseAndAddHeader(String header){
		
		if(header == null || header.equalsIgnoreCase(""))
			return;
		
		Header map = getHeaders();
		if(map == null)
			map = new Header();
		
		String tokens[] = header.split(":", 2);
		if(tokens.length==2){
			
			if(tokens[0].trim().equalsIgnoreCase(HeaderConstants.HEADER_TRANSFER_ENCODING))
				setHasTransferEncoding(true);
			if(tokens[0].trim().equalsIgnoreCase(HeaderConstants.HEADER_COOKIE)){
				setCookies(parseCookies(tokens[1].trim()));
			}else{
				map.addHeader(Utils.capitalizeString(tokens[0].trim()), tokens[1].trim());
			}
			
			
		}
		setHeaders(map);
	}
	
	private void parseRequestString(String requestString){
		
		if(requestString == null)
			return;	
		try{
			String tokens[] = requestString.split(" ");
			
			if(tokens.length > 0){
				setRequestMethod(tokens[0].trim());
				setRequestPath(parseRequestPath(tokens[1].trim()));
				setContextPath(parseContextPath(getRequestPath()));
				setProtocol(tokens[2].trim());
			}
		}catch(ArrayIndexOutOfBoundsException aib){
			LOG.error("Number of parameters invalid in the request string");
		}catch(MalformedURLException mfe){
			
		}
		
	}
	
	private String parseContextPath(String path){
		
		String[] tokens = path.split("/");
		String context ="/";
		if(tokens.length>1)
			context = "/"+tokens[1];
		return context;
		
	}
	
	private String parseRequestPath(String path)throws MalformedURLException{
			
		if(path.startsWith("http:") || path.startsWith("https:")){
			URL u = new URL(path);
			path = u.getPath();
		
		}
		
		URI ur = URI.create(path);
		setQueryString(ur.getQuery());
		return ur.getPath();
					
	}
	
	public boolean isValidHostHeader(){
		
		return (getHeaders().contains(HeaderConstants.HEADER_HOST) 
				&& 
				!getHeaders().getHeader(HeaderConstants.HEADER_HOST).equals(" "));
		
	}
	
	public boolean isNewHttpVersion(){
		
		if(protocol!=null)
			return protocol.equalsIgnoreCase(Constants.HTTP_VERSION_1_1);
		else
			return false;
	}
	
	public OutputStream getOutputStream() throws IOException{
		return this.socket.getOutputStream();
	}
	
	private Cookie[] parseCookies(String header){
		
		String[] tokens = header.split(";");
		Cookie[] cookies = new Cookie[tokens.length];
		int i=0;
		for(String token: tokens){
			String[] values = token.split("=");
			if(values[0].trim().equalsIgnoreCase(Constants.COOKIE_SESSION_ID)){
				setSessionId(values[1].trim());
			}
			Cookie cook = new Cookie(values[0], values[1]);
			cookies[i] = cook;
			i++;
		}
		return cookies;
	}
	
	private void updateParameters(){
		
		String qString = this.getQueryString();
		if(qString!=null){
		String[] params = qString.split("&");
		for(String param:params){
			String[] paramPair = param.split("=");
			this.parameterMap.put(paramPair[0], paramPair[1]);
		}
		}
		
	}
	
	private void parsePostParams(String params){
		
		String qString = params;
		if(qString!=null){
		String[] parameters = qString.split("&");
		for(String param:parameters){
			String[] paramPair = param.split("=");
			this.parameterMap.put(paramPair[0], paramPair[1]);
		}
		}
		
	}
	
}
