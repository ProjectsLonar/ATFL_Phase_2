package com.users.usersmanagement.dao;

import java.util.Date;
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

	@Override
	public void deletePromotionData(Long pramotionId) throws ServiceException {
		// TODO Auto-generated method stub
		String query = env.getProperty("deletePromotionDatabyId");
		Object[] person = new Object[] { pramotionId };
		jdbcTemplate.update(query, person);
	}

	@Override
	public LtPromotion getPromotionData(Long pramotionId) throws ServiceException {
		String query = env.getProperty("getPromotionDataById");
		LtPromotion ltPromotion = jdbcTemplate.queryForObject(query, new Object[] { pramotionId },
				new BeanPropertyRowMapper<LtPromotion>(LtPromotion.class));
		if (ltPromotion!= null)
			return ltPromotion;
		else
			return null;
	}

	@Override
	public void deletePromotionDataById(Long promotionId) throws ServiceException {
		String query = env.getProperty("deletePromotionDatabyId");
		Object[] person = new Object[] { promotionId };
		jdbcTemplate.update(query, person);
		
	}

	@Override
	public void updatePromotionData(MultipartFile file, String createdBy, String pramotionStatus, String promotionName,
			String allTimeShowFlag, String orgId, String startDate, String endDate, String createdBy1,
			String createdBy2, Long promotionId) {
		String query = env.getProperty("updatePromotionData");
		System.out.println(query+ file+ createdBy+ pramotionStatus+
				 promotionName+ allTimeShowFlag+ orgId+ startDate+ endDate+ new Date().toString()+ createdBy1+ createdBy2+ promotionId );
		
		this.jdbcTemplate.update(query, file, createdBy, pramotionStatus,
				 promotionName, allTimeShowFlag, orgId, startDate, endDate, //new Date().toString(), 
				 createdBy1, createdBy2, promotionId );
		
	}

}
