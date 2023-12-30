package com.users.usersmanagement.service;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.Status;

public interface LtFileUploadService {

	Status uploadfile(MultipartFile file, String orgId, String userId) throws ServiceException, ParseException;

	Status getUploadedFiles(String orgId, Long limit, Long offset, String userId) throws ServiceException;

	Status deleteUplodedFile(Long fileuploadId) throws ServiceException;
}
