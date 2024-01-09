package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dao.LtTemplateDao;
import com.lonar.cartservice.atflCartService.dto.LtTemplateDto;
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
		try {
		LtTemplateHeaders templateHeaders = ltTemplateDao.getTemplateAgainstDistributor(distributorId,templateHeaderId);
		List<LtTemplateLines> templateLineDetails = new ArrayList<LtTemplateLines>();
		LtTemplateDto ltTemplateDto = new LtTemplateDto();
		if(templateHeaders !=null) {
			templateLineDetails =ltTemplateDao.getProductDetailsAgainstheaderId(templateHeaders.getTemplateHeaderId());
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
		try {
		if(ltTemplateDto !=null) {
			LtTemplateHeaders ltTemplateHeaders = new LtTemplateHeaders();
			LtTemplateHeaders ltTemplateHeadersUpdated = null;
			if(ltTemplateDto.getTemplateHeaderId() ==null) {
			ltTemplateHeaders.setDistributorId(ltTemplateDto.getDistributorId());
			ltTemplateHeaders.setCreationDate(new Date());
			ltTemplateHeaders.setCreatedBy(ltTemplateDto.getUserId());
			ltTemplateHeaders.setLastUpdatedBy(ltTemplateDto.getUserId());
			ltTemplateHeaders.setLastUpdatedDate(new Date());
			ltTemplateHeaders.setLastUpdatedLogin(ltTemplateDto.getUserId());
			ltTemplateHeaders.setProductCount(ltTemplateDto.getLtTemplateLines().size());
			ltTemplateHeadersUpdated = ltTemplateDao.saveheaderData(ltTemplateHeaders);
			}else {
				LtTemplateHeaders ltTemplateHeader = ltTemplateDao.getTemplateAgainstDistributor(ltTemplateDto.getDistributorId(),
														ltTemplateDto.getTemplateHeaderId());
				ltTemplateHeaders.setDistributorId(ltTemplateDto.getDistributorId());
				ltTemplateHeaders.setCreationDate(ltTemplateHeader.getCreationDate());
				ltTemplateHeaders.setCreatedBy(ltTemplateHeader.getCreatedBy());
				ltTemplateHeaders.setLastUpdatedBy(ltTemplateDto.getUserId());
				ltTemplateHeaders.setLastUpdatedDate(new Date());
				ltTemplateHeaders.setLastUpdatedLogin(ltTemplateDto.getUserId());
				ltTemplateHeaders.setProductCount(ltTemplateDto.getLtTemplateLines().size());
				ltTemplateHeadersUpdated = ltTemplateDao.saveheaderData(ltTemplateHeaders);
			}
			if(ltTemplateHeadersUpdated !=null) {
				List<LtTemplateLines> productList = ltTemplateDao.getProductDetailsAgainstheaderId(ltTemplateHeadersUpdated.getTemplateHeaderId());
				if(productList !=null) {
				ltTemplateDao.deletelinedetailsbytemplateid(ltTemplateHeadersUpdated.getTemplateHeaderId());
				}
				LtTemplateLines ltTemplateLines = new LtTemplateLines();
				for(LtTemplateLines lines:ltTemplateDto.getLtTemplateLines()) {
					ltTemplateLines.setTemplateHeaderId(ltTemplateHeadersUpdated.getTemplateHeaderId());
					ltTemplateLines.setSalesType(lines.getSalesType());
					ltTemplateLines.setProductId(lines.getProductId());
					ltTemplateLines.setProductName(lines.getProductName());
					ltTemplateLines.setProductDescription(lines.getProductDescription());
					ltTemplateLines.setPtrPrice(lines.getPtrPrice());
					ltTemplateLines.setQuantity(lines.getQuantity());
					ltTemplateLines.setAvailableQuantity(lines.getAvailableQuantity());
					LtTemplateLines ltTemplateLinesupdated = ltTemplateDao.saveLineData(ltTemplateLines);
				}
			}
			Status status1 = getTemplateAgainstDistributor(ltTemplateHeadersUpdated.getDistributorId(), ltTemplateHeadersUpdated.getTemplateHeaderId());
			
			if(ltTemplateHeadersUpdated !=null) {
				status.setCode(INSERT_SUCCESSFULLY);
				status.setMessage("INSERT_SUCCESSFULLY");
				status.setData(status1.getData());
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
}
