package edu.upenn.cis.cis455.webserver.handlers;

import java.util.concurrent.Callable;

import edu.upenn.cis.cis455.webserver.context.ServerContext;

public abstract class RequestHandler implements Handler,Callable<Object>  {

	private ServerContext context;
	
	public void init(ServerContext context){
		this.context = context;
	}
	
	public ServerContext getServerContext(){
		return context;
	}
	
}
