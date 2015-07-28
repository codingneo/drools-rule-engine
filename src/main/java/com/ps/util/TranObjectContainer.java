package com.ps.util;

import java.util.List;

import com.ps.entity.Transaction;

public class TranObjectContainer {
	
	private Transaction newTransaction;
	private List<Transaction> oldTransactions;
	
	public Transaction getNewTransaction() {
		return newTransaction;
	}
	public void setNewTransaction(Transaction newTransaction) {
		this.newTransaction = newTransaction;
	}
	public List<Transaction> getOldTransactions() {
		return oldTransactions;
	}
	public void setOldTransactions(List<Transaction> oldTransactions) {
		this.oldTransactions = oldTransactions;
	}
}
