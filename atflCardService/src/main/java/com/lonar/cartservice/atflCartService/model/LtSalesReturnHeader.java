package com.lonar.cartservice.atflCartService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_SALES_RETURN_HEADERS")
@JsonInclude(Include.NON_NULL)
public class LtSalesReturnHeader extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LT_SALES_RETURN_HEADERS_S")
	@SequenceGenerator(name = "LT_SALES_RETURN_HEADERS_S", sequenceName = "LT_SALES_RETURN_HEADERS_S", allocationSize = 1)
	@Column(name = "SALES_RETURN_HEADER_ID")
	Long salesReturnHeaderId;
	
	@Column(name = "SALES_RETURN_NUMBER")
	String salesReturnNumber;
	
	@Column(name = "INVOICE_NUMBER")
	String invoiceNumber;
	
	@Column(name = "OUTLET_ID")
	String outletId;
	

	@Column(name = "RETURN_STATUS")
	String returnStatus;
	
	@Column(name = "ADDRESS")
	String address;
	
	@Column(name = "LATITUDE")
	String latitude;
	
	@Column(name = "LONGITUDE")
	String longitude;
	
	@Column(name = "RETURN_REASON")
	String returnReason;
	
	@Column(name = "SALES_RETURN_DATE")
	Date salesReturnDate;
	
	@Column(name = "OUTLET_NAME")
	String  outletName;
	
	@Column(name = "BEAT_NAME")
	String  beatName;
	
	@Column(name = "SIEBEL_STATUS")
	String siebelStatus;
	
	@Column(name = "SIEBEL_REMARK")
	String siebelRemark;
	
	@Column(name = "DISTRIBUTOR_ID")
	String distributorId;
	
	@Transient
	private String outletCode;
	
	@Column(name = "PRICE_LIST")
	private String priceList;
	
	@Column(name = "TEST")
	private String test;
	
	
	@Column(name = "SIEBEL_JSONPAYLOAD")
	private String siebelJsonpayload;
	
	@Transient
	private Long userId;
	
	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSiebelJsonpayload() {
		return siebelJsonpayload;
	}

	public void setSiebelJsonpayload(String siebelJsonpayload) {
		this.siebelJsonpayload = siebelJsonpayload;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
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

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
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

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	
	@Override
	public String toString() {
		return "LtSalesReturnHeader [salesReturnHeaderId=" + salesReturnHeaderId + ", salesReturnNumber="
				+ salesReturnNumber + ", invoiceNumber=" + invoiceNumber + ", outletId=" + outletId + ", returnStatus="
				+ returnStatus + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", returnReason=" + returnReason + ", salesReturnDate=" + salesReturnDate + ", outletName="
				+ outletName + ", beatName=" + beatName + ", siebelStatus=" + siebelStatus + ", siebelRemark="
				+ siebelRemark + ", distributorId=" + distributorId + ", outletCode=" + outletCode + ", priceList="
				+ priceList + ", test=" + test + ", siebelJsonpayload=" + siebelJsonpayload + ", userId=" + userId
				+ "]";
	}
		
}
