package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;
import java.util.List;

public class SalesReturnResponse {

	// Header Date
		Long salesReturnHeaderId;
		String salesReturnNumber;
		String invoiceNumber;
		String outletId;
		String returnStatus;
		String returnReason;
		Date salesReturnDate;
		String outletName;
		String beatName;
		String priceList;
		String status;
	
	List<LtSalesReturnLineDto> LtSalesReturnLineDto;

	public Long getSalesReturnHeaderId() {
		return salesReturnHeaderId;
	}

	public void setSalesReturnHeaderId(Long salesReturnHeaderId) {
		this.salesReturnHeaderId = salesReturnHeaderId;
	}

	public String getSalesReturnNumber() {
		return salesReturnNumber;
	}

	public void setSalesReturnNumber(String salesReturnNumber) {
		this.salesReturnNumber = salesReturnNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public Date getSalesReturnDate() {
		return salesReturnDate;
	}

	public void setSalesReturnDate(Date salesReturnDate) {
		this.salesReturnDate = salesReturnDate;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<LtSalesReturnLineDto> getLtSalesReturnLineDto() {
		return LtSalesReturnLineDto;
	}

	public void setLtSalesReturnLineDto(List<LtSalesReturnLineDto> ltSalesReturnLineDto) {
		LtSalesReturnLineDto = ltSalesReturnLineDto;
	}

	
	@Override
	public String toString() {
		return "SalesReturnResponse [salesReturnHeaderId=" + salesReturnHeaderId + ", salesReturnNumber="
				+ salesReturnNumber + ", invoiceNumber=" + invoiceNumber + ", outletId=" + outletId + ", returnStatus="
				+ returnStatus + ", returnReason=" + returnReason + ", salesReturnDate=" + salesReturnDate
				+ ", outletName=" + outletName + ", beatName=" + beatName + ", priceList=" + priceList + ", status="
				+ status + ", LtSalesReturnLineDto=" + LtSalesReturnLineDto + "]";
	}
	
	
}
