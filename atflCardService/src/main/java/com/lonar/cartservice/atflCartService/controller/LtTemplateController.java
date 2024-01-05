package com.lonar.cartservice.atflCartService.controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lonar.cartservice.atflCartService.common.BusinessException;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.service.LtTemplateService;

@RestController
@RequestMapping(value = "/lttemplatecontroller")
public class LtTemplateController implements CodeMaster {
	
	@Autowired
	LtTemplateService ltTemplateService;

	@GetMapping(value = "/getTemplateAgainstDistributor/{distributorId}/{templateHeaderId}",produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getTemplateAgainstDistributor(@PathVariable String distributorId,@PathVariable Long templateHeaderId ) throws ServerException {
		try {
			/*
			 * ObjectMapper requestMapper = new ObjectMapper(); String json =
			 * requestMapper.writeValueAsString(requestDto); String requestJson =
			 * requestMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDto)
			 * ; logger.
			 * info("Calling ==> API Name : get Order V1, Method Type : Post, JSON Request :"
			 * + request	Json);
			 */
			
			Status status = ltTemplateService.getTemplateAgainstDistributor(distributorId,templateHeaderId);
			
			/*
			 * ObjectMapper responceMapper = new ObjectMapper(); String jsonResponce =
			 * responceMapper.writeValueAsString(status); String responeJson =
			 * responceMapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
			 * logger.info("JSON Responce :"+ responeJson);
			 */
			
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			//logger.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
}
