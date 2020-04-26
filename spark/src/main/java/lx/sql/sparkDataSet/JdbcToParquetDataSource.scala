package lx.sql.sparkDataSet

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by 赌神♣♥♦♠ on 2018/6/20.
  */
object JdbcToParquetDataSource {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .appName("JdbcDataSource")
      .master("local[*]")
      .getOrCreate()
    val data: DataFrame = session.read.format("jdbc").options(Map(
      "url" -> "jdbc:mysql://localhost:3306/zyzs",
      "driver" -> "com.mysql.jdbc.Driver",
      "dbtable" -> "sd_data",
      "user" -> "root",
      "password" -> "root"
    )).load()
    data.createTempView("product")

    session.sql("select * from product order by REPORTDATE desc limit 10")
      .write.mode("append").parquet("D:\\数据\\sd_data")
  }

}
