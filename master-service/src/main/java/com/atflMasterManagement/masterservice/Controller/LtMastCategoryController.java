package com.atflMasterManagement.masterservice.Controller;

import java.rmi.ServerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.BusinessException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.LtMastCategoryService;

@RestController
@RequestMapping(value = "/categories")
public class LtMastCategoryController implements CodeMaster {

	static final Logger logger = LoggerFactory.getLogger(LtMastCategoryController.class);

	@Autowired
	private LtMastCategoryService ltMastCategoryService;

	@RequestMapping(value = "/getCategory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getCategory(@RequestBody RequestDto requestDto) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastCategoryService.getCategory(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(ENTITY_NOT_FOUND, null, e);
		}
	}
}
