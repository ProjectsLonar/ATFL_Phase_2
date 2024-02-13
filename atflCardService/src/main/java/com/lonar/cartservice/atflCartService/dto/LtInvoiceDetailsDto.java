package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;
import java.util.List;

public class LtInvoiceDetailsDto {

	private String distributorName;
	 private String distributorCode;
	 private String distributorId;
	 private String outletName;
	 private String outletCode;
	 private String orderNumber;
	 private String invoiveNumber;
	 private Date invoiceDate;
	 private String location;
	 private Long totalAmount;
	 private String priceListId;
	 private String priceListName;
	 
	 private String beatName;
	 
	 private List <LtInvoiceDetailsLineDto> ltInvoiceDetailsLineDto;

	 	 
	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
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

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
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

	public String getInvoiveNumber() {
		return invoiveNumber;
	}

	public void setInvoiveNumber(String invoiveNumber) {
		this.invoiveNumber = invoiveNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<LtInvoiceDetailsLineDto> getLtInvoiceDetailsLineDto() {
		return ltInvoiceDetailsLineDto;
	}

	public void setLtInvoiceDetailsLineDto(List<LtInvoiceDetailsLineDto> ltInvoiceDetailsLineDto) {
		this.ltInvoiceDetailsLineDto = ltInvoiceDetailsLineDto;
	}

	public String getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(String priceListId) {
		this.priceListId = priceListId;
	}

	public String getPriceListName() {
		return priceListName;
	}

	public void setPriceListName(String priceListName) {
		this.priceListName = priceListName;
	}
	 
	 
	
}
