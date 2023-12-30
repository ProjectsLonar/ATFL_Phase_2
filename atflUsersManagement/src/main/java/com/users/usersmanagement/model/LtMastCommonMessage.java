package com.users.usersmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LT_MAST_COMMON_MESSAGES")
@JsonInclude(Include.NON_NULL)
public class LtMastCommonMessage extends BaseClass {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "MESSAGE_ID")
	private Long messageId;
	
	@Column(name = "MESSAGE_CODE")
	private String messageCode;
	
	@Column(name = "MESSAGE_NAME")
	private String messageName;
	
	@Column(name = "MESSAGE_DESC")
	private String messageDesc;
	
	@Transient
	private String stDate;
	
	@Transient
	private String enDate;

	@Transient
	private String sort;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getMessageDesc() {
		return messageDesc;
	}

	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}

	public String getStDate() {
		return stDate;
	}

	public void setStDate(String stDate) {
		this.stDate = stDate;
	}

	public String getEnDate() {
		return enDate;
	}

	public void setEnDate(String enDate) {
		this.enDate = enDate;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "LtMastCommonMessage [messageId=" + messageId + ", messageCode=" + messageCode + ", messageName="
				+ messageName + ", messageDesc=" + messageDesc + ", stDate=" + stDate + ", enDate=" + enDate + ", sort="
				+ sort + "]";
	}

	
}
