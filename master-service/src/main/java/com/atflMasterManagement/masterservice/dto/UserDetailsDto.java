package com.atflMasterManagement.masterservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON

public class UserDetailsDto {

	private String userType;
	private String distributorId;
	private String userId;
	
	private String positionId;
	
	private String employeeCode;

	
	
	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	
	@Override
	public String toString() {
		return "UserDetailsDto [userType=" + userType + ", distributorId=" + distributorId + ", userId=" + userId
				+ ", positionId=" + positionId + ", employeeCode=" + employeeCode + "]";
	}

	
}
