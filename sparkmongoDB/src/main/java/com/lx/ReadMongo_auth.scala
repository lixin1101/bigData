package com.lx

import com.mongodb.spark.MongoSpark
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, SparkSession}

object ReadMongo_auth {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)

    val conf = new SparkConf().setAppName("ReadMongo_auth").setMaster("local")
    val sc = new SparkContext(conf)
    val array = GetMongoData.getData.toArray
    val rdd: RDD[AnyRef] = sc.parallelize(array)
    rdd.saveAsTextFile("D://aaaaaa")




  }
}
