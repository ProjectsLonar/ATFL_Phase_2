package com.users.usersmanagement.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@Column(name = "POSITION_ID")
	private String PositionId;
	
	@Column(name = "ADDRESS")
	private String Address;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ALTERNATE_NO")
	private String alternateNo;
	
	@Column(name = "LATITUDE")
	private String latitude;
	
	@Column(name = "LONGITUDE")
	private String longitude;
	
	@Column(name = "image_type")
	private String imageType;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "image_data")
	private String imageData;
	
	@Column(name = "ADDRESS_DETAILS")
	private String addressDetails;
	
	@Column(name = "RECENT_SEARCH_ID")
	private String recentSerachId;
	
	@Column(name = "TOKEN_DATA")
	private String token;
	
	@Column(name = "ISFIRSTLOGIN")
	private String isFirstLogin;
	
	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	
	@Transient
	String distributorCode;
	
	@Transient
	String primaryMobile;
	
	
	@Transient
	String distributorCrmCode;
	
	@Transient
	String distributorName;
	
	@Transient
	String distributorAddress;
	
	@Transient
	String employeeId;
	
	@Transient
	String empName;
	
	@Transient
	String empCode;
	
	@Transient
	String organisationCode;
	
	@Transient
	String organisationName;
	
	@Transient
	String outletName;
	
	@Transient
	String outletCode;
	
	@Transient
	String outletAddress;
	
	@Transient
	String proprietorName;
	
	@Transient
	String orgStatus;
	
	@Transient
	String position;
	
	@Transient 
	private String territory;
	
	@Transient
	private String inventoryLocationName;
	

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
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
	
	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDesignation() {
		return Designation;
	}

	public void setDesignation(String designation) {
		Designation = designation;
	}

	public String getPositionId() {
		return PositionId;
	}

	public void setPositionId(String positionId) {
		PositionId = positionId;
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}

	public String getRecentSerachId() {
		return recentSerachId;
	}

	public void setRecentSerachId(String recentSerachId) {
		this.recentSerachId = recentSerachId;
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

	public String getDistributorAddress() {
		return distributorAddress;
	}

	public void setDistributorAddress(String distributorAddress) {
		this.distributorAddress = distributorAddress;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getOrganisationCode() {
		return organisationCode;
	}

	public void setOrganisationCode(String organisationCode) {
		this.organisationCode = organisationCode;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

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

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}

	public String getProprietorName() {
		return proprietorName;
	}

	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}

	public String getPrimaryMobile() {
		return primaryMobile;
	}

	public void setPrimaryMobile(String primaryMobile) {
		this.primaryMobile = primaryMobile;
	}
	
	

	public String getInventoryLocationName() {
		return inventoryLocationName;
	}

	public void setInventoryLocationName(String inventoryLocationName) {
		this.inventoryLocationName = inventoryLocationName;
	}

	@Override
	public String toString() {
		return "LtMastUsers [userId=" + userId + ", orgId=" + orgId + ", distributorId=" + distributorId + ", outletId="
				+ outletId + ", mobileNumber=" + mobileNumber + ", userType=" + userType + ", userName=" + userName
				+ ", employeeCode=" + employeeCode + ", Designation=" + Designation + ", PositionId=" + PositionId
				+ ", Address=" + Address + ", email=" + email + ", alternateNo=" + alternateNo + ", latitude="
				+ latitude + ", longitude=" + longitude + ", imageType=" + imageType + ", imageName=" + imageName
				+ ", imageData=" + imageData + ", addressDetails=" + addressDetails + ", recentSerachId="
				+ recentSerachId + ", token=" + token + ", isFirstLogin=" + isFirstLogin + ", distributorCode="
				+ distributorCode + ", primaryMobile=" + primaryMobile + ", distributorCrmCode=" + distributorCrmCode
				+ ", distributorName=" + distributorName + ", distributorAddress=" + distributorAddress
				+ ", employeeId=" + employeeId + ", empName=" + empName + ", empCode=" + empCode + ", organisationCode="
				+ organisationCode + ", organisationName=" + organisationName + ", outletName=" + outletName
				+ ", outletCode=" + outletCode + ", outletAddress=" + outletAddress + ", proprietorName="
				+ proprietorName + ", orgStatus=" + orgStatus + ", position=" + position + ", territory=" + territory
				+ ", inventoryLocationName=" + inventoryLocationName + "]";
	}

	
	
}
