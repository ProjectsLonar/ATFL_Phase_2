package com.users.usersmanagement.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.LtUserAddressDetails;
import com.users.usersmanagement.model.LtUserPersonalDetails;
import com.users.usersmanagement.model.MobileSupportedVersionRequestDto;
import com.users.usersmanagement.model.MobileSupportedVersionResponseDto;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;

public interface AtflMastUsersService {

	// ResponceEntity sendOTP(String mobileNumber) throws
	// ServiceException,IOException;

	Status login(String mobileNumber, Long otp) throws ServiceException;

	Status sendOTPToUser(String mobileNumber) throws ServiceException, IOException;

	Status reSendOTP(String mobileNumber, Long distributorId) throws ServiceException, IOException;

	Status update(LtMastUsers ltMastUsers) throws ServiceException, IOException;

	Status getUserById(Long userId) throws ServiceException;

	Status delete(Long userId) throws ServiceException;

	Status getPenddingApprovalByDistributorId(Long distributorId, Long userId) throws ServiceException, IOException;

	Status changeUserStatus(Long userId, String status) throws ServiceException, IOException;

	LtMastUsers userResponce(LtMastUsers ltMastUsers) throws ServiceException;

	Status savePersonalDetails(LtUserPersonalDetails ltUserPersonalDetails) throws ServiceException;

	Status saveAddress(LtUserAddressDetails ltUserAddressDetails) throws ServiceException;

	Status getPersonaldetailsById(Long userId) throws ServiceException;

	Status getAddressDetailsById(Long userId) throws ServiceException;

	Status saveUserOrganisationDetails(Long userId) throws ServiceException, IOException;

	Status getAllUserByDistributorId(Long distributorId) throws ServiceException;

	Status uploadProfilePic(MultipartFile file, Long userId) throws ServiceException;
	
	Status saveRecentSearchId(Long userId,String serachId) throws ServiceException;
	
	Status getAllUsersData() throws ServiceException;
	
	Status getUserAllMasterDataById(Long userId) throws ServiceException;
	
	Status getUsersList(RequestDto requestDto) throws IOException;
	
	Status getInactiveUsers() throws ServiceException;
	
	MobileSupportedVersionResponseDto isMobileSupportedVersion(MobileSupportedVersionRequestDto requestDto)throws ServiceException;

	Status saveRecentSearchId1(Long userId, String searchId)throws ServiceException;
	
}
