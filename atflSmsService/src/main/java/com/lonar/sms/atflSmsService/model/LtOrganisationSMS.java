package com.lonar.sms.atflSmsService.model;

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

	@Column(name = "SERIAL_ID")
	private String serialId;
	
	@Column(name = "SMS_URL")
	private String smsUrl;
	
	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "API_KEY")
	private String apiKey;
	
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

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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
}
