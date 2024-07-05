package com.atflMasterManagement.masterservice.servcies;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.Status;

@Service
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtImageUploadServiceImpl implements LtImageUploadService, CodeMaster {

	@Autowired
	private Environment env;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;
/* this method comment on 19-June-2024 for api timing calculation
	@Override
	public Status imageUpload(MultipartFile file, String type) throws ServiceException, ParseException {
		Status status = new Status();
		try {
			String fileUploadPath = env.getProperty("fileUploadPath");
			String fileDownloadPath = env.getProperty("fileDownloadPath");
			Date date = new Date();
			Long msec = date.getTime();
			String imageName = file.getOriginalFilename().split("\\.")[0] + "_" + msec.toString() + "."
					+ file.getOriginalFilename().split("\\.")[1];

			String fileUoload;
			String fileDownload;
			if (type.equalsIgnoreCase("category")) {
				fileUoload = fileUploadPath + "category/";
				fileDownload = fileDownloadPath + "category/";
			} else if (type.equalsIgnoreCase("product")) {
				fileUoload = fileUploadPath + "product/";
				fileDownload = fileDownloadPath + "product/";
			} else if (type.equalsIgnoreCase("promotion")) {
				fileUoload = fileUploadPath + "promotion/";
				fileDownload = fileDownloadPath + "promotion/";
			} else {
				// user
				fileUoload = fileUploadPath + "user/";
				fileDownload = fileDownloadPath + "user/";
			}

			File dir = new File(fileUoload);
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
						new FileOutputStream(new File(fileUoload + imageName)));
				buffStream.write(bytes);
				buffStream.close();

				status.setCode(SUCCESS);
				status.setMessage("SUCCESS");
				status.setUrl(fileDownload + imageName);

			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
			}

			return status;

		} catch (IOException e) {
			e.printStackTrace();
			return ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		}
	}
*/
	
	@Override
	public Status imageUpload(MultipartFile file, String type) throws ServiceException, ParseException {
		System.out.println("In method imageUpload = "+LocalDateTime.now());
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		Status status = new Status();
		try {
			String fileUploadPath = env.getProperty("fileUploadPath");
			String fileDownloadPath = env.getProperty("fileDownloadPath");
			Date date = new Date();
			Long msec = date.getTime();
			String imageName = file.getOriginalFilename().split("\\.")[0] + "_" + msec.toString() + "."
					+ file.getOriginalFilename().split("\\.")[1];
 
			String fileUoload;
			String fileDownload;
			if (type.equalsIgnoreCase("category")) {
				fileUoload = fileUploadPath + "category/";
				fileDownload = fileDownloadPath + "category/";
			} else if (type.equalsIgnoreCase("product")) {
				fileUoload = fileUploadPath + "product/";
				fileDownload = fileDownloadPath + "product/";
			} else if (type.equalsIgnoreCase("promotion")) {
				fileUoload = fileUploadPath + "promotion/";
				fileDownload = fileDownloadPath + "promotion/";
			} else {
				// user
				fileUoload = fileUploadPath + "user/";
				fileDownload = fileDownloadPath + "user/";
			}
 
			File dir = new File(fileUoload);
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
						new FileOutputStream(new File(fileUoload + imageName)));
				buffStream.write(bytes);
				buffStream.close();
 
				status.setCode(SUCCESS);
				status.setMessage("SUCCESS");
				status.setUrl(fileDownload + imageName);
				long methodOut = System.currentTimeMillis();
				System.out.println("Exit from method imageUpload at "+LocalDateTime.now());
		        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
		        status.setTimeDifference(timeDifference);
 
			} else {
				status.setCode(FAIL);
				status.setMessage("FAIL");
			}
 
			return status;
 
		} catch (IOException e) {
			e.printStackTrace();
			return ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
		}
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
}
