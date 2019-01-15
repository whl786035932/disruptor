package multi;

import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class Consumer implements EventHandler<Order>,WorkHandler<Order> {
	
	private String consumerId;
	private static AtomicInteger count = new AtomicInteger(0);
	public Consumer(String string) {
		this.consumerId = string;
	}

	public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("��ǰ�����ߣ�"+this.consumerId+",������Ϣ"+event.getId());
		count.incrementAndGet();
	}
	
	public int getCount() {
		return count.get();
	}

	public void onEvent(Order event) throws Exception {
		System.out.println("��ǰ�����ߣ�"+this.consumerId+",������Ϣ"+event.getId());
		count.incrementAndGet();
	}
}
