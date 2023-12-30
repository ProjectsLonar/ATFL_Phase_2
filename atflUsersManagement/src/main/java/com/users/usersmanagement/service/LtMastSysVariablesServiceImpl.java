package com.users.usersmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.LtMastSysVariablesDao;
import com.users.usersmanagement.model.LtMastSysVariables;

@Service
public class LtMastSysVariablesServiceImpl implements LtMastSysVariablesService{

	@Autowired
	LtMastSysVariablesDao ltMastSysVariablesDao;
	
	@Override
	public List<LtMastSysVariables> loadAllConfiguration() throws ServiceException {
		return ltMastSysVariablesDao.loadAllConfiguration();
	}

}
