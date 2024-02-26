package com.atflMasterManagement.masterservice.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastProductCat;
import com.atflMasterManagement.masterservice.model.RequestDto;

@Repository
@PropertySource(value = "classpath:queries/ltMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastProdCatDaoImpl implements LtMastProdCatDao, CodeMaster {
	private static final Logger logger = LoggerFactory.getLogger(LtMastProdCatDaoImpl.class);
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
	public List<LtMastProductCat> getCategory(RequestDto requestDto) throws ServiceException {
		
		if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}

		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}

		String query = env.getProperty("getCategory");
		List<LtMastProductCat> ltMastProductCatlist = jdbcTemplate.query(query,
				new Object[] { requestDto.getOrgId(), searchField, requestDto.getLimit(), requestDto.getOffset() },
				new BeanPropertyRowMapper<LtMastProductCat>(LtMastProductCat.class));
		if (!ltMastProductCatlist.isEmpty()) {
			return ltMastProductCatlist;
		}
		return null;
	}

	@Override
	public Long getProdCatCount(RequestDto requestDto) throws ServiceException {
		
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
		
		Long totalCount;
		String sql = env.getProperty("getProdCatCount");
		totalCount = jdbcTemplate.queryForObject(sql, new Object[] { requestDto.getOrgId(), searchField,}, Long.class);
		return totalCount;
	}

}
