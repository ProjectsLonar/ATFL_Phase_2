package com.atflMasterManagement.masterservice.servcies;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.Status;

public interface PrivacyPolicyService {

	Status readPrivacyPolicy() throws ServiceException;

	Status termsAndConditions(Long orgId) throws ServiceException;

	Status privacypolicy(Long orgId) throws ServiceException;
}
