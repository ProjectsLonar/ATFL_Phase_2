package com.users.usersmanagement.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.DateTimeClass;
import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.LtFileUploadDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtFileUpload;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.repository.LtFileUploadRepository;

@Service
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtFileUploadServiceImpl implements LtFileUploadService, CodeMaster {

	@Autowired
	LtFileUploadRepository fileUploadRepository;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	private Environment env;
	
	@Autowired
	LtFileUploadDao ltFileUploadDao;

	@Override
	public Status uploadfile(MultipartFile file, String orgId, Long userId) throws ServiceException, ParseException {
		Status status = new Status();

		try {

			
			LtFileUpload fileUploadCheck=ltFileUploadDao.getfileByName(file);
			
			if (fileUploadCheck == null) {
			
			LtFileUpload fileUpload = new LtFileUpload();
			fileUpload.setCreatedBy(userId);
			fileUpload.setStatus("ACTIVE");
			fileUpload.setOrgId(orgId);
			fileUpload.setStartDate(DateTimeClass.getCurrentDateTime());
			fileUpload.setEndDate(DateTimeClass.getCurrentDateTime());
			fileUpload.setCreationDate(DateTimeClass.getCurrentDateTime());
			fileUpload.setLastUpdateDate(DateTimeClass.getCurrentDateTime());

			String fileUploadPath = env.getProperty("fileUploadExcelPdfPath");
			String fileDownloadPath = env.getProperty("fileDownloadExcelPdfPath");
			String imgDownloadPath = fileDownloadPath + "" + file.getOriginalFilename();

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
						new FileOutputStream(new File(fileUploadPath + file.getOriginalFilename())));
				buffStream.write(bytes);
				buffStream.close();
			}
			fileUpload.setFileName(file.getOriginalFilename());
			fileUpload.setImageName(file.getOriginalFilename());
			fileUpload.setImageData(imgDownloadPath);
			fileUpload.setImageType(file.getContentType());

			fileUpload = fileUploadRepository.save(fileUpload);
			status.setCode(INSERT_SUCCESSFULLY);
			status.setMessage("Insert Successfully");

			return status;
			
			}else {
				status.setCode(INSERT_FAIL);
				status.setMessage("File Already Exists");
				return status;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		}
	}

	@Override
	public Status getUploadedFiles(String orgId, Long limit, Long offset, Long userId) throws ServiceException {
		Status status = new Status();
		List<LtFileUpload> fileUploadList = ltFileUploadDao.getAllUploadedFiles(orgId, limit, offset, userId);

		if (!fileUploadList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(fileUploadList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(fileUploadList);
		}
		return status;
	}

	@Override
	public Status deleteUplodedFile(Long fileuploadId) throws ServiceException {
		Status status = new Status();
		Optional<LtFileUpload> ltFileUpload = fileUploadRepository.findById(fileuploadId);
		if (ltFileUpload.isPresent()) {
			LtFileUpload fileUpload = ltFileUpload.get();
			fileUpload.setStatus(INACTIVE);
			fileUpload.setLastUpdateDate(new Date());
			fileUploadRepository.save(fileUpload);
			status = ltMastCommonMessageService.getCodeAndMessage(DELETE_SUCCESSFULLY);
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		}
		return status;
	}

}


