package generate1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;

import base.LongEvent;
/**
 * 使用workpool 消息处理器
 * @author 10297
 *
 */
public class Main2 {
	public static <T> void main(String[] args) throws InterruptedException {
		int bufferSize = 1024;
		int THREAD_NUMBERS = 4;
		EventFactory<LongEvent> eventFactory = new EventFactory<LongEvent>() {

			public LongEvent newInstance() {
				// TODO Auto-generated method stub
				return new LongEvent();
			}
		};
		RingBuffer<LongEvent> ringBuffer = RingBuffer.createSingleProducer(eventFactory, bufferSize);


		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);
		WorkerPool<LongEvent> workerPool = new WorkerPool<LongEvent>(ringBuffer, sequenceBarrier,
				new IgnoreExceptionHandler(), new WorkHandler<LongEvent>() {

					public void onEvent(LongEvent event) throws Exception {
						System.out.println(Thread.currentThread().getName() + "消费" + event.getValue());
					}
				});
		workerPool.start(executor);
		Thread.sleep(1000);
		// 生产者
		for (int i = 0; i < 10; i++) {
			long sequence = ringBuffer.next();
			LongEvent event = ringBuffer.get(sequence);
			event.setValue(i);
			ringBuffer.publish(sequence);
		}
		workerPool.halt();
		executor.shutdown();
	}
}
