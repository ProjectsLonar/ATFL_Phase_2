package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.model.Status;

public interface LtMastProductService {

	Status getProduct(RequestDto requestDto) throws ServiceException, IOException;
	Status readImageProduct() throws ServiceException, IOException;
	Status getProductV2(RequestDto requestDto) throws ServiceException, IOException;
	Status getInStockProduct(RequestDto requestDto)throws ServiceException, IOException;
	Status getOutOfStockProduct(RequestDto requestDto)throws ServiceException, IOException;
}
