package com.lonar.sms.atflSmsService.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtOrganisationSMS;

@Repository
public class LtOrganisationSMSDAOImpl implements LtOrganisationSMSDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<LtOrganisationSMS> getAll() throws ServiceException {
		String query = "SELECT * FROM LT_ORGANISATION_SMS";
		List<LtOrganisationSMS> ltOrganisationSMSList = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtOrganisationSMS>(LtOrganisationSMS.class));
		return ltOrganisationSMSList;
	}

	@Override
	public LtOrganisationSMS getOrgSmsbyOrgId() throws ServiceException {
		String query = "SELECT orgsms.* FROM lt_organisation_sms orgsms";
		List<LtOrganisationSMS> ltOrganisationSMSList = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtOrganisationSMS>(LtOrganisationSMS.class));
		return ltOrganisationSMSList.get(0);
	}

}
