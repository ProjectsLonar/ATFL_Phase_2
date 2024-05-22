package com.atflMasterManagement.masterservice.dto;

import java.util.Date;

public class DailyAndMonthlyDto {

	private Date creationDate;
	private String listPrice;
	private Long ptrPrice;
	private Long quantity;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	public Long getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(Long ptrPrice) {
		this.ptrPrice = ptrPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	
	@Override
	public String toString() {
		return "DailyAndMonthlyDto [creationDate=" + creationDate + ", listPrice=" + listPrice + ", ptrPrice="
				+ ptrPrice + ", quantity=" + quantity + "]";
	}

	
}
