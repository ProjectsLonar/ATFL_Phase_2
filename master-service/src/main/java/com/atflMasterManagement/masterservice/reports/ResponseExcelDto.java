package com.atflMasterManagement.masterservice.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseExcelDto {
	
	private String region;
	private String distributorId;
	private String distributorName;
	private String distributorCode;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String employeeCode;
	private Long productId;
	private String productCode;
	private String productName;
	private String outletId;
	private String outletCode;
	private String outletName;
	private String status;
	private Long categoryId;
	private String categoryCode;
	private String categoryName;
	private String categoryDesc;
	
	private Long rnum;
	private Long employeeId1;
	
	
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Long getRnum() {
		return rnum;
	}
	public void setRnum(Long rnum) {
		this.rnum = rnum;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getDistributorCode() {
		return distributorCode;
	}
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}
	
	public Long getEmployeeId1() {
		return employeeId1;
	}
	public void setEmployeeId1(Long employeeId1) {
		this.employeeId1 = employeeId1;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	
	
	@Override
	public String toString() {
		return "ResponseExcelDto [region=" + region + ", distributorId=" + distributorId + ", distributorName="
				+ distributorName + ", distributorCode=" + distributorCode + ", employeeId=" + employeeId
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", employeeCode=" + employeeCode
				+ ", productId=" + productId + ", productCode=" + productCode + ", productName=" + productName
				+ ", outletId=" + outletId + ", outletCode=" + outletCode + ", outletName=" + outletName + ", status="
				+ status + ", categoryId=" + categoryId + ", categoryCode=" + categoryCode + ", categoryName="
				+ categoryName + ", categoryDesc=" + categoryDesc + ", rnum=" + rnum + ", employeeId1=" + employeeId1
				+ "]";
	}
	
		
	
}
