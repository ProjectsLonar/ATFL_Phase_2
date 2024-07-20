package com.lonar.cartservice.atflCartService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_outlets_v")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties in JSON
public class LtMastOutles extends BaseClass1 {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	//@SequenceGenerator(name = "LT_MAST_OUTLETS_V_S", sequenceName = "LT_MAST_OUTLETS_V_S", allocationSize = 1)
	@Column(name = "OUTLET_ID")
	//Long outletId;
	String outletId;

	
	@Column(name = "ORG_ID")
//	Long orgId;
	String orgId;
	
	@Column(name = "OUTLET_CODE")
	String outletCode;

	@Column(name = "OUTLET_TYPE")
	String outletType;

	@Column(name = "OUTLET_NAME")
	String outletName;

	@Column(name = "DISTRIBUTOR_ID")
//	Long distributorId;
	String distributorId;

	@Column(name = "PROPRIETOR_NAME")
	String proprietorName;

	@Column(name = "ADDRESS_1")
	String address_1;

	@Column(name = "ADDRESS_2")
	String address_2;

	@Column(name = "ADDRESS_3")
	String address_3;

	@Column(name = "ADDRESS_4")
	String address_4;

	@Column(name = "LANDMARK")
	String landmark;

	@Column(name = "COUNTRY")
	String country;

	@Column(name = "STATE")
	String state;

	@Column(name = "CITY")
	String city;

	@Column(name = "PIN_CODE")
	String pin_code;

	@Column(name = "REGION")
	String region;

	@Column(name = "AREA")
	String area;

	@Column(name = "TERRITORY")
	String territory;

	@Column(name = "OUTLET_GSTN")
	String outletGstn;

//	@Column(name = "outlet_pan")
//	String outletPan;

//	@Column(name = "licence_no")
//	String licenceNo;

	@Column(name = "POSITIONS_ID")
//	Long positionsId;
	String positionsId;
	
	@Column(name = "PHONE")
	String phone;

	@Column(name = "EMAIL")
	String email;

	@Column(name = "PRIMARY_MOBILE")
	String primaryMobile;
	
	@Transient
	String distributorCode;
	
	@Transient
	String distributorStatus;
	
	@Transient
	String distributorName;
	
	@Transient
//	Long employeeId;
	String employeeId;
	
	@Transient
	String empName;
	
	@Transient
	String employeeCode;
	
	@Transient
	String position;
	
	@Transient
	String outletAddress;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOutletCode() {
		return outletCode;
	}

	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}

	public String getOutletType() {
		return outletType;
	}

	public void setOutletType(String outletType) {
		this.outletType = outletType;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getProprietorName() {
		return proprietorName;
	}

	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}

	public String getAddress1() {
		return address_1;
	}

	public void setAddress1(String address1) {
		this.address_1 = address1;
	}

	public String getAddress2() {
		return address_2;
	}

	public void setAddress2(String address2) {
		this.address_2 = address2;
	}

	public String getAddress3() {
		return address_3;
	}

	public void setAddress3(String address3) {
		this.address_3 = address3;
	}

	public String getAddress4() {
		return address_4;
	}

	public void setAddress4(String address4) {
		this.address_4 = address4;
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

//	public String getOutletPan() {
//		return outletPan;
//	}
//
//	public void setOutletPan(String outletPan) {
//		this.outletPan = outletPan;
//	}

//	public String getLicenceNo() {
//		return licenceNo;
//	}
//
//	public void setLicenceNo(String licenceNo) {
//		this.licenceNo = licenceNo;
//	}

	public String getPositionsId() {
		return positionsId;
	}

	public void setPositionsId(String positionsId) {
		this.positionsId = positionsId;
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

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDistributorStatus() {
		return distributorStatus;
	}

	@Override
	public String toString() {
		return "LtMastOutlets [outletId=" + outletId + ", orgId=" + orgId + ", outletCode=" + outletCode
				+ ", outletType=" + outletType + ", outletName=" + outletName + ", distributorId=" + distributorId
				+ ", proprietorName=" + proprietorName + ", address1=" + address_1 + ", address2=" + address_2
				+ ", address3=" + address_3 + ", address4=" + address_4 + ", landmark=" + landmark + ", country="
				+ country + ", state=" + state + ", city=" + city + ", pin_code=" + pin_code + ", region=" + region
				+ ", area=" + area + ", territory=" + territory + ", outletGstn=" + outletGstn + ",  email=" + email + ", primaryMobile=" + primaryMobile + ", distributorCode=" + distributorCode
				+ ", distributorStatus=" + distributorStatus + ", distributorName=" + distributorName + ", employeeId="
				+ employeeId + ", empName=" + empName + ", employeeCode=" + employeeCode + "]";
	}

	public void setDistributorStatus(String distributorStatus) {
		this.distributorStatus = distributorStatus;
	}

	
}
