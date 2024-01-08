package com.users.usersmanagement.controller;

import java.io.IOException;
import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.LtFileUploadService;

@RestController
@RequestMapping(value = "/fileupload")
public class LtFileUploadController implements CodeMaster {

	@Autowired
	LtFileUploadService fileUploadService;

	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> uploadfile(@RequestParam("file") MultipartFile file,
			@RequestParam("orgId") String orgId,
			@RequestParam("userId") Long userId) throws ServerException {
		try {
			return new ResponseEntity<Status>(fileUploadService.uploadfile(file, orgId, userId), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getuploadedfiles/{orgId}/{limit}/{offset}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getUploadedFiles(@PathVariable("orgId") String orgId, @PathVariable("limit") Long limit,  @PathVariable("offset") Long offset,
			@PathVariable("userId") Long userId)
			throws ServiceException, IOException {  
		return new ResponseEntity<Status>(fileUploadService.getUploadedFiles(orgId, limit, offset, userId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteuplodedfile/{fileuploadId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> deleteUplodedFile(@PathVariable("fileuploadId") Long fileuploadId)
			throws ServiceException, IOException {
		return new ResponseEntity<Status>(fileUploadService.deleteUplodedFile(fileuploadId), HttpStatus.OK);
	}
} 
