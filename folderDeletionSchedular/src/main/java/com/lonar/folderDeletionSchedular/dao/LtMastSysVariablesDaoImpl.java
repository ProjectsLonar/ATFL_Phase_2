package com.lonar.folderDeletionSchedular.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.folderDeletionSchedular.model.LtMastSysVariables;
import com.lonar.folderDeletionSchedular.model.ServiceException;

@Repository
@PropertySource(value = "classpath:queries/ltMasterQueries.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:queries/config.properties", ignoreResourceNotFound = true)
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
		//System.out.println(query);
		List<LtMastSysVariables> ltMastSysVariablesList =   jdbcTemplate.query(query, new Object[]{ Long.parseLong(env.getProperty("orgId"))}, 
				 new BeanPropertyRowMapper<LtMastSysVariables>(LtMastSysVariables.class)); 
		return ltMastSysVariablesList;
	}

}
