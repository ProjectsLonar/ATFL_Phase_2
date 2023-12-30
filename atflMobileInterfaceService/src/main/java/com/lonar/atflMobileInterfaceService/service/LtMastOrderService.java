package com.lonar.atflMobileInterfaceService.service;

import java.util.List;

import com.lonar.atflMobileInterfaceService.model.LtJobLogs;
import com.lonar.atflMobileInterfaceService.model.LtMastOrder;
import com.lonar.atflMobileInterfaceService.model.LtSoLines;

public interface LtMastOrderService {
	
	public List<LtMastOrder> getAllInprocessOrder() throws Exception;
	public boolean updateOrderStatus(String orderNumber);
	LtJobLogs readOrderCSVFile(LtJobLogs ltJobLogs) throws Exception;
	
	public List<LtSoLines> checkDoubleOrderEntry(LtMastOrder ltMastOrder) throws Exception;
	
	LtSoLines delete(LtSoLines ltSoLines) throws Exception;
	
	void deleteDuplicateOrderLine() throws Exception;
	
	
}
