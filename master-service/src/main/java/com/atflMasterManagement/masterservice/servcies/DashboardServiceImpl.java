package com.atflMasterManagement.masterservice.servcies;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dao.DashboardDao;
import com.atflMasterManagement.masterservice.dto.CategoryRevenueDto;
import com.atflMasterManagement.masterservice.dto.CategoryRevenueResponseDto;
import com.atflMasterManagement.masterservice.dto.DailyAndMonthlyDto;
import com.atflMasterManagement.masterservice.dto.DailySalesResponseDto;
import com.atflMasterManagement.masterservice.dto.MonthlyResponseDto;
import com.atflMasterManagement.masterservice.dto.OrderAndLineCountDto;
import com.atflMasterManagement.masterservice.dto.StatusWiseOrdersCountDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.reports.ExcelDailySales;

@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class DashboardServiceImpl implements DashboardService, CodeMaster{
	
	@Autowired
	DashboardDao dashboardDao;
	
	@Autowired
	private Environment env;

	@Override
	public Status statusWiseOrdersCount(String orgId, String userId) throws ServiceException {
		List<StatusWiseOrdersCountDto> listStatusData = dashboardDao.statusWiseOrdersCount(orgId,userId);
		Status status = new Status();
		if (listStatusData != null) {
			status.setCode(SUCCESS);
			status.setMessage("Success");
			status.setData(listStatusData);
		} else {
			status.setCode(FAIL);
			status.setMessage("FAIL");
			status.setData(listStatusData);
		}
		return status;
	}

	@Override
	public Status categoryRevenueDistribution(String orgId, String userId) throws ServiceException {
		Status status;
		try {
			List<CategoryRevenueDto> categoryRevenueList = dashboardDao.categoryRevenueDistribution(orgId,userId);
			
			Map<String,CategoryRevenueResponseDto> categoryRevenueResponseDtoMap=new HashMap<String,CategoryRevenueResponseDto>();
			
			for (Iterator iterator = categoryRevenueList.iterator(); iterator.hasNext();) {
				CategoryRevenueDto categoryRevenueDto = (CategoryRevenueDto) iterator.next();
				if(categoryRevenueResponseDtoMap.get(categoryRevenueDto.getCategoryId())!=null) {
					CategoryRevenueResponseDto categoryRevenueResponseDto=categoryRevenueResponseDtoMap.get(categoryRevenueDto.getCategoryId());
					
					categoryRevenueResponseDto.setCategoryId(categoryRevenueDto.getCategoryId());
					categoryRevenueResponseDto.setCategoryName(categoryRevenueDto.getCategoryName());
					categoryRevenueResponseDto.setCategoryDesc(categoryRevenueDto.getCategoryDesc());
					
					Long price=(categoryRevenueDto.getQuantity()* categoryRevenueDto.getPtrPrice());
					price=categoryRevenueResponseDto.getRevenue()+price;
					categoryRevenueResponseDto.setRevenue(price);
					categoryRevenueResponseDtoMap.put(categoryRevenueDto.getCategoryId(), categoryRevenueResponseDto);
				}else {
					CategoryRevenueResponseDto categoryRevenueResponseDto=new CategoryRevenueResponseDto();
					categoryRevenueResponseDto.setCategoryId(categoryRevenueDto.getCategoryId());
					categoryRevenueResponseDto.setCategoryName(categoryRevenueDto.getCategoryName());
					categoryRevenueResponseDto.setCategoryDesc(categoryRevenueDto.getCategoryDesc());
					Long price=(categoryRevenueDto.getQuantity()* categoryRevenueDto.getPtrPrice());
					price=categoryRevenueResponseDto.getRevenue()+price;
					categoryRevenueResponseDto.setRevenue(price);
					
					//Long distId = dashboardDao.getDistributorIdByUserId(userId);
					String distId = dashboardDao.getDistributorIdByUserId(userId);
					//if(distId != 0) {
						OrderAndLineCountDto orderAndLineCountDto = dashboardDao.getOrderCountData(distId, categoryRevenueDto.getCategoryId(), userId);
						categoryRevenueResponseDto.setDbc(orderAndLineCountDto.getTotalOrderCount());
					//}
					
					categoryRevenueResponseDtoMap.put(categoryRevenueDto.getCategoryId(), categoryRevenueResponseDto);
				}
			}
			
			List<CategoryRevenueResponseDto> categoryRevenueResponseDtoList= new ArrayList(categoryRevenueResponseDtoMap.values());
			
			status = new Status();
			if (!categoryRevenueResponseDtoList.isEmpty()) {
				status.setCode(SUCCESS);
				status.setMessage("Success");
				status.setData(categoryRevenueResponseDtoList);
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
				status.setData(categoryRevenueResponseDtoList);
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	public Status categoryRevenueDistributionv2(String orgId, String userId) throws ServiceException {
		Status status;
		try {
			Status status1 =new Status();
			List<CategoryRevenueDto> categoryRevenueList = dashboardDao.categoryRevenueDistribution(orgId,userId);
			
			if(categoryRevenueList.isEmpty()) {
				status1.setMessage("Report data not available");
				status1.setCode(FAIL);
				return status1;
			}
			
			Map<String,CategoryRevenueResponseDto> categoryRevenueResponseDtoMap = new HashMap<String,CategoryRevenueResponseDto>();
			
			for(CategoryRevenueDto categoryRevenueDto:categoryRevenueList) {
				CategoryRevenueResponseDto categoryRevenueResponseDto=new CategoryRevenueResponseDto();
				categoryRevenueResponseDto.setCategoryId(categoryRevenueDto.getCategoryId());
				categoryRevenueResponseDto.setRevenue(categoryRevenueDto.getRevenue());
				categoryRevenueResponseDto.setDbc(categoryRevenueDto.getDbc());
				categoryRevenueResponseDto.setCategoryName(categoryRevenueDto.getCategoryName());
				categoryRevenueResponseDto.setCategoryDesc(categoryRevenueDto.getCategoryName());
				categoryRevenueResponseDtoMap.put(categoryRevenueResponseDto.getCategoryId(), categoryRevenueResponseDto);
			}
			
			//Map<Long,CategoryRevenueResponseDto> categoryRevenueResponseDtoMap2 = dashboardDao.getOrderCountDataV2(categoryRevenueResponseDtoMap,userId);

			List<CategoryRevenueResponseDto> categoryRevenueResponseDtoList= new ArrayList(categoryRevenueResponseDtoMap.values());
			
			status = new Status();
			if (!categoryRevenueResponseDtoList.isEmpty()) {
				status.setCode(SUCCESS);
				status.setMessage("Success");
				status.setData(categoryRevenueResponseDtoList);
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
				status.setData(categoryRevenueResponseDtoList);
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	@Override
	public Status dailySales(String orgId, String userId) throws ServiceException {
		try {
			List<DailyAndMonthlyDto> dailySalesList = dashboardDao.dailySales(orgId,userId);
			//Long distId = dashboardDao.getDistributorIdByUserId(userId);
			String distId = dashboardDao.getDistributorIdByUserId(userId);
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			int month = today.getMonthValue();
			int day = today.getDayOfMonth();
			Map<String,DailySalesResponseDto> dailySalesResponseDtoMap=new LinkedHashMap<String,DailySalesResponseDto>();
			for (int i = 1; i <= day; i++) {
				String dateInString = String.format("%02d", i)+"-"+ String.format("%02d", month)+"-"+year;
				DailySalesResponseDto dailySalesResponseDto=new DailySalesResponseDto();
				dailySalesResponseDto.setDate(dateInString);
				
					OrderAndLineCountDto orderAndLineCountDto = dashboardDao.getOrderAndLineCountData(distId, dateInString, userId);
					dailySalesResponseDto.setDbc(orderAndLineCountDto.getTotalOrderCount());
					dailySalesResponseDto.setTls(orderAndLineCountDto.getTotalLineItemCount());
				
				dailySalesResponseDtoMap.put(dateInString, dailySalesResponseDto);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			for (Iterator iterator = dailySalesList.iterator(); iterator.hasNext();) {
				DailyAndMonthlyDto dailyAndMonthlyDto = (DailyAndMonthlyDto) iterator.next();
				String orderDate=sdf.format(dailyAndMonthlyDto.getCreationDate());

				if(dailySalesResponseDtoMap.get(orderDate)!=null) {
					DailySalesResponseDto dailySalesResponseDto=dailySalesResponseDtoMap.get(orderDate);
					Long price=dailyAndMonthlyDto.getPtrPrice()*dailyAndMonthlyDto.getQuantity();
					//String sale=""+(Long.parseLong(dailySalesResponseDto.getSale())+price);
					Long sale=(Long.parseLong(dailySalesResponseDto.getSale())+price);
					dailySalesResponseDto.setDate(orderDate);
					dailySalesResponseDto.setSale(sale.toString());
					dailySalesResponseDtoMap.put(orderDate, dailySalesResponseDto);
				}else {
					//No data in Else
				}
			}
			List<DailySalesResponseDto> dailySalesResponseDtoList= new ArrayList(dailySalesResponseDtoMap.values());
			Status status = new Status();
			if(!dailySalesResponseDtoList.isEmpty()) {
				status.setCode(SUCCESS);
				status.setMessage("Success");
				status.setData(dailySalesResponseDtoList);
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
				status.setData(dailySalesResponseDtoList);
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	@Override
	public Status dailySalesV2(String orgId, String userId) throws ServiceException {
		try {
			List<DailySalesResponseDto> dailySalesList = dashboardDao.dailySalesV2(orgId,userId);
			
			//Long distId = dashboardDao.getDistributorIdByUserId(userId);
			
			Map<String,DailySalesResponseDto> dailySalesResponseDtoMap = new LinkedHashMap<String,DailySalesResponseDto>();
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			int month = today.getMonthValue();
			int day = today.getDayOfMonth();
			for (int i = 1; i <= day; i++) {
				DailySalesResponseDto dailySalesResponseObj = new DailySalesResponseDto();
				String dateInString = String.format("%02d", i) + "-" + String.format("%02d", month) + "-" + year;
				dailySalesResponseObj.setDate(dateInString);
				dailySalesResponseDtoMap.put(dateInString, dailySalesResponseObj);
			}
			
			if(dailySalesList!=null) {
				for(DailySalesResponseDto dailySalesResponseDto:dailySalesList) {
	 				if(dailySalesResponseDtoMap.containsKey(dailySalesResponseDto.getDate())) {
	 					dailySalesResponseDtoMap.get(dailySalesResponseDto.getDate()).setSale(dailySalesResponseDto.getSale());
	 					dailySalesResponseDtoMap.get(dailySalesResponseDto.getDate()).setTls(dailySalesResponseDto.getTls());
	 					dailySalesResponseDtoMap.get(dailySalesResponseDto.getDate()).setDbc(dailySalesResponseDto.getDbc());
					}
				}	
			}
			//Map<String,DailySalesResponseDto> dailySalesResponseMap = dashboardDao.getOrderAndLineCountDataV2(distId,userId,dailySalesResponseDtoMap);
			
			List<DailySalesResponseDto> dailySalesResponseDtoList= new ArrayList<DailySalesResponseDto>(dailySalesResponseDtoMap.values());
			
			Status status = new Status();
			
			if(!dailySalesResponseDtoList.isEmpty()) {
				status.setCode(SUCCESS);
				status.setMessage("Success");
				status.setData(dailySalesResponseDtoList);
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
				status.setData(dailySalesResponseDtoList);
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	@Override
	public Status monthlySalesV2(String orgId, String userId) throws ServiceException {
		try {
			
			List<MonthlyResponseDto> MonthlyResponseDtoList = dashboardDao.monthlySalesV2(orgId, userId);
			
			Map<String,MonthlyResponseDto> monthlyResponseDtoMap=new LinkedHashMap<String,MonthlyResponseDto>();
			
			//Long distId = dashboardDao.getDistributorIdByUserId(userId);
			String distId = dashboardDao.getDistributorIdByUserId(userId);
			
			for (int i = 11; i >= 0; i--) {
				YearMonth date = YearMonth.now().minusMonths(i);
				String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+" "+date.getYear();
				MonthlyResponseDto monthlyResponseDto=new MonthlyResponseDto();
				monthlyResponseDto.setMonth(monthName);
				monthlyResponseDto.setMonthNo(String.valueOf(date.getMonthValue()));
				monthlyResponseDtoMap.put(monthName, monthlyResponseDto);
			}
			
			if (MonthlyResponseDtoList != null) {
				for (MonthlyResponseDto monthlyResponseDto : MonthlyResponseDtoList) {
					if (monthlyResponseDtoMap.containsKey(monthlyResponseDto.getMonth())) {
						monthlyResponseDtoMap.get(monthlyResponseDto.getMonth()).setSale(monthlyResponseDto.getSale());
						monthlyResponseDtoMap.get(monthlyResponseDto.getMonth()).setTls(monthlyResponseDto.getTls());
						monthlyResponseDtoMap.get(monthlyResponseDto.getMonth()).setDbc(monthlyResponseDto.getDbc());
					}
				}
			}
			Map<String,MonthlyResponseDto> monthlyResponseDto = dashboardDao.getCountDataForMonthlyDashboardV2(distId,userId,monthlyResponseDtoMap);
			
			List<MonthlyResponseDto> monthlyResponseDtoList= new ArrayList(monthlyResponseDto.values());
			Status status = new Status();
			if (!monthlyResponseDtoList.isEmpty()) {
				status.setCode(SUCCESS);
				status.setMessage("Success");
				status.setData(monthlyResponseDtoList);
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
				status.setData(monthlyResponseDtoList);
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	@Override
	public Status monthlySales(String orgId, String userId) throws ServiceException {
		try {
			List<DailyAndMonthlyDto> monthlySalesList = dashboardDao.monthlySales(orgId,userId);
			
			LocalDate today = LocalDate.now();
			int day = today.getDayOfMonth();
			//System.out.println(day);
			Map<String,MonthlyResponseDto> monthlyResponseDtoMap=new LinkedHashMap<String,MonthlyResponseDto>();
			//Long distId = dashboardDao.getDistributorIdByUserId(userId);
			String distId = dashboardDao.getDistributorIdByUserId(userId);
			
			for (int i = 11; i >= 0; i--) {
			    YearMonth date = YearMonth.now().minusMonths(i); 
			    String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			    MonthlyResponseDto monthlyResponseDto = new MonthlyResponseDto();
			    monthlyResponseDto.setMonth(monthName + " " + date.getYear());
			    monthlyResponseDto.setMonthNo(String.format("%02d", date.getMonth().getValue()));
				//if(distId != 0) {
					OrderAndLineCountDto orderAndLineCountDto = dashboardDao.getCountDataForMonthlyDashboard(distId, String.format("%02d", date.getMonth().getValue()), userId);
					monthlyResponseDto.setTotalEff(orderAndLineCountDto.getSalesPersonsCount());
					monthlyResponseDto.setDbc(orderAndLineCountDto.getTotalOrderCount());
					monthlyResponseDto.setTls(orderAndLineCountDto.getTotalLineItemCount());
				//}
			    monthlyResponseDtoMap.put(String.format("%02d", date.getMonth().getValue()), monthlyResponseDto);
			}
			
			for (Iterator iterator = monthlySalesList.iterator(); iterator.hasNext();) {
				DailyAndMonthlyDto dailyAndMonthlyDto = (DailyAndMonthlyDto) iterator.next();
				Integer orderMonth= dailyAndMonthlyDto.getCreationDate().getMonth();
				String monthStr = String.format("%02d", orderMonth+1);
				
				if(monthlyResponseDtoMap.get(monthStr)!=null) {
					MonthlyResponseDto monthlyResponseDto=monthlyResponseDtoMap.get(monthStr);
					Long price=dailyAndMonthlyDto.getPtrPrice()*dailyAndMonthlyDto.getQuantity();
					//String sale=""+(Long.parseLong(dailySalesResponseDto.getSale())+price);
					Long sale=(Long.parseLong(monthlyResponseDto.getSale())+price);
					monthlyResponseDto.setSale(sale.toString());
					monthlyResponseDtoMap.put(monthStr, monthlyResponseDto);
				}else {
					//No data in Else
					System.out.println("In else part==>"+monthStr);
				}
			}
			
			List<MonthlyResponseDto> monthlyResponseDtoList= new ArrayList(monthlyResponseDtoMap.values());
			Status status = new Status();
			if (!monthlyResponseDtoList.isEmpty()) {
				status.setCode(SUCCESS);
				status.setMessage("Success");
				status.setData(monthlyResponseDtoList);
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
				status.setData(monthlyResponseDtoList);
			}
			return status;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;

	}

	@Override
	public Status dailySalesExcel(String orgId, String userId) throws ServiceException {
		Status status = dailySalesV2(orgId, userId);
		List<DailySalesResponseDto> dailySalesResponseDtoList = (List<DailySalesResponseDto>) status.getData();
		if(!dailySalesResponseDtoList.isEmpty()) {
			status = createExcelSalespersonReport(dailySalesResponseDtoList);
			return status;
		}else {
			status.setMessage("Dashboard data not available");
			status.setCode(FAIL);
			return status;
		}
	}
	
	private Status createExcelSalespersonReport(List<DailySalesResponseDto> dailySalesResponseDtoList) throws ServiceException{
		String reportDateTime =  new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());
		Status status = new Status();
		String saveDirectory = null;
		XSSFWorkbook workbook = new XSSFWorkbook();
		String fileName = null;	
		
		String reportCreationPath = env.getProperty("reportCreationPath");
		String reportShowPath = env.getProperty("reportShowPath");
		saveDirectory = reportCreationPath;
		
		File dir = new File(saveDirectory);
		if (!dir.isDirectory()) {
			if (dir.mkdirs()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Error in directory creation");
			}
		}
		XSSFSheet sheet = workbook.createSheet("Daily_Sales");
		try {
			ExcelDailySales.headerData(workbook, sheet,dailySalesResponseDtoList);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		fileName = "Daily_Sales" + "_" +reportDateTime+".xlsx";
		String filePath = saveDirectory + fileName;
		String fileShowPath = reportShowPath+ fileName;
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			status.setCode(SUCCESS);
			status.setMessage("File Generated successfully");
			status.setUrl(fileShowPath);		
			return status;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}

}

