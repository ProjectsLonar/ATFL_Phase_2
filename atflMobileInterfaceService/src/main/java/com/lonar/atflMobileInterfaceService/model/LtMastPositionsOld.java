package com.lonar.atflMobileInterfaceService.model;

import javax.persistence.Basic;
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
@Table(name = "LT_MAST_POSITIONS")
@JsonInclude(Include.NON_NULL)
public class LtMastPositions extends BaseClass{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "POSITION_ID")
	private Long positionId;
	
	@Column(name = "ORG_ID")
	private Long OrgId;
	
	@Column(name = "DISTRIBUTOR_ID")
	private Long distributorId;
	
	@Column(name = "POSITION")
	private String position;
	
	@Column(name = "PARENT_POSITION")
	private String parentPosition;

	@Column(name = "POSITION_TYPE")
	private String PositionType;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "JOB_TITLE")
	private String jobTitle;

	@Column(name = "START_DATE")
	private String startDate;
	
	@Column(name = "END_DATE")
	private String endDate;
	
	@Column(name = "POSITION_CODE")
	private String positionCode;

	@Transient
	private String distributorCode;
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
}
