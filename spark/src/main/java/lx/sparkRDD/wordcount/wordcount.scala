package lx.sparkRDD.wordcount

import java.sql.{Connection, Driver, DriverManager, PreparedStatement}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/3/6.
  */
object wordcount {
  def data2Mysql(part: Iterator[(String, Int)]): Unit = {
    //创建一个jdbc连接
    val conn: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata?characterEncoding=UTF-8", "root", "root")
    val ps: PreparedStatement = conn.prepareStatement("insert into wc(word,counts) values(?,?)")
    part.foreach(data => {
      ps.setString(1, data._1)
      ps.setInt(2, data._2)
      ps.executeUpdate()
    })
    ps.close()
    conn.close()
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().master("local").appName("wordcount").getOrCreate()
    /*val count=spark.read.text("F:\\a.txt").rdd.map(x=>x.toString()).flatMap(_.split(" "))
      .map((_,1)).reduceByKey(_+_).foreach(println(_))*/
    val res = spark.sparkContext.textFile("F:\\bigdataSource\\a.txt").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    //一次拿出来一个分区，分区用迭代器引用
    res.foreachPartition(part => {
      //传入一个分区过去，一个分区有多条数据，一个分区创建一个jdbc连接，写完这个分区的数据再关闭jdbc连接
      data2Mysql(part)
    })
    res.foreach(println(_))
    spark.stop()
  }

}
