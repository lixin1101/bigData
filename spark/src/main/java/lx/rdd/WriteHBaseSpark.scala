package lx.rdd


import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf

object WriteHBaseSpark {
  def main(args: Array[String]) {
    //获取Spark配置信息并创建与spark的连接
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HBaseApp")
    val sc = new SparkContext(sparkConf)

    //创建HBaseConf
    val conf = HBaseConfiguration.create()
    val jobConf = new JobConf(conf)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "fruit_spark")

    //构建Hbase表描述器
    val fruitTable = TableName.valueOf("fruit_spark")
    val tableDescr = new HTableDescriptor(fruitTable)
    tableDescr.addFamily(new HColumnDescriptor("info".getBytes))

    //创建Hbase表
    val admin = new HBaseAdmin(conf)
    if (admin.tableExists(fruitTable)) {
      admin.disableTable(fruitTable)
      admin.deleteTable(fruitTable)
    }
    admin.createTable(tableDescr)

    //定义往Hbase插入数据的方法
    def convert(triple: (Int, String, Int)) = {
      val put = new Put(Bytes.toBytes(triple._1))
      put.addImmutable(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(triple._2))
      put.addImmutable(Bytes.toBytes("info"), Bytes.toBytes("price"), Bytes.toBytes(triple._3))
      (new ImmutableBytesWritable, put)
    }

    //创建一个RDD
    val initialRDD = sc.parallelize(List((1,"apple",11), (2,"banana",12), (3,"pear",13)))

    //将RDD内容写到HBase
    val localData = initialRDD.map(convert)

    localData.saveAsHadoopDataset(jobConf)
  }
}
