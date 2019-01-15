package generate2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import base.LongEvent;

public class Main {  
    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {  
       
    	long beginTime=System.currentTimeMillis();  
        int bufferSize=1024;  
        ExecutorService executor=Executors.newFixedThreadPool(8);  

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(new EventFactory<LongEvent>() {  
            public LongEvent newInstance() {  
                return new LongEvent();  
            }  
        }, bufferSize, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());  
        
        //���β���
        EventHandlerGroup<LongEvent> handlerGroup = 
        		disruptor.handleEventsWith(new Handler1(), new Handler2());
        //������C1,C2����֮��ִ��JMS��Ϣ���Ͳ��� Ҳ���������ߵ�C3 
        handlerGroup.then(new Handler3());
        
        disruptor.start();
        
        //����������
        CountDownLatch countDownLatch = new CountDownLatch(1);
        executor.submit(new Producer(disruptor,countDownLatch));
        countDownLatch.await();
        
        executor.shutdown();
        disruptor.shutdown();
      
        
       
    }  
}  