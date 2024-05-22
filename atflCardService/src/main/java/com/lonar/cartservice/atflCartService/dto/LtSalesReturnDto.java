package com.lonar.cartservice.atflCartService.dto;

import java.sql.Clob;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;

@JsonInclude(Include.NON_NULL)
public class LtSalesReturnDto {
	private Long salesReturnHeaderId;
	private String salesReturnNumber;
	private String invoiceNumber;
	private String outletId;
	private String returnStatus;
	private String address;
	private String latitude;
	private String longitude;
	private String returnReason;
	private Date salesReturnDate;
	private String outletName;
	private String outletCode;
	private Long userId;
	private Double totalSalesreturnAmount;
	private Double price;
	private String beatName;
	
	String priceList;
	//String siebelStatus;
	//Clob siebelRemark; 
	
	String orderNumber;
	String inventoryId;
	
	String test;
	String status;
	
			
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

//	public String getSiebelStatus() {
//		return siebelStatus;
//	}
//
//	public void setSiebelStatus(String siebelStatus) {
//		this.siebelStatus = siebelStatus;
//	}
//
//	public Clob getSiebelRemark() {
//		return siebelRemark;
//	}
//
//	public void setSiebelRemark(Clob siebelRemark) {
//		this.siebelRemark = siebelRemark;
//	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPricelist(String priceList) {
		this.priceList = priceList;
	}

	List<LtSalesReturnLines> ltSalesReturnLines;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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

	public List<LtSalesReturnLines> getLtSalesReturnLines() {
		return ltSalesReturnLines;
	}

	public void setLtSalesReturnLines(List<LtSalesReturnLines> ltSalesReturnLines) {
		this.ltSalesReturnLines = ltSalesReturnLines;
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

	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

	public Double getTotalSalesreturnAmount() {
		return totalSalesreturnAmount;
	}

	public void setTotalSalesreturnAmount(Double totalSalesreturnAmount) {
		this.totalSalesreturnAmount = totalSalesreturnAmount;
	}
	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	
	@Override
	public String toString() {
		return "LtSalesReturnDto [salesReturnHeaderId=" + salesReturnHeaderId + ", salesReturnNumber="
				+ salesReturnNumber + ", invoiceNumber=" + invoiceNumber + ", outletId=" + outletId + ", returnStatus="
				+ returnStatus + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", returnReason=" + returnReason + ", salesReturnDate=" + salesReturnDate + ", outletName="
				+ outletName + ", outletCode=" + outletCode + ", userId=" + userId + ", totalSalesreturnAmount="
				+ totalSalesreturnAmount + ", price=" + price + ", beatName=" + beatName + ", priceList=" + priceList
				+ ", orderNumber=" + orderNumber + ", inventoryId=" + inventoryId + ", test=" + test + ", status="
				+ status + ", ltSalesReturnLines=" + ltSalesReturnLines + "]";
	}


}
