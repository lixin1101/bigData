package lx.sparkRDD.example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/3/6.
  * 需求：求出用户停留时间最长的两个基站
  */
object UserLocalDemo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().master("local").appName("UserLocalDemo").getOrCreate()
    val sc = spark.sparkContext
    val lineRdd = sc.textFile("F:\\bigdataSource\\jz_log")
    //18688888888,20160327082400,16030401EAFB68F1E3CDF819735E1C66,1
    val lacLogRDD = lineRdd.map(line => {
      val fields = line.split(",")
      val tel = fields(0)
      val locId = fields(2)
      val time = if (fields(3).equals("1")) -fields(1).toLong else fields(1).toLong
      ((tel, locId), time)
    })
    //计算每个用户在每个基站停留的总时间
    val stopTimeRDD = lacLogRDD.reduceByKey(_ + _)
    //stopTimeRDD.collect().foreach(println(_))
    val lacIdRDD = stopTimeRDD.map(tuple => {
      //((tel, lacId), time)===>(lacId,(tel, time ))
      (tuple._1._2, (tuple._1._1, tuple._2))
    })
    //lacIdRDD.collect().foreach(println(_))
    // -----------------------------------------------------
    //加载基站基本信息
    val lacInfoRDD = sc.textFile("F:\\bigdataSource\\loc_info.txt").map(line => {
      //line===>CC0710CC94ECC657A8561DE549D940E0,116.303955,40.041935,6===>(lacId,(x,y))
      val fields = line.split(",")
      val lacId = fields(0)
      val y = fields(1)
      val x = fields(2)
      (lacId, (x, y))
    })
    //(lacId,((tel, time ),(x, y)))
    val result = lacIdRDD.join(lacInfoRDD).map(tuple => {
      //(lacId,((tel, time ),(x, y)))===>(tel,(lacId,time,x,y))
      (tuple._2._1._1, (tuple._1, tuple._2._1._2, tuple._2._2._1, tuple._2._2._2))
    }).groupByKey() //(tel,List((lacId,time,x,y),(lacId,time,x,y),(lacId,time,x,y)，(lacId,time,x,y)))
      .mapValues(list => {
      list.toList.sortBy(x => x._2).reverse.take(2)
    })
      //.saveAsTextFile("D:\\aaaaaaa")
        .foreach(x=>{
      //print(x._1+"****"+x._2+"\n\n")
      for(i <- x._2){
        print(x._1+"  "+i+"\n\n")
      }
    })

    sc.stop()

  }

}
