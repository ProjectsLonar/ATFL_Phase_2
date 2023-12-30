package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.model.Status;

public interface LtMastCategoryService {

	Status getCategory(RequestDto requestDto) throws ServiceException, IOException;

}
