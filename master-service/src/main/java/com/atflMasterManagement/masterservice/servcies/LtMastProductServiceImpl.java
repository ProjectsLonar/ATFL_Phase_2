package com.atflMasterManagement.masterservice.servcies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Override
	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		
		try {
		
		//System.out.println("In method getInStockProduct at = "+LocalDateTime.now());
		//System.out.println("Above getUserTypeByUserId query call at = "+LocalDateTime.now());
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		//System.out.println("Below getUserTypeByUserId query call at = "+LocalDateTime.now());
		
		
//		List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
//				requestDto.getProductId(),requestDto.getPriceList());
 
	//	System.out.println("mrpList = "+mrpList);
	//	System.out.println("mrpList size = "+mrpList.size());
 
		if(userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD) ||userType.equalsIgnoreCase("ORGANIZATION_USER")) {
			System.out.println("Hi in prodAdmin query");
			//System.out.println("Above getInStockProductAdmin query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductAdmin(requestDto);
			//System.out.println("Below getInStockProductAdmin query call at = "+LocalDateTime.now());
			//System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			Long productCount = ltMastProductDao.getInStockProductCountForAdmin(requestDto);
			//System.out.println("Above getInStockProductCountForAdmin query call at = "+LocalDateTime.now());
			
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
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
					requestDto.getProductId(),requestDto.getPriceList());
			
		//	System.out.println("Above getInStockProductWithInventory query call at = "+LocalDateTime.now());
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
		//	System.out.println("Below getInStockProductWithInventory query call at = "+LocalDateTime.now());
		//	System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
		//	System.out.println("Above getInStockProductCountWithInventory query call at = "+LocalDateTime.now());
 
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
		//System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
		return status;
	}
	
	@Override
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
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
			
			List<ProductDto> mrpList = ltMastProductDao.getMultipleMrpForProduct(requestDto.getDistId(),requestDto.getOutletId(),
					requestDto.getProductId(),requestDto.getPriceList());
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductWithInventory(requestDto);
			Long productCount = ltMastProductDao.getOutOfStockProductCountWithInventory(requestDto);
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
		return status;
	}

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

	@Override
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
	
}
