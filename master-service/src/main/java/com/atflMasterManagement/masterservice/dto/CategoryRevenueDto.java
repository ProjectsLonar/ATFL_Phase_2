package com.atflMasterManagement.masterservice.dto;

public class CategoryRevenueDto {

	private String categoryId;
	private String categoryName;
	private String listPrice;
	private Long quantity;
	private Long ptrPrice;
	private String categoryDesc;
	private Long revenue = 0L;
	private String dbc = "0";
	
	//private Long dbc1=0L;
	String ptrPrice1;
	
	

public String getPtrPrice1() {
		return ptrPrice1;
	}

	public void setPtrPrice1(String ptrPrice1) {
		this.ptrPrice1 = ptrPrice1;
	}

	/*	public Long getDbc1() {
		return dbc1;
	}

	public void setDbc1(Long dbc1) {
		this.dbc1 = dbc1;
	}
*/
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

	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getPtrPrice() {
		return ptrPrice;
	}

	public void setPtrPrice(Long ptrPrice1) {
		this.ptrPrice = ptrPrice;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

	public String getDbc() {
		return dbc;
	}

	public void setDbc(String dbc) {
		this.dbc = dbc;
	}

	@Override
	public String toString() {
		return "CategoryRevenueDto [categoryId=" + categoryId + ", categoryName=" + categoryName + ", listPrice="
				+ listPrice + ", quantity=" + quantity + ", ptrPrice1=" + ptrPrice1 + ", categoryDesc=" + categoryDesc
				+ ", revenue=" + revenue + ", dbc=" + dbc + ", ptrPrice=" + ptrPrice + "]";
	}

	
		
}
