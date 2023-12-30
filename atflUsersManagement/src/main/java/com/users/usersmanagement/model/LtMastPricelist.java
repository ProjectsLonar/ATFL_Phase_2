package com.users.usersmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_PRICE_LISTS_V")
@JsonInclude(Include.NON_NULL)
public class LtMastPricelist extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "PRICE_LIST_ID")
	String priceListId;

	@Column(name = "org_id")
	Long orgId;
	
	@Column(name = "PRICE_LIST")
	String priceList;
	
	@Column(name = "PRICE_LIST_DESC")
	String priceListDesc;
	
	@Column(name = "CURRENCY")
	String currency;
	
	@Column(name = "PRODUCT_CODE")
	String productCode;
	
	@Column(name = "LIST_PRICE")
	String listPrice;
	
	@Column(name = "PTR_PRICE")
	String ptrPrice;

	public String getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(String priceListId) {
		this.priceListId = priceListId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getPriceListDesc() {
		return priceListDesc;
	}

	public void setPriceListDesc(String priceListDesc) {
		this.priceListDesc = priceListDesc;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	@Override
	public String toString() {
		return "LtMastPricelist [priceListId=" + priceListId + ", orgId=" + orgId + ", priceList=" + priceList
				+ ", priceListDesc=" + priceListDesc + ", currency=" + currency + ", productCode=" + productCode
				+ ", listPrice=" + listPrice + ", ptrPrice=" + ptrPrice + "]";
	}
	
	
}
