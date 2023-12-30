package com.users.usersmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.LtOrganisationSMSDAO;
import com.users.usersmanagement.model.LtOrganisationSMS;

@Service
public class LtOrganisationSMSServiceImpl implements LtOrganisationSMSService {

	@Autowired
	private LtOrganisationSMSDAO ltOrganisationSMSDAO;

	@Override
	public List<LtOrganisationSMS> getAll() throws ServiceException {
		return ltOrganisationSMSDAO.getAll();
	}
	
}
