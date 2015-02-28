package edu.upenn.cis.cis455.webserver;
import org.apache.log4j.Logger;

import java.io.File;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.server.Server;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Utils;

public class Launcher {
	static final Logger LOG = Logger.getLogger(Launcher.class);
	private LauncherArgs launch;
	private static ServerContext context;
	
	public Launcher(){
		
		launch = new LauncherArgs();
		context = ServerContext.getInstance();
	}
	
	public ServerContext getServerContext(){
		return context;
	}
//Initialize and launch server	
	boolean initialize(String[] args){
		
		populateDefaultLauncher();
		
		if(args!=null && args.length!=0)
		{	
			
			if(!validateAndPopulateArguments(args))
				return false;
			
			
			
		}else{
			System.out.println("No arguments provided.");
			System.out.println("Launching server with default arguments.");
			System.out.println("Default Port:"+launch.getPortNum()+"\nDefault folder:"+launch.getRootFolder()
					+"\nDefault Web.xml location:"+launch.getWebXmlPath()
					);
		}
		 populateServerContext();
		 launch();
		
		return true;
		
	}
	 
	private void launch(){
		
		Server server = new Server(launch);
		
		try{
		
			server.launchServer();
		
		}catch(Exception e){
			
			
				server.shutdown();
				
			LOG.debug("Unexpected Exception during request processing");
		}
	}
	
	private  boolean validateAndPopulateArguments(String[] args){
		
			for(int i=0;i<args.length;i+=1){
				String arg = args[i];
				
				if(arg.toLowerCase().equalsIgnoreCase(Constants.THREAD_COUNT_ARGUMENT)){
					i++;
					int count = Integer.parseInt(args[i]);
					if(isValidCount(count))
						launch.setThreadCount(count);
					else
						return false;
				}
				if(i==0)
				{					
					int port  = Integer.parseInt(arg);
					if(isValidPort(port) & Utils.isPortAvailable(port))
						launch.setPortNum(port);
					else
						{
							System.out.println("Either the port is not valid or not available");
							return false;
						}
				}
				
				if(i==1){
					
					File f = new File(arg);
					if(!f.isDirectory()){
						System.out.println("Root folder is not a valid directory");
						return false;
					}
					else
						launch.setRootFolder(arg);
					
				}
				
				if(i==2){
					
					File f = new File(arg);
					if(!f.exists()){
						System.out.println("Location of web.xml is invalid");
						return false;
					}else{
						launch.setWebXmlPath(arg);
					}
					
				}
			}
		return true;
	}
	
	private boolean isValidCount(int count){
		
		return (count<Constants.MAX_THREAD_COUNT && count>0);
				
	} 
	
	private boolean isValidPort(int port){
		
		return !(port > Constants.MAX_PORT_NUM || port < Constants.MIN_PORT_NUM); 
	    	
	}
	
	private void populateDefaultLauncher(){
		
		launch.setThreadCount(Constants.DEFAULT_THREAD_COUNT);
		launch.setPortNum(Constants.DEFAULT_SERVER_PORT);
		File def = new File(Constants.DEFAULT_ROOT_FOLDER);
		if(!def.exists())
			def.mkdirs();
		launch.setRootFolder(Constants.DEFAULT_ROOT_FOLDER);
		launch.setWebXmlPath(Constants.DEFAULT_CONF_LOCATION);
		
	}
	
	private void populateServerContext(){
		
		context.setPortNum(launch.getPortNum());
		context.setRootFolder(launch.getRootFolder());
	
		launch.setContext(context);
	}
	
	
}
