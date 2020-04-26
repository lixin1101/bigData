package lx.sql.sparkDataSet

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.util.control.Breaks.{break, breakable}

/**
  * Created by 赌神♣♥♦♠ on 2018/6/20.
  */
object SOLIpLocaltion2 {
  //IP地址转十进制
  def ip2Long(ip: String): Long = {
    val ips = ip.split("[.]")
    var numIP = 0L
    for (i <- 0 until (ips.length))
      numIP = ips(i).toLong | numIP << 8L
    numIP
  }

  //使用二分查找法查找ip
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

  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .appName("JdbcDataSource")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._

    val ruleLine: Dataset[String] = session.read.textFile("ip.txt")
    val ruleDs: Dataset[(Long, Long, String)] = ruleLine.map(line => {
      val fields = line.split("[|]")
      val startNum = fields(2).toLong
      val endNum = fields(3).toLong
      val province = fields(6)
      (startNum, endNum, province)
    })
    val rules: Array[(Long, Long, String)] = ruleDs.collect()
    val broadcast: Broadcast[Array[(Long, Long, String)]] = session.sparkContext.broadcast(rules)

    //读取已经处理好的parquet文件
    val accesslog: DataFrame = session.read.parquet("D:\\数据\\access_parquet")
    accesslog.createTempView("v_access")
    session.udf.register("ip2Province", (ip: String) => {
      val ru: Array[(Long, Long, String)] = broadcast.value
      val ipNum = ip2Long(ip)
      val index = binarySearch(ru,ipNum)
      ru(index)._3
    })
    session.sql("select ip2Province(ip),count(1) from v_access group by ip2Province(ip)")
      .show()


  }

}
