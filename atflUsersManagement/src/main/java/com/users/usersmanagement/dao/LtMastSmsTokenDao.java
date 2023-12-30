package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastOrganisations;
import com.users.usersmanagement.model.LtMastSmsToken;

public interface LtMastSmsTokenDao {

	List<LtMastSmsToken> getBySmsId(String userId, String transId) throws ServiceException;

	int updateStatus(String status, String userId, String transId) throws ServiceException;

	LtMastOrganisations getDefaultOrganisationByCode(String string) throws ServiceException;

	List<LtMastSmsToken> saveall(List<LtMastSmsToken> ltMastSmsToken) throws ServiceException;
}
