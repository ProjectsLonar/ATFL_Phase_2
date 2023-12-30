package com.atflMasterManagement.masterservice.reports;

public class SalesOrderLineCountDto {

	private Long totalSalesPersonCount;
	private Long totalOrderCount;
	private Long totalLineItemCount;

	public Long getTotalSalesPersonCount() {
		return totalSalesPersonCount;
	}

	public void setTotalSalesPersonCount(Long totalSalesPersonCount) {
		this.totalSalesPersonCount = totalSalesPersonCount;
	}

	public Long getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(Long totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public Long getTotalLineItemCount() {
		return totalLineItemCount;
	}

	public void setTotalLineItemCount(Long totalLineItemCount) {
		this.totalLineItemCount = totalLineItemCount;
	}

}
