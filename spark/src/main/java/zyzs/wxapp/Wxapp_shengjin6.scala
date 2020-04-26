package zyzs.wxapp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Wxapp_shengjin6 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    /* val df2 = spark.read.json("C:\\Users\\W541\\Desktop\\month.txt")

     df2.createOrReplaceTempView("t_week")

     val sql1 = spark.sql("select rank,author from t_week")
     val sql2 = spark.sql("select aid,openId,prizeName,rank from t_week")
     sql2.write.csv("F:\\bigdataSource\\sd_data\\c_res001")
     sql1.write.json("F:\\bigdataSource\\sd_data\\j_res001")*/
    val df1 = spark.read.json("F:\\bigdataSource\\sd_data\\t_articles.json")
    val df2 = spark.read.json("F:\\bigdataSource\\sd_data\\t_thumbs_record.json")
    val df3 = spark.read.json("F:\\bigdataSource\\sd_data\\a_id.txt")
    df1.createOrReplaceTempView("t_articles")
    df2.createOrReplaceTempView("t_thumbs_record")
    df3.createOrReplaceTempView("a_id")

    val sql1 = spark.sql("select count(1) from t_articles where periods='chunlian' and remark='通过'")

    val sql2 = spark.sql(
      """
        |
        |select t1.openId,t1.aid,t1.createTime,t1.thumbs,t1.content from t_articles t1
        |where t1.periods='chunlian' and t1.remark='通过'
        |order by t1.thumbs desc
        |
      """.stripMargin)

    val sql3 = spark.sql(
      """
        |select aid,count(1),
        |count(if(createDate >= '2020-01-01' and createDate < '2020-01-02',1,null)) as c1,
        |count(if(createDate >= '2020-01-02' and createDate < '2020-01-03',1,null)) as c2,
        |count(if(createDate >= '2020-01-03' and createDate < '2020-01-04',1,null)) as c3,
        |count(if(createDate >= '2020-01-04' and createDate < '2020-01-05',1,null)) as c4,
        |count(if(createDate >= '2020-01-05' and createDate < '2020-01-06',1,null)) as c5,
        |count(if(createDate >= '2020-01-06' and createDate < '2020-01-07',1,null)) as c6
        |from t_thumbs_record where  aid  in
        |(select aid from a_id) group by aid order by count(1) desc
      """.stripMargin)

    /*val sql3 = spark.sql(
      """
        |
        |select a1.openId,a1.aid,a1,thumbs from (
        |select t1.openId,t1.aid,t1.createTime,t1.thumbs,t2.aid as a_id,t2.openId as o_id,t2.createDate from t_articles t1
        |left join t_thumbs_record t2 on t1.aid=t2.aid
        |where t1.periods='chunlian' and t1.remark='通过'
        |) a1 order by a1.thumbs desc limit 100
        |
      """.stripMargin)*/

   /* sql2.show(1000,1000)
    sql2.write.csv("F:\\bigdataSource\\sd_data\\res01")*/
    sql3.show(1000,1000)
    sql3.write.csv("F:\\bigdataSource\\sd_data\\res02")

  }
}
