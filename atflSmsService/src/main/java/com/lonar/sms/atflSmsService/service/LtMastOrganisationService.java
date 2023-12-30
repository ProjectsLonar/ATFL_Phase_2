package com.lonar.sms.atflSmsService.service;

import java.util.List;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtMastOrganisations;
import com.lonar.sms.atflSmsService.model.Status;

public interface LtMastOrganisationService {

	LtMastOrganisations getLtMastOrgById(String orgId) throws ServiceException;

	List<LtMastOrganisations> getAllOrganisations()  throws ServiceException;

	Status getByOrgCode(String orgCode) throws ServiceException;

	//LtMastOrganisations getPaymentDetailsById(Long orgId) throws ServiceException;
}
