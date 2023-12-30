package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastSysVariables;

public interface LtMastSysVariablesDao {

	List<LtMastSysVariables> loadAllConfiguration() throws ServiceException;

}
