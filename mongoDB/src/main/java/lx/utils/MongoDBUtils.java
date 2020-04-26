package lx.utils;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBUtils {

	//使用collection用来保存Mongo的数据库连接对象
	static Mongo collection=null;
	static DB db=null;
	static DBCollection coll=null;

	@SuppressWarnings("deprecation")
	public static MongoCollection getDB(String dbName) {
		ServerAddress serverAddress = new ServerAddress("192.168.80.201", 27017);
		List<ServerAddress> addrs = new ArrayList<ServerAddress>();
		addrs.add(serverAddress);

		//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
		MongoCredential credential = MongoCredential.createScramSha1Credential("lx", dbName, "1101".toCharArray());
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(credential);

		//通过连接认证获取MongoDB连接
		MongoClient mongoClient = new MongoClient(addrs, credentials);

		//连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);

		MongoCollection<Document> collection = mongoDatabase.getCollection("products");
		//将具体的数据库连接返回给调用者
		return collection;
	}

	@SuppressWarnings("deprecation")
	public static DB getDB2(String dbName) {
		//创建一个Mongo的数据库连接对象
		collection =new Mongo("192.168.80.201:27017");
		//通过获取数据库的连接对象collection根据传递的数据库名dbName来连接具体的数据库
		db=collection.getDB(dbName);
		//将具体的数据库连接返回给调用者	
		return db;
	}
	
	@SuppressWarnings("deprecation")
	public static DBCollection getCollection(String dbName,String collName) {
		collection =new Mongo("127.0.0.1:27017");
		db=collection.getDB(dbName);
		coll=db.getCollection(collName);
		return coll;
	}
	public static void main(String[] args) {
		MongoCollection collection = MongoDBUtils.getDB("itcast");
		System.out.println(collection);
	}
}
