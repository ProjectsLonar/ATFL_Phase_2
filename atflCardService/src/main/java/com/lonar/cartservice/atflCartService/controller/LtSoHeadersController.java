package com.lonar.cartservice.atflCartService.controller;

import java.io.IOException;
import java.rmi.ServerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lonar.cartservice.atflCartService.common.BusinessException;
import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.service.LtSoHeadersService;

@RestController
@RequestMapping(value = "/ltsoheaders")
public class LtSoHeadersController implements CodeMaster {

	@Autowired
	private LtSoHeadersService ltSoHeadersService;

	private static final Logger logger = LoggerFactory.getLogger(LtSoHeadersController.class);
	

	@RequestMapping(value = "/saveOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> saveOrder(@RequestBody SoHeaderDto soHeaderDto) throws ServerException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(soHeaderDto);
			String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(soHeaderDto);
			logger.info("Calling==> API Name : Save Order, Method : Post, JSON Request :"+ requestJson);
			
			Status status = ltSoHeadersService.saveOrder(soHeaderDto);
			
			ObjectMapper responceMapper = new ObjectMapper();
			String jsonResponce = responceMapper.writeValueAsString(status);
			String responeJson = responceMapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
			logger.info("JSON Responce :"+ responeJson);
			
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getAllInprocessOrder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllInprocessOrder() throws ServiceException, IOException {
		try {
			logger.info("Calling ==> API Name : get All Inprocess Order, Method Type : GET");
			
			Status status = ltSoHeadersService.getAllOrderInprocess();
					
			ObjectMapper responceMapper = new ObjectMapper();
			String jsonResponce = responceMapper.writeValueAsString(status);
			String responeJson = responceMapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
			
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@RequestMapping(value = "/getOrderV1", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOrderV1(@RequestBody RequestDto requestDto) throws ServerException {
		try {
			ObjectMapper requestMapper = new ObjectMapper();
			String json = requestMapper.writeValueAsString(requestDto);
			String requestJson = requestMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDto);
			logger.info("Calling ==> API Name : get Order V1, Method Type : Post, JSON Request :"+ requestJson);
			
			Status status = ltSoHeadersService.getOrderV1(requestDto);
			
			ObjectMapper responceMapper = new ObjectMapper();
			String jsonResponce = responceMapper.writeValueAsString(status);
			String responeJson = responceMapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
			logger.info("JSON Responce :"+ responeJson);
			
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Description :", e);
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@GetMapping(value = "/getOrderCancellationReason", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOrderCancellationReport() throws ServerException {
		try {
			return new ResponseEntity<Status>(ltSoHeadersService.getOrderCancellationReport(), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	//ATFL Phase 2 new development 
		@RequestMapping(value = "/saveOrderV2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
		public ResponseEntity<Status> saveOrderV2(@RequestBody SoHeaderDto soHeaderDto) throws ServerException {
			try {
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(soHeaderDto);
				String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(soHeaderDto);
				logger.info("Calling==> API Name : Save Order, Method : Post, JSON Request :"+ requestJson);
				
				Status status = ltSoHeadersService.saveOrderV2(soHeaderDto);
				
				ObjectMapper responceMapper = new ObjectMapper();
				String jsonResponce = responceMapper.writeValueAsString(status);
				String responeJson = responceMapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
				logger.info("JSON Responce :"+ responeJson);
				
				return new ResponseEntity<Status>(status, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error Description :", e);
				throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
			}
		}
		
		@RequestMapping(value = "/getOrderV2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
		public ResponseEntity<Status> getOrderV2(@RequestBody RequestDto requestDto) throws ServerException {
			try {
				ObjectMapper requestMapper = new ObjectMapper();
				String json = requestMapper.writeValueAsString(requestDto);
				String requestJson = requestMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDto);
				logger.info("Calling ==> API Name : get Order V1, Method Type : Post, JSON Request :"+ requestJson);
				
				Status status = ltSoHeadersService.getOrderV2(requestDto);
				
				ObjectMapper responceMapper = new ObjectMapper();
				String jsonResponce = responceMapper.writeValueAsString(status);
				String responeJson = responceMapper.writerWithDefaultPrettyPrinter().writeValueAsString(status);
				logger.info("JSON Responce :"+ responeJson);
				
				return new ResponseEntity<Status>(status, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error Description :", e);
				throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
			}
		}
		
		@PostMapping(value = "/locationSaveOnNoOrder",consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Status> locationSaveOnNoOrder(@RequestBody LtSoHeaders ltSoHeaders) throws ServiceException, IOException {
			return new ResponseEntity<Status>(ltSoHeadersService.locationSaveOnNoOrder(ltSoHeaders), HttpStatus.OK);
		}
	
}
