package com.lonar.cartservice.atflCartService.service;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.OrderDetailsDto;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtMastEmailService {
	public Status saveEmailToken(OrderDetailsDto orderDetailsDto) throws InterruptedException, ServiceException;
}
