package com.users.usersmanagement.service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.Status;

public interface LtMastOrganisationsService {

	Status verifyOrganisation(String userCode, String orgCode, String userId) throws ServiceException;

	Status verifyOrganisationV2(String userCode, String orgCode, String userId) throws ServiceException;
}
