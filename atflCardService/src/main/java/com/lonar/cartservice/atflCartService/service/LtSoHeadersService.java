package com.lonar.cartservice.atflCartService.service;

import java.io.IOException;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.model.LtSalesPersonLocation;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtSoHeadersService {

	Status saveOrder(SoHeaderDto soHeaderDto) throws ServiceException, IOException;

	Status getAllOrderInprocess() throws ServiceException, IOException;

	Status getOrderV1(RequestDto requestDto) throws ServiceException, IOException;
	
	Status getOrderCancellationReport()throws ServiceException, IOException;

	// ATFL Phase 2 new development 
	Status saveOrderV2(SoHeaderDto soHeaderDto) throws ServiceException, IOException;
	Status getOrderV2(RequestDto requestDto) throws ServiceException, IOException;
	
	Status locationSaveOnNoOrder(LtSalesPersonLocation ltSalesPersonLocation) throws ServiceException, IOException;

	Status getOrderForPendingApprovals(RequestDto requestDto) throws ServiceException, IOException;

	Status getAllPendingOrders(RequestDto requestDto)throws ServiceException, IOException;

	Status removingPendingOrdersFromGetOrderV2(RequestDto requestDto) throws ServiceException, IOException;
}
