package com.users.usersmanagement.model;

import java.util.List;

public class ListOfOutletInterface {
private Account account;

public Account getAccount() {
	return account;
}

public void setAccount(Account account) {
	this.account = account;
}

@Override
public String toString() {
	return "ListOfOutletInterface [account=" + account + "]";
}

}
