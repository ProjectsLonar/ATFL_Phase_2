package com.lonar.sms.atflSmsService.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_USERS")
@JsonInclude(Include.NON_NULL)
public class LtMastUsers extends BaseClass {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_MAST_USERS_S")
	@SequenceGenerator(name = "LT_MAST_USERS_S", sequenceName = "LT_MAST_USERS_S", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "DISTRIBUTOR_ID")
	private String distributorId;

	@Column(name = "OUTLET_ID")
	private String outletId;

	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "DESIGNATION")
	private String Designation;
	
	@Column(name = "POSITION")
	private String Position;
	
	@Column(name = "ADDRESS")
	private String Address;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ALTERNATE_NO")
	private String alternateNo;

	
	@Column(name = "ISFIRSTLOGIN")
	private String isFirstLogin;
	
	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDesignation() {
		return Designation;
	}

	public void setDesignation(String designation) {
		Designation = designation;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlternateNo() {
		return alternateNo;
	}

	public void setAlternateNo(String alternateNo) {
		this.alternateNo = alternateNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	
	
}
