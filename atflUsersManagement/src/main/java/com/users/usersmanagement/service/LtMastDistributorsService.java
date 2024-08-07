package com.users.usersmanagement.service;

import java.rmi.ServerException;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;

public interface LtMastDistributorsService {

	Status verifyDistributor(String distributorCrmCode,String positionCode,String userCode,Long userId)
			throws ServiceException;
	
	Status verifyDistributorV1(String distributorCrmCode,String distributorName,String proprietorName,Long userId)
			throws ServiceException;
	
	Status getAllDistributorAgainstAreahead(RequestDto requestDto)throws ServerException, ServiceException;
	
	Status getAllNotification(RequestDto requestDto) throws ServerException, ServiceException;

	Status getUserDataByIdForValidation(Long userId) throws ServerException, ServiceException;

	Status saveSeibelUserData(Long userId)throws ServerException, ServiceException;

	Status deleteNotificationAfter72Hours()throws ServerException, ServiceException;

	Status updateReadNotificationFlag(RequestDto requestDto)throws ServerException, ServiceException;

}
