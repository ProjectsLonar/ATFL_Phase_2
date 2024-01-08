package com.users.usersmanagement.service;

import java.io.IOException;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.Status;

public interface LtMastSmsTokenService {

	Status sendSms(Long userId, String transId) throws ServiceException,IOException;

	Status getSmsBalance(Long userId) throws ServiceException, IOException;

}
