package com.ps.entity;

public class Merchant {
	private String id;
	
	private String name;

	private double totalExposureAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalExposureAmount() {
		return totalExposureAmount;
	}

	public void setTotalExposureAmount(double totalExposureAmount) {
		this.totalExposureAmount = totalExposureAmount;
	}

	@Override
	public String toString() {
		return "Merchant [id=" + id + ", name=" + name + ", totalExposureAmount=" + totalExposureAmount + "]";
	}
}
