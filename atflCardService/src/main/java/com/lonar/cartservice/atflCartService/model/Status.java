package com.lonar.cartservice.atflCartService.model;

import java.util.Map;

public class Status {

	private int code;
	private String message;
	private Object data;
	private Long  totalCount;
	private Long recordCount;
	
	private StringBuilder stringBuilder;

	private Map<String,String> timeDifference;
	
	
	
	public Map<String, String> getTimeDifference() {
		return timeDifference;
	}

	public void setTimeDifference(Map<String, String> timeDifference) {
		this.timeDifference = timeDifference;
	}

	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}

	public void setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

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

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	@Override
	public String toString() {
		return "Status [code=" + code + ", message=" + message + ", data=" + data + ", totalCount=" + totalCount
				+ ", recordCount=" + recordCount + ", stringBuilder=" + stringBuilder + ", timeDifference="
				+ timeDifference + "]";
	}

	
}