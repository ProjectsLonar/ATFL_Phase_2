package com.users.usersmanagement.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.LtFileUpload;

public interface LtFileUploadDao {
	
	List<LtFileUpload> getAllUploadedFiles(String orgId, Long limit, Long offset, String userId) throws ServiceException;
	
	LtFileUpload getfileByName(MultipartFile file)throws ServiceException;

}
