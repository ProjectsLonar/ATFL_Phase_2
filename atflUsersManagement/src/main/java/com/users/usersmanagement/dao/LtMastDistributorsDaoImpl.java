package com.users.usersmanagement.dao;

import java.util.List;
import java.util.Optional;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastDistributors;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.SiebelDto;
import com.users.usersmanagement.model.UserDto;
import com.users.usersmanagement.model.LtTemplateHeader;
import com.users.usersmanagement.repository.LtMastDistributorsRepository;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastDistributorsDaoImpl implements LtMastDistributorsDao {

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

	@Autowired
	LtMastDistributorsRepository ltMastDistributorsRepository;

	@Override
	public LtMastDistributors verifyDistributors(String distributorCrmCode, String positionCode, String userCode)
			throws ServiceException {
		String query = env.getProperty("verifyDistributors");
		List<LtMastDistributors> ltMastDistributorsList = jdbcTemplate.query(query,
				new Object[] { distributorCrmCode, positionCode, userCode },
				new BeanPropertyRowMapper<LtMastDistributors>(LtMastDistributors.class));
		if (!ltMastDistributorsList.isEmpty()) {
			return ltMastDistributorsList.get(0);
		}
		return null;
	}
	
	@Override
	public LtMastDistributors verifyDistributorsV1(String distributorCrmCode, String distributorName, String proprietorName)
			throws ServiceException {
		String query = env.getProperty("verifyDistributorsV1");
		List<LtMastDistributors> ltMastDistributorsList = jdbcTemplate.query(query,
				new Object[] { distributorCrmCode, distributorName, proprietorName },
				new BeanPropertyRowMapper<LtMastDistributors>(LtMastDistributors.class));
		if (!ltMastDistributorsList.isEmpty()) {
			return ltMastDistributorsList.get(0);
		}
		return null;
	}

	@Override
	public LtMastDistributors getLtDistributorsById(Long distributorId) throws ServiceException {

		Optional<LtMastDistributors> ltMastDistributors = ltMastDistributorsRepository.findById(distributorId);
		if (ltMastDistributors.isPresent()) {
			return ltMastDistributors.get();
		}
		return null;
	}
	
	@Override
	public List<LtMastDistributors> getAllDistributorAgainstAreahead(RequestDto requestDto)throws ServiceException{

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
		System.out.println("requestDto in area head == " + requestDto);
		
		
		String query = env.getProperty("getAllDistributorAgainstAreahead");
		List<LtMastDistributors> ltMastDistributorsList = jdbcTemplate.query(query,
				new Object[] {requestDto.getUserName(),searchField,requestDto.getLimit(),requestDto.getOffset() },
				new BeanPropertyRowMapper<LtMastDistributors>(LtMastDistributors.class));
		System.out.println("areahead == "+query);
		System.out.println("ltMastDistributorsList result =="+ltMastDistributorsList);
		if (!ltMastDistributorsList.isEmpty()) {
			return ltMastDistributorsList;
		}
		return null;
	}
	
	@Override
	public List<NotificationDetails> getAllNotification(RequestDto requestDto) throws ServiceException{
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
		System.out.println("requestDto" + requestDto);
		
		
		String query = env.getProperty("getAllNotification");
		List<NotificationDetails> notificationList = jdbcTemplate.query(query,
				new Object[] {requestDto.getDistributorId(),requestDto.getUserId(),requestDto.getSearchField(),requestDto.getLimit(),requestDto.getOffset() },
				new BeanPropertyRowMapper<NotificationDetails>(NotificationDetails.class));
		if (!notificationList.isEmpty()) {
			return notificationList;
		}
		return null;
	}

	@Override
	public SiebelDto getUserDataByIdForValidation(Long userId) throws ServiceException {
		try{
			String query= env.getProperty("getUserDataByIdForValidation");
		System.out.println(query); System.out.println(userId);
		List<SiebelDto> siebelUserDataList= jdbcTemplate.query(query, new Object[] {userId}, 
				new BeanPropertyRowMapper<SiebelDto>(SiebelDto.class));
		System.out.println("query =" +query);
		System.out.println("userData =" +siebelUserDataList);
		if(!siebelUserDataList.isEmpty()) {
			return siebelUserDataList.get(0);
		}
		return null;
	}
	catch(Exception e) {
		e.printStackTrace();
		return null;
	}	
	
 }

	@Override
	public String getUserTypeByUserId(Long userId) throws ServiceException {
		
		String query = env.getProperty("getUserTypeByUserId");
		String userType = jdbcTemplate.queryForObject(query, new Object[] {userId}, String.class);
		return userType;
	}

	@Override
	public List<LtMastDistributors> getAllDistributorAgainstSystemAdmin(RequestDto requestDto) throws ServiceException {
		
		List<LtMastDistributors> ltMastDistributorsList = new ArrayList<LtMastDistributors>();
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
		System.out.println("requestDto \n" + requestDto);
		
		
		String query = env.getProperty("getAllDistributorAgainstSystemAdmin");
		 ltMastDistributorsList = jdbcTemplate.query(query,
				new Object[] {requestDto.getUserName(),searchField,requestDto.getLimit(),requestDto.getOffset() },
				new BeanPropertyRowMapper<LtMastDistributors>(LtMastDistributors.class));
		 System.out.println("ltMastDistributorsList \n" + ltMastDistributorsList);
		// if (!ltMastDistributorsList.isEmpty()) {
		//	return ltMastDistributorsList;
		//} 
		
		String dist = "ALL";
		String query1 = env.getProperty("getTemplateForAllDistributor");
		List<LtMastDistributors> ltAllList = jdbcTemplate.query(query1,
				new Object[] {dist, requestDto.getLimit(),requestDto.getOffset()},
				new BeanPropertyRowMapper<LtMastDistributors>(LtMastDistributors.class));
		System.out.println("ltAllList \n" + ltAllList);
		if (!ltAllList.isEmpty()) {
			ltMastDistributorsList.addAll(0,ltAllList);
			return ltMastDistributorsList;
			
		 } 
		 
		System.out.println("Newwww ltMastDistributorsList" + ltMastDistributorsList);
		return ltMastDistributorsList;

	}

//	@Override
//	public LtMastUsers saveSeibelUserData(LtMastUsers user, Long userId) throws ServiceException {
//		String query =  env.getProperty("updateSeibelUserData");
//		//Object value = new Object[] {outletSeq, distCode, beatName, outletCode};
//		this.jdbcTemplate.update(query,user.getOutletId(),user.getRecentSearchId(),,,,,userId);
//		//return null;
//	}
}
