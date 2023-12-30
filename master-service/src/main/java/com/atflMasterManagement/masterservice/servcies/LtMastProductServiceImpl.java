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
		return status;
	}

	@Override
	public Status getInStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		
		if(userType.equalsIgnoreCase(ADMIN)||userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)) {
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
			List<ProductDto> list = ltMastProductDao.getInStockProductWithInventory(requestDto);
			Long productCount = ltMastProductDao.getInStockProductCountWithInventory(requestDto);
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
		return status;
	}
	
	@Override
	public Status getOutOfStockProduct(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		
		String userType =  ltMastProductDao.getUserTypeByUserId(requestDto.getUserId());
		
		if(userType.equalsIgnoreCase(ADMIN)||userType.equalsIgnoreCase(SALESOFFICER)||userType.equalsIgnoreCase(AREAHEAD)) {
			System.out.print("Hi in prodAdmin query");
			List<ProductDto> list = ltMastProductDao.getOutOfStockProductForAdmin(requestDto);
			Long productCount = ltMastProductDao.getOutOfStockProductCountForAdmin(requestDto);
			
			if(!list.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setData(list);
				status.setRecordCount(productCount);
			}else {
				status.setCode(RECORD_NOT_FOUND);
			}
			
		}else {
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
		return status;
	}
	
}
