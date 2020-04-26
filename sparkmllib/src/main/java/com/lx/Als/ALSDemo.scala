package com.lx.Als

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.mllib.recommendation.Rating

/**
  * Created by 赌神♣♥♦♠ on 2018/8/7.
  */
object ALSDemo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val session = SparkSession.builder()
      .appName("ALSDemo")
      .master("local[2]")
      .getOrCreate()
    val sc = session.sparkContext

    import session.implicits._
    //1 读取样本数据
    val data = sc.textFile("F:\\bigdataSource\\test.data")
    val ratings = data.map(_.split(',') match {
      case Array(user, item, rate) =>
        //将数据array对象变成rating评估对象
        Rating(user.toInt, item.toInt, rate.toDouble)
    })
    //ratings.foreach(println(_))
    //2 建立模型
    val rank = 10
    val numIterations = 20
    val model = ALS.train(ratings, rank, numIterations, 0.01)
    //3 预测结果
    val usersProducts = ratings.map {
      case Rating(user, product, rate) =>
        (user, product)
    }

    val predictions =
      model.predict(usersProducts).map {
        case Rating(user, product, rate) =>
          ((user, product), rate)
      }
    println("----------------------")
    predictions.foreach(println(_))
    val ratesAndPreds = ratings.map {
      case Rating(user, product, rate) =>
        ((user, product), rate)
    }.join(predictions)

    val MSE = ratesAndPreds.map {
      case ((user, product), (r1, r2)) =>
        val err = (r1 - r2)
        err * err
    }.mean()
    println("Mean Squared Error = " + MSE)
    ratesAndPreds.foreach(println(_))


    //4 保存/加载模型
    /*model.save(sc, "myModelPath")
    val sameModel = MatrixFactorizationModel.load(sc, "myModelPath")*/
  }
}
