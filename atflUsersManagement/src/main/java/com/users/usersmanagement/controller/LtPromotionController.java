package com.users.usersmanagement.controller;

import java.io.IOException;
import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.BusinessException;
import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtPromotion;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.service.LtPromotionService;

@RestController
@RequestMapping(value = "/promotion")
public class LtPromotionController implements CodeMaster {

	@Autowired
	LtPromotionService ltPromotionService;
	
	@RequestMapping(value = "/uploadPromotion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> uploadPromotion(@RequestParam("file") MultipartFile file, /* @RequestParam("promotion") String promotionStr, */
			@RequestParam("promotionId") Long promotionId, @RequestParam("createdBy") String createdBy,
			//@RequestParam("status") String status,
			@RequestParam("promotionName") String promotionName,
			@RequestParam("allTimeShowFlag") String allTimeShowFlag, @RequestParam("orgId") String orgId,
			@RequestParam("startDate") String  startDate, 
			@RequestParam("endDate") String  endDate
			) throws ServerException {
		try {
			String status = "ACTIVE";
			return new ResponseEntity<Status>(ltPromotionService.uploadPromotion(file, /* promotionStr, */
					promotionId, createdBy,
					status,  promotionName,
					allTimeShowFlag, orgId,
					startDate, endDate
					), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/editPromotion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> editPromotion(@RequestBody LtPromotion promotion) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltPromotionService.editPromotion(promotion), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}

	}
	
	
	@RequestMapping(value = "/getPromotionDataV1/{orgId}/{limit}/{offset}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getPromotionDataV1(@PathVariable("orgId") String orgId, @PathVariable("limit") Long limit,  @PathVariable("offset") Long offset,
			@PathVariable("userId") String userId)
			throws ServiceException, IOException {  
		return new ResponseEntity<Status>(ltPromotionService.getPromotionDataV1(orgId, limit, offset, userId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPromotionData/{orgId}/{limit}/{offset}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getPromotionData(@PathVariable("orgId") String orgId, @PathVariable("limit") Long limit,  @PathVariable("offset") Long offset
			)
			throws ServiceException, IOException {  
		return new ResponseEntity<Status>(ltPromotionService.getPromotionData(orgId, limit, offset), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deletePromotionData/{pramotionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> deletePromotionData(@PathVariable("pramotionId") Long pramotionId)
			throws ServiceException, IOException {
		return new ResponseEntity<Status>(ltPromotionService.deletePromotionData(pramotionId), HttpStatus.OK);
	}
}
