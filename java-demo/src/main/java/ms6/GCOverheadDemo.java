package ms6;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
 * <p>
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 */
public class GCOverheadDemo {
    public static void main(String[] args) {
        int i = 0;
        List<String> list = new ArrayList<>();
        try {
            while (true) {
                list.add(String.valueOf(++i).intern());
            }
        } catch (Throwable e) {
            System.out.println("***********i=" + i);
            e.printStackTrace();
            throw e;
        }

    }
}
