package com.users.usersmanagement.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "LT_ORGANISATION_SMS")
@XmlRootElement
public class LtOrganisationSMS implements Serializable  {

	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ORG_SMS_ID")
	private Long organisationSMSId;
	
	@Column(name = "ORG_ID")
	private Long orgId;

	@Column(name = "AUTHKEY")
	private String authkey;
	
	@Column(name = "SMS_URL")
	private String smsUrl;
	
	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "TEMPLATE_ID")
	private String templateId;
	
	@Column(name = "TEMPLATE_TYPE")
	private String templateType;
	
	@Column(name = "TEMPLATE_BODY")
	private String templateBody;

	public Long getOrganisationSMSId() {
		return organisationSMSId;
	}

	public void setOrganisationSMSId(Long organisationSMSId) {
		this.organisationSMSId = organisationSMSId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}
	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getTemplateBody() {
		return templateBody;
	}

	public void setTemplateBody(String templateBody) {
		this.templateBody = templateBody;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAuthkey() {
		return authkey;
	}

	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}
