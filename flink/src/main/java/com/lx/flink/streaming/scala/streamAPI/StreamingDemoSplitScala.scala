package com.lx.flink.streaming.scala.streamAPI

import java.util

import com.lx.flink.streaming.scala.custormSource.MyNoParallelSourceScala
import org.apache.flink.streaming.api.collector.selector.OutputSelector
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.log4j.{Level, Logger}

/**
  * Created by xuwei.tech on 2018/10/23.
  */
object StreamingDemoSplitScala {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._

    val text = env.addSource(new MyNoParallelSourceScala)

    val splitStream = text.split(new OutputSelector[Long] {
      override def select(value: Long) = {
        val list = new util.ArrayList[String]()
        if(value%2 == 0){
          list.add("even")// 偶数
        }else{
          list.add("odd")// 奇数
        }
        list
      }
    })

    val evenStream = splitStream.select("even")

    evenStream.print().setParallelism(1)

    env.execute("StreamingDemoWithMyNoParallelSourceScala")



  }

}
