package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseDto {
	private Long headerId;
	private String orderNumber;
	private Date orderDate;
	private String status;
	private String address; 
	private String outletId;
	private String outletName;
	private String outletCode;
	private String latitude;
	private String longitude;
	private String remark;
	private String userId;
	private String address1;
	private String city;
	private String outletAddress;
	private Long customerId;
	private String proprietorName;

	private String BeatId;
	private String headerPriceList;
	
	// Line Data
	private String lineId;
	private String productId;
	private Long quantity;
	private String productCode;
	private String productDesc;
	private String productName;
	private String listPrice;
	private String priceList;
	private String ptrPrice;
	private Date deliveryDate;
	private String ptrFlag;
	private String linelistPrice;
	private String linePtrPrice;
	private String inventoryQuantity;
	
	private String orgId;
	private String categoryId;
	private String productType;
	private String category;
	private String subCategory;
	private String primaryUom;
	private String secondaryUom;
	private String secondaryUomValue;
	private String unitsPerCase;
	private String segment;
	private String brand;
	private String subBrand;
	private String casePack;
	private String hsnCode;
	private String productImage;
	private String thumbnailImage;
	
	private String distributorCode;
	
	public String getDistributorCode() {
		return distributorCode;
	}
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
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
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
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
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public String getListPrice() {
		return listPrice;
	}
	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}
	public String getPriceList() {
		return priceList;
	}
	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getPrimaryUom() {
		return primaryUom;
	}
	public void setPrimaryUom(String primaryUom) {
		this.primaryUom = primaryUom;
	}
	public String getSecondaryUom() {
		return secondaryUom;
	}
	public void setSecondaryUom(String secondaryUom) {
		this.secondaryUom = secondaryUom;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSubBrand() {
		return subBrand;
	}
	public void setSubBrand(String subBrand) {
		this.subBrand = subBrand;
	}

	public String getSecondaryUomValue() {
		return secondaryUomValue;
	}
	public void setSecondaryUomValue(String secondaryUomValue) {
		this.secondaryUomValue = secondaryUomValue;
	}
	public String getUnitsPerCase() {
		return unitsPerCase;
	}
	public void setUnitsPerCase(String unitsPerCase) {
		this.unitsPerCase = unitsPerCase;
	}
	public String getCasePack() {
		return casePack;
	}
	public void setCasePack(String casePack) {
		this.casePack = casePack;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPtrPrice() {
		return ptrPrice;
	}
	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
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
	public String getThumbnailImage() {
		return thumbnailImage;
	}
	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getPtrFlag() {
		return ptrFlag;
	}
	public void setPtrFlag(String ptrFlag) {
		this.ptrFlag = ptrFlag;
	}
	public String getProprietorName() {
		return proprietorName;
	}
	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}
	public String getLinelistPrice() {
		return linelistPrice;
	}
	public void setLinelistPrice(String linelistPrice) {
		this.linelistPrice = linelistPrice;
	}
	public String getLinePtrPrice() {
		return linePtrPrice;
	}
	public void setLinePtrPrice(String linePtrPrice) {
		this.linePtrPrice = linePtrPrice;
	}
	public String getInventoryQuantity() {
		return inventoryQuantity;
	}
	public void setInventoryQuantity(String inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
	
	
	public String getBeatId() {
		return BeatId;
	}
	public void setBeatId(String beatId) {
		BeatId = beatId;
	}
	public String getHeaderPriceList() {
		return headerPriceList;
	}
	public void setHeaderPriceList(String headerPriceList) {
		this.headerPriceList = headerPriceList;
	}

	
}
