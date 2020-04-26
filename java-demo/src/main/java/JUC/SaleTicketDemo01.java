package JUC;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Ticket //资源类 = 实例变量+实例方法
{
    private int number = 30;
    // List list = new ArrayList();
    Lock lock = new ReentrantLock();//可重用锁

    public void sale() {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t卖出第：" + (number--) + "\t 还剩下： " + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}

/**
 * <p>
 * 题目：三个售票员         卖出          30张票
 * 笔记：如何编写企业级的多线程代码
 * 固定的编程套路+模板是什么？
 * <p>
 * 1 在高内聚低耦合的前提下，线程        操作      资源类
 * 1.1 一言不合，先创建一个资源类
 */
public class SaleTicketDemo01 {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        //接口可以new 匿名内部类 Runnable
        new Thread(() -> {
            for (int i = 1; i <= 40; i++) ticket.sale();
        }, "A").start();
        new Thread(() -> {
            for (int i = 1; i <= 40; i++) ticket.sale();
        }, "B").start();
        new Thread(() -> {
            for (int i = 1; i <= 40; i++) ticket.sale();
        }, "C").start();


     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 40; i++) ticket.sale();
                {
                    ticket.sale();
                }
            }
        }, "A").start();//并不是立刻启动

        //Thread(Runnable target, String name) Allocates a new Thread object.

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 40; i++) {
                    ticket.sale();
                }
            }
        }, "C").start();*/


    }
}