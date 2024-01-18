package com.lonar.cartservice.atflCartService.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_EMAIL")
@JsonInclude(Include.NON_NULL)
public class LtMastEmail implements Serializable {


	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "LT_MAST_EMAIL_SEQ")
	@SequenceGenerator(name = "LT_MAST_EMAIL_SEQ", sequenceName = "LT_MAST_EMAIL_SEQ", allocationSize = 1)
	@Column(name = "EMAIL_TOKEN_ID")
	private Long emailTokenId;

	@Column(name = "org_id")
	private String orgId;
	
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;

	@Column(name = "order_number")
	private String orderNumber;
	
	@Column(name = "SEND_DATE")
	private Date sendDate;

	@Column(name = "EXPIRED_WITHIN")
	private Long expiredWithin;

	@Column(name = "EMAIL_TEMPLATE")
	private String emailTemplate;

	@Column(name = "EMAIL_STATUS")
	private String emailStatus;

	@Column(name = "email_body")
	private String emailBody;

	@Column(name = "EMAIL_subject")
	private String emailSubject;

	@Column(name = "SEND_TO")
	private String sendTo;

	@Column(name = "SEND_CC")
	private String sendCc;

	@Column(name = "ATTACHMENT_PATH")
	private String attachmentPath;

	@Column(name = "FAILURE_COUNT")
	private Long failureCount;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	public Long getEmailTokenId() {
		return emailTokenId;
	}

	public void setEmailTokenId(Long emailTokenId) {
		this.emailTokenId = emailTokenId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Long getExpiredWithin() {
		return expiredWithin;
	}

	public void setExpiredWithin(Long expiredWithin) {
		this.expiredWithin = expiredWithin;
	}

	public String getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getSendCc() {
		return sendCc;
	}

	public void setSendCc(String sendCc) {
		this.sendCc = sendCc;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public Long getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "LtMastEmail [emailTokenId=" + emailTokenId + ", orgId=" + orgId + ", transactionId=" + transactionId
				+ ", orderNumber=" + orderNumber + ", sendDate=" + sendDate + ", expiredWithin=" + expiredWithin
				+ ", emailTemplate=" + emailTemplate + ", emailStatus=" + emailStatus + ", emailBody=" + emailBody
				+ ", emailSubject=" + emailSubject + ", sendTo=" + sendTo + ", sendCc=" + sendCc + ", attachmentPath="
				+ attachmentPath + ", failureCount=" + failureCount + ", creationDate=" + creationDate
				+ ", lastUpdateDate=" + lastUpdateDate + "]";
	}

	
	
}
