package com.eureka.zuul.common;

import java.io.IOException;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.zuul.service.UserServices;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	UserServices userServices;

	@RequestMapping(value = "/getInactiveUsers", method = RequestMethod.GET,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getInactiveUsers() throws ServiceException, IOException {
		System.out.println("IN getInactiveUsers API CALLLLLLLLLL");
		return new ResponseEntity<Status>(userServices.getInactiveUsers(), HttpStatus.OK);
	}
}
