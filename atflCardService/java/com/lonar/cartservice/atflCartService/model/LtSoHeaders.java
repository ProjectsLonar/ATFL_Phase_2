package com.lonar.cartservice.atflCartService.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_so_headers")
@JsonInclude(Include.NON_NULL)
public class LtSoHeaders extends BaseClass{
	
	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "HEADER_ID")
//	Long headerId;
	String headerId;
	
	@Column(name = "ORDER_NUMBER")
	String orderNumber;
	
	@Column(name = "ORDER_DATE")
	private Date orderDate;
	
	@Column(name = "OUTLET_ID")
//	Long outletId;
	String outletId;
	
	@Column(name = "DELIVERY_DATE")
	private Date deliveryDate;

	@Column(name = "ADDRESS")
	private String Address;
	
	@Column(name = "LATITUDE")
	private String latitude;
	
	@Column(name = "LONGITUDE")
	private String longitude;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "customer_id")
	private Long customerId;
	
	@Transient
//	Long userId;
	String userId;

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "LtSoHeaders [headerId=" + headerId + ", orderNumber=" + orderNumber + ", orderDate=" + orderDate
				+ ", outletId=" + outletId + ", deliveryDate=" + deliveryDate + ", Address=" + Address + ", latitude="
				+ latitude + ", longitude=" + longitude + ", userId=" + userId + "]";
	}
}
