package com.lonar.cartservice.atflCartService.model;

import java.util.List;

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

@Entity
@Table(name = "LT_TEMPLATE_LINES")
@JsonInclude(Include.NON_NULL)
public class LtTemplateLines {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_TEMPLATE_LINES_S")
	@SequenceGenerator(name = "LT_TEMPLATE_LINES_S", sequenceName = "LT_TEMPLATE_LINES_S", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "TEMPLATE_LINE_ID")
	Long templateLineId;
	
	@Column(name = "TEMPLATE_HEADER_ID")
	Long templateHeaderId;
	
	@Column(name = "SALES_TYPE")
	String salesType;
	
	@Column(name = "PRODUCT_ID")
	String productId;
	
	@Column(name = "PRODUCT_NAME")
	String productName;
	
	@Column(name = "PRODUCT_DESCRIPTION")
	String productDescription;
	
	@Column(name = "PTR_PRICE")
	Long ptrPrice;
	
	@Column(name = "QUANTITY")
	Long quantity;
	
	@Column(name = "AVAILABLE_QUANTITY")
	Long availableQuantity;
	
	@Column(name = "CREATION_DATE")
	String creationDate;
	
	@Column(name = "CREATED_BY")
	String createdBy;
	
	@Column(name = "LAST_UPDATED_DATE")
	String lastUpdatedDate;
	
	@Column(name = "LAST_UPDATED_BY")
	String lastUpdatedBy;
	
	@Column(name = "LAST_UPDATED_LOGIN")
	String lastUpdatedLogin;

	
	@Transient
	String listPrice;
	
	@Transient
	String unitsPerCase;
	
	@Transient
	private Long inventoryQuantity;
	
	@Transient
	private double MRP;
	
	@Transient
	List<Double> MRP1;
	
	@Transient
	List<LtTemplateLines> MRP2;
	
	
	public Long getInventoryQuantity() {
		return inventoryQuantity;
	}

	public void setInventoryQuantity(Long inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}

	public double getMRP() {
		return MRP;
	}

	public void setMRP(double mRP) {
		MRP = mRP;
	}

	public List<Double> getMRP1() {
		return MRP1;
	}

	public void setMRP1(List<Double> mRP1) {
		MRP1 = mRP1;
	}

	public List<LtTemplateLines> getMRP2() {
		return MRP2;
	}

	public void setMRP2(List<LtTemplateLines> mRP2) {
		MRP2 = mRP2;
	}

	public String getUnitsPerCase() {
		return unitsPerCase;
	}

	public void setUnitsPerCase(String unitsPerCase) {
		this.unitsPerCase = unitsPerCase;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	public Long getTemplateLineId() {
		return templateLineId;
	}

	public void setTemplateLineId(Long templateLineId) {
		this.templateLineId = templateLineId;
	}

	public Long getTemplateHeaderId() {
		return templateHeaderId;
	}

	public void setTemplateHeaderId(Long templateHeaderId) {
		this.templateHeaderId = templateHeaderId;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Long getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(Long ptrPrice) {
		this.ptrPrice = ptrPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
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

	
	@Override
	public String toString() {
		return "LtTemplateLines [templateLineId=" + templateLineId + ", templateHeaderId=" + templateHeaderId
				+ ", salesType=" + salesType + ", productId=" + productId + ", productName=" + productName
				+ ", productDescription=" + productDescription + ", ptrPrice=" + ptrPrice + ", quantity=" + quantity
				+ ", availableQuantity=" + availableQuantity + ", creationDate=" + creationDate + ", createdBy="
				+ createdBy + ", lastUpdatedDate=" + lastUpdatedDate + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", lastUpdatedLogin=" + lastUpdatedLogin + ", listPrice=" + listPrice + ", unitsPerCase="
				+ unitsPerCase + ", inventoryQuantity=" + inventoryQuantity + ", MRP=" + MRP + ", MRP1=" + MRP1
				+ ", MRP2=" + MRP2 + "]";
	}

	
}
