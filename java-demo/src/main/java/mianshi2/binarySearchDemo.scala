package mianshi2

import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._

object binarySearchDemo {

  /**
    * 二分查找 时间复杂度O(log2n);空间复杂度O(1)
    */
  def binarySearch(arr: Array[Int], left: Int, right: Int, findVal: Int): Int = {
    if (left > right) { //递归退出条件，找不到，返回-1
      -1
    }

    val midIndex = (left + right) / 2

    if (findVal < arr(midIndex)) { //向左递归查找
      binarySearch(arr, left, midIndex, findVal)
    } else if (findVal > arr(midIndex)) { //向右递归查找
      binarySearch(arr, midIndex, right, findVal)
    } else { //查找到，返回下标
      midIndex
    }
  }

  /*
  {1,8, 10, 89, 1000, 1000，1234} 当一个有序数组中，有多个相同的数值时，如何将所有的数值都查找到，比如这里的 1000.
  //分析
  1. 返回的结果是一个可变数组 ArrayBuffer
  2. 在找到结果时，向左边扫描，向右边扫描 [条件]
  3. 找到结果后，就加入到ArrayBuffer
   */
  def binarySearch2(arr: Array[Int], l: Int, r: Int,
                    findVal: Int): ArrayBuffer[Int] = {

    //找不到条件?
    if (l > r) {
      return ArrayBuffer()
    }

    val midIndex = (l + r) / 2
    val midVal = arr(midIndex)
    if (midVal > findVal) {
      //向左进行递归查找
      binarySearch2(arr, l, midIndex - 1, findVal)
    } else if (midVal < findVal) { //向右进行递归查找
      binarySearch2(arr, midIndex + 1, r, findVal)
    } else {
      println("midIndex=" + midIndex)
      //定义一个可变数组
      val resArr = ArrayBuffer[Int]()
      //向左边扫描
      var temp = midIndex - 1
      breakable {
        while (true) {
          if (temp < 0 || arr(temp) != findVal) {
            break()
          }
          if (arr(temp) == findVal) {
            resArr.append(temp)
          }
          temp -= 1
        }
      }
      //将中间这个索引加入
      resArr.append(midIndex)
      //向右边扫描
      temp = midIndex + 1
      breakable {
        while (true) {
          if (temp > arr.length - 1 || arr(temp) != findVal) {
            break()
          }
          if (arr(temp) == findVal) {
            resArr.append(temp)
          }
          temp += 1
        }
      }
      return resArr
    }
  }

  /**
    * 快排
    * 时间复杂度:O(nlogn)
    * 空间复杂度:O(n)
    */
  def merge(left: List[Int], right: List[Int]): List[Int] = (left, right) match {
    case (Nil, _) => right
    case (_, Nil) => left
    case (x :: xTail, y :: yTail) =>
      if (x <= y) x :: merge(xTail, right)
      else y :: merge(left, yTail)
  }



  def main(args: Array[String]): Unit = {

  }
}
