package com.lonar.cartservice.atflCartService.dto;

import java.sql.Clob;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RequestDto {

	private String orgId;
	private String distributorId;
	private Long salesPersonId;
	private String outletId;
	private String orderNumber;
	private String status;
	private String details;
	private Long headerId;
	private int limit;
	private int offset;
	private String searchField;
	private Long customerId;
	private Long userId;
	private String invoiceNumber;
	private String returnStatus;
	private Long salesReturnHeaderId;
	private String salesReturnNumber;
	private Double price;
	private Double totalPrice;
	
	private Long loginId;
	
	String siebelStatus;
	String siebelRemark; 
	
	String mobileNumber;
	private String positionId;
	private String priceList;
	
	
	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSiebelStatus() {
		return siebelStatus;
	}

	public void setSiebelStatus(String siebelStatus) {
		this.siebelStatus = siebelStatus;
	}

	public String getSiebelRemark() {
		return siebelRemark;
	}

	public void setSiebelRemark(String siebelRemark) {
		this.siebelRemark = siebelRemark;
	}

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public Long getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(Long salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}


	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
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

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public Long getSalesReturnHeaderId() {
		return salesReturnHeaderId;
	}

	public void setSalesReturnHeaderId(Long salesReturnHeaderId) {
		this.salesReturnHeaderId = salesReturnHeaderId;
	}

	
	public String getSalesReturnNumber() {
		return salesReturnNumber;
	}

	public void setSalesReturnNumber(String salesReturnNumber) {
		this.salesReturnNumber = salesReturnNumber;
	}
	
	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "RequestDto [orgId=" + orgId + ", distributorId=" + distributorId + ", salesPersonId=" + salesPersonId
				+ ", outletId=" + outletId + ", orderNumber=" + orderNumber + ", status=" + status + ", details="
				+ details + ", headerId=" + headerId + ", limit=" + limit + ", offset=" + offset + ", searchField="
				+ searchField + ", customerId=" + customerId + ", userId=" + userId + ", invoiceNumber=" + invoiceNumber
				+ ", returnStatus=" + returnStatus + ", salesReturnHeaderId=" + salesReturnHeaderId
				+ ", salesReturnNumber=" + salesReturnNumber + ", price=" + price + ", totalPrice=" + totalPrice
				+ ", loginId=" + loginId + ", siebelStatus=" + siebelStatus + ", siebelRemark=" + siebelRemark
				+ ", mobileNumber=" + mobileNumber + ", positionId=" + positionId + ", priceList=" + priceList + "]";
	}

		
}