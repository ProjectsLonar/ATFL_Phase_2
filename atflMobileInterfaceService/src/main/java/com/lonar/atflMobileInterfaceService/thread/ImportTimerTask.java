package com.lonar.atflMobileInterfaceService.thread;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lonar.atflMobileInterfaceService.common.ReadProductImages;
import com.lonar.atflMobileInterfaceService.ftp.FTPUtil;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.service.LtJobeService;
import com.lonar.atflMobileInterfaceService.service.LtReadCSVFilesServices;

public class ImportTimerTask extends TimerTask {

	LtJobeService ltJobeService;
	LtReadCSVFilesServices ltReadCSVFilesServices;
	
	private static final Logger logger = LoggerFactory.getLogger(ImportTimerTask.class);

	public ImportTimerTask(LtJobeService ltJobeService, LtReadCSVFilesServices ltReadCSVFilesServices) {

		this.ltJobeService = ltJobeService;
		this.ltReadCSVFilesServices = ltReadCSVFilesServices;
	}

	@Override
	public void run() {
		
		System.out.println("Import Timer task started at:" + new Date());

		FTPUtil ftpUtil = new FTPUtil();

		System.out.println("Public_IP :" + InitiateThread.configMap.get("Public_IP").toString());
		System.out.println("FTP_Port :" + InitiateThread.configMap.get("FTP_Port").toString());
		System.out.println("User_Name :" + InitiateThread.configMap.get("User_Name").toString());
		System.out.println("Password :" + InitiateThread.configMap.get("Password").toString());
		System.out.println("Import_FTP_SRC_File_Path :" + InitiateThread.configMap.get("Import_FTP_SRC_File_Path").toString());

		Map<String, HashMap<Date, LtJobLogs>> fileDetailsMap = ftpUtil.getFileDetails(
				InitiateThread.configMap.get("Public_IP").getValue(),
				InitiateThread.configMap.get("FTP_Port").getValue(),
				InitiateThread.configMap.get("User_Name").getValue(),
				InitiateThread.configMap.get("Password").getValue(),
				InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue());

		ImportMaster importMaster = new ImportMaster(ltJobeService, ltReadCSVFilesServices);

		HashMap<Long, LtJobeImportExport> ltJobeImportExportSeqMap = InitiateThread.jobImportExportMap.get("IMPORT");

		Set s = ltJobeImportExportSeqMap.entrySet();
		
		Iterator it = s.iterator();
		
		while (it.hasNext()) {
			
			Map.Entry entry = (Map.Entry) it.next();
			
			Long key = (Long) entry.getKey();
			
			LtJobeImportExport ltJobeImportExport = (LtJobeImportExport) entry.getValue();
			
			System.out.println(key + " => " + ltJobeImportExport);
			
			System.out.println("Schdule import master running for..." + ltJobeImportExport.toString());

			if (ltJobeImportExport.getStatus().equals("ACTIVE")) {
				
				String importMasterStr = ltJobeImportExport.getName();
				
				switch (importMasterStr) {
				
				case "Outlet_Master":
					System.out.println("Valid import master..." + importMasterStr);

					HashMap<Date, LtJobLogs> outletMap = fileDetailsMap.get("Outlets");
					if (outletMap != null) {
						Set set = outletMap.entrySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry entryit = (Map.Entry) iterator.next();
							Date date = (Date) entryit.getKey();
							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
							System.out.println(date + " => " + ltJobLogs);
							importMaster.outletImportMaster(ltJobLogs);
						} // while
					}

					break;
				case "Distributor_Master":
					System.out.println("Valid import master..." + importMasterStr);

					HashMap<Date, LtJobLogs> distributorsMap = fileDetailsMap.get("Distributors");

					if (distributorsMap != null) {
						Set distrset = distributorsMap.entrySet();
						Iterator iterator = distrset.iterator();
						while (iterator.hasNext()) {
							Map.Entry entryit = (Map.Entry) iterator.next();
							Date date = (Date) entryit.getKey();
							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
							System.out.println(date + " => " + ltJobLogs);
							importMaster.distributorImportMaster(ltJobLogs);
						} // while
					}
					break;
				case "Employee_Master":
					System.out.println("Valid import master..." + importMasterStr);

					HashMap<Date, LtJobLogs> employeeMap = fileDetailsMap.get("Employee");
					if (employeeMap != null) {
						Set set = employeeMap.entrySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry entryit = (Map.Entry) iterator.next();
							Date date = (Date) entryit.getKey();
							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
							System.out.println(date + " => " + ltJobLogs);
							importMaster.employeeImportMaster(ltJobLogs);
						} // while
					}
					break;
				case "Position_Master":
					System.out.println("Valid import master..." + importMasterStr);
					HashMap<Date, LtJobLogs> positionsMap = fileDetailsMap.get("Positions");
					if (positionsMap != null) {
						Set set = positionsMap.entrySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry entryit = (Map.Entry) iterator.next();
							Date date = (Date) entryit.getKey();
							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
							System.out.println(date + " => " + ltJobLogs);
							importMaster.positionImportMaster(ltJobLogs);
						} // while
					}
					break;
				case "Product_Master":
					System.out.println("Valid import master..." + importMasterStr);
					HashMap<Date, LtJobLogs> productMap = fileDetailsMap.get("Products");
					if (productMap != null) {
						Set set = productMap.entrySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry entryit = (Map.Entry) iterator.next();
							Date date = (Date) entryit.getKey();
							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
							System.out.println(date + " => " + ltJobLogs);
							importMaster.productImportMaster(ltJobLogs);
						} // while
					}
					break;
				case "Price_Master":
					System.out.println("Valid import master..." + importMasterStr);

					HashMap<Date, LtJobLogs> priceMap = fileDetailsMap.get("PriceList");
					if (priceMap != null) {
						Set set = priceMap.entrySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry entryit = (Map.Entry) iterator.next();
							Date date = (Date) entryit.getKey();
							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
							System.out.println(date + " => " + ltJobLogs);
							importMaster.priceImportMaster(ltJobLogs);
						} // while
					}
					try {
						ReadProductImages readProductImages = new ReadProductImages();
						boolean archivedImges = readProductImages.archivedIncrementaFaildlImages();
						if(archivedImges) {
							System.out.println("=====Archived Failed Images Data Successfully=====");
						}
					} catch (IOException e) {
						logger.error("ImportTimerTask",e);
						e.printStackTrace();
					}
					
					break;
//				case "Inventory_Master":
//					HashMap<Date, LtJobLogs> inventoryMap = fileDetailsMap.get("Inv");
//					
//					if (inventoryMap != null) {
//						
//						Set set = inventoryMap.entrySet();
//						Iterator iterator = set.iterator();
//						while (iterator.hasNext()) {
//							Map.Entry entryit = (Map.Entry) iterator.next();
//							Date date = (Date) entryit.getKey();
//							LtJobLogs ltJobLogs = (LtJobLogs) entryit.getValue();
//							importMaster.inventoryImportMaster(ltJobLogs);
//						} 
//					}
//					break;
				default:
					System.out.println("Invalid import master" + importMasterStr);
					break;
				}

			} 

		} 

		System.out.println("Import Timer task finished at:" + new Date());
	}

}
