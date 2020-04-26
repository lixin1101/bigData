package lx.spark_other

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * val pairRdd = sc.parallelize(List(("cat",2),("cat",5),("mouse",4),("cat",12),("dog",12),("mouse",2)),2)
  * pairRdd 这个RDD有两个区
  * 一个区中存放的是：
  * ("cat",2),("cat",5),("mouse",4)
  * 另一个分区中存放的是：
  * ("cat",12),("dog",12),("mouse",2)
  * pairRdd.aggregateByKey(100)(math.max(_ , _),  _ + _ ).collect
  * res0: Array[(String,Int)] = Array((dog,100),(cat,200),(mouse,200))
  * aggregateByKey的意思是：按照key进行聚合
  * 第一步：将每个分区内key相同数据放到一起
  * 分区一
  * ("cat",(2,5)),("mouse",4)
  * 分区二
  * ("cat",12),("dog",12),("mouse",2)
  * 第二步：局部求最大值
  * 对每个分区应用传入的第一个函数，math.max(_ , _)，这个函数的功能是求每个分区中每个key的最大值
  * 这个时候要特别注意，aggregateByKe(100)(math.max(_ , _),_+_)里面的那个100，其实是个初始值
  * 在分区一中求最大值的时候,100会被加到每个key的值中，这个时候每个分区就会变成下面的样子
  */
object aggregateByKeyDemo1 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf()
    conf.setAppName("aggregateByKeyDemo1")
    conf.setMaster("local[4]");
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("F:\\bigdataSource\\a.txt", 4)
    val rdd2 = rdd1.flatMap(_.split(" "))
    val rdd3 = rdd2.map((_, 1))

    def seq(a: Int, b: Int): Int = {
      math.max(a, b)
    }

    def comb(a: Int, b: Int): Int = {
      a + b
    }

    rdd3.aggregateByKey(0)(seq, (a: Int, b: Int) => {
      a + b
    }).foreach(println(_))
    /**
      * (spark,1)
      * (hive,2)
      * (hadoop,1)
      * (hello,6)
      * (java,1)
      * (world,1)
      */
  }
}
