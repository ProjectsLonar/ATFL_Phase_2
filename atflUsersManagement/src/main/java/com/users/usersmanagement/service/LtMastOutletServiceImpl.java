package com.users.usersmanagement.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.dao.AtflMastUsersDao;
import com.users.usersmanagement.dao.LtMastOutletDao;
import com.users.usersmanagement.model.Account;
import com.users.usersmanagement.model.BusinessAddress;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.ListOfBusinessAddress;
import com.users.usersmanagement.model.ListOfOutletInterface;
import com.users.usersmanagement.model.ListOfRelatedOrganization;
import com.users.usersmanagement.model.LtMastOrganisations;
//import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutletsChannel;
import com.users.usersmanagement.model.LtMastOutletsType;
import com.users.usersmanagement.model.LtMastPricelist;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.RelatedOrganization;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.SiebelMessage;
import com.users.usersmanagement.model.SiebelMessageRequest;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.repository.LtMastOutletRepository;



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
	private Environment env;
	
	@Override
	public Status verifyOutlet(String outletCode, String distributorCrmCode, String userId)
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
		List<LtMastOutlets> list = ltMastOutletDao.getOutlet(requestDto);
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
	public Status getAllUserDataByRecentId(String userId) throws ServiceException {

		Status status = new Status();

		LtMastUsers ltMastUser = ltMastUsersDao.getUserById(userId);

		if (ltMastUser != null) {

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.RETAILER)) {

				LtMastUsers LtMastUser = ltMastOutletDao.getMastDataByOutletId(ltMastUser.getOutletId());

				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);

				status.setData(LtMastUser);

				return status;

			} else if (ltMastUser.getRecentSerachId() != null) {

				LtMastUsers LtMastUser = ltMastOutletDao.getMastDataByOutletId(ltMastUser.getRecentSerachId().toString());

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
	LtMastOrganisations ltMastOrganisations = ltMastOutletDao.getOrganisationDetailsById(ltMastOutlets.getOrgId());
	System.out.println("ltMastOrganisations"+ltMastOrganisations);
	RelatedOrganization relatedOrganizationDetails =  new RelatedOrganization();
	relatedOrganizationDetails.setIsPrimaryMVG("Y");
	relatedOrganizationDetails.setOrganization(ltMastOrganisations.getOrganisationName());
	
	ListOfRelatedOrganization listOfRelatedOrganization = new ListOfRelatedOrganization();
	listOfRelatedOrganization.setRelatedOrganization(relatedOrganizationDetails);
	
	BusinessAddress businessAddress = new BusinessAddress();
	businessAddress.setAddressId("1");
	businessAddress.setStreetAddress(ltMastOutlets.getAddress1());
	businessAddress.setStreetAddress2(ltMastOutlets.getAddress2());
	businessAddress.setCounty("");
	businessAddress.setCounty("INDIA");
	businessAddress.setCity(ltMastOutlets.getCity());
	businessAddress.setState(ltMastOutlets.getState());
	businessAddress.setPostalCode(ltMastOutlets.getPin_code());
	businessAddress.setProvince("");
	businessAddress.setIsPrimaryMVG("Y");
	
	ListOfBusinessAddress listOfBusinessAddress = new ListOfBusinessAddress();
	listOfBusinessAddress.setListOfBusinessAddress(businessAddress);
	
	Account account = new Account();
	
	account.setAccountStatus(ltMastOutlets.getStatus());
	account.setType(ltMastOutlets.getOutletType());
	account.setAccountId("1");
	account.setRuleAttribute2("add outlet channel");
	account.setName(ltMastOutlets.getOutletName());
	account.setaTTerritory(ltMastOutlets.getTerritory());
	account.setLocation("need to add");
	account.setListOfBusinessAddress(listOfBusinessAddress);
	account.setListOfRelatedOrganization(listOfRelatedOrganization);
	
	ListOfOutletInterface listOfOutletInterface = new ListOfOutletInterface();
	listOfOutletInterface.setAccount(account);
	
	SiebelMessage siebelMessage = new SiebelMessage();
	siebelMessage.setIntObjectFormat("Siebel Hierarchical");
	siebelMessage.setIntObjectName("Outlet Interface");
	siebelMessage.setMessageId("");
	siebelMessage.setMessageType("Integration Object");
	siebelMessage.setListOfOutletInterface(listOfOutletInterface);
	
	SiebelMessageRequest SiebelMessageRequest = new SiebelMessageRequest();
	SiebelMessageRequest.setSiebelMessage(siebelMessage);
	
	String apiUrl = env.getProperty("SiebelCreateOutletApi");
	
	URL url = new URL(apiUrl);
	
	 String username = "Lonar_Test";
     String password = "Lonar123";
     String credentials = username + ":" + password;
	
	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	connection.setRequestMethod("POST");
	 connection.setDoOutput(true);
	 connection.setRequestProperty("Content-Type", "application/json");
     connection.setRequestProperty("Authorization", "Basic_Auth"+credentials);
     
     
     ObjectMapper objectMapper = new ObjectMapper();
     String jsonPayload = objectMapper.writeValueAsString(SiebelMessageRequest);
    
     System.out.println("jsonPayload"+jsonPayload);
     try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
         wr.writeBytes(jsonPayload);
         wr.flush();
     }

     // Get the HTTP response code
     int responseCode = connection.getResponseCode();
     System.out.println("Response Code: " + responseCode);

     // Read the response
     BufferedReader reader;
     if (responseCode == HttpURLConnection.HTTP_OK) {
         reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
     } else {
         reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
     }

     // Read the response
     String line;
     StringBuilder response = new StringBuilder();

     while ((line = reader.readLine()) != null) {
         response.append(line);
     }
     reader.close();

     // Print the response
     System.out.println("Response: " + response.toString());

     // Close the connection
     connection.disconnect();
     String outletCode = null;
     LtMastOutlets outletDetails = ltMastOutletDao.getOutletByOutletCode(outletCode);
     if(outletDetails !=null) {
    	 status.setCode(INSERT_SUCCESSFULLY);
    	 status.setData(outletDetails);
    	 status.setMessage("INSERT_SUCCESSFULLY");
     }
     else {
    	 status.setCode(INSERT_FAIL);
    	 status.setMessage("INSERT_FAILY");
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
	public Status getPriceListAgainstDistributor(String outletId )throws ServiceException, IOException {
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
}
