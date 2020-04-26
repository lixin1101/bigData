package lx.sparkRDD.AccumulatorOrBroadCast

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/6/2.
  */
case class Student(var id: Int, var name: String, var age: Int)

case class Score(var id: Int, var chinese: Int, var math: Int, var english: Int)

object Demo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("Demo").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val stuRDD = sc.textFile("F:\\bigdataSource\\score\\student.txt").map(line => {
      val fields = line.split(",")
      Student(fields(0).toInt, fields(1), fields(2).toInt)
    })
    val scoreRDD = sc.textFile("F:\\bigdataSource\\score\\score.txt").map(line => {
      val fields = line.split(",")
      Score(fields(0).toInt, fields(1).toInt, fields(2).toInt,fields(3).toInt)
    })
    import sqlContext.implicits._
    val stuDF = stuRDD.toDF()
    val scoreDF = scoreRDD.toDF()
    stuDF.registerTempTable("student")
    scoreDF.registerTempTable("score")
    sqlContext.sql("select st.name,st.age,sc.chinese,sc.math,sc.english from student st , score sc where st.id=sc.id  ").show()

    sc.stop()
  }
}
