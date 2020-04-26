package zyzs.wxapp

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Wxapp_shengjin {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    val df2 = spark.read.json("F:\\bigdataSource\\sd_data\\t_articles.json")
    val df3 = spark.read.json("F:\\bigdataSource\\sd_data\\t_terminalInfo.json")

    df2.createOrReplaceTempView("t_articles")
    df3.createOrReplaceTempView("t_terminalInfo")
    //val sql = spark.sql("select periods, count(1),count(distinct openId),sum(thumbs) from t_articles where createTime > '2019-08-21  12:00:00' group by periods order by count(1) desc ")
    //val sql2 = spark.sql("select * from t_articles where createTime > '2019-08-21  12:00:00' and  periods = '211100' ")
    val sql3 = spark.sql(
      """
        |select
        |CASE
        |WHEN periods = '210100' THEN '沈阳赛区'
        |WHEN periods = '210300' THEN '鞍山赛区'
        |WHEN periods = '210400' THEN '抚顺赛区'
        |WHEN periods = '210500' THEN '本溪赛区'
        |WHEN periods = '210600' THEN '丹东赛区'
        |WHEN periods = '210700' THEN '锦州赛区'
        |WHEN periods = '210800' THEN '营口赛区'
        |WHEN periods = '210900' THEN '阜新赛区'
        |WHEN periods = '211000' THEN '辽阳赛区'
        |WHEN periods = '211100' THEN '盘锦赛区'
        |WHEN periods = '211200' THEN '铁岭赛区'
        |WHEN periods = '211300' THEN '朝阳赛区'
        |WHEN periods = '211400' THEN '葫芦岛赛区'
        |END  as area,
        |count(1),count(distinct openId),sum(thumbs)
        |from t_articles where createTime > '2019-08-21  12:00:00'
        |group by
        |CASE
        |WHEN periods = '210100' THEN '沈阳赛区'
        |WHEN periods = '210300' THEN '鞍山赛区'
        |WHEN periods = '210400' THEN '抚顺赛区'
        |WHEN periods = '210500' THEN '本溪赛区'
        |WHEN periods = '210600' THEN '丹东赛区'
        |WHEN periods = '210700' THEN '锦州赛区'
        |WHEN periods = '210800' THEN '营口赛区'
        |WHEN periods = '210900' THEN '阜新赛区'
        |WHEN periods = '211000' THEN '辽阳赛区'
        |WHEN periods = '211100' THEN '盘锦赛区'
        |WHEN periods = '211200' THEN '铁岭赛区'
        |WHEN periods = '211300' THEN '朝阳赛区'
        |WHEN periods = '211400' THEN '葫芦岛赛区'
        |END
        |order by count(1) desc
        |
      """.stripMargin)


    sql3.show()

    /*val sql4=spark.sql(
      """
        |select areaName,name as s_name,openId,phone,shopName from t_terminalInfo
        |where (shopName like '%超市%' or shopName like '%店%' or shopName like '%批发%' or shopName like '%副食%')
      """.stripMargin)*/

    /*val sql4=spark.sql(
      """
        |select areaName,name as s_name,openId,phone,shopName from t_terminalInfo
      """.stripMargin)

    val sql5=spark.sql(
      """
        |select t1.name as s_name,t1.openId,t1.phone,t1.shopName,t2.thumbs,t2.periods from t_terminalInfo t1
        |left join t_articles t2 on t1.openId = t2.openId
        |where t2.createTime > '2019-08-21  12:00:00' and t2.periods like '21%' order by thumbs desc
        |
      """.stripMargin)

    sql5.show(100,1000)

    sql5.write.csv("F:\\bigdataSource\\sd_data\\res02")*/

  }
}
