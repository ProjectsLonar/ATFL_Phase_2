package com.lonar.cartservice.atflCartService.dao;

import java.rmi.ServerException;
import java.util.List;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.LtTemplateHeaders;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;

public interface LtTemplateDao {

	LtTemplateHeaders getTemplateAgainstDistributors(String distributorId)throws ServerException;
	
	LtTemplateHeaders getTemplateAgainstDistributor(String distributorId,Long templateHeaderId)throws ServerException;
	
	List<LtTemplateLines> getProductDetailsAgainstheaderId(Long templateHeaderId)throws ServerException;
	
	void deletelinedetailsbytemplateid(Long templateHeaderId) throws ServiceException;
	
	LtTemplateHeaders saveheaderData(LtTemplateHeaders ltTemplateHeaders) throws ServiceException;
	
	LtTemplateLines saveLineData (LtTemplateLines ltTemplateLines)throws ServiceException;
	
	
	
	
	
}
