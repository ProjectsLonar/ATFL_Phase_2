package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastFAQ;
import com.atflMasterManagement.masterservice.model.Status;

public interface LtMastFAQService {

	Status post(LtMastFAQ ltMastFAQ) throws ServiceException, IOException;

	Status getAll(Long orgId, Long userId) throws ServiceException, IOException;
}
