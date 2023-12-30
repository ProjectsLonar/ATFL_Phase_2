package com.lonar.atflMobileInterfaceService.model;

import javax.persistence.Basic;
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
@Table(name = "LT_MAST_PRICE_LISTS")
@JsonInclude(Include.NON_NULL)
public class LtMastPriceLists extends BaseClass {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "PRICE_LIST_ID")
	private Long PriceListId;

	@Column(name = "ORG_ID")
	private Long OrgId;

	@Column(name = "PRICE_LIST")
	private String PriceList;

	@Column(name = "PRICE_LIST_DESC")
	private String PriceListDesc;

	@Column(name = "CURRENCY")
	private String Currency;

	@Column(name = "PRODUCT_CODE")
	private String ProductCode;

	@Column(name = "LIST_PRICE")
	private String ListPrice;

	@Column(name = "PTR_PRICE")
	private String ptrPrice;

	@Column(name = "START_DATE")
	private String startDate;

	@Column(name = "END_DATE")
	private String endDate;
	
	@Transient
	String mobileStatus;

	@Transient
	String mobileRemarks;

	@Transient
	String mobileInsertUpdate;

	public Long getPriceListId() {
		return PriceListId;
	}

	public void setPriceListId(Long priceListId) {
		PriceListId = priceListId;
	}

	public Long getOrgId() {
		return OrgId;
	}

	public void setOrgId(Long orgId) {
		OrgId = orgId;
	}

	public String getPriceList() {
		return PriceList;
	}

	public void setPriceList(String priceList) {
		PriceList = priceList;
	}

	public String getPriceListDesc() {
		return PriceListDesc;
	}

	public void setPriceListDesc(String priceListDesc) {
		PriceListDesc = priceListDesc;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	public String getListPrice() {
		return ListPrice;
	}

	public void setListPrice(String listPrice) {
		ListPrice = listPrice;
	}

	public String getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
