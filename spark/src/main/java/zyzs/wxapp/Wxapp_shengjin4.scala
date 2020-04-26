package zyzs.wxapp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Wxapp_shengjin4 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    val df2 = spark.read.json("F:\\bigdataSource\\sd_data\\t_articles.json")
    val df3 = spark.read.json("F:\\bigdataSource\\sd_data\\t_terminalInfo.json")

    df2.createOrReplaceTempView("t_articles")
    df3.createOrReplaceTempView("t_terminalInfo")




    val sql4=spark.sql(
      """
        |select areaName,name as s_name,openId,phone,shopName from t_terminalInfo
      """.stripMargin)

    val sql5=spark.sql(
      """
        |select t1.name as s_name,t1.openId,t1.phone,t1.shopName,t2.thumbs,t2.periods,t2.content from t_terminalInfo t1
        |left join t_articles t2 on t1.openId = t2.openId
        |where t2.createTime > '2019-08-21  12:00:00' and t2.periods like '21%' order by thumbs desc
        |
      """.stripMargin)

    sql5.show(100,1000)

    sql5.write.csv("F:\\bigdataSource\\sd_data\\res03")

  }
}
