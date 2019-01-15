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
	 * onData���������¼���ÿ����һ�ξͷ���һ���¼�
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
