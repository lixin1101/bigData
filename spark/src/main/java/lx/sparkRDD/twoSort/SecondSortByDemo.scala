package lx.sparkRDD.twoSort

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 赌神♣♥♦♠ on 2017/5/27.
  */
class Boy(var name: String, var age: Int, var faceValue: Int) extends Comparable[Boy] with Serializable {
  override def compareTo(that: Boy): Int = {
    if (this.age - that.age != 0) return this.age - that.age
    else if (this.faceValue - that.faceValue != 0) return this.faceValue - that.faceValue
    else this.name.compareTo(that.name)
  }

  override def toString = s"Boy(name=$name, age=$age, faceValue=$faceValue)"
}

class Girl(var name: String, var age: Int, var faceValue: Int) extends Ordered[Girl] with Serializable {
  override def compare(that: Girl): Int = {
    if (this.age - that.age != 0) return this.age - that.age
    else if (this.faceValue - that.faceValue != 0) return this.faceValue - that.faceValue
    else this.name.compareTo(that.name)
  }

  override def toString = s"Girl(name=$name, age=$age, faceValue=$faceValue)"
}

class Student(var name: String, var age: Int, var faceValue: Int) extends Serializable {
  override def toString = s"Student(name=$name, age=$age, faceValue=$faceValue)"
}

object SecondSortByDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SecondSortByDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val arr = Array(
      new Student("s1", 18, 19),
      new Student("s2", 17, 19),
      new Student("s3", 17, 19),
      new Student("s4", 48, 14),
      new Student("s5", 18, 199),
      new Student("s6", 68, 290)

      /*new Girl("g1", 18, 19),
      new Girl("g2", 17, 19),
      new Girl("g3", 17, 19),
      new Girl("g4", 48, 14),
      new Girl("g5", 18, 199),
      new Girl("g6", 68, 290)*/
    )
    /*  implicit def student2Ordered(student:Student):Ordered[Student]=new Ordered[Student]{
         override def compare(that: Student): Int = {
           if (student.age - that.age != 0) return student.age - that.age
           else if (student.faceValue - that.faceValue != 0) return student.faceValue - that.faceValue
           else student.name.compareTo(that.name)
         }
       }*/

    implicit val ord = new Ordering[Student] {
      override def compare(student: Student, that: Student): Int = {
        if (student.age - that.age != 0) return student.age - that.age
        else if (student.faceValue - that.faceValue != 0) return student.faceValue - that.faceValue
        else student.name.compareTo(that.name)
      }
    }

    sc.parallelize(arr).map((_, 1)).sortByKey().foreach(x => println(x._1.toString))

    sc.stop()
  }
}
