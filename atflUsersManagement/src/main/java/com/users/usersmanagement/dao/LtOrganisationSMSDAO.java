package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtOrganisationSMS;

public interface LtOrganisationSMSDAO {
	
	List<LtOrganisationSMS> getAll() throws ServiceException;
	
	LtOrganisationSMS getOrgSmsbyOrgId()throws ServiceException;
	
	LtOrganisationSMS getsmsTemplateByType(String templateType)throws ServiceException;
}
