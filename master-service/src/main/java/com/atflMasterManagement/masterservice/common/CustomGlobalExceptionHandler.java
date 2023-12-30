package com.atflMasterManagement.masterservice.common;



import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	/*  @ExceptionHandler(Exception.class) public void
	  handleAllExceptions(HttpServletResponse response) throws IOException {
	  response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value()); }
	  
	  
	  @ExceptionHandler(BusinessException.class) public void
	  springHandleNotFound(HttpServletResponse response) throws IOException {
	  response.sendError(HttpStatus.NOT_FOUND.value()); }
	 
	
	*/

}
