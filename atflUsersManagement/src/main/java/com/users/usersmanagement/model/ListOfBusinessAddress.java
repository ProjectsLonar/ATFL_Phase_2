package com.users.usersmanagement.model;

import java.util.List;

public class ListOfBusinessAddress {
private BusinessAddress listOfBusinessAddress;

public BusinessAddress getListOfBusinessAddress() {
	return listOfBusinessAddress;
}

public void setListOfBusinessAddress(BusinessAddress listOfBusinessAddress) {
	this.listOfBusinessAddress = listOfBusinessAddress;
}

@Override
public String toString() {
	return "ListOfBusinessAddress [listOfBusinessAddress=" + listOfBusinessAddress + "]";
}


}
