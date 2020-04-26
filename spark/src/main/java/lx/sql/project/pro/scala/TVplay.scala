package lx.sql.project.pro.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

/**
  * Created by Administrator on 2017/6/3.
  * 鬼吹灯之精绝古城	靳东	2016
  * 更新至12集	http://kan.sogou.com/player/180940915/
  * 9.7	9.5	9.6	www.sina.com.cn 优酷视频  4	6
  */
case class TVplay(var nameTV: String, var name: String, var year: String, var tvNum: String
                  , var url: String, var p1: String, var p2: String, var p3: String,
                  var web: String, var app: String, var local: Int, var typeTV: Int)

case class TVlocal(var localId: Int, var local: String)

case class TVtype(var typeTVId: Int, var typeTV: String)

object TVplay {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("TVplay").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val tvRDD = sc.textFile("F:\\bigdataSource\\大数据数据\\TVplay3.txt").map(line => {
      val fields = line.split("\t")
      TVplay(fields(0), fields(1), fields(2), fields(3),
        fields(4), fields(5), fields(6), fields(7), fields(8), fields(9), fields(10).toInt, fields(11).toInt)
    })
    val tvLocalRDD = sc.textFile("F:\\bigdataSource\\大数据数据\\local.txt").map(line => {
      val fields = line.split("\t")
      TVlocal(fields(0).toInt, fields(1))
    })
    val tvTypeRDD = sc.textFile("F:\\bigdataSource\\大数据数据\\type.txt").map(line => {
      val fields = line.split("\t")
      TVtype(fields(0).toInt, fields(1))
    })
    import sqlContext.implicits._
    tvRDD.toDF().registerTempTable("tvPlay")
    tvLocalRDD.toDF().registerTempTable("local")
    tvTypeRDD.toDF().registerTempTable("typeTV")
    /*sqlContext.sql("select * from local").show()
    sqlContext.sql("select * from typeTV").show()
    sqlContext.sql("select * from tvPlay").show()*/
    sqlContext.sql(
      """
        |select tv.nameTV,tv.name,tv.year,tv.tvNum,
        |tv.p1,tv.p2,tv.p3,
        |local.local from tvPlay tv
        |left join local local on tv.local=local.localId
      """.stripMargin)
        .write.mode(SaveMode.Overwrite).json("F:\\bigdataSource\\大数据数据\\json")
    sc.stop()
  }
}
