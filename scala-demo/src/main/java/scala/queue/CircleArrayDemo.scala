package scala.queue

import scala.io.StdIn

object CircleArrayDemo {
  def main(args: Array[String]): Unit = {
    //测试一把
    val queue = new ArrayQueue2(4)
    //菜单演示
    var key = ""

    while (true) {
      println()
      println("请选择菜单")
      println("show: 显示队列")
      println("add : 添加数据")
      println("get : 获取数据")
      println("peek : 获取数据")
      println("exit: 退出程序")
      key = StdIn.readLine()
      key match {
        case "show" => queue.show()
        case "add" =>
          println("请输入一个数")
          val num = StdIn.readInt()
          queue.addQueue(num)
        case "get" =>
          //对取回的值，进行判断
          val res = queue.getQueue()
          //如果是异常
          if (res.isInstanceOf[Exception]) {
            println(res.asInstanceOf[Exception].getMessage)
          } else {
            //Int
            printf("队列取出的值=%d", res)
          }
        case "peek" =>
          //查看头元素值，进行判断
          val res = queue.peek()
          //如果是异常
          if (res.isInstanceOf[Exception]) {
            println(res.asInstanceOf[Exception].getMessage)
          } else {
            //Int
            printf("队列当前头元素=%d", res)
          }

      }
    }
  }
}

class ArrayQueue2(arrMaxSize: Int) {
  val maxSize = arrMaxSize // 指定队列的大小
  val arr = new Array[Int](maxSize) // 队列中数据，存放在数组,即数组模拟队列
  //front 初始化为0, 表示队列的头，指向队列的第一个元素
  var front = 0
  //rear 初始化0, 指向队列最后这个元素的后一个位置
  var rear = 0

  //判断队列空
  def isEmpty(): Boolean = {
    rear == front
  }

  //判断满
  def isFull(): Boolean = {
    (rear + 1) % maxSize == front
  }

  //添加数据到队列
  def addQueue(num: Int): Unit = {
    if (isFull()) {
      println("队列满，不能加入")
      return
    }
    arr(rear) = num
    //将rear 后移
    rear = (rear + 1) % maxSize
  }

  //从队列中取出数据, 可能取得数据，可能取不到数据(异常)
  def getQueue(): Any = {
    if (isEmpty()) {
      return new Exception("队列空,没有数据")
    }
    //因为front指向队列的第一个元素
    val res = arr(front) //先将保存到临时变量
    front = (front + 1) % maxSize // front后移
    return res //返回临时变量
  }

  //遍历显示队列, 动脑筋
  //思路
  // 1. 从front 开始打印，打印多少个元素
  // 2. 所以，需要统计出该队列有多少个有效元素
  def show(): Unit = {
    if (isEmpty()) {
      println("队列空")
      return
    }
    //这里使用%方式解决
    for (i <- front until front + size()) {
      printf("arr(%d)=%d \t", (i % maxSize), arr(i % maxSize))
    }
  }

  //编写一个方法，统计当前有多少个元素
  def size(): Int = {

    return (rear + maxSize - front) % maxSize
  }

  //查看队列的头元素，但是不取出
  def peek(): Any = {
    if (isEmpty()) {
      return new Exception("队列空，无数据")
    }
    return arr(front) //front 不要动
  }
}
