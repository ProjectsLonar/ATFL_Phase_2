package com.eureka.auth.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eureka.auth.model.LtMastLogins;
import com.eureka.auth.model.LtMastUsers;
import com.eureka.auth.model.LtVersion;
import com.eureka.auth.model.UserLoginDto;
import com.eureka.auth.repository.LtMastUsersRepository;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class AtflMastUsersDaoImpl implements AtflMastUsersDao {

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

	@Autowired
	LtMastUsersRepository ltMastUsersRepository;

	//This is original method commented by Rohan for optimization on 3 June 2024
	@Override
	public LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByMobileNumber");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim() },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		System.out.println("list"+list);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}
	
	//This is optimized method created by Rohan for optimization on 3 June 2024
	@Override
	public UserLoginDto getLtMastUsersByMobileNumber1(String mobileNumber) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByMobileNumber");
		List<UserLoginDto> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim() },
				new BeanPropertyRowMapper<UserLoginDto>(UserLoginDto.class));
		System.out.println("list"+list);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastLogins getLoginDetailsByUserId(Long userId) throws ServiceException {
		String query = env.getProperty("getLoginDetailsByUserId");
		List<LtMastLogins> list = jdbcTemplate.query(query, new Object[] { userId },
				new BeanPropertyRowMapper<LtMastLogins>(LtMastLogins.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}

	@Override
	public LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException {
		return ltMastUsersRepository.save(ltMastUser);
	}

	@Override
	public LtVersion getVersionAuthAPI(LtVersion ltVersion) throws ServiceException {
		String query = "select * from lt_version where service_name = '" + ltVersion.getService_name() + "'";
		List<LtVersion> ltVersionsList = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtVersion>(LtVersion.class));
		Map<String, LtVersion> apiVersionMap = new LinkedHashMap<String, LtVersion>();

		if (ltVersionsList != null) {
			return ltVersionsList.get(0);
		}

		return null;
	}

}
