package com.lx

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/7/24.
  * 线性回归模型--加载
  */
object Winequality_white_Demo2 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    /**
      * 固定酸度、挥发酸度、柠檬酸、残糖、 氯化物、游离二氧化硫、总二氧化硫、密度、pH值、硫酸盐、酒精，葡萄酒的质量（基于感觉）
      * 7    0.27      0.36   20.7  0.045     45          170       1.001  3      0.45  8.8     6
      */
    val session = SparkSession.builder()
      .appName("Winequality_white_Demo2")
      .master("local[*]")
      .getOrCreate()
    val sc = session.sparkContext

    import session.implicits._

    //加载线性回归模型
    val model=LinearRegressionModel.load("F:\\bigdataSource\\model")

    //创建内存测试数据数据框
    val testDF = session.createDataFrame(Seq(
      (5.0, Vectors.dense(7.4, 0.7, 0.0, 1.9, 0.076, 25.0, 67.0, 0.9968, 3.2, 0.68, 9.8)),
      (5.0, Vectors.dense(7.8, 0.88, 0.0, 2.6, 0.098, 11.0, 34.0, 0.9978, 3.51, 0.56, 9.4)),
      (7.0, Vectors.dense(7.3, 0.65, 0.0, 1.2, 0.065, 15.0, 18.0, 0.9968, 3.36, 0.57, 9.5)))
    ).toDF("label", "features")
    testDF.show(20,1000)//第二个参数设置长度

    //创建临时视图
    testDF.createOrReplaceTempView("test")
    println("======================")
    //利用model对测试数据进行变化，得到新数据框，查询features", "label", "prediction方面值。
    val tested = model.transform(testDF).select("features", "label", "prediction")
    tested.show()
  }

}
