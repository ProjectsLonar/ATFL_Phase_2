package com.users.usersmanagement.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.sql.Timestamp;

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
	public Status getPromotionDataV1(String orgId, Long limit, Long offset, Long userId) throws ServiceException {
		long methodIn = System.currentTimeMillis();
		Map<String,String> timeDifference = new HashMap<>();
		System.out.println("methodIn getPromotionDataV1 = " + LocalDateTime.now());
		Status status = new Status();
		System.out.println("before getPromotionDataV1 query call = " + LocalDateTime.now());
		List<LtPromotion> ltPromotionList = promotionDao.getPromotionDataV1(orgId, limit, offset, userId);
		System.out.println("after getPromotionDataV1 query call = " + LocalDateTime.now());
		if (!ltPromotionList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(ltPromotionList);
			long methodOut = System.currentTimeMillis();
			timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	        status.setTimeDifference(timeDifference);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(ltPromotionList);
		}
		System.out.println("method out getPromotionDataV1 = " + LocalDateTime.now());
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
		System.out.println("pramotion Id for delete == "+pramotionId);
	/*	Optional<LtPromotion> ltPromotion = ltPromotionRepository.findById(pramotionId);
		//System.out.println("promotion data to be deleted == "+ltPromotion);
		if (ltPromotion.isPresent()) {
			LtPromotion promotion = ltPromotion.get();
			//System.out.println("promotion optional data to be deleted == "+ltPromotion.get());
			promotion.setStatus(INACTIVE);
			//promotion.setLastUpdateDate(new Date());
			ltPromotionRepository.save(promotion);
			status = ltMastCommonMessageService.getCodeAndMessage(DELETE_SUCCESSFULLY);
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		}*/
		LtPromotion ltPromotion = promotionDao.getPromotionData(pramotionId);
		if (ltPromotion!= null) {
		promotionDao.deletePromotionData(pramotionId);
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
			System.out.println("ReqBody =" +file+"\n"+promotionId+"\n"+createdBy+"\n"+pramotionStatus+"\n"+promotionName+"\n"+
					allTimeShowFlag+"\n"+orgId+"\n"+startDate +"\n"+endDate);
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

//				Date sDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
//						.parse(startDate.replaceAll("Z$", "+0000"));
//				Date eDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(endDate.replaceAll("Z$", "+0000"));
//       		       
				//String dateString = "2024-03-11T18:30:00";
		        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		        
		        LocalDateTime sDate = LocalDateTime.parse(startDate, formatter);
		        //System.out.println("Parsed date and time: " + sDate);
		        Date sDate1 = java.sql.Timestamp.valueOf(sDate);
		        
		        LocalDateTime eDate = LocalDateTime.parse(endDate, formatter);
		        //System.out.println("Parsed date and time: " + eDate);
		        Date eDate1 = java.sql.Timestamp.valueOf(eDate);
		        
//				String strStartDate = startDate;
//				String strEndDate = endDate;

				
//				if (startDate != null) {
//					ltPromotion.setStartDate(sDate);
//				}
//				if (endDate != null) {
//					ltPromotion.setEndDate(eDate);
//				}
				
				
				if (startDate != null) {
					ltPromotion.setStartDate1("");
					ltPromotion.setStartDate(sDate1);
				}
				if (endDate != null) {
					ltPromotion.setEndDate1("");
					ltPromotion.setEndDate(eDate1);
				}

				ltPromotion.setCreationDate(new Date());
				ltPromotion.setLastUpdateDate(new Date());
				
				String fileUploadPath = env.getProperty("fileUploadPath");
				String fileDownloadPath = env.getProperty("fileDownloadPath");
				String imgDownloadPath = "";
				if(file==null) {
					LtPromotion ltPromotionforEdit = promotionDao.getPromotionData(promotionId);
					imgDownloadPath = fileDownloadPath+""+ ltPromotionforEdit.getImageName();
				}
				else {
					imgDownloadPath = fileDownloadPath+""+ file.getOriginalFilename();
				}
				
				
				File dir = new File(fileUploadPath);
				if (!dir.exists()) {
					dir.mkdirs();
					if (!dir.isDirectory()) {
						status.setCode(NO_DIRECTIVE_EXISTS); 
						status.setMessage("No Directive Exists");
						return status;
					}
				}
				
				if (file != null && !file.isEmpty()) {
					byte[] bytes = file.getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(new File(fileUploadPath +  file.getOriginalFilename())));
					buffStream.write(bytes);
					buffStream.close();
					ltPromotion.setImageName(file.getOriginalFilename());
					ltPromotion.setImageType(file.getContentType());

				}
				else {
					LtPromotion ltPromotionforEdit = promotionDao.getPromotionData(promotionId);
//					byte[] bytes = file.getBytes();
//					BufferedOutputStream buffStream = new BufferedOutputStream(
//							new FileOutputStream(new File(fileUploadPath +  ltPromotionforEdit.getImageName())));
//					buffStream.write(bytes);
//					buffStream.close();
					ltPromotion.setImageName(ltPromotionforEdit.getImageName());
					ltPromotion.setImageType(ltPromotionforEdit.getImageType());

				}
				//ltPromotion.setImageName(file.getOriginalFilename());
				ltPromotion.setImageData(imgDownloadPath);
				//ltPromotion.setImageType(file.getContentType());
//				System.out.println("file content type = "+file.getContentType()); 

				

				Optional<LtPromotion> promotionObj = ltPromotionRepository.findById(promotionId);
				
				LtPromotion ltPromotionObjFromDB = null;

				if (promotionObj.isPresent()) {  
					ltPromotionObjFromDB = promotionObj.get();
				}

				if (ltPromotionObjFromDB != null) {     					
//					if (file.isEmpty() || file == null) {
					if (file == null || file.isEmpty()) {
						Optional<LtPromotion> ltPromotionImg = ltPromotionRepository.findById(ltPromotion.getPromotionId());
						if (ltPromotionImg.isPresent()) {
							System.out.println("in edit ltPromotionImg" + ltPromotionImg); 
							LtPromotion promotionImg = ltPromotionImg.get();
							
							System.out.println("Above ltPromotionImg.get()"+promotionImg.getImageName()); 
							ltPromotion.setImageName(promotionImg.getImageName());
							System.out.println("promotionImg.getImageName()"+promotionImg.getImageName()); 
							
							System.out.println("Above promotionImg.getImageData()"+ promotionImg.getImageData());
							ltPromotion.setImageData(promotionImg.getImageData());
							System.out.println("promotionImg.getImageData()"+ promotionImg.getImageData()); 
							
							System.out.println("Above promotionImg.getImageType()"+promotionImg.getImageType());
							ltPromotion.setImageType(promotionImg.getImageType());
							System.out.println("promotionImg.getImageType()"+promotionImg.getImageType()); 
							
							 
						}
					}
					
						ltPromotion.setStartDate(sDate1);
						ltPromotion.setEndDate(eDate1);
					
					System.out.println("ltPromotion 1237"+ltPromotion);
					ltPromotion = ltPromotionRepository.save(ltPromotion);
					//System.out.println("ltPromotion 8347333"+ltPromotion);
					status.setCode(UPDATE_SUCCESSFULLY);
					status.setMessage("Update Successfully");
					 //status.setData(ltPromotion);
				} else {
					//System.out.println("ltPromotion 12373478347333"+ltPromotion);
					ltPromotion = ltPromotionRepository.save(ltPromotion);
					//System.out.println("ltPromotion 123333"+ltPromotion);
					status.setCode(INSERT_SUCCESSFULLY);
					status.setMessage("Insert Successfully");
					 //status.setData(ltPromotion);
				}
				return status;

			} catch (IOException e) {
				e.printStackTrace();
				return ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
			}

		}

	@Override
	public Status editPromotion(LtPromotion ltPromotionObj) throws ServiceException {
		//Status status = new Status();
		//System.out.println("ReqBody is ltPromotionObj"+ltPromotionObj);
		
/* comment on 25-May-2024 		
		try {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String startDate = ltPromotionObj.getStartDate();
//		String endDate = ltPromotionObj.getEndDate();
//		Date date = dateFormat.parse(startDate);		
//		Date date2 = dateFormat.parse(endDate);
//		
//		String pattern = "yyyy-MM-dd'T'HH:mm:ss";
//
//        // Create a SimpleDateFormat object with the pattern
//        SimpleDateFormat dateFormat1 = new SimpleDateFormat(pattern);
//
//        // Format the Date object to a string
//        String dateString = dateFormat1.format(date);
//        String dateString2 = dateFormat1.format(date2);
        
//			DateFormat utcFormat = new SimpleDateFormat("dd-MMM-yy");
//			//Date startDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getFromDate()));
//			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).withZone(ZoneId.systemDefault());
//	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
//	            Instant instant = Instant.from(inputFormatter.parse(ltPromotionObj.getStartDate()));
//	            String startDate = outputFormatter.format(instant.atZone(ZoneId.systemDefault()));
//	            
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = (Date)formatter.parse(ltPromotionObj.getStartDate());
			Date date1 = (Date)formatter.parse(ltPromotionObj.getEndDate());
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String deliveryDate =outputFormat.format(date);
			String deliveryDate1 =outputFormat.format(date1);
			System.out.println("formatedDate : " + deliveryDate); 
			
			//System.out.println("Date ="+ excelReportDto.getFromDate()); System.out.println("NewDate=" +startDate);
			//Date endDate = reportStartEndDateFormat.parse(DateTimeClass.getDateAndTimeFromUTCDate(excelReportDto.getToDate()));
			//Instant instant1 = Instant.from(inputFormatter.parse(ltPromotionObj.getEndDate()));
            //String endDate = outputFormatter.format(instant1.atZone(ZoneId.systemDefault()));
			
        //  String strStartDate = reportStartEndDateFormat.format(startDate);
		//	String strEndDate = reportStartEndDateFormat.format(endDate);

			String strStartDate = deliveryDate;
			String strEndDate = deliveryDate1;
			

        ltPromotionObj.setStartDate(strStartDate);
        ltPromotionObj.setEndDate(strEndDate);
        
       //String timestampString = startDate; // Example timestamp string
        //String timestampString1 = endDate;
        
            // Define the date format to match the timestamp string
            
            // Parse the timestamp string to a java.util.Date object
            //Date parsedDate = dateFormat.parse(timestampString);
            //Date parsedDate1 = dateFormat.parse(timestampString1);
            // Convert the java.util.Date to a java.sql.Timestamp object
            //Timestamp timestamp = new Timestamp(parsedDate.getTime());
            //Timestamp timestamp1 = new Timestamp(parsedDate1.getTime());
        }catch(Exception e) {e.printStackTrace();}
        
        
        
        
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
	*/	
	
		Status status = new Status();
        System.out.println("ReqBody is ltPromotionObj" + ltPromotionObj);

        // Define the date formats
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        String formattedDate1 = formatter.format(ltPromotionObj.getStartDate());
        String formattedDate2 = formatter.format(ltPromotionObj.getEndDate());      
//		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Parse the start and end dates
//            Date startDate = parseDate(ltPromotionObj.getStartDate(), formatter);
//            Date endDate = parseDate(ltPromotionObj.getEndDate(), formatter);
		
//            System.out.println("startDate = "+startDate);
//            System.out.println("endDate = "+endDate);
		
		// Format the dates to the desired output format
//            String formattedStartDate = outputFormat.format(startDate);
//            String formattedEndDate = outputFormat.format(endDate);
		
//            System.out.println("formattedStartDate = "+formattedStartDate);
//            System.out.println("formattedEndDate = "+formattedEndDate);
	
		System.out.println("startDate = "+ltPromotionObj.getStartDate());
		System.out.println("endDate = "+ltPromotionObj.getEndDate());

		ltPromotionObj.setStartDate(ltPromotionObj.getStartDate());
		ltPromotionObj.setEndDate(ltPromotionObj.getEndDate());

        // Retrieve the promotion entity from the repository
        Optional<LtPromotion> ltPromotion = ltPromotionRepository.findById(ltPromotionObj.getPromotionId());
        if (ltPromotion.isPresent()) {
        	System.out.println("in if ltPromotion.isPresent()");
            LtPromotion promotion = ltPromotion.get();
            promotion.setStatus(ACTIVE);

            // Update promotion fields
            if (ltPromotionObj.getAllTimeShowFlag() != null) {
                promotion.setAllTimeShowFlag(ltPromotionObj.getAllTimeShowFlag());
            }

            if (ltPromotionObj.getPromotionName() != null) {
                promotion.setPromotionName(ltPromotionObj.getPromotionName());
            }

            if (ltPromotionObj.getStartDate() != null) {
                promotion.setStartDate(ltPromotionObj.getStartDate());
            }

            if (ltPromotionObj.getEndDate() != null) {
                promotion.setEndDate(ltPromotionObj.getEndDate());
            }

            promotion.setLastUpdateDate(new Date());
            System.out.println("Above save = "+promotion);
            // Save the updated promotion entity
            ltPromotionRepository.save(promotion);
            System.out.println("Below save");
            status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
            status.setMessage("Update Successfully");
        } else {
        	System.out.println("in else update fail");
            status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_FAIL);
        }

        return status;
       
        
	}
		
/*
	@Override
	public Status editPromotionV1(MultipartFile file, 
			Long promotionId, String createdBy, String pramotionStatus,
			String promotionName, String allTimeShowFlag, String orgId, String startDate, String endDate)
			throws ServiceException, ParseException {
		Status status = new Status();

		try {
			System.out.println("ReqBody =" +file+"\n"+promotionId+createdBy+pramotionStatus+promotionName+allTimeShowFlag+orgId+startDate);
			//	promotionDao.deletePromotionDataById(promotionId);
			
			LtPromotion ltPromotion = promotionDao.getPromotionData(promotionId);
			if (ltPromotion!= null) {
				pramotionStatus = ltPromotion.getStatus();
				
				String fileUploadPath = env.getProperty("fileUploadPath");
				String fileDownloadPath = env.getProperty("fileDownloadPath");
				String imgDownloadPath = fileDownloadPath+""+ file.getOriginalFilename();
				
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
				
				promotionDao.updatePromotionData(file, createdBy, pramotionStatus,
					 promotionName, allTimeShowFlag, orgId, startDate, endDate, createdBy, createdBy, promotionId);
			status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_SUCCESSFULLY);
			}} else {
				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			}
			return status; 
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return status;
  }
*/

private Date parseDate(String dateStr, SimpleDateFormat formatter) throws ParseException {
    if (dateStr == null || dateStr.isEmpty()) {
        throw new ParseException("Date string is null or empty", 0);
    }
    return formatter.parse(dateStr);
}

//  this code is comment on 10-Jun-24 bcz upload promotion code work for both save & update 
@Override
public Status editPromotionV1(MultipartFile file, Long promotionId, String createdBy, String pramotionStatus,
		String promotionName, String allTimeShowFlag, String orgId, String startDate, String endDate)
		throws ServiceException, ParseException {
	Status status = new Status();
	System.out.println("ReqBody =" +file+"\n"+promotionId+"\n"+createdBy+"\n"+pramotionStatus+"\n"+promotionName+"\n"+
	allTimeShowFlag+"\n"+orgId+"\n"+startDate+"\n"+endDate);
	
	try {
		System.out.println("file data is = "+file.getSize());
		LtPromotion imageFileData =  promotionDao.getImageFileDataById(promotionId);
		System.out.println("imageFileData1 =" +imageFileData.getImageName());
		System.out.println("imageFileData2 =" +imageFileData.getImageData());
		System.out.println("imageFileData3 =" +imageFileData.getImageType());
		promotionDao.deletePromotionData(promotionId);
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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime sDate = LocalDateTime.parse(startDate, formatter);
        Date sDate1 = java.sql.Timestamp.valueOf(sDate);

        LocalDateTime eDate = LocalDateTime.parse(endDate, formatter);
        Date eDate1 = java.sql.Timestamp.valueOf(eDate);

        ltPromotion.setStartDate1("");
        ltPromotion.setEndDate1("");

		ltPromotion.setCreationDate(new Date());
		ltPromotion.setLastUpdateDate(new Date());
		
		String fileUploadPath = env.getProperty("fileUploadPath");
		String fileDownloadPath = env.getProperty("fileDownloadPath");
		//String imgDownloadPath =null;
		//if(file.getOriginalFilename()!=null) {
		String imgDownloadPath = fileDownloadPath+""+ file.getOriginalFilename();
		//}
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
			System.out.println("file data isss = "+file.getSize() + buffStream);
		}
		
//		ltPromotion.setImageName(file.getOriginalFilename()); this is original code
		//if(imgDownloadPath!= null) {
//		ltPromotion.setImageData(imgDownloadPath);            this is original code     
		//}
//		ltPromotion.setImageType(file.getContentType());      this is original code  

		if(file.getOriginalFilename()!= null) {
			ltPromotion.setImageName(file.getOriginalFilename());
			}else {
		            ltPromotion.setImageName(imageFileData.getImageName()); 
		}if(imgDownloadPath.equalsIgnoreCase("")) {
			ltPromotion.setImageData(imgDownloadPath);
		}else {  
		        ltPromotion.setImageData(imageFileData.getImageData());}
		if(file.getContentType()!= null) {
			ltPromotion.setImageType(file.getContentType());
		}else {
			    ltPromotion.setImageType(imageFileData.getImageType());}
		
		
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

					ltPromotion.setImageName(promotionImg.getImageName());   //old logic comment on 07-June-2024
					ltPromotion.setImageData(promotionImg.getImageData());
					ltPromotion.setImageType(promotionImg.getImageType());
					
					/*System.out.println("imageFileData4 =" +imageFileData.getImageName());
					System.out.println("imageFileData5 =" +imageFileData.getImageData());
					System.out.println("imageFileData6 =" +imageFileData.getImageType());
					ltPromotion.setImageName(imageFileData.getImageName());   
					ltPromotion.setImageData(imageFileData.getImageData());
					ltPromotion.setImageType(imageFileData.getImageType());*/
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

	
}
