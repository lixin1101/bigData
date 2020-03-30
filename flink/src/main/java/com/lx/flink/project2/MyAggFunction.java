package com.lx.flink.project2;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class MyAggFunction implements WindowFunction<Tuple3<Long, String, String>, Tuple4<String, String, String, Long>, Tuple, TimeWindow> {

    public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple3<Long, String, String>> input, Collector<Tuple4<String, String, String, Long>> out) throws Exception {
        //获取分组字段
        String type = tuple.getField(0).toString();
        String area = tuple.getField(1).toString();

        Iterator<Tuple3<Long, String, String>> it = input.iterator();
        //存储时间
        ArrayList<Long> arraylist = new ArrayList<Long>();
        long count = 0;
        while (it.hasNext()) {
            Tuple3<Long, String, String> next = it.next();
            arraylist.add(next.f0);
            count++;
        }

        System.out.println(Thread.currentThread().getId()+",window触发了，数据条数："+count);
        //排序
        Collections.sort(arraylist);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(arraylist.get(arraylist.size() - 1)));

        //组装结果
        Tuple4<String, String, String, Long> res = new Tuple4<String, String, String, Long>(time, type, area, count);
        out.collect(res);
    }

}
