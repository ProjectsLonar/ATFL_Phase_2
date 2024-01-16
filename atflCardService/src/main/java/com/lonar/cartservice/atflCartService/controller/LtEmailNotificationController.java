package com.lonar.cartservice.atflCartService.controller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.OrderDetailsDto;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.service.LtMastEmailService;

@RestController
@RequestMapping(value = "/ltNotification")
public class LtEmailNotificationController {

	@Autowired
	LtMastEmailService ltMastEmailtokenService;
	
	@Autowired
	private Environment env;

	
		@RequestMapping(value = "/saveEmailToken/{headerId}/{logTime}", method= RequestMethod.GET)
	public ResponseEntity<Status> saveEmailToken(@PathVariable("headerId") Long headerId) throws ServiceException,JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		try {
			OrderDetailsDto dto=new OrderDetailsDto();
			//dto.setSoHeaderDto(new);
		Status status =  ltMastEmailtokenService.saveEmailToken(dto);
		return new ResponseEntity<Status>(status,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
