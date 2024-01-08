package com.users.usersmanagement.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.LtPromotionDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtPromotion;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.repository.LtPromotionRepository;

@Service
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtPromotionServiceImpl implements LtPromotionService, CodeMaster {

	@Autowired
	LtPromotionDao promotionDao;

	@Autowired
	LtPromotionRepository ltPromotionRepository;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;
	
	@Autowired
	private Environment env;

	@Override
	public Status getPromotionDataV1(String orgId, Long limit, Long offset, Long userId) throws ServiceException {
		Status status = new Status();
		List<LtPromotion> ltPromotionList = promotionDao.getPromotionDataV1(orgId, limit, offset, userId);

		if (!ltPromotionList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(ltPromotionList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(ltPromotionList);
		}
		return status;
	}
	
	@Override
	public Status getPromotionData(String orgId, Long limit, Long offset) throws ServiceException {
		Status status = new Status();
		List<LtPromotion> ltPromotionList = promotionDao.getPromotionData(orgId, limit, offset);

		if (!ltPromotionList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(ltPromotionList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(ltPromotionList);
		}
		return status;
	}

	@Override
	public Status deletePromotionData(Long pramotionId) throws ServiceException {
		Status status = new Status();
		Optional<LtPromotion> ltPromotion = ltPromotionRepository.findById(pramotionId);
		if (ltPromotion.isPresent()) {
			LtPromotion promotion = ltPromotion.get();
			promotion.setStatus(INACTIVE);
			promotion.setLastUpdateDate(new Date());
			ltPromotionRepository.save(promotion);
			status = ltMastCommonMessageService.getCodeAndMessage(DELETE_SUCCESSFULLY);
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		}
		return status;
	}

	   @Override
		public Status uploadPromotion(MultipartFile file, Long promotionId, String createdBy, String pramotionStatus,
				String promotionName, String allTimeShowFlag, String orgId, String startDate, String endDate)
				throws ServiceException, ParseException {
			Status status = new Status();

			try {

				LtPromotion ltPromotion = new LtPromotion();

				if (promotionId != null) {
					ltPromotion.setPromotionId(promotionId);
				}
				if (createdBy != null) {
					ltPromotion.setCreatedBy(createdBy);
				}
				if (pramotionStatus != null) {
					ltPromotion.setStatus(pramotionStatus);
				}
				if (promotionName != null) {
					ltPromotion.setPromotionName(promotionName);
				}
				if (allTimeShowFlag != null) {
					ltPromotion.setAllTimeShowFlag(allTimeShowFlag);
				}
				if (orgId != null) {
					ltPromotion.setOrgId(orgId);
				}

				Date sDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
						.parse(startDate.replaceAll("Z$", "+0000"));
				Date eDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(endDate.replaceAll("Z$", "+0000"));

				if (startDate != null) {
					ltPromotion.setStartDate(sDate);
				}
				if (endDate != null) {
					ltPromotion.setEndDate(eDate);
				}

				ltPromotion.setCreationDate(new Date());
				ltPromotion.setLastUpdateDate(new Date());
				
				String fileUploadPath = env.getProperty("fileUploadPath");
				String fileDownloadPath = env.getProperty("fileDownloadPath");
				String imgDownloadPath = fileDownloadPath+""+ file.getOriginalFilename();
				
				File dir = new File(fileUploadPath);
				if (!dir.exists()) {
					dir.mkdirs();
					if (!dir.isDirectory()) {
						status.setCode(NO_DIRECTIVE_EXISTS); 
						status.setMessage("No Directive Exists");
						return status;
					}
				}
				
				if (!file.isEmpty()) {
					byte[] bytes = file.getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(new File(fileUploadPath +  file.getOriginalFilename())));
					buffStream.write(bytes);
					buffStream.close();
				}
				ltPromotion.setImageName(file.getOriginalFilename());
				ltPromotion.setImageData(imgDownloadPath);
				ltPromotion.setImageType(file.getContentType());

				Optional<LtPromotion> promotionObj = ltPromotionRepository.findById(promotionId);
				LtPromotion ltPromotionObjFromDB = null;

				if (promotionObj.isPresent()) {
					ltPromotionObjFromDB = promotionObj.get();
				}

				if (ltPromotionObjFromDB != null) {
					if (file.isEmpty() || file == null) {
						Optional<LtPromotion> ltPromotionImg = ltPromotionRepository.findById(ltPromotion.getPromotionId());
						if (ltPromotionImg.isPresent()) {
							LtPromotion promotionImg = ltPromotionImg.get();

							ltPromotion.setImageName(promotionImg.getImageName());
							ltPromotion.setImageData(promotionImg.getImageData());
							ltPromotion.setImageType(promotionImg.getImageType());
						}
					}

					ltPromotion = ltPromotionRepository.save(ltPromotion);
					status.setCode(UPDATE_SUCCESSFULLY);
					status.setMessage("Update Successfully");
					// status.setData(ltPromotion);
				} else {
					ltPromotion = ltPromotionRepository.save(ltPromotion);
					status.setCode(INSERT_SUCCESSFULLY);
					status.setMessage("Insert Successfully");
					// status.setData(ltPromotion);
				}
				return status;

			} catch (IOException e) {
				e.printStackTrace();
				return ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
			}

		}

	@Override
	public Status editPromotion(LtPromotion ltPromotionObj) throws ServiceException {
		Status status = new Status();
		Optional<LtPromotion> ltPromotion = ltPromotionRepository.findById(ltPromotionObj.getPromotionId());
		if (ltPromotion.isPresent()) {
			LtPromotion promotion = ltPromotion.get();
			promotion.setStatus(ACTIVE);
			
			if(ltPromotionObj.getAllTimeShowFlag() != null) {
				promotion.setAllTimeShowFlag(ltPromotionObj.getAllTimeShowFlag());
			}
			
			if(ltPromotionObj.getPromotionName() != null) {
				promotion.setPromotionName(ltPromotionObj.getPromotionName());
			}
			
			if(ltPromotionObj.getStartDate() != null) {
				promotion.setStartDate(ltPromotionObj.getStartDate());
			}
			
			if(ltPromotionObj.getEndDate() != null) {
				promotion.setEndDate(ltPromotionObj.getEndDate());
			}
			promotion.setLastUpdateDate(new Date());
			
			ltPromotionRepository.save(promotion);
			status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
			status.setMessage("Update Successfully");
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_FAIL);
		}
		return status;
	}
}
