package com.lonar.atflMobileInterfaceService.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;
import com.lonar.atflMobileInterfaceService.service.LtJobeService;
import com.lonar.atflMobileInterfaceService.service.LtMastOrderService;

public class ExportTimerTask extends TimerTask {

	LtMastOrderService ltMastOrderService;
	LtJobeService ltJobeService;
	
	
    public ExportTimerTask(LtMastOrderService ltMastOrderService,LtJobeService ltJobeService) {
    	this.ltMastOrderService=ltMastOrderService;
    	this.ltJobeService =ltJobeService;
	}

	@Override
    public void run() {
		
        System.out.println("Export Timer task started at:"+new Date());
        
        HashMap<Long,LtJobeImportExport> ltJobeImportExportSeqMap = InitiateThread.jobImportExportMap.get("EXPORT");
        
        ExportMaster exportMaster=new ExportMaster(ltMastOrderService,ltJobeService);
        
        Set s = ltJobeImportExportSeqMap.entrySet();
        
        Iterator it = s.iterator();
        
        while ( it.hasNext() ) {
        	
			Map.Entry entry = (Map.Entry) it.next();
			
			Long key = (Long) entry.getKey();
			
			LtJobeImportExport ltJobeImportExport = (LtJobeImportExport) entry.getValue();
			
			System.out.println(key + " => " + ltJobeImportExport);
			
			System.out.println("Schdule import master running for..." + ltJobeImportExport.toString());

			if(ltJobeImportExport.getStatus().equals("ACTIVE")) {
				exportMaster.orderExportMaster();
				exportMaster.orderImportMaster();
				try {
					exportMaster.deleteDuplicateOrderLine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        }
        
        System.out.println("Export Timer task finished at:"+new Date());
    }

}
