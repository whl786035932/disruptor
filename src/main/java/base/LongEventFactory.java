package base;

import com.lmax.disruptor.EventFactory;
/**
 * ����һ���¼�������ʵ����Event����
 * @author whl
 *
 */
public class LongEventFactory implements EventFactory<LongEvent>{

	public LongEvent newInstance() {
		return new LongEvent();
	}

}
