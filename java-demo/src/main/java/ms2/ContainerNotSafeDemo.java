package ms2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类不安全问题解决
 * ArrayList
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        //arraylist();
        //arrayset();
        arraymap();


    }

    public static void arraymap(){
        Map<String,String> map=new ConcurrentHashMap<>();//new HashMap<>();

        //java.util.ConcurrentModificationException:并发修改异常
        for (int i = 1; i < 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
    public static void arrayset() {
        Set<String> set = new CopyOnWriteArraySet<>();//Collections.synchronizedSet(new HashSet<>());//new HashSet<>();//线程不安全

        //java.util.ConcurrentModificationException:并发修改异常
        for (int i = 1; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    public static void arraylist() {
        List<String> list = new CopyOnWriteArrayList<>();//Collections.synchronizedList(new ArrayList<>()); //new Vector<>();// new ArrayList<String>();

        /*List<String> list= Arrays.asList("a","b","c");
        list.forEach(System.out::println);*/

        //java.util.ConcurrentModificationException:并发修改异常
        for (int i = 1; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        /**
         *
         * 1:故障现象
         *      java.util.ConcurrentModificationException:并发修改异常
         *
         * 2:导致原因
         *      并发争抢修改导致，参考我们的花名册签名情况
         *      一个人正在写入，另外一个同学过来抢夺，导致数据不一致异常，并发修改异常
         *
         * 3:解决方案
         *    3.1 new Vector<>()，不能用 虽然加锁，但是并发性极具下降
         *    3.2 Collections.synchronizedList(new ArrayList<>())
         *    3.3 写时复制 new CopyOnWriteArrayList<>()
         *
         * 4:优化建议
         *
         */
    }
}
