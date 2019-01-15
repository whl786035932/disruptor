package generate1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

import base.LongEvent;
/**
 * 使用EventProcessor 消息处理器
 * @author 10297
 *
 */
public class Main1 {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int bufferSize = 1024;
		int thread_nums = 4;

		final RingBuffer<LongEvent> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<LongEvent>() {
			public LongEvent newInstance() {
				return new LongEvent();
			}
		}, bufferSize, new YieldingWaitStrategy());

		ExecutorService executor = Executors.newFixedThreadPool(thread_nums);

		// 之前是由disruptor指定EventHandler,在这里模拟不适用Disruptor，只使用Ringbuffer,
		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

		// 创建消费者，在Disruptor中消费者是以Eventprocessor 形式创建的
		BatchEventProcessor<LongEvent> transProcessor = new BatchEventProcessor<LongEvent>(ringBuffer, sequenceBarrier,
				new EventHandler<LongEvent>() {

					public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName()+"消费" + event.getValue());
					}
				});

		executor.submit(transProcessor);

		Future<LongEvent> future = executor.submit(new Callable<LongEvent>() {

			public LongEvent call() throws Exception {
				long seq;
				for (int i = 0; i < 10; i++) {
					seq = ringBuffer.next();// 占个坑 --ringBuffer一个可用区块
					ringBuffer.get(seq).setValue(i);// 给这个区块放入 数据
					ringBuffer.publish(seq);// 发布这个区块的数据使handler(consumer)可见
				}
				return null;
			}
		});
		
		future.get(); // 等待生产者结束
		Thread.sleep(1000);// 等上1秒，等消费都处理完成
		transProcessor.halt(); /// 通知事件(或者说消息)处理器 可以结束了（并不是马上结束!!!）
		executor.shutdown();

	}

}
