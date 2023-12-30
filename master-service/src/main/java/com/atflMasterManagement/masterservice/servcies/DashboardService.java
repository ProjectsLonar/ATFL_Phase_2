package com.atflMasterManagement.masterservice.servcies;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.Status;

public interface DashboardService {

	Status statusWiseOrdersCount(String orgId, String userId) throws ServiceException;

	Status categoryRevenueDistribution(String orgId, String userId) throws ServiceException;
	
	Status categoryRevenueDistributionv2(String orgId, String userId) throws ServiceException;

	Status dailySales(String orgId, String userId) throws ServiceException;
	
	Status dailySalesV2(String orgId, String userId) throws ServiceException;

	Status monthlySales(String orgId, String userId) throws ServiceException;
	
	Status monthlySalesV2(String orgId, String userId) throws ServiceException;
	
	Status dailySalesExcel(String orgId, String userId) throws ServiceException;

}
