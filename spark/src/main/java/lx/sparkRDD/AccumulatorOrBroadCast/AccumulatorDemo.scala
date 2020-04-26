package lx.sparkRDD.AccumulatorOrBroadCast

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/5/28.
  *
  */
object AccumulatorDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("AccumulatorDemo").setMaster("local")
    val sc = new SparkContext(conf)
    //var a=10
    val a = sc.accumulator(1)
    val arr = Array(1, 2, 3)
    sc.parallelize(arr).foreach(x => {
      a += x
      // println(a.value)//会报错，不能在rdd上读取累加器上的值
    })
    println(a.value)//7
    sc.stop()
  }
}
