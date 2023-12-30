package com.lonar.atflMobileInterfaceService.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.lonar.atflMobileInterfaceService.ftp.FTPUtil;
import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.service.LtJobeService;
import com.lonar.atflMobileInterfaceService.service.LtReadCSVFilesServices;

public class ImportHourseTask extends TimerTask {
	LtJobeService ltJobeService;
	LtReadCSVFilesServices ltReadCSVFilesServices;

	public ImportHourseTask(LtJobeService ltJobeService, LtReadCSVFilesServices ltReadCSVFilesServices) {
		this.ltJobeService = ltJobeService;
		this.ltReadCSVFilesServices = ltReadCSVFilesServices;
	}

	@Override
	public void run() {

		FTPUtil ftpUtil = new FTPUtil();

		Map<String, HashMap<Date, LtJobLogs>> fileDetailsMap = ftpUtil.getFileDetails(
				InitiateThread.configMap.get("Public_IP").getValue(),
				InitiateThread.configMap.get("FTP_Port").getValue(),
				InitiateThread.configMap.get("User_Name").getValue(),
				InitiateThread.configMap.get("Password").getValue(),
				InitiateThread.configMap.get("Import_FTP_SRC_File_Path").getValue());

		ImportMaster importMaster = new ImportMaster(ltJobeService, ltReadCSVFilesServices);

		HashMap<Long, LtJobeImportExport> ltJobeImportExportSeqMap = InitiateThread.jobImportExportMap.get("IMPORT");

		for (Map.Entry<Long, LtJobeImportExport> entry : ltJobeImportExportSeqMap.entrySet()) {
			
			LtJobeImportExport ltJobeImportExport = entry.getValue();
			
			if(ltJobeImportExport.getStatus().equalsIgnoreCase("ACTIVE")) {
				
				String importMasterStr = ltJobeImportExport.getName();
				
				
				switch(importMasterStr) {
				
				case"Inventory_Master":
					System.err.println("Call "+new Date());
					
					HashMap<Date, LtJobLogs> inventoryMap = fileDetailsMap.get("Inv");
					
					if (inventoryMap != null) {
						for(Map.Entry<Date, LtJobLogs> entryInventory:inventoryMap.entrySet()) {
							LtJobLogs ltJobLogs = entryInventory.getValue();
							importMaster.inventoryImportMaster(ltJobLogs);
						}
						
					}
					
					break;
				}
			}
		}
	}
}
