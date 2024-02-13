package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsResponseDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;

public interface LtSalesreturnDao {

	List<LtSalesReturnStatus> getStatusForSalesReturn() throws ServerException;
	
	List<LtSalesReturnAvailability> getAvailabilityForSalesReturn() throws ServerException;
	
	List<LtSalesReturnAvailability> getLocationForSalesReturn(String distributorCode) throws ServerException;
	
	List<Long> getSalesReturnHeader(RequestDto requestDto)throws ServerException;
	
	Long getSalesReturnRecordCount(RequestDto requestDto)throws ServerException; 
	
	List<ResponseDto> getSalesReturn(List<Long> IdsList) throws ServerException;
	
	void deleteSalesReturnLinesByHeaderId(Long HeaderId)throws ServerException;
	
	LtSalesReturnHeader updateSalesReturnHeader(LtSalesReturnHeader ltSalesreturnHeader)throws ServerException;
	
	LtSalesReturnLines updateLines(LtSalesReturnLines ltSalesreturnlines) throws ServerException;
	
	List<LtInvoiceDetailsResponseDto> getInvoiceDetails( RequestDto requestDto) throws ServerException;
	
	String getSalesReturnSequence();
}
