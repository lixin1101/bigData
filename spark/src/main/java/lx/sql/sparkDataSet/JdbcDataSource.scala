package lx.sql.sparkDataSet

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by 赌神♣♥♦♠ on 2018/6/20.
  */
object JdbcDataSource {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .appName("JdbcDataSource")
      .master("local[*]")
      .getOrCreate()
    val data: DataFrame = session.read.format("jdbc").options(Map(
      "url" -> "jdbc:mysql://localhost:3306/bigdata",
      "driver" -> "com.mysql.jdbc.Driver",
      "dbtable" -> "wc",
      "user" -> "root",
      "password" -> "root"
    )).load()
    data.createTempView("wc")
    //session.sql("select * from wc order by counts desc").show()
    val prop=new Properties()
    prop.put("user","root")
    prop.put("password","root")
    prop.put("driver","com.mysql.jdbc.Driver")
    session.sql("select * from wc order by counts desc").write.mode("append").jdbc(
      "jdbc:mysql://localhost:3306/bigdata?useUnicode=true&characterEncoding=UTF-8",
      "wc",prop)
  }

}
