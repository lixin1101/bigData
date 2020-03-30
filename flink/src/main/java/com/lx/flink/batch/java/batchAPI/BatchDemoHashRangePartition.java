package com.lx.flink.batch.java.batchAPI;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Hash-Partition
 *
 * Range-Partition
 *
 *
 * Created by xuwei.tech on 2018/10/8.
 */
public class BatchDemoHashRangePartition {

    public static void main(String[] args) throws Exception{

        Logger.getLogger("org").setLevel(Level.WARN);
        //获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        ArrayList<Tuple2<Integer, String>> data = new ArrayList<Tuple2<Integer, String>>();
        data.add(new Tuple2<Integer, String>(1,"hello1"));
        data.add(new Tuple2<Integer, String>(2,"hello2"));
        data.add(new Tuple2<Integer, String>(2,"hello3"));
        data.add(new Tuple2<Integer, String>(3,"hello4"));
        data.add(new Tuple2<Integer, String>(3,"hello5"));
        data.add(new Tuple2<Integer, String>(3,"hello6"));
        data.add(new Tuple2<Integer, String>(4,"hello7"));
        data.add(new Tuple2<Integer, String>(4,"hello8"));
        data.add(new Tuple2<Integer, String>(4,"hello9"));
        data.add(new Tuple2<Integer, String>(4,"hello10"));
        data.add(new Tuple2<Integer, String>(5,"hello11"));
        data.add(new Tuple2<Integer, String>(5,"hello12"));
        data.add(new Tuple2<Integer, String>(5,"hello13"));
        data.add(new Tuple2<Integer, String>(5,"hello14"));
        data.add(new Tuple2<Integer, String>(5,"hello15"));
        data.add(new Tuple2<Integer, String>(6,"hello16"));
        data.add(new Tuple2<Integer, String>(6,"hello17"));
        data.add(new Tuple2<Integer, String>(6,"hello18"));
        data.add(new Tuple2<Integer, String>(6,"hello19"));
        data.add(new Tuple2<Integer, String>(6,"hello20"));
        data.add(new Tuple2<Integer, String>(6,"hello21"));


        DataSource<Tuple2<Integer, String>> text = env.fromCollection(data);

        /*text.partitionByHash(0).mapPartition(new MapPartitionFunction<Tuple2<Integer,String>, Tuple2<Integer,String>>() {
            @Override
            public void mapPartition(Iterable<Tuple2<Integer, String>> values, Collector<Tuple2<Integer, String>> out) throws Exception {
                Iterator<Tuple2<Integer, String>> it = values.iterator();
                while (it.hasNext()){
                    Tuple2<Integer, String> next = it.next();
                    System.out.println("当前线程id："+Thread.currentThread().getId()+","+next);
                }

            }
        }).print();*/


        text.partitionByRange(0).mapPartition(new MapPartitionFunction<Tuple2<Integer,String>, Tuple2<Integer,String>>() {
            public void mapPartition(Iterable<Tuple2<Integer, String>> values, Collector<Tuple2<Integer, String>> out) throws Exception {
                Iterator<Tuple2<Integer, String>> it = values.iterator();
                while (it.hasNext()){
                    Tuple2<Integer, String> next = it.next();
                    System.out.println("当前线程id："+Thread.currentThread().getId()+","+next);
                }

            }
        }).print();



    }



}
