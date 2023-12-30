package com.lonar.folderDeletionSchedular.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.folderDeletionSchedular.respository.LtMastMailRepository;
import com.lonar.folderDeletionSchedular.respository.LtMastSMSRepository;
import com.lonar.folderDeletionSchedular.model.LtMastMail;
import com.lonar.folderDeletionSchedular.model.LtMastSMS;
import com.lonar.folderDeletionSchedular.model.ServiceException;

@Repository
@PropertySource(value = "classpath:queries/sendQueries.properties", ignoreResourceNotFound = true)
public class LtSendDaoImpl implements LtSendDao {

	@Autowired
	private Environment env;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	private LtMastSMSRepository ltMastSMSRepository;

	@Autowired
	private LtMastMailRepository ltMastMailRepository;

	@Override
	public List<LtMastSMS> getSMSDataPainding() throws ServiceException {
		String query = env.getProperty("getSMSDataPending");

		List<LtMastSMS> list = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtMastSMS>(LtMastSMS.class));

		if (list.isEmpty())
			return list;
		else
			return list;
	}

	@Override
	public List<LtMastMail> getMailDataPainding() throws ServiceException {
		String query = env.getProperty("getMailDataPending");

		List<LtMastMail> list = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtMastMail>(LtMastMail.class));

		if (list.isEmpty())
			return list;
		else
			return list;
	}

	@Override
	public LtMastSMS saveSMS(LtMastSMS ltMastSMS) throws ServiceException {
		LtMastSMS ltMastSMSSave = ltMastSMSRepository.save(ltMastSMS);
		if (ltMastSMSSave != null) {
			return ltMastSMSSave;
		}
		return null;
	}

	@Override
	public LtMastMail saveMail(LtMastMail ltMastMail) throws ServiceException {
		LtMastMail ltMastMailSave = ltMastMailRepository.save(ltMastMail);
		if (ltMastMailSave != null) {
			return ltMastMailSave;
		}
		return null;
	}

}
