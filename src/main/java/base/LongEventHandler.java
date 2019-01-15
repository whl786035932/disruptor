package base;

import com.lmax.disruptor.EventHandler;
/**
 * 相当于一个消费者
 * @author whl
 *
 */
public class LongEventHandler implements EventHandler<LongEvent> {

	public void onEvent(LongEvent event, long arg1, boolean arg2) throws Exception {
		System.out.println("消费="+event.getValue());
	}

}
