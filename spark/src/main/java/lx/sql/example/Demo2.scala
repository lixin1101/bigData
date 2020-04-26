package lx.sql.example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by 赌神♣♥♦♠ on 2018/3/8.
  */
object Demo2 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val sparkConf = new SparkConf().setAppName("Demo2")
    sparkConf.setMaster("local")
    val spark = SparkSession.builder().appName("Demo2").config(sparkConf).getOrCreate()
    //创建数据库连接，从数据库查询数据，并存储本地。
    runJDBCDataSource(spark)
    //从json文件中加载数据，进行搜索
    //loadDataSourceFromeJson(spark)
    //从parquet文件中加载数据，进行搜索
    //loadDataSourceFromeParquet(spark)
    //从RDD中加载数据
    //runFromRDD(spark)
    spark.stop()
  }

  private def runJDBCDataSource(spark: SparkSession): Unit = {
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/root?user=root&password=root")
      //必须写表名
      .option("dbtable", "ecs_users")
      .load()
    //查询数据库中的id, name, telephone三个列并以parquet（列存储）的方式存储在src/main/resources/sec_users路径下（存储后记得将名字改为user.parquet）
    //jdbcDF.select("id", "name", "telephone").write.format("parquet").save("src/main/resources/sec_users")
    //查询数据库中的username, name, telephone三个列并以parquet（列存储）的方式存储在src/main/resources/sec_users路径下存储后记得将名字改为user.json）
    //jdbcDF.select("username", "name", "telephone").write.format("json").save("src/main/resources/sec_users")
    //存储成为一张虚表user_abel
    jdbcDF.select("email", "user_name", "password").write.mode("overwrite").saveAsTable("user")
    val jdbcSQl = spark.sql("select * from user where user_name like 'a%' ")
    jdbcSQl.show()

    //jdbcSQl.write.format("json").save("./out/resulted")
  }

  private def loadDataSourceFromeJson(spark: SparkSession): Unit = {
    //从runJDBCDataSource产生的user.json中读取数据
    val jsonDF = spark.read.json("src/main/resources/user.json")
    //输出结构
    jsonDF.printSchema()
    //创建临时视图
    jsonDF.createOrReplaceTempView("user")
    //从临时视图进行查询
    val namesDF = spark.sql("SELECT name FROM user WHERE name like '王%'")
    import spark.implicits._
    //操作查询结果，在每个查询结果前加"Name: " 但使用该方法必须导入spark.implicits._
    namesDF.map(attributes => "Name: " + attributes(0)).show()
    //将结果以json的形式写入到./out/resultedJSON 路径下 jsonDF.select("name").write.format("json").save("./out/resultedJSON")
  }

  private def loadDataSourceFromeParquet(spark: SparkSession): Unit = {
    //从runJDBCDataSource产生的user.json中读取数据
    val parquetDF = spark.read.load("src/main/resources/user.parquet")
    //创建临时视图
    parquetDF.createOrReplaceTempView("user")
    val namesDF = spark.sql("SELECT name FROM user WHERE id > 1 ")
    namesDF.show()
    //将结果以parquet的形式写入到./out/resultedParquet 路径下
    parquetDF.select("name").write.format("parquet").save("./out/resultedParquet")
  }

  private def runFromRDD(spark: SparkSession): Unit = {
    //创建一个json形式的RDD
    val otherPeopleRDD = spark.sparkContext.makeRDD(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    //从RDD中读取数据
    val otherPeople = spark.read.json(otherPeopleRDD)
    otherPeople.show()
  }
}
