package com.lx.flink.batch.java.batchAPI;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * 获取集合中的前N个元素
 */
public class BatchDemoFirstN {

    public static void main(String[] args) throws Exception{

        Logger.getLogger("org").setLevel(Level.WARN);
        //获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        ArrayList<Tuple2<Integer, String>> data = new ArrayList<Tuple2<Integer, String>>();
        data.add(new Tuple2<Integer, String>(2,"zs"));
        data.add(new Tuple2<Integer, String>(4,"ls"));
        data.add(new Tuple2<Integer, String>(3,"ww"));
        data.add(new Tuple2<Integer, String>(1,"xw"));
        data.add(new Tuple2<Integer, String>(1,"aw"));
        data.add(new Tuple2<Integer, String>(1,"mw"));


        DataSource<Tuple2<Integer, String>> text = env.fromCollection(data);


        //获取前3条数据，按照数据插入的顺序
        text.first(3).print();
        System.out.println("==============================");

        //根据数据中的第一列进行分组，获取每组的前2个元素
        text.groupBy(0).first(2).print();
        System.out.println("==============================");

        //根据数据中的第一列分组，再根据第二列进行组内排序[升序]，获取每组的前2个元素
        text.groupBy(0).sortGroup(1, Order.ASCENDING).first(2).print();
        System.out.println("==============================");

        //不分组，全局排序获取集合中的前3个元素，针对第一个元素升序，第二个元素倒序
        text.sortPartition(0,Order.ASCENDING).sortPartition(1,Order.DESCENDING).first(3).print();

    }



}
