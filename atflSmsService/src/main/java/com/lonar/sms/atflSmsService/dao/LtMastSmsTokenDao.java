package com.lonar.sms.atflSmsService.dao;

import java.util.List;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtMastOrganisations;
import com.lonar.sms.atflSmsService.model.LtMastSmsToken;

public interface LtMastSmsTokenDao {

	

	List<LtMastSmsToken> getBySmsId(String userId, Long transId) throws ServiceException;
	int updateStatus(String status,String userId, Long transId) throws ServiceException;
	LtMastOrganisations getDefaultOrganisationByCode(String string) throws ServiceException;

}
