package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastDistributors;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.RequestDto;

public interface LtMastDistributorsDao {

	LtMastDistributors verifyDistributors(String distributorCrmCode, String positionCode, String userCode)
			throws ServiceException;
	
	LtMastDistributors verifyDistributorsV1(String distributorCrmCode, String distributorName, String proprietorName)
			throws ServiceException;

	LtMastDistributors getLtDistributorsById(Long distributorId) throws ServiceException;
	
	List<LtMastDistributors> getAllDistributorAgainstAreahead(String userName)throws ServiceException;
	
	List<NotificationDetails> getAllNotification(RequestDto requestDto) throws ServiceException; 
}
