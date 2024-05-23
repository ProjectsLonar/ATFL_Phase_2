package com.users.usersmanagement.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class LtTemplateHeader {
	
	Long templateHeaderId;
	String distributorId;
	Date creationDate;
	Long createdBy;
	Date lastUpdatedDate;
	Long lastUpdatedBy;
	Long lastUpdatedLogin;
	String status;
	int productCount;
	Long userId;

	
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



	public Long getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(Long createdBy) {
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


	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}


	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}


	public Long getLastUpdatedLogin() {
		return lastUpdatedLogin;
	}

	public void setLastUpdatedLogin(Long lastUpdatedLogin) {
		this.lastUpdatedLogin = lastUpdatedLogin;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getProductCount() {
		return productCount;
	}


	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	
	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "LtTemplateHeaders [templateHeaderId=" + templateHeaderId + ", distributorId=" + distributorId
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedLogin=" + lastUpdatedLogin + ", status=" + status
				+ ", productCount=" + productCount + "]";
	}
}
