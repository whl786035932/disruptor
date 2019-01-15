package generate1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

import base.LongEvent;
/**
 * ʹ��EventProcessor ��Ϣ������
 * @author 10297
 *
 */
public class Main1 {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int bufferSize = 1024;
		int thread_nums = 4;

		final RingBuffer<LongEvent> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<LongEvent>() {
			public LongEvent newInstance() {
				return new LongEvent();
			}
		}, bufferSize, new YieldingWaitStrategy());

		ExecutorService executor = Executors.newFixedThreadPool(thread_nums);

		// ֮ǰ����disruptorָ��EventHandler,������ģ�ⲻ����Disruptor��ֻʹ��Ringbuffer,
		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

		// ���������ߣ���Disruptor������������Eventprocessor ��ʽ������
		BatchEventProcessor<LongEvent> transProcessor = new BatchEventProcessor<LongEvent>(ringBuffer, sequenceBarrier,
				new EventHandler<LongEvent>() {

					public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName()+"����" + event.getValue());
					}
				});

		executor.submit(transProcessor);

		Future<LongEvent> future = executor.submit(new Callable<LongEvent>() {

			public LongEvent call() throws Exception {
				long seq;
				for (int i = 0; i < 10; i++) {
					seq = ringBuffer.next();// ռ���� --ringBufferһ����������
					ringBuffer.get(seq).setValue(i);// ������������ ����
					ringBuffer.publish(seq);// ����������������ʹhandler(consumer)�ɼ�
				}
				return null;
			}
		});
		
		future.get(); // �ȴ������߽���
		Thread.sleep(1000);// ����1�룬�����Ѷ��������
		transProcessor.halt(); /// ֪ͨ�¼�(����˵��Ϣ)������ ���Խ����ˣ����������Ͻ���!!!��
		executor.shutdown();

	}

}
