package com.atflMasterManagement.masterservice.reports;

public class ExcelDataSelesperson {
	private Long userId;
	private String Title; 
    private int srNo;
    private String salesPersonName;
    private String employeeCode;
	private String positionCode;
	private String outletName;
	private Double revenue;
	private String outletId;
	private String ptrPrice1;
	private String ptrPrice;
	private Long quantity1;
	private Long quantity;
	private String employeeId;
	private String distributorId;
	private String distributorCode;
	private String distributorName;
	private String position;
	private String outletCode;
	private String distributorCrmCode;
	
	private String outletAddress;
	private String status;
	private String orderDate;
	private String orderNumber;
	private String listPrice1 ;
	private String listPrice ;
	
	private String productCode;
	private String productDesc;
	private String productName;
	
	private Double total = 0D;
	
	private String lastUpdateDate;
	private String lastUpdatedBy;
	
	
	
	public String getPtrPrice() {
		return ptrPrice;
	}
	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public String getListPrice() {
		return listPrice;
	}
	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
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
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	
	public String getPtrPrice1() {
		return ptrPrice1;
	}
	public void setPtrPrice1(String ptrPrice1) {
		this.ptrPrice1 = ptrPrice1;
	}
	public Long getQuantity1() {
		return quantity1;
	}
	public void setQuantity1(Long quantity1) {
		this.quantity1 = quantity1;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getDistributorCode() {
		return distributorCode;
	}
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getOutletAddress() {
		return outletAddress;
	}
	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
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
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getListPrice1() {
		return listPrice1;
	}
	public void setListPrice1(String listPrice1) {
		this.listPrice1 = listPrice1;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}
	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}
	@Override
	public String toString() {
		return "ExcelDataSelesperson [userId=" + userId + ", Title=" + Title + ", srNo=" + srNo + ", salesPersonName="
				+ salesPersonName + ", employeeCode=" + employeeCode + ", positionCode=" + positionCode
				+ ", outletName=" + outletName + ", revenue=" + revenue + ", outletId=" + outletId + ", ptrPrice1="
				+ ptrPrice1 + ", ptrPrice=" + ptrPrice + ", quantity1=" + quantity1 + ", quantity=" + quantity
				+ ", employeeId=" + employeeId + ", distributorId=" + distributorId + ", distributorCode="
				+ distributorCode + ", distributorName=" + distributorName + ", position=" + position + ", outletCode="
				+ outletCode + ", distributorCrmCode=" + distributorCrmCode + ", outletAddress=" + outletAddress
				+ ", status=" + status + ", orderDate=" + orderDate + ", orderNumber=" + orderNumber + ", listPrice1="
				+ listPrice1 + ", listPrice=" + listPrice + ", productCode=" + productCode + ", productDesc="
				+ productDesc + ", productName=" + productName + ", total=" + total + ", lastUpdateDate="
				+ lastUpdateDate + ", lastUpdatedBy=" + lastUpdatedBy + "]";
	}
	
	
	
	
}
