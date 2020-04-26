package lx.sql.sparkDataSet

import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  * Created by 赌神♣♥♦♠ on 2018/6/13.
  */
object DatasetWCtDemo {
  def main(args: Array[String]): Unit = {
    val session=SparkSession.builder()
      .appName("DatasetWCtDemo")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._
    val line: Dataset[String] = session.read.textFile("F:\\bigdataSource\\a.txt")
    val words: Dataset[String] = line.flatMap(_.split(" "))
    import org.apache.spark.sql.functions._
    val res: Dataset[Row] = words.groupBy($"value" as "word")
      .agg(count("*") as "counts").sort($"counts" desc)
    res.show()
  }

}
