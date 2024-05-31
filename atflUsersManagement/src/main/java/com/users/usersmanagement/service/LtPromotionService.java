package com.users.usersmanagement.service;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtPromotion;
import com.users.usersmanagement.model.Status;

public interface LtPromotionService {

	Status uploadPromotion(MultipartFile file, Long promotionId, String createdBy, String status, String promotionName,
			String allTimeShowFlag, String orgId, String startDate, String endDate)
			throws ServiceException, ParseException;

	Status getPromotionDataV1(String orgId, Long limit, Long offset, Long userId) throws ServiceException;
	
	Status getPromotionData(String orgId, Long limit, Long offset) throws ServiceException;

	Status deletePromotionData(Long pramotionId) throws ServiceException;
	
	Status editPromotion(LtPromotion ltPromotion) throws ServiceException;
	
	Status editPromotionV1(MultipartFile file, Long promotionId, String createdBy, String status, String promotionName,
			String allTimeShowFlag, String orgId, String startDate, String endDate)
			throws ServiceException, ParseException;
}
