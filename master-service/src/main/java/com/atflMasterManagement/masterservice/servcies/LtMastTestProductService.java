package com.atflMasterManagement.masterservice.servcies;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastTestProduct;
import com.atflMasterManagement.masterservice.model.Status;

public interface LtMastTestProductService {

	Status saveProduct(LtMastTestProduct ltMastTestProduct) throws ServiceException;

	Status deleteByProductId(Long productId) throws ServiceException;

	Status getAllProducts(Long limit, Long offset) throws ServiceException;

	Status updateProductDetails(LtMastTestProduct ltMastTestProduct) throws ServiceException;

	Status getStatesMap() throws ServiceException;

	Status getCategoryMap() throws ServiceException;

	Status getAllSubcategoryAgainstCatId(Integer categoryId) throws ServiceException;
}
