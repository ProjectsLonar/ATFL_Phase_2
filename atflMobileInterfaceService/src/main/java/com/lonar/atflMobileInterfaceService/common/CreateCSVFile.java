package com.lonar.atflMobileInterfaceService.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.lonar.atflMobileInterfaceService.dto.DistributorsDto;
import com.lonar.atflMobileInterfaceService.ftp.FTPUtil;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtMastDistributors;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployees;
import com.lonar.atflMobileInterfaceService.model.LtMastInventory;
import com.lonar.atflMobileInterfaceService.model.LtMastOrder;
import com.lonar.atflMobileInterfaceService.model.LtMastOutlets;
import com.lonar.atflMobileInterfaceService.model.LtMastPositions;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceLists;
import com.lonar.atflMobileInterfaceService.model.LtMastProducts;
import com.lonar.atflMobileInterfaceService.model.OrderOutDto;
import com.lonar.atflMobileInterfaceService.thread.InitiateThread;

public class CreateCSVFile {

	public static boolean createInventoryCSVFile(List<LtMastInventory> inventoryList, LtJobLogs ltJobLogs) {

		boolean createCSVFlag = false;
		try {

			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();
			if (headerList.isEmpty()) {

				headerList.add("Inventory_Code");
				headerList.add("Inventory_Name");
				headerList.add("Dist_Code");
				headerList.add("Inventory_Status");
				headerList.add("Prod_RID");
				headerList.add("Prod_Code");
				headerList.add("Quantity");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				String headerArray = Validation.isCreatePipeSeparator(headerList);
				csv.writeln(headerArray);

				for (LtMastInventory inventorytDto : inventoryList) {
					List<String> list = new ArrayList<>();
					list.add(inventorytDto.getInventoryCode());
					list.add(inventorytDto.getInventoryName());
					list.add(inventorytDto.getDistCode());
					list.add(inventorytDto.getInventoryStatus());
					list.add(inventorytDto.getProductRid());
					list.add(inventorytDto.getProductCode());
					list.add(inventorytDto.getQuantity());
					list.add(inventorytDto.getMobileStatus());
					list.add(inventorytDto.getMobileRemarks());
					list.add(inventorytDto.getMobileInsertUpdate());

					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);
				}
				csv.close();
				writer.flush();
				writer.close();
				System.out.println("File Outlet Created Successfully");
				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {

					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static boolean createOutletCSVFile(List<LtMastOutlets> outletList, LtJobLogs ltJobLogs) {

		boolean createCSVFlag = false;
		try {

			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();
			if (headerList.isEmpty()) {

				headerList.add("Distributor_Code");
				headerList.add("Outlet_code");
				headerList.add("Outlet_Type");
				headerList.add("Outlet_Name");
				headerList.add("Proprietor_Name");
				headerList.add("Address_1");
				headerList.add("Address_2");
				headerList.add("Address_3");
				headerList.add("Address_4");
				headerList.add("Country");
				headerList.add("State");
				headerList.add("City");
				headerList.add("Pin_Code");
				headerList.add("Region");
				headerList.add("Area");
				headerList.add("Territory");
				headerList.add("Outlet_GSTN");
				headerList.add("Outlet_PAN");
				headerList.add("Licence_No");
				headerList.add("Position_Code");
				headerList.add("Phone");
				headerList.add("Email");
				headerList.add("Price_List");
				headerList.add("Status");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				String headerArray = Validation.isCreatePipeSeparator(headerList);
				csv.writeln(headerArray);

				for (LtMastOutlets outletDto : outletList) {
					List<String> list = new ArrayList<>();
					list.add(outletDto.getDistributorCode());
					list.add(outletDto.getOutletCode());
					list.add(outletDto.getOutletType());
					list.add(outletDto.getOutletName());
					list.add(outletDto.getProprietorName());
					list.add(outletDto.getAddress1());
					list.add(outletDto.getAddress2());
					list.add(outletDto.getAddress3());
					list.add(outletDto.getAddress4());
					list.add(outletDto.getCountry());
					list.add(outletDto.getState());
					list.add(outletDto.getCity());
					list.add(outletDto.getPin_code());
					list.add(outletDto.getRegion());
					list.add(outletDto.getArea());
					list.add(outletDto.getTerritory());
					list.add(outletDto.getOutletGstn());
					list.add(outletDto.getOutletPan());
					list.add(outletDto.getLicenceNo());
					list.add(outletDto.getPositionCode());
					list.add(outletDto.getPhone());
					list.add(outletDto.getEmail());
					list.add(outletDto.getPriceList());
					list.add(outletDto.getStatus());
					list.add(outletDto.getMobileStatus());
					list.add(outletDto.getMobileRemarks());
					list.add(outletDto.getMobileInsertUpdate());
					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);
				}
				csv.close();
				writer.flush();
				writer.close();
				System.out.println("File Outlet Created Successfully");
				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {

					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static LtMastOutlets setOutletDto(LtMastOutlets outletDto, CSVRecord record) {
		outletDto.setDistributorCode(record.get("Distributor_Code").trim());
		outletDto.setPositionCode(record.get("Position_Code").trim());
		if (record.isMapped("Outlet_code")) {
			outletDto.setOutletCode(record.get("Outlet_code").trim());

		} else {
			outletDto.setOutletCode(record.get(0).trim());

		}
		// outletDto.setOutletCode(record.get("Outlet_code").trim());
		outletDto.setOutletType(record.get("Outlet_Type").trim());
		outletDto.setOutletName(record.get("Outlet_Name").trim());
		outletDto.setProprietorName(record.get("Proprietor_Name").trim());
		outletDto.setAddress1(record.get("Address_1").trim());
		outletDto.setAddress2(record.get("Address_2").trim());
		outletDto.setAddress3(record.get("Address_3").trim());
		outletDto.setAddress4(record.get("Address_4").trim());
		outletDto.setCountry(record.get("Country").trim());
		outletDto.setState(record.get("State").trim());
		outletDto.setCity(record.get("City").trim());
		outletDto.setPin_code(record.get("Pin_Code").trim());
		outletDto.setOutletGstn(record.get("Outlet_GSTN").trim());
		outletDto.setOutletPan(record.get("Outlet_PAN").trim());
		outletDto.setLicenceNo(record.get("Licence_No").trim());
		outletDto.setRegion(record.get("Region").trim());
		outletDto.setArea(record.get("Area").trim());
		outletDto.setTerritory(record.get("Territory").trim());
		outletDto.setPrimaryMobile(record.get("Phone").trim());
		outletDto.setEmail(record.get("Email").trim());
		outletDto.setStatus(record.get("Status").trim());
		outletDto.setPriceList(record.get("Price_List").trim());

		return outletDto;
	}

	public static boolean createDistributorCSVFile(List<LtMastDistributors> distributorsList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {

			FTPUtil ftpUtil = new FTPUtil();

			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();
			FileWriter writer = new FileWriter(csvFile);

			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();

			if (headerList.isEmpty()) {

				headerList.add("Distributor_code");
				headerList.add("Distributor_Type");
				headerList.add("Dis_Code");
				headerList.add("Distributor_Name");
				headerList.add("Proprietor_Name");
				headerList.add("Address_1");
				headerList.add("Address_2");
				headerList.add("Address_3");
				headerList.add("Address_4");
				headerList.add("Country");
				headerList.add("State");
				headerList.add("City");
				headerList.add("Pin_Code");
				headerList.add("Region");
				headerList.add("Area");
				headerList.add("Territory");
				headerList.add("Distributor_GSTN");
				headerList.add("Distributor_PAN");
				headerList.add("Licence_No");
				headerList.add("Position_Code");
				headerList.add("Phone");
				headerList.add("Email");
				headerList.add("Status");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				// CSVUtils.writeLine(writer, headerList);
				// String[] headerArray = headerList.toArray(new String[headerList.size()]);
				String headerArray = Validation.isCreatePipeSeparator(headerList);

				csv.writeln(headerArray);

				for (LtMastDistributors distributorsDto : distributorsList) {

					List<String> list = new ArrayList<>();
					list.add(distributorsDto.getDistributorCode());
					list.add(distributorsDto.getDistributorType());
					list.add(distributorsDto.getDistributorCrmCode());
					list.add(distributorsDto.getDistributorName());
					list.add(distributorsDto.getProprietorName());
					list.add(distributorsDto.getAddress_1());
					list.add(distributorsDto.getAddress_2());
					list.add(distributorsDto.getAddress_3());
					list.add(distributorsDto.getAddress_4());
					list.add(distributorsDto.getCountry());
					list.add(distributorsDto.getState());
					list.add(distributorsDto.getCity());
					list.add(distributorsDto.getPinCode());
					list.add(distributorsDto.getRegion());
					list.add(distributorsDto.getArea());
					list.add(distributorsDto.getTerritory());
					list.add(distributorsDto.getDistributorGstn());
					list.add(distributorsDto.getDistributorPan());
					list.add(distributorsDto.getLicenceNo());
					list.add(distributorsDto.getPositions());
					list.add(distributorsDto.getPrimaryMobile());
					list.add(distributorsDto.getEmail());
					list.add(distributorsDto.getStatus());
					list.add(distributorsDto.getMobileStatus());
					list.add(distributorsDto.getMobileRemarks());
					list.add(distributorsDto.getMobileInsertUpdate());
					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);

				}
				csv.close();
				writer.flush();
				writer.close();
				System.out.println("File Distributor Created Successfully");

				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {
					// DELETE FILE HERE
					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static DistributorsDto setDistributorDto11(DistributorsDto distributorsDto, CSVRecord record) {

		if (record.isMapped("Distributor_code")) {
			distributorsDto.setDistributorCode(record.get("Distributor_code").trim());
		} else {
			distributorsDto.setDistributorCode(record.get(0).trim());
		}
		distributorsDto.setDistributorCode(record.get("Distributor_code").trim());
		distributorsDto.setDistributorType(record.get("Distributor_Type").trim());
		distributorsDto.setDistributorCrmCode(record.get("Dis_Code").trim());
		distributorsDto.setDistributorName(record.get("Distributor_Name").trim());
		distributorsDto.setProprietorName(record.get("Proprietor_Name").trim());
		distributorsDto.setAddress_1(record.get("Address_1").trim());
		distributorsDto.setAddress_2(record.get("Address_2").trim());
		distributorsDto.setAddress_3(record.get("Address_3").trim());
		distributorsDto.setAddress_4(record.get("Address_4").trim());
		distributorsDto.setCountry(record.get("Country").trim());
		distributorsDto.setState(record.get("State").trim());
		distributorsDto.setCity(record.get("City").trim());
		distributorsDto.setPinCode(record.get("Pin_Code").trim());
		distributorsDto.setDistributorGstn(record.get("Distributor_GSTN").trim());
		distributorsDto.setDistributorPan(record.get("Distributor_PAN").trim());
		distributorsDto.setLicenceNo(record.get("Licence_No").trim());
		distributorsDto.setRegion(record.get("Region").trim());
		distributorsDto.setArea(record.get("Area").trim());
		distributorsDto.setTerritory(record.get("Territory").trim());
		distributorsDto.setPositions(record.get("Position_Code").trim());
		distributorsDto.setPrimaryMobile(record.get("Phone").trim());
		distributorsDto.setEmail(record.get("Email").trim());
		distributorsDto.setStatus(record.get("Status").trim());

		return distributorsDto;
	}

	public static boolean createPositionCSVFile(List<LtMastPositions> PositionsList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {
			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();

			if (headerList.isEmpty()) {
				headerList.add("Distibutor_Code");
				headerList.add("Position_Code");
				headerList.add("Position");
				headerList.add("Parent_Position");
				headerList.add("Position_Type");
				headerList.add("First_Name");
				headerList.add("Last_Name");
				headerList.add("Job_Title");
				headerList.add("Start_Date");
				headerList.add("End_Date");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				String headerArray = Validation.isCreatePipeSeparator(headerList);
				csv.writeln(headerArray);
				for (LtMastPositions positionsDto : PositionsList) {

					List<String> list = new ArrayList<>();

					list.add(positionsDto.getDistributorCode());
					list.add(positionsDto.getPositionCode());
					list.add(positionsDto.getPosition());
					list.add(positionsDto.getParentPosition());
					list.add(positionsDto.getPositionType());
					list.add(positionsDto.getFirstName());
					list.add(positionsDto.getLastName());
					list.add(positionsDto.getJobTitle());
					list.add(positionsDto.getStartDate());
					list.add(positionsDto.getEndDate());
					list.add(positionsDto.getMobileStatus());
					list.add(positionsDto.getMobileRemarks());
					list.add(positionsDto.getMobileInsertUpdate());
					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);

				}
				csv.close();
				writer.flush();
				writer.close();
				System.out.println("File Position Created Successfully");
				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {
					// DELETE FILE HERE
					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static LtMastPositions setPositionDto(LtMastPositions positionsDto, CSVRecord record) {

		if(record.isMapped("Distibutor_Code"))
		{positionsDto.setDistributorCode(record.get("Distibutor_Code").trim());
		}
		else
		{
			positionsDto.setDistributorCode(record.get(0).trim());
			
		}
		positionsDto.setPosition(record.get("Position").trim());
		positionsDto.setPositionCode(record.get("Position_Code").trim());
		positionsDto.setParentPosition(record.get("Parent_Position").trim());
		positionsDto.setPositionType(record.get("Position_Type").trim());
		positionsDto.setFirstName(record.get("First_Name").trim());
		positionsDto.setLastName(record.get("Last_Name").trim());
		positionsDto.setJobTitle(record.get("Job_Title").trim());
		positionsDto.setStartDate(record.get("Start_Date").trim());
		positionsDto.setEndDate(record.get("End_Date").trim());

		return positionsDto;
	}

	public static boolean createEmployeeCSVFile(List<LtMastEmployees> employeeList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {

			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();

			if (headerList.isEmpty()) {

				headerList.add("Distributor_Code");
				headerList.add("Position_Code");
				headerList.add("Employee_Code");
				headerList.add("Login");
				headerList.add("First_Name");
				headerList.add("Last_Name");
				headerList.add("Job_Title");
				headerList.add("Employee_Type");

				headerList.add("Primary_Mobile");
				// headerList.add("Row_Number");
				headerList.add("Email");
				headerList.add("Primary_Position_Id");
				headerList.add("Status");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				String headerArray = Validation.isCreatePipeSeparator(headerList);
				csv.writeln(headerArray);

				for (LtMastEmployees employeesDto : employeeList) {

					List<String> list = new ArrayList<>();

					list.add(employeesDto.getDistributorCode());
					list.add(employeesDto.getPositionCode());
					list.add(employeesDto.getRowNumber());
					list.add(employeesDto.getEmployeeCode());
					list.add(employeesDto.getFirstName());
					list.add(employeesDto.getLastName());
					list.add(employeesDto.getJobTitle());
					list.add(employeesDto.getEmployeeType());
					list.add(employeesDto.getPrimaryMobile());
					// list.add(employeesDto.getRowNumber());
					list.add(employeesDto.getEmail());
					list.add(employeesDto.getPrimaryPositionId());
					list.add(employeesDto.getStatus());
					list.add(employeesDto.getMobileStatus());
					list.add(employeesDto.getMobileRemarks());
					list.add(employeesDto.getMobileInsertUpdate());

					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);

				}
				csv.close();
				writer.flush();
				writer.close();
				System.out.println("File Employee Created Successfully");
				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {

					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static LtMastEmployees setEmployeeDto(LtMastEmployees employeesDto, CSVRecord record) {
		if (record.isMapped("Distributor_Code")) {
			employeesDto.setDistributorCode(record.get("Distributor_Code"));
		} else {
			employeesDto.setDistributorCode(record.get(0));
		}
		employeesDto.setPositionCode(record.get("Position_Code"));
		employeesDto.setEmployeeCode(record.get("Login"));
		employeesDto.setRowNumber("Employee_Code");
		employeesDto.setFirstName(record.get("First_Name"));
		employeesDto.setLastName(record.get("Last_Name"));
		employeesDto.setJobTitle(record.get("Job_Title"));
		employeesDto.setEmployeeType(record.get("Employee_Type"));
		employeesDto.setPrimaryMobile(record.get("Primary_Mobile"));
		employeesDto.setRowNumber(record.get("Employee_Code"));
		employeesDto.setEmail(record.get("Email"));
		employeesDto.setPrimaryPositionId("Primary_Position_Id");
		return employeesDto;
	}

	public static boolean createProductCSVFile(List<LtMastProducts> productList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {

			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));
			List<String> headerList = new ArrayList<>();

			if (headerList.isEmpty()) {

				headerList.add("Product_Type");
				headerList.add("Category");
				headerList.add("Sub_Category");
				headerList.add("Product_Code");
				headerList.add("Product_Desc");
				headerList.add("Product_Desc");
				headerList.add("Primary_Uom");
				headerList.add("Secondary_Uom");
				headerList.add("Sec_Uom_Value");
				headerList.add("Unit_Per_Case");
				headerList.add("Segment");
				headerList.add("Brand");
				headerList.add("Sub_Brand");
				headerList.add("Style");
				headerList.add("Flavor");
				headerList.add("Case_Pack");
				headerList.add("HSN_Code");
				headerList.add("Orderable");
				headerList.add("Product_Image");
				headerList.add("Thumbnail_Image");
				headerList.add("Status");
				headerList.add("PTR_Flag");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				String headerArray = Validation.isCreatePipeSeparator(headerList);
				csv.writeln(headerArray);
				for (LtMastProducts productDto : productList) {

					List<String> list = new ArrayList<>();

					list.add(productDto.getProductType());
					list.add(productDto.getCategory());
					list.add(productDto.getSubCategory());
					list.add(productDto.getProductCode());
					list.add(productDto.getProductDesc());
					list.add(productDto.getProductDesc());
					list.add(productDto.getPrimaryUom());
					list.add(productDto.getSecondaryUom());
					list.add(productDto.getSecondaryUomValue());
					list.add(productDto.getUnitsPerCase());
					list.add(productDto.getSegment());
					list.add(productDto.getBrand());
					list.add(productDto.getSubBrand());
					list.add(productDto.getStyle());
					list.add(productDto.getFlavor());
					list.add(productDto.getCasePack());
					list.add(productDto.getHsnCode());
					list.add(productDto.getOrderable());
					list.add(productDto.getProductImage());
					list.add(productDto.getThumbnailImage());
					list.add(productDto.getStatus());
					list.add(productDto.getPtrFlag());
					list.add(productDto.getMobileStatus());
					list.add(productDto.getMobileRemarks());
					list.add(productDto.getMobileInsertUpdate());

					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);
				}
				csv.close();
				writer.flush();
				writer.close();
				System.out.println("File Product Created Successfully");
				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {
					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static LtMastProducts setProductDto(LtMastProducts productDto, CSVRecord record) {

		if (record.isMapped("Product_Type")) {
			productDto.setProductType(record.get("Product_Type").trim());
		} else {
			productDto.setProductType(record.get(0).trim());

		}
		productDto.setCategory(record.get("Category").trim());
		productDto.setSubCategory(record.get("Sub_Category").trim());
		productDto.setProductCode(record.get("Product_Code").trim());
		productDto.setProductName(record.get("Product_Desc").trim());
		productDto.setProductDesc(record.get("Product_Name").trim());
		productDto.setPrimaryUom(record.get("Primary_Uom").trim());
		productDto.setSecondaryUom(record.get("Secondary_Uom").trim());
		productDto.setSecondaryUomValue(record.get("Sec_Uom_Value").trim());
		productDto.setUnitsPerCase(record.get("Unit_Per_Case").trim());
		productDto.setSegment(record.get("Segment").trim());
		productDto.setBrand(record.get("Brand").trim());
		productDto.setSubBrand(record.get("Sub_Brand").trim());
		productDto.setStyle(record.get("Style").trim());
		productDto.setFlavor(record.get("Flavor").trim());
		productDto.setCasePack(record.get("Case_Pack").trim());
		productDto.setHsnCode(record.get("HSN_Code").trim());
		productDto.setOrderable(record.get("Orderable").trim());
		productDto.setStatus(record.get("Status").trim());
		productDto.setPtrFlag(record.get("PTR_Flag").trim());

		return productDto;
	}

	public static boolean createPriceListCSVFile(List<LtMastPriceLists> priceList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {

			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
					+ ltJobLogs.getFileName().toString();

			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();
			if (headerList.isEmpty()) {

				headerList.add("Price_List");
				headerList.add("Price_List_Desc");
				headerList.add("Currency");
				headerList.add("Product_Code");
				headerList.add("List_Price");
				headerList.add("PTR");
				headerList.add("Start_Date");
				headerList.add("End_Date");
				headerList.add("Mobile_Status");
				headerList.add("Mobile_Remarks");
				headerList.add("Mobile_Insert_Update");

				String headerArray = Validation.isCreatePipeSeparator(headerList);
				csv.writeln(headerArray);

				for (LtMastPriceLists priceListsDto : priceList) {
					List<String> list = new ArrayList<>();
					list.add(priceListsDto.getPriceList());
					list.add(priceListsDto.getPriceListDesc());
					list.add(priceListsDto.getCurrency());
					list.add(priceListsDto.getProductCode());
					list.add(priceListsDto.getListPrice());
					list.add(priceListsDto.getPtrPrice().toString());
					list.add(priceListsDto.getStartDate());
					list.add(priceListsDto.getEndDate());
					list.add(priceListsDto.getMobileStatus());
					list.add(priceListsDto.getMobileRemarks());
					list.add(priceListsDto.getMobileInsertUpdate());
					String headerValueArray = Validation.isCreatePipeSeparator(list);
					csv.writeln(headerValueArray);
				}
				csv.close();
				writer.flush();
				writer.close();
				csv.close();
				System.out.println("File PriceList Created Successfully");

				String updateFilePath = InitiateThread.configMap.get("Import_Dest_Update_Master_Path").getValue() + "//"
						+ ltJobLogs.getFileName().toString();
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						updateFilePath);

				if (uploadBoolean == true) {
					// DELETE FILE HERE
					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static LtMastPriceLists setPriceListDto(LtMastPriceLists priceListsDto, CSVRecord record) {
		if(record.isMapped("Price_List"))
		{priceListsDto.setPriceList(record.get("Price_List"));
		}
		else
		{priceListsDto.setPriceList(record.get(0));
		}
		//priceListsDto.setPriceList(record.get("Price_List"));
		priceListsDto.setPriceListDesc(record.get("Price_List_Desc"));
		priceListsDto.setCurrency(record.get("Currency"));
		priceListsDto.setProductCode(record.get("Product_Code"));
		priceListsDto.setListPrice(record.get("List_Price"));
		priceListsDto.setPtrPrice(record.get("PTR"));
		priceListsDto.setStartDate(record.get("Start_Date"));
		priceListsDto.setEndDate(record.get("End_Date"));
		return priceListsDto;
	}

	public static boolean exportOrderFile(List<LtMastOrder> orderList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			String date = simpleDateFormat.format(new Date());
			String fileName = "In" + date;
			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "//" + fileName
					+ ".csv";
			ltJobLogs.setFileName(fileName + ".csv");
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();
			if (headerList.isEmpty()) {
				headerList.add("ORDERNUM");
				headerList.add("OUTLETCODE");
				headerList.add("PRODUCTCODE");
				headerList.add("QUANTITY");
				String[] headerArray = headerList.toArray(new String[headerList.size()]);
				csv.writeln(headerArray);

				for (LtMastOrder ltMastOrder : orderList) {

					List<String> list = new ArrayList<>();

					list.add(ltMastOrder.getOrderNumber());
					list.add(ltMastOrder.getOutletCode());
					list.add(ltMastOrder.getProductCode());
					list.add(ltMastOrder.getQuantity());

					String[] headerValueArray = list.toArray(new String[list.size()]);
					csv.writeln(headerValueArray);
				}

				writer.flush();
				writer.close();
				csv.close();

				System.out.println("File Distributor Created Successfully");

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Export_FTP_Archived_File_Path").getValue() + "/" + file.getName(),
						file.getAbsolutePath());

				if (uploadBoolean == true) {
					createCSVFlag = true;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static boolean exportOrderEmptyFile(List<LtMastOrder> orderList, LtJobLogs ltJobLogs) {
		boolean createCSVFlag = false;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			String date = simpleDateFormat.format(new Date());
			String fileName = "EIn" + date;
			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "//" + fileName
					+ ".csv";
			ltJobLogs.setFileName(fileName + ".csv");
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();
			if (headerList.isEmpty()) {
				headerList.add("ORDERNUM");
				headerList.add("OUTLETCODE");
				headerList.add("PRODUCTCODE");
				headerList.add("QUANTITY");
				String[] headerArray = headerList.toArray(new String[headerList.size()]);
				csv.writeln(headerArray);
				writer.flush();
				writer.close();
				csv.close();

				System.out.println("File Distributor Created Successfully");

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Export_FTP_Archived_File_Path").getValue() + "/" + file.getName(),
						file.getAbsolutePath());

				if (uploadBoolean == true) {
					createCSVFlag = true;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static boolean createOrderCSVFile(List<OrderOutDto> orderList, LtJobLogs ltJobLogs) {

		boolean createCSVFlag = false;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			String date = simpleDateFormat.format(new Date());
			System.out.println(date);
			FTPUtil ftpUtil = new FTPUtil();
			String csvFile = InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "//"
					+ ltJobLogs.getFileName();
			FileWriter writer = new FileWriter(csvFile);
			File file = new File(csvFile);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator"));

			List<String> headerList = new ArrayList<>();
			if (headerList.isEmpty()) {
				headerList.add("ORDERNUM");
				headerList.add("OUTLETCODE");
				headerList.add("PRODUCTCODE");
				headerList.add("EIMSTATUS");
				headerList.add("STATUS");
				String[] headerArray = headerList.toArray(new String[headerList.size()]);
				csv.writeln(headerArray);

				for (OrderOutDto orderOutDto : orderList) {

					List<String> list = new ArrayList<>();
					list.add(orderOutDto.getOrderNum());
					list.add(orderOutDto.getOutletCode());
					list.add(orderOutDto.getProductCode());
					list.add(orderOutDto.geteImStatus());
					list.add(orderOutDto.getStatus());

					// CSVUtils.writeLine(writer, list);
					String[] headerValueArray = list.toArray(new String[list.size()]);
					csv.writeln(headerValueArray);
				}

				writer.flush();
				writer.close();
				csv.close();

				System.out.println("File Distributor Created Successfully");

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Export_FTP_Archived_File_Path").getValue() + "/" + file.getName(),
						file.getAbsolutePath());

				if (uploadBoolean == true) {
					// DELETE FILE HERE
					System.out
							.println("=========>" + InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue()
									+ "/" + ltJobLogs.getFileName());
					Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
							InitiateThread.configMap.get("FTP_Port").getValue(),
							InitiateThread.configMap.get("User_Name").getValue(),
							InitiateThread.configMap.get("Password").getValue(),
							InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
									+ ltJobLogs.getFileName());

					if (deleteBoolean == true) {
						createCSVFlag = true;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createCSVFlag;
	}

	public static LtMastInventory setInventoryDto(LtMastInventory inventoryDto, CSVRecord record) {

		if (record.isMapped("Inventory_Code")) {
			inventoryDto.setInventoryCode(record.get("Inventory_Code").trim());

		} else {
			inventoryDto.setInventoryCode(record.get(0).trim());

		}
		inventoryDto.setInventoryName(record.get("Inventory_Name").trim());
		inventoryDto.setInventoryStatus(record.get("Dist_Code").trim());
		inventoryDto.setDistCode(record.get("Inventory_Status").trim());
		inventoryDto.setProductRid(record.get("Product_Code").trim());
		inventoryDto.setProductCode(record.get("Prod_RID").trim());
		inventoryDto.setQuantity(record.get("Quantity").trim());

		return inventoryDto;
	}

	public static String setMobileNumber(String mobileNumber) {

		if (mobileNumber != null && !mobileNumber.isEmpty()) {
			if (mobileNumber.length() > 10) {
				String Mb = mobileNumber.substring(mobileNumber.length() - 10);
				return Mb;
			}
		}
		return mobileNumber;

	}
}
