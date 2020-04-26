package lx.sql.project.pro.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 根据地区和类型分区
  * Created by Administrator on 2017/6/3.
  * 孤芳不自赏	钟汉良	2017
  * 更新至5集	http://kan.sogou.com/player/180944983/
  * 9.8	9.1	9.8	iface.qiyi.com	腾讯视频	6	5
  */
object MrCountYearDemo {
  def main(args: Array[String]): Unit = {
    /**
      * 自定义分区算出每年的电视剧量
      */
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("AvgMaxDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val comRDD = sc.textFile("F:\\bigdataSource\\大数据数据\\TVplay3.txt").map(line => {
      val fields = line.split("\t")
      val year = fields(2)
      (year, 1)
    }).reduceByKey(_ + _).map(tuple => {
      (tuple._1, ("该年的电视剧产量", tuple._2))
    })
    //val arr: Array[String] = comRDD.keys.collect()
    comRDD.foreach(println(_))
    /*comRDD.partitionBy(new MyParatationerYear(arr))
      //.saveAsTextFile("hdfs://hadoop01:9000/out/" +System.currentTimeMillis())
      .saveAsTextFile("D:\\tmp\\" + System.currentTimeMillis())*/
    sc.stop()
  }
}
