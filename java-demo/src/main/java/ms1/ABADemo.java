package ms1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题的解决
 */
public class ABADemo {
    static AtomicReference<Integer> atomicReference = new AtomicReference<Integer>(100);
    //参数是：初始化和版本号
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<Integer>(100, 1);

    public static void main(String[] args) {

        System.out.println("=======以下是ABA问题的产生=========");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            //暂停1秒钟，保证上面的t1线程完成了一次ABA操作
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicReference.compareAndSet(100, 2019) + "\t " + atomicReference.get());
        }, "t2").start();

        /**
         * 停一停
         */
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("=======以下是ABA问题的解决=========");

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号：" + stamp);

            //暂停1秒钟的t3线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stampedReference.compareAndSet(100,101,stampedReference.getStamp(),stampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + "\t 第二次版本号：" + stampedReference.getStamp());
            stampedReference.compareAndSet(101,100,stampedReference.getStamp(),stampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + "\t 第三次版本号：" + stampedReference.getStamp());

        }, "t3").start();

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号：" + stamp);

            //暂停3秒钟的t4线程
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean result = stampedReference.compareAndSet(100, 2019, stamp, stamp + 1);

            System.out.println(Thread.currentThread().getName() + "\t 修改后的情况：" + result+"\t 版本号："+stampedReference.getStamp());

            System.out.println(stampedReference.getReference());

        }, "t4").start();

    }
}
