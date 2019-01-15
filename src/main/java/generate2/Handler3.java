package generate2;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import base.LongEvent;

public class Handler3 implements EventHandler<LongEvent>{

	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		String name = event.getName()+"c3";
		event.setName(name);
		System.out.println("C3ฯ๛ทั --eventhandler---"+event.getName());
	}
}
