package com.lonar.cartservice.atflCartService.service;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.dao.LtTemplateDao;
import com.lonar.cartservice.atflCartService.dto.LtTemplateDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtTemplateHeaders;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;
import com.lonar.cartservice.atflCartService.model.Status;

@Service
@Transactional
public class LtTemplateServiceImpl implements LtTemplateService,CodeMaster {

	@Autowired
	LtTemplateDao ltTemplateDao;
	
	@Override
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
			}
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
}
