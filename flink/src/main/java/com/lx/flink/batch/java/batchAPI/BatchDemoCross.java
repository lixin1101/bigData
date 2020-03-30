package com.lx.flink.batch.java.batchAPI;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.CrossOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * 获取笛卡尔积
 *
 */
public class BatchDemoCross {

    public static void main(String[] args) throws Exception{

        Logger.getLogger("org").setLevel(Level.WARN);
        //获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //tuple2<用户id，用户姓名>
        ArrayList<String> data1 = new ArrayList<String>();
        data1.add("zs");
        data1.add("ww");

        //tuple2<用户id，用户所在城市>
        ArrayList<Integer> data2 = new ArrayList<Integer>();
        data2.add(1);
        data2.add(2);

        DataSource<String> text1 = env.fromCollection(data1);
        DataSource<Integer> text2 = env.fromCollection(data2);

        CrossOperator.DefaultCross<String, Integer> cross = text1.cross(text2);

        cross.print();


    }



}
