package com.atflMasterManagement.masterservice.Controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.BusinessException;
import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastFAQ;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.LtMastFAQService;

@RestController
@RequestMapping(value = "/faq")
public class LtMastFAQController implements CodeMaster {

	static final Logger logger = LoggerFactory.getLogger(LtMastCategoryController.class);

	@Autowired
	private LtMastFAQService ltMastFAQService;

	@RequestMapping(value = "/postFAQ", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> Post(@RequestBody LtMastFAQ ltMastFAQ) throws ServiceException {
		try {
			return new ResponseEntity<Status>(ltMastFAQService.post(ltMastFAQ), HttpStatus.OK);
		} catch (Exception e) {

			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/aboutUsAndFaqDetails/{orgId}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> findFAQOrgId(@PathVariable("orgId") Long orgId, @PathVariable("userId") Long userId) throws ServiceException, IOException {
		try {
			return new ResponseEntity<Status>(ltMastFAQService.getAll(orgId, userId), HttpStatus.OK);
		} catch (Exception e) {

			throw new BusinessException(INTERNAL_SERVER_ERROR, "FAQ not found", e);
		}
	}

}
