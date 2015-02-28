package edu.upenn.cis.cis455.webserver.util;

import java.io.File;

/**
 * Constants being used all over
 * @author cis455
 *
 */
public class Constants {

	public static long MAX_QUEUE_SIZE = Integer.MAX_VALUE-1;	
	
	public static int DEFAULT_THREAD_COUNT = Integer.parseInt(SystemProperties.load().getProperty("server.threadcount","50"));
	public static int DEFAULT_SERVER_PORT = Integer.parseInt(SystemProperties.load().getProperty("server.defaultport","60000"));
	public static int MAX_PORT_NUM = 65000;
	public static int MIN_PORT_NUM = 80;
	public static int MAX_THREAD_COUNT = 20000;
	public static int DEFAULT_OUTPUT_BUFFER_SIZE = 1024*4;
	
	public static final String PROP_FILE = "/conf/userconfig.properties";
	public static final String DEFAULT_ROOT_FOLDER = System.getProperty("user.dir")+File.separator+"www";
	public static final String DEFAULT_CONF_LOCATION="conf/web.xml";
	public static final String MATCH_ALL="/*";
	public static final String THREAD_COUNT_ARGUMENT = "-t";
	public static final String INVALID_ARGS = "Invalid arguments received\n";
	public static String HELP_STRING="Usage:\n"
			+"java -cp workspace/hw2 edu.cis.upenn.edu.cis455.hw2.HttpServer [options]\n"
			+ "Valid Options:\n"
			+"PortNum* \t: Port Number to be used for launching server ("+ MIN_PORT_NUM +"-"+MAX_PORT_NUM+")\n"
			+"RootFolder*\t: Root folder to be used by the server for serving request.Defaults to "
			+DEFAULT_ROOT_FOLDER+"\n"
			+ "-t Count\t: Number of threads to use for handling request(1-"+MAX_THREAD_COUNT+")\n"
			+"* Mandatory options";
	public static final String REPLACE_DIRECTORY_PATH="%%Replace Directory Here%%";
	public static final String REPLACE_DIRECTORY_LISTING="%%Replace Listing Here%%";
	public static final String REPLACE_CONTROL_TABLE="%%Replace Table Here%%";
	public static final String REPLACE_LOG_CONTENTS="%%Place LOG Contents%%";
	public static final String CONTENT_LENGTH="content-length";
	public static final String CONTENT_STREAM="content-stream";
	public static final String CONTENT_TYPE="content-type";
	public static final String SERVER_INFO="Homework-1_Milestone_2";
	public static final String DEFAULT_CONTEXT="/";
	
	public static final String THREAD_WAITING="Waiting";
	public static final String SHUTDOWN="Shut it down";
	
	public static final String PROTOCOL_HEAD = "HEAD";
	public static final String PROTOCOL_GET = "GET";
	public static final String PROTOCOL_POST = "POST";
	public static final String PROTOCOL_DELETE = "DELETE";
	public static final String RESPONSE_PERSISTENT_STRING="HTTP/1.1 100 Continue";
	public static final String HTTP_VERSION_1_1 = "HTTP/1.1";
	public static final String HTTP_VERSION_1 ="HTTP/1.0";
	
	public static final String BAD_REQUEST_STRING="bad request";
	public static final String NOT_FOUND_STRING="not found";
	public static final String METHOD_NOT_SUPPORTED_STRING="not supported";
	public static final String SERVER_ERROR_STRING="server error";
	public static final String FORBIDDEN_STRING="forbidden";
	
	public static final String BAD_REQUEST_FILE = "src/edu/upenn/cis/cis455/webserver/util/staticcontent/badrequest.html";
	public static final String NOT_FOUND_FILE = "src/edu/upenn/cis/cis455/webserver/util/staticcontent/notfound.html";
	public static final String METHOD_NOT_SUPPORTED_FILE="src/edu/upenn/cis/cis455/webserver/util/staticcontent/notsupported.html";
	public static final String SERVER_ERROR_FILE = "src/edu/upenn/cis/cis455/webserver/util/staticcontent/servererror.html";
	public static final String FORBIDDEN_FILE="src/edu/upenn/cis/cis455/webserver/util/staticcontent/forbidden.html";
	public static final String DIRECTORY_LISTING_FILE = "src/edu/upenn/cis/cis455/webserver/util/staticcontent/listing.html";
	public static final String CONTROLS_FILE = "src/edu/upenn/cis/cis455/webserver/util/staticcontent/control.html";
	public static final String SHUTDOWN_FILE = "src/edu/upenn/cis/cis455/webserver/util/staticcontent/shutdown.html";
	public static final String LOG_FILE=System.getProperty("user.home")+File.separator+"test.log";

	public static final String SPECIAL_PATH_SHUTDOWN="/shutdown";
	public static final String SPECIAL_PATH_CONTROl="/control";
	
	public static final String DEFAULT_ENCODING="ISO-8859-1";
	public static final String DEFAULT_SCHEME="http";
	
	public static final String COOKIE_SESSION_ID="JSESSIONID";
}
