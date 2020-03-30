package com.lx.flink.batch.scala.batchAPI

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.log4j.{Level, Logger}

import scala.collection.mutable.ListBuffer

/**
  * Created by xuwei.tech on 2018/10/30.
  */
object BatchDemoDistinctScala {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._

    val data = ListBuffer[String]()

    data.append("hello you")
    data.append("hello me")

    val text = env.fromCollection(data)

    val flatMapData = text.flatMap(line=>{
      val words = line.split("\\W+")
      for(word <- words){
        println("单词："+word)
      }
      words
    })

    flatMapData.distinct().print()


  }

}
