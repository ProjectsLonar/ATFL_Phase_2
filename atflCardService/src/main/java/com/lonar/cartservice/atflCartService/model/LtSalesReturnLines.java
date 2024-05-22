package com.lonar.cartservice.atflCartService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_SALES_RETURN_LINES")
@JsonInclude(Include.NON_NULL)
public class LtSalesReturnLines extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LT_SALES_RETURN_LINES_S")
	@SequenceGenerator(name = "LT_SALES_RETURN_LINES_S", sequenceName = "LT_SALES_RETURN_LINES_S", allocationSize = 1)
	@Column(name = "SALES_RETURN_LINE_ID")
	Long salesReturnLineId;
	
	@Column(name = "SALES_RETURN_HEADER_ID")
	Long salesReturnHeaderId;
	
	@Column(name = "PRODUCT_ID")
	String  productId;
	
	
	@Column(name = "SHIPPED_QUANTITY")
	Long  shippedQuantity;
	
	@Column(name = "RETURN_QUANTITY")
	Long  returnQuantity;
	
	@Column(name = "REMAINING_QUANTITY")
	Long  remainingQuantity;
	
	@Column(name = "AVAILABILITY")
	String availability;
	
	@Column(name = "LOCATION")
	String location;
	
	@Column(name = "PRICE")
	Double price;
	
	@Column(name = "TOTAL_PRICE")
	Double totalPrice;
	
	@Transient
	String productName;
	
	@Column(name = "LOT_NUMBER")
	String lotNumber;

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

	@Override
	public String toString() {
		return "LtSalesReturnLines [salesReturnLineId=" + salesReturnLineId + ", salesReturnHeaderId="
				+ salesReturnHeaderId + ", productId=" + productId + ", shippedQuantity=" + shippedQuantity
				+ ", returnQuantity=" + returnQuantity + ", remainingQuantity=" + remainingQuantity + ", availability="
				+ availability + ", location=" + location + ", price=" + price + ", totalPrice=" + totalPrice
				+ ", productName=" + productName + ", lotNumber=" + lotNumber + "]";
	}	
	
	
}
