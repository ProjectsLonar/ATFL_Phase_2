package com.users.usersmanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OutletSequenceData {

	private String outletName;
	private String outletCode;
	private String beatName;
	private int outletSequence;
	private String address;
	
	
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public String getOutletCode() {
		return outletCode;
	}
	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}
	public String getBeatName() {
		return beatName;
	}
	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}
	public int getOutletSequence() {
		return outletSequence;
	}
	public void setOutletSequence(int outletSequence) {
		this.outletSequence = outletSequence;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	@Override
	public String toString() {
		return "OutletSequenceData [outletName=" + outletName + ", outletCode=" + outletCode + ", beatName=" + beatName
				+ ", outletSequence=" + outletSequence + ", address=" + address + "]";
	}

}
