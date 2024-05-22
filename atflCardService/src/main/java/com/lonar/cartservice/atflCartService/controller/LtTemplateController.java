package com.lonar.cartservice.atflCartService.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.lonar.cartservice.atflCartService.common.BusinessException;
import com.lonar.cartservice.atflCartService.dto.LtTemplateDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.service.LtTemplateService;

@RestController
@RequestMapping(value = "/lttemplatecontroller")
public class LtTemplateController implements CodeMaster {
	
	@Autowired
	LtTemplateService ltTemplateService;

	@GetMapping(value = "/getTemplateAgainstDistributor/{distributorId}",produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getTemplateAgainstDistributor(@PathVariable String distributorId) throws ServerException {
		try {
			
			Status status = ltTemplateService.getTemplateAgainstDistributors(distributorId);
			
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			//logger.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@PostMapping(value = "/createTemplate", consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> createTemplate(@RequestBody LtTemplateDto ltTemplateDto) throws ServerException {
		try {
		Status status = ltTemplateService.createTemplate(ltTemplateDto);
		return new ResponseEntity<Status>(status, HttpStatus.OK);
		}catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}


	@GetMapping(value = "/getTemplateAgainstDistributor/{distributorId}/{priceList}",produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getTemplateAgainstDistributor(@PathVariable String distributorId, 
			@PathVariable String priceList) throws ServerException {
		try {
			
			Status status = ltTemplateService.getTemplateAgainstDistributors(distributorId, priceList);
			
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			//logger.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}


}
