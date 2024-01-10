package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastDistributors;

public interface LtMastDistributorsDao {

	LtMastDistributors verifyDistributors(String distributorCrmCode, String positionCode, String userCode)
			throws ServiceException;
	
	LtMastDistributors verifyDistributorsV1(String distributorCrmCode, String distributorName, String proprietorName)
			throws ServiceException;

	LtMastDistributors getLtDistributorsById(Long distributorId) throws ServiceException;
	
	List<LtMastDistributors> getAllDistributorAgainstAreahead(String userName)throws ServiceException;
}
