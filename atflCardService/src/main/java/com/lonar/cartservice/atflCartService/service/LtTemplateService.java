package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.LtTemplateDto;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtTemplateService {

	Status getTemplateAgainstDistributor (String distributorId, Long templateHeaderId)throws ServerException;
	
	Status getTemplateAgainstDistributors (String distributorId)throws ServerException;
	
	Status createTemplate(LtTemplateDto ltTemplateDto)throws ServerException, ServiceException;
    
	Status getTemplateAgainstDistributors (String distributorId, String priceList)throws ServerException;
}
