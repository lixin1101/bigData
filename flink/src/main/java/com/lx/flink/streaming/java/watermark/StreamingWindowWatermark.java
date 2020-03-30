package com.lx.flink.streaming.java.watermark;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 *
 * Watermark 案例
 *
 *
 * 测试数据-1如下：watermark+window处理乱序数据
 * 0001,1538359882000		2018-10-01 10:11:22
 * 0001,1538359886000		2018-10-01 10:11:26
 * 0001,1538359892000		2018-10-01 10:11:32
 * 0001,1538359893000		2018-10-01 10:11:33
 * 0001,1538359894000		2018-10-01 10:11:34
 * 0001,1538359896000		2018-10-01 10:11:36
 * 0001,1538359897000		2018-10-01 10:11:37
 * 0001,1538359899000		2018-10-01 10:11:39
 * 0001,1538359891000		2018-10-01 10:11:31
 * 0001,1538359903000		2018-10-01 10:11:43
 * 0001,1538359892000		2018-10-01 10:11:32
 * 0001,1538359891000		2018-10-01 10:11:31
 *
 *
 * 测试数据-2如下：延迟数据被丢弃
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359903000		2018-10-01 10:11:43
 *
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359891000		2018-10-01 10:11:31
 * 0001,1538359892000		2018-10-01 10:11:32
 *
 *
 * 测试数据-3如下：allowedLateness
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359903000		2018-10-01 10:11:43
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359891000		2018-10-01 10:11:31
 * 0001,1538359892000		2018-10-01 10:11:32
 * 0001,1538359904000		2018-10-01 10:11:44
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359891000		2018-10-01 10:11:31
 * 0001,1538359892000		2018-10-01 10:11:32
 * 0001,1538359905000		2018-10-01 10:11:45
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359891000		2018-10-01 10:11:31
 * 0001,1538359892000		2018-10-01 10:11:32
 *
 * 测试数据-4如下：sideOutputLateData
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359903000		2018-10-01 10:11:43
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359891000		2018-10-01 10:11:31
 * 0001,1538359892000		2018-10-01 10:11:32
 *
 * 测试数据-5如下：多并行度下的watermark-8
 * 0001,1538359882000		2018-10-01 10:11:22
 * 0001,1538359886000		2018-10-01 10:11:26
 * 0001,1538359892000		2018-10-01 10:11:32
 * 0001,1538359893000		2018-10-01 10:11:33
 * 0001,1538359894000		2018-10-01 10:11:34
 * 0001,1538359896000		2018-10-01 10:11:36
 * 0001,1538359897000		2018-10-01 10:11:37
 *
 * 测试数据-6如下：
 * 0001,1538359890000		2018-10-01 10:11:30
 * 0001,1538359903000		2018-10-01 10:11:43
 * 0001,1538359908000		2018-10-01 10:11:48
 *
 *
 */
public class StreamingWindowWatermark {

    public static void main(String[] args) throws Exception {
        //定义socket的端口号
        int port = 9000;
        //获取运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置使用eventtime，默认是使用processtime
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


        //设置并行度为1,默认并行度是当前机器的cpu数量
        env.setParallelism(1);

        //连接socket获取输入的数据
        DataStream<String> text = env.socketTextStream("127.0.0.1", port, "\n");

        //解析输入的数据
        DataStream<Tuple2<String, Long>> inputMap = text.map(new MapFunction<String, Tuple2<String, Long>>() {
            public Tuple2<String, Long> map(String value) throws Exception {
                String[] arr = value.split(",");
                return new Tuple2<String, Long>(arr[0], Long.parseLong(arr[1]));
            }
        });

        //抽取timestamp和生成watermark
        DataStream<Tuple2<String, Long>> waterMarkStream =
                inputMap.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String, Long>>() {

            Long currentMaxTimestamp = 0L;
            final Long maxOutOfOrderness = 10000L;// 最大允许的乱序时间是10s

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            /**
             * 定义生成watermark的逻辑
             * 默认100ms被调用一次
             */
            @Nullable
            public Watermark getCurrentWatermark() {
                return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
            }

            //定义如何提取timestamp
            public long extractTimestamp(Tuple2<String, Long> element, long previousElementTimestamp) {
                long timestamp = element.f1;
                currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);
                long id = Thread.currentThread().getId();
                System.out.println("currentThreadId:"+id+",key:"
                        +element.f0+",eventtime:["+element.f1+"|"+
                        sdf.format(element.f1)+"],currentMaxTimestamp:["+currentMaxTimestamp+"|"+
                        sdf.format(currentMaxTimestamp)+"],watermark:["+getCurrentWatermark().getTimestamp()+"|"+
                        sdf.format(getCurrentWatermark().getTimestamp())+"]");
                return timestamp;
            }
        });

        DataStream<String> window = waterMarkStream.keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(3)))//按照消息的EventTime分配窗口，和调用TimeWindow效果一样
                .apply(new WindowFunction<Tuple2<String, Long>, String, Tuple, TimeWindow>() {
                    /**
                     * 对window内的数据进行排序，保证数据的顺序
                     * @param tuple
                     * @param window
                     * @param input
                     * @param out
                     * @throws Exception
                     */
                    public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple2<String, Long>> input, Collector<String> out) throws Exception {
                        String key = tuple.toString();
                        List<Long> arrarList = new ArrayList<Long>();
                        Iterator<Tuple2<String, Long>> it = input.iterator();
                        while (it.hasNext()) {
                            Tuple2<String, Long> next = it.next();
                            arrarList.add(next.f1);
                        }
                        Collections.sort(arrarList);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        String result = key + "," + arrarList.size() + "," + sdf.format(arrarList.get(0)) + "," + sdf.format(arrarList.get(arrarList.size() - 1))
                                + "," + sdf.format(window.getStart()) + "," + sdf.format(window.getEnd());
                        out.collect(result);
                    }
                });
        /**
         * 1、watermark 时间 >= window_end_time
         * 2、在[window_start_time,window_end_time)区间中有数据存在，注意是左闭右开的区间
         */
        //测试-把结果打印到控制台即可
        window.print();

        //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
        env.execute("eventtime-watermark");

    }



}
