package com.lonar.atflMobileInterfaceService.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lonar.atflMobileInterfaceService.common.Validation;
import com.lonar.atflMobileInterfaceService.ftp.FTPUtil;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.service.LtJobeService;
import com.lonar.atflMobileInterfaceService.service.LtReadCSVFilesServices;

@Component
public class ImportMaster {

	LtReadCSVFilesServices ltReadCSVFilesServices;

	LtJobeService ltJobeService;

	private static final Logger logger = LoggerFactory.getLogger(ImportMaster.class);
	
	public ImportMaster(LtJobeService ltJobeService, LtReadCSVFilesServices ltReadCSVFilesServices) {
		this.ltJobeService = ltJobeService;
		this.ltReadCSVFilesServices = ltReadCSVFilesServices;
	}

	public void outletImportMaster(LtJobLogs ltJobLogs) {
		try {
			System.out.println("Import Master in method outletImportMaster...");
			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EOUTLETS")) {

				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {
				// File is not empty
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltReadCSVFilesServices.readOutletCSVFile(ltJobLogs);
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}

	}

	public void distributorImportMaster(LtJobLogs ltJobLogs) {
		try {
			System.out.println("Import Master in method distributorImportMaster...");

			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EDISTRIBUTORS")) {

				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),

						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {

				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);

				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltJobLogs = ltReadCSVFilesServices.readDistributorCSVFile(ltJobLogs);
				
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}

	}

	public void employeeImportMaster(LtJobLogs ltJobLogs) {
		try {

			System.out.println("Import Master in method employeeImportMaster...");
			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EEMPLOYEE")) {
				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {
				// File is not empty
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltJobLogs = ltReadCSVFilesServices.readEmployeeCSVFile(ltJobLogs);
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}
	}

	public void positionImportMaster(LtJobLogs ltJobLogs) {

		try {

			System.out.println("Import Master in method employeeImportMaster...");

			FTPUtil ftpUtil = new FTPUtil();

			if (ltJobLogs.getFileName().toUpperCase().contains("EPOSITIONS")) {
				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {
				// File is not empty
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),

						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltJobLogs = ltReadCSVFilesServices.readPositionCSVFile(ltJobLogs);
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}
	}

	public void productImportMaster(LtJobLogs ltJobLogs) {

		try {
			System.out.println("Import Master in method employeeImportMaster...");
			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EPRODUCTS")) {
				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {
				// File is not empty
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltJobLogs = ltReadCSVFilesServices.readProductCSVFile(ltJobLogs);
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}
	}

	public void priceImportMaster(LtJobLogs ltJobLogs) {

		try {
			System.out.println("Import Master in method employeeImportMaster...");
			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EPRICELIST")) {
				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),

						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {
				// File is not empty
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltJobLogs = ltReadCSVFilesServices.readPriceListCSVFile(ltJobLogs);
				}
			}
			
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}
	}
	
	public void inventoryImportMaster(LtJobLogs ltJobLogs) {
		try {
			System.out.println("Import Master in method Inventory...");
			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EINV")) {

				// File is Empty
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");

				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
			} else {
				// File is not empty
				ltJobLogs.setEndDate(Validation.getCurrentDateTime());
				ltJobLogs.setLogsStatus("CSV File Processing");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// download file from FTP
				Boolean downloadBoolean = ftpUtil.downloadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_DEST_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					ltReadCSVFilesServices.readInventoryCSVFile(ltJobLogs);
				}
			}
		} catch (Exception e) {
			logger.error("Error Description Import Master",e);
			e.printStackTrace();
		}

	}

}
