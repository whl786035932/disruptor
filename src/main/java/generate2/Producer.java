package generate2;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import base.LongEvent;

public class Producer implements Runnable {
	private Disruptor disruptor;
	private CountDownLatch countDownlatch;
	public void run() {
		for (int i = 0; i < 1; i++) {
			LongEventMainWithTranslator translator = new LongEventMainWithTranslator(i);
			disruptor.publishEvent(translator);
		}
		countDownlatch.countDown();
	}
	public Producer(Disruptor disruptorParam) {
		this.disruptor= disruptorParam;
	}
	public Producer(Disruptor<LongEvent> disruptor, CountDownLatch countDownLatch) {
		this.disruptor= disruptor;
		this.countDownlatch= countDownLatch;
	}
}

class LongEventMainWithTranslator  implements EventTranslator<LongEvent>{
	private int value;
	public void translateTo(LongEvent event, long sequence) {
		event.setValue(value);
	}
	
	public LongEventMainWithTranslator(int value) {
		this.value = value;
	} 
	
}
