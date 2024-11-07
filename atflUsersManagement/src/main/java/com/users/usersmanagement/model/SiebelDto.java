package com.users.usersmanagement.model;

import java.util.Date;
import java.util.Optional;

public class SiebelDto {

	 String outletId;
	 String recentSearchId;
	 String distributorId;
	 String status;
	 String employeeCode;
	 String userName;
	 Optional<Integer> latitude;
	 Optional<Integer> longitude;
	 String userType;
	 String address;
	 String email;
	 String homephNum;
	 String mobileNumber;
	 String asstPhNum;
	 String addressDetails;
	 String PositionId;
	 Date lastUpdateDate;
	 String lastUpdatedBy;
	 Date creationDate;
	 String createdBy;
	 String territory;
	 
	 String orgId;
	 String PositionCode;
	 
	 
	 
	public String getPositionCode() {
		return PositionCode;
	}
	public void setPositionCode(String positionCode) {
		PositionCode = positionCode;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getTerritory() {
		return territory;
	}
	public void setTerritory(String territory) {
		this.territory = territory;
	}
	public String getOutletId() {
		return outletId;
	}
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}
	public String getRecentSearchId() {
		return recentSearchId;
	}
	public void setRecentSearchId(String recentSearchId) {
		this.recentSearchId = recentSearchId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Optional<Integer> getLatitude() {
		return latitude;
	}
	public void setLatitude(Optional<Integer> latitude) {
		this.latitude = latitude;
	}
	public Optional<Integer> getLongitude() {
		return longitude;
	}
	public void setLongitude(Optional<Integer> longitude) {
		this.longitude = longitude;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomephNum() {
		return homephNum;
	}
	public void setHomephNum(String homephNum) {
		this.homephNum = homephNum;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAsstPhNum() {
		return asstPhNum;
	}
	public void setAsstPhNum(String asstPhNum) {
		this.asstPhNum = asstPhNum;
	}
	public String getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}
	public String getPositionId() {
		return PositionId;
	}
	public void setPositionId(String positionId) {
		PositionId = positionId;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	@Override
	public String toString() {
		return "SiebelDto [outletId=" + outletId + ", recentSearchId=" + recentSearchId + ", distributorId="
				+ distributorId + ", status=" + status + ", employeeCode=" + employeeCode + ", userName=" + userName
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", userType=" + userType + ", address="
				+ address + ", email=" + email + ", homephNum=" + homephNum + ", mobileNumber=" + mobileNumber
				+ ", asstPhNum=" + asstPhNum + ", addressDetails=" + addressDetails + ", PositionId=" + PositionId
				+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy=" + lastUpdatedBy + ", creationDate="
				+ creationDate + ", createdBy=" + createdBy + ", territory=" + territory + ", orgId=" + orgId
				+ ", PositionCode=" + PositionCode + "]";
	}
	
		
}
