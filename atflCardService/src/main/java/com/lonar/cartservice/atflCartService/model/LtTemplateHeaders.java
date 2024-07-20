package com.lonar.cartservice.atflCartService.model;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_TEMPLATE_HEADERS")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class LtTemplateHeaders {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_TEMPLATE_HEADERS_S")
	@SequenceGenerator(name = "LT_TEMPLATE_HEADERS_S", sequenceName = "LT_TEMPLATE_HEADERS_S", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "TEMPLATE_HEADER_ID")
	Long templateHeaderId;
	
	@Column(name = "DISTRIBUTOR_ID")
	String distributorId;
	
	@Column(name = "CREATION_DATE")
	Date creationDate;
	
	@Column(name = "CREATED_BY")
	Long createdBy;
	
	@Column(name = "LAST_UPDATED_DATE")
	Date lastUpdatedDate;
	
	@Column(name = "LAST_UPDATED_BY")
	Long lastUpdatedBy;
	
	
	@Column(name = "LAST_UPDATE_LOGIN")
	Long lastUpdatedLogin;
	
	@Column(name = "STATUS")
	String status;
	
	
	@Column(name = "PRODUCT_COUNT")
	int productCount;
	
	@Transient
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
