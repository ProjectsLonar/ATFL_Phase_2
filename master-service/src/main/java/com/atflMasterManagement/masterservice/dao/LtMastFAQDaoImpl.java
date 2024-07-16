package com.atflMasterManagement.masterservice.dao;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

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
import com.atflMasterManagement.masterservice.dto.UserDetailsDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastFAQ;
import com.atflMasterManagement.masterservice.repository.LtMastFAQRepository;
import com.atflMasterManagement.masterservice.servcies.ConsumeApiService;

@Repository
@PropertySource(value = "classpath:queries/ltMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastFAQDaoImpl implements LtMastFAQDao,CodeMaster{
	private static final Logger logger = LoggerFactory.getLogger(LtMastFAQDaoImpl.class);
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

	@Autowired
	private LtMastFAQRepository  ltMastFAQRepository;

	@Override
	public LtMastFAQ saveFAQ(LtMastFAQ ltMastFAQ) throws ServiceException {
		logger.info("in save FAQ");
		return ltMastFAQRepository.save(ltMastFAQ);
	}

	@Override
	public List<LtMastFAQ> getAllFAQ(Long orgId, Long userId) throws ServiceException {
		String query = env.getProperty("findFAQbyOrgId");
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		System.out.println("in dao userDetailsDto = " + userDetailsDto);
		List<LtMastFAQ> ltMastFAQlist = new ArrayList<>();
		String userTypeStr = null;
		if (userDetailsDto.getUserType() != null) {
			userTypeStr = "%" + userDetailsDto.getUserType().toUpperCase().trim() + "%";
		}
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastFAQ> ltMastFAQlist =jdbcTemplate.query(query, new Object[] { orgId, userTypeStr },
//				new BeanPropertyRowMapper<LtMastFAQ>(LtMastFAQ.class));
		System.out.println("in dao userDetailsDto = " + orgId + userTypeStr);
		try {
			ltMastFAQlist = consumeApiService.consumeApi(query, 
					new Object[] { orgId, userTypeStr }, 
					LtMastFAQ.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("FAQ details"+ltMastFAQlist);
		if(!ltMastFAQlist.isEmpty()) {
			return ltMastFAQlist;
		}
		return null;
		
	}
	
	private UserDetailsDto getUserTypeAndDisId(Long userId) throws ServiceException {
		String query = env.getProperty("getUserTypeAndDisId");
		ConsumeApiService consumeApiService = new ConsumeApiService();
		List<UserDetailsDto> list = new ArrayList<>();
//		List<UserDetailsDto> list = jdbcTemplate.query(query, new Object[] { userId },
//				new BeanPropertyRowMapper<UserDetailsDto>(UserDetailsDto.class));
		try {
			list = consumeApiService.consumeApi(query, 
					new Object[] { userId }, 
					UserDetailsDto.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}

}
