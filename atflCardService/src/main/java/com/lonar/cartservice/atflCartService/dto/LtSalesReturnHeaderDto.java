package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LtSalesReturnHeaderDto {

	
	Long salesReturnHeaderId;
	String salesReturnNumber;
	String invoiceNumber;
	String outletId;
	String returnStatus;
	String address;
	String latitude;
	String longitude;
	String returnReason;
	Date salesReturnDate;
	String outletName;
	String beatName;
	String siebelStatus;
	String siebelRemark;
	String outletCode;
	String priceList;
	String test;
	String Status;
	
	
	private List <LtSalesReturnLineDto> ltSalesReturnLineDto;
	
	
	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

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

	public String getSiebelStatus() {
		return siebelStatus;
	}

	public void setSiebelStatus(String siebelStatus) {
		this.siebelStatus = siebelStatus;
	}

	public String getSiebelRemark() {
		return siebelRemark;
	}

	public void setSiebelRemark(String siebelRemark) {
		this.siebelRemark = siebelRemark;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public List<LtSalesReturnLineDto> getLtSalesReturnLineDto() {
		return ltSalesReturnLineDto;
	}

	public void setLtSalesReturnLineDto(List<LtSalesReturnLineDto> ltSalesReturnLineDto) {
		this.ltSalesReturnLineDto = ltSalesReturnLineDto;
	}

	
	@Override
	public String toString() {
		return "LtSalesReturnHeaderDto [salesReturnHeaderId=" + salesReturnHeaderId + ", salesReturnNumber="
				+ salesReturnNumber + ", invoiceNumber=" + invoiceNumber + ", outletId=" + outletId + ", returnStatus="
				+ returnStatus + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", returnReason=" + returnReason + ", salesReturnDate=" + salesReturnDate + ", outletName="
				+ outletName + ", beatName=" + beatName + ", siebelStatus=" + siebelStatus + ", siebelRemark="
				+ siebelRemark + ", outletCode=" + outletCode + ", priceList=" + priceList + ", test=" + test
				+ ", Status=" + Status + ", ltSalesReturnLineDto=" + ltSalesReturnLineDto + "]";
	}
	

}
