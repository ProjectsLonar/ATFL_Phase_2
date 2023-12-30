package com.lonar.sms.atflSmsService.dao;

import java.awt.Menu;
import java.util.List;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtMastLogins;
import com.lonar.sms.atflSmsService.model.LtMastUsers;

public interface LtMastUsersDao {

	LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber, Long supplierId) throws ServiceException;

	LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException;

	LtMastLogins getLoginDetailsByUserId(Long userId) throws ServiceException;

	LtMastUsers getUserById(Long userId) throws ServiceException;

	Long getLtMastUsersCount(LtMastUsers input, Long supplierId) throws ServiceException;

	List<LtMastUsers> getLtMastUsersDataTable(LtMastUsers input, Long supplierId) throws ServiceException;

	LtMastUsers delete(Long userId) throws ServiceException;

	List<LtMastUsers> getUserByType(String userType, Long supplierId) throws ServiceException;

	LtMastUsers getLtMastUsersByEmail(String email, Long supplierId) throws ServiceException;

	List<Menu> findMenu(String role, Long supplierId)  throws ServiceException;

	List<LtMastUsers> getUserByName(String name, Long supplierId) throws ServiceException;

	boolean saveFireBaseToken(LtMastLogins ltMastLogins) throws ServiceException;

	Long getAllUserBySupplierCount(Long supplierId, LtMastUsers ltMastUsers) throws ServiceException;

	List<LtMastUsers> getAllUserBySupplierDataTable(Long supplierId, LtMastUsers ltMastUsers) throws ServiceException;

	List<LtMastUsers> getCustomerByName(String name, Long supplierId) throws ServiceException;

	LtMastUsers getUserStatusById(Long userId) throws ServiceException;

}
