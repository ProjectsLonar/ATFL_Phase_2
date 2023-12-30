package com.eureka.auth.service;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eureka.auth.dao.LtMastSysVariablesDao;
import com.eureka.auth.model.LtMastSysVariables;

@Service
public class LtMastSysVariablesServiceImpl implements LtMastSysVariablesService{

	@Autowired
	LtMastSysVariablesDao ltMastSysVariablesDao;
	
	@Override
	public List<LtMastSysVariables> loadAllConfiguration() throws ServiceException {
		return ltMastSysVariablesDao.loadAllConfiguration();
	}

}
