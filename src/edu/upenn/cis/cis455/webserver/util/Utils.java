package edu.upenn.cis.cis455.webserver.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.UID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.pool.ThreadPool;
import edu.upenn.cis.cis455.webserver.pool.ThreadPool.Worker;

public class Utils {
	
	static final Logger LOG = Logger.getLogger(Utils.class);
	
	public static boolean isPortAvailable(int port){
			
			if (port > Constants.MAX_PORT_NUM || port < Constants.MIN_PORT_NUM) {
		        throw new IllegalArgumentException("Invalid start port: " + port);
		    }
	
		    ServerSocket ss = null;
		    try {
		        ss = new ServerSocket(port);
		        ss.setReuseAddress(true);
		        return true;
		    } catch (IOException e) {
		    } finally {
		        if (ss != null) {
		            try {
		                ss.close();
		            } catch (IOException e) {
		                
		            }
		        }
		    }
	
			return false;
			
		}
	
	public static String getFormattedDate(){
		return getFormattedDate(new Date().getTime());
	}
	
	public static String getFormattedDate(long time){
		String date="";
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = sdf.format(new Date(time));
				
		return date;
	}
	
	public static long getTimeFromString(String date){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			Date dt = sdf.parse(date);
			return dt.getTime();
		}catch(ParseException pe){
			LOG.debug("Unable to parse date:"+date+"\n"+pe.getMessage());
		}
		return 0;
	}
	
	public static boolean isResourceModified(String path, String date){
		boolean modified = false;
		Date requestedDate = new Date();
		boolean fetched = false;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		
		try{
		requestedDate =	sdf.parse(date);
		fetched = true;
		}catch(ParseException pe){
			LOG.debug("Unable to parse date string");
		}
		if(!fetched){
			SimpleDateFormat format2 = new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss z");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			try{
				requestedDate = format2.parse(date);
				fetched = true;
			}catch(ParseException pe){
				LOG.debug("Unable to parse date string");
			}
		}
		if(!fetched){
			SimpleDateFormat format3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			try{
				requestedDate = format3.parse(date);
				fetched = true;
			}catch(ParseException pe){
				LOG.debug("Unable to parse date string");
			}
		}
		if(!fetched)
			return true;
		File f = new File(path);
		long dateMod =  f.lastModified();
		modified = dateMod > requestedDate.getTime();		
		return modified;
	}
	
	
	public static InputStream getStreamFromString(String s){
		
		return new ByteArrayInputStream(s.getBytes());
		
	}
	
	public static HashMap<String,Object> getStreamFromResource(String path)throws FileNotFoundException{
		
		return getStreamFromResource(new File(path));
	}
	
	public static HashMap<String,Object> getStreamFromResource(File f)throws FileNotFoundException{
		
		InputStream stream = null;
		long size = 0;
		String type="";
		HashMap<String,Object> streamInfo = new HashMap<String,Object>();
		if(!f.isDirectory() && f.exists() )
		{
			stream = new BufferedInputStream(new FileInputStream(f));
			
			size = f.length();
			try{
				
				type = Files.probeContentType(f.toPath());
			}catch(IOException ioe){
				LOG.debug("Unable to probe content type of the file: "+ioe.getMessage());
			}
		}
		else{
			if(f.isDirectory() && f.exists()){
				
				String directory[] = f.getAbsolutePath().split(Pattern.quote(File.separator));
				String name = directory[directory.length-1];
				String data =	readFileFromDisk(new File(Constants.DIRECTORY_LISTING_FILE))
						.replace(Constants.REPLACE_DIRECTORY_LISTING, generateDirectoryListing(f))
						.replace(Constants.REPLACE_DIRECTORY_PATH,name);
				stream = getStreamFromString(data);
				size = data.length();
				type = "text/html";
			}
		}
		streamInfo.put(Constants.CONTENT_STREAM, stream);
		streamInfo.put(Constants.CONTENT_LENGTH, size);
		streamInfo.put(Constants.CONTENT_TYPE, type);
		return streamInfo;
		
	}
	
	private static String readFileFromDisk(File f)throws FileNotFoundException{
		StringBuffer sb = new StringBuffer();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		try{
			String read="";
			while((read = br.readLine())!=null){
				sb.append(read);
			}
			br.close();
		}catch(IOException ioe){
			LOG.debug("Unable to read file from disk "+ioe.getMessage());
		}finally{
			if(br != null){
				try{
					br.close();
				}catch(IOException ioe){
					
				}
			}
			
		}
		
		return sb.toString();
	}
	
	public static String generateDirectoryListing(File f){
		StringBuffer sb = new StringBuffer();
		if(f.isDirectory()){
			
			File[] files = f.listFiles();
			sb.append("<ul>");
			
			for(File fle : files){
				sb.append("<li><a href=\""+f.toURI().relativize(fle.toURI()).getPath()+"\">"+fle.getName()+"</a></li><br />");
			}
			sb.append("</ul>");
		}
		
		return sb.toString();
	}
	
	public static InputStream generateBadResponseBody(){
		
		String file = Constants.BAD_REQUEST_FILE;
		InputStream stream = null;
		
		try{
			
			stream = new FileInputStream(new File(file));
			
		}catch(IOException ioe){
			
			LOG.debug("Unable to read badrequest html file. "+ioe.getMessage());
			
		}
		
		return stream;
		
	}
	
	public static InputStream generateNotFoundResponseBody(){
	
		String file = Constants.NOT_FOUND_FILE;
		
		InputStream stream = null;
		
		try{
			
			stream = new FileInputStream(new File(file));
			
		}catch(IOException ioe){
			
			LOG.debug("Unable to read notfound html file. "+ioe.getMessage());
			
		}
		
		return stream;
		
		
	}
	
	public static InputStream generateMethodNotSupportedBody(){
		
		String file = Constants.METHOD_NOT_SUPPORTED_FILE;
		
		InputStream stream = null;
		
		try{
			
			stream = new FileInputStream(new File(file));
			
		}catch(IOException ioe){
			
			LOG.debug("Unable to read methodnotsupported html file. "+ioe.getMessage());
			
		}
		
		return stream;
		
		
	}
	
public static InputStream generateServerErrorBody(){
		
		String file = Constants.SERVER_ERROR_FILE;
		
		InputStream stream = null;
		
		try{
			
			stream = new FileInputStream(new File(file));
			
		}catch(IOException ioe){
			
			LOG.debug("Unable to read servererror html file. "+ioe.getMessage());
			
		}
		
		return stream;
		
		
	}

public static InputStream generateForbiddenBody(){
	
	String file = Constants.FORBIDDEN_FILE;
	
	InputStream stream = null;
	
	try{
		
		stream = new FileInputStream(new File(file));
		
	}catch(IOException ioe){
		
		LOG.debug("Unable to read forbidden html file. "+ioe.getMessage());
		
	}
	
	return stream;
	
	
}


private static String generateLogFileLines(){
	String logData="";
	try{
		logData = readFileFromDisk(new File(Constants.LOG_FILE));
	}catch(FileNotFoundException fnfe){
		return "";
	}
	
	return logData;
}


private static String generateThreadPoolTable(ThreadPool pool){
	StringBuilder sb = new StringBuilder();
	String fileDisk="";
	try{
		fileDisk = readFileFromDisk(new File(Constants.CONTROLS_FILE));
	}catch(FileNotFoundException fne){
		return "";
	}
	String logData = generateLogFileLines();
	Worker[] workers = pool.getWorkers();
	int count=1;
	for(Worker w:workers){
		sb.append("<tr><td>Worker-"+count+"</td><td>"+w.getStatus()+"</td></tr>");
		count++;
	}
	
	fileDisk = fileDisk.replace(Constants.REPLACE_CONTROL_TABLE, sb.toString());
	fileDisk = fileDisk.replace(Constants.REPLACE_LOG_CONTENTS, logData);
	return fileDisk;
	
}

public static HashMap<String, Object> generateControlData(ThreadPool pool)throws IOException{
	HashMap<String,Object> values = new HashMap<String, Object>();
	String val = generateThreadPoolTable(pool);
	long len = val.length();
	String contenttype = Files.probeContentType(Paths.get(Constants.CONTROLS_FILE));
	values.put(Constants.CONTENT_STREAM, getStreamFromString(val));
	values.put(Constants.CONTENT_LENGTH,len);
	values.put(Constants.CONTENT_TYPE,contenttype);
	
	return values;
}

public static HashMap<String,Object> getErrorPageInfo(String error) throws FileNotFoundException{
	
	switch(error){
		case Constants.BAD_REQUEST_STRING:
			return getStreamFromResource(Constants.BAD_REQUEST_FILE);
		case Constants.NOT_FOUND_STRING:
			return getStreamFromResource(Constants.NOT_FOUND_FILE);
		case Constants.FORBIDDEN_STRING:
			return getStreamFromResource(Constants.FORBIDDEN_FILE);
		case Constants.SERVER_ERROR_STRING:
			return getStreamFromResource(Constants.SERVER_ERROR_FILE);
		case Constants.METHOD_NOT_SUPPORTED_STRING:
			return getStreamFromResource(Constants.METHOD_NOT_SUPPORTED_FILE);
		default:
			return null;
	}
		
}

public static boolean isCharsetSupported(String name) {
	  try {
	    Charset.forName(name);
	    return true;
	  } catch (IllegalArgumentException e) {
	    return false;
	  }
}

public static String generateUniqieId(){
	
	String id = null;
	
	UID uuid = new UID();
	id = uuid.toString();
	return id;
}

public static String getParentFolder(String path){
	
	File f = new File(path);
	return f.getParent();
	
}

public static boolean isSessionValid(HttpSession session){
	
	boolean isValid = true;
	
	if(session.getLastAccessedTime()-session.getCreationTime()>0){
		if(session.getLastAccessedTime()-session.getCreationTime() > session.getMaxInactiveInterval()*1000){
			isValid = false;
		}
	}
	
	return isValid;
	
}


public static String capitalizeString(String string) {
	  char[] chars = string.toLowerCase().toCharArray();
	  boolean found = false;
	  for (int i = 0; i < chars.length; i++) {
	    if (!found && Character.isLetter(chars[i])) {
	      chars[i] = Character.toUpperCase(chars[i]);
	      found = true;
	    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'' || chars[i]=='-' ) { // You can add other chars here
	      found = false;
	    }
	  }
	  return String.valueOf(chars);
	}
	
	
}
