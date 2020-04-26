package lx.rdd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object BroadcastTest {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //1.初始化spark配置信息并建立与spark的连接
    val session = SparkSession.builder()
      .appName("BroadcastTest")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._
    val sc = session.sparkContext
    val rdd1: RDD[(Int, Int)] = sc.makeRDD(List((1, 4), (2, 5), (3, 7)))
    val list = List((1, 1), (2, 2), (3, 3))

    val broadcast: Broadcast[List[(Int, Int)]] = sc.broadcast(list)
    val resultRdd: RDD[(Int, (Int, Any))] = rdd1.map {
      case (key, value) => {
        var v2: Any = null
        for (elem <- broadcast.value) {
          if (key == elem._1) {
            v2 = elem._2
          }
        }
        (key, (value, v2))
      }
    }
    resultRdd.foreach(println(_))



  }
}
