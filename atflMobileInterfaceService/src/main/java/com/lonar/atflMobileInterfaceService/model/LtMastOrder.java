package com.lonar.atflMobileInterfaceService.model;

import javax.persistence.Transient;

public class LtMastOrder {
	
	@Transient
	private String orderNumber;
	
	@Transient
	private String outletCode;
	
	@Transient
	private String productCode;
	
	@Transient
	private String quantity;
	
	@Override
	public String toString() {
		return "LtMastOrder [orderNumber=" + orderNumber + ", outletCode=" + outletCode + ", productCode=" + productCode
				+ ", quantity=" + quantity + "]";
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	
	
}
