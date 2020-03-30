package com.lx.flink.batch.scala

import org.apache.flink.api.scala.ExecutionEnvironment

/**
  * Created by xuwei.tech on 2018/10/8.
  */
object BatchWordCountScala {

  def main(args: Array[String]): Unit = {

    val inputPath = "D:\\data\\file"
    val outPut = "D:\\data\\result"


    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.readTextFile(inputPath)

    //引入隐式转换
    import org.apache.flink.api.scala._

    val counts = text.flatMap(_.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map((_,1))
      .groupBy(0)
      .sum(1)
    counts.writeAsCsv(outPut,"\n"," ").setParallelism(1)//行分隔符，字段分隔符
    env.execute("batch word count")
  }

}
