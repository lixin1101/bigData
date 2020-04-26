package com.lx

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import org.bson.Document

object WriteMongo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("WriteMongo")
      .config("spark.mongodb.output.uri", "mongodb://192.168.80.202/lx.products")
      .getOrCreate()

    // 设置log级别
    spark.sparkContext.setLogLevel("WARN")

    val document1 = new Document()
    document1.append("pid", "10").append("pname", "lx").append("price", "30000")
    val document2 = new Document()
    document2.append("pid", "11").append("pname", "zl").append("price", "920")
    val document3 = new Document()
    document3.append("pid", "12").append("pname", "wq").append("price", "400")

    val seq = Seq(document1, document2, document3)
    val df = spark.sparkContext.parallelize(seq)

    // 将数据写入mongo
    MongoSpark.save(df)

    spark.stop()
    System.exit(0)
  }
}
