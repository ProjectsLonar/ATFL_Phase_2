package com.lonar.sms.atflSmsService.dao;

import java.util.List;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtOrganisationSMS;

public interface LtOrganisationSMSDAO {
	
	List<LtOrganisationSMS> getAll() throws ServiceException;
	
	LtOrganisationSMS getOrgSmsbyOrgId()throws ServiceException;
}
