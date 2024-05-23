package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;

public class LtSalesReturnResponseDto {

	// Header Date
	Long salesReturnHeaderId;
	String salesReturnNumber;
	String invoiceNumber;
	String outletId;
	String returnStatus;
	String returnReason;
	Date salesReturnDate;
	String outletName;
	String beatName;
	String priceList;
	String status;
		
	//Line Data
	String  productId;
	Long  shippedQuantity;
	Long  returnQuantity;
	Long  remainingQuantity;
	String availability;
	String location;
	Double price;
	Double totalPrice;
	String productName;
	String lotNumber;
	String status1;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSalesReturnHeaderId() {
		return salesReturnHeaderId;
	}
	public void setSalesReturnHeaderId(Long salesReturnHeaderId) {
		this.salesReturnHeaderId = salesReturnHeaderId;
	}
	public String getSalesReturnNumber() {
		return salesReturnNumber;
	}
	public void setSalesReturnNumber(String salesReturnNumber) {
		this.salesReturnNumber = salesReturnNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	public String getReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Date getSalesReturnDate() {
		return salesReturnDate;
	}
	public void setSalesReturnDate(Date salesReturnDate) {
		this.salesReturnDate = salesReturnDate;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getBeatName() {
		return beatName;
	}
	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}
	public String getPriceList() {
		return priceList;
	}
	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Long getShippedQuantity() {
		return shippedQuantity;
	}
	public void setShippedQuantity(Long shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}
	public Long getReturnQuantity() {
		return returnQuantity;
	}
	public void setReturnQuantity(Long returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	public Long getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(Long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getStatus1() {
		return status1;
	}
	public void setStatus1(String status1) {
		this.status1 = status1;
	}
	
	
	@Override
	public String toString() {
		return "LtSalesReturnResponseDto [salesReturnHeaderId=" + salesReturnHeaderId + ", salesReturnNumber="
				+ salesReturnNumber + ", invoiceNumber=" + invoiceNumber + ", outletId=" + outletId + ", returnStatus="
				+ returnStatus + ", returnReason=" + returnReason + ", salesReturnDate=" + salesReturnDate
				+ ", outletName=" + outletName + ", beatName=" + beatName + ", priceList=" + priceList + ", status="
				+ status + ", productId=" + productId + ", shippedQuantity=" + shippedQuantity + ", returnQuantity="
				+ returnQuantity + ", remainingQuantity=" + remainingQuantity + ", availability=" + availability
				+ ", location=" + location + ", price=" + price + ", totalPrice=" + totalPrice + ", productName="
				+ productName + ", lotNumber=" + lotNumber + ", status1=" + status1 + "]";
	}
	
	
}
