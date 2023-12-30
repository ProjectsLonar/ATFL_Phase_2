package com.lonar.atflMobileInterfaceService.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;

import com.lonar.atflMobileInterfaceService.model.LtMastDistributors;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployees;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployeesPosition;
import com.lonar.atflMobileInterfaceService.model.LtMastInventory;
import com.lonar.atflMobileInterfaceService.model.LtMastOutlets;
import com.lonar.atflMobileInterfaceService.model.LtMastPositions;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceLists;
import com.lonar.atflMobileInterfaceService.model.LtMastProductCat;
import com.lonar.atflMobileInterfaceService.model.LtMastProducts;

public interface LtReadCSVFilesDao {
	
	public LtMastOutlets checkOutletCode(String outletCode) throws ServiceException;

	public LtMastDistributors checkDistributorCode(String distributorCode) throws ServiceException;

	public Map<String, LtMastDistributors> getAllDistributor() throws ServiceException;

	public Map<String, LtMastPositions> getAllPosition() throws ServiceException;

	public Map<String, LtMastOutlets> getAllOutlet() throws ServiceException;
	
	public Map<String, LtMastEmployees> getAllEmployee() throws ServiceException;
	
	public Map<String, LtMastProducts> getAllProduct() throws ServiceException;
	
	public LtMastPositions checkPosition(String position) throws ServiceException;

	public LtMastEmployees checkEmployeeCode(String emplyeeCode) throws ServiceException;
	
	public LtMastEmployees checkRowNumber(String rowNumber) throws ServiceException;
	
	public LtMastEmployeesPosition getEmployeesPositionById(Long rowNumber) throws ServiceException;

	public void deletEmployeesPositionById(long eId) throws ServiceException ;
	
	public Map<Long,Long> getEmployeesPositionListById(long eId) throws ServiceException;
	
	public LtMastProducts checkProductCode(String productCode) throws ServiceException;

	public LtMastPriceLists checkPriceListsCode(String productCode, String priceList) throws ServiceException;

	public Map<String, LtMastProductCat> getAllCategories() throws ServiceException;
	
	public LtMastDistributors saveDistributor(LtMastDistributors ltMastDistributor) throws ServiceException;
	
	public LtMastPositions savePosition(LtMastPositions ltMastPosition) throws ServiceException;
	
	public LtMastOutlets saveOutlet(LtMastOutlets ltMastOutlet) throws ServiceException;
	
	public LtMastInventory saveInventory(LtMastInventory ltMastInventory) throws ServiceException;
	
	public LtMastEmployees saveEmployee(LtMastEmployees ltMastEmployee) throws ServiceException;
	
	public LtMastProducts saveProduct(LtMastProducts ltMastProduct) throws ServiceException;
	
	public LtMastPriceLists savePriceList(LtMastPriceLists ltMastPriceList) throws ServiceException;
	
	public List<LtMastOutlets> saveAllOutlet(List<LtMastOutlets> list) throws ServiceException;

	public LtMastInventory checkInventoryCode(String inventoryCode) throws ServiceException;
	
	public LtMastInventory checkInvCodeDisCode(String inventoryCode,String disCode) throws ServiceException;
	
	public LtMastInventory checkInvCodeDisCodeProdCode(String inventoryCode,String disCode,String prodCode) throws ServiceException;
	
	public LtMastEmployeesPosition saveEmployeePosition(LtMastEmployeesPosition ltMastEmployeesPosition) throws ServiceException;
}
