package com.lonar.atflMobileInterfaceService.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.model.LtConfigurartion;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtJobeSchedule;
import com.lonar.atflMobileInterfaceService.repository.LtConfigurationRepository;
import com.lonar.atflMobileInterfaceService.repository.LtJobImportExportRepository;
import com.lonar.atflMobileInterfaceService.repository.LtJobScheduleRepository;
import com.lonar.atflMobileInterfaceService.repository.LttJobeLogsRepository;

@Repository
@PropertySource(value = "classpath:queries/jobeQueries.properties", ignoreResourceNotFound = true)
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
	public List<LtJobLogs> getAllJobeLogs() throws ServiceException {
		List<LtJobLogs> list = (List<LtJobLogs>) lttJobeLogsRepository.findAll();
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
	public LtJobLogs saveJobeLogsData(LtJobLogs ltJobeLogs) throws ServiceException {
		LtJobLogs ltJobeImportExportSave = lttJobeLogsRepository.save(ltJobeLogs);
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
