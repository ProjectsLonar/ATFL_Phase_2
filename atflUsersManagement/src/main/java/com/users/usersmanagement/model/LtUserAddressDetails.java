package com.users.usersmanagement.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LtUserAddressDetails {
	
	@NotNull(message="Please enter user id")
	private String userId;

	@NotBlank(message="Please enter user Address")
	private String Address;
	
	@NotBlank(message="Please enter latitude")
	private String latitude;
	
	@NotBlank(message="Please enter longitude")
	private String longitude;
	
	@NotBlank(message="Please enter address details")
	private String addressDetails;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}

	@Override
	public String toString() {
		return "LtUserAddressDetails [userId=" + userId + ", Address=" + Address + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", addressDetails=" + addressDetails + "]";
	}
	
}
