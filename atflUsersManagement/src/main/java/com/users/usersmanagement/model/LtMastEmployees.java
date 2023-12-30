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
@Table(name = "lt_mast_employees_v")
@JsonInclude(Include.NON_NULL)
public class LtMastEmployees extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "employee_id")
	String employeeId;

	@Column(name = "org_id")
	String orgId;

	@Column(name = "distributor_id")
	String distributorId;

	@Column(name = "position_id")
	String positionId;

	@Column(name = "employee_code")
	String employeeCode;

	@Column(name = "first_name")
	String firstName;

	@Column(name = "last_name")
	String lastName;

	@Column(name = "job_title")
	String jobTitle;

	@Column(name = "employee_type")
	String employeeType;
	
	@Column(name = "primary_mobile")
	String primaryMobile;
	
	@Column(name = "row_number")
	String rowNumber;
	
	@Column(name = "email")
	String email;

	@Transient
	String distributorCode;
	
	@Transient
	String distributorCrmCode;
	
	@Transient
	String distributorName;
	
	@Transient
	String distributorStatus;
	
	@Transient
	String positionStatus;

	@Transient
	private String position;

	@Transient
	private String mobileNumber;
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDistributorStatus() {
		return distributorStatus;
	}

	public void setDistributorStatus(String distributorStatus) {
		this.distributorStatus = distributorStatus;
	}

	public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
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

	
	public String getDistributorCrmCode() {
		return distributorCrmCode;
	}

	public void setDistributorCrmCode(String distributorCrmCode) {
		this.distributorCrmCode = distributorCrmCode;
	}
	
	public String getPrimaryMobile() {
		return primaryMobile;
	}

	public void setPrimaryMobile(String primaryMobile) {
		this.primaryMobile = primaryMobile;
	}
	
	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "LtMastEmployees [employeeId=" + employeeId + ", orgId=" + orgId + ", distributorId=" + distributorId
				+ ", positionId=" + positionId + ", employeeCode=" + employeeCode + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", jobTitle=" + jobTitle + ", employeeType=" + employeeType
				+ ", primaryMobile=" + primaryMobile + ", rowNumber=" + rowNumber + ", email=" + email
				+ ", distributorCode=" + distributorCode + ", distributorCrmCode=" + distributorCrmCode
				+ ", distributorName=" + distributorName + ", distributorStatus=" + distributorStatus
				+ ", positionStatus=" + positionStatus + ", position=" + position + "]";
	}
	
}
