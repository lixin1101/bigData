package zyzs.wxapp


import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Wxapp_shengjin7 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().appName("Demo").master("local[*]")
      .config("spark.sql.shuffle.partitions", 1)
      .getOrCreate()

    val df1 = spark.read.json("F:\\bigdataSource\\sd_data\\t_address.json")
    val df2 = spark.read.json("F:\\bigdataSource\\sd_data\\t_articles.json")
    val df3 = spark.read.json("F:\\bigdataSource\\sd_data\\t_prize_record.json")
    val df4 = spark.read.json("F:\\bigdataSource\\sd_data\\a_id.txt")

    df1.createOrReplaceTempView("t_address")
    df2.createOrReplaceTempView("t_articles")
    df3.createOrReplaceTempView("t_prize_record")
    df4.createOrReplaceTempView("a_id")
   /* spark.udf.register("get_json_field", (json:String, field:String) => {
      val jsonObject = JSON.parseObject(json).getJSONObject("author")
      jsonObject.getString(field)
    })*/

    val sql1 = spark.sql("select * from t_address where periods='chunlian'")
    val sql2 = spark.sql("select * from t_prize_record where isWin='1'")
    val sql4 = spark.sql("select openid,aid,createTime,content from t_articles where periods='chunlian' order by createTime desc ")
    val sql5 = spark.sql("select aid,author from t_articles where periods='chunlian' order by createTime desc ")

    val sql6 = spark.sql("select count(1) from t_prize_record where isWin='1'")
    val sql7 = spark.sql("select aid from a_id ")
    val sql8 = spark.sql(
      """
        |select * from t_prize_record where isWin='1' and aid not in
        |(select aid from a_id)
      """.stripMargin)

    val sq13=spark.sql(
      """
        | select
        | t1.aid as a_id,t1.openId as oid,t2.address,
        | t2.aid,t2.province,t2.city,t2.district,
        | t2.openId,t2.phone,t2.name,
        | t3.content,t3.createTime
        | from t_prize_record t1
        | left join t_address t2 on t1.aid=t2.aid
        | left join t_articles t3 on t1.aid=t3.aid
        | where t1.isWin='1' and t2.periods='chunlian' and t3.periods='chunlian' order by t3.createTime desc
      """.stripMargin)

    sql6.show(100,1000)
    sql8.show(100,1000)
    //sql4.write.csv("F:\\bigdataSource\\sd_data\\r001")


    //sq13.show(100,100)

    //sq13.write.csv("F:\\bigdataSource\\sd_data\\r1")
    //sql2.show(100,100)
    //sql4.show(100,100)

  }
}
