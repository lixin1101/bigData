package lx.sql.example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/3/8.
  */
object Demo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark=SparkSession.builder().appName("Demo").master("local[*]").getOrCreate()
    import spark.implicits._
    val df = spark.read.json("F:\\bigdataSource\\plant.txt")
    df.createOrReplaceTempView("plant")
    val sql = spark.sql("select * from plant where userName='李君'")
    sql.show()

  }

}
