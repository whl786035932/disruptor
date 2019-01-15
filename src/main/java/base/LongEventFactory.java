package base;

import com.lmax.disruptor.EventFactory;
/**
 * 定义一个事件工厂来实例化Event对象
 * @author whl
 *
 */
public class LongEventFactory implements EventFactory<LongEvent>{

	public LongEvent newInstance() {
		return new LongEvent();
	}

}
