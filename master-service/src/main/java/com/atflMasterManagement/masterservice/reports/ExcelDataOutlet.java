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

}
