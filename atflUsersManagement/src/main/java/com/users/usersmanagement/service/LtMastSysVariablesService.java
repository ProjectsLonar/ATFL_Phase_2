package com.users.usersmanagement.service;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastSysVariables;

public interface LtMastSysVariablesService {

	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;

}
