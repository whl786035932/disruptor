package generate2;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import base.LongEvent;

public class Handler2 implements WorkHandler<LongEvent>,EventHandler<LongEvent>{

	public void onEvent(LongEvent event) throws Exception {
		String newName = event.getName()+"c2";
		event.setName(newName);
		System.out.println("C2消费"+event.getName());
	}

	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		String newName = event.getName()+"c2";
		event.setName(newName);
		System.out.println("C2消费 --eventhandler---"+event.getName());
	}

}
