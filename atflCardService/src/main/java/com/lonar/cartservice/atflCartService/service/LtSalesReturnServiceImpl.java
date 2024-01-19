package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.cartservice.atflCartService.dao.LtSalesreturnDao;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.Status;

@Service
public class LtSalesReturnServiceImpl implements LtSalesReturnService,CodeMaster {

	@Autowired
	LtSalesreturnDao  ltSalesreturnDao;
	
	@Override
	public Status saveSalesReturn(LtSalesReturnDto ltSalesReturnDto) throws ServerException{
		
		
	}
}
