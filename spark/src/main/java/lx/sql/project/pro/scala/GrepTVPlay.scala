package lx.sql.project.pro.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/6/2.
  * 孤芳不自赏	钟汉良	2017
  * 更新至5集	http://kan.sogou.com/player/180944983/
  * 9.8	9.1	9.8	iface.qiyi.com	腾讯视频	6	5
  */
object GrepTVPlay {
  /**
    * 过滤出正在更新的数据
    */
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("AvgMaxDemo").setMaster("local")
    val sc = new SparkContext(conf)
    sc.textFile("F:\\bigdataSource\\大数据数据\\TVplay3.txt").map(line => {
      val fields = line.split("\t")
      val name = fields(0)
      val tvNum = fields(3)
      (name,tvNum)
    }).filter(x=>{
      !x._2.contains("至")
    }).take(5000).foreach(println(_))
    sc.stop()
  }
}
