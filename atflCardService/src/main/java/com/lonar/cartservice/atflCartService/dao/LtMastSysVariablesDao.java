package com.lonar.cartservice.atflCartService.dao;

import java.util.List;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.LtMastSysVariables;

public interface LtMastSysVariablesDao {
	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;
}
