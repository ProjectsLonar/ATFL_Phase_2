package com.users.usersmanagement.controller;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.common.Logging;
import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.LtUserAddressDetails;
import com.users.usersmanagement.model.LtUserPersonalDetails;
import com.users.usersmanagement.model.MobileSupportedVersionRequestDto;
import com.users.usersmanagement.model.MobileSupportedVersionResponseDto;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.AtflMastUsersService;

@RestController
@RequestMapping(value = "/users")
public class AtflMastUsersController implements CodeMaster {

	@Autowired
	private AtflMastUsersService atflMastUsersService;

	private static final Logger log = LoggerFactory.getLogger(AtflMastUsersController.class);

	@RequestMapping(value = "/sendOTP/{mobileNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> sendOTP(@PathVariable("mobileNumber") String mobileNumber)
			throws ServiceException, IOException {
		try {
			// set logger request
			String requestURL = "/sendOTP/" + mobileNumber;
			Logging.setRequestLog(null, "sendOTP()", requestURL, "GET");
			Status status = atflMastUsersService.sendOTPToUser(mobileNumber); //this is original comment on 29-May-2024 for optimization & async call
//			CompletableFuture<Status> status = atflMastUsersService.sendOTPToUser(mobileNumber);
			Logging.setResponceLog(status, "sendOTP()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
//			return new ResponseEntity<CompletableFuture<Status>>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> update(@RequestBody LtMastUsers ltMastUsers) throws ServiceException, IOException {

		try {
			String requestURL = "/update";
			Logging.setRequestLog(ltMastUsers, "update()", requestURL, "POST");
			Status status = atflMastUsersService.update(ltMastUsers);
			Logging.setResponceLog(status, "update()", requestURL, "POST");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getUserById/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getUserById(@PathVariable("userId") Long userId) throws ServiceException {

		try {
			String requestURL = "/getUserById/" + userId;
			Logging.setRequestLog(null, "getUserById()", requestURL, "GET");
			Status status = atflMastUsersService.getUserById(userId);
			Logging.setResponceLog(status, "getUserById()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> delete(@PathVariable("userId") Long userId) throws ServiceException {
		try {
			String requestURL = "/delete/" + userId;
			Logging.setRequestLog(null, "delete()", requestURL, "DELETE");
			Status status = atflMastUsersService.delete(userId);
			Logging.setResponceLog(status, "delete()", requestURL, "DELETE");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getPenddingApprovalByDistributorId/{distributorId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getPenddingApprovalByDistributorId(@PathVariable("distributorId") Long distributorId,
			@PathVariable("userId") Long userId) throws ServiceException, IOException {

		try {
			String requestURL = "/getPenddingApprovalByDistributorId/" + distributorId + "/" + userId;
			Logging.setRequestLog(null, "getPenddingApprovalByDistributorId()", requestURL, "GET");
			Status status = atflMastUsersService.getPenddingApprovalByDistributorId(distributorId, userId);
			Logging.setResponceLog(status, "getPenddingApprovalByDistributorId()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/changeUserStatus/{userId}/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> changeUserStatus(@PathVariable("userId") Long userId,
			@PathVariable("status") String status) throws ServiceException, IOException {

		try {
			String requestURL = "/changeUserStatus/" + userId + "/" + status;
			Logging.setRequestLog(null, "changeUserStatus()", requestURL, "GET");
			Status statusResponce = atflMastUsersService.changeUserStatus(userId, status);
			Logging.setResponceLog(statusResponce, "changeUserStatus()", requestURL, "GET");
			return new ResponseEntity<Status>(statusResponce, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/savePersonalDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> savePersonalDetails(@Valid @RequestBody LtUserPersonalDetails ltUserPersonalDetails)
			throws ServerException {
		try {
			String requestURL = "/savePersonalDetails";
			Logging.setRequestLog(ltUserPersonalDetails, "savePersonalDetails()", requestURL, "POST");
			Status status = atflMastUsersService.savePersonalDetails(ltUserPersonalDetails);
			Logging.setResponceLog(status, "savePersonalDetails()", requestURL, "POST");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/setSelectedOutlet/{userId}/{searchId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> setSelectedOutlet(@PathVariable("userId") Long userId,
			@PathVariable("searchId") String searchId) throws ServiceException, IOException {

		try {
			String requestURL = "/setSelectedOutlet/" + userId + "/" + searchId;
			Logging.setRequestLog(null, "setSelectedOutlet()", requestURL, "GET");
			Status status = atflMastUsersService.saveRecentSearchId(userId, searchId);
			Logging.setResponceLog(status, "setSelectedOutlet()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	@RequestMapping(value = "/deselectOutletForNoOrder/{userId}/{searchId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> deselectOutletForNoOrder(@PathVariable("userId") Long userId,
			@PathVariable("searchId") String searchId) throws ServiceException, IOException {

		try {
			String requestURL = "/setSelectedOutlet/" + userId + "/" + searchId;
			Logging.setRequestLog(null, "setSelectedOutlet()", requestURL, "GET");
			Status status = atflMastUsersService.saveRecentSearchId1(userId, searchId);
			Logging.setResponceLog(status, "setSelectedOutlet()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}


	@RequestMapping(value = "/getPersonaldetailsById/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getPersonaldetailsById(@PathVariable("userId") Long userId)
			throws ServiceException, IOException {

		try {
			String requestURL = "/getPersonaldetailsById/" + userId;
			Logging.setRequestLog(null, "getPersonaldetailsById()", requestURL, "GET");
			Status status = atflMastUsersService.getPersonaldetailsById(userId);
			Logging.setResponceLog(status, "getPersonaldetailsById()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/saveAddress", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> saveAddress(@RequestBody LtUserAddressDetails ltUserAddressDetails)
			throws ServerException {
		try {
			String requestURL = "/saveAddress";
			Logging.setRequestLog(ltUserAddressDetails, "saveAddress()", requestURL, "POST");
			Status status = atflMastUsersService.saveAddress(ltUserAddressDetails);
			Logging.setResponceLog(status, "saveAddress()", requestURL, "POST");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getAddressDetailsById/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAddressDetailsById(@PathVariable("userId") Long userId)
			throws ServiceException, IOException {
		try {
			String requestURL = "/getAddressDetailsById/" + userId;
			Logging.setRequestLog(null, "getAddressDetailsById()", requestURL, "GET");
			Status status = atflMastUsersService.getAddressDetailsById(userId);
			Logging.setResponceLog(status, "getAddressDetailsById()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);

		}

	}

	@RequestMapping(value = "/activeProfile/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> activeProfile(@PathVariable("userId") Long userId) throws ServerException {
		try {
			String requestURL = "/activeProfile/" + userId;
			Logging.setRequestLog(null, "activeProfile()", requestURL, "GET");
			Status status = atflMastUsersService.saveUserOrganisationDetails(userId);
			Logging.setResponceLog(status, "activeProfile()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getAllUserByDistributorId/{distributorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllUserByDistributorId(@PathVariable("distributorId") Long distributorId)
			throws ServiceException {
		try {
			String requestURL = "/getAllUserByDistributorId/" + distributorId;
			Logging.setRequestLog(null, "getAllUserByDistributorId()", requestURL, "GET");
			Status status = atflMastUsersService.getAllUserByDistributorId(distributorId);
			Logging.setResponceLog(status, "getAllUserByDistributorId()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getAllUsersData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllUsersData() throws ServiceException {
		try {
			String requestURL = "/getAllUsersData";
			Logging.setRequestLog(null, "getAllUsersData()", requestURL, "GET");
			Status status = atflMastUsersService.getAllUsersData();
			Logging.setResponceLog(status, "getAllUsersData()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/uploadProfilePic", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> uploadProfilePic(@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) throws ServerException {
		try {
			String requestURL = "/uploadProfilePic/" + userId;
			Logging.setRequestLog(null, "uploadProfilePic()", requestURL, "POST");
			Status status = atflMastUsersService.uploadProfilePic(file, userId);
			Logging.setResponceLog(status, "uploadProfilePic()", requestURL, "POST");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getUserAllMasterDataById/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getUserAllMasterDataById(@PathVariable("userId") Long userId)
			throws ServiceException {
		try {
			String requestURL = "/getUserAllMasterDataById/" + userId;
			Logging.setRequestLog(null, "getUserAllMasterDataById()", requestURL, "GET");
			Status status = atflMastUsersService.getUserAllMasterDataById(userId);
			Logging.setResponceLog(status, "getUserAllMasterDataById()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getUsersList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getUsersList(@RequestBody RequestDto requestDto) {
		try {
			String requestURL = "/getUsersList";
			Logging.setRequestLog(requestDto, "getUsersList()", requestURL, "POST");
			Status status = atflMastUsersService.getUsersList(requestDto);
			Logging.setResponceLog(status, "getUsersList()", requestURL, "POST");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}

	@RequestMapping(value = "/getInactiveUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getInactiveUsers() throws ServiceException, IOException {

		try {
			String requestURL = "/getInactiveUsers";
			Logging.setRequestLog(null, "getInactiveUsers()", requestURL, "GET");
			Status status = atflMastUsersService.getInactiveUsers();
			Logging.setResponceLog(status, "getInactiveUsers()", requestURL, "GET");
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/isMobileSupportedVersion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public MobileSupportedVersionResponseDto isMobileSupportedVersion(@RequestBody MobileSupportedVersionRequestDto requestDto)
			throws ServiceException {
		try {
			String requestURL = "/isMobileSupportedVersion/" + requestDto;
			Logging.setRequestLog(null, "isMobileSupportedVersion()", requestURL, "POST");
			MobileSupportedVersionResponseDto response = atflMastUsersService.isMobileSupportedVersion(requestDto);
			Logging.setResponceLog(response, "isMobileSupportedVersion()", requestURL, "POST");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
}