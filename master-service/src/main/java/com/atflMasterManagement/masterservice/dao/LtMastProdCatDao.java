package com.atflMasterManagement.masterservice.dao;

import java.util.List;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastProductCat;
import com.atflMasterManagement.masterservice.model.RequestDto;

public interface LtMastProdCatDao {

	public List<LtMastProductCat> getCategory(RequestDto requestDto) throws ServiceException;
	public Long getProdCatCount(RequestDto requestDto) throws ServiceException;
	
}
