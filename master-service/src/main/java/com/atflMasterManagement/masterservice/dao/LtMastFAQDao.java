package com.atflMasterManagement.masterservice.dao;



import java.util.List;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.LtMastFAQ;

public interface LtMastFAQDao {

	public LtMastFAQ saveFAQ(LtMastFAQ ltMastFAQ)throws ServiceException;

	public List<LtMastFAQ> getAllFAQ(Long orgId, Long userId)throws ServiceException;

}
