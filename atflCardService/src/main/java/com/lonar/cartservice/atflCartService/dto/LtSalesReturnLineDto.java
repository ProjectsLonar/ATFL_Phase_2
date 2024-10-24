package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class LtSalesReturnLineDto {
	 
	Long salesReturnLineId;
	Long salesReturnHeaderId;
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
	String status;
	String status1;
	
	String productDesc;
	
	
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getStatus1() {
		return status1;
	}
	public void setStatus1(String status1) {
		this.status1 = status1;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSalesReturnLineId() {
		return salesReturnLineId;
	}
	public void setSalesReturnLineId(Long salesReturnLineId) {
		this.salesReturnLineId = salesReturnLineId;
	}
	public Long getSalesReturnHeaderId() {
		return salesReturnHeaderId;
	}
	public void setSalesReturnHeaderId(Long salesReturnHeaderId) {
		this.salesReturnHeaderId = salesReturnHeaderId;
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
	
	
	@Override
	public String toString() {
		return "LtSalesReturnLineDto [salesReturnLineId=" + salesReturnLineId + ", salesReturnHeaderId="
				+ salesReturnHeaderId + ", productId=" + productId + ", shippedQuantity=" + shippedQuantity
				+ ", returnQuantity=" + returnQuantity + ", remainingQuantity=" + remainingQuantity + ", availability="
				+ availability + ", location=" + location + ", price=" + price + ", totalPrice=" + totalPrice
				+ ", productName=" + productName + ", lotNumber=" + lotNumber + ", status=" + status + ", status1="
				+ status1 + ", productDesc=" + productDesc + "]";
	}
	
	
}
