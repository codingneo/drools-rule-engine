package com.paysense.dao;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.paysense.entity.Merchant;
import com.paysense.entity.Transaction;
import com.paysense.entity.User;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

public class MongoDBManager implements DBManager {

	public static void main(String [] args) {

		try {
			//MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),new ServerAddress("localhost", 27018), new ServerAddress("localhost", 27019)));
			MongoCredential credential = MongoCredential.createCredential("paysense", "paysense-app-develop", "paysense2015".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress("ds037067.mongolab.com",37067), Arrays.asList(credential));
			DB db = mongoClient.getDB("paysense-app-develop");
			
			DBCollection coll = db.getCollection("transactions");
			DBObject myDoc = coll.findOne();
			System.out.println("test1: "+myDoc);
			/*
			BasicDBObject doc = new BasicDBObject("name", "MongoDB")
			        .append("type", "database")
			        .append("count", 1)
			        .append("info", new BasicDBObject("x", 203).append("y", 102));
			coll.insert(doc);
			*/
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<Transaction> retrieveHistoricTransactions(String userId, Date cutoff) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction retreiveTransaction(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User retreiveUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Merchant retreiveMerchant(String marchantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertTransaction(Transaction transaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearTransactions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetUsers() {
		// TODO Auto-generated method stub

	}

}
