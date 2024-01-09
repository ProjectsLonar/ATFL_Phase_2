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
@Table(name = "lt_mast_outlets_v")
@JsonInclude(Include.NON_NULL)
public class LtMastOutlets extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "outlet_id")
	String outletId;

	@Column(name = "org_id")
	String orgId;

	@Column(name = "outlet_code")
	String outletCode;

	@Column(name = "outlet_type")
	String outletType;

	@Column(name = "outlet_name")
	String outletName;

	@Column(name = "distributor_id")
	String distributorId;

	@Column(name = "proprietor_name")
	String proprietorName;

	@Column(name = "address_1")
	String address1;

	@Column(name = "address_2")
	String address2;

	@Column(name = "address_3")
	String address3;

	@Column(name = "address_4")
	String address4;

	@Column(name = "landmark")
	String landmark;

	@Column(name = "country")
	String country;

	@Column(name = "state")
	String state;

	@Column(name = "city")
	String city;

	@Column(name = "pin_code")
	String pin_code;

	@Column(name = "region")
	String region;

	@Column(name = "area")
	String area;

	@Column(name = "territory")
	String territory;

	@Column(name = "outlet_gstn")
	String outletGstn;

	@Column(name = "outlet_pan")
	String outletPan;

	@Column(name = "licence_no")
	String licenceNo;

	@Column(name = "positions_id")
	String positionsId;

	@Column(name = "phone")
	String phone;

	@Column(name = "email")
	String email;

	@Column(name = "primary_mobile")
	String primaryMobile;
	
	@Column(name = "PRICE_LIST")
	String priceList;
	
	@Transient
	String distributorCode;
	
	@Transient
	String distributorCrmCode;
	
	@Transient
	String distributorStatus;

	@Transient
	String outletChannel;
	@Transient
	String distributorName;
	
	@Transient
	String employeeId;
	
	@Transient
	String empName;
	
	@Transient
	String employeeCode;
	
	@Transient
	String position;
	
	@Transient
	String outletAddress;
	
	@Transient
	Long userId;

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

	public String getOutletPan() {
		return outletPan;
	}

	public void setOutletPan(String outletPan) {
		this.outletPan = outletPan;
	}

	public String getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

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
	
	

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}

	public void setDistributorStatus(String distributorStatus) {
		this.distributorStatus = distributorStatus;
	}
	
	

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

	public String getOutletChannel() {
		return outletChannel;
	}

	public void setOutletChannel(String outletChannel) {
		this.outletChannel = outletChannel;
	}

	@Override
	public String toString() {
		return "LtMastOutlets [outletId=" + outletId + ", orgId=" + orgId + ", outletCode=" + outletCode
				+ ", outletType=" + outletType + ", outletName=" + outletName + ", distributorId=" + distributorId
				+ ", proprietorName=" + proprietorName + ", address1=" + address1 + ", address2=" + address2
				+ ", address3=" + address3 + ", address4=" + address4 + ", landmark=" + landmark + ", country="
				+ country + ", state=" + state + ", city=" + city + ", pin_code=" + pin_code + ", region=" + region
				+ ", area=" + area + ", territory=" + territory + ", outletGstn=" + outletGstn + ", outletPan="
				+ outletPan + ", licenceNo=" + licenceNo + ", positionsId=" + positionsId + ", phone=" + phone
				+ ", email=" + email + ", primaryMobile=" + primaryMobile + ", priceList=" + priceList
				+ ", distributorCode=" + distributorCode + ", distributorCrmCode=" + distributorCrmCode
				+ ", distributorStatus=" + distributorStatus + ", distributorName=" + distributorName + ", employeeId="
				+ employeeId + ", empName=" + empName + ", employeeCode=" + employeeCode + ", position=" + position
				+ ", outletAddress=" + outletAddress + ", userId=" + userId + "]";
	}

	
}

