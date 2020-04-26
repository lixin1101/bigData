package lx.sparkRDD.TransformationORaction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/3/6.
  */
object Demo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().master("local").appName("Demo").getOrCreate();
    val sc = spark.sparkContext
    /*val arr=Array(1,2,3,4,5,6,7,8,9,10)
    val rdd1 = context.parallelize(arr,3)
    println(rdd1.partitions.length)*/
    //val list1 = List(5, 6, 4, 7, 3, 8, 2, 9, 1, 10)
    //context.parallelize(list1).map(x=>x*2).foreach(println(_))
    //context.parallelize(list1).map(x=>x*2).sortBy(x=>x,true).foreach(println(_))
    /*context.parallelize(list1).map(x=>x*2).sortBy(x=>x,true)
      .foreach(println(_))*/
    /*val arr = Array("a b c", "d e f", "h i j")
    context.parallelize(arr).flatMap(_.split(" ")).collect().foreach(println(_))
    val rdd = context.parallelize(List(List("a b c", "a b b"),List("e f g", "a f g"), List("h i j", "a a b")))
    rdd.flatMap(_.flatMap(_.split(" "))).collect().foreach(x=>print(x+" "))*/
    val rdd1 = sc.parallelize(List(5,6,4,7))
    val rdd2 = sc.parallelize(List(1,2,3,4))
    //rdd1.union(rdd2).collect().foreach(println(_)) //并集
    //rdd1.intersection(rdd2).collect().foreach(println(_)) //交集
    //rdd1.union(rdd2).distinct().sortBy(x=>x,true).collect().foreach(println(_))

  }

}
