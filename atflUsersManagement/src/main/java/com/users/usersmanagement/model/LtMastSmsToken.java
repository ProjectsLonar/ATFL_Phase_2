package com.users.usersmanagement.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LT_MAST_SMS_TOKENS")
public class LtMastSmsToken {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "LT_MAST_SMS_TOKENS_S")
	@SequenceGenerator(name = "LT_MAST_SMS_TOKENS_S", sequenceName = "LT_MAST_SMS_TOKENS_S", allocationSize = 1)
	@Column(name = "SMS_TOKEN_ID")
	private Long smsTokenId;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "TRANSACTION_ID")
	private String transactionId;
	
	@Column(name = "SEND_DATE")
	private Date sendDate;
	
	@Column(name = "SMS_STATUS")
	private String smsStatus;
	
	@Column(name = "SMS_OBJECT")
	private String smsObject;

	@Column(name = "SEND_TO")
	private Long sendTo;
	
	@Column(name = "SMS_TYPE")
	private String smsType;
	
	@Transient
	private String orgId;

	@Transient
	private String distributorId;
	
	@Transient
	private String outletId;

	public Long getSmsTokenId() {
		return smsTokenId;
	}

	public void setSmsTokenId(Long smsTokenId) {
		this.smsTokenId = smsTokenId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	public String getSmsObject() {
		return smsObject;
	}

	public void setSmsObject(String smsObject) {
		this.smsObject = smsObject;
	}

	public Long getSendTo() {
		return sendTo;
	}

	public void setSendTo(Long sendTo) {
		this.sendTo = sendTo;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
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

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	@Override
	public String toString() {
		return "LtMastSmsToken [smsTokenId=" + smsTokenId + ", userId=" + userId + ", transactionId=" + transactionId
				+ ", sendDate=" + sendDate + ", smsStatus=" + smsStatus + ", smsObject=" + smsObject + ", sendTo="
				+ sendTo + ", smsType=" + smsType + ", orgId=" + orgId + ", distributorId=" + distributorId
				+ ", outletId=" + outletId + "]";
	}
	
}
