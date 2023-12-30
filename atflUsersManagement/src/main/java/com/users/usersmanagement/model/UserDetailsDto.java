package com.users.usersmanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserDetailsDto {

	private String userType;
	private String distributorId;
	private String userId;

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
}
