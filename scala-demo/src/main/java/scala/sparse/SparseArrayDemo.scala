package scala.sparse

import scala.collection.mutable.ArrayBuffer

/**
  * 稀疏数组
  */
object SparseArrayDemo {

  def main(args: Array[String]): Unit = {

    //先使用二维数组，映射成棋盘
    val rows = 11
    val cols = 11
    val chessMap1 = Array.ofDim[Int](rows, cols)
    //初始化
    chessMap1(1)(2) = 1 //表示黑子
    chessMap1(2)(3) = 2 //表示蓝子

    chessMap1(5)(6) = 1 //表示黑子
    chessMap1(7)(9) = 2 //表示蓝子

    println("原始的棋盘")
    for (row <- chessMap1) {
      for (item <- row) {
        printf("%d", item)
      }
      //换行
      println()
    }

    //对原始的二维数组进行压缩
    //思路
    //1.创建一个ArrayBuffer，可以动态的添加数据
    //2.使用node对象，表示一行数据
    val SparseArray = ArrayBuffer[Node]()
    //先将第一行数据放入
    SparseArray.append(new Node(rows, cols, 0))
    //遍历chessMap1，如果发现有非0的值，就创建一个node对象，并加入到SparseArray
    for (i <- 0 until chessMap1.length) {
      for (j <- 0 until chessMap1(i).length) {
        if (chessMap1(i)(j) != 0) {
          //说明是有效数据，需要保存
          SparseArray.append(new Node(i, j, chessMap1(i)(j)))
        }
      }
    }

    println("稀疏数组情况是")
    for (i <- 0 until SparseArray.length) {
      val node = SparseArray(i)
      printf("%d %d %d\n", node.row, node.col, node.value)
    }

    /**
      * 将稀疏数组恢复成原始棋盘
      * 1：读取稀疏数组的第一行，创建一个二维数组chessMap2
      * 2：遍历（从稀疏数组的第二行），每读取到一个node，就将对应的值，恢复到chessMap2
      */
    val node = SparseArray(0)
    val chessMap2 = Array.ofDim[Int](node.row, node.col)

    for (i <- 1 until SparseArray.length) {
      val node1 = SparseArray(i)
      chessMap2(node1.row)(node1.col) = node1.value
    }
    println("恢复后的棋盘是")
    for (row <- chessMap2) {
      for (item <- row) {
        printf("%d", item)
      }
      //换行
      println()
    }
  }
}

class Node(val row: Int, val col: Int, val value: Int)
