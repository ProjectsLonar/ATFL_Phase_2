package com.lonar.cartservice.atflCartService.model;

import java.util.Date;

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
@Table(name = "LT_NOTIFICATION")
@JsonInclude(Include.NON_NULL)
public class NotificationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_NOTIFICATION_S")
	@SequenceGenerator(name = "LT_NOTIFICATION_S", sequenceName = "LT_NOTIFICATION_S", allocationSize = 1)
	@Column(name = "NOTIFICATION_ID")
	private Long notificationId;
	//private String notificationId;
	
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;

	@Column(name = "user_id")
private Long userId;
	//private String userId;
	
	@Column(name = "NOTIFICATION_TITLE")
	private String notificationTitle;

	@Column(name = "NOTIFICATION_BODY")
	private String notificationBody;

	@Column(name = "SEND_DATE")
	private Date sendDate;

	@Column(name = "NOTIFICATION_STATUS")
	private String notificationStatus;

	@Column(name = "READ_FLAG")
	private String readFlag;

	@Column(name = "distributor_id")
//	private Long distributorId;
	private String distributorId;

	@Transient
	private String tokenId;

	@Transient
	private Integer start;

	@Transient
	private Integer length;

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
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

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
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

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
}