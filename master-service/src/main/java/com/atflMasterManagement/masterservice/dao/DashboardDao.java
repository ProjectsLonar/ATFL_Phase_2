package com.atflMasterManagement.masterservice.dao;

import java.util.List;
import java.util.Map;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dto.CategoryRevenueDto;
import com.atflMasterManagement.masterservice.dto.CategoryRevenueResponseDto;
import com.atflMasterManagement.masterservice.dto.DailyAndMonthlyDto;
import com.atflMasterManagement.masterservice.dto.DailySalesResponseDto;
import com.atflMasterManagement.masterservice.dto.MonthlyResponseDto;
import com.atflMasterManagement.masterservice.dto.OrderAndLineCountDto;
import com.atflMasterManagement.masterservice.dto.StatusWiseOrdersCountDto;

public interface DashboardDao {
	List<StatusWiseOrdersCountDto> statusWiseOrdersCount(String orgId, String userId) throws ServiceException;

	List<CategoryRevenueDto> categoryRevenueDistribution(String orgId, String userId) throws ServiceException;

	List<DailyAndMonthlyDto> dailySales(String orgId, String userId) throws ServiceException;
	
	List<DailySalesResponseDto> dailySalesV2(String orgId, String userId) throws ServiceException;

	List<DailyAndMonthlyDto> monthlySales(String orgId, String userId) throws ServiceException;
	
	List<MonthlyResponseDto> monthlySalesV2(String orgId, String userId) throws ServiceException;
	
	//Long getDistributorIdByUserId(String userId) throws ServiceException;
	String getDistributorIdByUserId(String userId) throws ServiceException;
	
	OrderAndLineCountDto getOrderAndLineCountData(String distId, String orderDate, String userId) throws ServiceException;
	
	Map<String,DailySalesResponseDto> getOrderAndLineCountDataV2(String distId, String userId,Map<String,DailySalesResponseDto> monthlyResponseDtoMap) throws ServiceException;
	
	OrderAndLineCountDto getOrderCountData(String distId, String catId, String userId) throws ServiceException;
	
	Map<Long,CategoryRevenueResponseDto> getOrderCountDataV2(Map<Long,CategoryRevenueResponseDto> categoryRevenueResponseDtoMap, String userId) throws ServiceException;
	
	OrderAndLineCountDto getCountDataForMonthlyDashboard(String distId, String monthString, String userId) throws ServiceException;

	Map<String,MonthlyResponseDto> getCountDataForMonthlyDashboardV2(String distId,String userId,Map<String,MonthlyResponseDto> monthlyResponseDtoMap) throws ServiceException;
}
