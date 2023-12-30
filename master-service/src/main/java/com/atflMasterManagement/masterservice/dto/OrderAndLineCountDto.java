package com.atflMasterManagement.masterservice.dto;

public class OrderAndLineCountDto {
	private String totalOrderCount;
	private String totalLineItemCount;
	private String salesPersonsCount;

	public String getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(String totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public String getTotalLineItemCount() {
		return totalLineItemCount;
	}

	public void setTotalLineItemCount(String totalLineItemCount) {
		this.totalLineItemCount = totalLineItemCount;
	}

	public String getSalesPersonsCount() {
		return salesPersonsCount;
	}

	public void setSalesPersonsCount(String salesPersonsCount) {
		this.salesPersonsCount = salesPersonsCount;
	}

}
