package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

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
@Table(name = "LT_SO_LINES")
@JsonInclude(Include.NON_NULL)
public class LtSoLines  extends BaseClass{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Line_Id")
	Long lineId;
	
	@Column(name = "Header_id")
	Long headerId;
	
	@Column(name = "Product_Id")
	Long productId;
	
	@Column(name = "Quantity")
	Long quantity;
	
	@Column(name = "List_Price")
	String listPrice;
	
	@Column(name = "Delivery_Date")
	Date deliveryDate;
	
	@Column(name = "EIMSTATUS")
	String eimstatus;
	
	@Transient
	Long userId;
	
	@Transient
	Long outletId;

	public String getEimstatus() {
		return eimstatus;
	}

	public void setEimstatus(String eimstatus) {
		this.eimstatus = eimstatus;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
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

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	@Override
	public String toString() {
		return "LtSoLines [lineId=" + lineId + ", headerId=" + headerId + ", productId=" + productId + ", quantity="
				+ quantity + ", listPrice=" + listPrice + ", deliveryDate=" + deliveryDate + ", eimstatus=" + eimstatus
				+ ", userId=" + userId + ", outletId=" + outletId + "]";
	}

}
