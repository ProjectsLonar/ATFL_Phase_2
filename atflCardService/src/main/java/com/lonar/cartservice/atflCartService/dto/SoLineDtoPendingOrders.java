package com.lonar.cartservice.atflCartService.dto;
 
import java.util.Date;
 
public class SoLineDtoPendingOrders {
	Long lineId;
    String productId;
    Long quantity;
    String productCode;
    String productDesc;
    String productName;
    String listPrice;
    String priceList;
    String ptrPrice;
    Date deliveryDate1;
    String linelistPrice;
    String linePtrPrice;
    String inventoryQuantity;
    String orgId;
    String categoryId;
    String productType;
    String category;
    String subCategory;
    String primaryUom;
    String secondaryUom;
    String secondaryUomValue;
    String unitsPerCase;
    String segment;
    String brand;
    String casePack;
    String hsnCode;
    String BeatId;
    
	public SoLineDtoPendingOrders() {
		super();
	}
 
	
	public SoLineDtoPendingOrders(Long lineId, String productId, Long quantity, String productCode, String productDesc,
			String productName, String listPrice, String priceList, String ptrPrice, Date deliveryDate1,
			String linelistPrice, String linePtrPrice, String inventoryQuantity, String orgId, String categoryId,
			String productType, String category, String subCategory, String primaryUom, String secondaryUom,
			String secondaryUomValue, String unitsPerCase, String segment, String brand, String casePack,
			String hsnCode, String BeatId) {
		super();
		this.lineId = lineId;
		this.productId = productId;
		this.quantity = quantity;
		this.productCode = productCode;
		this.productDesc = productDesc;
		this.productName = productName;
		this.listPrice = listPrice;
		this.priceList = priceList;
		this.ptrPrice = ptrPrice;
		this.deliveryDate1 = deliveryDate1;
		this.linelistPrice = linelistPrice;
		this.linePtrPrice = linePtrPrice;
		this.inventoryQuantity = inventoryQuantity;
		this.orgId = orgId;
		this.categoryId = categoryId;
		this.productType = productType;
		this.category = category;
		this.subCategory = subCategory;
		this.primaryUom = primaryUom;
		this.secondaryUom = secondaryUom;
		this.secondaryUomValue = secondaryUomValue;
		this.unitsPerCase = unitsPerCase;
		this.segment = segment;
		this.brand = brand;
		this.casePack = casePack;
		this.hsnCode = hsnCode;
		this.BeatId = BeatId;
	}
 
	public Long getLineId() {
		return lineId;
	}
 
 
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
 
 
	public String getProductId() {
		return productId;
	}
 
 
	public void setProductId(String productId) {
		this.productId = productId;
	}
 
 
	public Long getQuantity() {
		return quantity;
	}
 
 
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
 
 
	public String getProductCode() {
		return productCode;
	}
 
 
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
 
 
	public String getProductDesc() {
		return productDesc;
	}
 
 
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
 
 
	public String getProductName() {
		return productName;
	}
 
 
	public void setProductName(String productName) {
		this.productName = productName;
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
 
 
	public void setPtrPrice(String ptrPrice) {
		this.ptrPrice = ptrPrice;
	}
 
 
	public Date getDeliveryDate1() {
		return deliveryDate1;
	}
 
 
	public void setDeliveryDate1(Date deliveryDate1) {
		this.deliveryDate1 = deliveryDate1;
	}
 
 
	public String getLinelistPrice() {
		return linelistPrice;
	}
 
 
	public void setLinelistPrice(String linelistPrice) {
		this.linelistPrice = linelistPrice;
	}
 
 
	public String getLinePtrPrice() {
		return linePtrPrice;
	}
 
 
	public void setLinePtrPrice(String linePtrPrice) {
		this.linePtrPrice = linePtrPrice;
	}
 
 
	public String getInventoryQuantity() {
		return inventoryQuantity;
	}
 
 
	public void setInventoryQuantity(String inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
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
 
 
	public String getUnitsPerCase() {
		return unitsPerCase;
	}
 
 
	public void setUnitsPerCase(String unitsPerCase) {
		this.unitsPerCase = unitsPerCase;
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
 
 
	public String getBeatId() {
		return BeatId;
	}
 
 
	public void setBeatId(String beatId) {
		this.BeatId = beatId;
	}
 
 
	@Override
	public String toString() {
		return "SoLineDtoPendingOrders [lineId=" + lineId + ", productId=" + productId + ", quantity=" + quantity
				+ ", productCode=" + productCode + ", productDesc=" + productDesc + ", productName=" + productName
				+ ", listPrice=" + listPrice + ", priceList=" + priceList + ", ptrPrice=" + ptrPrice
				+ ", deliveryDate1=" + deliveryDate1 + ", linelistPrice=" + linelistPrice + ", linePtrPrice="
				+ linePtrPrice + ", inventoryQuantity=" + inventoryQuantity + ", orgId=" + orgId + ", categoryId="
				+ categoryId + ", productType=" + productType + ", category=" + category + ", subCategory="
				+ subCategory + ", primaryUom=" + primaryUom + ", secondaryUom=" + secondaryUom + ", secondaryUomValue="
				+ secondaryUomValue + ", unitsPerCase=" + unitsPerCase + ", segment=" + segment + ", brand=" + brand
				+ ", casePack=" + casePack + ", hsnCode=" + hsnCode + ", beatId=" + BeatId + "]";
	}
 
	
    // Constructor, getters, and setters can be generated here
    
    
	
 
}