package com.users.usersmanagement.service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.Status;

public interface LtMastDistributorsService {

	Status verifyDistributor(String distributorCrmCode,String positionCode,String userCode,String userId)
			throws ServiceException;
	
	Status verifyDistributorV1(String distributorCrmCode,String distributorName,String proprietorName,String userId)
			throws ServiceException;

}