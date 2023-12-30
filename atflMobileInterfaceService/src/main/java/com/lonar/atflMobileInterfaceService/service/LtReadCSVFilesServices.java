package com.lonar.atflMobileInterfaceService.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.Status;

public interface LtReadCSVFilesServices {

	Status downloadfilesFromFTP() throws ServiceException;
	
	Status readCsvFile() throws ServiceException, FileNotFoundException, IOException;
	
	LtJobLogs readDistributorCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	LtJobLogs readPositionCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	LtJobLogs readOutletCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	LtJobLogs readProductCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	LtJobLogs readPriceListCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	LtJobLogs readEmployeeCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	LtJobLogs readInventoryCSVFile(LtJobLogs ltJobLogs) throws IOException, FileNotFoundException, ServiceException;
	
	
}
