package com.eureka.zuul.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eureka.zuul.common.LtMastUsers;
import com.eureka.zuul.common.LtVersion;
@Repository
public class UserDaoImpl implements UserDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;
				
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<LtMastUsers> getAllInactiveUsers() throws ServiceException {
		String query = "select * from lt_mast_users lmu where UPPER(lmu.status) = 'INACTIVE' ";
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	@Override
	public Map<String, LtVersion> getAllAPIVersion() throws ServiceException {
		String query = "select * from lt_version";
		List<LtVersion> ltVersionsList = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtVersion>(LtVersion.class));
		Map<String, LtVersion> apiVersionMap = new LinkedHashMap<String, LtVersion>();
		if (ltVersionsList != null) {
			for (LtVersion ltVersion : ltVersionsList) {
				if (ltVersion.getService_name() != null) {
					System.out.println("API Services Added===>"+ltVersion.getService_name());
					apiVersionMap.put(ltVersion.getService_name(), ltVersion);
				}
			}
		}
		return apiVersionMap;
	}

}
