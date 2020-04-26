package com.lx.breeze

import breeze.linalg._
import breeze.numerics._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object breeze_test01 {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.WARN)
    val session = SparkSession.builder()
      .appName("breeze_test01")
      .master("local[*]")
      .getOrCreate()
    val sc = session.sparkContext

    // 3.1.1 Breeze 创建函数

    println("*********创建函数********")
    val m1 = DenseMatrix.zeros[Double](2, 3) //指定2行3列的0密度矩阵，double类型
    println(m1)
    println("*****************")
    val v1 = DenseVector.zeros[Double](3) //指定3个double类型的0值向量
    println(v1)
    println("*****************")
    val v2 = DenseVector.ones[Double](3) // 指定3个double类型的1值向量
    println(v2)
    println("*****************")
    val v3 = DenseVector.fill(3) {
      5.0
    } //指定3个5值的向量
    println(v3)
    println("*****************")
    val v4 = DenseVector.range(1, 10, 2) //指定1到10的范围的2阶向量
    println(v4)
    println("*****************")
    val m2 = DenseMatrix.eye[Double](3) //指定3行3列的单位矩阵
    println(m2)
    println("*****************")
    val v6 = diag(DenseVector(1.0, 2.0, 3.0)) //指定 1,2,3 的对角矩阵
    println(v6)
    println("*****************")

    val m3 = DenseMatrix((1.0, 2.0), (3.0, 4.0)) //指定2行2列矩阵
    println(m3)
    println("*****************")
    val v8 = DenseVector(1, 2, 3, 4)
    println(v8)
    println("*****************")
    val v9 = DenseVector(1, 2, 3, 4).t //转置
    println(v9)
    println("*****************")
    val v10 = DenseVector.tabulate(3) { i => 2 * i } //指定索引对应函数的向量（函数创建）
    println(v10)
    println("*****************")
    val m4 = DenseMatrix.tabulate(3, 2) { case (i, j) => i + j } //指定3行2列的函数矩阵
    println(m4)
    println("*****************")
    val v11 = new DenseVector(Array(1, 2, 3, 4)) //通过数组创建向量
    println(v11)
    println("*****************")
    val m5 = new DenseMatrix(2, 3, Array(11, 12, 13, 21, 22, 23)) //指定2行3列的数组矩阵
    println(m5)
    println("*****************")
    val v12 = DenseVector.rand(4) //指定4个0到1的随机向量
    println(v12)
    println("*****************")
    val m6 = DenseMatrix.rand(2, 3) //指定2行3列的随机矩阵
    println(m6)
    println("*****************")

    // 3.1.2 Breeze 元素访问及操作函数
    // 元素访问
    val a = DenseVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println("&&&&&&&&&&元素访问&&&&&&&&&&&")
    println(a(0))
    println(a(1 to 4)) //包含1和4
    println(a(5 to 0 by -1)) //索引从5到0的向量
    println(a(1 to -1)) //索引从1开始的所有值的向量
    println(a(-1)) //最大的向量值
    println("&&&&&&&&&&&&&&&&&&&&&")
    val m = DenseMatrix((1.0, 2.0, 3.0), (3.0, 4.0, 5.0))
    println(m)
    println(m(0, 1)) //其实是找第一行 第二列的元素（）里面是索引
    println(m(::, 1)) //访问第二列列的元素，返回一个向量 其中1是索引
    println("&&&&&&&&&&&&&&&&&&&&&")

    // 元素操作
    val m_1 = DenseMatrix((1.0, 2.0, 3.0), (3.0, 4.0, 5.0))
    println("&&&&&&&&&&元素操作&&&&&&&&&&&")
    println(m_1.reshape(3, 2)) //调整矩阵形状为3行2列，以列的形式走
    println(m_1.toDenseVector) //矩阵转成向量 按列排序成向量
    println("&&&&&&&&&&&&&&&&&&&&&")
    val m_3 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(lowerTriangular(m_3)) //复制下三角  下三角形矩阵
    println("&&&&&&&&&&&&&&&&&&&&&")
    println(upperTriangular(m_3)) //复制上三角
    println("&&&&&&&&&&&&&&&&&&&&&")
    println(m_3.copy) //复制全部矩阵
    println(diag(m_3)) //取对角线元素成向量
    m_3(::, 2) := 5.0 //把2索引的列的元素改成5（矩阵赋值为5）
    println(m_3)
    m_3(1 to 2, 1 to 2) := 5.0 //子集赋数值
    println(m_3)
    println("&&&&&&&&&&&&&&&&&&&&&")
    /*val a_1 = DenseVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    a_1(1 to 4) := 5
    a_1(1 to 4) := DenseVector(1, 2, 3, 4)
    println(a_1)*/
    val a1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val a2 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println(DenseMatrix.vertcat(a1, a2)) //垂直连接矩阵
    println(DenseMatrix.horzcat(a1, a2)) //横向连接矩阵
    println("&&&&&&&&&&&&&&&&&&&&&")
    val b1 = DenseVector(1, 2, 3, 4)
    val b2 = DenseVector(1, 1, 1, 1)
    println(DenseVector.vertcat(b1, b2)) //向量连接
    println("&&&&&&&&&&&&&&&&&&&&&")

    println("^^^^^^^^^^ 数值计算函数^^^^^^^^^^^")
    // 3.1.3 Breeze 数值计算函数
    val a_3 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val b_3 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println(a_3 + b_3) //矩阵相加（对应行列）
    println(a_3 :* b_3) //矩阵相乘（对应行列）
    println(a_3 :/ b_3) //矩阵相除（对应行列）
    println(a_3 :< b_3) //矩阵元素比较（如果相等true，不等false）
    println(a_3 :== b_3) //矩阵元素相等（如果等，就true）
    a_3 :+= 1.0 //矩阵元素加1赋值给矩阵自己
    println(a_3)
    a_3 :*= 2.0 // 矩阵元素乘2赋值给矩阵自己
    println(a_3)
    println(max(a_3)) //矩阵中最大的元素
    println(argmax(a_3)) //矩阵元素中最大值的位置

    /**
      * 两个向量a = [a1, a2,…, an]和b = [b1, b2,…, bn]的点积定义为：
      * a·b=a1b1+a2b2+……+anbn。
      */
    println(DenseVector(1, 2, 3, 4) dot DenseVector(1, 1, 2, 1)) //向量点积
    println("^^^^^^^^^^^^^^^^^^^^^^^^^")

    // 3.1.4 Breeze 求和函数
    println("$$$$$$$$$求和函数$$$$$$$$$$$")
    val a_4 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(a_4)
    println(sum(a_4)) //元素求和
    println(sum(a_4, Axis._0)) //每一列求和 返回一个矩阵
    println(sum(a_4, Axis._1)) //每一行求和 返回一个向量
    println(trace(a_4)) //对角线元素和
    println(accumulate(DenseVector(1, 2, 3, 4))) //累积和
    println("$$$$$$$$$$$$$$$$$$$$$$$$$$")

    // 3.1.5 Breeze 布尔函数
    println("~~~~~~~~~~布尔函数~~~~~~~~~~")
    val a_5 = DenseVector(true, false, true)
    val b_5 = DenseVector(false, true, true)
    println(a_5 :& b_5) //并且
    println(a_5 :| b_5) //或者
    println(!a_5) //非
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    //val a_5_2 = DenseVector(1.0, 0.0, -2.0)
    //println(any(a_5_2)) //任意元素非0
    //println(all(a_5_2)) //所有元素非0

    // 3.1.6 Breeze 线性代数函数
    println("&&&&&&&&&&&线性代数函数&&&&&&&&&&&&&&")
    val a_6 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    val b_6 = DenseMatrix((1.0, 1.0, 1.0), (1.0, 1.0, 1.0), (1.0, 1.0, 1.0))
    println(a_6)
    println("&&&&&&&&&&&&&&&&&&&&&&")
    println(b_6)
    println("&&&&&&&&&&&&&&&&&&&&&&")
    println(a_6 \ b_6) //?????怎么算的?????
    println("&&&&&&&&&&&&&&&&&&&&&&")
    println(a_6.t) //转置
    println("&&&&&&&&&&&&&&&&&&&&&&")
    println(det(a_6)) //求特征值
    println("&&&&&&&&&&&&&&&&&&&&&&")
    println(inv(a_6)) //求逆矩阵
    println("&&&&&&&&&&&&&&&&&&&&&&")
    /**
      * 奇异值分解（Singular Value Decomposition）是线性代数中一种重要的矩阵分解，
      * 是矩阵分析中正规矩酉对角化的推广。在信号处理、统计学等领域有重要应用
      */
    val svd.SVD(u, s, v) = svd(a_6)
    println(a_6.rows)
    println(a_6.cols)
    println("&&&&&&&&&&&&&&&&&&&&&&")


    // 3.1.7 Breeze 取整函数
    println("(取整函数)")
    val a_7 = DenseVector(1.2, 0.6, -2.3)
    println(round(a_7)) //四舍五入
    println(ceil(a_7)) //向上取整
    println(floor(a_7)) //向下取整
    /**
      * 当x>0，sign(x)=1;
      * 当x=0，sign(x)=0;
      * 当x<0，sign(x)=-1；
      */
    println(signum(a_7)) //符号函数
    println(abs(a_7)) //绝对值
    println("-------结束---------")
  }
}