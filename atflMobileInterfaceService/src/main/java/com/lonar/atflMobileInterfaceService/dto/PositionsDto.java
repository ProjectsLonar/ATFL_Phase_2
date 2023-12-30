package com.lonar.atflMobileInterfaceService.dto;

public class PositionsDto extends BaseClass {

	Long positionId;
	
	Long OrgId;

	Long distributorId;
	
	String distributorCode;

	String position;
	
	String positionCode;

	String parentPosition;

	String PositionType;

	String firstName;

	String lastName;

	String jobTitle;

	String startDate;

	String endDate;

	String mobileStatus;

	String mobileRemarks;

	String mobileInsertUpdate;
	
	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public Long getOrgId() {
		return OrgId;
	}

	public void setOrgId(Long orgId) {
		OrgId = orgId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(String parentPosition) {
		this.parentPosition = parentPosition;
	}

	public String getPositionType() {
		return PositionType;
	}

	public void setPositionType(String positionType) {
		PositionType = positionType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus(String mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public String getMobileRemarks() {
		return mobileRemarks;
	}

	public void setMobileRemarks(String mobileRemarks) {
		this.mobileRemarks = mobileRemarks;
	}

	public String getMobileInsertUpdate() {
		return mobileInsertUpdate;
	}

	public void setMobileInsertUpdate(String mobileInsertUpdate) {
		this.mobileInsertUpdate = mobileInsertUpdate;
	}

}
