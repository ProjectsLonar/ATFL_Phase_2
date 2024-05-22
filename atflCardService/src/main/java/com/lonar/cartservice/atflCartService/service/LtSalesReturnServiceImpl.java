package com.lonar.cartservice.atflCartService.service;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lonar.cartservice.atflCartService.dao.LtSalesreturnDao;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsLineDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsResponseDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnHeaderDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnLineDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnResponseDto;
import com.lonar.cartservice.atflCartService.dto.SalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoLineDto;
import com.lonar.cartservice.atflCartService.dto.LtSoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderLineDto;

import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.repository.LtSalesRetrunLinesRepository;
import com.lonar.cartservice.atflCartService.repository.LtSalesReturnRepository;
import com.lonar.cartservice.atflCartService.controller.WebController;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.dto.SalesReturnApproval;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnHeaderDto;

@Service
@PropertySource(value = "classpath:queries/cartmasterqueries.properties", ignoreResourceNotFound = true)
public class LtSalesReturnServiceImpl implements LtSalesReturnService,CodeMaster {

	@Autowired
	LtSalesreturnDao  ltSalesreturnDao;
	
	@Autowired
	LtSalesReturnRepository ltSalesReturnRepository;
	
	@Autowired
	LtSalesRetrunLinesRepository ltSalesRetrunLinesRepository;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private WebController webController;
	
	@Override
	public Status saveSalesReturn(LtSalesReturnDto ltSalesReturnDto) throws ServerException{
		try {
		Status status = new Status();
		LtSalesReturnHeader ltSalesReturnHeader = new LtSalesReturnHeader();
		if(ltSalesReturnDto !=null) {
			if(ltSalesReturnDto.getSalesReturnHeaderId() !=null && ltSalesReturnDto.getSalesReturnNumber() !=null) {
				//code for update
				
				//delete sales return lines
				ltSalesreturnDao.deleteSalesReturnLinesByHeaderId(ltSalesReturnDto.getSalesReturnHeaderId());
				
				ltSalesReturnHeader.setSalesReturnHeaderId(ltSalesReturnDto.getSalesReturnHeaderId());
				ltSalesReturnHeader.setSalesReturnNumber(ltSalesReturnDto.getSalesReturnNumber());
				if(ltSalesReturnDto.getOutletName() !=null) {
					ltSalesReturnHeader.setOutletName(ltSalesReturnDto.getOutletName());
				}
				if(ltSalesReturnDto.getOutletCode() !=null) {
					ltSalesReturnHeader.setOutletCode(ltSalesReturnDto.getOutletCode());
				}
				if(ltSalesReturnDto.getOutletId() !=null) {
					ltSalesReturnHeader.setOutletId(ltSalesReturnDto.getOutletId());
				}if(ltSalesReturnDto.getInvoiceNumber() !=null) {
					ltSalesReturnHeader.setInvoiceNumber(ltSalesReturnDto.getInvoiceNumber());
				}
				
				if(ltSalesReturnDto.getReturnReason() !=null) {
					ltSalesReturnHeader.setReturnReason(ltSalesReturnDto.getReturnReason());
				}
				if(ltSalesReturnDto.getReturnStatus() !=null) {
					ltSalesReturnHeader.setReturnStatus(ltSalesReturnDto.getReturnStatus());
				}
				
				if(ltSalesReturnDto.getAddress() !=null) {
					ltSalesReturnHeader.setAddress(ltSalesReturnDto.getAddress());
				}
				if(ltSalesReturnDto.getLatitude() !=null) {
					ltSalesReturnHeader.setLatitude(ltSalesReturnDto.getLatitude());
				}
				if(ltSalesReturnDto.getLongitude() !=null) {
					ltSalesReturnHeader.setLongitude(ltSalesReturnDto.getLongitude());
				}
				
				if(ltSalesReturnDto.getBeatName() !=null) {
					ltSalesReturnHeader.setBeatName(ltSalesReturnDto.getBeatName());
				}
				
				if(ltSalesReturnDto.getStatus() !=null) {
					ltSalesReturnHeader.setStatus(ltSalesReturnDto.getStatus());
				}
				
				System.out.print("SalesReturn PriceList111 = "+ltSalesReturnDto.getPriceList());
				if(ltSalesReturnDto.getPriceList()!=null) {
					ltSalesReturnHeader.setPriceList(ltSalesReturnDto.getPriceList());
				}
				
				LtSalesReturnHeader siebelData = ltSalesreturnDao.getSalesReturnSiebelDataById(ltSalesReturnDto.getSalesReturnHeaderId());
				 if(siebelData.getSiebelRemark()!= null) {
				ltSalesReturnHeader.setSiebelRemark(siebelData.getSiebelRemark());
				}
				 if(siebelData.getSiebelStatus()!= null) {
					 ltSalesReturnHeader.setSiebelStatus(siebelData.getSiebelStatus());
				 }
				 
				 ltSalesReturnHeader.setTest(ltSalesReturnDto.getTest());
				
				ltSalesReturnHeader.setSalesReturnDate(new Date());
				//ltSalesReturnHeader.setCreatedBy(ltSalesReturnDto.getUserId());
				//ltSalesReturnHeader.setCreationDate(new Date());
				ltSalesReturnHeader.setLastUpdatedBy(ltSalesReturnDto.getUserId());
				ltSalesReturnHeader.setLastUpdateDate(new Date());
				
				ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
				
				//save sales return lines
				List<LtSalesReturnLines> salesReturnLines = ltSalesReturnDto.getLtSalesReturnLines();
				
				System.out.println("salesReturnLines"+salesReturnLines);
				for (Iterator iterator = salesReturnLines.iterator(); iterator.hasNext();) {
					LtSalesReturnLines soLine = (LtSalesReturnLines) iterator.next();

					LtSalesReturnLines ltSoLinestosave = new LtSalesReturnLines();
					
					if(ltSalesReturnHeader.getSalesReturnHeaderId() !=null) {
						ltSoLinestosave.setSalesReturnHeaderId(ltSalesReturnHeader.getSalesReturnHeaderId());
					}
					if(soLine.getProductId() !=null) {
						ltSoLinestosave.setProductId(soLine.getProductId());
					}
					if(soLine.getShippedQuantity() !=null) {
						ltSoLinestosave.setShippedQuantity(soLine.getShippedQuantity());
					}
					if(soLine.getReturnQuantity() !=null) {
						ltSoLinestosave.setReturnQuantity(soLine.getReturnQuantity());
					}
					if(soLine.getAvailability() !=null) {
						ltSoLinestosave.setAvailability(soLine.getAvailability());
					}if(soLine.getLocation() !=null) {
						ltSoLinestosave.setLocation(soLine.getLocation());
					}if(soLine.getStatus() !=null) {
						ltSoLinestosave.setStatus(soLine.getStatus());
					}
					if(soLine.getPrice() !=null) {
						ltSoLinestosave.setPrice(soLine.getPrice());
						ltSoLinestosave.setTotalPrice((ltSoLinestosave.getPrice() *ltSoLinestosave.getReturnQuantity()));
					}
					if(soLine.getLotNumber()!= null) {
					ltSoLinestosave.setLotNumber(soLine.getLotNumber());
					}
					ltSoLinestosave = updateLines(ltSoLinestosave);
					
					System.out.println("ltSoLinestosave"+ltSoLinestosave);
				}
				
			}else {
				//code for invoice number generation
				String salesReturnNumber = generateSalesreturnNumber(ltSalesReturnDto.getOutletId());
				
				ltSalesReturnHeader.setSalesReturnNumber(salesReturnNumber);
				
				if(ltSalesReturnDto.getOutletName() !=null) {
					ltSalesReturnHeader.setOutletName(ltSalesReturnDto.getOutletName());
				}
				if(ltSalesReturnDto.getOutletCode() !=null) {
					ltSalesReturnHeader.setOutletCode(ltSalesReturnDto.getOutletCode());
				}
				if(ltSalesReturnDto.getOutletId() !=null) {
					ltSalesReturnHeader.setOutletId(ltSalesReturnDto.getOutletId());
				}if(ltSalesReturnDto.getInvoiceNumber() !=null) {
					ltSalesReturnHeader.setInvoiceNumber(ltSalesReturnDto.getInvoiceNumber());
				}
				
				if(ltSalesReturnDto.getReturnReason() !=null) {
					ltSalesReturnHeader.setReturnReason(ltSalesReturnDto.getReturnReason());
				}
				if(ltSalesReturnDto.getReturnStatus() !=null) {
					ltSalesReturnHeader.setReturnStatus(ltSalesReturnDto.getReturnStatus());
				}
				
				if(ltSalesReturnDto.getAddress() !=null) {
					ltSalesReturnHeader.setAddress(ltSalesReturnDto.getAddress());
				}
				if(ltSalesReturnDto.getLatitude() !=null) {
					ltSalesReturnHeader.setLatitude(ltSalesReturnDto.getLatitude());
				}
				if(ltSalesReturnDto.getLongitude() !=null) {
					ltSalesReturnHeader.setLongitude(ltSalesReturnDto.getLongitude());
				}
				
				if(ltSalesReturnDto.getBeatName() !=null) {
					ltSalesReturnHeader.setBeatName(ltSalesReturnDto.getBeatName());
				}
				System.out.print("SalesReturn PriceList = "+ltSalesReturnDto.getPriceList());
				//if(ltSalesReturnDto.getPriceList()!=null) {
					ltSalesReturnHeader.setPriceList(ltSalesReturnDto.getPriceList());
					ltSalesReturnHeader.setTest(ltSalesReturnDto.getTest());
				//}
				
					//ltSalesReturnHeader.setStatus(ltSalesReturnDto.getStatus());
					ltSalesReturnHeader.setStatus(PENDINGAPPROVAL);
					
				ltSalesReturnHeader.setSalesReturnDate(new Date());
				ltSalesReturnHeader.setCreatedBy(ltSalesReturnDto.getUserId());
				ltSalesReturnHeader.setCreationDate(new Date());
				ltSalesReturnHeader.setLastUpdatedBy(ltSalesReturnDto.getUserId());
				ltSalesReturnHeader.setLastUpdateDate(new Date());
				
				ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
				
				List<LtSalesReturnLines> salesReturnLines = ltSalesReturnDto.getLtSalesReturnLines();
				
				for (Iterator iterator = salesReturnLines.iterator(); iterator.hasNext();) {
					LtSalesReturnLines soLine = (LtSalesReturnLines) iterator.next();

					LtSalesReturnLines ltSoLinestosave = new LtSalesReturnLines();
					
					if(ltSalesReturnHeader.getSalesReturnHeaderId() !=null) {
						ltSoLinestosave.setSalesReturnHeaderId(ltSalesReturnHeader.getSalesReturnHeaderId());
					}
					if(soLine.getProductId() !=null) {
						ltSoLinestosave.setProductId(soLine.getProductId());
					}
					if(soLine.getShippedQuantity() !=null) {
						ltSoLinestosave.setShippedQuantity(soLine.getShippedQuantity());
					}
					if(soLine.getReturnQuantity() !=null) {
						ltSoLinestosave.setReturnQuantity(soLine.getReturnQuantity());
					}
					if(soLine.getAvailability() !=null) {
						ltSoLinestosave.setAvailability(soLine.getAvailability());
					}if(soLine.getLocation() !=null) {
						ltSoLinestosave.setLocation(soLine.getLocation());
					}if(soLine.getStatus() !=null) {
						ltSoLinestosave.setStatus(soLine.getStatus());
					}
					if(soLine.getPrice() !=null) {
						ltSoLinestosave.setPrice(soLine.getPrice());
						ltSoLinestosave.setTotalPrice((ltSoLinestosave.getPrice() *ltSoLinestosave.getReturnQuantity()));
					}					
					if(soLine.getLotNumber()!= null) {
						ltSoLinestosave.setLotNumber(soLine.getLotNumber());
						}
					
					ltSoLinestosave = updateLines(ltSoLinestosave);
				}
				
			}
			
			if(ltSalesReturnHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
			// Send Notification Code & E-mail to Area Head for approval 
			//System.out.println("Hi This is the incoice number for notification:"+ltSalesReturnHeader.getInvoiceNumber()); 
			//System.out.println("Hi This is the sales return number for notification:"+ltSalesReturnHeader.getSalesReturnNumber());
			//List<LtMastUsers> ltMastUsersAreaHead = ltSalesreturnDao.getAreaHeadDetails(ltSalesReturnHeader.getInvoiceNumber(), ltSalesReturnHeader.getSalesReturnNumber());
			List<LtMastUsers> ltMastUsersAreaHead = ltSalesreturnDao.getAreaHeadDetails(ltSalesReturnHeader.getOutletId());
			//List<LtMastUsers> sysAdminUserList = ltSoHeadersDao.getActiveSysAdminUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
			List<LtMastUsers> ltMastUsersSalesOfficer = ltSalesreturnDao.getSalesOfficersDetails(ltSalesReturnHeader.getOutletId());
			List<LtMastUsers> ltMastUsersSysAdmin = ltSalesreturnDao.getSysAdminDetails(ltSalesReturnHeader.getOutletId());
			
			//System.out.println("Hi this is AreaHead" + ltMastUsersAreaHead);
			if(ltMastUsersAreaHead !=null) {
				for(LtMastUsers user:ltMastUsersAreaHead) {
					System.out.println("This is user for notification: "+user);
					if(user !=null) {   
			//sending notification to areahead
						
						//System.out.println("Hi this is before notification");
			webController.sendSalesReturnNotification(ltSalesReturnHeader, user);
			            //System.out.println("Hi this is after notification");
					}
				  }
				}
			
			if(ltMastUsersSalesOfficer !=null) {
				for(LtMastUsers user:ltMastUsersSalesOfficer) {
					System.out.println("This is user for notification: "+user);
					if(user !=null) {   
			//sending notification to areahead
						
						//System.out.println("Hi this is before notification");
			webController.sendSalesReturnNotification(ltSalesReturnHeader, user);
			            //System.out.println("Hi this is after notification");
					}
				  }
				}
			
			if(ltMastUsersSysAdmin !=null) {
				for(LtMastUsers user:ltMastUsersSysAdmin) {
					System.out.println("This is user for notification: "+user);
					if(user !=null) {   
			//sending notification to areahead
						
						//System.out.println("Hi this is before notification");
			webController.sendSalesReturnNotification(ltSalesReturnHeader, user);
			            //System.out.println("Hi this is after notification");
					}
				  }
				}
			
			// End of Send Notification Code
			
			// Code for Send E-mail to Area Head
			
			 if(ltMastUsersAreaHead!= null) 
			      {			
				 System.out.println("Hi I'm in send email sales return");
						for(Iterator iterator = ltMastUsersAreaHead.iterator(); iterator.hasNext();) {
							LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
							System.out.print(ltMastUsers.getTokenData());
							
							String subject = "EMAIL_SALES_RETURN_APPROVAL_";
						      
						     // String body = ltSoHeadersDao.getEmailBody(subject);
							//String body= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
							String body= "EMAIL For SALES RETURN APPROVAL";
							System.out.print("Email body"+body);
						      //String userName = ltSalesreturnDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
							String userName = ltSalesreturnDao.getUserNameAgainsUserId(ltSalesReturnDto.getUserId());
						      System.out.print("userName for Email"+userName);
						      
						      //List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
						      //System.out.println("amount is........" + amount);
						      //double amt = amount.stream().collect(Collectors.summingDouble((Double::doubleValue)));
						      							      
						 /*     List<String> amount = ltSalesreturnDao.getTotalAmount1(ltSoHeader.getHeaderId());
						     
						      double sum = 0.0;
						      for (String str : amount) {
						    	  if(str.equals("null")) {
						    		  str= "0";
						    	  }
						             double number = Double.parseDouble(str);
						            sum = sum+number;
						        }
						      String totalAmount= String.valueOf(sum);
						      System.out.print("amount for Email" +totalAmount);
						      //System.out.print("amount for Email"+amt);            
						 */
						      
						      String salespersonName=userName;
						      //String totalAmount= Double.toString(amt);
						      //String body= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
						      body = body.replace("${salesOrder}", ltSalesReturnDto.getOrderNumber());
						      body = body.replace("${salespersonName}", salespersonName);
						      //body = body.replace("${totalAmount}", totalAmount);
						      if(ltMastUsers.getEmail()!= null) {
						      String to= ltMastUsers.getEmail();
//						      emailService.sendSimpleMessage(to, subject, body);
						      sendEmail1( subject, body, to);
				          }
						}
			}
		}	
			// End of Send E-mail code
			
			///siebel json creation
			System.out.println("Hi I'm in Siebel Mehtod");
			
			//if(ltSalesReturnDto.getReturnStatus().equalsIgnoreCase("APPROVED")) {
			if(ltSalesReturnDto.getStatus().equalsIgnoreCase("APPROVED")) {
					
			String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateReturnOrder?matchrequestformat=y";
	        String method = "POST";
	       // String contentType = "Content-Type: application/json";
	       // String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw==";
			
	        
/*			JSONObject salesReturnDetail = new JSONObject();
			JSONObject SiebelMessage = new JSONObject();
			JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
			JSONObject Header = new JSONObject();
			JSONObject ListOfLineItem = new JSONObject();
*/			JSONArray LineItem = new JSONArray();
			JSONObject lineData = new JSONObject();
			JSONObject ListOfXA = new JSONObject();
			JSONObject XA = new JSONObject();
/*			
			//salesReturnDetail.put("InvoiceNumber","INV-29686-2223-000218");
			salesReturnDetail.put("InvoiceNumber", ltSalesReturnDto.getInvoiceNumber());
			//salesReturnDetail.put("ReturnReason", "SHORT RECEIVED BY CUSTOMER");  
			salesReturnDetail.put("ReturnReason", ltSalesReturnDto.getReturnReason());
			salesReturnDetail.put("SiebelMessage", SiebelMessage);
			
			
			SiebelMessage.put("IntObjectFormat", "Siebel Hierarchical");
			SiebelMessage.put("MessageId", "");
			SiebelMessage.put("IntObjectName", "AT Orders Integration IO");
			SiebelMessage.put("MessageType", "Integration Object");
			SiebelMessage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
			
			ListOfATOrdersIntegrationIO.put("Header", Header);
			
			//Header.put("Account Id", "1-EEWE-478");
			Header.put("Account Id", ltSalesReturnDto.getOutletId());
			//Header.put("Account", "3M CAR CARE");
			Header.put("Account", ltSalesReturnDto.getOutletName());
			Header.put("Order Number", "RMA-29686-2223-000078");
			//Header.put("Source Inventory Id", "1-2C7QNZG");             // handle by Jainuddin 
			Header.put("ListOfLine Item", ListOfLineItem);
			
			ListOfLineItem.put("Line Item", LineItem);
			
			//lineData.put("Product Id", "1-MZZR-1");
			//lineData.put("Name", "P02IAPKP040");
			//lineData.put("Quantity", "10");
			//lineData.put("ListOfXA", ListOfXA);
			
			//ListOfXA.put("XA", XA);
			
//			XA.put("Qty", "10");
//			XA.put("Location", "20602-29686-HFS");
//			XA.put("Availability", "On Hand");
//			XA.put("Status", "Good");
//			XA.put("Lot Number", "");
	*/		
			List<LtSalesReturnLines>lineItem= ltSalesReturnDto.getLtSalesReturnLines();
     		    for(int i=0; i<lineItem.size(); i++) 
     		    {
     		    	
     			    //LtSalesReturnLines ltSalesReturnLines= new LtSalesReturnLines();
     		    	System.out.println("Hi Test Line Data == "+lineItem.get(i).getProductId());
     			    String prodId= lineItem.get(i).getProductId();
     			    String prodName= lineItem.get(i).getProductName();
     			    
     			   String qty= null;
     			    if(lineItem.get(i).getReturnQuantity()!=null) {
     			    qty=   Long.toString(lineItem.get(i).getReturnQuantity());    			   
     			   }else {qty= null;}
     			    
     			    //data for listOfXA
     			   String qty1= null;
    			    if(lineItem.get(i).getReturnQuantity()!= null) {
     			    qty1 = Long.toString(lineItem.get(i).getReturnQuantity()); }else {qty1= null;}
    			    
     			    String location = lineItem.get(i).getLocation();
     			    String availability = lineItem.get(i).getAvailability();
     			    String status1= lineItem.get(i).getStatus();
     			    String lotNo= lineItem.get(i).getLotNumber();
     			    
     			//  lineItem.add(ltSalesReturnLines);
     			
     			    lineData.put("Product Id", prodId);
     			    lineData.put("Name", prodName );
     			    lineData.put("Quantity", qty);
     			
     			    XA.put("Qty", qty1);
     			    
     			// need to find out location using this query
     			//  SELECT lmiv.INVENTORY_NAME , lmiv.INVENTORY_ID FROM LT_MAST_INVENTORY_V lmiv, LT_MAST_DISTRIBUTORS_V lmdv,  
     			//  LT_MAST_OUTLETS_V lmov, LT_SO_HEADERS lsh 
     			//  WHERE lmdv.DISTRIBUTOR_ID = lmiv.DIST_ID
     			//  AND lsh.OUTLET_ID = lmov.OUTLET_ID 
     			//  AND lmdv.DISTRIBUTOR_ID = lmov.DISTRIBUTOR_ID 
     			//  AND lsh.OUTLET_ID = '1-EEWE-478'
     			    XA.put("Location",location);     
     			    
     			    XA.put("Availability", availability);
     			    XA.put("Status",status1);
     			    
     			//    Need to find out lotNo using this query
     			//  SELECT distinct SERIAL_NUM AS LOT_NUMBER FROM SIEBEL.S_ASSET
     			//  WHERE PROD_ID = '1-MZZR-1'
     			//  and INVLOC_ID = '1-2FPGVLJ';
     			    XA.put("Lot Number",lotNo);
     			    
     			   ListOfXA.put("XA", XA);
     			   lineData.put("ListOfXA", ListOfXA);
     			   LineItem.put(lineData);
     					
   		}
     		   System.out.print(" New line data of LineItem is :" + LineItem);
     				
			//  String jsonPayload =salesReturnDetail.toString();

			  String payload = "{\n" +
					    "    \"InvoiceNumber\": \"INV-29686-2223-000218\",\n" +
					    "    \"ReturnReason\": \"SHORT RECEIVED BY CUSTOMER\",\n" +
					    "    \"SiebelMessage\": {\n" +
					    "        \"IntObjectFormat\": \"Siebel Hierarchical\",\n" +
					    "        \"MessageId\": \"\",\n" +
					    "        \"IntObjectName\": \"AT Orders Integration IO\",\n" +
					    "        \"MessageType\": \"Integration Object\",\n" +
					    "        \"ListOfAT Orders Integration IO\": {\n" +
					    "            \"Header\": {\n" +
					    "                \"Account Id\": \"1-EEWE-478\",\n" +
					    "                \"Account\": \"3M CAR CARE\",\n" +
					    "                \"Order Number\": \"RMA-29686-2224-000019\",\n" +
					    "                \"Source Inventory Id\": \"1-2C7QNZG\",\n" +
					    "                \"ListOfLine Item\": {\n" +
					    "                    \"Line Item\": "+LineItem.toString()+"\n" +
					    "                }\n" +
					    "            }\n" +
					    "        }\n" +
					    "    }\n" +
					    "}";

			    String jsonPayload= payload;
			  
			    // Replace Header Data
			    String invoiceNum= "\"InvoiceNumber\":\""+ ltSalesReturnDto.getInvoiceNumber()+"\"";
		        jsonPayload= jsonPayload.replace("\"InvoiceNumber\": \"INV-29686-2223-000218\"", invoiceNum);
		        System.out.print(" New invoiceNum is :" + invoiceNum);
		        
		        String returnReason= "\"ReturnReason\":\""+ ltSalesReturnDto.getReturnReason()+"\"";
		        jsonPayload= jsonPayload.replace("\"ReturnReason\": \"SHORT RECEIVED BY CUSTOMER\"", returnReason);
		        System.out.print(" New returnReason is :" + returnReason);
		         
		        String outletId= "\"Account Id\":\""+ ltSalesReturnDto.getOutletId()+"\"";
		        jsonPayload= jsonPayload.replace("\"Account Id\": \"1-EEWE-478\"", outletId);
		        System.out.print(" New outLid is :" + outletId);
		        
		        String outletName= "\"Account\":\""+ ltSalesReturnDto.getOutletName()+"\"";
		        jsonPayload= jsonPayload.replace("\"Account\": \"3M CAR CARE\"", outletName);
		        System.out.print(" New outletName is :" + outletName);
		        
		        //Order Number", "RMA-29686-2223-000078"
		        // query to find order number 
		        //SELECT DISTINCT lsh.ORDER_NUMBER FROM LT_SO_HEADERS lsh, LT_SALES_RETURN_HEADERS lsrh 
		        //WHERE REPLACE(lsh.SIEBEL_INVOICENUMBER, '{"Invoice Number":', '')  =  'INV-29686-2324-000093'
		        //AND lsrh.INVOICE_NUMBER = lsh.SIEBEL_INVOICENUMBER 
		        
		       // String orderNo = ltSalesreturnDao.getOrderNoFromInvoiceNo(ltSalesReturnDto.getInvoiceNumber());
			   // System.out.print("orderNo for siebel SalesReturn"+orderNo);
			      
		        String orderNumber= "\"Order Number\":\""+ ltSalesReturnDto.getSalesReturnNumber()+"\"";
		        jsonPayload= jsonPayload.replace("\"Order Number\": \"RMA-29686-2224-000019\"", orderNumber);
		        System.out.print(" New orderNumber is :" + orderNumber);
		        
		        
		        // query to find inventory Id
		        //SELECT DISTINCT lmiv.INVENTORY_ID FROM LT_MAST_INVENTORY_V lmiv, LT_MAST_DISTRIBUTORS_V lmdv,  
		        //LT_MAST_OUTLETS_V lmov, LT_SO_HEADERS lsh 
		        //WHERE lmdv.DISTRIBUTOR_ID = lmiv.DIST_ID
		        //AND lsh.OUTLET_ID = lmov.OUTLET_ID 
		        //AND lmdv.DISTRIBUTOR_ID = lmov.DISTRIBUTOR_ID 
		        //AND lsh.OUTLET_ID = '1-EEWE-478'

		        //For testing currently we are using vinayKumar inventory Id i.e 1-2FPGVLJ
		        String invntId = "1-2FPGVLJ";
		        String inventoryId= "\"Source Inventory Id\":\""+ invntId+"\"";  //ltSalesReturnDto.getInventoryId()+"\"";
		        jsonPayload= jsonPayload.replace("\"Source Inventory Id\": \"1-2C7QNZG\"", inventoryId);
		        System.out.print(" New inventoryId is :" + inventoryId);
		        
		       /* // Replace Line Item Data
		        //String lineItemData= "\"Source Inventory Id\":\""+ ltSalesReturnDto.getOutletId()+"\"";
		       // jsonPayload= jsonPayload.replace("\"Source Inventory Id\": \"1-2C7QNZG\"", inventoryId);
		       // System.out.print(" New lineItem is :" + lineItemData);
		        //String lineItemData= LineItem.toString();
		        //jsonPayload= jsonPayload.replace("{[listOfLineItem1]}", lineItemData); */
		        
		       // System.out.println(jsonPayload);     //(requestBody);
			  System.out.println("jsonPayload =" +jsonPayload);  
			  // Create URL object
		        URL obj = new URL(url);
		        System.out.println("url is..... = "+ url);
		        // Add this line before opening the connection
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

		        
		        // Create HttpURLConnection object
		        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		        // Set request method
		        con.setRequestMethod(method);
		        // Set request headers
		        con.setRequestProperty("Content-Type", "application/json");
		        //con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpMb25hcjEyMw==");

		        String username = "VINAY.KUMAR6";
		        String password = "Welcome1";
		        String auth = username + ":" + password;
		        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
		        String authHeaderValue = "Basic " + new String(encodedAuth);
		        System.out.println("This is user authHeaderValue"+authHeaderValue);
		        con.setRequestProperty("Authorization", authHeaderValue);
		        
		        // Enable output and set request body
		        con.setDoOutput(true);
		        try (OutputStream os = con.getOutputStream()) {
		            byte[] input = jsonPayload.getBytes("utf-8");
		            os.write(input, 0, input.length);
		        }
		        
		     // Get response code
		        int responseCode = con.getResponseCode();
		        String msg = con.getResponseMessage();
		        System.out.println("Response Code : " + responseCode);
		        System.out.println("Response Message : " + msg);
		        
		     // Read the response body
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		        StringBuilder response = new StringBuilder();
//		        String line;
//		        while ((line = reader.readLine()) != null) {
//		            response.append(line);
//		        }
//		        reader.close();

		        // Show the response
//		        System.out.println("Response Body: " + response.toString());

		        StringBuilder response = new StringBuilder();
		        BufferedReader reader;
		        InputStream inputStream;
		        if(responseCode >= 200 && responseCode < 300 ) {
		        	inputStream = con.getInputStream();
		        	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		            String line;
		          while ((line = reader.readLine()) != null) {
		        	  System.out.println("line success response is="+line);
		              response.append(line);
		              System.out.println("success response is = " + response);
		          }
		          reader.close();
		  
//		           Show the response
		          System.out.println("Response Body: " + response.toString());

		        }else {
		        	    reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	                    String line;
	                      while ((line = reader.readLine()) != null) 
	                   {
	      	              System.out.println("line error response is="+line);
	                      response.append(line);
	                   }        
	        	          inputStream = con.getErrorStream();
	        	          System.out.println("Error response: " + responseCode + " - " + msg);
	        	          System.out.println("Error Response Body: " + response);
	        	    }
			  			
		        //saving siebel response & status code in to table
		        
		        //LtSalesReturnHeader ltSalesReturnHeader = new LtSalesReturnHeader();
		        String resCode = Integer.toString(responseCode);
		        //System.out.println("Save Response before Body: " + resCode);
		        ltSalesReturnHeader.setSiebelStatus(resCode);
		        //System.out.println("Save Response after Body: " + resCode);

		        String res = response.toString();
		        //System.out.println("Save Response before Body: " + res);
		        ltSalesReturnHeader.setSiebelRemark(res);
		        //System.out.println("Save Response after Body: " + res);
		        
		        ltSalesReturnHeader.setSiebelJsonpayload(jsonPayload);
                   
		        ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
		        
			}
			//get sales return response
			RequestDto requestDto = new RequestDto();
			requestDto.setSalesReturnNumber(ltSalesReturnHeader.getSalesReturnNumber());
			
			//requestDto.setSiebelRemark(ltSalesReturnHeader.getSiebelRemark());
			//requestDto.setSiebelStatus(ltSalesReturnHeader.getSiebelStatus());
			
			requestDto.setLimit(-1);
			requestDto.setOffset(2);
			
			status = getSalesReturn(requestDto);
			if(status.getData() !=null) {
				status.setCode(SUCCESS);
				status.setMessage("Save Successfully");
				status.setData(status.getData());
				return status;
			}else {
				status.setCode(FAIL);
				status.setMessage("RECORD NOT FOUND");
				return status;
			} 
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void sendEmail1(String subject, String text, String to) throws IOException {
		System.out.println("Hi I'm in send email sales return...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
        	
            // Escape double quotes in the text string
            //String escapedText = text.replaceAll("\"", "\\\\\"");

            // Other variables
            //String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
            //String to = "Shubham.magare@lonartech.com";
        	
            HttpPost httpPost = new HttpPost("http://10.245.4.187:7085/email/send-email");
        	//HttpPost httpPost = new HttpPost("http://10.245.4.187:6070/master/email/send-email");
            httpPost.addHeader("accept", "application/json");
            httpPost.addHeader("X-API-Version", "v1.0");
            httpPost.addHeader("Content-Type", "application/json");
            //to= "Shubham.magare@lonartech.com";
            // Create JSON payload
            String jsonPayload = "{\"subject\": \"" + subject + "\", \"text\": \"" + text + "\", \"to\": \"" + to + "\"}";
            System.out.println("Hi I'm in send email1111... jsonPayload" + jsonPayload);
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
 
            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                // Print response if needed
                if (responseEntity != null) {
                    System.out.println(EntityUtils.toString(responseEntity));
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }		
	}
	
	
	String generateSalesreturnNumber(String outletId) throws ServerException{
		
		String distributorCRMCode = ltSalesreturnDao.getDistIdFromOutletCode(outletId);
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
	    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
	    String finYear;
	    System.out.println("Financial month : " + month);
	    if (month <= 3) {
	        finYear = (String.valueOf(year- 1)).substring(2)+""+(String.valueOf(year)).substring(2);
	    } else {
	        finYear = (String.valueOf(year)).substring(2)+""+(String.valueOf(year + 1)).substring(2);
	    }
	
	   //Long sequanceNo = ltSalesreturnDao.getSequancesValue();
		//String seqNoSixDigit = String.format("%06d", 123);
	    String seqNoSixDigit = ltSalesreturnDao.getSalesReturnSequence();
	    System.out.println("seqNoSixDigit"+seqNoSixDigit);
	    //return "MSR-" + finYear + "-" + seqNoSixDigit;
	    return "RMA-" + distributorCRMCode + "-" + finYear + "-" + seqNoSixDigit;
	}
	
	@Override
	public Status getStatusForSalesReturn()throws ServerException{
		try {
		Status status = new Status();
		List<LtSalesReturnStatus> list = ltSalesreturnDao.getStatusForSalesReturn();
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Status getAvailabilityForSalesReturn() throws ServerException {
		try {
		Status status = new Status();
		List<LtSalesReturnAvailability> list = ltSalesreturnDao.getAvailabilityForSalesReturn();
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Status getLocationForSalesReturn(String distributorCode) throws ServerException {
		try {
		Status status = new Status();
		List<LtSalesReturnAvailability> list = ltSalesreturnDao.getLocationForSalesReturn(distributorCode);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(list);
			return status;
		} else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public Status getSalesReturn(RequestDto requestDto) throws ServerException{
		try {
		Status status = new Status();
		System.out.println("requestDto"+requestDto);
		List<Long> IdsList = ltSalesreturnDao.getSalesReturnHeader(requestDto);
		Long recordCount = ltSalesreturnDao.getSalesReturnRecordCount(requestDto);
		//System.out.println("IdsList"+IdsList +" "+recordCount);
		status.setTotalCount(recordCount);
		status.setRecordCount(recordCount);
		if(IdsList== null) {
			status.setCode(RECORD_NOT_FOUND);
			status.setCode(FAIL);
			status.setData("Record not found"); 
			return status;
		}
		
		SalesReturnApproval salesReturnApproval = new SalesReturnApproval();
		List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
		
		Double totalReturnAmount = (double) 0;
		 //responseDtoList = ltSalesreturnDao.getSalesReturn(IdsList);
		 
		 System.out.println("responseDtoList"+responseDtoList);
		
/*		Map<Long, LtSalesReturnDto> salesReturnHeaderDtoMap = new LinkedHashMap<Long, LtSalesReturnDto>();
		Map<Long, List<LtSalesReturnLines>> salesReturnLineDtoMap = new LinkedHashMap<Long, List<LtSalesReturnLines>>();
		
		for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
			ResponseDto responseDto = (ResponseDto) iterator.next();

			LtSalesReturnLines salesReturnLineDto = new LtSalesReturnLines();

			if (responseDto.getSalesReturnLineId() != null) {
				salesReturnLineDto.setSalesReturnLineId(responseDto.getSalesReturnLineId());;
			}
			if (responseDto.getSalesReturnHeaderId() != null) {
				salesReturnLineDto.setSalesReturnHeaderId(responseDto.getSalesReturnHeaderId());;
			}
			if (responseDto.getProductId() != null) {
				salesReturnLineDto.setProductId(responseDto.getProductId());
			}
			if (responseDto.getShippedQuantity() != null) {
				salesReturnLineDto.setShippedQuantity(responseDto.getShippedQuantity());
			}
			if (responseDto.getReturnQuantity() != null) {
				salesReturnLineDto.setReturnQuantity(responseDto.getReturnQuantity());
			}
			if (responseDto.getAvailability() != null) {
				salesReturnLineDto.setAvailability(responseDto.getAvailability());
			}
			if (responseDto.getLocation() != null) {
				salesReturnLineDto.setLocation(responseDto.getLocation());
			}
			
			if (responseDto.getStatus() != null) {
				salesReturnLineDto.setStatus(responseDto.getStatus());
			}
			if (responseDto.getPrice() != null) {
				salesReturnLineDto.setPrice(responseDto.getPrice());
			}
			if(responseDto.getTotalPrice() !=null) {
				salesReturnLineDto.setTotalPrice(responseDto.getTotalPrice());
				totalReturnAmount = totalReturnAmount + salesReturnLineDto.getTotalPrice();
			}
			
			 System.out.println("responseDto.getPrice()"+responseDto.getPrice());
			
			if (salesReturnLineDtoMap.get(responseDto.getSalesReturnHeaderId()) != null) {
				// already exost
				List<LtSalesReturnLines> soLineDtoList = salesReturnLineDtoMap.get(responseDto.getSalesReturnHeaderId());
				if(salesReturnLineDto.getSalesReturnLineId() != null) {
					soLineDtoList.add(salesReturnLineDto);
				}
				salesReturnLineDtoMap.put(responseDto.getSalesReturnHeaderId(), soLineDtoList);
			} else {
				// add data into soheader map
				LtSalesReturnDto soHeaderDto = new LtSalesReturnDto();
				soHeaderDto.setSalesReturnHeaderId(responseDto.getSalesReturnHeaderId());
				soHeaderDto.setSalesReturnNumber(responseDto.getSalesReturnNumber());
				soHeaderDto.setInvoiceNumber(responseDto.getInvoiceNumber());
				soHeaderDto.setOutletId(responseDto.getOutletId());
				soHeaderDto.setReturnReason(responseDto.getReturnReason());
				soHeaderDto.setReturnStatus(responseDto.getReturnStatus());
				soHeaderDto.setStatus(responseDto.getStatus());
				soHeaderDto.setSalesReturnDate(responseDto.getSalesReturnDate());
				soHeaderDto.setTotalSalesreturnAmount(totalReturnAmount);
				soHeaderDto.setOutletName(responseDto.getOutletName());
				soHeaderDto.setBeatName(responseDto.getBeatName());
				
				soHeaderDto.setPricelist(responseDto.getPriceList());
				soHeaderDto.setTest(responseDto.getTest());
				
				//soHeaderDto.setSiebelRemark(responseDto.getSiebelRemark());
				//soHeaderDto.setSiebelStatus(responseDto.getSiebelStatus());
				
				salesReturnHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);

				List<LtSalesReturnLines> soLineDtoList = new ArrayList<LtSalesReturnLines>();
				
				if (salesReturnLineDto.getSalesReturnLineId() != null) {
					soLineDtoList.add(salesReturnLineDto);
				}
				salesReturnLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);	 
			}

		}
		
		LtSalesReturnDetailsDto orderDetailsDto = new LtSalesReturnDetailsDto();

		List<LtSalesReturnDto> soHeaderDtoList = new ArrayList<LtSalesReturnDto>();

		for (Map.Entry<Long, LtSalesReturnDto> entry : salesReturnHeaderDtoMap.entrySet()) {
			LtSalesReturnDto soHeaderDto = entry.getValue();

			List<LtSalesReturnLines> soLineDtoList = salesReturnLineDtoMap.get(entry.getKey());
			soHeaderDto.setLtSalesReturnLines(soLineDtoList);

			soHeaderDtoList.add(soHeaderDto);
		}
		orderDetailsDto.setLtSalesReturnDto(soHeaderDtoList);
*/		
		if(!IdsList.isEmpty()) {
		 for(int i =0; i<IdsList.size(); i++) 
			{
				System.out.println("I'm in for loop 11111111111");
				System.out.println("Hiiii" +i);
                //i = Long.valueOf(ids).intValue();
				ResponseDto	ltSalesReturnHeaderDto = new ResponseDto();
				
				List<LtSalesReturnLines> salesReturnLineData = ltSalesreturnDao.getSalesReturnLineDataForApproval(IdsList.get(i), requestDto);
				System.out.println("sales Return Line Data ==-------== "+salesReturnLineData);
				
				//List<LtSalesReturnResponseDto> salesReturnHeaderData = ltSalesreturnDao.getSalesReturnForPendingAprroval1(salesReturnHeaderId.get(i));
				ResponseDto responseDtoList1 = ltSalesreturnDao.getSalesReturnForAprroval1(IdsList.get(i), requestDto);
				
				//System.out.println("sales Return Header Data ==-------== "+salesReturnHeaderData);
				Long id1 = responseDtoList1.getSalesReturnHeaderId();
				//System.out.println("sales Return Header IDDDDDDDDDD" + id1);
				//if(id1!= null) {
				ltSalesReturnHeaderDto.setSalesReturnHeaderId(id1);//}
				ltSalesReturnHeaderDto.setSalesReturnNumber(responseDtoList1.getSalesReturnNumber());
				
				ltSalesReturnHeaderDto.setInvoiceNumber(responseDtoList1.getInvoiceNumber());
				ltSalesReturnHeaderDto.setOutletId(responseDtoList1.getOutletId());
				ltSalesReturnHeaderDto.setReturnStatus(responseDtoList1.getReturnStatus());
				ltSalesReturnHeaderDto.setReturnReason(responseDtoList1.getReturnReason());
				ltSalesReturnHeaderDto.setStatus(responseDtoList1.getStatus());
				ltSalesReturnHeaderDto.setSalesReturnDate(responseDtoList1.getSalesReturnDate());
				ltSalesReturnHeaderDto.setBeatName(responseDtoList1.getBeatName());
				ltSalesReturnHeaderDto.setOutletName(responseDtoList1.getOutletName());
				ltSalesReturnHeaderDto.setPriceList(responseDtoList1.getPriceList());
								
				List<LtSalesReturnLines> ltSalesReturnLineDto1 = new ArrayList<LtSalesReturnLines>();
				//int line=0;
				//System.out.println("I'm in for loop above 2222222222");
				//for(LtSalesReturnLineDto ltSalesReturnLineDto:salesReturnLineData)
				for(int line =0; line<salesReturnLineData.size(); line++) 
					{
					LtSalesReturnLines ltSalesReturnLineDto = new LtSalesReturnLines();
					
					//System.out.println("salesssss ==-------== "+salesReturnLineData.get(line).getSalesReturnHeaderId());
					
					//System.out.println("I'm in for loop 22222222");
					Long id= salesReturnLineData.get(line).getSalesReturnHeaderId();
					ltSalesReturnLineDto.setSalesReturnHeaderId(id);	
					ltSalesReturnLineDto.setProductId(salesReturnLineData.get(line).getProductId());
					ltSalesReturnLineDto.setReturnQuantity(salesReturnLineData.get(line).getReturnQuantity());
					ltSalesReturnLineDto.setAvailability(salesReturnLineData.get(line).getAvailability());
					ltSalesReturnLineDto.setLotNumber(salesReturnLineData.get(line).getLotNumber());
					ltSalesReturnLineDto.setRemainingQuantity(salesReturnLineData.get(line).getRemainingQuantity());
					ltSalesReturnLineDto.setLocation(salesReturnLineData.get(line).getLocation());
					ltSalesReturnLineDto.setShippedQuantity(salesReturnLineData.get(line).getShippedQuantity());
					ltSalesReturnLineDto.setStatus(salesReturnLineData.get(line).getStatus());
					ltSalesReturnLineDto.setTotalPrice(salesReturnLineData.get(line).getTotalPrice());
					
					ltSalesReturnLineDto1.add(ltSalesReturnLineDto);
									
				}
				ltSalesReturnHeaderDto.setLtSalesReturnLines(ltSalesReturnLineDto1);
				responseDtoList.add(ltSalesReturnHeaderDto);
			}
		 salesReturnApproval.setResponseDto(responseDtoList);

		 
		if (responseDtoList != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(salesReturnApproval);
			return status;
		} }else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private LtSalesReturnHeader updateSalesReturnHeader(LtSalesReturnHeader ltSalesreturnHeader) throws ServerException {
		LtSalesReturnHeader ltSoHeaderOp = null;
		ltSoHeaderOp = ltSalesreturnDao.updateSalesReturnHeader(ltSalesreturnHeader);
		return ltSoHeaderOp;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private LtSalesReturnLines updateLines(LtSalesReturnLines ltSalesreturnlines) throws ServerException {
		LtSalesReturnLines ltSoHeaderOp = null;
		ltSoHeaderOp = ltSalesreturnDao.updateLines(ltSalesreturnlines);
		return ltSoHeaderOp;
	}
	@Override
	public Status getInvoices(RequestDto requestDto) throws ServerException{
		Status status = new Status();
		Date d1 = new Date();
		System.out.println("in method getInvoices service"+ new Date() + "this is datwe & time "+d1);
		try {
			List<LtInvoiceDetailsResponseDto> ltInvoiceDetailsResponseDto = new ArrayList<LtInvoiceDetailsResponseDto>();
/*		ltInvoiceDetailsResponseDto = ltSalesreturnDao.getInvoiceDetails(requestDto);
		System.out.println("Invoice details are = "+ ltInvoiceDetailsResponseDto);
		Map<String, LtInvoiceDetailsDto> invoiceDetailsHeaderDtoMap = new LinkedHashMap<String, LtInvoiceDetailsDto>();
		Map<String, List<LtInvoiceDetailsLineDto>> invoiceDetailsLineDtoMap = new LinkedHashMap<String, List<LtInvoiceDetailsLineDto>>();
		
		System.out.println("response Dto"+ ltInvoiceDetailsResponseDto);
		
		for (Iterator iterator = ltInvoiceDetailsResponseDto.iterator(); iterator.hasNext();) {
			LtInvoiceDetailsResponseDto responseDto = (LtInvoiceDetailsResponseDto) iterator.next();
		
			LtInvoiceDetailsLineDto invoiceDetailsLineDto = new LtInvoiceDetailsLineDto();

			if (responseDto.getProductCode() != null) {
				invoiceDetailsLineDto.setProductCode(responseDto.getProductCode());
			}
			if(responseDto.getProductName() !=null) {
				invoiceDetailsLineDto.setProductName(responseDto.getProductName());
			}
			if(responseDto.getShippedQuantity() !=null) {
				invoiceDetailsLineDto.setShippedQuantity(responseDto.getShippedQuantity());
			}
			if(responseDto.getPtrPrice() !=null) {
				invoiceDetailsLineDto.setPtrPrice(responseDto.getPtrPrice());
			}if(responseDto.getListPrice() !=null) {
				invoiceDetailsLineDto.setListPrice(responseDto.getListPrice());
			}
			if(responseDto.getPtrBasePrice() !=null) {
				invoiceDetailsLineDto.setPtrBasePrice(responseDto.getPtrBasePrice());
			}
			
			if(invoiceDetailsLineDtoMap.get(responseDto.getProductCode()) !=null) {
				List<LtInvoiceDetailsLineDto> invoiceLineDtoList = invoiceDetailsLineDtoMap.get(responseDto.getProductCode());
				if(invoiceDetailsLineDto.getProductCode() != null) {
					invoiceLineDtoList.add(invoiceDetailsLineDto);
				}
				invoiceDetailsLineDtoMap.put(responseDto.getProductCode(), invoiceLineDtoList);
			}else {
				

				// add data into soheader map
				LtInvoiceDetailsDto invoiceHeaderDto = new LtInvoiceDetailsDto();
				
				
				String invoiceNo =null;
				String beatName= null;
				if(responseDto.getInvoiceNumber() !=null) {
					invoiceNo = responseDto.getInvoiceNumber();
					//beatName =  ltSalesreturnDao.getBeatNameAgainstInvoiceNo(invoiceNo);
					beatName = "AYYAPPANTHANGAL";
				}
				 
				
				System.out.println("beatName is =" + beatName);
				//if(responseDto.getInvoiveNumber()!= null) {
				//	ltInvoiceDetailsResponseDto = ltSalesreturnDao.getInvoiceDetails(requestDto);
					//}
								
				invoiceHeaderDto.setDistributorName(responseDto.getDistributorName());
				invoiceHeaderDto.setDistributorCode(responseDto.getDistributorCode());
				invoiceHeaderDto.setDistributorId(responseDto.getDistributorId());
				invoiceHeaderDto.setOrderNumber(responseDto.getOrderNumber());
				invoiceHeaderDto.setOutletCode(responseDto.getOutletCode());
				invoiceHeaderDto.setOutletName(responseDto.getOutletName());
				invoiceHeaderDto.setInvoiceNumber(responseDto.getInvoiceNumber());
				invoiceHeaderDto.setInvoiceDate(responseDto.getInvoiceDate());
				invoiceHeaderDto.setLocation(responseDto.getLocation());
				invoiceHeaderDto.setTotalAmount(responseDto.getTotalAmount());
				invoiceHeaderDto.setPriceListId(responseDto.getPriceListId());
				invoiceHeaderDto.setPriceListName(responseDto.getPriceListName());
				invoiceHeaderDto.setInventoryId(responseDto.getInventoryId());
				if(beatName!= null) {
				invoiceHeaderDto.setBeatName(beatName);;
				}				
				
				invoiceDetailsHeaderDtoMap.put(responseDto.getInvoiceNumber(), invoiceHeaderDto);

				List<LtInvoiceDetailsLineDto> invoiceLineDtoList = new ArrayList<LtInvoiceDetailsLineDto>();
				
				//List<LtInvoiceDetailsLineDto> invoiceLineDtoList = invoiceDetailsLineDtoMap.get(responseDto.getProductCode());
				if(invoiceDetailsLineDto.getProductCode() != null) {
					invoiceLineDtoList.add(invoiceDetailsLineDto);
				}
				invoiceDetailsLineDtoMap.put(responseDto.getInvoiceNumber(), invoiceLineDtoList);
				
			}
		}
		
		LtInvoiceDto invoiceDetailsDto = new LtInvoiceDto();

		List<LtInvoiceDetailsDto> invoiceHeaderDtoList = new ArrayList<LtInvoiceDetailsDto>();

		for (Map.Entry<String, LtInvoiceDetailsDto> entry : invoiceDetailsHeaderDtoMap.entrySet()) {
			LtInvoiceDetailsDto invoiceHeaderDto = entry.getValue();

			List<LtInvoiceDetailsLineDto> ltInvoiceDetailsLineDto = invoiceDetailsLineDtoMap.get(entry.getKey());
			invoiceHeaderDto.setLtInvoiceDetailsLineDto(ltInvoiceDetailsLineDto);

			invoiceHeaderDtoList.add(invoiceHeaderDto);
		}
		invoiceDetailsDto.setLtInvoiceDetailsDto(invoiceHeaderDtoList);
	*/	
			/*		if (ltInvoiceDetailsResponseDto != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			//status.setData(invoiceDetailsDto);
			return status;
		} 
			else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e) {e.printStackTrace();}
		return status;
	*/
			//System.out.println("RequestDto is = "+requestDto);
			List<String> salesReturnInvoice = ltSalesreturnDao.getSalesOrderInvoiceNo(requestDto);
			//System.out.println("in method getInvoices service line 1224 ="+ new Date() + "this is datwe & time line 1224= "+d1);
			//System.out.println("Invocie no List = "+salesReturnInvoice + "List Size is "+salesReturnInvoice.size());
			
			//SoHeaderDto SoHeaderDto12 = ltSalesreturnDao.getSoHeaderData(salesReturnInvoice.get(0), requestDto);
			//System.out.println("Header id List = "+SoHeaderDto12);
			
			//List<SoHeaderDto> salesReturnResponse = ltSalesreturnDao.getSoHeaderDataFromInvoiceNo(salesReturnInvoice, requestDto);
			//List<SoHeaderDto> salesReturnResponse = ltSalesreturnDao.getSoHeaderDataFromInvoiceNo12(salesReturnInvoice.get(0), requestDto);               
			
			//System.out.println("Header data  fro invocie no = "+salesReturnResponse);
			//SoHeaderLineDto SoHeaderDto1 = new SoHeaderLineDto();
			SoHeaderDto SoHeaderDto1 = new SoHeaderDto();
			
			LtSoHeaderDto ltSoHeaderDto = new LtSoHeaderDto();
			List<SoHeaderDto> soHeaderDtoList = new ArrayList<SoHeaderDto>();	
			   		
			if(salesReturnInvoice!= null) {
			//	System.out.println("in method getInvoices service line 1240 ="+ new Date() + "this is datwe & time line 1240= "+d1);
			List<SoHeaderDto> SoHeaderDtoList = new ArrayList<SoHeaderDto>();	
			SoHeaderDtoList = ltSalesreturnDao.getSoHeaderDataNew(salesReturnInvoice, requestDto);
			List<SoLineDto> SoLineDataList = ltSalesreturnDao.getSoLineData(SoHeaderDtoList.get(0).getInvoiceNumber(), requestDto);
			String beatName1= null;
			List<SoHeaderDto>beatNameList =  ltSalesreturnDao.getBeatNameAgainstInvoiceNo(salesReturnInvoice.get(0));
				for(int i =0; i<salesReturnInvoice.size(); i++) 
				{
					SoHeaderDto	soHeaderDto = new SoHeaderDto();
					//SoHeaderLineDto soHeaderDto = new SoHeaderLineDto();
					
					//System.out.println("In loop for SoHeaderDto12222  " +SoHeaderDto12);
                   //comment on 21-may-24 for optimisation					
					  //SoHeaderDto1 = ltSalesreturnDao.getSoHeaderData(salesReturnInvoice.get(i), requestDto);
					final int ii = i;
				//	System.out.println("In loop for SoLineDataList  " +SoLineDataList);
					System.out.println("In loop for salesReturnInvoice.get(ii)  " +salesReturnInvoice.get(ii) +
							"in method getInvoices service"+ new Date() + "this is datwe & time "+d1);
					Optional<SoHeaderDto> soHeaderDto1= SoHeaderDtoList.stream().filter(x->x.getSiebelInvoicenumber().equalsIgnoreCase(salesReturnInvoice.get(ii))).findFirst();
				//	System.out.println("in method getInvoices service line 1259"+ new Date() + "this is datwe & time line 1259 "+d1);
              //     System.out.println("In loop for SoHeaderDto12  " +soHeaderDto1.get());
                   SoHeaderDto1 = soHeaderDto1.get();
                   
                   //System.out.println("In loop for SoHeaderDto12  " +SoHeaderDto1);
					
					//System.out.println("In loop for HeaderId "+SoHeaderDto12.getHeaderId());
					if(SoHeaderDto1== null) {
						continue;
					}
					
					//SoLineDto prodId= ltSalesreturnDao.getProductId(SoHeaderDto12.getSiebelInvoicenumber());
				
					
					//List<SoLineDto> SoLineData = ltSalesreturnDao.getSoLineData(SoHeaderDto1.getSiebelInvoicenumber(), requestDto);
					final Long soHeader= SoHeaderDto1.getHeaderId();
					List<SoLineDto> SoLineData= SoLineDataList.stream().filter(x->x.getHeaderId()==soHeader).collect(Collectors.toList());
					
					
					//List<SoLineDto> SoLineData = ltSalesreturnDao.getSoLineData(SoHeaderDto12.getHeaderId(), requestDto);
					//List<SoLineDto> SoLineData = ltSalesreturnDao.getSoLineData(salesReturnInvoice.get(i), requestDto);
				
					//SoHeaderLineDto SoHeaderDto1 = ltSalesreturnDao.getSoHeaderData(salesReturnInvoice.get(i), requestDto);
					
					//String invoiceNo =null;
					String beatName= null;
					//if(responseDto.getInvoiceNumber() !=null) {
						//invoiceNo = responseDto.getInvoiceNumber();
							
                        // beatName =  ltSalesreturnDao.getBeatNameAgainstInvoiceNo(salesReturnInvoice.get(i));
					final String invoiceNo= SoHeaderDto1.getSiebelInvoicenumber();
					//System.out.println("invoiceNo == " + invoiceNo);
					Optional<SoHeaderDto> soHeaderDtoList1 = beatNameList.stream().filter(x->x.getSiebelInvoicenumber().toString().equalsIgnoreCase(invoiceNo)).findFirst();
					System.out.println("beatNameList ====" + beatNameList);
					System.out.println("invoiceNo == " + invoiceNo);
					System.out.println("In loop for soHeaderDtoList1.get().getBeatName();  " +soHeaderDtoList1);
					if(soHeaderDtoList1.isPresent()) {
					beatName=soHeaderDtoList1.get().getBeatId();
					}
						//beatName = "AYYAPPANTHANGAL";
					//}
					 
					
					//System.out.println("beatName is =" + beatName);
					
					Long id1 = SoHeaderDto1.getHeaderId();
					soHeaderDto.setHeaderId(id1);
					soHeaderDto.setDistributorCode(SoHeaderDto1.getDistributorCode());
					soHeaderDto.setDistributorCode(SoHeaderDto1.getDistributorCode());
					soHeaderDto.setDistributorName(SoHeaderDto1.getDistributorName());
										
					soHeaderDto.setOutletCode(SoHeaderDto1.getOutletCode());
					soHeaderDto.setOutletName(SoHeaderDto1.getOutletName());
					
					soHeaderDto.setOrderNumber(SoHeaderDto1.getOrderNumber());
					soHeaderDto.setInvoiceNumber(SoHeaderDto1.getInvoiceNumber());
					soHeaderDto.setInvoiceDate(SoHeaderDto1.getInvoiceDate());
					//soHeaderDto.setOrderDate(SoHeaderDto1.getOrderDate().toString()); // as invoice date
					
					//soHeaderDto.setInventoryId(SoHeaderDto1.getInventoryId());
					//soHeaderDto.setLocation(SoHeaderDto1.getLocation());
					
					soHeaderDto.setPriceListName(SoHeaderDto1.getPriceListName());
					//soHeaderDto.setPriceList(SoHeaderDto1.getPriceList()); // as pricelist name
					
					//soHeaderDto.setBeatName(beatName.get(i));          //(SoHeaderDto1.getBeatName());
					//soHeaderDto.setBeatId(SoHeaderDto1.getBeatId()); // as Beat Name
					//soHeaderDto.setStatus(SoHeaderDto1.getStatus());
					
					if(SoLineData== null) {
						continue;
					}
					if(SoLineData.size()>0) {
					soHeaderDto.setInventoryId(SoLineData.get(0).getInventoryId());
					soHeaderDto.setLocation(SoLineData.get(0).getLocation());}
					
//					soHeaderDto.setBeatName(salesReturnHeaderData.getBeatName());
//					soHeaderDto.setOutletName(salesReturnHeaderData.getOutletName());
//					soHeaderDto.setPriceList(salesReturnHeaderData.getPriceList());
//									
					List<SoLineDto> SoLineDto1 = new ArrayList<SoLineDto>();
					
					for(int line =0; line<SoLineData.size(); line++) 
						{
						SoLineDto ltSoLineDto = new SoLineDto();
												
						Long id= SoLineData.get(line).getHeaderId();
						//System.out.println("In loop for LineId  " +id);
						ltSoLineDto.setHeaderId(id);	
						//ltSoLineDto.setProductId(SoLineData.get(line).getProductId());  // as product code 
						ltSoLineDto.setProductCode(SoLineData.get(line).getProductCode());
						ltSoLineDto.setProductName(SoLineData.get(line).getProductName());
						ltSoLineDto.setPtrPrice(SoLineData.get(line).getPtrPrice());
			     		//ltSoLineDto.setQuantity(SoLineData.get(line).getQuantity());
			     		ltSoLineDto.setShippedQuantity(SoLineData.get(line).getShippedQuantity());
						ltSoLineDto.setListPrice(SoLineData.get(line).getListPrice());
						ltSoLineDto.setPtrBasePrice(SoLineData.get(line).getPtrBasePrice());
						
						ltSoLineDto.setLocation(SoLineData.get(line).getLocation());
						ltSoLineDto.setInventoryId(SoLineData.get(line).getInventoryId());
//						ltSoLineDto.setShippedQuantity(SoLineData.get(line).getShippedQuantity());
//						ltSoLineDto.setStatus(SoLineData.get(line).getStatus());
//						ltSoLineDto.setTotalPrice(SoLineData.get(line).getTotalPrice());
						
						ltSoLineDto.setReturnQuantity(SoLineData.get(line).getReturnQuantity());
						//only return quantity not get
						
						SoLineDto1.add(ltSoLineDto);
										
					}
					soHeaderDto.setSoLineDtoList(SoLineDto1);;
					soHeaderDtoList.add(soHeaderDto);
				}
				System.out.println("in method getInvoices service line 1341 ="+ new Date() + "this is datwe & time line 1341= "+d1);
				ltSoHeaderDto.setSoHeaderDto(soHeaderDtoList);
				
				if(SoHeaderDto1!= null) {
					status.setCode(RECORD_FOUND);
					status.setMessage("Recod_Found_Successfully");
					status.setData(ltSoHeaderDto);  //(salesReturnDto);
				  }
				}else {
				     status.setCode(FAIL); 
				     status.setMessage("Recod_Not_Found");
				}
			}catch(Exception e) {
				   e.printStackTrace();
				 }
		System.out.println("in method getInvoices service line 1356 ="+ new Date() + "this is datwe & time line 1356= "+d1);
			return status;		
		}

	
	@Override
	public Status getInvoicePdfAgainstInvoiceNumber(RequestDto requestDto) throws ServerException {
		StringBuilder response = new StringBuilder();
		Status status = new Status();
		try {
			//Status status = new Status();
			
			String invoiceNum = ltSalesreturnDao.getInvoiceNumber(requestDto.getOrderNumber());
			System.out.println("invoiceNum" + invoiceNum);
			if(invoiceNum!= null) {
				System.out.println("Hi I.m insiebel Api call");
			String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/InvoicePdf?matchrequestformat=y";
			String method = "POST";
	        String contentType = "Content-Type: application/json";
	        //String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="; need to check
			
	        JSONObject siebelReq = new JSONObject();
	        //siebelReq.put("InvoiceNum",requestDto.getInvoiceNumber());
	        siebelReq.put("InvoiceNum",invoiceNum);
	        
	        String jsonPayload =siebelReq.toString();
            System.out.println(siebelReq);     //(requestBody);
            
	        URL obj = new URL(url);
	        System.out.println("url is..... = "+ url);
	        // Add this line before opening the connection
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

	        	// Create HttpURLConnection object
	            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	            // Set request method
	            con.setRequestMethod(method);
	            // Set request headers
	           /*con.setRequestProperty("Content-Type", "application/json");
	            con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="); //need to check
	            // Enable output and set request body
	            */
	            con.setRequestProperty("Content-Type", "application/json");
	            String username = "VINAY.KUMAR6";
	            String password = "Welcome1";
	            String auth = username + ":" + password;
	            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
	            String authHeaderValue = "Basic " + new String(encodedAuth);
	            System.out.println("This is user authHeaderValue"+authHeaderValue);
	            con.setRequestProperty("Authorization", authHeaderValue);
	            
	            con.setDoOutput(true);
	            try (OutputStream os = con.getOutputStream()) {
	                byte[] input = jsonPayload.getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }

	         // Get response code
	            int responseCode = con.getResponseCode();
	            String msg = con.getResponseMessage();
	            System.out.println("Response Code : " + responseCode);
	            System.out.println("Response Message : " + msg);
	            
	         // Read the response body
	            //StringBuilder response = new StringBuilder();
	            BufferedReader reader;
	            InputStream inputStream;
	            if(responseCode >= 200 && responseCode < 300 ) {
	            	inputStream = con.getInputStream();
	            	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	                String line;
	              while ((line = reader.readLine()) != null) {
	            	  System.out.println("line success response is="+line);
	                  response.append(line);
	                  System.out.println("success response is = " + response);
	              }
	              reader.close();
	      
//	               Show the response
	              System.out.println("Response Body: " + response.toString());

	            }else {
	            	     reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	                     String line;
	                       while ((line = reader.readLine()) != null) 
	                     {
	          	            System.out.println("line error response is="+line);
	                        response.append(line);
	                     }        
	            	     inputStream = con.getErrorStream();
	            	     System.out.println("Error response: " + responseCode + " - " + msg);
	            	     System.out.println("Error Response Body: " + response);
	            	 }
                   
	         // saving response details in to table 
	            String resCode= Integer.toString(responseCode);
	            //tableNameToStore.setSiebelStatus(resCode);
	            String res= response.toString();
	            //tableNameToStore.setSiebelRemark(res);
	            
	           // Create an ObjectMapper instance
	           ObjectMapper objectMapper = new ObjectMapper();
	           
	           // Parse the response body into a JSON object
	           JsonNode rootNode = objectMapper.readTree(response.toString());
	           // Access the "Invoice Number" field from the JSON object
	           if(rootNode!= null && responseCode ==200) {
	           //String invoiceNumber = rootNode.get("Invoice Number").asText();
	           // Now you can use the invoiceNumber variable as needed
	           //System.out.println("Invoice Number: " + invoiceNumber);
	                 
	          // tableNameToStore.setSiebelInvoiceNumber(invoiceNumber);
	    		}
	        	
/*	         //saving siebel response & status code in to table
		        
		        LtSalesReturnHeader ltSalesReturnHeader = new LtSalesReturnHeader();
		        //String resCode = Integer.toString(responseCode);
		        //System.out.println("Save Response before Body: " + resCode);
		        //ltSalesReturnHeader.setSiebelStatus(resCode);
		        //System.out.println("Save Response after Body: " + resCode);

		        //String res = response.toString();
		        //System.out.println("Save Response before Body: " + res);
		        ltSalesReturnHeader.setSiebelRemark(res);
		        //System.out.println("Save Response after Body: " + res);
		        
		        ltSalesReturnHeader.setSiebelJsonpayload(jsonPayload);
                  
		        ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
*/
	         
	       /* 	if(response!=null) {
					status.setCode(RECORD_FOUND);
					status.setMessage("RECORD_FOUND");
					status.setData(response);
				}else {
					 status.setCode(RECORD_NOT_FOUND);
					}*/
	           status.setStringBuilder(response);
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		return status;
	}

	@Override
	public Status getLotNumber(String prodId, String inventId) throws ServerException {
		Status status= new Status();
		try {
			List<LtSalesReturnAvailability> lotNumber = ltSalesreturnDao.getLotNumber(prodId, inventId);
			if(!lotNumber.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setMessage("RECORD_FOUND");
				status.setData(lotNumber);
			 }else {
				status.setCode(RECORD_NOT_FOUND);
			  }
			} catch(Exception e) 
		      {e.printStackTrace();}
		return status;
	}

	@Override
	public Status getSalesReturnForPendingAprroval(RequestDto requestDto) throws ServerException {
		Status status = new Status();
		try {
			
			System.out.println("requestDto == " + requestDto);
			List<Long> salesReturnHeaderId = ltSalesreturnDao.getSalesReturnHeaderId(requestDto);
			System.out.println("salesReturnHeaderId == " + salesReturnHeaderId +"size is = "+salesReturnHeaderId.size());
			if( salesReturnHeaderId!= null && !salesReturnHeaderId.isEmpty() ) {
			//System.out.println("sales Return Header Id ==-------== "+salesReturnHeaderId +"Size is "+ salesReturnHeaderId.size());
			List<LtSalesReturnResponseDto> salesReturnResponse = ltSalesreturnDao.getSalesReturnForPendingAprroval(salesReturnHeaderId);
			System.out.println("sales Return Response **==*** "+salesReturnResponse);
               
			SalesReturnDto salesReturnDto = new SalesReturnDto();
			List<LtSalesReturnHeaderDto> ltSalesReturnHeaderList = new ArrayList<LtSalesReturnHeaderDto>();
			//int i=0;
			//for(Long ids:salesReturnHeaderId)
			
			
			for(int i =0; i<salesReturnHeaderId.size(); i++) 
			{
				System.out.println("I'm in for loop 11111111111");
				System.out.println("Hiiii" +i);
                   //i = Long.valueOf(ids).intValue();
				LtSalesReturnHeaderDto	ltSalesReturnHeaderDto = new LtSalesReturnHeaderDto();
				
				List<LtSalesReturnLineDto> salesReturnLineData = ltSalesreturnDao.getSalesReturnLineData(salesReturnHeaderId.get(i), requestDto);
				System.out.println("sales Return Line Data ==-------== "+salesReturnLineData);
				
				//List<LtSalesReturnResponseDto> salesReturnHeaderData = ltSalesreturnDao.getSalesReturnForPendingAprroval1(salesReturnHeaderId.get(i));
				LtSalesReturnResponseDto salesReturnHeaderData = ltSalesreturnDao.getSalesReturnForPendingAprroval1(salesReturnHeaderId.get(i), requestDto);
				
				System.out.println("sales Return Header Data ==-------== "+salesReturnHeaderData);
				Long id1 = salesReturnHeaderData.getSalesReturnHeaderId();
				//System.out.println("sales Return Header IDDDDDDDDDD" + id1);
				//if(id1!= null) {
				ltSalesReturnHeaderDto.setSalesReturnHeaderId(id1);//}
				ltSalesReturnHeaderDto.setSalesReturnNumber(salesReturnHeaderData.getSalesReturnNumber());
				
				ltSalesReturnHeaderDto.setInvoiceNumber(salesReturnHeaderData.getInvoiceNumber());
				ltSalesReturnHeaderDto.setOutletId(salesReturnHeaderData.getOutletId());
				ltSalesReturnHeaderDto.setReturnStatus(salesReturnHeaderData.getReturnStatus());
				ltSalesReturnHeaderDto.setReturnReason(salesReturnHeaderData.getReturnReason());
				ltSalesReturnHeaderDto.setStatus(salesReturnHeaderData.getStatus());
				ltSalesReturnHeaderDto.setSalesReturnDate(salesReturnHeaderData.getSalesReturnDate());
				ltSalesReturnHeaderDto.setBeatName(salesReturnHeaderData.getBeatName());
				ltSalesReturnHeaderDto.setOutletName(salesReturnHeaderData.getOutletName());
				ltSalesReturnHeaderDto.setPriceList(salesReturnHeaderData.getPriceList());
								
				List<LtSalesReturnLineDto> ltSalesReturnLineDto1 = new ArrayList<LtSalesReturnLineDto>();
				//int line=0;
				//System.out.println("I'm in for loop above 2222222222");
				//for(LtSalesReturnLineDto ltSalesReturnLineDto:salesReturnLineData)
				for(int line =0; line<salesReturnLineData.size(); line++) 
					{
					LtSalesReturnLineDto ltSalesReturnLineDto = new LtSalesReturnLineDto();
					
					//System.out.println("salesssss ==-------== "+salesReturnLineData.get(line).getSalesReturnHeaderId());
					
					String productName = ltSalesreturnDao.getProdNameFromProdId(salesReturnLineData.get(line).getProductId());
					
					//System.out.println("I'm in for loop 22222222");
					Long id= salesReturnLineData.get(line).getSalesReturnHeaderId();
					ltSalesReturnLineDto.setSalesReturnHeaderId(id);	
				  //ltSalesReturnLineDto.setProductId(salesReturnLineData.get(line).getProductId());
					ltSalesReturnLineDto.setProductId(productName);   
					ltSalesReturnLineDto.setReturnQuantity(salesReturnLineData.get(line).getReturnQuantity());
					ltSalesReturnLineDto.setAvailability(salesReturnLineData.get(line).getAvailability());
					ltSalesReturnLineDto.setLotNumber(salesReturnLineData.get(line).getLotNumber());
					ltSalesReturnLineDto.setRemainingQuantity(salesReturnLineData.get(line).getRemainingQuantity());
					ltSalesReturnLineDto.setLocation(salesReturnLineData.get(line).getLocation());
					ltSalesReturnLineDto.setShippedQuantity(salesReturnLineData.get(line).getShippedQuantity());
					ltSalesReturnLineDto.setStatus(salesReturnLineData.get(line).getStatus1());
					ltSalesReturnLineDto.setTotalPrice(salesReturnLineData.get(line).getTotalPrice());
					
					ltSalesReturnLineDto1.add(ltSalesReturnLineDto);
									
				}
				ltSalesReturnHeaderDto.setLtSalesReturnLineDto(ltSalesReturnLineDto1);
				ltSalesReturnHeaderList.add(ltSalesReturnHeaderDto);
			}
			salesReturnDto.setLtSalesReturnHeaderDto(ltSalesReturnHeaderList);
			
			
			
/*			Map<String, LtSalesReturnHeaderDto> ltSalesReturnHeaderDtoMap = new LinkedHashMap<String, LtSalesReturnHeaderDto>();
			Map<String, List<LtSalesReturnLineDto>> ltSalesReturnLineDtoMap = new LinkedHashMap<String, List<LtSalesReturnLineDto>>();
			
			System.out.println("sales Return Response Size is **==*** "+salesReturnResponse.size());
			
			for (Iterator iterator = salesReturnResponse.iterator(); iterator.hasNext();) {
				LtSalesReturnResponseDto responseDto = (LtSalesReturnResponseDto) iterator.next();

				LtSalesReturnLineDto ltSalesReturnLineDto = new LtSalesReturnLineDto();

				if (responseDto.getProductId()!= null) {
					ltSalesReturnLineDto.setProductId(responseDto.getProductId());
				}
				if(responseDto.getShippedQuantity()!= null) {
					ltSalesReturnLineDto.setShippedQuantity(responseDto.getShippedQuantity());
				}
				if(responseDto.getReturnQuantity() !=null) {
					ltSalesReturnLineDto.setReturnQuantity(responseDto.getReturnQuantity());
				}
				if(responseDto.getAvailability() !=null) {
					ltSalesReturnLineDto.setAvailability(responseDto.getAvailability());
				}
				if(responseDto.getStatus1() !=null) {
					ltSalesReturnLineDto.setStatus(responseDto.getStatus1());
				}
				if(responseDto.getLocation() !=null) {
					ltSalesReturnLineDto.setLocation(responseDto.getLocation());
				}
				if(responseDto.getRemainingQuantity() !=null) {
					ltSalesReturnLineDto.setRemainingQuantity(responseDto.getRemainingQuantity());
				}
				if(responseDto.getPrice() !=null) {
					ltSalesReturnLineDto.setPrice(responseDto.getPrice());
				}
				if(responseDto.getTotalPrice() !=null) {
					ltSalesReturnLineDto.setTotalPrice(responseDto.getTotalPrice());
				}
				if(responseDto.getLotNumber() !=null) {
					ltSalesReturnLineDto.setLotNumber(responseDto.getLotNumber());
				}
				
//				if(ltSalesReturnLineDtoMap.get(responseDto.getProductId()) !=null) {
//					List<LtSalesReturnLineDto> salesReturnLineDtoList = ltSalesReturnLineDtoMap.get(responseDto.getProductId());
//					if(ltSalesReturnLineDto.getProductId() != null) {
//						salesReturnLineDtoList.add(ltSalesReturnLineDto);
//					}
//					ltSalesReturnLineDtoMap.put(responseDto.getProductId(), salesReturnLineDtoList);
//				}
				if(ltSalesReturnLineDtoMap.get(responseDto.getSalesReturnHeaderId())!= null) {
					List<LtSalesReturnLineDto> salesReturnLineDtoList = ltSalesReturnLineDtoMap.get(responseDto.getSalesReturnHeaderId());
					if(ltSalesReturnLineDto.getSalesReturnHeaderId() != null) {
						salesReturnLineDtoList.add(ltSalesReturnLineDto);
					}
					ltSalesReturnLineDtoMap.put(responseDto.getProductId(), salesReturnLineDtoList);
				}
				else {
					

					// add data into salesReturnHeader map
					LtSalesReturnHeaderDto salesReturnHeaderDto = new LtSalesReturnHeaderDto();
					
													
					salesReturnHeaderDto.setSalesReturnHeaderId(responseDto.getSalesReturnHeaderId());
					salesReturnHeaderDto.setSalesReturnNumber(responseDto.getSalesReturnNumber());
					salesReturnHeaderDto.setInvoiceNumber(responseDto.getInvoiceNumber());
					salesReturnHeaderDto.setOutletId(responseDto.getOutletId());
					salesReturnHeaderDto.setReturnStatus(responseDto.getReturnStatus());
					salesReturnHeaderDto.setReturnReason(responseDto.getReturnReason());
					salesReturnHeaderDto.setStatus(responseDto.getStatus());
					salesReturnHeaderDto.setSalesReturnDate(responseDto.getSalesReturnDate());
					salesReturnHeaderDto.setBeatName(responseDto.getBeatName());
					salesReturnHeaderDto.setOutletName(responseDto.getOutletName());
					salesReturnHeaderDto.setPriceList(responseDto.getPriceList());	
					
					ltSalesReturnHeaderDtoMap.put(responseDto.getInvoiceNumber(), salesReturnHeaderDto);

					List<LtSalesReturnLineDto> salesReturnLineDto = new ArrayList<LtSalesReturnLineDto>();
					
					//List<LtInvoiceDetailsLineDto> invoiceLineDtoList = invoiceDetailsLineDtoMap.get(responseDto.getProductCode());
					if(ltSalesReturnLineDto.getProductId() != null) {
						salesReturnLineDto.add(ltSalesReturnLineDto);
					}
					ltSalesReturnLineDtoMap.put(responseDto.getInvoiceNumber(), salesReturnLineDto);
					
				}
			}
			
			SalesReturnDto salesReturnDto = new SalesReturnDto();

			//System.out.println("sales Return Response11111 == "+salesReturnResponse);
			//System.out.println("sales Return Dto111 == "+salesReturnDto);
			//System.out.println("lt Sales Return Header Dto Map" + ltSalesReturnHeaderDtoMap.size() + ltSalesReturnHeaderDtoMap) ;
			//System.out.println("sales Return Response Size is **==*** "+salesReturnResponse.size());
			List<LtSalesReturnHeaderDto> ltSalesReturnHeaderDto = new ArrayList<LtSalesReturnHeaderDto>();

			for (Map.Entry<String, LtSalesReturnHeaderDto> entry : ltSalesReturnHeaderDtoMap.entrySet()) {
				LtSalesReturnHeaderDto salesReturnHeaderDto = entry.getValue();

				List<LtSalesReturnLineDto> ltSalesReturnLineDto = ltSalesReturnLineDtoMap.get(entry.getKey());
				salesReturnHeaderDto.setLtSalesReturnLineDto(ltSalesReturnLineDto);

				ltSalesReturnHeaderDto.add(salesReturnHeaderDto);
			}
			salesReturnDto.setLtSalesReturnHeaderDto(ltSalesReturnHeaderDto);

			System.out.println("sales Return Response == "+salesReturnResponse);
			System.out.println("sales Return Dto == "+salesReturnDto);
*/						
			if(!salesReturnResponse.isEmpty()) {
				status.setCode(RECORD_FOUND);
				status.setMessage("Recod_Found_Successfully");
				status.setData(salesReturnDto);  //(salesReturnDto);
			}}else {
			     status.setCode(FAIL); 
			     status.setMessage("Recod_Not_Found");
			}
			
		}catch(Exception e) {
			   e.printStackTrace();
			 }
		return status;
	}

	@Override
	public StringBuilder getInvoicePdfAgainstInvoiceNumber1(RequestDto requestDto) throws ServerException {
		StringBuilder response = new StringBuilder();
		Status status = new Status();
		try {
			//Status status = new Status();
			
			String invoiceNum = ltSalesreturnDao.getInvoiceNumber(requestDto.getOrderNumber());
			System.out.println("invoiceNum" + invoiceNum);
			if(invoiceNum!= null) {
				//if(invoiceNum.isPresent()){
				System.out.println("Hi I.m insiebel Api call");
			String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/InvoicePdf?matchrequestformat=y";
			String method = "POST";
	        String contentType = "Content-Type: application/json";
	        //String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="; need to check
			
	        JSONObject siebelReq = new JSONObject();
	        //siebelReq.put("InvoiceNum",requestDto.getInvoiceNumber());
	        siebelReq.put("InvoiceNum",invoiceNum);
	        
	        String jsonPayload =siebelReq.toString();
            System.out.println(siebelReq);     //(requestBody);
            
	        URL obj = new URL(url);
	        System.out.println("url is..... = "+ url);
	        // Add this line before opening the connection
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

	        	// Create HttpURLConnection object
	            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	            // Set request method
	            con.setRequestMethod(method);
	            // Set request headers
	           /*con.setRequestProperty("Content-Type", "application/json");
	            con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="); //need to check
	            // Enable output and set request body
	            */
	            con.setRequestProperty("Content-Type", "application/json");
	            String username = "VINAY.KUMAR6";
	            String password = "Welcome1";
	            String auth = username + ":" + password;
	            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
	            String authHeaderValue = "Basic " + new String(encodedAuth);
	            System.out.println("This is user authHeaderValue"+authHeaderValue);
	            con.setRequestProperty("Authorization", authHeaderValue);
	            
	            con.setDoOutput(true);
	            try (OutputStream os = con.getOutputStream()) {
	                byte[] input = jsonPayload.getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }

	         // Get response code
	            int responseCode = con.getResponseCode();
	            String msg = con.getResponseMessage();
	            System.out.println("Response Code : " + responseCode);
	            System.out.println("Response Message : " + msg);
	            
	         // Read the response body
	            //StringBuilder response = new StringBuilder();
	            BufferedReader reader;
	            InputStream inputStream;
	            if(responseCode >= 200 && responseCode < 300 ) {
	            	inputStream = con.getInputStream();
	            	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	                String line;
	              while ((line = reader.readLine()) != null) {
	            	  System.out.println("line success response is="+reader + "\n"+line);
	                  response.append(line);
	                  System.out.println("success response is = " + response);
	              }
	              reader.close();
	      
//	               Show the response
	              System.out.println("Response Body: " + response.toString());

	            }else {
	            	     reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	                     String line;
	                       while ((line = reader.readLine()) != null) 
	                     {
	          	            System.out.println("line error response is="+line);
	                        response.append(line);
	                     }        
	            	     inputStream = con.getErrorStream();
	            	     System.out.println("Error response: " + responseCode + " - " + msg);
	            	     System.out.println("Error Response Body: " + response);
	            	 }
                   
	         // saving response details in to table 
	            String resCode= Integer.toString(responseCode);
	            //tableNameToStore.setSiebelStatus(resCode);
	            String res= response.toString();
	            //tableNameToStore.setSiebelRemark(res);
	            
	           // Create an ObjectMapper instance
	           ObjectMapper objectMapper = new ObjectMapper();
	           
	           // Parse the response body into a JSON object
	           JsonNode rootNode = objectMapper.readTree(response.toString());
	           // Access the "Invoice Number" field from the JSON object
	           if(rootNode!= null && responseCode ==200) {
	           //String invoiceNumber = rootNode.get("Invoice Number").asText();
	           // Now you can use the invoiceNumber variable as needed
	           //System.out.println("Invoice Number: " + invoiceNumber);
	                 
	          // tableNameToStore.setSiebelInvoiceNumber(invoiceNumber);
	    		}
	        	
/*	         //saving siebel response & status code in to table
		        
		        LtSalesReturnHeader ltSalesReturnHeader = new LtSalesReturnHeader();
		        //String resCode = Integer.toString(responseCode);
		        //System.out.println("Save Response before Body: " + resCode);
		        //ltSalesReturnHeader.setSiebelStatus(resCode);
		        //System.out.println("Save Response after Body: " + resCode);

		        //String res = response.toString();
		        //System.out.println("Save Response before Body: " + res);
		        ltSalesReturnHeader.setSiebelRemark(res);
		        //System.out.println("Save Response after Body: " + res);
		        
		        ltSalesReturnHeader.setSiebelJsonpayload(jsonPayload);
                  
		        ltSalesReturnHeader = updateSalesReturnHeader(ltSalesReturnHeader);
*/
	         
	       /* 	if(response!=null) {
					status.setCode(RECORD_FOUND);
					status.setMessage("RECORD_FOUND");
					status.setData(response);
				}else {
					 status.setCode(RECORD_NOT_FOUND);
					}*/
	           //status.setStringBuilder(response);
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		return response;
	}


	@Override
	public Status getSalesReturnOrderAgainstReturnOrderNo(String returnOrderNo) throws ServerException {
		Status status = new Status();  
		try {
               //SalesReturnApproval salesReturnApproval = new SalesReturnApproval();
              //List<LtSalesReturnResponseDto> ltSalesReturnResponseDto = new ArrayList<LtSalesReturnResponseDto>();
              //List<LtSalesReturnHeaderDto> ltSalesReturnHeaderDto = new ArrayList<LtSalesReturnHeaderDto>();
              LtSalesReturnHeaderDto ltSalesReturnHeaderDto = new LtSalesReturnHeaderDto();
              System.out.println("returnOrderNo is"+returnOrderNo);
              ltSalesReturnHeaderDto = ltSalesreturnDao.getSalesReturnOrderAgainstReturnOrderNo(returnOrderNo);
              //ltSalesReturnHeaderDto.get(0);
              List<LtSalesReturnLineDto>  LtSalesReturnResponseDto1 = ltSalesreturnDao.getSalesReturnOrderLineData(ltSalesReturnHeaderDto.getSalesReturnHeaderId());
              
              List<LtSalesReturnLineDto>  salesLineList = new ArrayList<LtSalesReturnLineDto>();
              
              for(int i=0; i<LtSalesReturnResponseDto1.size(); i++ ) {
            	
            	  LtSalesReturnLineDto LtSalesReturnLineDto = new LtSalesReturnLineDto();
            	  
            	  LtSalesReturnLineDto.setProductId(LtSalesReturnResponseDto1.get(i).getProductId());
            	  LtSalesReturnLineDto.setShippedQuantity(LtSalesReturnResponseDto1.get(i).getShippedQuantity());
            	  LtSalesReturnLineDto.setReturnQuantity(LtSalesReturnResponseDto1.get(i).getReturnQuantity());
            	  LtSalesReturnLineDto.setRemainingQuantity(LtSalesReturnResponseDto1.get(i).getRemainingQuantity());
            	  LtSalesReturnLineDto.setAvailability(LtSalesReturnResponseDto1.get(i).getAvailability());
            	  LtSalesReturnLineDto.setPrice(LtSalesReturnResponseDto1.get(i).getPrice());
            	  LtSalesReturnLineDto.setTotalPrice(LtSalesReturnResponseDto1.get(i).getTotalPrice());
            	  LtSalesReturnLineDto.setLotNumber(LtSalesReturnResponseDto1.get(i).getLotNumber());
            	  LtSalesReturnLineDto.setStatus1(LtSalesReturnResponseDto1.get(i).getStatus1());
            	  
            	  salesLineList.add(LtSalesReturnLineDto);
              }
              
              //LtSalesReturnHeaderDto ltSalesReturnHeaderDto1 = new LtSalesReturnHeaderDto();
              ltSalesReturnHeaderDto.setLtSalesReturnLineDto(salesLineList);
              
              
             //ltSalesReturnHeaderDto.add(ltSalesReturnHeaderDto1);
              
              //SalesReturnDto salesReturnDto = new SalesReturnDto();
              //salesReturnDto.setLtSalesReturnHeaderDto(ltSalesReturnHeaderDto);
              
              System.out.println("ltSalesReturnResponseDto is"+ltSalesReturnHeaderDto);
              if(ltSalesReturnHeaderDto!= null) 
                {
            	   status.setCode(SUCCESS);
            	   status.setMessage("RECORD FOUND SUCCESSFULLY");
            	   status.setData(ltSalesReturnHeaderDto); //salesReturnDto
                }else 
                 {
            	   status.setCode(FAIL);
            	   status.setMessage("RECORD NOT FOUND");
            	   return status;
                 }
              }catch(Exception e) 
                 {
            	   e.printStackTrace();
                 }
		return status;
	}
	
	
}
