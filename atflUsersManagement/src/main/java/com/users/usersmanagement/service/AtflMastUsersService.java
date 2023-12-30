package com.users.usersmanagement.service;

import java.io.IOException;

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

	Status getUserById(String userId) throws ServiceException;

	Status delete(String userId) throws ServiceException;

	Status getPenddingApprovalByDistributorId(Long distributorId, String userId) throws ServiceException, IOException;

	Status changeUserStatus(String userId, String status) throws ServiceException, IOException;

	LtMastUsers userResponce(LtMastUsers ltMastUsers) throws ServiceException;

	Status savePersonalDetails(LtUserPersonalDetails ltUserPersonalDetails) throws ServiceException;

	Status saveAddress(LtUserAddressDetails ltUserAddressDetails) throws ServiceException;

	Status getPersonaldetailsById(String userId) throws ServiceException;

	Status getAddressDetailsById(String userId) throws ServiceException;

	Status saveUserOrganisationDetails(String userId) throws ServiceException, IOException;

	Status getAllUserByDistributorId(Long distributorId) throws ServiceException;

	Status uploadProfilePic(MultipartFile file, String userId) throws ServiceException;
	
	Status saveRecentSearchId(String userId,Long serachId) throws ServiceException;
	
	Status getAllUsersData() throws ServiceException;
	
	Status getUserAllMasterDataById(String userId) throws ServiceException;
	
	Status getUsersList(RequestDto requestDto) throws IOException;
	
	Status getInactiveUsers() throws ServiceException;
	
	MobileSupportedVersionResponseDto isMobileSupportedVersion(MobileSupportedVersionRequestDto requestDto)throws ServiceException;
	
}
