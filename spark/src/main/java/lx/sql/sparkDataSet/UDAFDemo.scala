package lx.sql.sparkDataSet

import java.lang.Long

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * Created by 赌神♣♥♦♠ on 2018/6/21.
  * 未完成
  *
  */
class GeoMean extends UserDefinedAggregateFunction {
  override def inputSchema: StructType = StructType(List(StructField("value", DoubleType)))

  override def bufferSchema: StructType = StructType(List(
    StructField("count", LongType),
    StructField("product", DoubleType)
  ))

  override def dataType: DataType = DoubleType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 1.0
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getAs[Long](0) + 1
    buffer(1) = buffer.getAs[Double](1) * input.getAs[Double](0)
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getAs[Long](0) + buffer2.getAs[Long](0)
    buffer1(1) = buffer1.getAs[Double](1) * buffer2.getAs[Double](1)
  }

  override def evaluate(buffer: Row): Any = {
    Math.pow(buffer.getDouble(1),1.toDouble/buffer.getLong(0))
  }
}

object UDAFDemo {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder()
      .appName("JdbcDataSource")
      .master("local[*]")
      .getOrCreate()
    import session.implicits._
    /*val r = Math.pow(1 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9, 1.toDouble / 9)
    println(r)*/
    import org.apache.spark.sql.functions._
    val df: Dataset[Long] = session.range(1,10)
    df.createTempView("v_num")
    val gm=new GeoMean
    session.udf.register("gm",gm)
    session.sql("select gm(id) as gm from v_num").show()
  }

}
