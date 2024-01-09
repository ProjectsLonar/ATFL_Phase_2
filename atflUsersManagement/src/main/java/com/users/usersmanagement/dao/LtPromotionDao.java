package com.users.usersmanagement.dao;

import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtPromotion;

public interface LtPromotionDao {
	List<LtPromotion> getPromotionDataV1(String orgId, Long limit, Long offset, Long userId) throws ServiceException;
	
	List<LtPromotion> getPromotionData(String orgId, Long limit, Long offset) throws ServiceException;
}
