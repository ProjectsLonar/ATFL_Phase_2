package com.eureka.auth.model;

import java.util.Map;

public class Status {

	private int code;
	private String message;
	private Object data;
	private Map<String,String> timeDifference;

	public Status() {
	}

	public Status(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	

	public Map<String, String> getTimeDifference() {
		return timeDifference;
	}

	public void setTimeDifference(Map<String, String> timeDifference) {
		this.timeDifference = timeDifference;
	}

	@Override
	public String toString() {
		return "Status [code=" + code + ", message=" + message + ", data=" + data + ", timeDifference=" + timeDifference
				+ "]";
	}
	
}