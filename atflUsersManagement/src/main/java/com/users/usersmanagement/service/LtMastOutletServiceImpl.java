package com.users.usersmanagement.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.controller.WebController;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastOutletDao;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastOrganisations;
//import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutletsChannel;
import com.users.usersmanagement.model.LtMastOutletsDump;
import com.users.usersmanagement.model.LtMastOutletsType;
import com.users.usersmanagement.model.LtMastPricelist;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.repository.LtMastOutletDumpRepository;
import com.users.usersmanagement.repository.LtMastOutletRepository;
import com.users.usersmanagement.repository.LtMastUsersRepository;

@Service
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastOutletServiceImpl implements LtMastOutletService, CodeMaster {

	@Autowired
	private LtMastOutletDao ltMastOutletDao;

	@Autowired
	private LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	AtflMastUsersDao ltMastUsersDao;

	@Autowired
	AtflMastUsersService ltMastUsersService;

	@Autowired
	LtMastOutletRepository ltMastOutletRepository;

	@Autowired
	LtMastOutletDumpRepository ltMastOutletDumpRepository;
	
	@Autowired
	LtMastUsersRepository ltMastUsersRepository;

	@Autowired
	private Environment env;

	@Autowired
	private WebController webController;
	
	@Override
	public Status verifyOutlet(String outletCode, String distributorCrmCode, Long userId)
			throws ServiceException, IOException {
		Status status = new Status();
		LtMastUsers ltMastUsers = ltMastUsersDao.getUserById(userId);
		if (ltMastUsers != null) {
			LtMastOutlets ltMastOutlets = ltMastOutletDao.verifyOutlet(outletCode, distributorCrmCode);
			if (ltMastOutlets != null) {

				if (ltMastOutlets.getStatus().equalsIgnoreCase(ACTIVE)) {

					if (ltMastOutlets.getDistributorStatus().equalsIgnoreCase(ACTIVE)) {

						ltMastUsers.setLastUpdatedBy(ltMastUsers.getUserId());
						ltMastUsers.setLastUpdateDate(new Date());

						ltMastUsers.setOrgId(ltMastOutlets.getOrgId());

						ltMastUsers.setDistributorId(ltMastOutlets.getDistributorId());

						ltMastUsers.setOutletId(ltMastOutlets.getOutletId());

						ltMastUsers.setUserType(RoleMaster.RETAILER);

						ltMastUsers.setPositionId(ltMastOutlets.getPositionsId());

						ltMastUsers = ltMastUsersDao.saveLtMastUsers(ltMastUsers);
						if (ltMastUsers != null) {
							status.setCode(SUCCESS);
							status.setMessage("VERFIED");
							ltMastOutlets.setDistributorCode(ltMastOutlets.getDistributorCrmCode());
							status.setData(ltMastOutlets);
							return status;
						}

					} else {
						status.setCode(FAIL);
						status.setMessage(
								ltMastCommonMessageService.getCommonMessage("lonar.users.distributor.noactive"));
						return status;
					}

				} else {
					status.setCode(FAIL);
					status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.retailer.noactive"));
					return status;
				}
			}
		}
		status.setCode(FAIL);
		status.setMessage(ltMastCommonMessageService.getCommonMessage("lonar.users.outletnotverified"));
		status.setData(null);
		return status;
	}

	@Override
	public Status getOutlet(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		System.out.println("requestDto"+requestDto);
		List<LtMastOutlets> list = ltMastOutletDao.getOutlet(requestDto);
		System.out.println("list"+list);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		return status;
	}

	@Override
	public Status getAllUserDataByRecentId(Long userId) throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUser = ltMastUsersDao.getUserById(userId);

		if (ltMastUser != null) {

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.RETAILER)) {

				LtMastUsers LtMastUser = ltMastOutletDao.getMastDataByOutletId(ltMastUser.getOutletId());

				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);

				status.setData(LtMastUser);

				return status;

			} else if (ltMastUser.getRecentSerachId() != null) {

				LtMastUsers LtMastUser = ltMastOutletDao
						.getMastDataByOutletId(ltMastUser.getRecentSerachId().toString());

				if (LtMastUser != null) {
					status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
					status.setData(LtMastUser);
					return status;
				}
			}
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status createOutlet(LtMastOutlets ltMastOutlets) throws ServiceException, IOException {
		Status status = new Status();

		LtMastOutletsDump ltMastOutletsDump = new LtMastOutletsDump();

		if (ltMastOutlets != null) {

			ltMastOutletsDump.setDistributorId(ltMastOutlets.getDistributorId());
			ltMastOutletsDump.setOutletType(ltMastOutlets.getOutletType());
			ltMastOutletsDump.setOutletName(ltMastOutlets.getOutletName());
			if (ltMastOutlets.getProprietorName() != null) {
				ltMastOutletsDump.setProprietorName(ltMastOutlets.getProprietorName());
			}
			ltMastOutletsDump.setOutletChannel(ltMastOutlets.getOutletChannel());
			ltMastOutletsDump.setAddress1(ltMastOutlets.getAddress1());
			ltMastOutletsDump.setAddress2(ltMastOutlets.getAddress2());
			ltMastOutletsDump.setLandmark(ltMastOutlets.getLandmark());
			ltMastOutletsDump.setCountry("INDIA");
			ltMastOutletsDump.setState(ltMastOutlets.getState());
			ltMastOutletsDump.setCity(ltMastOutlets.getCity());
			ltMastOutletsDump.setPin_code(ltMastOutlets.getPin_code());

			if (ltMastOutlets.getRegion() != null) {
				ltMastOutletsDump.setRegion(ltMastOutlets.getRegion());
			}
			ltMastOutletsDump.setArea(ltMastOutlets.getArea());
			ltMastOutletsDump.setTerritory(ltMastOutlets.getTerritory());
			ltMastOutletsDump.setPrimaryMobile(ltMastOutlets.getPrimaryMobile());
			ltMastOutletsDump.setStatus("DRAFT");
			ltMastOutletsDump.setPriceList(ltMastOutlets.getPriceList());
			ltMastOutletsDump.setOrgId(ltMastOutlets.getOrgId());

			ltMastOutletsDump.setCreatedBy(ltMastOutlets.getUserId());
			ltMastOutletsDump.setLastUpdatedBy(ltMastOutlets.getUserId());
			ltMastOutletsDump.setLastUpdateLogin(ltMastOutlets.getUserId());
			ltMastOutletsDump.setCreationDate(new Date());
			ltMastOutletsDump.setLastUpdateDate(new Date());

			try {
			LtMastOutletsDump ltMastOutletsDumpupdated = ltMastOutletDumpRepository.save(ltMastOutletsDump);
			
			LtMastUsers ltMastUsersSysAdmin = ltMastOutletDao.getSystemAdministartorDetails(ltMastOutletsDumpupdated.getOrgId());
			if(ltMastUsersSysAdmin!=null) {
			webController.sendOutletApprovalNotification(ltMastUsersSysAdmin, ltMastOutletsDumpupdated);
			}
			
			if (ltMastOutletsDumpupdated != null) {
				// send notification to mapped system administrator and sales officer
				
				List<LtMastUsers> ltMastUsers = ltMastOutletDao.getAllSalesOfficerAgainstDist(ltMastOutletsDumpupdated.getDistributorId(),
						ltMastOutletsDumpupdated.getOrgId());
				for(LtMastUsers user:ltMastUsers) {
					System.out.println("user"+user);
					if(user !=null) {
				webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
				}}

				status.setMessage("Send For approval.");
				status.setData(ltMastOutletsDumpupdated);
				status.setCode(INSERT_SUCCESSFULLY);
			} else {
				status.setMessage("INSERT_FAIL");
				status.setData(null);
				status.setCode(INSERT_FAIL);
			}
			}catch (Exception e) {
			    // Log the exception or handle it accordingly
			    e.printStackTrace();
			}
			
		}

		return status;
	}

	@Override
	public Status getAllOutletType() throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastOutletsType> list = ltMastOutletDao.getAllOutletType();
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		return status;
	}

	@Override
	public Status getAllOutletChannel() throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastOutletsChannel> list = ltMastOutletDao.getAllOutletChannel();
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		return status;
	}

	@Override
	public Status getPriceListAgainstDistributor(String outletId) throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastPricelist> list = ltMastOutletDao.getPriceListAgainstDistributor(outletId);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		return status;
	}

	@Override
	public Status getPendingAprrovalOutlet(RequestDto requestDto) throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastOutletsDump> list = ltMastOutletDao.getPendingAprrovalOutlet(requestDto);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		return status;
	}

	@Override
	public Status approveOutlet(LtMastOutletsDump ltMastOutletsDumps) throws ServiceException, IOException {
		Status status = new Status();
try {
		//bring object from databse, change status, lastupdatedby,lastupdatedlogin and date  n save
		LtMastOutletsDump ltMastOutletsDump = ltMastOutletDao.getOutletToChangeStatus(ltMastOutletsDumps.getDistributorId(),
				ltMastOutletsDumps.getOrgId(),ltMastOutletsDumps.getPrimaryMobile(),ltMastOutletsDumps.getOutletName());
		
		ltMastOutletsDump.setStatus(ltMastOutletsDumps.getStatus());
		ltMastOutletsDump.setLastUpdatedBy(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateLogin(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateDate(new Date());
		
		ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
		  LtMastOrganisations ltMastOrganisations =
		  ltMastOutletDao.getOrganisationDetailsById(ltMastOutletsDump.getOrgId());
		  System.out.println("ltMastOrganisations"+ltMastOrganisations);
			/*
			 * RelatedOrganization relatedOrganizationDetails = new RelatedOrganization();
			 * relatedOrganizationDetails.setIsPrimaryMVG("Y");
			 * relatedOrganizationDetails.setOrganization(ltMastOrganisations.
			 * getOrganisationName());
			 * 
			 * ListOfRelatedOrganization listOfRelatedOrganization = new
			 * ListOfRelatedOrganization();
			 * listOfRelatedOrganization.setRelatedOrganization(relatedOrganizationDetails);
			 */
		  
			/*
			 * BusinessAddress businessAddress = new BusinessAddress();
			 * businessAddress.setAddressId("1");
			 * businessAddress.setStreetAddress(ltMastOutletsDump.getAddress1());
			 * businessAddress.setStreetAddress2(ltMastOutletsDump.getAddress2());
			 * businessAddress.setCounty(""); businessAddress.setCounty("INDIA");
			 * businessAddress.setCity(ltMastOutletsDump.getCity());
			 * businessAddress.setState(ltMastOutletsDump.getState());
			 * businessAddress.setPostalCode(ltMastOutletsDump.getPin_code());
			 * businessAddress.setProvince(""); businessAddress.setIsPrimaryMVG("Y");
			 */
		  
			/*
			 * ListOfBusinessAddress listOfBusinessAddress = new ListOfBusinessAddress();
			 * listOfBusinessAddress.setListOfBusinessAddress(businessAddress);
			 */
		  
			/*
			 * Account account = new Account();
			 * 
			 * account.setAccountStatus("NEW");
			 * account.setType(ltMastOutletsDump.getOutletType());
			 * account.setAccountId("1");
			 * account.setRuleAttribute2(ltMastOutletsDump.getOutletChannel());
			 * account.setName(ltMastOutletsDump.getOutletName());
			 * account.setaTTerritory(ltMastOutletsDump.getTerritory());
			 * account.setLocation("need to add");
			 * account.setListOfBusinessAddress(listOfBusinessAddress);
			 * account.setListOfRelatedOrganization(listOfRelatedOrganization);
			 * 
			 * ListOfOutletInterface listOfOutletInterface = new ListOfOutletInterface();
			 * listOfOutletInterface.setAccount(account);
			 * 
			 * SiebelMessage siebelMessage = new SiebelMessage();
			 * siebelMessage.setIntObjectFormat("Siebel Hierarchical");
			 * siebelMessage.setIntObjectName("Outlet Interface");
			 * siebelMessage.setMessageId("");
			 * siebelMessage.setMessageType("Integration Object");
			 * siebelMessage.setListOfOutletInterface(listOfOutletInterface);
			 * 
			 * SiebelMessageRequest SiebelMessageRequest = new SiebelMessageRequest();
			 * SiebelMessageRequest.setSiebelMessage(siebelMessage);
			 */
		  
		  JSONObject relatedOrganizationDetail = new JSONObject();
		  
		  relatedOrganizationDetail.put("IsPrimaryMVG","Y");
		  relatedOrganizationDetail.put("Organization", ltMastOrganisations.getOrganisationName());
		  
		  
		  JSONObject listOfRelatedOrganizations = new JSONObject();
		  
		  listOfRelatedOrganizations.put("Related Organization",relatedOrganizationDetail);
		  
		  JSONObject businessAddress = new JSONObject();
		  businessAddress.put("Address Id", "1");
		 // businessAddress.put("Street Address", ltMastOutletsDump.getAddress1());
		  businessAddress.put("Street Address","South primary");
		  businessAddress.put("County", "");
		 // businessAddress.put("Street Address 2",ltMastOutletsDump.getAddress2());
		  businessAddress.put("Street Address 2","kalupurr");
		 // businessAddress.put("City", ltMastOutletsDump.getCity());
		  businessAddress.put("City","Ahmadabad" );
		 // businessAddress.put("State", ltMastOutletsDump.getState());
		  businessAddress.put("State", "Maharashtra");
		  businessAddress.put("Country", "India");
		  //businessAddress.put("Postal Code", ltMastOutletsDump.getPin_code());
		  businessAddress.put("Postal Code","380708");
		  businessAddress.put("Province", "");
		  businessAddress.put("IsPrimaryMVG", "Y");
		  
		  
		  JSONArray businessAddres = new JSONArray();
			businessAddres.put(businessAddress);
		 
		  JSONObject listOfBusinessAddres = new JSONObject();
		  
		  listOfBusinessAddres.put("Business Address", businessAddres);
		  
		  
		  
		  JSONObject accounts = new JSONObject();
		  accounts.put("Account Status", "NEW");
		 // accounts.put("Type", ltMastOutletsDump.getOutletType());
		  accounts.put("Type", "Retailer");
		  accounts.put("Account Id", "1");
		  //accounts.put("Rule Attribute 2", ltMastOutletsDump.getOutletChannel());
		  accounts.put("Rule Attribute 2", "Whole Sellers");
		  //accounts.put("Name", ltMastOutletsDump.getOutletName());
		  accounts.put("Name", "Sardar Traders");
		  accounts.put("AT Territory","30801:AMDAVAD RURAl");
		  accounts.put("Location", "Baner");
		  accounts.put("ListOfBusiness Address", listOfBusinessAddres);
		  accounts.put("ListOfRelated Organization", listOfRelatedOrganizations);
		  
		  JSONObject listOfOutletInterfaces = new JSONObject();
		  listOfOutletInterfaces.put("Account",accounts);
		  
		  JSONObject siebelMassage = new JSONObject();
		  siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
		  siebelMassage.put("MessageId", "");
		  siebelMassage.put("IntObjectName","Outlet Interface");
		  siebelMassage.put("MessageType","Integration Object");
		  siebelMassage.put("ListOfOutlet Interface", listOfOutletInterfaces);
		  
		  
		  JSONObject siebelMassages = new JSONObject();
		  
		  siebelMassages.put("SiebelMessage", siebelMassage);
		  
		  String apiUrl = env.getProperty("SiebelCreateOutletApi");
		  
		  URL url = new URL(apiUrl);
		  
		  String username = "Lonar_Test"; String password = "Lonar123"; String
		  credentials = username + ":" + password;
		  
		  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		  connection.setRequestMethod("POST"); connection.setDoOutput(true);
		  connection.setRequestProperty("Content-Type", "application/json");
		  connection.setRequestProperty("Authorization", "Basic_Auth"+credentials);
		  
		  
		  //ObjectMapper objectMapper = new ObjectMapper(); 
		  String jsonPayload =siebelMassages.toString();
		  
		  //Map<String, Object> jsonData = objectMapper.readValue(jsonPayload, Map.class);
		 
		  //Map<String, Object> transformedData = transformKeys(jsonData);
		  
		 System.out.println("jsonPayload"+jsonPayload);
		  try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
			  wr.writeBytes(jsonPayload);
		  wr.flush(); }

		  
		  
		  // Get the HTTP response code 
		  int responseCode = connection.getResponseCode(); 
		  System.out.println("Response Code: " + responseCode);
		  
		  // Read the response 
		  BufferedReader reader; 
		  if (responseCode ==  HttpURLConnection.HTTP_OK) {
			  reader = new BufferedReader(new
		  InputStreamReader(connection.getInputStream())); 
			  } else { 
				  reader = new BufferedReader(new InputStreamReader(connection.getErrorStream())); }
		  
		  // Read the response 
		  String line; 
		  StringBuilder response = new
		  StringBuilder();
		  
		  while ((line = reader.readLine()) != null) { response.append(line); }
		  reader.close();
		  
		  // Print the response System.out.println("Response: " + response.toString());
		  
		  // Close the connection connection.disconnect(); 
		  String outletCode = null;   //once you got response add outlet code and pass it here.
		  LtMastOutlets outletDetails =ltMastOutletDao.getOutletByOutletCode(outletCode);
		  LtMastUsers ltMastUsers = new LtMastUsers();
		  if(outletDetails !=null) {
		
		  ltMastUsers.setOrgId(outletDetails.getOrgId());
		  ltMastUsers.setDistributorId(outletDetails.getDistributorId());
		  ltMastUsers.setOutletId(outletDetails.getOutletId());
		  ltMastUsers.setMobileNumber(outletDetails.getPrimaryMobile());
		  ltMastUsers.setUserType("RETAILER");
		  ltMastUsers.setUserName(outletDetails.getOutletName());
		  ltMastUsers.setEmployeeCode(outletDetails.getPosition());
		  ltMastUsers.setPositionId(outletDetails.getPositionsId());
		  ltMastUsers.setAddress(outletDetails.getAddress1() + " "+outletDetails.getAddress2());
		  ltMastUsers.setAddressDetails(outletDetails.getAddress1() + " "+outletDetails.getAddress2()
		  +" "+outletDetails.getLandmark()+ " "+outletDetails.getArea() +" "+outletDetails.getCity()+" "+outletDetails.getState()
		  +" "+outletDetails.getCountry() +" "+outletDetails.getPin_code());
		  
		  ltMastUsers = ltMastUsersRepository.save(ltMastUsers);
		  }

		  if(outletDetails !=null) {
		  status.setCode(INSERT_SUCCESSFULLY); 
		  status.setData(outletDetails);
		  status.setMessage("INSERT_SUCCESSFULLY");
		  } else {
		  status.setCode(INSERT_FAIL); 
		  status.setMessage("INSERT_FAIL"); 
		  }
}catch (Exception e) {
    // Log the exception or handle it accordingly
    e.printStackTrace();
}
	return status;	 
	}
	
	private static Map<String, Object> transformKeys(Map<String, Object> originalData) {
        Map<String, Object> transformedData = new HashMap<>();

        for (Map.Entry<String, Object> entry : originalData.entrySet()) {
            String originalKey = entry.getKey();
            Object value = entry.getValue();

            // Convert key to initcap with spaces
            String transformedKey = convertToInitcapWithSpaces(originalKey);

            // Add the transformed key-value pair to the new map
            transformedData.put(transformedKey, value);
        }

        return transformedData;
    }
	
	  private static String convertToInitcapWithSpaces(String originalKey) {
	        StringBuilder result = new StringBuilder();
	        boolean capitalizeNext = true;

	        for (char ch : originalKey.toCharArray()) {
	            if (Character.isSpaceChar(ch)) {
	                capitalizeNext = true;
	            } else if (capitalizeNext) {
	                result.append(Character.toUpperCase(ch));
	                capitalizeNext = false;
	            } else {
	                result.append(Character.toLowerCase(ch));
	            }
	        }

	        return result.toString();
	    }
}
