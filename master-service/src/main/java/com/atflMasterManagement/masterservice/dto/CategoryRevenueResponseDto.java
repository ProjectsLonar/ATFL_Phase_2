package com.atflMasterManagement.masterservice.dto;

public class CategoryRevenueResponseDto {

	private String categoryId;
	private String categoryName;
	private String categoryDesc;
	private Long revenue = 0L;
	private String dbc = "0";
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getDbc() {
		return dbc;
	}

	public void setDbc(String dbc) {
		this.dbc = dbc;
	}

}
