package lx.hbase.crud;

import lx.hbase.utils.HbaseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HbaseApiTest {
    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = HbaseApiTest.class.getClassLoader().getResourceAsStream("hbaseConfig.properties");
        // 使用properties对象加载输入流
        properties.load(in);

        /*
        boolean tableExist = HbaseApi.isTableExist(properties.getProperty("table1"));//判断表是否存在
        System.out.println(tableExist);*/

        //HbaseApi.createTable(properties.getProperty("table2"),"info","common");//创建表

        //HbaseApi.dropTable(properties.getProperty("table2"));//删除表

        /*HbaseApi.addRowData(properties.getProperty("table2"),"zyzs001","info","name","zhangsan");//向表中插入数据
        HbaseApi.addRowData(properties.getProperty("table2"),"zyzs002","info","age","15");//向表中插入数据
        HbaseApi.addRowData(properties.getProperty("table2"),"zyzs003","info","sex","male");//向表中插入数据
        */

        //HbaseApi.deleteMultiRow(properties.getProperty("table2"), "zyzs001", "zyzs002");//删除多行数据

        /*
        HbaseApi.getAllRows(properties.getProperty("table2"));//获取所有数据
        System.out.println("----------------------------");
        HbaseApi.getRow(properties.getProperty("table2"),"zyzs001");//获取某一行数据
        System.out.println("----------------------------");
        HbaseApi.getRowQualifier(properties.getProperty("table2"),"zyzs001","info","name");//获取某一行指定“列族:列”的数据
        */

        HbaseUtil.getHbaseConnection();
        //中间这里写逻辑【略】
        HbaseUtil.close();

    }
}
