package base;

import com.lmax.disruptor.EventHandler;
/**
 * �൱��һ��������
 * @author whl
 *
 */
public class LongEventHandler implements EventHandler<LongEvent> {

	public void onEvent(LongEvent event, long arg1, boolean arg2) throws Exception {
		System.out.println("����="+event.getValue());
	}

}
