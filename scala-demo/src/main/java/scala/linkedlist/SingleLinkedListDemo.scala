package scala.linkedlist

import util.control.Breaks._
object SingleLinkedListDemo {
  def main(args: Array[String]): Unit = {
    //测试一把链表的创建和遍历
    val singleLinkedList = new SingleLinkedList()
    //创建三个英雄
    val node1 = new HeroNode(1, "宋江", "及时雨")
    val node2 = new HeroNode(2, "卢俊义", "玉麒麟")
    val node3 = new HeroNode(3, "吴用", "智多星")
    val node4 = new HeroNode(4, "张飞", "翼德")

    //    singleLinkedList.add(node4)
    //    singleLinkedList.add(node1)
    //    singleLinkedList.add(node3)
    //    singleLinkedList.add(node2)

    singleLinkedList.addByOrder(node4)
    singleLinkedList.addByOrder(node1)
    singleLinkedList.addByOrder(node3)
    singleLinkedList.addByOrder(node2)
    //singleLinkedList.addByOrder(node2)




    println("链表的情况")
    singleLinkedList.list()

    /*
    //测试对结点的修改
    val node5 = new HeroNode(4, "公孙胜", "入云龙")
    singleLinkedList.update(node5)
    printf("链表修改后的情况\n\n")
    singleLinkedList.list()

    //测试一把删除结点
    println()
    println()
    println("删除后的链表情况")
    singleLinkedList.del(3)
    singleLinkedList.del(1)
    singleLinkedList.del(4)
    singleLinkedList.del(12)

    singleLinkedList.list()*/

  }
}

//创建单链表的类
class SingleLinkedList {
  //创建头结点，指向该链表的头部
  val head = new HeroNode(-1, "","")


  //删除一个结点
  def del(no:Int): Unit = {
    //判断是否为空
    if(isEmpty()) {
      println("链表为空")
      return
    }
    //让temp指向head
    var temp = head
    var flag = false
    //遍历，让temp指向，要删除的结点的前一个结点
    breakable {
      while (true) {
        if (temp.next.no == no) {
          //找到
          flag = true
          break()
        }
        //判断temp是否指向链表的倒数第二个结点
        if (temp.next.next == null) {
          break()
        }
        //让temp后移，实现遍历
        temp = temp.next
      }
    }
    //判断flag 的情况
    if(flag) {//找到
      temp.next = temp.next.next
    } else {
      printf("你要删除的结点%d 不存在", no)
    }
  }

  //修改结点信息
  def update(heroNode: HeroNode): Unit = {
    //如果链表为空
    if(isEmpty()) {
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
    if(flag) { //找到
      temp.nickname = heroNode.nickname
      temp.name = heroNode.name
    } else { // 没有找到
      printf("你要修改的%d英雄不存在.", heroNode.no)
    }
  }

  //添加英雄到单链表
  //默认直接将人加入到链表的最后
  def add(heroNode: HeroNode): Unit = {
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
  }

  //按照no编号的从小到大进行插入
  def addByOrder(heroNode: HeroNode): Unit = {
    //让temp 指向 head
    var temp = head
    var flag = false // 标识是否已经存在这个编号的结点
    breakable {
      //将temp 定位要添加的结点前一个位置，即将新的结点，添加到temp 后面
      while (true) {
        //判断是否是最后一个，如果就是最后一个，也找到位置
        if (temp.next == null) {
          break()
        }

        if (temp.next.no == heroNode.no) { //说明no已经存在
          flag = true
          break()
        } else if (temp.next.no > heroNode.no) { //说明heroNode 就应该加入到temp的后面
          break()
        }

        //将temp 后移，实现遍历
        temp = temp.next

      }
    }
    //当退出while循环后，
    if(flag) { //说明已经有no
      printf("已经存在no=%d 人物", heroNode.no)
    } else {
      heroNode.next = temp.next //
      temp.next = heroNode
    }

  }

  //遍历单向链表
  //思路
  //1. 仍然让temp帮助进行遍历
  //  2.判断链表是否空，空退出, 不为空，就遍历，直到最后结点
  def list(): Unit = {
    if(isEmpty()) {
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
}

class HeroNode(hNo: Int, hName: String, hNickname: String) {
  val no = hNo
  var name = hName
  var nickname = hNickname
  var next: HeroNode = null //默认为null
}
