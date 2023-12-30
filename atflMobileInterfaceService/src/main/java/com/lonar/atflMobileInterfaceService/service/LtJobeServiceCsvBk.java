package com.lonar.atflMobileInterfaceService.service;

import java.io.IOException;
import java.util.List;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.model.LtMastDistributors;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployees;
import com.lonar.atflMobileInterfaceService.model.LtMastInventory;
import com.lonar.atflMobileInterfaceService.model.LtMastOutlets;
import com.lonar.atflMobileInterfaceService.model.LtMastPositions;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceLists;
import com.lonar.atflMobileInterfaceService.model.LtMastProducts;

public interface LtJobeServiceCsvBk {
	
	boolean saveDistributorCSVBk(List<LtMastDistributors> distributorsList) throws IOException, ServiceException;
	
	boolean savePositionCSVBk(List<LtMastPositions> PositionsList) throws IOException, ServiceException;
	
	boolean saveOutletCSVBk(List<LtMastOutlets> outletList) throws IOException,  ServiceException;
	
	boolean saveProductCSVBk(List<LtMastProducts> productList) throws IOException,  ServiceException;
	
	boolean savePriceListCSVBk(List<LtMastPriceLists> priceList) throws IOException,  ServiceException;
	
	boolean saveEmployeeCSVBk(List<LtMastEmployees> employeeList) throws IOException, ServiceException;
	
	boolean saveInventoryCSVBk(List<LtMastInventory> inventoryList) throws IOException, ServiceException;
	
	

}
