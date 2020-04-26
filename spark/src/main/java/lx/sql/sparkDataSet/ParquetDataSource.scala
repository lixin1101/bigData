package lx.sql.sparkDataSet

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by 赌神♣♥♦♠ on 2018/6/20.
  */
object ParquetDataSource {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder().appName("ParquetDataSource").master("local[*]")
      .getOrCreate()
    val data: DataFrame = session.read.format("parquet").load("H:\\原电脑d盘数据\\数据\\sd_data")
    data.createTempView("s")
    session.sql("select TOBANAME from s limit 10").show()


  }

}
