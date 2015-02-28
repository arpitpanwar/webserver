package edu.upenn.cis.cis455.webserver.server;

import edu.upenn.cis.cis455.webserver.context.ServerContext;
import edu.upenn.cis.cis455.webserver.exception.ShutdownException;
import edu.upenn.cis.cis455.webserver.handlers.HttpRequestHandler;
import edu.upenn.cis.cis455.webserver.model.LauncherArgs;
import edu.upenn.cis.cis455.webserver.model.servlet.ServletContextImpl;
import edu.upenn.cis.cis455.webserver.pool.ThreadPool;
import edu.upenn.cis.cis455.webserver.util.Constants;
import edu.upenn.cis.cis455.webserver.util.Parser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
/**
 * Server class which launches the and monitors shutdown
 * @author cis455
 *
 */
public class Server {
	static final Logger LOG = Logger.getLogger(Server.class);
	
	private LauncherArgs launchArgs;
	private volatile boolean SERVER_RUNNING= true;
	private List<Thread> runningServers;
	private ThreadPool requestPool;
	private ServerContext context;
	
	public Server(LauncherArgs launcherArgs){
		if(launcherArgs != null)
			this.launchArgs = launcherArgs;
		else
			throw new IllegalArgumentException("Invalid Launcher Arguments Passed");
		
		context = launcherArgs.getContext();
		runningServers = new Vector<>();
		requestPool = new ThreadPool(launchArgs.getThreadCount());
	}
	
	/*Launches the server after loading the servlets by creating a new thread.
	 * Technically multiple instances of the server can be launched using this functions on different ports.
	 * Though this is not exposed yet.
	 */
	public void launchServer(){
		loadServlets();
		context.addThreadPool(getRequestPool());
		try{
			TCPServer serve = new TCPServer();
			Thread t = new Thread(serve);
			runningServers.add(t);
			t.start();
			
			loop:while(true){
			try{	
				if(requestPool.isShutdownRequested()){
					shutdown();
					serve.close();
					break loop;
				}
				Thread.sleep(2000);
				}catch(InterruptedException e){
					LOG.debug("Main thread interrupted");
			 }
			}
			
			
		}catch(IllegalStateException ilse){
			LOG.debug("Illegal state of the server exiting");
			shutdown();
			throw new IllegalStateException("Shutdown everything");
		}catch(Exception e){
			LOG.debug("Unexpected Exception: "+e.getMessage());
			shutdown();
		}

	}
//Shutdown the server
	public void shutdown(){
		
		synchronized (this) {
			try{
			Thread.sleep(3000);
			this.SERVER_RUNNING = false;
			getRequestPool().shutDown();
			destroyServlets();
			for(Thread t : runningServers){
				t.interrupt();
			}
			
								
			}catch(Exception e){
				LOG.debug("Some exception during shutdown."+e.getMessage());
			}	
		}
		
		
	}
	
	public ThreadPool getRequestPool(){
		
		return requestPool;
		
	}
	//Load the servlets
	private void loadServlets(){
		
		String path = this.launchArgs.getWebXmlPath();
		Parser parser = new Parser();
		parser.parse(path);
		ServletLoader loader = new ServletLoader(path);
		context.setServletMap(loader.getServlets(generateServletContext(parser), parser));
		context.addServletContext(Constants.DEFAULT_CONTEXT, generateServletContext(parser));
	} 
	
	private ServletContext generateServletContext(Parser parser){
			ServletContextImpl context = new ServletContextImpl(parser,this.context.getRootFolder());
			return context;
	}
	
	private void destroyServlets(){
		HashMap<String,HttpServlet> servletMap = context.getServletMap();
		for(Map.Entry<String, HttpServlet> servletEntry : servletMap.entrySet()){
			servletEntry.getValue().destroy();
		}
	}

	class TCPServer implements Runnable {
		private ServerSocket server;
		
		public void close()throws IOException{
			if(server!=null)
				server.close();
		}
	
		@Override
		public void run(){
			
			try{
				
				server = new ServerSocket(launchArgs.getPortNum());
				context.setLocalAddress(server.getInetAddress());
				loop:while(SERVER_RUNNING){
					try{
						if(requestPool.isShutdownRequested())
						{
							server.close();
							shutdown();
						}
						Socket request = server.accept();
						
						requestPool.execute(new HttpRequestHandler(request,context));
						if(requestPool.isShutdownRequested())
						{
							shutdown();
							break loop;
						}
						
					}catch(IOException io){
						LOG.debug("IO Exception while handling request");
					}catch(NullPointerException np){
						LOG.debug("Null Socket passed to the request handler");
					}catch(Exception e){
						if (e instanceof SocketException)
						{	System.out.println("Socket Exception");
							break loop;
						}
						LOG.debug("Unexpected exception: "+e.getMessage());
					}
				}
				if(!server.isClosed())
					server.close();
			}catch(IOException ioe){
				LOG.debug("IO Exception while creating server socket");
				throw new IllegalStateException("Socket Malfunction");
			}
			catch(ShutdownException she){
				throw new ShutdownException("Shutdown Requested");
			}catch(Exception e){
				LOG.debug("Unexpected Exception "+e.getMessage());
			}
			finally{
				
				if(server != null){
					try{
						if(!server.isClosed())
							server.close();
					}catch(IOException io){
						LOG.debug("Could not close Server socket..Run for your lives.");
					}
				}
			}
						
		}
				
	}
	
}
