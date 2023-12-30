package com.eureka.auth.dao;

import java.util.List;

import org.hibernate.service.spi.ServiceException;

import com.eureka.auth.model.LtMastSysVariables;

public interface LtMastSysVariablesDao {

	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;

}
