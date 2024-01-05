package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;

import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.model.Status;

public interface LtTemplateService {

	//Status getTemplateAgainstDistributor(RequestDto requestDto)throws ServerException;
	
	Status getTemplateAgainstDistributor (String distributorId, Long templateHeaderId)throws ServerException;
}
