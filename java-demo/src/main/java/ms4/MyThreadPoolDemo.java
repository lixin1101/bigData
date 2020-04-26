package ms4;


import java.util.concurrent.*;

/**
 * 1.继承thread类，2：实现runnable接口 3：实现callable接口
 * 第四种获得 使用java多线程的方式，线程池
 */
public class MyThreadPoolDemo {
    public static void main(String[] args) {
        //threadPoolInit();
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());

        //模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
        try {
            //AbortPolicy 超过8就会报异常
            //CallerRunsPolicy 回退调用者 不报异常，出现main线程处理业务
            //DiscardOldestPolicy 抛弃队列中等待最久的任务
            //DiscardPolicy 直接丢弃任务

            for (int i = 1; i <= 11; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });

                //TimeUnit.SECONDS.sleep(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }

    public static void threadPoolInit() {
        //System.out.println(Runtime.getRuntime().availableProcessors());//8cpu
        /**
         * Array Arrays
         * Collection Collections
         * Executor Executors
         */
        //List<String> list= Arrays.asList("a","b","c");
        //ExecutorService threadPool = Executors.newFixedThreadPool(5);//一池5个处理线程
        //ExecutorService threadPool = Executors.newSingleThreadExecutor();//一池1个处理线程
        ExecutorService threadPool = Executors.newCachedThreadPool();//一池n个处理线程
        //模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
        try {
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });

                //TimeUnit.SECONDS.sleep(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
