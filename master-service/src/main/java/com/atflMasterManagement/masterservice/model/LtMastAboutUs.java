package com.atflMasterManagement.masterservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_mast_about_us")
@JsonInclude(Include.NON_NULL)
public class LtMastAboutUs extends BaseClass{
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE)
	@Column(name = "about_us_id")
	String aboutUsId;
	
	@Column(name="about_us")
	String aboutUs;
	
	@Column(name="org_support_contact")
	String orgSupportContact;
	
	@Column(name="org_support_email")
	String orgSupportEmail;
	
	@Column(name="distributors_support_contact")
	String distributorsSupportContact;
	
	@Column(name="distributors_support_email")
	String distributorsSupportEmail;
	
	@Column(name="distributor_code")
	String distributorCode;

	public String getAboutUsId() {
		return aboutUsId;
	}

	public void setAboutUsId(String aboutUsId) {
		this.aboutUsId = aboutUsId;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	public String getOrgSupportContact() {
		return orgSupportContact;
	}

	public void setOrgSupportContact(String orgSupportContact) {
		this.orgSupportContact = orgSupportContact;
	}

	public String getOrgSupportEmail() {
		return orgSupportEmail;
	}

	public void setOrgSupportEmail(String orgSupportEmail) {
		this.orgSupportEmail = orgSupportEmail;
	}

	public String getDistributorsSupportContact() {
		return distributorsSupportContact;
	}

	public void setDistributorsSupportContact(String distributorsSupportContact) {
		this.distributorsSupportContact = distributorsSupportContact;
	}

	public String getDistributorsSupportEmail() {
		return distributorsSupportEmail;
	}

	public void setDistributorsSupportEmail(String distributorsSupportEmail) {
		this.distributorsSupportEmail = distributorsSupportEmail;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	@Override
	public String toString() {
		return "LtMastAboutUs [aboutUsId=" + aboutUsId + ", aboutUs=" + aboutUs + ", orgSupportContact="
				+ orgSupportContact + ", orgSupportEmail=" + orgSupportEmail + ", distributorsSupportContact="
				+ distributorsSupportContact + ", distributorsSupportEmail=" + distributorsSupportEmail
				+ ", distributorCode=" + distributorCode + "]";
	}

	
}
