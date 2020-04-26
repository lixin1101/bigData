package lx.sparkRDD.partitionOrcache

import java.net.URL

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 赌神♣♥♦♠ on 2017/5/26.
  */
object UrlAccessCountDemo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("UrlAccessCountDemo").setMaster("local")
    val sc = new SparkContext(conf)
    //解析日志
    val commonRDD = sc.textFile("F:\\bigdataSource\\beicai.log").map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })
      .reduceByKey(_ + _).map(tuple => {
      val url = tuple._1
      //val hostName = new URL(url).getHost
      val hostName = new URL(url).getHost
      (hostName, tuple)
    })
    val arr: Array[String] = commonRDD.keys.distinct().collect()
      /**
        * coalesce 有两个参数
        * 第一个参数：重分区的个数
        * 第二个参数：是否进行shuffle
        *
        * 如果原来有 N个分区，重分区之后有M个分区
        * M > N  true
        * M < N  false
        */

     // .saveAsTextFile("D:\\tmp\\" + System.currentTimeMillis())
      commonRDD.partitionBy(new MyParatationer(arr))
     // commonRDD.coalesce(4,true)//没有按照自己的意愿分区
    //commonRDD.repartition(100) //底层默认调用的  coalesce(numPartitions, shuffle = true)
        .saveAsTextFile("H:\\tmp\\" + System.currentTimeMillis())
    sc.stop()
  }
}
