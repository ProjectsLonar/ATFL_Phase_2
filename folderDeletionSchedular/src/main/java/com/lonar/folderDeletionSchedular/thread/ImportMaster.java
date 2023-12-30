package com.lonar.folderDeletionSchedular.thread;

import org.springframework.stereotype.Component;

import com.lonar.folderDeletionSchedular.model.LtJobeLogs;
import com.lonar.folderDeletionSchedular.model.ServiceException;
import com.lonar.folderDeletionSchedular.services.LtFolderDeletionService;
import com.lonar.folderDeletionSchedular.services.LtJobeService;

@Component
public class ImportMaster {

	LtJobeService ltJobeService;
	LtFolderDeletionService ltFolderDeletionService;

	public ImportMaster(LtJobeService ltJobeService, LtFolderDeletionService ltFolderDeletionService) {
		this.ltJobeService = ltJobeService;
		this.ltFolderDeletionService = ltFolderDeletionService;
	}
	public boolean folderEmptyImportMaster(LtJobeLogs ltJobeLogs) throws ServiceException {
		try {
			int failedRecord = 0;
			int successRecord = 0;
			ltJobeLogs.setFailedRecord(failedRecord);
			ltJobeLogs.setSuccessRecord(successRecord);
			ltJobeLogs = ltJobeService.saveJobeLogsData(ltJobeLogs);
			
			boolean	deletionFlag = 
				ltFolderDeletionService.deleteFolderContains();
			if (deletionFlag) {
				ltJobeLogs.setSuccessRecord(++successRecord);
				ltJobeLogs = ltJobeService.saveJobeLogsData(ltJobeLogs);
			} else {
				ltJobeLogs.setFailedRecord(++failedRecord);
				ltJobeLogs = ltJobeService.saveJobeLogsData(ltJobeLogs);
			}

			return deletionFlag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
