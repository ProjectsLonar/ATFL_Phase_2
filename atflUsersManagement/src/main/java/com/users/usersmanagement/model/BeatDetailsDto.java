package com.users.usersmanagement.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BeatDetailsDto {

	private String distributorNumber;
	private String distributorName;
	private String distributorCode;
	
	private List<OutletSequenceData> outletSequenceData;
	
	
	private String outletName;
	private String outletCode;
	private String beatName;
	private int outletSequence;
	private String address;
	private String NAME;
	private int STORE_SIZE;
	private String MASTER_OU_ID;
	private String OU_TYPE_CD;
	private String BU_ID;
	private String RULE_ATTRIB1;   ///i.e. beatName
	
	private String searchField;
	private int limit;
	private int offset;
	
	
	String outletId;
	String distributorId;
	String outletType;
	String proprietorName;
	String outletAddress;
	String address2;
	String address3;
	String address4;
	String address_3;
	String address_4;
	String landmark;
	String country;
	String state;
	String city;
	String pin_code;
	String region;
	String area;
	String territory;
	String outletGstn;
	String phone;
	String email;
	String primaryMobile;
	String status;
	Date startDate;
	Date endDate;
	String createdBy; 
	String priceList;
	String positionsId;
	String orgId;
	Date lastUpdateDate;
	String address1;
	String outletChannel;
	String name_position;
	String position;
	String beatId;
		
	
	
	
    public String getAddress_3() {
		return address_3;
	}
	public void setAddress_3(String address_3) {
		this.address_3 = address_3;
	}
	public String getAddress_4() {
		return address_4;
	}
	public void setAddress_4(String address_4) {
		this.address_4 = address_4;
	}
	public String getName_position() {
		return name_position;
	}
	public void setName_position(String name_position) {
		this.name_position = name_position;
	}
	public String getBeatId() {
		return beatId;
	}
	public void setBeatId(String beatId) {
		this.beatId = beatId;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getOutletType() {
		return outletType;
	}
	public void setOutletType(String outletType) {
		this.outletType = outletType;
	}
	public String getProprietorName() {
		return proprietorName;
	}
	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}
	public String getOutletAddress() {
		return outletAddress;
	}
	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getAddress4() {
		return address4;
	}
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPin_code() {
		return pin_code;
	}
	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTerritory() {
		return territory;
	}
	public void setTerritory(String territory) {
		this.territory = territory;
	}
	public String getOutletGstn() {
		return outletGstn;
	}
	public void setOutletGstn(String outletGstn) {
		this.outletGstn = outletGstn;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPrimaryMobile() {
		return primaryMobile;
	}
	public void setPrimaryMobile(String primaryMobile) {
		this.primaryMobile = primaryMobile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getPriceList() {
		return priceList;
	}
	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
	public String getPositionsId() {
		return positionsId;
	}
	public void setPositionsId(String positionsId) {
		this.positionsId = positionsId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getOutletChannel() {
		return outletChannel;
	}
	public void setOutletChannel(String outletChannel) {
		this.outletChannel = outletChannel;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
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
		return "BeatDetailsDto [distributorNumber=" + distributorNumber + ", distributorName=" + distributorName
				+ ", distributorCode=" + distributorCode + ", outletSequenceData=" + outletSequenceData
				+ ", outletName=" + outletName + ", outletCode=" + outletCode + ", beatName=" + beatName
				+ ", outletSequence=" + outletSequence + ", address=" + address + ", NAME=" + NAME + ", STORE_SIZE="
				+ STORE_SIZE + ", MASTER_OU_ID=" + MASTER_OU_ID + ", OU_TYPE_CD=" + OU_TYPE_CD + ", BU_ID=" + BU_ID
				+ ", RULE_ATTRIB1=" + RULE_ATTRIB1 + ", searchField=" + searchField + ", limit=" + limit + ", offset="
				+ offset + ", outletId=" + outletId + ", distributorId=" + distributorId + ", outletType=" + outletType
				+ ", proprietorName=" + proprietorName + ", outletAddress=" + outletAddress + ", address2=" + address2
				+ ", address3=" + address3 + ", address4=" + address4 + ", address_3=" + address_3 + ", address_4="
				+ address_4 + ", landmark=" + landmark + ", country=" + country + ", state=" + state + ", city=" + city
				+ ", pin_code=" + pin_code + ", region=" + region + ", area=" + area + ", territory=" + territory
				+ ", outletGstn=" + outletGstn + ", phone=" + phone + ", email=" + email + ", primaryMobile="
				+ primaryMobile + ", status=" + status + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", createdBy=" + createdBy + ", priceList=" + priceList + ", positionsId=" + positionsId + ", orgId="
				+ orgId + ", lastUpdateDate=" + lastUpdateDate + ", address1=" + address1 + ", outletChannel="
				+ outletChannel + ", name_position=" + name_position + ", position=" + position + ", beatId=" + beatId
				+ "]";
	}
	
	
}
