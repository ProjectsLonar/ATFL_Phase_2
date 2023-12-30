package com.lonar.sms.atflSmsService.dao;

import java.awt.Menu;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lonar.sms.atflSmsService.common.ServiceException;
import com.lonar.sms.atflSmsService.model.LtMastLogins;
import com.lonar.sms.atflSmsService.model.LtMastUsers;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastUsersDaoImpl implements LtMastUsersDao {

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
	public LtMastUsers getLtMastUsersByMobileNumber(String mobileNumber, Long supplierId) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByMobileNumber");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { mobileNumber.trim(), supplierId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastUsers saveLtMastUsers(LtMastUsers ltMastUser) throws ServiceException {
		return null;
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
	public LtMastUsers getUserById(Long userId) throws ServiceException {
		String query = "SELECT lmu.* FROM LT_MAST_USERS lmu WHERE lmu.USER_ID = ?";
		LtMastUsers ltMastUsers = jdbcTemplate.queryForObject(query, new Object[] { userId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return ltMastUsers;
	}

	@Override
	public Long getLtMastUsersCount(LtMastUsers input, Long supplierId) throws ServiceException {
		String query = env.getProperty("getLtMastUsersCount");
		String name = null;
		if (input.getUserName() != null && !input.getUserName().equals("")) {
			name = "%" + input.getUserName().trim().toUpperCase() + "%";
		}
		String count = (String) getJdbcTemplate().queryForObject(query, new Object[] { supplierId, name },
				String.class);
		return Long.parseLong(count);
	}

	@Override
	public List<LtMastUsers> getLtMastUsersDataTable(LtMastUsers input, Long supplierId) throws ServiceException {

		if (input.getColumnNo() == null || input.getColumnNo() == 0) {
			input.setColumnNo(1);
		}
		String name = null;
		if (input.getUserName() != null && !input.getUserName().equals("")) {
			name = "%" + input.getUserName().toUpperCase() + "%";
		}

		String query = env.getProperty("getLtMastUsersDataTable");
		return (List<LtMastUsers>) jdbcTemplate.query(query,
				new Object[] { supplierId, name, input.getColumnNo(), input.getLength(), input.getStart() },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
	}

	@Override
	public LtMastUsers delete(Long userId) throws ServiceException {
		
			return null;
	}

	@Override
	public List<LtMastUsers> getUserByType(String userType, Long supplierId) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByType");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { userType.toUpperCase(), supplierId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return list;
	}

	@Override
	public LtMastUsers getLtMastUsersByEmail(String email, Long supplierId) throws ServiceException {
		String query = env.getProperty("getLtMastUsersByEmail");
		List<LtMastUsers> list = jdbcTemplate.query(query, new Object[] { email.trim().toUpperCase(), supplierId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public List<Menu> findMenu(String role, Long supplierId) throws ServiceException {
	return null;

		
	}

	@Override
	public List<LtMastUsers> getUserByName(String name, Long supplierId) throws ServiceException {
		String query = env.getProperty("getUserByName");
		List<LtMastUsers> ltMastUsers = jdbcTemplate.query(query,
				new Object[] { "%" + name.toUpperCase() + "%", supplierId },
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
	public Long getAllUserBySupplierCount(Long supplierId, LtMastUsers ltMastUsers) throws ServiceException {
		String query = env.getProperty("getAllUserBySupplierCount");
		String name = null;
		if (ltMastUsers.getUserName() != null && !ltMastUsers.getUserName().equals("")) {
			name = "%" + ltMastUsers.getUserName().trim().toUpperCase() + "%";
		}
		List<LtMastUsers> count = jdbcTemplate.query(query, new Object[] { supplierId, name },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return (long) count.size();
	}

	@Override
	public List<LtMastUsers> getAllUserBySupplierDataTable(Long supplierId, LtMastUsers ltMastUsers)
			throws ServiceException {
		if (ltMastUsers.getColumnNo() == null || ltMastUsers.getColumnNo() == 0) {
			ltMastUsers.setColumnNo(1);
		}
		String name = null;
		if (ltMastUsers.getUserName() != null && !ltMastUsers.getUserName().equals("")) {
			name = "%" + ltMastUsers.getUserName().toUpperCase() + "%";
		}
		String query = env.getProperty("getAllUserBySupplierDataTable");
		return (List<LtMastUsers>) jdbcTemplate.query(query,
				new Object[] { supplierId, name, ltMastUsers.getLength(), ltMastUsers.getStart() },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
	}

	@Override
	public List<LtMastUsers> getCustomerByName(String name, Long supplierId) throws ServiceException {
		String query = env.getProperty("getCustomerByName");
		List<LtMastUsers> ltMastUsers = jdbcTemplate.query(query,
				new Object[] { "%" + name.toUpperCase() + "%", supplierId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return ltMastUsers;
	}

	@Override
	public LtMastUsers getUserStatusById(Long userId) throws ServiceException {
		String query = env.getProperty("getUserStatusById");
		/*LtMastUsers ltMastUsers = jdbcTemplate.queryForObject(query, new Object[] { userId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return ltMastUsers;*/
		List<LtMastUsers> ltMastUsersList = jdbcTemplate.query(query,
				new Object[] { userId },new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		if(!ltMastUsersList.isEmpty())
			return ltMastUsersList.get(0);
		else
			return null;
	}

}
