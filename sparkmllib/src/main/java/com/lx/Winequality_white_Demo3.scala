package com.lx

/**
  * Created by Administrator on 2017/4/8.
  *
  * 逻辑回归--分类
  */

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.sql.{Row, SparkSession}



object Winequality_white_Demo3 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val sess = SparkSession.builder().appName("Winequality_white_Demo3").master("local[4]").getOrCreate();
    val sc = sess.sparkContext
    import sess.implicits._
    //数据目录
    val dataDir = "F:\\bigdataSource\\winequality-white.csv"
    //定义样例类
    case class Wine3(FixedAcidity: Double, VolatileAcidity: Double,
                     CitricAcid: Double, ResidualSugar: Double, Chlorides: Double,
                     FreeSulfurDioxide: Double, TotalSulfurDioxide: Double, Density: Double, PH:
                     Double, Sulphates: Double, Alcohol: Double, Quality: Double)

    //变换
    val wineDataRDD = sc.textFile(dataDir).map(_.split(";")).map(w => Wine3(w(0).toDouble, w(1).toDouble,
      w(2).toDouble, w(3).toDouble, w(4).toDouble, w(5).toDouble, w(6).toDouble, w(7).toDouble, w(8).toDouble
      , w(9).toDouble, w(10).toDouble, w(11).toDouble))


    //转换RDD成DataFrame
    val trainingDF = wineDataRDD.map(w =>
      (if (w.Quality < 7) 0D else 1D,
        Vectors.dense(
        w.FixedAcidity, w.VolatileAcidity, w.CitricAcid,
        w.ResidualSugar, w.Chlorides, w.FreeSulfurDioxide,
        w.TotalSulfurDioxide, w.Density, w.PH, w.Sulphates, w.Alcohol
        )
      )).toDF("label", "features")

    //创建逻辑回归对象
    val lr = new LogisticRegression()
    //设置最大迭代次数
    lr.setMaxIter(10).setRegParam(0.01)
    //拟合
    val model = lr.fit(trainingDF)

    //创建测试Dataframe
    val testDF = sess.createDataFrame(Seq(
      (1.0, Vectors.dense(6.1, 0.32, 0.24, 1.5, 0.036, 43,  140, 0.9894, 3.36, 0.64, 10.7)),
      (0.0, Vectors.dense(5.2, 0.44, 0.04, 1.4, 0.036, 38,  124, 0.9898, 3.29, 0.42, 12.4)),
      (0.0, Vectors.dense(7.2, 0.32, 0.47, 5.1, 0.044, 19,  65,  0.9951, 3.38, 0.36, 9)),
      (0.0, Vectors.dense(6.4, 0.595, 0.14, 5.2, 0.058, 15, 97, 0.991, 3.03, 0.41, 12.6))
    )).toDF("label", "features")

    //显式测试数据
    testDF.show()
    println("==========显式测试数据==============")

    //预测测试数据(带标签),评测模型的质量。
    testDF.createOrReplaceTempView("test")
    val tested = model.transform(testDF).select("features", "label", "prediction")
    tested.show()
    println("==========评测模型的质量==============")


    //预测无标签的测试数据。
    val predictDF = sess.sql("SELECT features FROM test")
    //预测结果
    val predicted = model.transform(predictDF).select("features", "prediction")
    predicted.show()

  }
}
