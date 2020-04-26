package ms3;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

class MyThread2 implements Runnable {

    @Override
    public void run() {

    }
}

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName()+" *********come in callable");
        TimeUnit.SECONDS.sleep(5);
        return 1024;
    }
}


public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {


        FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyThread());

        Thread t1 = new Thread(futureTask, "AAA");
        t1.start();

        System.out.println(Thread.currentThread().getName()+"\t come in ********");
        int result01 = 100;

        while(!futureTask.isDone()){

        }

        int result02=futureTask.get();
        System.out.println(result01+result02);


    }
}
