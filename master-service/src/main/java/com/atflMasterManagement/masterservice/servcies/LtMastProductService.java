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
	Status getInStockProductV1(RequestDto requestDto)throws ServiceException, IOException;
	Status getInStockProductV2(RequestDto requestDto)throws ServiceException, IOException;
	
	Status getOutOfStockProduct(RequestDto requestDto)throws ServiceException, IOException;
	Status getOutOfStockProductV1(RequestDto requestDto)throws ServiceException, IOException;
	Status getOutOfStockProductV2(RequestDto requestDto)throws ServiceException, IOException;
	
	Status getMultipleMrpForProduct(String distId, String outId, String prodId, String priceList)throws ServiceException, IOException;
	Status getMultipleMrpForProductV1(String prodId, String distId)throws ServiceException, IOException;
	Status getTlForProductDescription(String priceList, String productId) throws ServiceException, IOException;
	Status getEtlForProductDescription(String priceList, String productId)throws ServiceException, IOException;
	Status getInstockProductsForSysAdminTemplate(RequestDto requestDto)throws ServiceException, IOException;
	Status getOutOfStockProductsForSysAdminTemplate(RequestDto requestDto)throws ServiceException, IOException;
}
 