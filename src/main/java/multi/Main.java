package multi;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {
	
	public static <T> void main(String[] args) throws InterruptedException {
		int bufferSize =1024*1024;
		EventFactory<Order> eventFactory = new EventFactory<Order>() {

			public Order newInstance() {
				return new Order();
			}
		};
		
		RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI	, eventFactory, bufferSize, new YieldingWaitStrategy());
		SequenceBarrier newBarrier = ringBuffer.newBarrier();
		
		/**
		 * 创建三个消费者，交给workerpool
		 */
		Consumer[] consumers = new Consumer[3];
		
		for(int i=0;i<consumers.length;i++) {
			consumers[i]= new Consumer("c"+i);
		}
		
//		WorkerPool<Order> workerPool = new WorkerPool<Order>(eventFactory, new IgnoreExceptionHandler(), consumers);
		WorkerPool<Order> workerPool = new WorkerPool<Order>((RingBuffer<Order>) ringBuffer, newBarrier, new IgnoreExceptionHandler(), consumers);
		ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
		workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
		
		//生产消息
		final CountDownLatch latch = new CountDownLatch(1);
		
		for(int i=0;i<100;i++) {
			final Producer producer = new Producer(ringBuffer);
			new Thread(new Runnable() {
				
				public void run() {
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int j=0;j<100;j++) {
						producer.onData(UUID.randomUUID().toString());
					}
						
					
				}
			}).start();
		}
		
		Thread.sleep(2000);
		System.out.println("开始生产");
		
		latch.countDown();
		Thread.sleep(5000);
		System.out.println("总数"+consumers[0].getCount());
		System.out.println("总数"+consumers[1].getCount());
		System.out.println("总数"+consumers[2].getCount());
		
		
		
	}

}
