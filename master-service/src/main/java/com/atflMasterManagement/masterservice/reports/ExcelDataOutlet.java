package com.atflMasterManagement.masterservice.reports;

public class ExcelDataOutlet {

	private int srNo;
	private String outletId;
	private String outletName;
	private String outletCode;
	private String orderNumber;
	private String status;
	private String creationDate;
	private String ptrPrice;
	private Long quantity;
	private Double amount = 0D;
	private String distributorName;
	private String distributorCrmCode;

	private String categoryCode;
	private String categoryDesc;

	private Double edibleOils = 0D;
	private Double readyToCook = 0D;
	private Double chocolateyConfectionery = 0D;
	private Double readyToEat = 0D;
	private Double spreads = 0D;
	private Double cerealSnacks = 0D;
	private String listPrice;
	
	private String amount1;
	private String edibleOils1;
	private String readyToCook1;
	private String chocolateyConfectionery1;
	private String readyToEat1;
	private String spreads1;
	private String cerealSnacks1;
	

	
	
	
	public String getEdibleOils1() {
		return edibleOils1;
	}

	public void setEdibleOils1(String edibleOils1) {
		this.edibleOils1 = edibleOils1;
	}

	public String getReadyToCook1() {
		return readyToCook1;
	}

	public void setReadyToCook1(String readyToCook1) {
		this.readyToCook1 = readyToCook1;
	}

	public String getChocolateyConfectionery1() {
		return chocolateyConfectionery1;
	}

	public void setChocolateyConfectionery1(String chocolateyConfectionery1) {
		this.chocolateyConfectionery1 = chocolateyConfectionery1;
	}

	public String getReadyToEat1() {
		return readyToEat1;
	}

	public void setReadyToEat1(String readyToEat1) {
		this.readyToEat1 = readyToEat1;
	}

	public String getSpreads1() {
		return spreads1;
	}

	public void setSpreads1(String spreads1) {
		this.spreads1 = spreads1;
	}

	public String getCerealSnacks1() {
		return cerealSnacks1;
	}

	public void setCerealSnacks1(String cerealSnacks1) {
		this.cerealSnacks1 = cerealSnacks1;
	}

	public String getAmount1() {
		return amount1;
	}

	public void setAmount1(String amount1) {
		this.amount1 = amount1;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public Double getEdibleOils() {
		return edibleOils;
	}

	public void setEdibleOils(Double edibleOils) {
		this.edibleOils = edibleOils;
	}

	public Double getReadyToCook() {
		return readyToCook;
	}

	public void setReadyToCook(Double readyToCook) {
		this.readyToCook = readyToCook;
	}

	public Double getChocolateyConfectionery() {
		return chocolateyConfectionery;
	}

	public void setChocolateyConfectionery(Double chocolateyConfectionery) {
		this.chocolateyConfectionery = chocolateyConfectionery;
	}

	public Double getReadyToEat() {
		return readyToEat;
	}

	public void setReadyToEat(Double readyToEat) {
		this.readyToEat = readyToEat;
	}

	public Double getSpreads() {
		return spreads;
	}

	public void setSpreads(Double spreads) {
		this.spreads = spreads;
	}

	public Double getCerealSnacks() {
		return cerealSnacks;
	}

	public void setCerealSnacks(Double cerealSnacks) {
		this.cerealSnacks = cerealSnacks;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}

	
	@Override
	public String toString() {
		return "ExcelDataOutlet [srNo=" + srNo + ", outletId=" + outletId + ", outletName=" + outletName
				+ ", outletCode=" + outletCode + ", orderNumber=" + orderNumber + ", status=" + status
				+ ", creationDate=" + creationDate + ", ptrPrice=" + ptrPrice + ", quantity=" + quantity + ", amount="
				+ amount + ", distributorName=" + distributorName + ", distributorCrmCode=" + distributorCrmCode
				+ ", categoryCode=" + categoryCode + ", categoryDesc=" + categoryDesc + ", edibleOils=" + edibleOils
				+ ", readyToCook=" + readyToCook + ", chocolateyConfectionery=" + chocolateyConfectionery
				+ ", readyToEat=" + readyToEat + ", spreads=" + spreads + ", cerealSnacks=" + cerealSnacks
				+ ", listPrice=" + listPrice + ", amount1=" + amount1 + ", edibleOils1=" + edibleOils1
				+ ", readyToCook1=" + readyToCook1 + ", chocolateyConfectionery1=" + chocolateyConfectionery1
				+ ", readyToEat1=" + readyToEat1 + ", spreads1=" + spreads1 + ", cerealSnacks1=" + cerealSnacks1 + "]";
	}

	
			
}
