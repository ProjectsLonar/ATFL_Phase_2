package com.users.usersmanagement.controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


	/*
	 * @GetMapping(value = "/getAllDistributorAgainstAreahead/{userName}", produces
	 * = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0") public
	 * ResponseEntity<Status>
	 * getAllDistributorAgainstAreahead(@PathVariable("userName") String userName)
	 * throws ServerException { try { return new ResponseEntity<Status>(
	 * ltMastDistributorsService.getAllDistributorAgainstAreahead(userName),
	 * HttpStatus.OK); } catch (Exception e) { throw new
	 * BusinessException(INTERNAL_SERVER_ERROR, null, e); } }
	 */
	
	@PostMapping(value = "/getAllDistributorAgainstAreahead", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllDistributorAgainstAreahead(@RequestBody RequestDto requestDto) throws ServerException {
		try {
			return new ResponseEntity<Status>(
					ltMastDistributorsService.getAllDistributorAgainstAreahead(requestDto),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
}
	
	@PostMapping(value = "/getAllNotification", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllNotification(@RequestBody RequestDto requestDto) throws ServerException {
		try {
			return new ResponseEntity<Status>(
					ltMastDistributorsService.getAllNotification(requestDto),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
}
	
	@DeleteMapping(value = "/deleteNotificationAfter72Hours", produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
  public ResponseEntity<Status> deleteNotificationAfter72Hours()throws ServerException {
	  try {
		  return new ResponseEntity<Status>(ltMastDistributorsService.deleteNotificationAfter72Hours(), HttpStatus.OK);
	  }catch(Exception e) {
		  throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
	  }	
  }
	
	@PostMapping(value = "updateReadNotificationFlag", produces = MediaType.APPLICATION_JSON_VALUE, headers ="X-API-Version=v1.0")
	public ResponseEntity<Status> updateReadNotificationFlag(@RequestBody RequestDto requestDto) throws ServerException{
		try {
		return new ResponseEntity<Status>(ltMastDistributorsService.updateReadNotificationFlag(requestDto), HttpStatus.OK);
	}catch(Exception e) {
		throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
	}
	}
	
	@RequestMapping(value="getUserDataByIdForValidation/{userId}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getUserDataByIdForValidation(@PathVariable("userId")Long userId) throws ServerException{
		try {
			return new ResponseEntity<Status>(ltMastDistributorsService.getUserDataByIdForValidation(userId),HttpStatus.OK);
		}catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}			
	}
	
	@RequestMapping(value="saveSeibelUserData/{userId}", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> saveSeibelUserData(@PathVariable("userId")Long userId) throws ServerException{
		try {
			return new ResponseEntity<Status>(ltMastDistributorsService.saveSeibelUserData(userId),HttpStatus.OK);
		}catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
		
		
	}
	
	
}
