package com.lonar.cartservice.atflCartService.service;

import java.util.List;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.LtMastSysVariables;

public interface LtMastSysVariablesService {

	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;
}
