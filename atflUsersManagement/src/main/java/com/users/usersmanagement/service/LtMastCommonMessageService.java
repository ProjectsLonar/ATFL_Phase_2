package com.users.usersmanagement.service;

import com.users.usersmanagement.model.Status;

public interface LtMastCommonMessageService {
	public Status getCodeAndMessage(Integer code);

	public String getCommonMessage(String message);

}
