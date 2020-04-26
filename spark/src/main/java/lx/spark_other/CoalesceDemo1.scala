package lx.spark_other

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object CoalesceDemo1 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf()
    conf.setAppName("WordCountScala")
    conf.setMaster("local[8]")
    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("F:\\bigdataSource\\a.txt", 7)
    println("rdd1' parti : " + rdd1.partitions.length) //rdd1' parti : 8

    //降低分区
    val rdd11 = rdd1.coalesce(5)

    //再分区,可憎可减
    val rdd111 = rdd11.repartition(5)

    val rdd2 = rdd111.flatMap(_.split(" "))
    println("rdd2' parti : " + rdd2.partitions.length)

    //
    val rdd3 = rdd2.map((_, 1))
    println("rdd3' parti : " + rdd3.partitions.length)
  }
}
