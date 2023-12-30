package com.lonar.sms.atflSmsService.service;

import java.util.List;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtOrganisationSMS;

public interface LtOrganisationSMSService {

	List<LtOrganisationSMS> getAll() throws ServiceException;
}
