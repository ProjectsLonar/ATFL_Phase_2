package com.atflMasterManagement.masterservice.servcies;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.Status;

public interface LtImageUploadService {
	Status imageUpload(MultipartFile file, String type)
			throws ServiceException, ParseException;
}
