package com.lx.SpamFilter

/**
  * Created by 赌神♣♥♦♠ on 2018/7/12.
  * 垃圾邮件分类 + 逻辑回归 -- 朴素贝叶斯分类
  */

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{HashingTF, RegexTokenizer, StopWordsRemover, Tokenizer, Word2Vec}

object SpamFilterDemo1 {
    def main(args: Array[String]): Unit = {
        Logger.getLogger("org").setLevel(Level.WARN)
        val sess = SparkSession.builder().appName("ml").master("local[4]").getOrCreate()
        val sc = sess.sparkContext;

        //垃圾邮
        //分词器,指定输入列，生成输出列
        val tokenizer = new Tokenizer().setInputCol("message").setOutputCol("words")
        //哈希词频  设置桶数，设置输入列，设置输出列
        val hashingTF = new HashingTF().setNumFeatures(1000).setInputCol("words").setOutputCol("features")
        //创建逻辑回归对象
        val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.01)
        //设置管线件训练数据
        val training = sess.createDataFrame(Seq(
            ("you@example.com", "hope you are well", 0.0),
            ("raj@example.com", "nice to hear from you", 0.0),
            ("thomas@example.com", "happy holidays", 0.0),
            ("mark@example.com", "see you tomorrow", 0.0),
            ("dog@example.com", "save loan money", 1.0),
            ("xyz@example.com", "save money", 1.0),
            ("top10@example.com", "low interest rate", 1.0),
            ("marketing@example.com", "cheap loan", 1.0)))
            .toDF("email", "message", "label")

        //设置管线
        val pipeline = new Pipeline().setStages(Array(tokenizer,hashingTF, lr))
        //拟合，产生模型
        val model = pipeline.fit(training)

        //测试数据，评判model的质量
        val test = sess.createDataFrame(Seq(
            ("you@example.com", "ab how are you"),
            ("jain@example.com", "ab hope doing well"),
            ("caren@example.com", "ab want some money"),
            ("zhou@example.com", "ab secure loan"),
            ("ted@example.com", "ab need loan"))).toDF("email", "message")

        //对测试数据进行模型变换,得到模型的预测结果
        val prediction = model.transform(training).select("email", "message", "prediction")
        prediction.show()

        //类似于切割动作。
        val wordsDF = tokenizer.transform(training)

        /**
          * {"email":"thomas@example.com","message":"happy holidays","label":0.0,"words":["happy","holidays"]}
          * {"email":"mark@example.com","message":"see you tomorrow","label":0.0,"words":["see","you","tomorrow"]}
          * {"email":"dog@example.com","message":"save loan money","label":1.0,"words":["save","loan","money"]}
          * {"email":"xyz@example.com","message":"save money","label":1.0,"words":["save","money"]}
          * {"email":"top10@example.com","message":"low interest rate","label":1.0,"words":["low","interest","rate"]}
          * {"email":"marketing@example.com","message":"cheap loan","label":1.0,"words":["cheap","loan"]}
          * {"email":"you@example.com","message":"hope you are well","label":0.0,"words":["hope","you","are","well"]}
          * {"email":"raj@example.com","message":"nice to hear from you","label":0.0,"words":["nice","to","hear","from","you"]}
          */
        wordsDF.show()
        wordsDF.write.json("D://aaa")

        //哈希词频，把单词变成数字 -- 1000,[242,520]  1000是设置的桶数
        val featurizedDF = hashingTF.transform(wordsDF)

        /**
          * {"thomas@example.com"   "happy holidays"        0.0 "happy","holidays"              "features":{"type":0,"size":1000,"indices":[141,457],"values":[1.0,1.0]}}
          * {"mark@example.com"     "see you tomorrow"      0.0 "see","you","tomorrow"          "features":{"type":0,"size":1000,"indices":[25,425,515],"values":[1.0,1.0,1.0]}}
          * {"dog@example.com"      "save loan money"       1.0 "save","loan","money"           "features":{"type":0,"size":1000,"indices":[242,410,520],"values":[1.0,1.0,1.0]}}
          * {"xyz@example.com"      "save money"            1.0 "save","money"                  "features":{"type":0,"size":1000,"indices":[242,520],"values":[1.0,1.0]}}
          * {"top10@example.com"    "low interest rate"     1.0 "low","interest","rate"         "features":{"type":0,"size":1000,"indices":[70,253,618],"values":[1.0,1.0,1.0]}}
          * {"marketing@example.com""cheap loan"            1.0 "cheap","loan"                  "features":{"type":0,"size":1000,"indices":[410,666],"values":[1.0,1.0]}}
          * {"you@example.com"      "hope you are well"     0.0 "hope","you","are","well"       "features":{"type":0,"size":1000,"indices":[0,138,157,425],"values":[1.0,1.0,1.0,1.0]}}
          * {"raj@example.com"      "nice to hear from you" 0.0 "nice","to","hear","from","you" "features":{"type":0,"size":1000,"indices":[370,388,425,842,921],"values":[1.0,1.0,1.0,1.0,1.0]}}
          */
        featurizedDF.show()
        featurizedDF.write.json("D://aaaa")



    }
}
