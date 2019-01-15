package base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class LongEventMainWithTranslator {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		LongEventFactory eventFactory = new LongEventFactory();
		int ringBufferSize = 1024*1024;
		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, executor);
		
		LongEventHandler longEventHandler = new LongEventHandler();
		disruptor.handleEventsWith(longEventHandler);
		disruptor.start();
		
		
		
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
		
		for(int i=0;i<10;i
				++) {
			producer.onData(i);
			Thread.sleep(1000);
		}
	}

}
