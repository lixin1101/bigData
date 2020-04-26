package com.lx.linearRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SparkSession

/**
  * 线性回归算法
  */
object LinearRegression {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.WARN)
    val session = SparkSession.builder()
      .appName("LinearRegression")
      .master("local[*]")
      .getOrCreate()
    val sc = session.sparkContext

    import session.implicits._

    //读取样本数据
    val data_path1 = "F:\\bigdataSource\\lpsa.data"
    val data = sc.textFile(data_path1)
    val examples = data.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }.cache()
    val numExamples = examples.count()
    println(numExamples)

    // 新建线性回归模型，并设置训练参数
    //迭代次数
    val numIterations = 100
    //步长
    val stepSize = 1
    //随机抽样比例
    val miniBatchFraction = 1.0
    val model = LinearRegressionWithSGD.train(examples, numIterations, stepSize, miniBatchFraction)
    //model.weights
    //model.intercept
    println(model.weights)
    println(model.intercept)

    // 对样本进行测试
    val prediction = model.predict(examples.map(_.features))
    val predictionAndLabel = prediction.zip(examples.map(_.label))
    val print_predict = predictionAndLabel.take(20)
    println("prediction" + "\t" + "label")
    for (i <- 0 to print_predict.length - 1) {
      println(print_predict(i)._1 + "\t" + print_predict(i)._2)
    }

    // 计算测试误差  均方根误差
    val loss = predictionAndLabel.map {
      case (p, l) =>
        val err = p - l
        err * err
    }.reduce(_ + _)
    val rmse = math.sqrt(loss / numExamples)
    println(s"Test RMSE = $rmse.")

    // 模型保存
    /*val ModelPath = "/user/huangmeiling/LinearRegressionModel"
    model.save(sc, ModelPath)
    val sameModel = LinearRegressionModel.load(sc, ModelPath)*/

  }

}
