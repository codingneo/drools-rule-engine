package com.paysense.util;

public enum PSResponse {

	INIT(0, "INIT"), ELIGIBLE(1, "ELIGIBLE"), REVIEW(2, "REVIEW"), REJECT(3, "REJECT"), UNKNOWN(-1, "UNKNOWN");

	private int code;
	private String name;
	private String message;

	PSResponse(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static PSResponse getPSResponseByCode(int code) {
		for (PSResponse res : PSResponse.values()) {
			if (res.getCode() == code) {
				return res;
			}
		}
		return UNKNOWN;
	}

	@Override
	public String toString() {
		String result = "Status Code = " + code + ",Status Name = " + name;
		if (message != null && message.trim().length() > 0) {
			result = result + ", Message: " + message;
		}

		return result;
	}
}
