package com.users.usersmanagement.service;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtOrganisationSMS;

public interface LtOrganisationSMSService {

	List<LtOrganisationSMS> getAll() throws ServiceException;
}
