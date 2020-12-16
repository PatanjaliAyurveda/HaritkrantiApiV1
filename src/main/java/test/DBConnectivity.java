package test;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DBConnectivity {
	public DB getDatabase() {
		DB db=null;
		try {
			MongoClient mongo = new MongoClient("localhost", 27017);
		//	MongoClient mongo = new MongoClient("172.16.70.4", 27017);
			db = mongo.getDB("haritkranti");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return db;
	}
}
