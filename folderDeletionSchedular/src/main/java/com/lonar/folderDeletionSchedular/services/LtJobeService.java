package com.lonar.folderDeletionSchedular.services;

import com.lonar.folderDeletionSchedular.model.LtConfigurartion;
import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;
import com.lonar.folderDeletionSchedular.model.LtJobeLogs;
import com.lonar.folderDeletionSchedular.model.LtJobeSchedule;
import com.lonar.folderDeletionSchedular.model.ServiceException;
import com.lonar.folderDeletionSchedular.model.Status;

public interface LtJobeService {

	Status getAllConfiguration() throws ServiceException;

	Status getAllJobeImportExport() throws ServiceException;
	
	Status getJobeImportExportByName(String name) throws ServiceException;

	Status getAllJobeLogs() throws ServiceException;

	Status getAllJobeSchedule() throws ServiceException;
	
	Status saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException;
	
	LtJobeLogs saveJobeLogsData(LtJobeLogs ltJobeLogs) throws ServiceException;
	
	Status saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException;
	
	Status saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException;

}
