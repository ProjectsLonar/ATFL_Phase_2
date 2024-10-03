package com.users.usersmanagement.dao;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtConfigurartion;
import com.users.usersmanagement.model.LtMastLogins;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.UserDto;
import com.users.usersmanagement.repository.LtConfigurationRepository;
import com.users.usersmanagement.repository.LtMastUsersRepository;
import com.users.usersmanagement.service.ConsumeApiService;


@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class AtflMastUsersDaoImpl implements AtflMastUsersDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;
	
	@Autowired
	LtConfigurationRepository ltConfigurationRepository;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	LtMastUsersRepository ltMastUsersRepository;

	@Override
	public LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber) throws ServiceException {
		
		
		String query = env.getProperty("getLtMastUsersByMobileNumber");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim() },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}
		

	@Override
	public LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException {
		return ltMastUsersRepository.save(ltMastUser);
	}

	@Override
	public LtMastLogins getLoginDetailsByUserId(Long userId) throws ServiceException {
		String query = env.getProperty("getLoginDetailsByUserId");
		List<LtMastLogins> list = jdbcTemplate.query(query, new Object[] { userId},
				new BeanPropertyRowMapper<LtMastLogins>(LtMastLogins.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}

	@Override
	public LtMastUsers getUserById(Long userId) throws ServiceException {
		System.out.println("in dao user id"+userId);
		Optional<LtMastUsers> ltMastUsers = ltMastUsersRepository.findById(userId);
		System.out.println("after repo execution user id"+ltMastUsers.get());
		
		if (ltMastUsers.isPresent()) {
			return ltMastUsers.get();
		}
		return null;
	
	}

	@Override
	public LtMastUsers delete(Long userId) throws ServiceException {
		
		ltMastUsersRepository.deleteById(userId);
		if (ltMastUsersRepository.existsById(userId)) {
			return ltMastUsersRepository.findById(userId).get();
		} else
			return null;
	}

	@Override
	public List<LtMastUsers> getUserByName(String name) throws ServiceException {
		String query = env.getProperty("getUserByName");
		List<LtMastUsers> ltMastUsers = jdbcTemplate.query(query, new Object[] { "%" + name.toUpperCase() + "%" },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return ltMastUsers;
	}

	@Override
	public boolean saveFireBaseToken(LtMastLogins ltMastLogins) throws ServiceException {
		if (ltMastLogins.getTokenId() != null) {
			String sqlQuery = env.getProperty("saveFireBaseToken");
			int res = jdbcTemplate.update(sqlQuery, ltMastLogins.getTokenId(), ltMastLogins.getUserId());
			if (res != 0) {
				return true;
			} else
				return false;
		} else {
			return false;
		}
	}

	@Override
	public List<LtMastUsers> getCustomerByName(String name) throws ServiceException {
		String query = env.getProperty("getCustomerByName");
		List<LtMastUsers> ltMastUsers = jdbcTemplate.query(query, new Object[] { "%" + name.toUpperCase() + "%" },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return ltMastUsers;
	}

	@Override
	public List<LtMastUsers> getPenddingApprovalByDistributorId(Long distributorId, Long userId)
			throws ServiceException {
		String query = env.getProperty("getPenddingApprovalByDistributorId");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { distributorId, userId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	@Override
	public List<LtMastUsers> getDistributorUserByRoleAndId(Long distributorId) throws ServiceException {
		String query = env.getProperty("getDistributorUserByRoleAndId");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { distributorId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	@Override
	public List<LtMastUsers> getAllUserByDistributorId(Long distributorId) throws ServiceException {
		String query = env.getProperty("getAllUserByDistributorId");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { distributorId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	@Override
	public List<LtMastUsers> getAllUsersData() throws ServiceException {
		Iterable<LtMastUsers> ltMastUsers = ltMastUsersRepository.findAll();
		return null;
	}

	@Override
	public LtMastUsers getUserAllMasterDataById(Long userId) throws ServiceException {

		String query = env.getProperty("getUserAllMasterDataById");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { userId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<LtMastUsers> getUsersList(RequestDto requestDto) throws IOException {
		String query = env.getProperty("getUsersList");
		if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
		
		
		Long userId;
		if(requestDto.getUserId() != null) {
			userId = requestDto.getUserId();
		}else {
			userId =0L;
		}
		System.out.println("USER ID :: "+userId);

		String userName = null;
		if (requestDto.getSearchField() != null) {
			userName = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
		ConsumeApiService consumeApiService = new ConsumeApiService();
		List<LtMastUsers> ltMastOutletslist = new ArrayList<>();
		try {
//			List<LtMastUsers> ltMastOutletslist = jdbcTemplate.query(query,
//					new Object[] { requestDto.getOrgId(), requestDto.getDistributorId(), requestDto.getOutletId(),
//							requestDto.getStatus(), requestDto.getUserType(), userName,
//							requestDto.getSalesPersonId(), userId ,
//							requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

			System.out.println("query =  "+query);
			
			ltMastOutletslist = consumeApiService.consumeApi(query, 
					new Object[] { requestDto.getOrgId(), requestDto.getOrgId() ,//requestDto.getDistributorId(), 
							requestDto.getOutletId(),
							requestDto.getStatus(), requestDto.getUserType(), userName,
							//requestDto.getSalesPersonId(), 
							userId ,
							requestDto.getLimit(), requestDto.getOffset() }, 
					LtMastUsers.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			if (!ltMastOutletslist.isEmpty()) {
				return ltMastOutletslist;
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return null;

	}

	@Override
	public List<LtMastUsers> getAllInactiveUsers() throws ServiceException {
		String query = env.getProperty("getAllInactiveUsers");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}

	@Override
	public List<LtMastUsers> getActiveUsersDistByUserId(Long userId) throws ServiceException {
		String query = env.getProperty("getDistributorsByUserId");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] {userId.toString()},
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (!list.isEmpty()) {
			return list;
		}
		return list;
	}
	
	@Override
	public List<LtConfigurartion> getAllConfiguration() throws ServiceException {
		List<LtConfigurartion> list = (List<LtConfigurartion>) ltConfigurationRepository.findAll();
		return list;
	}

	@Override
	public UserDto verifyUserDetailsByMobileNumbervInSiebel(String mobileNumber) throws ServiceException {
		try {
		String query = env.getProperty("verifyUserDetailsByMobileNumbervInSiebel");
//		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber, mobileNumber,mobileNumber },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
//		
		System.out.println("Input Dao MobNo is"+mobileNumber);
		List<UserDto> list = jdbcTemplate.query(query, new Object[] {mobileNumber},
				new BeanPropertyRowMapper<UserDto>(UserDto.class));
		System.out.println("User list is"+list);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LtMastUsers getSiebelUsersByMobileNumber(String mobileNumber) throws ServiceException {
		String query = env.getProperty("getSiebelUsersByMobileNumber");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim() },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}
	
	@Override
	public LtMastUsers verifyUserDetailsByMobileNumbervInSiebel1(String mobileNumber) throws ServiceException {
		try {
		String query = env.getProperty("verifyUserDetailsByMobileNumbervInSiebel");
//		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber, mobileNumber,mobileNumber },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
//		
		System.out.println("Input Dao MobNo is"+mobileNumber);
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] {mobileNumber},
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		System.out.println("User list is"+list);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return list.get(0);
	}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
