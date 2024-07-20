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
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.service.LtSalesReturnService;

@RestController
@RequestMapping(value = "/ltsalesreturn")
public class LtSalesReturnController implements  CodeMaster {

	@Autowired
	LtSalesReturnService ltSalesReturnService;
	
	@PostMapping(value = "/saveSalesReturn", consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> saveSalesReturn(@RequestBody LtSalesReturnDto ltSalesReturnDto) throws ServerException {
		try {
		Status status = ltSalesReturnService.saveSalesReturn(ltSalesReturnDto);
		return new ResponseEntity<Status>(status, HttpStatus.OK);
		}catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@GetMapping(value = "/getStatusForSalesReturn", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getStatusForSalesReturn() throws ServerException {
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getStatusForSalesReturn(), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	@GetMapping(value = "/getAvailabilityForSalesReturn", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAvailabilityForSalesReturn() throws ServerException {
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getAvailabilityForSalesReturn(), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	
	@GetMapping(value = "/getLocationForSalesReturn/{invoiceNumber}", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getLocationForSalesReturn(@PathVariable String invoiceNumber) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getLocationForSalesReturn(invoiceNumber), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}

	@PostMapping(value = "/getSalesReturn",produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getSalesReturn(@RequestBody RequestDto requestDto) throws ServerException  {
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getSalesReturn(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@PostMapping(value = "/getInvoices",produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getInvoices(@RequestBody RequestDto requestDto) throws ServerException  {
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getInvoices(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

/*	@PostMapping(value = "/getInvoicePdf", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
    public ResponseEntity<Status> getInvoicePdfAgainstInvoiceNumber(@RequestBody RequestDto requestDto)throws ServerException{
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getInvoicePdfAgainstInvoiceNumber(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}*/	

	@PostMapping(value = "/getInvoicePdf", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
    public StringBuilder getInvoicePdfAgainstInvoiceNumber1(@RequestBody RequestDto requestDto)throws ServerException{
		try {
			 StringBuilder res =  new StringBuilder();
			 return res= ltSalesReturnService.getInvoicePdfAgainstInvoiceNumber1(requestDto);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}	

	
	@GetMapping(value = "/getLotNumber/{prodId}/{inventId}", produces = MediaType.APPLICATION_JSON_VALUE, headers= "X-API-Version=v1.0")
    public ResponseEntity<Status> getLotNumber(@PathVariable String prodId, @PathVariable String inventId) throws ServerException{
		try {
			   return new ResponseEntity<Status>(ltSalesReturnService.getLotNumber(prodId, inventId), HttpStatus.OK);
		}catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);

		}
	}	

    @PostMapping(value = "/getSalesReturnForPendingAprroval", produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getSalesReturnForPendingAprroval(@RequestBody RequestDto requestDto) throws ServerException{
		try {
			return new ResponseEntity<Status>(ltSalesReturnService.getSalesReturnForPendingAprroval(requestDto), HttpStatus.OK);
		}catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
			}
	}

    
    @GetMapping(value = "/getSalesReturnOrderAgainstReturnOrderNo/{returnorderNo}", produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v2.0")
    public ResponseEntity<Status> getSalesReturnOrderAgainstReturnOrderNo(@PathVariable String returnorderNo) throws ServerException{
    	try {
    		return new ResponseEntity<Status>(ltSalesReturnService.getSalesReturnOrderAgainstReturnOrderNo(returnorderNo), HttpStatus.OK);
    	}catch(Exception e) {
    		throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
    	}
    }
    
}
