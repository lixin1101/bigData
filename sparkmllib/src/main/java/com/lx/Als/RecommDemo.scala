package com.lx.Als

/**
  * Created by Administrator on 2017/4/9.
  *
  * 训练一个矩阵分解模型，给定用户对产品子集的RDD评级。
  * 评级矩阵近似为给定秩的两个低秩矩阵的乘积。
  * （特征数）。为了解决这些特性，ALS用可配置的迭代运行。
  * 平行度水平。
  */

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.mllib.recommendation.Rating

object RecommDemo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("Recommend").setMaster("local[4]")
    val sc = new SparkContext(conf)
    // Load and parse the data
    val data = sc.textFile("F:\\bigdataSource\\data2.txt")
    /**
      * user, item, rate
      * 1,0,1.0
      * 1,1,2.0
      * 1,2,5.0
      * 1,3,5.0
      * 1,4,5.0
      * 2,0,1.0
      * 2,1,2.0
      * 2,2,5.0
      * 2,5,5.0
      * 2,6,4.5
      * 3,1,2.5
      * 3,2,5.0
      * 3,3,4.0
      * 3,4,3.0
      * 4,0,5.0
      * 4,1,5.0
      * 4,2,5.0
      * 4,3,0.0
      */
    //变换数据成为Rating[评估]。
    val ratings = data.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    //ratings.collect().foreach(println(_))
    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    //交替最小二乘法算法构建推荐模型
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    // 取出评分数据的(User,product)
    /*val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }*/

    //通过model对(user,product)进行预测,((user, product),rate)
    val ug2 = sc.makeRDD(Array(
        (2, 3),
        (2, 4)
      ))
    //对数据进行转换，把用户和商品id放到一起
    val predictions =
      model.predict(ug2).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    println("对测试数据进行预测，返回元组")
    predictions.collect().foreach(println)
    println("############################")
    //向用户5推荐8款商品
    val res0 = model.recommendProducts(4,8)
    res0.foreach(println(_))
    println("#####################")
    //将指定的商品3推荐给5个用户
    val res1 = model.recommendUsers(3,5)
    res1.foreach(println(_))
    println("#####################")
    //向所有用户推荐3种商品  top3
    val res2 = model.recommendProductsForUsers(3)
    res2.foreach(e => {
      println(e._1 + " ======= ")
      e._2.foreach(println)
    })
  }
}
