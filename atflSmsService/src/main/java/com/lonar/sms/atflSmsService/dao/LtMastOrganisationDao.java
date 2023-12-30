package com.lonar.sms.atflSmsService.dao;

import java.util.List;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtMastOrganisations;

public interface LtMastOrganisationDao {

	LtMastOrganisations getLtMastOrganisationById(String orgId) throws ServiceException;

	List<LtMastOrganisations> getAllOrganisation() throws ServiceException;

	List<LtMastOrganisations> ltMastOrganisationDao(String organisationCode) throws ServiceException;

	//LtMastOrganisations getPaymentDetailsById(Long orgId) throws ServiceException;

}
