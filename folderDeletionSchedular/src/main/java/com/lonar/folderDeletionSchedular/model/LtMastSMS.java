package com.lonar.folderDeletionSchedular.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_master_sms")
@JsonInclude(Include.NON_NULL)
public class LtMastSMS {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sms_id")
	private Long smsId;

	@Column(name = "sms_body")
	String smsBody;

	@Column(name = "sms_send_to")
	String smsSendTo;

	@Column(name = "subject")
	String subject;

	@Column(name = "status")
	String status;

	public Long getSmsId() {
		return smsId;
	}

	public void setSmsId(Long smsId) {
		this.smsId = smsId;
	}

	public String getSmsBody() {
		return smsBody;
	}

	public void setSmsBody(String smsBody) {
		this.smsBody = smsBody;
	}

	public String getSmsSendTo() {
		return smsSendTo;
	}

	public void setSmsSendTo(String smsSendTo) {
		this.smsSendTo = smsSendTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LtMastSMS [smsId=" + smsId + ", smsBody=" + smsBody + ", smsSendTo=" + smsSendTo + ", subject="
				+ subject + ", status=" + status + "]";
	}
	
}
