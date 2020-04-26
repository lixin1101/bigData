package zyzs.wxapp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Wxapp_shengjin5 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    val df1 = spark.read.json("F:\\bigdataSource\\sd_data\\t_articles.json")
    val df2 = spark.read.json("F:\\bigdataSource\\sd_data\\t_address.json")
    val df3 = spark.read.json("F:\\bigdataSource\\sd_data\\t_rank_result.json")
    df1.createOrReplaceTempView("t_articles")
    df2.createOrReplaceTempView("t_address")
    df3.createOrReplaceTempView("t_rank_result")

    /*val sql1: DataFrame = spark.sql("select aid,openId,rank,prizeName,author from t_rank_result where type='month'")
    val sql2: DataFrame = spark.sql("select aid,openId,rank,prizeName,author from t_rank_result where type='week'")
    val sql3: DataFrame = spark.sql("select aid,openId,rank,prizeName,author from t_rank_result where type='custom'")
    sql1.show(1000, 1000)
    sql2.show(1000, 1000)
    sql2.show(1000, 1000)*/

    val sql4: DataFrame = spark.sql(
      """
        |
        |select
        |t1.aid,t2.aid as a_id,
        |t1.openId,t2.openId as o_id,
        |t2.phone,t2.name,t2.address,concat(province,'-',city,'-',district) as area,
        |t1.prizeName
        |from t_rank_result t1
        |left join t_address t2 on t1.aid=t2.aid
        |where t1.type='custom'
        |
        |
      """.stripMargin)

    val sql5: DataFrame = spark.sql(
      """
        |
        |select
        |t1.aid,t2.aid as a_id,
        |t1.openId,t2.openId as o_id,
        |t1.rank,t1.author
        |from t_rank_result t1
        |left join t_address t2 on t1.aid=t2.aid
        |where t1.type='custom'
        |
        |
      """.stripMargin)

    sql4.write.csv("F:\\bigdataSource\\sd_data\\custom_result_c")
    sql5.write.json("F:\\bigdataSource\\sd_data\\custom_result_j")
  }
}
