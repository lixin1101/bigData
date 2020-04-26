package com.lx

import com.mongodb.spark.MongoSpark
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * mongodb on spark的参数基本分为输入参数和输出参数。
  *
  * 1. mongodb on spark输入的参数：
  * spark.mongodb.input.uri：mongo的uri，
  * 如：mongodb://host:port/
  * 或者直接指定数据库和集合：mongodb://host:port/database.collection，相当于使用了uri，database，collection参数
  * spark.mongodb.input.database：mongo的数据库指定
  * spark.mongodb.input.collection：mongo的集合
  *
  * spark.mongodb.input.partitioner：用于对数据进行分区的分区程序的类名
  * 默认使用：MongoDefaultPartitioner，其他值有：
  * MongoSamplePartitioner：使用集合的平均文档大小和随机抽样来确定集合的合适分区。
  * MongoShardedPartitioner：根据数据块对集合进行分区。需要对config数据库的读访问权限。
  * MongoSplitVectorPartitioner：使用splitVector独立命令或主数据库上的命令来确定数据库的分区。需要特权才能运行splitVector命令
  * MongoPaginateByCountPartitioner：创建特定数量的分区。需要查询每个分区。
  * MongoPaginateBySizePartitioner：根据数据大小创建分区。需要查询每个分区。
  * 每个Partitioner类都有option参数，具体参阅文档
  *
  * 2. mongodb on spark输出的参数
  * spark.mongodb.output.uri：和输入参数一样
  * spark.mongodb.output.database：和输入参数一样
  * spark.mongodb.output.collection：和输入参数一样
  * spark.mongodb.output.replaceDocument：保存包含_id字段的数据集时替换整个文档。如果为false，它将仅更新文档中与数据集中的字段匹配的字段。默认true。
  * maxBatchSize：保存数据时批量操作的最大批量大小。默认512M。
  */
object ReadMongo {
  def main(args: Array[String]): Unit = {
    /**
      * spark连接mongodb需要（权限认证）
      *
      * final String mgohost = "127.0.0.1"
      * SparkSession spark = SparkSession.builder()
      * .appName("spot")
      * .config("spark.mongodb.output.uri", "mongodb://spark:spark@" + mgohost + ":27017/admin")
      * .config("spark.mongodb.output.database","demo")
      * .config("spark.mongodb.output.collection","test")
      * .getOrCreate()
      *
      *  mongodb://用户名:密码@” + mgohost + “:27017/admin
      *  admin为mongodb系统database，通过系统库连接database权限认证通过
      *  demo指定其它database
      *  test指定collection
      */
    Logger.getLogger("org").setLevel(Level.WARN)
    val spark = SparkSession.builder()
      .master("local")
      .appName("ReadMongo")

      /**
        * mongodb版本低于3.2时，读取数据时如果不指定ReadConfig中partitioner，
        * 会使用默认的DefaultMongoPartitioner，但是3.2的时候还没有DefaultMongoPartitioner这个类，所以会报错
        */
      .config("spark.mongodb.input.partitioner", "MongoShardedPartitioner") //不加这个就报错了 应为在3.2之前mongo没有DefaultMongoPartitioner
      .config("spark.mongodb.input.partitionerOptions.shardkey", "_id")
      .config("spark.mongodb.input.uri", "mongodb://192.168.80.103/lx.products")
      .getOrCreate()

    val df = MongoSpark.load(spark)
    //df.show()

    df.createOrReplaceTempView("pro")

    val resDf1 = spark.sql("select * from pro")
    resDf1.show()

    val resDf = spark.sql("select pname,count(1) as c from pro group by pname order by c desc")
    resDf.show()

    spark.stop()
    System.exit(0)
  }
}
