package com.users.usersmanagement.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import com.users.usersmanagement.model.LtOutletDto;
import com.users.usersmanagement.model.NotificationDetails;
import com.users.usersmanagement.model.OutletSequenceData;
import com.users.usersmanagement.model.RequestDto;
import com.users.usersmanagement.model.RoleMaster;
import com.users.usersmanagement.model.Status;
import com.users.usersmanagement.model.LtMastStates;
import com.users.usersmanagement.repository.LtMastOutletDumpRepository;
import com.users.usersmanagement.repository.LtMastOutletRepository;
import com.users.usersmanagement.repository.LtMastUsersRepository;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


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
		
		List<LtOutletDto> list = new ArrayList<>();
		
		if(requestDto.getUserId()!= null) {
		LtMastUsers ltMastUsers = ltMastOutletDao.getUserFromUserId(requestDto.getUserId());	
		System.out.println("ltMastUsers is ="+ltMastUsers);
		if(ltMastUsers.getUserType().equalsIgnoreCase("AREAHEAD")) {
		List<String> distId=ltMastOutletDao.getDistributorIdFromAreaHead(ltMastUsers.getEmployeeCode());
		System.out.println("distIdList123"+distId);

			list = ltMastOutletDao.getOutletForAreaHead(requestDto,distId);
		}else if(ltMastUsers.getUserType().equalsIgnoreCase("SALESOFFICER")) {
			String emp= "GOPAGANI_ANIL";
			List<String> distId=ltMastOutletDao.getDistributorIdFromSalesOfficer(emp);
			System.out.println("distIdList123"+distId);
                  if(distId==null) {
                	 List<String> distId1=ltMastOutletDao.getDistributorIdFromAreaHead(emp);
                	  list = ltMastOutletDao.getOutletForAreaHead(requestDto,distId1);
                  }else {
				   list = ltMastOutletDao.getOutletForAreaHead(requestDto,distId);
				}
			}
		else {
		      list = ltMastOutletDao.getOutlet(requestDto);
		}
		}
		else {
		      list = ltMastOutletDao.getOutlet(requestDto);
		}
		
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
	public Status getAllUserDataByRecentId(Long userId) throws ServiceException {
		System.out.println("Enter in method getSelectedOutlet at "+LocalDateTime.now());
		long methodIn = System.currentTimeMillis();
		Map<String,String> timeDifference = new HashMap<>();
		Status status = new Status();

		LtMastUsers ltMastUser = ltMastUsersDao.getUserById(userId);

		if (ltMastUser != null) {

			if (ltMastUser.getUserType().equalsIgnoreCase(RoleMaster.RETAILER)) {

				LtMastUsers LtMastUser = ltMastOutletDao.getMastDataByOutletId(ltMastUser.getOutletId());
				status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				status.setData(LtMastUser);
				long methodOut = System.currentTimeMillis();
				timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
		        status.setTimeDifference(timeDifference);
				return status;
			} else if (ltMastUser.getRecentSerachId() != null) {
				LtMastUsers LtMastUser = ltMastOutletDao.getMastDataByOutletId(ltMastUser.getRecentSerachId().toString());
				if (LtMastUser != null) {
					status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
					status.setData(LtMastUser);
					long methodOut = System.currentTimeMillis();
					timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
			        status.setTimeDifference(timeDifference);			       
					return status;
				}
			}
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		long methodOut = System.currentTimeMillis();
		System.out.println("Exit from method getSelectedOutlet at "+LocalDateTime.now());
        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
        status.setTimeDifference(timeDifference);
		
		return status;
	}

	
	private void sendEmail(LtMastOutletsDump ltMastOutletsDumpupdated) throws ServiceException, IOException {
		
//		List<LtMastUsers> ltMastAllUsers = ltMastOutletDao.getAllSalesOfficerAgainstDist(ltMastOutletsDumpupdated.getDistributorId(),
//				ltMastOutletsDumpupdated.getOrgId());this is old logic comment on 26-Sep-2024 bcz get new query from venkat
		List<LtMastUsers> ltMastAllUsers = ltMastOutletDao.getAllAreaHeadAgainstDist(ltMastOutletsDumpupdated.getDistributorId());
		
		if(!ltMastAllUsers.isEmpty()) 
		      {			
			 System.out.println("Hi I'm in send email outlet");
			 System.out.println("ltMastAllUsersList is "+ ltMastAllUsers);
					for(Iterator iterator = ltMastAllUsers.iterator(); iterator.hasNext();) 
					{
						LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
						System.out.println(ltMastUsers.getToken());
						
						String subject = "OUTLET_APPROVAL";
					    String userName = ltMastOutletDao.getUserNameAgainsUserId(ltMastOutletsDumpupdated.getCreatedBy());
					    System.out.println("userName for Email = "+userName+"...Ok");
					      
					      String salespersonName=userName;
					      
					      String outletName = ltMastOutletsDumpupdated.getOutletName();
					      System.out.println("outletName is"+ outletName);
						  String text = "";
					      
					      if(ltMastUsers.getEmail()!= null) {
					      String to= ltMastUsers.getEmail();
					      System.out.println("Above sendSimpleMessage");
					      try {
							sendSimpleMessageWithAuth(to,subject,text,outletName,salespersonName);
						} catch (Exception e) {
							e.printStackTrace();
						}
			          }
					}
		}
		
	}
	
	public void sendSimpleMessageWithAuth(String to, String subject, String text,String outletName,String salespersonName) throws MessagingException {
//      MimeMessage message = emailSender.createMimeMessage();
//      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      Map<String,String> map = new HashMap<String,String>();
      System.out.println("in sendSimpleMessageWithAuth");
      String host = "14.140.146.196";    // "smtp.gmail.com";         
      String port = "25";//  "587";         
      String mailFrom = "noreply@atfoods.com";  //"atfl4867@gmail.com";         
      String password = "Welcome12";    //"vjug xicp wiuw fakw";         
      // Outgoing email information
      String mailTo = to;        
      String subject1 = "Sub:- Outlet Approval";   
//      text = "<html><head></head><body><h1>Greetings!</h1><p>We hope this message finds you well.</p></body></html>";
//      String salesOrder ="myOrder123";
//      String salespersonName = "Vaibhav";
//      String totalAmount= "100";
      text = "<html> <head> </head> <body>"
              + "<div style=\"margin-top:0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\">"
              + "<div style=\"font-size:14px;margin:0px auto;max-width:620px;margin-bottom:20px;background:#c02e2e05;\">"
              + "<div style=\"padding: 0px 0px 9px 0px;width:100%;color: #fff;font-weight:bold;font-size:15px;line-height: 20px; width:562px; margin:0 auto;\">"
              + "<center><table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td valign=\"top\" style=\"padding:48px 48px 32px\">"
              + "<div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">"
              + "<p style=\"margin:0 0 16px\">Dear Sir/Ma’am,</p>"
              + "<p style=\"margin:0 0 16px\">An outlet has come for your approval. Please go to the app to approve/reject the outlet.</p>"
              + "<h4 style=\"font-weight:bold\">Outlet Name:"+ outletName +"</h4>"
              + "<h4 style=\"font-weight:bold\">Created by:"+ salespersonName +"</h4>"
              + "<h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>"
              + "<p>Regards,</p><p>Lakshya App team.</p>"
              + "<div style= \"margin-bottom:40px\"></div></div></td></tr></tbody></table></center></div></div></div>"
              + "</body> </html>";
      
      String message = text;
      String result = String.join("   ", host,mailFrom,password,mailTo,subject1,message);
      System.out.println("Port = "+port);
  	System.out.println("result in method = "+result);
      try {

          System.out.println("Port = "+port);
      	System.out.println("result in try = "+result);
      	   sendEmail(host, port, mailFrom, password, mailTo, subject1, message);
//          map.put("status", "true");
//          map.put("message", "Email sent successfully to: "+ to);
      } catch (Exception ex) {
          System.out.println("Port = "+port);
      	System.out.println("result in catch = "+result);
          System.out.println("Failed to send email to: " + to);
          map.put("status", "false");
          map.put("message", "Failed to send email to: "+to);
          ex.printStackTrace(); // Log the stack trace for debugging
      }
      
  }
  
  
	private void sendEmail(String host, String port, String mailFrom, String password, String mailTo, String subject1,
			String message)throws AddressException, MessagingException {
		   	// Set up mail server properties
		   	System.out.println("in sendEmail");
		   	Map<String,String> map = new HashMap<String,String>();
		   	Properties properties = new Properties();         
		   	properties.put("mail.smtp.host", host);   
		   	System.out.println("Above Port in sendEmail = "+port);
		   	properties.put("mail.smtp.socketFactory.port", port);
		   	System.out.println("Below Port in sendEmail = "+port);
		   	properties.put("mail.smtp.auth", "true");         
		   	properties.put("mail.smtp.starttls.enable", "true"); 
		   	
		   	properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		   	properties.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384");
		   	properties.put("mail.smtp.ssl.enable", "false");
		   	properties.put("mail.transport.protocol", "smtp");
		   	properties.put("mail.smtp.sendpartial", "true");
		   	properties.put("mail.smtp.ssl.trust", host);
		   	
		   	properties.put("mail.debug", "true");
		   	System.out.println("below properties");
		   	// Create a mail session
		   	Authenticator auth = new Authenticator() { 
		   		public PasswordAuthentication getPasswordAuthentication() { 
		   			return new PasswordAuthentication(mailFrom, password);           
		   			}       
		   		};
		       	System.out.println("below Authenticator");
		   		Session session = Session.getInstance(properties, auth); 
		       	System.out.println("below session");
		   		// Create the email message
		   		Message message1 = new MimeMessage(session); 
		   		message1.setFrom(new InternetAddress(mailFrom)); 
		   		message1.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo)); 
		   		message1.setSubject(subject1); 
//		   		message.setText(messageBody);
		           message1.setContent(message, "text/html; charset=utf-8");
		   		// Send the email 
		       	System.out.println("Above Transport");
		       	try {
		       		Transport.send(message1);
		       		System.out.println("Success...");
		       		map.put("status", "101");
		       		map.put("message", "Email sent successfully to: "+ mailTo);
		       	}catch(Exception ex) {
		       		System.out.println("Failed...");
		       		map.put("status", "102");
		               map.put("message", "Failed to send email to: "+ mailTo);
		       		ex.printStackTrace();
		       	}
		       	System.out.println("Below Transport");
		       	
		   }
		

	@Override
	public Status createOutlet(LtMastOutletsDump ltMastOutlets) throws ServiceException, IOException {
		Status status = new Status();
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
		LtMastOutletsDump ltMastOutletsDump = new LtMastOutletsDump();
		
		LtMastUsers ltMastUser = ltMastOutletDao.getUserFromUserId(ltMastOutlets.getUserId());
		
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
			ltMastOutletsDump.setBeatId(ltMastOutlets.getBeatId());

			if (ltMastOutlets.getRegion() != null) {
				ltMastOutletsDump.setRegion(ltMastOutlets.getRegion());
			}
			ltMastOutletsDump.setArea(ltMastOutlets.getArea());
			//ltMastOutletsDump.setTerritory(ltMastOutlets.getTerritory());
			ltMastOutletsDump.setTerritory(ltMastUser.getTerritory());
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
			
			System.out.println("ltMastUsers user = "+ltMastOutletsDumpupdated);
//			LtMastUsers ltMastUsersSysAdmin = ltMastOutletDao.getSystemAdministartorDetails(ltMastOutletsDumpupdated.getOrgId()); this is for single sysadmin
			List<LtMastUsers> ltMastUsersSysAdmin = ltMastOutletDao.getSystemAdministartorsDetails(ltMastOutletsDumpupdated.getOrgId());
			
			System.out.println("ltMastUsersSysAdmin user = "+ltMastUsersSysAdmin);
			if(ltMastUsersSysAdmin!=null) {
				System.out.println("Hi im in sysadmin notification"); 
			
				for(LtMastUsers user:ltMastUsersSysAdmin) {
					System.out.println("user"+user);
					if(user !=null) {
					  CompletableFuture.runAsync(() -> {
			                try {
			                	webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
			    				sendEmail(ltMastOutletsDumpupdated);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }, executor);
						
				}}
				
				//webController.sendOutletApprovalNotification(ltMastUsersSysAdmin, ltMastOutletsDumpupdated); for single sysAdmin
			}
			
			if (ltMastOutletsDumpupdated != null) {
				// send notification to mapped system administrator and sales officer
				
///				List<LtMastUsers> ltMastUsers = ltMastOutletDao.getAllSalesOfficerAgainstDist(ltMastOutletsDumpupdated.getDistributorId(),
//						ltMastOutletsDumpupdated.getOrgId()); this is old logic comment on 26-Sep-2024 bcz get new query from venkat
				List<LtMastUsers> ltMastUsersAreaHead = ltMastOutletDao.getAllAreaHeadAgainstDist(ltMastOutletsDumpupdated.getDistributorId());
				System.out.println("ltMastUsers user = "+ltMastUsersAreaHead);
				if(ltMastUsersAreaHead !=null) {
					System.out.println("Hi im in areahead notification"); 
				for(LtMastUsers user:ltMastUsersAreaHead) {
					System.out.println("user"+user);
					if(user !=null) {
				//webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated); this is original code
				//sendEmail(ltMastOutletsDumpupdated);                                          this is original code    
						CompletableFuture.runAsync(() -> {
			                try {
			                	webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
			    				sendEmail(ltMastOutletsDumpupdated);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }, executor);
						
				}}
				} 
				
				List<LtMastUsers> ltMastUsersSalesOfficers = ltMastOutletDao.getAllSalesOfficersAgainstDist(ltMastOutletsDumpupdated.getDistributorId());
				System.out.println("ltMastUsers user = "+ltMastUsersSalesOfficers);
				if(ltMastUsersSalesOfficers !=null) {
					System.out.println("Hi im in salesOfficers notification"); 
				for(LtMastUsers user:ltMastUsersSalesOfficers) {
					System.out.println("user"+user);
					if(user !=null) {
				//webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated); this is original code
				//sendEmail(ltMastOutletsDumpupdated);                                          this is original code    
						CompletableFuture.runAsync(() -> {
			                try {
			                	webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
			    				sendEmail(ltMastOutletsDumpupdated);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }, executor);
						
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
LtMastOutletsDump ltMastOutletsDump1 = new LtMastOutletsDump();
			try{ltMastOutletsDump = ltMastOutletDao.getoutletByIdAndCode(ltMastOutlets.getOutletCode());
			
			if(ltMastOutletsDump == null)
			{
				
				ltMastOutletsDump1.setDistributorId(ltMastOutlets.getDistributorId());
				ltMastOutletsDump1.setOutletType(ltMastOutlets.getOutletType());
				ltMastOutletsDump1.setOutletName(ltMastOutlets.getOutletName());
				
				if (ltMastOutlets.getDistributorName() != null) {
					ltMastOutletsDump1.setDistributorName(ltMastOutlets.getDistributorName());
				}
				
				  if (ltMastOutlets.getPositionsId() != null) {
				  ltMastOutletsDump1.setPositionsId(ltMastOutlets.getPositionsId()); 
				  }
				 
				if (ltMastOutlets.getProprietorName() != null) {
					ltMastOutletsDump1.setProprietorName(ltMastOutlets.getProprietorName());
				}
				ltMastOutletsDump1.setOutletChannel(ltMastOutlets.getOutletChannel());
				ltMastOutletsDump1.setAddress1(ltMastOutlets.getAddress1());
				ltMastOutletsDump1.setAddress2(ltMastOutlets.getAddress2());
				ltMastOutletsDump1.setLandmark(ltMastOutlets.getLandmark());
				ltMastOutletsDump1.setCountry("INDIA");
				ltMastOutletsDump1.setState(ltMastOutlets.getState());
				ltMastOutletsDump1.setCity(ltMastOutlets.getCity());
				ltMastOutletsDump1.setPin_code(ltMastOutlets.getPin_code());
				ltMastOutletsDump1.setBeatId(ltMastOutlets.getBeatId());
				
				if (ltMastOutlets.getRegion() != null) {
					ltMastOutletsDump1.setRegion(ltMastOutlets.getRegion());
				}
				ltMastOutletsDump1.setArea(ltMastOutlets.getArea());
				//ltMastOutletsDump1.setTerritory(ltMastOutlets.getTerritory());
				ltMastOutletsDump.setTerritory(ltMastUser.getTerritory());
				ltMastOutletsDump1.setPrimaryMobile(ltMastOutlets.getPrimaryMobile());
				ltMastOutletsDump1.setStatus("PENDING_APPROVAL");
				ltMastOutletsDump1.setPriceList(ltMastOutlets.getPriceList());
				ltMastOutletsDump1.setOrgId(ltMastOutlets.getOrgId());

				ltMastOutletsDump1.setCreatedBy(ltMastOutlets.getUserId());
				ltMastOutletsDump1.setLastUpdatedBy(ltMastOutlets.getUserId());
				ltMastOutletsDump1.setLastUpdateLogin(ltMastOutlets.getUserId());
				ltMastOutletsDump1.setCreationDate(new Date());
				ltMastOutletsDump1.setLastUpdateDate(new Date());
				
			
			}else {
				ltMastOutletsDump1.setDistributorId(ltMastOutlets.getDistributorId());
				ltMastOutletsDump1.setOutletType(ltMastOutlets.getOutletType());
				ltMastOutletsDump1.setOutletName(ltMastOutlets.getOutletName());
				ltMastOutletsDump1.setPrimaryMobile(ltMastOutlets.getPrimaryMobile());
				
				ltMastOutletsDump1.setProprietorName(ltMastOutlets.getProprietorName());
				ltMastOutletsDump1.setOutletChannel(ltMastOutlets.getOutletChannel());
				ltMastOutletsDump1.setAddress1(ltMastOutlets.getAddress1());
				ltMastOutletsDump1.setAddress2(ltMastOutlets.getAddress2());
				ltMastOutletsDump1.setState(ltMastOutlets.getState());
				ltMastOutletsDump1.setCity(ltMastOutlets.getCity());
				ltMastOutletsDump1.setPin_code(ltMastOutlets.getPin_code());
				ltMastOutletsDump1.setBeatId(ltMastOutlets.getBeatId());
				ltMastOutletsDump1.setPriceList(ltMastOutlets.getPriceList());
				
				ltMastOutletsDump1.setOutletCode(ltMastOutlets.getOutletCode());
				//ltMastOutletsDump1.setStatus("APPROVED");
				//ltMastOutletsDump1.set
				ltMastOutletsDump1.setStatus("APPROVED");
				
				ltMastOutletsDump1.setOrgId(ltMastOutlets.getOrgId());
				ltMastOutletsDump1.setLastUpdatedBy(ltMastOutlets.getUserId());
				ltMastOutletsDump1.setLastUpdateLogin(ltMastOutlets.getUserId());
				ltMastOutletsDump1.setLastUpdateDate(new Date());
				ltMastOutletsDump1.setOutletId(ltMastOutletsDump.getOutletId());
			}}catch (Exception e) {
			    // Log the exception or handle it accordingly
			    e.printStackTrace();
			}

			try {
			LtMastOutletsDump ltMastOutletsDumpupdated = ltMastOutletDumpRepository.save(ltMastOutletsDump1);
			
//			LtMastUsers ltMastUsersSysAdmin = ltMastOutletDao.getSystemAdministartorDetails(ltMastOutletsDumpupdated.getOrgId());
//			if(ltMastUsersSysAdmin!=null) {
//			webController.sendOutletApprovalNotification(ltMastUsersSysAdmin, ltMastOutletsDumpupdated);
//			}
			
            List<LtMastUsers> ltMastUsersSysAdmin = ltMastOutletDao.getSystemAdministartorsDetails(ltMastOutletsDumpupdated.getOrgId());
			System.out.println("ltMastUsersSysAdmin user = "+ltMastUsersSysAdmin);
			if(ltMastUsersSysAdmin!=null) {
				System.out.println("Hi im in sysadmin notification"); 
				for(LtMastUsers user:ltMastUsersSysAdmin) {
					System.out.println("user"+user);
					if(user !=null) {
					  CompletableFuture.runAsync(() -> {
			                try {
			                	webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
			    				sendEmail(ltMastOutletsDumpupdated);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }, executor);
						
				}}
				
				//webController.sendOutletApprovalNotification(ltMastUsersSysAdmin, ltMastOutletsDumpupdated); for single sysAdmin
			}
			
			if (ltMastOutletsDumpupdated != null) {
				// send notification to mapped system administrator and sales officer
				
//				List<LtMastUsers> ltMastUsers = ltMastOutletDao.getAllSalesOfficerAgainstDist(ltMastOutletsDumpupdated.getDistributorId(),
//						ltMastOutletsDumpupdated.getOrgId());  this is old logic comment on 26-Sep-2024 bcz get new query from venkat
				List<LtMastUsers> ltMastUsers = ltMastOutletDao.getAllAreaHeadAgainstDist(ltMastOutletsDumpupdated.getDistributorId());
				if(ltMastUsers !=null) {
				for(LtMastUsers user:ltMastUsers) {
					System.out.println("user"+user);
					if(user !=null) {
				webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
				//sendEmail(ltMastOutletsDumpupdated);
				}}
				} 
				
				List<LtMastUsers> ltMastUsersSalesOfficers = ltMastOutletDao.getAllSalesOfficersAgainstDist(ltMastOutletsDumpupdated.getDistributorId());
				System.out.println("ltMastUsers user = "+ltMastUsersSalesOfficers);
				if(ltMastUsersSalesOfficers !=null) {
					System.out.println("Hi im in salesOfficers notification"); 
				for(LtMastUsers user:ltMastUsersSalesOfficers) {
					System.out.println("user"+user);
					if(user !=null) {
				//webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated); this is original code
				//sendEmail(ltMastOutletsDumpupdated);                                          this is original code    
						CompletableFuture.runAsync(() -> {
			                try {
			                	webController.sendOutletApprovalNotification(user, ltMastOutletsDumpupdated);
			    				sendEmail(ltMastOutletsDumpupdated);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }, executor);
						
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
		}	catch(Exception e) {
			//logger.error("Error Description :", e);
			e.printStackTrace();
			return null;
			}	
		finally {
			System.out.println("Connection closed in update");
	        executor.shutdown();
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
	public Status getPriceListAgainstDistributor(String distributorId, String priceist) throws ServiceException, IOException {
		Status status = new Status();
		System.out.println("In controller priceist"+priceist);
		List<LtMastPricelist> list = new ArrayList<>();
		try {
			if(priceist!= null) {
				list = ltMastOutletDao.getPriceListAgainstPriceListName(priceist);
			}else {
		        list = ltMastOutletDao.getPriceListAgainstDistributor(distributorId);
		    }
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
		try {
		List<LtMastOutletsDump> list = new ArrayList<>();
		
		LtMastUsers ltMastUsers = ltMastOutletDao.getUserFromUserId(requestDto.getUserId());	
		System.out.println("ltMastUsers is ="+ltMastUsers);
		if(ltMastUsers.getUserType().equalsIgnoreCase("AREAHEAD")) {
		List<String> distId=ltMastOutletDao.getDistributorIdFromAreaHead(ltMastUsers.getEmployeeCode());
		System.out.println("distIdList123"+distId);

			list = ltMastOutletDao.getPendingAprrovalOutletForAreaHead(requestDto,distId);
		}
		else {		
		list = ltMastOutletDao.getPendingAprrovalOutlet(requestDto);
		}
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
		} else {
			//status.setCode(FAIL);
			status.setCode(RECORD_NOT_FOUND);
			status.setMessage("RECORD NOT FOUND");
		}
		
	}catch(Exception e) {
		e.printStackTrace();
	}return status;
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
		//ltMastOutletsDump.setStatus(ltMastOutletsDumps.getStatus());
		ltMastOutletsDump.setLastUpdatedBy(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateLogin(ltMastOutletsDumps.getUserId());
		ltMastOutletsDump.setLastUpdateDate(new Date());
		
		ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
		System.out.println("Table ltMastOutletsDump = "+ ltMastOutletsDump);
		LtMastOrganisations ltMastOrganisations =
		  ltMastOutletDao.getOrganisationDetailsById(ltMastOutletsDump.getOrgId());
		  System.out.println("ltMastOrganisations"+ltMastOrganisations);
          
		  Long id=1L;
          Long storeId= ltMastOutletDao.getStoreIdFromBeat(ltMastOutletsDump.getBeatId());
             storeId= storeId+id;
             System.out.println("storeId Beat Sequence is --" + storeId);
		  
            String priceListId= ltMastOutletDao.getPriceListId(ltMastOutletsDump.getPriceList());
            
            String distName= ltMastOutletDao.getDistNameFromDistId(ltMastOutletsDumps.getDistributorId());
             
		  String url = "https://10.245.4.70:9014/siebel/v1.0/service/Siebel%20Outlet%20Integration/InsertOrUpdate?matchrequestformat=y";
         System.out.println("Url is---" + url);
		  String method = "POST";
         // String contentType = "Content-Type: application/json";
         // String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw==";

		  
		  JSONObject relatedOrganizationDetail = new JSONObject();
		  
		  relatedOrganizationDetail.put("IsPrimaryMVG","Y");
		  relatedOrganizationDetail.put("Organization",distName);//ltMastOutletsDumps.getDistributorName());//ltMastOrganisations.getOrganisationName());
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
//		  System.out.println("Input ltMastOutletsDump.getPin_code() = " + ltMastOutletsDump.getPin_code());
		  businessAddress.put("Postal Code", ltMastOutletsDump.getPin_code());
//		  businessAddress.put("Postal Code","411045");
		  businessAddress.put("Province", "");
		  businessAddress.put("IsPrimaryMVG", "Y");
		  		  		 
		  JSONObject listOfBusinessAddress = new JSONObject();
		  
		  listOfBusinessAddress.put("Business Address", businessAddress);  
//		  listOfBusinessAddres.put("ListOfRelated Organization", listOfRelatedOrganizations);
		  
		  JSONObject accounts = new JSONObject();
		  accounts.put("Account Status", "Active");
		  System.out.println("Get ltMastOutletsDump.getPriceList() = "+ltMastOutletsDump.getPriceList());
		  accounts.put("Price List", ltMastOutletsDump.getPriceList());
		  accounts.put("Price List Id", priceListId);
		  accounts.put("Main Phone Number",ltMastOutletsDump.getPrimaryMobile()); // this is added on 6-Nov-24 as per chandra's requirement.
		  accounts.put("Type", ltMastOutletsDump.getOutletType());
//		  accounts.put("Type", "Retailer");
		  accounts.put("Account Id", "1");
		  accounts.put("Rule Attribute 2", ltMastOutletsDump.getOutletChannel());
//		  accounts.put("Rule Attribute 2", "Whole Sellers");
		  accounts.put("Name", ltMastOutletsDump.getOutletName());
//		  accounts.put("Name", "Up South");
//		  accounts.put("AT Territory","30801:AMDAVAD RURAl");
		  accounts.put("AT Territory", ltMastOutletsDump.getTerritory());
		  accounts.put("Rule Attribute 1", ltMastOutletsDump.getBeatId());
		  accounts.put("Store Size", storeId);
//		  accounts.put("Location", "Pimpri");
		  accounts.put("Location", ltMastOutletsDump.getProprietorName());
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
        // con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpEMTBueXN1JA=="); // this is LONAR_TEST:D10nysu$
         //"Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="); this is LONAR_TEST:Lonar123//comment on 30-May-24 for auth 
                                                      
         String username;
         String mobileNo = ltMastOutletDao.getMobileNoFromOutletName(ltMastOutletsDumps.getOutletName());
	        if(mobileNo!= null) {
	         username = ltMastOutletDao.getUserNameFromSiebel(mobileNo); // comment on 15-Oct-2024 bcz of siebel auth error
	        	//username = "LONAR_TEST";
	         }else {
	         username = "LONAR_TEST";}   // "VINAY.KUMAR6";}
	        String password = "D10nysu$";
	        String auth = username + ":" + password;
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
	        String authHeaderValue = "Basic " + new String(encodedAuth);
	        System.out.println("This is user auth credentials = "+auth+mobileNo);
	        System.out.println("This is user authHeaderValue"+authHeaderValue);
	        con.setRequestProperty("Authorization", authHeaderValue);
// this is comment on 05-June-24 bcz auth is not working  
         
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
  	
  	      
  	    ConsumeApiService consumeApiService = new ConsumeApiService();
        consumeApiService.SiebelAPILog(url.toString(), jsonPayload, response.toString());
  	      
  	 // saving siebel response & status code in to table
  	    String resCode = Integer.toString(responseCode);
        String res = responseBody.toString();
        System.out.println("Final response = "+resCode+res);
        ltMastOutletsDump.setSiebelStatus(resCode);
        ltMastOutletsDump.setSiebelRemark(res);
        ltMastOutletsDump.setSiebelJsonpaylod(jsonPayload);
        
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
		            
        }
		  }else {
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
        	 
        	     ConsumeApiService consumeApiService = new ConsumeApiService();
                 consumeApiService.SiebelAPILog(url.toString(), jsonPayload, response.toString());
        	     
        	  // saving siebel response & status code in to table   
        	        String resCode = Integer.toString(responseCode);
        	        String res = responseBody.toString();
        	        System.out.println("Final response = "+resCode+res);
        	        ltMastOutletsDump.setSiebelStatus(resCode);
        	        ltMastOutletsDump.setSiebelRemark(res);
        	        ltMastOutletsDump.setSiebelJsonpaylod(jsonPayload);
        	        ltMastOutletsDump.setStatus("PENDING_APPROVAL");
        	        
        	  	    ltMastOutletsDump = ltMastOutletDumpRepository.save(ltMastOutletsDump);
        	  	  status.setCode(INSERT_FAIL); 
        		  status.setMessage("INSERT_FAIL");
        		  status.setData(ltMastOutletsDump.getSiebelRemark());
        		  
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
		System.out.println("Beat NAme is in controller "+beatDetailsDto.getBeatName());
		try {   
		Status status = new Status();
		//BeatDetailsDto beatDetailsDto1 = new BeatDetailsDto();
		List<OutletSequenceData> outletSequenceData= new ArrayList<OutletSequenceData>();
		  //BeatDetailsDto headerlist = ltMastOutletDao.getBeatDetailsAgainsDistirbutorCodeAndBeatName(beatDetailsDto);
		
//		LtMastUsers ltMastUsers = ltMastOutletDao.getUserFromUserId(beatDetailsDto.getUserId());	
//		System.out.println("ltMastUsers is ="+ltMastUsers);
//		if(ltMastUsers.getUserType().equalsIgnoreCase("AREAHEAD")) {
//		List<String> distId=ltMastOutletDao.getDistributorIdFromAreaHead(ltMastUsers.getEmployeeCode());
//		System.out.println("distIdList123"+distId);
//		outletSequenceData = ltMastOutletDao.getBeatDetailsAgainsDistirbutorCodeForAreaHead(beatDetailsDto, distId);
//		}
		if(beatDetailsDto.getBeatName()!= null) {
			//OutletSequenceData outletSequenceData1= new OutletSequenceData();
			//String beatDetails = ltMastOutletDao.getBeatDetailsAgainsBeatName(beatName);
			String beatName = beatDetailsDto.getBeatName();
			System.out.println("Beat NAme is beatName "+beatDetailsDto.getBeatName());
			OutletSequenceData outletSequenceData1= new OutletSequenceData();
			outletSequenceData1.setBeatName(beatName);
			
			outletSequenceData.add(outletSequenceData1);
			status.setCode(RECORD_FOUND);
    		status.setMessage("Record Found Successfully");
    		status.setData(outletSequenceData);
		}else {
		  outletSequenceData = ltMastOutletDao.getBeatDetailsAgainsDistirbutorCode(beatDetailsDto);
	    	if(outletSequenceData!= null) {
	    		status.setCode(RECORD_FOUND);
	    		status.setMessage("Record Found Successfully");
	    		status.setData(outletSequenceData);
		    	  }else {
		    	status.setCode(FAIL);
		    	status.setMessage("RECORD NOT FOUND");
		    }
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

	@Override
	public Status getAllStates() throws ServiceException, IOException {
		Status status = new Status();
		List<LtMastStates> list = ltMastOutletDao.getAllStates();
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
	public Status getOutletById(String outletId) throws ServiceException, IOException {
		
			Status status = new Status();
			try {
			List<LtMastOutletsDump> list = ltMastOutletDao.getOutletById(outletId);
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
	
	
}
