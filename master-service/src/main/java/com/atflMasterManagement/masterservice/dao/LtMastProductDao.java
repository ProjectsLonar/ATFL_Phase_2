package com.atflMasterManagement.masterservice.dao;

import java.io.IOException;
import java.util.List;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dto.ProductDto;
//import com.atflMasterManagement.masterservice.model.LtMastProducts;
import com.atflMasterManagement.masterservice.model.LtMastProducts;
import com.atflMasterManagement.masterservice.model.RequestDto;

public interface LtMastProductDao {

	public List<ProductDto> getProduct(RequestDto requestDto) throws ServiceException, IOException;

	public Long getProductCount(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<LtMastProducts> getAllProduct() throws ServiceException, IOException;
	
	public List<ProductDto> getProductForAdmin(RequestDto requestDto) throws ServiceException, IOException;

	public Long getProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException;
	
	public String getUserTypeByUserId(String userId)throws ServiceException, IOException;
	
	public List<ProductDto> getProductWithInventory(RequestDto requestDto) throws ServiceException, IOException;

	public Long getProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getInStockProductAdmin(RequestDto requestDto) throws ServiceException, IOException;

	public Long getInStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getInStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException;

	public Long getInStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getOutOfStockProductForAdmin(RequestDto requestDto) throws ServiceException, IOException;

	public Long getOutOfStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getOutOfStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException;

	public Long getOutOfStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException;

}
