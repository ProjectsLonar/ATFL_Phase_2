package com.lonar.cartservice.atflCartService.dto;

public class QuantityCheck {
	
	private long quantity;
	private String productId;
	private String distId;
	
	public QuantityCheck() {
		super();
	}
 
	public QuantityCheck(long quantity, String productId, String distId) {
		super();
		this.quantity = quantity;
		this.productId = productId;
		this.distId = distId;
	}
 
	public long getQuantity() {
		return quantity;
	}
 
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
 
	public String getProductId() {
		return productId;
	}
 
	public void setProductId(String productId) {
		this.productId = productId;
	}
 
	public String getDistId() {
		return distId;
	}
 
	public void setDistId(String distId) {
		this.distId = distId;
	}
 
	@Override
	public String toString() {
		return "QuantityCheck [quantity=" + quantity + ", productId=" + productId + ", distId=" + distId
				+ "]";
	}
	
	
 
}
