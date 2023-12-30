package com.atflMasterManagement.masterservice.model;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import antlr.collections.List;

@Entity
@Table(name = "lt_mast_test_product")
@JsonInclude(Include.NON_NULL)
public class LtMastTestProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long productId;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "sub_category_id")
	private Long subCategoryId;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_desc")
	private String productDesc;

	@Column(name = "list_price")
	private Long listPrice;

	@Column(name = "units_per_case")
	private Long unitsPerCase;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "status")
	private String status;
	
	@Transient
	ArrayList<String> statesList;
	
	@Column(name = "states")
	private String states;


	@Column(name = "product_image")
	private String productImage;

	@Column(name = "display_to_customer")
	private boolean displayToCustomer;

	@Column(name = "hsn_code")
	private String hsnCode;

	@Transient
	private int limit;

	@Transient
	private int offset;

	@Transient
	private Long subCategory;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public Long getListPrice() {
		return listPrice;
	}

	public void setListPrice(Long listPrice) {
		this.listPrice = listPrice;
	}
	public Long getUnitsPerCase() {
		return unitsPerCase;
	}

	public void setUnitsPerCase(Long unitsPerCase) {
		this.unitsPerCase = unitsPerCase;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<String> getStatesList() {
		return statesList;
	}

	public void setStatesList(ArrayList<String> statesList) {
		this.statesList = statesList;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public boolean isDisplayToCustomer() {
		return displayToCustomer;
	}

	public void setDisplayToCustomer(boolean displayToCustomer) {
		this.displayToCustomer = displayToCustomer;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Long getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Long subCategory) {
		this.subCategory = subCategory;
	}
	
	

}
