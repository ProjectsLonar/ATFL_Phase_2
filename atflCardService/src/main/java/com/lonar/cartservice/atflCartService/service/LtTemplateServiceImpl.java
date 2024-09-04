package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dao.LtTemplateDao;
import com.lonar.cartservice.atflCartService.dto.LtTemplateDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtTemplateHeaders;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.repository.LtTemplateHeadersRepository;
import com.lonar.cartservice.atflCartService.repository.LtTemplateLinesRepository;

@Service
@Transactional
public class LtTemplateServiceImpl implements LtTemplateService,CodeMaster {

	@Autowired
	LtTemplateDao ltTemplateDao;
	
	
	
	@Override
	@Transactional
	public Status getTemplateAgainstDistributor(String distributorId, Long templateHeaderId)throws ServerException{

		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetTemplateAgainstDistributor = 0;
		long outQuerygetTemplateAgainstDistributor = 0;
		long inQuerygetProductDetailsAgainstheaderId = 0;
		long outQuerygetProductDetailsAgainstheaderId = 0;
//		long inQuerygetInStockProductCountForAdmin = 0;
//		long outQuerygetInStockProductCountForAdmin = 0;
//		long inQuerygetMultipleMrpForProduct = 0;
//		long outQuerygetMultipleMrpForProduct = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		
		try {
			inQuerygetTemplateAgainstDistributor = System.currentTimeMillis();
		LtTemplateHeaders templateHeaders = ltTemplateDao.getTemplateAgainstDistributor(distributorId,templateHeaderId);
		outQuerygetTemplateAgainstDistributor = System.currentTimeMillis();
		
		List<LtTemplateLines> templateLineDetails = new ArrayList<LtTemplateLines>();
		LtTemplateDto ltTemplateDto = new LtTemplateDto();
		if(templateHeaders !=null) {
			
			inQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis();
			templateLineDetails =ltTemplateDao.getProductDetailsAgainstheaderId(templateHeaders.getTemplateHeaderId());
			outQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis();
			
			ltTemplateDto.setTemplateHeaderId(templateHeaders.getTemplateHeaderId());
			ltTemplateDto.setDistributorId(templateHeaders.getDistributorId());
			ltTemplateDto.setStatus(templateHeaders.getStatus());
			ltTemplateDto.setCreatedBy(templateHeaders.getCreatedBy());
			ltTemplateDto.setCreationDate(templateHeaders.getCreationDate());
			ltTemplateDto.setLastUpdatedBy(templateHeaders.getLastUpdatedBy());
			ltTemplateDto.setLastUpdatedLogin(templateHeaders.getLastUpdatedLogin());
			ltTemplateDto.setLastUpdatedDate(templateHeaders.getLastUpdatedDate());
						
			if(!templateLineDetails.isEmpty()) {
				ltTemplateDto.setLtTemplateLines(templateLineDetails);
			}else {
				
			}status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
	
		if (ltTemplateDto != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(ltTemplateDto);
			
			timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetTemplateAgainstDistributor,outQuerygetTemplateAgainstDistributor));
			timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetProductDetailsAgainstheaderId,outQuerygetProductDetailsAgainstheaderId));
//			timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
//			timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
//			timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//			timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
			
			long methodOut = System.currentTimeMillis();
			System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
	        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	        status.setTimeDifference(timeDifference);
	        
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;	
	
	}
	
	@Override
	
	public Status createTemplate(LtTemplateDto ltTemplateDto)throws ServerException, ServiceException{
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetTemplateAgainstDistributor12 = 0;
		long outQuerygetTemplateAgainstDistributor12 = 0;
		long inQuerydeletelinedetailsbytemplateid = 0;
		long outQuerydeletelinedetailsbytemplateid = 0;
		long inQuerydeleteHeaderdetailsbytemplateid = 0;
		long outQuerydeleteHeaderdetailsbytemplateid = 0;
		long inQuerygetTemplateAgainstDistributor = 0;
		long outQuerygetTemplateAgainstDistributor = 0;
		long inQuerygetProductDetailsAgainstheaderId = 0;
		long outQuerygetProductDetailsAgainstheaderId = 0;
		long inQuerydeletelinedetailsbytemplateid2 = 0;
		long outQuerydeletelinedetailsbytemplateid2 = 0;
		try {
		if(ltTemplateDto !=null) {
			LtTemplateHeaders ltTemplateHeaders = new LtTemplateHeaders();
			LtTemplateHeaders ltTemplateHeadersUpdated = null;
			
			inQuerygetTemplateAgainstDistributor12 = System.currentTimeMillis();
			LtTemplateHeaders ltTemplateHeader12 = ltTemplateDao.getTemplateAgainstDistributor12(ltTemplateDto.getDistributorId());
			inQuerygetTemplateAgainstDistributor12 = System.currentTimeMillis();
			
			if((ltTemplateHeader12 !=null)){				
			//ltTemplateDto.setTemplateHeaderId(ltTemplateHeader12.getTemplateHeaderId());
				inQuerydeletelinedetailsbytemplateid = System.currentTimeMillis();
				ltTemplateDao.deletelinedetailsbytemplateid(ltTemplateHeader12.getTemplateHeaderId());
				outQuerydeletelinedetailsbytemplateid = System.currentTimeMillis();
				
				inQuerydeleteHeaderdetailsbytemplateid = System.currentTimeMillis();
				ltTemplateDao.deleteHeaderdetailsbytemplateid(ltTemplateHeader12.getDistributorId());
				outQuerydeleteHeaderdetailsbytemplateid = System.currentTimeMillis();
				
			ltTemplateDto.setTemplateHeaderId(null);
			}
			else {
				ltTemplateDto.setTemplateHeaderId(null);
			}
			
		//	System.out.println("Disrtibutor Id ===== "+ ltTemplateHeader12.getDistributorId());
			if(ltTemplateDto.getTemplateHeaderId() ==null) {
				//System.out.println("Hi I'm in if ");
				//System.out.println("Checking Disrtibutor Id ===== "+ ltTemplateDto.getDistributorId() +"&&&" + ltTemplateHeader12.getDistributorId());
//			if(ltTemplateDto.getDistributorId()== ltTemplateHeader12.getDistributorId()){
//				ltTemplateDao.deleteHeaderdetailsbytemplateid(ltTemplateDto.getDistributorId());
//				ltTemplateDao.deletelinedetailsbytemplateid(ltTemplateHeader12.getTemplateHeaderId());
//			}
				
			ltTemplateHeaders.setDistributorId(ltTemplateDto.getDistributorId());
			ltTemplateHeaders.setCreationDate(new Date());
			ltTemplateHeaders.setCreatedBy(ltTemplateDto.getUserId());
			ltTemplateHeaders.setLastUpdatedBy(ltTemplateDto.getUserId());
			ltTemplateHeaders.setLastUpdatedDate(new Date());
			ltTemplateHeaders.setLastUpdatedLogin(ltTemplateDto.getUserId());
			ltTemplateHeaders.setProductCount(ltTemplateDto.getLtTemplateLines().size());
			ltTemplateHeadersUpdated = ltTemplateDao.saveheaderData(ltTemplateHeaders);
			
			}
		else {System.out.println("Hi I'm in else ");
			
			System.out.println("Hi input Data......."+ ltTemplateDto.getDistributorId()+" Hi New data"+
					ltTemplateDto.getTemplateHeaderId());
			inQuerygetTemplateAgainstDistributor = System.currentTimeMillis();
				LtTemplateHeaders ltTemplateHeader = ltTemplateDao.getTemplateAgainstDistributor(ltTemplateDto.getDistributorId(),
														ltTemplateDto.getTemplateHeaderId());
				outQuerygetTemplateAgainstDistributor = System.currentTimeMillis();

				//System.out.println("QueryHeaderId= "+ltTemplateHeader.getTemplateHeaderId());
				//System.out.println("ReqHeaderId"+ltTemplateDto.getTemplateHeaderId() + "\n"+ltTemplateHeader +"\n" +ltTemplateDto);
				ltTemplateHeaders.setDistributorId(ltTemplateDto.getDistributorId());
				System.out.println("Hi Data......."+ ltTemplateHeader);
				System.out.println("Null data..."+ltTemplateHeader.getCreationDate());
				
				if(ltTemplateHeader.getCreationDate()!= null) {
				ltTemplateHeaders.setCreationDate(ltTemplateHeader.getCreationDate());}
				ltTemplateHeaders.setCreatedBy(ltTemplateHeader.getCreatedBy());
				ltTemplateHeaders.setLastUpdatedBy(ltTemplateDto.getUserId());
				ltTemplateHeaders.setLastUpdatedDate(new Date());
				ltTemplateHeaders.setLastUpdatedLogin(ltTemplateDto.getUserId());
				ltTemplateHeaders.setProductCount(ltTemplateDto.getLtTemplateLines().size());
				ltTemplateHeaders.setTemplateHeaderId(ltTemplateDto.getTemplateHeaderId());
				ltTemplateHeadersUpdated = ltTemplateDao.saveheaderData(ltTemplateHeaders);
			}
			if(ltTemplateHeadersUpdated !=null) {
				inQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis();
				List<LtTemplateLines> productList = ltTemplateDao.getProductDetailsAgainstheaderId(ltTemplateHeadersUpdated.getTemplateHeaderId());
				outQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis();

				if(productList !=null) {
					inQuerydeletelinedetailsbytemplateid2 = System.currentTimeMillis();
				ltTemplateDao.deletelinedetailsbytemplateid(ltTemplateHeadersUpdated.getTemplateHeaderId());
				outQuerydeletelinedetailsbytemplateid2 = System.currentTimeMillis();
				}
				
				
				List<LtTemplateLines> lineData = ltTemplateDto.getLtTemplateLines();
				
				for(LtTemplateLines lines:lineData) {
					LtTemplateLines ltTemplateLines = new LtTemplateLines();
					//Long availableQuantity = ltTemplateDao.getAvailableQuantity(ltTemplateDto.getDistributorId(),lines.getProductId());
					ltTemplateLines.setTemplateHeaderId(ltTemplateHeadersUpdated.getTemplateHeaderId());
					ltTemplateLines.setSalesType(lines.getSalesType());
					ltTemplateLines.setProductId(lines.getProductId());
					ltTemplateLines.setProductName(lines.getProductName());
					ltTemplateLines.setProductDescription(lines.getProductDescription());
					ltTemplateLines.setPtrPrice(lines.getPtrPrice());
					ltTemplateLines.setQuantity(lines.getQuantity());
					ltTemplateLines.setAvailableQuantity(lines.getAvailableQuantity());  //comment on 22-may-2024
				//	ltTemplateLines.setAvailableQuantity(availableQuantity);
					LtTemplateLines ltTemplateLinesupdated = ltTemplateDao.saveLineData(ltTemplateLines);
				}
			}
			Status status1 = getTemplateAgainstDistributor(ltTemplateHeadersUpdated.getDistributorId(), ltTemplateHeadersUpdated.getTemplateHeaderId());
			
			if(ltTemplateHeadersUpdated !=null) {
				status.setCode(INSERT_SUCCESSFULLY);
				status.setMessage("INSERT_SUCCESSFULLY");
				status.setData(status1.getData());
				
				timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetTemplateAgainstDistributor12,outQuerygetTemplateAgainstDistributor12));
				timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerydeletelinedetailsbytemplateid,outQuerydeletelinedetailsbytemplateid));
				timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerydeleteHeaderdetailsbytemplateid, outQuerydeleteHeaderdetailsbytemplateid));
				timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetTemplateAgainstDistributor, outQuerygetTemplateAgainstDistributor));
				timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetProductDetailsAgainstheaderId,outQuerygetProductDetailsAgainstheaderId));
				timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerydeletelinedetailsbytemplateid2,outQuerydeletelinedetailsbytemplateid2));
				
				long methodOut = System.currentTimeMillis();
				System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
		        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
		        status.setTimeDifference(timeDifference);
				
			}else {
				status.setCode(INSERT_FAIL);
				status.setMessage("Fail To Insert");
			}
		}
	
		}catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	
	@Override
	@Transactional
	public Status getTemplateAgainstDistributors (String distributorId)throws ServerException{

		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetTemplateAgainstDistributors = 0;
		long outQuerygetTemplateAgainstDistributors = 0;
		long inQuerygetProductDetailsAgainstheaderId = 0;
		long outQuerygetProductDetailsAgainstheaderId = 0;
		long inQuerygetAllTemplateAgainstDistributors = 0;
		long outQuerygetAllTemplateAgainstDistributors = 0;
		long inQuerygetProductDetailsAgainstheaderId2 = 0;
		long outQuerygetProductDetailsAgainstheaderId2 = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		
		try {
			System.out.println("distributorId"+distributorId);
			inQuerygetTemplateAgainstDistributors = System.currentTimeMillis();
		LtTemplateHeaders templateHeaders = ltTemplateDao.getTemplateAgainstDistributors(distributorId);
		outQuerygetTemplateAgainstDistributors = System.currentTimeMillis();
		System.out.println(templateHeaders);
		List<LtTemplateLines> templateLineDetails = new ArrayList<LtTemplateLines>();
		LtTemplateDto ltTemplateDto = new LtTemplateDto();
		if(templateHeaders !=null) {
			
			inQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis();
			templateLineDetails =ltTemplateDao.getProductDetailsAgainstheaderId(templateHeaders.getTemplateHeaderId());
			outQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis();
			
			System.out.println(templateLineDetails);
			ltTemplateDto.setTemplateHeaderId(templateHeaders.getTemplateHeaderId());
			ltTemplateDto.setDistributorId(templateHeaders.getDistributorId());
			ltTemplateDto.setStatus(templateHeaders.getStatus());
			ltTemplateDto.setCreatedBy(templateHeaders.getCreatedBy());
			ltTemplateDto.setCreationDate(templateHeaders.getCreationDate());
			ltTemplateDto.setLastUpdatedBy(templateHeaders.getLastUpdatedBy());
			ltTemplateDto.setLastUpdatedLogin(templateHeaders.getLastUpdatedLogin());
			ltTemplateDto.setLastUpdatedDate(templateHeaders.getLastUpdatedDate());
			
			if(templateLineDetails!= null && !templateLineDetails.isEmpty()) {
				ltTemplateDto.setLtTemplateLines(templateLineDetails);
			}else {
				//ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}
			/*if( templateLineDetails == null && templateLineDetails.isEmpty()) {
				ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}else {
			ltTemplateDto.setLtTemplateLines(templateLineDetails);
		    }*/
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}else {
			String distId = "ALL";
			outQuerygetAllTemplateAgainstDistributors = System.currentTimeMillis();
		    LtTemplateHeaders getAllTemplateHeaders = ltTemplateDao.getAllTemplateAgainstDistributors(distId);
			outQuerygetAllTemplateAgainstDistributors = System.currentTimeMillis(); 
			
			inQuerygetProductDetailsAgainstheaderId2 = System.currentTimeMillis();
		    templateLineDetails =ltTemplateDao.getProductDetailsAgainstheaderId(getAllTemplateHeaders.getTemplateHeaderId());
			inQuerygetProductDetailsAgainstheaderId2 = System.currentTimeMillis();

		    System.out.println(templateLineDetails);
		    ltTemplateDto.setTemplateHeaderId(getAllTemplateHeaders.getTemplateHeaderId());
		    ltTemplateDto.setDistributorId(getAllTemplateHeaders.getDistributorId());
		    ltTemplateDto.setStatus(getAllTemplateHeaders.getStatus());
		    ltTemplateDto.setCreatedBy(getAllTemplateHeaders.getCreatedBy());
		    ltTemplateDto.setCreationDate(getAllTemplateHeaders.getCreationDate());
		    ltTemplateDto.setLastUpdatedBy(getAllTemplateHeaders.getLastUpdatedBy());
		    ltTemplateDto.setLastUpdatedLogin(getAllTemplateHeaders.getLastUpdatedLogin());
		    ltTemplateDto.setLastUpdatedDate(getAllTemplateHeaders.getLastUpdatedDate());
		
		    
		    List<String> prodIdList = new ArrayList<>();
			prodIdList = templateLineDetails.stream().map(LtTemplateLines::getProductId).collect(Collectors.toList());
			String ids = prodIdList.stream().map(id->"'"+id +"'").collect(Collectors.joining(", "));
			List<LtTemplateLines> mrpList = new ArrayList<>();
			mrpList = ltTemplateDao.getMultipleMrpForTemplateProductV1(ids, distributorId);
			
			for (LtTemplateLines product : templateLineDetails) {
                // Initialize MRP1 list if it is null
                if (product.getListPrice() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                List<LtTemplateLines>mrpList2 = mrpList.stream().filter(x-> x.getProductId().equalsIgnoreCase(product.getProductId())).collect(Collectors.toList());
                
                product.setMRP2(mrpList2);
                if(mrpList2.size()> 1) {
                	//product.setAvailableQuantity(mrpList2.get(0).getAvailableQuantity());
                	product.setAvailableQuantity(mrpList2.get(0).getInventoryQuantity());
                }
                product.setAvailableQuantity(mrpList2.get(0).getInventoryQuantity());
                System.out.println("mrpList2 is = "+mrpList2);
            }
		    
		    
		    if(templateLineDetails!= null && !templateLineDetails.isEmpty()) {
		    	ltTemplateDto.setLtTemplateLines(templateLineDetails);
		    }else {
		    	//ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
		    }
		    	/*if( templateLineDetails == null && templateLineDetails.isEmpty()) {
				ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}else {
				ltTemplateDto.setLtTemplateLines(templateLineDetails);
	    	}*/
		    status.setCode(FAIL);
		    status.setMessage("RECORD NOT FOUND");
		}
	
		if (ltTemplateDto != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(ltTemplateDto);
			
			timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetTemplateAgainstDistributors,outQuerygetTemplateAgainstDistributors));
			timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetProductDetailsAgainstheaderId,outQuerygetProductDetailsAgainstheaderId));
			timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetAllTemplateAgainstDistributors, outQuerygetAllTemplateAgainstDistributors));
			timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetProductDetailsAgainstheaderId2, outQuerygetProductDetailsAgainstheaderId2));
//			timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//			timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
			
			long methodOut = System.currentTimeMillis();
			System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
	        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	        status.setTimeDifference(timeDifference);
			
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
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

	@Override
	@Transactional
	public Status getTemplateAgainstDistributors (String distributorId, String priceList)throws ServerException{

		Status status = new Status();
		
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetTemplateAgainstDistributors = 0;
		long outQuerygetTemplateAgainstDistributors = 0;
		long inQuerygetProductDetailsAgainstheaderId = 0;
		long outQuerygetProductDetailsAgainstheaderId = 0;
		long inQuerygetAllTemplateAgainstDistributors = 0;
		long outQuerygetAllTemplateAgainstDistributors = 0;
		long inQuerygetProductDetailsAgainstheaderId2 = 0;
		long outQuerygetProductDetailsAgainstheaderId2 = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		try {
			System.out.println("distributorId"+distributorId);
			inQuerygetTemplateAgainstDistributors = System.currentTimeMillis();
		LtTemplateHeaders templateHeaders = ltTemplateDao.getTemplateAgainstDistributors(distributorId);
		outQuerygetTemplateAgainstDistributors = System.currentTimeMillis();

		System.out.println(templateHeaders);
		List<LtTemplateLines> templateLineDetails = new ArrayList<LtTemplateLines>();
		LtTemplateDto ltTemplateDto = new LtTemplateDto();
		if(templateHeaders !=null) {
			inQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis(); 
			templateLineDetails =ltTemplateDao.getProductDetailsAgainstheaderId(templateHeaders.getTemplateHeaderId(), priceList);
			outQuerygetProductDetailsAgainstheaderId = System.currentTimeMillis(); 

			System.out.println(templateLineDetails);
			ltTemplateDto.setTemplateHeaderId(templateHeaders.getTemplateHeaderId());
			ltTemplateDto.setDistributorId(templateHeaders.getDistributorId());
			ltTemplateDto.setStatus(templateHeaders.getStatus());
			ltTemplateDto.setCreatedBy(templateHeaders.getCreatedBy());
			ltTemplateDto.setCreationDate(templateHeaders.getCreationDate());
			ltTemplateDto.setLastUpdatedBy(templateHeaders.getLastUpdatedBy());
			ltTemplateDto.setLastUpdatedLogin(templateHeaders.getLastUpdatedLogin());
			ltTemplateDto.setLastUpdatedDate(templateHeaders.getLastUpdatedDate());
			
			if(templateLineDetails!= null && !templateLineDetails.isEmpty()) {
				ltTemplateDto.setLtTemplateLines(templateLineDetails);
			}else {
				//ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}
			/*if( templateLineDetails == null && templateLineDetails.isEmpty()) {
				ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}else {
			ltTemplateDto.setLtTemplateLines(templateLineDetails);
		    }*/
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}else {
			String distId = "ALL";
			inQuerygetAllTemplateAgainstDistributors = System.currentTimeMillis();
			LtTemplateHeaders getAllTemplateHeaders = ltTemplateDao.getAllTemplateAgainstDistributors(distId);
			outQuerygetAllTemplateAgainstDistributors = System.currentTimeMillis();
			inQuerygetProductDetailsAgainstheaderId2 = System.currentTimeMillis(); 
			templateLineDetails =ltTemplateDao.getProductDetailsAgainstheaderId(getAllTemplateHeaders.getTemplateHeaderId(), priceList);
			outQuerygetProductDetailsAgainstheaderId2 = System.currentTimeMillis();
			System.out.println(templateLineDetails);
			ltTemplateDto.setTemplateHeaderId(getAllTemplateHeaders.getTemplateHeaderId());
			ltTemplateDto.setDistributorId(getAllTemplateHeaders.getDistributorId());
			ltTemplateDto.setStatus(getAllTemplateHeaders.getStatus());
			ltTemplateDto.setCreatedBy(getAllTemplateHeaders.getCreatedBy());
			ltTemplateDto.setCreationDate(getAllTemplateHeaders.getCreationDate());
			ltTemplateDto.setLastUpdatedBy(getAllTemplateHeaders.getLastUpdatedBy());
			ltTemplateDto.setLastUpdatedLogin(getAllTemplateHeaders.getLastUpdatedLogin());
			ltTemplateDto.setLastUpdatedDate(getAllTemplateHeaders.getLastUpdatedDate());
			
			
			List<String> prodIdList = new ArrayList<>();
			prodIdList = templateLineDetails.stream().map(LtTemplateLines::getProductId).collect(Collectors.toList());
			String ids = prodIdList.stream().map(id->"'"+id +"'").collect(Collectors.joining(", "));
			List<LtTemplateLines> mrpList = new ArrayList<>();
			mrpList = ltTemplateDao.getMultipleMrpForTemplateProductV1(ids, distributorId);
			
			for (LtTemplateLines product : templateLineDetails) {
                // Initialize MRP1 list if it is null
                if (product.getListPrice() == null) {
                    product.setMRP1(new ArrayList<>());
                }
                List<LtTemplateLines>mrpList2 = mrpList.stream().filter(x-> x.getProductId().equalsIgnoreCase(product.getProductId())).collect(Collectors.toList());
                
                product.setMRP2(mrpList2);
                if(mrpList2.size()> 1) {
                	//product.setAvailableQuantity(mrpList2.get(0).getAvailableQuantity());
                	product.setAvailableQuantity(mrpList2.get(0).getInventoryQuantity());
                }
                product.setAvailableQuantity(mrpList2.get(0).getInventoryQuantity());
                System.out.println("mrpList2 is = "+mrpList2);
            }
			
			
			if(templateLineDetails!= null && !templateLineDetails.isEmpty()) {
				ltTemplateDto.setLtTemplateLines(templateLineDetails);
			}else {
				//ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}
			/*if( templateLineDetails == null && templateLineDetails.isEmpty()) {
				ltTemplateDao.deleteHeaderdetailsbytemplateid(templateHeaders.getTemplateHeaderId());
			}else {
			ltTemplateDto.setLtTemplateLines(templateLineDetails);
		    }*/
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
	
		}
	
		if (ltTemplateDto != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(ltTemplateDto);
			timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetTemplateAgainstDistributors,outQuerygetTemplateAgainstDistributors));
			timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetProductDetailsAgainstheaderId,outQuerygetProductDetailsAgainstheaderId));
			timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetAllTemplateAgainstDistributors, outQuerygetAllTemplateAgainstDistributors));
			timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetProductDetailsAgainstheaderId2, outQuerygetProductDetailsAgainstheaderId2));
		//	timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
		//	timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
			
			long methodOut = System.currentTimeMillis();
			System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
	        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	        status.setTimeDifference(timeDifference);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;	
	
	}

	
}
