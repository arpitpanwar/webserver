package edu.upenn.cis.cis455.webserver.pool;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis455.webserver.exception.ShutdownException;
import edu.upenn.cis.cis455.webserver.handlers.HttpRequestHandler;
import edu.upenn.cis.cis455.webserver.util.*;

/**
 * Threadpool for handling requests
 * @author cis455
 *
 */

public class ThreadPool {
	static final Logger LOG = Logger.getLogger(ThreadPool.class);

	private BlockingQueue<Object> queue;
	private int threadCount;
	private Worker[] workers;
	private Thread[] workerThreads;
	private volatile boolean shutdown = false;
	private static final Object KILL_ME = new Object();
	private boolean shutdownRequested = false;
	
	public ThreadPool(int count) throws IllegalArgumentException{
		
		if(isValidCount(count))
			this.threadCount = count;
		else
			throw new IllegalArgumentException("Invalid Thread Count");
		
		workers = new Worker[count];
		workerThreads = new Thread[count];
		queue = new BlockingQueue<Object>();
		startThreads();
	}
	
	public boolean execute(Callable<Object> task){
		
		return this.queue.offer(task);
		
	}
	
	private void startThreads(){
		
		synchronized (this) {
		
			for(int i=0;i<threadCount;i++){
				
				this.workers[i] = new Worker();
				this.workerThreads[i] = new Thread(workers[i]);
				this.workerThreads[i].start();
			}
		}
	}
	
	public synchronized boolean shutDown(){
		
		this.shutdown = true;
		this.queue.add(KILL_ME);
			try{
				for(Worker w: workers){
					
					w.thread.interrupt();
					
				}
				
				queue.clear();
				
				for(Thread workerThread : workerThreads){
					
					workerThread.interrupt();
					
				}
				
				return true;
				
			}catch(Exception e){
				LOG.debug(Thread.currentThread()+": Worker Thread Shutdown Failed");
				return false;
				
			}
		
	}
	
	private void throwException(){
		throw new ShutdownException();
	}
	
	private boolean isValidCount(int count){
		
		if(count<Constants.MAX_THREAD_COUNT && count>0)
			return true;
		else
			return false;
		
	} 
	
	public Thread[] getWorkerThreads(){
		return workerThreads;
	}
	
	public Worker[] getWorkers(){
		return workers;
	}
	
	public boolean isShutdownRequested() {
		return shutdownRequested;
	}

	public void setShutdownRequested(boolean shutdownRequested) {
		this.shutdownRequested = shutdownRequested;
	}
/**
 * Worker class which executes the task
 * @author cis455
 *
 */
	 public class Worker implements Runnable{

		private final Thread thread;
		private String status = "Waiting"; 		
		
		public String getStatus(){
			return status;
		}
		
		private void setStatus(String s){
			this.status = s;
		}
		
		private Worker(){
			this.thread = Thread.currentThread();
		}
		
		@Override
		public void run() {
			try{
			 while(!shutdown){
					try{
						    Object r = queue.take();
							if(r == KILL_ME)
								return;
							else
								if(r instanceof Callable)
								{	
									if(r instanceof HttpRequestHandler)
									{
										HttpRequestHandler req = (HttpRequestHandler) r;
										setStatus(req.getInitialReq().getRequestPath());
									}
									@SuppressWarnings("unchecked")
									Object  o = ((Callable<Object>) r).call();
									if(o==null){
										System.out.println("Null");
									}
									if(o instanceof String)
									{	String s = (String) o;
										if(s.equalsIgnoreCase(Constants.SHUTDOWN)){
											setShutdownRequested(true);
										}
									}		
									setStatus(Constants.THREAD_WAITING);
								}
								else
									LOG.error("Invalid Object received "+r.getClass()+".Only Runnable expected");
						   						
					}catch(InterruptedException interrupt){
						if(thread.isInterrupted())
							return;
					    LOG.debug("Interrupted exception in: "+thread.getName());
					}				
				}
			}catch(Exception intr){
				if(intr instanceof ShutdownException)
					throwException();
			
				this.thread.interrupt();
				
			}finally{
				
				this.thread.interrupt();
			}
		}
		
	}

}
