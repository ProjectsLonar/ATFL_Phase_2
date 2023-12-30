package com.users.usersmanagement.dao;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastOrganisations;

public interface LtMastOrganisationsDao {
	
	LtMastOrganisations verifyOrganisation(String userCode, String orgCode) throws ServiceException;
	
	LtMastOrganisations verifyOrganisationV2(String userCode, String orgCode) throws ServiceException;

}
