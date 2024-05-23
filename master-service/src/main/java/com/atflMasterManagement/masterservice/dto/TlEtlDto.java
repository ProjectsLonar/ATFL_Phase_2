package com.atflMasterManagement.masterservice.dto;

public class TlEtlDto {

	String priceList;
	String adjGroupName;
	String productName;
	String priAdjTypeCd;
	String adjValAmt;
	String productId;
	
	
	public String getPriceList() {
		return priceList;
	}
	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
	public String getAdjGroupName() {
		return adjGroupName;
	}
	public void setAdjGroupName(String adjGroupName) {
		this.adjGroupName = adjGroupName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPriAdjTypeCd() {
		return priAdjTypeCd;
	}
	public void setPriAdjTypeCd(String priAdjTypeCd) {
		this.priAdjTypeCd = priAdjTypeCd;
	}
	public String getAdjValAmt() {
		return adjValAmt;
	}
	public void setAdjValAmt(String adjValAmt) {
		this.adjValAmt = adjValAmt;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	@Override
	public String toString() {
		return "TlEtlDto [priceList=" + priceList + ", adjGroupName=" + adjGroupName + ", productName=" + productName
				+ ", priAdjTypeCd=" + priAdjTypeCd + ", adjValAmt=" + adjValAmt + ", productId=" + productId + "]";
	}
	
	
}
