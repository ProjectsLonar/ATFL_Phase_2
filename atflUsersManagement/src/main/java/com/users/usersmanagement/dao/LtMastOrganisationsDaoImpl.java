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
import com.users.usersmanagement.model.LtMastOrganisations;
import com.users.usersmanagement.repository.LtMastOrganisationsRepository;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastOrganisationsDaoImpl implements LtMastOrganisationsDao {

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
	LtMastOrganisationsRepository ltMastOrganisationsRepository;

	@Override
	public LtMastOrganisations verifyOrganisation(String userCode, String orgCode) throws ServiceException {

		String query = env.getProperty("verifyOrgUser");

		List<LtMastOrganisations> ltMastOrganisationslist = jdbcTemplate.query(query,
				new Object[] { userCode, orgCode },
				new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class));

		if (!ltMastOrganisationslist.isEmpty()) {

			return ltMastOrganisationslist.get(0);

		}
		return null;
	}

	@Override
	public LtMastOrganisations verifyOrganisationV2(String userCode, String orgCode) throws ServiceException {

		String query = env.getProperty("verifyOrgUserV1.1");

		List<LtMastOrganisations> ltMastOrganisationslist = jdbcTemplate.query(query,
				new Object[] { userCode, orgCode },
				new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class));

		if (!ltMastOrganisationslist.isEmpty()) {

			return ltMastOrganisationslist.get(0);

		}
		return null;
	}

}
