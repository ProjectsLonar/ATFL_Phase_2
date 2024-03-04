package com.atflMasterManagement.masterservice.model;

public class RequestDto {

	private String catId;
	private String orgId;
	private String distId;
	private String outletId;
	private int limit;
	private int offset;
	private String searchField;
	private String productId;
	private String categoryId;
	private String subCategoryId;
	private String userId;
	private String userType;

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDistId() {
		return distId;
	}

	public void setDistId(String distId) {
		this.distId = distId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
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

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "RequestDto [catId=" + catId + ", orgId=" + orgId + ", distId=" + distId + ", outletId=" + outletId
				+ ", limit=" + limit + ", offset=" + offset + ", searchField=" + searchField + ", productId="
				+ productId + ", categoryId=" + categoryId + ", subCategoryId=" + subCategoryId + ", userId=" + userId
				+ ", userType=" + userType + "]";
	}
	

}
