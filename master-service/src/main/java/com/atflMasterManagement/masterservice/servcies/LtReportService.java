package com.atflMasterManagement.masterservice.servcies;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.reports.ExcelReportDto;

public interface LtReportService {

	Status getSalesReportData(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;

	Status getRegionwiseSalesReportData(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;

	Status getRegionwiseSalesReportData2(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;
	
	Status getProductReportData(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;

	Status getProductReportData2(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;
	
	Status getDistributorReportData(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;

	Status getOutletReportData(ExcelReportDto excelReportDto)throws ServiceException, FileNotFoundException, IOException;

	Status searchDistributor(ExcelReportDto excelReportDto)throws ServiceException;

	Status searchSalesPerson(ExcelReportDto excelReportDto)throws ServiceException;

	Status getRegion(String orgId)throws ServiceException;
	
	Status getRegionV2(String orgId, String userid)throws ServiceException;

	Status searchProduct(ExcelReportDto excelReportDto)throws ServiceException;

	Status searchOutlets(ExcelReportDto excelReportDto)throws ServiceException;

	Status getOutletStatus(String orgId)throws ServiceException;

	Status getCategoryDetails(String orgId)throws ServiceException;

}
