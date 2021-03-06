package lx.spark_other

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object cartesianDemo1 {
    def main(args: Array[String]): Unit = {
        Logger.getLogger("org").setLevel(Level.WARN)
        val conf = new SparkConf()
        conf.setAppName("cartesianDemo1")
        conf.setMaster("local[4]") ;
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array("tom","tomas","tomasle","tomson"))
        val rdd2 = sc.parallelize(Array("1234","3456","5678","7890"))

        val rdd = rdd1.cartesian(rdd2);
        rdd.collect().foreach(t=>println(t))
    }
}
