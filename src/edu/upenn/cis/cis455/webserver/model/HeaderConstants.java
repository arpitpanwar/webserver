package edu.upenn.cis.cis455.webserver.model;

public class HeaderConstants {
	
	public static final int STATUSCODE_BADREQ_VAL=400;
	public static final int STATUSCODE_NOTFOUND_VAL=404;
	public static final int STATUSCODE_SERVER_ERROR_VAL=500;
	public static final int STATUSCODE_OK_VAL=200;
	public static final int STATUSCODE_CONTINUE_VAL=100;
	public static final int STATUSCODE_FORBIDDEN_VAL=403;
	public static final int STATUSCODE_NOT_SUPPORTED_VAL=405;
	public static final int STATUSCODE_NOT_MODIFIED_VAL=304;
	public static final int STATUSCODE_PRECONDITION_FAILED_VAL=412;
	
	
	public static final String HEADER_ACCEPT="Accept";
	public static final String HEADER_ACCEPT_LANGUAGE="Accept-Language";
	public static final String HEADER_ALLOW = "Allow";
	public static final String HEADER_CONNECTION="Connection";
	public static final String HEADER_CACHE_CONTROL = "Cache-Control";
	public static final String HEADER_COOKIE = "Cookie";
	public static final String HEADER_CONTENT_TYPE = "Content-type";
	public static final String HEADER_CONTENT_LENGTH ="Content-Length";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";	
	public static final String HEADER_CONTENT_LANGUAGE="Content-Language";
	public static final String HEADER_DATE = "Date";
	public static final String HEADER_FROM = "From";
	public static final String HEADER_HOST="Host";
	public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String HEADER_IF_UNMODIFIED_SINCE="If-Unmodified-Since";
	public static final String HEADER_LOCATION="Location";
	public static final String HEADER_ORIGIN = "Origin";
	public static final String HEADER_SET_COOKIE="Set-Cookie";
	public static final String HEADER_TRANSFER_ENCODING="Transfer-Encoding";
	public static final String HEADER_USER_AGENT="User-Agent";	
	public static final String HEADER_EXPECT="Entry";
	public static final String HEADER_EXPECT_MESSAGE="100- continue";
	
	public static final String STATUSCODE_BADREQ="400";
	public static final String STATUSCODE_NOTFOUND="404";
	public static final String STATUSCODE_SERVER_ERROR="500";
	public static final String STATUSCODE_OK="200";
	public static final String STATUSCODE_CONTINUE="100";
	public static final String STATUSCODE_FORBIDDEN="403";
	public static final String STATUSCODE_FOUND="302";
	public static final String STATUSCODE_NOT_SUPPORTED="405";
	public static final String STATUSCODE_NOT_MODIFIED="304";
	public static final String STATUSCODE_PRECONDITION_FAILED="412";
		
	public static final String STATUSCODE_BADREQ_MESSAGE="Bad Request";
	public static final String STATUSCODE_NOTFOUND_MESSAGE="Not Found";
	public static final String STATUSCODE_SERVER_ERROR_MESSAGE="Internal Server Error";
	public static final String STATUSCODE_OK_MESSAGE="Ok";
	public static final String STATUSCODE_CONTINUE_MESSAGE="Continue";
	public static final String STATUSCODE_FORBIDDEN_MESSAGE="Forbidden";
	public static final String STATUSCODE_FOUND_MESSAGE="Found";
	public static final String STATUSCODE_NOT_SUPPORTED_MESSAGE="Method Not Allowed";
	public static final String STATUSCODE_NOT_MODIFIED_MESSAGE="Not Modified";
	public static final String STATUSCODE_PRECONDITION_FAILED_MESSAGE="Precondition Failed";

	
	public static final String[] SUPPORTED_METHODS = {"HEAD","GET","POST"};
	
}