package com.lonar.folderDeletionSchedular.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;
import com.lonar.folderDeletionSchedular.model.LtJobeLogs;
import com.lonar.folderDeletionSchedular.services.LtFolderDeletionService;
import com.lonar.folderDeletionSchedular.services.LtJobeService;

public class ImportTimerTask extends TimerTask {

	LtJobeService ltJobeService;

	//LtSendServices ltSendServices;
	LtFolderDeletionService ltFolderDeletionService;

	public ImportTimerTask(LtJobeService ltJobeService,LtFolderDeletionService ltFolderDeletionService) {
		this.ltJobeService = ltJobeService;
		this.ltFolderDeletionService = ltFolderDeletionService;
	}

	@Override
	public void run() {

		ImportMaster importMaster = new ImportMaster(ltJobeService, ltFolderDeletionService);

		HashMap<Long, LtJobeImportExport> ltJobeImportExportSeqMap = InitiateThread.jobImportExportMap.get("IMPORT");

		Set s = ltJobeImportExportSeqMap.entrySet();

		Iterator it = s.iterator();

		while (it.hasNext()) {

			Map.Entry entry = (Map.Entry) it.next();

			LtJobeImportExport ltJobeImportExport = (LtJobeImportExport) entry.getValue();

			if (ltJobeImportExport.getStatus().equals("ACTIVE")) {
				String importMasterStr = ltJobeImportExport.getName();
				LtJobeLogs ltJobeLogs = new LtJobeLogs();
				switch (importMasterStr) {
				case "FOLDER_EMPTY_MASTER":
					try {
						System.out.println("FOLDER_EMPTY_MASTER");
						ltJobeLogs.setImportExportId(ltJobeImportExport.getImportExportId());
						ltJobeLogs.setStartDate(new Date());
						ltJobeLogs.setJobType("FOLDER EMPTY IMPORT");
						ltJobeLogs.setLogsStatus("Inprocess");
						ltJobeLogs = ltJobeService.saveJobeLogsData(ltJobeLogs);
						boolean flag = importMaster.folderEmptyImportMaster(ltJobeLogs);
						
						if(flag) {
							ltJobeLogs.setLogsStatus("Completed");
							ltJobeLogs.setEndDate(new Date());
							ltJobeLogs = ltJobeService.saveJobeLogsData(ltJobeLogs);
						}else {
							ltJobeLogs.setLogsStatus("Failed");
							ltJobeLogs.setEndDate(new Date());
							ltJobeLogs = ltJobeService.saveJobeLogsData(ltJobeLogs);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					System.out.println("Invalid import master" + importMasterStr);
					break;
				}
			}
		}
		System.out.println("Import Timer task finished at:" + new Date());
	}

}
