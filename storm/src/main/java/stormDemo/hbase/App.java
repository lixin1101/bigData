package stormDemo.hbase;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;


/**
 * App
 */
public class App {
   /* private static final String WORD_SPOUT = "WORD_SPOUT";
    private static final String COUNT_BOLT = "COUNT_BOLT";
    private static final String HBASE_BOLT = "HBASE_BOLT";*/

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("wcspout", new WordCountSpout()).setNumTasks(1);
        builder.setBolt("split-bolt", new SplitBolt(), 2).shuffleGrouping("wcspout").setNumTasks(2);
        builder.setBolt("hbase-bolt", new HbaseBolot(), 2).shuffleGrouping("split-bolt").setNumTasks(2);

        Config conf = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("wc", conf, builder.createTopology());

    }
}
