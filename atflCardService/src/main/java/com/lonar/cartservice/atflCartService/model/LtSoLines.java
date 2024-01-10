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
@Table(name = "LT_SO_LINES")
@JsonInclude(Include.NON_NULL)
public class LtSoLines  extends BaseClass{
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_SO_LINES_S")
	@SequenceGenerator(name = "LT_SO_LINES_S", sequenceName = "LT_SO_LINES_S", allocationSize = 1)
	@Column(name = "Line_Id")
//	Long lineId;
	String lineId;
	
	@Column(name = "Header_id")
	Long headerId;
//  String headerId;
	
	@Column(name = "Product_Id")
//	Long productId;
	String productId;
	
	@Column(name = "Quantity")
	Long quantity;
	
	@Column(name = "List_Price")
	String listPrice;
	
	@Column(name = "ptr_price")
	String ptrPrice;
	
	@Column(name = "Delivery_Date")
	Date deliveryDate;
	
	@Transient
	Long userId;
	
	@Transient
	String outletId;

	@Column(name= "eimStatus")
	String eimStatus;
	
	public String getEimStatus() {
		return eimStatus;
	}

	public void setEimStatus(String eimStatus) {
		this.eimStatus = eimStatus;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
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

	
	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}

	@Override
	public String toString() {
		return "LtSoLines [lineId=" + lineId + ", headerId=" + headerId + ", productId=" + productId + ", quantity="
				+ quantity + ", listPrice=" + listPrice + ", deliveryDate=" + deliveryDate + ", userId=" + userId
				+ ", outletId=" + outletId + "]";
	}

}
