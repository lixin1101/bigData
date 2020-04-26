package lx.sparkRDD.AccumulatorOrBroadCast

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/5/28.
  */
object BroadCastDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("BroadCastDemo").setMaster("local")
    val sc = new SparkContext(conf)
    var num = sc.broadcast(10)
    val arr = Array(1, 2, 3)
    sc.parallelize(arr).foreach(x => {
      println(x + num.value)
    })
    println(num.value)
    sc.stop()
  }
}
