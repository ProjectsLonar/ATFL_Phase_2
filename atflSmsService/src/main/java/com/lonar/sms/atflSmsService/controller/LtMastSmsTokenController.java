package com.lonar.sms.atflSmsService.controller;

import java.io.IOException;

import org.hibernate.service.spi.ServiceException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lonar.sms.atflSmsService.model.Status;
import com.lonar.sms.atflSmsService.service.LtMastSmsTokenService;

@RestController
@RequestMapping("/sms")
public class LtMastSmsTokenController {

	@Autowired
	LtMastSmsTokenService ltMastSmsTokenService;
	
	@GetMapping
	public String hello() {
		return "hello server I am testing";
	}
	
	@RequestMapping(value = "/sendSms/{transId}/{userId}", method= RequestMethod.GET)
	public ResponseEntity<Status> sendSms(@PathVariable("transId") Long transId,@PathVariable("userId") String userId) throws ServiceException, ParseException, JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		try {
			Status status =  ltMastSmsTokenService.sendSms(userId,transId);
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/sendSms/{transId}", method= RequestMethod.GET)
	public ResponseEntity<Status> sendSms(@PathVariable("transId") Long transId) throws ServiceException, ParseException, JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		try {
			Status status =  ltMastSmsTokenService.sendSms(null,transId);
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/getSmsBalance/{userId}/{logTime}", method= RequestMethod.GET)
	public ResponseEntity<Status> getSmsBalance(@PathVariable("userId") String userId,@PathVariable("logTime") Long logTime) throws ServiceException, ParseException, JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		try {
			Status status =  ltMastSmsTokenService.getSmsBalance(userId);
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
