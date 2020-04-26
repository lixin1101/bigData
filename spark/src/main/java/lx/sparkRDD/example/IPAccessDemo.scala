package lx.sparkRDD.example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.control.Breaks._

/**
  * Created by Administrator on 2017/5/28.
  *
  *
  *
  * 1， 需求：求每个地区的访问量（pv）
  * 2，最终结果：（北京，200）,(上海，10000)。。。。。
  * 3，数据来源
  * 3.1 ip规则映射表,ip.txt
  * 3.2,用户访问日志 access.log
  * 4，思路
  * 4.1 将ip规则每条记录转换成（startIP,endIP,area）
  * 例如：
  * 1.0.1.0|1.0.3.255|16777472|16778239|亚洲|中国|福建|福州||电信|350100|China|CN|119.306239|26.075302
  * （16777472，16778239，福州）
  * Array（
  * （startIP,endIP,area），
  * （startIP,endIP,area），
  * （startIP,endIP,area），
  * （startIP,endIP,area）,
  * ................
  * ）
  * 4.2 将解析后的ip规则广播出去
  * 4.3 解析用户访问日志，取出ip，然后封装成元组（IP，1）
  * 4.4 使用reduceByKey算子，进行计算统计每个ip访问次数
  * Array（
  * （IP,6）,
  * (IP,100),
  * .....
  */
object IPAccessDemo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("IPAccessDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val ipRules = sc.textFile("H:\\原电脑d盘数据\\数据\\计算IP地址归属地\\ip.txt").map(line => {
      val fields = line.split("\\|")
      val startIp = fields(2).toLong
      val stopIp = fields(3).toLong
      val area = fields(6)
      (startIp, stopIp, area)
    }).collect()
    val ipBroad: Broadcast[Array[(Long, Long, String)]] = sc.broadcast(ipRules)


    sc.textFile("H:\\原电脑d盘数据\\数据\\计算IP地址归属地\\计算IP地址归属地\\access.log").map(line => {
      val ipAccess = line.split(" ")(0)
      (ipAccess, 1)
    }).reduceByKey(_ + _).map(tuple2 => {
      val ip = tuple2._1
      val count = tuple2._2
      val numIp = ip2Long(ip)
      val index = binarySearch(ipBroad.value, numIp)
      if (index != -1) {
        val area = ipBroad.value(index)._3
        (area, count)
      } else {
        ("unknown", count)
      }
    }).reduceByKey(_ + _).sortBy(x=>x._2,false).foreach(println(_))

    sc.stop()
  }

  //IP地址转十进制
  def ip2Long(ip: String): Long = {
    val ips = ip.split("[.]")
    var numIP = 0L
    for (i <- 0 until (ips.length))
      numIP = ips(i).toLong | numIP << 8L
    numIP
  }

  //使用二分查找法查找ip
  /**
    *
    * @param arr   :查找的范围
    * @param numIP ：目标ip
    */
  def binarySearch(arr: Array[(Long, Long, String)], numIP: Long) = {
    var min = 0
    var max = arr.length - 1
    var index = -1
    breakable {
      while (min <= max) {
        var middle = (min + max) / 2
        if (numIP >= arr(middle)._1 && numIP <= arr(middle)._2) {
          index = middle
          break()
        } else if (numIP < arr(middle)._1) max = middle - 1
        else if (numIP > arr(middle)._2) min = middle + 1
      }
    }
    index
  }
}
