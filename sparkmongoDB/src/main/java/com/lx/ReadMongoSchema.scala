package com.lx

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object ReadMongoSchema {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("ReadMongoSchema")
      .config("spark.mongodb.input.partitioner","MongoShardedPartitioner")
      .config("spark.mongodb.input.uri", "mongodb://192.168.80.103/lx.products")
      .getOrCreate()

    // 设置log级别
    spark.sparkContext.setLogLevel("WARN")

    val schema = StructType(
      List(
        StructField("pid", StringType),
        StructField("pname", StringType),
        StructField("price", StringType)
      )
    )

    // 通过schema约束，直接获取需要的字段
    val df = spark.read.format("com.mongodb.spark.sql").schema(schema).load()
    //df.show()

    df.createOrReplaceTempView("pro")

    val resDf = spark.sql("select * from pro")
    resDf.show()

    spark.stop()
    System.exit(0)
  }
}
