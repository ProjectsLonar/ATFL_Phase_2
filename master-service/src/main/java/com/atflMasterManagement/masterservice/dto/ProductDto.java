package com.atflMasterManagement.masterservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductDto {
	
	private String productId;
	private String orgId;
	private String categoryId;
	private String subCategoryId;
	private String productType;
	private String category;
	private String subCategory;
	private String productCode;
	private String productName;
	private String productDesc;
	private String primaryUom;
	private String secondaryUom;
	private String secondaryUomValue;
	private String unitsPerCase;
	private String segment;
	private String brand;
	private String subBrand;
	private String style;
	private String flavor;
	private String casePack;
	private String hsnCode;
	private String productImage;
	private String status;
	private String thumbnailImage;
	private String ptrFlag;
	
	private String currency;
	private String listPrice;
	private String priceList;
	private String ptrPrice; 
	
	private String categoryCode;
	private String categoryName;
	private String categoryDesc;
	private String categoryThumbnail;
	
	private String categoryImage;
	private String subCategoryCode;
	private String subCategoryName;
	private String subCategoryDesc;
	private String subCategoryImage;
	
	private String inventoryQuantity;
	private String  inventoryCode;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
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
	public String getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
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
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
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
	
	public String getSecondaryUomValue() {
		return secondaryUomValue;
	}
	public void setSecondaryUomValue(String secondaryUomValue) {
		this.secondaryUomValue = secondaryUomValue;
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
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getFlavor() {
		return flavor;
	}
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getThumbnailImage() {
		return thumbnailImage;
	}
	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	public String getPtrPrice() {
		return ptrPrice;
	}
	public void setPtrPrice(String string) {
		this.ptrPrice = string;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getCategoryThumbnail() {
		return categoryThumbnail;
	}
	public void setCategoryThumbnail(String categoryThumbnail) {
		this.categoryThumbnail = categoryThumbnail;
	}
	public String getCategoryImage() {
		return categoryImage;
	}
	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}
	public String getSubCategoryCode() {
		return subCategoryCode;
	}
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	public String getSubCategoryDesc() {
		return subCategoryDesc;
	}
	public void setSubCategoryDesc(String subCategoryDesc) {
		this.subCategoryDesc = subCategoryDesc;
	}
	public String getSubCategoryImage() {
		return subCategoryImage;
	}
	public void setSubCategoryImage(String subCategoryImage) {
		this.subCategoryImage = subCategoryImage;
	}
	public String getPtrFlag() {
		return ptrFlag;
	}
	public void setPtrFlag(String ptrFlag) {
		this.ptrFlag = ptrFlag;
	}
	public String getInventoryQuantity() {
		return inventoryQuantity;
	}
	public void setInventoryQuantity(String inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
	public String getInventoryCode() {
		return inventoryCode;
	}
	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}
}
