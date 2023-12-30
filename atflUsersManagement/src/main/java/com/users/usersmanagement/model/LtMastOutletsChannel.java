package com.users.usersmanagement.model;

public class LtMastOutletsChannel {
	private String outletChannelCode;
	private String outletChannelValue;
	public String getOutletChannelCode() {
		return outletChannelCode;
	}
	public void setOutletChannelCode(String outletChannelCode) {
		this.outletChannelCode = outletChannelCode;
	}
	public String getOutletChannelValue() {
		return outletChannelValue;
	}
	public void setOutletChannelValue(String outletChannelValue) {
		this.outletChannelValue = outletChannelValue;
	}
	@Override
	public String toString() {
		return "LtMastOutletsChannel [outletChannelCode=" + outletChannelCode + ", outletChannelValue="
				+ outletChannelValue + "]";
	}
	

}
