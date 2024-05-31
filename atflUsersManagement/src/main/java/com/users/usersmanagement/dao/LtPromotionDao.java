package com.users.usersmanagement.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtPromotion;

public interface LtPromotionDao {
	List<LtPromotion> getPromotionDataV1(String orgId, Long limit, Long offset, Long userId) throws ServiceException;
	
	List<LtPromotion> getPromotionData(String orgId, Long limit, Long offset) throws ServiceException;

	void deletePromotionData(Long pramotionId)throws ServiceException;

	LtPromotion getPromotionData(Long pramotionId)throws ServiceException;

	void deletePromotionDataById(Long promotionId)throws ServiceException;

	void updatePromotionData(MultipartFile file, String createdBy, String pramotionStatus,
			String promotionName, String allTimeShowFlag, String orgId, String startDate, String endDate, String createdBy1, String createdBy2, Long promotionId);
}
