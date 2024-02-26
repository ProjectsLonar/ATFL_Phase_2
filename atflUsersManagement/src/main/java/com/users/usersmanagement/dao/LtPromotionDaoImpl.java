package com.users.usersmanagement.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtPromotion;
import com.users.usersmanagement.model.UserDetailsDto;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtPromotionDaoImpl implements LtPromotionDao, CodeMaster {

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
	public List<LtPromotion> getPromotionDataV1(String orgId, Long limit, Long offset, Long userId)
			throws ServiceException {
		String defaultLimit = env.getProperty("limit_value");
		List<LtPromotion> list;
		
		String defaultoffset = env.getProperty("offset_value");
		
		
		if (limit == 0 || limit == 1) {
			limit = Long.valueOf(defaultLimit);
		}

		if (offset == 0) {
			offset = Long.valueOf(defaultoffset);
		}
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
			String query = env.getProperty("getPromotionDataForAdmin");
			list = jdbcTemplate.query(query, new Object[] { orgId, limit, offset },
					new BeanPropertyRowMapper<LtPromotion>(LtPromotion.class));
		} else {
			String query = env.getProperty("getPromotionData");
			list = jdbcTemplate.query(query, new Object[] { orgId, limit, offset },
					new BeanPropertyRowMapper<LtPromotion>(LtPromotion.class));
		}

		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	@Override
	public List<LtPromotion> getPromotionData(String orgId, Long limit, Long offset) throws ServiceException {
		String defaultLimit = env.getProperty("limit_value");
		List<LtPromotion> list;
String defaultoffset = env.getProperty("offset_value");
		
		
		if (limit == 0 || limit == 1) {
			limit = Long.valueOf(defaultLimit);
		}

		if (offset == 0) {
			offset = Long.valueOf(defaultoffset);
		}

		String query = env.getProperty("getPromotionData");
		list = jdbcTemplate.query(query, new Object[] { orgId, limit, offset },
				new BeanPropertyRowMapper<LtPromotion>(LtPromotion.class));

		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	private UserDetailsDto getUserTypeAndDisId(Long userId) throws ServiceException {
		String query = env.getProperty("getUserTypeAndDisId");
		List<UserDetailsDto> list = jdbcTemplate.query(query, new Object[] { userId },
				new BeanPropertyRowMapper<UserDetailsDto>(UserDetailsDto.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}
}
