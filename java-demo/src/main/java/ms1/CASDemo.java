package ms1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS是什么：比较并交换  compareAndSet的缩写
 */
public class CASDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        /**
         * public final boolean compareAndSet(int expect, int update) {
         *         return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
         *     }
         */
        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current data:" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 2020) + "\t current data:" + atomicInteger.get());

        atomicInteger.getAndIncrement();

    }
}
