package edu.upenn.cis.cis455.webserver.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

/**
 * Blocking queue which queues in the incoming requests.
 * The interface is similar to java.util.concurrent.LinkedBlockingQueue
 * @author cis455
 *
 * @param <item>
 */
public class BlockingQueue<item> {
	
	private final Lock offerLock = new ReentrantLock();
	private final Lock takeLock = new ReentrantLock();
	private final Condition notEmpty = takeLock.newCondition();
	private final Condition notFull = offerLock.newCondition();
	private final AtomicLong count = new AtomicLong();
	private long capacity;
	private ArrayList<item> items;
	
	public BlockingQueue(){
			
		this(Constants.MAX_QUEUE_SIZE);
				
	}
	
	public BlockingQueue(long capacity){
		
		this.capacity = capacity;
		items = new ArrayList<item>();
	}
	
	
	public boolean offer(item i) {
		
		final AtomicLong count = this.count;
		if(count.get() == capacity)
			return false;
		long c =-1;
		final Lock l = this.offerLock;
		l.lock();
		try{
			
			if(count.get() < capacity)
				insert(i);
			c = count.getAndIncrement();
			if(c+1 < capacity)
				notFull.signal();
			
		}finally{
				
			l.unlock();
			if(c==0)
				signalNotEmpty();
		}
		
						
		return c>=0;
	}
	
	public boolean add(item i) throws IllegalStateException{
		
		if(offer(i))
			return true;
		else
			throw new IllegalStateException("Queue at full capacity");
			
	}
	
	public item take() throws InterruptedException{
		
		final Lock l = this.takeLock;
		final AtomicLong count = this.count;
		item i;
		long c =-1;
		l.lockInterruptibly();
		try{
			while(count.get() == 0)
				notEmpty.await();
			 i = remove();
			 c = count.getAndDecrement();
			 if(c>1)
				 notEmpty.signal();
			
		}finally{
			
			l.unlock();
			if(c==capacity)
				signalNotFull();
		}
		 
		return i;
	}
	
	public void clear(){
		offerLock.lock();
		takeLock.lock();
		
		items.clear();
		
		offerLock.unlock();
		takeLock.unlock();
					
	}
	
	private void signalNotEmpty(){
		final Lock l = this.takeLock;
		l.lock();
		try{
			notEmpty.signal();
		}finally{
			l.unlock();
		}
	}
	
	private void signalNotFull(){
		final Lock l = this.offerLock;
		l.lock();
		try{
			notFull.signal();
		}finally{
			l.unlock();
		}
		
	}
	
	private void insert(item i){
		
		items.add(i);	
	}
	
	private item remove(){
		
		return items.remove(0);
	}
	
	
}
