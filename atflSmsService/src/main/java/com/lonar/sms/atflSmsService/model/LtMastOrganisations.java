package com.lonar.sms.atflSmsService.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_ORGANISATIONS")
@JsonInclude(Include.NON_NULL)
public class LtMastOrganisations extends BaseClass{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic(optional = false)
	@Column(name = "ORG_ID")
	private String orgId;
	
	@Column(name = "ORGANISATION_CODE")
	private String organisationCode;
	
	@Column(name = "ORGANISATION_NAME")
	private String organisationName;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "LtMastOrganisations [orgId=" + orgId + ", organisationCode=" + organisationCode + ", organisationName="
				+ organisationName + "]";
	}
	
	

}
