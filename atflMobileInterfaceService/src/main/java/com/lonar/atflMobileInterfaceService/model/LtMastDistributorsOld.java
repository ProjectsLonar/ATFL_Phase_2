package com.lonar.atflMobileInterfaceService.model;

import java.util.Date;

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
@Table(name = "LT_MAST_DISTRIBUTORS")
@JsonInclude(Include.NON_NULL)
public class LtMastDistributors extends BaseClass {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "DISTRIBUTOR_ID")
	private Long distributorId;
	
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
	private String address_1;
	
	@Column(name = "ADDRESS_2")
	private String address_2;
	
	@Column(name = "ADDRESS_3")
	private String address_3;
	
	@Column(name = "ADDRESS_4")
	private String address_4;
	
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
	private String distributorGstn;
	
	@Column(name = "DISTRIBUTOR_PAN")
	private String distributorPan;
	
	@Column(name = "LICENCE_NO")
	private String licenceNo;
	
	@Column(name = "PHONE")
	private String phone;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "PRIMARY_MOBILE")
	private String primaryMobile;
	
	@Column(name = "positions")
	String positions; 
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Transient
	private Long userId;
	
	
	@Column(name = "distributor_crm_code")
	private String distributorCrmCode;
	
	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
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

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getAddress_3() {
		return address_3;
	}

	public void setAddress_3(String address_3) {
		this.address_3 = address_3;
	}

	public String getAddress_4() {
		return address_4;
	}

	public void setAddress_4(String address_4) {
		this.address_4 = address_4;
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

	public String getDistributorGstn() {
		return distributorGstn;
	}

	public void setDistributorGstn(String distributorGstn) {
		this.distributorGstn = distributorGstn;
	}

	public String getDistributorPan() {
		return distributorPan;
	}

	public void setDistributorPan(String distributorPan) {
		this.distributorPan = distributorPan;
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

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}
	
	
}
