package com.users.usersmanagement.controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.LtMastOrganisationsService;

@RestController
@RequestMapping(value = "/org")
public class LtMastOrganisationsController implements CodeMaster {

	@Autowired
	LtMastOrganisationsService ltMastOrganisationsService;

	@RequestMapping(value = "/verifyOrganisation/{userCode}/{orgCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> verifyOrganisation(@PathVariable("userCode") String userCode,
			@PathVariable("orgCode") String orgCode, @PathVariable("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastOrganisationsService.verifyOrganisation(userCode, orgCode, userId),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
//	@RequestMapping(value = "/verifyOrganisation/{userCode}/{orgCode}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.1")
//	public ResponseEntity<Status> verifyOrganisationV1(@PathVariable("userCode") String userCode,
//			@PathVariable("orgCode") String orgCode, @PathVariable("userId") Long userId) throws ServerException {
//		try {
//			return new ResponseEntity<Status>(ltMastOrganisationsService.verifyOrganisationV2(userCode, orgCode, userId),
//					HttpStatus.OK);
//		} catch (Exception e) {
//			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
//		}
//	}
}
