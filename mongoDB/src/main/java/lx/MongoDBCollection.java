package lx;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBCollection {
    public static void main(String[] args) {
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress("192.168.80.103", 27017);
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential("lx", "apps", "1101".toCharArray());
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            credentials.add(credential);

            //通过连接认证获取MongoDB连接
            MongoClient mongoClient = new MongoClient(addrs, credentials);

            //连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("apps");
            System.out.println("Connect to database successfully");

            //mongoDatabase.createCollection("xxx");
            //System.out.println("集合创建成功");

            MongoCollection<Document> collection = mongoDatabase.getCollection("products");
            System.out.println("集合选择成功");

            //检索所有文档
            /**
             * 1. 获取迭代器FindIterable<Document>
             * 2. 获取游标MongoCursor<Document>
             * 3. 通过游标遍历检索出的文档集合
             * */
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            int i = 0;
            while (mongoCursor.hasNext()) {
                //System.out.println(mongoCursor.next());
                Document dou = mongoCursor.next();
                Double pid = dou.getDouble("pid");
                String pname = dou.getString("pname");
                Double price = dou.getDouble("price");
                System.out.println(pid+","+pname+","+price);
                i++;
            }
            System.out.println("i=" + i);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
