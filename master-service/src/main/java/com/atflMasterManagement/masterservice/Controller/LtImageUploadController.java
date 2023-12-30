package com.atflMasterManagement.masterservice.Controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atflMasterManagement.masterservice.common.BusinessException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.LtImageUploadService;

@RestController
@RequestMapping(value = "/imageUpload")
public class LtImageUploadController implements CodeMaster {

	@Autowired
	LtImageUploadService imageUploadService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> imageUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("type") String type) throws ServerException {
		try {
			return new ResponseEntity<Status>(imageUploadService.imageUpload(file, type), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
}
