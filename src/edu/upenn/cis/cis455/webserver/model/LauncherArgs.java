package edu.upenn.cis.cis455.webserver.model;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.util.Constants;

public class LauncherArgs {
	
	private int threadCount=0;
	private int portNum=Constants.DEFAULT_SERVER_PORT;
	private String rootFolder = Constants.DEFAULT_ROOT_FOLDER;
	private String webXmlPath = Constants.DEFAULT_CONF_LOCATION;
	private ServerContext context;
	
	
	
	public String getWebXmlPath() {
		return webXmlPath;
	}

	public void setWebXmlPath(String webXmlPath) {
		this.webXmlPath = webXmlPath;
	}

	public ServerContext getContext() {
		return context;
	}

	public void setContext(ServerContext context) {
		this.context = context;
	}

	public String getRootFolder(){
		return rootFolder;
	}

	public void setRootFolder(String rootFolder){
		this.rootFolder = rootFolder;
		
	}
	
	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	

}
