package com.atflMasterManagement.masterservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//import oracle.sql.DATE;
import java.util.Date;

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
	private String inventoryCode;
	private String outletId;
	private String subcategory;
	private String unitPerCase;
	private String orderable;
	private String categoryId1;
	private String orgId1;
	private String locationName;
	private String distributorId;
		
	private String prodShortDesc;
	
	private String inventoryName;
	private String inventoryId;
	private String outletCode;
	private String outletName;
	private String distributorName;
	private String lotNumber;
	private Date lotMaufacturingDate;
	private String distributorCode;
	private double MRP; 
	
		
	
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public Date getLotMaufacturingDate() {
		return lotMaufacturingDate;
	}
	public void setLotMaufacturingDate(Date lotMaufacturingDate) {
		this.lotMaufacturingDate = lotMaufacturingDate;
	}
	public double getMRP() {
		return MRP;
	}
	public void setMRP(double mRP) {
		MRP = mRP;
	}
	public String getInventoryName() {
		return inventoryName;
	}
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
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
	public String getProdShortDesc() {
		return prodShortDesc;
	}
	public void setProdShortDesc(String prodShortDesc) {
		this.prodShortDesc = prodShortDesc;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public String getUnitPerCase() {
		return unitPerCase;
	}
	public void setUnitPerCase(String unitPerCase) {
		this.unitPerCase = unitPerCase;
	}
	public String getOrderable() {
		return orderable;
	}
	public void setOrderable(String orderable) {
		this.orderable = orderable;
	}
	public String getCategoryId1() {
		return categoryId1;
	}
	public void setCategoryId1(String categoryId1) {
		this.categoryId1 = categoryId1;
	}
	public String getOrgId1() {
		return orgId1;
	}
	public void setOrgId1(String orgId1) {
		this.orgId1 = orgId1;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
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
	
	
	@Override
	public String toString() {
		return "ProductDto [productId=" + productId + ", orgId=" + orgId + ", categoryId=" + categoryId
				+ ", subCategoryId=" + subCategoryId + ", productType=" + productType + ", category=" + category
				+ ", subCategory=" + subCategory + ", productCode=" + productCode + ", productName=" + productName
				+ ", productDesc=" + productDesc + ", primaryUom=" + primaryUom + ", secondaryUom=" + secondaryUom
				+ ", secondaryUomValue=" + secondaryUomValue + ", unitsPerCase=" + unitsPerCase + ", segment=" + segment
				+ ", brand=" + brand + ", subBrand=" + subBrand + ", style=" + style + ", flavor=" + flavor
				+ ", casePack=" + casePack + ", hsnCode=" + hsnCode + ", productImage=" + productImage + ", status="
				+ status + ", thumbnailImage=" + thumbnailImage + ", ptrFlag=" + ptrFlag + ", currency=" + currency
				+ ", listPrice=" + listPrice + ", priceList=" + priceList + ", ptrPrice=" + ptrPrice + ", categoryCode="
				+ categoryCode + ", categoryName=" + categoryName + ", categoryDesc=" + categoryDesc
				+ ", categoryThumbnail=" + categoryThumbnail + ", categoryImage=" + categoryImage + ", subCategoryCode="
				+ subCategoryCode + ", subCategoryName=" + subCategoryName + ", subCategoryDesc=" + subCategoryDesc
				+ ", subCategoryImage=" + subCategoryImage + ", inventoryQuantity=" + inventoryQuantity
				+ ", inventoryCode=" + inventoryCode + ", outletId=" + outletId + ", subcategory=" + subcategory
				+ ", unitPerCase=" + unitPerCase + ", orderable=" + orderable + ", categoryId1=" + categoryId1
				+ ", orgId1=" + orgId1 + ", locationName=" + locationName + ", distributorId=" + distributorId
				+ ", prodShortDesc=" + prodShortDesc + ", inventoryName=" + inventoryName + ", inventoryId="
				+ inventoryId + ", outletCode=" + outletCode + ", outletName=" + outletName + ", distributorName="
				+ distributorName + ", lotNumber=" + lotNumber + ", lotMaufacturingDate=" + lotMaufacturingDate
				+ ", distributorCode=" + distributorCode + ", MRP=" + MRP + "]";
	}	
	
					
}
