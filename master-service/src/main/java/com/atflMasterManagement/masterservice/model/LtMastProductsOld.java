package com.atflMasterManagement.masterservice.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_products")
@JsonInclude(Include.NON_NULL)
public class LtMastProductsOld  extends BaseClass {
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	Long productId;

	@Column(name = "org_id")
	Long orgId;

	@Column(name = "category_id")
	Long categoryId;

	@Column(name = "sub_category_id")
	Long subCategoryId;

	@Column(name = "product_type")
	String productType;

	@Column(name = "category")
	String category;

	@Column(name = "subcategory")
	String subCategory;

	@Column(name = "product_code")
	String productCode;

	@Column(name = "product_name")
	String productName;

	@Column(name = "product_desc")
	String productDesc;

	@Column(name = "primary_uom")
	String primaryUom;

	@Column(name = "secondary_uom")
	String secondaryUom;

	@Column(name = "secondary_uom_value")
	String secondaryUomValue;

	@Column(name = "units_per_case")
	String unitsPerCase;

	@Column(name = "segment")
	String segment;

	@Column(name = "brand")
	String brand;

	@Column(name = "subbrand")
	String subBrand;

	@Column(name = "style")
	String style;

	@Column(name = "flavor")
	String flavor;

	@Column(name = "case_pack")
	String casePack;

	@Column(name = "hsn_code")
	String hsnCode;

	@Column(name = "orderable")
	String orderable;

	@Column(name = "product_image")
	String productImage;

	@Column(name = "thumbnail_image")
	String thumbnailImage;
	
	@Column(name = "ptr_flag")
	String ptrFlag;
	
	public String getPtrFlag() {
		return ptrFlag;
	}

	public void setPtrFlag(String ptrFlag) {
		this.ptrFlag = ptrFlag;
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

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
}
