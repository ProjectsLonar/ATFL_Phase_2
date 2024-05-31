package com.atflMasterManagement.masterservice.reports;

public class ExcelDataProduct {

	private int srNo;
	private Double amount = 0D;
	private String productName;
	private String productCode;
	private Long quantity1 = 0L;
	private String ptrPrice1;
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

	public Long getQuantity1() {
		return quantity1;
	}

	public void setQuantity1(Long quantity1) {
		this.quantity1 = quantity1;
	}

	public String getPtrPrice1() {
		return ptrPrice1;
	}

	public void setPtrPrice1(String ptrPrice1) {
		this.ptrPrice1 = ptrPrice1;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	@Override
	public String toString() {
		return "ExcelDataProduct [srNo=" + srNo + ", amount=" + amount + ", productName=" + productName
				+ ", productCode=" + productCode + ", quantity1=" + quantity1 + ", ptrPrice1=" + ptrPrice1 + ", listPrice="
				+ listPrice + "]";
	}
	

}
