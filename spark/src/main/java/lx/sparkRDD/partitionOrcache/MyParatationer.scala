package lx.sparkRDD.partitionOrcache

import org.apache.spark.Partitioner

import scala.collection.mutable

/**
  * Created by 赌神♣♥♦♠ on 2017/5/27.
  */
class MyParatationer(var arr: Array[String]) extends Partitioner {
  val map = new mutable.HashMap[String, Int]()
  for (i <- 0 until (arr.length)) {
    map.put(arr(i), i)
  }

  override def numPartitions: Int = arr.length

  override def getPartition(key: Any): Int = {
    val k = key.toString
    map.getOrElse(k, arr.length + 1)
  }
}
