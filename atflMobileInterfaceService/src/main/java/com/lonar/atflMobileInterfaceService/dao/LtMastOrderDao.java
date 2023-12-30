package com.lonar.atflMobileInterfaceService.dao;

import java.util.List;

import com.lonar.atflMobileInterfaceService.model.LtMastOrder;
import com.lonar.atflMobileInterfaceService.model.LtSoHeaders;
import com.lonar.atflMobileInterfaceService.model.LtSoLines;

public interface LtMastOrderDao {
	public List<LtMastOrder> getAllInprocessOrder() throws Exception;
	
	public boolean updateOrderStatus(String orderNumber);
	
	public LtSoLines getLineByOrdernumProductCode(String orderNum,String productCode);
	
	public  List<LtSoHeaders> getFailedorderList(String orderNum);
	
	public LtSoHeaders getHeaderByOrderNumber(String orderNum);
	
	public List<LtSoLines> checkDoubleOrderEntry(LtMastOrder ltMastOrder) throws Exception;
	
	public void deleteDuplicateOrderLine() throws Exception;
}
