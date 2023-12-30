package com.lonar.folderDeletionSchedular.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.folderDeletionSchedular.dao.LtJobeDao;
import com.lonar.folderDeletionSchedular.model.LtConfigurartion;
import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;
import com.lonar.folderDeletionSchedular.model.LtJobeLogs;
import com.lonar.folderDeletionSchedular.model.LtJobeSchedule;
import com.lonar.folderDeletionSchedular.model.ServiceException;
import com.lonar.folderDeletionSchedular.model.Status;


@Service
public class LtJobeServiceImpl implements LtJobeService {

	@Autowired
	LtJobeDao ltJobeDao;

	@Override
	public Status getAllConfiguration() throws ServiceException {
		Status status = new Status();

		List<LtConfigurartion> list = ltJobeDao.getAllConfiguration();

		if (!list.isEmpty()) {
			status.setData(list);
			return status;

		}
		status.setData(list);
		return status;
	}

	@Override
	public Status getAllJobeImportExport() throws ServiceException {
		Status status = new Status();

		List<LtJobeImportExport> list = ltJobeDao.getAllJobeImportExport();

		if (!list.isEmpty()) {
			status.setData(list);
			return status;

		}
		status.setData(list);
		return status;
	}

	@Override
	public Status getAllJobeLogs() throws ServiceException {
		Status status = new Status();

		List<LtJobeLogs> list = ltJobeDao.getAllJobeLogs();

		if (!list.isEmpty()) {
			status.setData(list);
			return status;

		}
		status.setData(list);
		return status;
	}

	@Override
	public Status getAllJobeSchedule() throws ServiceException {
		Status status = new Status();

		List<LtJobeSchedule> list = ltJobeDao.getAllJobeSchedule();

		if (!list.isEmpty()) {
			status.setData(list);
			return status;

		}
		status.setData(list);
		return status;
	}

	@Override
	public Status saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException {
		Status status = new Status();
		ltJobeImportExport = ltJobeDao.saveJobeImportExportData(ltJobeImportExport);

		if (ltJobeImportExport != null) {
			status.setData(ltJobeImportExport);
			return status;
		}
		status.setData(null);
		return status;
	}

	@Override
	public LtJobeLogs saveJobeLogsData(LtJobeLogs ltJobeLogs) throws ServiceException {
		ltJobeLogs = ltJobeDao.saveJobeLogsData(ltJobeLogs);
		return ltJobeLogs;
	}

	@Override
	public Status saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException {
		Status status = new Status();
		ltJobeSchedule = ltJobeDao.saveJobeScheduleData(ltJobeSchedule);
		if (ltJobeSchedule != null) {
			status.setData(ltJobeSchedule);
			return status;
		}
		status.setData(null);
		return status;
	}

	@Override
	public Status saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException {
		Status status = new Status();
		ltConfigurartion = ltJobeDao.saveConfigurationData(ltConfigurartion);
		if (ltConfigurartion != null) {
			status.setData(ltConfigurartion);
			return status;
		}
		status.setData(null);
		return status;
	}

	@Override
	public Status getJobeImportExportByName(String name) throws ServiceException {
		Status status = new Status();
		LtJobeImportExport ltJobeImportExport = ltJobeDao.getJobeImportExportByName(name);
		
		if (ltJobeImportExport != null) {
			status.setData(ltJobeImportExport);
			return status;
		}
		status.setData(null);
		return status;
	}

}
