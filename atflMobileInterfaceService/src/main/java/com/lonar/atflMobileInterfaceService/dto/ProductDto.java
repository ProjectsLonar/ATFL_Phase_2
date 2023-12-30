package com.lonar.atflMobileInterfaceService.dto;

public class ProductDto extends BaseClass {

	Long productId;

	Long orgId;

	Long categoryId;

	Long subCategoryId;

	String productType;

	String category;

	String subCategory;

	String productCode;

	String productName;

	String productDesc;

	String primaryUom;

	String secondaryUom;

	String secondaryUomValue;

	String unitsPerCase;

	String segment;

	String brand;

	String subBrand;

	String style;

	String flavor;

	String casePack;

	String hsnCode;

	String orderable;

	byte[] productImage;

	byte[] thumbnailImage;

	String mobileStatus;

	String mobileRemarks;

	String mobileInsertUpdate;
	

	public String getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus(String mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public String getMobileRemarks() {
		return mobileRemarks;
	}

	public void setMobileRemarks(String mobileRemarks) {
		this.mobileRemarks = mobileRemarks;
	}

	public String getMobileInsertUpdate() {
		return mobileInsertUpdate;
	}

	public void setMobileInsertUpdate(String mobileInsertUpdate) {
		this.mobileInsertUpdate = mobileInsertUpdate;
	}

	public byte[] getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(byte[] thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
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

	public String getOrderable() {
		return orderable;
	}

	public void setOrderable(String orderable) {
		this.orderable = orderable;
	}

	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

}
