package com.eureka.zuul.service;

import java.util.Map;

import org.hibernate.service.spi.ServiceException;

import com.eureka.zuul.common.LtVersion;
import com.eureka.zuul.common.Status;

public interface UserServices {
	
	Status getInactiveUsers() throws ServiceException;
	
	Map<String, LtVersion> getAllAPIVersion() throws ServiceException;
	
}
