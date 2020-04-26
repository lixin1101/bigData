package lx.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Stock_result {
  case class tbStock(ordernumber:String,locationid:String,dateid:String) extends Serializable
  case class tbStockDetail(ordernumber:String, rownum:Int,
                           itemid:String, number:Int, price:Double, amount:Double) extends Serializable
  case class tbDate(dateid:String, years:Int, theyear:Int, month:Int, day:Int,
                    weekday:Int, week:Int, quarter:Int, period:Int, halfmonth:Int) extends Serializable

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder().master("local").appName("Stock_result").getOrCreate()

    import spark.implicits._

    val tbStockRdd = spark.sparkContext.textFile("F:\\bigdataSource\\shangguigu\\tbStock.txt")
    val tbStockDS = tbStockRdd.map(_.split(","))
      .map(attr=>tbStock(attr(0),attr(1),attr(2)))
      .toDS

    //tbStockDS.show()

    val tbStockDetailRdd = spark.sparkContext.textFile("F:\\bigdataSource\\shangguigu\\tbStockDetail.txt")
    val tbStockDetailDS = tbStockDetailRdd.map(_.split(","))
      .map(attr=> tbStockDetail(attr(0),attr(1).trim().toInt,
        attr(2),attr(3).trim().toInt,
        attr(4).trim().toDouble,
        attr(5).trim().toDouble))
      .toDS

    //tbStockDetailDS.show()

    val tbDateRdd = spark.sparkContext.textFile("F:\\bigdataSource\\shangguigu\\tbDate.txt")
    val tbDateDS = tbDateRdd
      .map(_.split(",")).map(attr=> tbDate(
      attr(0),
      attr(1).trim().toInt,
      attr(2).trim().toInt,
      attr(3).trim().toInt,
      attr(4).trim().toInt,
      attr(5).trim().toInt,
      attr(6).trim().toInt,
      attr(7).trim().toInt,
      attr(8).trim().toInt,
      attr(9).trim().toInt))
      .toDS
    //tbDateDS.show()

    tbStockDS.createOrReplaceTempView("tbStock")
    tbDateDS.createOrReplaceTempView("tbDate")
    tbStockDetailDS.createOrReplaceTempView("tbStockDetail")

    spark.sql(
      """
        |
        |SELECT c.theyear, COUNT(DISTINCT a.ordernumber), SUM(b.amount)
        |FROM tbStock a
        |JOIN tbStockDetail b ON a.ordernumber = b.ordernumber
        |JOIN tbDate c ON a.dateid = c.dateid
        |GROUP BY c.theyear ORDER BY c.theyear
        |
      """.stripMargin).show

    spark.sql(
      """
        |
        |SELECT theyear, MAX(c.SumOfAmount) AS SumOfAmount FROM
        |(
        |SELECT a.dateid, a.ordernumber, SUM(b.amount) AS SumOfAmount
        |FROM tbStock a
        |JOIN tbStockDetail b ON a.ordernumber = b.ordernumber
        |GROUP BY a.dateid, a.ordernumber
        |) c
        |JOIN tbDate d ON c.dateid = d.dateid
        |GROUP BY theyear ORDER BY theyear DESC
        |
      """.stripMargin).show()

    spark.sql(
      """
        |
        |SELECT DISTINCT e.theyear, e.itemid, f.maxofamount FROM
        |(
        |SELECT c.theyear, b.itemid, SUM(b.amount) AS sumofamount
        |FROM tbStock a
        |JOIN tbStockDetail b ON a.ordernumber = b.ordernumber
        |JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear, b.itemid
        | ) e
        |JOIN
        |(
        |SELECT d.theyear, MAX(d.sumofamount) AS maxofamount FROM
        |(SELECT c.theyear, b.itemid, SUM(b.amount) AS sumofamount
        |FROM tbStock a
        |JOIN tbStockDetail b ON a.ordernumber = b.ordernumber
        |JOIN tbDate c ON a.dateid = c.dateid
        |GROUP BY c.theyear, b.itemid
        |) d GROUP BY d.theyear ) f
        |ON e.theyear = f.theyear AND e.sumofamount = f.maxofamount
        |ORDER BY e.theyear
        |
      """.stripMargin).show()
    spark.stop()
  }
}
