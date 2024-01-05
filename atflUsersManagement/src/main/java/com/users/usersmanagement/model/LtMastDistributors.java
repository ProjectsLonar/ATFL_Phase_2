package com.users.usersmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_DISTRIBUTORS_v")
@JsonInclude(Include.NON_NULL)
public class LtMastDistributors extends BaseClass {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE)
	@Column(name = "DISTRIBUTOR_ID")
	private String distributorId;
	
	@Column(name = "ORG_ID")
	String orgId;
	
	@Column(name = "DISTRIBUTOR_CODE")
	String distributorCode;
	
	@Column(name = "DISTRIBUTOR_TYPE")
	String distributorType;
	
	@Column(name = "DISTRIBUTOR_NAME")
	String distributorName;
	
	@Column(name = "PROPRIETOR_NAME")
	private String proprietorName;
	
	@Column(name = "ADDRESS_1")
	private String address1;
	
	@Column(name = "ADDRESS_2")
	private String address2;
	
	@Column(name = "ADDRESS_3")
	private String address3;
	
	@Column(name = "ADDRESS_4")
	private String address4;
	
	@Column(name = "LANDMARK")
	private String landMark;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "PIN_CODE")
	private String pinCode;
	
	@Column(name = "REGION")
	private String region;
	
	@Column(name = "AREA")
	private String area;
	
	@Column(name = "TERRITORY")
	private String territory;
	
	@Column(name = "DISTRIBUTOR_GSTN")
	private String distributorGSTN;
	
	@Column(name = "DISTRIBUTOR_PAN")
	private String distributorPAN;
	
	@Column(name = "LICENCE_NO")
	private String licenceNo;
	
	@Column(name = "PHONE")
	private String phone;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "PRIMARY_MOBILE")
	private String primaryMobile;
	
	@Column(name = "DISTRIBUTOR_CRM_CODE")
	private String distributorCrmCode;
	
	@Transient
	String employeeCode;

	@Transient
	String userName;
	
	@Transient
	String employeeId;  
	
	@Transient
	String positionName;
	
	@Transient
	String position;
	
	@Transient
	String positionId;
	
	@Transient
	String positionStatus;
	
	@Transient
	String empStatus;

	@Transient
	String distributorOperator;
	
	@Transient
	String templateProductCount;
	
	@Transient
	String templateHeaderId;
	
	@Transient
	String templateCreationDate;
	
	
	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getDistributorType() {
		return distributorType;
	}

	public void setDistributorType(String distributorType) {
		this.distributorType = distributorType;
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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
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

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
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

	public String getDistributorGSTN() {
		return distributorGSTN;
	}

	public void setDistributorGSTN(String distributorGSTN) {
		this.distributorGSTN = distributorGSTN;
	}

	public String getDistributorPAN() {
		return distributorPAN;
	}

	public void setDistributorPAN(String distributorPAN) {
		this.distributorPAN = distributorPAN;
	}

	public String getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
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

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	
	public String getDistributorOperator() {
		return distributorOperator;
	}

	public void setDistributorOperator(String distributorOperator) {
		this.distributorOperator = distributorOperator;
	}
	
	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}
	
	

	public String getTemplateProductCount() {
		return templateProductCount;
	}

	public void setTemplateProductCount(String templateProductCount) {
		this.templateProductCount = templateProductCount;
	}

	public String getTemplateHeaderId() {
		return templateHeaderId;
	}

	public void setTemplateHeaderId(String templateHeaderId) {
		this.templateHeaderId = templateHeaderId;
	}

	public String getTemplateCreationDate() {
		return templateCreationDate;
	}

	public void setTemplateCreationDate(String templateCreationDate) {
		this.templateCreationDate = templateCreationDate;
	}

	@Override
	public String toString() {
		return "LtMastDistributors [distributorId=" + distributorId + ", orgId=" + orgId + ", distributorCode="
				+ distributorCode + ", distributorType=" + distributorType + ", distributorName=" + distributorName
				+ ", proprietorName=" + proprietorName + ", address1=" + address1 + ", address2=" + address2
				+ ", address3=" + address3 + ", address4=" + address4 + ", landMark=" + landMark + ", country="
				+ country + ", state=" + state + ", city=" + city + ", pinCode=" + pinCode + ", region=" + region
				+ ", area=" + area + ", territory=" + territory + ", distributorGSTN=" + distributorGSTN
				+ ", distributorPAN=" + distributorPAN + ", licenceNo=" + licenceNo + ", phone=" + phone + ", email="
				+ email + ", primaryMobile=" + primaryMobile + ", distributorCrmCode=" + distributorCrmCode
				+ ", employeeCode=" + employeeCode + ", userName=" + userName + ", employeeId=" + employeeId
				+ ", positionName=" + positionName + ", position=" + position + ", positionId=" + positionId
				+ ", positionStatus=" + positionStatus + ", empStatus=" + empStatus + ", distributorOperator="
				+ distributorOperator + ", templateProductCount=" + templateProductCount + ", templateHeaderId="
				+ templateHeaderId + ", templateCreationDate=" + templateCreationDate + "]";
	}

}
