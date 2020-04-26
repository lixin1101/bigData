package lx.rdd

import java.sql.DriverManager

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}


object MysqlRDD {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //1.创建spark配置信息
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MysqlRDD")

    //2.创建SparkContext
    val sc = new SparkContext(sparkConf)

    //3.定义连接mysql的参数
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://127.0.0.1:3306/test"
    val userName = "root"
    val passWd = "root"

    //创建JdbcRDD
    val rdd = new JdbcRDD(sc, () => {
      Class.forName(driver)
      DriverManager.getConnection(url, userName, passWd)
    },
      "select * from `user` where `id` >= ? and `id`< ?;",
      1,
      10,
      1,
      r => (r.getInt(1), r.getString(2), r.getInt(3))
    )

    //打印最后结果
    println(rdd.count())
    rdd.foreach(println)

    sc.stop()
  }
}
