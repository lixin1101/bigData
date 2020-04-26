package lx.sparkRDD.partitionOrcache

import org.apache.log4j.{Level, Logger}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 赌神♣♥♦♠ on 2017/5/27.
  */
object RDDCacheDemo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("RDDCacheDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val commonRDD = sc.textFile("H:\\原电脑d盘数据\\数据\\log_network.txt").flatMap(_.split(" "))
      .map((_, 1)).reduceByKey(_ + _).persist(StorageLevel.MEMORY_ONLY)//.cache()

    var startTime = System.currentTimeMillis()
    println(commonRDD.count())
    var endTime = System.currentTimeMillis()
    println(endTime - startTime)
    println("**************************************")


    startTime = System.currentTimeMillis()
    println(commonRDD.collect().toBuffer)
    endTime = System.currentTimeMillis()
    println(endTime - startTime)
    println("**************************************")

    startTime = System.currentTimeMillis()
    commonRDD.foreach(println(_))
    endTime = System.currentTimeMillis()
    println(endTime - startTime)
    println("**************************************")


    startTime = System.currentTimeMillis()
    println(commonRDD.count())
    endTime = System.currentTimeMillis()
    println(endTime - startTime)
    println("**************************************")

    sc.stop()

  }
}
