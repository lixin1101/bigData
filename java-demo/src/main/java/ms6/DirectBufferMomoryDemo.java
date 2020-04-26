package ms6;

import java.nio.ByteBuffer;

public class DirectBufferMomoryDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("配置的maxDirectMemory:" + (sun.misc.VM.maxDirectMemory() / (double) 1024 / 1024) + "MB");
        Thread.sleep(3000);

        ByteBuffer bb = ByteBuffer.allocateDirect(6 * 1024 * 1024);//java.lang.OutOfMemoryError: Direct buffer memory
    }
}
