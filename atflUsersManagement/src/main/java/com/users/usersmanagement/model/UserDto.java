package com.users.usersmanagement.model;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Transient;

public class UserDto {

	
	private String recentSearchId;
	private String recentSearchId1;
	private String status;
	private String createdBy;
	private Optional<Integer> latitude;
	private String lastUpdatedBy;
	private Optional<Integer> longitude;
	private Date lastUpdateDate;
	private String address;
	private Date creationDate;
	private String homephNum;
	private String PositionId;
	private String asstPhNum;
	private String mobileNumber;
	private String positionId;
	private String outletId;
	private String distributorId;
	private String userName;
	private String userType;
	private String email;
	private String employeeCode;
	private String addressDetails;
	
	
	
	public String getRecentSearchId() {
		return recentSearchId;
	}
	public void setRecentSearchId(String recentSearchId) {
		this.recentSearchId = recentSearchId;
	}
	public String getRecentSearchId1() {
		return recentSearchId1;
	}
	public void setRecentSearchId1(String recentSearchId1) {
		this.recentSearchId1 = recentSearchId1;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Optional<Integer> getLatitude() {
		return latitude;
	}
	public void setLatitude(Optional<Integer> latitude) {
		this.latitude = latitude;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lstUpdatedBy) {
		this.lastUpdatedBy = lstUpdatedBy;
	}
	public Optional<Integer> getLongitude() {
		return longitude;
	}
	public void setLongitude(Optional<Integer> longitude) {
		this.longitude = longitude;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lstUpdateDate) {
		this.lastUpdateDate = lstUpdateDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getHomephNum() {
		return homephNum;
	}
	public void setHomephNum(String homephNum) {
		this.homephNum = homephNum;
	}
	public String getPositionId() {
		return PositionId;
	}
	public void setPositionId(String positionId) {
		PositionId = positionId;
	}
	public String getAsstPhNum() {
		return asstPhNum;
	}
	public void setAsstPhNum(String asstPhNum) {
		this.asstPhNum = asstPhNum;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}
	
	
	@Override
	public String toString() {
		return "UserDto [recentSearchId=" + recentSearchId + ", recentSearchId1=" + recentSearchId1 + ", status="
				+ status + ", createdBy=" + createdBy + ", latitude=" + latitude + ", lstUpdatedBy=" + lastUpdatedBy
				+ ", longitude=" + longitude + ", lstUpdateDate=" + lastUpdateDate + ", address=" + address
				+ ", creationDate=" + creationDate + ", homephNum=" + homephNum + ", PositionId=" + PositionId
				+ ", asstPhNum=" + asstPhNum + ", mobileNumber=" + mobileNumber + ", positionId=" + positionId
				+ ", outletId=" + outletId + ", distributorId=" + distributorId + ", userName=" + userName
				+ ", userType=" + userType + ", email=" + email + ", employeeCode=" + employeeCode + ", addressDetails="
				+ addressDetails + "]";
	}
		
	
}
