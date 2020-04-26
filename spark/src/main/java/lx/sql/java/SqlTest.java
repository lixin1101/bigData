package lx.sql.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;


/**
 * Created by 赌神♣♥♦♠ on 2018/7/26.
 */
public class SqlTest {
    /**
     * 使用类似SQL方式访问hadoop，实现MR计算。RDD
     *
     * @param args
     */
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("SqlTest");
        SparkSession session = SparkSession.builder()
                .appName("SQLJava")
                //设置master方式.
                .config("spark.master", "local")
                .getOrCreate();
        Dataset<Row> df = session.read().json("F:\\bigdataSource\\plant.txt");
        df.createOrReplaceTempView("plant");
        Dataset<Row> result = session.sql("select landName,estPickDt,sowArea,userName from plant where userName like '李%'");
        result.show();
        Dataset<Row> result2 = session.sql("select userName,count(1) as c from plant group by userName having c >1 order by c desc");
        result2.show();
        //result2.write().json("G:\\淘宝大数据\\13 - 大数据-Spark\\Spark-05\\plant\\out");
    }
}
