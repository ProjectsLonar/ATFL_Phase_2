package com.eureka.auth.dao;

import org.hibernate.service.spi.ServiceException;

import com.eureka.auth.model.LtMastLogins;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;
import com.eureka.auth.model.UserLoginDto;

public interface AtflMastUsersDao {

	LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber) throws ServiceException;
	
	UserLoginDto getLtMastUsersByMobileNumber1(String mobileNumber) throws ServiceException;

	LtMastLogins getLoginDetailsByUserId(Long userId) throws ServiceException;
	
	LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException;
	
	public LtVersion getVersionAuthAPI(LtVersion ltVersion)throws ServiceException;
	
}
