package com.users.usersmanagement.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastOrganisations;
import com.users.usersmanagement.model.LtMastSmsToken;
import com.users.usersmanagement.repository.LtMastSmsTokenRepository;

@Repository
public class LtMastSmsTokenDaoImpl implements LtMastSmsTokenDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;
	
	@Autowired
	LtMastSmsTokenRepository ltMastSmsTokenRepository;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<LtMastSmsToken> getBySmsId(Long userId, String transId) throws ServiceException {
		String query = "SELECT * FROM LT_MAST_SMS_TOKENS WHERE ( SMS_STATUS = 'SENDING' OR SMS_STATUS = 'FAIL TO SEND' ) "
				+ " AND COALESCE(USER_ID,'xx') =  COALESCE(?,COALESCE(USER_ID,'xx'))  AND Transaction_id = ? ";

		List<LtMastSmsToken> ltMastSmsTokenList = jdbcTemplate.query(query, new Object[] { userId, transId },
				new BeanPropertyRowMapper<LtMastSmsToken>(LtMastSmsToken.class));
		return ltMastSmsTokenList;
	}

	@Override
	public int updateStatus(String status, Long userId, String transId) throws ServiceException {
		int result = 0;
		result = jdbcTemplate.update(" UPDATE LT_MAST_SMS_TOKENS SET SMS_STATUS = ?  " + "  WHERE   Transaction_id = ? ",
				status.toUpperCase(), transId);
		return result;

	}

	@Override
	public LtMastOrganisations getDefaultOrganisationByCode(String code) throws ServiceException {
		String query = "SELECT * FROM LT_MAST_ORGANISATIONS	WHERE company_name = ? ";

		List<LtMastOrganisations> ltMastOrganisationsList = jdbcTemplate.query(query, new Object[] { code },
				new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class));
		if (!ltMastOrganisationsList.isEmpty())
			return ltMastOrganisationsList.get(0);
		else
			return null;
	}

	@Override
	public List<LtMastSmsToken> saveall(List<LtMastSmsToken> ltMastSmsTokenList) throws ServiceException {
		return (List<LtMastSmsToken>) ltMastSmsTokenRepository.saveAll(ltMastSmsTokenList);
	}

}
