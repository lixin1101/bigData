package JUC;

import java.util.concurrent.TimeUnit;

/**
 * 8 lock
 */
class Phone {
    public static synchronized void sendEmail() throws Exception {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("*****sendEmail");
    }

    public synchronized void sendSMS() throws Exception {
        System.out.println("*****sendSMS");
    }

    public void sayHello() throws Exception {
        System.out.println("*****sayHello");
    }
}


/**
 * 8 lock
 * 1 标准访问，请问先打印邮件还是短信
 * 2 暂停4秒钟在邮件方法，请问先打印邮件还是短信
 * 3 新增普通sayHello方法，请问先打印邮件还是hello  先出hello
 * 4 两部手机，请问先打印邮件还是短信
 * 5 两个静态同步方法，同一部手机，请问先打印邮件还是短信
 * 6 两个静态同步方法，2部手机，请问先打印邮件还是短信
 * 7 1个静态同步方法，1个普通同步方法,同一部手机，请问先打印邮件还是短信
 * 8 1个静态同步方法，1个普通同步方法,2部手机，请问先打印邮件还是短信
 *
 * 一个对象里面如果有多个synchronized方法，某一个时刻内，只要一个线程去调用其中的一个synchronized方法了，
 * 其它的线程都只能等待，换句话说，某一个时刻内，只能有唯一一个线程去访问这些synchronized方法
 *
 * 锁的是当前对象this，被锁定后，其它的线程都不能进入到当前对象的其它的synchronized方法
 *
 * 加个普通方法后发现和同步锁无关
 *
 * 换成两个对象后，不是同一把锁了，情况立刻变化。
 *
 * synchronized实现同步的基础：Java中的每一个对象都可以作为锁。
 * 具体表现为以下3种形式。
 * 对于普通同步方法，锁是当前实例对象,锁的是当前对象this，
 * 对于同步方法块，锁是Synchonized括号里配置的对象。
 *
 * 对于静态同步方法，锁是当前类的Class对象。
 */
public class Lock8Demo05 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();

        Thread.sleep(100);

        new Thread(() -> {
            try {
                //phone.sendSMS();
                //phone.sayHello();
                phone2.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();

    }
}







