package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;

import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtSalesReturnService {
	
	Status saveSalesReturn(LtSalesReturnDto ltSalesReturnDto) throws ServerException;
	

}
