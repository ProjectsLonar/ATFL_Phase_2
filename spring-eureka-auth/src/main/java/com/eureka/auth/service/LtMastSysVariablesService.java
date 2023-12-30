package com.eureka.auth.service;

import java.util.List;

import org.hibernate.service.spi.ServiceException;

import com.eureka.auth.model.LtMastSysVariables;

public interface LtMastSysVariablesService {

	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;

}
