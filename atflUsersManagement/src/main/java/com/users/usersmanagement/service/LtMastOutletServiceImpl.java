package com.users.usersmanagement.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import com.users.usersmanagement.model.BeatDetailsDto;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastOrganisations;
//import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutletsChannel;
import com.users.usersmanagement.model.LtMastOutletsDump;
import com.users.usersmanagement.model.LtMastOutletsType;
import com.users.usersmanagement.model.LtMastPricelist;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.OutletSequenceData;
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
						
						ltMastUsers.setIsFirstLogin("N");

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
	public Status createOutlet(LtMastOutletsDump ltMastOutlets) throws ServiceException, IOException {
		Status status = new Status();

		LtMastOutletsDump ltMastOutletsDump = new LtMastOutletsDump();

		if (ltMastOutlets != null) {
			System.out.println("ltMastOutlets"+ltMastOutlets);
				if (ltMastOutlets.getOutletCode()== null) {
			ltMastOutletsDump.setDistributorId(ltMastOutlets.getDistributorId());
			ltMastOutletsDump.setOutletType(ltMastOutlets.getOutletType());
			ltMastOutletsDump.setOutletName(ltMastOutlets.getOutletName());
			
			if (ltMastOutlets.getDistributorName() != null) {
				ltMastOutletsDump.setDistributorName(ltMastOutlets.getDistributorName());
			}
			
			  if (ltMastOutlets.getPositionsId() != null) {
			  ltMastOutletsDump.setPositionsId(ltMastOutlets.getPositionsId()); 
			  }
			 
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
			ltMastOutletsDump.setStatus("PENDING_APPROVAL");
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
				if(ltMastUsers !=null) {
				for(LtMastUsers user:ltMastUsers) {
					System.out.println("user"+user);
					if(user !=null) {
				webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
				}}
				} 

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
		else {
System.out.println("in else"+ltMastOutlets.getOutletCode());
			ltMastOutletsDump = ltMastOutletDao.getoutletByIdAndCode(ltMastOutlets.getOutletCode());
			
			if(ltMastOutletsDump == null)
			{
				
				ltMastOutletsDump.setDistributorId(ltMastOutlets.getDistributorId());
				ltMastOutletsDump.setOutletType(ltMastOutlets.getOutletType());
				ltMastOutletsDump.setOutletName(ltMastOutlets.getOutletName());
				
				if (ltMastOutlets.getDistributorName() != null) {
					ltMastOutletsDump.setDistributorName(ltMastOutlets.getDistributorName());
				}
				
				  if (ltMastOutlets.getPositionsId() != null) {
				  ltMastOutletsDump.setPositionsId(ltMastOutlets.getPositionsId()); 
				  }
				 
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
				ltMastOutletsDump.setStatus("PENDING_APPROVAL");
				ltMastOutletsDump.setPriceList(ltMastOutlets.getPriceList());
				ltMastOutletsDump.setOrgId(ltMastOutlets.getOrgId());

				ltMastOutletsDump.setCreatedBy(ltMastOutlets.getUserId());
				ltMastOutletsDump.setLastUpdatedBy(ltMastOutlets.getUserId());
				ltMastOutletsDump.setLastUpdateLogin(ltMastOutlets.getUserId());
				ltMastOutletsDump.setCreationDate(new Date());
				ltMastOutletsDump.setLastUpdateDate(new Date());

			
			}else {
				ltMastOutletsDump.setPrimaryMobile(ltMastOutlets.getPrimaryMobile());
				ltMastOutletsDump.setStatus("PENDING_APPROVAL");
				ltMastOutletsDump.setLastUpdatedBy(ltMastOutlets.getUserId());
				ltMastOutletsDump.setLastUpdateLogin(ltMastOutlets.getUserId());
				ltMastOutletsDump.setLastUpdateDate(new Date());
			}

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
				if(ltMastUsers !=null) {
				for(LtMastUsers user:ltMastUsers) {
					System.out.println("user"+user);
					if(user !=null) {
				webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
				}}
				} 

				status.setMessage("Send For approval.");
				status.setData(ltMastOutletsDumpupdated);
				status.setCode(UPDATE_SUCCESSFULLY);
			} else {
				status.setMessage("UPDATE_FAIL");
				status.setData(null);
				status.setCode(UPDATE_FAIL);
			}
			}catch (Exception e) {
			    // Log the exception or handle it accordingly
			    e.printStackTrace();
			}
			
		
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
	public Status getPriceListAgainstDistributor(String distributorId) throws ServiceException, IOException {
		Status status = new Status();
		try {
		List<LtMastPricelist> list = ltMastOutletDao.getPriceListAgainstDistributor(distributorId);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
		}
		
		}catch(Exception e) {
			e.printStackTrace();
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
	
	if(ltMastOutletsDumps.getStatus().equalsIgnoreCase("APPROVED")) {
		//bring object from databse, change status, lastupdatedby,lastupdatedlogin and date  n save
		LtMastOutletsDump ltMastOutletsDump = ltMastOutletDao.getOutletToChangeStatus(ltMastOutletsDumps.getDistributorId(),
				ltMastOutletsDumps.getOrgId(),ltMastOutletsDumps.getPrimaryMobile());
		System.out.println("ltMastOutletsDump is ---"+ltMastOutletsDump);
		ltMastOutletsDump.setStatus(ltMastOutletsDumps.getStatus());
		ltMastOutletsDump.setLastUpdatedBy(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateLogin(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateDate(new Date());
		
		ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
		  LtMastOrganisations ltMastOrganisations =
		  ltMastOutletDao.getOrganisationDetailsById(ltMastOutletsDump.getOrgId());
		  System.out.println("ltMastOrganisations"+ltMastOrganisations);

		  
		  String url = "https://10.245.4.70:9014/siebel/v1.0/service/Siebel%20Outlet%20Integration/InsertOrUpdate?matchrequestformat=y";
         System.out.println("Url is---" + url);
		  String method = "POST";
          String contentType = "Content-Type: application/json";
          String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw==";

		  
		  JSONObject relatedOrganizationDetail = new JSONObject();
		  
		  relatedOrganizationDetail.put("IsPrimaryMVG","Y");
		  relatedOrganizationDetail.put("Organization", ltMastOrganisations.getOrganisationName());
//		  relatedOrganizationDetail.put("Organization", "JSB AGENCIES");
		  
		  
		  JSONObject listOfRelatedOrganizations = new JSONObject();
		  
		  listOfRelatedOrganizations.put("Related Organization",relatedOrganizationDetail);
		  
		  JSONObject businessAddress = new JSONObject();
		  businessAddress.put("Address Id", "1");
		  businessAddress.put("Street Address", ltMastOutletsDump.getAddress1());
//		  businessAddress.put("Street Address","South East");
		  businessAddress.put("County", "");
		  businessAddress.put("Street Address 2",ltMastOutletsDump.getAddress2());
//		  businessAddress.put("Street Address 2","Pune");
		  businessAddress.put("City", ltMastOutletsDump.getCity());
//		  businessAddress.put("City","Aurangabad" );
		  businessAddress.put("State", ltMastOutletsDump.getState());
//		  businessAddress.put("State", "MH");
		  businessAddress.put("Country", "India");
		  businessAddress.put("Postal Code", ltMastOutletsDump.getPin_code());
//		  businessAddress.put("Postal Code","411045");
		  businessAddress.put("Province", "");
		  businessAddress.put("IsPrimaryMVG", "Y");
		  		  		 
		  JSONObject listOfBusinessAddress = new JSONObject();
		  
		  listOfBusinessAddress.put("Business Address", businessAddress);  
//		  listOfBusinessAddres.put("ListOfRelated Organization", listOfRelatedOrganizations);
		  
		  JSONObject accounts = new JSONObject();
		  accounts.put("Account Status", "Active");
		  accounts.put("Type", ltMastOutletsDump.getOutletType());
//		  accounts.put("Type", "Retailer");
		  accounts.put("Account Id", "1");
		  accounts.put("Rule Attribute 2", ltMastOutletsDump.getOutletChannel());
//		  accounts.put("Rule Attribute 2", "Whole Sellers");
		  accounts.put("Name", ltMastOutletsDump.getOutletName());
//		  accounts.put("Name", "Up South");
//		  accounts.put("AT Territory","30801:AMDAVAD RURAl");
		  accounts.put("Location", "Pimpri");
		  accounts.put("ListOfBusiness Address", listOfBusinessAddress);
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
		            
		  String jsonPayload = siebelMassages.toString();
		  System.out.println("JsonPayload is ===" +jsonPayload);
		  
		  //String apiUrl = env.getProperty("SiebelCreateOutletApi");
		  
		  URL obj = new URL(url);
		  System.out.println("UrlOBj is---" + obj);

          javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
              new javax.net.ssl.HostnameVerifier(){
                  public boolean verify(String hostname,
                          javax.net.ssl.SSLSession sslSession) {
                      return true;
                  }
              });

          TrustManager[] trustAllCertificates = new TrustManager[]{
          	    new X509TrustManager() {
          	        public X509Certificate[] getAcceptedIssuers() {
          	            return null;
          	        }
          	        public void checkClientTrusted(X509Certificate[] certs, String authType) {
          	        }
          	        public void checkServerTrusted(X509Certificate[] certs, String authType) {
          	        }
          	    }
          	};

          SSLContext sslContext = SSLContext.getInstance("SSL");
        	sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
        	HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
         con.setRequestMethod(method);
         con.setRequestProperty("Content-Type", "application/json");
         con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpMb25hcjEyMw==");

        // Enable output and set request body
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        int responseCode = con.getResponseCode();
        String msg = con.getResponseMessage(); 
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response Message : " + msg); 
        
        String responseBody = null;
//        if (responseCode >= 200 && responseCode < 300) {
//            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
//                StringBuilder response = new StringBuilder();
//                String line;
//
//                while ((line = in.readLine()) != null) {
//                    response.append(line);
//                }
//
//                responseBody = response.toString();
//                System.out.println("Response body: " + responseBody);
//            }
//        } else {
//            // Handle error responses if needed
//            System.out.println("Error response: " + responseCode + " - " + msg);
//        }
        
        StringBuilder response = new StringBuilder();
        BufferedReader reader;
        InputStream inputStream;
        LtMastOutlets outletDetails = new LtMastOutlets();
        if(responseCode >= 200 && responseCode < 300 ) {
        	inputStream = con.getInputStream();
        	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
          while ((line = reader.readLine()) != null) {
        	  System.out.println("line success response is="+line);
              response.append(line);
              responseBody = response.toString();
              System.out.println("success response is = " + responseBody);
          }
          //reader.close();
  
//           Show the response
          System.out.println("Response Body: " + response.toString());

          // Parse JSON response
          JSONObject jsonObject = new JSONObject(responseBody);

          
          // Navigate through the structure to get the Account Id
          JSONArray accountArray = jsonObject.getJSONObject("SiebelMessage")
                  .getJSONObject("ListOfOutlet Interface")
                  .getJSONArray("Account");

          // Assuming there is only one account in the array
          String accountId = accountArray.getJSONObject(0).getString("Account Id");
          //}
          // Print the result //ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);

          System.out.println("Account Id: " + accountId);
             
  		  String outletCode = accountId;   //once you got response add outlet code and pass it here.
  	      outletDetails =ltMastOutletDao.getOutletByOutletCode(outletCode);
  	
  	 // saving siebel response & status code in to table
  	    String resCode = Integer.toString(responseCode);
        String res = responseBody.toString();
        System.out.println("Final response = "+resCode+res);
        ltMastOutletsDump.setSiebelStatus(resCode);
        ltMastOutletsDump.setSiebelRemark(res);
        
        
        ltMastOutletsDump.setStatus(ltMastOutletsDumps.getStatus());
		ltMastOutletsDump.setLastUpdatedBy(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateLogin(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateDate(new Date());
		
		//ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
  		  ltMastOutletsDump.setOutletCode(outletCode);
  		  ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);

  		LtMastUsers ltMastUsers = new LtMastUsers();
		  if(outletDetails !=null) {	
		  ltMastUsers.setOrgId(outletDetails.getOrgId());
		  ltMastUsers.setDistributorId(outletDetails.getDistributorId());
		  ltMastUsers.setOutletId(outletDetails.getOutletId());
		  ltMastUsers.setMobileNumber(ltMastOutletsDump.getPrimaryMobile());
		  ltMastUsers.setUserType("RETAILER");
		  ltMastUsers.setIsFirstLogin("Y");
		  ltMastUsers.setUserName(outletDetails.getOutletName());
		  ltMastUsers.setStatus("ACTIVE");
		  ltMastUsers.setCreatedBy(ltMastOutletsDump.getCreatedBy());
		  ltMastUsers.setCreationDate(new Date());
		  ltMastUsers.setLastUpdateDate(new Date());
		  ltMastUsers.setLastUpdatedBy(ltMastOutletsDump.getLastUpdatedBy());
		  ltMastUsers.setLastUpdateLogin(ltMastOutletsDump.getLastUpdateLogin());
		 // ltMastUsers.setEmployeeCode(outletDetails.getPosition());
		  //ltMastUsers.setPositionId(outletDetails.getPositionsId());
		  ltMastUsers.setAddress(outletDetails.getAddress1() + " "+outletDetails.getAddress2());
		  ltMastUsers.setAddressDetails(outletDetails.getAddress1() + " "+outletDetails.getAddress2()
		  +" "+outletDetails.getLandmark()+ " "+outletDetails.getArea() +" "+outletDetails.getCity()+" "+outletDetails.getState()
		  +" "+outletDetails.getCountry() +" "+outletDetails.getPin_code());
		  
		  ltMastUsers = ltMastUsersRepository.save(ltMastUsers);
		  if(outletDetails !=null) {
			  status.setCode(INSERT_SUCCESSFULLY); 
			  status.setData(outletDetails);
			  status.setMessage("INSERT_SUCCESSFULLY");
			  }
          
        }}else {
        	     reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                 String line;
                   while ((line = reader.readLine()) != null) 
                 {
      	            System.out.println("line error response is="+line);
                    response.append(line);
                    responseBody = response.toString();
                    System.out.println("Error response is = " + responseBody);
                 }        
        	     inputStream = con.getErrorStream();
        	     System.out.println("Error response: " + responseCode + " - " + msg);
        	     System.out.println("Error Response Body: " + responseBody);
        	 
        	  // saving siebel response & status code in to table   
        	        String resCode = Integer.toString(responseCode);
        	        String res = responseBody.toString();
        	        System.out.println("Final response = "+resCode+res);
        	        ltMastOutletsDump.setSiebelStatus(resCode);
        	        ltMastOutletsDump.setSiebelRemark(res);
        	        
        	  	    ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
        	  	  status.setCode(INSERT_FAIL); 
        		  status.setMessage("INSERT_FAIL");
        }
                
//		  if(outletDetails !=null) {
//		  status.setCode(INSERT_SUCCESSFULLY); 
//		  status.setData(outletDetails);
//		  status.setMessage("INSERT_SUCCESSFULLY");
//		  } 
//        else {
//		  status.setCode(INSERT_FAIL); 
//		  status.setMessage("INSERT_FAIL"); 
//		  }
}else{
	LtMastOutletsDump ltMastOutletsDump = ltMastOutletDao.getOutletToChangeStatus(ltMastOutletsDumps.getDistributorId(),
			ltMastOutletsDumps.getOrgId(),ltMastOutletsDumps.getPrimaryMobile());
	System.out.println("ltMastOutletsDump in else ---"+ltMastOutletsDump);
	ltMastOutletsDump.setStatus(ltMastOutletsDumps.getStatus());
	ltMastOutletsDump.setLastUpdatedBy(ltMastOutletsDumps.getUserId());
	ltMastOutletsDump.setLastUpdateLogin(ltMastOutletsDumps.getUserId());
	ltMastOutletsDump.setLastUpdateDate(new Date());
	
	ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
	if(ltMastOutletsDump !=null) {
		 status.setCode(INSERT_SUCCESSFULLY); 
		  status.setData(ltMastOutletsDump);
		  status.setMessage("INSERT_SUCCESSFULLY");
	}
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

	@Override
	public Status getBeatDetailsAgainsDistirbutorCode(BeatDetailsDto beatDetailsDto) throws ServiceException, IOException {
		try {   
		Status status = new Status();
		//BeatDetailsDto beatDetailsDto1 = new BeatDetailsDto();
		List<OutletSequenceData> outletSequenceData= new ArrayList<OutletSequenceData>();
		  //BeatDetailsDto headerlist = ltMastOutletDao.getBeatDetailsAgainsDistirbutorCodeAndBeatName(beatDetailsDto);
		  outletSequenceData = ltMastOutletDao.getBeatDetailsAgainsDistirbutorCode(beatDetailsDto);
	    	if(outletSequenceData!= null) {
	    		status.setCode(RECORD_FOUND);
	    		status.setMessage("Record Found Successfully");
	    		status.setData(outletSequenceData);
		    	  }else {
		    	status.setCode(FAIL);
		    	status.setMessage("RECORD NOT FOUND");
		    }
		    return status;
	      }
	 catch(Exception e) {
		e.printStackTrace();
		return null;
	   }
		
	}

	@Override
	public Status updateBeatSequence(BeatDetailsDto beatDetailsDto) throws ServiceException, IOException {
		try {
			  Status status = new Status();	
		 // System.out.print("Req data Isss === "+beatDetailsDto);
               List<OutletSequenceData>	outletSequenceData = beatDetailsDto.getOutletSequenceData();
              // System.out.println("beatDetailsDto.getDistributorCode()"+beatDetailsDto.getDistributorCode());
               String distCode =  beatDetailsDto.getDistributorCode();
             //  System.out.println("distCode"+distCode);
              int a;
               for(int i=0; i<outletSequenceData.size(); i++) {
            	  int outletSeq = outletSequenceData.get(i).getOutletSequence();
            	  String beatName = outletSequenceData.get(i).getBeatName();
            	   String outletCode = outletSequenceData.get(i).getOutletCode();
            	    ltMastOutletDao.updateBeatSequence(outletSeq,distCode,beatName,outletCode);
            	   
               }
               
             
         	 // System.out.print("beat data is =" +outletSeq+ ","+distCode+"," +beatName+"," +outletCode);
               
                         //System.out.print("Updated data is =" +beatDetailsDto);
            	         status.setCode(SUCCESS);
  			             status.setMessage("Record Update Successfully");
  			             status.setData(beatDetailsDto);
  			           return status;
              }	catch(Exception e) {
			      e.printStackTrace(); 
			      return null;
			}		
	}

	
	@Override
	public Status getOutletagainstBeat(BeatDetailsDto beatDetailsDto) throws ServiceException, IOException {
		try {
			System.out.println("In service");
		Status status = new Status();
		List<BeatDetailsDto> beatDetailsDto1 = ltMastOutletDao.getOutletagainstBeat(beatDetailsDto) ;
		if(beatDetailsDto1 != null) {
			status.setCode(RECORD_FOUND);
			status.setMessage("Reoced Found Sucessfully");
			status.setData(beatDetailsDto1);
		}else {
			status.setCode(FAIL);
	    	status.setMessage("RECORD NOT FOUND");
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public Status getOutletAgainstBeat(BeatDetailsDto beatDetailsDto) throws ServerException{
		Status status = new Status();
		try {
			List<BeatDetailsDto> List  = ltMastOutletDao.getOutletAgainstBeat(beatDetailsDto);
			if(List != null) {
				status.setCode(SUCCESS);
				status.setMessage("RECORD FOUND SUCCESSFULLY");
				status.setData(List);
			}else {
				status.setCode(FAIL);
				status.setMessage("RECORD NOT FOUND");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
}
