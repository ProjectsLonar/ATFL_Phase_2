package com.lonar.cartservice.atflCartService.dao;

import java.rmi.ServerException;
import java.util.List;

import com.lonar.cartservice.atflCartService.model.LtTemplateHeaders;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;

public interface LtTemplateDao {

	LtTemplateHeaders getTemplateAgainstDistributor(String distributorId,Long templateHeaderId)throws ServerException;
	
	List<LtTemplateLines> getProductDetailsAgainstheaderId(Long templateHeaderId)throws ServerException;
}
