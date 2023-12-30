package com.eureka.auth.service;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.service.spi.ServiceException;

import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;
import com.eureka.auth.model.ResponceEntity;

public interface AtflMastUsersService {

	ResponceEntity login(String mobileNumber,String otp, HttpServletResponse response)  throws ServiceException;
	
	public LtMastUsers getLtMastUsers(String username);
	
	public LtVersion getVersionAuthAPI(LtVersion ltVersion);
	
	
}
