package com.eureka.zuul.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;

import com.eureka.zuul.common.LtMastUsers;
import com.eureka.zuul.common.LtVersion;

public interface UserDao {
	List<LtMastUsers> getAllInactiveUsers() throws ServiceException;
	
	Map<String, LtVersion> getAllAPIVersion() throws ServiceException;
}
