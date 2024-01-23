package com.lonar.cartservice.atflCartService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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

	@Override
	public String toString() {
		return "LtSalesReturnLines [salesReturnLineId=" + salesReturnLineId + ", salesReturnHeaderId="
				+ salesReturnHeaderId + ", productId=" + productId + ", shippedQuantity=" + shippedQuantity
				+ ", returnQuantity=" + returnQuantity + ", remainingQuantity=" + remainingQuantity + ", availability="
				+ availability + ", location=" + location + "]";
	}

	
	
	
}
