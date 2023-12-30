package com.lonar.atflMobileInterfaceService.service;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.model.LtConfigurartion;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtJobeSchedule;
import com.lonar.atflMobileInterfaceService.model.Status;

public interface LtJobeService {

	Status getAllConfiguration() throws ServiceException;

	Status getAllJobeImportExport() throws ServiceException;
	
	Status getJobeImportExportByName(String name) throws ServiceException;

	Status getAllJobeLogs() throws ServiceException;

	Status getAllJobeSchedule() throws ServiceException;
	
	Status saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException;
	
	LtJobLogs saveJobeLogsData(LtJobLogs ltJobeLogs) throws ServiceException;
	
	Status saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException;
	
	Status saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException;

}
