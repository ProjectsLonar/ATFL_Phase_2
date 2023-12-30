package com.atflMasterManagement.masterservice.reports;

public class ExcelDataProduct {

	private int srNo;
	private Double amount = 0D;
	private String productName;
	private String productCode;
	private Long quantity = 0L;
	private String ptrPrice;
	private String listPrice;

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

}
