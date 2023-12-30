package com.lonar.sms.atflSmsService.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lonar.sms.atflSmsService.model.Status;

@RestControllerAdvice
public class HandleGlobalException {

	//static final Logger logger = Logger.getLogger(HandleGlobalException.class);
	
	/*@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Status> handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response){
		e.getException().printStackTrace();
		logger.error("Error in  "+request.getRequestURI(), e.getException());
		Status status = new Status();
		status.setCode(e.getCode());
		 
		if(e.getMessage() == null){
			status.setMessage("Internal Server Error!!!");
		}else{
			status.setMessage(e.getMessage());
		}
		return new ResponseEntity<Status>(status, HttpStatus.EXPECTATION_FAILED );
	}*/
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<Status> handleServiceException(ServiceException e, HttpServletRequest request, HttpServletResponse response){
		e.getException().printStackTrace();
		//logger.error("Error in  "+request.getRequestURI(), e.getException());
		Status status = new Status();
		status.setCode(e.getCode());
		if(e.getMessage() == null){
			status.setMessage("Internal Server Error!!!");
		}else{
			status.setMessage(e.getMessage());
		}
		return new ResponseEntity<Status>(status, HttpStatus.EXPECTATION_FAILED );
	}
}
