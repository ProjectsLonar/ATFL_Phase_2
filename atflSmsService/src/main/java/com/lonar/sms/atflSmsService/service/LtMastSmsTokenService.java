package com.lonar.sms.atflSmsService.service;

import java.io.IOException;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.Status;

public interface LtMastSmsTokenService {

	Status sendSms(String userId, Long transId) throws ServiceException,IOException;

	Status getSmsBalance(String userId) throws ServiceException, IOException;

}
