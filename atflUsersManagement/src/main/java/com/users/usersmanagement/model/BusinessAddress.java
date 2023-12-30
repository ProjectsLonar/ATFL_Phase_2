package com.users.usersmanagement.model;

public class BusinessAddress {
private String addressId;
private String streetAddress;
private String county;
private String streetAddress2;
private String city;
private String state;
private String country;
private String postalCode;
private String province;
private String IsPrimaryMVG;
public String getAddressId() {
	return addressId;
}
public void setAddressId(String addressId) {
	this.addressId = addressId;
}
public String getStreetAddress() {
	return streetAddress;
}
public void setStreetAddress(String streetAddress) {
	this.streetAddress = streetAddress;
}
public String getCounty() {
	return county;
}
public void setCounty(String county) {
	this.county = county;
}
public String getStreetAddress2() {
	return streetAddress2;
}
public void setStreetAddress2(String streetAddress2) {
	this.streetAddress2 = streetAddress2;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getPostalCode() {
	return postalCode;
}
public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
}
public String getProvince() {
	return province;
}
public void setProvince(String province) {
	this.province = province;
}
public String getIsPrimaryMVG() {
	return IsPrimaryMVG;
}
public void setIsPrimaryMVG(String isPrimaryMVG) {
	IsPrimaryMVG = isPrimaryMVG;
}
@Override
public String toString() {
	return "BusinessAddress [addressId=" + addressId + ", streetAddress=" + streetAddress + ", county=" + county
			+ ", streetAddress2=" + streetAddress2 + ", city=" + city + ", state=" + state + ", country=" + country
			+ ", postalCode=" + postalCode + ", province=" + province + ", IsPrimaryMVG=" + IsPrimaryMVG + "]";
}

}
