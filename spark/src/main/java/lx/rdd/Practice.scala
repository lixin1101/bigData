package lx.rdd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Practice {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //1.初始化spark配置信息并建立与spark的连接
    val session = SparkSession.builder()
      .appName("WorldCountDemo")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._
    val sc = session.sparkContext
    //2.读取数据生成RDD：TS，Province，City，User，AD  统计出每一个省份广告被点击次数的TOP3
    /**
      * TS，Province，City，User，AD
      * 1516609143867 6 7 64 16
      * 1516609143869 9 4 75 18
      * 1516609143869 1 7 87 12
      */
    val line = sc.textFile("F:\\bigdataSource\\shangguigu\\agent.log")

    //3.按照最小粒度聚合：((Province,AD),1)
    val provinceAdToOne = line.map { x =>
      val fields: Array[String] = x.split(" ")
      ((fields(1), fields(4)), 1)
    }

    //4.计算每个省中每个广告被点击的总数：((Province,AD),sum)
    val provinceAdToSum = provinceAdToOne.reduceByKey(_ + _)

    //5.将省份作为key，广告加点击数为value：(Province,(AD,sum))
    val provinceToAdSum = provinceAdToSum.map(x => (x._1._1, (x._1._2, x._2)))

    //6.将同一个省份的所有广告进行聚合(Province,List((AD1,sum1),(AD2,sum2)...))
    val provinceGroup = provinceToAdSum.groupByKey()

    //7.对同一个省份所有广告的集合进行排序并取前3条，排序规则为广告点击总数
    val provinceAdTop3 = provinceGroup.mapValues { x =>
      x.toList.sortWith((x, y) => x._2 > y._2).take(3)
    }

    //8.将数据拉取到Driver端并打印
    provinceAdTop3.collect().foreach(println)

    //9.关闭与spark的连接
    sc.stop()

  }
}
