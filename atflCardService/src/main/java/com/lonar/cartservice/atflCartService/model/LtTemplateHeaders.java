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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_TEMPLATE_HEADERS")
@JsonInclude(Include.NON_NULL)
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
	String createdBy;
	
	@Column(name = "LAST_UPDATED_DATE")
	Date lastUpdatedDate;
	
	@Column(name = "LAST_UPDATED_BY")
	String lastUpdatedBy;
	
	
	@Column(name = "LAST_UPDATE_LOGIN")
	String lastUpdatedLogin;
	
	@Column(name = "STATUS")
	String status;
	
	
	@Column(name = "PRODUCT_COUNT")
	String productCount;

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


	public String getProductCount() {
		return productCount;
	}


	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}


	@Override
	public String toString() {
		return "LtTemplateHeaders [templateHeaderId=" + templateHeaderId + ", distributorId=" + distributorId
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedLogin=" + lastUpdatedLogin + ", status=" + status
				+ ", productCount=" + productCount + "]";
	}


	
	
}
