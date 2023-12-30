package com.lonar.atflMobileInterfaceService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.dao.LtJobeDao;
import com.lonar.atflMobileInterfaceService.model.CodeMaster;
import com.lonar.atflMobileInterfaceService.model.LtConfigurartion;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtJobeSchedule;
import com.lonar.atflMobileInterfaceService.model.Status;

@Service
public class LtJobeServiceImpl implements LtJobeService, CodeMaster {

	@Autowired
	LtJobeDao ltJobeDao;

	@Autowired
	private LtMastCommonMessageService ltMastCommonMessageService;

	@Override
	public Status getAllConfiguration() throws ServiceException {
		Status status = new Status();

		List<LtConfigurartion> list = ltJobeDao.getAllConfiguration();

		if (!list.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(list);
			return status;

		}

		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(list);
		return status;
	}

	@Override
	public Status getAllJobeImportExport() throws ServiceException {
		Status status = new Status();

		List<LtJobeImportExport> list = ltJobeDao.getAllJobeImportExport();

		if (!list.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(list);
			return status;

		}

		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(list);
		return status;
	}

	@Override
	public Status getAllJobeLogs() throws ServiceException {
		Status status = new Status();

		List<LtJobLogs> list = ltJobeDao.getAllJobeLogs();

		if (!list.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(list);
			return status;

		}

		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(list);
		return status;
	}

	@Override
	public Status getAllJobeSchedule() throws ServiceException {
		Status status = new Status();

		List<LtJobeSchedule> list = ltJobeDao.getAllJobeSchedule();

		if (!list.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(list);
			return status;

		}

		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(list);
		return status;
	}

	@Override
	public Status saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException {
		Status status = new Status();
		ltJobeImportExport = ltJobeDao.saveJobeImportExportData(ltJobeImportExport);

		if (ltJobeImportExport != null) {
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
			status.setData(ltJobeImportExport);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public LtJobLogs saveJobeLogsData(LtJobLogs ltJobeLogs) throws ServiceException {
		ltJobeLogs = ltJobeDao.saveJobeLogsData(ltJobeLogs);
		return ltJobeLogs;
	}

	@Override
	public Status saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException {
		Status status = new Status();
		ltJobeSchedule = ltJobeDao.saveJobeScheduleData(ltJobeSchedule);
		if (ltJobeSchedule != null) {
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
			status.setData(ltJobeSchedule);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException {
		Status status = new Status();
		ltConfigurartion = ltJobeDao.saveConfigurationData(ltConfigurartion);
		if (ltConfigurartion != null) {
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
			status.setData(ltConfigurartion);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		status.setData(null);
		return status;
	}

	@Override
	public Status getJobeImportExportByName(String name) throws ServiceException {
		Status status = new Status();
		LtJobeImportExport ltJobeImportExport = ltJobeDao.getJobeImportExportByName(name);
		
		if (ltJobeImportExport != null) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(ltJobeImportExport);
			return status;
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

}
