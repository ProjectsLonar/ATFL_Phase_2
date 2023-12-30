package com.atflMasterManagement.masterservice.dao;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atflMasterManagement.masterservice.common.DateTimeClass;
import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dto.RecordCountDto;
import com.atflMasterManagement.masterservice.dto.UserDetailsDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.reports.DistributorDto;
import com.atflMasterManagement.masterservice.reports.ExcelDataDistributor;
import com.atflMasterManagement.masterservice.reports.ExcelDataOutlet;
import com.atflMasterManagement.masterservice.reports.ExcelDataProduct;
import com.atflMasterManagement.masterservice.reports.ExcelDataRegion;
import com.atflMasterManagement.masterservice.reports.ExcelDataSelesperson;
import com.atflMasterManagement.masterservice.reports.ExcelReportDto;
import com.atflMasterManagement.masterservice.reports.ResponseExcelDto;
import com.atflMasterManagement.masterservice.reports.SalesOrderLineCountDto;

@Repository
public class LtReportDaoImpl implements LtReportDao, CodeMaster {

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

	SimpleDateFormat reportStartEndDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 2020-07-31

	@Override
	public List<ExcelDataSelesperson> getSalesReportData(ExcelReportDto excelReportDto) throws ServiceException {
		try {

			DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			String query = env.getProperty("getReportDataBySalesPersonAdmin");

			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());

			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataBySalesPersonDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.order_number desc";
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataBySalesPersonDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.order_number desc";
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());

				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataBySalesPersonDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.order_number desc";

			}
			List<ExcelDataSelesperson> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getStatus(), excelReportDto.getOrgId(),
							excelReportDto.getDistributorId(), excelReportDto.getEmployeeId(), startDate, endDate,
							strStartDate, strEndDate },
					new BeanPropertyRowMapper<ExcelDataSelesperson>(ExcelDataSelesperson.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Long checkOrderedIsProcessed(List<Long> userList) throws ServiceException, IOException {
		String query = env.getProperty("checkOrderedIsProcessed");
		query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "") + ") ";
		Long count = getJdbcTemplate().queryForObject(query, new Object[] {}, Long.class);
		return count;
	}

	@Override
	public List<ExcelDataRegion> getRegionwiseSalesReportData(ExcelReportDto excelReportDto) {
		try {
			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			String query = env.getProperty("getReportDataByRegionwiseSalesAdmin");

			if (excelReportDto.getUserId() != null) {

				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());

				if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

					List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
					
					if (userList == null) {
						return null;
					}

					Long processedCount = checkOrderedIsProcessed(userList);

					query = env.getProperty("getReportDataByRegionwiseSales");

					if (processedCount != 0) {
						query = query + " and lsh.last_updated_by in ("
								+ userList.toString().replace("[", "").replace("]", "")
								+ ")group by lmo.region,lmd.distributor_id, lmd.distributor_name, lmd.distributor_code , lmd.distributor_crm_code";
					}

				} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

					List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());

					if (userList == null) {
						return null;
					}
					Long processedCount = checkOrderedIsProcessed(userList);

					query = env.getProperty("getReportDataByRegionwiseSales");

					if (processedCount != 0) {
						query = query + " and lsh.last_updated_by in ("
								+ userList.toString().replace("[", "").replace("]", "")
								+ ")group by lmo.region,lmd.distributor_id, lmd.distributor_name, lmd.distributor_code , lmd.distributor_crm_code";
					}
				}

			}
			List<ExcelDataRegion> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getRegion(), excelReportDto.getDistributorId(), startDate, endDate,
							strStartDate, strEndDate },
					new BeanPropertyRowMapper<ExcelDataRegion>(ExcelDataRegion.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ExcelDataProduct> getProductReportData(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			String query = env.getProperty("getReportDataByProductAdmin");
			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByProductDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") ";
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByProductDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") ";
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByProductDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") ";
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getProductId(), startDate, endDate, strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ExcelDataDistributor> getDistributorReportData(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			if (excelReportDto.getUserId() != null) {
				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());

				if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
					List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
					if (userList == null) {
						return null;
					}

					String query = env.getProperty("getReportDataByDistributor");

					query = query + " and lsh.last_updated_by in ("
							+ userList.toString().replace("[", "").replace("]", "") + ") "
							+ " group by lmd.distributor_crm_code,lmpc.category_code,lmd.distributor_name,lmpc.category_name order by lmd.distributor_crm_code asc";

					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));

					if (dataList != null) {
						return dataList;
					}
				} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
					List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
					if (userList == null) {
						return null;
					}
					String query = env.getProperty("getReportDataByDistributor");
					query = query + " and lsh.last_updated_by in ("
							+ userList.toString().replace("[", "").replace("]", "") + ") "
							+ " group by lmd.distributor_crm_code,lmpc.category_code,lmd.distributor_name,lmpc.category_name order by lmd.distributor_crm_code asc";

					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));

					if (dataList != null) {
						return dataList;
					}
				} else if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
					String query = env.getProperty("getReportDataByDistributorAdmin");
					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));

					if (dataList != null) {
						return dataList;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ExcelDataOutlet> getOutletReportData(ExcelReportDto excelReportDto) throws ServiceException {
		try {

			String status = null;
			if (excelReportDto.getStatus() != null) {
				status = excelReportDto.getStatus().trim().toUpperCase();
			}

			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			String query = env.getProperty("getReportDataByOutletAdmin");

			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByOutletDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.last_update_date desc";
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByOutletDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.last_update_date desc";
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByOutletDistributor");
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.last_update_date desc";
			}

			List<ExcelDataOutlet> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getOrgId(), excelReportDto.getOutletId(),
							excelReportDto.getDistributorId(), status, startDate, endDate, strStartDate, strEndDate },
					new BeanPropertyRowMapper<ExcelDataOutlet>(ExcelDataOutlet.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DistributorDto> searchDistributor(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			String query = env.getProperty("getSearchDistributorForRegion");

			if (excelReportDto.getUserId() != null) {

				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());

				if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

					List<Long> userList = getDistributorIdByAH(userDetailsDto.getPositionId());
					query = env.getProperty("getSearchDistributorForRegionOther");
					query = query + " and lmd.distributor_id in ("
							+ userList.toString().replace("[", "").replace("]", "")
							+ ")order by distributor_id asc ) a limit ? offset ?";

				} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

					List<Long> userList = getDistributorIdBySO(userDetailsDto.getPositionId());
					query = env.getProperty("getSearchDistributorForRegionOther");
					query = query + " and lmd.distributor_id in ("
							+ userList.toString().replace("[", "").replace("]", "")
							+ ")order by distributor_id asc ) a limit ? offset ?";

				}
			}

			String searchFieldStr = null;
			if (excelReportDto.getSearchField() != null) {
				searchFieldStr = "%" + excelReportDto.getSearchField().toUpperCase().trim() + "%";
			}

			String regionStr = null;
			if (excelReportDto.getRegion() != null) {
				regionStr = excelReportDto.getRegion();
			}

			if (excelReportDto.getLimit() == 0) {
				excelReportDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}

			List<DistributorDto> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getOrgId().toString(), regionStr, searchFieldStr,
							excelReportDto.getLimit(), excelReportDto.getOffset() },
					new BeanPropertyRowMapper<DistributorDto>(DistributorDto.class));

			if (dataList != null) {
				return dataList;
			}

		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ResponseExcelDto> searchSalesPerson(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			String query = env.getProperty("getSearchSalesPerson");

			String searchFieldStr = null;
			if (excelReportDto.getSearchField() != null) {
				searchFieldStr = "%" + excelReportDto.getSearchField().toUpperCase().trim() + "%";
			}

			if (excelReportDto.getLimit() == 0) {
				excelReportDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}

			List<ResponseExcelDto> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getOrgId(), excelReportDto.getDistributorId(), searchFieldStr,
							excelReportDto.getLimit(), excelReportDto.getOffset() },
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ResponseExcelDto> getRegion(String orgId) throws ServiceException {
		try {
			String query = env.getProperty("getRegion");
			List<ResponseExcelDto> dataList = jdbcTemplate.query(query, new Object[] { orgId },
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ResponseExcelDto> searchProduct(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			String query = env.getProperty("getSearchProduct");

			String searchFieldStr = null;
			if (excelReportDto.getSearchField() != null) {
				searchFieldStr = "%" + excelReportDto.getSearchField().toUpperCase().trim() + "%";
			}

			if (excelReportDto.getLimit() == 0) {
				excelReportDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}

			List<ResponseExcelDto> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getOrgId(), searchFieldStr, excelReportDto.getLimit(),
							excelReportDto.getOffset() },
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ResponseExcelDto> searchOutlets(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			String query = env.getProperty("getSearchOutlets");

			String searchFieldStr = null;
			if (excelReportDto.getSearchField() != null) {
				searchFieldStr = "%" + excelReportDto.getSearchField().toUpperCase().trim() + "%";
			}

			List<ResponseExcelDto> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getOrgId(), excelReportDto.getDistributorId(), searchFieldStr },
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ResponseExcelDto> getOutletStatus(String orgId) throws ServiceException {
		try {
			String query = env.getProperty("getOutletStatus");
			List<ResponseExcelDto> dataList = jdbcTemplate.query(query, new Object[] {},
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ResponseExcelDto> getCategoryDetails(String orgId) throws ServiceException {
		try {
			String query = env.getProperty("getCategoryDetails");
			List<ResponseExcelDto> dataList = jdbcTemplate.query(query, new Object[] { orgId },
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (dataList != null) {
				return dataList;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
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

	private List<Long> getUsersByDistributorId(String distributorId) throws ServiceException {
		String query = env.getProperty("getUsersByDistributorId");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	public List<Long> getUsersBySalesOfficer(String userId) throws ServiceException {
		String query = env.getProperty("getUsersBySalesOfficer");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, userId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

//	private List<Long> getUsersByAreaHead(Long userId) throws ServiceException {
//		String query = env.getProperty("getUsersByAreaHead");
//		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, userId);
//		if (!userIdList.isEmpty())
//			return userIdList;
//		else
//			return null;
//	}

	private List<Long> getReportUsersByAreaHead(String userId) throws ServiceException {
		String query = env.getProperty("getReportUsersByAreaHead");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, userId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	@Override // Same Used for dashboard also
	public SalesOrderLineCountDto getSalesOrderLineCount(String distId, ExcelReportDto excelReportDto)
			throws ServiceException {

		SalesOrderLineCountDto countDto = new SalesOrderLineCountDto();
		long salesPersonCount = 0;
		long orderCount = 0;
		long orderLineCount = 0;

		try {
			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			if (excelReportDto.getUserId() != null) {
				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());

				if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
					String salesPersonCountQuery = env.getProperty("getSalesPersonCountCOPY");
					List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonCountQuery,
							new Object[] { distId }, new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
					if (!salesPersonCountList.isEmpty()) {
						salesPersonCount = salesPersonCountList.size();
					}

					String getOrderCountQuery = env.getProperty("getOrderCountCOPY");

					List<RecordCountDto> orderCountList = jdbcTemplate.query(getOrderCountQuery,
							new Object[] { strEndDate, strStartDate, distId },
							new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
					if (!orderCountList.isEmpty()) {
						orderCount = orderCountList.get(0).getCount();
					}

					String getOrderLineCountQuery = env.getProperty("getOrderLineCountCOPY");
					List<RecordCountDto> orderLineCountList = jdbcTemplate.query(getOrderLineCountQuery,
							new Object[] { strEndDate, strStartDate, distId },
							new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
					if (!orderLineCountList.isEmpty()) {
						orderLineCount = orderLineCountList.get(0).getCount();
					}
				} else {
					String salesPersonCountQuery = env.getProperty("getSalesPersonCount");
					List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonCountQuery,
							new Object[] { distId }, new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
					if (!salesPersonCountList.isEmpty()) {
						salesPersonCount = salesPersonCountList.size();
					}

					String getOrderCountQuery = env.getProperty("getOrderCount");

					List<RecordCountDto> orderCountList = jdbcTemplate.query(getOrderCountQuery,
							new Object[] { strEndDate, strStartDate, distId },
							new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
					if (!orderCountList.isEmpty()) {
						orderCount = orderCountList.get(0).getCount();
					}

					String getOrderLineCountQuery = env.getProperty("getOrderLineCount");
					List<RecordCountDto> orderLineCountList = jdbcTemplate.query(getOrderLineCountQuery,
							new Object[] { strEndDate, strStartDate, distId },
							new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
					if (!orderLineCountList.isEmpty()) {
						orderLineCount = orderLineCountList.get(0).getCount();
					}
				}
			}

			countDto.setTotalSalesPersonCount(salesPersonCount);
			countDto.setTotalOrderCount(orderCount);
			countDto.setTotalLineItemCount(orderLineCount);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return countDto;
	}

	@Override
	public String getRegionV2(String orgId, String userId) throws ServiceException {
		try {
			String query = env.getProperty("getRegionV2");
			List<ResponseExcelDto> dataList = jdbcTemplate.query(query, new Object[] { userId, orgId },
					new BeanPropertyRowMapper<ResponseExcelDto>(ResponseExcelDto.class));
			if (!dataList.isEmpty()) {
				return dataList.get(0).getRegion();
			} else {
				return "";
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	List<Long> getDistributorIdByAH(String distributorId) {
		String query = env.getProperty("getDistributorIdByAH");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	List<Long> getDistributorIdBySO(String distributorId) {
		String query = env.getProperty("getDistributorIdBySO");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	@Override
	public List<ExcelDataProduct> getProductReportData2(ExcelReportDto excelReportDto) throws ServiceException {
		try {
			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			String query = env.getProperty("getReportDataByProductAdmin");

			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
			
			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByProductDistributor");

				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") group by lmp.product_id,lmp.product_name, lmp.product_code, lsl.ptr_price";
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByProductDistributor");

				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") group by lmp.product_id,lmp.product_name, lmp.product_code, lsl.ptr_price";
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByProductDistributor");

				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") group by lmp.product_id,lmp.product_name, lmp.product_code, lsl.ptr_price";
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {
				List<ExcelDataProduct> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getProductId(), startDate, endDate, strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				if (dataList != null) {
					return dataList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ExcelDataRegion> getSalesOrderLineCount2(List<ExcelDataRegion> list, ExcelReportDto excelReportDto)
			throws ServiceException {

		long salesPersonCount = 0;
		List<ExcelDataRegion> listMain = new LinkedList<ExcelDataRegion>();
		try {
			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);

			if (excelReportDto.getUserId() != null) {
				
				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());

				if (userDetailsDto.getUserType().equalsIgnoreCase(ADMIN)) {

					for (ExcelDataRegion excelDataRegion : list) {
						if (excelDataRegion.getDistributorId() != null) {
							
							String salesPersonCountQuery = env.getProperty("getSalesPersonCount");
							
							List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonCountQuery,
									new Object[] { excelDataRegion.getDistributorId(), startDate, endDate,
											strStartDate, strEndDate },
									new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
							
							if (!salesPersonCountList.isEmpty()) {
								RecordCountDto recordCountDto = salesPersonCountList.get(0);
								salesPersonCount = recordCountDto.getCount();
							}
							
							excelDataRegion.setTotalEff(salesPersonCount);
							listMain.add(excelDataRegion);
							
						}
					}

				} else {
					for (ExcelDataRegion excelDataRegion : list) {
						if (excelDataRegion.getDistributorId() != null) {
							
							String salesPersonCountQuery = env.getProperty("getSalesPersonCount");
							
							List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonCountQuery,
									new Object[] { excelDataRegion.getDistributorId(), startDate, endDate,
											strStartDate, strEndDate },
									new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
							
							if (!salesPersonCountList.isEmpty()) {
								RecordCountDto recordCountDto = salesPersonCountList.get(0);
								salesPersonCount = recordCountDto.getCount();
							}
							excelDataRegion.setTotalEff(salesPersonCount);
							listMain.add(excelDataRegion);
						}
					}
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listMain;
	}

}
