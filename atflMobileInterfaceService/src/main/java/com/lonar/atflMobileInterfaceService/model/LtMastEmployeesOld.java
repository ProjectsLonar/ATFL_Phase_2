package com.lonar.atflMobileInterfaceService.model;

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
@Table(name = "lt_mast_employees")
@JsonInclude(Include.NON_NULL)
public class LtMastEmployees extends BaseClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	Long employeeId;

	@Column(name = "org_id")
	Long orgId;

	@Column(name = "distributor_id")
	Long distributorId;

	@Column(name = "position_id")
	Long positionId;

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
	
	@Column(name = "position")
	String position;
	
	@Column(name = "PRIMARY_MOBILE")
	String primaryMobile;
	
	@Column(name = "ROW_NUMBER")
	String rowNumber;
	
	@Column(name = "email")
	String email;
	
	@Transient
	private String parentPosition;
	
	@Transient
	private String primaryPositionId;
	
	@Transient
	private Long userId;
	
	@Transient
	String distributorCode;
	
	@Transient
	String positionCode;
	
	@Transient
	Long positionCodeId;

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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

	public String getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(String parentPosition) {
		this.parentPosition = parentPosition;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrimaryPositionId() {
		return primaryPositionId;
	}

	public void setPrimaryPositionId(String primaryPositionId) {
		this.primaryPositionId = primaryPositionId;
	}

	public Long getPositionCodeId() {
		return positionCodeId;
	}

	public void setPositionCodeId(Long positionCodeId) {
		this.positionCodeId = positionCodeId;
	}

	@Override
	public String toString() {
		return "LtMastEmployees [employeeId=" + employeeId + ", orgId=" + orgId + ", distributorId=" + distributorId
				+ ", positionId=" + positionId + ", employeeCode=" + employeeCode + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", jobTitle=" + jobTitle + ", employeeType=" + employeeType + ", position="
				+ position + ", primaryMobile=" + primaryMobile + ", rowNumber=" + rowNumber + ", email=" + email
				+ ", parentPosition=" + parentPosition + ", primaryPositionId=" + primaryPositionId + ", userId="
				+ userId + ", distributorCode=" + distributorCode + ", positionCode=" + positionCode
				+ ", positionCodeId=" + positionCodeId + "]";
	}
	
	
}
