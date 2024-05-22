package com.lonar.cartservice.atflCartService.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SoLineDto {
	private Long lineId;
	private String productId;
	private Long quantity;
	private String productCode;
	private String productDesc;
	private String productName;
	private String listPrice;
	private String priceList;
	private String ptrPrice;
	private Date deliveryDate;
	private String status;
	
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
	
	private String eimStatus;
	private String priceListId;
	private String headerPriceList;
	
	
	private Long headerId;
	
	private Long ptrBasePrice;
	private String location;
	private String inventoryId;
	private String shippedQuantity;
	private Long returnQuantity;
	
	
	public Long getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Long returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(String shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Long getPtrBasePrice() {
		return ptrBasePrice;
	}

	public void setPtrBasePrice(Long ptrBasePrice) {
		this.ptrBasePrice = ptrBasePrice;
	}

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public String getHeaderPriceList() {
		return headerPriceList;
	}

	public void setHeaderPriceList(String headerPriceList) {
		this.headerPriceList = headerPriceList;
	}

	public String getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(String priceListId) {
		this.priceListId = priceListId;
	}

	public String getEimStatus() {
		return eimStatus;
	}

	public void setEimStatus(String eimStatus) {
		this.eimStatus = eimStatus;
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

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	
	@Override
	public String toString() {
		return "SoLineDto [lineId=" + lineId + ", productId=" + productId + ", quantity=" + quantity + ", productCode="
				+ productCode + ", productDesc=" + productDesc + ", productName=" + productName + ", listPrice="
				+ listPrice + ", priceList=" + priceList + ", ptrPrice=" + ptrPrice + ", deliveryDate=" + deliveryDate
				+ ", status=" + status + ", linelistPrice=" + linelistPrice + ", linePtrPrice=" + linePtrPrice
				+ ", inventoryQuantity=" + inventoryQuantity + ", orgId=" + orgId + ", categoryId=" + categoryId
				+ ", productType=" + productType + ", category=" + category + ", subCategory=" + subCategory
				+ ", primaryUom=" + primaryUom + ", secondaryUom=" + secondaryUom + ", secondaryUomValue="
				+ secondaryUomValue + ", unitsPerCase=" + unitsPerCase + ", segment=" + segment + ", brand=" + brand
				+ ", subBrand=" + subBrand + ", casePack=" + casePack + ", hsnCode=" + hsnCode + ", productImage="
				+ productImage + ", thumbnailImage=" + thumbnailImage + ", eimStatus=" + eimStatus + ", priceListId="
				+ priceListId + ", headerPriceList=" + headerPriceList + ", headerId=" + headerId + ", ptrBasePrice="
				+ ptrBasePrice + ", location=" + location + ", inventoryId=" + inventoryId + ", shippedQuantity="
				+ shippedQuantity + ", returnQuantity=" + returnQuantity + "]";
	}

	
}
