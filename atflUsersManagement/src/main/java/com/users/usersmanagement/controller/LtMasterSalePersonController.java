package com.users.usersmanagement.controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.LtMastEmployeeService;

@RestController
@RequestMapping(value = "/salepersons")
public class LtMasterSalePersonController implements CodeMaster {
	@Autowired
	LtMastEmployeeService ltMastEmployeeService;

	@RequestMapping(value = "/getSalesPersonsForDistributorV1", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getSalesPersonsForDistributorV1(@RequestBody RequestDto requestDto)
			throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastEmployeeService.getSalesPersonsForDistributorV1(requestDto),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/verifyEmployee/{employeeCode}/{distributorCrmCode}/{positionCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifyEmployee(@PathVariable("employeeCode") String employeeCode,
			@PathVariable("distributorCrmCode") String distributorCrmCode, @PathVariable("positionCode") String positionCode,
			@PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(
					ltMastEmployeeService.verifyEmployee(employeeCode, distributorCrmCode, positionCode, userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/verifySalesOfficer/{primaryMobile}/{emailId}/{positionCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifySalesOfficer(@PathVariable("primaryMobile") String primaryMobile,@PathVariable("emailId") String emailId, @PathVariable("positionCode") String positionCode,
			@PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastEmployeeService.verifySalesOfficer(primaryMobile,emailId,positionCode,userId),HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/verifySalesOfficer/{employeeCode}/{userId}/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v2.0")
	public ResponseEntity<Status> verifySalesOfficerV1(@PathVariable("employeeCode") String employeeCode,@PathVariable("userId") Long userId,@PathVariable("userName") String userName) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastEmployeeService.verifySalesOfficerV1(employeeCode,userId,userName),HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/verifyAreaHead/{primaryMobile}/{emailId}/{positionCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifyAreaHead(@PathVariable("primaryMobile") String primaryMobile,@PathVariable("emailId") String emailId,@PathVariable("positionCode") String positionCode,
			@PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastEmployeeService.verifyAreaHead(primaryMobile,emailId,positionCode,userId),HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/verifyAreaHead/{employeeCode}/{userId}/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v2.0")
	public ResponseEntity<Status> verifyAreaHeadV1(@PathVariable("employeeCode") String employeeCode,@PathVariable("userId") Long userId,@PathVariable("userName") String userName) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastEmployeeService.verifyAreaHeadV1(employeeCode,userId,userName),HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/systemAdministrator/{employeeCode}/{userId}/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifySystemAdministrator(@PathVariable("employeeCode") String employeeCode,@PathVariable("userId") Long userId,@PathVariable("userName") String userName) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastEmployeeService.verifySystemAdministrator(employeeCode,userId,userName),HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
			
		}
	}
}
