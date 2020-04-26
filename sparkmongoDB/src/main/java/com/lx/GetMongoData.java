package com.lx;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetMongoData {


    public static List<String> getData() throws Exception {
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
        MongoCollection<Document> collection = mongoDatabase.getCollection("products");

        //检索所有文档
        /**
         * 1. 获取迭代器FindIterable<Document>
         * 2. 获取游标MongoCursor<Document>
         * 3. 通过游标遍历检索出的文档集合
         * */
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        int i = 0;
        List<String> list=new ArrayList<String>();
        while (mongoCursor.hasNext()) {
            //System.out.println(mongoCursor.next());
            Document dou = mongoCursor.next();
            Double pid = dou.getDouble("pid");
            String pname = dou.getString("pname");
            Double price=dou.getDouble("price");
            //String context="{pid:" + pid+",pname:"+pname+",price:"+price + "}";
            String context=pid+"\t"+pname+"\t"+price;
            list.add(context);
            i++;
        }
        System.out.println("i=" + i);
        return list;
    }

    public static void main(String[] args) throws Exception {

        List<String> data = getData();
        System.out.println(data);
    }
}
