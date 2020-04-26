package stormDemo.wordCount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * App
 */
public class App {
    public static void main(String[] args) throws Exception {
        /**
         * nc -lk 7777
         * nc -lk 8888
         * nc -lk 9999
         *
         * 并发度 ==== 所有的task个数的总和
         */
        TopologyBuilder builder = new TopologyBuilder();
        //设置Spout  设置Spout的并发暗示 (executor个数) 跑几个线程
        builder.setSpout("wcspout", new WordCountSpout(),3).setNumTasks(3);//设置spout的task，任务个数 每个线程可以执行多个task
        //设置creator-Bolt   设置bolt的并发暗示
        builder.setBolt("split-bolt", new SplitBolt(),4).shuffleGrouping("wcspout").setNumTasks(4);
        //设置counter-Bolt
        builder.setBolt("counter-bolt", new CountBolt(),5).fieldsGrouping("split-bolt", new Fields("word")).setNumTasks(5);

        //说法：同一主机，同一进程，不同线程的不同对象执行的方法
        Config conf = new Config();
        //设置多个worker
        conf.setNumWorkers(2);
        conf.setDebug(true);

        /**
         * 本地模式storm
         */
//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology("wc", conf, builder.createTopology());
//        Thread.sleep(10000);
        StormSubmitter.submitTopology("wordcount", conf, builder.createTopology());
//        cluster.shutdown();

    }
}
