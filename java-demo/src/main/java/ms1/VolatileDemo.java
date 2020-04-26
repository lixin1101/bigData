package ms1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Mydata2 {
    int number = 0;

    public void addTo60() {
        this.number = 60;
    }
}

class Mydata {
    volatile int number = 0;

    public void addTo60() {
        this.number = 60;
    }

    //此时number前面是加了volatile关键字 修饰的
    public synchronized void addPlusPlus2() {
        number++;
    }

    /**
     * synchronized 但是不太好
     */
    public void addPlusPlus() {
        number++;
    }

    AtomicInteger atomicInteger=new AtomicInteger();
    public void addAtomic(){
        atomicInteger.getAndIncrement();
    }

}

/**
 * 1 验证Volatile的可见性
 * 1.1 假如int number=0  number变量之前根本没有添加Volatile关键字修饰
 * 1.2 添加了Volatile，可以解决可见性问题
 * <p>
 * 2 验证Volatile不保证原子性
 * 2.1 原子性指的是什么意思
 * 不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完整，
 * 要么成功，要么失败
 * 2.2 Volatile不保证原子性演示
 * 2.3 why：写覆盖，数据丢失
 * 2.4 如何解决原子性问题？
 *  AtomicInteger
 *
 */
public class VolatileDemo {
    public static void main(String[] args) {

        Mydata mydata = new Mydata();

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    mydata.addPlusPlus();
                    mydata.addAtomic();
                }

            }, String.valueOf(i)).start();
        }

        //等待上面20个线程全部计算完以后，再用main最终查询得到多少

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t result" + mydata.number);//最终结果不是20000 加了synchronized就达到了
        System.out.println(Thread.currentThread().getName() + "\t result" + mydata.atomicInteger);
    }

    public static void seeOkbyVolatile() {
        //线程操纵资源类
        Mydata mydata = new Mydata();
        //线程1
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mydata.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t update" + mydata.number);
        }, "AAA").start();

        while (mydata.number == 0) {
            //main线程就在一直这里等待循环，直到number值不再等于0
        }
        System.out.println(Thread.currentThread().getName() + "\t is over" + mydata.number);
    }
}
