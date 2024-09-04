package com.atflMasterManagement.masterservice.dao;

import java.io.IOException;
import java.util.List;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dto.ProductDto;
import com.atflMasterManagement.masterservice.dto.TlEtlDto;
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
//	public List<ProductDto> getInStockProductAdminV1(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getInStockProductForAreaHead(RequestDto requestDto) throws ServiceException, IOException;

	public Long getInStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException;
//	public Long getInStockProductCountForAdminV1(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getInStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException;
//	public List<ProductDto> getInStockProductWithInventoryV1(RequestDto requestDto) throws ServiceException, IOException;
	
	public Long getInStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException;
//	public Long getInStockProductCountWithInventoryV1(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getOutOfStockProductForAdmin(RequestDto requestDto) throws ServiceException, IOException;
//	public List<ProductDto> getOutOfStockProductForAdminV2(RequestDto requestDto) throws ServiceException, IOException;
	
	public Long getOutOfStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException;
//	public Long getOutOfStockProductCountForAdminV2(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getOutOfStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException;
//	public List<ProductDto> getOutOfStockProductWithInventoryV2(RequestDto requestDto) throws ServiceException, IOException;
	
	public Long getOutOfStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException;
//	public Long getOutOfStockProductCountWithInventoryV2(RequestDto requestDto) throws ServiceException, IOException;
	
	public List<ProductDto> getMultipleMrpForProduct(String distId, String outId, String prodId, String priceList)throws ServiceException, IOException;

	public List<ProductDto> getMultipleMrpForProductV1(String prodId, String distId)throws ServiceException, IOException;
	public List<ProductDto> getMultipleMrpForProductV2(List<String> prodId, String distId)throws ServiceException, IOException;
	public List<ProductDto> getMultipleMrpForInStockProductV3(String prodId, String distId, String priceList)throws ServiceException, IOException;
	
	public List<ProductDto> getMultipleMrpForOutofStockProductV1(String prodId, String distId,String priceList)throws ServiceException, IOException;
	public List<ProductDto> getMultipleMrpForOutofStockProductV2(String prodId, String distId, String priceList)throws ServiceException, IOException;
	
	public List<TlEtlDto> getTlForProductDescription(String priceList, String productId) throws ServiceException, IOException;

	public List<TlEtlDto> getEtlForProductDescription(String priceList, String productId) throws ServiceException, IOException;
	
	public List<ProductDto> getProductListFromProcedure()throws ServiceException, IOException;
	 
	public List<ProductDto> getMultipleMrpFromProcedure()throws ServiceException, IOException;
	
	public Long getProductCountFromProcedure() throws ServiceException, IOException;

}
