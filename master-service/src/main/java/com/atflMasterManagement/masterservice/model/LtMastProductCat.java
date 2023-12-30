package com.atflMasterManagement.masterservice.model;

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
	@GeneratedValue(strategy= GenerationType.SEQUENCE)
	@Column(name = "category_id")
	String categoryId;
	
	@Column(name = "org_id")
	String orgId;
		
	@Column(name = "category_code")
	String categoryCode;
	
	@Column(name = "category_name")
	String categoryName;
	
	@Column(name = "category_desc")
	String categoryDesc;
		
	@Column(name = "category_image")
	String categoryImage;
	
	@Column(name = "status")
	String status;

	@Column(name = "category_thumbnail")
	String categoryThumbnail;
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryThumbnail() {
		return categoryThumbnail;
	}

	public void setCategoryThumbnail(String categoryThumbnail) {
		this.categoryThumbnail = categoryThumbnail;
	}

	@Override
	public String toString() {
		return "LtMastProductCat [categoryId=" + categoryId + ", orgId=" + orgId + ", categoryCode=" + categoryCode
				+ ", categoryName=" + categoryName + ", categoryDesc=" + categoryDesc + ", categoryImage="
				+ categoryImage + ", status=" + status + "]";
	}
	
}
