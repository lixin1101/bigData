package lx.sql.project.pro.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/6/2.
  * 鬼吹灯之精绝古城	靳东	2016
  * 更新至12集	http://kan.sogou.com/player/180940915/
  * 9.7	9.5	9.6	www.sina.com.cn 优酷视频  4	6
  *
  * 孤芳不自赏	钟汉良	2017
  * 更新至5集	http://kan.sogou.com/player/180944983/
  * 9.8	9.1	9.8	iface.qiyi.com	腾讯视频	6	5
  */
object AvgMaxDemo {
  def main(args: Array[String]): Unit = {
    /**
      * 算出平均评分最高的电视剧
      */
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("AvgMaxDemo").setMaster("local")
    val sc = new SparkContext(conf)
    sc.textFile("F:\\bigdataSource\\大数据数据\\TVplay3.txt").map(line => {
      val fields = line.split("\t")
      val name = fields(0)
      val p1 = fields(5).toDouble
      val p2 = fields(6).toDouble
      val p3 = fields(7).toDouble
      val avgScores = ((p1 + p2 + p3) / 3).formatted("%.2f")
      (name, avgScores)
    }).sortBy(x=>x._2,false).take(10).foreach(x=>println(x._1+":"+x._2))
    sc.stop()
  }
}
