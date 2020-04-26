package ms3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * 1:阻塞队列
 * 阻塞队列有没有好的一面
 *
 * 不得不阻塞，你如何管理
 *
 */
public class BlockingQueueDemo2 {
    public static void main(String[] args) throws Exception{

        //List<String> list=new ArrayList<>();
        BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("x"));

        System.out.println(blockingQueue.peek());//a
        System.out.println("-----------");
        /**
         * a
         * a
         * a
         * null
         */
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());




    }
}
