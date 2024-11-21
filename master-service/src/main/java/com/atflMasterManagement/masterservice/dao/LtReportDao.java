package com.atflMasterManagement.masterservice.dao;

import java.util.List;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.reports.DistributorDto;
import com.atflMasterManagement.masterservice.reports.ExcelDataDistributor;
import com.atflMasterManagement.masterservice.reports.ExcelDataOutlet;
import com.atflMasterManagement.masterservice.reports.ExcelDataProduct;
import com.atflMasterManagement.masterservice.reports.ExcelDataRegion;
import com.atflMasterManagement.masterservice.reports.ExcelDataSelesperson;
import com.atflMasterManagement.masterservice.reports.ExcelReportDto;
import com.atflMasterManagement.masterservice.reports.ResponseExcelDto;
import com.atflMasterManagement.masterservice.reports.SalesOrderLineCountDto;

public interface LtReportDao {

	List<ExcelDataSelesperson> getSalesReportData(ExcelReportDto excelReportDto) throws ServiceException;

	List<ExcelDataRegion> getRegionwiseSalesReportData(ExcelReportDto excelReportDto) throws ServiceException;

	List<ExcelDataProduct> getProductReportData(ExcelReportDto excelReportDto) throws ServiceException;
	
	List<ExcelDataProduct> getProductReportData2(ExcelReportDto excelReportDto) throws ServiceException;

	List<ExcelDataDistributor> getDistributorReportData(ExcelReportDto excelReportDto) throws ServiceException;

	List<ExcelDataOutlet> getOutletReportData(ExcelReportDto excelReportDto) throws ServiceException;

	List<DistributorDto> searchDistributor(ExcelReportDto excelReportDto) throws ServiceException;
	
	List<ResponseExcelDto> searchSalesPerson(ExcelReportDto excelReportDto)
			throws ServiceException;

	List<ResponseExcelDto> getRegion(String orgId) throws ServiceException;

	List<ResponseExcelDto> searchProduct(ExcelReportDto excelReportDto) throws ServiceException;

	List<ResponseExcelDto> searchOutlets(ExcelReportDto excelReportDto) throws ServiceException;

	List<ResponseExcelDto> getOutletStatus(String orgId) throws ServiceException;

	List<ResponseExcelDto> getCategoryDetails(String orgId) throws ServiceException;

	SalesOrderLineCountDto getSalesOrderLineCount(String distId,ExcelReportDto excelReportDto) throws ServiceException;
	
	List<ExcelDataRegion> getSalesOrderLineCount2(List<ExcelDataRegion> list,ExcelReportDto excelReportDto) throws ServiceException;
	
	String getRegionV2(String orgId, String userId) throws ServiceException;
	
	List<String> getRegionV3(String orgId, String userId) throws ServiceException;
	
	List<Long> getUsersBySalesOfficer(String userId) throws ServiceException;
}
