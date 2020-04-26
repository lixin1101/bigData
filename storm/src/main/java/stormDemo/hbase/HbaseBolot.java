package stormDemo.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.shade.org.apache.http.conn.HttpConnectionFactory;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;

/**
 * HbaseBolt,写入数据到hbase库中。
 */
public class HbaseBolot implements IRichBolt {

    private Table t ;
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        try {
            Configuration conf = HBaseConfiguration.create();
            Connection conn = ConnectionFactory.createConnection(conf);
            TableName tname = TableName.valueOf("ns1:wordcount");
            t = conn.getTable(tname);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void execute(Tuple tuple) {
        String word = tuple.getString(0);
        Integer count = tuple.getInteger(1);
        //使用hbase的increment机制进行wordcount  计数器
        byte[] rowkey = Bytes.toBytes(word);
        byte[] f = Bytes.toBytes("f1");
        byte[] c = Bytes.toBytes("count");
        try {
            t.incrementColumnValue(rowkey,f,c,count);
        } catch (IOException e) {
        }
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
