package com.eureka.auth.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String mobileNumber;
	private List<String> roles;
	private String status;
	private String userId;
	private String userType;
	private String employeeCode;
	private String orgId;
	private String userName;
	private String distributorId;
	private String outletId;
	private String designation;
	private String positionId;
	private String notifyToken;
	private String outletName;
	private String distributorCode;
	private String distributorName;
	private String proprietorName;
	private String position;
	

	public JwtResponse() {
		super();
	}

	public JwtResponse(String token, String type, String mobileNumber, List<String> roles, String status, String userId,
			String userType, String employeeCode, String orgId, String userName, String distributorId, String outletId,
			String designation, String positionId, String notifyToken,String outletName,String distributorCode,String distributorName,
			String proprietorName,String position) {
		super();
		this.token = token;
		this.type = type;
		this.mobileNumber = mobileNumber;
		this.roles = roles;
		this.status = status;
		this.userId = userId;
		this.userType = userType;
		this.employeeCode = employeeCode;
		this.orgId = orgId;
		this.userName = userName;
		this.distributorId = distributorId;
		this.outletId = outletId;
		this.designation = designation;
		this.positionId = positionId;
		this.notifyToken=notifyToken;
		this.outletName= outletName;
		this.distributorCode = distributorCode;
		this.distributorName = distributorName;
		this.proprietorName= proprietorName;
		this.position = position;
	
	}
	

	public String getNotifyToken() {
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken) {
		this.notifyToken = notifyToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getProprietorName() {
		return proprietorName;
	}

	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}



}
