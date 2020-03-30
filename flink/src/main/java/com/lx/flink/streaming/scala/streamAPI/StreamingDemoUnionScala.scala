package com.lx.flink.streaming.scala.streamAPI

import com.lx.flink.streaming.scala.custormSource.MyNoParallelSourceScala
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.log4j.{Level, Logger}

/**
  * Created by xuwei.tech on 2018/10/23.
  */
object StreamingDemoUnionScala {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._

    val text1 = env.addSource(new MyNoParallelSourceScala)
    val text2 = env.addSource(new MyNoParallelSourceScala)


    val unionall = text1.union(text2)

    val sum = unionall.map(line=>{
      println("接收到的数据："+line)
      line
    }).timeWindowAll(Time.seconds(2)).sum(0)

    sum.print().setParallelism(1)

    env.execute("StreamingDemoWithMyNoParallelSourceScala")



  }

}
