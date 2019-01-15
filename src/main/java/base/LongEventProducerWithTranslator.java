package base;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class LongEventProducerWithTranslator {
	private RingBuffer<LongEvent> ringbuffer;
	
	
	
	private EventTranslatorOneArg<LongEvent, Long> translator = new EventTranslatorOneArg<LongEvent, Long>() {

		public void translateTo(LongEvent event, long arg1, Long value) {
			event.setValue(value);
		}
	};
	public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringbuffer) {
		this.ringbuffer = ringbuffer;
	}
	
	
	public void onData(long l) {
		ringbuffer.publishEvent(translator,l);
	}
	
	
	

}
