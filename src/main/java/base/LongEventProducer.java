package base;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;


public class LongEventProducer {
	
	RingBuffer<LongEvent> ringbuffer;
	
	
	public LongEventProducer(RingBuffer<LongEvent> ringbuffer) {
		this.ringbuffer = ringbuffer;
	}

	/**
	 * 
	 * onData用来发布事件，每调用一次就发布一次事件
	 * @param bb
	 */
	public void onData(long bb) {
		long sequence = ringbuffer.next();
		try {
			
			LongEvent longEvent = ringbuffer.get(sequence);
			longEvent.setValue(bb);
		}finally {
			ringbuffer.publish(sequence);
		}
		
	}
}
