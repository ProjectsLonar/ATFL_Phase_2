package com.atflMasterManagement.masterservice.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Status {

	private int code;
	private String message;
	private Object data;
	private Long recordCount;
	private Long  totalCount;
	private Object url;
	private Map<String,String> timeDifference;
	
	
	public Map<String, String> getTimeDifference() {
		return timeDifference;
	}

	public void setTimeDifference(Map<String, String> timeDifference) {
		this.timeDifference = timeDifference;
	}

	public Status() {
	}

	public Status(int code, String message) {
		this.code = code;
		this.message = message;
	}	
	
	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
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

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "Status [code=" + code + ", message=" + message + ", data=" + data + ", recordCount=" + recordCount
				+ ", totalCount=" + totalCount + ", url=" + url + ", timeDifference=" + timeDifference + "]";
	}

	
}