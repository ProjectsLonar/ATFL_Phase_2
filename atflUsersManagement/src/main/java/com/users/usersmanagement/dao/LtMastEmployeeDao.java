package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastEmployees;
import com.users.usersmanagement.model.LtMastPositions;
import com.users.usersmanagement.model.RequestDto;

public interface LtMastEmployeeDao {

	LtMastEmployees verifyEmployee(String employeeCode, String distributorCrmCode, String positionCode)throws ServiceException;

	List<LtMastPositions> getSalesPersonsForDistributorV1(RequestDto requestDto) throws ServiceException;
	List<LtMastPositions> getSalesPersonsForDistributorV2(RequestDto requestDto) throws ServiceException;
	
	LtMastEmployees verifySalesOfficer(String primaryMobile,String emailId,String positionCode)throws ServiceException;
	
	LtMastEmployees verifyAreaHead(String primaryMobile,String emailId,String positionCode)throws ServiceException;
	
	
	LtMastEmployees getEmployeeByCode(String employeeCode)throws ServiceException;
	
	LtMastEmployees verifySalesOfficerV1(String employeeCode)throws ServiceException;
	
	LtMastEmployees verifyAreaHeadV1(String employeeCode)throws ServiceException;
	
	LtMastEmployees verifySystemAdministrator(String employeeCode)throws ServiceException;

	String getUserTypeById(Long userId)throws ServiceException;

}
