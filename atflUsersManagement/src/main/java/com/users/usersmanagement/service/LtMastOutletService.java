package com.users.usersmanagement.service;

import java.io.IOException;
import java.rmi.ServerException;

import org.springframework.http.HttpStatus;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.BeatDetailsDto;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutletsDump;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;

public interface LtMastOutletService {

	Status getOutlet(RequestDto requestDto) throws ServiceException, IOException;

	Status getAllUserDataByRecentId(Long userId) throws ServiceException;

	Status verifyOutlet(String outletCode, String distributorCrmCode, Long userId) throws ServiceException, IOException;
	
	Status getAllOutletType() throws ServiceException, IOException;
	
	Status getAllOutletChannel() throws ServiceException, IOException;
	
	Status createOutlet(LtMastOutletsDump ltMastOutlets) throws ServiceException, IOException;
	
	Status getPriceListAgainstDistributor(String distributorId )throws ServiceException, IOException;
	
	Status getPendingAprrovalOutlet(RequestDto requestDto) throws ServiceException, IOException;
	
	Status approveOutlet(LtMastOutletsDump ltMastOutletsDump)throws ServiceException, IOException;

//	Status getBeatDetailsAgainsDistirbutorCodeAndBeatName(String distributorCode, String beatName)throws ServiceException, IOException;

	Status getBeatDetailsAgainsDistirbutorCode(BeatDetailsDto beatDetailsDto)throws ServiceException, IOException;
	
	Status updateBeatSequence(BeatDetailsDto beatDetailsDto) throws ServiceException, IOException;

	Status getOutletagainstBeat(BeatDetailsDto beatDetailsDto) throws ServiceException, IOException;
	
	Status getOutletAgainstBeat(BeatDetailsDto beatDetailsDto) throws ServerException;
}
