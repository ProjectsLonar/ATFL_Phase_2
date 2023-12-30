package com.lonar.sms.atflSmsService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LT_MAST_NOTIFICATION_TOKEN")
public class LtMastNotificationToken {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "NOTIFICATION_ID")
	private Long notificationId;
	
	@Column(name = "SUPPLIER_ID")
	private Long supplierId;
	
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "NOTIFICATION_TITLE")
	private String notificationTitle;
	
	@Column(name = "NOTIFICATION_BODY")
	private String notificationBody;
	
	@Column(name = "SEND_DATE")
	private Date sendDate;
	
	@Column(name = "NOTIFICATION_STATUS")
	private String notificationStatus;
	
	@Column(name = "TOKEN_ID")
	private String tokenId;

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationBody() {
		return notificationBody;
	}

	public void setNotificationBody(String notificationBody) {
		this.notificationBody = notificationBody;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public String toString() {
		return "LtMastNotificationToken [notificationId=" + notificationId + ", supplierId=" + supplierId
				+ ", transactionId=" + transactionId + ", userId=" + userId + ", notificationTitle=" + notificationTitle
				+ ", notificationBody=" + notificationBody + ", sendDate=" + sendDate + ", notificationStatus="
				+ notificationStatus + ", tokenId=" + tokenId + "]";
	}

	
	
	
}
