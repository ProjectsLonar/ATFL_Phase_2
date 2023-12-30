package com.lonar.folderDeletionSchedular.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.folderDeletionSchedular.respository.LtConfigurationRepository;
import com.lonar.folderDeletionSchedular.respository.LtJobImportExportRepository;
import com.lonar.folderDeletionSchedular.respository.LtJobScheduleRepository;
import com.lonar.folderDeletionSchedular.respository.LttJobeLogsRepository;
import com.lonar.folderDeletionSchedular.model.LtConfigurartion;
import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;
import com.lonar.folderDeletionSchedular.model.LtJobeLogs;
import com.lonar.folderDeletionSchedular.model.LtJobeSchedule;
import com.lonar.folderDeletionSchedular.model.ServiceException;


@Repository
public class LtJobeDaoImpl implements LtJobeDao {

	@Autowired
	LtJobScheduleRepository ltJobScheduleRepository;

	@Autowired
	LtConfigurationRepository ltConfigurationRepository;

	@Autowired
	LtJobImportExportRepository ltJobImportExportRepository;

	@Autowired
	LttJobeLogsRepository lttJobeLogsRepository;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<LtConfigurartion> getAllConfiguration() throws ServiceException {
		List<LtConfigurartion> list = (List<LtConfigurartion>) ltConfigurationRepository.findAll();
		return list;
	}

	@Override
	public List<LtJobeImportExport> getAllJobeImportExport() throws ServiceException {
		List<LtJobeImportExport> list = (List<LtJobeImportExport>) ltJobImportExportRepository.findAll();
		return list;
	}

	@Override
	public List<LtJobeLogs> getAllJobeLogs() throws ServiceException {
		List<LtJobeLogs> list = (List<LtJobeLogs>) lttJobeLogsRepository.findAll();
		return list;
	}

	@Override
	public List<LtJobeSchedule> getAllJobeSchedule() throws ServiceException {
		List<LtJobeSchedule> list = (List<LtJobeSchedule>) ltJobScheduleRepository.findAll();
		return list;
	}

	@Override
	public LtJobeImportExport saveJobeImportExportData(LtJobeImportExport ltJobeImportExport) throws ServiceException {
		LtJobeImportExport ltJobeImportExportSave = ltJobImportExportRepository.save(ltJobeImportExport);
		if (ltJobeImportExportSave != null) {
			return ltJobeImportExportSave;
		}
		return null;
	}

	@Override
	public LtJobeLogs saveJobeLogsData(LtJobeLogs ltJobeLogs) throws ServiceException {
		LtJobeLogs ltJobeImportExportSave = lttJobeLogsRepository.save(ltJobeLogs);
		if (ltJobeImportExportSave != null) {
			return ltJobeImportExportSave;
		}
		return null;
	}

	@Override
	public LtJobeSchedule saveJobeScheduleData(LtJobeSchedule ltJobeSchedule) throws ServiceException {
		LtJobeSchedule ltJobeImportExportSave = ltJobScheduleRepository.save(ltJobeSchedule);
		if (ltJobeImportExportSave != null) {
			return ltJobeImportExportSave;
		}
		return null;
	}

	@Override
	public LtConfigurartion saveConfigurationData(LtConfigurartion ltConfigurartion) throws ServiceException {
		LtConfigurartion ltConfigurartionSave = ltConfigurationRepository.save(ltConfigurartion);
		if (ltConfigurartionSave != null) {
			return ltConfigurartionSave;
		}
		return null;
	}

	@Override
	public LtJobeImportExport getJobeImportExportByName(String name) throws ServiceException {

		String query = "select ljie.* from lt_job_import_export ljie where ljie.name = ?";

		List<LtJobeImportExport> list = jdbcTemplate.query(query, new Object[] { name },
				new BeanPropertyRowMapper<LtJobeImportExport>(LtJobeImportExport.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}
}
