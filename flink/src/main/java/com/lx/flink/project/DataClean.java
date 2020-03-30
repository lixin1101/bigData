package com.lx.flink.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchemaWrapper;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Properties;

/**
 * 数据清洗
 *
 * 执行步骤：
 * 1：先往redis里放几个大区与国家对应关系的配置数据
 * 2：启动utils中的生产者，生产数据
 * 3：启动flink程序
 *
 */
public class DataClean {

    public static void main(String[] args) throws Exception {

        //获取flink的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(3);
        //checkpoint配置
        env.enableCheckpointing(60000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000);
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        //设置statebackend

        env.setStateBackend(new RocksDBStateBackend("hdfs://s101:9000/flink/checkpoints",true));


        //指定kafka source
        String topic = "allData";
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers", "s101:9092,s102:9092,s103:9092");
        prop.setProperty("group.id", "con1");

        FlinkKafkaConsumer011<String> myConsumer = new FlinkKafkaConsumer011<String>(topic, new SimpleStringSchema(), prop);

        //{“dt”:”2018-01-01 11:11:11”,”countryCode”:”US”,”data”:[{“type”:s1,”source”:0.3,”level”:”A”},{“type”:s2,”source”:0.1,”level”:”B”}]}
        DataStreamSource<String> data = env.addSource(myConsumer);

        //获取最新的国家码和大区的映射关系
        DataStream<HashMap<String, String>> mapData = env.addSource(new MyRedisSource()).broadcast();

        //这里注意：mapData是我们自己实现的source，并行度是1，而data的并行度是默认机器的cpu，所有如果不加处理的话会有的线程没有数据
        //所以在上面 获取最新的国家码和大区的映射关系 时要给一个broadcast
        DataStream<String> resData = data.connect(mapData).flatMap(new CoFlatMapFunction<String, HashMap<String, String>, String>() {

            HashMap<String, String> allmap = new HashMap<String, String>();

            //flatMap1 处理的是kafka中的数据
            public void flatMap1(String value, Collector<String> out) throws Exception {
                JSONObject jsonObject = JSONObject.parseObject(value);
                String dt = jsonObject.getString("dt");
                String countryCode = jsonObject.getString("countryCode");
                //获取大区
                String area = allmap.get(countryCode);

                JSONArray jsonArray = JSONObject.parseArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    System.out.println("area="+area+"-------------");

                    jsonObject1.put("area", area);
                    jsonObject1.put("dt", dt);
                    out.collect(jsonObject1.toJSONString());
                }

            }

            //flatMap2 处理的是redis返回的数据
            public void flatMap2(HashMap<String, String> value, Collector<String> out) throws Exception {
                this.allmap = value;
            }
        });

        String outTopic = "allDataClean";
        String brokerList = "s101:9092";
        Properties outProp = new Properties();
        outProp.setProperty("bootstrap.servers",brokerList);

        //第一种解决方案，设置FlinkKafkaProducer011里面的事务超时时间
        //设置事务超时时间
        outProp.setProperty("transaction.timeout.ms",60000*15+"");

        FlinkKafkaProducer011<String> myProducer = new FlinkKafkaProducer011<String>
                (outTopic, new KeyedSerializationSchemaWrapper<String>(new SimpleStringSchema()), outProp, FlinkKafkaProducer011.Semantic.EXACTLY_ONCE);

        resData.addSink(myProducer);


        //这一行代码一定要实现，否则程序不执行
        env.execute("DataClean");
    }
}
