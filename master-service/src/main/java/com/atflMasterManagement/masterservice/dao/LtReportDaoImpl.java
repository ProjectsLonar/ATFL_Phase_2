package com.atflMasterManagement.masterservice.dao;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

	SimpleDateFormat reportStartEndDateFormat = new SimpleDateFormat("dd-MMM-yy"); // 2020-07-31

	@Override
	public List<ExcelDataSelesperson> getSalesReportData(ExcelReportDto excelReportDto) throws ServiceException {
		try {

			//DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			DateFormat utcFormat = new SimpleDateFormat("dd-MMM-yy");
			//Date startDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
	            Instant instant = Instant.from(inputFormatter.parse(excelReportDto.getFromDate()));
	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
	            
			System.out.println("Date ="+ excelReportDto.getFromDate()); System.out.println("NewDate=" +startDate);
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			Instant instant1 = Instant.from(inputFormatter.parse(excelReportDto.getToDate()));
            String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
			
        //  String strStartDate = reportStartEndDateFormat.format(startDate);
		//	String strEndDate = reportStartEndDateFormat.format(endDate);

			String strStartDate = startDate;
			String strEndDate = endDate;

			
			String query = env.getProperty("getReportDataBySalesPersonAdmin");

			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
			System.out.println("Hi i'm in Dao query reqbody = "+excelReportDto + "\n user Id is "+excelReportDto.getUserId());
			System.out.println("Hi i'm in Dao userDetailsDto = "+userDetailsDto);
			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				System.out.println("Hi i'm in Dao DISTRIBUTOR userList = "+userList);
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataBySalesPersonDistributor");
				query = query + " and lsv.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsv.order_number desc";
				System.out.println("Fullquery =" +query);
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				System.out.println("Hi i'm in Dao SALESOFFICER userList = "+userList);
//				if (userList == null) {
//					return null;           this block comment on 05-Nov-2024 by vaibhav to use new query
//				}
//				query = env.getProperty("getReportDataBySalesPersonDistributor");
				query = env.getProperty("getReportDataBySalesPersonDistributorAH");
//				query = query + " and lsv.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
//						+ ") order by lsv.order_number desc";   this block comment on 05-Nov-2024 by vaibhav to use new query
				System.out.println("SALESOFFICER Fullquery =" +query);
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
				System.out.println("Hi i'm in Dao AREAHEAD userList = "+userList);
//				if (userList == null) {
//					return null;                this block comment on 05-Nov-2024 by vaibhav to use new query
//				}

				//query = env.getProperty("getReportDataBySalesPersonDistributor");  comment on 05-Nov-2024 by vaibhav to use new query
				query = env.getProperty("getReportDataBySalesPersonDistributorAH");
//				query = query + " and lsv.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
//						+ ") order by lsv.order_number desc";   this block comment on 05-Nov-2024 by vaibhav to use new query
				System.out.println("AREAHEAD query =" +query);
			}
			else if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR") ||
					(userDetailsDto.getUserType().equalsIgnoreCase("ORGANIZATION_USER"))) {
				List<ExcelDataSelesperson> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getStatus(), //excelReportDto.getOrgId(),
								excelReportDto.getDistributorId(), //excelReportDto.getEmployeeId(), 
								startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataSelesperson>(ExcelDataSelesperson.class));
				System.out.println("SYSTEMADMINISTRATOR Fullquery =" +query);
				if (dataList != null) {
				      System.out.print("result="+dataList);
								return dataList;
							}
			}
				else if (userDetailsDto.getUserType().equalsIgnoreCase("SALES")) {
				List<ExcelDataSelesperson> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getStatus(), //excelReportDto.getOrgId(),
								excelReportDto.getDistributorId(), //excelReportDto.getEmployeeId(), 
								startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataSelesperson>(ExcelDataSelesperson.class));
				System.out.println("SALES Fullquery =" +query);
				if (dataList != null) {
				      System.out.print("result="+dataList);
								return dataList;
							}
			}
			
			List<ExcelDataSelesperson> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getStatus(), //excelReportDto.getOrgId(),
							excelReportDto.getDistributorId(), excelReportDto.getEmployeeId(), 
							startDate, endDate//,
							//strStartDate, strEndDate 
							},
					new BeanPropertyRowMapper<ExcelDataSelesperson>(ExcelDataSelesperson.class));
			if (dataList != null) {
      System.out.print("result="+dataList);
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
		//	Date startDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
	            Instant instant = Instant.from(inputFormatter.parse(excelReportDto.getFromDate()));
	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
						
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
	            Instant instant1 = Instant.from(inputFormatter.parse(excelReportDto.getToDate()));
	            String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
	            
	        //String strStartDate = reportStartEndDateFormat.format(startDate);
			//String strEndDate = reportStartEndDateFormat.format(endDate);

	            String strStartDate = startDate;
				String strEndDate = endDate;  
	            
				System.out.println("Hi i'm in Dao query reqbody = "+excelReportDto + "\n user Id is "+excelReportDto.getUserId());
				
			String query = env.getProperty("getReportDataByRegionwiseSalesAdmin");

			if (excelReportDto.getUserId() != null) {

				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
				System.out.println("Hi i'm in Dao userDetailsDto is = "+userDetailsDto);
				
				if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {

					List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
					System.out.println("Hi i'm in Dao AREAHEAD userList is = "+userList);
					
					if (userList == null) {
						return null;
					}

					Long processedCount = checkOrderedIsProcessed(userList);
					System.out.println("Hi i'm in Dao AREAHEAD processedCount is = "+processedCount);
					query = env.getProperty("getReportDataByRegionwiseSales");

// 		if (processedCount != 0) {         comment temprary on 13-May-2024
						query = query + " and lsh.last_updated_by in ("
								+ userList.toString().replace("[", "").replace("]", "")
								+ ")group by lmo.region,lmd.distributor_id, lmd.distributor_name, lmd.distributor_code , lmd.distributor_crm_code , lsl.ptr_price, lsl.quantity";
					//}
					System.out.println("Hi i'm in Dao AREAHEAD query is = "+query);

				} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

					List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
					System.out.println("Hi i'm in Dao SALESOFFICER userList is = "+userList);

					if (userList == null) {
						return null;
					}
					Long processedCount = checkOrderedIsProcessed(userList);
					System.out.println("Hi i'm in Dao SALESOFFICER processedCount is = "+processedCount);
					
					query = env.getProperty("getReportDataByRegionwiseSales");

//					if (processedCount != 0) { comment temprary on 13-May-2024
						query = query + " and lsh.last_updated_by in ("
								+ userList.toString().replace("[", "").replace("]", "")
								+ ")group by lmo.region,lmd.distributor_id, lmd.distributor_name, lmd.distributor_code , lmd.distributor_crm_code, lsl.ptr_price, lsl.quantity";
//					}
					System.out.println("Hi i'm in Dao SALESOFFICER query is = "+query);
				}

			}
			System.out.println("Hi i'm in Dao query is = "+query);
			List<ExcelDataRegion> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getDistributorId(), startDate, endDate,
							strStartDate, strEndDate, excelReportDto.getRegion(), excelReportDto.getDistributorId(),
							startDate, endDate, strStartDate, strEndDate },
					new BeanPropertyRowMapper<ExcelDataRegion>(ExcelDataRegion.class));
			System.out.println("Hi i'm in Dao query dataList is = "+dataList.size());
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
			} else if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR")) {
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
			//Date startDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
	            Instant instant = Instant.from(inputFormatter.parse(excelReportDto.getFromDate()));
	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
			
	            Instant instant1 = Instant.from(inputFormatter.parse(excelReportDto.getToDate()));
	            String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
	            
	         //String strStartDate = reportStartEndDateFormat.format(startDate);
			//String strEndDate = reportStartEndDateFormat.format(endDate);

	            String strStartDate = startDate;
				String strEndDate = endDate; 
				System.out.println("Hi i'm in Dao query reqbody = "+excelReportDto + "\n user Id is "+excelReportDto.getUserId());
				
			if (excelReportDto.getUserId() != null) {
				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
				System.out.println("Hi i'm in Dao query userDetailsDto = "+ userDetailsDto);
				
				if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
					List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
					System.out.println("Hi i'm in Dao query userList = "+ userList);
					if (userList == null) {
						return null;
					}

					String query = env.getProperty("getReportDataByDistributor");

					query = query + " and lsh.last_updated_by in ("
							+ userList.toString().replace("[", "").replace("]", "") + ") "
							+ " group by lmd.distributor_crm_code,lmpc.category_code,lmd.distributor_name,lmpc.category_name,lsl.ptr_price,lsl.quantity order by lmd.distributor_crm_code asc";
					System.out.println("Hi i'm in Dao query = "+ query);
					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));
					System.out.println("Hi i'm in Dao query dataList = "+ dataList);
					if (dataList != null) {
						return dataList;
					}
				} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
					List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
					System.out.println("Hi i'm in Dao query userList = "+ userList);
					if (userList == null) {
						return null;
					}
					String query = env.getProperty("getReportDataByDistributor");
					query = query + " and lsh.last_updated_by in ("
							+ userList.toString().replace("[", "").replace("]", "") + ") "
							+ " group by lmd.distributor_crm_code,lmpc.category_code,lmd.distributor_name,lmpc.category_name,lsl.ptr_price,lsl.quantity order by lmd.distributor_crm_code asc";
					System.out.println("Hi i'm in Dao query = "+ query);
					
					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));
					System.out.println("Hi i'm in Dao query dataList= "+ dataList);
					if (dataList != null) {
						return dataList;
					}
				}else if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
					List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
					System.out.println("Hi i'm in Dao query userList = "+ userList);
					if (userList == null) {
						return null;
					}
					String query = env.getProperty("getReportDataByDistributor");
					query = query + " and lsh.last_updated_by in ("
							+ userList.toString().replace("[", "").replace("]", "") + ") "
							+ " group by lmd.distributor_crm_code,lmpc.category_code,lmd.distributor_name,lmpc.category_name,lsl.ptr_price,lsl.quantity order by lmd.distributor_crm_code asc";
					System.out.println("Hi i'm in Dao query = "+ query);
					
					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));
					System.out.println("Hi i'm in Dao query dataList= "+ dataList);
					if (dataList != null) {
						return dataList;
					}
				} 
				
				
				else if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR") ||
						userDetailsDto.getUserType().equalsIgnoreCase("ORGANIZATION_USER")) {
					String query = env.getProperty("getReportDataByDistributorAdmin");
					System.out.println("Hi i'm in Dao query = "+ query);
					List<ExcelDataDistributor> dataList = jdbcTemplate.query(query,
							new Object[] { excelReportDto.getOrgId(), excelReportDto.getCategoryId(),
									excelReportDto.getDistributorId(), startDate, endDate, strStartDate, strEndDate },
							new BeanPropertyRowMapper<ExcelDataDistributor>(ExcelDataDistributor.class));
					System.out.println("Hi i'm in Dao query dataList = "+ dataList);
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

			//Date startDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
	            Instant instant = Instant.from(inputFormatter.parse(excelReportDto.getFromDate()));
	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
			
	            Instant instant1 = Instant.from(inputFormatter.parse(excelReportDto.getToDate()));
	            String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
			
			//String strStartDate = reportStartEndDateFormat.format(startDate);
			//String strEndDate = reportStartEndDateFormat.format(endDate);

	            String strStartDate = startDate;
				String strEndDate = endDate;
	            
			String query = env.getProperty("getReportDataByOutletAdmin");

			System.out.println("Hi i'm in Dao query reqbody = "+excelReportDto + "\n user Id is "+excelReportDto.getUserId());
			
			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
			System.out.println("Hi i'm in Dao userDetailsDto is = "+userDetailsDto);
			
			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				System.out.println("Hi i'm in Dao userList is = "+userList);
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByOutletDistributor");
				System.out.println("Hi i'm in Dao DISTRIBUTOR query is = "+query);
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.last_update_date desc";
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				System.out.println("Hi i'm in Dao userList is = "+userList);
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByOutletDistributor");
				System.out.println("Hi i'm in Dao SALESOFFICER query is = "+query);
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.last_update_date desc";
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
				System.out.println("Hi i'm in Dao userList is = "+userList);
				if (userList == null) {
					return null;
				}
				query = env.getProperty("getReportDataByOutletDistributor");
				System.out.println("Hi i'm in Dao AREAHEAD query is = "+query);
				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") order by lsh.last_update_date desc";
			}else if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR")||
					(userDetailsDto.getUserType().equalsIgnoreCase("ORGANIZATION_USER"))) {
				
				System.out.println("Hi i'm in Dao query is = "+query);
				
				List<ExcelDataOutlet> dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getOutletId(),
								excelReportDto.getDistributorId(), status, startDate, endDate, strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataOutlet>(ExcelDataOutlet.class));
				if (dataList != null) {
					return dataList;
				}
			}

			System.out.println("Hi i'm in Dao query is = "+query);
			List<ExcelDataOutlet> dataList = jdbcTemplate.query(query,
					new Object[] { excelReportDto.getOrgId(), excelReportDto.getOutletId(),
							excelReportDto.getDistributorId(), status, startDate, endDate, strStartDate, strEndDate },
					new BeanPropertyRowMapper<ExcelDataOutlet>(ExcelDataOutlet.class));
			System.out.println("dataList size is = "+dataList.size());
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
			List<Object> params = new ArrayList<>();
			String query = env.getProperty("getSearchDistributorForRegion");
			String searchFieldStr = null;
			if (excelReportDto.getSearchField() != null) {
				searchFieldStr = "%" + excelReportDto.getSearchField().toUpperCase().trim() + "%";
			}

			String regionStr = null;
			if (excelReportDto.getRegion() != null) {
				regionStr = excelReportDto.getRegion();
			}
			
			if (excelReportDto.getLimit() == 0 || excelReportDto.getLimit() == 1) {
				excelReportDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(excelReportDto.getOffset() == 0) {
				excelReportDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			
			if (excelReportDto.getUserId() != null) {
System.out.println("ReqData is ="+excelReportDto + "UserId is = "+excelReportDto.getUserId());
				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
				System.out.println("userDetailsDto"+userDetailsDto);
//				if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
//
//					//List<Long> userList = getDistributorIdByAH(userDetailsDto.getPositionId());
//					List<String> userList = getDistributorIdByAH(userDetailsDto.getPositionId());
//					System.out.println("userList AREAHEAD"+userList);
//					
//					query = env.getProperty("getSearchDistributorForRegionOther");
//					
//					String userList1 = userList.stream().map(disId->"?").collect(Collectors.joining(", "));
//					
//					params = new ArrayList<>();
//					 
//				    params.add(excelReportDto.getOrgId().toString());
//					params.add(regionStr);
//					params.add(searchFieldStr);
//				    params.addAll(userList);
//				    params.add(excelReportDto.getLimit()); 
//				    params.add(excelReportDto.getOffset());
//				    
//					query = query + " and lmd.distributor_id in ("+userList1+") order by distributor_id asc ) a OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//					System.out.println("userList AREAHEAD query"+query);
//					
//				} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {
//
//				//	List<Long> userList = getDistributorIdBySO(userDetailsDto.getPositionId());
//					List<String> userList = getDistributorIdBySO(userDetailsDto.getPositionId());
//					System.out.println("userList SALESOFFICER userList"+userList);
//					
//					String userList12 = userList.stream().map(disId->"?").collect(Collectors.joining(", "));
//					params = new ArrayList<>();
//					 
//				    params.add(excelReportDto.getOrgId().toString());
//					params.add(regionStr);
//					params.add(searchFieldStr);
//				    params.addAll(userList);
//				    params.add(excelReportDto.getLimit()); 
//				    params.add(excelReportDto.getOffset());
//				    
//					
//					query = env.getProperty("getSearchDistributorForRegionOther");
//					query = query + " and lmd.distributor_id in ("+userList12+") order by distributor_id asc ) a OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//					System.out.println("userList SALESOFFICER query"+query);
//				}
				 if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR") ||
						userDetailsDto.getUserType().equalsIgnoreCase("ORGANIZATION_USER") ||
						userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD) ||
						userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

					//	List<Long> userList = getDistributorIdBySO(userDetailsDto.getPositionId());
					///	List<String> userList = getDistributorIdBySO(userDetailsDto.getPositionId());
					//	System.out.println("userList SALESOFFICER userList"+userList);
						
					//	String userList12 = userList.stream().map(disId->"?").collect(Collectors.joining(", "));
					//	params = new ArrayList<>();
						 
					//    params.add(excelReportDto.getOrgId().toString());
					//	params.add(regionStr);
					//	params.add(searchFieldStr);
					//    params.addAll(userList);
					//    params.add(excelReportDto.getLimit()); 
					//    params.add(excelReportDto.getOffset());
					    
						
						query = env.getProperty("getSearchDistributorForSysAdmin");
						List<DistributorDto> dataList = jdbcTemplate.query(query, new Object[] {
								excelReportDto.getOrgId(),
								regionStr, searchFieldStr,
							excelReportDto.getLimit(),
							excelReportDto.getOffset()},
								new BeanPropertyRowMapper<DistributorDto>(DistributorDto.class));
						if (dataList != null) {
							return dataList;
						}
					}
			}

						System.out.println("In query"+query);
//			List<DistributorDto> dataList = jdbcTemplate.query(query,
//					new Object[] { excelReportDto.getOrgId().toString(), regionStr, searchFieldStr,
//							excelReportDto.getLimit(), excelReportDto.getOffset() },
//					new BeanPropertyRowMapper<DistributorDto>(DistributorDto.class));
						
						List<DistributorDto> dataList = jdbcTemplate.query(query,params.toArray(),
								new BeanPropertyRowMapper<DistributorDto>(DistributorDto.class));

			System.out.println("In dataList"+dataList);
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

			if (excelReportDto.getLimit() == 0 || excelReportDto.getLimit() == 1) {
				excelReportDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(excelReportDto.getOffset() == 0) {
				excelReportDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
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

			if (excelReportDto.getLimit() == 0 || excelReportDto.getLimit() == 1) {
				excelReportDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(excelReportDto.getOffset() == 0) {
				excelReportDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
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

				if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR")) {
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

	//List<Long> getDistributorIdByAH(String distributorId) { original
		List<String> getDistributorIdByAH(String distributorId) {
		String query = env.getProperty("getDistributorIdByAH");
		//List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);  original
		List<String> userIdList = jdbcTemplate.queryForList(query, String.class, distributorId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	//List<Long> getDistributorIdBySO(String distributorId) {
		List<String> getDistributorIdBySO(String distributorId) {
		String query = env.getProperty("getDistributorIdBySO");
		//List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
		List<String> userIdList = jdbcTemplate.queryForList(query, String.class, distributorId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	@Override
	public List<ExcelDataProduct> getProductReportData2(ExcelReportDto excelReportDto) throws ServiceException {
		List<ExcelDataProduct> dataList =  new ArrayList<ExcelDataProduct>();
		try {
			//Date startDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
	            Instant instant = Instant.from(inputFormatter.parse(excelReportDto.getFromDate()));
	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
			
	            Instant instant1 = Instant.from(inputFormatter.parse(excelReportDto.getToDate()));
	            String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
	            
	        //String strStartDate = reportStartEndDateFormat.format(startDate);
			//String strEndDate = reportStartEndDateFormat.format(endDate);

	            String strStartDate = startDate;
				String strEndDate = endDate;     
	            
			String query = env.getProperty("getReportDataByProductAdmin");

			UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
			System.out.println("Hi i'm in serviceImpl userDetailsDto = "+userDetailsDto +"&&&"+excelReportDto.getUserId());
			if (userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				System.out.println("Hi i'm in serviceImpl DISTRIBUTOR userList = "+userList);
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByProductDistributor");

				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") group by lmp.product_id,lmp.product_name, lmp.product_code, lsl.ptr_price, lsl.quantity";
				 dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				 System.out.println( excelReportDto.getOrgId()+ excelReportDto.getProductId()+ startDate+ endDate+
					strStartDate+ strEndDate);
				System.out.println("Hi i'm in serviceImpl DISTRIBUTOR query = "+query);
				System.out.println("Hi i'm in serviceImpl DISTRIBUTOR dataList = "+dataList);
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(SALESOFFICER)) {

				List<Long> userList = getUsersBySalesOfficer(userDetailsDto.getPositionId());
				System.out.println("Hi i'm in serviceImpl SALESOFFICER userList = "+userList);
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByProductDistributor");

				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") group by lmp.product_id,lmp.product_name, lmp.product_code, lsl.ptr_price, lsl.quantity";
				 dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				System.out.println("Hi i'm in serviceImpl SALESOFFICER query = "+query);
				System.out.println("Hi i'm in serviceImpl SALESOFFICER dataList = "+dataList);
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase(AREAHEAD)) {
				List<Long> userList = getReportUsersByAreaHead(userDetailsDto.getPositionId());
				System.out.println("Hi i'm in serviceImpl AREAHEAD userList = "+userList);
				if (userList == null) {
					return null;
				}

				query = env.getProperty("getReportDataByProductDistributor");

				query = query + " and lsh.last_updated_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") group by lmp.product_id,lmp.product_name, lmp.product_code, lsl.ptr_price, lsl.quantity";
				 dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getOrgId(), excelReportDto.getProductId(), startDate, endDate,
								strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				System.out.println("Hi i'm in serviceImpl AREAHEAD query = "+query);
				System.out.println("Hi i'm in serviceImpl AREAHEAD dataList = "+dataList);
				if (dataList != null) {
					return dataList;
				}
			} else if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR") ||
					userDetailsDto.getUserType().equalsIgnoreCase("ORGANIZATION_USER")) {
				 dataList = jdbcTemplate.query(query,
						new Object[] { excelReportDto.getProductId(), startDate, endDate, strStartDate, strEndDate },
						new BeanPropertyRowMapper<ExcelDataProduct>(ExcelDataProduct.class));
				System.out.println("Hi i'm in serviceImpl ADMIN query = "+query);
				System.out.println("Hi i'm in serviceImpl ADMIN dataList = "+dataList);
				if (dataList != null) {
					return dataList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	@Override
	public List<ExcelDataRegion> getSalesOrderLineCount2(List<ExcelDataRegion> list, ExcelReportDto excelReportDto)
			throws ServiceException {
		//System.out.println("Hi i'm in DaoImpl salesOrder count2");
		long salesPersonCount = 0;
		List<ExcelDataRegion> listMain = new LinkedList<ExcelDataRegion>();
		try {
/*			Date startDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
			Date endDate = reportStartEndDateFormat
					.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			String strStartDate = reportStartEndDateFormat.format(startDate);
			String strEndDate = reportStartEndDateFormat.format(endDate);
*/
			
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
	            Instant instant = Instant.from(inputFormatter.parse(excelReportDto.getFromDate()));
	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
						
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
	            Instant instant1 = Instant.from(inputFormatter.parse(excelReportDto.getToDate()));
	            String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
	            
	        //String strStartDate = reportStartEndDateFormat.format(startDate);
			//String strEndDate = reportStartEndDateFormat.format(endDate);

	            String strStartDate = startDate;
				String strEndDate = endDate; 
			
			
			if (excelReportDto.getUserId() != null) {
				
				UserDetailsDto userDetailsDto = getUserTypeAndDisId(excelReportDto.getUserId());
				//System.out.println("Hi i'm in DaoImpl salesOrder count2 userDetailsDto" + userDetailsDto);
				
				if (userDetailsDto.getUserType().equalsIgnoreCase("SYSTEMADMINISTRATOR")) {

					for (ExcelDataRegion excelDataRegion : list) {
						if (excelDataRegion.getDistributorId() != null) {
							
							String salesPersonCountQuery = env.getProperty("getSalesPersonCount");
						//System.out.println("Hi i'm in DaoImpl salesOrder count2 salesPersonCountQuery" + salesPersonCountQuery);
							List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonCountQuery,
									new Object[] { excelDataRegion.getDistributorId(), startDate, endDate,
											strStartDate, strEndDate },
									new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
							
							if (!salesPersonCountList.isEmpty()) {
								RecordCountDto recordCountDto = salesPersonCountList.get(0);
								salesPersonCount = recordCountDto.getCount();
							}
							//System.out.println("Hi i'm in DaoImpl salesOrder count2 salesPersonCountList" + salesPersonCountList);
							excelDataRegion.setTotalEff(salesPersonCount);
							listMain.add(excelDataRegion);
							//System.out.println(listMain);
						}
					}

				} else {
					for (ExcelDataRegion excelDataRegion : list) {
						if (excelDataRegion.getDistributorId() != null) {
							
							String salesPersonCountQuery = env.getProperty("getSalesPersonCount");
							//System.out.println("salesPersonCountQuery" + salesPersonCountQuery);
							List<RecordCountDto> salesPersonCountList = jdbcTemplate.query(salesPersonCountQuery,
									new Object[] { excelDataRegion.getDistributorId(), startDate, endDate,
											strStartDate, strEndDate },
									new BeanPropertyRowMapper<RecordCountDto>(RecordCountDto.class));
							
							//System.out.println("salesPersonCountList" + salesPersonCountList);
							
							if (!salesPersonCountList.isEmpty()) {
								RecordCountDto recordCountDto = salesPersonCountList.get(0);
							//	System.out.println("recordCountDto" + recordCountDto +"count is " +recordCountDto.getCount());
								salesPersonCount = recordCountDto.getCount();
							//	System.out.println("salesPersonCount" + salesPersonCount);
							}
						//	System.out.println("Hi i'm in else DaoImpl salesOrder count2 salesPersonCountList" + salesPersonCountList);
							
							excelDataRegion.setTotalEff(salesPersonCount);
							listMain.add(excelDataRegion);
						//	System.out.println(listMain);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}System.out.println("Hi i'm in DaoImpl salesOrder count2 listMain" + listMain);
		return listMain;
	}

}
