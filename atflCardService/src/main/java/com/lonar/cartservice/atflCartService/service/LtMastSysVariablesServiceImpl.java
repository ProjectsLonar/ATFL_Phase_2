package com.lonar.cartservice.atflCartService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dao.LtMastSysVariablesDao;
import com.lonar.cartservice.atflCartService.model.LtMastSysVariables;

public class LtMastSysVariablesServiceImpl implements LtMastSysVariablesService{

	@Autowired
	LtMastSysVariablesDao ltMastSysVariablesDao;
	
	@Override
	public List<LtMastSysVariables> loadAllConfiguration() throws ServiceException {
		return ltMastSysVariablesDao.loadAllConfiguration();
	}

}