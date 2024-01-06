package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;

@JsonInclude(Include.NON_NULL)
public class LtTemplateDto {
	private Long templateHeaderId;
	private String distributorId;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdatedDate;
	private String lastUpdatedBy;
	private String lastUpdatedLogin;
	private String status;
	private String userId;
	private List<LtTemplateLines> ltTemplateLines;
	
	public Long getTemplateHeaderId() {
		return templateHeaderId;
	}
	public void setTemplateHeaderId(Long templateHeaderId) {
		this.templateHeaderId = templateHeaderId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedLogin() {
		return lastUpdatedLogin;
	}
	public void setLastUpdatedLogin(String lastUpdatedLogin) {
		this.lastUpdatedLogin = lastUpdatedLogin;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public List<LtTemplateLines> getLtTemplateLines() {
		return ltTemplateLines;
	}
	public void setLtTemplateLines(List<LtTemplateLines> ltTemplateLines) {
		this.ltTemplateLines = ltTemplateLines;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "LtTemplateDto [templateHeaderId=" + templateHeaderId + ", distributorId=" + distributorId
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedLogin=" + lastUpdatedLogin + ", status=" + status
				+ ", userId=" + userId + ", ltTemplateLines=" + ltTemplateLines + "]";
	}	
}
