package lx.hbase.crud;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseApi2 {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        //获取hbase连接对象
        Connection connection = ConnectionFactory.createConnection(conf);

        //获取操作对象admin
        Admin admin = connection.getAdmin();

        //操作数据库
        //判断命名空间是否存在
        try {
            admin.getNamespaceDescriptor("ns3");
        } catch (Exception e) {
            //创建表空间
            NamespaceDescriptor ns1 = NamespaceDescriptor.create("ns3").build();
            admin.createNamespace(ns1);
        }
        //判断表是否存在
        TableName tableName = TableName.valueOf("ns3:student");
        boolean flag = admin.tableExists(tableName);

        System.out.println(flag);
        if (flag) {
            //获取指定的表对象
            Table table = connection.getTable(tableName);
            //查询数据
            String rowkey = "1001";
            //String => byte[]
            //字符编码      byte[] bt=String(???) ==>getBytes("ISO8859-1")  new String(bt,"utf-8")
            Get get = new Get(Bytes.toBytes(rowkey));
            Result result = table.get(get);
            boolean empty = result.isEmpty();
            System.out.println("1001数据是否存在 = "+ !empty);
            if(empty){
                //新增数据
                Put put = new Put(Bytes.toBytes(rowkey));

                String family="info";
                String column="name";
                String val="zhangsan";
                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(column),Bytes.toBytes(val));

                table.put(put);
                System.out.println("增加数据。。。");
            }else {

                for (Cell cell : result.rawCells()) {
                    System.out.println("value:"+Bytes.toString(CellUtil.cloneValue(cell)));
                    System.out.println("family:"+Bytes.toString(CellUtil.cloneFamily(cell)));
                    System.out.println("qualifier:"+Bytes.toString(CellUtil.cloneQualifier(cell)));
                    System.out.println("rowkey:"+Bytes.toString(CellUtil.cloneRow(cell)));
                }
            }
        } else {
            //创建表
            //创建表描述对象
            HTableDescriptor td = new HTableDescriptor(tableName);
            //增加列族
            HColumnDescriptor cd = new HColumnDescriptor("info");
            td.addFamily(cd);
            admin.createTable(td);

            System.out.println("表创建成功");

        }
    }


}
