package zyzs.wxapp

import net.sf.json.JSONObject

object Test {

  def main(args: Array[String]): Unit = {
    val jsonObject = JSONObject.fromObject("{\"headimg\":\"https://wx.qlogo.cn/mmopen/vi_32/0TenicClhplePJukW8rYOd0LQlPQreRZpu2MkqX0SSfiaMeTlkQHmVT45jdqZsLAcuRxowrYYgcYBdMLAMkrjIbw/132\",\"nickname\":\"峰哥\"}")
    jsonObject.getString("nickname")
  }
}
