package com.atflMasterManagement.masterservice.dto;

public class StatusWiseOrdersCountDto {
	
	private String status;
	private Long recordCount;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

}
