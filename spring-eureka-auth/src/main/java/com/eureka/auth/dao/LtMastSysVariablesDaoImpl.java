package com.eureka.auth.dao;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.LtMastSysVariables;

@Repository
public class LtMastSysVariablesDaoImpl implements LtMastSysVariablesDao {

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
	public List<LtMastSysVariables> loadAllConfiguration() throws ServiceException {
		String query = "SELECT * FROM LT_MAST_SYS_VARIABLES";
		List<LtMastSysVariables> ltMastSysVariablesList =   jdbcTemplate.query(query, new Object[]{ }, 
				 new BeanPropertyRowMapper<LtMastSysVariables>(LtMastSysVariables.class)); 
		return ltMastSysVariablesList;
	}

}
