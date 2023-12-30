package com.lonar.atflMobileInterfaceService.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lonar.atflMobileInterfaceService.common.CreateCSVFile;
import com.lonar.atflMobileInterfaceService.common.Validation;
import com.lonar.atflMobileInterfaceService.ftp.FTPUtil;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtMastOrder;
import com.lonar.atflMobileInterfaceService.model.LtSoLines;
import com.lonar.atflMobileInterfaceService.service.LtJobeService;
import com.lonar.atflMobileInterfaceService.service.LtMastOrderService;

public class ExportMaster {

	LtMastOrderService ltMastOrderService;
	LtJobeService ltJobeService;
	private static final Logger logger = LoggerFactory.getLogger(ExportMaster.class);

	public ExportMaster(LtMastOrderService ltMastOrderService, LtJobeService ltJobeService) {
		this.ltMastOrderService = ltMastOrderService;
		this.ltJobeService = ltJobeService;
	}

	public void checkDoubleProductEntry(List<LtMastOrder> orderList) throws Exception {

		for (LtMastOrder ltOrder : orderList) {
			List<LtSoLines> lineList = ltMastOrderService.checkDoubleOrderEntry(ltOrder);
			int count = 0;
			if (lineList.size() != 1) {
				for (LtSoLines ltSoLines : lineList) {
					if (count != lineList.size() - 1) {
						ltMastOrderService.delete(ltSoLines);
						count++;
					}else {
						break;
					}
				}

			} 

		}
	}

	public void orderExportMaster() {
		try {
			System.out.println("Export Master in method orderExportMaster...");
			List<LtMastOrder> getAllInprocessOrderList = ltMastOrderService.getAllInprocessOrder();
			LtJobLogs ltJobLogs = new LtJobLogs();
			ltJobLogs.setStartDate(Validation.getCurrentDateTime());
			ltJobLogs.setCsvDate(Validation.getCurrentDateTime());
			if (getAllInprocessOrderList != null && !getAllInprocessOrderList.isEmpty()) {
				ltJobLogs.setLogsStatus("Order Inprocess");
				ltJobLogs.setTotalRecord((long) getAllInprocessOrderList.size());
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);

				// export data here
				//check duplicate data if yes then delete in this method
				checkDoubleProductEntry(getAllInprocessOrderList);
				
				List<LtMastOrder> orderList = ltMastOrderService.getAllInprocessOrder();
				
				boolean exportOderFlag = CreateCSVFile.exportOrderFile(orderList, ltJobLogs);

				if (exportOderFlag == true) {
					boolean updateFlag = false;
					for (LtMastOrder ltMastOrder : orderList) {
						updateFlag = ltMastOrderService.updateOrderStatus(ltMastOrder.getOrderNumber());
					}
					if (updateFlag == true) {
						ltJobLogs.setEndDate(Validation.getCurrentDateTime());
						ltJobLogs.setLogsStatus("Order Export Completed");
						ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
					}

				} else {
					ltJobLogs.setLogsStatus("FTP Server Connection error");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				}

			} else {
				System.out.println("No order in Inprocessed");
				ltJobLogs.setJobType("Order");
				ltJobLogs.setLogsStatus("Empty Order Inprocess");
				ltJobLogs.setTotalRecord(0L);
				ltJobLogs.setFailedRecord(0L);
				ltJobLogs.setSuccessRecord(0L);
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// export data here
				boolean exportOderFlag = CreateCSVFile.exportOrderEmptyFile(getAllInprocessOrderList, ltJobLogs);
				if (exportOderFlag == true) {
					ltJobLogs.setEndDate(Validation.getCurrentDateTime());
					ltJobLogs.setLogsStatus("Order Export Completed");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				} else {
					ltJobLogs.setLogsStatus("FTP Server Connection error");
					ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				}

			}
		} catch (Exception e) {
			logger.error("Error Description Export Master", e);
			e.printStackTrace();
		}
	}

	public void orderImportMaster() {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			Map<String, HashMap<Date, LtJobLogs>> fileDetailsMap = ftpUtil.getFileDetails(
					InitiateThread.configMap.get("Public_IP").getValue(),
					InitiateThread.configMap.get("FTP_Port").getValue(),
					InitiateThread.configMap.get("User_Name").getValue(),
					InitiateThread.configMap.get("Password").getValue(),
					InitiateThread.configMap.get("Import_Order_Src_File_Path").getValue());

			System.out.println("File Details Map===>" + fileDetailsMap);
			HashMap<Date, LtJobLogs> outletMap = fileDetailsMap.get("Out");
			//if(outletMap!=null) {
			Set set = outletMap.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry entryit = (Map.Entry) iterator.next();
				Date date = (Date) entryit.getKey();
				LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
				System.out.println(date + " => " + ltJobLogs);
				importOrderOutFile(ltJobLogs);
			}
			//}
		} catch (Exception e) {
			logger.error("Error Description Export Master", e);
			e.printStackTrace();
		}

	}

	public void importOrderOutFile(LtJobLogs ltJobLogs) {
		try {
			System.out.println("Import Master in method outletImportMaster...");
			FTPUtil ftpUtil = new FTPUtil();
			if (ltJobLogs.getFileName().toUpperCase().contains("EOUT")) {

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
						InitiateThread.configMap.get("Import_Order_Src_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());
				ltJobLogs.setLogsStatus("CSV File Download");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);

				// upload file from FTP
				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_Order_Src_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Import_Order_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("CSV File Uploaded");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_Order_Src_File_Path").getValue() + "/"
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
						InitiateThread.configMap.get("Import_Order_Src_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "//"
								+ ltJobLogs.getFileName());

				Boolean uploadBoolean = ftpUtil.uploadFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_Order_Archived_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName(),
						InitiateThread.configMap.get("Export_Dest_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				if (downloadBoolean == true) {
					System.out.println("CSV file downloaded");
					ltJobLogs = ltMastOrderService.readOrderCSVFile(ltJobLogs);
				}
				ltJobLogs.setLogsStatus("CSV File downloaded");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);
				// upload file from FTP

				// delete file from FTP
				Boolean deleteBoolean = ftpUtil.deleteFile(InitiateThread.configMap.get("Public_IP").getValue(),
						InitiateThread.configMap.get("FTP_Port").getValue(),
						InitiateThread.configMap.get("User_Name").getValue(),
						InitiateThread.configMap.get("Password").getValue(),
						InitiateThread.configMap.get("Import_Order_Src_File_Path").getValue() + "/"
								+ ltJobLogs.getFileName());

				ltJobLogs.setLogsStatus("Completed");
				ltJobLogs = ltJobeService.saveJobeLogsData(ltJobLogs);

			}
		} catch (Exception e) {
			logger.error("Error Description Export Master", e);
			e.printStackTrace();
		}

	}
	public void deleteDuplicateOrderLine() throws Exception {
		ltMastOrderService.deleteDuplicateOrderLine();
	}
}
