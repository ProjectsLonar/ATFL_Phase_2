package com.users.usersmanagement.service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;

public interface LtMastEmployeeService {

	Status verifyEmployee(String employeeCode, String distributorCode, String positionCode, Long userId)
			throws ServiceException;

	Status getSalesPersonsForDistributorV1(RequestDto requestDto) throws ServiceException;
	
	Status verifySalesOfficer(String primaryMobile,String emailId,String positionCode, Long userId)
			throws ServiceException;
	
	Status verifySalesOfficerV1(String employeeCode,Long userId,String userName)
			throws ServiceException;
	
	Status verifyAreaHead(String primaryMobile,String emailId,String positionCode, Long userId)
			throws ServiceException;
	
	Status verifyAreaHeadV1(String employeeCode, Long userId,String userName)
			throws ServiceException;
	
	Status verifySystemAdministrator(String employeeCode, Long userId,String userName)throws ServiceException;

}
