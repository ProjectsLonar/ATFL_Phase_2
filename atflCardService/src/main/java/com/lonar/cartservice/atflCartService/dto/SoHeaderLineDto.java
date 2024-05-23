package com.lonar.cartservice.atflCartService.dto;

import java.sql.Clob;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SoHeaderLineDto {

	private Long headerId;
	private String orderNumber;
	private Date orderDate;
	private String status;
	private String outletId;
	private String outletCode;
	private Date deliveryDate;
	private String latitude;
	private String longitude;
	private Long userId;
	private String priceList;
	private String beatId;
	private String siebelInvoicenumber;
	
	private String distributorName;
	 private String distributorCode;
	 private String distributorId;
	 private String outletName;
	 private String invoiceNumber;
	 private Date invoiceDate;
	 private String location;
	 private Long totalAmount;
	 private String priceListId;
	 private String priceListName;
	 
	 private String beatName;
	 private String inventoryId; 
	
	// Line data
	
	private Long lineId;
	private String productId;
	private Long quantity;
	private String listPrice;
	private String ptrPrice;
	private String headerPriceList1;
	private Long headerId1;
		
	
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
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
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
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
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
	public String getSiebelInvoicenumber() {
		return siebelInvoicenumber;
	}
	public void setSiebelInvoicenumber(String siebelInvoicenumber) {
		this.siebelInvoicenumber = siebelInvoicenumber;
	}
	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public String getListPrice() {
		return listPrice;
	}
	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}
	public String getPtrPrice() {
		return ptrPrice;
	}
	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}
	public String getHeaderPriceList1() {
		return headerPriceList1;
	}
	public void setHeaderPriceList1(String headerPriceList1) {
		this.headerPriceList1 = headerPriceList1;
	}
	public Long getHeaderId1() {
		return headerId1;
	}
	public void setHeaderId1(Long headerId1) {
		this.headerId1 = headerId1;
	}
	
	
	@Override
	public String toString() {
		return "SoHeaderLineDto [headerId=" + headerId + ", orderNumber=" + orderNumber + ", orderDate=" + orderDate
				+ ", status=" + status + ", outletId=" + outletId + ", outletCode=" + outletCode + ", deliveryDate="
				+ deliveryDate + ", latitude=" + latitude + ", longitude=" + longitude + ", userId=" + userId
				+ ", priceList=" + priceList + ", beatId=" + beatId + ", siebelInvoicenumber=" + siebelInvoicenumber
				+ ", distributorName=" + distributorName + ", distributorCode=" + distributorCode + ", distributorId="
				+ distributorId + ", outletName=" + outletName + ", invoiceNumber=" + invoiceNumber + ", invoiceDate="
				+ invoiceDate + ", location=" + location + ", totalAmount=" + totalAmount + ", priceListId="
				+ priceListId + ", priceListName=" + priceListName + ", beatName=" + beatName + ", inventoryId="
				+ inventoryId + ", lineId=" + lineId + ", productId=" + productId + ", quantity=" + quantity
				+ ", listPrice=" + listPrice + ", ptrPrice=" + ptrPrice + ", headerPriceList1=" + headerPriceList1
				+ ", headerId1=" + headerId1 + "]";
	}
	
	
}
