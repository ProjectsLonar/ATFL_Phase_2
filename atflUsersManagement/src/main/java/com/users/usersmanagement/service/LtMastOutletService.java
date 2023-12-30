package com.users.usersmanagement.service;

import java.io.IOException;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;

public interface LtMastOutletService {

	Status getOutlet(RequestDto requestDto) throws ServiceException, IOException;

	Status getAllUserDataByRecentId(String userId) throws ServiceException;

	Status verifyOutlet(String outletCode, String distributorCrmCode, String userId) throws ServiceException, IOException;
	
	Status getAllOutletType() throws ServiceException, IOException;
	
	Status getAllOutletChannel() throws ServiceException, IOException;
	
	Status createOutlet(LtMastOutlets ltMastOutlets) throws ServiceException, IOException;
	
	Status getPriceListAgainstDistributor(String outletCode )throws ServiceException, IOException;
}
