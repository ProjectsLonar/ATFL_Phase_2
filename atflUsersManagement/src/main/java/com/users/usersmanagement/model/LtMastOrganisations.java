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
@Table(name = "LT_MAST_ORGANISATIONS")
@JsonInclude(Include.NON_NULL)
public class LtMastOrganisations extends BaseClass {

	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_MAST_ORGANISATIONS_S")
	@SequenceGenerator(name = "LT_MAST_ORGANISATIONS_S", sequenceName = "LT_MAST_ORGANISATIONS_S", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "ORGANISATION_CODE")
	private String organisationCode;

	@Column(name = "ORGANISATION_NAME")
	private String organisationName;

	@Transient
	String employeeCode;

	@Transient
	String userName;
	
	@Transient
	String employeeStatus;
	
	@Transient
	String positionId;
	
	@Transient
	String position;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public static Long getSerialversionuid() {
		return serialVersionUID;
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

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}
