package com.lonar.cartservice.atflCartService.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.LtMastSysVariables;

@Repository
@PropertySource(value = "classpath:queries/cartMasterQueries.properties", ignoreResourceNotFound = true)
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
		String query = env.getProperty("getAllConfiguration");
		List<LtMastSysVariables> ltMastSysVariablesList =   jdbcTemplate.query(query, new Object[]{ }, 
				 new BeanPropertyRowMapper<LtMastSysVariables>(LtMastSysVariables.class)); 
		return ltMastSysVariablesList;
	}

}