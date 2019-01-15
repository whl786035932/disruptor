package generate2;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import base.LongEvent;

public class Handler1 implements EventHandler<LongEvent>,WorkHandler<LongEvent>{

	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		String newName = event.getName()+"c1";
		event.setName(newName);
		System.out.println("C1消费 --eventhandler---"+event.getName());
	}

	public void onEvent(LongEvent event) throws Exception {
		System.out.println("C1消费  onEvent"+event.getValue());
	}

}
