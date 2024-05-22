package com.atflMasterManagement.masterservice.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExcelReportDto {
	private String fromDate;
    private String toDate;
    private String orgId;
    private int limit;
	private int offset;
	private Long userId;
    
    //salesperson
    private String distributorCode;
    private String employeeCode;
    private String distributorId;
    private String employeeId;
    private String productId;
    private String outletId;
    private String categoryId;
    
    //Region
    private String region;
    private String priceList;
    
    //---product Wise revenue summary report
    private String productName;
    
  //---outletwise order summary report
    private String outletCode;
    private String status;
    private String searchField;
    
  //---distributor wise summary report
    private String categoryCode;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getProductId() {
		return productId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	@Override
	public String toString() {
		return "ExcelReportDto [fromDate=" + fromDate + ", toDate=" + toDate + ", orgId=" + orgId + ", limit=" + limit
				+ ", offset=" + offset + ", userId=" + userId + ", distributorCode=" + distributorCode
				+ ", employeeCode=" + employeeCode + ", distributorId=" + distributorId + ", employeeId=" + employeeId
				+ ", productId=" + productId + ", outletId=" + outletId + ", categoryId=" + categoryId + ", region="
				+ region + ", priceList=" + priceList + ", productName=" + productName + ", outletCode=" + outletCode
				+ ", status=" + status + ", searchField=" + searchField + ", categoryCode=" + categoryCode + "]";
	}
	
	
}
