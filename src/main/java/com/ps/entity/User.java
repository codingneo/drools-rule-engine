package com.ps.entity;

public class User {
	private String id;
	
	private double totalSpend;
	
	private int numOfGoodTrans;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getTotalSpend() {
		return totalSpend;
	}

	public void setTotalSpend(double totalSpend) {
		this.totalSpend = totalSpend;
	}

	public int getNumOfGoodTrans() {
		return numOfGoodTrans;
	}

	public void setNumOfGoodTrans(int numOfGoodTrans) {
		this.numOfGoodTrans = numOfGoodTrans;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", totalSpend=" + totalSpend + ", numOfGoodTrans=" + numOfGoodTrans + "]";
	}
	
}
