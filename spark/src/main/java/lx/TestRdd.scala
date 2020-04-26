package lx

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object TestRdd {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().master("local").appName("wordcount").getOrCreate()
    val sc = spark.sparkContext

    /**
      * 1. 作用：返回一个新的RDD，该RDD由每一个输入元素经过func函数转换后组成
      * 2. 需求：创建一个1-10数组的RDD，将所有元素*2形成新的RDD
      */
    /*val source = sc.parallelize(1 to 10)
    val value = source.map(_*2)
    value.foreach(println(_))*/

    /**
      * 1. 作用：类似于map，但独立地在RDD的每一个分片上运行，因此在类型为T的RDD上运行时，
      * func的函数类型必须是Iterator[T] => Iterator[U]。假设有N个元素，有M个分区，
      * 那么map的函数的将被调用N次,而mapPartitions被调用M次,一个函数一次处理所有分区。
      * 2. 需求：创建一个RDD，使每个元素*2组成新的RDD
      */
    /*val source = sc.parallelize(Array(1,2,3,4))
    val value = source.mapPartitions(x=>x.map(_*2))
    value.foreach(println(_))*/

    /**
      * 1. 作用：类似于mapPartitions，但func带有一个整数参数表示分片的索引值，
      * 因此在类型为T的RDD上运行时，func的函数类型必须是(Int, Interator[T]) => Iterator[U]；
      * 2. 需求：创建一个RDD，使每个元素跟所在分区形成一个元组组成一个新的RDD
      */
    /*val rdd = sc.parallelize(Array(1, 2, 3, 4),3)
    val indexRdd = rdd.mapPartitionsWithIndex((index, items) => (items.map((index, _))))
    indexRdd.foreach(println(_))*/

    /**
      * 1. 作用：类似于map，但是每一个输入元素可以被映射为0或多个输出元素（所以func应该返回一个序列，而不是单一元素）
      * 2. 需求：创建一个元素为1-5的RDD，运用flatMap创建一个新的RDD，新的RDD为原RDD的每个元素的2倍（2，4，6，8，10）
      */

    val sourceFlat = sc.parallelize(1 to 5)
    val flatMap = sourceFlat.flatMap(1 to _)
    flatMap.foreach(println(_))



  }

}
