package com.users.usersmanagement.dao;

import java.util.Date;
import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastDistributors;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.UserDto;
import com.users.usersmanagement.model.SiebelDto;

public interface LtMastDistributorsDao {

	LtMastDistributors verifyDistributors(String distributorCrmCode, String positionCode, String userCode)
			throws ServiceException;
	
	LtMastDistributors verifyDistributorsV1(String distributorCrmCode, String distributorName, String proprietorName)
			throws ServiceException;

	LtMastDistributors getLtDistributorsById(Long distributorId) throws ServiceException;
	
	List<LtMastDistributors> getAllDistributorAgainstAreahead(RequestDto requestDto)throws ServiceException;
	
	List<NotificationDetails> getAllNotification(RequestDto requestDto) throws ServiceException;

	SiebelDto getUserDataByIdForValidation(Long userId)throws ServiceException;

	String getUserTypeByUserId(Long userId)throws ServiceException;

	List<LtMastDistributors> getAllDistributorAgainstSystemAdmin(RequestDto requestDto)throws ServiceException;

	NotificationDetails deleteNotificationAfter72Hours(Date cutoffDate)throws ServiceException;

	void updateReadNotificationFlag(int notificationId)throws ServiceException;

	//LtMastUsers saveSeibelUserData(LtMastUsers user, Long userId)throws ServiceException;

	

	
}
