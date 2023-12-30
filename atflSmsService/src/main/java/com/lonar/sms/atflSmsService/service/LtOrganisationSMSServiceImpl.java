package com.lonar.sms.atflSmsService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.dao.LtOrganisationSMSDAO;
import com.lonar.sms.atflSmsService.model.LtOrganisationSMS;

@Service
public class LtOrganisationSMSServiceImpl implements LtOrganisationSMSService {

	@Autowired
	private LtOrganisationSMSDAO ltOrganisationSMSDAO;

	@Override
	public List<LtOrganisationSMS> getAll() throws ServiceException {
		return ltOrganisationSMSDAO.getAll();
	}
	
}
