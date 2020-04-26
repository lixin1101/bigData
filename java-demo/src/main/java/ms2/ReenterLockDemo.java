package ms2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class phone implements Runnable {

    public synchronized void sendSMS() throws Exception {
        System.out.println(Thread.currentThread().getId() + "\t invoked sms");
        sendEmail();
    }

    public synchronized void sendEmail() throws Exception {
        System.out.println(Thread.currentThread().getId() + "\t invoked Email");
    }

    //*******************************************
    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    public void get() {

        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t get()");
            set();
        } finally {
            lock.unlock();
        }
    }

    public void set() {

        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t set()");
        } finally {
            lock.unlock();
        }
    }
}

public class ReenterLockDemo {
    /**
     * 可重入锁【递归锁】
     * <p>
     * 指的是同一线程外层函数获得锁之后，内层递归函数仍然能获得该锁的代码
     * 在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁
     * <p>
     * 也就是说，线程可以进入任何一个它已经拥有的锁所同步着的代码块
     * <p>
     * 13	 invoked sms
     * 13	 invoked Email
     * 12	 invoked sms
     * 12	 invoked Email
     */
    public static void main(String[] args) {
        phone phone = new phone();
        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("&&&&&&&&&&&");
        Thread t3 = new Thread(phone);
        Thread t4 = new Thread(phone);
        t3.start();
        t4.start();

    }
}
