package edu.upenn.cis.cis455.webserver;

import org.apache.log4j.Logger;
import edu.upenn.cis.cis455.webserver.util.Constants;

class HttpServer {
  static final Logger LOG = Logger.getLogger(HttpServer.class);
	
  public static void main(String args[])
  {
	  
	  try{
		  //Launch the server after parsing the arguments
		  Launcher launch = new Launcher();
		  if(!launch.initialize(args))
			  System.out.println(Constants.INVALID_ARGS+Constants.HELP_STRING);
		  
	  }catch(Exception e){
		  
		  LOG.debug("Exception while Launching");
		  
	  }
	  
  }
  
}
