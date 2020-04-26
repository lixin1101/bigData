package lx.rdd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.hbase.util.Bytes

object ReadHBaseSpark {
  def main(args: Array[String]): Unit = {

    //创建spark配置信息
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("ReadHBaseSpark")

    //创建SparkContext
    val sc = new SparkContext(sparkConf)

    //构建HBase配置信息
    val conf: Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "s101,s102,s103")
    conf.set(TableInputFormat.INPUT_TABLE, "student")

    //从HBase读取数据形成RDD
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      conf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])

    val count: Long = hbaseRDD.count()
    println(count)

    //对hbaseRDD进行处理
    hbaseRDD.foreach {

      case (_, result) =>
        val key: String = Bytes.toString(result.getRow)
        val name: String = Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")))
        val age: String = Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")))
        println("RowKey:" + key + ",Name:" + name + ",Age:" + age)

      /*case (_, result) => {
        val cells: Array[Cell] = result.rawCells()
        for (elem <- cells) {
          println(Bytes.toString(result.getRow))
          println(Bytes.toString(CellUtil.cloneValue(elem)))
        }
      }*/

    }

    //关闭连接
    sc.stop()
  }
}
