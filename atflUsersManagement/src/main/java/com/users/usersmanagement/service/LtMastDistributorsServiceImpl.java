package com.users.usersmanagement.service;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastDistributorsDao;
import com.users.usersmanagement.dao.LtMastOrganisationsDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastDistributors;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.model.UserDto;
import com.users.usersmanagement.model.SiebelDto;

@Service
@PropertySource(value = "classpath:queries/messages.properties", ignoreResourceNotFound = true)
public class LtMastDistributorsServiceImpl implements LtMastDistributorsService, CodeMaster {

	@Autowired
	LtMastDistributorsDao ltMastDistributorsDao;

	@Autowired
	LtMastOrganisationsDao ltMastOrganisationsDao;

	@Autowired
	LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	AtflMastUsersDao ltMastUsersDao;

	@Autowired
	AtflMastUsersService ltMastUsersService;

	@Autowired
	private Environment env;

	@Override
	public Status verifyDistributor(String distributorCrmCode, String positionCode, String userCode, Long userId)
			throws ServiceException {

		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {
			LtMastDistributors ltMastDistributors = ltMastDistributorsDao.verifyDistributors(distributorCrmCode,
					positionCode, userCode);
			if (ltMastDistributors != null) {

				if (ltMastDistributors.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastDistributors.getEmpStatus().equalsIgnoreCase(ACTIVE)) {

						if (ltMastDistributors.getPositionStatus().equalsIgnoreCase(ACTIVE)) {

							ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
							
							ltMastUsers.setLastUpdateDate(new Date());

							//ltMastUsers.setOrgId(Long.parseLong(ltMastDistributors.getOrgId()));
							ltMastUsers.setOrgId(ltMastDistributors.getOrgId());

							ltMastUsers.setDistributorId(ltMastDistributors.getDistributorId());

							ltMastUsers.setUserType(RoleMaster.DISTRIBUTOR);
							
							ltMastUsers.setIsFirstLogin("N");
							

							ltMastUsers.setEmployeeCode(ltMastDistributors.getEmployeeCode());

							//LtMastDistributors ltDistributor = ltMastDistributorsDao.getLtDistributorsById(ltMastDistributors.getDistributorId());

							if (ltMastDistributors.getPrimaryMobile() != null) {
								
								if (ltMastDistributors.getPrimaryMobile().length() >= 10) {

									String mobileNumber = ltMastDistributors.getPrimaryMobile()
											.substring(ltMastDistributors.getPrimaryMobile().length() - 10);

									if (mobileNumber.equalsIgnoreCase(ltMastUsers.getMobileNumber())) {
										ltMastDistributors.setDistributorOperator("NO");
									} else {
										ltMastDistributors.setDistributorOperator("YES");
									}
								} else {
									ltMastDistributors.setDistributorOperator("YES");
								}
							} else {
								ltMastDistributors.setDistributorOperator("YES");
							}

							//ltMastUsers.setPositionId(Long.parseLong(ltMastDistributors.getPositionId()));
							ltMastUsers.setPositionId(ltMastDistributors.getPositionId());
							ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

							if (ltMastUsers != null) {
								status.setCode(SUCCESS);
								status.setMessage(env.getProperty("lonar.users.distributorverified"));
								
								ltMastDistributors.setDistributorCode(ltMastDistributors.getDistributorCrmCode());
								
								status.setData(ltMastDistributors);
								return status;

							}

						} else {
							status.setCode(FAIL);
							status.setMessage(
									ltMastCommonMessageService.getCommonMessage("lonar.users.position.noactive"));
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.distributor.noactive"));
					return status;
				}
			}
		}
		status.setCode(FAIL);
		status.setMessage(env.getProperty("lonar.users.distributornotverified"));
		status.setData(null);
		return status;
	}
	
	@Override
	public Status verifyDistributorV1(String distributorCrmCode,String distributorName,String proprietorName,Long userId)
			throws ServiceException {

		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserById = 0;
		long outQuerygetUserById = 0;
		long inQueryverifyDistributorsV1 = 0;
		long outQueryverifyDistributorsV1 = 0;
//		long inQuerygetInStockProductCountForAdmin = 0;
//		long outQuerygetInStockProductCountForAdmin = 0;
//		long inQuerygetMultipleMrpForProduct = 0;
//		long outQuerygetMultipleMrpForProduct = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		Status status = new Status();
		inQuerygetUserById = System.currentTimeMillis();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		outQuerygetUserById = System.currentTimeMillis();
		if (ltMastUsers != null) {
			inQueryverifyDistributorsV1 = System.currentTimeMillis();
			LtMastDistributors ltMastDistributors = ltMastDistributorsDao.verifyDistributorsV1(distributorCrmCode,
					distributorName, proprietorName);
			outQueryverifyDistributorsV1 = System.currentTimeMillis();
			if (ltMastDistributors != null) {

				if (ltMastDistributors.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastDistributors.getEmpStatus().equalsIgnoreCase(ACTIVE)) {

						if (ltMastDistributors.getPositionStatus().equalsIgnoreCase(ACTIVE)) {

							ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
							
							ltMastUsers.setLastUpdateDate(new Date());

							//ltMastUsers.setOrgId(Long.parseLong(ltMastDistributors.getOrgId()));
							ltMastUsers.setOrgId(ltMastDistributors.getOrgId());

							ltMastUsers.setDistributorId(ltMastDistributors.getDistributorId());

							ltMastUsers.setUserType(RoleMaster.DISTRIBUTOR);
							
							ltMastUsers.setIsFirstLogin("N");

							ltMastUsers.setEmployeeCode(ltMastDistributors.getEmployeeCode());

							//LtMastDistributors ltDistributor = ltMastDistributorsDao.getLtDistributorsById(ltMastDistributors.getDistributorId());

							if (ltMastDistributors.getPrimaryMobile() != null) {
								
								if (ltMastDistributors.getPrimaryMobile().length() >= 10) {

									String mobileNumber = ltMastDistributors.getPrimaryMobile()
											.substring(ltMastDistributors.getPrimaryMobile().length() - 10);

									if (mobileNumber.equalsIgnoreCase(ltMastUsers.getMobileNumber())) {
										ltMastDistributors.setDistributorOperator("NO");
									} else {
										ltMastDistributors.setDistributorOperator("YES");
									}
								} else {
									ltMastDistributors.setDistributorOperator("YES");
								}
							} else {
								ltMastDistributors.setDistributorOperator("YES");
							}

							//ltMastUsers.setPositionId(Long.parseLong(ltMastDistributors.getPositionId()));
							ltMastUsers.setPositionId(ltMastDistributors.getPositionId());
							ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);

							if (ltMastUsers != null) {
								status.setCode(SUCCESS);
								status.setMessage(env.getProperty("lonar.users.distributorverified"));
								
								ltMastDistributors.setDistributorCode(ltMastDistributors.getDistributorCrmCode());
								//ltMastDistributors.setDistributorCode(ltMastDistributors.getDistributorCode());
								status.setData(ltMastDistributors);
								
								timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserById,outQuerygetUserById));
								timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQueryverifyDistributorsV1,outQueryverifyDistributorsV1));
				//				timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
				//				timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
				//				timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
				//				timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
								
								long methodOut = System.currentTimeMillis();
								System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
						        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
						        status.setTimeDifference(timeDifference);
						        
								return status;

							}

						} else {
							status.setCode(FAIL);
							status.setMessage(
									ltMastCommonMessageService.getCommonMessage("lonar.users.position.noactive"));
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.employee.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.distributor.noactive"));
					return status;
				}
			}
		}
		status.setCode(FAIL);
		status.setMessage(env.getProperty("lonar.users.distributornotverified"));
		status.setData(null);
		return status;
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
	
	@Override
	public Status getAllDistributorAgainstAreahead(RequestDto requestDto)throws ServerException, ServiceException{
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeByUserId = 0;
		long outQuerygetUserTypeByUserId = 0;
		long inQuerygetAllDistributorAgainstAreahead = 0;
		long outQuerygetAllDistributorAgainstAreahead = 0;
		long inQuerygetAllDistributorAgainstSystemAdmin = 0;
		long outQuerygetAllDistributorAgainstSystemAdmin = 0;
//		long inQuerygetMultipleMrpForProduct = 0;
//		long outQuerygetMultipleMrpForProduct = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		try {
			inQuerygetUserTypeByUserId = System.currentTimeMillis();
			String userType= ltMastDistributorsDao.getUserTypeByUserId(requestDto.getUserId());
			outQuerygetUserTypeByUserId = System.currentTimeMillis();
			System.out.println("userType =" +userType);
			if(userType.equalsIgnoreCase("AREAHEAD") ) {
				inQuerygetAllDistributorAgainstAreahead = System.currentTimeMillis();
		List<LtMastDistributors> distributorList = ltMastDistributorsDao.getAllDistributorAgainstAreahead(requestDto);
		        outQuerygetAllDistributorAgainstAreahead = System.currentTimeMillis();
		System.out.println("distributorList \n"+distributorList);
		if (distributorList != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(distributorList);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
			}
			inQuerygetAllDistributorAgainstSystemAdmin = System.currentTimeMillis();
			List<LtMastDistributors> distributorList = ltMastDistributorsDao.getAllDistributorAgainstSystemAdmin(requestDto);
			inQuerygetAllDistributorAgainstSystemAdmin = System.currentTimeMillis();
			System.out.println("distributorList Admin =="+distributorList);
			if (distributorList != null) {
				status.setCode(SUCCESS);
				status.setMessage("RECORD FOUND SUCCESSFULLY");
				status.setData(distributorList);
				
				timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeByUserId,outQuerygetUserTypeByUserId));
				timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetAllDistributorAgainstAreahead,outQuerygetAllDistributorAgainstAreahead));
				timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetAllDistributorAgainstSystemAdmin, outQuerygetAllDistributorAgainstSystemAdmin));
		//		timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
		//		timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
		//		timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
				
				long methodOut = System.currentTimeMillis();
				System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
		        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
		        status.setTimeDifference(timeDifference);
			} //else {
				//status.setCode(FAIL);
				//status.setMessage("RECORD NOT FOUND");
			//}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;	
	}
	
	@Override
	public Status getAllNotification(RequestDto requestDto) throws ServerException, ServiceException{
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetAllNotification = 0;
		long outQuerygetAllNotification = 0;
		try {
			inQuerygetAllNotification = System.currentTimeMillis();
			List<NotificationDetails> notificationList  = ltMastDistributorsDao.getAllNotification(requestDto);
			outQuerygetAllNotification = System.currentTimeMillis();
			if(notificationList != null) {
				status.setCode(SUCCESS);
				status.setMessage("RECORD FOUND SUCCESSFULLY");
				status.setData(notificationList);
				timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetAllNotification,outQuerygetAllNotification));
//				timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetInStockProductAdmin,outQuerygetInStockProductAdmin));
//				timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
//				timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
//				timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//				timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
				
				long methodOut = System.currentTimeMillis();
				System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
		        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
		        status.setTimeDifference(timeDifference);
			}else {
				status.setCode(FAIL);
				status.setMessage("RECORD NOT FOUND");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	public Status getUserDataByIdForValidation(Long userId) throws ServerException, ServiceException {
		 Status status = new Status();
		 SiebelDto seibelUserdata = new SiebelDto();
		 Map<String,String> timeDifference = new HashMap<>();
			long methodIn = System.currentTimeMillis();
			long inQuerygetUserDataByIdForValidation = 0;
			long outQuerygetUserDataByIdForValidation = 0;
//			long inQuerygetInStockProductAdmin = 0;
//			long outQuerygetInStockProductAdmin = 0;
//			long inQuerygetInStockProductCountForAdmin = 0;
//			long outQuerygetInStockProductCountForAdmin = 0;
//			long inQuerygetMultipleMrpForProduct = 0;
//			long outQuerygetMultipleMrpForProduct = 0;
//			long inQuerygetInStockProductWithInventory = 0;
//			long outQuerygetInStockProductWithInventory = 0;
//			long inQuerygetInStockProductCountWithInventory = 0;
//			long outQuerygetInStockProductCountWithInventory = 0;
		 try {
			 
			 /*if(seibelUserdata.getMobileNumber()==("8806962104") || seibelUserdata.getMobileNumber() =="7013600327" || 
					 seibelUserdata.getMobileNumber() =="7796551219" || seibelUserdata.getMobileNumber() =="9987792717" || 
					 seibelUserdata.getMobileNumber() =="9775977932" || seibelUserdata.getMobileNumber() =="6370239243" ||
			         seibelUserdata.getMobileNumber() =="8092383807")
			 {*/
			 System.out.println("userId =" +userId);
			 //SiebelDto seibelUserdata = new SiebelDto();
			 inQuerygetUserDataByIdForValidation = System.currentTimeMillis();
			 seibelUserdata = ltMastDistributorsDao.getUserDataByIdForValidation(userId);
			 outQuerygetUserDataByIdForValidation = System.currentTimeMillis();
			 /*}else {
			
				//String mobileNo= user.getMobileNumber();
				 String mobileNo= seibelUserdata.getMobileNumber();
				//LtMastUsers	seibelUserdata= ltMastUsersDao.getLtMastUsersByMobileNumber(mobileNo);
				 LtMastUsers	seibelUserdata1= ltMastUsersDao.getLtMastUsersByMobileNumber(mobileNo);
				//UserDto	seibelUserdata = ltMastUsersDao.verifyUserDetailsByMobileNumbervInSiebel(mobileNo);
				 UserDto	seibelUserdata2 = ltMastUsersDao.verifyUserDetailsByMobileNumbervInSiebel(mobileNo);
				System.out.println("userId =" +userId);
			 }*/
			 if(seibelUserdata!=null) {
				 
				 if(seibelUserdata.getStatus() !=null) {
			      seibelUserdata.setStatus(seibelUserdata.getStatus().toUpperCase());
				 }			 
			      seibelUserdata.setOrgId("1");
			 
			 if(seibelUserdata.getUserType().equalsIgnoreCase("Sales Person")) {
				 seibelUserdata.setUserType("SALES");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Area Head")) {
					seibelUserdata.setUserType("AREAHEAD");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Distributor Proprietor")) {
					seibelUserdata.setUserType("DISTRIBUTOR");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Sales Officer")) {
					seibelUserdata.setUserType("SALESOFFICER");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Organization User")) {
					seibelUserdata.setUserType("ORGANIZATION_USER");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("System Administrator")) {
					seibelUserdata.setUserType("SYSTEMADMINISTRATOR");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("RETAILER")) {
					seibelUserdata.setUserType("RETAILER");
				}
			 
				seibelUserdata.setTerritory(seibelUserdata.getTerritory());
				
				System.out.println("seibelUserdata =" +seibelUserdata);
				if(seibelUserdata!= null) {
					status.setCode(SUCCESS);
					status.setMessage("RECORD FOUND SUCCESSFULLY");
					status.setData(seibelUserdata);
					
					timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetUserDataByIdForValidation,outQuerygetUserDataByIdForValidation));
					
					long methodOut = System.currentTimeMillis();
					System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
			        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
			        status.setTimeDifference(timeDifference);
				}
			}
			 else {
					status.setCode(FAIL);
					status.setMessage("RECORD NOT FOUND");
				} 
			 
		 }catch(Exception e) {
			 e.printStackTrace();}
		return status;
	}

	@Override
	public Status saveSeibelUserData(Long userId) throws ServerException, ServiceException {
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserDataByIdForValidation = 0;
		long outQuerygetUserDataByIdForValidation = 0;
		Status status = new Status();
		LtMastUsers user = new LtMastUsers();
		//LtMastUsers userData = ltMastUsersDao.getLtMastUsersByMobileNumber(userId);
		//System.out.println("mobileNo =" +userId);
		//System.out.println("userData =" +userData);
		//UserDto siebelUserData = ltMastUsersDao.verifyUserDetailsByMobileNumbervInSiebel(userId);
		//System.out.println("siebelUserData2 =" +userData);
		
		SiebelDto seibelUserdata = new SiebelDto();
		
		inQuerygetUserDataByIdForValidation =  System.currentTimeMillis();
		seibelUserdata = ltMastDistributorsDao.getUserDataByIdForValidation(userId);
		outQuerygetUserDataByIdForValidation =  System.currentTimeMillis();
		
		//if(userData.getMobileNumber()==seibelUserdata.getMobileNumber())
		if(seibelUserdata!= null) {
			
			user.setOutletId(seibelUserdata.getOutletId()); 
			user.setRecentSerachId(seibelUserdata.getRecentSearchId()); 
			//siebelUserData.setOrgId("1");
			user.setOrgId("1");
			user.setDistributorId(seibelUserdata.getDistributorId());
			
			user.setStatus(seibelUserdata.getStatus().toUpperCase());
			user.setEmployeeCode(seibelUserdata.getEmployeeCode());
			user.setUserName(seibelUserdata.getUserName());
			user.setLatitud(seibelUserdata.getLatitude());
			user.setLongitud(seibelUserdata.getLongitude());
			
			if(seibelUserdata.getUserType().equalsIgnoreCase("Sales Person")) {
				user.setUserType("SALES");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Area Head")) {
					user.setUserType("AREAHEAD");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Distributor Proprietor")) {
					user.setUserType("DISTRIBUTOR");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Sales Officer")) {
					user.setUserType("SALESOFFICER");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("Organization User")) {
					user.setUserType("ORGANIZATION_USER");
				}
				if(seibelUserdata.getUserType().equalsIgnoreCase("System Administrator")) {
					user.setUserType("SYSTEMADMINISTRATOR");
				}
//				if(seibelUserdata.getUserType().equalsIgnoreCase("RETAILER")) {
//					user.setUserType("RETAILER");
//				}
			
			//user.setUserType(seibelUserdata.getUserType()); 
			user.setAddress(seibelUserdata.getAddress());
			user.setEmail(seibelUserdata.getEmail()); 
			user.setHomephNum(seibelUserdata.getHomephNum());
			user.setMobileNumber(seibelUserdata.getMobileNumber()); 
			user.setAsstOPhNum(seibelUserdata.getAsstPhNum());
			user.setAddressDetails(seibelUserdata.getAddressDetails()); 
			user.setPositionId(seibelUserdata.getPositionId());
			user.setCreatedBy(-1L); 
			user.setLastUpdatedBy(-1L);
			user.setLastUpdateLogin(-1L);
			user.setCreationDate(seibelUserdata.getCreationDate()); 
			user.setLastUpdateDate(seibelUserdata.getLastUpdateDate());
			user.setUserId(userId);
			user.setIsFirstLogin("N");
			user.setTerritory(seibelUserdata.getTerritory());
						
			user = ltMastUsersDao.saveLtMastUsers(user);
			//user = ltMastDistributorsDao.saveSeibelUserData(user, userId);
		}
		if(user!= null) {
			status.setCode(SUCCESS);
			status.setMessage("Update Successfully");
			status.setData(user);
			
			timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetUserDataByIdForValidation,outQuerygetUserDataByIdForValidation));
			
			long methodOut = System.currentTimeMillis();
			System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
	        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	        status.setTimeDifference(timeDifference);
			return status;
		}
		
		return null;
	}

	
	@Override
	public Status deleteNotificationAfter72Hours() throws ServerException, ServiceException {
	        Status status= new Status();    
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR, -72);
			Date cutoffDate = calendar.getTime(); 
			
			NotificationDetails notificationDetails= ltMastDistributorsDao.deleteNotificationAfter72Hours(cutoffDate);
	    	   
			if(notificationDetails == null) {
				status.setCode(DELETE_SUCCESSFULLY);
				status.setMessage("Record_Deleted_Successfully");
				return status;
			}else {
				status.setCode(FAIL);
				status.setMessage("Recorde_Not_Deleted");
				return status;
			}
			
	       }catch(Exception e) {
	    	   e.printStackTrace();
	       } 
		
		return status;
	}

	@Override
	public Status updateReadNotificationFlag(RequestDto requestDto) throws ServerException, ServiceException {
		Status status = new Status();
		try {
			if(requestDto!= null) {
				
				System.out.println("Notifi id is "+requestDto.getNotificationId());
				ltMastDistributorsDao.updateReadNotificationFlag(requestDto.getNotificationId());
			
			//if(notificationDetails.getReadFlag().equalsIgnoreCase("Y")) {
				status.setCode(UPDATE_SUCCESSFULLY);
				status.setMessage("Record_Updated_Successfully");
				return status;
			//}
			}/*else {
				status.setCode(FAIL);
				status.setMessage("Recorde_Not_Deleted");
				return status;
			}*/
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;
	}
}
