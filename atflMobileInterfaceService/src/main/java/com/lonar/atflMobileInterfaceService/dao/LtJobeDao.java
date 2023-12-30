package com.lonar.atflMobileInterfaceService.dao;

import java.util.List;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.model.LtConfigurartion;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtJobeSchedule;

public interface LtJobeDao {

	List<LtConfigurartion> getAllConfiguration() throws ServiceException;

	List<LtJobeImportExport> getAllJobeImportExport() throws ServiceException;

	LtJobeImportExport getJobeImportExportByName(String name) throws ServiceException;
	
	List<LtJobLogs> getAllJobeLogs() throws ServiceException;

	List<LtJobeSchedule> getAllJobeSchedule() throws ServiceException;
	
	LtJobeImportExport saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException;
	
	LtJobLogs saveJobeLogsData(LtJobLogs ltJobeLogs) throws ServiceException;
	
	LtJobeSchedule saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException;
	
	LtConfigurartion saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException;

}
