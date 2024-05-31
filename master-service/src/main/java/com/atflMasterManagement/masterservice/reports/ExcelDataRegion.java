package com.atflMasterManagement.masterservice.reports;

public class ExcelDataRegion {
	private int srNo;
	private Double revenue = 0D;
	private String region;
	private String ptrPrice;
	private Long quantity;
	private String distributorName;
	private String distributorCode;
	private String distributorCrmCode;
	private Long totalEff;
	private Long dbc;
	private Long tls;
	private String distributorId;
	private String listPrice;

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
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

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}

	public Long getTotalEff() {
		return totalEff;
	}

	public void setTotalEff(Long totalEff) {
		this.totalEff = totalEff;
	}

	public Long getDbc() {
		return dbc;
	}

	public void setDbc(Long dbc) {
		this.dbc = dbc;
	}

	public Long getTls() {
		return tls;
	}

	public void setTls(Long tls) {
		this.tls = tls;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	
	@Override
	public String toString() {
		return "ExcelDataRegion [srNo=" + srNo + ", revenue=" + revenue + ", region=" + region + ", ptrPrice="
				+ ptrPrice + ", quantity=" + quantity + ", distributorName=" + distributorName + ", distributorCode="
				+ distributorCode + ", distributorCrmCode=" + distributorCrmCode + ", totalEff=" + totalEff + ", dbc="
				+ dbc + ", tls=" + tls + ", distributorId=" + distributorId + ", listPrice=" + listPrice + "]";
	}

	
}
