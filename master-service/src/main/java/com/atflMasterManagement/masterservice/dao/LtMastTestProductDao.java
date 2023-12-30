package com.atflMasterManagement.masterservice.dao;

import java.util.List;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastTestProduct;

public interface LtMastTestProductDao {

	public LtMastTestProduct saveProduct(LtMastTestProduct ltMastTestProduct)throws ServiceException;
	
	public LtMastTestProduct deleteByProductId(Long productId) throws ServiceException;
	
	public LtMastTestProduct getById(Long productId) throws ServiceException;
	
	public List<LtMastTestProduct>  getAllProducts(Long limit,Long offset) throws ServiceException;
}
