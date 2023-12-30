package com.atflMasterManagement.masterservice.reports;

public class ExcelTableData {

	private int srNo;
	private String Revenue;
	
	//---sales person performance
	private String salesPersonName;
	private String positionCode;
	private String outletName;
	private String ouletCode;
	
	//---region wise sales report
	private String region;
	private String distributorName;
	private String distributorCode;
	
	//---product Wise revenue summary report
	private String productName;
	private String productCode;
	private String productQty;
	private String ptrPrice;
	
	//---outletwise order summary report
	private String  orderNo;
	private String status;
	private String orderDate;
	//---distributor wise summary report
	private String categoryName;
	private String categoryCode;
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public String getRevenue() {
		return Revenue;
	}
	public void setRevenue(String revenue) {
		Revenue = revenue;
	}
	public String getSalesPersonName() {
		return salesPersonName;
	}
	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getOuletCode() {
		return ouletCode;
	}
	public void setOuletCode(String ouletCode) {
		this.ouletCode = ouletCode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductQty() {
		return productQty;
	}
	public void setProductQty(String productQty) {
		this.productQty = productQty;
	}
	public String getPtrPrice() {
		return ptrPrice;
	}
	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	
}
