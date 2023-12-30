package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_inventory")
@JsonInclude(Include.NON_NULL)
public class LtMastInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INVENTORY_ID")
	private Long inventoryId;

	@Column(name = "INVENTORY_CODE")
	String inventoryCode;

	@Column(name = "INVENTORY_NAME")
	String inventoryName;

	@Column(name = "INVENTORY_STATUS")
	String inventoryStatus;

	@Column(name = "DIST_CODE")
	String distCode;

//	@Column(name = "DIST_NAME")
//	String distName;

	@Column(name = "DIST_ID")
	Long distId;

	@Column(name = "PRODUCT_RID")
	private String productRid;

	@Column(name = "PRODUCT_CODE")
	String productCode;

	@Column(name = "QUANTITY")
	String quantity;

	@Column(name = "PRODUCT_ID")
	Long productId;
	
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

	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

//	public String getDistName() {
//		return distName;
//	}
//
//	public void setDistName(String distName) {
//		this.distName = distName;
//	}

	public String getProductRid() {
		return productRid;
	}

	public void setProductRid(String productRid) {
		this.productRid = productRid;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Long getDistId() {
		return distId;
	}

	public void setDistId(Long distId) {
		this.distId = distId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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
	

}
