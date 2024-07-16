package com.users.usersmanagement.dao;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtMastEmployees;
import com.users.usersmanagement.model.LtMastPositions;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.repository.LtMastEmployeeRepository;
import com.users.usersmanagement.service.ConsumeApiService;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastEmployeeDaoImpl implements LtMastEmployeeDao {

	private JdbcTemplate jdbcTemplate;

	// private static final Logger logger =
	// LoggerFactory.getLogger(LtMastEmployeeDaoImpl.class);

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
	LtMastEmployeeRepository ltMastEmployeeRepository;

	@Override
	public LtMastEmployees verifyEmployee(String employeeCode, String distributorCrmCode, String positionCode)
			throws ServiceException {

		String query = env.getProperty("verifyEmployee");

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,
				new Object[] { employeeCode, distributorCrmCode, positionCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;

	}

	@Override
	public List<LtMastPositions> getSalesPersonsForDistributorV1(RequestDto requestDto) throws ServiceException {
		String query = env.getProperty("getSalesPersonsForDistributorV1");

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
		List<LtMastPositions> positionsList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastPositions> positionsList = jdbcTemplate.query(
//				query, new Object[] { requestDto.getDistributorId(), //requestDto.getOrgId(), 
//						searchField,
//						requestDto.getLimit(), requestDto.getOffset() },
//				new BeanPropertyRowMapper<LtMastPositions>(LtMastPositions.class));
		
		try {
			positionsList = consumeApiService.consumeApi(query, 
					new Object[] { requestDto.getDistributorId(), //requestDto.getOrgId(), 
							searchField,
							requestDto.getLimit(), requestDto.getOffset() }, 
					LtMastPositions.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!positionsList.isEmpty()) {
			return positionsList;
		}
		return null;
	}

	@Override
	public LtMastEmployees verifySalesOfficer(String primaryMobile, String emailId, String positionCode)
			throws ServiceException {

		String query = env.getProperty("verifySalesOfficer");

		String email = emailId.toUpperCase();

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,
				new Object[] { primaryMobile, email, positionCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;

	}

	@Override
	public LtMastEmployees verifyAreaHead(String primaryMobile, String emailId, String positionCode)
			throws ServiceException {

		String query = env.getProperty("verifyAreaHead");

		String email = emailId.toUpperCase();

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,
				new Object[] { primaryMobile, email, positionCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;

	}

	@Override
	public LtMastEmployees getEmployeeByCode(String employeeCode) throws ServiceException {
		String query = env.getProperty("getEmployeeByCode");

		String eCode = employeeCode.toUpperCase();

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,new Object[] { eCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;
	}
	
	@Override
	public LtMastEmployees verifySalesOfficerV1(String employeeCode)throws ServiceException{

		String query = env.getProperty("verifySalesOfficerV1");

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,
				new Object[] { employeeCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;

	}

	
	@Override
	public LtMastEmployees verifyAreaHeadV1(String employeeCode)throws ServiceException{

		String query = env.getProperty("verifyAreaHeadV1");

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,
				new Object[] { employeeCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;

	}
	
	@Override
	public LtMastEmployees verifySystemAdministrator(String employeeCode)throws ServiceException{

		String query = env.getProperty("verifySystemAdministrator");

		List<LtMastEmployees> ltMastEmployees = jdbcTemplate.query(query,
				new Object[] { employeeCode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (!ltMastEmployees.isEmpty()) {
			return ltMastEmployees.get(0);
		}
		return null;

	}
}
