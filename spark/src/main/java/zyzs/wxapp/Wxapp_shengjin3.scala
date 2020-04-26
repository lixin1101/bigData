package zyzs.wxapp

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Wxapp_shengjin3 {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    val df2 = spark.read.json("F:\\bigdataSource\\sd_data\\t_articles.json")

    df2.createOrReplaceTempView("t_articles")

    val sql=spark.sql(
      """
        |select openid,aid,createTime,periods,thumbs from t_articles
        |where createTime >= '2019-08-21  12:00:00'
        |and periods like '21%' order by thumbs desc
      """.stripMargin)

    val prop=new Properties()
    prop.put("user","root")
    prop.put("password","root")
    prop.put("driver","com.mysql.jdbc.Driver")
    sql.write.mode("append").jdbc(
      "jdbc:mysql://localhost:3306/zyzs?useUnicode=true&characterEncoding=UTF-8",
      "aid_status",prop)


  }
}
