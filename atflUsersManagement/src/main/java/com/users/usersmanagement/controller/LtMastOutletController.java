package com.users.usersmanagement.controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.LtMastOutletService;

@RestController
@RequestMapping(value = "/outlets")
public class LtMastOutletController implements CodeMaster {

	@Autowired
	private LtMastOutletService ltMastOutletService;

	@RequestMapping(value = "/getSelectedOutlet/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getSelectedOutlet(@PathVariable("userId") String userId) throws ServiceException {
		try {
			return new ResponseEntity<Status>(ltMastOutletService.getAllUserDataByRecentId(userId), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/verifyOutlet/{ouletCode}/{distributorCrmCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifyOutlet(@PathVariable("ouletCode") String ouletCode,
			@PathVariable("distributorCrmCode") String distributorCrmCode, @PathVariable("userId") String userId)
			throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastOutletService.verifyOutlet(ouletCode, distributorCrmCode, userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getOutlet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOutlet(@RequestBody RequestDto requestDto) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastOutletService.getOutlet(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	@GetMapping(value = "/getAllOutletType", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllOutletType() throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastOutletService.getAllOutletType(), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	@GetMapping(value = "/getAllOutletChannel", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllOutletChannel() throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastOutletService.getAllOutletChannel(), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	@PostMapping(value = "/createOutlet", consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status>createOutlet(@RequestBody LtMastOutlets ltMastOutlets) throws ServerException{
	try {
		  return new ResponseEntity<Status>(ltMastOutletService.createOutlet(ltMastOutlets), HttpStatus.OK);
	    }catch(Exception e) {
	  throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
    }	
	}
	
	@RequestMapping(value = "/getPriceListAgainstDistributor/{outletId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getPriceListAgainstDistributor(@PathVariable("outletId") String outletId) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastOutletService.getPriceListAgainstDistributor(outletId), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
}
