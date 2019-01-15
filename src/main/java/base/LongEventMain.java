package base;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class LongEventMain {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		ExecutorService pool = Executors.newCachedThreadPool();
		LongEventFactory eventFactory = new LongEventFactory();

		int ringbufferSize = 1024 * 1024;

		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory, ringbufferSize, pool);

		LongEventHandler longEventHandler = new LongEventHandler();
		disruptor.handleEventsWith(longEventHandler);

		disruptor.start();

		// 从disruptor中获取ringbuffer用来发布事件
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		LongEventProducer producer = new LongEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; l<20; l++) {
			bb.putLong(0, l);
			producer.onData(l);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("-----------------------------");
	}
}