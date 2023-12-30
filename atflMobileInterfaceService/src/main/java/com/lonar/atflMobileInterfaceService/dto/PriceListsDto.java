package com.lonar.atflMobileInterfaceService.dto;

public class PriceListsDto extends BaseClass {

	Long PriceListId;

	Long OrgId;

	String PriceList;

	String PriceListDesc;

	String Currency;

	String ProductCode;

	String ListPrice;

	Long ptrPrice;

	String startDate;

	String endDate;

	String mobileStatus;

	String mobileRemarks;

	String mobileInsertUpdate;
	

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

	public Long getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(Long ptrPrice) {
		this.ptrPrice = ptrPrice;
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

}
