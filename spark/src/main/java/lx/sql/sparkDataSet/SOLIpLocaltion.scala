package lx.sql.sparkDataSet

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
  * Created by 赌神♣♥♦♠ on 2018/6/20.
  */
object SOLIpLocaltion {

  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .appName("JdbcDataSource")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._

    /*val ruleLine: Dataset[String] = session.read.textFile("ip.txt")
    val ruleDs: Dataset[(Long, Long, String)] = ruleLine.map(line => {
      val fields = line.split("[|]")
      val startNum = fields(2).toLong
      val endNum = fields(3).toLong
      val province = fields(6)
      (startNum, endNum, province)
    })
    val ruleDf: DataFrame = ruleDs.toDF("start_num", "end_num", "province")
    ruleDf.createTempView("v_rules")*/

    //读取访问日志
    val accessDs: Dataset[String] = session.read.textFile("F:\\bigdataSource\\计算IP地址归属地\\计算IP地址归属地\\access.log")
    val ipDs: Dataset[(Long, String)] = accessDs.map(line => {
      val fields = line.split("[|]")
      val time = fields(0).toLong
      val ip = fields(1)
      (time, ip)
    })
    val ipDf: DataFrame = ipDs.toDF("time","ip")
    ipDf.foreach(println(_))
    //ipDf.write.parquet("D:\\数据\\access_parquet")

  }

}
