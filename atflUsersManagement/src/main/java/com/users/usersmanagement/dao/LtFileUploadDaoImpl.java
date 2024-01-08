package com.users.usersmanagement.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtFileUpload;
import com.users.usersmanagement.repository.LtConfigurationRepository;

@Repository
@PropertySource(value = "classpath:queries/usermasterqueries.properties", ignoreResourceNotFound = true)
public class LtFileUploadDaoImpl implements LtFileUploadDao{

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;
	
	@Autowired
	LtConfigurationRepository ltConfigurationRepository;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	
	@Override
	public List<LtFileUpload> getAllUploadedFiles(String orgId, Long limit, Long offset, Long userId)
			throws ServiceException {

		String query = env.getProperty("getAllUploadedFiles");
		List<LtFileUpload> list = jdbcTemplate.query(query, new Object[] { limit, offset},
				new BeanPropertyRowMapper<LtFileUpload>(LtFileUpload.class));
		
		return list;
	}

	@Override
	public LtFileUpload getfileByName(MultipartFile file) throws ServiceException {
		String query = env.getProperty("getfileByName");
		String fileName = file.getOriginalFilename();
		List<LtFileUpload> list = jdbcTemplate.query(query, new Object[] { fileName },
				new BeanPropertyRowMapper<LtFileUpload>(LtFileUpload.class));
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

}
