package com.lonar.cartservice.atflCartService.service;

import java.io.IOException;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtSoHeadersService {

	Status saveOrder(SoHeaderDto soHeaderDto) throws ServiceException, IOException;

	Status getAllOrderInprocess() throws ServiceException, IOException;

	Status getOrderV1(RequestDto requestDto) throws ServiceException, IOException;

}
