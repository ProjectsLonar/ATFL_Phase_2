package com.lonar.atflMobileInterfaceService.model;

public class OrderOutDto {

	private String orderNum;

	private String outletCode;


	private String productCode;
	
	private String eImStatus;

	private String status;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String geteImStatus() {
		return eImStatus;
	}

	public void seteImStatus(String eImStatus) {
		this.eImStatus = eImStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
