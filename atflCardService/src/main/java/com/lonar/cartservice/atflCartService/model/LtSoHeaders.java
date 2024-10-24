package com.lonar.cartservice.atflCartService.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_so_headers")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class LtSoHeaders extends BaseClass1{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_SO_HEADERS_S")
	@SequenceGenerator(name = "LT_SO_HEADERS_S", sequenceName = "LT_SO_HEADERS_S", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "HEADER_ID")
	private Long headerId;
	
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
	Long userId;
	//String userId;
	
// ATFL Phase2 new development
	@Column(name= "INSTOCK_FLAG")
    private String inStockFlag;	
    
	@Column(name= "BEAT_ID")
	private String beatId;
	
	@Column(name= "PRICE_LIST")
	private String priceList;
	
	@Column(name= "SIEBEL_INVOICENUMBER")
	private String siebelInvoiceNumber;
	
	@Column(name= "SIEBEL_STATUS")
	private String siebelStatus;
	
	@Column(name= "SIEBEL_REMARK")
	private String siebelRemark;
		
	@Column(name= "SIEBEL_JSONPAYLOAD")
	private String siebelJsonpayload;
	
	@Column(name= "DISTRIBUTOR_ID")
	private String distributorId;	
	
	@Column(name= "SALES_PERSON_ID")
	private String salesPersonId;
	
	
	
	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getSiebelJsonpayload() {
		return siebelJsonpayload;
	}

	public void setSiebelJsonpayload(String siebelJsonpayload) {
		this.siebelJsonpayload = siebelJsonpayload;
	}

	public String getSiebelInvoiceNumber() {
		return siebelInvoiceNumber;
	}

	public void setSiebelInvoiceNumber(String siebelInvoiceNumber) {
		this.siebelInvoiceNumber = siebelInvoiceNumber;
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	

	public String getInStockFlag() {
		return inStockFlag;
	}

	public void setInStockFlag(String inStockFlag) {
		this.inStockFlag = inStockFlag;
	}
	
	public String getBeatId() {
		return beatId;
	}

	public void setBeatId(String beatId) {
		this.beatId = beatId;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	@Override
	public String toString() {
		return "LtSoHeaders [headerId=" + headerId + ", orderNumber=" + orderNumber + ", orderDate=" + orderDate
				+ ", outletId=" + outletId + ", deliveryDate=" + deliveryDate + ", Address=" + Address + ", latitude="
				+ latitude + ", longitude=" + longitude + ", remark=" + remark + ", customerId=" + customerId
				+ ", userId=" + userId + ", inStockFlag=" + inStockFlag + ", beatId=" + beatId + ", priceList="
				+ priceList + ", siebelInvoiceNumber=" + siebelInvoiceNumber + ", siebelStatus=" + siebelStatus
				+ ", siebelRemark=" + siebelRemark + ", siebelJsonpayload=" + siebelJsonpayload + ", distributorId="
				+ distributorId + ", salesPersonId=" + salesPersonId + "]";
	}

	
			
}
