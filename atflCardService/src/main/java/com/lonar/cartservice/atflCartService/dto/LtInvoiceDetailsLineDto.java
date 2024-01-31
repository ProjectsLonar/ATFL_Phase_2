package com.lonar.cartservice.atflCartService.dto;

public class LtInvoiceDetailsLineDto {

	private String productCode;
	 private String productName;
	 private Long shippedQuantity;
	 private Long ptrPrice;
	 private Long listPrice;
	 private Long ptrBasePrice;
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
	 
	 
}
