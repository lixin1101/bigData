package lx.sql.project.pro.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/6/3.
  */
class ScoreTV(var name: String, var p1: Double, var p2: Double, var p3: Double) extends Serializable {

  override def toString = s"ScoreTV(name=$name, p1=$p1, p2=$p2, p3=$p3)"
}

object SecondSortByTVplay {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().setAppName("SecondSortByTVplay").setMaster("local")
    val sc = new SparkContext(conf)

    implicit def score2Ordered(score: ScoreTV): Ordered[ScoreTV] = new Ordered[ScoreTV] {
      override def compare(that: ScoreTV): Int = {
        if (score.p1.compareTo(that.p1) != 0) return that.p1.compareTo(score.p1)
        else if (score.p2.compareTo(that.p2) != 0) return that.p2.compareTo(score.p2)
        else that.p3.compareTo(score.p3)
      }
    }

    sc.textFile("F:\\bigdataSource\\大数据数据\\TVplay3.txt").map(line => {
      val fields = line.split("\t")
      val name = fields(0)
      val p1 = fields(5).toDouble
      val p2 = fields(6).toDouble
      val p3 = fields(7).toDouble
      new ScoreTV(name, p1, p2, p3)
    }).map((_, 1)).sortByKey().foreach(x => println(x._1.toString))
    sc.stop()
  }
}
