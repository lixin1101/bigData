package ms1;

public class SingletonDemo {

    //private static SingletonDemo instance = null;
    private static volatile SingletonDemo instance = null;

    private SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法SingletonDemo()");
    }

    /**
     * public static SingletonDemo getInstance() {
     * if (instance == null) {
     * instance = new SingletonDemo();
     * }
     * return instance;
     * }
     * <p>
     * public static synchronized SingletonDemo getInstance() {
     * if (instance == null) {
     * instance = new SingletonDemo();
     * }
     * return instance;
     * }
     */

    //DCL (Double check lock 双端检锁机制)
    public static SingletonDemo getInstance() {
        if (instance == null) {
            //由于存在指令重排 需要在instance加volatile
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        /**
         * main	 我是构造方法SingletonDemo()
         * true
         * true
         * true
         */

        /*
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println("--------------------");
        */

        //并发多线程，情况发生了变化
        /**
         * 1	 我是构造方法SingletonDemo()
         * 5	 我是构造方法SingletonDemo()
         * 4	 我是构造方法SingletonDemo()
         * 3	 我是构造方法SingletonDemo()
         * 2	 我是构造方法SingletonDemo()
         *
         * 解决办法
         * 1：getInstance方法前加synchronized  [不是很好]
         *
         */
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }

    }
}
