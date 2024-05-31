package com.lonar.cartservice.atflCartService.dto;
 
import java.util.*;
 
public class SoHeaderDtoPendingOrders {
    Long headerId;
    String orderNumber;
    Date orderDate;
    String status;
    String status1;
    String address;
    String outletId;
    String outletName;
    String outletCode;
    String latitude;
    String longitude;
    Long userId;
    String outletAddress;
    String proprietorName;
    Date deliveryDate;
    String instockFlag;
 
    // Constructor, getters, and setters can be generated here
    public SoHeaderDtoPendingOrders() {
		super();
	}
    
	public SoHeaderDtoPendingOrders(Long headerId, String orderNumber, Date orderDate, String status, String status1,
			String address, String outletId, String outletName, String outletCode, String latitude, String longitude,
			Long userId, String outletAddress, String proprietorName, Date deliveryDate, String instockFlag) {
		super();
		this.headerId = headerId;
		this.orderNumber = orderNumber;
		this.orderDate = orderDate;
		this.status = status;
		this.status1 = status1;
		this.address = address;
		this.outletId = outletId;
		this.outletName = outletName;
		this.outletCode = outletCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.userId = userId;
		this.outletAddress = outletAddress;
		this.proprietorName = proprietorName;
		this.deliveryDate = deliveryDate;
		this.instockFlag = instockFlag;
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
 
	public String getStatus1() {
		return status1;
	}
 
	public void setStatus1(String status1) {
		this.status1 = status1;
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
 
	public String getOutletAddress() {
		return outletAddress;
	}
 
	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}
 
	public String getProprietorName() {
		return proprietorName;
	}
 
	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}
 
	public Date getDeliveryDate() {
		return deliveryDate;
	}
 
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
 
	public String getInstockFlag() {
		return instockFlag;
	}
 
	public void setInstockFlag(String instockFlag) {
		this.instockFlag = instockFlag;
	}
 
	// Override equals and hashCode based on headerId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoHeaderDtoPendingOrders soHeaderDtoPendingOrders = (SoHeaderDtoPendingOrders) o;
        return headerId == soHeaderDtoPendingOrders.headerId;
    }
 
	@Override
    public int hashCode() {
        return Objects.hash(headerId);
    }
 
	@Override
	public String toString() {
		return "SoHeaderDtoPendingOrders [headerId=" + headerId + ", orderNumber=" + orderNumber + ", orderDate=" + orderDate
				+ ", status=" + status + ", status1=" + status1 + ", address=" + address + ", outletId=" + outletId
				+ ", outletName=" + outletName + ", outletCode=" + outletCode + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", userId=" + userId + ", outletAddress=" + outletAddress
				+ ", proprietorName=" + proprietorName + ", deliveryDate=" + deliveryDate + ", instockFlag="
				+ instockFlag + "]";
	}
	
	
}