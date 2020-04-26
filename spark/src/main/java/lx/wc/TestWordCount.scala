package lx.wc

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TestWordCount {
  def data2Mysql(part: Iterator[Row]): Unit = {
    val conn: Connection = DriverManager
      .getConnection("jdbc:mysql://localhost:3306/bigdata?characterEncoding=UTF-8",
        "root", "root")
    val ps: PreparedStatement =
      conn.prepareStatement("insert into wc1(word,counts) values(?,?)")
    part.foreach(r => {
      val w = r.getAs[String](0)
      val c = r.getAs[Long](1)
      ps.setString(1, w)
      ps.setLong(2, c)
      ps.executeUpdate()
    })
    ps.close()
    conn.close()
  }

  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .appName("WorldCountDemo")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._
    val line: Dataset[String] = session.read.textFile("F:\\bigdataSource\\a.txt")
    val words: Dataset[String] = line.flatMap(_.split(" "))
    val df: DataFrame = words.withColumnRenamed("value", "words")
    df.createTempView("v_wc")
    val res: DataFrame = session.sql("select words,count(1) as c from v_wc group by words order by c desc")
    res.show()
    /*res.foreachPartition(part => {
      data2Mysql(part)
    })*/
  }
}
