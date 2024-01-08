package com.users.usersmanagement.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LtUserPersonalDetails {
	
	@NotNull(message="Please enter user id")
	private Long userId;

	
	@NotBlank(message="Please enter user name")
	private String userName;

	@Pattern(regexp="(^[6-9]\\d{9}$)",message = "Mobile Number should be valid")
	private String mobileNumber;

	@Pattern(regexp="(^[6-9]\\d{9}$)",message = "Alternate Mobile Number should be valid")
	private String alternateNo;

	@Email(message = "Email should be valid")
	private String email;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAlternateNo() {
		return alternateNo;
	}

	public void setAlternateNo(String alternateNo) {
		this.alternateNo = alternateNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "LtUserPersonalDetails [userId=" + userId + ", userName=" + userName + ", mobileNumber=" + mobileNumber
				+ ", alternateNo=" + alternateNo + ", email=" + email + "]";
	}
}
