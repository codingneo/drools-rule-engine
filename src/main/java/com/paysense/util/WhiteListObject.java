package com.paysense.util;

public class WhiteListObject {
	enum Type {
		PHONE,
		CUSTOMER;
	}
	
	private Type type;
	private String value;
	
	public WhiteListObject(Type type, String value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "WhiteListObject [type=" + type + ", value=" + value + "]";
	}

}
