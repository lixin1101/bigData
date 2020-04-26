package scala.linkedlist

import scala.util.control.Breaks.{break, breakable}

object DoubleLinkedListDemo {
  def main(args: Array[String]): Unit = {
    //测试一下双向遍历的添加和遍历
    //测试一把链表的创建和遍历
    val doubleLinkedList = new DoubleLinkedList()
    //创建三个英雄
    val node1 = new HeroNode2(1, "宋江", "及时雨")
    val node2 = new HeroNode2(2, "卢俊义", "玉麒麟")
    val node3 = new HeroNode2(3, "吴用", "智多星")
    val node4 = new HeroNode2(4, "张飞", "翼德")
    doubleLinkedList.add(node1)
    doubleLinkedList.add(node2)
    doubleLinkedList.add(node3)
    doubleLinkedList.add(node4)

    println("双向链表的情况")
    doubleLinkedList.list()

    val node5 = new HeroNode2(4, "公孙胜", "入云龙")
    doubleLinkedList.update(node5)
    println("双向链表的修改后情况")
    doubleLinkedList.list()

    //删除的测试
    doubleLinkedList.del(1)
    doubleLinkedList.del(2)
    doubleLinkedList.del(3)
    doubleLinkedList.del(4)
    println()
    println()
    println("双向链表的删除后情况")
    doubleLinkedList.list()

  }
}

//添加,遍历,修改,删除
class DoubleLinkedList {
  //创建头结点，指向该链表的头部
  val head = new HeroNode2(-1, "", "")


  //删除结点,因为双向链表可以实现自我删除，因此我们让temp指向要删除的结点即可
  def del(no: Int): Unit = {
    if (isEmpty()) {
      println("链表空")
      return
    }

    var temp = head.next
    var flag = false
    breakable {
      while (true) {
        if (temp.no == no) {
          flag = true
          break()
        }
        if (temp.next == null) {
          break()
        }
        temp = temp.next //遍历
      }
    }
    //当退出while循环后，如果flag=true,则temp 就指向要删除的结点
    if (flag) {
      //删除
      temp.pre.next = temp.next
      if (temp.next != null) {
        temp.next.pre = temp.pre
      }

    } else {
      printf("要删除的no=%d,不存在", no)
    }
  }

  //添加英雄到单链表
  //默认直接将人加入到链表的最后
  def add(heroNode: HeroNode2): Unit = {
    //思路
    //1. 先找到链表的最后结点
    //2. 然后让最后结点.next = 新的结点

    //因为head 不动，因此我们使用一个辅助指针来完成定位
    var temp = head

    breakable {
      while (true) {
        if (temp.next == null) {
          //temp已经到链表的最后
          break()
        }
        temp = temp.next // 让temp后移
      }
    }
    //当退出while时,temp指向最后
    temp.next = heroNode
    heroNode.pre = temp //必须
  }


  //遍历单向链表
  //思路
  //1. 仍然让temp帮助进行遍历
  //  2.判断链表是否空，空退出, 不为空，就遍历，直到最后结点
  def list(): Unit = {
    if (isEmpty()) {
      println("链表为空，无法遍历")
      return
    }
    //记住，head不能动, 为什么指向 head.next,因为我们有效的数据不包括head
    var temp = head.next
    breakable {
      while (true) {
        //输出当前这个结点的信息
        printf("no=%d name=%s nickname=%s --> ", temp.no, temp.name, temp.nickname)
        //是否是最后结点
        if (temp.next == null) { //是最后结点
          break()
        }
        //如果不是最后的结点，让temp 后移
        temp = temp.next
      }
    }
  }

  def isEmpty(): Boolean = {
    head.next == null
  }

  //修改结点信息
  def update(heroNode: HeroNode2): Unit = {
    //如果链表为空
    if (isEmpty()) {
      println("链表为空")
      return
    }
    //辅助指针，帮助我们定位到修改的结点
    var temp = head.next
    //需要定义一个变量，标识是否找到该节点
    var flag = false

    breakable {
      while (true) {
        //比较
        if (temp.no == heroNode.no) {
          flag = true
          break()
        }
        //判断temp 是不是到最后
        if (temp.next == null) { //最后
          break()
        }
        //让temp后移，实现遍历
        temp = temp.next
      }
    }
    //判断
    if (flag) { //找到
      temp.nickname = heroNode.nickname
      temp.name = heroNode.name
    } else { // 没有找到
      printf("你要修改的%d英雄不存在.", heroNode.no)
    }
  }
}

class HeroNode2(hNo: Int, hName: String, hNickname: String) {
  val no = hNo
  var name = hName
  var nickname = hNickname
  var next: HeroNode2 = null //指向后一个结点 默认为null
  var pre: HeroNode2 = null // 指向前一个结点,默认null
}