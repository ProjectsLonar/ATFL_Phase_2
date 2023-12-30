package com.lonar.atflMobileInterfaceService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_prod_sub_categories")
@JsonInclude(Include.NON_NULL)
public class LtMastSubCategory extends BaseClass{
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "sub_category_id")
	Long subCategoryId;
	
	@Column(name = "org_id")
	Long orgId;
	
	@Column(name = "category_id")
	Long categoryId;

	
	@Column(name = "sub_category_code")
	String subCategoryCode;
	
	@Column(name = "sub_category_name")
	String subCategoryName;
	
	@Column(name = "sub_category_desc")
	String subCategoryDesc;
	
	@Column(name = "sub_category_image")
	byte[] subCategoryImage;
	
	/*@Column(name = "status")
	String status;*/

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
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

	public byte[] getSubCategoryImage() {
		return subCategoryImage;
	}

	public void setSubCategoryImage(byte[] subCategoryImage) {
		this.subCategoryImage = subCategoryImage;
	}

}
