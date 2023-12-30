package com.lonar.atflMobileInterfaceService.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.atflMobileInterfaceService.model.LtMastDistributors;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployees;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployeesPosition;
import com.lonar.atflMobileInterfaceService.model.LtMastInventory;
import com.lonar.atflMobileInterfaceService.model.LtMastOutlets;
import com.lonar.atflMobileInterfaceService.model.LtMastPositions;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceLists;
import com.lonar.atflMobileInterfaceService.model.LtMastProductCat;
import com.lonar.atflMobileInterfaceService.model.LtMastProducts;
import com.lonar.atflMobileInterfaceService.repository.LtMastDistributorsRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastEmployeePositionRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastEmployeeRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastInventoryRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastOutletRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastPositionsRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastPriceListRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastProdCatRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastProductRepository;

@Repository
@PropertySource(value = "classpath:queries/interfaceQueries.properties", ignoreResourceNotFound = true)
public class LtReadCSVFilesDaoImpl implements LtReadCSVFilesDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	LtMastProdCatRepository ltMastProdCatRepository;

	@Autowired
	LtMastDistributorsRepository ltMastDistributorsRepository;

	@Autowired
	LtMastPositionsRepository ltMastPositionsRepository;

	@Autowired
	LtMastOutletRepository ltMastOutletRepository;

	@Autowired
	LtMastEmployeeRepository ltMastEmployeeRepository;

	@Autowired
	LtMastProductRepository ltMastProductRepository;
	
	@Autowired
	LtMastPriceListRepository ltMastPriceListRepository;

	@Autowired
	LtMastInventoryRepository ltMastInventoryRepository;
	
	@Autowired
	LtMastEmployeePositionRepository ltMastEmployeePositionRepository;
	
	@Override
	public LtMastOutlets checkOutletCode(String outletCode) throws ServiceException {
		String query = env.getProperty("checkOutletCode");

		String code = outletCode.toUpperCase().trim();

		List<LtMastOutlets> list = jdbcTemplate.query(query, new Object[] { code },
				new BeanPropertyRowMapper<LtMastOutlets>(LtMastOutlets.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastDistributors checkDistributorCode(String distributorCrmCode) throws ServiceException {
		String query = env.getProperty("checkDistributorCode");
		String code = distributorCrmCode.toUpperCase().trim();

		List<LtMastDistributors> list = jdbcTemplate.query(query, new Object[] { code },
				new BeanPropertyRowMapper<LtMastDistributors>(LtMastDistributors.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastPositions checkPosition(String positionCode) throws ServiceException {
		String query = env.getProperty("checkPosition");

		String code = positionCode.toUpperCase().trim();

		List<LtMastPositions> list = jdbcTemplate.query(query, new Object[] { code },
				new BeanPropertyRowMapper<LtMastPositions>(LtMastPositions.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastEmployees checkEmployeeCode(String emplyeeCode) throws ServiceException {
		String query = env.getProperty("checkEmployeeCode");

		String ecode = emplyeeCode.toUpperCase().trim();

		List<LtMastEmployees> list = jdbcTemplate.query(query, new Object[] { ecode },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastProducts checkProductCode(String productCode) throws ServiceException {
		String query = env.getProperty("checkProductCode");
		try {
			String code = productCode.toUpperCase().trim();

			List<LtMastProducts> list = jdbcTemplate.query(query, new Object[] { code },
					new BeanPropertyRowMapper<LtMastProducts>(LtMastProducts.class));

			if (list.isEmpty())
				return null;
			else
				return list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public LtMastPriceLists checkPriceListsCode(String productCode, String priceList) throws ServiceException {
		String query = env.getProperty("checkPriceListsCode");

		String code = productCode.toUpperCase().trim();

		List<LtMastPriceLists> list = jdbcTemplate.query(query, new Object[] { code, priceList },
				new BeanPropertyRowMapper<LtMastPriceLists>(LtMastPriceLists.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public Map<String, LtMastProductCat> getAllCategories() throws ServiceException {
		Map<String, LtMastProductCat> categoryMap = new HashMap<>();
		Iterable<LtMastProductCat> list = ltMastProdCatRepository.findAll();
		for (LtMastProductCat ltMastProductCat : list) {
			categoryMap.put(ltMastProductCat.getCategoryName(), ltMastProductCat);
		}
		if (categoryMap.isEmpty()) {
			return null;
		} else {
			return categoryMap;
		}

	}

	@Override
	public Map<String, LtMastDistributors> getAllDistributor() throws ServiceException {
		Map<String, LtMastDistributors> distributorsMap = new HashMap<>();
		Iterable<LtMastDistributors> list = ltMastDistributorsRepository.findAll();
		for (LtMastDistributors ltMastDistributors : list) {
			distributorsMap.put(ltMastDistributors.getDistributorCode(), ltMastDistributors);
		}
		if (distributorsMap.isEmpty()) {
			return distributorsMap;
		} else {
			return distributorsMap;
		}

	}

	@Override
	public Map<String, LtMastPositions> getAllPosition() throws ServiceException {

		Map<String, LtMastPositions> positionMap = new HashMap<>();

		Iterable<LtMastPositions> list = ltMastPositionsRepository.findAll();

		for (LtMastPositions ltMastPositions : list) {
			positionMap.put(ltMastPositions.getPositionCode(), ltMastPositions);
		}
		if (positionMap.isEmpty()) {
			return positionMap;
		} else {
			return positionMap;
		}
	}

	@Override
	public Map<String, LtMastOutlets> getAllOutlet() throws ServiceException {

		Map<String, LtMastOutlets> outletMap = new HashMap<>();

		Iterable<LtMastOutlets> list = ltMastOutletRepository.findAll();

		for (LtMastOutlets ltMastOutlets : list) {
			outletMap.put(ltMastOutlets.getOutletCode(), ltMastOutlets);
		}
		if (outletMap.isEmpty()) {
			return outletMap;
		} else {
			return outletMap;
		}
	}

	@Override
	public Map<String, LtMastEmployees> getAllEmployee() throws ServiceException {

		Map<String, LtMastEmployees> employeeMap = new HashMap<>();

		Iterable<LtMastEmployees> list = ltMastEmployeeRepository.findAll();

		for (LtMastEmployees ltMastEmployees : list) {
			employeeMap.put(ltMastEmployees.getEmployeeCode(), ltMastEmployees);
		}
		if (employeeMap.isEmpty()) {
			return employeeMap;
		} else {
			return employeeMap;
		}
	}

	@Override
	public Map<String, LtMastProducts> getAllProduct() throws ServiceException {
		Map<String, LtMastProducts> productMap = new HashMap<>();

		Iterable<LtMastProducts> list = ltMastProductRepository.findAll();

		for (LtMastProducts ltMastProducts : list) {
			productMap.put(ltMastProducts.getProductCode(), ltMastProducts);
		}
		if (productMap.isEmpty()) {
			return productMap;
		} else {
			return productMap;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastDistributors saveDistributor(LtMastDistributors ltMastDistributor) throws ServiceException {
		return ltMastDistributorsRepository.save(ltMastDistributor);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastPositions savePosition(LtMastPositions ltMastPosition) throws ServiceException {
		return ltMastPositionsRepository.save(ltMastPosition);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastOutlets saveOutlet(LtMastOutlets ltMastOutlet) throws ServiceException {
		return ltMastOutletRepository.save(ltMastOutlet);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastEmployees saveEmployee(LtMastEmployees ltMastEmployee) throws ServiceException {
		return ltMastEmployeeRepository.save(ltMastEmployee);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastProducts saveProduct(LtMastProducts ltMastProduct) throws ServiceException {
		return ltMastProductRepository.save(ltMastProduct);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastPriceLists savePriceList(LtMastPriceLists ltMastPriceList) throws ServiceException {
		return ltMastPriceListRepository.save(ltMastPriceList);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<LtMastOutlets> saveAllOutlet(List<LtMastOutlets> list) throws ServiceException {
		return ltMastOutletRepository.saveAll(list);
	}

	@Override
	public LtMastInventory saveInventory(LtMastInventory ltMastInventory) throws ServiceException {
		return ltMastInventoryRepository.save(ltMastInventory);
	}

	@Override
	public LtMastInventory checkInventoryCode(String inventoryCode) throws ServiceException {
		
		String query = env.getProperty("checkInventoryCode");

		String code = inventoryCode.toUpperCase().trim();

		List<LtMastInventory> list = jdbcTemplate.query(query, new Object[] { code },
				new BeanPropertyRowMapper<LtMastInventory>(LtMastInventory.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastInventory checkInvCodeDisCode(String inventoryCode, String disCode) throws ServiceException {
		String query = env.getProperty("checkInvCodeDisCode");

		String invCode = inventoryCode.toUpperCase().trim();
		String dCode = disCode.toUpperCase().trim();

		List<LtMastInventory> list = jdbcTemplate.query(query, new Object[] { invCode,dCode },
				new BeanPropertyRowMapper<LtMastInventory>(LtMastInventory.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastInventory checkInvCodeDisCodeProdCode(String inventoryCode, String disCode, String prodCode)
			throws ServiceException {
		String query = env.getProperty("checkInvCodeDisCodeProdCode");

		String invCode = inventoryCode.toUpperCase().trim();
		String dCode = disCode.toUpperCase().trim();
		String pCode = prodCode.toUpperCase().trim();

		List<LtMastInventory> list = jdbcTemplate.query(query, new Object[] { invCode,dCode,pCode },
				new BeanPropertyRowMapper<LtMastInventory>(LtMastInventory.class));

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public LtMastEmployees checkRowNumber(String rowNumber) throws ServiceException {
		
		String query = env.getProperty("checkEmployeeRowCode");
		String code = rowNumber.toUpperCase().trim();
		List<LtMastEmployees> list = jdbcTemplate.query(query, new Object[] { code },
				new BeanPropertyRowMapper<LtMastEmployees>(LtMastEmployees.class));
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtMastEmployeesPosition saveEmployeePosition(LtMastEmployeesPosition ltMastEmployeesPosition)
			throws ServiceException {
		return ltMastEmployeePositionRepository.save(ltMastEmployeesPosition);
	}

	@Override
	public LtMastEmployeesPosition getEmployeesPositionById(Long rowNumber) throws ServiceException {
		
		String query = env.getProperty("getEmployeesPositionById");
		
		List<LtMastEmployeesPosition> list = jdbcTemplate.query(query, new Object[] { rowNumber },
				new BeanPropertyRowMapper<LtMastEmployeesPosition>(LtMastEmployeesPosition.class));
		
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deletEmployeesPositionById(long eId) throws ServiceException {
		//String query = env.getProperty("deletEmployeesPositionById");
		String query = "delete from lt_mast_emp_position lmep where lmep.employee_id = ?";
		Object[] person = new Object[] { eId };
		int i = jdbcTemplate.update(query, person);
		System.out.println("Deleted===>"+i);
	}
	@Override
	public Map<Long,Long> getEmployeesPositionListById(long eId) throws ServiceException {
		
		String query = env.getProperty("getEmployeesPositionListById");
		
		Map<Long,Long> map=new LinkedHashMap<Long,Long>();
		
		List<LtMastEmployeesPosition> list = jdbcTemplate.query(query, new Object[] { eId },
				new BeanPropertyRowMapper<LtMastEmployeesPosition>(LtMastEmployeesPosition.class));
		
		if (list.isEmpty()) {
			return null;
		} else {
			for (LtMastEmployeesPosition ltMastEmployeesPosition : list) {
				map.put(ltMastEmployeesPosition.getPositionId(), ltMastEmployeesPosition.getEmpPositionId());
			}
			return map;
		}
	}
}
