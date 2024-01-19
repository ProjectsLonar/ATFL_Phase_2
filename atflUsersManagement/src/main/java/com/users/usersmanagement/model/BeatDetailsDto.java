package com.users.usersmanagement.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BeatDetailsDto {

	private String distributorNumber;
	private String distributorName;
	private String distributorCode;
	
	private List<OutletSequenceData> outletSequenceData;
	
	
//	private String outletName;
//	private String outletCode;
//	private String beatName;
//	private int outletSequence;
//	private String address;
	
//	private String addrLane2;
//	private String addrLane3;
//	private String city;
	
	
public List<OutletSequenceData> getOutletSequenceData() {
		return outletSequenceData;
	}
	public void setOutletSequenceData(List<OutletSequenceData> outletSequenceData) {
		this.outletSequenceData = outletSequenceData;
	}
	//	public int getOutletSequence() {
//		return outletSequence;
//	}
//	public void setOutletSequence(int outletSequence) {
//		this.outletSequence = outletSequence;
//	}
	public String getDistributorNumber() {
		return distributorNumber;
	}
	public void setDistributorNumber(String distributorNumber) {
		this.distributorNumber = distributorNumber;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getDistributorCode() {
		return distributorCode;
	}
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}
//	public String getOutletName() {
//		return outletName;
//	}
//	public void setOutletName(String outletName) {
//		this.outletName = outletName;
//	}
//	public String getOutletCode() {
//		return outletCode;
//	}
//	public void setOutletCode(String outletCode) {
//		this.outletCode = outletCode;
//	}
//	public String getBeatName() {
//		return beatName;
//	}
//	public void setBeatName(String beatName) {
//		this.beatName = beatName;
//	}
	
//	public String getAddress() {
//		return address;
//	}
//	public void setAddress(String addr) {
//		this.address = address;
//	}
	
	
	
	@Override
	public String toString() {
		return "BeatDetailsDto [distributorNumber=" + distributorNumber + ", distributorName=" + distributorName
				+ ", distributorCode=" + distributorCode + ", outletSequenceData=" + outletSequenceData + "]";
	}
		
	
}
