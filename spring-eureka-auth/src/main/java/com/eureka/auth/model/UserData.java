package com.eureka.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class UserData {

    @JsonProperty("USER_ID")
    private Long userId; // Use Long to match the JSON data type

    @JsonProperty("ORG_ID")
    private String orgId;

    @JsonProperty("DISTRIBUTOR_ID")
    private String distributorId;

    @JsonProperty("OUTLET_ID")
    private String outletId;

    @JsonProperty("MOBILE_NUMBER")
    private String mobileNumber;

    @JsonProperty("USER_TYPE")
    private String userType;

    @JsonProperty("USER_NAME")
    private String userName;

    @JsonProperty("EMPLOYEE_CODE")
    private String employeeCode;

    @JsonProperty("DESIGNATION")
    private String designation;

    @JsonProperty("POSITION_ID")
    private String positionId;

    @JsonProperty("ADDRESS")
    private String address;

    @JsonProperty("EMAIL")
    private String email;

    @JsonProperty("ALTERNATE_NO")
    private String alternateNo;

    @JsonProperty("LATITUDE")
    private String latitude;

    @JsonProperty("LONGITUDE")
    private String longitude;

    @JsonProperty("IMAGE_TYPE")
    private String imageType;

    @JsonProperty("IMAGE_NAME")
    private String imageName;

    @JsonProperty("IMAGE_DATA")
    private String imageData;

    @JsonProperty("ADDRESS_DETAILS")
    private String addressDetails;

    @JsonProperty("RECENT_SEARCH_ID")
    private String recentSearchId;

    @JsonProperty("TOKEN_DATA")
    private String tokenData;

    @JsonProperty("IS_FIRST_LOGIN")
    private String isFirstLogin;

    @JsonProperty("TERRITORY")
    private String territory;

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
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
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

	public String getAlternateNo() {
		return alternateNo;
	}

	public void setAlternateNo(String alternateNo) {
		this.alternateNo = alternateNo;
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

	public String getRecentSearchId() {
		return recentSearchId;
	}

	public void setRecentSearchId(String recentSearchId) {
		this.recentSearchId = recentSearchId;
	}

	public String getTokenData() {
		return tokenData;
	}

	public void setTokenData(String tokenData) {
		this.tokenData = tokenData;
	}

	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	@Override
	public String toString() {
		return "UserData [userId=" + userId + ", orgId=" + orgId + ", distributorId=" + distributorId + ", outletId="
				+ outletId + ", mobileNumber=" + mobileNumber + ", userType=" + userType + ", userName=" + userName
				+ ", employeeCode=" + employeeCode + ", designation=" + designation + ", positionId=" + positionId
				+ ", address=" + address + ", email=" + email + ", alternateNo=" + alternateNo + ", latitude="
				+ latitude + ", longitude=" + longitude + ", imageType=" + imageType + ", imageName=" + imageName
				+ ", imageData=" + imageData + ", addressDetails=" + addressDetails + ", recentSearchId="
				+ recentSearchId + ", tokenData=" + tokenData + ", isFirstLogin=" + isFirstLogin + ", territory="
				+ territory + "]";
	}

    
}
