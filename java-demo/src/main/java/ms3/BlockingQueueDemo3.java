package ms3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 1:阻塞队列
 * 阻塞队列有没有好的一面
 * <p>
 * 不得不阻塞，你如何管理
 */
public class BlockingQueueDemo3 {
    public static void main(String[] args) throws Exception {

        //List<String> list=new ArrayList<>();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

       /* blockingQueue.put("a");
        blockingQueue.put("a");
        blockingQueue.put("a");
        System.out.println("-----------");*/
        //blockingQueue.put("x");
        /*blockingQueue.take();
        blockingQueue.take();
        blockingQueue.take();*/
        //blockingQueue.take();

        System.out.println(blockingQueue.offer("a",2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("a",2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("a",2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("a",2L, TimeUnit.SECONDS));

    }
}
