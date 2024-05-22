package com.lonar.cartservice.atflCartService.dto;

import java.sql.Clob;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class SoHeaderDto {
	private Long headerId;
	private String orderNumber;
	private String orderDate;
	private String status;
	private String address;
	private String outletId;
	private String outletName;
	private String outletCode;
	private String outletAddress;
	private Long customerId;
	private String proprietorName;
	
	private Date deliveryDate;
	private String latitude;
	private String longitude;
	private Long userId;
	private String remark;
	private String address1;
	private String city;	
	
	private String distributorName;
	 private String distributorCode;
	 private String distributorId;
	 private String invoiceNumber;
	 private Date invoiceDate;
	 private Long totalAmount;
	 private String priceListId;
	 private String priceListName;
	 
	 private String beatName;
	 
	
	
	private List<SoLineDto> soLineDtoList;

	
	//ATFL phase 2 new development
	
	private String priceList;
	private String beatId;
	private String instockFlag;
	private String headerPriceList;
	private int createdBy;
	
	
	private String siebelStatus;
	//private Clob siebelRemark;
	private String siebelRemark;
	private String siebelInvoicenumber;
	private String siebelJsonpayload;
	
	private Date orderDate1;
	private String inventoryId; 
	 private String location;
         
	
	
	 
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

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getOrderDate1() {
		return orderDate1;
	}

	public void setOrderDate1(Date orderDate1) {
		this.orderDate1 = orderDate1;
	}

	public String getSiebelJsonpayload() {
		return siebelJsonpayload;
	}

	public void setSiebelJsonpayload(String siebelJsonpayload) {
		this.siebelJsonpayload = siebelJsonpayload;
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

	public String getSiebelInvoicenumber() {
		return siebelInvoicenumber;
	}

	public void setSiebelInvoicenumber(String siebelInvoicenumber) {
		this.siebelInvoicenumber = siebelInvoicenumber;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getHeaderPriceList() {
		return headerPriceList;
	}

	public void setHeaderPriceList(String headerPriceList) {
		this.headerPriceList = headerPriceList;
	}

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public List<SoLineDto> getSoLineDtoList() {
		return soLineDtoList;
	}

	public void setSoLineDtoList(List<SoLineDto> soLineDtoList) {
		this.soLineDtoList = soLineDtoList;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getProprietorName() {
		return proprietorName;
	}

	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getBeatId() {
		return beatId;
	}

	public void setBeatId(String beatId) {
		this.beatId = beatId;
	}

	public String getInstockFlag() {
		return instockFlag;
	}

	public void setInstockFlag(String instockFlag) {
		this.instockFlag = instockFlag;
	}

	
	@Override
	public String toString() {
		return "SoHeaderDto [headerId=" + headerId + ", orderNumber=" + orderNumber + ", orderDate=" + orderDate
				+ ", status=" + status + ", address=" + address + ", outletId=" + outletId + ", outletName="
				+ outletName + ", outletCode=" + outletCode + ", outletAddress=" + outletAddress + ", customerId="
				+ customerId + ", proprietorName=" + proprietorName + ", deliveryDate=" + deliveryDate + ", latitude="
				+ latitude + ", longitude=" + longitude + ", userId=" + userId + ", remark=" + remark + ", address1="
				+ address1 + ", city=" + city + ", distributorName=" + distributorName + ", distributorCode="
				+ distributorCode + ", distributorId=" + distributorId + ", invoiceNumber=" + invoiceNumber
				+ ", invoiceDate=" + invoiceDate + ", totalAmount=" + totalAmount + ", priceListId=" + priceListId
				+ ", priceListName=" + priceListName + ", beatName=" + beatName + ", soLineDtoList=" + soLineDtoList
				+ ", priceList=" + priceList + ", beatId=" + beatId + ", instockFlag=" + instockFlag
				+ ", headerPriceList=" + headerPriceList + ", createdBy=" + createdBy + ", siebelStatus=" + siebelStatus
				+ ", siebelRemark=" + siebelRemark + ", siebelInvoicenumber=" + siebelInvoicenumber
				+ ", siebelJsonpayload=" + siebelJsonpayload + ", orderDate1=" + orderDate1 + ", inventoryId="
				+ inventoryId + ", location=" + location + "]";
	}

	
	
}
