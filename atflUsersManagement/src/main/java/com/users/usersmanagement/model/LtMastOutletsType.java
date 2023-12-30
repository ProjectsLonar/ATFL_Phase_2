package com.users.usersmanagement.model;

public class LtMastOutletsType {
	private String outletTypeCode;
	private String outletTypeValue;
	public String getOutletTypeCode() {
		return outletTypeCode;
	}
	public void setOutletTypeCode(String outletTypeCode) {
		this.outletTypeCode = outletTypeCode;
	}
	public String getOutletTypeValue() {
		return outletTypeValue;
	}
	public void setOutletTypeValue(String outletTypeValue) {
		this.outletTypeValue = outletTypeValue;
	}
	@Override
	public String toString() {
		return "LtMastOutletsType [outletTypeCode=" + outletTypeCode + ", outletTypeValue=" + outletTypeValue + "]";
	}
	

}
