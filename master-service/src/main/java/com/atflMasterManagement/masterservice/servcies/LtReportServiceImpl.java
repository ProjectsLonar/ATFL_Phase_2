package com.atflMasterManagement.masterservice.servcies;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dao.LtReportDao;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.reports.DistributorDto;
import com.atflMasterManagement.masterservice.reports.ExcelDataDistributor;
import com.atflMasterManagement.masterservice.reports.ExcelDataOutlet;
import com.atflMasterManagement.masterservice.reports.ExcelDataProduct;
import com.atflMasterManagement.masterservice.reports.ExcelDataRegion;
import com.atflMasterManagement.masterservice.reports.ExcelDataSelesperson;
import com.atflMasterManagement.masterservice.reports.ExcelReportDistributor;
import com.atflMasterManagement.masterservice.reports.ExcelReportDto;
import com.atflMasterManagement.masterservice.reports.ExcelReportOutlet;
import com.atflMasterManagement.masterservice.reports.ExcelReportProduct;
import com.atflMasterManagement.masterservice.reports.ExcelReportRegion;
import com.atflMasterManagement.masterservice.reports.ExcelReportSalesperson;
import com.atflMasterManagement.masterservice.reports.ResponseExcelDto;
import com.atflMasterManagement.masterservice.reports.SalesOrderLineCountDto;

@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class LtReportServiceImpl implements LtReportService, CodeMaster {

	@Autowired
	private LtReportDao ltReportDao;

	@Autowired
	private Environment env;

	Date cdate = new Date();

	@Override
	public Status getSalesReportData(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataSelesperson> salesReportDataList = ltReportDao.getSalesReportData(excelReportDto);
			System.out.println("Hi i'm in serviceImpl query dats is = "+excelReportDto +"&&&"+salesReportDataList);
			if (salesReportDataList == null || salesReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}

			if (!salesReportDataList.isEmpty()) {
				status = createExcelSalespersonReport(salesReportDataList, excelReportDto);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getRegionwiseSalesReportData(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataRegion> regionReportDataList = ltReportDao.getRegionwiseSalesReportData(excelReportDto);
			if (regionReportDataList == null || regionReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}

			Map<String, ExcelDataRegion> regionReportDataMap = new LinkedHashMap<String, ExcelDataRegion>();

			for (Iterator iterator = regionReportDataList.iterator(); iterator.hasNext();) {
				ExcelDataRegion excelDataRegion = (ExcelDataRegion) iterator.next();
				String mapKey = excelDataRegion.getDistributorCode().trim() + "_" + excelDataRegion.getRegion().trim();
				if (regionReportDataMap.get(mapKey) != null) {
					ExcelDataRegion excelDataRegionMapObj = regionReportDataMap.get(mapKey);

					Double revenu = (excelDataRegion.getQuantity()
							* Double.parseDouble(excelDataRegion.getListPrice()));

					revenu = excelDataRegionMapObj.getRevenue() + revenu;

					excelDataRegionMapObj.setRevenue(revenu);

					regionReportDataMap.put(mapKey, excelDataRegionMapObj);
				} else {
					ExcelDataRegion excelDataRegionObj = new ExcelDataRegion();
					excelDataRegionObj.setDistributorName(excelDataRegion.getDistributorName());
					excelDataRegionObj.setDistributorCode(excelDataRegion.getDistributorCrmCode());
					excelDataRegionObj.setRegion(excelDataRegion.getRegion());

					Double revenu = (excelDataRegion.getQuantity()
							* Double.parseDouble(excelDataRegion.getListPrice()));

					revenu = excelDataRegionObj.getRevenue() + revenu;

					excelDataRegionObj.setRevenue(revenu);

					SalesOrderLineCountDto salesOrderLineCountDto = ltReportDao
							.getSalesOrderLineCount(excelDataRegion.getDistributorId(), excelReportDto);
					excelDataRegionObj.setTotalEff(salesOrderLineCountDto.getTotalSalesPersonCount());
					excelDataRegionObj.setDbc(salesOrderLineCountDto.getTotalOrderCount());
					excelDataRegionObj.setTls(salesOrderLineCountDto.getTotalLineItemCount());

					regionReportDataMap.put(mapKey, excelDataRegionObj);
				}
			}
			List<ExcelDataRegion> excelDataRegionList = new ArrayList(regionReportDataMap.values());

			if (!excelDataRegionList.isEmpty()) {
				status = createExcelRegionReport(excelDataRegionList, excelReportDto);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getProductReportData(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataProduct> productReportDataList = ltReportDao.getProductReportData(excelReportDto);
			if (productReportDataList == null || productReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}
			Map<String, ExcelDataProduct> productReportDataMap = new LinkedHashMap<String, ExcelDataProduct>();

			for (Iterator iterator = productReportDataList.iterator(); iterator.hasNext();) {
				ExcelDataProduct excelDataProduct = (ExcelDataProduct) iterator.next();

				if (productReportDataMap.get(excelDataProduct.getProductCode()) != null) {
					ExcelDataProduct excelDataProductObj = productReportDataMap.get(excelDataProduct.getProductCode());
					Long quantity = (excelDataProductObj.getQuantity1() + excelDataProduct.getQuantity1());
					excelDataProductObj.setQuantity1(quantity);
					Double revenu = (excelDataProduct.getQuantity1()
							* Double.parseDouble(excelDataProduct.getPtrPrice1()));
					revenu = excelDataProductObj.getAmount() + revenu;
					excelDataProductObj.setAmount(revenu);
					productReportDataMap.put(excelDataProduct.getProductCode(), excelDataProductObj);
				} else {
					ExcelDataProduct excelDataProductObj = new ExcelDataProduct();
					excelDataProductObj.setProductCode(excelDataProduct.getProductCode());
					excelDataProductObj.setProductName(excelDataProduct.getProductName());
					excelDataProductObj.setPtrPrice1(excelDataProduct.getPtrPrice1());
					excelDataProductObj.setQuantity1(excelDataProduct.getQuantity1());
					Double revenu = (excelDataProduct.getQuantity1()
							* Double.parseDouble(excelDataProduct.getPtrPrice1()));
					revenu = excelDataProductObj.getAmount() + revenu;
					excelDataProductObj.setAmount(revenu);
					productReportDataMap.put(excelDataProduct.getProductCode(), excelDataProductObj);
				}
			}
			List<ExcelDataProduct> excelDataProductList = new ArrayList(productReportDataMap.values());

			if (!excelDataProductList.isEmpty()) {
				status = createExcelProductReport(excelDataProductList, excelReportDto);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getDistributorReportData(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataDistributor> distributorReportDataList = ltReportDao.getDistributorReportData(excelReportDto);
			System.out.println("Hi i'm in serviceImpl query dats is = "+distributorReportDataList);
			if (distributorReportDataList == null || distributorReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}
			if (!distributorReportDataList.isEmpty()) {
				status = createExcelDistributorReport(distributorReportDataList, excelReportDto);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getOutletReportData(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataOutlet> outletReportDataList = ltReportDao.getOutletReportData(excelReportDto);
			System.out.println("Hi i'm in serviceImpl query dats is = "+outletReportDataList);
			if (outletReportDataList == null || outletReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}

			Map<String, ExcelDataOutlet> outletReportDataMap = new LinkedHashMap<String, ExcelDataOutlet>();

			for (Iterator iterator = outletReportDataList.iterator(); iterator.hasNext();) {
				ExcelDataOutlet excelDataOutletObj = (ExcelDataOutlet) iterator.next();

				String mapKey = excelDataOutletObj.getOrderNumber().trim() + "_"
						+ excelDataOutletObj.getStatus().trim();

				if (outletReportDataMap.get(mapKey) != null) {
					ExcelDataOutlet excelDataOutlet = outletReportDataMap.get(mapKey);
					if(excelDataOutletObj.getListPrice()!=null) {
						System.out.println("in map for loop "+excelDataOutletObj.getQuantity()+ "in loop of map "+(excelDataOutletObj.getListPrice()));
						
					Double totalAmount = (excelDataOutletObj.getQuantity()
							* Double.parseDouble(excelDataOutletObj.getListPrice()));
//					String totalAmount = (excelDataOutletObj.getQuantity()+"*"+(excelDataOutletObj.getListPrice()));
//					totalAmount = excelDataOutlet.getAmount() + totalAmount;
					System.out.println("in map for totalAmount"+totalAmount);
					excelDataOutlet.setAmount(totalAmount);}
//					excelDataOutlet.setAmount1(totalAmount);}
					switch (excelDataOutletObj.getCategoryCode()) {
					case EDIBLE_OILS: // C101
						Double edibleOilsTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String edibleOilsTotal = (excelDataOutletObj.getQuantity()+"*"+
//								 (excelDataOutletObj.getListPrice()));
						
						edibleOilsTotal = excelDataOutlet.getEdibleOils() + edibleOilsTotal;
						excelDataOutlet.setEdibleOils(edibleOilsTotal);
//						excelDataOutlet.setEdibleOils1(edibleOilsTotal);
						break;
					case READY_TO_COOK: // C102
						Double readyToCookTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String readyToCookTotal = (excelDataOutletObj.getQuantity()+"*"+
//								 (excelDataOutletObj.getListPrice()));
						readyToCookTotal = excelDataOutlet.getReadyToCook() + readyToCookTotal;
						excelDataOutlet.setReadyToCook(readyToCookTotal);
//						excelDataOutlet.setReadyToCook1(readyToCookTotal);
						break;
					case READY_TO_EAT_SNACKS: // C103
						Double readyToEatTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String readyToEatTotal = (excelDataOutletObj.getQuantity()+"*"+
//								 (excelDataOutletObj.getListPrice()));
						readyToEatTotal = excelDataOutlet.getReadyToEat() + readyToEatTotal;
						excelDataOutlet.setReadyToEat(readyToEatTotal);
//						excelDataOutlet.setReadyToEat1(readyToEatTotal);
						break;
					case SPREADS: // C104
						if(excelDataOutletObj.getListPrice()!=null) {
						Double spreadsTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String spreadsTotal = (excelDataOutletObj.getQuantity()+"*"+
//								 (excelDataOutletObj.getListPrice()));
						spreadsTotal = excelDataOutlet.getSpreads() + spreadsTotal;
						excelDataOutlet.setSpreads(spreadsTotal);}
//						excelDataOutlet.setSpreads1(spreadsTotal);}
						break;
					case CEREAL_SNACKS: // C105
						Double cerealSnacksTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String cerealSnacksTotal = (excelDataOutletObj.getQuantity()+"*"+
//								 (excelDataOutletObj.getListPrice()));
						cerealSnacksTotal = excelDataOutlet.getCerealSnacks() + cerealSnacksTotal;
						excelDataOutlet.setCerealSnacks(cerealSnacksTotal);
//						excelDataOutlet.setCerealSnacks1(cerealSnacksTotal);
						break;
					case CHOCOLATEY_CONFECTIONERY: // C106
						Double chocolateyConfectioneryTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String chocolateyConfectioneryTotal = (excelDataOutletObj.getQuantity()+"*"+
//								 (excelDataOutletObj.getListPrice()));
						chocolateyConfectioneryTotal = excelDataOutlet.getChocolateyConfectionery()
								+ chocolateyConfectioneryTotal;
						excelDataOutlet.setChocolateyConfectionery(chocolateyConfectioneryTotal);
//						excelDataOutlet.setChocolateyConfectionery1(chocolateyConfectioneryTotal);
						break;
					}
					outletReportDataMap.put(mapKey, excelDataOutlet);
				} else {
					ExcelDataOutlet excelDataOutlet = new ExcelDataOutlet();
					excelDataOutlet.setOutletName(excelDataOutletObj.getOutletName());
					excelDataOutlet.setOutletCode(excelDataOutletObj.getOutletCode());
					excelDataOutlet.setOrderNumber(excelDataOutletObj.getOrderNumber());
					excelDataOutlet.setStatus(excelDataOutletObj.getStatus());
					excelDataOutlet.setCreationDate(excelDataOutletObj.getCreationDate());
					excelDataOutlet.setDistributorName(excelDataOutletObj.getDistributorName());
					excelDataOutlet.setDistributorCrmCode(excelDataOutletObj.getDistributorCrmCode());
                   
					
					if(excelDataOutletObj.getPtrPrice()!= null && excelDataOutletObj.getListPrice()!= null && 
							excelDataOutletObj.getQuantity()!=null) {
				Double totalAmount = (excelDataOutletObj.getQuantity()
							* Double.parseDouble(excelDataOutletObj.getListPrice()));
//				String totalAmount = (excelDataOutletObj.getQuantity()+"*" +excelDataOutletObj.getListPrice());
//					totalAmount = excelDataOutlet.getAmount() + totalAmount;
				excelDataOutlet.setAmount(totalAmount);       //original
//					excelDataOutlet.setAmount1((totalAmount));}

					switch (excelDataOutletObj.getCategoryCode()) {
					case EDIBLE_OILS: // C101
						Double edibleOilsTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String edibleOilsTotal = (excelDataOutletObj.getQuantity()+"*"+ (excelDataOutletObj.getListPrice()));
//						edibleOilsTotal = excelDataOutlet.getEdibleOils() + edibleOilsTotal;
						excelDataOutlet.setEdibleOils(edibleOilsTotal);
//						excelDataOutlet.setEdibleOils1(edibleOilsTotal);
						break;
					case READY_TO_COOK: // C102
					    Double readyToCookTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String readyToCookTotal = (excelDataOutletObj.getQuantity()+"*"+ (excelDataOutletObj.getListPrice()));
//						readyToCookTotal = excelDataOutlet.getReadyToCook() + readyToCookTotal;
					    excelDataOutlet.setReadyToCook(readyToCookTotal);
//						excelDataOutlet.setReadyToCook1(readyToCookTotal);
						break;
					case READY_TO_EAT_SNACKS: // C103
					Double readyToEatTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String readyToEatTotal = (excelDataOutletObj.getQuantity()+"*"+ (excelDataOutletObj.getListPrice()));
//						readyToEatTotal = excelDataOutlet.getReadyToEat() + readyToEatTotal;
					excelDataOutlet.setReadyToEat(readyToEatTotal);
//						excelDataOutlet.setReadyToEat1(readyToEatTotal);
						break;
					case SPREADS: // C104
						if(excelDataOutletObj.getListPrice()!=null) {
						Double spreadsTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String spreadsTotal = (excelDataOutletObj.getQuantity()+"*"+ (excelDataOutletObj.getListPrice()));
//						spreadsTotal = excelDataOutlet.getSpreads() + spreadsTotal;
						excelDataOutlet.setSpreads(spreadsTotal);}
//						excelDataOutlet.setSpreads1(spreadsTotal);}
						break;
					case CEREAL_SNACKS: // C105
					Double cerealSnacksTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String cerealSnacksTotal = (excelDataOutletObj.getQuantity()+"*"+ (excelDataOutletObj.getListPrice()));
//						cerealSnacksTotal = excelDataOutlet.getCerealSnacks() + cerealSnacksTotal;
					excelDataOutlet.setCerealSnacks(cerealSnacksTotal);
//						excelDataOutlet.setCerealSnacks1(cerealSnacksTotal);
						break;
					case CHOCOLATEY_CONFECTIONERY: // C106
					Double chocolateyConfectioneryTotal = (excelDataOutletObj.getQuantity()
								* Double.parseDouble(excelDataOutletObj.getListPrice()));
//						String chocolateyConfectioneryTotal = (excelDataOutletObj.getQuantity()+"*"+ (excelDataOutletObj.getListPrice()));
//						chocolateyConfectioneryTotal = excelDataOutlet.getChocolateyConfectionery()
//								+ chocolateyConfectioneryTotal;
					excelDataOutlet.setChocolateyConfectionery(chocolateyConfectioneryTotal);
//						excelDataOutlet.setChocolateyConfectionery1(chocolateyConfectioneryTotal);
						break;
					}

					outletReportDataMap.put(mapKey, excelDataOutlet);
				}
			}}
			List<ExcelDataOutlet> excelDataProductList = new ArrayList(outletReportDataMap.values());
			
		if (!excelDataProductList.isEmpty()) {
				//if (!outletReportDataList.isEmpty()) {
				status = createExcelOutletReport(excelDataProductList, excelReportDto);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		
			} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	private Status createExcelSalespersonReport(List<ExcelDataSelesperson> salesReportDataList,
			ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		String saveDirectory = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		String fileName = null;
		String reportDateTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());
		String reportCreationPath = env.getProperty("reportCreationPath");
		String reportShowPath = env.getProperty("reportShowPath");
	    String logoImagePath = env.getProperty("logoImagePath"); //Remove for NNF
		//String logoImagePath = "";
		saveDirectory = reportCreationPath;

		File dir = new File(saveDirectory);
		if (!dir.isDirectory()) {
			if (dir.mkdirs()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Error in directory creation");
			}
		}
		// XSSFSheet sheet = workbook.createSheet("Sales_Person_Performance_Report");
		Sheet sheet = workbook.createSheet("Sales_Person_Performance_Report");
		ExcelReportSalesperson.headerData(workbook, sheet, excelReportDto, salesReportDataList, logoImagePath);

		fileName = "SalesPerson_Report" + "_" + reportDateTime + ".xlsx";
		String filePath = saveDirectory + fileName;
		String fileShowPath = reportShowPath + fileName;
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

	private Status createExcelRegionReport(List<ExcelDataRegion> regionReportDataList, ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		String saveDirectory = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		String fileName = null;
		String reportDateTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());
		String reportCreationPath = env.getProperty("reportCreationPath");
		String reportShowPath = env.getProperty("reportShowPath");
		String logoImagePath = "";
		saveDirectory = reportCreationPath;
		File dir = new File(saveDirectory);
		if (!dir.isDirectory()) {
			if (dir.mkdirs()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Error in directory creation");
			}
		}
		Sheet sheet = workbook.createSheet("Regionwise_Sales_Report");
		ExcelReportRegion.headerData(workbook, sheet, excelReportDto, regionReportDataList, logoImagePath);
		fileName = "RegionWiseSales_Report" + "_" + reportDateTime + ".xlsx";
		String filePath = saveDirectory + fileName;
		String fileShowPath = reportShowPath + fileName;

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

	private Status createExcelProductReport(List<ExcelDataProduct> productReportDataList, ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		String saveDirectory = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		String fileName = null;
		String reportDateTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());
		String reportCreationPath = env.getProperty("reportCreationPath");
		String reportShowPath = env.getProperty("reportShowPath");
		// String logoImagePath = env.getProperty("logoImagePath");//Remove for NNF
		String logoImagePath = "";
		saveDirectory = reportCreationPath;
		File dir = new File(saveDirectory);
		if (!dir.isDirectory()) {
			if (dir.mkdirs()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Error in directory creation");
			}
		}
		Sheet sheet = workbook.createSheet("Product_wise_Revenue_Summary_Report");
		System.out.println("Directory creation" + excelReportDto +"\n"+ productReportDataList);
		ExcelReportProduct.headerData(workbook, sheet, excelReportDto, productReportDataList, logoImagePath);

		fileName = "ProductWiseReport" + "_" + reportDateTime + ".xlsx";
		String filePath = saveDirectory + fileName;
		String fileShowPath = reportShowPath + fileName;
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

	private Status createExcelDistributorReport(List<ExcelDataDistributor> distributorReportDataList,
			ExcelReportDto excelReportDto) throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		String saveDirectory = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		String fileName = null;
		String reportDateTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());

		String reportCreationPath = env.getProperty("reportCreationPath");
		String reportShowPath = env.getProperty("reportShowPath");
		// String logoImagePath = env.getProperty("logoImagePath");//Remove for NNF
		String logoImagePath = "";
		saveDirectory = reportCreationPath;
		File dir = new File(saveDirectory);
		if (!dir.isDirectory()) {
			if (dir.mkdirs()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Error in directory creation");
			}
		}
		Sheet sheet = workbook.createSheet("Distributorwise_Category_Summary_Report");
		ExcelReportDistributor.headerData(workbook, sheet, excelReportDto, distributorReportDataList, logoImagePath);

		fileName = "DistributorWiseCategoryReport" + "_" + reportDateTime + ".xlsx";
		String filePath = saveDirectory + fileName;
		String fileShowPath = reportShowPath + fileName;

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

	private Status createExcelOutletReport(List<ExcelDataOutlet> outletReportDataList, ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		String reportDateTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());
		Status status = new Status();
		String saveDirectory = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		String fileName = null;

		String reportCreationPath = env.getProperty("reportCreationPath");
		String reportShowPath = env.getProperty("reportShowPath");
		// String logoImagePath = env.getProperty("logoImagePath");//Remove for NNF
		String logoImagePath = "";
		saveDirectory = reportCreationPath;

		File dir = new File(saveDirectory);
		if (!dir.isDirectory()) {
			if (dir.mkdirs()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Error in directory creation");
			}
		}
		Sheet sheet = workbook.createSheet("Outletwise_Order_Summary_Report");
		ExcelReportOutlet.headerData(workbook, sheet, excelReportDto, outletReportDataList, logoImagePath);

		fileName = "OutletWiseOrderReport" + "_" + reportDateTime + ".xlsx";
		String filePath = saveDirectory + fileName;
		String fileShowPath = reportShowPath + fileName;

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

	@Override
	public Status searchDistributor(ExcelReportDto excelReportDto) throws ServiceException {
		Status status = new Status();
		try {
			List<DistributorDto> searchDistributorList = ltReportDao.searchDistributor(excelReportDto);
			System.out.println("In service list"+searchDistributorList);
			if (!searchDistributorList.isEmpty()) {
				status.setData(searchDistributorList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status searchSalesPerson(ExcelReportDto excelReportDto) throws ServiceException {
		Status status = new Status();
		try {
			List<ResponseExcelDto> searchSalesPersonList = ltReportDao.searchSalesPerson(excelReportDto);
			if (!searchSalesPersonList.isEmpty()) {
				status.setData(searchSalesPersonList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getRegion(String orgId) throws ServiceException {
		Status status = new Status();
		try {
			List<ResponseExcelDto> getRegionList = ltReportDao.getRegion(orgId);
			if (!getRegionList.isEmpty()) {
				status.setData(getRegionList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status searchProduct(ExcelReportDto excelReportDto) throws ServiceException {
		Status status = new Status();
		try {
			List<ResponseExcelDto> searchProductList = ltReportDao.searchProduct(excelReportDto);
			if (!searchProductList.isEmpty()) {
				status.setData(searchProductList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status searchOutlets(ExcelReportDto excelReportDto) throws ServiceException {
		Status status = new Status();
		try {
			List<ResponseExcelDto> searchOutletsList = ltReportDao.searchOutlets(excelReportDto);
			if (!searchOutletsList.isEmpty()) {
				status.setData(searchOutletsList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getOutletStatus(String orgId) throws ServiceException {
		Status status = new Status();
		try {
			List<ResponseExcelDto> getOutletStatusList = ltReportDao.getOutletStatus(orgId);
			if (!getOutletStatusList.isEmpty()) {
				status.setData(getOutletStatusList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getCategoryDetails(String orgId) throws ServiceException {
		Status status = new Status();
		try {
			List<ResponseExcelDto> getCategoryDetailsList = ltReportDao.getCategoryDetails(orgId);
			if (!getCategoryDetailsList.isEmpty()) {
				status.setData(getCategoryDetailsList);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getRegionV2(String orgId, String userid) throws ServiceException {
		Status status = new Status();
		try {
			String defaultRegion = ltReportDao.getRegionV2(orgId, userid);
			if (defaultRegion != "") {
				status.setData(defaultRegion);
				status.setMessage("Sucess");
				status.setCode(SUCCESS);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getProductReportData2(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataProduct> productReportDataList = ltReportDao.getProductReportData2(excelReportDto);
			System.out.println("Hi i'm in serviceImpl query dats is = "+excelReportDto +"&&&"+productReportDataList);
			if (productReportDataList == null || productReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}

			if (!productReportDataList.isEmpty()) {
				System.out.println("Hi i'm"+ productReportDataList);
				status = createExcelProductReport(productReportDataList, excelReportDto);

			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status getRegionwiseSalesReportData2(ExcelReportDto excelReportDto)
			throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();
		try {
			List<ExcelDataRegion> regionReportDataList = ltReportDao.getRegionwiseSalesReportData(excelReportDto);
			if (regionReportDataList == null || regionReportDataList.isEmpty()) {
				status.setMessage("Report data not available");
				status.setCode(FAIL);
				return status;
			}
			//System.out.println("Hi i'm in serviceImpl dats is ="+ regionReportDataList);
			List<ExcelDataRegion> regionReportDataList2 = ltReportDao.getSalesOrderLineCount2(regionReportDataList,
					excelReportDto);

			if (!regionReportDataList2.isEmpty()) {
				status = createExcelRegionReport(regionReportDataList2, excelReportDto);
			} else {
				status.setMessage("Fail");
				status.setCode(FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Hi i'm in serviceImpl status is ="+ status);
		return status;
	}

}
