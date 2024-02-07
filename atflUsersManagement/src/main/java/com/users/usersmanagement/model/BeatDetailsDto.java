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
	
	private String NAME;
	private int STORE_SIZE;
	private String MASTER_OU_ID;
	private String OU_TYPE_CD;
	private String BU_ID;
	private String RULE_ATTRIB1;   ///i.e. beatName
	
	private String searchField;
	private int limit;
	private int offset;
	
		
    public String getRULE_ATTRIB1() {
		return RULE_ATTRIB1;
	}
	public void setRULE_ATTRIB1(String rULE_ATTRIB1) {
		RULE_ATTRIB1 = rULE_ATTRIB1;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
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
public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public int getSTORE_SIZE() {
		return STORE_SIZE;
	}
	public void setSTORE_SIZE(int sTORE_SIZE) {
		STORE_SIZE = sTORE_SIZE;
	}
	public String getMASTER_OU_ID() {
		return MASTER_OU_ID;
	}
	public void setMASTER_OU_ID(String mASTER_OU_ID) {
		MASTER_OU_ID = mASTER_OU_ID;
	}
	public String getOU_TYPE_CD() {
		return OU_TYPE_CD;
	}
	public void setOU_TYPE_CD(String oU_TYPE_CD) {
		OU_TYPE_CD = oU_TYPE_CD;
	}
	public String getBU_ID() {
		return BU_ID;
	}
	public void setBU_ID(String bU_ID) {
		BU_ID = bU_ID;
	}
	
	
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
				+ ", distributorCode=" + distributorCode + ", outletSequenceData=" + outletSequenceData + ", NAME="
				+ NAME + ", STORE_SIZE=" + STORE_SIZE + ", MASTER_OU_ID=" + MASTER_OU_ID + ", OU_TYPE_CD=" + OU_TYPE_CD
				+ ", BU_ID=" + BU_ID + ", RULE_ATTRIB1=" + RULE_ATTRIB1 + ", searchField=" + searchField + ", limit="
				+ limit + ", offset=" + offset + "]";
	}	
	
	
}
