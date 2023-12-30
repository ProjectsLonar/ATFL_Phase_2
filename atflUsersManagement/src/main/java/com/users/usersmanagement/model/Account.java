package com.users.usersmanagement.model;

import java.util.List;

public class Account {
private String accountStatus;
private String type;
private String accountId;
private String ruleAttribute2;
private String name;
private String aTTerritory;
private String location;
private ListOfBusinessAddress listOfBusinessAddress;
private ListOfRelatedOrganization listOfRelatedOrganization;
public String getAccountStatus() {
	return accountStatus;
}
public void setAccountStatus(String accountStatus) {
	this.accountStatus = accountStatus;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getAccountId() {
	return accountId;
}
public void setAccountId(String accountId) {
	this.accountId = accountId;
}
public String getRuleAttribute2() {
	return ruleAttribute2;
}
public void setRuleAttribute2(String ruleAttribute2) {
	this.ruleAttribute2 = ruleAttribute2;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getaTTerritory() {
	return aTTerritory;
}
public void setaTTerritory(String aTTerritory) {
	this.aTTerritory = aTTerritory;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}

public ListOfBusinessAddress getListOfBusinessAddress() {
	return listOfBusinessAddress;
}
public void setListOfBusinessAddress(ListOfBusinessAddress listOfBusinessAddress) {
	this.listOfBusinessAddress = listOfBusinessAddress;
}
public ListOfRelatedOrganization getListOfRelatedOrganization() {
	return listOfRelatedOrganization;
}
public void setListOfRelatedOrganization(ListOfRelatedOrganization listOfRelatedOrganization) {
	this.listOfRelatedOrganization = listOfRelatedOrganization;
}
@Override
public String toString() {
	return "Account [accountStatus=" + accountStatus + ", type=" + type + ", accountId=" + accountId
			+ ", ruleAttribute2=" + ruleAttribute2 + ", name=" + name + ", aTTerritory=" + aTTerritory + ", location="
			+ location + ", listOfBusinessAddress=" + listOfBusinessAddress + ", listOfRelatedOrganization="
			+ listOfRelatedOrganization + "]";
}


}
