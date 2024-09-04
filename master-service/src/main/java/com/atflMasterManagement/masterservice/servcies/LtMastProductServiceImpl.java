package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dao.LtMastProductDao;
import com.atflMasterManagement.masterservice.dto.ProductDto;
import com.atflMasterManagement.masterservice.dto.TlEtlDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastProducts;
//import com.atflMasterManagement.masterservice.model.LtMastProductsView;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.repository.LtMastProductsRepository;

@Service
public class LtMastProductServiceImpl implements LtMastProductService, CodeMaster {

	@Autowired
	private LtMastProductDao ltMastProductDao;

	@Autowired
	private LtMastProductsRepository ltMastProductsRepository;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public Status getProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		System.out.print("Hi In getProd");
		List<ProductDto> list = ltMastProductDao.getProduct(requestDto);
		Long productCount = ltMastProductDao.getProductCount(requestDto);

		if (list != null) {
			List<ProductDto> productDtoList = new ArrayList<ProductDto>();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				ProductDto productDto = (ProductDto) iterator.next();
				if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
					productDto.setPtrPrice(productDto.getListPrice());
				}
				productDtoList.add(productDto);
			}
			
			status.setCode(RECORD_FOUND);
			status.setData(productDtoList);
			status.setRecordCount(productCount);
		} else {
			status.setCode(RECORD_NOT_FOUND);
		}
		return status;
	}

	@Override
	public Status readImageProduct() throws ServiceException, IOException {
		try {
			List<LtMastProducts> list = ltMastProductDao.getAllProduct();
			int count = 1;
			for (LtMastProducts ltMastProducts : list) {
				ltMastProducts.setProductImage("/images/product_dev/" + count + ".jpg");
				ltMastProductsRepository.save(ltMastProducts);

				if (count == 50) {
					count = 1;
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Status getProductV2(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		try {
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		
		if(userType.equalsIgnoreCase(ADMIN)||userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)) {
			System.out.print("Hi in prodAdmin query");
			List<ProductDto> list = ltMastProductDao.getProductForAdmin(requestDto);
			Long productCount = ltMastProductDao.getProductCountForAdmin(requestDto);
			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			List<ProductDto> list = ltMastProductDao.getProductWithInventory(requestDto);
			Long productCount = ltMastProductDao.getProductCountWithInventory(requestDto);
			//System.out.print("Hi in prodInvent query");
			//System.out.print(list);

			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					//System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
						productDto.setPtrPrice(productDto.getListPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDtoList.add(productDto);
				}
				
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}

/*  this is original code comment on 03-June-2024 for mrp list & optimization	
	@Override
	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		
		System.out.print("In service getInStockProduct");
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		
		if(userType.equalsIgnoreCase("SYSTEMADMINISTRATOR")||userType.equalsIgnoreCase(SALESOFFICER)
				||userType.equalsIgnoreCase(AREAHEAD) ||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.print("Hi in prodAdmin query");
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			System.out.print("In Controller else getInStockProduct");
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
			//System.out.print("Hi in prodInvent query");
			System.out.print("ProductList= "+ list);

			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
					//	productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDtoList.add(productDto);
				}
				
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
		return status;
	}
*/
/*	this is original code comment on 18-June-2024 for api time diff calculation 
	@Override
	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		
		try {
		
		System.out.println("In method getInStockProduct at = "+LocalDateTime.now());
		System.out.println("Above getUserTypeByUserId query call at = "+LocalDateTime.now());
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		System.out.println("Below getUserTypeByUserId query call at = "+LocalDateTime.now());
		
		
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
 
	//	System.out.println("mrpList = "+mrpList);
	//	System.out.println("mrpList size = "+mrpList.size());
 
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD) ||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.println("Hi in prodAdmin query");
			System.out.println("Above getInStockProductAdmin query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			System.out.println("Below getInStockProductAdmin query call at = "+LocalDateTime.now());
			System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			System.out.println("Below getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			
		//	System.out.println("ProductList = "+ list);
		//	System.out.println("productCount = "+ productCount);
			
/*			for (ProductDto product : list) {
                // Initialize MRP1 list if it is null
                if (product.getMRP1() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                for (ProductDto mrpProduct : mrpList) {
                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
                        product.getMRP1().add(mrpProduct.getMRP());
                    }
                }
            }
 */
		//	System.out.println("ProductList below for loop = "+ list);
//			if(!list.isEmpty()) {
//				status.setCode(RECORD_FOUND);
//				status.setData(list);
//				status.setRecordCount(productCount);
//			}else {
//				status.setCode(RECORD_NOT_FOUND);
//			}
//			
//		}else {
//			System.out.println("Above getMultipleMrpForProduct query call at = "+LocalDateTime.now());
//			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//					requestDto.getProductId(),requestDto.getPriceList());
//			System.out.println("Below getMultipleMrpForProduct query call at = "+LocalDateTime.now());
//			
//			System.out.println("Above getInStockProductWithInventory query call at = "+LocalDateTime.now());
//			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
//			System.out.println("Below getInStockProductWithInventory query call at = "+LocalDateTime.now());
//			System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
//			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
//			System.out.println("Below getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
// 
//			//System.out.print("Hi in prodInvent query");
//			System.out.println("ProductList = "+ list);
//			System.out.println("productCount = "+ productCount);
//			if (list != null) {
//				
//				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
//			//	System.out.println("Above for loop at = "+LocalDateTime.now());
//				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//					
//					ProductDto productDto = (ProductDto) iterator.next();
//					
//					System.out.print(productDto);
//					
//					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
//					//	productDto.setPtrPrice(productDto.getListPrice());
//						productDto.setPtrPrice(productDto.getPtrPrice());
//					}
//					
//					if(productDto.getInventoryQuantity() !=null) {
//					}else {
//						productDto.setInventoryQuantity("0");
//					}
//					
//					productDtoList.add(productDto);
//				}
//				
//				for (ProductDto product : productDtoList) {
//	                // Initialize MRP1 list if it is null
//	                if (product.getMRP1() == null) {
//	                    product.setMRP1(new ArrayList<>());
//	                }
//	                for (ProductDto mrpProduct : mrpList) {
//	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
//	                        product.getMRP1().add(mrpProduct.getMRP());
//	                    }
//	                }
//	            }
//				
//				//System.out.println("Below for loop at = "+LocalDateTime.now());
// 
//				status.setCode(RECORD_FOUND);
//				status.setData(productDtoList);
//				status.setRecordCount(productCount);
//			} else {
//				status.setCode(RECORD_NOT_FOUND);
//			}
//		}
//		}catch(Exception ex) {
//			ex.printStackTrace();
//			System.out.println("In exception....");
//		}
//		System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
//		return status;
//	}
// this end of original code */	

/*	 this is working code comment on 10-july 2024 to call procedure
	@Override
	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetInStockProductAdmin = 0;
		long outQuerygetInStockProductAdmin = 0;
		long inQuerygetInStockProductCountForAdmin = 0;
		long outQuerygetInStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetInStockProductWithInventory = 0;
		long outQuerygetInStockProductWithInventory = 0;
		long inQuerygetInStockProductCountWithInventory = 0;
		long outQuerygetInStockProductCountWithInventory = 0;
		try {
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		System.out.println("Above getUserTypeByUserId query call at = "+LocalDateTime.now());
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
		System.out.println("Below getUserTypeByUserId query call at = "+LocalDateTime.now());
		
		
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
 
	//	System.out.println("mrpList = "+mrpList);
	//	System.out.println("mrpList size = "+mrpList.size());
 
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD) ||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.println("Hi in prodAdmin query");
			inQuerygetInStockProductAdmin = System.currentTimeMillis();
			System.out.println("Above getInStockProductAdmin query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			System.out.println("Below getInStockProductAdmin query call at = "+LocalDateTime.now());
			outQuerygetInStockProductAdmin = System.currentTimeMillis();
			System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			outQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			System.out.println("Below getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			
		//	System.out.println("ProductList = "+ list);
		//	System.out.println("productCount = "+ productCount);
			
		//	System.out.println("ProductList below for loop = "+ list);
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			System.out.println("Above getMultipleMrpForProduct query call at = "+LocalDateTime.now());
			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
					requestDto.getProductId(),requestDto.getPriceList());
			System.out.println("Below getMultipleMrpForProduct query call at = "+LocalDateTime.now());
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			
			System.out.println("Above getInStockProductWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
			outQuerygetInStockProductWithInventory = System.currentTimeMillis();
			System.out.println("Below getInStockProductWithInventory query call at = "+LocalDateTime.now());
			System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
			outQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			System.out.println("Below getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
 
			//System.out.print("Hi in prodInvent query");
			System.out.println("ProductList = "+ list);
			System.out.println("productCount = "+ productCount);
			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
			//	System.out.println("Above for loop at = "+LocalDateTime.now());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
					//	productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDtoList.add(productDto);
				}
				
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
	                for (ProductDto mrpProduct : mrpList) {
	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
	                        product.getMRP1().add(mrpProduct.getMRP());
	                    }
	                }
	            }
				
				//System.out.println("Below for loop at = "+LocalDateTime.now());
 
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("In exception....");
		}
		timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
		timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetInStockProductAdmin,outQuerygetInStockProductAdmin));
		timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
		timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
		timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
		timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
		
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
end of original working code */	
	
// this is working procedure call code	
//	@Override
//	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
//		long methodIn = System.currentTimeMillis();
//		Map<String,String> timeDifference = new HashMap<>();
//		Status status = new Status();
//		Connection con = null;
//		CallableStatement callableStatement = null;
//		try {
//			con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
//			callableStatement = con.prepareCall("{ call LT_GET_INSTOCK_PRODUCT(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
// 
//			Long userId = Long.parseLong(requestDto.getUserId());
//			callableStatement.setLong(1, userId);
//			callableStatement.setInt(2, requestDto.getLimit());
//			callableStatement.setInt(3, requestDto.getOffset());
//			callableStatement.setString(4, requestDto.getSearchField());
//			callableStatement.setString(5, requestDto.getProductId());
//			callableStatement.setString(6, requestDto.getCategoryId());
//			callableStatement.setString(7, requestDto.getDistId());
//			callableStatement.setString(8, requestDto.getOutletId());
//			callableStatement.setString(9, requestDto.getPriceList());
// 
//			boolean result = callableStatement.execute();
// 
//			List<ProductDto> productDataGt = new ArrayList<>();
//			List<ProductDto> multipleMrpGt = new ArrayList<>();
// 
//			productDataGt = ltMastProductDao.getProductListFromProcedure();
//			multipleMrpGt = ltMastProductDao.getMultipleMrpFromProcedure();
//            Long productCount = ltMastProductDao.getProductCountFromProcedure(); 
//			
//			if (productDataGt != null) {
// 
//				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
//				// System.out.println("Above for loop at = "+LocalDateTime.now());
//				for (Iterator iterator = productDataGt.iterator(); iterator.hasNext();) {
// 
//					ProductDto productDto = (ProductDto) iterator.next();
// 
//					System.out.print(productDto);
// 
//					if (productDto.getPtrFlag().equalsIgnoreCase("Y")) {
//						// productDto.setPtrPrice(productDto.getListPrice());
//						productDto.setPtrPrice(productDto.getPtrPrice());
//					}
// 
//					
//					if (productDto.getInventoryQuantity() != null) {
//					} else {
//						productDto.setInventoryQuantity("0");
//					}
// 
//					productDtoList.add(productDto);
//				}
// 
//				for (ProductDto product : productDtoList) {
//					// Initialize MRP1 list if it is null
//					if (product.getMRP1() == null) {
//						product.setMRP1(new ArrayList<>());
//					}
//					for (ProductDto mrpProduct : multipleMrpGt) {
//						if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
//							product.getMRP1().add(mrpProduct.getMRP());
//						}
//					}
//				}
//				status.setCode(RECORD_FOUND);
//				status.setData(productDtoList);
//				status.setRecordCount(productCount);
//				status.setTotalCount(productCount);
//			} else {
//				status.setCode(RECORD_NOT_FOUND);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		long methodOut = System.currentTimeMillis();
//        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
//        status.setTimeDifference(timeDifference);
//		System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
//		return status;
//	}
	
	
	// this below code is for .net query call 
	@Override
	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetInStockProductAdmin = 0;
		long outQuerygetInStockProductAdmin = 0;
		long inQuerygetInStockProductCountForAdmin = 0;
		long outQuerygetInStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetInStockProductWithInventory = 0;
		long outQuerygetInStockProductWithInventory = 0;
		long inQuerygetInStockProductCountWithInventory = 0;
		long outQuerygetInStockProductCountWithInventory = 0;
		try {
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		System.out.println("Above getUserTypeByUserId query call at = "+LocalDateTime.now());
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
		System.out.println("Below getUserTypeByUserId query call at = "+LocalDateTime.now());

 
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD) 
				||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.println("Hi in prodAdmin query");
			inQuerygetInStockProductAdmin = System.currentTimeMillis();
			System.out.println("Above getInStockProductAdmin query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			System.out.println("Below getInStockProductAdmin query call at = "+LocalDateTime.now());
			outQuerygetInStockProductAdmin = System.currentTimeMillis();
			System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			outQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			System.out.println("Below getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}/*else if(userType.equalsIgnoreCase(AREAHEAD)){
			List<ProductDto> list = ltMastProductDao.getInStockProductForAreaHead(requestDto);
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}*/
		else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			System.out.println("Above getMultipleMrpForProduct query call at = "+LocalDateTime.now());
			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
					requestDto.getProductId(),requestDto.getPriceList());
			System.out.println("Below getMultipleMrpForProduct query call at = "+LocalDateTime.now());
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			
			System.out.println("Above getInStockProductWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
			outQuerygetInStockProductWithInventory = System.currentTimeMillis();
			System.out.println("Below getInStockProductWithInventory query call at = "+LocalDateTime.now());
			System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
			outQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			System.out.println("Below getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
 
			//System.out.print("Hi in prodInvent query");
			System.out.println("ProductList = "+ list);
			System.out.println("productCount = "+ productCount);
			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
			//	System.out.println("Above for loop at = "+LocalDateTime.now());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
					//	productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDtoList.add(productDto);
				}
				
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
	                for (ProductDto mrpProduct : mrpList) {
	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
	                        product.getMRP1().add(mrpProduct.getMRP());
	                    }
	                }
	            }
				
				//System.out.println("Below for loop at = "+LocalDateTime.now());
 
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("In exception....");
		}
		timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
		timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetInStockProductAdmin,outQuerygetInStockProductAdmin));
		timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
		timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
		timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
		timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
		
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
	

	@Override
	public Status getInStockProductV1(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetInStockProductAdmin = 0;
		long outQuerygetInStockProductAdmin = 0;
		long inQuerygetInStockProductCountForAdmin = 0;
		long outQuerygetInStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetInStockProductWithInventory = 0;
		long outQuerygetInStockProductWithInventory = 0;
		long inQuerygetInStockProductCountWithInventory = 0;
		long outQuerygetInStockProductCountWithInventory = 0;
		long inQuerygetMultipleMrpForProductV1 = 0;
		long outQuerygetMultipleMrpForProductV1 = 0;
		try {
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		System.out.println("Above getUserTypeByUserId query call at = "+LocalDateTime.now());
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
		System.out.println("Below getUserTypeByUserId query call at = "+LocalDateTime.now());

 
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD) 
				||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.println("Hi in prodAdmin query");
			inQuerygetInStockProductAdmin = System.currentTimeMillis();
			System.out.println("Above getInStockProductAdmin query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			System.out.println("Below getInStockProductAdmin query call at = "+LocalDateTime.now());
			outQuerygetInStockProductAdmin = System.currentTimeMillis();
			System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			outQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			System.out.println("Below getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}/*else if(userType.equalsIgnoreCase(AREAHEAD)){
			List<ProductDto> list = ltMastProductDao.getInStockProductForAreaHead(requestDto);
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}*/
		else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			
			System.out.println("Above getInStockProductWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
			outQuerygetInStockProductWithInventory = System.currentTimeMillis();
			System.out.println("Below getInStockProductWithInventory query call at = "+LocalDateTime.now());
			//System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
			outQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			//System.out.println("Below getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
 
			//System.out.print("Hi in prodInvent query");
			//System.out.println("ProductList = "+ list);
			//System.out.println("productCount = "+ productCount);
			
			List<String> prodIdList = new ArrayList<>();
			List<ProductDto> mrpList = new ArrayList<>();
			if (list != null) {
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
					//	productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDto.setProductId(productDto.getProductId());
					prodIdList.add(productDto.getProductId());
					productDtoList.add(productDto);
				}
//				for (ProductDto product : productDtoList) {
//	                // Initialize MRP1 list if it is null
//	                if (product.getMRP1() == null) {
//	                    product.setMRP1(new ArrayList<>());
//	                }
//	                mrpList = ltMastProductDao.getMultipleMrpForProductV1(product.getProductId(), requestDto.getDistId());
//    				product.setMRP2(mrpList);
//	              }
				
				String ids = prodIdList.stream().map(id ->"'"+ id +"'").collect(Collectors.joining(", "));
				System.out.println("prodIdList = "+ prodIdList);
				inQuerygetMultipleMrpForProductV1 = System.currentTimeMillis();
				System.out.println("before for getMultipleMrpForProductV1 query call = "+ LocalDateTime.now());
				mrpList = ltMastProductDao.getMultipleMrpForProductV1(ids, requestDto.getDistId());
				System.out.println("after for getMultipleMrpForProductV1 query call = "+ LocalDateTime.now());
				outQuerygetMultipleMrpForProductV1 = System.currentTimeMillis();
				System.out.println("before for loop of mrp2 = "+ LocalDateTime.now());
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
	                List<ProductDto>mrpList2 = mrpList.stream().filter(x-> x.getProductId().equalsIgnoreCase(product.getProductId())).collect(Collectors.toList());
                    
	                product.setMRP2(mrpList2);
//	                if(mrpList2.size() == 1) {
//	                	product.setInventoryQuantity(mrpList2.get(0).getInventoryQuantity());
//	                }
                    System.out.println("mrpList2 is = "+mrpList2);
//	                for (ProductDto mrpProduct : mrpList) {
//	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
//	                        product.getMRP1().add(mrpProduct.getMRP());
//	                        List<ProductDto>mrpList2 = mrpList.stream().filter(x-> x.getProductId() == mrpProduct.getProductId()).collect(Collectors.toList());
//	                        product.setMRP2(mrpList2);
//	                    }
//	                }
	            }
				System.out.println("after for loop of mrp2 = "+LocalDateTime.now());
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("In exception....");
		}
		timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
		timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetInStockProductAdmin,outQuerygetInStockProductAdmin));
		timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
		timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
		timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
		timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
		timeDifference.put("QuerygetMultipleMrpForProductV1", timeDiff(inQuerygetMultipleMrpForProductV1,outQuerygetMultipleMrpForProductV1));
		long methodOut = System.currentTimeMillis();
		//System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}

	
	@Override
	public Status getInStockProductV2(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetInStockProductAdmin = 0;
		long outQuerygetInStockProductAdmin = 0;
		long inQuerygetInStockProductCountForAdmin = 0;
		long outQuerygetInStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetInStockProductWithInventory = 0;
		long outQuerygetInStockProductWithInventory = 0;
		long inQuerygetInStockProductCountWithInventory = 0;
		long outQuerygetInStockProductCountWithInventory = 0;
		try {
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		//System.out.println("Above getUserTypeByUserId query call at = "+LocalDateTime.now());
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
		//System.out.println("Below getUserTypeByUserId query call at = "+LocalDateTime.now());

 
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD) 
				||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.println("Hi in prodAdmin query");
			inQuerygetInStockProductAdmin = System.currentTimeMillis();
			//System.out.println("Above getInStockProductAdmin query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			//System.out.println("Below getInStockProductAdmin query call at = "+LocalDateTime.now());
			outQuerygetInStockProductAdmin = System.currentTimeMillis();
			//System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			outQuerygetInStockProductCountForAdmin = System.currentTimeMillis();
			//System.out.println("Below getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}
		else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			
			//System.out.println("Above getInStockProductWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
			outQuerygetInStockProductWithInventory = System.currentTimeMillis();
			//System.out.println("Below getInStockProductWithInventory query call at = "+LocalDateTime.now());
			//System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
			inQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
			outQuerygetInStockProductCountWithInventory = System.currentTimeMillis();
			//System.out.println("Below getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
 
			//System.out.print("Hi in prodInvent query");
			//System.out.println("ProductList = "+ list);
			//System.out.println("productCount = "+ productCount);
			
			List<String> prodIdList = new ArrayList<>();
			List<ProductDto> mrpList = new ArrayList<>();
			if (list != null) {
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
					//	productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDto.setProductId(productDto.getProductId());
					prodIdList.add(productDto.getProductId());
					productDtoList.add(productDto);
				}		
				String ids = prodIdList.stream().map(id ->"'"+ id +"'").collect(Collectors.joining(", "));
				System.out.println("prodIdList = "+ prodIdList);
				mrpList = ltMastProductDao.getMultipleMrpForInStockProductV3(ids, requestDto.getDistId(), requestDto.getPriceList());
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
	                List<ProductDto>mrpList2 = mrpList.stream().filter(x-> x.getProductId().equalsIgnoreCase(product.getProductId())).collect(Collectors.toList());
                    
	                product.setMRP2(mrpList2);
	                if(mrpList2.size() == 1) {
	                	product.setInventoryQuantity(mrpList2.get(0).getInventoryQuantity());
	                	product.setPtrPrice(mrpList2.get(0).getPtrPrice());
	                	product.setListPrice(mrpList2.get(0).getMrp11());
	                }
                    System.out.println("mrpList2 is = "+mrpList2);
	            }
				
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("In exception....");
		}
		timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
		timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetInStockProductAdmin,outQuerygetInStockProductAdmin));
		timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
		timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
		timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
		timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
		
		long methodOut = System.currentTimeMillis();
		//System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
	
	
	public String timeDiff(long startTime,long endTime) {
		// Step 4: Calculate the time difference in milliseconds
        long durationInMillis = endTime - startTime;
 
        // Step 5: Convert the duration into a human-readable format
        long seconds = durationInMillis / 1000;
        long milliseconds = durationInMillis % 1000;
 
        String formattedDuration = String.format(
            "%d seconds, %d milliseconds",
            seconds, milliseconds
        );
		return formattedDuration;
	}
	
/* comment on 19-June-2024 for api time calculation
	public Status getOutOfStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
		
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)||userType.equalsIgnoreCase("ORGANIZATION_USER") ) {
			System.out.print("Hi in prodAdmin query");
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductForAdmin(requestDto);
			Long productCount = ltMastProductDao.getOutOfStockProductCountForAdmin(requestDto);
			
/*			for (ProductDto product : list) {
                // Initialize MRP1 list if it is null
                if (product.getMRP1() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                for (ProductDto mrpProduct : mrpList) {
                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
                        product.getMRP1().add(mrpProduct.getMRP());
                    }
                }
            }
*/			
//			if(!list.isEmpty()) {
//				status.setCode(RECORD_FOUND);
//				status.setData(list);
//				status.setRecordCount(productCount);
//			}else {
//				status.setCode(RECORD_NOT_FOUND);
//			}
//			
//		}else {
//			
//			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//					requestDto.getProductId(),requestDto.getPriceList());
//			List<ProductDto> list = ltMastProductDao.getOutOfStockProductWithInventory(requestDto);
//			Long productCount = ltMastProductDao.getOutOfStockProductCountWithInventory(requestDto);
			//System.out.print("Hi in prodInvent query");
			//System.out.print(list);
//
//			if (list != null) {
//				
//				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
//				
//				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//					
//					ProductDto productDto = (ProductDto) iterator.next();
//					
//					//System.out.print(productDto);
//					
//					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
//						//productDto.setPtrPrice(productDto.getListPrice());
//						productDto.setPtrPrice(productDto.getPtrPrice());
//					}
//					
//					if(productDto.getInventoryQuantity() !=null) {
//					}else {
//						productDto.setInventoryQuantity("0");
//					}
//					
//					productDtoList.add(productDto);
//				}
//				
//				for (ProductDto product : productDtoList) {
//	                // Initialize MRP1 list if it is null
//	                if (product.getMRP1() == null) {
//	                    product.setMRP1(new ArrayList<>());
//	                }
//	                for (ProductDto mrpProduct : mrpList) {
//	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
//	                        product.getMRP1().add(mrpProduct.getMRP());
//	                    }
//	                }
//	            }
//				
//				status.setCode(RECORD_FOUND);
//				status.setData(productDtoList);
//				status.setRecordCount(productCount);
//			} else {
//				status.setCode(RECORD_NOT_FOUND);
//			}
//		}
//		return status;
//	}
//*/

	public Status getOutOfStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetOutOfStockProductForAdmin = 0;
		long outQuerygetOutOfStockProductForAdmin = 0;
		long inQuerygetOutOfStockProductCountForAdmin = 0;
		long outQuerygetOutOfStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetOutOfStockProductWithInventory = 0;
		long outQuerygetOutOfStockProductWithInventory = 0;
		long inQuerygetOutOfStockProductCountWithInventory = 0;
		long outQuerygetOutOfStockProductCountWithInventory = 0;
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
		
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)||userType.equalsIgnoreCase("ORGANIZATION_USER") ) {
			System.out.print("Hi in prodAdmin query");
			inQuerygetOutOfStockProductForAdmin = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductForAdmin(requestDto);
			outQuerygetOutOfStockProductForAdmin = System.currentTimeMillis();
			inQuerygetOutOfStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getOutOfStockProductCountForAdmin(requestDto);
			outQuerygetOutOfStockProductCountForAdmin = System.currentTimeMillis();
/*			for (ProductDto product : list) {
                // Initialize MRP1 list if it is null
                if (product.getMRP1() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                for (ProductDto mrpProduct : mrpList) {
                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
                        product.getMRP1().add(mrpProduct.getMRP());
                    }
                }
            }
*/			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
					requestDto.getProductId(),requestDto.getPriceList());
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			inQuerygetOutOfStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductWithInventory(requestDto);
			outQuerygetOutOfStockProductWithInventory = System.currentTimeMillis();
			inQuerygetOutOfStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getOutOfStockProductCountWithInventory(requestDto);
			outQuerygetOutOfStockProductCountWithInventory = System.currentTimeMillis();
			//System.out.print("Hi in prodInvent query");
			//System.out.print(list);
 
			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					//System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					
					productDtoList.add(productDto);
				}
				
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
	                for (ProductDto mrpProduct : mrpList) {
	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
	                        product.getMRP1().add(mrpProduct.getMRP());
	                    }
	                }
	            }
				
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
        timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
        timeDifference.put("QuerygetOutOfStockProductForAdmin", timeDiff(inQuerygetOutOfStockProductForAdmin,outQuerygetOutOfStockProductForAdmin));
        timeDifference.put("QuerygetOutOfStockProductCountForAdmin", timeDiff(inQuerygetOutOfStockProductCountForAdmin,outQuerygetOutOfStockProductCountForAdmin));
        timeDifference.put("QuerygetMultipleMrpForProduct", timeDiff(inQuerygetMultipleMrpForProduct,outQuerygetMultipleMrpForProduct));
        timeDifference.put("QuerygetOutOfStockProductWithInventory", timeDiff(inQuerygetOutOfStockProductWithInventory,outQuerygetOutOfStockProductWithInventory));
        timeDifference.put("QuerygetOutOfStockProductCountWithInventory", timeDiff(inQuerygetOutOfStockProductCountWithInventory,outQuerygetOutOfStockProductCountWithInventory));
 
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getOutofStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
	
	
	public Status getOutOfStockProductV1(RequestDto requestDto) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetOutOfStockProductForAdmin = 0;
		long outQuerygetOutOfStockProductForAdmin = 0;
		long inQuerygetOutOfStockProductCountForAdmin = 0;
		long outQuerygetOutOfStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetOutOfStockProductWithInventory = 0;
		long outQuerygetOutOfStockProductWithInventory = 0;
		long inQuerygetOutOfStockProductCountWithInventory = 0;
		long outQuerygetOutOfStockProductCountWithInventory = 0;
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
		
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)||userType.equalsIgnoreCase("ORGANIZATION_USER") ) {
			System.out.print("Hi in prodAdmin query");
			inQuerygetOutOfStockProductForAdmin = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductForAdmin(requestDto);
			outQuerygetOutOfStockProductForAdmin = System.currentTimeMillis();
			inQuerygetOutOfStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getOutOfStockProductCountForAdmin(requestDto);
			outQuerygetOutOfStockProductCountForAdmin = System.currentTimeMillis();
/*			for (ProductDto product : list) {
                // Initialize MRP1 list if it is null
                if (product.getMRP1() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                for (ProductDto mrpProduct : mrpList) {
                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
                        product.getMRP1().add(mrpProduct.getMRP());
                    }
                }
            }
*/			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
//			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//					requestDto.getProductId(),requestDto.getPriceList());
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			inQuerygetOutOfStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductWithInventory(requestDto);
			outQuerygetOutOfStockProductWithInventory = System.currentTimeMillis();
			inQuerygetOutOfStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getOutOfStockProductCountWithInventory(requestDto);
			outQuerygetOutOfStockProductCountWithInventory = System.currentTimeMillis();
			//System.out.print("Hi in prodInvent query");
			//System.out.print(list);
			List<String> prodIdList = new ArrayList<>();
			List<ProductDto> mrpList = new ArrayList<>();
			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					//System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					prodIdList.add(productDto.getProductId());
					productDtoList.add(productDto);
				}
				String ids = prodIdList.stream().map(id ->"'"+ id +"'").collect(Collectors.joining(", "));
				mrpList = ltMastProductDao.getMultipleMrpForOutofStockProductV1(ids, requestDto.getDistId(), requestDto.getPriceList());
				
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
//	                for (ProductDto mrpProduct : mrpList) {
//	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
//	                        product.getMRP1().add(mrpProduct.getMRP());
//	                    }
//	                }
	                List<ProductDto>mrpList2 = mrpList.stream().filter(x-> x.getProductId().equalsIgnoreCase(product.getProductId())).collect(Collectors.toList());
	                product.setMRP2(mrpList2);
	                
	                if(mrpList2.size() == 1) {
	                	product.setInventoryQuantity(mrpList2.get(0).getInventoryQuantity());
	                }
	            }
				
                
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
        timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
        timeDifference.put("QuerygetOutOfStockProductForAdmin", timeDiff(inQuerygetOutOfStockProductForAdmin,outQuerygetOutOfStockProductForAdmin));
        timeDifference.put("QuerygetOutOfStockProductCountForAdmin", timeDiff(inQuerygetOutOfStockProductCountForAdmin,outQuerygetOutOfStockProductCountForAdmin));
        timeDifference.put("QuerygetMultipleMrpForProduct", timeDiff(inQuerygetMultipleMrpForProduct,outQuerygetMultipleMrpForProduct));
        timeDifference.put("QuerygetOutOfStockProductWithInventory", timeDiff(inQuerygetOutOfStockProductWithInventory,outQuerygetOutOfStockProductWithInventory));
        timeDifference.put("QuerygetOutOfStockProductCountWithInventory", timeDiff(inQuerygetOutOfStockProductCountWithInventory,outQuerygetOutOfStockProductCountWithInventory));
 
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getOutofStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}

	
	public Status getOutOfStockProductV2(RequestDto requestDto) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetOutOfStockProductForAdmin = 0;
		long outQuerygetOutOfStockProductForAdmin = 0;
		long inQuerygetOutOfStockProductCountForAdmin = 0;
		long outQuerygetOutOfStockProductCountForAdmin = 0;
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		long inQuerygetOutOfStockProductWithInventory = 0;
		long outQuerygetOutOfStockProductWithInventory = 0;
		long inQuerygetOutOfStockProductCountWithInventory = 0;
		long outQuerygetOutOfStockProductCountWithInventory = 0;
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		inQuerygetUserTypeByUserId = System.currentTimeMillis();
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		outQuerygetUserTypeByUserId = System.currentTimeMillis();
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
		
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)||userType.equalsIgnoreCase("ORGANIZATION_USER") ) {
			System.out.print("Hi in prodAdmin query");
			inQuerygetOutOfStockProductForAdmin = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductForAdmin(requestDto);
			outQuerygetOutOfStockProductForAdmin = System.currentTimeMillis();
			inQuerygetOutOfStockProductCountForAdmin = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getOutOfStockProductCountForAdmin(requestDto);
			outQuerygetOutOfStockProductCountForAdmin = System.currentTimeMillis();
/*			for (ProductDto product : list) {
                // Initialize MRP1 list if it is null
                if (product.getMRP1() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                for (ProductDto mrpProduct : mrpList) {
                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
                        product.getMRP1().add(mrpProduct.getMRP());
                    }
                }
            }
*/			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
//			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//					requestDto.getProductId(),requestDto.getPriceList());
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			inQuerygetOutOfStockProductWithInventory = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductWithInventory(requestDto);
			outQuerygetOutOfStockProductWithInventory = System.currentTimeMillis();
			inQuerygetOutOfStockProductCountWithInventory = System.currentTimeMillis();
			Long productCount = ltMastProductDao.getOutOfStockProductCountWithInventory(requestDto);
			outQuerygetOutOfStockProductCountWithInventory = System.currentTimeMillis();
			//System.out.print("Hi in prodInvent query");
			//System.out.print(list);
			List<String> prodIdList = new ArrayList<>();
			List<ProductDto> mrpList = new ArrayList<>();
			if (list != null) {
				
				List<ProductDto> productDtoList = new ArrayList<ProductDto>();
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					ProductDto productDto = (ProductDto) iterator.next();
					
					//System.out.print(productDto);
					
					if(productDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//productDto.setPtrPrice(productDto.getListPrice());
						productDto.setPtrPrice(productDto.getPtrPrice());
					}
					
					if(productDto.getInventoryQuantity() !=null) {
					}else {
						productDto.setInventoryQuantity("0");
					}
					prodIdList.add(productDto.getProductId());
					productDtoList.add(productDto);
				}
				String ids = prodIdList.stream().map(id ->"'"+ id +"'").collect(Collectors.joining(", "));
				mrpList = ltMastProductDao.getMultipleMrpForOutofStockProductV2(ids, requestDto.getDistId(), requestDto.getPriceList());
				
				for (ProductDto product : productDtoList) {
	                // Initialize MRP1 list if it is null
	                if (product.getMRP1() == null) {
	                    product.setMRP1(new ArrayList<>());
	                }
//	                for (ProductDto mrpProduct : mrpList) {
//	                    if (product.getProductId().equalsIgnoreCase(mrpProduct.getProductId())) {
//	                        product.getMRP1().add(mrpProduct.getMRP());
//	                    }
//	                }
	                List<ProductDto>mrpList2 = mrpList.stream().filter(x-> x.getProductId().equalsIgnoreCase(product.getProductId())).collect(Collectors.toList());
	                product.setMRP2(mrpList2);
	                
	                if(mrpList2.size() == 1) {
	                	product.setInventoryQuantity(mrpList2.get(0).getInventoryQuantity());
	                	product.setPtrPrice(mrpList2.get(0).getPtrPrice());
	                	product.setListPrice(mrpList2.get(0).getMrp11());
	                }
	            }
				
                
				status.setCode(RECORD_FOUND);
				status.setData(productDtoList);
				status.setRecordCount(productCount);
			} else {
				status.setCode(RECORD_NOT_FOUND);
			}
		}
        timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
        timeDifference.put("QuerygetOutOfStockProductForAdmin", timeDiff(inQuerygetOutOfStockProductForAdmin,outQuerygetOutOfStockProductForAdmin));
        timeDifference.put("QuerygetOutOfStockProductCountForAdmin", timeDiff(inQuerygetOutOfStockProductCountForAdmin,outQuerygetOutOfStockProductCountForAdmin));
        timeDifference.put("QuerygetMultipleMrpForProduct", timeDiff(inQuerygetMultipleMrpForProduct,outQuerygetMultipleMrpForProduct));
        timeDifference.put("QuerygetOutOfStockProductWithInventory", timeDiff(inQuerygetOutOfStockProductWithInventory,outQuerygetOutOfStockProductWithInventory));
        timeDifference.put("QuerygetOutOfStockProductCountWithInventory", timeDiff(inQuerygetOutOfStockProductCountWithInventory,outQuerygetOutOfStockProductCountWithInventory));
 
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getOutofStockProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}

	
/* this is comment on 19-June-2024 for api time calculation
	@Override
	public Status getMultipleMrpForProduct(String distId, String outId, String prodId, String priceList)
			throws ServiceException, IOException {
		       Status status= new Status();
		try{
			List<ProductDto> list = ltMastProductDao.getMultipleMrpForProduct(distId,outId,prodId,priceList);
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
			}else 
			{
			  status.setCode(RECORD_NOT_FOUND);
			}
			}catch(Exception e) 
		     {
				e.printStackTrace();
				return null;
			 }
		return status;		
	}
*/
	@Override
	public Status getMultipleMrpForProduct(String distId, String outId, String prodId, String priceList)
			throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method getMultipleMrpForProduct at "+LocalDateTime.now());
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		Map<String,String> timeDifference = new HashMap<>();
		Status status= new Status();
		try{
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getMultipleMrpForProduct(distId,outId,prodId,priceList);
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
			}else
			{
			  status.setCode(RECORD_NOT_FOUND);
			}
			}catch(Exception e)
		     {
				e.printStackTrace();
				return null;
			 }
		
        timeDifference.put("QuerygetMultipleMrpForProduct", timeDiff(inQuerygetMultipleMrpForProduct,outQuerygetMultipleMrpForProduct));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getMultipleMrpForProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;		
	}
	
	
	@Override
	public Status getMultipleMrpForProductV1(String prodId, String distId)
			throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method getMultipleMrpForProductV1 at "+LocalDateTime.now());
		long inQuerygetMultipleMrpForProduct = 0;
		long outQuerygetMultipleMrpForProduct = 0;
		Map<String,String> timeDifference = new HashMap<>();
		Status status= new Status();
		try{
			inQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			List<ProductDto> list = ltMastProductDao.getMultipleMrpForProductV1(prodId, distId);
			outQuerygetMultipleMrpForProduct = System.currentTimeMillis();
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
			}else
			{
			  status.setCode(RECORD_NOT_FOUND);
			}
			}catch(Exception e)
		     {
				e.printStackTrace();
				return null;
			 }
		
        timeDifference.put("QuerygetMultipleMrpForProduct", timeDiff(inQuerygetMultipleMrpForProduct,outQuerygetMultipleMrpForProduct));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getMultipleMrpForProduct at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;		
	}
	
/*	@Override   this is comment on 19-June-2024 for api time calculation
	public Status getTlForProductDescription(String priceList, String productId) throws ServiceException, IOException {
		    Status status= new Status();
		try {
			List<TlEtlDto> tlList = ltMastProductDao.getTlForProductDescription(priceList, productId);
			if(!tlList.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(tlList);
			 }else {
				status.setCode(RECORD_NOT_FOUND);
			  }
			} catch(Exception e) 
		      {e.printStackTrace();}
		return status;
	}
*/
	
	@Override
	public Status getTlForProductDescription(String priceList, String productId) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method getTlForProductDescription at "+LocalDateTime.now());
		long inQuerygetTlForProductDescription= 0;
		long outQuerygetTlForProductDescription = 0;
		Map<String,String> timeDifference = new HashMap<>();
		    Status status= new Status();
		try {
			inQuerygetTlForProductDescription = System.currentTimeMillis();
			List<TlEtlDto> tlList = ltMastProductDao.getTlForProductDescription(priceList, productId);
			outQuerygetTlForProductDescription = System.currentTimeMillis();
			if(!tlList.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(tlList);
			 }else {
				status.setCode(RECORD_NOT_FOUND);
			  }
			} catch(Exception e)
		      {e.printStackTrace();}
		timeDifference.put("QuerygetTlForProductDescription", timeDiff(inQuerygetTlForProductDescription,outQuerygetTlForProductDescription));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getTlForProductDescription at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
	
/*	this is comment on 19-June-2024 for api time calculation
	@Override
	public Status getEtlForProductDescription(String priceList, String productId) throws ServiceException, IOException {
		Status status= new Status();
		try {
			List<TlEtlDto> tlList = ltMastProductDao.getEtlForProductDescription(priceList, productId);
			if(!tlList.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(tlList);
			 }else {
				status.setCode(RECORD_NOT_FOUND);
			  }
			} catch(Exception e) 
		      {e.printStackTrace();}
		return status;
	}
*/	
	@Override
	public Status getEtlForProductDescription(String priceList, String productId) throws ServiceException, IOException {
		long methodIn = System.currentTimeMillis();
		System.out.println("In method getEtlForProductDescription at "+LocalDateTime.now());
		long inQuerygetEtlForProductDescription= 0;
		long outQuerygetEtlForProductDescription = 0;
		Map<String,String> timeDifference = new HashMap<>();
		Status status= new Status();
		try {
			inQuerygetEtlForProductDescription = System.currentTimeMillis();
			List<TlEtlDto> tlList = ltMastProductDao.getEtlForProductDescription(priceList, productId);
			outQuerygetEtlForProductDescription = System.currentTimeMillis();
			if(!tlList.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(tlList);
			 }else {
				status.setCode(RECORD_NOT_FOUND);
			  }
			} catch(Exception e)
		      {e.printStackTrace();}
		timeDifference.put("QuerygetTlForProductDescription", timeDiff(inQuerygetEtlForProductDescription,outQuerygetEtlForProductDescription));
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getEtlForProductDescription at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		return status;
	}
	
}
