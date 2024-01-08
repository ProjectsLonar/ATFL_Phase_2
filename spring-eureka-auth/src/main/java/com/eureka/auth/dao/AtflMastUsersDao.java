package com.eureka.auth.dao;

import org.hibernate.service.spi.ServiceException;

import com.eureka.auth.model.LtMastLogins;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;

public interface AtflMastUsersDao {

	LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber) throws ServiceException;

	LtMastLogins getLoginDetailsByUserId(Long userId) throws ServiceException;
	
	LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException;
	
	public LtVersion getVersionAuthAPI(LtVersion ltVersion)throws ServiceException;
	
}
