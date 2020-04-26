package com.lx

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/7/12.
  * 线性回归--保存
  */


object Winequality_white_Demo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    /**
      * 固定酸度、挥发酸度、柠檬酸、残糖、 氯化物、游离二氧化硫、总二氧化硫、密度、 pH值、 硫酸盐、酒精，葡萄酒的质量（基于感觉）
      *    7       0.27    0.36   20.7   0.045     45          170       1.001  3      0.45   8.8     6
      */
    val session = SparkSession.builder()
      .appName("Winequality_white_Demo")
      .master("local[*]")
      .getOrCreate()
    val sc = session.sparkContext
    import session.implicits._
    //数据目录
    val dataDir = "F:\\bigdataSource\\winequality-white.csv"
    //定义样例类
    case class Wine(FixedAcidity: Double, VolatileAcidity: Double,
                    CitricAcid: Double, ResidualSugar: Double, Chlorides: Double,
                    FreeSulfurDioxide: Double, TotalSulfurDioxide: Double, Density: Double, PH:
                    Double, Sulphates: Double, Alcohol: Double, Quality: Double)
    //变换
    val wineDataRDD = sc.textFile(dataDir).map(_.split(";")).map(w => Wine(w(0).toDouble, w(1).toDouble,
      w(2).toDouble, w(3).toDouble, w(4).toDouble, w(5).toDouble, w(6).toDouble, w(7).toDouble, w(8).toDouble
      , w(9).toDouble, w(10).toDouble, w(11).toDouble))
    //wineDataRDD.foreach(println(_))

    //转换RDD成DataFrame
    val trainingDF = wineDataRDD.map(w => (w.Quality,
      Vectors.dense(w.FixedAcidity, w.VolatileAcidity, w.CitricAcid,
        w.ResidualSugar, w.Chlorides, w.FreeSulfurDioxide, w.TotalSulfurDioxide,
        w.Density, w.PH, w.Sulphates, w.Alcohol))).toDF("label", "features") //转换成 label==》标签  和  features==》特征
    //显式数据
    trainingDF.show(1000,1000)
    println("======================")
    //创建线性回归对象
    val lr = new LinearRegression()
    //设置最大迭代次数
    lr.setMaxIter(10)
    //通过线性回归拟合训练数据，生成模型
    val model = lr.fit(trainingDF)
    //模型持久化，非常重要，分布式的话保存到hdfs上
    model.save("F:\\bigdataSource\\model1")
    //创建内存测试数据数据框
    val testDF = session.createDataFrame(Seq(
      (5.0, Vectors.dense(7.4, 0.7, 0.0, 1.9, 0.076, 25.0, 67.0, 0.9968, 3.2, 0.68, 9.8)),
      (5.0, Vectors.dense(7.8, 0.88, 0.0, 2.6, 0.098, 11.0, 34.0, 0.9978, 3.51, 0.56, 9.4)),
      (7.0, Vectors.dense(7.3, 0.65, 0.0, 1.2, 0.065, 15.0, 18.0, 0.9968, 3.36, 0.57, 9.5)))
    ).toDF("label", "features")
    testDF.show(1000,1000)

    //创建临时视图
    testDF.createOrReplaceTempView("test")
    println("======================")
    //利用model对测试数据进行变化，得到新数据框，查询features==特征", "label==标签", "prediction == 预测 方面值。
    val tested = model.transform(testDF).select("features", "label", "prediction")
    tested.show(1000,1000)
  }
}
