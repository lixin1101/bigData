package lx.dao;

import lx.demain.Product;
import lx.utils.MongoDBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ProductDao {
	/*DB db = MongoDBUtils.getDB("itcast");
	DBCollection coll = db.getCollection("products");
	
	//查询所有商品信息
	public DBCursor findAllProducts() {
		DBCursor cur = coll.find();
		return cur;
	}

	//根据商品编号查询商品信息
	public DBCursor findProductsByPid(int pid) {
		//BasicDBObject底层是hashMap
		DBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("pid", pid);
		DBCursor cur = coll.find(basicDBObject);
		return cur;
	}

	//添加商品
	public void addProduct(Product p) {
		DBCollection coll = MongoDBUtils.getCollection("itcast", "products");
		DBObject basicDBObject = new BasicDBObject();
		//添加数据
		basicDBObject.put("pid", p.getPid());
		basicDBObject.put("pname", p.getPname());
		basicDBObject.put("price", p.getPrice());
		//插入数据
		coll.insert(basicDBObject);
	}

	//根据编号删除商品
	public void deleteProductByPid(int pid) {
		DBCollection coll = MongoDBUtils.getCollection("itcast", "products");
		DBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("pid", pid);
		coll.remove(basicDBObject);
		
	}*/


	
}
