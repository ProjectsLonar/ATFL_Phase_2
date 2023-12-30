package com.lonar.sms.atflSmsService.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtMastOrganisations;

@Repository
public class LtMastOrganisationDaoImpl implements LtMastOrganisationDao{

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private JdbcTemplate getJdbcTemplate() {
		// TODO Auto-generated method stub
		return jdbcTemplate;
	}
	
	@Override
	public LtMastOrganisations getLtMastOrganisationById(String orgId) throws ServiceException {
		String query = " SELECT * FROM LT_MAST_ORGANISATIONS WHERE ORG_ID = ? ";
		LtMastOrganisations ltMastOrganisations=   jdbcTemplate.queryForObject(query, new Object[]{ orgId}, 
				 new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class)); 
		return ltMastOrganisations;
	}

	@Override
	public List<LtMastOrganisations> getAllOrganisation() throws ServiceException {
		String query = " SELECT * FROM LT_MAST_ORGANISATIONS  ";
		List<LtMastOrganisations> ltMastOrganisationsList = jdbcTemplate.query(query,  new Object[]{ }, 
				new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class));
		return ltMastOrganisationsList;
	}

	@Override
	public List<LtMastOrganisations> ltMastOrganisationDao(String organisationCode) throws ServiceException {
		String query = " SELECT * FROM LT_MAST_ORGANISATIONS  WHERE UPPER(ORGANISATION_CODE) = ? ";
		List<LtMastOrganisations> ltMastOrganisationsList = jdbcTemplate.query(query,  new Object[]{organisationCode.toUpperCase() }, 
				new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class));
		return ltMastOrganisationsList;
	}

	/*@Override
	public LtMastOrganisations getPaymentDetailsById(Long orgId) throws ServiceException {
		String query = " SELECT SUPPLIER_ID,PRIMARY_NUMBER,SUPPLIER_UPI_ID,SUPPLIER_PAYEE_NAME FROM LT_MAST_SUPPLIERS WHERE SUPPLIER_ID = ? ";
		LtMastOrganisations ltMastOrganisations=   jdbcTemplate.queryForObject(query, new Object[]{orgId}, 
				 new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class)); 
		return ltMastOrganisations;
	}*/

}
