package com.atflMasterManagement.masterservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.PrivacyPolicyService;

@RestController
@RequestMapping(value = "/privacypolicy")
public class LtMastPrivacyPolicyController {

	@Autowired
	PrivacyPolicyService privacyPolicyService;

	@RequestMapping(value = "/readPrivacyPolicy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> readPrivacyPolicy() throws ServiceException {

		return new ResponseEntity<Status>(privacyPolicyService.readPrivacyPolicy(), HttpStatus.OK);
	}

	@RequestMapping(value = "/termsAndConditions/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> termsAndConditions(@PathVariable("orgId") Long orgId) throws ServiceException {
		return new ResponseEntity<Status>(privacyPolicyService.termsAndConditions(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/privacypolicy/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> privacypolicy(@PathVariable("orgId") Long orgId) throws ServiceException {
		return new ResponseEntity<Status>(privacyPolicyService.privacypolicy(orgId), HttpStatus.OK);
	}
}
