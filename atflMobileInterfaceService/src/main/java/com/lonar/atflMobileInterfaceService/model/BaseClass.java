package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@MappedSuperclass
@JsonInclude(Include.NON_NULL)
public class BaseClass {

	@Column(name = "STATUS")
	private String status;
	
/*	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;*/
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "LAST_UPDATE_LOGIN")
	private Long lastUpdateLogin;
	
	@Column(name = "LAST_UPDATED_BY")
	private Long lastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	
	@Transient
	String mobileStatus;//insert fa

	@Transient
	String mobileRemarks;//fail

	@Transient
	String mobileInsertUpdate;//update

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Long getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(Long lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus(String mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public String getMobileRemarks() {
		return mobileRemarks;
	}

	public void setMobileRemarks(String mobileRemarks) {
		this.mobileRemarks = mobileRemarks;
	}

	public String getMobileInsertUpdate() {
		return mobileInsertUpdate;
	}

	public void setMobileInsertUpdate(String mobileInsertUpdate) {
		this.mobileInsertUpdate = mobileInsertUpdate;
	}

	@Override
	public String toString() {
		return "BaseClass [status=" + status + ", createdBy=" + createdBy + ", creationDate=" + creationDate
				+ ", lastUpdateLogin=" + lastUpdateLogin + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdateDate="
				+ lastUpdateDate + ", mobileStatus=" + mobileStatus + ", mobileRemarks=" + mobileRemarks
				+ ", mobileInsertUpdate=" + mobileInsertUpdate + "]";
	}
	
	
}
