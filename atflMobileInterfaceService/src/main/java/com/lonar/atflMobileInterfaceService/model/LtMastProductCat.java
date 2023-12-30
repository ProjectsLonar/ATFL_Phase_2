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
@Table(name = "lt_mast_prod_categories")
@JsonInclude(Include.NON_NULL)
public class LtMastProductCat extends BaseClass {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "category_id")
	Long categoryId;
	
	@Column(name = "org_id")
	Long orgId;
		
	@Column(name = "category_code")
	String categoryCode;
	
	@Column(name = "category_name")
	String categoryName;
	
	@Column(name = "category_desc")
	String categoryDesc;
		
	@Column(name = "category_image")
	byte[] categoryImage;
	
	/*@Column(name = "status")
	String status;*/

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public byte[] getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(byte[] categoryImage) {
		this.categoryImage = categoryImage;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
