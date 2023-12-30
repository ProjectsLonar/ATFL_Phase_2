package com.lonar.sms.atflSmsService.model;

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
@Table(name = "lt_mast_logins")
@JsonInclude(Include.NON_NULL)
public class LtMastLogins {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LT_MAST_LOGINS_S")
	@SequenceGenerator(name = "LT_MAST_LOGINS_S", sequenceName = "LT_MAST_LOGINS_S", allocationSize = 1)
	@Column(name = "LOGIN_ID")
	private Long loginId;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "LOGIN_DATE")
	private Date loginDate;
	
	@Column(name = "IP_ADDRESS")
	private String ipAddress;
	
	@Column(name = "DEVICE")
	private String device;
	
	@Column(name = "OTP")
	private Long otp;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "TOKEN_ID")
	private String tokenId;

	@Transient
	private String mobile;

	@Transient
	private Long supplierId;

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Long getOtp() {
		return otp;
	}

	public void setOtp(Long otp) {
		this.otp = otp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	public String toString() {
		return "LtMastLogins [loginId=" + loginId + ", userId=" + userId + ", loginDate=" + loginDate + ", ipAddress="
				+ ipAddress + ", device=" + device + ", otp=" + otp + ", status=" + status + ", tokenId=" + tokenId
				+ ", mobile=" + mobile + ", supplierId=" + supplierId + "]";
	}

	
}
