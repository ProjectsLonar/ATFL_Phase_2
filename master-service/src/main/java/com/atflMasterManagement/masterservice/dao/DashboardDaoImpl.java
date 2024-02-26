package com.atflMasterManagement.masterservice.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dto.CategoryRevenueDto;
import com.atflMasterManagement.masterservice.dto.CategoryRevenueResponseDto;
import com.atflMasterManagement.masterservice.dto.DailyAndMonthlyDto;
import com.atflMasterManagement.masterservice.dto.DailySalesResponseDto;
import com.atflMasterManagement.masterservice.dto.MonthlyResponseDto;
import com.atflMasterManagement.masterservice.dto.OrderAndLineCountDto;
import com.atflMasterManagement.masterservice.dto.RecordCountDto;
import com.atflMasterManagement.masterservice.dto.StatusWiseOrdersCountDto;
import com.atflMasterManagement.masterservice.dto.UserDetailsDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;

@Repository
@PropertySource(value = "classpath:queries/ltMasterQueries.properties", ignoreResourceNotFound = true)
public class DashboardDaoImpl implements DashboardDao, CodeMaster {
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

	@Override
	public List<StatusWiseOrdersCountDto> statusWiseOrdersCount(String orgId, String userId) throws ServiceException {
		String query = env.getProperty("statusWiseOrdersCountForAdmin");
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			query = env.getProperty("statusWiseOrdersCountForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by lsh.status";
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
//			List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			query = env.getProperty("statusWiseOrdersCountForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by lsh.status";
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			query = env.getProperty("statusWiseOrdersCountForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by lsh.status";
		}

		List<StatusWiseOrdersCountDto> statusWiseOrdersCountList = jdbcTemplate.query(query, new Object[] { orgId },
				new BeanPropertyRowMapper<StatusWiseOrdersCountDto>(StatusWiseOrdersCountDto.class));
		return statusWiseOrdersCountList;
	}

	//public List<Long> getUsersBySalesOfficer(String userId) throws ServiceException {
		public List<String> getUsersBySalesOfficer(String userId) throws ServiceException {
		String query = env.getProperty("getUsersBySalesOfficer");
		List<String> userIdList = jdbcTemplate.queryForList(query, String.class, userId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	private List<Long> getUsersByMonthlySalesAH(String userId) throws ServiceException {
		String query = env.getProperty("getReportUsersByAreaHead");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, userId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	@Override
	public List<CategoryRevenueDto> categoryRevenueDistribution(String orgId, String userId) throws ServiceException {
		
		String query = env.getProperty("categoryRevenueDistributionForAdmin");

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			query = env.getProperty("categoryRevenueDistributionForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "") + ") "
					+ "group by lmpc.category_name,lmpc.category_id ";
			
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			
	//		List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			query = env.getProperty("categoryRevenueDistributionForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "") + ") "
					+ "group by lmpc.category_name,lmpc.category_id ";
			
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			
			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			query = env.getProperty("categoryRevenueDistributionForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "") + ") "
					+ "group by lmpc.category_name,lmpc.category_id ";
			
		}

		List<CategoryRevenueDto> categoryRevenueList = jdbcTemplate.query(query, new Object[] { },
				new BeanPropertyRowMapper<CategoryRevenueDto>(CategoryRevenueDto.class));
		return categoryRevenueList;
	}

	@Override
	public List<DailyAndMonthlyDto> dailySales(String orgId, String userId) throws ServiceException {
		String query = env.getProperty("dailySalesForAdmin");

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			query = env.getProperty("dailySalesForDistributor");
			query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") order by lsh.header_id , lsh.last_update_date ";
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			//List<Long> userList = getUsersBySalesOfficer(userId);
			List<String> userList = getUsersBySalesOfficer(userId);
			query = env.getProperty("dailySalesForDistributor");
			query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") order by lsh.header_id , lsh.last_update_date ";
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			query = env.getProperty("dailySalesForDistributor");
			query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") order by lsh.header_id , lsh.last_update_date ";
		}

		if (userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			query = env.getProperty("dailySalesForDistributor");
			query = query + " and lsh.last_updated_by in (" + userId
					+ ") order by lsh.header_id , lsh.last_update_date ";
		}

		List<DailyAndMonthlyDto> dailySalesList = jdbcTemplate.query(query, new Object[] { orgId },
				new BeanPropertyRowMapper<DailyAndMonthlyDto>(DailyAndMonthlyDto.class));
		return dailySalesList;
	}

	@Override
	public List<DailySalesResponseDto> dailySalesV2(String orgId, String userId) throws ServiceException {
		
		String query = env.getProperty("dailySalesForAdminV2");
		
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {

			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			if (userList == null) {
				return null;
			}
			query = env.getProperty("dailySalesForDistributorV2");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by to_char(lsh.last_update_date at time zone 'IST', 'DD-MM-YYYY') ";

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			//List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}
			query = env.getProperty("dailySalesForDistributorV2");
			
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ")group by to_char(lsh.last_update_date, 'YYYY-MM-DD') ";
					//+ "group by to_char(lsh.last_update_date at time zone 'IST', 'DD-MM-YYYY') ";
			
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			
			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}
			
			query = env.getProperty("dailySalesForDistributorV2");
			
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by to_char(lsh.last_update_date at time zone 'IST', 'DD-MM-YYYY') ";
			
		}else if (userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			query = env.getProperty("dailySalesForDistributorV2");
			query = query + " and lsh.last_updated_by in (" + userId
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by to_char(lsh.last_update_date at time zone 'IST', 'DD-MM-YYYY')";
		}

		List<DailySalesResponseDto> dailySalesList = jdbcTemplate.query(query, new Object[] { },
				new BeanPropertyRowMapper<DailySalesResponseDto>(DailySalesResponseDto.class));
		return dailySalesList;
	}

	@Override
	public List<MonthlyResponseDto> monthlySalesV2(String orgId, String userId) throws ServiceException {

		String query = env.getProperty("monthlySalesForAdminV2");
		
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);

		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {

			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			if (userList == null) {
				return null;
			}
			query = env.getProperty("monthlySalesForDistributorV2");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
//					+ "group by  to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY') ";

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

//			List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}
			query = env.getProperty("monthlySalesForDistributorV2");
			
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by  to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY') ";

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}

			query = env.getProperty("monthlySalesForDistributorV2");
			
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by  to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY') ";

		}

		List<MonthlyResponseDto> monthlySalesList = jdbcTemplate.query(query, new Object[] { },
				new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
		return monthlySalesList;
	}

	@Override
	public List<DailyAndMonthlyDto> monthlySales(String orgId, String userId) throws ServiceException {
		String query = env.getProperty("monthlySalesForAdmin");

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			query = env.getProperty("monthlySalesForDistributor");
			query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") order by lsh.header_id , lsh.last_update_date ";
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			//List<Long> userList = getUsersBySalesOfficer(userId);
			List<String> userList = getUsersBySalesOfficer(userId);
			query = env.getProperty("monthlySalesForDistributor");
			query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") order by lsh.header_id , lsh.last_update_date ";
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			query = env.getProperty("monthlySalesForDistributor");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") order by lsh.header_id , lsh.last_update_date ";
		}

		List<DailyAndMonthlyDto> monthlySalesList = jdbcTemplate.query(query, new Object[] { orgId },
				new BeanPropertyRowMapper<DailyAndMonthlyDto>(DailyAndMonthlyDto.class));
		return monthlySalesList;
	}

	private UserDetailsDto getUserTypeAndDisId(String userId) throws ServiceException {
		String query = env.getProperty("getUserTypeAndDisId");
		List<UserDetailsDto> list = jdbcTemplate.query(query, new Object[] { userId },
				new BeanPropertyRowMapper<UserDetailsDto>(UserDetailsDto.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}

	private List<Long> getUsersByDistributorId(String distributorId) throws ServiceException {
		String query = env.getProperty("getUsersByDistributorId");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	@Override
	//public Long getDistributorIdByUserId(String userId) throws ServiceException {
	public String getDistributorIdByUserId(String userId) throws ServiceException {
		String query = env.getProperty("getDistributorIdByUserId");
//		List<Long> disIdList = jdbcTemplate.queryForList(query, Long.class, userId);
		List<String> disIdList = jdbcTemplate.queryForList(query, String.class, userId);
		if (!disIdList.isEmpty())
			return disIdList.get(0);
			//return disIdList;
		else
			//return 0L;
			return null;
	}

	@Override
	public OrderAndLineCountDto getOrderAndLineCountData(String distId, String orderDate, String userId)
			throws ServiceException {
		OrderAndLineCountDto countDto = new OrderAndLineCountDto();

		SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		String reformattedStr = null;
		try {
			reformattedStr = myFormat.format(fromUser.parse(orderDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String orderCount = "0";
		String orderLineCount = "0";

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			String getOrderCountQuery = env.getProperty("getOrderCountDashboard");

			List<RecordCountDto> getOrderCountList = jdbcTemplate.query(getOrderCountQuery,
					new Object[] { reformattedStr, distId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountList.isEmpty()) {
				orderCount = getOrderCountList.get(0).getCount().toString();
			}

			String getOrderLineCountQuery = env.getProperty("getOrderLineCountDashboard");
			List<RecordCountDto> getOrderLineCountList = jdbcTemplate.query(getOrderLineCountQuery,
					new Object[] { reformattedStr, distId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderLineCountList.isEmpty()) {
				orderLineCount = getOrderLineCountList.get(0).getCount().toString();

				countDto.setTotalOrderCount(orderCount);
				countDto.setTotalLineItemCount(orderLineCount);
			}
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			String getOrderCountQueryForSales = env.getProperty("getOrderCountSales");

			List<RecordCountDto> getOrderCountListForSales = jdbcTemplate.query(getOrderCountQueryForSales,
					new Object[] { reformattedStr, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListForSales.isEmpty()) {
				orderCount = getOrderCountListForSales.get(0).getCount().toString();
			}

			String getOrderLineCountQueryForSales = env.getProperty("getOrderLineCountDashboardSales");
			List<RecordCountDto> getOrderLineCountListForSales = jdbcTemplate.query(getOrderLineCountQueryForSales,
					new Object[] { reformattedStr, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderLineCountListForSales.isEmpty()) {
				orderLineCount = getOrderLineCountListForSales.get(0).getCount().toString();

				countDto.setTotalOrderCount(orderCount);
				countDto.setTotalLineItemCount(orderLineCount);
			}
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			String getOrderCountQueryForAdmin = env.getProperty("getOrderCountDashboardSO");

			List<RecordCountDto> getOrderCountListAdmin = jdbcTemplate.query(getOrderCountQueryForAdmin,
					new Object[] { reformattedStr, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				orderCount = getOrderCountListAdmin.get(0).getCount().toString();
			}

			String getOrderLineCountQueryAdmin = env.getProperty("getOrderLineCountDashboardSO");
			List<RecordCountDto> getOrderLineCountListAdmin = jdbcTemplate.query(getOrderLineCountQueryAdmin,
					new Object[] { reformattedStr, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderLineCountListAdmin.isEmpty()) {
				orderLineCount = getOrderLineCountListAdmin.get(0).getCount().toString();

				countDto.setTotalOrderCount(orderCount);
				countDto.setTotalLineItemCount(orderLineCount);
			}

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			String getOrderCountQueryForAdmin = env.getProperty("getOrderCountDashboardAH");

			List<RecordCountDto> getOrderCountListAdmin = jdbcTemplate.query(getOrderCountQueryForAdmin,
					new Object[] { reformattedStr, userDetailsDto.getPositionId() },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				orderCount = getOrderCountListAdmin.get(0).getCount().toString();
			}

			String getOrderLineCountQueryAdmin = env.getProperty("getOrderLineCountDashboardAH");
			List<RecordCountDto> getOrderLineCountListAdmin = jdbcTemplate.query(getOrderLineCountQueryAdmin,
					new Object[] { reformattedStr, userDetailsDto.getPositionId() },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderLineCountListAdmin.isEmpty()) {
				orderLineCount = getOrderLineCountListAdmin.get(0).getCount().toString();

				countDto.setTotalOrderCount(orderCount);
				countDto.setTotalLineItemCount(orderLineCount);
			}

		} else {
			String getOrderCountQueryForAdmin = env.getProperty("getOrderCountDashboardAdmin");

			List<RecordCountDto> getOrderCountListAdmin = jdbcTemplate.query(getOrderCountQueryForAdmin,
					new Object[] { reformattedStr }, new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				orderCount = getOrderCountListAdmin.get(0).getCount().toString();
			}

			String getOrderLineCountQueryAdmin = env.getProperty("getOrderLineCountDashboardAdmin");
			List<RecordCountDto> getOrderLineCountListAdmin = jdbcTemplate.query(getOrderLineCountQueryAdmin,
					new Object[] { reformattedStr }, new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderLineCountListAdmin.isEmpty()) {
				orderLineCount = getOrderLineCountListAdmin.get(0).getCount().toString();

				countDto.setTotalOrderCount(orderCount);
				countDto.setTotalLineItemCount(orderLineCount);
			}
		}
		return countDto;

	}

	@Override
	public Map<String, DailySalesResponseDto> getOrderAndLineCountDataV2(String distId, String userId,
			Map<String, DailySalesResponseDto> dailySalesResponseMap) throws ServiceException {

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		
		if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			String getOrderCountQuery = env.getProperty("getOrderCountDashboardV2");

			List<DailySalesResponseDto> getOrderCountList = jdbcTemplate.query(getOrderCountQuery, new Object[] { distId },
					new BeanPropertyRowMapper<DailySalesResponseDto>(DailySalesResponseDto.class));
			if (!getOrderCountList.isEmpty()) {
				for (DailySalesResponseDto dailySalesResponseDto : getOrderCountList) {
					if (dailySalesResponseMap.containsKey(dailySalesResponseDto.getDate())) {
						DailySalesResponseDto obj = dailySalesResponseMap.get(dailySalesResponseDto.getDate());
						obj.setDbc(dailySalesResponseDto.getDbc());
						dailySalesResponseMap.put(dailySalesResponseDto.getDate(), obj);
					}
				}
			}
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			String getOrderCountQueryForSales = env.getProperty("getOrderCountSalesV2");

			List<DailySalesResponseDto> getOrderCountListForSales = jdbcTemplate.query(getOrderCountQueryForSales,
					new Object[] { userId }, new BeanPropertyRowMapper<DailySalesResponseDto>(DailySalesResponseDto.class));
			if (!getOrderCountListForSales.isEmpty()) {
				for (DailySalesResponseDto dailySalesResponseDto : getOrderCountListForSales) {
					if (dailySalesResponseMap.containsKey(dailySalesResponseDto.getDate())) {
						DailySalesResponseDto obj = dailySalesResponseMap.get(dailySalesResponseDto.getDate());
						obj.setDbc(dailySalesResponseDto.getDbc());
						dailySalesResponseMap.put(dailySalesResponseDto.getDate(), obj);
					}
				}
			}

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			
			String query = env.getProperty("getOrderCountDashboardSOV2");
			
			//List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			
			if (userList == null) {
				return null;
			}
			query = query + "and lmu.user_id in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by  to_char(lsh.last_update_date at time zone 'IST', 'DD-MM-YYYY') ";

			List<DailySalesResponseDto> getOrderCountListAdmin = jdbcTemplate.query(query, new Object[] { }, new BeanPropertyRowMapper<DailySalesResponseDto>(DailySalesResponseDto.class));
			
			if (!getOrderCountListAdmin.isEmpty()) {
				for (DailySalesResponseDto dailySalesResponseDto : getOrderCountListAdmin) {
					if (dailySalesResponseMap.containsKey(dailySalesResponseDto.getDate())) {
						DailySalesResponseDto obj = dailySalesResponseMap.get(dailySalesResponseDto.getDate());
						obj.setDbc(dailySalesResponseDto.getDbc());
						dailySalesResponseMap.put(dailySalesResponseDto.getDate(), obj);
					}
				}
			}

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			
			String query = env.getProperty("getOrderCountDashboardAHV2");

			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}
			
			query = query + "and lmu.user_id in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')";
					//+ "group by to_char(lsh.last_update_date at time zone 'IST', 'DD-MM-YYYY')";
			
			List<DailySalesResponseDto> getOrderCountListAdmin = jdbcTemplate.query(query,
					new Object[] { },
					new BeanPropertyRowMapper<DailySalesResponseDto>(DailySalesResponseDto.class));
			
			if (!getOrderCountListAdmin.isEmpty()) {
				for (DailySalesResponseDto dailySalesResponseDto : getOrderCountListAdmin) {
					if (dailySalesResponseMap.containsKey(dailySalesResponseDto.getDate())) {
						DailySalesResponseDto obj = dailySalesResponseMap.get(dailySalesResponseDto.getDate());
						obj.setDbc(dailySalesResponseDto.getDbc());
						dailySalesResponseMap.put(dailySalesResponseDto.getDate(), obj);
					}
				}
			}

		} else {
			String getOrderCountQueryForAdmin = env.getProperty("getOrderCountDashboardAdminV2");

			List<DailySalesResponseDto> getOrderCountListAdmin = jdbcTemplate.query(getOrderCountQueryForAdmin,
					new Object[] {}, new BeanPropertyRowMapper<DailySalesResponseDto>(DailySalesResponseDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				for (DailySalesResponseDto dailySalesResponseDto : getOrderCountListAdmin) {
					if (dailySalesResponseMap.containsKey(dailySalesResponseDto.getDate())) {
						DailySalesResponseDto obj = dailySalesResponseMap.get(dailySalesResponseDto.getDate());
						obj.setDbc(dailySalesResponseDto.getDbc());
						System.out.println(dailySalesResponseDto.getDate()+" "+dailySalesResponseDto.getDbc());
						dailySalesResponseMap.put(dailySalesResponseDto.getDate(), obj);
					}
				}
			}
		}
		return dailySalesResponseMap;
	}

	@Override
	public OrderAndLineCountDto getOrderCountData(String distId, String catId, String userId) throws ServiceException {
		OrderAndLineCountDto countDto = new OrderAndLineCountDto();
		Integer orderCount = 0;
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
			String queryAdmin = env.getProperty("getOrderCountDashboardCategoryAdmin");
			List<RecordCountDto> getOrderCountListAdmin = jdbcTemplate.query(queryAdmin, new Object[] { catId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				orderCount = getOrderCountListAdmin.size();
			}
			countDto.setTotalOrderCount(orderCount.toString());
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

			String queryAdmin = env.getProperty("getOrderCountDashboardCategorySO");
			List<RecordCountDto> getOrderCountListAdmin = jdbcTemplate.query(queryAdmin, new Object[] { catId, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				orderCount = getOrderCountListAdmin.size();
			}
			countDto.setTotalOrderCount(orderCount.toString());
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

			String queryAdmin = env.getProperty("getOrderCountDashboardCategoryAH");
			List<RecordCountDto> getOrderCountListAdmin = jdbcTemplate.query(queryAdmin,
					new Object[] { catId, userDetailsDto.getPositionId() },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountListAdmin.isEmpty()) {
				orderCount = getOrderCountListAdmin.size();
			}
			countDto.setTotalOrderCount(orderCount.toString());
		} else {
			String query = env.getProperty("getOrderCountDashboardCategory");
			List<RecordCountDto> getOrderCountList = jdbcTemplate.query(query, new Object[] { catId, distId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!getOrderCountList.isEmpty()) {
				orderCount = getOrderCountList.size();
			}
			countDto.setTotalOrderCount(orderCount.toString());
		}

		return countDto;
	}
	
	@Override
	public Map<Long,CategoryRevenueResponseDto> getOrderCountDataV2(Map<Long,CategoryRevenueResponseDto> categoryRevenueResponseDtoMap, String userId) throws ServiceException {
		
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
	
		if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
			
			String queryAdmin = env.getProperty("getOrderCountDashboardCategoryAdmin");
			
			List<CategoryRevenueDto> getOrderCountListAdmin = jdbcTemplate.query(queryAdmin, new Object[] {},
					new BeanPropertyRowMapper<CategoryRevenueDto>(CategoryRevenueDto.class));
			
			
			if (!getOrderCountListAdmin.isEmpty()) {
				for(CategoryRevenueDto categoryRevenueDto:getOrderCountListAdmin) {
					categoryRevenueResponseDtoMap.get(categoryRevenueDto.getCategoryId()).setDbc(categoryRevenueDto.getDbc());
				}
			}
			
			
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
			
		//	List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}
			
			String query = env.getProperty("getOrderCountDashboardCategory");
			
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by lmpc.category_id,lsh.header_id) abc "
					+ "group by abc.category_name,abc.category_id ";
			
			List<CategoryRevenueDto> getOrderCountListAdmin = jdbcTemplate.query(query, new Object[] { },
					new BeanPropertyRowMapper<CategoryRevenueDto>(CategoryRevenueDto.class));
			
			if (!getOrderCountListAdmin.isEmpty()) {
				for(CategoryRevenueDto categoryRevenueDto:getOrderCountListAdmin) {
					categoryRevenueResponseDtoMap.get(categoryRevenueDto.getCategoryId()).setDbc(categoryRevenueDto.getDbc());
				}
			}
			
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
			
			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			if (userList == null) {
				return null;
			}

			String query = env.getProperty("getOrderCountDashboardCategory");
			
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by lmpc.category_id,lsh.header_id) abc "
					+ "group by abc.category_name,abc.category_id ";
			
			List<CategoryRevenueDto> getOrderCountListAdmin = jdbcTemplate.query(query,
					new Object[] { },
					new BeanPropertyRowMapper<CategoryRevenueDto>(CategoryRevenueDto.class));
			
			if (!getOrderCountListAdmin.isEmpty()) {
				for(CategoryRevenueDto categoryRevenueDto:getOrderCountListAdmin) {
					categoryRevenueResponseDtoMap.get(categoryRevenueDto.getCategoryId()).setDbc(categoryRevenueDto.getDbc());
				}
			}
			
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)){
			
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			if (userList == null) {
				return null;
			}
			
			String query = env.getProperty("getOrderCountDashboardCategory");
			query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by lmpc.category_id,lsh.header_id) abc "
					+ "group by abc.category_name,abc.category_id ";
			List<CategoryRevenueDto> getOrderCountList = jdbcTemplate.query(query, new Object[] { },
					new BeanPropertyRowMapper<CategoryRevenueDto>(CategoryRevenueDto.class));
			
			if (!getOrderCountList.isEmpty()) {
				for(CategoryRevenueDto categoryRevenueDto:getOrderCountList) {
					categoryRevenueResponseDtoMap.get(categoryRevenueDto.getCategoryId()).setDbc(categoryRevenueDto.getDbc());
				}
			}
		}

		return categoryRevenueResponseDtoMap;
	}

	@Override
	public OrderAndLineCountDto getCountDataForMonthlyDashboard(String distId, String monthString, String userId)
			throws ServiceException {
		OrderAndLineCountDto countDto = new OrderAndLineCountDto();

		YearMonth yeardate = YearMonth.now();

		String startDate = yeardate.getYear() + "-" + monthString + "-01";
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
		LocalDate date = LocalDate.parse(startDate, dateFormat);
		LocalDate endDate = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));

		String salesPersonsCount = "0";
		String orderCount = "0";
		String orderLineCount = "0";

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
		if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDashAdmin");
			List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] { endDate.toString(), startDate },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!salesPersonCountList.isEmpty()) {
				salesPersonsCount = String.valueOf(salesPersonCountList.size());
			}

			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardAdmin");
			List<RecordCountDto> orderCountList = jdbcTemplate.query(orderCountQuery,
					new Object[] { endDate.toString(), startDate },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderCountList.isEmpty()) {
				orderCount = orderCountList.get(0).getCount().toString();
			}

			String lineCountQuery = env.getProperty("getOrderLineCountMonthlyDashboardAdmin");
			List<RecordCountDto> orderLineCountList = jdbcTemplate.query(lineCountQuery,
					new Object[] { endDate.toString(), startDate },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderLineCountList.isEmpty()) {
				orderLineCount = orderLineCountList.get(0).getCount().toString();
			}
		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDashSO");

			List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] { endDate.toString(), startDate, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!salesPersonCountList.isEmpty()) {
				salesPersonsCount = String.valueOf(salesPersonCountList.size());
			}

			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardSO");
			List<RecordCountDto> orderCountList = jdbcTemplate.query(orderCountQuery,
					new Object[] { endDate.toString(), startDate, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderCountList.isEmpty()) {
				orderCount = orderCountList.get(0).getCount().toString();
			}

			String lineCountQuery = env.getProperty("getOrderLineCountMonthlyDashboardSO");
			List<RecordCountDto> orderLineCountList = jdbcTemplate.query(lineCountQuery,
					new Object[] { endDate.toString(), startDate, userId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderLineCountList.isEmpty()) {
				orderLineCount = orderLineCountList.get(0).getCount().toString();
			}

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDashAH");

			List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] { endDate.toString(), startDate, userDetailsDto.getPositionId() },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!salesPersonCountList.isEmpty()) {
				salesPersonsCount = salesPersonCountList.get(0).getCount().toString();
			}

			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardAH");
			List<RecordCountDto> orderCountList = jdbcTemplate.query(orderCountQuery,
					new Object[] { endDate.toString(), startDate, userDetailsDto.getPositionId() },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderCountList.isEmpty()) {
				orderCount = orderCountList.get(0).getCount().toString();
			}

			String lineCountQuery = env.getProperty("getOrderLineCountMonthlyDashboardAH");
			List<RecordCountDto> orderLineCountList = jdbcTemplate.query(lineCountQuery,
					new Object[] { endDate.toString(), startDate, userDetailsDto.getPositionId() },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderLineCountList.isEmpty()) {
				orderLineCount = orderLineCountList.get(0).getCount().toString();
			}

		} else {
			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDash");
			List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] { endDate.toString(), startDate, distId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!salesPersonCountList.isEmpty()) {
				// salesPersonsCount = salesPersonCountList.get(0).getCount().toString();
				// added group by condition in query
				salesPersonsCount = String.valueOf(salesPersonCountList.size());
			}

			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboard");
			List<RecordCountDto> orderCountList = jdbcTemplate.query(orderCountQuery,
					new Object[] { endDate.toString(), startDate, distId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderCountList.isEmpty()) {
				orderCount = orderCountList.get(0).getCount().toString();
			}

			String lineCountQuery = env.getProperty("getOrderLineCountMonthlyDashboard");
			List<RecordCountDto> orderLineCountList = jdbcTemplate.query(lineCountQuery,
					new Object[] { endDate.toString(), startDate, distId },
					new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
			if (!orderLineCountList.isEmpty()) {
				orderLineCount = orderLineCountList.get(0).getCount().toString();
			}
		}
		countDto.setSalesPersonsCount(salesPersonsCount);
		countDto.setTotalOrderCount(orderCount);
		countDto.setTotalLineItemCount(orderLineCount);

		return countDto;
	}

	@Override
	public Map<String, MonthlyResponseDto> getCountDataForMonthlyDashboardV2(String distId, String userId,
			Map<String, MonthlyResponseDto> monthlyResponseDtoMap) throws ServiceException {

		UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);

		if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {

			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDashAdminV2");
			List<MonthlyResponseDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] {},
					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
			if (!salesPersonCountList.isEmpty()) {
				for (MonthlyResponseDto monthlyResponseDto : salesPersonCountList) {
					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
						obj.setTotalEff(monthlyResponseDto.getTotalEff());
						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
					}
				}
			}

//			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardAdminV2");
//			List<MonthlyResponseDto> orderCountList = jdbcTemplate.query(orderCountQuery, new Object[] {},
//					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
//
//			if (!orderCountList.isEmpty()) {
//				for (MonthlyResponseDto monthlyResponseDto : orderCountList) {
//					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
//						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
//						obj.setDbc(monthlyResponseDto.getDbc());
//						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
//					}
//				}
//			}

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

			String query = env.getProperty("getSalesPersonsMonthlyCountDashSOV2");
			
		//	List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			List<String> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
			
			if (userList == null) {
				return null;
			}
			query = query + "and lmu.user_id in (" + userList.toString().replace("[", "").replace("]", "")
					+ ") group by to_char(lsh.last_update_date, 'YYYY-MM-DD')  ";
//					+ "group by to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY') ";

			List<MonthlyResponseDto> salesPersonCountList = jdbcTemplate.query(query,
					new Object[] {},
					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
			if (!salesPersonCountList.isEmpty()) {
				for (MonthlyResponseDto monthlyResponseDto : salesPersonCountList) {
					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
						obj.setTotalEff(monthlyResponseDto.getTotalEff());
						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
					}
				}
			}

//			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardSOV2");
//			
//			orderCountQuery = orderCountQuery + "and lmu.user_id in (" + userList.toString().replace("[", "").replace("]", "")
//					+ ") group by to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY') ";
//			
//			List<MonthlyResponseDto> orderCountList = jdbcTemplate.query(orderCountQuery, new Object[] { },
//					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
//			if (!orderCountList.isEmpty()) {
//				for (MonthlyResponseDto monthlyResponseDto : orderCountList) {
//					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
//						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
//						obj.setDbc(monthlyResponseDto.getDbc());
//						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
//					}
//				}
//			}

		} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDashAHV2");

			List<Long> userList = getUsersByMonthlySalesAH(userDetailsDto.getPositionId());
			
			if (userList == null) {
				return null;
			}
			
			salesPersonsCountQuery = salesPersonsCountQuery + "and lmu.user_id in (" + userList.toString().replace("[", "").replace("]", "")
					+ ")group by to_char(lsh.last_update_date, 'YYYY-MM-DD') ";
//					+ "group by to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY')";

			List<MonthlyResponseDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] {},
					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
			
			
			if (!salesPersonCountList.isEmpty()) {
				for (MonthlyResponseDto monthlyResponseDto : salesPersonCountList) {
					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
						obj.setTotalEff(monthlyResponseDto.getTotalEff());
						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
					}
				}
			}

//			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardAHV2");
//			
//			orderCountQuery = orderCountQuery + "AND lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
//					+ ") group by to_char(lsh.last_update_date at time zone 'IST', 'Mon YYYY')";
//			
//			List<MonthlyResponseDto> orderCountList = jdbcTemplate.query(orderCountQuery,
//					new Object[] { },
//					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
//			if (!orderCountList.isEmpty()) {
//				for (MonthlyResponseDto monthlyResponseDto : orderCountList) {
//					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
//						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
//						obj.setDbc(monthlyResponseDto.getDbc());
//						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
//					}
//				}
//			}

		} else {
			String salesPersonsCountQuery = env.getProperty("getSalesPersonsMonthlyCountDashV2");
			List<MonthlyResponseDto> salesPersonCountList = jdbcTemplate.query(salesPersonsCountQuery,
					new Object[] {distId},
					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
			if (!salesPersonCountList.isEmpty()) {
				for (MonthlyResponseDto monthlyResponseDto : salesPersonCountList) {
					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
						obj.setTotalEff(monthlyResponseDto.getTotalEff());
						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
					}
				}
			}

//			String orderCountQuery = env.getProperty("getOrderCountMonthlyDashboardV2");
//			List<MonthlyResponseDto> orderCountList = jdbcTemplate.query(orderCountQuery, new Object[] { distId },
//					new BeanPropertyRowMapper<MonthlyResponseDto>(MonthlyResponseDto.class));
//			if (!orderCountList.isEmpty()) {
//				for (MonthlyResponseDto monthlyResponseDto : orderCountList) {
//					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
//						MonthlyResponseDto obj = monthlyResponseDtoMap.get(monthlyResponseDto.getMonth());
//						obj.setDbc(monthlyResponseDto.getDbc());
//						monthlyResponseDtoMap.put(monthlyResponseDto.getMonth(), obj);
//					}
//				}
//			}
		}
		return monthlyResponseDtoMap;
	}
}
