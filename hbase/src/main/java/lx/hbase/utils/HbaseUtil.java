package lx.hbase.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseUtil {

    //ThreadLocal解决的是数据共享问题，而不是解决数据安全问题
    private static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    //private static Connection conn = null;
    private HbaseUtil() {
    }

    /**
     * 获取连接对象
     */
    public static void getHbaseConnection() throws IOException {
        /*Configuration conf = HBaseConfiguration.create();
        conn= ConnectionFactory.createConnection(conf);
        return conn;*/
        Connection conn = connHolder.get();
        if (conn == null) {
            Configuration conf = HBaseConfiguration.create();
            conn = ConnectionFactory.createConnection(conf);
            connHolder.set(conn);
        }
    }

    /**
     * 关闭连接
     */
    public static void close() throws IOException {
        Connection conn = connHolder.get();
        if (conn != null) {
            conn.close();
            connHolder.remove();
        }
    }

    /**
     * 生成分区号
     */
    public static String genRegionNum(String rowkey, int regionCount) {

        int regionNum;
        int hash = rowkey.hashCode();
        if (regionCount > 0 && (regionCount & (regionCount - 1)) == 0) {
            regionNum = hash % (regionCount - 1);
        } else {
            regionNum = hash % (regionCount);
        }
        return regionNum + "_" + rowkey;
    }

    /**
     * 生成分区键
     */
    public static byte[][] genRegionKey(int regionCount) {
        byte[][] bs = new byte[regionCount - 1][];
        //3 ==  2  === 0  1

        for (int i = 0; i < regionCount - 1; i++) {
            bs[i] = Bytes.toBytes(i + "|");

        }
        return  bs;
    }

    public static void main(String[] args) {
        byte[][] bytes = genRegionKey(6);
        for (byte[] aByte : bytes) {
            System.out.println(Bytes.toString(aByte));
        }

        String zs = genRegionNum("ww", 3);
        System.out.println(zs);
    }


}
