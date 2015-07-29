package com.ps.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ps.entity.Merchant;
import com.ps.entity.Transaction;
import com.ps.entity.User;
import com.ps.util.TransactionStatus;

import org.apache.log4j.Logger;

/**
 * Temporary class to simulate Data records
 * 
 *
 */
@Component
public class TestDBManagerStub implements DBManager {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	private static Map<String,User> userDB = new HashMap<String,User>();
	private static Map<String,Merchant> merchantDB = new HashMap<String,Merchant>();
	
	private static Map<String,Transaction> transDB = new LinkedHashMap<String,Transaction>();
	
	static {
		User user1 = new User();
		user1.setId("U1");
		userDB.put(user1.getId(), user1);
		
		User user2 = new User();
		user2.setId("U2");
		userDB.put(user2.getId(), user2);

		Merchant mer1 = new Merchant();
		mer1.setId("M1");
		mer1.setTotalExposureAmount(100000);
		merchantDB.put(mer1.getId(), mer1);

		Merchant mer2 = new Merchant();
		mer2.setId("M2");
		mer2.setTotalExposureAmount(200000);
		merchantDB.put(mer2.getId(), mer2);	
	}
	
	@Override
	public List<Transaction> retrieveHistoricTransactions(String userId, Date cutoff) {
		List<Transaction> result = new ArrayList<Transaction>();
		
		for(Transaction t: transDB.values()){
			System.out.println("t.getUser().getId().equals(userId): "+t.getUser().getId().equals(userId));
			System.out.println("t.getDate().after(cutoff): "+t.getDate().after(cutoff));
			if(t.getUser().getId().equals(userId) && t.getDate().after(cutoff)){
				result.add(t);
			}
		}
		
		return result;
	}

	@Override
	public User retreiveUser(String userId) {
		return userDB.get(userId);
	}

	@Override
	public Transaction retreiveTransaction(String transactionId) {
		return transDB.get(transactionId);
	}

	@Override
	public Merchant retreiveMerchant(String marchantId) {
		return merchantDB.get(marchantId);
	}

	@Override
	public void insertTransaction(Transaction transaction) {
		transDB.put(transaction.getId(), transaction);
		
		//if the transaction is eligible, increase total spend for the user
		//increase number of good transactions of the user
		if(transaction.getStatus().equals(TransactionStatus.ELIGIBLE)){
			User user = userDB.get(transaction.getUser().getId());
			
			user.setTotalSpend(user.getTotalSpend() + transaction.getAmount());
			user.setNumOfGoodTrans(user.getNumOfGoodTrans() + 1);
		}
	}


	@Override
	public void updateUser(User user) {
		userDB.put(user.getId(), user);
	}

	@Override
	public void clearTransactions() {
		transDB.clear();
	}

	@Override
	public void resetUsers() {
		for(User u: userDB.values()){
			u.setNumOfGoodTrans(0);
			u.setTotalSpend(0);
		}
		
	}

}
