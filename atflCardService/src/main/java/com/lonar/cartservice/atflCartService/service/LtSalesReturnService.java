package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;

import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtSalesReturnService {
	
	Status saveSalesReturn(LtSalesReturnDto ltSalesReturnDto) throws ServerException;
	
	Status getStatusForSalesReturn() throws ServerException;
	
	Status getAvailabilityForSalesReturn() throws ServerException;
	
	Status getLocationForSalesReturn(String distributorCode) throws ServerException;
	
	Status getSalesReturn(RequestDto requestDto) throws ServerException;
	
	Status getInvoices(RequestDto requestDto) throws ServerException;

	Status getInvoicePdfAgainstInvoiceNumber(RequestDto requestDto)throws ServerException;

	Status getLotNumber(String prodId, String inventId)throws ServerException;

	Status getSalesReturnForPendingAprroval(RequestDto requestDto)throws ServerException;
	
	StringBuilder getInvoicePdfAgainstInvoiceNumber1(RequestDto requestDto)throws ServerException;

	Status getSalesReturnOrderAgainstReturnOrderNo(String returnOrderNo)throws ServerException;

}
