package ms3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData {

    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() {
        lock.lock();
        try {
            //判断
            while (number != 0) {
                //等待，不能生产
                condition.await();
            }

            //干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //通知唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void decrement() {
        lock.lock();
        try {
            //判断
            while (number == 0) {
                //等待，不能生产
                condition.await();
            }

            //干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //通知唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}

/**
 * 一个初始值为0的变量，两个线程对其交替操作，一个加1一个减1，来5轮
 * <p>
 * 1:线程操作资源类
 * 2:判断干活与通知
 * 3:防止虚假唤醒机制
 */
public class ProdConsumer_TraditionDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                shareData.increment();
            }
        },"AAA").start();

        new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                shareData.decrement();
            }
        },"BBB").start();
    }
}
