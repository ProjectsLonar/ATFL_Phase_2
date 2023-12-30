package com.lonar.atflMobileInterfaceService.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.atflMobileInterfaceService.common.CreateCSVFile;
import com.lonar.atflMobileInterfaceService.common.ReadProductImages;
import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.common.Validation;
import com.lonar.atflMobileInterfaceService.dao.LtJobeDao;
import com.lonar.atflMobileInterfaceService.dao.LtReadCSVFilesDao;
import com.lonar.atflMobileInterfaceService.ftp.FTPDownloader;
import com.lonar.atflMobileInterfaceService.model.CodeMaster;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtMastDistributors;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployees;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployeesPosition;
import com.lonar.atflMobileInterfaceService.model.LtMastInventory;
import com.lonar.atflMobileInterfaceService.model.LtMastOutlets;
import com.lonar.atflMobileInterfaceService.model.LtMastPositions;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceLists;
import com.lonar.atflMobileInterfaceService.model.LtMastProductCat;
import com.lonar.atflMobileInterfaceService.model.LtMastProducts;
import com.lonar.atflMobileInterfaceService.model.Status;
import com.lonar.atflMobileInterfaceService.thread.ExportMaster;
import com.lonar.atflMobileInterfaceService.thread.InitiateThread;

@Service
@PropertySource({ "classpath:persistence.properties" })
public class LtReadCSVFilesServicesImpl implements LtReadCSVFilesServices, CodeMaster {

	@Autowired
	LtReadCSVFilesDao ltReadCSVFilesDao;

	@Autowired
	LtJobeDao ltJobeDao;

	@Autowired
	LtJobeService ltJobeService;

	@Autowired
	LtJobeServiceCsvBk ltJobeServiceCsvBk;

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(ExportMaster.class);

	@Override
	public Status downloadfilesFromFTP() throws ServiceException {
		Status status = new Status();
		try {
			String msg = FTPDownloader.downloadFile();
			status.setMessage(msg);
			status.setCode(SUCCESS);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status readCsvFile() throws ServiceException, FileNotFoundException, IOException {
		Status status = new Status();

		return status;
	}

	@Transactional
	public LtJobLogs readOutletCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		Map<String, LtMastDistributors> distributorData = ltReadCSVFilesDao.getAllDistributor();

		Map<String, LtMastPositions> possitionData = ltReadCSVFilesDao.getAllPosition();

		if (distributorData.isEmpty() && possitionData.isEmpty()) {
			return ltJobLogs;
		}

		List<LtMastOutlets> outletList = new ArrayList<LtMastOutlets>();
		List<LtMastOutlets> outletSuccessList = new ArrayList<LtMastOutlets>();
		List<LtMastOutlets> outletFailedList = new ArrayList<LtMastOutlets>();
		boolean failedRecordFlag = false;
		try {

			for (CSVRecord record : parser) {

				if (record.size() >= parser.getHeaderMap().size()) {

					LtMastOutlets outletData = new LtMastOutlets();

					if (distributorData.containsKey(record.get("Distributor_Code").trim())) {

						if (possitionData.containsKey(record.get("Position_Code").trim())) {

							outletData.setDistributorId(
									distributorData.get(record.get("Distributor_Code").trim()).getDistributorId());
							outletData.setPositionsId(
									possitionData.get(record.get("Position_Code").trim()).getPositionId());
							outletData.setDistributorCode(record.get("Distributor_Code").trim());
							outletData.setPositionCode(record.get("Position_Code").trim());
							if (record.isMapped("Outlet_code")) {
								outletData.setOutletCode(record.get("Outlet_code").trim());
							} else {
								outletData.setOutletCode(record.get(0).trim());
							}
							outletData.setOutletType(record.get("Outlet_Type").trim());
							outletData.setOutletName(record.get("Outlet_Name").trim());
							outletData.setProprietorName(record.get("Proprietor_Name").trim());
							outletData.setAddress1(record.get("Address_1").trim());
							outletData.setAddress2(record.get("Address_2").trim());
							outletData.setAddress3(record.get("Address_3").trim());
							outletData.setAddress4(record.get("Address_4").trim());
							outletData.setCountry(record.get("Country").trim());
							outletData.setState(record.get("State").trim());
							outletData.setCity(record.get("City").trim());
							outletData.setPin_code(record.get("Pin_Code").trim());
							outletData.setOutletGstn(record.get("Outlet_GSTN").trim());
							outletData.setOutletPan(record.get("Outlet_PAN").trim());
							outletData.setLicenceNo(record.get("Licence_No").trim());
							outletData.setRegion(record.get("Region").trim());
							outletData.setArea(record.get("Area").trim());
							outletData.setTerritory(record.get("Territory").trim());
							outletData.setPrimaryMobile(record.get("Phone").trim());
							outletData.setEmail(record.get("Email").trim());
							outletData.setStatus(record.get("Status").trim());
							outletData.setPriceList(record.get("Price_List").trim());
							// Not in Excel
							outletData.setOrgId(Long.valueOf("1"));

							// check record empty or null
							if (record.isMapped("Outlet_code")) {
								failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Outlet_code").trim());

							} else {
								failedRecordFlag = Validation.isStrNullOrEmpty(record.get(0).trim());

							}
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Outlet_Type").trim());
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Proprietor_Name").trim());
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Outlet_Name").trim());
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Status").trim());

							if (failedRecordFlag == true) {
								outletData.setMobileInsertUpdate("FAILED");
								outletData.setMobileStatus("FAILED");
								outletData.setMobileRemarks("DATA IS EMPTY");
								outletFailedList.add(outletData);
							} else {
								outletList.add(outletData);
							}

						} else {
							outletData.setMobileRemarks("Position Code Failed");
							outletData.setMobileInsertUpdate("FAILED");
							outletData.setMobileStatus("Failed");
							outletData = CreateCSVFile.setOutletDto(outletData, record);
							outletFailedList.add(outletData);
						}

					} else {
						outletData.setMobileRemarks("Distributor Code Failed");
						outletData.setMobileStatus("Failed");
						outletData = CreateCSVFile.setOutletDto(outletData, record);
						outletFailedList.add(outletData);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Read Outlet CSVFile", e);
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			return ltJobLogs;
		}
		parser.close();
		System.out.println("List Size :: " + outletList.size());
		int count = 1;

		for (Iterator<LtMastOutlets> iterator = outletList.iterator(); iterator.hasNext();) {

			LtMastOutlets ltMastOutlets = (LtMastOutlets) iterator.next();

			System.out.println("ltMastOutlets Save==========>" + count++);
			LtMastOutlets outletsObj = null;

			if (ltMastOutlets.getOutletCode() != null) {
				outletsObj = ltReadCSVFilesDao.checkOutletCode(ltMastOutlets.getOutletCode());
			}

			if (outletsObj != null) {
				// Update existing data
				ltMastOutlets.setOutletId(outletsObj.getOutletId());
				ltMastOutlets.setCreationDate(outletsObj.getCreationDate());
				ltMastOutlets.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastOutlets.setCreatedBy((long) 100);
				ltMastOutlets.setLastUpdateLogin((long) 100);
				ltMastOutlets.setLastUpdatedBy((long) 100);

				// outletRepository.save(ltMastOutlets);
				ltReadCSVFilesDao.saveOutlet(ltMastOutlets);

				ltMastOutlets.setMobileInsertUpdate("UPDATE");
				ltMastOutlets.setMobileRemarks("Record inserted");
				ltMastOutlets.setMobileStatus("Success");
				outletSuccessList.add(ltMastOutlets);

			} else {
				// Insert into outlet
				ltMastOutlets.setCreationDate(Validation.getCurrentDateTime());
				ltMastOutlets.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastOutlets.setCreatedBy((long) 100);
				ltMastOutlets.setLastUpdateLogin((long) 100);
				ltMastOutlets.setLastUpdatedBy((long) 100);
				ltReadCSVFilesDao.saveOutlet(ltMastOutlets);
				ltMastOutlets.setMobileInsertUpdate("INSERT");
				ltMastOutlets.setMobileRemarks("Record inserted");
				ltMastOutlets.setMobileStatus("Success");
				outletSuccessList.add(ltMastOutlets);

			}
		}

		// Create CSV File Here
		int successRecord = outletSuccessList.size();
		outletSuccessList.addAll(outletFailedList);

		boolean fileCreate = CreateCSVFile.createOutletCSVFile(outletSuccessList, ltJobLogs);
		if (fileCreate) {
			ltJobLogs.setSuccessRecord(Long.valueOf(successRecord));
			ltJobLogs.setFailedRecord(Long.valueOf(outletFailedList.size()));
			ltJobLogs.setTotalRecord(Long.valueOf(outletSuccessList.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
		}

		if (!outletSuccessList.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.saveOutletCSVBk(outletList);
		}

		return ltJobLogs;
	}

	@Transactional
	public LtJobLogs readDistributorCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		List<LtMastDistributors> distributorsSList = new ArrayList<LtMastDistributors>();
		List<LtMastDistributors> distributorsSuccessList = new ArrayList<LtMastDistributors>();
		List<LtMastDistributors> distributorsFailedList = new ArrayList<LtMastDistributors>();

		boolean failedRecordFlag = false;
		try {
			int c = 1;
			for (CSVRecord record : parser) {
				System.out.println("Get count===>" + c++);
				if (record.size() >= parser.getHeaderMap().size()) {

					System.out.println("Distributor" + record.get("Dis_Code").trim());
					LtMastDistributors distributorData = new LtMastDistributors();
					if (record.isMapped("Distributor_code")) {
						distributorData.setDistributorCode(record.get("Distributor_code").trim());
					} else {
						distributorData.setDistributorCode(record.get(0).trim());

					}
					distributorData.setDistributorType(record.get("Distributor_Type").trim());
					distributorData.setDistributorCrmCode(record.get("Dis_Code").trim());
					distributorData.setDistributorName(record.get("Distributor_Name").trim());
					distributorData.setProprietorName(record.get("Proprietor_Name").trim());
					distributorData.setAddress_1(record.get("Address_1").trim());
					distributorData.setAddress_2(record.get("Address_2").trim());
					distributorData.setAddress_3(record.get("Address_3").trim());
					distributorData.setAddress_4(record.get("Address_4").trim());
					distributorData.setCountry(record.get("Country").trim());
					distributorData.setState(record.get("State").trim());
					distributorData.setCity(record.get("City").trim());
					distributorData.setPinCode(record.get("Pin_Code").trim());
					distributorData.setDistributorGstn(record.get("Distributor_GSTN").trim());
					distributorData.setDistributorPan(record.get("Distributor_PAN").trim());
					distributorData.setLicenceNo(record.get("Licence_No").trim());
					distributorData.setRegion(record.get("Region").trim());
					distributorData.setArea(record.get("Area").trim());
					distributorData.setTerritory(record.get("Territory").trim());
					distributorData.setPositions(record.get("Position_Code").trim());
					distributorData.setPrimaryMobile(record.get("Phone").trim());
					distributorData.setEmail(record.get("Email").trim());
					distributorData.setStatus(record.get("Status").trim());
					// Not in Excel
					distributorData.setOrgId("1");

					// check record empty or null
					if (record.isMapped("Distributor_code")) {
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Distributor_code").trim());
					} else {
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get(0).trim());
					}
					// failedRecordFlag =
					// Validation.isStrNullOrEmpty(record.get("Distributor_code").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Distributor_Type").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Dis_Code").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Distributor_Name").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Proprietor_Name").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Status").trim());
					// failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Phone"));

					if (failedRecordFlag == true) {
						distributorData.setMobileInsertUpdate("FAILED");
						distributorData.setMobileStatus("FAILED");
						distributorData.setMobileRemarks("DATA IS EMPTY");
						distributorsFailedList.add(distributorData);
					} else {
						distributorsSList.add(distributorData);
					}

				}
			}

		} catch (Exception e) {
			logger.error("Error Description Read Distributor CSVFile", e);
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			e.printStackTrace();
			return ltJobLogs;
		}

		parser.close();

		System.out.println("List Size :: " + distributorsSList.size());

		try {
			int count = 1;
			for (Iterator<LtMastDistributors> iterator = distributorsSList.iterator(); iterator.hasNext();) {

				LtMastDistributors ltMastDistributors = (LtMastDistributors) iterator.next();
				LtMastDistributors DistributorsObj = null;

				if (ltMastDistributors.getDistributorCrmCode() != null) {
					DistributorsObj = ltReadCSVFilesDao
							.checkDistributorCode(ltMastDistributors.getDistributorCrmCode());
				}
				System.out.println("Distributor Save====>" + count++);

				if (DistributorsObj != null) {
					// Update existing data
					ltMastDistributors.setDistributorId(DistributorsObj.getDistributorId());
					ltMastDistributors.setCreationDate(DistributorsObj.getCreationDate());
					ltMastDistributors.setLastUpdateDate(Validation.getCurrentDateTime());
					ltMastDistributors.setCreatedBy((long) 100);
					ltMastDistributors.setLastUpdateLogin((long) 100);
					ltMastDistributors.setLastUpdatedBy((long) 100);
					// distributorsRepository.save(ltMastDistributors);
					ltReadCSVFilesDao.saveDistributor(ltMastDistributors);
					ltMastDistributors.setMobileInsertUpdate("UPDATE");
					ltMastDistributors.setMobileStatus("SUCCESS");
					ltMastDistributors.setMobileRemarks("DISTRIBUTOR UPDATED");
					distributorsSuccessList.add(ltMastDistributors);

				} else {
					// Insert into outlet
					ltMastDistributors.setCreationDate(Validation.getCurrentDateTime());
					ltMastDistributors.setLastUpdateDate(Validation.getCurrentDateTime());
					ltMastDistributors.setCreatedBy((long) 100);
					ltMastDistributors.setLastUpdateLogin((long) 100);
					ltMastDistributors.setLastUpdatedBy((long) 100);
					ltReadCSVFilesDao.saveDistributor(ltMastDistributors);
					ltMastDistributors.setMobileInsertUpdate("INSERT");
					ltMastDistributors.setMobileStatus("SUCCESS");
					ltMastDistributors.setMobileRemarks("DISTRIBUTOR INSERT");
					distributorsSuccessList.add(ltMastDistributors);

				}
			}

		} catch (Exception e) {
			logger.error("Error Description Read Distributor CSVFile", e);
			e.printStackTrace();
		}

		// Create CSV File Here
		distributorsSuccessList.addAll(distributorsFailedList);

		boolean fileCreate = CreateCSVFile.createDistributorCSVFile(distributorsSuccessList, ltJobLogs);
		if (fileCreate) {
			ltJobLogs.setSuccessRecord(Long.valueOf(distributorsSuccessList.size() - distributorsFailedList.size()));
			ltJobLogs.setFailedRecord(Long.valueOf(distributorsFailedList.size()));
			ltJobLogs.setTotalRecord(Long.valueOf(distributorsSList.size() + distributorsFailedList.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			System.out.println("file created");
		}

		if (!distributorsSuccessList.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.saveDistributorCSVBk(distributorsSuccessList);
		}

		return ltJobLogs;
	}

	@Transactional
	public LtJobLogs readPositionCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		logger.info("Input Read Outlet", ltJobLogs);

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		Map<String, LtMastDistributors> distributorMap = ltReadCSVFilesDao.getAllDistributor();

		if (distributorMap.isEmpty()) {
			return ltJobLogs;
		}

		List<LtMastPositions> ltMastPositionsList = new ArrayList<LtMastPositions>();
		List<LtMastPositions> positionsFailedList = new ArrayList<LtMastPositions>();
		List<LtMastPositions> positionsSuccessList = new ArrayList<LtMastPositions>();
		boolean failedRecordFlag = false;

		try {
			for (CSVRecord record : parser) {
				if (record.size() >= parser.getHeaderMap().size()) {
					LtMastPositions positionsData = new LtMastPositions();
					if (record.isMapped("Distibutor_Code")) {
						if (distributorMap.containsKey(record.get("Distibutor_Code").trim())) {

							System.out.println(record.get("Distibutor_Code").trim());
							//
							positionsData.setDistributorId(
									distributorMap.get(record.get("Distibutor_Code").trim()).getDistributorId());
							positionsData.setPosition(record.get("Position").trim());
							positionsData.setPositionCode(record.get("Position_Code").trim());
							positionsData.setParentPosition(record.get("Parent_Position").trim());
							positionsData.setPositionType(record.get("Position_Type").trim());
							positionsData.setFirstName(record.get("First_Name").trim());
							positionsData.setLastName(record.get("Last_Name").trim());
							positionsData.setJobTitle(record.get("Job_Title").trim());
							positionsData.setStartDate(record.get("Start_Date").trim());
							positionsData.setEndDate(record.get("End_Date").trim());
							positionsData.setDistributorCode(record.get("Distibutor_Code").trim());
							// Not in excel
							positionsData.setOrgId(Long.parseLong("1"));
							positionsData.setStatus(ACTIVE);

							// Validate record empty or null
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Position_Code").trim());
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Position").trim());
							if (failedRecordFlag == true) {
								positionsData.setMobileInsertUpdate("FAILED");
								positionsData.setMobileStatus("FAILED");
								positionsData.setMobileRemarks("DATA IS EMPTY");
								positionsFailedList.add(positionsData);
							} else {
								ltMastPositionsList.add(positionsData);
							}

						} else {
							positionsData.setMobileRemarks("Distributor Code Failed");
							positionsData.setMobileStatus("Failed");
							positionsData.setMobileInsertUpdate("FAILED");
							positionsData = CreateCSVFile.setPositionDto(positionsData, record);
							positionsFailedList.add(positionsData);

						}
					} else {
						if (distributorMap.containsKey(record.get(0).trim())) {

							// System.out.println(record.get("Distibutor_Code").trim());
							//
							positionsData.setDistributorId(distributorMap.get(record.get(0).trim()).getDistributorId());
							positionsData.setPosition(record.get("Position").trim());
							positionsData.setPositionCode(record.get("Position_Code").trim());
							positionsData.setParentPosition(record.get("Parent_Position").trim());
							positionsData.setPositionType(record.get("Position_Type").trim());
							positionsData.setFirstName(record.get("First_Name").trim());
							positionsData.setLastName(record.get("Last_Name").trim());
							positionsData.setJobTitle(record.get("Job_Title").trim());
							positionsData.setStartDate(record.get("Start_Date").trim());
							positionsData.setEndDate(record.get("End_Date").trim());
							positionsData.setDistributorCode(record.get(0).trim());
							// Not in excel
							positionsData.setOrgId(Long.parseLong("1"));
							positionsData.setStatus(ACTIVE);

							// Validate record empty or null
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Position_Code").trim());
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Position").trim());
							if (failedRecordFlag == true) {
								positionsData.setMobileInsertUpdate("FAILED");
								positionsData.setMobileStatus("FAILED");
								positionsData.setMobileRemarks("DATA IS EMPTY");
								positionsFailedList.add(positionsData);
							} else {
								ltMastPositionsList.add(positionsData);
							}

						} else {
							positionsData.setMobileRemarks("Distributor Code Failed");
							positionsData.setMobileStatus("Failed");
							positionsData.setMobileInsertUpdate("FAILED");
							positionsData = CreateCSVFile.setPositionDto(positionsData, record);
							positionsFailedList.add(positionsData);

						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error Description Read Position CSVFile", e);
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			return ltJobLogs;
		}

		parser.close();
		System.out.println("List Size :: " + ltMastPositionsList.size());
		int count = 1;
		// Insert outlet data into DB
		for (Iterator<LtMastPositions> iterator = ltMastPositionsList.iterator(); iterator.hasNext();) {

			LtMastPositions ltMastPositions = (LtMastPositions) iterator.next();
			LtMastPositions ltMastPositionsObj = null;

			System.out.println("ltMastPositions Count===>" + count++);

			if (ltMastPositions.getPositionCode() != null) {
				ltMastPositionsObj = ltReadCSVFilesDao.checkPosition(ltMastPositions.getPositionCode());
			}

			if (ltMastPositionsObj != null) {
				// Update existing data
				ltMastPositions.setPositionId(ltMastPositionsObj.getPositionId());
				ltMastPositions.setCreationDate(ltMastPositionsObj.getCreationDate());
				ltMastPositions.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastPositions.setCreatedBy((long) 100);
				ltMastPositions.setLastUpdateLogin((long) 100);
				ltMastPositions.setLastUpdatedBy((long) 100);

				// ltMastPositionsRepository.save(ltMastPositions);
				ltReadCSVFilesDao.savePosition(ltMastPositions);

				ltMastPositions.setMobileRemarks("Position updated successfully");
				ltMastPositions.setMobileStatus("SUCCESS");
				ltMastPositions.setMobileInsertUpdate("UPDATE");
				positionsSuccessList.add(ltMastPositions);

			} else {
				// Insert into outlet
				ltMastPositions.setCreationDate(Validation.getCurrentDateTime());
				ltMastPositions.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastPositions.setCreatedBy((long) 100);
				ltMastPositions.setLastUpdateLogin((long) 100);
				ltMastPositions.setLastUpdatedBy((long) 100);
				ltReadCSVFilesDao.savePosition(ltMastPositions);
				ltMastPositions.setMobileRemarks("Position insert successfully");
				ltMastPositions.setMobileStatus("SUCCESS");
				ltMastPositions.setMobileInsertUpdate("INSERT");
				positionsSuccessList.add(ltMastPositions);

			}
		}

		int successRecord = positionsSuccessList.size();
		positionsSuccessList.addAll(positionsFailedList);

		boolean createCSVFile = CreateCSVFile.createPositionCSVFile(positionsSuccessList, ltJobLogs);

		if (createCSVFile) {
			ltJobLogs.setSuccessRecord(Long.valueOf(successRecord));
			ltJobLogs.setTotalRecord(Long.valueOf(positionsSuccessList.size()));
			ltJobLogs.setFailedRecord(Long.valueOf(positionsFailedList.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			System.out.println("file created");
		}

		if (!positionsSuccessList.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.savePositionCSVBk(positionsSuccessList);
		}

		return ltJobLogs;
	}

	@Transactional
	public LtJobLogs readEmployeeCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		List<LtMastEmployees> employeeSaleList = new ArrayList<LtMastEmployees>();
		List<LtMastEmployees> employeesFailedList = new ArrayList<LtMastEmployees>();
		List<LtMastEmployees> employeesSuccessList = new ArrayList<LtMastEmployees>();

		Map<String, ArrayList<LtMastEmployees>> empMap = new LinkedHashMap<String, ArrayList<LtMastEmployees>>();

		boolean failedRecordFlag = false;

		Map<String, LtMastDistributors> distributorData = ltReadCSVFilesDao.getAllDistributor();
		Map<String, LtMastPositions> possitionData = ltReadCSVFilesDao.getAllPosition();

		if (distributorData.isEmpty() && possitionData.isEmpty()) {
			return ltJobLogs;
		}

		try {

			for (CSVRecord record : parser) {
				if (record.size() >= parser.getHeaderMap().size()) {
					LtMastEmployees ltMastEmployee = new LtMastEmployees();
					if (record.isMapped("Distributor_Code")) {

						if (distributorData.containsKey(record.get("Distributor_Code").trim())) {

							if (possitionData.containsKey(record.get("Position_Code").trim())
									&& possitionData.containsKey(record.get("Primary_Position_Id").trim())) {

								ltMastEmployee.setDistributorId(
										distributorData.get(record.get("Distributor_Code").trim()).getDistributorId());
								ltMastEmployee.setPositionId(
										possitionData.get(record.get("Primary_Position_Id").trim()).getPositionId());
								ltMastEmployee.setPosition(
										possitionData.get(record.get("Primary_Position_Id").trim()).getPosition());
								ltMastEmployee.setPrimaryPositionId(record.get("Primary_Position_Id").trim());
								ltMastEmployee.setPositionCodeId(
										possitionData.get(record.get("Position_Code").trim()).getPositionId());
								ltMastEmployee.setEmployeeCode(record.get("Login").trim());
								ltMastEmployee.setFirstName(record.get("First_Name").trim());
								ltMastEmployee.setLastName(record.get("Last_Name").trim());
								ltMastEmployee.setJobTitle(record.get("Job_Title").trim());
								ltMastEmployee.setEmployeeType(record.get("Employee_Type").trim());
								ltMastEmployee.setDistributorCode(record.get("Distributor_Code").trim());
								ltMastEmployee.setPositionCode(record.get("Position_Code").trim());
								// ltMastEmployee.setPrimaryMobile(record.get("Primary_Mobile").trim());
								ltMastEmployee.setPrimaryMobile(
										CreateCSVFile.setMobileNumber(record.get("Primary_Mobile").trim()));
								ltMastEmployee.setRowNumber(record.get("Employee_Code").trim());
								ltMastEmployee.setEmail(record.get("Email").trim());
								ltMastEmployee.setParentPosition(
										possitionData.get(record.get("Position_Code").trim()).getParentPosition());
								ltMastEmployee.setStatus(record.get("Status").trim());
								// Not in excel Status
								ltMastEmployee.setOrgId(Long.valueOf("1"));
								failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Employee_Code").trim());
								if (failedRecordFlag == true) {
									ltMastEmployee.setMobileInsertUpdate("FAILED");
									ltMastEmployee.setMobileStatus("FAILED");
									ltMastEmployee.setMobileRemarks("DATA IS EMPTY");
									employeesFailedList.add(ltMastEmployee);
								} else {
									employeeSaleList.add(ltMastEmployee);
								}

							} else {
								ltMastEmployee.setMobileRemarks("Position Code Failed");
								ltMastEmployee.setMobileStatus("Failed");
								ltMastEmployee.setMobileInsertUpdate("FAILED");
								ltMastEmployee = CreateCSVFile.setEmployeeDto(ltMastEmployee, record);
								employeesFailedList.add(ltMastEmployee);
							}

						} else {
							ltMastEmployee.setMobileRemarks("Distributor Code Failed");
							ltMastEmployee.setMobileStatus("Failed");
							ltMastEmployee.setMobileInsertUpdate("FAILED");
							ltMastEmployee = CreateCSVFile.setEmployeeDto(ltMastEmployee, record);
							employeesFailedList.add(ltMastEmployee);
						}
					} else {

						if (distributorData.containsKey(record.get(0).trim())) {

							if (possitionData.containsKey(record.get("Position_Code").trim())
									&& possitionData.containsKey(record.get("Primary_Position_Id").trim())) {

								ltMastEmployee
										.setDistributorId(distributorData.get(record.get(0).trim()).getDistributorId());
								ltMastEmployee.setPositionId(
										possitionData.get(record.get("Primary_Position_Id").trim()).getPositionId());
								ltMastEmployee.setPosition(
										possitionData.get(record.get("Primary_Position_Id").trim()).getPosition());
								ltMastEmployee.setPrimaryPositionId(record.get("Primary_Position_Id").trim());
								ltMastEmployee.setPositionCodeId(
										possitionData.get(record.get("Position_Code").trim()).getPositionId());
								ltMastEmployee.setEmployeeCode(record.get("Login").trim());
								ltMastEmployee.setFirstName(record.get("First_Name").trim());
								ltMastEmployee.setLastName(record.get("Last_Name").trim());
								ltMastEmployee.setJobTitle(record.get("Job_Title").trim());
								ltMastEmployee.setEmployeeType(record.get("Employee_Type").trim());
								ltMastEmployee.setDistributorCode(record.get(0).trim());
								ltMastEmployee.setPositionCode(record.get("Position_Code").trim());
								// ltMastEmployee.setPrimaryMobile(record.get("Primary_Mobile").trim());
								ltMastEmployee.setPrimaryMobile(
										CreateCSVFile.setMobileNumber(record.get("Primary_Mobile").trim()));
								ltMastEmployee.setRowNumber(record.get("Employee_Code").trim());
								ltMastEmployee.setEmail(record.get("Email").trim());
								ltMastEmployee.setParentPosition(
										possitionData.get(record.get("Position_Code").trim()).getParentPosition());
								ltMastEmployee.setStatus(record.get("Status").trim());
								// Not in excel Status
								ltMastEmployee.setOrgId(Long.valueOf("1"));
								failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Employee_Code").trim());
								if (failedRecordFlag == true) {
									ltMastEmployee.setMobileInsertUpdate("FAILED");
									ltMastEmployee.setMobileStatus("FAILED");
									ltMastEmployee.setMobileRemarks("DATA IS EMPTY");
									employeesFailedList.add(ltMastEmployee);
								} else {
									employeeSaleList.add(ltMastEmployee);
								}

							} else {
								ltMastEmployee.setMobileRemarks("Position Code Failed");
								ltMastEmployee.setMobileStatus("Failed");
								ltMastEmployee.setMobileInsertUpdate("FAILED");
								ltMastEmployee = CreateCSVFile.setEmployeeDto(ltMastEmployee, record);
								employeesFailedList.add(ltMastEmployee);
							}

						} else {
							ltMastEmployee.setMobileRemarks("Distributor Code Failed");
							ltMastEmployee.setMobileStatus("Failed");
							ltMastEmployee.setMobileInsertUpdate("FAILED");
							ltMastEmployee = CreateCSVFile.setEmployeeDto(ltMastEmployee, record);
							employeesFailedList.add(ltMastEmployee);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Read Employee CSVFile", e);
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			return ltJobLogs;
		}
		parser.close();
		int count = 1;

		for (Iterator<LtMastEmployees> iterator = employeeSaleList.iterator(); iterator.hasNext();) {

			LtMastEmployees ltMastEmployee = (LtMastEmployees) iterator.next();
			LtMastEmployees saleObj = null;
			System.out.println("ltMastEmployee Count===>" + count++);

			if (ltMastEmployee.getEmployeeCode() != null) {

				saleObj = ltReadCSVFilesDao.checkEmployeeCode(ltMastEmployee.getEmployeeCode());

				LtMastEmployees objEmp = new LtMastEmployees();

				if (saleObj != null) {
					ltMastEmployee.setEmployeeId(saleObj.getEmployeeId());
					ltMastEmployee.setCreationDate(saleObj.getCreationDate());
					ltMastEmployee.setLastUpdateDate(Validation.getCurrentDateTime());
					ltMastEmployee.setCreatedBy((long) 100);
					ltMastEmployee.setLastUpdateLogin((long) 100);
					ltMastEmployee.setLastUpdatedBy((long) 100);
					objEmp = ltReadCSVFilesDao.saveEmployee(ltMastEmployee);
					ltMastEmployee.setMobileRemarks("Update Successfully");
					ltMastEmployee.setMobileStatus("SUCCESS");
					ltMastEmployee.setMobileInsertUpdate("UPDATE");
					employeesSuccessList.add(ltMastEmployee);

				} else {
					// Insert into Employee
					ltMastEmployee.setCreationDate(Validation.getCurrentDateTime());
					ltMastEmployee.setLastUpdateDate(Validation.getCurrentDateTime());
					ltMastEmployee.setCreatedBy((long) 100);
					ltMastEmployee.setLastUpdateLogin((long) 100);
					ltMastEmployee.setLastUpdatedBy((long) 100);
					objEmp = ltReadCSVFilesDao.saveEmployee(ltMastEmployee);
					ltMastEmployee.setMobileRemarks("Save Successfully");
					ltMastEmployee.setMobileStatus("SUCCESS");
					ltMastEmployee.setMobileInsertUpdate("INSERT");
					employeesSuccessList.add(ltMastEmployee);
					ltMastEmployee.setEmployeeId(objEmp.getEmployeeId());
				}

				if (empMap.containsKey(ltMastEmployee.getEmployeeCode())) {
					empMap.get(ltMastEmployee.getEmployeeCode()).add(ltMastEmployee);
				} else {
					ArrayList<LtMastEmployees> empList = new ArrayList<LtMastEmployees>();
					empList.add(ltMastEmployee);
					empMap.put(ltMastEmployee.getEmployeeCode(), empList);
				}
			}
		}

		// Save saveLtMastEmployeesPosition
		if (!empMap.isEmpty()) {
			saveLtMastEmployeesPosition(empMap);
		}

		int successRecord = employeesSuccessList.size();
		employeesSuccessList.addAll(employeesFailedList);
		boolean createCSVFile = CreateCSVFile.createEmployeeCSVFile(employeesSuccessList, ltJobLogs);

		if (createCSVFile) {
			ltJobLogs.setSuccessRecord(Long.valueOf(successRecord));
			ltJobLogs.setFailedRecord(Long.valueOf(employeesFailedList.size()));
			ltJobLogs.setTotalRecord(Long.valueOf(employeesSuccessList.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			System.out.println("file created");
		}

		if (!employeesSuccessList.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.saveEmployeeCSVBk(employeesSuccessList);
		}

		return ltJobLogs;
	}

	public void saveLtMastEmployeesPosition(Map<String, ArrayList<LtMastEmployees>> empMap) {

		for (Map.Entry<String, ArrayList<LtMastEmployees>> entry : empMap.entrySet())

		{

			LtMastEmployees empObj = ltReadCSVFilesDao.checkEmployeeCode(entry.getKey());

			if (empObj != null) {

				ltReadCSVFilesDao.deletEmployeesPositionById(empObj.getEmployeeId());

				ArrayList<LtMastEmployees> list = empMap.get(entry.getKey());

				if (!list.isEmpty()) {

					for (LtMastEmployees ltMastEmployee : list) {

						if (ltMastEmployee.getEmployeeId() != null && ltMastEmployee.getPositionId() != null) {
							LtMastEmployeesPosition ltMastEmployeesPosition = new LtMastEmployeesPosition();
							ltMastEmployeesPosition.setCreationDate(Validation.getCurrentDateTime());
							ltMastEmployeesPosition.setLastUpdateDate(Validation.getCurrentDateTime());
							ltMastEmployeesPosition.setCreatedBy((long) 100);
							ltMastEmployeesPosition.setLastUpdateLogin((long) 100);
							ltMastEmployeesPosition.setLastUpdatedBy((long) 100);
							ltMastEmployeesPosition.setRowNumber(ltMastEmployee.getRowNumber());
							ltMastEmployeesPosition.setStatus(ACTIVE);
							ltMastEmployeesPosition.setPositionId(ltMastEmployee.getPositionCodeId());
							ltMastEmployeesPosition.setEmployeeId(ltMastEmployee.getEmployeeId());
							ltMastEmployeesPosition.setParentPosition(ltMastEmployee.getParentPosition());
							ltReadCSVFilesDao.saveEmployeePosition(ltMastEmployeesPosition);
						}

					}
				}
			}
		}
	}

	@Transactional
	public LtJobLogs readProductCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		Map<String, LtMastProductCat> categoryMap = ltReadCSVFilesDao.getAllCategories();
		List<LtMastProducts> productList = new ArrayList<LtMastProducts>();
		List<LtMastProducts> productFailedList = new ArrayList<LtMastProducts>();
		List<LtMastProducts> productSuccessList = new ArrayList<LtMastProducts>();
		boolean failedRecordFlag = false;

		try {

			for (CSVRecord record : parser) {

				if (record.size() >= parser.getHeaderMap().size()) {

					LtMastProducts ltMastProducts = new LtMastProducts();

					if (categoryMap.containsKey(record.get("Category").trim())) {

						ltMastProducts.setCategoryId(categoryMap.get(record.get("Category").trim()).getCategoryId());

						ltMastProducts.setOrgId(Long.valueOf("1"));

						System.out.println("Product Code===>" + record.get("Product_Code").trim());
						if (record.isMapped("Product_Type")) {
							ltMastProducts.setProductType(record.get("Product_Type").trim());
						} else {
							ltMastProducts.setProductType(record.get(0).trim());

						}

						// ltMastProducts.setProductType(record.get("Product_Type").trim());
						ltMastProducts.setCategory(record.get("Category").trim());
						ltMastProducts.setSubCategory(record.get("Sub_Category").trim());
						ltMastProducts.setProductCode(record.get("Product_Code").trim());
						ltMastProducts.setProductName(record.get("Product_Desc").trim());
						ltMastProducts.setProductDesc(record.get("Product_Name").trim());
						ltMastProducts.setPrimaryUom(record.get("Primary_Uom").trim());
						ltMastProducts.setSecondaryUom(record.get("Secondary_Uom").trim());
						ltMastProducts.setSecondaryUomValue(record.get("Sec_Uom_Value").trim());
						ltMastProducts.setUnitsPerCase(record.get("Unit_Per_Case").trim());
						ltMastProducts.setSegment(record.get("Segment").trim());
						ltMastProducts.setBrand(record.get("Brand").trim());
						ltMastProducts.setSubBrand(record.get("Sub_Brand").trim());
						ltMastProducts.setStyle(record.get("Style").trim());
						ltMastProducts.setFlavor(record.get("Flavor").trim());
						ltMastProducts.setCasePack(record.get("Case_Pack").trim());
						ltMastProducts.setHsnCode(record.get("HSN_Code").trim());
						ltMastProducts.setOrderable(record.get("Orderable").trim());
						ltMastProducts.setStatus(record.get("Status").trim());
						ltMastProducts.setPtrFlag(record.get("PTR_Flag").trim());

						// Validate record empty or null
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Product_Code").trim());
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Product_Desc").trim());
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Product_Desc").trim());
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Status").trim());

						if (failedRecordFlag == true) {
							ltMastProducts.setMobileInsertUpdate("FAILED");
							ltMastProducts.setMobileStatus("FAILED");
							ltMastProducts.setMobileRemarks("DATA IS EMPTY");
							productFailedList.add(ltMastProducts);
						} else {
							productList.add(ltMastProducts);
						}

					} else {
						ltMastProducts.setMobileRemarks("Category Failed");
						ltMastProducts.setMobileStatus("Failed");
						ltMastProducts.setMobileInsertUpdate("FAILED");
						ltMastProducts = CreateCSVFile.setProductDto(ltMastProducts, record);
						productFailedList.add(ltMastProducts);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error description Read Product CSVFile", e);
			e.printStackTrace();
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			return ltJobLogs;
		}
		parser.close();
		System.out.println("Size=====>" + productList.size());

		// // Insert outlet data into DB
		ReadProductImages readProductImages = new ReadProductImages();

		FTPFile productImagesList[] = readProductImages.getAllProductImages();

		int count = 1;
		for (Iterator<LtMastProducts> iterator = productList.iterator(); iterator.hasNext();) {

			LtMastProducts ltMastProducts = (LtMastProducts) iterator.next();
			LtMastProducts productObj = null;

			if (ltMastProducts.getProductCode() != null) {
				productObj = ltReadCSVFilesDao.checkProductCode(ltMastProducts.getProductCode());
			}
			System.out.println("LtMastPrice Count===>" + count++);
			if (productObj != null) {
				// Update existing data
				ltMastProducts.setProductId(productObj.getProductId());
				ltMastProducts.setProductImage(productObj.getProductImage());
				ltMastProducts.setThumbnailImage(productObj.getThumbnailImage());
				ltMastProducts.setCreationDate(productObj.getCreationDate());
				ltMastProducts.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastProducts.setCreatedBy((long) 100);
				ltMastProducts.setLastUpdateLogin((long) 100);
				ltMastProducts.setLastUpdatedBy((long) 100);

				String productImage = readProductImages.getProductImage(ltMastProducts.getProductCode(),
						productImagesList);

				String thumbnailImage = readProductImages.getThumbnailImage(ltMastProducts.getProductCode(),
						productImagesList);

				if (productImage != null) {
					String productImageA = env.getProperty("productImagePath") + productImage;
					ltMastProducts.setProductImage(productImageA);
				}
				if (thumbnailImage != null) {
					if (productImage == null) {
						String thumbnailImageForProductA = env.getProperty("productImagePath") + thumbnailImage;
						ltMastProducts.setProductImage(thumbnailImageForProductA);
					}
					String thumbnailImageB = env.getProperty("productImagePath") + thumbnailImage;
					ltMastProducts.setThumbnailImage(thumbnailImageB);
				}

				// ltMastProductRepository.save(ltMastProducts);
				ltReadCSVFilesDao.saveProduct(ltMastProducts);
				ltMastProducts.setMobileRemarks("Update Successfully");
				ltMastProducts.setMobileStatus("SUCCESS");
				ltMastProducts.setMobileInsertUpdate("UPDATE");
				productSuccessList.add(ltMastProducts);

			} else {
				// Insert into outlet
				ltMastProducts.setCreationDate(Validation.getCurrentDateTime());
				ltMastProducts.setLastUpdateDate(Validation.getCurrentDateTime());
				ltMastProducts.setCreatedBy((long) 100);
				ltMastProducts.setLastUpdateLogin((long) 100);
				ltMastProducts.setLastUpdatedBy((long) 100);

				String productImage = readProductImages.getProductImage(ltMastProducts.getProductCode(),
						productImagesList);
				String thumbnailImage = readProductImages.getThumbnailImage(ltMastProducts.getProductCode(),
						productImagesList);

				if (productImage != null) {
					String productImageA = env.getProperty("productImagePath") + productImage;
					ltMastProducts.setProductImage(productImageA);
				}
				if (thumbnailImage != null) {
					if (productImage == null) {
						String thumbnailImageForProductA = env.getProperty("productImagePath") + thumbnailImage;
						ltMastProducts.setProductImage(thumbnailImageForProductA);
					}
					String thumbnailImageB = env.getProperty("productImagePath") + thumbnailImage;
					ltMastProducts.setThumbnailImage(thumbnailImageB);
				}
				/////////////////////////////////
				ltReadCSVFilesDao.saveProduct(ltMastProducts);
				ltMastProducts.setMobileRemarks("Insert Successfully");
				ltMastProducts.setMobileStatus("SUCCESS");
				ltMastProducts.setMobileInsertUpdate("INSERT");
				productSuccessList.add(ltMastProducts);

			}
		}

		int successRecord = productSuccessList.size();
		productSuccessList.addAll(productFailedList);

		boolean createCsv = CreateCSVFile.createProductCSVFile(productSuccessList, ltJobLogs);

		if (createCsv) {
			ltJobLogs.setSuccessRecord(Long.valueOf(successRecord));
			ltJobLogs.setFailedRecord(Long.valueOf(productFailedList.size()));
			ltJobLogs.setTotalRecord(Long.valueOf(productSuccessList.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			System.out.println("file created");
		}
		if (!productSuccessList.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.saveProductCSVBk(productSuccessList);
		}

		return ltJobLogs;
	}

	@Transactional
	public LtJobLogs readPriceListCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		List<LtMastPriceLists> ltMastPriceList = new ArrayList<LtMastPriceLists>();
		List<LtMastPriceLists> ltMastPriceFailedList = new ArrayList<LtMastPriceLists>();
		boolean failedRecordFlag = false;

		try {
			for (CSVRecord record : parser) {
				if (record.size() >= parser.getHeaderMap().size()) {
					LtMastPriceLists priceList = new LtMastPriceLists();
					if (record.isMapped("Price_List")) {
						priceList.setPriceList(record.get("Price_List").trim());
					} else {
						priceList.setPriceList(record.get(0).trim());

					}
					priceList.setPriceListDesc(record.get("Price_List_Desc").trim());
					priceList.setCurrency(record.get("Currency").trim());
					priceList.setProductCode(record.get("Product_Code").trim());
					priceList.setListPrice(record.get("List_Price").trim());
					priceList.setStartDate(record.get("Start_Date").trim());
					priceList.setEndDate(record.get("End_Date").trim());
					priceList.setPtrPrice(record.get("PTR").trim());

					// Not in Excel
					priceList.setOrgId(Long.valueOf("1"));
					priceList.setStatus(ACTIVE);

					// check record empty or null
					if (record.isMapped("Price_List")) {
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Price_List").trim());

					} else {
						failedRecordFlag = Validation.isStrNullOrEmpty(record.get(0).trim());

					}
					// failedRecordFlag =
					// Validation.isStrNullOrEmpty(record.get("Price_List").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Product_Code").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("List_Price").trim());
					failedRecordFlag = Validation.isStrNullOrEmpty(record.get("PTR").trim());

					if (failedRecordFlag == true) {
						priceList.setMobileInsertUpdate("FAILED");
						priceList.setMobileStatus("FAILED");
						priceList.setMobileRemarks("DATA IS EMPTY");
						ltMastPriceFailedList.add(priceList);
					} else {
						ltMastPriceList.add(priceList);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error description Read Price List CSVFile", e);
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			return ltJobLogs;
		}
		parser.close();
		System.out.println("List Size :: " + ltMastPriceList.size());

		List<LtMastPriceLists> priceListCSV = new ArrayList<LtMastPriceLists>();
		int count = 1;

		for (Iterator<LtMastPriceLists> iterator = ltMastPriceList.iterator(); iterator.hasNext();) {

			LtMastPriceLists priceList = (LtMastPriceLists) iterator.next();
			LtMastPriceLists priceListObj = null;
			if (priceList.getProductCode() != null) {
				priceListObj = ltReadCSVFilesDao.checkPriceListsCode(priceList.getProductCode(),
						priceList.getPriceList());
			}

			System.out.println("LtMastPrice Count===>" + count++);

			if (priceListObj != null) {
				// Update existing data
				priceList.setPriceListId(priceListObj.getPriceListId());
				priceList.setCreationDate(priceListObj.getCreationDate());
				priceList.setLastUpdateDate(Validation.getCurrentDateTime());
				priceList.setCreatedBy((long) 100);
				priceList.setLastUpdateLogin((long) 100);
				priceList.setLastUpdatedBy((long) 100);
				// ltMastPriceListRepository.save(priceList);
				ltReadCSVFilesDao.savePriceList(priceList);

				priceList.setMobileInsertUpdate("UPDATE");
				priceList.setMobileRemarks("Record updated");
				priceList.setMobileStatus("Success");
				priceListCSV.add(priceList);

			} else {
				// Insert into outlet
				priceList.setCreationDate(Validation.getCurrentDateTime());
				priceList.setLastUpdateDate(Validation.getCurrentDateTime());
				priceList.setCreatedBy((long) 100);
				priceList.setLastUpdateLogin((long) 100);
				priceList.setLastUpdatedBy((long) 100);
				ltReadCSVFilesDao.savePriceList(priceList);
				priceList.setMobileInsertUpdate("INSERT");
				priceList.setMobileRemarks("Record inserted");
				priceList.setMobileStatus("Success");
				priceListCSV.add(priceList);

			}
		}

		int successRecord = priceListCSV.size();
		priceListCSV.addAll(ltMastPriceFailedList);
		boolean createCSVFlag = CreateCSVFile.createPriceListCSVFile(priceListCSV, ltJobLogs);
		if (createCSVFlag) {
			ltJobLogs.setSuccessRecord(Long.valueOf(successRecord));
			ltJobLogs.setFailedRecord(Long.valueOf(ltMastPriceFailedList.size()));
			ltJobLogs.setTotalRecord(Long.valueOf(priceListCSV.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			System.out.println("file created");

		}

		if (!priceListCSV.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.savePriceListCSVBk(priceListCSV);
		}
		return ltJobLogs;
	}

	@Override
	public LtJobLogs readInventoryCSVFile(LtJobLogs ltJobLogs)
			throws IOException, FileNotFoundException, ServiceException {

		String filePath = InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
				+ ltJobLogs.getFileName().toString();

		CSVParser parser = new CSVParser(new FileReader(filePath),
				CSVFormat.DEFAULT.withHeader().withDelimiter('|').withQuote(null).withTrim());

		Map<String, LtMastDistributors> distributorData = ltReadCSVFilesDao.getAllDistributor();

		Map<String, LtMastProducts> productData = ltReadCSVFilesDao.getAllProduct();

		if (distributorData.isEmpty() && productData.isEmpty()) {
			return ltJobLogs;
		}

		List<LtMastInventory> inventoryList = new ArrayList<LtMastInventory>();
		List<LtMastInventory> inventorySuccessList = new ArrayList<LtMastInventory>();
		List<LtMastInventory> inventoryFailedList = new ArrayList<LtMastInventory>();

		boolean failedRecordFlag = false;

		try {
			int count = 1;
			for (CSVRecord record : parser) {

				if (record.size() >= parser.getHeaderMap().size()) {

					LtMastInventory inventoryData = new LtMastInventory();
					System.out.println("Inv count==>" + count++);
					inventoryData.setDistCode(record.get("Inventory_Name"));
					if (distributorData.containsKey(record.get("Dist_Code").trim())) {

						if (productData.containsKey(record.get("Prod_RID").trim())) {

							inventoryData
									.setDistId(distributorData.get(record.get("Dist_Code").trim()).getDistributorId());
							inventoryData.setDistCode(record.get("Dist_Code").trim());

							if (record.isMapped("Inventory_Code")) {
								inventoryData.setInventoryCode(record.get("Inventory_Code").trim());

							} else {
								inventoryData.setInventoryCode(record.get(0).trim());

							}
							// inventoryData.setInventoryCode(record.get("Inventory_Code").trim());
							inventoryData.setInventoryName(record.get("Inventory_Name").trim());
							inventoryData.setInventoryStatus(record.get("Inventory_Status").trim());

							inventoryData.setProductId(productData.get(record.get("Prod_RID").trim()).getProductId());
							inventoryData.setProductRid(record.get("Product_Code").trim());
							inventoryData.setProductCode(record.get("Prod_RID").trim());
							inventoryData.setQuantity(record.get("Quantity").trim());

							// check record empty or null
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Prod_RID").trim());
							failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Dist_Code").trim());
							// failedRecordFlag =
							// Validation.isStrNullOrEmpty(record.get("Inventory_Code").trim());
							if (record.isMapped("Inventory_Code")) {
								failedRecordFlag = Validation.isStrNullOrEmpty(record.get("Inventory_Code").trim());

							} else {
								failedRecordFlag = Validation.isStrNullOrEmpty(record.get(0).trim());

							}

							if (failedRecordFlag == true) {
								inventoryData.setMobileInsertUpdate("FAILED");
								inventoryData.setMobileStatus("FAILED");
								inventoryData.setMobileRemarks("DATA IS EMPTY");
								inventoryFailedList.add(inventoryData);
							} else {
								inventoryList.add(inventoryData);
							}

						} else {
							inventoryData.setMobileRemarks("Product Code Failed");
							inventoryData.setMobileStatus("Failed");
							inventoryData = CreateCSVFile.setInventoryDto(inventoryData, record);
							inventoryFailedList.add(inventoryData);
						}

					} else {
						inventoryData.setMobileRemarks("Distributor Code Failed");
						inventoryData.setMobileStatus("Failed");
						inventoryData = CreateCSVFile.setInventoryDto(inventoryData, record);
						inventoryFailedList.add(inventoryData);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Description Read Outlet CSVFile", e);
			ltJobLogs.setLogsStatus("Excel file failed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			return ltJobLogs;
		}

		parser.close();

		System.out.println("List Size :: " + inventoryList.size());

		int count = 1;
		for (Iterator<LtMastInventory> iterator = inventoryList.iterator(); iterator.hasNext();) {

			LtMastInventory ltMastInventory = (LtMastInventory) iterator.next();

			if (ltMastInventory.getInventoryCode() != null && ltMastInventory.getDistCode() != null
					&& ltMastInventory.getProductCode() != null) {

				LtMastInventory ltMastInventoryThree = ltReadCSVFilesDao.checkInvCodeDisCodeProdCode(
						ltMastInventory.getInventoryCode(), ltMastInventory.getDistCode(),
						ltMastInventory.getProductCode());

				System.out.println("Inv Data Added ==>" + count++);

				if (ltMastInventoryThree == null) {
					ltMastInventory.setCreationDate(Validation.getCurrentDateTime());
					ltMastInventory.setLastUpdateDate(Validation.getCurrentDateTime());
					ltMastInventory.setCreatedBy((long) 100);
					ltMastInventory.setLastUpdateLogin((long) 100);
					ltMastInventory.setLastUpdatedBy((long) 100);
					ltReadCSVFilesDao.saveInventory(ltMastInventory);
					ltMastInventory.setMobileInsertUpdate("INSERT");
					ltMastInventory.setMobileRemarks("Record inserted");
					ltMastInventory.setMobileStatus("Success");
					inventorySuccessList.add(ltMastInventory);
				} else {
					ltMastInventory.setInventoryId(ltMastInventoryThree.getInventoryId());
					ltMastInventory.setCreationDate(ltMastInventoryThree.getCreationDate());
					ltMastInventory.setLastUpdateDate(Validation.getCurrentDateTime());
					ltMastInventory.setCreatedBy((long) 100);
					ltMastInventory.setLastUpdateLogin((long) 100);
					ltMastInventory.setLastUpdatedBy((long) 100);
					ltReadCSVFilesDao.saveInventory(ltMastInventory);
					ltMastInventory.setMobileInsertUpdate("UPDATE");
					ltMastInventory.setMobileRemarks("Record Updated");
					ltMastInventory.setMobileStatus("Success");
					inventorySuccessList.add(ltMastInventory);
				}

			}

		}

		// Create CSV File Here
		int successRecord = inventorySuccessList.size();
		inventorySuccessList.addAll(inventoryFailedList);

		boolean fileCreate = CreateCSVFile.createInventoryCSVFile(inventorySuccessList, ltJobLogs);
		if (fileCreate) {
			ltJobLogs.setSuccessRecord(Long.valueOf(successRecord));
			ltJobLogs.setFailedRecord(Long.valueOf(inventoryFailedList.size()));
			ltJobLogs.setTotalRecord(Long.valueOf(inventorySuccessList.size()));
			ltJobLogs.setLogsStatus("Completed");
			ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
		}

		if (!inventorySuccessList.isEmpty()) {
			boolean flag = ltJobeServiceCsvBk.saveInventoryCSVBk(inventorySuccessList);
		}

		return ltJobLogs;
	}
}
