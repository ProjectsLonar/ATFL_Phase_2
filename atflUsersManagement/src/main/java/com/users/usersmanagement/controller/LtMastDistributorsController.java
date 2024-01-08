package com.users.usersmanagement.controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.LtMastDistributorsService;

@RestController
@RequestMapping(value = "/distributor")
public class LtMastDistributorsController implements CodeMaster {

	@Autowired
	LtMastDistributorsService ltMastDistributorsService;

	@RequestMapping(value = "/verifyDistributor/{distributorCrmCode}/{positionCode}/{userCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifyDistributor(@PathVariable("distributorCrmCode") String distributorCrmCode,
			@PathVariable("positionCode") String positionCode, @PathVariable("userCode") String userCode,
			@PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(
					ltMastDistributorsService.verifyDistributor(distributorCrmCode, positionCode, userCode, userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/verifyDistributor/{distributorCrmCode}/{distributorName}/{proprietorName}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v2.0")
	public ResponseEntity<Status> verifyDistributorV1(@PathVariable("distributorCrmCode") String distributorCrmCode,
			@PathVariable("distributorName") String distributorName, @PathVariable("proprietorName") String proprietorName,
			@PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(
					ltMastDistributorsService.verifyDistributorV1(distributorCrmCode, distributorName, proprietorName, userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
		
	}


	@GetMapping(value = "/getAllDistributorAgainstAreahead/{userId}", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllDistributorAgainstAreahead(@PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(
					ltMastDistributorsService.getAllDistributorAgainstAreahead(userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
}
}
