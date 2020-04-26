package zyzs.wxapp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Wxapp_shengjin8 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    val df1 = spark.read.json("C:\\Users\\W541\\Desktop\\a.txt")
    val df2 = spark.read.json("C:\\Users\\W541\\Desktop\\b.txt")


    df1.createOrReplaceTempView("t_res")
    df2.createOrReplaceTempView("t_address")



    val sql1 = spark.sql("select * from t_res ")
    val sql2 = spark.sql("select openId,aid,phone,province,city,district,name,address from t_address ")

    sql1.show(100,100)
    sql2.show(100,100)

    sql2.write.csv("F:\\bigdataSource\\sd_data\\r1")





  }
}
