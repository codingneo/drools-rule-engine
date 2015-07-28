package com.ps.entity;

import java.util.Date;

import com.ps.util.TimeUtil;
import com.ps.util.TransactionStatus;

public class Transaction {
	private String id;
	
	private User user;
	private Merchant merchant;
		
	private double amount;
	private Date date;
	
	private String location;
	
	private String ip;
	private String deviceId;
	
	private TransactionStatus status = TransactionStatus.INIT;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	
	public String getDateAsString(){
		return TimeUtil.formatDate(date);
	}
	
	public void setDateAsString(String dateStr){
		this.date = TimeUtil.parseDate(dateStr);
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", user=" + user + ", merchant=" + merchant + ", amount=" + amount + ", time="
				+ date + ", location=" + location + ", ip=" + ip + ", deviceId=" + deviceId + ", status=" + status
				+ "]";
	}
}
