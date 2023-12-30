package com.lonar.folderDeletionSchedular.dao;

import java.util.List;

import com.lonar.folderDeletionSchedular.model.LtConfigurartion;
import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;
import com.lonar.folderDeletionSchedular.model.LtJobeLogs;
import com.lonar.folderDeletionSchedular.model.LtJobeSchedule;
import com.lonar.folderDeletionSchedular.model.ServiceException;

public interface LtJobeDao {

	List<LtConfigurartion> getAllConfiguration() throws ServiceException;

	List<LtJobeImportExport> getAllJobeImportExport() throws ServiceException;

	LtJobeImportExport getJobeImportExportByName(String name) throws ServiceException;
	
	List<LtJobeLogs> getAllJobeLogs() throws ServiceException;

	List<LtJobeSchedule> getAllJobeSchedule() throws ServiceException;
	
	LtJobeImportExport saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException;
	
	LtJobeLogs saveJobeLogsData(LtJobeLogs ltJobeLogs) throws ServiceException;
	
	LtJobeSchedule saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException;
	
	LtConfigurartion saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException;

}
