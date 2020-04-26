package scala.linkedlist

import util.control.Breaks._

object JosephuDemo {
  def main(args: Array[String]): Unit = {
    //测试一把
    val josephu = new Josephu()
    josephu.addBoy(5)
    josephu.list()

    //测试一下游戏
    //josephu.countBoy(2,2,5) // 3 - 5 - 2 - 1 - 4
    josephu.countBoy2(2, 2, 5) //3 - 5 - 2 - 1 - 4
  }
}

class Josephu {
  //定义一个first,初始为null
  var first: Boy = null

  //分析：因为在出圈过程中,first 实际上是没有什么用处，因此可以去掉
  def countBoy2(startNO: Int, countNum: Int, boyNums: Int): Unit = {
    //做下数据的验证
    if (first == null || startNO > boyNums || startNO <= 0) {
      println("输出参数有误，不能玩游戏")
      return
    }
    //1创建辅助指针
    var helper = first
    //2让 helper 移动到 first 的上一个
    breakable {
      while (true) {
        if (helper.next == first) {
          break()
        }
        helper = helper.next
      }
    }

    //3. 让helper 移动 k - 1
    for (i <- 0 until startNO - 1) {
      helper = helper.next
    }

    breakable {
      while (true) {
        //4. 让first 和 helper 再移动  m - 1 个位置
        for (i <- 0 until countNum - 1) { //for循环结束后，helper.next就指向了要删除小孩
          helper = helper.next
        }
        //5. helper.next就指向了要删除的小孩
        printf("小孩no=%d出圈\n", helper.next.no)

        helper.next = helper.next.next
        //判断是否已经只有一个小孩了
        if (helper == helper.next) {
          break()
        }
      }
    }

    printf("最后留在圈中的小孩是no=%d\n", helper.no)

  }

  //小孩出圈
  def countBoy(startNO: Int, countNum: Int, boyNums: Int): Unit = {
    //做下数据的验证
    if (first == null || startNO > boyNums || startNO <= 0) {
      println("输出参数有误，不能玩游戏")
      return
    }
    //1创建辅助指针
    var helper = first
    //2让 helper 移动到 first 的上一个
    breakable {
      while (true) {
        if (helper.next == first) {
          break()
        }
        helper = helper.next
      }
    }

    //3.让 first 移动  k - 1 个位置, 同时让helper 也做相应的移动
    for (i <- 0 until startNO - 1) {
      first = first.next
      helper = helper.next
    }

    breakable {
      while (true) {
        //4. 让first 和 helper 再移动  m - 1 个位置
        for (i <- 0 until countNum - 1) { //for循环结束后，first就指向了要删除小孩
          first = first.next
          helper = helper.next
        }
        //5. first 就指向了要删除的小孩
        printf("小孩no=%d出圈\n", first.no)
        first = first.next
        helper.next = first
        //判断是否已经只有一个小孩了
        if (first == helper) {
          break()
        }
      }
    }

    printf("最后留在圈中的小孩是no=%d\n", first.no)

  }

  //添加小孩，形成环形链表
  def addBoy(boyNums: Int): Unit = {
    var curBoy: Boy = null
    for (i <- 1 to boyNums) {
      //创建小孩对象
      val boy = new Boy(i)
      //处理第一个小孩, 就形成环形
      if (i == 1) {
        first = boy
        curBoy = boy
        first.next = first
      } else {
        curBoy.next = boy
        boy.next = first
        curBoy = boy
      }
    }
  }

  //遍历环形链表
  def list(): Unit = {

    //判断链表是否为空
    if (first == null) {
      println("没有小孩")
      return
    }

    var curBoy = first
    breakable {
      while (true) {
        printf("no=%d \n", curBoy.no)
        if (curBoy.next == first) {
          //表示curBoy已经是最后这个
          break()
        }
        //curBoy后移
        curBoy = curBoy.next
      }
    }
  }
}

//小孩类
class Boy(bNo: Int) {
  val no = bNo
  var next: Boy = null
}
