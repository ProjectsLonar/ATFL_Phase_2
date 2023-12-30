package com.users.usersmanagement.dao;

import java.io.IOException;
import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtConfigurartion;
import com.users.usersmanagement.model.LtMastLogins;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.RequestDto;

public interface AtflMastUsersDao {

	LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber) throws ServiceException;

	LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException;

	LtMastLogins getLoginDetailsByUserId(String userId) throws ServiceException;

	LtMastUsers getUserById(String userId) throws ServiceException;

	LtMastUsers delete(String userId) throws ServiceException;

	List<LtMastUsers> getUserByName(String name) throws ServiceException;

	boolean saveFireBaseToken(LtMastLogins ltMastLogins) throws ServiceException;

	List<LtMastUsers> getCustomerByName(String name) throws ServiceException;

	List<LtMastUsers> getPenddingApprovalByDistributorId(Long distributorId, String userId) throws ServiceException;

	List<LtMastUsers> getDistributorUserByRoleAndId(Long distributorId) throws ServiceException;
	
	List<LtMastUsers> getAllUserByDistributorId(Long distributorId) throws ServiceException;
	
	List<LtMastUsers> getAllUsersData() throws ServiceException;
	
	LtMastUsers getUserAllMasterDataById(String userId) throws ServiceException;
	
	public List<LtMastUsers> getUsersList(RequestDto requestDto) throws IOException;
	
	List<LtMastUsers> getAllInactiveUsers() throws ServiceException;
	
	List<LtMastUsers> getActiveUsersDistByUserId(String userId)throws ServiceException;
	
	List<LtConfigurartion> getAllConfiguration() throws ServiceException;
	
}
