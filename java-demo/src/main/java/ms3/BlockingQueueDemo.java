package ms3;

import java.util.ArrayList;
import java.util.List;
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
public class BlockingQueueDemo {
    public static void main(String[] args) throws Exception{

        //List<String> list=new ArrayList<>();
        BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<>(3);
       /* blockingQueue.add("a");
        blockingQueue.add("b");
        blockingQueue.add("c");
        blockingQueue.add("d");*/
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));

        System.out.println(blockingQueue.element());//队列首元素
        //System.out.println(blockingQueue.add("d"));//Exception in thread "main" java.lang.IllegalStateException: Queue full
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());//先进先出
        //System.out.println(blockingQueue.remove());//Exception in thread "main" java.util.NoSuchElementException




    }
}
