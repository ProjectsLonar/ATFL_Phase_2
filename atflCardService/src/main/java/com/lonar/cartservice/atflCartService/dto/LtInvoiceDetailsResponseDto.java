package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;

public class LtInvoiceDetailsResponseDto {
 private String distributorName;
 private String distributorCode;
 private String distributorId;
 private String outletName;
 private String outletCode;
 private String orderNumber;
 private String productCode;
 private String productName;
 private Long shippedQuantity;
 private Long ptrPrice;
 private Long listPrice;
 private Long ptrBasePrice;
 private String invoiveNumber;
 private Date invoiceDate;
 private String location;
 private Long totalAmount;
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
public String getDistributorId() {
	return distributorId;
}
public void setDistributorId(String distributorId) {
	this.distributorId = distributorId;
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

public String getOrderNumber() {
	return orderNumber;
}
public void setOrderNumber(String orderNumber) {
	this.orderNumber = orderNumber;
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
public Long getShippedQuantity() {
	return shippedQuantity;
}
public void setShippedQuantity(Long shippedQuantity) {
	this.shippedQuantity = shippedQuantity;
}
public Long getPtrPrice() {
	return ptrPrice;
}
public void setPtrPrice(Long ptrPrice) {
	this.ptrPrice = ptrPrice;
}
public Long getListPrice() {
	return listPrice;
}
public void setListPrice(Long listPrice) {
	this.listPrice = listPrice;
}
public Long getPtrBasePrice() {
	return ptrBasePrice;
}
public void setPtrBasePrice(Long ptrBasePrice) {
	this.ptrBasePrice = ptrBasePrice;
}
public String getInvoiveNumber() {
	return invoiveNumber;
}
public void setInvoiveNumber(String invoiveNumber) {
	this.invoiveNumber = invoiveNumber;
}
public Date getInvoiceDate() {
	return invoiceDate;
}
public void setInvoiceDate(Date invoiceDate) {
	this.invoiceDate = invoiceDate;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public Long getTotalAmount() {
	return totalAmount;
}
public void setTotalAmount(Long totalAmount) {
	this.totalAmount = totalAmount;
}
@Override
public String toString() {
	return "LtInvoiceDetailsResponseDto [distributorName=" + distributorName + ", distributorCode=" + distributorCode
			+ ", distributorId=" + distributorId + ", outletName=" + outletName + ", outletCode=" + outletCode
			+ ", orderNumber=" + orderNumber + ", productCode=" + productCode + ", productName=" + productName
			+ ", shippedQuantity=" + shippedQuantity + ", ptrPrice=" + ptrPrice + ", listPrice=" + listPrice
			+ ", ptrBasePrice=" + ptrBasePrice + ", invoiveNumber=" + invoiveNumber + ", invoiceDate=" + invoiceDate
			+ ", location=" + location + ", totalAmount=" + totalAmount + "]";
}

}
