package com.ps.dao;

import java.util.Date;
import java.util.List;

import com.ps.entity.Merchant;
import com.ps.entity.Transaction;
import com.ps.entity.User;

public interface DBManager {
	
	//retrieval 
	public List<Transaction> retrieveHistoricTransactions(String userId, Date cutoff);
	
	public Transaction retreiveTransaction(String transactionId);
	
	public User retreiveUser(String userId);
	
	public Merchant retreiveMerchant(String marchantId);
	
	//update
	public void insertTransaction(Transaction transaction);
	
	public void updateUser(User user);
	
	//remove
	
	//temp
	public void clearTransactions();
	public void resetUsers();
	
}
