package com.paysense.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;
import com.paysense.entity.Merchant;
import com.paysense.entity.Transaction;
import com.paysense.entity.User;
import com.paysense.entity.WhiteListObject;
import com.paysense.util.PSResponse;

@Component
public class MongoDBManager implements DBManager {

	public static void main(String[] args) throws UnknownHostException {


		//MongoCredential credential = MongoCredential.createCredential("", "ps", "".toCharArray());
		
		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
		
		DB db = mongoClient.getDB("ps");
		
		/*
		DBCollection coll = db.getCollection(COLLECTION_TRANS);
		
		BasicDBObject query = new BasicDBObject("id","T1");
		DBCursor cursor = coll.find(query);

	    try {
	       while(cursor.hasNext()) {
	          DBObject dbobj = cursor.next();
	        //Converting BasicDBObject to a custom Class(Employee)
	          Transaction emp = (new Gson()).fromJson(dbobj.toString(), Transaction.class);
	          System.out.println("test1: "+emp);
	       }
	    } finally {
	       cursor.close();
	    }	
	    */
		
		/*
		Transaction tran = new Transaction();
		tran.setId("T1");
	
		tran.setUserId("U1");

		//Converting a custom Class(Employee) to BasicDBObject
		Gson gson = new Gson();
		BasicDBObject obj = (BasicDBObject)JSON.parse(gson.toJson(tran));
		coll.insert(obj);
		//findEmployee(new BasicDBObject("id","1001"));
		 */
	}

	private static final String COLLECTION_TRANS = "transactions";
	private static final String COLLECTION_USERS = "users";
	private static final String COLLECTION_WHITE_LIST = "white_list";
	
	@Value("${db.main.host}")
	private String dbHost;

	@Value("${db.main.port}")
	private int dbPort;

	@Value("${db.main.name}")
	private String dbName;

	@Value("${db.main.user.name}")
	private String userName;

	@Value("${db.main.user.password}")
	private String password;

	private DB dbInstance;

	private DBCollection getDBCollection(String name) {

		if(dbInstance == null) {
			MongoClient mongoClient = null;
			try {
				MongoCredential credential = MongoCredential.createCredential(userName, dbName, password.toCharArray());
				mongoClient = new MongoClient(new ServerAddress(dbHost, dbPort), Arrays.asList(credential));
				
				//mongoClient = new MongoClient(new ServerAddress(dbHost, dbPort));
	
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
			dbInstance = mongoClient.getDB(dbName);
		}

		return dbInstance.getCollection(name);
	}

	@Override
	public List<Transaction> retrieveHistoricTransactions(Transaction transaction, Date cutoff) {
		List<Transaction> result = new ArrayList<Transaction>();
		
		DBCollection coll = getDBCollection(COLLECTION_TRANS);
		BasicDBObject query = new BasicDBObject("userId",transaction.getUserId());
		DBCursor cursor = coll.find(query);

	    try {
	       while(cursor.hasNext()) {
	          DBObject dbobj = cursor.next();

	          Transaction t = (new Gson()).fromJson(dbobj.toString(), Transaction.class);
	          if(t.getId().equals(transaction.getId())) {
	        	  result.add(t);
	          }
	       }
	    } finally {
	       cursor.close();
	    }	
	    
		return result;
	}

	@Override
	public Transaction retreiveTransaction(String transactionId) {
		DBCollection coll = getDBCollection(COLLECTION_TRANS);
		BasicDBObject query = new BasicDBObject("id",transactionId);
	
		DBObject tran = coll.findOne(query);
		
		Transaction t = null;
		if(tran != null) {
			t = (new Gson()).fromJson(tran.toString(), Transaction.class);
		}

		return t;
	}

	@Override
	public User retreiveUser(String userId) {
		DBCollection coll = getDBCollection(COLLECTION_USERS);
		BasicDBObject query = new BasicDBObject("id",userId);
	
		DBObject user = coll.findOne(query);
		
		User u = null;
		if(user != null) {
			u = (new Gson()).fromJson(user.toString(), User.class);
		}

		return u;
	}

	@Override
	public Merchant retreiveMerchant(String marchantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertTransaction(Transaction transaction) {
		DBCollection coll = getDBCollection(COLLECTION_TRANS);
		BasicDBObject obj = (BasicDBObject)JSON.parse(new Gson().toJson(transaction));
		
		Transaction t = this.retreiveTransaction(transaction.getId());
		if(t==null){
			coll.insert(obj);
		} else {
			BasicDBObject searchQuery = new BasicDBObject().append("id", t.getId());

			coll.update(searchQuery, obj);
		}
	}
	
	@Override
	public void updateUser(User user) {
		DBCollection coll = getDBCollection(COLLECTION_USERS);
		
		BasicDBObject searchQuery = new BasicDBObject().append("id", user.getId());

		coll.update(searchQuery, (BasicDBObject)JSON.parse(new Gson().toJson(user)));
	}

	@Override
	public void clearTransactions() {
		DBCollection coll = getDBCollection(COLLECTION_TRANS);
		coll.drop();
	}

	@Override
	public void resetUsers() {
		DBCollection coll = getDBCollection(COLLECTION_USERS);
		DBCursor cursor = coll.find();

	    try {
	       while(cursor.hasNext()) {
	          DBObject dbobj = cursor.next();

	          User user = (new Gson()).fromJson(dbobj.toString(), User.class);
	          user.setNumOfGoodTrans(0);
	          user.setTotalSpend(0);
	          
	          updateUser(user);
	       }
	    } finally {
	       cursor.close();
	    }	

	}

	@Override
	public List<WhiteListObject> retrieveWhiteList() {
		List<WhiteListObject> result = new ArrayList<WhiteListObject>();
		
		DBCollection coll = getDBCollection(COLLECTION_WHITE_LIST);
	
		DBCursor cursor = coll.find();

	    try {
	       while(cursor.hasNext()) {
	          DBObject dbobj = cursor.next();

	          WhiteListObject t = (new Gson()).fromJson(dbobj.toString(), WhiteListObject.class);
	         
	          result.add(t);
	       }
	    } finally {
	       cursor.close();
	    }	
	    
		return result;
	}

}
