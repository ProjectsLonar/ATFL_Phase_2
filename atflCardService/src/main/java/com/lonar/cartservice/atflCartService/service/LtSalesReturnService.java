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

}
