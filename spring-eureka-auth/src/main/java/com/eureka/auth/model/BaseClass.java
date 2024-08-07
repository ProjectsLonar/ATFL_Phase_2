package com.eureka.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@MappedSuperclass
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class BaseClass {

	@Column(name = "STATUS")
	//@JsonProperty("STATUS")
	private String status;
	
	@Column(name = "CREATED_BY")
	//@JsonProperty("CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATION_DATE")
	//@JsonProperty("CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "LAST_UPDATE_LOGIN")
	//@JsonProperty("LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;
	
	@Column(name = "LAST_UPDATED_BY")
	//@JsonProperty("LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_DATE")
	//@JsonProperty("LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Transient
	private String stDate;
	
	@Transient
	private String enDate;
	
	@Transient
	private Integer start;

	@Transient
	private Integer length;
	
	@Transient
	private Integer columnNo;
	
	@Transient
	private String sort;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(String lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getStDate() {
		return stDate;
	}

	public void setStDate(String stDate) {
		this.stDate = stDate;
	}

	public String getEnDate() {
		return enDate;
	}

	public void setEnDate(String enDate) {
		this.enDate = enDate;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Integer columnNo) {
		this.columnNo = columnNo;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "BaseClass [status=" + status + ", createdBy=" + createdBy + ", creationDate=" + creationDate
				+ ", lastUpdateLogin=" + lastUpdateLogin + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdateDate="
				+ lastUpdateDate + ", stDate=" + stDate + ", enDate=" + enDate + ", start=" + start + ", length="
				+ length + ", columnNo=" + columnNo + ", sort=" + sort + "]";
	}
	
}
