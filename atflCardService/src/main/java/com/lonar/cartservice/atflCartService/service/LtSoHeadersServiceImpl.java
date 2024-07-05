package com.lonar.cartservice.atflCartService.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.rowset.OracleCachedRowSet;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

import java.sql.Connection;
import java.sql.Statement;

//import com.lonar.cartservice.atflCartService.service.EmailService;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.controller.WebController;
import com.lonar.cartservice.atflCartService.dao.LtSoHeadersDao;
import com.lonar.cartservice.atflCartService.dto.DistributorDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtOrderLineDataGt;
import com.lonar.cartservice.atflCartService.dto.OrderDetailsDto;
import com.lonar.cartservice.atflCartService.dto.QuantityCheck;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.ResponsePendingOrdersDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDtoPendingOrders;
import com.lonar.cartservice.atflCartService.dto.SoLineDto;
import com.lonar.cartservice.atflCartService.dto.SoLineDtoPendingOrders;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtMastOutles;

//import com.lonar.cartservice.atflCartService.model.LtMastOutlets;

import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtOrderCancellationReason;
import com.lonar.cartservice.atflCartService.model.LtSalesPersonLocation;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.repository.LtMastOutletRepository;
import com.lonar.cartservice.atflCartService.repository.LtSalesPersonLocationRepository;
import com.lonar.cartservice.atflCartService.repository.LtSoHeadersRepository;
import com.lonar.cartservice.atflCartService.repository.LtSoLinesRepository;

import oracle.jdbc.OracleTypes;


@Service
@PropertySource(value= "classpath:queries/cartMasterQueries.properties", ignoreResourceNotFound = true)
public class LtSoHeadersServiceImpl implements LtSoHeadersService, CodeMaster {

	@Autowired
	LtSoHeadersDao ltSoHeadersDao;

	@Autowired
	LtSoLinesRepository ltSoLinesRepository;

	@Autowired
	private LtSoHeadersRepository ltSoHeadersRepository;
	
	@Autowired
	LtMastOutletRepository ltMastOutletRepository;
	
    @Autowired
    LtSalesPersonLocationRepository ltSalesPersonLocationRepository;
	
	@Autowired
	WebController webController;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
    JavaMailSender emailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(LtSoHeadersServiceImpl.class);

	@Override 
	public Status saveOrder(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
		
		Status status = new Status();
		try {
			if (soHeaderDto.getOutletId() != null) {
				LtSoHeaders ltSoHeader = ltSoHeadersDao.checkHeaderStatusIsDraft(soHeaderDto.getOutletId());
				LtSoHeaders checkOrder = null;
				if (soHeaderDto.getOrderNumber() != null && soHeaderDto.getOrderNumber() != "") {
					checkOrder = ltSoHeadersDao.checkOrderStatus(soHeaderDto.getOrderNumber());
				}

				if (ltSoHeader == null && checkOrder == null && !soHeaderDto.getSoLineDtoList().isEmpty()) {
					// Save header in draft status with line and Line data is not empty
					status = saveSoHeadeLineInDraft(soHeaderDto);
					return status;
				} else if(ltSoHeader != null && ltSoHeader.getStatus().equalsIgnoreCase(DRAFT) && soHeaderDto.getOrderNumber() == null){
					// Status Draft and Order Number null
					status.setCode(FAIL);
					status.setMessage("DB status draft and order number null");
					return status;
				}else if(ltSoHeader != null && ltSoHeader.getStatus().equalsIgnoreCase(DRAFT) && soHeaderDto.getSoLineDtoList().isEmpty()){
					// Status Draft and NO LINE DATA
					//#### Delete Header confirm ??
					//deleteSoHeaderLineEmpty(ltSoHeader.getHeaderId()); // 13 Aug 2020 Comment
					ltSoHeadersDao.deleteLineDataByHeaderId(ltSoHeader.getHeaderId());
					status.setCode(FAIL);
					status.setMessage("DB status draft and no line data against header");
					return status;
				}else if(soHeaderDto.getSoLineDtoList().isEmpty()) {
					// No line data against header for any status
					if(ltSoHeader.getHeaderId()!= null) {
						//deleteSoHeaderLineEmpty(ltSoHeader.getHeaderId());
						ltSoHeadersDao.deleteLineDataByHeaderId(ltSoHeader.getHeaderId());
					}
					status.setCode(FAIL);
					status.setMessage("No line data against header");
					return status;
				}
				else {
					synchronized(this){
						status = updateSoHeadeLineInDraft(soHeaderDto, checkOrder.getHeaderId(), checkOrder.getCreationDate(), checkOrder.getCreatedBy());
						return status;
					}
				}
			} else {
				status.setCode(FAIL);
				status.setMessage("Outlet id not found");
			}
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void deleteSoHeaderLineEmpty(Long headerId) {
		ltSoHeadersRepository.deleteById(headerId);
	}
	
	private Status updateSoHeadeLineInDraft(SoHeaderDto soHeaderDto, Long headerId, Date creationDate, Long createdBy)
			throws ServiceException, IOException {
		
		int lineDeleteStatus = ltSoHeadersDao.deleteLineDataByHeaderIdAndReturnStatus(headerId);
		
		
		Status status = new Status();
		LtSoHeaders ltSoHeader = new LtSoHeaders();
		
		String mobileNumber = ltSoHeadersDao.getMobileNumber(soHeaderDto.getUserId());

		ltSoHeader.setHeaderId(headerId);
		ltSoHeader.setOutletId(soHeaderDto.getOutletId());
		if (soHeaderDto.getOrderNumber() != null) {
			ltSoHeader.setOrderNumber(soHeaderDto.getOrderNumber());
		}
		if (soHeaderDto.getDeliveryDate() != null) {
			ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
		}
		if (soHeaderDto.getLatitude() != null) {
			ltSoHeader.setLatitude(soHeaderDto.getLatitude());
		}
		if (soHeaderDto.getLongitude() != null) {
			ltSoHeader.setLongitude(soHeaderDto.getLongitude());
		}
		
		ltSoHeader.setCreatedBy(createdBy);
		
		if (soHeaderDto.getUserId() != null) {
			ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
		}
//		if (soHeaderDto.getUserId() != null) {
//			ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
//		}
		if (soHeaderDto.getAddress() != null) {
			ltSoHeader.setAddress(soHeaderDto.getAddress());
		}
		
		if(mobileNumber.equalsIgnoreCase("8857885605") && soHeaderDto.getStatus().equalsIgnoreCase("APPROVED")) {
			ltSoHeader.setStatus("DELIVERED");
		}else {
			if (soHeaderDto.getStatus() != null) {
				ltSoHeader.setStatus(soHeaderDto.getStatus());
			}	
		}
		
		if (soHeaderDto.getRemark() != null) {
			ltSoHeader.setRemark(soHeaderDto.getRemark());
		}
		
		if (soHeaderDto.getCustomerId() != null) {
			ltSoHeader.setCustomerId(soHeaderDto.getCustomerId());
		}
		
		ltSoHeader.setCreationDate(creationDate); //(new Date());
		ltSoHeader.setOrderDate(creationDate);  // order date also same as creation date for update// (new Date());
		ltSoHeader.setLastUpdateDate(new Date()); // DateTimeClass.getCurrentDateTime()

		LtSoHeaders ltSoHeaderUpdated = updateSoHeader(ltSoHeader);
		
		if(ltSoHeaderUpdated.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
			sendNotifications(ltSoHeaderUpdated);
		}
		
		List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
		
		StringBuffer strQuery1 =new StringBuffer();
		strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
		StringBuffer strQuery =  new StringBuffer();
		if(lineDeleteStatus >= 0) 
			{
				
				for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
					
					SoLineDto soLineDto = (SoLineDto) iterator.next();
					
					strQuery.append("(");
					
					//LtSoLines ltSoLines = new LtSoLines();
		
					if (ltSoHeaderUpdated.getHeaderId() != null) {
						//ltSoLines.setHeaderId(ltSoHeaderUpdated.getHeaderId());
						strQuery.append(ltSoHeaderUpdated.getHeaderId()+",");
					}
					if (soLineDto.getProductId() != null) {
						//ltSoLines.setProductId(soLineDto.getProductId());
						strQuery.append(soLineDto.getProductId()+",");
					}
					if (soLineDto.getQuantity() != null) {
						//ltSoLines.setQuantity(soLineDto.getQuantity());
						strQuery.append(soLineDto.getQuantity()+",");
					}
					if (soLineDto.getListPrice() != null) {
						//ltSoLines.setListPrice(soLineDto.getListPrice());
						strQuery.append("'"+soLineDto.getListPrice()+"',");
					}
					if (soLineDto.getDeliveryDate() != null) {
						//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
						strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
					}
					
					if (soLineDto.getStatus() != null) {
						//ltSoLines.setStatus(soLineDto.getStatus());
						strQuery.append("'"+soLineDto.getStatus()+"',");//Status
					}
					
					if (soHeaderDto.getUserId() != null) {
						//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
						strQuery.append(soHeaderDto.getUserId()+",");
					}
					
					strQuery.append("'"+new Date()+"',");//Created Date
					
					if (soHeaderDto.getUserId() != null) {
						//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
						strQuery.append(soHeaderDto.getUserId()+",");
					}
					
					if (soHeaderDto.getUserId() != null) {
						//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
						strQuery.append(soHeaderDto.getUserId()+",");
					}
					
					strQuery.append("'"+new Date()+"',");//Last update date
					
					if (soLineDto.getPtrPrice() != null) {
						//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
						strQuery.append("'"+soLineDto.getPtrPrice()+"'");
					}else {
						//ltSoLines.setPtrPrice(soLineDto.getListPrice());
						strQuery.append("'"+soLineDto.getPtrPrice()+"'");
					}
					if (soLineDto.getPriceList() != null) {
						soLineDto.setPriceList(soLineDto.getPriceList());
					}
					strQuery.append("),");
					//ltSoLines.setLastUpdateDate(new Date());
					//ltSoLines.setCreationDate(new Date());
					//ltSoLines = ltSoLinesRepository.save(ltSoLines);
				}
			}
		strQuery.deleteCharAt(strQuery.length()-1); 
		strQuery1 = strQuery1.append(strQuery);
		
		String query =strQuery1.toString();
		int n = ltSoHeadersDao.insertLine(query);
		
		if (n > 0) {
			RequestDto requestDto = new RequestDto();
			requestDto.setHeaderId(ltSoHeaderUpdated.getHeaderId());
			requestDto.setOrderNumber(ltSoHeaderUpdated.getOrderNumber());
			if(ltSoHeader.getCustomerId() != null) {
				requestDto.setCustomerId(ltSoHeader.getCustomerId());
			}
			requestDto.setOffset(0);
			
			status = getOrderV1(requestDto);
			
			OrderDetailsDto orderDetailsDtoStatus=(OrderDetailsDto) status.getData();
			orderDetailsDtoStatus.getSoHeaderDto().get(0).setStatus(ltSoHeaderUpdated.getStatus());
			if(ltSoHeader.getAddress() != null) {
				orderDetailsDtoStatus.getSoHeaderDto().get(0).setAddress(ltSoHeader.getAddress());
			}
			status.setData(orderDetailsDtoStatus);
			status.setCode(UPDATE_SUCCESSFULLY);
			status.setMessage("Update Successfully");
			return status;
		}
		
		return null;
	}
	
	private LtSoHeaders updateSoHeader(LtSoHeaders ltSoHeader) {
		LtSoHeaders ltSoHeaderOp=null;
		ltSoHeaderOp = ltSoHeadersRepository.save(ltSoHeader);
		return ltSoHeaderOp;
	}

	private void sendNotifications(LtSoHeaders ltSoHeader) throws ServiceException, IOException {
		
		System.out.print("This is in sendNotifications method ");
		
		List<LtMastUsers> distUsersList  = ltSoHeadersDao.getActiveDistUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		//System.out.print("This is distUsersList in sendNotifications method " + distUsersList);
		
		List<LtMastUsers>salesUsersList  = ltSoHeadersDao.getActiveSalesUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		//System.out.print("This is salesUsersList in sendNotifications method " + salesUsersList);
		
		List<LtMastUsers> areaHeadUserList = ltSoHeadersDao.getActiveAreaHeadeUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		//System.out.print("This is areaHeadUserList in sendNotifications method " + areaHeadUserList);
		
		List<LtMastUsers> sysAdminUserList = ltSoHeadersDao.getActiveSysAdminUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		//System.out.print("This is sysAdminUserList in sendNotifications method " + sysAdminUserList);
		
		//	Optional<LtMastOutles> outletsObj =ltMastOutletRepository.findById(ltSoHeader.getOutletId());
		//List<LtMastOutles> ltMastOutle = new ArrayList<LtMastOutles>();
		List<LtMastOutles>ltMastOutle = ltSoHeadersDao.getOutletDetailsById(ltSoHeader.getOutletId());
		//System.out.print("This is ltMastOutle in sendNotifications method " + ltMastOutle);
		
		String defailtPriceList = ltSoHeadersDao.getDefaultPriceListAgainstOutletId(ltSoHeader.getOutletId());
		//System.out.print("This is defailtPriceList in sendNotifications method " + defailtPriceList);
		
		//merging areaHeader & sysAdmin list
		//areaHeadUserList.addAll(sysAdminUserList);
		//System.out.print("This is new areaHeadList = "+areaHeadUserList);
		
			String outletCode = "";
			String outletName = "";
			//if (outletsObj.isPresent()) {
			if (!ltMastOutle.isEmpty()) {
				//LtMastOutles ltMastOutlets = outletsObj.get();
				
				LtMastOutles ltMastOutlets = new LtMastOutles();
						
//				if(ltMastOutlets.getOutletCode() != null) {
//					outletCode = ltMastOutlets.getOutletCode() ; 
//				}
//				
//				if(ltMastOutlets.getOutletName() != null) {
//					outletName = ltMastOutlets.getOutletName() ;
//				}
				
				if(ltMastOutle.get(0).getOutletCode() != null) {
					outletCode = ltMastOutle.get(0).getOutletCode(); 
				}
				
				if(ltMastOutle.get(0).getOutletName() != null) {
					outletName = ltMastOutle.get(0).getOutletName();
				}
			}
		
//			if(!areaHeadUserList.isEmpty()) {System.out.println("THis is 1 if");}
//			System.out.println("This is--" +ltSoHeader.getInStockFlag());
//			String flag=ltSoHeader.getInStockFlag();
//			System.out.println("This is flag is --"+flag);
//			if(flag.equalsIgnoreCase("N")){System.out.println("THis is 2 of equlcase if");}
//			if(flag=="N") {System.out.println("THis is 2 if");}
//			if(ltSoHeader.getStatus()=="DRAFT") {System.out.println("THis is 3 if");}
//			if(ltSoHeader.getOrderNumber()!= null) {System.out.println("THis is 4 if");}
//			
//			System.out.println("Thisss ISSSS ISSSUEEEE"+ ltSoHeader.getInStockFlag());
			
			String userType = ltSoHeadersDao.getUserTypeAgainsUserId(ltSoHeader.getCreatedBy());
			//System.out.print("This is userType in sendNotifications method " + userType);
			
			if(userType.equalsIgnoreCase("RETAILER")) {
				System.out.print("This is RETAILER in sendNotifications method " + userType);
				if(!distUsersList.isEmpty()) {
					
			          for (Iterator iterator = distUsersList.iterator(); iterator.hasNext();) {
				          LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
				      if(ltMastUsers.getTokenData() != null) {
					      webController.send(ltMastUsers, ltSoHeader, outletCode, outletName );
				    }
			     }
		      }
		
		               if(!salesUsersList.isEmpty()) {
			               for (Iterator iterator = salesUsersList.iterator(); iterator.hasNext();) {
				           LtMastUsers ltMastUsers2 = (LtMastUsers) iterator.next();
				       if(ltMastUsers2.getTokenData() != null) {
					       webController.send(ltMastUsers2, ltSoHeader, outletCode, outletName);
				    }
			     }
		      }
				
		               if(!sysAdminUserList.isEmpty()) {
			               for (Iterator iterator = sysAdminUserList.iterator(); iterator.hasNext();) {
				           LtMastUsers ltMastUsers2 = (LtMastUsers) iterator.next();
				       if(ltMastUsers2.getTokenData() != null) {
					       webController.send(ltMastUsers2, ltSoHeader, outletCode, outletName);
				    }
			     }
		      }
		               
			}
			if(userType.equalsIgnoreCase("SALES") || userType.equalsIgnoreCase("DISTRIBUTOR")) {
				System.out.print("This is SALES/DISTRIBUTOR in sendNotifications method " + userType);
				// send salesOrder approval notification to areHead if order is outOfStock
//				System.out.println("In-Notif1"+areaHeadUserList);
//				System.out.println("In-Notif2"+ltSoHeader.getInStockFlag());
//				System.out.println("In-Notif3"+ltSoHeader.getStatus());
//				System.out.println("In-Notif4"+ltSoHeader.getOrderNumber());
				
				if(!areaHeadUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("Y") 
						&&	ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null) 
										
					{	
						for(Iterator iterator = areaHeadUserList.iterator(); iterator.hasNext();) {
							LtMastUsers ltMastUsers4 = (LtMastUsers) iterator.next();
							//System.out.print("In-Notif--5"+ltMastUsers4.getTokenData());
	                        						
							if(ltMastUsers4.getTokenData() != null) {
								webController.send(ltMastUsers4, ltSoHeader, outletCode, outletName);
								
							}
						}
				}// send salesOrder approval notification to areHead if order is inStock & priceList is not default
				 if(!areaHeadUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("N") 
						 && ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null 
						 && !ltSoHeader.getPriceList().equals(defailtPriceList)) 
					 
				      {			
							for(Iterator iterator = areaHeadUserList.iterator(); iterator.hasNext();) {
								LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
							//	System.out.print(ltMastUsers.getTokenData());
								if(ltMastUsers.getTokenData() != null) {
									webController.send(ltMastUsers, ltSoHeader, outletCode, outletName);
								}
							}
				} //send salesOrder approval notification to sysAdmin if order is outOfStock
				//System.out.println();
				
				 if(!sysAdminUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("Y") && 
						ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null)
					 
				{	
//			    System.out.println("In-Notif"+sysAdminUserList);
//				System.out.println("In-Notif"+ltSoHeader.getInStockFlag());
//				System.out.println("In-Notif"+ltSoHeader.getStatus());
//				System.out.println("In-Notif"+ltSoHeader.getOrderNumber());
				
						for(Iterator iterator = sysAdminUserList.iterator(); iterator.hasNext();) {
							LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
						//	System.out.print("In-Notif"+ltMastUsers.getTokenData());
							if(ltMastUsers.getTokenData() != null) {
								webController.send(ltMastUsers, ltSoHeader, outletCode, outletName);								
							}
						}
				}// send salesOrder approval notification to sysAdmin if order is inStock & priceList is not default
				  if(!sysAdminUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("N") && 
						  ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null 
						  && !ltSoHeader.getPriceList().equals(defailtPriceList)) 
					  
				      {			
							for(Iterator iterator = sysAdminUserList.iterator(); iterator.hasNext();) {
								LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
						//		System.out.print(ltMastUsers.getTokenData());
								if(ltMastUsers.getTokenData() != null) {
									webController.send(ltMastUsers, ltSoHeader, outletCode, outletName);
								}
							}
				}
				
			}
			
	}

	
	private void sendEmail(LtSoHeaders ltSoHeader) throws ServiceException, IOException {
		
		System.out.println("Hi I'm in send email");
		List<LtMastUsers> distUsersList  = ltSoHeadersDao.getActiveDistUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		System.out.println("Hi I'm in send email distUsersList" + distUsersList);
		
		List<LtMastUsers>salesUsersList  = ltSoHeadersDao.getActiveSalesUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		System.out.println("Hi I'm in send email salesUsersList" + salesUsersList);
		
		List<LtMastUsers> areaHeadUserList = ltSoHeadersDao.getActiveAreaHeadeUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		System.out.println("Hi I'm in send email areaHeadUserList" + areaHeadUserList);
		
		List<LtMastUsers> sysAdminUserList = ltSoHeadersDao.getActiveSysAdminUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		System.out.println("Hi I'm in send email sysAdminUserList" + sysAdminUserList);
		
//			Optional<LtMastOutles> outletsObj =ltMastOutletRepository.findById(ltSoHeader.getOutletId());
//		List<LtMastOutles> ltMastOutle = new ArrayList<LtMastOutles>();
		List<LtMastOutles>ltMastOutle = ltSoHeadersDao.getOutletDetailsById(ltSoHeader.getOutletId());
		System.out.println("Hi I'm in send email ltMastOutle" + ltMastOutle);
		
		String defailtPriceList = ltSoHeadersDao.getDefaultPriceListAgainstOutletId(ltSoHeader.getOutletId());
		System.out.println("Hi I'm in send email defailtPriceList" + defailtPriceList);
		
//		merging areaHeader & sysAdmin list
//		areaHeadUserList.addAll(sysAdminUserList);
//		System.out.print("This is new areaHeadList = "+areaHeadUserList);
		
			String outletCode = "";
			String outletName = "";
//			if (outletsObj.isPresent()) {
			System.out.println("Hi I'm in send email ltMastOutle" + ltMastOutle);
			if (!ltMastOutle.isEmpty()) {
//				LtMastOutles ltMastOutlets = outletsObj.get();
				
				LtMastOutles ltMastOutlets = new LtMastOutles();
						
//				if(ltMastOutlets.getOutletCode() != null) {
//					outletCode = ltMastOutlets.getOutletCode() ; 
//				}
//				
//				if(ltMastOutlets.getOutletName() != null) {
//					outletName = ltMastOutlets.getOutletName() ;
//				}
				System.out.println("Hi I'm in send email ltMastOutle" + ltMastOutle.get(0).getOutletCode());
				if(ltMastOutle.get(0).getOutletCode() != null) {
					outletCode = ltMastOutle.get(0).getOutletCode(); 
				}
				System.out.println("Hi I'm in send email ltMastOutle" + ltMastOutle.get(0).getOutletName());
				if(ltMastOutle.get(0).getOutletName() != null) {
					outletName = ltMastOutle.get(0).getOutletName();
				}
			}
		
//			if(!areaHeadUserList.isEmpty()) {System.out.println("THis is 1 if");}
//			System.out.println("This is--" +ltSoHeader.getInStockFlag());
//			String flag=ltSoHeader.getInStockFlag();
//			System.out.println("This is flag is --"+flag);
//			if(flag.equalsIgnoreCase("N")){System.out.println("THis is 2 of equlcase if");}
//			if(flag=="N") {System.out.println("THis is 2 if");}
//			if(ltSoHeader.getStatus()=="DRAFT") {System.out.println("THis is 3 if");}
//			if(ltSoHeader.getOrderNumber()!= null) {System.out.println("THis is 4 if");}
//			
//			System.out.println("Thisss ISSSS ISSSUEEEE"+ ltSoHeader.getInStockFlag());
			
			String userType = ltSoHeadersDao.getUserTypeAgainsUserId(ltSoHeader.getCreatedBy());
			System.out.println("Hi I'm in send email userType" + userType);
			
			if(userType.equalsIgnoreCase("RETAILER")) {
				System.out.println("Hi I'm in send email RETAILER");
				if(!distUsersList.isEmpty()) {
					System.out.println("Hi I'm in send email RETAILER distUsersList"+ distUsersList);
			          for (Iterator iterator = distUsersList.iterator(); iterator.hasNext();) {
				          LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
				      
					      String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
					      
					      String body = ltSoHeadersDao.getEmailBody(subject);
					      System.out.print("Email body"+body);
					      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
					      System.out.print("userName for Email"+userName);
					      //List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
					      // double amt= amount.stream().collect(Collectors.summingDouble(Double::doubleValue));
					      
					       List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
						     
						      double sum = 0.0;
						      for (String str : amount) {
						    	  if(str.equals("null")) {
						    		  str= "0";
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						    	  else {
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						           
						        }
						      String totalAmount= String.valueOf(sum);
						      System.out.print("amount for Email" +totalAmount);
					       
					       
					      // System.out.print("amount for Email"+amt);
					      String salespersonName=userName;
					      //String totalAmount=  Double.toString(amt);
					      
					      String salesOrder = ltSoHeader.getOrderNumber();
						     String text = "";
					      
					      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
					      body = body.replace("${salespersonName}", salespersonName);
					      body = body.replace("${totalAmount}", totalAmount);
					      if(ltMastUsers.getEmail()!= null) {
					      String to= ltMastUsers.getEmail();
//					    emailService.sendSimpleMessage(to, subject, body);
					     // sendEmail1( subject, body, to);
							try {
								sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

			          }
			     }
		      }
				System.out.println("Hi I'm in send email RETAILER2222");
		               if(!salesUsersList.isEmpty()) {
		            	   System.out.println("Hi I'm in send email RETAILER2222"+ salesUsersList);
			               for (Iterator iterator = salesUsersList.iterator(); iterator.hasNext();) {
				           LtMastUsers ltMastUsers2 = (LtMastUsers) iterator.next();
				           
				           String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
						      
						      String body = ltSoHeadersDao.getEmailBody(subject);
						      System.out.print("Email body"+body);
						      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
						      System.out.print("userName for Email"+userName);
						      //List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
						      //double amt= amount.stream().collect(Collectors.summingDouble(Double::doubleValue));
						      
						      List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
							     
						      double sum = 0.0;
						      for (String str : amount) {
						    	  if(str.equals("null")) {
						    		  str= "0";
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						    	  else {
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						           
						        }
						      String totalAmount= String.valueOf(sum);
						      System.out.print("amount for Email" +totalAmount);
						      
						      
						     // System.out.print("amount for Email"+amt);
						      String salespersonName=userName;
						     // String totalAmount=  Double.toString(amt);
						      
						      String salesOrder = ltSoHeader.getOrderNumber();
							     String text = "";
						      
						      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
						      body = body.replace("${salespersonName}", salespersonName);
						      body = body.replace("${totalAmount}", totalAmount);
						      if(ltMastUsers2.getEmail()!= null) {
						      String to= ltMastUsers2.getEmail();
//						    emailService.sendSimpleMessage(to, subject, body);
						     // sendEmail1( subject, body, to);
								try {
									sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

				          }
				           
			     }
		      }
				
		               if(!sysAdminUserList.isEmpty()) {
		            	   System.out.println("Hi I'm in send email RETAILER2222 sysAdminUserList"+ sysAdminUserList);
			               for (Iterator iterator = sysAdminUserList.iterator(); iterator.hasNext();) {
				           LtMastUsers ltMastUsers2 = (LtMastUsers) iterator.next();
				       
				           String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
						      
						      String body = ltSoHeadersDao.getEmailBody(subject);
						      System.out.print("Email body"+body);
						      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
						      System.out.print("userName for Email"+userName);
						     // List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
						     // double amt = amount.stream().collect(Collectors.summingDouble(Double::doubleValue));
						      
						      List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
							     
						      double sum = 0.0;
						      for (String str : amount) {
						    	  if(str.equals("null")) {
						    		  str= "0";
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						    	  else {
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						           
						        }
						      String totalAmount= String.valueOf(sum);
						      System.out.print("amount for Email" +totalAmount);
						      
						     // System.out.print("amount for Email"+amt);
						      String salespersonName=userName;
						     // String totalAmount= Double.toString(amt);
						      
						      String salesOrder = ltSoHeader.getOrderNumber();
							     String text = "";
						      
						      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
						      body = body.replace("${salespersonName}", salespersonName);
						      body = body.replace("${totalAmount}", totalAmount);
						      if(ltMastUsers2.getEmail()!= null) {
						      String to= ltMastUsers2.getEmail();
//						    emailService.sendSimpleMessage(to, subject, body);
						     // sendEmail1( subject, body, to);
								try {
									sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

				          }
			     }
		      }
		               
			}
			if(userType.equalsIgnoreCase("SALES") || userType.equalsIgnoreCase("DISTRIBUTOR")) {
				System.out.println("Hi I'm in send email SALES SALES");
				// send salesOrder approval notification to areHead if order is outOfStock
				System.out.println("In-Notif"+areaHeadUserList);
				System.out.println("In-Notif"+ltSoHeader.getInStockFlag());
				System.out.println("In-Notif"+ltSoHeader.getStatus());
				System.out.println("In-Notif"+ltSoHeader.getOrderNumber());
//				System.out.println();
				if(!areaHeadUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("Y") 
						&&	ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null) 
					{	
						for(Iterator iterator = areaHeadUserList.iterator(); iterator.hasNext();) {
							LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
							System.out.print("In-Notif"+ltMastUsers.getTokenData());
							
							String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
						      
						      String body = ltSoHeadersDao.getEmailBody(subject);
						      System.out.print("Email body"+body);
						      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
						      System.out.print("userName for Email"+userName);
						      //List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
						      //double amt = amount.stream().collect(Collectors.summingDouble((Double::doubleValue)));
						      
						      List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
							     
						      double sum = 0.0;
						      for (String str : amount) {
						    	  if(str.equals("null")) {
						    		  str= "0";
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						    	  else {
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						           
						        }
						      String totalAmount= String.valueOf(sum);
						      System.out.print("amount for Email" +totalAmount);
						      
						     // System.out.print("amount for Email"+amt);
						      String salespersonName=userName;
						     // String totalAmount= Double.toString(amt);
						      
						      String salesOrder = ltSoHeader.getOrderNumber();
							     String text = "";
						      
						      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
						      body = body.replace("${salespersonName}", salespersonName);
						      body = body.replace("${totalAmount}", totalAmount);
						      if(ltMastUsers.getEmail()!= null) {
						      String to= ltMastUsers.getEmail();
//						      emailService.sendSimpleMessage(to, subject, body);
						      //sendEmail1( subject, body, to);
						      try {
								sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						      
				          }
						}
				}// send salesOrder approval notification to areHead if order is inStock & priceList is not default
				 if(!areaHeadUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("N") 
						 && ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null 
						 )//&& !ltSoHeader.getPriceList().equals(defailtPriceList)) 
				      {			
					 System.out.println("Hi I'm in send email SALESssss");
							for(Iterator iterator = areaHeadUserList.iterator(); iterator.hasNext();) {
								LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
								System.out.println(ltMastUsers.getTokenData());
								
								String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
							      
							     // String body = ltSoHeadersDao.getEmailBody(subject);
								//String body= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
								String body= "EMAIL For SALES ORDER APPROVAL";
								System.out.println("Email body"+body);
							      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
							      System.out.println("userName for Email = "+userName+"...Ok");
							      
							      //List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
							      //System.out.println("amount is........" + amount);
							      //double amt = amount.stream().collect(Collectors.summingDouble((Double::doubleValue)));
							      							      
							      List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
							     
							      double sum = 0.0;
							      for (String str : amount) {
							    	  if(str.equals("null")) {
							    		  str= "0";
							    		  double number = Double.parseDouble(str);
							    		  sum = sum+number;
							    	  }
							    	  else {
							    		  double number = Double.parseDouble(str);
							    		  sum = sum+number;
							    	  }
							           
							        }
							      String totalAmount= String.valueOf(sum);
							      System.out.println("amount for Email" +totalAmount);
							      //System.out.print("amount for Email"+amt);
							      String salespersonName=userName;
							      
							      String salesOrder = ltSoHeader.getOrderNumber();
								     String text = "";
							      
							      //String totalAmount= Double.toString(amt);
							      //String body= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
							      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
							      body = body.replace("${salespersonName}", salespersonName);
							      body = body.replace("${totalAmount}", totalAmount);
							      if(ltMastUsers.getEmail()!= null) {
							      String to= ltMastUsers.getEmail();
							      //emailService.sendSimpleMessage(to, subject, body);
							      System.out.println("Above sendSimpleMessage");
							      //sendSimpleMessage("rohan.pacharne@lonartech.com", "Order", "My first order");
							      try {
									sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					          }
							}
				} //send salesOrder approval notification to sysAdmin if order is outOfStock
				//System.out.println();
				
				 if(!sysAdminUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("Y") && 
						ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null) 
				{	
			   // System.out.println("In-Notif"+sysAdminUserList);
				//System.out.println("In-Notif"+ltSoHeader.getInStockFlag());
				//System.out.println("In-Notif"+ltSoHeader.getStatus());
				//System.out.println("In-Notif"+ltSoHeader.getOrderNumber());
				
						for(Iterator iterator = sysAdminUserList.iterator(); iterator.hasNext();) {
							LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
							System.out.print("In-Notif"+ltMastUsers.getTokenData());
							
							String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
						      
						      String body = ltSoHeadersDao.getEmailBody(subject);
						      System.out.print("Email body"+body);
						      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
						      System.out.print("userName for Email"+userName);
						      //List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
						      //double amt = amount.stream().collect(Collectors.summingDouble((Double::doubleValue)));
						      
						      List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
							     
						      double sum = 0.0;
						      for (String str : amount) {
						    	  if(str.equals("null")) {
						    		  str= "0";
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						    	  else {
						    		  double number = Double.parseDouble(str);
						    		  sum = sum+number;
						    	  }
						           
						        }
						      String totalAmount= String.valueOf(sum);
						      System.out.print("amount for Email" +totalAmount);
						      
						      
						     // System.out.print("amount for Email"+amt);
						      String salespersonName=userName;
						     // String totalAmount= Double.toString(amt);
						      
						      String salesOrder = ltSoHeader.getOrderNumber();
							     String text = "";
						      
						      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
						      body = body.replace("${salespersonName}", salespersonName);
						      body = body.replace("${totalAmount}", totalAmount);
						      if(ltMastUsers.getEmail()!= null) {
						      String to= ltMastUsers.getEmail();
//					        emailService.sendSimpleMessage(to, subject, body);
						     // sendEmail1( subject, body, to);
						      try {
								sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				          }
							
						}
				}// send salesOrder approval notification to sysAdmin if order is inStock & priceList is not default
				  if(!sysAdminUserList.isEmpty() && !ltSoHeader.getInStockFlag().equalsIgnoreCase("N") && 
						  ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL") && ltSoHeader.getOrderNumber()!= null 
						  && !ltSoHeader.getPriceList().equals(defailtPriceList)) 
				      {			
					  System.out.println("Hi I'm in send email SALES SALESsssssss");
							for(Iterator iterator = sysAdminUserList.iterator(); iterator.hasNext();) {
								LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
								System.out.print(ltMastUsers.getTokenData());
								
								String subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
							      
							      //String body = ltSoHeadersDao.getEmailBody(subject);
							      //String body= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
							      String body= "EMAIL For SALES ORDER APPROVAL";
							      System.out.print("Email body"+body);
							      String userName = ltSoHeadersDao.getUserNameAgainsUserId(ltSoHeader.getCreatedBy());
							      System.out.print("userName for Email"+userName);
							     // List<Double> amount = ltSoHeadersDao.getTotalAmount(ltSoHeader.getHeaderId());
							      //double amt = amount.stream().mapToDouble(Double::doubleValue).sum();
							      
							      List<String> amount = ltSoHeadersDao.getTotalAmount1(ltSoHeader.getHeaderId());
								     
							      double sum = 0.0;
							      for (String str : amount) {
							    	  if(str.equals("null")) {
							    		  str= "0";
							    		  double number = Double.parseDouble(str);
							    		  sum = sum+number;
							    	  }
							    	  else {
							    		  double number = Double.parseDouble(str);
							    		  sum = sum+number;
							    	  }
							          
							        }
							      String totalAmount= String.valueOf(sum);
							      System.out.print("amount for Email" +totalAmount);

							      
							      //System.out.print("amount for Email"+amt);
							      String salespersonName=userName;
							     // String totalAmount= Double.toString(amt);
							      
							     String salesOrder = ltSoHeader.getOrderNumber();
							     String text = "";
							      
							      body = body.replace("${salesOrder}", ltSoHeader.getOrderNumber());
							      body = body.replace("${salespersonName}", salespersonName);
							      body = body.replace("${totalAmount}", totalAmount);
							      if(ltMastUsers.getEmail()!= null) {
							      String to= ltMastUsers.getEmail();
//							      emailService.sendSimpleMessage(to, subject, body);
//							      sendEmail1( subject, body, to);
							      try {
									sendSimpleMessageWithAuth(to,subject,text,salesOrder,salespersonName,totalAmount);
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					          }
								
							}
				}
				
			}
			
	}
	
	
	 public void sendSimpleMessageWithAuth(String to, String subject, String text,String salesOrder,String salespersonName,String totalAmount) throws MessagingException {
//       MimeMessage message = emailSender.createMimeMessage();
//       MimeMessageHelper helper = new MimeMessageHelper(message, true);
       Map<String,String> map = new HashMap<String,String>();
       System.out.println("in sendSimpleMessageWithAuth");
       String host = "14.140.146.196";    // "smtp.gmail.com";         
       String port = "25";//  "587";         
       String mailFrom = "noreply@atfoods.com";  //"atfl4867@gmail.com";         
       String password = "Welcome12";    //"vjug xicp wiuw fakw";         
       // Outgoing email information
       String mailTo = to;        
       String subject1 = "SALES_ORDER_APPROVAL";   
//       text = "<html><head></head><body><h1>Greetings!</h1><p>We hope this message finds you well.</p></body></html>";
//       String salesOrder ="myOrder123";
//       String salespersonName = "Vaibhav";
//       String totalAmount= "100";
        text = "<html><head></head><body>"
               + "<div style=\"margin-top:0;background-color:#f4f4f4!important;color:#555;font-family:'Open Sans',sans-serif;\">"
               + "<div style=\"font-size:14px;margin:0px auto;max-width:620px;margin-bottom:20px;background:#c02e2e05;\">"
               + "<div style=\"padding:0px 0px 9px 0px;width:100%;color:#fff;font-weight:bold;font-size:15px;line-height:20px;width:562px;margin:0 auto;\">"
               + "<center><table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td valign=\"top\" style=\"padding:48px 48px 32px\">"
               + "<div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">"
               + "<p style=\"margin:0 0 16px\">Dear Sir/Ma’am,</p>"
               + "<p style=\"margin:0 0 16px\">" + salesOrder + " has come for your approval. Please visit the app to approve/ reject the record.</p>"
               + "<h4 style=\"font-weight:bold\">Created by:" + salespersonName + "</h4>"
               + "<h4 style=\"font-weight:bold\">Order Amount:" + totalAmount + "</h4>"
               + "<h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>"
               + "<p>Regards,</p><p>Lakshya App team.</p>"
               + "<div style=\"margin-bottom:40px\"></div></div></td></tr></tbody></table></center></div></div></div></body></html>";
       
       String message = text;
       String result = String.join("   ", host,mailFrom,password,mailTo,subject1,message);
       System.out.println("Port = "+port);
   	System.out.println("result in method = "+result);
       try {
//           helper.setTo(to);
//           helper.setSubject(subject);
//           helper.setText(text, true); // true indicates HTML content
//           emailSender.send(message);
           System.out.println("Port = "+port);
       	System.out.println("result in try = "+result);
       	   sendEmail(host, port, mailFrom, password, mailTo, subject1, message);
//           map.put("status", "true");
//           map.put("message", "Email sent successfully to: "+ to);
       } catch (MailException ex) {
           System.out.println("Port = "+port);
       	System.out.println("result in catch = "+result);
           System.out.println("Failed to send email to: " + to);
           map.put("status", "false");
           map.put("message", "Failed to send email to: "+to);
           ex.printStackTrace(); // Log the stack trace for debugging
       }
       
   }
   
   
   public static void sendEmail(String host, String port, final String userName, final String password, String toAddress,String subject, String messageBody) throws AddressException, MessagingException {         
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
   	
//   	if ("465".equals(port)) {
//        properties.put("mail.smtp.socketFactory.port", port);
//        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        properties.put("mail.smtp.socketFactory.fallback", "false");
//    }
    
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
   			return new PasswordAuthentication(userName, password);           
   			}       
   		};
       	System.out.println("below Authenticator");
   		Session session = Session.getInstance(properties, auth); 
       	System.out.println("below session");
   		// Create the email message
   		Message message = new MimeMessage(session); 
   		message.setFrom(new InternetAddress(userName)); 
   		message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress)); 
   		message.setSubject(subject); 
//   		message.setText(messageBody);
           message.setContent(messageBody, "text/html; charset=utf-8");
   		// Send the email 
       	System.out.println("Above Transport");
       	try {
       		Transport.send(message);
       		System.out.println("Success...");
       		map.put("status", "101");
       		map.put("message", "Email sent successfully to: "+ toAddress);
       	}catch(Exception ex) {
       		System.out.println("Failed...");
       		map.put("status", "102");
               map.put("message", "Failed to send email to: "+ toAddress);
       		ex.printStackTrace();
       	}
       	System.out.println("Below Transport");
       	
   }
	
		
/*	
	public void sendSimpleMessage(String to, String subject, String text) {
		System.out.println("In sendSimpleMessage");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        
        message.setText(text);
        emailSender.send(message);
        System.out.println(to+" "+subject+" "+text);
        System.out.println("Exit from sendSimpleMessage");
    }
*/	
	

	    public static void sendEmail1(String subject, String text, String to) throws IOException {
	    	System.out.println("Hi I'm in send email1111...");
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        try {
	        	//text= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
	        	//basetext= "PGh0bWw+IDxoZWFkPiA8L2hlYWQ+IDxib2R5PiAgICAgPGRpdiBzdHlsZT0ibWFyZ2luLXRvcDogMDtiYWNrZ3JvdW5kLWNvbG9yOiNmNGY0ZjQhaW1wb3J0YW50O2NvbG9yOiM1NTU7IGZvbnQtZmFtaWx5OiAnT3BlbiBTYW5zJywgc2Fucy1zZXJpZjsiPjxkaXYgc3R5bGU9ImZvbnQtc2l6ZTogMTRweDttYXJnaW46IDBweCBhdXRvO21heC13aWR0aDogNjIwcHg7bWFyZ2luLWJvdHRvbTogMjBweDtiYWNrZ3JvdW5kOiNjMDJlMmUwNTsiPiAgICAgICAgICAgICAgPGRpdiBzdHlsZT0icGFkZGluZzogMHB4IDBweCA5cHggMHB4O3dpZHRoOiAxMDAlO2NvbG9yOiAjZmZmO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOiAxNXB4O2xpbmUtaGVpZ2h0OiAyMHB4OyB3aWR0aDogNTYycHg7IG1hcmdpbjogMCBhdXRvOyI+ICAgICAgICAgICAgICAgICA8Y2VudGVyPiAgICAgICAgICAgICAgICAgICAgIDx0YWJsZSBib3JkZXI9IjAiIGNlbGxwYWRkaW5nPSIyMCJjZWxsc3BhY2luZz0iMCIgd2lkdGg9IjEwMCUiPiAgICAgICAgICAgICAgICAgICAgICAgICA8dGJvZHk+ICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8dHI+ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPHRkIHZhbGlnbj0idG9wIiBzdHlsZT0icGFkZGluZzogNDhweCA0OHB4IDMycHgiPiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IHN0eWxlPSJjb2xvcjojMTAxMDEwO2ZvbnQtZmFtaWx5OkhlbHZldGljYU5ldWUsSGVsdmV0aWNhLFJvYm90byxBcmlhbCxzYW5zLXNlcmlmO2ZvbnQtc2l6ZTogMTRweDtsaW5lLWhlaWdodDogMTUwJTt0ZXh0LWFsaWduOmxlZnQiPiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwIHN0eWxlPSJtYXJnaW46IDAgMCAxNnB4Ij5EZWFyIFNpci9NYeKAmWFtLDwvcD4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwIHN0eWxlPSJtYXJnaW46IDAgMCAxNnB4Ij5NU08tMjk2ODYtMjQyNS05NjcgaGFzIGNvbWUgZm9yIHlvdXIgYXBwcm92YWwuIFBsZWFzZSB2aXNpdCB0aGUgYXBwIHRvIGFwcHJvdmUvIHJlamVjdCB0aGUgcmVjb3JkLjwvcD4gICAgPGg0IHN0eWxlPSJmb250LXdlaWdodDpib2xkIj5DcmVhdGVkIGJ5OlBhbmNoYW5hbiBSb3V0PC9oND4gIDxoNCBzdHlsZT0iZm9udC13ZWlnaHQ6Ym9sZCI+T3JkZXIgQW1vdW50OjAuMDwvaDQ+ICAgPGg0IHN0eWxlPSJmb250LXdlaWdodDpib2xkO2NvbG9yOmJsdWUiPlRoaXMgaXMgYSBzeXN0ZW0tZ2VuZXJhdGVkIGVtYWlsLiBQbGVhc2UgZG8gbm90IHJlcGx5IHRvIHRoaXMuPC9oND4gIDxwPlJlZ2FyZHMsPC9wPjxwPkxha3NoeWEgQXBwIHRlYW0uPC9wPiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8ZGl2IHN0eWxlPSAibWFyZ2luLWJvdHRvbTogNDBweCI+ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9kaXY+ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L2Rpdj4gICAgICAgICAgICAgICAgICAgICAgICAgIDwvaHRtbD4gICAgICAgPC90ZD4gICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvdHI+ICAgICAgICAgICAgICAgICAgICAgICAgIDwvdGJvZHk+ICAgICAgICAgICAgICAgICAgICAgPC90YWJsZT4gICAgICAgICAgICAgICAgIDwvY2VudGVyPiAgICAgICAgICAgICA8L2Rpdj4gICAgICAgICAgICA8L2Rpdj4gICAgICA8L2Rpdj4gPC9ib2R5Pgo=";
	            //System.out.println(htmlString);
	        	//String htmlString = "<html> <head> </head> <body> <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\"><div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\"><center><table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\"><tbody><tr><td valign=\"top\" style=\"padding: 48px 48px 32px\"><div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\"><p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p><p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p><h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4><h4 style=\"font-weight:bold\">Order Amount:0.0</h4><h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4><p>Regards,</p><p>Lakshya App team.</p><div style=\"margin-bottom: 40px\"></div></div></html></td></tr></tbody></table></center></div></div></div></body>";

	            // Assign the HTML string to jsonPayload variable
	           //String jsonPayload = "{\"text\": \"" + htmlString + "\"}";

	        	
	        	//System.out.println("Hi I'm in try sendEmail1");
	        	
	        	//text = "<html> <head> </head> <body> <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\"> <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\"> <center> <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td valign=\"top\" style=\"padding: 48px 48px 32px\"> <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\"> <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p> <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p> <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4> <h4 style=\"font-weight:bold\">Order Amount:0.0</h4> <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4> <p>Regards,</p><p>Lakshya App team.</p> <div style= \"margin-bottom: 40px\"> </div> </div> </html> </td> </tr> </tbody> </table> </center> </div> </div> </div> </body>";

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
	            subject = "EMAIL_SALES_ORDER_APPROVAL_MAILBODY";
	            to= "rohan.pacharne@lonartech.com";
	            String salesOrder ="myOrder123";
	            String salespersonName = "Vaibhav";
	            String totalAmount= "100";
	             text = "<html><head></head><body>"
	                    + "<div style=\"margin-top:0;background-color:#f4f4f4!important;color:#555;font-family:'Open Sans',sans-serif;\">"
	                    + "<div style=\"font-size:14px;margin:0px auto;max-width:620px;margin-bottom:20px;background:#c02e2e05;\">"
	                    + "<div style=\"padding:0px 0px 9px 0px;width:100%;color:#fff;font-weight:bold;font-size:15px;line-height:20px;width:562px;margin:0 auto;\">"
	                    + "<center><table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td valign=\"top\" style=\"padding:48px 48px 32px\">"
	                    + "<div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">"
	                    + "<p style=\"margin:0 0 16px\">Dear Sir/Ma’am,</p>"
	                    + "<p style=\"margin:0 0 16px\">" + salesOrder + " has come for your approval. Please visit the app to approve/ reject the record.</p>"
	                    + "<h4 style=\"font-weight:bold\">Created by:" + salespersonName + "</h4>"
	                    + "<h4 style=\"font-weight:bold\">Order Amount:" + totalAmount + "</h4>"
	                    + "<h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>"
	                    + "<p>Regards,</p><p>Lakshya App team.</p>"
	                    + "<div style=\"margin-bottom:40px\"></div></div></td></tr></tbody></table></center></div></div></div></body></html>";

	            
	            String jsonPayload = "{\"subject\": \"" + subject + "\", \"text\": \"" + text + "\", \"to\": \"" + to + "\"}";
	            System.out.println("Hi I'm in send email1111... jsonPayload" + jsonPayload);
	            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
	            System.out.println("entity1"+entity); 
                httpPost.setEntity(entity);
                System.out.println("entity2"+entity); 
                
	            // Execute the request
	            CloseableHttpResponse response = httpClient.execute(httpPost);
	            System.out.println("response1"+response);
	            try {
	            	int statusCode = response.getStatusLine().getStatusCode();
	            	if (statusCode >= 200 && statusCode < 300) {
	                    HttpEntity responseEntity = response.getEntity();
	                    if (responseEntity != null) {
	                        String responseString = EntityUtils.toString(responseEntity);
	                        System.out.println("Email sent successfully. Response: " + responseString);
	                    } else {
	                        System.out.println("Email sent successfully. No response content.");
	                    }
	                } else {
	                    System.out.println("Failed to send email. HTTP error code: " + statusCode);
	                }
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpClient.close();
	        }
	    }
	    
	 
//	    public static void main(String[] args) {
//	        try {
//	            sendEmail("Siebel Issues", "Hello Shubham, Please update on pending siebel issues.", "Shubham.magare@lonartech.com");
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
	
	
	
	public static int getYearFromDate(Date date) {
        int result = -1;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(Calendar.YEAR);
        }
        return result;
    }
	
	String genrateOrderNumber(String outletId) throws ServiceException, IOException {
		//MSO-DISCrMCode-2021-6sequncsNoUsinHeaderId
		
		//old fin year calculation;
		//int year = getYearFromDate(new Date());
	    //String finYear = (String.valueOf(year)).substring(2)+""+(String.valueOf(year+1)).substring(2);
		
	    int year = Calendar.getInstance().get(Calendar.YEAR);
	    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
	    String finYear;
	    System.out.println("Financial month : " + month);
	    if (month <= 3) {
	       // System.out.println("Financial Year : " + (year - 1) + "-" + year);
	        finYear = (String.valueOf(year- 1)).substring(2)+""+(String.valueOf(year)).substring(2);
	    } else {
	       // System.out.println("Financial Year : " + year + "-" + (year + 1));
	        finYear = (String.valueOf(year)).substring(2)+""+(String.valueOf(year + 1)).substring(2);
	    }
		
	   //Long sequanceNo;
	   String sequanceNo;
	    DistributorDetailsDto distributorDetailsDto=null;
		 synchronized(this){//synchronized block  
			    distributorDetailsDto = ltSoHeadersDao.getDistributorDetailsByOutletId(outletId);
			    
			    if(distributorDetailsDto != null && distributorDetailsDto.getDistributorSequance() != null) {
			    	sequanceNo = distributorDetailsDto.getDistributorSequance() + "1";
			    }else {
			    	sequanceNo = "1";
			    }
	//Vaibhav		    ltSoHeadersDao.updateDistributorSequance(distributorDetailsDto.getDistributorId(), sequanceNo);
		 	}
		 	//String seqNofiveDigit = String.format("%05d", sequanceNo);
		 String seqNofiveDigit = sequanceNo;
		 	//String seqNoSixDigit = "1"+seqNofiveDigit;
		 String seqNoSixDigit = ltSoHeadersDao.getOrderSequence();	
		 
		 System.out.println("seqNoSixDigit"+seqNoSixDigit);
		 
		 String orderNumber = null;
			if(distributorDetailsDto.getDistributorCrmCode() != null) {
				 orderNumber = "MSO-"+distributorDetailsDto.getDistributorCrmCode()+"-"+finYear+"-"+seqNoSixDigit;
			}else {
				 orderNumber = "MSO-"+finYear+"-"+seqNoSixDigit;
			}
			return orderNumber;
	}
	
	private Status saveSoHeadeLineInDraft(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
		try {
			Status status = new Status();
			// SaveOrderResponseDto saveOrderResponseDto = new SaveOrderResponseDto();
			LtSoHeaders ltSoHeader = new LtSoHeaders();
			
			String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
			//System.out.println("orderNumber :: "+orderNumber);
			
			//Long orderNo = System.currentTimeMillis();
			ltSoHeader.setOrderNumber(orderNumber);

			if (soHeaderDto.getOutletId() != null) {
				ltSoHeader.setOutletId(soHeaderDto.getOutletId());
			}
			if (soHeaderDto.getDeliveryDate() != null) {
				ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
			}
			if (soHeaderDto.getLatitude() != null) {
				ltSoHeader.setLatitude(soHeaderDto.getLatitude());
			}
			if (soHeaderDto.getLongitude() != null) {
				ltSoHeader.setLongitude(soHeaderDto.getLongitude());
			}
			if (soHeaderDto.getUserId() != null) {
				ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
			}
			if (soHeaderDto.getUserId() != null) {
				ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
			}
//			if (soHeaderDto.getUserId() != null) {
//				ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
//			}
			if (soHeaderDto.getAddress() != null) {
				ltSoHeader.setAddress(soHeaderDto.getAddress());
			}
			if (soHeaderDto.getRemark() != null) {
				ltSoHeader.setRemark(soHeaderDto.getRemark());
			}

			ltSoHeader.setOrderDate(new Date()); //new Date()
			ltSoHeader.setLastUpdateDate(new Date()); // new Date()
			ltSoHeader.setCreationDate(new Date()); // new Date()
			ltSoHeader.setStatus(DRAFT);

			ltSoHeader = updateSoHeader(ltSoHeader);

			List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
			
			StringBuffer strQuery1 =  new StringBuffer();
			
			strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
			
			StringBuffer strQuery =  new StringBuffer();
			
			for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
				
				SoLineDto soLineDto = (SoLineDto) iterator.next();
				
				strQuery.append("(");

				//LtSoLines ltSoLines = new LtSoLines();
				
				if (ltSoHeader.getHeaderId() != null) {
					//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
					strQuery.append(ltSoHeader.getHeaderId()+",");
					
				}
				if (soLineDto.getProductId() != null) {
					//ltSoLines.setProductId(soLineDto.getProductId());
					strQuery.append(soLineDto.getProductId()+",");
				}
				if (soLineDto.getQuantity() != null) {
					//ltSoLines.setQuantity(soLineDto.getQuantity());
					strQuery.append(soLineDto.getQuantity()+",");
				}
				if (soLineDto.getListPrice() != null) {
					//ltSoLines.setListPrice(soLineDto.getListPrice());
					strQuery.append("'"+soLineDto.getListPrice()+"',");
				}
				if (soLineDto.getDeliveryDate() != null) {
					//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
					strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
				}
				strQuery.append("'"+DRAFT.toString()+"',");//Status
				

				if (soHeaderDto.getUserId() != null) {
					//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
					strQuery.append(soHeaderDto.getUserId()+",");
				}
				
				strQuery.append("'"+new Date()+"',");//Created Date
				
				if (soHeaderDto.getUserId() != null) {
					//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
					strQuery.append(soHeaderDto.getUserId()+",");
				}
				
				if (soHeaderDto.getUserId() != null) {
					//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
					strQuery.append(soHeaderDto.getUserId()+",");
				}
				
				strQuery.append("'"+new Date()+"',");//Last update date
				 
				if (soLineDto.getPtrPrice() != null) {
					//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
					strQuery.append("'"+soLineDto.getPtrPrice()+"'");
				}else {
					//ltSoLines.setPtrPrice(soLineDto.getListPrice());
					strQuery.append("'"+soLineDto.getListPrice()+"'");
				}
				
				//ltSoLines.setStatus(DRAFT);
				//ltSoLines.setLastUpdateDate(new Date());
				//ltSoLines.setCreationDate(new Date());
				strQuery.append("),");
				
				//ltSoLines = ltSoLinesRepository.save(ltSoLines);

			}
			strQuery.deleteCharAt(strQuery.length()-1); 
			strQuery1 = strQuery1.append(strQuery);
			
			String query = strQuery1.toString();
			
			int n = ltSoHeadersDao.insertLine(query);
			
			if (n > 0) {
				System.out.println("Line insert successfully");
				RequestDto requestDto = new RequestDto();
				requestDto.setHeaderId(ltSoHeader.getHeaderId());
				requestDto.setOffset(0);
				status = getOrderV1(requestDto);
				status.setCode(INSERT_SUCCESSFULLY);
				status.setMessage("Insert Successfully");

				return status;
			}
			
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null; 
	}

	@Override
	public Status getAllOrderInprocess() throws ServiceException, IOException {
		Status status = new Status();

		List<ResponseDto> responseDtoList = ltSoHeadersDao.getAllOrderInprocess();

		if (!responseDtoList.isEmpty() && responseDtoList != null) {
			status.setCode(RECORD_FOUND);
			status.setData("Record found");
			status.setData(responseDtoList);
			return status;
		}
		status.setCode(RECORD_NOT_FOUND);
		status.setData("Record not found");
		status.setData(null);
		return status;
	}

	@Override
	public Status getOrderV1(RequestDto requestDto) throws ServiceException, IOException {
		try {
			Status status = new Status();
			
			List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
			Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
			//Long recordCount = (long) headerIdsList.size() + 1;
			
			System.out.println("headerIdsList====>"+headerIdsList.size());
			//System.out.println("recordCount====>"+recordCount);
			
			status.setTotalCount(recordCount);
			status.setRecordCount(recordCount);
			if(headerIdsList.isEmpty()) {
				status.setCode(RECORD_NOT_FOUND);
				status.setData("Record not found");
				return status;
			}
			
			List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
			
			responseDtoList = ltSoHeadersDao.getOrderV1(headerIdsList);
			
			Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<Long, SoHeaderDto>();
			Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<Long, List<SoLineDto>>();

			for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
				ResponseDto responseDto = (ResponseDto) iterator.next();

				SoLineDto soLineDto = new SoLineDto();

				if (responseDto.getLineId() != null) {
					soLineDto.setLineId(responseDto.getLineId());
				}
				if (responseDto.getProductId() != null) {
					soLineDto.setProductId(responseDto.getProductId());
				}
				if (responseDto.getQuantity() != null) {
					soLineDto.setQuantity(responseDto.getQuantity());
				}
				if (responseDto.getProductCode() != null) {
					soLineDto.setProductCode(responseDto.getProductCode());
				}
				if (responseDto.getProductDesc() != null) {
					soLineDto.setProductDesc(responseDto.getProductDesc());
				}
				if (responseDto.getProductName() != null) {
					soLineDto.setProductName(responseDto.getProductName());
				}
				if (responseDto.getPriceList() != null) {
					soLineDto.setPriceList(responseDto.getPriceList());
				}
				
				if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
					if (responseDto.getListPrice() != null) {
						soLineDto.setListPrice(responseDto.getListPrice());
					}
				}else {
					if (responseDto.getLinelistPrice() != null) {
						soLineDto.setListPrice(responseDto.getLinelistPrice());
					}
				}
				
				
				
				if (responseDto.getPtrPrice() != null) {
					if(responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						soLineDto.setPtrPrice(responseDto.getListPrice());
					}else {
						soLineDto.setPtrPrice(responseDto.getPtrPrice());
					}
				}
			
				if (responseDto.getPtrPrice() != null) {
					//System.out.println("IF responseDto.getPtrPrice() != null "+responseDto.getPtrPrice());
					if(responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//soLineDto.setPtrPrice(responseDto.getListPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
							if (responseDto.getListPrice() != null) {
								soLineDto.setListPrice(responseDto.getListPrice());
							}
						}else {
							if (responseDto.getLinelistPrice() != null) {
								soLineDto.setListPrice(responseDto.getLinelistPrice());
							}
						}
					}else {
						//soLineDto.setPtrPrice(responseDto.getPtrPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
							if (responseDto.getPtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getPtrPrice());
							}
						}else {
							if (responseDto.getLinePtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getLinePtrPrice());
							}
						}
					}
				}
				
				if (responseDto.getInventoryQuantity() != null) {
					soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
				}
				if (responseDto.getDeliveryDate() != null) {
					soLineDto.setDeliveryDate(responseDto.getDeliveryDate());
				}
				if (responseDto.getOrgId() != null) {
					soLineDto.setOrgId(responseDto.getOrgId());
				}
				if (responseDto.getProductType() != null) {
					soLineDto.setProductType(responseDto.getProductType());
				}
				if (responseDto.getPrimaryUom() != null) {
					soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
				}
				if (responseDto.getSecondaryUom() != null) {
					soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
				}
				if (responseDto.getSecondaryUomValue() != null) {
					soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
				}
				if (responseDto.getUnitsPerCase() != null) {
					soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
				}
				if (responseDto.getProductImage() != null) {
					soLineDto.setProductImage(responseDto.getProductImage());
				}
				if (responseDto.getBrand() != null) {
					soLineDto.setBrand(responseDto.getBrand());
				}
				if (responseDto.getSubBrand() != null) {
					soLineDto.setSubBrand(responseDto.getSubBrand());
				}
				if (responseDto.getSegment() != null) {
					soLineDto.setSegment(responseDto.getSegment());
				}
				if (responseDto.getCasePack() != null) {
					soLineDto.setCasePack(responseDto.getCasePack());
				}
				if (responseDto.getHsnCode() != null) {
					soLineDto.setHsnCode(responseDto.getHsnCode());
				}
				if (responseDto.getThumbnailImage() != null) {
					soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
				}

				if (soLineDtoMap.get(responseDto.getHeaderId()) != null) {
					// already exost
					List<SoLineDto> soLineDtoList = soLineDtoMap.get(responseDto.getHeaderId());
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				} else {
					// add data into soheader map
					SoHeaderDto soHeaderDto = new SoHeaderDto();
					soHeaderDto.setHeaderId(responseDto.getHeaderId());
					soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
					
					//FOR DEV UTC TIME ZONE
					//soHeaderDto.setOrderDate(responseDto.getOrderDate().toString());
					
					//FOR DEV IST TIME ZONE
					final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
					final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
					final TimeZone utc = TimeZone.getTimeZone("UTC");
					sdf.setTimeZone(utc);
					soHeaderDto.setOrderDate(sdf.format(responseDto.getOrderDate()));
					
					soHeaderDto.setStatus(responseDto.getStatus());
					soHeaderDto.setAddress(responseDto.getAddress());
					soHeaderDto.setOutletName(responseDto.getOutletName());
					soHeaderDto.setOutletId(responseDto.getOutletId());
					soHeaderDto.setOutletCode(responseDto.getOutletCode());
					soHeaderDto.setLatitude(responseDto.getLatitude());
					soHeaderDto.setLongitude(responseDto.getLongitude());
					if (responseDto.getProprietorName() != null) {
						soHeaderDto.setProprietorName(responseDto.getProprietorName());
					}
					if (responseDto.getRemark() != null) {
						soHeaderDto.setRemark(responseDto.getRemark());
					}
					if (responseDto.getDeliveryDate() != null) {
						soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
					}
					if (responseDto.getUserId() != null) {
						soHeaderDto.setUserId(responseDto.getUserId());
					}
					if (responseDto.getOutletAddress() != null) {
						soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
					}
					if (responseDto.getCustomerId() != null) {
						soHeaderDto.setCustomerId(responseDto.getCustomerId());
					}
					if (responseDto.getAddress() != null) {
						soHeaderDto.setAddress(responseDto.getAddress());
					}
					
					if (responseDto.getCity() != null) { soHeaderDto.setCity(responseDto.getCity()); }
					
					if(responseDto.getBeatId()!= null) {
						soHeaderDto.setBeatId(responseDto.getBeatId());
					}
					if(responseDto.getHeaderPriceList()!= null) {
					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
					}
					
					soHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);

					List<SoLineDto> soLineDtoList = new ArrayList<SoLineDto>();
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				}

			}
			OrderDetailsDto orderDetailsDto = new OrderDetailsDto();

			List<SoHeaderDto> soHeaderDtoList = new ArrayList<SoHeaderDto>();

			for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
				SoHeaderDto soHeaderDto = entry.getValue();

				List<SoLineDto> soLineDtoList = soLineDtoMap.get(entry.getKey());
				soHeaderDto.setSoLineDtoList(soLineDtoList);

				soHeaderDtoList.add(soHeaderDto);
			}
			orderDetailsDto.setSoHeaderDto(soHeaderDtoList);

			if (!responseDtoList.isEmpty()) {
				status.setCode(RECORD_FOUND);// = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				status.setData(orderDetailsDto);
				return status;
			}
			status.setCode(RECORD_NOT_FOUND); // status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
			return status;
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		} return null;
	}

	@Override
	public Status getOrderCancellationReport()throws ServiceException, IOException{
		Status status = new Status();
		List<LtOrderCancellationReason> list = ltSoHeadersDao.getOrderCancellationReport();
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
	
//	ATFL Phase 2 new development 
	@Override 
	public Status saveOrderV2(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
		
		Status status = new Status();
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerycheckHeaderStatusIsDraft = 0;
		long outQuerycheckHeaderStatusIsDraft = 0;
		long inQuerycheckOrderStatus = 0;
		long outQuerycheckOrderStatus = 0;
		
		
		try {
			
			if (soHeaderDto.getOutletId() != null) {
				inQuerycheckHeaderStatusIsDraft = System.currentTimeMillis();
				LtSoHeaders ltSoHeader = ltSoHeadersDao.checkHeaderStatusIsDraft(soHeaderDto.getOutletId());
				outQuerycheckHeaderStatusIsDraft = System.currentTimeMillis();
				LtSoHeaders checkOrder = null;
				if (soHeaderDto.getOrderNumber() != null && soHeaderDto.getOrderNumber() != "") {
					inQuerycheckOrderStatus = System.currentTimeMillis();
					checkOrder = ltSoHeadersDao.checkOrderStatus(soHeaderDto.getOrderNumber());
					outQuerycheckOrderStatus = System.currentTimeMillis();
				}
                  
				if (ltSoHeader == null && checkOrder == null && !soHeaderDto.getSoLineDtoList().isEmpty()) {
					// Save header in draft status with line and Line data is not empty
					status = saveSoHeadeLineInDraftV2(soHeaderDto);      //comment on 24-May-24 for optimization
					
					//status = isQuantityAvailable(soHeaderDto);
					
					//status = saveSoHeadeLineInDraftInternal(soHeaderDto);
					long methodOut = System.currentTimeMillis();
					System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
			        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
			        status.setTimeDifference(timeDifference);
					return status;
				} else if(ltSoHeader != null && ltSoHeader.getStatus().equalsIgnoreCase(DRAFT) && soHeaderDto.getOrderNumber() == null){
					// Status Draft and Order Number null
					status.setCode(FAIL);
					status.setMessage("DB status draft and order number null");
					return status;
				}else if(ltSoHeader != null && ltSoHeader.getStatus().equalsIgnoreCase(DRAFT) && soHeaderDto.getSoLineDtoList().isEmpty()){
					// Status Draft and NO LINE DATA
					//#### Delete Header confirm ??
					//deleteSoHeaderLineEmpty(ltSoHeader.getHeaderId()); // 13 Aug 2020 Comment
					ltSoHeadersDao.deleteLineDataByHeaderId(ltSoHeader.getHeaderId());
					deleteSoHeaderLineEmpty(ltSoHeader.getHeaderId());
					status.setCode(FAIL);
					status.setMessage("DB status draft and no line data against header");
					return status;
				}else if(soHeaderDto.getSoLineDtoList().isEmpty()) {
					// No line data against header for any status
					if(ltSoHeader.getHeaderId()!= null) {
						//deleteSoHeaderLineEmpty(ltSoHeader.getHeaderId());
						ltSoHeadersDao.deleteLineDataByHeaderId(ltSoHeader.getHeaderId());
						deleteSoHeaderLineEmpty(ltSoHeader.getHeaderId());
					}
					status.setCode(FAIL);
					status.setMessage("No line data against header");
					return status;
				}
				else {
					synchronized(this){
						status = updateSoHeadeLineInDraftV2(soHeaderDto, checkOrder.getHeaderId(), checkOrder.getCreationDate(), checkOrder.getCreatedBy());
						long methodOut = System.currentTimeMillis();
						System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
				        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
				        status.setTimeDifference(timeDifference);
						return status;
					}
				}
			} else {
				status.setCode(FAIL);
				status.setMessage("Outlet id not found");
			}
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}

	@Override 
	public Status getAvailableProductQuantity(SoHeaderDto soHeaderDto)throws ServiceException, IOException {
		System.out.println("is Qty called = "+new Date());
		Status status = new Status();
		//List<String> productIdList = new ArrayList<>();
		try {
			System.out.println("soHeaderDto = "+soHeaderDto.getSoLineDtoList());
//		boolean isQuantity = true;
//		List<String> productIdList = new ArrayList<>();
//		for(SoLineDto soLineDto:soHeaderDto.getSoLineDtoList()) {
//			productIdList.add(soLineDto.getProductId());
//		}
			
		List<String> productIdList = soHeaderDto.getSoLineDtoList().stream()
			    .map(SoLineDto::getProductId)
			    .collect(Collectors.toList());
		
//		System.out.println("productIdList: " + productIdList);
		
	    List<QuantityCheck> productList = ltSoHeadersDao.quantityCheck(soHeaderDto.getDistributorId(),productIdList);
//	    System.out.println("productList: " + productList);
//	    System.out.println("size is: " + productList.size());
	    
	    if(productList.size()>0) {
	    	status.setCode(RECORD_FOUND);
	    	status.setMessage("Record found");
	    	status.setData(productList);
	    }
	    else {
	    	status.setCode(RECORD_NOT_FOUND);
	    	status.setMessage("Record not found");
	    	status.setData(productList);
	    }
	}catch(Exception e) {
		e.printStackTrace();
		status.setCode(RECORD_NOT_FOUND);
    	status.setMessage("Record not found");
    	status.setData(null);
	}
	    // Map to store product ID and its available quantity for quick lookup
//	    Map<String, Long> productQuantityMap = new HashMap<>();
//	    for (QuantityCheck product : productList) {
//	        productQuantityMap.put(product.getProductId(), product.getQuantity());
//	    }
	    
//	    Map<String, Long> productQuantityMap = productList.stream()
//        .collect(Collectors.toMap(
//            QuantityCheck::getProductId,
//            QuantityCheck::getQuantity
//        ));
//	    System.out.println("productQuantityMap = "+productQuantityMap);
//	    System.out.println("productQuantityMap = "+productQuantityMap.size());
	    
//	    for (SoLineDto soLineDto : soHeaderDto.getSoLineDtoList()) {
//	        String productId = soLineDto.getProductId();
//	        long requestedQuantity = soLineDto.getQuantity();
//	        long availableQuantity = productQuantityMap.get(productId);
// 
//	        if (availableQuantity < requestedQuantity) {
//	            // Quantity is not available or less than requested
//	        	isQuantity = false;
//	        	break;
//	        }
//	    }
	    
//	    isQuantity = soHeaderDto.getSoLineDtoList().stream()
//	    	    .noneMatch(soLineDto -> {
//	    	        String productId = soLineDto.getProductId();
//	    	        long requestedQuantity = soLineDto.getQuantity();
//	    	        long availableQuantity = productQuantityMap.get(productId);
//	    	        // Treat availableQuantity as 0 if it is null
//	    	        return availableQuantity < requestedQuantity;
//	    	    });
	    
//	    System.out.println("isQuantity = "+isQuantity);
//	    
//	    if(isQuantity==false) {
//	    	status.setCode(FAIL);
//			status.setMessage("Requested quantity not available for one or more products.");
//			return status;
//	    }else {
//	    	try {
//				return saveSoHeadeLineInDraftV2(soHeaderDto);
//			} catch (ServiceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	    }
		return status;
 
	}
/* this is original method comment on 24-May-2024 for optimization	
	private Status saveSoHeadeLineInDraftV2(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
		try {
			System.out.println("Hi This is saveSoHeadeLineInDraftV2 ");
			Status status = new Status();
			// SaveOrderResponseDto saveOrderResponseDto = new SaveOrderResponseDto();
			LtSoHeaders ltSoHeader = new LtSoHeaders();
			
			  LtMastUsers user = ltSoHeadersDao.getUserDetailsAgainsUserId(soHeaderDto.getUserId());
			  System.out.println("This user issssssss" + user);
			  if(user.getUserType().equalsIgnoreCase("RETAILER")) 
			  {
				  System.out.println("Hi This is RETAILER saveSoHeadeLineInDraftV2 ");
				  // need to copy here old save order code
				  try {				
						
						String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
						//System.out.println("orderNumber :: "+orderNumber);
						
						//Long orderNo = System.currentTimeMillis();
						ltSoHeader.setOrderNumber(orderNumber);

						if (soHeaderDto.getOutletId() != null) {
							ltSoHeader.setOutletId(soHeaderDto.getOutletId());
						}
						if (soHeaderDto.getDeliveryDate() != null) {
							ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
						}
						if (soHeaderDto.getLatitude() != null) {
							ltSoHeader.setLatitude(soHeaderDto.getLatitude());
						}
						if (soHeaderDto.getLongitude() != null) {
							ltSoHeader.setLongitude(soHeaderDto.getLongitude());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getAddress() != null) {
							ltSoHeader.setAddress(soHeaderDto.getAddress());
						}
						if (soHeaderDto.getRemark() != null) {
							ltSoHeader.setRemark(soHeaderDto.getRemark());
						}

						ltSoHeader.setOrderDate(new Date()); //new Date()
						ltSoHeader.setLastUpdateDate(new Date()); // new Date()
						ltSoHeader.setCreationDate(new Date()); // new Date()
						ltSoHeader.setStatus(DRAFT);

						ltSoHeader = updateSoHeader(ltSoHeader);

						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
					//	StringBuffer strQuery1 =  new StringBuffer();
						
					//	strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
						
					//	StringBuffer strQuery =  new StringBuffer();
						
						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
//							
//							SoLineDto soLineDto = (SoLineDto) iterator.next();
//							StringBuffer strQuery =  new StringBuffer();
//							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
//
//							strQuery.append("(");
//
//							//LtSoLines ltSoLines = new LtSoLines();
//							
//							if (ltSoHeader.getHeaderId() != null) {
//								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
//								strQuery.append(ltSoHeader.getHeaderId()+",");
//								
//							}
//							if (soLineDto.getProductId() != null) {
//								//ltSoLines.setProductId(soLineDto.getProductId());
//								strQuery.append("'"+soLineDto.getProductId()+"',");
//							}
//							if (soLineDto.getQuantity() != null) {
//								//ltSoLines.setQuantity(soLineDto.getQuantity());
//								strQuery.append(soLineDto.getQuantity()+",");
//							}
//							if (soLineDto.getListPrice() != null) {
//								//ltSoLines.setListPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}
//							if (soLineDto.getDeliveryDate() != null) {
//								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//								strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//							}
//							strQuery.append("'"+DRAFT.toString()+"',");//Status
//							
//
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							strQuery.append("'"+new Date()+"',");//Created Date
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							strQuery.append("'"+new Date()+"',");//Last update date
//							 
//							if (soLineDto.getPtrPrice() != null) {
//								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"'");
//							}else {
//								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"'");
//							}
//							
//							//ltSoLines.setStatus(DRAFT);
//							//ltSoLines.setLastUpdateDate(new Date());
//							//ltSoLines.setCreationDate(new Date());
//							strQuery.append(")");
//							
//							//ltSoLines = ltSoLinesRepository.save(ltSoLines);									
//							String query = strQuery.toString();
//							int n = ltSoHeadersDao.insertLine(query);
							
							

							
							SoLineDto soLineDto = (SoLineDto) iterator.next();	
							StringBuffer strQuery =  new StringBuffer();
							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
							
							strQuery.append("(");

							//LtSoLines ltSoLines = new LtSoLines();
							
							if (ltSoHeader.getHeaderId() != null) {
								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
								strQuery.append(ltSoHeader.getHeaderId()+",");
								
							}
							if (soLineDto.getProductId() != null) {
								//ltSoLines.setProductId(soLineDto.getProductId());
								strQuery.append("'"+soLineDto.getProductId()+"',");
							}
							if (soLineDto.getQuantity() != null) {
								//ltSoLines.setQuantity(soLineDto.getQuantity());
								strQuery.append(soLineDto.getQuantity()+",");
							}
							if (soLineDto.getListPrice() != null) {
								//ltSoLines.setListPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}else {
								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}
							strQuery.append("'"+DRAFT.toString()+"',");//Status
							
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soLineDto.getPtrPrice() != null) {
								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
							}else {
								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
							}
						//	if(soLineDto.getEimStatus()!= null) {
							strQuery.append("'"+null+"',"); // eimstatus
						//	} 		
							if (soLineDto.getDeliveryDate() != null) {
								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
								
								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
								String deliveryDate =outputFormat.format(date);
								System.out.println("formatedDate : " + deliveryDate); 
													
								strQuery.append("'"+deliveryDate+"',");
							//	strQuery.append("'"+"',");
								
							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
							}
							
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
							Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
							Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
							String crationDate =outputFormat.format(date);
							String lastUpdateDate =outputFormat.format(date1);
							System.out.println("crationDate : " + crationDate);
							System.out.println("lastUpdateDate : " + lastUpdateDate);
							
							//strQuery.append("'"+new Date()+"',");//Created Date
							strQuery.append("'"+crationDate+"',");  // set null for demo
							
							//strQuery.append("'"+new Date()+"'");//Last update date
							strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
							
							//ltSoLines.setStatus(DRAFT);
							//ltSoLines.setLastUpdateDate(new Date());
							//ltSoLines.setCreationDate(new Date());
//							strQuery.append("),");
							
							strQuery.append(")");
							String query = strQuery.toString();
							int n = ltSoHeadersDao.insertLine(query);
						
							
						}				
							System.out.println("Line insert successfully");
							
					//		sending notification & email for approve order
							//if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")){
							//sendNotifications(ltSoHeader);
							//}
							//sendEmail("Siebel Issues", "Hello Shubham, This email coming from code .", "Shubham.magare@lonartech.com");
							sendEmail(ltSoHeader);
							
					//    inserting save order data into siebel   
							System.out.println("Hi This is in Draft RETAILER before saveOrderIntoSiebel ");
							//if(ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")){
					//		saveOrderIntoSiebel(ltSoHeader, soHeaderDto);//}
							System.out.println("Hi This is in Draft RETAILER after saveOrderIntoSiebel ");
								  
							RequestDto requestDto = new RequestDto();
							requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
							requestDto.setLimit(-1);
							requestDto.setOffset(2);
					//		requestDto.setUserId(0L);
							status = getOrderV2(requestDto);
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");
							//sendNotifications(ltSoHeader);
							return status;
						
					} catch (Exception e) {
						logger.error("Error Description :", e);
						e.printStackTrace();
					}
					return null;
				  				  
				  
			  }else  if(user.getUserType().equalsIgnoreCase(SALES) || user.getUserType().equalsIgnoreCase(DISTRIBUTOR) ||
					    user.getUserType().equalsIgnoreCase(SYSTEMADMINISTRATOR))
			    {
				   // for Instock products order
				  System.out.println("Hi This is Draft for instock saveSoHeadeLineInDraftV2 ");
				  
				  String defailtPriceList = ltSoHeadersDao.getDefaultPriceListAgainstOutletId(soHeaderDto.getOutletId());
				  
				   if(soHeaderDto.getInstockFlag().equals("Y")) {
						
					String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
					//System.out.println("orderNumber :: "+orderNumber);
					
					//Long orderNo = System.currentTimeMillis();
					ltSoHeader.setOrderNumber(orderNumber);
		            					
					if (soHeaderDto.getOutletId() != null) {
						ltSoHeader.setOutletId(soHeaderDto.getOutletId());
					}
					if (soHeaderDto.getDeliveryDate() != null) {
						ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
					}
					if (soHeaderDto.getLatitude() != null) {
						ltSoHeader.setLatitude(soHeaderDto.getLatitude());
					}
					if (soHeaderDto.getLongitude() != null) {
						ltSoHeader.setLongitude(soHeaderDto.getLongitude());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getAddress() != null) {
						ltSoHeader.setAddress(soHeaderDto.getAddress());
					}
					if (soHeaderDto.getRemark() != null) {
						ltSoHeader.setRemark(soHeaderDto.getRemark());
					}

					ltSoHeader.setOrderDate(new Date()); //new Date()
					ltSoHeader.setLastUpdateDate(new Date()); // new Date()
					ltSoHeader.setCreationDate(new Date()); // new Date()
					//ltSoHeader.setStatus(DRAFT);

					if(soHeaderDto.getInstockFlag()!= null) {
						ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
					}
					if(soHeaderDto.getPriceList()!= null) {
						ltSoHeader.setPriceList(soHeaderDto.getPriceList());
					}
					
				//	considering default priceList
					if(!soHeaderDto.getPriceList().equals(defailtPriceList) ) {
						ltSoHeader.setStatus(DRAFT);
					}else {
						//ltSoHeader.setStatus("APPROVED");
						ltSoHeader.setStatus(DRAFT);
					}
					if(soHeaderDto.getBeatId()!= null) {
						ltSoHeader.setBeatId(soHeaderDto.getBeatId());
					}
					
					ltSoHeader = updateSoHeader(ltSoHeader);
					System.out.println("saved ltSoHeader data is==" +ltSoHeader);
					
//					if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
//							ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList().equals("ALL_INDIA_RDS")) {
//						// considering ALL_INDIA_RDS as default priceList
//						// need to send this reqBody to siebel
//						
//					}else {
//						// inStock order with different priceList need to send for approval to areHead
//						sendNotifications(ltSoHeader);
//					}		
					
					List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
					
				//	StringBuffer strQuery1 =  new StringBuffer();
					
			//		strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
					
			//		StringBuffer strQuery =  new StringBuffer();
					
					for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
						
						SoLineDto soLineDto = (SoLineDto) iterator.next();
						StringBuffer strQuery =  new StringBuffer();
						strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
						
						strQuery.append("(");

						//LtSoLines ltSoLines = new LtSoLines();
						
						if (ltSoHeader.getHeaderId() != null) {
							//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
							strQuery.append(ltSoHeader.getHeaderId()+",");
							
						}
						if (soLineDto.getProductId() != null) {
							//ltSoLines.setProductId(soLineDto.getProductId());
							strQuery.append("'"+soLineDto.getProductId()+"',");
						}
						if (soLineDto.getQuantity() != null) {
							//ltSoLines.setQuantity(soLineDto.getQuantity());
							strQuery.append(soLineDto.getQuantity()+",");
						}
						if (soLineDto.getListPrice() != null) {
							//ltSoLines.setListPrice(soLineDto.getListPrice());
							strQuery.append("'"+soLineDto.getListPrice()+"',");
						}else {
							//ltSoLines.setPtrPrice(soLineDto.getListPrice());
							strQuery.append("'"+soLineDto.getListPrice()+"',");
						}
						strQuery.append("'"+DRAFT.toString()+"',");//Status
						
						if (soHeaderDto.getUserId() != null) {
							//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
							strQuery.append(soHeaderDto.getUserId()+",");
						}
						if (soHeaderDto.getUserId() != null) {
							//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
							strQuery.append(soHeaderDto.getUserId()+",");
						}
						if (soHeaderDto.getUserId() != null) {
							//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
							strQuery.append(soHeaderDto.getUserId()+",");
						}
						if (soLineDto.getPtrPrice() != null) {
							//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
							strQuery.append("'"+soLineDto.getPtrPrice()+"',");
						}else {
							//ltSoLines.setPtrPrice(soLineDto.getListPrice());
							strQuery.append("'"+soLineDto.getPtrPrice()+"',");
						}
					//	if(soLineDto.getEimStatus()!= null) {
						strQuery.append("'"+null+"',"); // eimstatus
					//	} 		
						if (soLineDto.getDeliveryDate() != null) {
							//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
							
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
							Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
							String deliveryDate =outputFormat.format(date);
							System.out.println("formatedDate : " + deliveryDate); 
												
							strQuery.append("'"+deliveryDate+"',");
						//	strQuery.append("'"+"',");
							
						 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
						}
						
						DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
						Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
						Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
						SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
						String crationDate =outputFormat.format(date);
						String lastUpdateDate =outputFormat.format(date1);
						System.out.println("crationDate : " + crationDate);
						System.out.println("lastUpdateDate : " + lastUpdateDate);
						
						//strQuery.append("'"+new Date()+"',");//Created Date
						strQuery.append("'"+crationDate+"',");  // set null for demo
						
						//strQuery.append("'"+new Date()+"'");//Last update date
						strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
						
						//ltSoLines.setStatus(DRAFT);
						//ltSoLines.setLastUpdateDate(new Date());
						//ltSoLines.setCreationDate(new Date());
//						strQuery.append("),");
						
						strQuery.append(")");
						//strQuery1 = strQuery1.append(strQuery);
						//ltSoLines = ltSoLinesRepository.save(ltSoLines);
						String query = strQuery.toString();
						int n = ltSoHeadersDao.insertLine(query);
					}
					 System.out.print("This is default pl beforeeeeeeeeeee");
				// ATFL Phase 2 siebel devlopement		  
					try {
						 if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
								ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList()==defailtPriceList) {
									
							 System.out.println("Hi This is RETAILER instock saveSoHeadeLineInDraftV2 "); 
							// sendEmail(ltSoHeader); 
						// inserting save order data into siebel using saveOrderIntoSiebel()   
							 System.out.print("This is Draft default pl before");
							 //saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							 System.out.print("This is Draft default pl after");						
						// saveOutlet();  inserting the create outlet data into siebel using saveOutlet() 
																	
						}else {
							
						// inStock order with different priceList need to send for approval to areHead
							//if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
							sendNotifications(ltSoHeader);
							//}
							System.out.println("I'm calling send email");
							sendEmail(ltSoHeader);
							
						//    inserting save order data into siebel  if order is approved using sampleCode()   
							System.out.println("Hi This is Draft Sales,distri & or before saveOrderIntoSiebel ");
							//if(ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")) 
							//{
						//	      saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							//}
							System.out.println("Hi This is Draft Not RETAILER after saveOrderIntoSiebel ");
													
						}
						
						RequestDto requestDto = new RequestDto();
						requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
						requestDto.setLimit(-1);
						requestDto.setOffset(2);
					//	requestDto.setLoginId(0L);
						status = getOrderV2(requestDto);
						
						// saving salesPerson details in salesPersonLocation table
						if(user.getUserType().equalsIgnoreCase(SALES)) {
							
							LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
							
							ltSalesPersonLocation.setBeatId(soHeaderDto.getBeatId());
							ltSalesPersonLocation.setOutletId(soHeaderDto.getOutletId());
							ltSalesPersonLocation.setOrderName(orderNumber);
							ltSalesPersonLocation.setAddress(soHeaderDto.getAddress());
							ltSalesPersonLocation.setLatitude(soHeaderDto.getLatitude());
							ltSalesPersonLocation.setLongitude(soHeaderDto.getLongitude());
							ltSalesPersonLocation.setCreatedBy(soHeaderDto.getUserId());
							ltSalesPersonLocation.setLastUpdatedBy(soHeaderDto.getUserId());
							ltSalesPersonLocation.setCreationDate(new Date());
							ltSalesPersonLocation.setLastUpdateDate(new Date());
							
							ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
						}
					   }
						catch (Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}
			            status.setCode(INSERT_SUCCESSFULLY);
						status.setMessage("Insert Successfully");
						//sendNotifications(ltSoHeader);
						return status;
								
				} 
				// for Out_Of_Stock Products order	i.e. isInstockFlag ="N"
					else {
						System.out.println("Hi This is outof stock saveSoHeadeLineInDraftV2 ");
						String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
						//System.out.println("orderNumber :: "+orderNumber);
						
						//Long orderNo = System.currentTimeMillis();
						ltSoHeader.setOrderNumber(orderNumber);
			            
						
						if (soHeaderDto.getOutletId() != null) {
							ltSoHeader.setOutletId(soHeaderDto.getOutletId());
						}
						if (soHeaderDto.getDeliveryDate() != null) {
							ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
						}
						if (soHeaderDto.getLatitude() != null) {
							ltSoHeader.setLatitude(soHeaderDto.getLatitude());
						}
						if (soHeaderDto.getLongitude() != null) {
							ltSoHeader.setLongitude(soHeaderDto.getLongitude());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getAddress() != null) {
							ltSoHeader.setAddress(soHeaderDto.getAddress());
						}
						if (soHeaderDto.getRemark() != null) {
							ltSoHeader.setRemark(soHeaderDto.getRemark());
						}

						ltSoHeader.setOrderDate(new Date()); //new Date()
						ltSoHeader.setLastUpdateDate(new Date()); // new Date()
						ltSoHeader.setCreationDate(new Date()); // new Date()
						//ltSoHeader.setStatus(DRAFT);

						 ltSoHeader.setStatus(DRAFT);
					    						
						if(soHeaderDto.getInstockFlag()!= null) {
							ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
						}
						if(soHeaderDto.getBeatId()!= null) {
							ltSoHeader.setBeatId(soHeaderDto.getBeatId());
						}if(soHeaderDto.getPriceList()!= null) {
							ltSoHeader.setPriceList(soHeaderDto.getPriceList());
						}
						
						ltSoHeader = updateSoHeader(ltSoHeader);
							
						// send OutofStock order for approval to areHead
						//	sendNotifications(ltSoHeader);
						
						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
						//StringBuffer strQuery1 =  new StringBuffer();
								
						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
												
							SoLineDto soLineDto = (SoLineDto) iterator.next();	
							StringBuffer strQuery =  new StringBuffer();
							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
							
							strQuery.append("(");

							//LtSoLines ltSoLines = new LtSoLines();
							
							if (ltSoHeader.getHeaderId() != null) {
								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
								strQuery.append(ltSoHeader.getHeaderId()+",");
								
							}
							if (soLineDto.getProductId() != null) {
								//ltSoLines.setProductId(soLineDto.getProductId());
								strQuery.append("'"+soLineDto.getProductId()+"',");
							}
							if (soLineDto.getQuantity() != null) {
								//ltSoLines.setQuantity(soLineDto.getQuantity());
								strQuery.append(soLineDto.getQuantity()+",");
							}
							if (soLineDto.getListPrice() != null) {
								//ltSoLines.setListPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}else {
								//strQuery.append("'"+null+"',");
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}
							strQuery.append("'"+DRAFT.toString()+"',");//Status
							
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soLineDto.getPtrPrice() != null) {
								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
							}else {
								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
							}
						//	if(soLineDto.getEimStatus()!= null) {
							strQuery.append("'"+null+"',"); // eimstatus
						//	} 		
							if (soLineDto.getDeliveryDate() != null) {
								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
								
								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
								String deliveryDate =outputFormat.format(date);
								System.out.println("formatedDate : " + deliveryDate); 
													
								strQuery.append("'"+deliveryDate+"',");
							//	strQuery.append("'"+"',");
								
							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
							}
							
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
							Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
							Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
							String crationDate =outputFormat.format(date);
							String lastUpdateDate =outputFormat.format(date1);
							System.out.println("crationDate : " + crationDate);
							System.out.println("lastUpdateDate : " + lastUpdateDate);
							
							//strQuery.append("'"+new Date()+"',");//Created Date
							strQuery.append("'"+crationDate+"',");  // set null for demo
							
							//strQuery.append("'"+new Date()+"'");//Last update date
							strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
							
							//ltSoLines.setStatus(DRAFT);
							//ltSoLines.setLastUpdateDate(new Date());
							//ltSoLines.setCreationDate(new Date());
//							strQuery.append("),");
							
							strQuery.append(")");
							String query = strQuery.toString();
							int n = ltSoHeadersDao.insertLine(query);
						}
						
						// outOfStock order need to send for approval to areHead
						//if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {   
						//sendNotifications(ltSoHeader);
						//}
						   //sendEmail(ltSoHeader);
								
						// ATFL Phase 2 siebel devlopement					
						try {
							//if(ltSoHeader.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
								
						  // inserting save order data into siebel using sampleCode() and update the order status to NEW 
							System.out.println("Hi This is Draft outofStock order before saveOrderIntoSiebel ");
							//if(ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")) {
					//		saveOrderIntoSiebel(ltSoHeader, soHeaderDto);//}
							System.out.println("Hi This is Draft outofStock Order after saveOrderIntoSiebel ");	  
							//}
							
							RequestDto requestDto = new RequestDto();
							requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
							requestDto.setLimit(-1);
							requestDto.setOffset(2);
						//	requestDto.setLoginId(0L);
							status = getOrderV2(requestDto);
							
							// saving salesPerson details in salesPersonLocation table
							if(user.getUserType().equalsIgnoreCase(SALES)) {
								
								LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
								
								ltSalesPersonLocation.setBeatId(soHeaderDto.getBeatId());
								ltSalesPersonLocation.setOutletId(soHeaderDto.getOutletId());
								ltSalesPersonLocation.setOrderName(orderNumber);
								ltSalesPersonLocation.setAddress(soHeaderDto.getAddress());
								ltSalesPersonLocation.setLatitude(soHeaderDto.getLatitude());
								ltSalesPersonLocation.setLongitude(soHeaderDto.getLongitude());
								ltSalesPersonLocation.setCreatedBy(soHeaderDto.getUserId());
								ltSalesPersonLocation.setLastUpdatedBy(soHeaderDto.getUserId());
								ltSalesPersonLocation.setCreationDate(new Date());
								ltSalesPersonLocation.setLastUpdateDate(new Date());
								
								ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
							}
						}catch (Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}	
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");
							return status;						
					} 		  
				  
			  }
						
		}catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null; 
	}

*/ // this is end of original menthod comment on 24-may-2024 for optimization	
	
	private Status saveSoHeadeLineInDraftV2(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
		System.out.println("saveSoHeadeLineInDraftV2 = "+new Date());
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserDetailsAgainsUserId = 0;
		long outQuerygetUserDetailsAgainsUserId = 0;
		long inQuerygetDefaultPriceListAgainstOutletId = 0;
		long outQuerygetDefaultPriceListAgainstOutletId = 0;
//		long inQuerygetInStockProductCountForAdmin = 0;
//		long outQuerygetInStockProductCountForAdmin = 0;
//		long inQuerygetMultipleMrpForProduct = 0;
//		long outQuerygetMultipleMrpForProduct = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		
		try {
			System.out.println("Hi This is saveSoHeadeLineInDraftV2 ");
			Status status = new Status();
			// SaveOrderResponseDto saveOrderResponseDto = new SaveOrderResponseDto();
			LtSoHeaders ltSoHeader = new LtSoHeaders();
			inQuerygetUserDetailsAgainsUserId = System.currentTimeMillis();
			  LtMastUsers user = ltSoHeadersDao.getUserDetailsAgainsUserId(soHeaderDto.getUserId());
			  outQuerygetUserDetailsAgainsUserId = System.currentTimeMillis();
			  System.out.println("This user issssssss" + user);
			  if(user.getUserType().equalsIgnoreCase("RETAILER")) 
			  {
				  System.out.println("Hi This is RETAILER saveSoHeadeLineInDraftV2 ");
				  // need to copy here old save order code
				  try {				
						
						String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
						//System.out.println("orderNumber :: "+orderNumber);
						
						//Long orderNo = System.currentTimeMillis();
						ltSoHeader.setOrderNumber(orderNumber);

						if (soHeaderDto.getOutletId() != null) {
							ltSoHeader.setOutletId(soHeaderDto.getOutletId());
						}
						if (soHeaderDto.getDeliveryDate() != null) {
							ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
						}
						if (soHeaderDto.getLatitude() != null) {
							ltSoHeader.setLatitude(soHeaderDto.getLatitude());
						}
						if (soHeaderDto.getLongitude() != null) {
							ltSoHeader.setLongitude(soHeaderDto.getLongitude());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getAddress() != null) {
							ltSoHeader.setAddress(soHeaderDto.getAddress());
						}
						if (soHeaderDto.getRemark() != null) {
							ltSoHeader.setRemark(soHeaderDto.getRemark());
						}

						ltSoHeader.setOrderDate(new Date()); //new Date()
						ltSoHeader.setLastUpdateDate(new Date()); // new Date()
						ltSoHeader.setCreationDate(new Date()); // new Date()
						ltSoHeader.setStatus(DRAFT);

						ltSoHeader = updateSoHeader(ltSoHeader);

						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
					//	StringBuffer strQuery1 =  new StringBuffer();
						
					//	strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
						
					//	StringBuffer strQuery =  new StringBuffer();
//						System.out.println("for retailer = "+new Date());
//						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) { //old dev take one line
//							
//							SoLineDto soLineDto = (SoLineDto) iterator.next();
//							StringBuffer strQuery =  new StringBuffer();
//							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
//
//							strQuery.append("(");
//
//							//LtSoLines ltSoLines = new LtSoLines();
//							
//							if (ltSoHeader.getHeaderId() != null) {
//								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
//								strQuery.append(ltSoHeader.getHeaderId()+",");
//								
//							}
//							if (soLineDto.getProductId() != null) {
//								//ltSoLines.setProductId(soLineDto.getProductId());
//								strQuery.append("'"+soLineDto.getProductId()+"',");
//							}
//							if (soLineDto.getQuantity() != null) {
//								//ltSoLines.setQuantity(soLineDto.getQuantity());
//								strQuery.append(soLineDto.getQuantity()+",");
//							}
//							if (soLineDto.getListPrice() != null) {
//								//ltSoLines.setListPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}
//							if (soLineDto.getDeliveryDate() != null) {
//								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//								strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//							}
//							strQuery.append("'"+DRAFT.toString()+"',");//Status
//							
//
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							strQuery.append("'"+new Date()+"',");//Created Date
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							strQuery.append("'"+new Date()+"',");//Last update date
//							 
//							if (soLineDto.getPtrPrice() != null) {
//								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"'");
//							}else {
//								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"'");
//							}
//							
//							//ltSoLines.setStatus(DRAFT);
//							//ltSoLines.setLastUpdateDate(new Date());
//							//ltSoLines.setCreationDate(new Date());
//							strQuery.append(")");
//							
//							//ltSoLines = ltSoLinesRepository.save(ltSoLines);									
//							String query = strQuery.toString();
//							int n = ltSoHeadersDao.insertLine(query);
							
							

							//old dev
//							SoLineDto soLineDto = (SoLineDto) iterator.next();	
//							StringBuffer strQuery =  new StringBuffer();
//							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
//							
//							strQuery.append("(");
//
//							//LtSoLines ltSoLines = new LtSoLines();
//							
//							if (ltSoHeader.getHeaderId() != null) {
//								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
//								strQuery.append(ltSoHeader.getHeaderId()+",");
//								
//							}
//							if (soLineDto.getProductId() != null) {
//								//ltSoLines.setProductId(soLineDto.getProductId());
//								strQuery.append("'"+soLineDto.getProductId()+"',");
//							}
//							if (soLineDto.getQuantity() != null) {
//								//ltSoLines.setQuantity(soLineDto.getQuantity());
//								strQuery.append(soLineDto.getQuantity()+",");
//							}
//							if (soLineDto.getListPrice() != null) {
//								//ltSoLines.setListPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}else {
//								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}
//							strQuery.append("'"+DRAFT.toString()+"',");//Status
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							if (soLineDto.getPtrPrice() != null) {
//								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
//							}else {
//								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
//							}
//						//	if(soLineDto.getEimStatus()!= null) {
//							strQuery.append("'"+null+"',"); // eimstatus
//						//	} 		
//							if (soLineDto.getDeliveryDate() != null) {
//								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//								
//								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
//								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//								String deliveryDate =outputFormat.format(date);
//								System.out.println("formatedDate : " + deliveryDate); 
//													
//								strQuery.append("'"+deliveryDate+"',");
//							//	strQuery.append("'"+"',");
//								
//							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//							}
//							
//							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//							Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
//							Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
//							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//							String crationDate =outputFormat.format(date);
//							String lastUpdateDate =outputFormat.format(date1);
//							System.out.println("crationDate : " + crationDate);
//							System.out.println("lastUpdateDate : " + lastUpdateDate);
//							
//							//strQuery.append("'"+new Date()+"',");//Created Date
//							strQuery.append("'"+crationDate+"',");  // set null for demo
//							
//							//strQuery.append("'"+new Date()+"'");//Last update date
//							strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
//							
//							//ltSoLines.setStatus(DRAFT);
//							//ltSoLines.setLastUpdateDate(new Date());
//							//ltSoLines.setCreationDate(new Date());
////							strQuery.append("),");
//							
//							strQuery.append(")");
//							String query = strQuery.toString();
//							int n = ltSoHeadersDao.insertLine(query);
//						
//							
//						}				
//							System.out.println("Line insert successfully");
//							System.out.println("for retailer for loop end = "+new Date());
							//old dev
							//new Dev 24-05-2024
							
						 System.out.println("Time calculation for inserting data for retailer 1824 === " + new Date());

					        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
					        DateFormat inputFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");

					        List<String> queries = new ArrayList<>();

					        for (SoLineDto soLineDto : soLineDtoList) {
					            StringBuilder strQuery = new StringBuilder();
					            strQuery.append("INSERT INTO lt_so_lines (header_id, product_id, quantity, list_price, status, created_by, last_update_login, last_updated_by, ptr_price, eimstatus, delivery_date, creation_date, last_update_date) VALUES (");

					            strQuery.append(ltSoHeader.getHeaderId() != null ? ltSoHeader.getHeaderId() : "NULL").append(", ");
					            strQuery.append(soLineDto.getProductId() != null ? "'" + soLineDto.getProductId() + "'" : "NULL").append(", ");
					            strQuery.append(soLineDto.getQuantity() != null ? soLineDto.getQuantity() : "NULL").append(", ");
					            strQuery.append(soLineDto.getListPrice() != null ? "'" + soLineDto.getListPrice() + "'" : "NULL").append(", ");
					            strQuery.append("'").append(DRAFT.toString()).append("', ");
					            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
					            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
					            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
					            strQuery.append(soLineDto.getPtrPrice() != null ? "'" + soLineDto.getPtrPrice() + "'" : "'" + soLineDto.getListPrice() + "'").append(", ");
					            strQuery.append("NULL, "); // eimstatus

					            if (soLineDto.getDeliveryDate() != null) {
					                try {
					                    Date date = inputFormatter.parse(soLineDto.getDeliveryDate().toString());
					                    String deliveryDate = outputFormat.format(date);
					                    strQuery.append("'").append(deliveryDate).append("', ");
					                } catch (Exception e) {
					                    strQuery.append("NULL, ");
					                }
					            } else {
					                strQuery.append("NULL, ");
					            }

					            try {
					                String creationDate = outputFormat.format(inputFormatter.parse(ltSoHeader.getCreationDate().toString()));
					                String lastUpdateDate = outputFormat.format(inputFormatter.parse(ltSoHeader.getLastUpdateDate().toString()));
					                strQuery.append("'").append(creationDate).append("', ");
					                strQuery.append("'").append(lastUpdateDate).append("')");
					            } catch (Exception e) {
					                strQuery.append("NULL, NULL)");
					            }

					            queries.add(strQuery.toString());
					        }

					        int[] updateCounts = ltSoHeadersDao.batchInsert(queries);

					        System.out.println("Inserted " + updateCounts.length + " lines.");
					        System.out.println("1873 = " + new Date());
							//new dev
//					    }
							
					//		sending notification & email for approve order
							//if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")){
							//sendNotifications(ltSoHeader);
							//}
							//sendEmail("Siebel Issues", "Hello Shubham, This email coming from code .", "Shubham.magare@lonartech.com");
					        
					        //old dev
//					        System.out.println("send mail start at = "+new Date());
//							sendEmail(ltSoHeader);
//							System.out.println("send mail end at = "+new Date());
							//old dev
					        
					     // Asynchronously send the email
					        System.out.println("ltSoHeader = "+ltSoHeader);
					        final LtSoHeaders ltSoHeadersForMailSent = ltSoHeader;
					        System.out.println("_________________________");
					        System.out.println("ltSoHeadersForMailSent = "+ltSoHeadersForMailSent);
			                CompletableFuture.runAsync(() -> {
			                    try {
			                        sendEmail(ltSoHeadersForMailSent);
			                    } catch (Exception e) {
			                        e.printStackTrace();
			                    }
			                }, executor);
							
					//    inserting save order data into siebel   
							System.out.println("Hi This is in Draft RETAILER before saveOrderIntoSiebel ");
							//if(ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")){
					//		saveOrderIntoSiebel(ltSoHeader, soHeaderDto);//}
							System.out.println("Hi This is in Draft RETAILER after saveOrderIntoSiebel ");
								  
							RequestDto requestDto = new RequestDto();
							requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
							requestDto.setLimit(-1);
							requestDto.setOffset(2);
							//requestDto.setUserId(soHeaderDto.getUserId());
							status = getOrderV2(requestDto);
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");
							//sendNotifications(ltSoHeader);
							return status;
						
					} catch (Exception e) {
						logger.error("Error Description :", e);
						e.printStackTrace();
					}
					return null;
				  				  
				  
			  }
			  
			  else  if(user.getUserType().equalsIgnoreCase(SALES) || user.getUserType().equalsIgnoreCase(DISTRIBUTOR) ||
					    user.getUserType().equalsIgnoreCase(SYSTEMADMINISTRATOR))
			    {
				   // for Instock products order
				  System.out.println("Hi This is Draft for instock saveSoHeadeLineInDraftV2 ");
				  inQuerygetDefaultPriceListAgainstOutletId = System.currentTimeMillis();
				  String defailtPriceList = ltSoHeadersDao.getDefaultPriceListAgainstOutletId(soHeaderDto.getOutletId());
				  outQuerygetDefaultPriceListAgainstOutletId = System.currentTimeMillis();

				   if(soHeaderDto.getInstockFlag().equals("Y")) {
						
					String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
					//System.out.println("orderNumber :: "+orderNumber);
					
					//Long orderNo = System.currentTimeMillis();
					ltSoHeader.setOrderNumber(orderNumber);
		            					
					if (soHeaderDto.getOutletId() != null) {
						ltSoHeader.setOutletId(soHeaderDto.getOutletId());
					}
					if (soHeaderDto.getDeliveryDate() != null) {
						ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
					}
					if (soHeaderDto.getLatitude() != null) {
						ltSoHeader.setLatitude(soHeaderDto.getLatitude());
					}
					if (soHeaderDto.getLongitude() != null) {
						ltSoHeader.setLongitude(soHeaderDto.getLongitude());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getAddress() != null) {
						ltSoHeader.setAddress(soHeaderDto.getAddress());
					}
					if (soHeaderDto.getRemark() != null) {
						ltSoHeader.setRemark(soHeaderDto.getRemark());
					}

					ltSoHeader.setOrderDate(new Date()); //new Date()
					ltSoHeader.setLastUpdateDate(new Date()); // new Date()
					ltSoHeader.setCreationDate(new Date()); // new Date()
					//ltSoHeader.setStatus(DRAFT);

					if(soHeaderDto.getInstockFlag()!= null) {
						ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
					}
					if(soHeaderDto.getPriceList()!= null) {
						ltSoHeader.setPriceList(soHeaderDto.getPriceList());
					}
					
				//	considering default priceList
					if(!soHeaderDto.getPriceList().equals(defailtPriceList) ) {
						ltSoHeader.setStatus(DRAFT);
					}else {
						//ltSoHeader.setStatus("APPROVED");
						ltSoHeader.setStatus(DRAFT);
					}
					if(soHeaderDto.getBeatId()!= null) {
						ltSoHeader.setBeatId(soHeaderDto.getBeatId());
					}
					
					ltSoHeader = updateSoHeader(ltSoHeader);
					System.out.println("saved ltSoHeader data is==" +ltSoHeader);
					
//					if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
//							ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList().equals("ALL_INDIA_RDS")) {
//						// considering ALL_INDIA_RDS as default priceList
//						// need to send this reqBody to siebel
//						
//					}else {
//						// inStock order with different priceList need to send for approval to areHead
//						sendNotifications(ltSoHeader);
//					}		
					
					List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
					
				//	StringBuffer strQuery1 =  new StringBuffer();
					
			//		strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
					
			//		StringBuffer strQuery =  new StringBuffer();
					
					//old dev
					
//					System.out.println("Time calculation for inserting data = "+new Date());
//					System.out.println("1990 = "+new Date());
//					for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
//						
//						SoLineDto soLineDto = (SoLineDto) iterator.next();
//						StringBuffer strQuery =  new StringBuffer();
//						strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
//						
//						strQuery.append("(");
//
//						//LtSoLines ltSoLines = new LtSoLines();
//						
//						if (ltSoHeader.getHeaderId() != null) {
//							//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
//							strQuery.append(ltSoHeader.getHeaderId()+",");
//							
//						}
//						if (soLineDto.getProductId() != null) {
//							//ltSoLines.setProductId(soLineDto.getProductId());
//							strQuery.append("'"+soLineDto.getProductId()+"',");
//						}
//						if (soLineDto.getQuantity() != null) {
//							//ltSoLines.setQuantity(soLineDto.getQuantity());
//							strQuery.append(soLineDto.getQuantity()+",");
//						}
//						if (soLineDto.getListPrice() != null) {
//							//ltSoLines.setListPrice(soLineDto.getListPrice());
//							strQuery.append("'"+soLineDto.getListPrice()+"',");
//						}else {
//							//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//							strQuery.append("'"+soLineDto.getListPrice()+"',");
//						}
//						strQuery.append("'"+DRAFT.toString()+"',");//Status
//						
//						if (soHeaderDto.getUserId() != null) {
//							//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//							strQuery.append(soHeaderDto.getUserId()+",");
//						}
//						if (soHeaderDto.getUserId() != null) {
//							//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//							strQuery.append(soHeaderDto.getUserId()+",");
//						}
//						if (soHeaderDto.getUserId() != null) {
//							//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//							strQuery.append(soHeaderDto.getUserId()+",");
//						}
//						if (soLineDto.getPtrPrice() != null) {
//							//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//							strQuery.append("'"+soLineDto.getPtrPrice()+"',");
//						}else {
//							//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//							strQuery.append("'"+soLineDto.getPtrPrice()+"',");
//						}
//					//	if(soLineDto.getEimStatus()!= null) {
//						strQuery.append("'"+null+"',"); // eimstatus
//					//	} 		
//						if (soLineDto.getDeliveryDate() != null) {
//							//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//							
//							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//							Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
//							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//							String deliveryDate =outputFormat.format(date);
//							System.out.println("formatedDate : " + deliveryDate); 
//												
//							strQuery.append("'"+deliveryDate+"',");
//						//	strQuery.append("'"+"',");
//							
//						 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//						}
//						
//						DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//						Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
//						Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
//						SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//						String crationDate =outputFormat.format(date);
//						String lastUpdateDate =outputFormat.format(date1);
//						System.out.println("crationDate : " + crationDate);
//						System.out.println("lastUpdateDate : " + lastUpdateDate);
//						
//						//strQuery.append("'"+new Date()+"',");//Created Date
//						strQuery.append("'"+crationDate+"',");  // set null for demo
//						
//						//strQuery.append("'"+new Date()+"'");//Last update date
//						strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
//						
//						//ltSoLines.setStatus(DRAFT);
//						//ltSoLines.setLastUpdateDate(new Date());
//						//ltSoLines.setCreationDate(new Date());
////						strQuery.append("),");
//						
//						strQuery.append(")");
//						//strQuery1 = strQuery1.append(strQuery);
//						//ltSoLines = ltSoLinesRepository.save(ltSoLines);
//						String query = strQuery.toString();
//						int n = ltSoHeadersDao.insertLine(query);
//					}
//					System.out.println("2086 = "+new Date());
					
					//old dev
					
					// new dev
					
					System.out.println("Time calculation for inserting data for sales,distributor,sysAdmin= 2119" + new Date());

			        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
			        DateFormat inputFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");

			        List<String> queries = new ArrayList<>();

			        for (SoLineDto soLineDto : soLineDtoList) {
			            StringBuilder strQuery = new StringBuilder();
			            strQuery.append("INSERT INTO lt_so_lines (header_id, product_id, quantity, list_price, status, created_by, last_update_login, last_updated_by, ptr_price, eimstatus, delivery_date, creation_date, last_update_date) VALUES (");

			            strQuery.append(ltSoHeader.getHeaderId() != null ? ltSoHeader.getHeaderId() : "NULL").append(", ");
			            strQuery.append(soLineDto.getProductId() != null ? "'" + soLineDto.getProductId() + "'" : "NULL").append(", ");
			            strQuery.append(soLineDto.getQuantity() != null ? soLineDto.getQuantity() : "NULL").append(", ");
			            strQuery.append(soLineDto.getListPrice() != null ? "'" + soLineDto.getListPrice() + "'" : "NULL").append(", ");
			            strQuery.append("'").append(DRAFT.toString()).append("', ");
			            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
			            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
			            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
			            strQuery.append(soLineDto.getPtrPrice() != null ? "'" + soLineDto.getPtrPrice() + "'" : "'" + soLineDto.getListPrice() + "'").append(", ");
			            strQuery.append("NULL, "); // eimstatus

			            if (soLineDto.getDeliveryDate() != null) {
			                try {
			                    Date date = inputFormatter.parse(soLineDto.getDeliveryDate().toString());
			                    String deliveryDate = outputFormat.format(date);
			                    strQuery.append("'").append(deliveryDate).append("', ");
			                } catch (Exception e) {
			                    strQuery.append("NULL, ");
			                }
			            } else {
			                strQuery.append("NULL, ");
			            }

			            try {
			                String creationDate = outputFormat.format(inputFormatter.parse(ltSoHeader.getCreationDate().toString()));
			                String lastUpdateDate = outputFormat.format(inputFormatter.parse(ltSoHeader.getLastUpdateDate().toString()));
			                strQuery.append("'").append(creationDate).append("', ");
			                strQuery.append("'").append(lastUpdateDate).append("')");
			            } catch (Exception e) {
			                strQuery.append("NULL, NULL)");
			            }

			            queries.add(strQuery.toString());
			        }

			        int[] updateCounts = ltSoHeadersDao.batchInsert(queries);

			        System.out.println("Inserted " + updateCounts.length + " lines.");
			        System.out.println("2145 = " + new Date());
			        
			        //new dev
			        
					 System.out.print("This is default pl beforeeeeeeeeeee");
				// ATFL Phase 2 siebel devlopement		  
					try {
						 if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
								ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList()==defailtPriceList) {
									
							 System.out.println("Hi This is RETAILER instock saveSoHeadeLineInDraftV2 "); 
							// sendEmail(ltSoHeader); 
						// inserting save order data into siebel using saveOrderIntoSiebel()   
							 System.out.print("This is Draft default pl before");
							 //saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							 System.out.print("This is Draft default pl after");						
						// saveOutlet();  inserting the create outlet data into siebel using saveOutlet() 
																	
						}else {
							
						// inStock order with different priceList need to send for approval to areHead
							//if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
//							sendNotifications(ltSoHeader);//old dev
							//}
							System.out.println("I'm calling send email in sales, distributor, sysadmin ");
//							sendEmail(ltSoHeader);//old dev
							final LtSoHeaders ltSoHeaderForSentMail = ltSoHeader;
							CompletableFuture.runAsync(() -> {
			                    try {
			                    	System.out.println("in ascyc method...");
			                    	sendNotifications(ltSoHeaderForSentMail);
			                        sendEmail(ltSoHeaderForSentMail);
			                    } catch (Exception e) {
			                        e.printStackTrace();
			                    }
			                }, executor);
							
						//    inserting save order data into siebel  if order is approved using sampleCode()   
							System.out.println("Hi This is Draft Sales,distri & or before saveOrderIntoSiebel ");
							//if(ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")) 
							//{
						//	      saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							//}
							System.out.println("Hi This is Draft Not RETAILER after saveOrderIntoSiebel ");
													
						}
						
						RequestDto requestDto = new RequestDto();
						requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
						requestDto.setLimit(-1);
						requestDto.setOffset(2);
					//	requestDto.setUserId(soHeaderDto.getUserId());
					//	requestDto.setLoginId(0L);
						status = getOrderV2(requestDto);
						
						// saving salesPerson details in salesPersonLocation table
						if(user.getUserType().equalsIgnoreCase(SALES)) {
							
							LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
							
							ltSalesPersonLocation.setBeatId(soHeaderDto.getBeatId());
							ltSalesPersonLocation.setOutletId(soHeaderDto.getOutletId());
							ltSalesPersonLocation.setOrderName(orderNumber);
							ltSalesPersonLocation.setAddress(soHeaderDto.getAddress());
							ltSalesPersonLocation.setLatitude(soHeaderDto.getLatitude());
							ltSalesPersonLocation.setLongitude(soHeaderDto.getLongitude());
							ltSalesPersonLocation.setCreatedBy(soHeaderDto.getUserId());
							ltSalesPersonLocation.setLastUpdatedBy(soHeaderDto.getUserId());
							ltSalesPersonLocation.setCreationDate(new Date());
							ltSalesPersonLocation.setLastUpdateDate(new Date());
							
							ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
						}
					   }
						catch (Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}
			            status.setCode(INSERT_SUCCESSFULLY);
						status.setMessage("Insert Successfully");
						//sendNotifications(ltSoHeader);
						return status;
								
				} 
				// for Out_Of_Stock Products order	i.e. isInstockFlag ="N"
					else {
						System.out.println("Hi This is outof stock saveSoHeadeLineInDraftV2 ");
						String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
						//System.out.println("orderNumber :: "+orderNumber);
						
						//Long orderNo = System.currentTimeMillis();
						ltSoHeader.setOrderNumber(orderNumber);
			            
						
						if (soHeaderDto.getOutletId() != null) {
							ltSoHeader.setOutletId(soHeaderDto.getOutletId());
						}
						if (soHeaderDto.getDeliveryDate() != null) {
							ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
						}
						if (soHeaderDto.getLatitude() != null) {
							ltSoHeader.setLatitude(soHeaderDto.getLatitude());
						}
						if (soHeaderDto.getLongitude() != null) {
							ltSoHeader.setLongitude(soHeaderDto.getLongitude());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getAddress() != null) {
							ltSoHeader.setAddress(soHeaderDto.getAddress());
						}
						if (soHeaderDto.getRemark() != null) {
							ltSoHeader.setRemark(soHeaderDto.getRemark());
						}

						ltSoHeader.setOrderDate(new Date()); //new Date()
						ltSoHeader.setLastUpdateDate(new Date()); // new Date()
						ltSoHeader.setCreationDate(new Date()); // new Date()
						//ltSoHeader.setStatus(DRAFT);

						 ltSoHeader.setStatus(DRAFT);
					    						
						if(soHeaderDto.getInstockFlag()!= null) {
							ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
						}
						if(soHeaderDto.getBeatId()!= null) {
							ltSoHeader.setBeatId(soHeaderDto.getBeatId());
						}if(soHeaderDto.getPriceList()!= null) {
							ltSoHeader.setPriceList(soHeaderDto.getPriceList());
						}
						
						ltSoHeader = updateSoHeader(ltSoHeader);
							
						// send OutofStock order for approval to areHead
						//	sendNotifications(ltSoHeader);
						
						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
						//StringBuffer strQuery1 =  new StringBuffer();
							
						//old dev
//						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
//												
//							SoLineDto soLineDto = (SoLineDto) iterator.next();	
//							StringBuffer strQuery =  new StringBuffer();
//							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
//							
//							strQuery.append("(");
//
//							//LtSoLines ltSoLines = new LtSoLines();
//							
//							if (ltSoHeader.getHeaderId() != null) {
//								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
//								strQuery.append(ltSoHeader.getHeaderId()+",");
//								
//							}
//							if (soLineDto.getProductId() != null) {
//								//ltSoLines.setProductId(soLineDto.getProductId());
//								strQuery.append("'"+soLineDto.getProductId()+"',");
//							}
//							if (soLineDto.getQuantity() != null) {
//								//ltSoLines.setQuantity(soLineDto.getQuantity());
//								strQuery.append(soLineDto.getQuantity()+",");
//							}
//							if (soLineDto.getListPrice() != null) {
//								//ltSoLines.setListPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}else {
//								//strQuery.append("'"+null+"',");
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}
//							strQuery.append("'"+DRAFT.toString()+"',");//Status
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							if (soLineDto.getPtrPrice() != null) {
//								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
//							}else {
//								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
//							}
//						//	if(soLineDto.getEimStatus()!= null) {
//							strQuery.append("'"+null+"',"); // eimstatus
//						//	} 		
//							if (soLineDto.getDeliveryDate() != null) {
//								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//								
//								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
//								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//								String deliveryDate =outputFormat.format(date);
//								System.out.println("formatedDate : " + deliveryDate); 
//													
//								strQuery.append("'"+deliveryDate+"',");
//							//	strQuery.append("'"+"',");
//								
//							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//							}
//							
//							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//							Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
//							Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
//							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//							String crationDate =outputFormat.format(date);
//							String lastUpdateDate =outputFormat.format(date1);
//							System.out.println("crationDate : " + crationDate);
//							System.out.println("lastUpdateDate : " + lastUpdateDate);
//							
//							//strQuery.append("'"+new Date()+"',");//Created Date
//							strQuery.append("'"+crationDate+"',");  // set null for demo
//							
//							//strQuery.append("'"+new Date()+"'");//Last update date
//							strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
//							
//							//ltSoLines.setStatus(DRAFT);
//							//ltSoLines.setLastUpdateDate(new Date());
//							//ltSoLines.setCreationDate(new Date());
////							strQuery.append("),");
//							
//							strQuery.append(")");
//							String query = strQuery.toString();
//							int n = ltSoHeadersDao.insertLine(query);
//						}
						//old dev
						
						//new dev
						
						System.out.println("Time calculation for inserting data for out of stock= 2402" + new Date());

				        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
				        DateFormat inputFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");

				        List<String> queries = new ArrayList<>();

				        for (SoLineDto soLineDto : soLineDtoList) {
				            StringBuilder strQuery = new StringBuilder();
				            strQuery.append("INSERT INTO lt_so_lines (header_id, product_id, quantity, list_price, status, created_by, last_update_login, last_updated_by, ptr_price, eimstatus, delivery_date, creation_date, last_update_date) VALUES (");

				            strQuery.append(ltSoHeader.getHeaderId() != null ? ltSoHeader.getHeaderId() : "NULL").append(", ");
				            strQuery.append(soLineDto.getProductId() != null ? "'" + soLineDto.getProductId() + "'" : "NULL").append(", ");
				            strQuery.append(soLineDto.getQuantity() != null ? soLineDto.getQuantity() : "NULL").append(", ");
				            strQuery.append(soLineDto.getListPrice() != null ? "'" + soLineDto.getListPrice() + "'" : "NULL").append(", ");
				            strQuery.append("'").append(DRAFT.toString()).append("', ");
				            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
				            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
				            strQuery.append(soHeaderDto.getUserId() != null ? soHeaderDto.getUserId() : "NULL").append(", ");
				            strQuery.append(soLineDto.getPtrPrice() != null ? "'" + soLineDto.getPtrPrice() + "'" : "'" + soLineDto.getListPrice() + "'").append(", ");
				            strQuery.append("NULL, "); // eimstatus

				            if (soLineDto.getDeliveryDate() != null) {
				                try {
				                    Date date = inputFormatter.parse(soLineDto.getDeliveryDate().toString());
				                    String deliveryDate = outputFormat.format(date);
				                    strQuery.append("'").append(deliveryDate).append("', ");
				                } catch (Exception e) {
				                    strQuery.append("NULL, ");
				                }
				            } else {
				                strQuery.append("NULL, ");
				            }

				            try {
				                String creationDate = outputFormat.format(inputFormatter.parse(ltSoHeader.getCreationDate().toString()));
				                String lastUpdateDate = outputFormat.format(inputFormatter.parse(ltSoHeader.getLastUpdateDate().toString()));
				                strQuery.append("'").append(creationDate).append("', ");
				                strQuery.append("'").append(lastUpdateDate).append("')");
				            } catch (Exception e) {
				                strQuery.append("NULL, NULL)");
				            }

				            queries.add(strQuery.toString());
				        }

				        int[] updateCounts = ltSoHeadersDao.batchInsert(queries);

				        System.out.println("Inserted " + updateCounts.length + " lines.");
				        System.out.println("2451 = " + new Date());
						
						
						// outOfStock order need to send for approval to areHead
						//if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {   
						//sendNotifications(ltSoHeader);
						//}
						   //sendEmail(ltSoHeader);
								
						// ATFL Phase 2 siebel devlopement					
						try {
							//if(ltSoHeader.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
								
						  // inserting save order data into siebel using sampleCode() and update the order status to NEW 
							System.out.println("Hi This is Draft outofStock order before saveOrderIntoSiebel ");
							//if(ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")) {
					//		saveOrderIntoSiebel(ltSoHeader, soHeaderDto);//}
							System.out.println("Hi This is Draft outofStock Order after saveOrderIntoSiebel ");	  
							//}
							
							RequestDto requestDto = new RequestDto();
							requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
							requestDto.setLimit(-1);
							requestDto.setOffset(2);
						//	requestDto.setUserId(soHeaderDto.getUserId());
						//	requestDto.setLoginId(0L);
							status = getOrderV2(requestDto);
							
							// saving salesPerson details in salesPersonLocation table
							if(user.getUserType().equalsIgnoreCase(SALES)) {
								
								LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
								
								ltSalesPersonLocation.setBeatId(soHeaderDto.getBeatId());
								ltSalesPersonLocation.setOutletId(soHeaderDto.getOutletId());
								ltSalesPersonLocation.setOrderName(orderNumber);
								ltSalesPersonLocation.setAddress(soHeaderDto.getAddress());
								ltSalesPersonLocation.setLatitude(soHeaderDto.getLatitude());
								ltSalesPersonLocation.setLongitude(soHeaderDto.getLongitude());
								ltSalesPersonLocation.setCreatedBy(soHeaderDto.getUserId());
								ltSalesPersonLocation.setLastUpdatedBy(soHeaderDto.getUserId());
								ltSalesPersonLocation.setCreationDate(new Date());
								ltSalesPersonLocation.setLastUpdateDate(new Date());
								
								ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
							}
						}catch (Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}	
						
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");
							timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserDetailsAgainsUserId,outQuerygetUserDetailsAgainsUserId));
							timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetDefaultPriceListAgainstOutletId,outQuerygetDefaultPriceListAgainstOutletId));
//							timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
//							timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
//							timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//							timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
//							
							long methodOut = System.currentTimeMillis();
							System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
					        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
					        status.setTimeDifference(timeDifference);
							return status;						
					} 		  
				  
			  }
						
		}catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}finally {
	        executor.shutdown();
	        System.out.println("connection closed");
	    }
		return null;
		
	}	
	
	// New method for save order without notification
	
	private Status saveSoHeadeLineInDraftInternal(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
		try {
			Status status = new Status();
			// SaveOrderResponseDto saveOrderResponseDto = new SaveOrderResponseDto();
			LtSoHeaders ltSoHeader = new LtSoHeaders();
			
			  LtMastUsers user = ltSoHeadersDao.getUserDetailsAgainsUserId(soHeaderDto.getUserId());
			
			  if(user.getUserType().equalsIgnoreCase("RETAILER")) 
			  {
				  // need to copy here old save order code
				  try {				
						
						String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
						//System.out.println("orderNumber :: "+orderNumber);
						
						//Long orderNo = System.currentTimeMillis();
						ltSoHeader.setOrderNumber(orderNumber);

						if (soHeaderDto.getOutletId() != null) {
							ltSoHeader.setOutletId(soHeaderDto.getOutletId());
						}
						if (soHeaderDto.getDeliveryDate() != null) {
							ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
						}
						if (soHeaderDto.getLatitude() != null) {
							ltSoHeader.setLatitude(soHeaderDto.getLatitude());
						}
						if (soHeaderDto.getLongitude() != null) {
							ltSoHeader.setLongitude(soHeaderDto.getLongitude());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getAddress() != null) {
							ltSoHeader.setAddress(soHeaderDto.getAddress());
						}
						if (soHeaderDto.getRemark() != null) {
							ltSoHeader.setRemark(soHeaderDto.getRemark());
						}

						ltSoHeader.setOrderDate(new Date()); //new Date()
						ltSoHeader.setLastUpdateDate(new Date()); // new Date()
						ltSoHeader.setCreationDate(new Date()); // new Date()
						ltSoHeader.setStatus(DRAFT);

						ltSoHeader = updateSoHeader(ltSoHeader);

						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
					//	StringBuffer strQuery1 =  new StringBuffer();
						
					//	strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
						
					//	StringBuffer strQuery =  new StringBuffer();
						
						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
//							
//							SoLineDto soLineDto = (SoLineDto) iterator.next();
//							StringBuffer strQuery =  new StringBuffer();
//							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
//
//							strQuery.append("(");
//
//							//LtSoLines ltSoLines = new LtSoLines();
//							
//							if (ltSoHeader.getHeaderId() != null) {
//								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
//								strQuery.append(ltSoHeader.getHeaderId()+",");
//								
//							}
//							if (soLineDto.getProductId() != null) {
//								//ltSoLines.setProductId(soLineDto.getProductId());
//								strQuery.append("'"+soLineDto.getProductId()+"',");
//							}
//							if (soLineDto.getQuantity() != null) {
//								//ltSoLines.setQuantity(soLineDto.getQuantity());
//								strQuery.append(soLineDto.getQuantity()+",");
//							}
//							if (soLineDto.getListPrice() != null) {
//								//ltSoLines.setListPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"',");
//							}
//							if (soLineDto.getDeliveryDate() != null) {
//								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//								strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//							}
//							strQuery.append("'"+DRAFT.toString()+"',");//Status
//							
//
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							strQuery.append("'"+new Date()+"',");//Created Date
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							if (soHeaderDto.getUserId() != null) {
//								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//								strQuery.append(soHeaderDto.getUserId()+",");
//							}
//							
//							strQuery.append("'"+new Date()+"',");//Last update date
//							 
//							if (soLineDto.getPtrPrice() != null) {
//								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//								strQuery.append("'"+soLineDto.getPtrPrice()+"'");
//							}else {
//								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//								strQuery.append("'"+soLineDto.getListPrice()+"'");
//							}
//							
//							//ltSoLines.setStatus(DRAFT);
//							//ltSoLines.setLastUpdateDate(new Date());
//							//ltSoLines.setCreationDate(new Date());
//							strQuery.append(")");
//							
//							//ltSoLines = ltSoLinesRepository.save(ltSoLines);									
//							String query = strQuery.toString();
//							int n = ltSoHeadersDao.insertLine(query);
							
							

							
							SoLineDto soLineDto = (SoLineDto) iterator.next();	
							StringBuffer strQuery =  new StringBuffer();
							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
							
							strQuery.append("(");

							//LtSoLines ltSoLines = new LtSoLines();
							
							if (ltSoHeader.getHeaderId() != null) {
								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
								strQuery.append(ltSoHeader.getHeaderId()+",");
								
							}
							if (soLineDto.getProductId() != null) {
								//ltSoLines.setProductId(soLineDto.getProductId());
								strQuery.append("'"+soLineDto.getProductId()+"',");
							}
							if (soLineDto.getQuantity() != null) {
								//ltSoLines.setQuantity(soLineDto.getQuantity());
								strQuery.append(soLineDto.getQuantity()+",");
							}
							if (soLineDto.getListPrice() != null) {
								//ltSoLines.setListPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}else {
								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}
							strQuery.append("'"+DRAFT.toString()+"',");//Status
							
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soLineDto.getPtrPrice() != null) {
								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
							}else {
								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}
						//	if(soLineDto.getEimStatus()!= null) {
							strQuery.append("'"+null+"',"); // eimstatus
						//	} 		
							if (soLineDto.getDeliveryDate() != null) {
								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
								
								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
								String deliveryDate =outputFormat.format(date);
								System.out.println("formatedDate : " + deliveryDate); 
													
								strQuery.append("'"+deliveryDate+"',");
							//	strQuery.append("'"+"',");
								
							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
							}
							
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
							Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
							Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
							String crationDate =outputFormat.format(date);
							String lastUpdateDate =outputFormat.format(date1);
							System.out.println("crationDate : " + crationDate);
							System.out.println("lastUpdateDate : " + lastUpdateDate);
							
							//strQuery.append("'"+new Date()+"',");//Created Date
							strQuery.append("'"+crationDate+"',");  // set null for demo
							
							//strQuery.append("'"+new Date()+"'");//Last update date
							strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
							
							//ltSoLines.setStatus(DRAFT);
							//ltSoLines.setLastUpdateDate(new Date());
							//ltSoLines.setCreationDate(new Date());
//							strQuery.append("),");
							
							strQuery.append(")");
							String query = strQuery.toString();
							int n = ltSoHeadersDao.insertLine(query);
						
							
						}				
							System.out.println("Line insert successfully");
							
					//		sending notification & email for approve order
						//	sendNotifications(ltSoHeader);
						//	sendEmail(ltSoHeader);
							
					//    inserting save order data into siebel   
							System.out.println("Hi This is RETAILER before saveOrderIntoSiebel ");
						//	saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							System.out.println("Hi This is RETAILER after saveOrderIntoSiebel ");
								  
							RequestDto requestDto = new RequestDto();
							requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
							requestDto.setLimit(-1);
							requestDto.setOffset(2);
					//		requestDto.setUserId(0L);
							status = getOrderV2(requestDto);
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");
							sendNotifications(ltSoHeader);
							saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							return status;
						
					} catch (Exception e) {
						logger.error("Error Description :", e);
						e.printStackTrace();
					}
					return null;
				  				  
				  
			  }else  if(user.getUserType().equalsIgnoreCase(SALES) || user.getUserType().equalsIgnoreCase(DISTRIBUTOR) ||
					    user.getUserType().equalsIgnoreCase(SYSTEMADMINISTRATOR))
			    {
				   // for Instock products order
				  
				  String defailtPriceList = ltSoHeadersDao.getDefaultPriceListAgainstOutletId(soHeaderDto.getOutletId());
				  
				   if(soHeaderDto.getInstockFlag().equals("Y")) {
						
					String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
					//System.out.println("orderNumber :: "+orderNumber);
					
					//Long orderNo = System.currentTimeMillis();
					ltSoHeader.setOrderNumber(orderNumber);
		            					
					if (soHeaderDto.getOutletId() != null) {
						ltSoHeader.setOutletId(soHeaderDto.getOutletId());
					}
					if (soHeaderDto.getDeliveryDate() != null) {
						ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
					}
					if (soHeaderDto.getLatitude() != null) {
						ltSoHeader.setLatitude(soHeaderDto.getLatitude());
					}
					if (soHeaderDto.getLongitude() != null) {
						ltSoHeader.setLongitude(soHeaderDto.getLongitude());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getUserId() != null) {
						ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
					}
					if (soHeaderDto.getAddress() != null) {
						ltSoHeader.setAddress(soHeaderDto.getAddress());
					}
					if (soHeaderDto.getRemark() != null) {
						ltSoHeader.setRemark(soHeaderDto.getRemark());
					}

					ltSoHeader.setOrderDate(new Date()); //new Date()
					ltSoHeader.setLastUpdateDate(new Date()); // new Date()
					ltSoHeader.setCreationDate(new Date()); // new Date()
					//ltSoHeader.setStatus(DRAFT);

					if(soHeaderDto.getInstockFlag()!= null) {
						ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
					}
					if(soHeaderDto.getPriceList()!= null) {
						ltSoHeader.setPriceList(soHeaderDto.getPriceList());
					}
					
				//	considering default priceList
					if(!soHeaderDto.getPriceList().equals(defailtPriceList) ) {
				 	          ltSoHeader.setStatus(DRAFT);
					}else {
						//ltSoHeader.setStatus("APPROVED");
						ltSoHeader.setStatus(DRAFT);
					}
					if(soHeaderDto.getBeatId()!= null) {
						ltSoHeader.setBeatId(soHeaderDto.getBeatId());
					}
					
					ltSoHeader = updateSoHeader(ltSoHeader);
					System.out.println("saved ltSoHeader data is==" +ltSoHeader);
					
//					if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
//							ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList().equals("ALL_INDIA_RDS")) {
//						// considering ALL_INDIA_RDS as default priceList
//						// need to send this reqBody to siebel
//						
//					}else {
//						// inStock order with different priceList need to send for approval to areHead
//						sendNotifications(ltSoHeader);
//					}		
					
					List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
					
				//	StringBuffer strQuery1 =  new StringBuffer();
					
			//		strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
					
			//		StringBuffer strQuery =  new StringBuffer();
					
					for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
						
						SoLineDto soLineDto = (SoLineDto) iterator.next();
						StringBuffer strQuery =  new StringBuffer();
						strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
						
						strQuery.append("(");

						//LtSoLines ltSoLines = new LtSoLines();
						
						if (ltSoHeader.getHeaderId() != null) {
							//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
							strQuery.append(ltSoHeader.getHeaderId()+",");
							
						}
						if (soLineDto.getProductId() != null) {
							//ltSoLines.setProductId(soLineDto.getProductId());
							strQuery.append("'"+soLineDto.getProductId()+"',");
						}
						if (soLineDto.getQuantity() != null) {
							//ltSoLines.setQuantity(soLineDto.getQuantity());
							strQuery.append(soLineDto.getQuantity()+",");
						}
						if (soLineDto.getListPrice() != null) {
							//ltSoLines.setListPrice(soLineDto.getListPrice());
							strQuery.append("'"+soLineDto.getListPrice()+"',");
						}else {
							//ltSoLines.setPtrPrice(soLineDto.getListPrice());
							strQuery.append("'"+soLineDto.getListPrice()+"',");
						}
						strQuery.append("'"+DRAFT.toString()+"',");//Status
						
						if (soHeaderDto.getUserId() != null) {
							//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
							strQuery.append(soHeaderDto.getUserId()+",");
						}
						if (soHeaderDto.getUserId() != null) {
							//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
							strQuery.append(soHeaderDto.getUserId()+",");
						}
						if (soHeaderDto.getUserId() != null) {
							//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
							strQuery.append(soHeaderDto.getUserId()+",");
						}
						if (soLineDto.getPtrPrice() != null) {
							//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
							strQuery.append("'"+soLineDto.getPtrPrice()+"',");
						}else {
							//ltSoLines.setPtrPrice(soLineDto.getListPrice());
							strQuery.append("'"+soLineDto.getListPrice()+"',");
						}
					//	if(soLineDto.getEimStatus()!= null) {
						strQuery.append("'"+null+"',"); // eimstatus
					//	} 		
						if (soLineDto.getDeliveryDate() != null) {
							//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
							
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
							Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
							String deliveryDate =outputFormat.format(date);
							System.out.println("formatedDate : " + deliveryDate); 
												
							strQuery.append("'"+deliveryDate+"',");
						//	strQuery.append("'"+"',");
							
						 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
						}
						
						DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
						Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
						Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
						SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
						String crationDate =outputFormat.format(date);
						String lastUpdateDate =outputFormat.format(date1);
						System.out.println("crationDate : " + crationDate);
						System.out.println("lastUpdateDate : " + lastUpdateDate);
						
						//strQuery.append("'"+new Date()+"',");//Created Date
						strQuery.append("'"+crationDate+"',");  // set null for demo
						
						//strQuery.append("'"+new Date()+"'");//Last update date
						strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
						
						//ltSoLines.setStatus(DRAFT);
						//ltSoLines.setLastUpdateDate(new Date());
						//ltSoLines.setCreationDate(new Date());
//						strQuery.append("),");
						
						strQuery.append(")");
						//strQuery1 = strQuery1.append(strQuery);
						//ltSoLines = ltSoLinesRepository.save(ltSoLines);
						String query = strQuery.toString();
						int n = ltSoHeadersDao.insertLine(query);
					}
					 System.out.print("This is default pl beforeeeeeeeeeee");
				// ATFL Phase 2 siebel devlopement		  
					try {
						 if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
								ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList()==defailtPriceList) {
									
							// sendEmail(ltSoHeader); 
						// inserting save order data into siebel using sampleCode()   
							 System.out.print("This is default pl before");
							 saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							 System.out.print("This is default pl after");						
						// saveOutlet();  inserting the create outlet data into siebel using saveOutlet() 
																	
						}else {
							
						// inStock order with different priceList need to send for approval to areHead
						//	sendNotifications(ltSoHeader);
						//	sendEmail(ltSoHeader);
							
						//    inserting save order data into siebel  if order is approved using sampleCode()   
							System.out.println("Hi This is Sales,distri & or before saveOrderIntoSiebel ");
						//	saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							System.out.println("Hi This is Not RETAILER after saveOrderIntoSiebel ");
													
						}
						
						RequestDto requestDto = new RequestDto();
						requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
						requestDto.setLimit(-1);
						requestDto.setOffset(2);
					//	requestDto.setLoginId(0L);
						status = getOrderV2(requestDto);
						
						// saving salesPerson details in salesPersonLocation table
						if(user.getUserType().equalsIgnoreCase(SALES)) {
							
							LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
							
							ltSalesPersonLocation.setBeatId(soHeaderDto.getBeatId());
							ltSalesPersonLocation.setOutletId(soHeaderDto.getOutletId());
							ltSalesPersonLocation.setOrderName(orderNumber);
							ltSalesPersonLocation.setAddress(soHeaderDto.getAddress());
							ltSalesPersonLocation.setLatitude(soHeaderDto.getLatitude());
							ltSalesPersonLocation.setLongitude(soHeaderDto.getLongitude());
							ltSalesPersonLocation.setCreatedBy(soHeaderDto.getUserId());
							ltSalesPersonLocation.setLastUpdatedBy(soHeaderDto.getUserId());
							ltSalesPersonLocation.setCreationDate(new Date());
							ltSalesPersonLocation.setLastUpdateDate(new Date());
							
							ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
						}
					   }
						catch (Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}
			            status.setCode(INSERT_SUCCESSFULLY);
						status.setMessage("Insert Successfully");
						sendNotifications(ltSoHeader);
						saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
						
						return status;
								
				} 
				// for Out_Of_Stock Products order	i.e. isInstockFlag ="N"
					else {
						
						String orderNumber = genrateOrderNumber(soHeaderDto.getOutletId());
						//System.out.println("orderNumber :: "+orderNumber);
						
						//Long orderNo = System.currentTimeMillis();
						ltSoHeader.setOrderNumber(orderNumber);
			            
						
						if (soHeaderDto.getOutletId() != null) {
							ltSoHeader.setOutletId(soHeaderDto.getOutletId());
						}
						if (soHeaderDto.getDeliveryDate() != null) {
							ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
						}
						if (soHeaderDto.getLatitude() != null) {
							ltSoHeader.setLatitude(soHeaderDto.getLatitude());
						}
						if (soHeaderDto.getLongitude() != null) {
							ltSoHeader.setLongitude(soHeaderDto.getLongitude());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setCreatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getUserId() != null) {
							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
						}
						if (soHeaderDto.getAddress() != null) {
							ltSoHeader.setAddress(soHeaderDto.getAddress());
						}
						if (soHeaderDto.getRemark() != null) {
							ltSoHeader.setRemark(soHeaderDto.getRemark());
						}

						ltSoHeader.setOrderDate(new Date()); //new Date()
						ltSoHeader.setLastUpdateDate(new Date()); // new Date()
						ltSoHeader.setCreationDate(new Date()); // new Date()
						//ltSoHeader.setStatus(DRAFT);

						 ltSoHeader.setStatus(DRAFT);
					    						
						if(soHeaderDto.getInstockFlag()!= null) {
							ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
						}
						if(soHeaderDto.getBeatId()!= null) {
							ltSoHeader.setBeatId(soHeaderDto.getBeatId());
						}if(soHeaderDto.getPriceList()!= null) {
							ltSoHeader.setPriceList(soHeaderDto.getPriceList());
						}
						
						ltSoHeader = updateSoHeader(ltSoHeader);
							
						// send OutofStock order for approval to areHead
						//	sendNotifications(ltSoHeader);
						
						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
						//StringBuffer strQuery1 =  new StringBuffer();
								
						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
												
							SoLineDto soLineDto = (SoLineDto) iterator.next();	
							StringBuffer strQuery =  new StringBuffer();
							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
							
							strQuery.append("(");

							//LtSoLines ltSoLines = new LtSoLines();
							
							if (ltSoHeader.getHeaderId() != null) {
								//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
								strQuery.append(ltSoHeader.getHeaderId()+",");
								
							}
							if (soLineDto.getProductId() != null) {
								//ltSoLines.setProductId(soLineDto.getProductId());
								strQuery.append("'"+soLineDto.getProductId()+"',");
							}
							if (soLineDto.getQuantity() != null) {
								//ltSoLines.setQuantity(soLineDto.getQuantity());
								strQuery.append(soLineDto.getQuantity()+",");
							}
							if (soLineDto.getListPrice() != null) {
								//ltSoLines.setListPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}else {
								strQuery.append("'"+null+"',");	
							}
							strQuery.append("'"+DRAFT.toString()+"',");//Status
							
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soHeaderDto.getUserId() != null) {
								//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
								strQuery.append(soHeaderDto.getUserId()+",");
							}
							if (soLineDto.getPtrPrice() != null) {
								//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
								strQuery.append("'"+soLineDto.getPtrPrice()+"',");
							}else {
								//ltSoLines.setPtrPrice(soLineDto.getListPrice());
								strQuery.append("'"+soLineDto.getListPrice()+"',");
							}
						//	if(soLineDto.getEimStatus()!= null) {
							strQuery.append("'"+null+"',"); // eimstatus
						//	} 		
							if (soLineDto.getDeliveryDate() != null) {
								//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
								
								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
								String deliveryDate =outputFormat.format(date);
								System.out.println("formatedDate : " + deliveryDate); 
													
								strQuery.append("'"+deliveryDate+"',");
							//	strQuery.append("'"+"',");
								
							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
							}
							
							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
							Date date = (Date)formatter.parse(ltSoHeader.getCreationDate().toString());
							Date date1 = (Date)formatter.parse(ltSoHeader.getLastUpdateDate().toString());
							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
							String crationDate =outputFormat.format(date);
							String lastUpdateDate =outputFormat.format(date1);
							System.out.println("crationDate : " + crationDate);
							System.out.println("lastUpdateDate : " + lastUpdateDate);
							
							//strQuery.append("'"+new Date()+"',");//Created Date
							strQuery.append("'"+crationDate+"',");  // set null for demo
							
							//strQuery.append("'"+new Date()+"'");//Last update date
							strQuery.append("'"+lastUpdateDate+"'"); // set null for demo
							
							//ltSoLines.setStatus(DRAFT);
							//ltSoLines.setLastUpdateDate(new Date());
							//ltSoLines.setCreationDate(new Date());
//							strQuery.append("),");
							
							strQuery.append(")");
							String query = strQuery.toString();
							int n = ltSoHeadersDao.insertLine(query);
						}
						
						// outOfStock order need to send for approval to areHead
						 //  sendNotifications(ltSoHeader);
						 //  sendEmail(ltSoHeader);
								
						// ATFL Phase 2 siebel devlopement					
						try {
							//if(ltSoHeader.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
								
						  // inserting save order data into siebel using sampleCode() and update the order status to NEW 
							System.out.println("Hi This is outofStock order before saveOrderIntoSiebel ");
						//	saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							System.out.println("Hi This is outofStock Order after saveOrderIntoSiebel ");	  
							//}
							
							RequestDto requestDto = new RequestDto();
							requestDto.setOrderNumber(ltSoHeader.getOrderNumber());
							requestDto.setLimit(-1);
							requestDto.setOffset(2);
						//	requestDto.setLoginId(0L);
							status = getOrderV2(requestDto);
							
							// saving salesPerson details in salesPersonLocation table
							if(user.getUserType().equalsIgnoreCase(SALES)) {
								
								LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
								
								ltSalesPersonLocation.setBeatId(soHeaderDto.getBeatId());
								ltSalesPersonLocation.setOutletId(soHeaderDto.getOutletId());
								ltSalesPersonLocation.setOrderName(orderNumber);
								ltSalesPersonLocation.setAddress(soHeaderDto.getAddress());
								ltSalesPersonLocation.setLatitude(soHeaderDto.getLatitude());
								ltSalesPersonLocation.setLongitude(soHeaderDto.getLongitude());
								ltSalesPersonLocation.setCreatedBy(soHeaderDto.getUserId());
								ltSalesPersonLocation.setLastUpdatedBy(soHeaderDto.getUserId());
								ltSalesPersonLocation.setCreationDate(new Date());
								ltSalesPersonLocation.setLastUpdateDate(new Date());
								
								ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
							}
						}catch (Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}	
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");
							sendNotifications(ltSoHeader);
							saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
							return status;						
					} 		  
				  
			  }
						
		}catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null; 
	}

	
	// new method end 	
	
	
	private void saveOrderIntoSiebel(LtSoHeaders ltSoHeader, SoHeaderDto soHeaderDto){
		try {
			
			System.out.println("Hi I'm in Siebel Mehtod");
			
        //String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateOrder?matchrequestformat=y";
        String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateOrder?matchrequestformat=y";
        
        String method = "POST";
//        String contentType = "Content-Type: application/json";
//        String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="; //LONAR_TEST:Lonar123
//       String authorization = "Authorization: Basic VklOQVkuS1VNQVI2OldlbGNvbWUx"; //VINAY.KUMAR6:Welcome1
                                                              
        JSONObject lineItemObject = new JSONObject();
		lineItemObject.put("Id", "1");
//		lineItemObject.put("Product Id", soLineDto.getProductId());
		lineItemObject.put("Product Id", "1-4XBK-2");   //"1-MZZR-1");     ////"1-4XBK-8");
//		lineItemObject.put("Due Date", soLineDto.getDeliveryDate());
		lineItemObject.put("Due Date", "04/16/2024");
//  	lineItemObject.put("Item Price List Id", soLineDto.getPriceListId());
//		lineItemObject.put("Item Price List Id", "1-475Z");
		lineItemObject.put("Action Code", "New");
//  	lineItemObject.put("Name", soLineDto.getProductName());
//		lineItemObject.put("Name", "P02IAPKP040");  ///"P03RSPOP010");  ///"P02IAPKP040");
//   	lineItemObject.put("Quantity", soLineDto.getQuantity());
		lineItemObject.put("Quantity", "1");
		
		JSONArray lineItemArray = new JSONArray();
		for (int i =0; i<lineItemObject.length(); i++) {
			lineItemArray.put(lineItemObject);	
		}
		
		List<SoLineDto> lineItem = soHeaderDto.getSoLineDtoList();
////        for (int i =0; i<lineItem.size(); i++) {
////        	 SoLineDto soLineList = new SoLineDto();
////        	 String id = Integer.toString(i+1);   
////        	 String prodId=  lineItem.get(i).getProductId();
////        	 //String deliDate=  lineItem.get(i).getDeliveryDate().toString();
////        	 DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
////				Date date = (Date)formatter.parse(lineItem.get(i).getDeliveryDate().toString());
////				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
////				String deliDate =outputFormat.format(date);
////			//	System.out.println("formatedDate : " + deliveryDate);
////        	 String prilst=  lineItem.get(i).getPriceListId();
////        	 //String ActionCode= "New"; 
////        	 String prodName=  lineItem.get(i).getProductName();
////        	 String qty=  Long.toString(lineItem.get(i).getQuantity());
////        	   
////        	 //lineItem.add(soLineList);
////        	 lineItemObject.put("Id",id);
////        	 lineItemObject.put("Product Id",prodId);
////        	 lineItemObject.put("Due Date",deliDate);
////        	 lineItemObject.put("Item Price List Id",prilst);
////        	 lineItemObject.put("Action Code","New");
////        	 lineItemObject.put("Name",prodName);
////        	 lineItemObject.put("Quantity",qty);
////        	 
////        	 lineItemArray.put(lineItemObject);
////            }
		
        JSONArray listOfLineItem1 = new JSONArray();
        
        for (int i = 0; i < lineItem.size(); i++) {
            JSONObject lineItem1 = new JSONObject();
            lineItem1.put("Id", Integer.toString(i+1));
            lineItem1.put("Product Id", lineItem.get(i).getProductId());
//            lineItem1.put("Item Price List Id", lineItem.get(i).getPriceListId());
                        
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
			Date date = (Date)formatter.parse(lineItem.get(i).getDeliveryDate().toString());
			SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss"); //("dd/MM/yyyy hh:mm:ss");
			String deliDate =outputFormat.format(date);
            lineItem1.put("Due Date", deliDate);
            lineItem1.put("Action Code", "New");
            lineItem1.put("Quantity", Long.toString(lineItem.get(i).getQuantity()));
//            lineItem1.put("Name", lineItem.get(i).getProductName());     // in table this is as product desc
//              lineItem1.put("Name", lineItem.get(i).getProductDesc());  // in table this is as product name;
            listOfLineItem1.put(lineItem1);
        }   
           System.out.println("listOfLineItem1 is === " +listOfLineItem1);
        
		JSONObject listOfLineItem = new JSONObject();
		listOfLineItem.put("Line Item", listOfLineItem1);   ////lineItemArray); ////listOfLineItem1);
		
		SoHeaderDto soHeaders = new SoHeaderDto();
		
		JSONObject header = new JSONObject();
		
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
		Date date = (Date)formatter.parse(soHeaderDto.getDeliveryDate().toString());
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");  //("dd/MM/yyyy hh:mm:ss");
		String deliDate =outputFormat.format(date);
		System.out.println("formatedDate : " + deliDate);
	
//		header.put("Requested Ship Date", deliDate);
		header.put("Requested Ship Date", "04/06/2024");
		header.put("Order Type Id", "0-D14G");
		header.put("Account Id", soHeaderDto.getOutletId());
//		header.put("Account Id", "1-EEWE-430");      //"1-BRWN-27");
		header.put("Status", "New");
		header.put("Order Type", "Service Order");
		header.put("Account", soHeaderDto.getOutletName());
//		header.put("Account", "BHAVANI TRADERS");     //"SHREE MAHALAXMI KIRANA AND GENERAL STORE");
		header.put("Currency Code", "INR");
		header.put("Order Number", ltSoHeader.getOrderNumber());
//		header.put("Order Number", "MSO-10791-2425-755");     ///"MSO-53623-2324-11");
		header.put("Source Inventory Id", "1-2FPGVLJ");  //"1-27PLQ8" ); //"1-2FPGVLJ");//"1-2GR1JJ1");//"1-2C7QNZG");// "1-2KK4ILD"); );
		header.put("ListOfLine Item", listOfLineItem);
		//header.put("ListOfLine Item", listOfLineItem);
		
		JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
		ListOfATOrdersIntegrationIO.put("Header", header);
		
		JSONObject siebelMassage = new JSONObject();
		siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
		siebelMassage.put("MessageId", "");
		siebelMassage.put("IntObjectName", "AT Orders Integration IO");
		siebelMassage.put("MessageType", "Integration Object");
		siebelMassage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
		
		JSONObject siebelMassages = new JSONObject();
		siebelMassages.put("SubmitFlag", "Y");
		siebelMassages.put("InvoiceFlag", "Y");
		siebelMassages.put("SiebelMessage" , siebelMassage);
		
		String jsonPayload =siebelMassages.toString();
   
        String payload = "{\r\n    \"SubmitFlag\": \"Y\",\r\n    \"InvoiceFlag\": \"Y\",\r\n    \"SiebelMessage\": {\r\n        \"IntObjectFormat\": \"Siebel Hierarchical\",\r\n        \"MessageId\": \"\",\r\n        \"IntObjectName\": \"AT Orders Integration IO\",\r\n        \"MessageType\": \"Integration Object\",\r\n        \"ListOfAT Orders Integration IO\": {\r\n            \"Header\": {\r\n                \"Requested Ship Date\": \"04/03/2024 18:50:01\",\r\n                \"Order Type Id\": \"0-D14G\",\r\n                \"Account Id\": \"1-EEWE-430\",\r\n                \"Status\": \"New\",\r\n                \"Order Type\": \"Service Order\",\r\n                \"Account\": \"BHAVANI TRADERS\",\r\n                \"Currency Code\": \"INR\",\r\n                \"Order Number\": \"MSO-53624-2322-86\",\r\n                \"ListOfLine Item\": {\r\n                    \"Line Item\": {[listOfLineItem1]}\r\n                }\r\n            }\r\n        }\r\n    }\r\n}";
        jsonPayload = payload;
  
//      Order Header data  
        String outLid= "\"Account Id\":\""+ soHeaderDto.getOutletId()+"\"";
        jsonPayload= jsonPayload.replace("\"Account Id\": \"1-EEWE-430\"", outLid);
        System.out.print(" New outLid is :" + outLid);
		
		String outName= "\"Account\":\""+ soHeaderDto.getOutletName()+"\"";
		jsonPayload=jsonPayload.replace("\"Account\": \"BHAVANI TRADERS\"", outName);
		
		String ordNo= "\"Order Number\":\""+ ltSoHeader.getOrderNumber()+"\"";
		jsonPayload=jsonPayload.replace("\"Order Number\": \"MSO-53624-2322-86\"", ordNo);
		
		String reqShipDate= "\"Requested Ship Date\":\""+ deliDate+"\"";
		jsonPayload=jsonPayload.replace("\"Requested Ship Date\": \"04/03/2024 18:50:01\"", reqShipDate);
		
		String itemString =listOfLineItem1.toString();
		jsonPayload= jsonPayload.replace("{[listOfLineItem1]}", itemString);
		
//		Order Line Date
		//String id= "\"Id\":\""+ id+"\"";
		//jsonPayload.replace("\"Id\": \"1\"", id);
		
		//String prodId= "\"Product Id\":\""+ prodId+"\"";
		//jsonPayload.replace("\"Product Id\": \"1-MZZR-1\"", prodId);
		//System.out.print(" New prodId is :" + prodId);
	
		//String dueSDate= "\"Due Date\":\""+ deliDate+"\"";
		//jsonPayload.replace("\"Due Date\": \"04/12/2024 18:50:01\"", dueSDate);
		
		//System.out.print(" New dueSDate is :" + dueSDate);
		
		//String quantity= "\"Quantity\":\""+ qty+"\"";
		//jsonPayload.replace("\"Quantity\": \"04/03/2024 18:50:01\"", quantity);
		//System.out.print(" New quantity is :" + quantity);
		
		
		//		
//		String jsonString = "{"
//			    + "\"SubmitFlag\": \"Y\","
//			    + "\"InvoiceFlag\": \"Y\","
//			    + "\"SiebelMessage\": {"
//			    +     "\"IntObjectFormat\": \"Siebel Hierarchical\","
//			    +     "\"MessageId\": \"\","
//			    +     "\"IntObjectName\": \"AT Orders Integration IO\","
//			    +     "\"MessageType\": \"Integration Object\","
//			    +     "\"ListOfAT Orders Integration IO\": {"
//			    +         "\"Header\": {"
//			    +             "\"Requested Ship Date\": \"04/03/2024 18:50:01\","
//			    +             "\"Order Type Id\": \"0-D14G\","
//			    +             "\"Account Id\": \"1-EEWE-430\","
//			    +             "\"Status\": \"New\","
//			    +             "\"Order Type\": \"Service Order\","
//			    +             "\"Account\": \"BHAVANI TRADERS\","
//			    +             "\"Currency Code\": \"INR\","
//			    +             "\"Order Number\": \"MSO-53624-2324-69\","
//			    +             "\"ListOfLine Item\": {"
//			    +                 "\"Line Item\": ["
//			    +                     "{"
//			    +                         "\"Id\"= \"1\","
//			    +                         "\"Product Id\": \"1-MZZR-1\","
//			    +                         "\"Due Date\": \"04/12/2024 18:50:01\","
//			    +                         "\"Action Code\": \"New\","
//			    +                         "\"Quantity\": \"1\""
//			    +                     "}"
//			    +                 "]"
//			    +             "}"
//			    +         "}"
//			    +     "}"
//			    + "}";
//
//			// Now you can use the jsonString variable in your Java code
//
//		
//		
//		jsonString =siebelMassages.toString();
		try {
        System.out.println("jsonPayload =" +jsonPayload);     //(requestBody);
		}catch(Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
//        // Create URL object
        URL obj = new URL(url);
        System.out.println("url is..... = "+ url);
//        // Add this line before opening the connection
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

        
//         Create HttpURLConnection object
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//         Set request method
        con.setRequestMethod(method);
//         Set request headers
        con.setRequestProperty("Content-Type", "application/json");
        //String username = "VINAY.KUMAR6";
        //String password = "Welcome1";
        String username;
        if(soHeaderDto.getMobileNumber()!= null) {
         username = ltSoHeadersDao.getUserNameFromSiebel(soHeaderDto.getMobileNumber());
         }else {
         username = "VINAY.KUMAR6";}String password = "D10nysu$";
        String auth = username + ":" + password;
        System.out.println("Siebel auth is = "+auth);
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeaderValue = "Basic " + new String(encodedAuth);
        System.out.println("This is user authHeaderValue"+authHeaderValue);
        con.setRequestProperty("Authorization", authHeaderValue);

//         Enable output and set request body
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
//      Get response code
        int responseCode = con.getResponseCode();
        String msg = con.getResponseMessage();
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response Message : " + msg);
        
//      Read the response body
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
  
//           Show the response
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
        
//        // saving response details in to table 
        //LtSoHeaders  ltSoHeader1 = new LtSoHeaders();
        String resCode= Integer.toString(responseCode);
        ltSoHeader.setSiebelStatus(resCode);
        String res= response.toString();
        ltSoHeader.setSiebelRemark(res);
        ltSoHeader.setSiebelJsonpayload(jsonPayload);
        
        String invoiceNumber = null;
//       // Create an ObjectMapper instance
       ObjectMapper objectMapper = new ObjectMapper();
       
//       // Parse the response body into a JSON object
       JsonNode rootNode = objectMapper.readTree(response.toString());
//       // Access the "Invoice Number" field from the JSON object
       if(rootNode!= null && responseCode ==200) {
        invoiceNumber = rootNode.get("Invoice Number").asText();
//       // Now you can use the invoiceNumber variable as needed
       System.out.println("Invoice Number: " + invoiceNumber);
             
       ltSoHeader.setSiebelInvoiceNumber(invoiceNumber);
       
       //ltSoHeader1 = updateSoHeader(ltSoHeader1);
		}ltSoHeader.setSiebelInvoiceNumber(invoiceNumber);
              
	} catch (Exception e) {
		logger.error("Error Description :", e);
        e.printStackTrace();
    }

}

	
	private void saveOutOfStockOrderIntoSiebel(LtSoHeaders ltSoHeader, SoHeaderDto soHeaderDto){
		try {
			
			System.out.println("Hi I'm in Siebel Mehtod");
			
        //String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateOrder?matchrequestformat=y";
        String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateOrder?matchrequestformat=y";
        
        String method = "POST";
//        String contentType = "Content-Type: application/json";
//        String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw=="; //LONAR_TEST:Lonar123
//       String authorization = "Authorization: Basic VklOQVkuS1VNQVI2OldlbGNvbWUx"; //VINAY.KUMAR6:Welcome1
                                                              
        JSONObject lineItemObject = new JSONObject();
		lineItemObject.put("Id", "1");
//		lineItemObject.put("Product Id", soLineDto.getProductId());
		lineItemObject.put("Product Id", "1-4XBK-2");   //"1-MZZR-1");     ////"1-4XBK-8");
//		lineItemObject.put("Due Date", soLineDto.getDeliveryDate());
		lineItemObject.put("Due Date", "04/16/2024");
//  	lineItemObject.put("Item Price List Id", soLineDto.getPriceListId());
//		lineItemObject.put("Item Price List Id", "1-475Z");
		lineItemObject.put("Action Code", "New");
//  	lineItemObject.put("Name", soLineDto.getProductName());
//		lineItemObject.put("Name", "P02IAPKP040");  ///"P03RSPOP010");  ///"P02IAPKP040");
//   	lineItemObject.put("Quantity", soLineDto.getQuantity());
		lineItemObject.put("Quantity", "1");
		
		JSONArray lineItemArray = new JSONArray();
		for (int i =0; i<lineItemObject.length(); i++) {
			lineItemArray.put(lineItemObject);	
		}
		
		List<SoLineDto> lineItem = soHeaderDto.getSoLineDtoList();
////        for (int i =0; i<lineItem.size(); i++) {
////        	 SoLineDto soLineList = new SoLineDto();
////        	 String id = Integer.toString(i+1);   
////        	 String prodId=  lineItem.get(i).getProductId();
////        	 //String deliDate=  lineItem.get(i).getDeliveryDate().toString();
////        	 DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
////				Date date = (Date)formatter.parse(lineItem.get(i).getDeliveryDate().toString());
////				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
////				String deliDate =outputFormat.format(date);
////			//	System.out.println("formatedDate : " + deliveryDate);
////        	 String prilst=  lineItem.get(i).getPriceListId();
////        	 //String ActionCode= "New"; 
////        	 String prodName=  lineItem.get(i).getProductName();
////        	 String qty=  Long.toString(lineItem.get(i).getQuantity());
////        	   
////        	 //lineItem.add(soLineList);
////        	 lineItemObject.put("Id",id);
////        	 lineItemObject.put("Product Id",prodId);
////        	 lineItemObject.put("Due Date",deliDate);
////        	 lineItemObject.put("Item Price List Id",prilst);
////        	 lineItemObject.put("Action Code","New");
////        	 lineItemObject.put("Name",prodName);
////        	 lineItemObject.put("Quantity",qty);
////        	 
////        	 lineItemArray.put(lineItemObject);
////            }
		
        JSONArray listOfLineItem1 = new JSONArray();
        
        for (int i = 0; i < lineItem.size(); i++) {
            JSONObject lineItem1 = new JSONObject();
            lineItem1.put("Id", Integer.toString(i+1));
            lineItem1.put("Product Id", lineItem.get(i).getProductId());
//            lineItem1.put("Item Price List Id", lineItem.get(i).getPriceListId());
                        
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
			Date date = (Date)formatter.parse(lineItem.get(i).getDeliveryDate().toString());
			SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss"); //("dd/MM/yyyy hh:mm:ss");
			String deliDate =outputFormat.format(date);
            lineItem1.put("Due Date", deliDate);
            lineItem1.put("Action Code", "New");
            lineItem1.put("Quantity", Long.toString(lineItem.get(i).getQuantity()));
//            lineItem1.put("Name", lineItem.get(i).getProductName());     // in table this is as product desc
//              lineItem1.put("Name", lineItem.get(i).getProductDesc());  // in table this is as product name;
            listOfLineItem1.put(lineItem1);
        }   
           System.out.println("listOfLineItem1 is === " +listOfLineItem1);
        
		JSONObject listOfLineItem = new JSONObject();
		listOfLineItem.put("Line Item", listOfLineItem1);   ////lineItemArray); ////listOfLineItem1);
		
		SoHeaderDto soHeaders = new SoHeaderDto();
		
		JSONObject header = new JSONObject();
		
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
		Date date = (Date)formatter.parse(soHeaderDto.getDeliveryDate().toString());
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");  //("dd/MM/yyyy hh:mm:ss");
		String deliDate =outputFormat.format(date);
		System.out.println("formatedDate : " + deliDate);
	
//		header.put("Requested Ship Date", deliDate);
		header.put("Requested Ship Date", "04/06/2024");
		header.put("Order Type Id", "0-D14G");
		header.put("Account Id", soHeaderDto.getOutletId());
//		header.put("Account Id", "1-EEWE-430");      //"1-BRWN-27");
		header.put("Status", "New");
		header.put("Order Type", "Service Order");
		header.put("Account", soHeaderDto.getOutletName());
//		header.put("Account", "BHAVANI TRADERS");     //"SHREE MAHALAXMI KIRANA AND GENERAL STORE");
		header.put("Currency Code", "INR");
		header.put("Order Number", ltSoHeader.getOrderNumber());
//		header.put("Order Number", "MSO-10791-2425-755");     ///"MSO-53623-2324-11");
		header.put("Source Inventory Id", "1-2FPGVLJ");  //"1-27PLQ8" ); //"1-2FPGVLJ");//"1-2GR1JJ1");//"1-2C7QNZG");// "1-2KK4ILD"); );
		header.put("ListOfLine Item", listOfLineItem);
		//header.put("ListOfLine Item", listOfLineItem);
		
		JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
		ListOfATOrdersIntegrationIO.put("Header", header);
		
		JSONObject siebelMassage = new JSONObject();
		siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
		siebelMassage.put("MessageId", "");
		siebelMassage.put("IntObjectName", "AT Orders Integration IO");
		siebelMassage.put("MessageType", "Integration Object");
		siebelMassage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
		
		JSONObject siebelMassages = new JSONObject();
		siebelMassages.put("SubmitFlag", "Y");
		siebelMassages.put("InvoiceFlag", "Y");
		siebelMassages.put("SiebelMessage" , siebelMassage);
		
		String jsonPayload =siebelMassages.toString();
   
        String payload = "{\r\n    \"SubmitFlag\": \"\",\r\n    \"InvoiceFlag\": \"\",\r\n    \"SiebelMessage\": {\r\n        \"IntObjectFormat\": \"Siebel Hierarchical\",\r\n        \"MessageId\": \"\",\r\n        \"IntObjectName\": \"AT Orders Integration IO\",\r\n        \"MessageType\": \"Integration Object\",\r\n        \"ListOfAT Orders Integration IO\": {\r\n            \"Header\": {\r\n                \"Requested Ship Date\": \"04/03/2024 18:50:01\",\r\n                \"Order Type Id\": \"0-D14G\",\r\n                \"Account Id\": \"1-EEWE-430\",\r\n                \"Status\": \"New\",\r\n                \"Order Type\": \"Service Order\",\r\n                \"Account\": \"BHAVANI TRADERS\",\r\n                \"Currency Code\": \"INR\",\r\n                \"Order Number\": \"MSO-53624-2322-86\",\r\n                \"ListOfLine Item\": {\r\n                    \"Line Item\": {[listOfLineItem1]}\r\n                }\r\n            }\r\n        }\r\n    }\r\n}";
        jsonPayload = payload;
  
//      Order Header data  
        String outLid= "\"Account Id\":\""+ soHeaderDto.getOutletId()+"\"";
        jsonPayload= jsonPayload.replace("\"Account Id\": \"1-EEWE-430\"", outLid);
        System.out.print(" New outLid is :" + outLid);
		
		String outName= "\"Account\":\""+ soHeaderDto.getOutletName()+"\"";
		jsonPayload=jsonPayload.replace("\"Account\": \"BHAVANI TRADERS\"", outName);
		
		String ordNo= "\"Order Number\":\""+ ltSoHeader.getOrderNumber()+"\"";
		jsonPayload=jsonPayload.replace("\"Order Number\": \"MSO-53624-2322-86\"", ordNo);
		
		String reqShipDate= "\"Requested Ship Date\":\""+ deliDate+"\"";
		jsonPayload=jsonPayload.replace("\"Requested Ship Date\": \"04/03/2024 18:50:01\"", reqShipDate);
		
		String itemString =listOfLineItem1.toString();
		jsonPayload= jsonPayload.replace("{[listOfLineItem1]}", itemString);
		
//		Order Line Date
		//String id= "\"Id\":\""+ id+"\"";
		//jsonPayload.replace("\"Id\": \"1\"", id);
		
		//String prodId= "\"Product Id\":\""+ prodId+"\"";
		//jsonPayload.replace("\"Product Id\": \"1-MZZR-1\"", prodId);
		//System.out.print(" New prodId is :" + prodId);
	
		//String dueSDate= "\"Due Date\":\""+ deliDate+"\"";
		//jsonPayload.replace("\"Due Date\": \"04/12/2024 18:50:01\"", dueSDate);
		
		//System.out.print(" New dueSDate is :" + dueSDate);
		
		//String quantity= "\"Quantity\":\""+ qty+"\"";
		//jsonPayload.replace("\"Quantity\": \"04/03/2024 18:50:01\"", quantity);
		//System.out.print(" New quantity is :" + quantity);
		
		
		//		
//		String jsonString = "{"
//			    + "\"SubmitFlag\": \"Y\","
//			    + "\"InvoiceFlag\": \"Y\","
//			    + "\"SiebelMessage\": {"
//			    +     "\"IntObjectFormat\": \"Siebel Hierarchical\","
//			    +     "\"MessageId\": \"\","
//			    +     "\"IntObjectName\": \"AT Orders Integration IO\","
//			    +     "\"MessageType\": \"Integration Object\","
//			    +     "\"ListOfAT Orders Integration IO\": {"
//			    +         "\"Header\": {"
//			    +             "\"Requested Ship Date\": \"04/03/2024 18:50:01\","
//			    +             "\"Order Type Id\": \"0-D14G\","
//			    +             "\"Account Id\": \"1-EEWE-430\","
//			    +             "\"Status\": \"New\","
//			    +             "\"Order Type\": \"Service Order\","
//			    +             "\"Account\": \"BHAVANI TRADERS\","
//			    +             "\"Currency Code\": \"INR\","
//			    +             "\"Order Number\": \"MSO-53624-2324-69\","
//			    +             "\"ListOfLine Item\": {"
//			    +                 "\"Line Item\": ["
//			    +                     "{"
//			    +                         "\"Id\"= \"1\","
//			    +                         "\"Product Id\": \"1-MZZR-1\","
//			    +                         "\"Due Date\": \"04/12/2024 18:50:01\","
//			    +                         "\"Action Code\": \"New\","
//			    +                         "\"Quantity\": \"1\""
//			    +                     "}"
//			    +                 "]"
//			    +             "}"
//			    +         "}"
//			    +     "}"
//			    + "}";
//
//			// Now you can use the jsonString variable in your Java code
//
//		
//		
//		jsonString =siebelMassages.toString();
		try {
        System.out.println("jsonPayload =" +jsonPayload);     //(requestBody);
		}catch(Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
//        // Create URL object
        URL obj = new URL(url);
        System.out.println("url is..... = "+ url);
//        // Add this line before opening the connection
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

        
//         Create HttpURLConnection object
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//         Set request method
        con.setRequestMethod(method);
//         Set request headers
        con.setRequestProperty("Content-Type", "application/json");
        //String username = "VINAY.KUMAR6";
        //String password = "Welcome1";
        String username;
        if(soHeaderDto.getMobileNumber()!= null) {
         username = ltSoHeadersDao.getUserNameFromSiebel(soHeaderDto.getMobileNumber());
         }else {
         username = "VINAY.KUMAR6";}
        String password = "D10nysu$";
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeaderValue = "Basic " + new String(encodedAuth);
        System.out.println("This is user authHeaderValue"+authHeaderValue);
        con.setRequestProperty("Authorization", authHeaderValue);

//         Enable output and set request body
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
//      Get response code
        int responseCode = con.getResponseCode();
        String msg = con.getResponseMessage();
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response Message : " + msg);
        
//      Read the response body
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
  
//           Show the response
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
        
//        // saving response details in to table 
        //LtSoHeaders  ltSoHeader1 = new LtSoHeaders();
        String resCode= Integer.toString(responseCode);
        ltSoHeader.setSiebelStatus(resCode);
        String res= response.toString();
        ltSoHeader.setSiebelRemark(res);
        ltSoHeader.setSiebelJsonpayload(jsonPayload);
        
        String invoiceNumber = null;
//       // Create an ObjectMapper instance
       ObjectMapper objectMapper = new ObjectMapper();
       
//       // Parse the response body into a JSON object
       JsonNode rootNode = objectMapper.readTree(response.toString());
//       // Access the "Invoice Number" field from the JSON object
       if(rootNode!= null && responseCode ==200) {
        invoiceNumber = rootNode.get("Invoice Number").asText();
//       // Now you can use the invoiceNumber variable as needed
       System.out.println("Invoice Number: " + invoiceNumber);
             
       ltSoHeader.setSiebelInvoiceNumber(invoiceNumber);
       
       //ltSoHeader1 = updateSoHeader(ltSoHeader1);
		}ltSoHeader.setSiebelInvoiceNumber(invoiceNumber);
              
	} catch (Exception e) {
		logger.error("Error Description :", e);
        e.printStackTrace();
    }

}

	
/*	//public static void sendCreateOrderRequest() throws IOException {
	private void saveOrderIntoSiebel(LtSoHeaders ltSoHeader, SoHeaderDto soHeaderDto)throws IOException{
        // Replace the URL below with the actual URL of your request
		try {
        String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateOrder?matchrequestformat=y";

        // Replace the username and password with your credentials
        String username = "VINAY.KUMAR6";
        String password = "Welcome1";

        // Replace the rawRequestBody with your JSON request body
        String rawRequestBody = "{\r\n    \"SubmitFlag\": \"Y\",\r\n    \"InvoiceFlag\": \"Y\",\r\n    \"SiebelMessage\": {\r\n        \"IntObjectFormat\": \"Siebel Hierarchical\",\r\n        \"MessageId\": \"\",\r\n        \"IntObjectName\": \"AT Orders Integration IO\",\r\n        \"MessageType\": \"Integration Object\",\r\n        \"ListOfAT Orders Integration IO\": {\r\n            \"Header\": {\r\n                \"Requested Ship Date\": \"04/03/2024 18:50:01\",\r\n                \"Order Type Id\": \"0-D14G\",\r\n                \"Account Id\": \"1-EEWE-430\",\r\n                \"Status\": \"New\",\r\n     \"Source Inventory Id\": \"1-2FPGVLJ\",\r\n             \"Order Type\": \"Service Order\",\r\n                \"Account\": \"BHAVANI TRADERS\",\r\n                \"Currency Code\": \"INR\",\r\n                \"Order Number\": \"MSO-53624-2324-56\",\r\n                \"ListOfLine Item\": {\r\n                    \"Line Item\": [\r\n                        {\r\n                            \"Id\"=\"1\",\r\n                            \"Product Id\": \"1-MZZR-1\",\r\n                            \"Due Date\": \"04/12/2024 18:50:01\",\r\n                            \"Action Code\": \"New\",\r\n                            \"Quantity\": \"1\"\r\n                        }\r\n                    ]\r\n                }\r\n            }\r\n        }\r\n    }\r\n}";

        // Create URL object
       // HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
        
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
              
        
//        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Add request headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Add basic authentication
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeaderValue = "Basic " + new String(encodedAuth);
        con.setRequestProperty("Authorization", authHeaderValue);

        // Enable output and input streams for POST request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        // Send POST request
        wr.writeBytes(rawRequestBody);
        wr.flush();
        wr.close();

        System.out.println("jsonPayload =" +rawRequestBody);
        // Get the response code
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        // Read the response from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print the response
        System.out.println("Response : " + response.toString());
    
		} catch (Exception e) {
	        e.printStackTrace();
	    }		
}
	*/
	
	public void saveOutlet() {
		try {

			// Add this line before opening the connection
		//	HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

			// URL
            String url = "https://10.245.4.70:9014/siebel/v1.0/service/Siebel%20Outlet%20Integration/InsertOrUpdate?matchrequestformat=y";

            // Request method
            String method = "POST";

            // Request headers
            String contentType = "Content-Type: application/json";
            String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw==";

            // Request body
            String requestBody = "{\"SiebelMessage\":{\"IntObjectFormat\":\"Siebel Hierarchical\",\"MessageId\":\"\",\"IntObjectName\":\"Outlet Interface\",\"MessageType\":\"Integration Object\",\"ListOfOutlet Interface\":{\"Account\":{\"Account Status\":\"Active\",\"Type\":\"Retailer\",\"Account Id\":\"13\",\"Rule Attribute 2\":\"Whole Sellers\",\"Name\":\"RAVAN TRADERS\",\"AT Territory\":\"30801: AMDAVAD RURAL\",\"Location\":\"DINESHBHAI\",\"ListOfBusiness Address\":{\"Business Address\":{\"Address Id\":\"1\",\"Street Address\":\"SOUTH WEST PRIMARY STREET\",\"County\":\"\",\"Street Address 2\":\"JAMSHEDAPUR\",\"City\":\"PUNE\",\"State\":\"MH\",\"Country\":\"India\",\"Postal Code\":\"380708\",\"Province\":\"\",\"IsPrimaryMVG\":\"Y\"}},\"ListOfRelated Organization\":{\"Related Organization\":{\"IsPrimaryMVG\":\"Y\",\"Organization\":\"JSB AGENCIES\"}}}}}}";
            System.out.println(requestBody);
            // Create URL object
            URL obj = new URL(url);

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
            con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpMb25hcjEyMw==");

            // Enable output and set request body
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = con.getResponseCode();
            String msg = con.getResponseMessage();
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Message : " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	private Status updateSoHeadeLineInDraftV2(SoHeaderDto soHeaderDto, Long headerId, Date creationDate, Long createdBy)
			throws ServiceException, IOException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetSiebelDataById = 0;
		long outQuerygetSiebelDataById = 0;
		long inQuerydeleteLineDataByHeaderIdAndReturnStatus = 0;
		long outQuerydeleteLineDataByHeaderIdAndReturnStatus = 0;
		long inQuerygetMobileNumber = 0;
		long outQuerygetMobileNumber = 0;
		long inQuerybatchInsert = 0;
		long outQuerybatchInsert = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
		
		try {
			inQuerygetSiebelDataById = System.currentTimeMillis();
			LtSoHeaders siebelData = ltSoHeadersDao.getSiebelDataById(headerId);
			outQuerygetSiebelDataById = System.currentTimeMillis();
			
			inQuerydeleteLineDataByHeaderIdAndReturnStatus = System.currentTimeMillis();
		int lineDeleteStatus = ltSoHeadersDao.deleteLineDataByHeaderIdAndReturnStatus(headerId);
		outQuerydeleteLineDataByHeaderIdAndReturnStatus = System.currentTimeMillis();

		
		Status status = new Status();
		LtSoHeaders ltSoHeader = new LtSoHeaders();
		inQuerygetMobileNumber = System.currentTimeMillis();
		String mobileNumber = ltSoHeadersDao.getMobileNumber(soHeaderDto.getUserId());
		outQuerygetMobileNumber = System.currentTimeMillis();
		
		ltSoHeader.setHeaderId(headerId);
		ltSoHeader.setOutletId(soHeaderDto.getOutletId());
		
		
		ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
		
		if (soHeaderDto.getOrderNumber() != null) {
			ltSoHeader.setOrderNumber(soHeaderDto.getOrderNumber());
		}
		if (soHeaderDto.getDeliveryDate() != null) {
			ltSoHeader.setDeliveryDate(soHeaderDto.getDeliveryDate());
		}
		if (soHeaderDto.getLatitude() != null) {
			ltSoHeader.setLatitude(soHeaderDto.getLatitude());
		}
		if (soHeaderDto.getLongitude() != null) {
			ltSoHeader.setLongitude(soHeaderDto.getLongitude());
		}
		
		ltSoHeader.setCreatedBy(createdBy);
		
		if (soHeaderDto.getUserId() != null) {
			ltSoHeader.setLastUpdatedBy(soHeaderDto.getUserId());
		}
		if (soHeaderDto.getUserId() != null) {
			ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
		}
		if (soHeaderDto.getAddress() != null) {
			ltSoHeader.setAddress(soHeaderDto.getAddress());
		}
		
		if(mobileNumber.equalsIgnoreCase("8857885605") && soHeaderDto.getStatus().equalsIgnoreCase("APPROVED")) {
			ltSoHeader.setStatus("DELIVERED");
		}else {
			if (soHeaderDto.getStatus() != null) {
				ltSoHeader.setStatus(soHeaderDto.getStatus());
			}	
		}
		
		if (soHeaderDto.getRemark() != null) {
			ltSoHeader.setRemark(soHeaderDto.getRemark());
		}
		
		if (soHeaderDto.getCustomerId() != null) {
			ltSoHeader.setCustomerId(soHeaderDto.getCustomerId());
		}
		
		if(soHeaderDto.getBeatId()!= null) {
			ltSoHeader.setBeatId(soHeaderDto.getBeatId());
		}
		System.out.println("Above if = "+soHeaderDto.getPriceList());
		if(soHeaderDto.getPriceList()!= null) {
			ltSoHeader.setPriceList(soHeaderDto.getPriceList());
		}
		System.out.println("below if ="+soHeaderDto.getPriceList());
		if(soHeaderDto.getInstockFlag()!= null) {
			ltSoHeader.setInStockFlag(soHeaderDto.getInstockFlag());
		}
		
		ltSoHeader.setCreationDate(creationDate); //(new Date());
		ltSoHeader.setOrderDate(creationDate);  // order date also same as creation date for update// (new Date());
		ltSoHeader.setLastUpdateDate(new Date()); // DateTimeClass.getCurrentDateTime()

		ltSoHeader.setSiebelInvoiceNumber(siebelData.getSiebelInvoiceNumber());
		ltSoHeader.setSiebelJsonpayload(siebelData.getSiebelJsonpayload());
		ltSoHeader.setSiebelRemark(siebelData.getSiebelRemark());
		ltSoHeader.setSiebelStatus(siebelData.getSiebelStatus());
		
//		System.out.print("Hii sievelgel 1"+siebelData.getSiebelInvoiceNumber());
//		System.out.print("Hii sievelgel 2"+siebelData.getSiebelJsonpayload());
//		System.out.print("Hii sievelgel 3"+siebelData.getSiebelStatus());
//		System.out.print("Hii sievelgel 4"+siebelData.getSiebelRemark());
		
		
		if(ltSoHeader.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
			System.out.print("Hi I'm in 1 in updateOrder for pending status ====");
			
			CompletableFuture.runAsync(() -> {
                try {
                	sendNotifications(ltSoHeader);
                    sendEmail(ltSoHeader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, executor);
			
//			sendNotifications(ltSoHeader);   comment on 24-may-2024 for optimization
//		    sendEmail(ltSoHeader);           comment on 24-may-2024 for optimization 
	     }
		
		System.out.print("New Update siebel call before  ====" +ltSoHeader.getStatus());
		if(ltSoHeader.getInStockFlag().equalsIgnoreCase("N") &&  ltSoHeader.getStatus().equalsIgnoreCase("APPROVED")) {
			System.out.print("New Update siebel out of stock order call in ====" +ltSoHeader.getStatus());
		saveOutOfStockOrderIntoSiebel(ltSoHeader, soHeaderDto);
		}else {
			System.out.print("New Update siebel instock order call in ====" +ltSoHeader.getStatus());
			saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
		}
		System.out.print("New Update siebel call after ====" +ltSoHeader.getStatus());
		
		LtSoHeaders ltSoHeaderUpdated = updateSoHeader(ltSoHeader);
		System.out.print("Final data is = "+ltSoHeaderUpdated);
			
		/*if(ltSoHeaderUpdated.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
			System.out.print("Hi I'm in 2 in updateOrder for pending status ====");
			sendNotifications(ltSoHeaderUpdated);
			sendEmail(ltSoHeader);
		}*/
		
		List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
		
		//StringBuffer strQuery1 =new StringBuffer();
		//strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
		//StringBuffer strQuery =  new StringBuffer();
		if(lineDeleteStatus >= 0) 
			{
				
//				for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
//					
//					SoLineDto soLineDto = (SoLineDto) iterator.next();
//					
//					strQuery.append("(");
//					
//					//LtSoLines ltSoLines = new LtSoLines();
//		
//					if (ltSoHeaderUpdated.getHeaderId() != null) {
//						//ltSoLines.setHeaderId(ltSoHeaderUpdated.getHeaderId());
//						strQuery.append(ltSoHeaderUpdated.getHeaderId()+",");
//					}
//					if (soLineDto.getProductId() != null) {
//						//ltSoLines.setProductId(soLineDto.getProductId());
//						strQuery.append("'"+soLineDto.getProductId()+"',");
//					}
//					if (soLineDto.getQuantity() != null) {
//						//ltSoLines.setQuantity(soLineDto.getQuantity());
//						strQuery.append(soLineDto.getQuantity()+",");
//					}
//					if (soLineDto.getListPrice() != null) {
//						//ltSoLines.setListPrice(soLineDto.getListPrice());
//						strQuery.append("'"+soLineDto.getListPrice()+"',");
//					}
//					if (soLineDto.getDeliveryDate() != null) {
//						//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
//						strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
//					}
//					
//					if (soLineDto.getStatus() != null) {
//						//ltSoLines.setStatus(soLineDto.getStatus());
//						strQuery.append("'"+soLineDto.getStatus()+"',");//Status
//					}
//					
//					if (soHeaderDto.getUserId() != null) {
//						//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
//						strQuery.append(soHeaderDto.getUserId()+",");
//					}
//					
//					strQuery.append("'"+new Date()+"',");//Created Date
//					
//					if (soHeaderDto.getUserId() != null) {
//						//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
//						strQuery.append(soHeaderDto.getUserId()+",");
//					}
//					
//					if (soHeaderDto.getUserId() != null) {
//						//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
//						strQuery.append(soHeaderDto.getUserId()+",");
//					}
//					
//					strQuery.append("'"+new Date()+"',");//Last update date
//					
//					if (soLineDto.getPtrPrice() != null) {
//						//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
//						strQuery.append("'"+soLineDto.getPtrPrice()+"'");
//					}else {
//						//ltSoLines.setPtrPrice(soLineDto.getListPrice());
//						strQuery.append("'"+soLineDto.getPtrPrice()+"'");
//					}
//					if (soLineDto.getPriceList() != null) {
//						soLineDto.setPriceList(soLineDto.getPriceList());
//					}
//					strQuery.append("),");
//					//ltSoLines.setLastUpdateDate(new Date());
//					//ltSoLines.setCreationDate(new Date());
//					//ltSoLines = ltSoLinesRepository.save(ltSoLines);
//				}
		
// this for loop commented on 23-May-2024 for code optimization purpose			
/*			for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
				
				SoLineDto soLineDto = (SoLineDto) iterator.next();	
				StringBuffer strQuery =  new StringBuffer();
				strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
				
				strQuery.append("(");

				//LtSoLines ltSoLines = new LtSoLines();
				
				if (ltSoHeader.getHeaderId() != null) {
					//ltSoLines.setHeaderId(ltSoHeader.getHeaderId());
					strQuery.append(ltSoHeader.getHeaderId()+",");
					
				}
				if (soLineDto.getProductId() != null) {
					//ltSoLines.setProductId(soLineDto.getProductId());
					strQuery.append("'"+soLineDto.getProductId()+"',");
				}
				if (soLineDto.getQuantity() != null) {
					//ltSoLines.setQuantity(soLineDto.getQuantity());
					strQuery.append(soLineDto.getQuantity()+",");
				}
				if (soLineDto.getListPrice() != null) {
					//ltSoLines.setListPrice(soLineDto.getListPrice());
					strQuery.append("'"+soLineDto.getListPrice()+"',");
				}
				else {
					//strQuery.append("'"+null+"',");	
					strQuery.append("'"+soLineDto.getListPrice()+"',");
				}
				//strQuery.append("'"+DRAFT.toString()+"',");//Status  
				strQuery.append("'"+soHeaderDto.getStatus()+"',");
				
				if (soHeaderDto.getUserId() != null) {
					//ltSoLines.setCreatedBy(soHeaderDto.getUserId());
					strQuery.append(soHeaderDto.getUserId()+",");
				}
				if (soHeaderDto.getUserId() != null) {
					//ltSoLines.setLastUpdateLogin(soHeaderDto.getUserId());
					strQuery.append(soHeaderDto.getUserId()+",");
				}
				if (soHeaderDto.getUserId() != null) {
					//ltSoLines.setLastUpdatedBy(soHeaderDto.getUserId());
					strQuery.append(soHeaderDto.getUserId()+",");
				}
				if (soLineDto.getPtrPrice() != null) {
					//ltSoLines.setPtrPrice(soLineDto.getPtrPrice());
					strQuery.append("'"+soLineDto.getPtrPrice()+"',");
				}else {
					//ltSoLines.setPtrPrice(soLineDto.getListPrice());
					strQuery.append("'"+soLineDto.getPtrPrice()+"',");
				}
			//	if(soLineDto.getEimStatus()!= null) {
				strQuery.append("'"+null+"',"); // eimstatus
			//	} 		
				if (soLineDto.getDeliveryDate() != null) {
					//ltSoLines.setDeliveryDate(soLineDto.getDeliveryDate());
					
					//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");   ///"E MMM dd HH:mm:ss Z yy");
					Date date = soLineDto.getDeliveryDate();
					SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");  //"dd-MM-yyyy");
					String deliveryDate =outputFormat.format(date);
					System.out.println("formatedDate : " + deliveryDate); 
										
					strQuery.append("'"+deliveryDate+"',");
					//strQuery.append("'"+null+"',");
					
				// strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
				}
				
				//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");    //"E MMM dd HH:mm:ss Z yy");
				Date date = ltSoHeader.getCreationDate();
				Date date1 = ltSoHeader.getLastUpdateDate();
				SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");   //"dd-MM-yyyy");
				String crationDate =outputFormat.format(date);
				String lastUpdateDate =outputFormat.format(date1);
				System.out.println("crationDate : " + crationDate);
				System.out.println("lastUpdateDate : " + lastUpdateDate);
				
				//strQuery.append("'"+new Date()+"',");//Created Date
				strQuery.append("'"+crationDate+"',");  
				
				//strQuery.append("'"+null+"',");    // set null for demo
				
				//strQuery.append("'"+new Date()+"'");//Last update date
				strQuery.append("'"+lastUpdateDate+"'");
				//strQuery.append("'"+null+"'");    // set null for demo
								
				//ltSoLines.setStatus(DRAFT);
				//ltSoLines.setLastUpdateDate(new Date());
				//ltSoLines.setCreationDate(new Date());
//				strQuery.append("),");
				
				strQuery.append(")");
				String query = strQuery.toString();
				int n = ltSoHeadersDao.insertLine(query);
			}*/
			
System.out.println("3820 = "+new Date());
			
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
		    List<String> queries = new ArrayList<>();
 
		    for (SoLineDto soLineDto : soLineDtoList) {
		        StringBuilder strQuery = new StringBuilder();
		        strQuery.append("INSERT INTO lt_so_lines (header_id, product_id, quantity, list_price, status, created_by, last_update_login, last_updated_by, ptr_price, eimstatus, delivery_date, creation_date, last_update_date) VALUES (");
 
		        strQuery.append(ltSoHeader.getHeaderId()).append(", ");
		        strQuery.append("'").append(soLineDto.getProductId()).append("', ");
		        strQuery.append(soLineDto.getQuantity()).append(", ");
		        strQuery.append("'").append(soLineDto.getListPrice()).append("', ");
		        strQuery.append("'").append(soHeaderDto.getStatus()).append("', ");
		        strQuery.append(soHeaderDto.getUserId()).append(", ");
		        strQuery.append(soHeaderDto.getUserId()).append(", ");
		        strQuery.append(soHeaderDto.getUserId()).append(", ");
		        strQuery.append("'").append(soLineDto.getPtrPrice()).append("', ");
		        strQuery.append("NULL, "); // eimstatus
 
		        if (soLineDto.getDeliveryDate() != null) {
		            String deliveryDate = outputFormat.format(soLineDto.getDeliveryDate());
		            strQuery.append("'").append(deliveryDate).append("', ");
		        } else {
		            strQuery.append("NULL, ");
		        }
 
		        String creationDate1 = outputFormat.format(ltSoHeader.getCreationDate());
		        String lastUpdateDate = outputFormat.format(ltSoHeader.getLastUpdateDate());
 
		        strQuery.append("'").append(creationDate1).append("', ");
		        strQuery.append("'").append(lastUpdateDate).append("')");
 
		        queries.add(strQuery.toString());
		    }
		    inQuerybatchInsert = System.currentTimeMillis();
		    int[] updateCounts = ltSoHeadersDao.batchInsert(queries);
		    outQuerybatchInsert = System.currentTimeMillis();
		    
		    System.out.println("Inserted " + updateCounts.length + " lines.");
		    System.out.println("3864 = "+new Date());
			
			}
				
		ltSoHeader.setSiebelInvoiceNumber(soHeaderDto.getSiebelInvoicenumber());
		ltSoHeader.setSiebelStatus(soHeaderDto.getSiebelStatus());
		//ltSoHeader.setSiebelRemark(soHeaderDto.getSiebelRemark());
		
		//strQuery.deleteCharAt(strQuery.length()-1); 
		//strQuery1 = strQuery1.append(strQuery);
		
		//String query =strQuery1.toString();
		//int n = ltSoHeadersDao.insertLine(query);
		
		//if (n > 0) {
		
			RequestDto requestDto = new RequestDto();
			requestDto.setHeaderId(ltSoHeaderUpdated.getHeaderId());
			requestDto.setOrderNumber(ltSoHeaderUpdated.getOrderNumber());
			if(ltSoHeader.getCustomerId() != null) {
				requestDto.setCustomerId(ltSoHeader.getCustomerId());
			}
			requestDto.setLimit(-1);
			requestDto.setOffset(2);
		//	requestDto.setUserId(soHeaderDto.getUserId());
			status = getOrderV2(requestDto);
			
			OrderDetailsDto orderDetailsDtoStatus=(OrderDetailsDto) status.getData();
			orderDetailsDtoStatus.getSoHeaderDto().get(0).setStatus(ltSoHeaderUpdated.getStatus());
			if(ltSoHeader.getAddress() != null) {
				orderDetailsDtoStatus.getSoHeaderDto().get(0).setAddress(ltSoHeader.getAddress());
			}
			//System.out.print("New Update siebel call before  ====" +ltSoHeaderUpdated.getStatus());
			//if(ltSoHeaderUpdated.getStatus().equalsIgnoreCase("APPROVED")) {
			//	System.out.print("New Update siebel call in ====" +ltSoHeaderUpdated.getStatus());
			//saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
			//}
			System.out.print("New Update siebel call after ====" +ltSoHeaderUpdated.getStatus());
			status.setData(orderDetailsDtoStatus);
			
			timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetSiebelDataById,outQuerygetSiebelDataById));
			timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerydeleteLineDataByHeaderIdAndReturnStatus,outQuerydeleteLineDataByHeaderIdAndReturnStatus));
			timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetMobileNumber, outQuerygetMobileNumber));
			timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerybatchInsert, outQuerybatchInsert));
//			timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//			timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
			
			long methodOut = System.currentTimeMillis();
			System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
	        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	        status.setTimeDifference(timeDifference);
			
			status.setCode(UPDATE_SUCCESSFULLY);
			status.setMessage("Update Successfully");
			return status;
	}//}
		
		//return null;
	catch(Exception e) {
		logger.error("Error Description :", e);
		e.printStackTrace();
		return null;
		}
		finally {
			System.out.println("Connection closed in update");
	        executor.shutdown();
	    }
	}

/*  this is	original method comment on 27-may-24 for optimization purpose
	@Override
	public Status getOrderV2(RequestDto requestDto) throws ServiceException, IOException {
		try {
			Status status = new Status();
			//System.out.println("Login Id is ="+requestDto.getLoginId());
			List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
			System.out.print("headerIdsList is ====" +headerIdsList);
			System.out.print("requestDto data is =="+requestDto);
			Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
			//Long recordCount = (long) headerIdsList.size() + 1;
			
			System.out.println("headerIdsList====>"+headerIdsList.size());
			//System.out.println("recordCount====>"+recordCount);
			
			status.setTotalCount(recordCount);
			status.setRecordCount(recordCount);
			if(headerIdsList.isEmpty()) {
				status.setCode(RECORD_NOT_FOUND);
				status.setData("Record not found");
				return status;
			}
			
			// code for not showing pending_approval to himself
			
//			List<Long> headerIdsListUpdated = new ArrayList<Long>();
//					if(!headerIdsList.isEmpty()) {
//						for(Long headerId:headerIdsList) {
//							SoHeaderDto list = ltSoHeadersDao.getheaderByHeaderId(headerId);
//							System.out.println("listlistlist=="+list);
//							System.out.println("list.getCreatedBy() is="+list.getCreatedBy());
//							System.out.println("requestDto.getUserId() is ="+requestDto.getLoginId());
//							System.out.println("list.getStatus() is="+list.getStatus());
//							
//							if (!(list.getCreatedBy() == requestDto.getLoginId()) && !list.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
//										headerIdsListUpdated.add(headerId);
//										System.out.println("header Ids List Updated is="+headerIdsListUpdated);
//									}
//						}
//					}
			
			
			List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
			
			responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsList);
			
		//	responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsListUpdated);
			
			Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<Long, SoHeaderDto>();
			Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<Long, List<SoLineDto>>();

//			System.out.println("responseDto befoe filter is ===" +responseDtoList1);
//			System.out.println("UserId is ===="+requestDto.getUserId());
//			List<ResponseDto> responseDtoList = responseDtoList1.stream().filter(X->X.getUserId()!=requestDto.getUserId()).collect(Collectors.toList());
//			System.out.println("responseDto after filter is ===" +responseDtoList);
			
			for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
				ResponseDto responseDto = (ResponseDto) iterator.next();
                						
				System.out.println("responseDto is ===" +responseDto);
				
				SoLineDto soLineDto = new SoLineDto();

				if (responseDto.getLineId() != null) {
					soLineDto.setLineId(responseDto.getLineId());
				}
				if (responseDto.getProductId() != null) {
					soLineDto.setProductId(responseDto.getProductId());
				}
				if (responseDto.getQuantity() != null) {
					soLineDto.setQuantity(responseDto.getQuantity());
				}
				if (responseDto.getProductCode() != null) {
					soLineDto.setProductCode(responseDto.getProductCode());
				}
				if (responseDto.getProductDesc() != null) {
					soLineDto.setProductDesc(responseDto.getProductDesc());
				}
				if (responseDto.getProductName() != null) {
					soLineDto.setProductName(responseDto.getProductName());
				}
				if (responseDto.getPriceList() != null) {
					soLineDto.setPriceList(responseDto.getPriceList());
					
				}
			
				if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) 
					|| responseDto.getStatus().equals("APPROVED")) {
					if (responseDto.getListPrice() != null) {
						soLineDto.setListPrice(responseDto.getListPrice());
					}
				}else {
					if (responseDto.getLinelistPrice() != null) {
						soLineDto.setListPrice(responseDto.getLinelistPrice());
					}
				}
								
				if (responseDto.getPtrPrice() != null) {
					if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						soLineDto.setPtrPrice(responseDto.getListPrice());
					}else {
						soLineDto.setPtrPrice(responseDto.getPtrPrice());
					}
				}
			
				if (responseDto.getPtrPrice() != null) {
					//System.out.println("IF responseDto.getPtrPrice() != null "+responseDto.getPtrPrice());
					if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//soLineDto.setPtrPrice(responseDto.getListPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
								responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
							if (responseDto.getListPrice() != null) {
								soLineDto.setListPrice(responseDto.getListPrice());
							}
						}else {
							if (responseDto.getLinelistPrice() != null) {
								soLineDto.setListPrice(responseDto.getLinelistPrice());
							}
						}
					}else {
						//soLineDto.setPtrPrice(responseDto.getPtrPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
								responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
							if (responseDto.getPtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getPtrPrice());
							}
						}else {
							if (responseDto.getLinePtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getLinePtrPrice());
							}
						}
					}
				}
				
				if (responseDto.getInventoryQuantity() != null) {
					soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
				}
				if (responseDto.getDeliveryDate() != null) {
					soLineDto.setDeliveryDate(responseDto.getDeliveryDate1());
				}
				if (responseDto.getOrgId() != null) {
					soLineDto.setOrgId(responseDto.getOrgId());
				}
				if (responseDto.getProductType() != null) {
					soLineDto.setProductType(responseDto.getProductType());
				}
				if (responseDto.getPrimaryUom() != null) {
					soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
				}
				if (responseDto.getSecondaryUom() != null) {
					soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
				}
				if (responseDto.getSecondaryUomValue() != null) {
					soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
				}
				if (responseDto.getUnitsPerCase() != null) {
					soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
				}
				if (responseDto.getProductImage() != null) {
					soLineDto.setProductImage(responseDto.getProductImage());
				}
				if (responseDto.getBrand() != null) {
					soLineDto.setBrand(responseDto.getBrand());
				}
				if (responseDto.getSubBrand() != null) {
					soLineDto.setSubBrand(responseDto.getSubBrand());
				}
				if (responseDto.getSegment() != null) {
					soLineDto.setSegment(responseDto.getSegment());
				}
				if (responseDto.getCasePack() != null) {
					soLineDto.setCasePack(responseDto.getCasePack());
				}
				if (responseDto.getHsnCode() != null) {
					soLineDto.setHsnCode(responseDto.getHsnCode());
				}
				if (responseDto.getThumbnailImage() != null) {
					soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
				}

				if (soLineDtoMap.get(responseDto.getHeaderId()) != null) {
					// already exost
					List<SoLineDto> soLineDtoList = soLineDtoMap.get(responseDto.getHeaderId());
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				} else {
					// add data into soheader map
					SoHeaderDto soHeaderDto = new SoHeaderDto();
					soHeaderDto.setHeaderId(responseDto.getHeaderId());
					soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
					
					//FOR DEV UTC TIME ZONE
					//soHeaderDto.setOrderDate(responseDto.getOrderDate().toString());
					
					//FOR DEV IST TIME ZONE
					final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
					final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
					final TimeZone utc = TimeZone.getTimeZone("UTC");
					sdf.setTimeZone(utc);
					soHeaderDto.setOrderDate(sdf.format(responseDto.getOrderDate()));
					
					soHeaderDto.setStatus(responseDto.getStatus());
//					if(soHeaderDto.getStatus().equalsIgnoreCase("APPROVED")) {
//						saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
//					}
					
					soHeaderDto.setAddress(responseDto.getAddress());
					soHeaderDto.setOutletName(responseDto.getOutletName());
					soHeaderDto.setOutletId(responseDto.getOutletId());
					soHeaderDto.setOutletCode(responseDto.getOutletCode());
					soHeaderDto.setLatitude(responseDto.getLatitude());
					soHeaderDto.setLongitude(responseDto.getLongitude());
					
					soHeaderDto.setInstockFlag(responseDto.getInstockFlag());
					soHeaderDto.setPriceList(responseDto.getPriceList());
					//soHeaderDto.setBeatId(responseDto.getBeatId());
					try {
					System.out.println("Hi Siebel in get order v2 try = SiebelStatus"+responseDto.getSiebelStatus());
					//System.out.println("SiebelRemark"+responseDto.getSiebelRemark());
					//System.out.println(responseDto.getSiebelInvoicenumber());
					//System.out.println("SiebelJsonpayload"+responseDto.getSiebelJsonpayload());
					
					//soHeaderDto.setSiebelStatus(responseDto.getSiebelStatus());
					soHeaderDto.setSiebelRemark(responseDto.getSiebelRemark());
					//soHeaderDto.setSiebelInvoicenumber(responseDto.getSiebelInvoicenumber());
					//soHeaderDto.setSiebelJsonpayload(responseDto.getSiebelJsonpayload());
					
					/*System.out.println("Hi Siebel 123= SiebelStatus"+soHeaderDto.getSiebelStatus());
					System.out.println("SiebelRemark"+soHeaderDto.getSiebelRemark());
					System.out.println(soHeaderDto.getSiebelInvoicenumber());
					System.out.println("SiebelJsonpayload"+responseDto.getSiebelJsonpayload());*/
//					}catch(Exception e) {
//						logger.error("Error Description :", e);
//						e.printStackTrace();
//					}
//					if(responseDto.getBeatId()!= null) {
//					soHeaderDto.setBeatId(responseDto.getBeatId());
//					}
//					if(responseDto.getHeaderPriceList()!= null) {
//					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
//					}
//					
//					if (responseDto.getProprietorName() != null) {
//						soHeaderDto.setProprietorName(responseDto.getProprietorName());
//					}
//					if (responseDto.getRemark() != null) {
//						soHeaderDto.setRemark(responseDto.getRemark());
//					}
//					if (responseDto.getDeliveryDate() != null) {
//						soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
//					}
//					if (responseDto.getUserId() != null) {
//						soHeaderDto.setUserId(responseDto.getUserId());
//					}
//					if (responseDto.getOutletAddress() != null) {
//						soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
//					}
//					if (responseDto.getCustomerId() != null) {
//						soHeaderDto.setCustomerId(responseDto.getCustomerId());
//					}
//					if (responseDto.getAddress() != null) {
//						soHeaderDto.setAddress(responseDto.getAddress());
//					}
//					
//					if (responseDto.getCity() != null) { soHeaderDto.setCity(responseDto.getCity()); }
//					
//					if(responseDto.getBeatId()!= null) {
//						soHeaderDto.setBeatId(responseDto.getBeatId());
//					}
//					if(responseDto.getHeaderPriceList()!= null) {
//					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
//					}
//					
//					soHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);
//
//					List<SoLineDto> soLineDtoList = new ArrayList<SoLineDto>();
//					if(soLineDto.getLineId() != null) {
//						soLineDtoList.add(soLineDto);
//					}
//					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
//				}
//
//			}
//			OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
//
//			List<SoHeaderDto> soHeaderDtoList = new ArrayList<SoHeaderDto>();
//
//			for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
//				SoHeaderDto soHeaderDto = entry.getValue();
//
//				List<SoLineDto> soLineDtoList = soLineDtoMap.get(entry.getKey());
//				soHeaderDto.setSoLineDtoList(soLineDtoList);
//
//				soHeaderDtoList.add(soHeaderDto);
//			}
//			orderDetailsDto.setSoHeaderDto(soHeaderDtoList);
//
//			if (!responseDtoList.isEmpty()) {
//				status.setCode(RECORD_FOUND);// = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
//				status.setData(orderDetailsDto);
//				return status;
//			}
//			status.setCode(RECORD_NOT_FOUND); // status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
//			status.setData(null);
//			return status;
//		} catch (Exception e) {
//			logger.error("Error Description :", e);
//			e.printStackTrace();
//		} return null;
//	}

// this is end of original method comment on 27-may-2024 for optimization purpose	
	
/*	// this is new dev comment on 18-June-2024 for api time difference calculation
		public Status getOrderV2(RequestDto requestDto) throws ServiceException, IOException {
			System.out.println("In getOrderV2 method = "+LocalDateTime.now());
		    try {
		        Status status = new Status();
		        System.out.println("Above getSoHeader query call method at = "+LocalDateTime.now());
		        List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
		        System.out.println("header Ids List = "+ headerIdsList);
		        System.out.println("Below getSoHeader query call method at = "+LocalDateTime.now());
		        System.out.println("Above getRecordCount query call method at = "+LocalDateTime.now());
		        Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
		        System.out.println("Below getRecordCount query call method at = "+LocalDateTime.now());
		        status.setTotalCount(recordCount);
		        status.setRecordCount(recordCount);
	 
		        if (headerIdsList.isEmpty()) {
		            status.setCode(RECORD_NOT_FOUND);
		            status.setData("Record not found");
		            return status;
		        }
		        System.out.println("Above headerIdsList getOrderV2 query call method at = "+LocalDateTime.now()); 
		        List<ResponseDto> responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsList);
		        System.out.println("Below headerIdsList getOrderV2 query call method at = "+LocalDateTime.now());
		        Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<>();
		        Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<>();
	 
//		        for (ResponseDto responseDto : responseDtoList) {
//		            SoLineDto soLineDto = buildSoLineDto(responseDto);
	//
//		            soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>());
//		            if (soLineDto.getLineId() != null) {
//		                soLineDtoMap.get(responseDto.getHeaderId()).add(soLineDto);
//		            }
	//
//		            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto(responseDto));
//		        }
		        
		        for (ResponseDto responseDto : responseDtoList) {
		            SoLineDto soLineDto = buildSoLineDto(responseDto);
	 
		            // Add soLineDto to the soLineDtoMap only if lineId is not null
		            if (soLineDto.getLineId() != null) {
		                soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>()).add(soLineDto);
		            }
	 
		            // Add or update soHeaderDto in the soHeaderDtoMap
		            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto(responseDto));
		        }
	 
//		        List<SoHeaderDto> soHeaderDtoList = new ArrayList<>();
//		        for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
//		            SoHeaderDto soHeaderDto = entry.getValue();
//		            soHeaderDto.setSoLineDtoList(soLineDtoMap.get(entry.getKey()));
//		            soHeaderDtoList.add(soHeaderDto);
//		        }
		        
		        List<SoHeaderDto> soHeaderDtoList = new ArrayList<>(soHeaderDtoMap.size());
	 
		        soHeaderDtoMap.forEach((headerId, soHeaderDto) -> {
		            soHeaderDto.setSoLineDtoList(soLineDtoMap.get(headerId));
		            soHeaderDtoList.add(soHeaderDto);
		        });
	 
		        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
		        orderDetailsDto.setSoHeaderDto(soHeaderDtoList);
	 
		        if (!responseDtoList.isEmpty()) {
		            status.setCode(RECORD_FOUND);
		            status.setData(orderDetailsDto);
		            System.out.println("Exit from getOrderV2 method = "+LocalDateTime.now());
		            return status;
		        }
	 
		        status.setCode(RECORD_NOT_FOUND);
		        status.setData(null);
		        System.out.println("Exit from getOrderV2 method = "+LocalDateTime.now());
		        return status;
		    } catch (Exception e) {
		        logger.error("Error Description :", e);
		        e.printStackTrace();
		    }
		    return null;
		}
*/
	
//* this is original code comment on 27-June-2024 to use procedure 	
	public Status getOrderV2(RequestDto requestDto) throws ServiceException, IOException {
		System.out.println("In getOrderV2 method = "+LocalDateTime.now());
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetSoHeader = 0;
		long outQuerygetSoHeader = 0;
		long inQuerygetRecordCount = 0;
		long outQuerygetRecordCount = 0;
		long inQuerygetOrderV2 = 0;
		long outQuerygetOrderV2 = 0;
	    try {
	        Status status = new Status();
	        inQuerygetSoHeader = System.currentTimeMillis();
	        System.out.println("Above getSoHeader query call method at = "+LocalDateTime.now());
	        List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
	        outQuerygetSoHeader = System.currentTimeMillis();
	        System.out.println("Below getSoHeader query call method at = "+LocalDateTime.now());
	        inQuerygetRecordCount = System.currentTimeMillis();
	        System.out.println("Above getRecordCount query call method at = "+LocalDateTime.now());
	        Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
	        outQuerygetRecordCount = System.currentTimeMillis();
	        System.out.println("Below getRecordCount query call method at = "+LocalDateTime.now());
	        
	        status.setTotalCount(recordCount);
	        status.setRecordCount(recordCount);

	        if (headerIdsList.isEmpty()) {
	            status.setCode(RECORD_NOT_FOUND);
	            status.setData("Record not found");
	            return status;
	        }

	        inQuerygetOrderV2 = System.currentTimeMillis();
	        System.out.println("Above headerIdsList getOrderV2 query call method at = "+LocalDateTime.now());
	        List<ResponseDto> responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsList);
	        outQuerygetOrderV2 = System.currentTimeMillis();
	        System.out.println("Below headerIdsList getOrderV2 query call method at = "+LocalDateTime.now());
	        Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<>();
	        Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<>();

//	        for (ResponseDto responseDto : responseDtoList) {
//	            SoLineDto soLineDto = buildSoLineDto(responseDto);
//
//	            soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>());
//	            if (soLineDto.getLineId() != null) {
//	                soLineDtoMap.get(responseDto.getHeaderId()).add(soLineDto);
//	            }
//
//	            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto(responseDto));
//	        }
	        
	        for (ResponseDto responseDto : responseDtoList) {
	            SoLineDto soLineDto = buildSoLineDto(responseDto);

	            // Add soLineDto to the soLineDtoMap only if lineId is not null
	            if (soLineDto.getLineId() != null) {
	                soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>()).add(soLineDto);
	            }

	            // Add or update soHeaderDto in the soHeaderDtoMap
	            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto(responseDto));
	        }

//	        List<SoHeaderDto> soHeaderDtoList = new ArrayList<>();
//	        for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
//	            SoHeaderDto soHeaderDto = entry.getValue();
//	            soHeaderDto.setSoLineDtoList(soLineDtoMap.get(entry.getKey()));
//	            soHeaderDtoList.add(soHeaderDto);
//	        }
	        
	        List<SoHeaderDto> soHeaderDtoList = new ArrayList<>(soHeaderDtoMap.size());

	        soHeaderDtoMap.forEach((headerId, soHeaderDto) -> {
	            soHeaderDto.setSoLineDtoList(soLineDtoMap.get(headerId));
	            soHeaderDtoList.add(soHeaderDto);
	        });

	        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
	        orderDetailsDto.setSoHeaderDto(soHeaderDtoList);

	        if (!responseDtoList.isEmpty()) {
	            status.setCode(RECORD_FOUND);
	            status.setData(orderDetailsDto);
	            timeDifference.put("QuerygetSoHeader", timeDiff(inQuerygetSoHeader,outQuerygetSoHeader));
	            timeDifference.put("QuerygetRecordCount", timeDiff(inQuerygetRecordCount,outQuerygetRecordCount));
	            timeDifference.put("QuerygetOrderV2", timeDiff(inQuerygetOrderV2,outQuerygetOrderV2));
	            System.out.println("Exit from getOrderV2 method = "+LocalDateTime.now());
	            long methodOut = System.currentTimeMillis();
	            timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	            status.setTimeDifference(timeDifference);
	            return status;
	        }

	        status.setCode(RECORD_NOT_FOUND);
	        status.setData(null);
            System.out.println("Exit from getOrderV2 method = "+LocalDateTime.now());
	        return status;
	    } catch (Exception e) {
	        logger.error("Error Description :", e);
	        e.printStackTrace();
	    }
	    return null;
	}
//	this is end of original code comment on 27-June-2024 to use procedure //
	
/*	
	public Status getOrderV2(RequestDto requestDto) throws ServiceException, IOException {
		System.out.println("In getOrderV2 method = "+LocalDateTime.now());
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetSoHeader = 0;
		long outQuerygetSoHeader = 0;
		long inQuerygetRecordCount = 0;
		long outQuerygetRecordCount = 0;
		long inQuerygetOrderV2 = 0;
		long outQuerygetOrderV2 = 0;
	    try {
	        Status status = new Status();
	        //inQuerygetSoHeader = System.currentTimeMillis();
	        //System.out.println("Above getSoHeader query call method at = "+LocalDateTime.now());
	        List<Long> headerIdsList = ltSoHeadersDao.getSoHeaderFromProcedure();
	        //outQuerygetSoHeader = System.currentTimeMillis();
	        //System.out.println("Below getSoHeader query call method at = "+LocalDateTime.now());
	        //inQuerygetRecordCount = System.currentTimeMillis();
	        //System.out.println("Above getRecordCount query call method at = "+LocalDateTime.now());
	        //Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
	        //outQuerygetRecordCount = System.currentTimeMillis();
	        //System.out.println("Below getRecordCount query call method at = "+LocalDateTime.now());
	        
	        //status.setTotalCount(recordCount);
	        //status.setRecordCount(recordCount);

	        if (headerIdsList.isEmpty()) {
	            status.setCode(RECORD_NOT_FOUND);
	            status.setData("Record not found");
	            return status;
	        }

	        //inQuerygetOrderV2 = System.currentTimeMillis();
	        //System.out.println("Above headerIdsList getOrderV2 query call method at = "+LocalDateTime.now());
	        List<ResponseDto> responseDtoList = ltSoHeadersDao.getOrderV2FromProcedure(headerIdsList);
	       // outQuerygetOrderV2 = System.currentTimeMillis();
	       // System.out.println("Below headerIdsList getOrderV2 query call method at = "+LocalDateTime.now());
	        Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<>();
	        Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<>();

//	        for (ResponseDto responseDto : responseDtoList) {
//	            SoLineDto soLineDto = buildSoLineDto(responseDto);
//
//	            soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>());
//	            if (soLineDto.getLineId() != null) {
//	                soLineDtoMap.get(responseDto.getHeaderId()).add(soLineDto);
//	            }
//
//	            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto(responseDto));
//	        }
	        
	        for (ResponseDto responseDto : responseDtoList) {
	            SoLineDto soLineDto = buildSoLineDto(responseDto);

	            // Add soLineDto to the soLineDtoMap only if lineId is not null
	            if (soLineDto.getLineId() != null) {
	                soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>()).add(soLineDto);
	            }

	            // Add or update soHeaderDto in the soHeaderDtoMap
	            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto(responseDto));
	        }

//	        List<SoHeaderDto> soHeaderDtoList = new ArrayList<>();
//	        for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
//	            SoHeaderDto soHeaderDto = entry.getValue();
//	            soHeaderDto.setSoLineDtoList(soLineDtoMap.get(entry.getKey()));
//	            soHeaderDtoList.add(soHeaderDto);
//	        }
	        
	        List<SoHeaderDto> soHeaderDtoList = new ArrayList<>(soHeaderDtoMap.size());

	        soHeaderDtoMap.forEach((headerId, soHeaderDto) -> {
	            soHeaderDto.setSoLineDtoList(soLineDtoMap.get(headerId));
	            soHeaderDtoList.add(soHeaderDto);
	        });

	        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
	        orderDetailsDto.setSoHeaderDto(soHeaderDtoList);

	        if (!responseDtoList.isEmpty()) {
	            status.setCode(RECORD_FOUND);
	            status.setData(orderDetailsDto);
	            timeDifference.put("QuerygetSoHeader", timeDiff(inQuerygetSoHeader,outQuerygetSoHeader));
	            timeDifference.put("QuerygetRecordCount", timeDiff(inQuerygetRecordCount,outQuerygetRecordCount));
	            timeDifference.put("QuerygetOrderV2", timeDiff(inQuerygetOrderV2,outQuerygetOrderV2));
	            System.out.println("Exit from getOrderV2 method = "+LocalDateTime.now());
	            long methodOut = System.currentTimeMillis();
	            timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	            status.setTimeDifference(timeDifference);
	            return status;
	        }

	        status.setCode(RECORD_NOT_FOUND);
	        status.setData(null);
            System.out.println("Exit from getOrderV2 method = "+LocalDateTime.now());
	        return status;
	    } catch (Exception e) {
	        logger.error("Error Description :", e);
	        e.printStackTrace();
	    }
	    return null;
	}
*/
	

/*	//on 4july
	public Status getOrderV2(RequestDto requestDto) {
		long methodIn = System.currentTimeMillis();
		System.out.println("methodIn time"+ LocalDateTime.now());
        
		Map<String,String> timeDifference = new HashMap<>();
		
		
        Status status = new Status();
        System.out.println("RequestDto in getoderV2 procedure"+requestDto);
        List<LtOrderLineDataGt> resultList = new ArrayList<>();
         //LtOrderLineDataGt ltOrderLineDataGt;
        long beforeProcedureCallGetOrderV2 = System.currentTimeMillis();
        return jdbcTemplate.execute((Connection conn) -> {
            try (CallableStatement callableStatement = conn.prepareCall("{call LT_GET_ORDER_GT_NEW_1(?,?,?,?,?,?,?,?,?)}")) {
                // Set input parameters
            	int Limit = requestDto.getLimit();
            	if(requestDto.getUserId()!= null) {
                callableStatement.setLong(1, requestDto.getUserId());}
                callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
                callableStatement.setString(3, requestDto.getDistributorId());
                callableStatement.setString(4, requestDto.getStatus());
                callableStatement.setString(5, requestDto.getOrderNumber());
                callableStatement.setString(6, requestDto.getSearchField());
                callableStatement.setString(7, requestDto.getOutletId());
                if(requestDto.getLimit()== 1 || requestDto.getLimit()== 0) {
                	Limit = -1;                	
                }
                callableStatement.setInt(8, Limit);
                callableStatement.setInt(9, requestDto.getOffset());
                
                //System.out.println("requestDto is = "+requestDto.getUserId()+requestDto.getDistributorId());
                // Execute the stored procedure
                System.out.println("above result");
                boolean result = callableStatement.execute();
                long afterProcedureCallGetOrderV2 = System.currentTimeMillis();
				timeDifference.put("procedureExecutionTime", timeDiff(beforeProcedureCallGetOrderV2,afterProcedureCallGetOrderV2));

                System.out.println("below result");
                //Object lmn =  callableStatement.getObject(2);
               //System.out.println("lmn" + lmn);
                System.out.println("before callable" + LocalDateTime.now());
                ResultSet rs = (ResultSet) callableStatement.getObject(2);
                
                //rs.setFetchSize(3);
              
                System.out.println("after callable" + LocalDateTime.now());
                System.out.println("ResultSet = " + rs);
               // List<LtOrderLineDataGt> resultList = new ArrayList<>();
//                System.out.println("before while" + LocalDateTime.now());
               //int batchSize = 1000;
                //List<LtOrderLineDataGt> batchList = new ArrayList<>(batchSize);
                while (rs.next()) {     
                    // Fetching values from ResultSet into local variables to reduce repeated method calls.
                    long inventoryQuantity = rs.getLong("inventoryQuantity");     
                    Date cdate = rs.getDate("Cdate");     
                    String outletAddress = rs.getString("outletAddress");     
                    long headerId = rs.getLong("HEADER_ID");     
                    String orderNumber = rs.getString("order_number");     
                    String productId = rs.getString("product_id");     
                    String categoryId = rs.getString("category_id");     
                    String orgId = rs.getString("org_id");     
                    String subCategoryId = rs.getString("sub_category_id");     
                    String productType = rs.getString("product_type");     
                    String category = rs.getString("category");     
                    String subCategory = rs.getString("subCategory");     
                    String productCode = rs.getString("product_code");     
                    String productName = rs.getString("product_name");     
                    String productDesc = rs.getString("product_desc");     
                    String primaryUom = rs.getString("primary_uom");     
                    String secondaryUom = rs.getString("secondary_uom");     
                    long secondaryUomValue = rs.getLong("secondary_uom_value");     
                    long unitsPerCase = rs.getLong("units_per_case");     
                    String segment = rs.getString("segment");     
                    String style = rs.getString("style");     
                    String flavor = rs.getString("flavor");     
                    long casePack = rs.getLong("case_pack");     
                    String hsnCode = rs.getString("hsn_code");     
                    String orderable = rs.getString("orderable");     
                    String status1 = rs.getString("status1");     
                    Date orderDate = rs.getDate("order_date");     
                    // String status = rs.getString("status");     
                    String address = rs.getString("address");     
                    String latitude = rs.getString("latitude");     
                    String longitude = rs.getString("longitude");     
                    String remark = rs.getString("remark");     
                    Date deliveryDate = rs.getDate("delivery_date");     
                    long userid = rs.getLong("USERID");     
                    String instockFlag = rs.getString("instock_flag");     
                    String beatId = rs.getString("beat_id");     
                    String outletId = rs.getString("outlet_id");     
                    String outletName = rs.getString("outlet_name");     
                    String outletCode = rs.getString("outlet_code");     
                    String proprietorName = rs.getString("proprietor_name");     
                    double listPrice = rs.getDouble("list_price");     
                    String priceList = rs.getString("price_list");     
                    long lineId = rs.getLong("line_id"); 
                    String productId1 = rs.getString("product_id_1"); 
                    long quantity = rs.getLong("quantity"); 
                    Date deliveryDate1 = rs.getDate("delivery_date1"); 
                    long statusO = rs.getLong("status_o"); 

                    // Creating the object once outside of the loop can save some time.
                    LtOrderLineDataGt ltOrderLineDataGt = new LtOrderLineDataGt();     
                    // Setting values to the object. 
                    ltOrderLineDataGt.setInventoryQuantity(inventoryQuantity); 
                    ltOrderLineDataGt.setCdate(cdate); 
                    ltOrderLineDataGt.setOutletAddress(outletAddress); 
                    ltOrderLineDataGt.setHeaderId(headerId);
                    ltOrderLineDataGt.setOrderNumber(orderNumber);
                    ltOrderLineDataGt.setProductId(productId);
                    ltOrderLineDataGt.setCategoryId(categoryId);
                    ltOrderLineDataGt.setOrgId(orgId);
                    ltOrderLineDataGt.setSubCategoryId(subCategoryId);
                    ltOrderLineDataGt.setProductType(productType);
                    ltOrderLineDataGt.setCategory(category);
                    ltOrderLineDataGt.setSubCategory(subCategory);
                    ltOrderLineDataGt.setProductCode(productCode);
                    ltOrderLineDataGt.setProductName(productName);
                    ltOrderLineDataGt.setProductDesc(productDesc);
                    ltOrderLineDataGt.setPrimaryUom(primaryUom);
                    ltOrderLineDataGt.setSecondaryUom(secondaryUom);
                    ltOrderLineDataGt.setSecondaryUomValue(secondaryUomValue);
                    ltOrderLineDataGt.setUnitPerCase(unitsPerCase);
                    ltOrderLineDataGt.setSegment(segment);
                    ltOrderLineDataGt.setStyle(style);
                    ltOrderLineDataGt.setFlavor(flavor);
                    ltOrderLineDataGt.setCasePack(casePack);
                    ltOrderLineDataGt.setHsnCode(hsnCode);
                    ltOrderLineDataGt.setOrderable(orderable);
                    ltOrderLineDataGt.setStatus1(status1);
                    ltOrderLineDataGt.setOrderDate(orderDate);
                    // ltOrderLineDataGt.setStatus(status);
                    ltOrderLineDataGt.setAddress(address);
                    ltOrderLineDataGt.setLatitude(latitude);
                    ltOrderLineDataGt.setLongitude(longitude);
                    ltOrderLineDataGt.setRemark(remark);
                    ltOrderLineDataGt.setDeliveryDate(deliveryDate);
                    ltOrderLineDataGt.setUserid(userid);
                    ltOrderLineDataGt.setInstockFlag(instockFlag);
                    ltOrderLineDataGt.setBeatId(beatId);
                    ltOrderLineDataGt.setOutletId(outletId);
                    ltOrderLineDataGt.setOutletName(outletName);
                    ltOrderLineDataGt.setOutletCode(outletCode);
                    ltOrderLineDataGt.setProprietorName(proprietorName);
                    ltOrderLineDataGt.setListPrice(listPrice);
                    ltOrderLineDataGt.setPriceList(priceList); 
                    // ltOrderLineDataGt.setPtrPrice(rs.getString("PTRPRICE"));
                    // ltOrderLineDataGt.setLinePtrPrice(rs.getString("LINEPTRPRICE"));
                    ltOrderLineDataGt.setLineId(lineId);
                    ltOrderLineDataGt.setProductId1(productId1);
                    ltOrderLineDataGt.setQuantity(quantity);
                    ltOrderLineDataGt.setDeliveryDate1(deliveryDate1);
                    ltOrderLineDataGt.setStatusO(statusO);

                    
                        resultList.add(ltOrderLineDataGt);
                       
                    }
               // }

               // Map<Long, SoHeaderDto> soHeaderDtoMap = new ConcurrentHashMap<>();
//                int count =0;
//                System.out.println("before = " + LocalDateTime.now());//("before while" + LocalDateTime.now());
//                rs.setFetchSize(10);
/*               while(rs.next()) {
//                for (boolean hasNext = rs.next(); hasNext; hasNext = rs.next()) {         
                	//int id = rs.getInt("id");         
                	//String name = rs.getString("name");         // Process the result set row }
                	System.out.println("executing procedure = ");    //("in while loop");
                	//count = count+1;
                	System.out.println("procedure executed =" +LocalDateTime.now() ); //("before mapResultSetToSoHeaderDto = " + LocalDateTime.now());
                	SoHeaderDto headerDto =  mapResultSetToSoHeaderDto(rs);
                	System.out.println("after mapResultSetToSoHeaderDto = " + LocalDateTime.now());
                	
                	System.out.println("before mapResultSetToSoLineDto = " + LocalDateTime.now());
                	SoLineDto lineDto = mapResultSetToSoLineDto(rs);
                	System.out.println("after mapResultSetToSoLineDto = " + LocalDateTime.now());
                	
                	System.out.println("before add(lineDto) = " + LocalDateTime.now());
                	headerDto.getSoLineDtoList().add(lineDto);
                	System.out.println("after add(lineDto) = " + LocalDateTime.now());

                    // Use computeIfAbsent to ensure each header is added only once
                	System.out.println("before computeIfAbsent = " + LocalDateTime.now());
                    soHeaderDtoMap.computeIfAbsent(headerDto.getHeaderId(), k -> headerDto);
                    System.out.println("after computeIfAbsent = " + LocalDateTime.now());
                    count++;
                }
 */             
                //rs.close();
                //callableStatement.close();
               // System.out.println("map soze = "+soHeaderDtoMap.size());
               // System.out.println("count is "+count );
 //on 4july               System.out.println("after while" + LocalDateTime.now());
                // Add any remaining records
//                if (!batchList.isEmpty()) {
//                    resultList.addAll(batchList);
//                }

	//on 4july               System.out.println("ResultList size: " + resultList.size());
	//on 4july             System.out.println("resultList = "+resultList);

//                Map<Long, SoHeaderDto> soHeaderDtoMap = new ConcurrentHashMap<>();
//                Map<Long, List<SoLineDto>> soLineDtoMap = new ConcurrentHashMap<>();

//                resultList.parallelStream().forEach(responseDto -> {
//                    SoLineDto soLineDto = buildSoLineDto1(responseDto);
//
//                    // Synchronize block for concurrent modification
//                    synchronized (soLineDtoMap) {
//                        if (soLineDto.getLineId() != null) {
//                            soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>()).add(soLineDto);
//                        }
//                    }
//
//                    // Synchronize block for concurrent modification
//                    synchronized (soHeaderDtoMap) {
//                        soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto1(responseDto));
//                    }
//                });

//                for (LtOrderLineDataGt responseDto : rs) {
//    	            SoLineDto soLineDto = buildSoLineDto1(responseDto);
//
//    	            // Add soLineDto to the soLineDtoMap only if lineId is not null
//    	            if (soLineDto.getLineId() != null) {
//    	                soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>()).add(soLineDto);
//    	            }
//
//    	            // Add or update soHeaderDto in the soHeaderDtoMap
//    	            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto1(responseDto));
//    	        }
                
                // Post-processing the soHeaderDtoMap
//                List<SoHeaderDto> soHeaderDtoList = soHeaderDtoMap.entrySet()
//                    .stream()
//                    .map(entry -> {
//                        SoHeaderDto soHeaderDto = entry.getValue();
//                        soHeaderDto.setSoLineDtoList(soLineDtoMap.get(entry.getKey()));
//                        return soHeaderDto;
//                    })
//                    .collect(Collectors.toList());

	//on 4july           System.out.println("Completed processing resultList.");
	//on 4july           System.out.println("after processing headers" + LocalDateTime.now());

    	        //OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
   	        //orderDetailsDto.setSoHeaderDto(soHeaderDtoList);
   	     //System.out.println("order Details Dto = "+orderDetailsDto);
   	  //System.out.println("soHeaderDtoMap Dto = "+soHeaderDtoMap);
        //        status.setData(soHeaderDtoMap);
                long methodOut = System.currentTimeMillis();
                
              //on 4july			timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
              //on 4july              System.out.println("Exit from in else getOrderV2 at =" + LocalDateTime.now());
              //on 4july            status.setTimeDifference(timeDifference);
                 
                
                
                
                
               // List<LtOrderLineDataGt> resultList = ltSoHeadersDao.getOrderV2DataFromProcedure();
                //if(resultList.size()>0) {
                	 //status.setData(resultList);
                     //return status;
                //}
               //System.out.println("Result is = "+result);
                //System.out.println("resultList is = "+resultList);
       //on 4july             Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<>();
       //on 4july	        Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<>();
                
       //on 4july	        for (LtOrderLineDataGt responseDto : resultList) {
       //on 4july     SoLineDto soLineDto = buildSoLineDto1(responseDto);

    	            // Add soLineDto to the soLineDtoMap only if lineId is not null
              //on 4july   	            if (soLineDto.getLineId() != null) {
              //on 4july          soLineDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> new ArrayList<>()).add(soLineDto);
              //on 4july      }

    	            // Add or update soHeaderDto in the soHeaderDtoMap
              //on 4july  	            soHeaderDtoMap.computeIfAbsent(responseDto.getHeaderId(), k -> buildSoHeaderDto1(responseDto));
              //on 4july        }
              //on 4july       List<SoHeaderDto> soHeaderDtoList = new ArrayList<>(soHeaderDtoMap.size());

              //on 4july       soHeaderDtoMap.forEach((headerId, soHeaderDto) -> {
              //on 4july         soHeaderDto.setSoLineDtoList(soLineDtoMap.get(headerId));
    	          //on 4july          soHeaderDtoList.add(soHeaderDto);
              //on 4july     });

              //on 4july        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
              //on 4july       orderDetailsDto.setSoHeaderDto(soHeaderDtoList);
 /*               List<LtOrderLineDataGt> resultList = new ArrayList<>();
                if (result!=true) {
                    try (ResultSet rs = callableStatement.getResultSet()) {
                        while (rs.next()) {
                            LtOrderLineDataGt ltOrderLineDataGt = new LtOrderLineDataGt();
                            ltOrderLineDataGt.setInventoryQuantity(rs.getLong("inventoryQuantity"));
                            System.out.println("List id ="+rs.getLong("inventoryQuantity"));
                            ltOrderLineDataGt.setCdate(rs.getDate("Cdate"));
                            ltOrderLineDataGt.setOutletAddress(rs.getString("outletAddress"));
                            ltOrderLineDataGt.setHeaderId(rs.getLong("headerId"));
                            ltOrderLineDataGt.setOrderNumber(rs.getString("orderNumber"));
                            ltOrderLineDataGt.setProductId(rs.getString("productId"));
                            ltOrderLineDataGt.setCategoryId(rs.getString("categoryId"));
                            ltOrderLineDataGt.setOrgId(rs.getString("orgId"));
                            ltOrderLineDataGt.setSubCategoryId(rs.getString("subCategoryId"));
                            ltOrderLineDataGt.setProductType(rs.getString("productType"));
                            ltOrderLineDataGt.setCategory(rs.getString("category"));
                            ltOrderLineDataGt.setSubCategory(rs.getString("subCategory"));
                            ltOrderLineDataGt.setProductCode(rs.getString("productCode"));
                            ltOrderLineDataGt.setProductName(rs.getString("productName"));
                            ltOrderLineDataGt.setProductDesc(rs.getString("productDesc"));
                            ltOrderLineDataGt.setPrimaryUom(rs.getString("primaryUom"));
                            ltOrderLineDataGt.setSecondaryUom(rs.getString("secondaryUom"));
                            ltOrderLineDataGt.setSecondaryUomValue(rs.getLong("secondaryUomValue"));
                            ltOrderLineDataGt.setUnitPerCase(rs.getLong("unitPerCase"));
                            ltOrderLineDataGt.setSegment(rs.getString("segment"));
                            ltOrderLineDataGt.setStyle(rs.getString("style"));
                            ltOrderLineDataGt.setFlavor(rs.getString("flavor"));
                            ltOrderLineDataGt.setCasePack(rs.getLong("casePack"));
                            ltOrderLineDataGt.setHsnCode(rs.getString("hsnCode"));
                            ltOrderLineDataGt.setOrderable(rs.getString("orderable"));
                            ltOrderLineDataGt.setStatus1(rs.getString("status1"));
                            ltOrderLineDataGt.setOrderDate(rs.getDate("orderDate"));
                            ltOrderLineDataGt.setStatus(rs.getString("status"));
                            ltOrderLineDataGt.setAddress(rs.getString("address"));
                            ltOrderLineDataGt.setLatitude(rs.getString("latitude"));
                            ltOrderLineDataGt.setLongitude(rs.getString("longitude"));
                            ltOrderLineDataGt.setRemark(rs.getString("remark"));
                            ltOrderLineDataGt.setDeliveryDate(rs.getDate("deliveryDate"));
                            ltOrderLineDataGt.setUserid(rs.getLong("userid"));
                            ltOrderLineDataGt.setInstockFlag(rs.getString("instockFlag"));
                            ltOrderLineDataGt.setBeatId(rs.getString("beatId"));
                            ltOrderLineDataGt.setOutletId(rs.getString("outletId"));
                            ltOrderLineDataGt.setOutletName(rs.getString("outletName"));
                            ltOrderLineDataGt.setOutletCode(rs.getString("outletCode"));
                            ltOrderLineDataGt.setProprietorName(rs.getString("proprietorName"));
                            ltOrderLineDataGt.setListPrice(rs.getDouble("listPrice"));
                            ltOrderLineDataGt.setPriceList(rs.getString("priceList"));
                            ltOrderLineDataGt.setPtrprice(rs.getDouble("ptrprice"));
                            ltOrderLineDataGt.setLineptrprice(rs.getString("lineptrprice"));
                            ltOrderLineDataGt.setLineId(rs.getLong("lineId"));
                            ltOrderLineDataGt.setProductId1(rs.getString("productId1"));
                            ltOrderLineDataGt.setQuantity(rs.getLong("quantity"));
                            ltOrderLineDataGt.setDeliveryDate1(rs.getDate("deliveryDate1"));
                            ltOrderLineDataGt.setStatusO(rs.getLong("statusO"));
                            System.out.println("orderNumber ="+rs.getString("orderNumber"));
                            
                            resultList.add(ltOrderLineDataGt);
                            System.out.println("List id ="+resultList);
                        }
                    }
                }
*/
              //on 4july         status.setData(orderDetailsDto);
              //on 4july          return status;
              //on 4july   } catch (Exception e) {
                //throw new RuntimeException("Error executing stored procedure", e);
              //on 4july      e.printStackTrace();
             //on 4july    } 
             //on 4july   return status;
             //on 4july     });
             //on 4july }
              //on 4july*/// end of comment on 4-july-2024//	
	
	private SoHeaderDto mapResultSetToSoHeaderDto(ResultSet rs) throws SQLException{
		//System.out.println("in mapResultSetToSoHeaderDto" + LocalDateTime.now());
	    SoHeaderDto headerDto = new SoHeaderDto();
	    headerDto.setHeaderId(rs.getLong("HEADER_ID"));
	    headerDto.setOrderNumber(rs.getString("order_number"));
	    //headerDto.setOrderDate(rs.getDate("order_date"));
	    headerDto.setStatus(rs.getString("status1"));
	    headerDto.setAddress(rs.getString("address"));
	    headerDto.setOutletId(rs.getString("outlet_id"));
	    headerDto.setOutletName(rs.getString("outlet_name"));
	    headerDto.setOutletCode(rs.getString("outlet_code"));
	    headerDto.setOutletAddress(rs.getString("outletAddress"));
	    headerDto.setCustomerId(rs.getLong("USERID"));
	    headerDto.setProprietorName(rs.getString("proprietor_name"));
	    headerDto.setDeliveryDate(rs.getDate("delivery_date"));
	    headerDto.setLatitude(rs.getString("latitude"));
	    headerDto.setLongitude(rs.getString("longitude"));
	    headerDto.setUserId(rs.getLong("USERID"));
	    headerDto.setRemark(rs.getString("remark"));
	   // headerDto.setAddress1(rs.getString("address1"));
	    //headerDto.setCity(rs.getString("city"));
//	    headerDto.setDistributorName(rs.getString("distributor_name"));
//	    headerDto.setDistributorCode(rs.getString("distributor_code"));
//	    headerDto.setDistributorId(rs.getString("distributor_id"));
//	    headerDto.setInvoiceNumber(rs.getString("invoice_number"));
//	    headerDto.setInvoiceDate(rs.getDate("invoice_date"));
//	    headerDto.setTotalAmount(rs.getLong("total_amount"));
//	    headerDto.setPriceListId(rs.getString("price_list_id"));
//	    headerDto.setPriceListName(rs.getString("price_list_name"));
//	    headerDto.setBeatName(rs.getString("beat_name"));
//	    headerDto.setPriceList(rs.getString("price_list"));
//	    headerDto.setBeatId(rs.getString("beat_id"));
//	    headerDto.setInstockFlag(rs.getString("instock_flag"));
//	    headerDto.setHeaderPriceList(rs.getString("header_price_list"));
//	    headerDto.setCreatedBy(rs.getInt("created_by"));
//	    headerDto.setSiebelStatus(rs.getString("siebel_status"));
//	    headerDto.setSiebelRemark(rs.getString("siebel_remark"));
//	    headerDto.setSiebelInvoicenumber(rs.getString("siebel_invoicenumber"));
//	    headerDto.setSiebelJsonpayload(rs.getString("siebel_jsonpayload"));
//	    headerDto.setOrderDate1(rs.getDate("order_date1"));
//	    headerDto.setInventoryId(rs.getString("inventory_id"));
//	    headerDto.setLocation(rs.getString("location"));
//	    headerDto.setMobileNumber(rs.getString("mobile_number"));

	    // Initialize empty list for soLineDtoList
	    headerDto.setSoLineDtoList(new ArrayList<>());
	    return headerDto;
	}

	private SoLineDto mapResultSetToSoLineDto(ResultSet rs) throws SQLException {
		System.out.println("in mapResultSetToSoLineDto");
	    SoLineDto lineDto = new SoLineDto();
	    lineDto.setLineId(rs.getLong("line_id"));
	    lineDto.setProductId(rs.getString("product_id"));
	    lineDto.setQuantity(rs.getLong("quantity"));
	    lineDto.setProductCode(rs.getString("product_code"));
	    lineDto.setProductDesc(rs.getString("product_desc"));
	    lineDto.setProductName(rs.getString("product_name"));
	    lineDto.setListPrice(rs.getString("list_price"));
	    lineDto.setPriceList(rs.getString("price_list"));
	    lineDto.setDeliveryDate(rs.getDate("delivery_date"));
	    lineDto.setStatus(rs.getString("status"));
//	    lineDto.setLinelistPrice(rs.getString("linelist_price"));
//	    lineDto.setLinePtrPrice(rs.getString("line_ptr_price"));
//	    lineDto.setInventoryQuantity(rs.getString("inventory_quantity"));
//	    lineDto.setOrgId(rs.getString("org_id"));
//	    lineDto.setCategoryId(rs.getString("category_id"));
//	    lineDto.setProductType(rs.getString("product_type"));
//	    lineDto.setCategory(rs.getString("category"));
//	    lineDto.setSubCategory(rs.getString("sub_category"));
//	    lineDto.setPrimaryUom(rs.getString("primary_uom"));
//	    lineDto.setSecondaryUom(rs.getString("secondary_uom"));
//	    lineDto.setSecondaryUomValue(rs.getString("secondary_uom_value"));
//	    lineDto.setUnitsPerCase(rs.getString("units_per_case"));
//	    lineDto.setSegment(rs.getString("segment"));
//	    lineDto.setBrand(rs.getString("brand"));
//	    lineDto.setSubBrand(rs.getString("sub_brand"));
//	    lineDto.setCasePack(rs.getString("case_pack"));
//	    lineDto.setHsnCode(rs.getString("hsn_code"));
//	    lineDto.setProductImage(rs.getString("product_image"));
//	    lineDto.setThumbnailImage(rs.getString("thumbnail_image"));
//	    lineDto.setEimStatus(rs.getString("eim_status"));
//	    lineDto.setPriceListId(rs.getString("price_list_id"));
//	    lineDto.setHeaderPriceList(rs.getString("header_price_list"));
//	    lineDto.setHeaderId(rs.getLong("HEADER_ID"));
//	    lineDto.setPtrBasePrice(rs.getLong("ptr_base_price"));
//	    lineDto.setLocation(rs.getString("location"));
//	    lineDto.setInventoryId(rs.getString("inventory_id"));
//	    lineDto.setShippedQuantity(rs.getString("shipped_quantity"));
//	    lineDto.setReturnQuantity(rs.getLong("return_quantity"));

	    return lineDto;
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
	
		private SoLineDto buildSoLineDto(ResponseDto responseDto) {
		    SoLineDto soLineDto = new SoLineDto();
		    soLineDto.setLineId(responseDto.getLineId());
		    soLineDto.setProductId(responseDto.getProductId());
		    soLineDto.setQuantity(responseDto.getQuantity());
		    soLineDto.setProductCode(responseDto.getProductCode());
		    soLineDto.setProductDesc(responseDto.getProductDesc());
		    soLineDto.setProductName(responseDto.getProductName());
		    soLineDto.setPriceList(responseDto.getPriceList());
		    soLineDto.setListPrice(determineListPrice(responseDto));
		    soLineDto.setPtrPrice(determinePtrPrice(responseDto));
		    soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
		    soLineDto.setDeliveryDate(responseDto.getDeliveryDate1());
		    soLineDto.setOrgId(responseDto.getOrgId());
		    soLineDto.setProductType(responseDto.getProductType());
		    soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
		    soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
		    soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
		    soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
		    soLineDto.setProductImage(responseDto.getProductImage());
		    soLineDto.setBrand(responseDto.getBrand());
		    soLineDto.setSubBrand(responseDto.getSubBrand());
		    soLineDto.setSegment(responseDto.getSegment());
		    soLineDto.setCasePack(responseDto.getCasePack());
		    soLineDto.setHsnCode(responseDto.getHsnCode());
		    soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
		    return soLineDto;
		}
		
		private SoLineDto buildSoLineDto1(LtOrderLineDataGt responseDto) {
		    SoLineDto soLineDto = new SoLineDto();
		    soLineDto.setLineId(responseDto.getLineId());
		    soLineDto.setProductId(responseDto.getProductId());
		    soLineDto.setQuantity(responseDto.getQuantity());
		    soLineDto.setProductCode(responseDto.getProductCode());
		    soLineDto.setProductDesc(responseDto.getProductDesc());
		    soLineDto.setProductName(responseDto.getProductName());
		    soLineDto.setPriceList(responseDto.getPriceList());
		    //soLineDto.setListPrice(determineListPrice(responseDto));
		    //soLineDto.setPtrPrice(determinePtrPrice(responseDto));
		    //soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
		    soLineDto.setDeliveryDate(responseDto.getDeliveryDate1());
		    soLineDto.setOrgId(responseDto.getOrgId());
		    soLineDto.setProductType(responseDto.getProductType());
		    soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
		    soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
		    //soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
		    //soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
		    soLineDto.setProductImage(responseDto.getProductImage());
		    soLineDto.setBrand(responseDto.getBrand());
		    //soLineDto.setSubBrand(responseDto.getSubBrand());
		    soLineDto.setSegment(responseDto.getSegment());
		    //soLineDto.setCasePack(responseDto.getCasePack());
		    soLineDto.setHsnCode(responseDto.getHsnCode());
		    //soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
		    return soLineDto;
		}
		
		private SoHeaderDto buildSoHeaderDto(ResponseDto responseDto) {
		    SoHeaderDto soHeaderDto = new SoHeaderDto();
		    soHeaderDto.setHeaderId(responseDto.getHeaderId());
		    soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
		    soHeaderDto.setOrderDate(formatOrderDate(responseDto.getOrderDate()));
		    soHeaderDto.setStatus(responseDto.getStatus());
		    soHeaderDto.setAddress(responseDto.getAddress());
		    soHeaderDto.setOutletName(responseDto.getOutletName());
		    soHeaderDto.setOutletId(responseDto.getOutletId());
		    soHeaderDto.setOutletCode(responseDto.getOutletCode());
		    soHeaderDto.setLatitude(responseDto.getLatitude());
		    soHeaderDto.setLongitude(responseDto.getLongitude());
		    soHeaderDto.setInstockFlag(responseDto.getInstockFlag());
		    soHeaderDto.setPriceList(responseDto.getPriceList());
		    soHeaderDto.setSiebelRemark(responseDto.getSiebelRemark());
		    soHeaderDto.setBeatId(responseDto.getBeatId());
		    soHeaderDto.setProprietorName(responseDto.getProprietorName());
		    soHeaderDto.setRemark(responseDto.getRemark());
		    soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
		    soHeaderDto.setUserId(responseDto.getUserId());
		    soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
		    soHeaderDto.setCustomerId(responseDto.getCustomerId());
		    soHeaderDto.setCity(responseDto.getCity());
		    return soHeaderDto;
		}
		
		private SoHeaderDto buildSoHeaderDto1(LtOrderLineDataGt responseDto) {
		    SoHeaderDto soHeaderDto = new SoHeaderDto();
		    soHeaderDto.setHeaderId(responseDto.getHeaderId());
		    soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
		    soHeaderDto.setOrderDate(formatOrderDate(responseDto.getOrderDate()));
		    soHeaderDto.setStatus(responseDto.getStatus());
		    soHeaderDto.setAddress(responseDto.getAddress());
		    soHeaderDto.setOutletName(responseDto.getOutletName());
		    soHeaderDto.setOutletId(responseDto.getOutletId());
		    soHeaderDto.setOutletCode(responseDto.getOutletCode());
		    soHeaderDto.setLatitude(responseDto.getLatitude());
		    soHeaderDto.setLongitude(responseDto.getLongitude());
		    soHeaderDto.setInstockFlag(responseDto.getInstockFlag());
		    soHeaderDto.setPriceList(responseDto.getPriceList());
		    //soHeaderDto.setSiebelRemark(responseDto.getSiebelRemark());
		    soHeaderDto.setBeatId(responseDto.getBeatId());
		    soHeaderDto.setProprietorName(responseDto.getProprietorName());
		    soHeaderDto.setRemark(responseDto.getRemark());
		    soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
		    soHeaderDto.setUserId(responseDto.getUserid());
		    soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
		   // soHeaderDto.setCustomerId(responseDto.getCustomerId());
		   // soHeaderDto.setCity(responseDto.getCity());
		    return soHeaderDto;
		}
		
		
		private String formatOrderDate(Date orderDate) {
		    final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		    final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
		    final TimeZone utc = TimeZone.getTimeZone("UTC");
		    sdf.setTimeZone(utc);
		    return sdf.format(orderDate);
		}
		
		
		private String determineListPrice(ResponseDto responseDto) {
		    if (responseDto.getStatus().equalsIgnoreCase(DRAFT) ||
		        responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
		        responseDto.getStatus().equals("APPROVED")) {
		        return responseDto.getListPrice();
		    } else {
		        return responseDto.getLinelistPrice();
		    }
		}
	 
		private String determinePtrPrice(ResponseDto responseDto) {
		    if (responseDto.getPtrPrice() != null) {
		        if ("Y".equalsIgnoreCase(responseDto.getPtrFlag())) {
		            return determineListPrice(responseDto);
		        } else {
		            if (responseDto.getStatus().equalsIgnoreCase(DRAFT) ||
		                responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
		                responseDto.getStatus().equals("APPROVED")) {
		                return responseDto.getPtrPrice();
		            } else {
		                return responseDto.getLinePtrPrice();
		            }
		        }
		    }
		    return null;
		}//new dev
	
	
	@Override
	public Status locationSaveOnNoOrder(LtSalesPersonLocation ltSalesPersonLocation) throws ServiceException, IOException{
		Status status = new Status();
		ltSalesPersonLocation.setCreatedBy(ltSalesPersonLocation.getUserId());
		ltSalesPersonLocation.setCreationDate(new Date());
		ltSalesPersonLocation.setLastUpdateDate(new Date());
		ltSalesPersonLocation.setLastUpdatedBy(ltSalesPersonLocation.getUserId());
		
		LtSalesPersonLocation list = ltSoHeadersDao.locationSaveOnNoOrder(ltSalesPersonLocation);
		if (list != null) {
			status.setCode(SUCCESS);
			status.setMessage("Location Saved Successfully.");
			status.setData(list);
		} else {
			status.setCode(FAIL);
			status.setMessage("Fail To Save.");
		}
		return status;
	}

	
	@Override
	public Status getOrderForPendingApprovals(RequestDto requestDto) throws ServiceException, IOException {
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetUserTypeAgainsUserId = 0;
		long outQuerygetUserTypeAgainsUserId = 0;
		long inQuerygetSoHeader = 0;
		long outQuerygetSoHeader = 0;
		long inQuerygetRecordCount = 0;
		long outQuerygetRecordCount = 0;
		long inQuerygetOrderV2 = 0;
		long outQuerygetOrderV2 = 0;
		//long inQuerygetInStockProductWithInventory = 0;
		//long outQuerygetInStockProductWithInventory = 0;
		//long inQuerygetInStockProductCountWithInventory = 0;
		//long outQuerygetInStockProductCountWithInventory = 0;
		
		try {
			Status status = new Status();
			inQuerygetUserTypeAgainsUserId = System.currentTimeMillis();
			String userType = ltSoHeadersDao.getUserTypeAgainsUserId(requestDto.getUserId());
			outQuerygetUserTypeAgainsUserId = System.currentTimeMillis();

			if(userType.equalsIgnoreCase(DISTRIBUTOR) || userType.equalsIgnoreCase("SALESOFFICER")) {
				inQuerygetSoHeader = System.currentTimeMillis();
			List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
				outQuerygetSoHeader = System.currentTimeMillis();
			System.out.print("headerIdsList is ====" +headerIdsList);
			
			inQuerygetRecordCount = System.currentTimeMillis();
			Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
			//Long recordCount = (long) headerIdsList.size() + 1;
			outQuerygetRecordCount = System.currentTimeMillis();
			
			System.out.println("headerIdsList====>"+headerIdsList.size());
			//System.out.println("recordCount====>"+recordCount);
			
			status.setTotalCount(recordCount);
			status.setRecordCount(recordCount);
			if(headerIdsList.isEmpty()) {
				status.setCode(RECORD_NOT_FOUND);
				status.setData("Record not found");
				return status;
			}
			
			List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
			
			inQuerygetOrderV2 = System.currentTimeMillis();
			responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsList);
			inQuerygetOrderV2 = System.currentTimeMillis();
			
			Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<Long, SoHeaderDto>();
			Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<Long, List<SoLineDto>>();

			for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
				ResponseDto responseDto = (ResponseDto) iterator.next();
                
				System.out.println("responseDto is ===" +responseDto);
				
				SoLineDto soLineDto = new SoLineDto();

				if (responseDto.getLineId() != null) {
					soLineDto.setLineId(responseDto.getLineId());
				}
				if (responseDto.getProductId() != null) {
					soLineDto.setProductId(responseDto.getProductId());
				}
				if (responseDto.getQuantity() != null) {
					soLineDto.setQuantity(responseDto.getQuantity());
				}
				if (responseDto.getProductCode() != null) {
					soLineDto.setProductCode(responseDto.getProductCode());
				}
				if (responseDto.getProductDesc() != null) {
					soLineDto.setProductDesc(responseDto.getProductDesc());
				}
				if (responseDto.getProductName() != null) {
					soLineDto.setProductName(responseDto.getProductName());
				}
				if (responseDto.getPriceList() != null) {
					soLineDto.setPriceList(responseDto.getPriceList());
					
				}
			
				if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) 
					|| responseDto.getStatus().equals("APPROVED")) {
					if (responseDto.getListPrice() != null) {
						soLineDto.setListPrice(responseDto.getListPrice());
					}
				}else {
					if (responseDto.getLinelistPrice() != null) {
						soLineDto.setListPrice(responseDto.getLinelistPrice());
					}
				}
								
				if (responseDto.getPtrPrice() != null) {
					if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						soLineDto.setPtrPrice(responseDto.getListPrice());
					}else {
						soLineDto.setPtrPrice(responseDto.getPtrPrice());
					}
				}
			
				if (responseDto.getPtrPrice() != null) {
					//System.out.println("IF responseDto.getPtrPrice() != null "+responseDto.getPtrPrice());
					if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//soLineDto.setPtrPrice(responseDto.getListPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
								responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
							if (responseDto.getListPrice() != null) {
								soLineDto.setListPrice(responseDto.getListPrice());
							}
						}else {
							if (responseDto.getLinelistPrice() != null) {
								soLineDto.setListPrice(responseDto.getLinelistPrice());
							}
						}
					}else {
						//soLineDto.setPtrPrice(responseDto.getPtrPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
								responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
							if (responseDto.getPtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getPtrPrice());
							}
						}else {
							if (responseDto.getLinePtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getLinePtrPrice());
							}
						}
					}
				}
				
				if (responseDto.getInventoryQuantity() != null) {
					soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
				}
				if (responseDto.getDeliveryDate() != null) {
					soLineDto.setDeliveryDate(responseDto.getDeliveryDate());
				}
				if (responseDto.getOrgId() != null) {
					soLineDto.setOrgId(responseDto.getOrgId());
				}
				if (responseDto.getProductType() != null) {
					soLineDto.setProductType(responseDto.getProductType());
				}
				if (responseDto.getPrimaryUom() != null) {
					soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
				}
				if (responseDto.getSecondaryUom() != null) {
					soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
				}
				if (responseDto.getSecondaryUomValue() != null) {
					soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
				}
				if (responseDto.getUnitsPerCase() != null) {
					soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
				}
				if (responseDto.getProductImage() != null) {
					soLineDto.setProductImage(responseDto.getProductImage());
				}
				if (responseDto.getBrand() != null) {
					soLineDto.setBrand(responseDto.getBrand());
				}
				if (responseDto.getSubBrand() != null) {
					soLineDto.setSubBrand(responseDto.getSubBrand());
				}
				if (responseDto.getSegment() != null) {
					soLineDto.setSegment(responseDto.getSegment());
				}
				if (responseDto.getCasePack() != null) {
					soLineDto.setCasePack(responseDto.getCasePack());
				}
				if (responseDto.getHsnCode() != null) {
					soLineDto.setHsnCode(responseDto.getHsnCode());
				}
				if (responseDto.getThumbnailImage() != null) {
					soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
				}

				if (soLineDtoMap.get(responseDto.getHeaderId()) != null) {
					// already exost
					List<SoLineDto> soLineDtoList = soLineDtoMap.get(responseDto.getHeaderId());
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				} else {
					// add data into soheader map
					SoHeaderDto soHeaderDto = new SoHeaderDto();
					soHeaderDto.setHeaderId(responseDto.getHeaderId());
					soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
					
					//FOR DEV UTC TIME ZONE
					//soHeaderDto.setOrderDate(responseDto.getOrderDate().toString());
					
					//FOR DEV IST TIME ZONE
					final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
					final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
					final TimeZone utc = TimeZone.getTimeZone("UTC");
					sdf.setTimeZone(utc);
					soHeaderDto.setOrderDate(sdf.format(responseDto.getOrderDate()));
					
					soHeaderDto.setStatus(responseDto.getStatus());
					soHeaderDto.setAddress(responseDto.getAddress());
					soHeaderDto.setOutletName(responseDto.getOutletName());
					soHeaderDto.setOutletId(responseDto.getOutletId());
					soHeaderDto.setOutletCode(responseDto.getOutletCode());
					soHeaderDto.setLatitude(responseDto.getLatitude());
					soHeaderDto.setLongitude(responseDto.getLongitude());
					
					soHeaderDto.setInstockFlag(responseDto.getInstockFlag());
					soHeaderDto.setPriceList(responseDto.getPriceList());
					//soHeaderDto.setBeatId(responseDto.getBeatId());
					
					
					if(responseDto.getBeatId()!= null) {
					soHeaderDto.setBeatId(responseDto.getBeatId());
					}
					if(responseDto.getHeaderPriceList()!= null) {
					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
					}
					
					if (responseDto.getProprietorName() != null) {
						soHeaderDto.setProprietorName(responseDto.getProprietorName());
					}
					if (responseDto.getRemark() != null) {
						soHeaderDto.setRemark(responseDto.getRemark());
					}
					if (responseDto.getDeliveryDate() != null) {
						soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
					}
					if (responseDto.getUserId() != null) {
						soHeaderDto.setUserId(responseDto.getUserId());
					}
					if (responseDto.getOutletAddress() != null) {
						soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
					}
					if (responseDto.getCustomerId() != null) {
						soHeaderDto.setCustomerId(responseDto.getCustomerId());
					}
					if (responseDto.getAddress() != null) {
						soHeaderDto.setAddress(responseDto.getAddress());
					}
					
					if (responseDto.getCity() != null) { soHeaderDto.setCity(responseDto.getCity()); }
					
					if(responseDto.getBeatId()!= null) {
						soHeaderDto.setBeatId(responseDto.getBeatId());
					}
					if(responseDto.getHeaderPriceList()!= null) {
					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
					}
					
					soHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);

					List<SoLineDto> soLineDtoList = new ArrayList<SoLineDto>();
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				}

			}
			OrderDetailsDto orderDetailsDto = new OrderDetailsDto();

			List<SoHeaderDto> soHeaderDtoList = new ArrayList<SoHeaderDto>();

			for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
				SoHeaderDto soHeaderDto = entry.getValue();

				List<SoLineDto> soLineDtoList = soLineDtoMap.get(entry.getKey());
				soHeaderDto.setSoLineDtoList(soLineDtoList);

				soHeaderDtoList.add(soHeaderDto);
			}
			orderDetailsDto.setSoHeaderDto(soHeaderDtoList);

			if (!responseDtoList.isEmpty()) {
				status.setCode(RECORD_FOUND);// = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				status.setData(orderDetailsDto);
				
				timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetUserTypeAgainsUserId,outQuerygetUserTypeAgainsUserId));
				timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetSoHeader,outQuerygetSoHeader));
				timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetRecordCount, outQuerygetRecordCount));
				timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetOrderV2, outQuerygetOrderV2));
//				timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//				timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
				
				long methodOut = System.currentTimeMillis();
				System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
		        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
		        status.setTimeDifference(timeDifference);
				
				return status;
			}
			status.setCode(RECORD_NOT_FOUND); // status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
			return status;
		}} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		} return null;
	}

/* this is original code comment on 30-May-2024 for optimization 	
	@Override
	public Status getAllPendingOrders(RequestDto requestDto) throws ServiceException, IOException {
		try {
			System.out.println("in method getAllPendingOrders service line 4454 ="+ LocalDateTime.now());
			Status status = new Status();
			System.out.println("Above getSoHeader query call at = "+ LocalDateTime.now());
			List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
			System.out.println("Below getSoHeader query call at = "+ LocalDateTime.now());
			//System.out.print("headerIdsList is ====" +headerIdsList);
			//System.out.print("requestDto data is =="+requestDto);
			System.out.println("Above getRecordCount query call at = "+ LocalDateTime.now());
			Long recordCount = ltSoHeadersDao.getRecordCount(requestDto);
			System.out.println("Below getRecordCount query call at = "+ LocalDateTime.now());
			//Long recordCount = (long) headerIdsList.size() + 1;
			
			System.out.println("headerIdsList====>"+headerIdsList.size());
			//System.out.println("recordCount====>"+recordCount);
			
			status.setTotalCount(recordCount);
			status.setRecordCount(recordCount);
			if(headerIdsList.isEmpty()) {
				status.setCode(RECORD_NOT_FOUND);
				status.setData("Record not found");
				return status;
			}
			SoHeaderDto soHeaderDto1 = new SoHeaderDto();
			Long headerId1;
			System.out.println("Above getheaderByHeaderIdNew query call at = "+ LocalDateTime.now());
			List<SoHeaderDto> soHeaderList = ltSoHeadersDao.getheaderByHeaderIdNew(headerIdsList);
			System.out.println("Below getheaderByHeaderIdNew query call at = "+ LocalDateTime.now());
			System.out.println("soHeaderListTTTTTTTT"+ soHeaderList);
			// code for not showing pending_approval to himself
			
			List<Long> headerIdsListUpdated = new ArrayList<Long>();
					if(!headerIdsList.isEmpty()) {
						System.out.println("in if !headerIdsList.isEmpty() at ="+ LocalDateTime.now());
						for(Long headerId:headerIdsList) {
//      comment on 22-May-24 for optimization							
							SoHeaderDto list = ltSoHeadersDao.getheaderByHeaderId(headerId);
//							
							System.out.println("list headerId=="+headerId);
			Optional<SoHeaderDto> list= soHeaderList.stream().filter(x->x.getHeaderId().equals(headerId)).findFirst();
	
							//System.out.println("listlistlist1=="+list.get().getHeaderId());
							//System.out.println("list.getCreatedBy() is="+list.getCreatedBy());
							//System.out.println("requestDto.getUserId() is ="+requestDto.getLoginId());
							//System.out.println("list.getStatus() is="+list.getStatus());
			                //soHeaderDto1= list.get();
			
			                     if(list.isPresent()) {
			                    	 SoHeaderDto SoHeaderDto = list.get();
							if (SoHeaderDto.getCreatedBy()!= requestDto.getLoginId() ) {//&& !list.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
										headerIdsListUpdated.add(headerId);
										System.out.println("header Ids List Updated is="+headerIdsListUpdated);
									}}
			                     System.out.println("header Ids List Updated is22="+headerIdsListUpdated);
						}System.out.println("in method getAllPendingOrders service line 4490 ="+ new Date());
					}
			
			
			List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
			
		//	responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsList);
			System.out.println("Above getOrderV2 query call at = "+ LocalDateTime.now());
			responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsListUpdated);
			System.out.println("Below getOrderV2 query call at = "+ LocalDateTime.now());
			
			Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<Long, SoHeaderDto>();
			Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<Long, List<SoLineDto>>();

//			System.out.println("responseDto befoe filter is ===" +responseDtoList1);
//			System.out.println("UserId is ===="+requestDto.getUserId());
//			List<ResponseDto> responseDtoList = responseDtoList1.stream().filter(X->X.getUserId()!=requestDto.getUserId()).collect(Collectors.toList());
//			System.out.println("responseDto after filter is ===" +responseDtoList);
			System.out.println("in method getAllPendingOrders service line 4507 ="+ LocalDateTime.now());
			for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
				ResponseDto responseDto = (ResponseDto) iterator.next();
                						
				System.out.println("responseDto is ===" +responseDto);
				
				SoLineDto soLineDto = new SoLineDto();

				if (responseDto.getLineId() != null) {
					soLineDto.setLineId(responseDto.getLineId());
				}
				if (responseDto.getProductId() != null) {
					soLineDto.setProductId(responseDto.getProductId());
				}
				if (responseDto.getQuantity() != null) {
					soLineDto.setQuantity(responseDto.getQuantity());
				}
				if (responseDto.getProductCode() != null) {
					soLineDto.setProductCode(responseDto.getProductCode());
				}
				if (responseDto.getProductDesc() != null) {
					soLineDto.setProductDesc(responseDto.getProductDesc());
				}
				if (responseDto.getProductName() != null) {
					soLineDto.setProductName(responseDto.getProductName());
				}
				if (responseDto.getPriceList() != null) {
					soLineDto.setPriceList(responseDto.getPriceList());
					
				}
			
				if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) 
					|| responseDto.getStatus().equals("APPROVED")) {
					if (responseDto.getListPrice() != null) {
						soLineDto.setListPrice(responseDto.getListPrice());
					}
				}else {
					if (responseDto.getLinelistPrice() != null) {
						soLineDto.setListPrice(responseDto.getLinelistPrice());
					}
				}
								
				if (responseDto.getPtrPrice() != null) {
					if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						soLineDto.setPtrPrice(responseDto.getListPrice());
					}else {
						soLineDto.setPtrPrice(responseDto.getPtrPrice());
					}
				}
			
				if (responseDto.getPtrPrice() != null) {
					//System.out.println("IF responseDto.getPtrPrice() != null "+responseDto.getPtrPrice());
					if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
						//soLineDto.setPtrPrice(responseDto.getListPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
								responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
							if (responseDto.getListPrice() != null) {
								soLineDto.setListPrice(responseDto.getListPrice());
							}
						}else {
							if (responseDto.getLinelistPrice() != null) {
								soLineDto.setListPrice(responseDto.getLinelistPrice());
							}
						}
					}else {
						//soLineDto.setPtrPrice(responseDto.getPtrPrice());
						if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
								responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
							if (responseDto.getPtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getPtrPrice());
							}
						}else {
							if (responseDto.getLinePtrPrice() != null) {
								soLineDto.setPtrPrice(responseDto.getLinePtrPrice());
							}
						}
					}
				}
				
				if (responseDto.getInventoryQuantity() != null) {
					soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
				}
				if (responseDto.getDeliveryDate() != null) {
					soLineDto.setDeliveryDate(responseDto.getDeliveryDate());
				}
				if (responseDto.getOrgId() != null) {
					soLineDto.setOrgId(responseDto.getOrgId());
				}
				if (responseDto.getProductType() != null) {
					soLineDto.setProductType(responseDto.getProductType());
				}
				if (responseDto.getPrimaryUom() != null) {
					soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
				}
				if (responseDto.getSecondaryUom() != null) {
					soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
				}
				if (responseDto.getSecondaryUomValue() != null) {
					soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
				}
				if (responseDto.getUnitsPerCase() != null) {
					soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
				}
				if (responseDto.getProductImage() != null) {
					soLineDto.setProductImage(responseDto.getProductImage());
				}
				if (responseDto.getBrand() != null) {
					soLineDto.setBrand(responseDto.getBrand());
				}
				if (responseDto.getSubBrand() != null) {
					soLineDto.setSubBrand(responseDto.getSubBrand());
				}
				if (responseDto.getSegment() != null) {
					soLineDto.setSegment(responseDto.getSegment());
				}
				if (responseDto.getCasePack() != null) {
					soLineDto.setCasePack(responseDto.getCasePack());
				}
				if (responseDto.getHsnCode() != null) {
					soLineDto.setHsnCode(responseDto.getHsnCode());
				}
				if (responseDto.getThumbnailImage() != null) {
					soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
				}

				if (soLineDtoMap.get(responseDto.getHeaderId()) != null) {
					// already exost
					List<SoLineDto> soLineDtoList = soLineDtoMap.get(responseDto.getHeaderId());
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				} else {
					// add data into soheader map
					SoHeaderDto soHeaderDto = new SoHeaderDto();
					soHeaderDto.setHeaderId(responseDto.getHeaderId());
					soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
					
					//FOR DEV UTC TIME ZONE
					//soHeaderDto.setOrderDate(responseDto.getOrderDate().toString());
					
					//FOR DEV IST TIME ZONE
					final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
					final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
					final TimeZone utc = TimeZone.getTimeZone("UTC");
					sdf.setTimeZone(utc);
					soHeaderDto.setOrderDate(sdf.format(responseDto.getOrderDate()));
					
					soHeaderDto.setStatus(responseDto.getStatus());
					soHeaderDto.setAddress(responseDto.getAddress());
					soHeaderDto.setOutletName(responseDto.getOutletName());
					soHeaderDto.setOutletId(responseDto.getOutletId());
					soHeaderDto.setOutletCode(responseDto.getOutletCode());
					soHeaderDto.setLatitude(responseDto.getLatitude());
					soHeaderDto.setLongitude(responseDto.getLongitude());
					
					soHeaderDto.setInstockFlag(responseDto.getInstockFlag());
					soHeaderDto.setPriceList(responseDto.getPriceList());
					//soHeaderDto.setBeatId(responseDto.getBeatId());
					
					System.out.println("Hi Siebel= SiebelStatus"+responseDto.getSiebelStatus());
					//System.out.println(responseDto.getSiebelRemark());
					System.out.println("SiebelInvoicenumber"+responseDto.getSiebelInvoicenumber());
					
					soHeaderDto.setSiebelStatus(responseDto.getSiebelStatus());
					//soHeaderDto.setSiebelRemark(responseDto.getSiebelRemark());
					soHeaderDto.setSiebelInvoicenumber(responseDto.getSiebelInvoicenumber());
					
					System.out.println("Hi Siebel 123= SiebelStatus"+soHeaderDto.getSiebelStatus());
					//System.out.println(soHeaderDto.getSiebelRemark());
					System.out.println("SiebelInvoicenumber"+soHeaderDto.getSiebelInvoicenumber());
					
					if(responseDto.getBeatId()!= null) {
					soHeaderDto.setBeatId(responseDto.getBeatId());
					}
					if(responseDto.getHeaderPriceList()!= null) {
					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
					}
					
					if (responseDto.getProprietorName() != null) {
						soHeaderDto.setProprietorName(responseDto.getProprietorName());
					}
					if (responseDto.getRemark() != null) {
						soHeaderDto.setRemark(responseDto.getRemark());
					}
					if (responseDto.getDeliveryDate() != null) {
						soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
					}
					if (responseDto.getUserId() != null) {
						soHeaderDto.setUserId(responseDto.getUserId());
					}
					if (responseDto.getOutletAddress() != null) {
						soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
					}
					if (responseDto.getCustomerId() != null) {
						soHeaderDto.setCustomerId(responseDto.getCustomerId());
					}
					if (responseDto.getAddress() != null) {
						soHeaderDto.setAddress(responseDto.getAddress());
					}
					
					if (responseDto.getCity() != null) { soHeaderDto.setCity(responseDto.getCity()); }
					
					if(responseDto.getBeatId()!= null) {
						soHeaderDto.setBeatId(responseDto.getBeatId());
					}
					if(responseDto.getHeaderPriceList()!= null) {
					soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
					}
					
					soHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);

					List<SoLineDto> soLineDtoList = new ArrayList<SoLineDto>();
					if(soLineDto.getLineId() != null) {
						soLineDtoList.add(soLineDto);
					}
					soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
				}

			}System.out.println("in method getAllPendingOrders service line 4726 ="+ new Date());
			OrderDetailsDto orderDetailsDto = new OrderDetailsDto();

			List<SoHeaderDto> soHeaderDtoList = new ArrayList<SoHeaderDto>();

			for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
				SoHeaderDto soHeaderDto = entry.getValue();

				List<SoLineDto> soLineDtoList = soLineDtoMap.get(entry.getKey());
				soHeaderDto.setSoLineDtoList(soLineDtoList);

				soHeaderDtoList.add(soHeaderDto);
			}System.out.println("in method getAllPendingOrders service line 4738 ="+ new Date());
			orderDetailsDto.setSoHeaderDto(soHeaderDtoList);

			if (!responseDtoList.isEmpty()) {
				status.setCode(RECORD_FOUND);// = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
				status.setData(orderDetailsDto);
				System.out.println("Exit from  method getAllPendingOrders service line 4744 ="+ LocalDateTime.now());
				return status;
			}
			status.setCode(RECORD_NOT_FOUND); // status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
			System.out.println("Exit from record not found method getAllPendingOrders service line 4744 ="+ LocalDateTime.now());
			return status;
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		} return null;
	}
*/

/* this is 1st optimized code comment on 03-June-2024 for optimization	
	@Override
	public Status getAllPendingOrders(RequestDto requestDto) throws ServiceException, IOException {
		    try {
		        System.out.println("In method getAllPendingOrders at =" + LocalDateTime.now());
		        Status status = new Status();
		        System.out.println("Above getSoHeader11 query call at =" + LocalDateTime.now());
		        List<ResponseDto> responseList = ltSoHeadersDao.getSoHeader11(requestDto);
		        System.out.println("Below getSoHeader11 query call at =" + LocalDateTime.now());
		        System.out.println("headerIdsList size is = " + responseList.size());
	 
		        // Use a Map to store SoHeaderDtoPendingOrders and their corresponding SoLineDtoPendingOrders
		        Map<Long, SoHeaderDtoPendingOrders> headerMap = new HashMap<>();
		        Map<SoHeaderDtoPendingOrders, List<SoLineDtoPendingOrders>> resultMap = new HashMap<>();
		        System.out.println("Above for loop "+LocalDateTime.now());
		        for (ResponseDto response : responseList) {
		            Long headerId = response.getHeaderId();
	 
		            // Extract header and line information from response
		            SoHeaderDtoPendingOrders header = extractSoHeader(response);
		            SoLineDtoPendingOrders line = extractSoLine(response);
	 
		            // If header is not in the map, add it
		            if (!headerMap.containsKey(headerId)) {
		                headerMap.put(headerId, header);
		                resultMap.put(header, new ArrayList<>());
		            }
	 
		            // Add line to the corresponding header's list
		            resultMap.get(headerMap.get(headerId)).add(line);
		        }
		        System.out.println("Below for loop "+LocalDateTime.now());
		        // Printing the map to verify
		        resultMap.forEach((key, value) -> {
		            System.out.println("Header: " + key + " -> Lines: " + value);
		        });
	 
		        if (responseList.isEmpty()) {
		            status.setCode(RECORD_NOT_FOUND);
		            status.setData("Record not found");
			        System.out.println("Exit from in if getAllPendingOrders at =" + LocalDateTime.now());
		            return status;
		        } else {
		            status.setCode(SUCCESS);
		            status.setData(resultMap);
			        System.out.println("Exit from in else getAllPendingOrders at =" + LocalDateTime.now());
		            return status;
		        }
		    } catch (Exception e) {
		        logger.error("Error Description :", e);
		        e.printStackTrace();
		    }
		    return null;
		}
*/		
	
	public Status getAllPendingOrders(RequestDto requestDto) throws ServiceException, IOException {
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetSoHeader111 = 0;
		long outQuerygetSoHeader111 = 0;
//		long inQuerygetInStockProductAdmin = 0;
//		long outQuerygetInStockProductAdmin = 0;
//		long inQuerygetInStockProductCountForAdmin = 0;
//		long outQuerygetInStockProductCountForAdmin = 0;
//		long inQuerygetMultipleMrpForProduct = 0;
//		long outQuerygetMultipleMrpForProduct = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
	    try {
	        System.out.println("In method getAllPendingOrders at =" + LocalDateTime.now());
	        Status status = new Status();
	        System.out.println("Above getSoHeader11 query call at =" + LocalDateTime.now());
	        inQuerygetSoHeader111 = System.currentTimeMillis();
	        List<ResponseDto> responseList = ltSoHeadersDao.getSoHeader111(requestDto);
	        outQuerygetSoHeader111 = System.currentTimeMillis();
	        System.out.println("Below getSoHeader11 query call at =" + LocalDateTime.now());
	        System.out.println("headerIdsList size is = " + responseList.size());
 
	        // Use a Map to store SoHeaderDtoPendingOrders and their corresponding SoLineDtoPendingOrders
	        Map<Long, SoHeaderDtoPendingOrders> headerMap = new HashMap<>();
	        Map<SoHeaderDtoPendingOrders, List<SoLineDtoPendingOrders>> resultMap = new HashMap<>();
	        System.out.println("Above for loop "+LocalDateTime.now());
	        for (ResponseDto response : responseList) {
	            Long headerId = response.getHeaderId();
 
	            // Extract header and line information from response
	            SoHeaderDtoPendingOrders header = extractSoHeader(response);
	            SoLineDtoPendingOrders line = extractSoLine(response);
 
	            // If header is not in the map, add it
	            if (!headerMap.containsKey(headerId)) {
	                headerMap.put(headerId, header);
	                resultMap.put(header, new ArrayList<>());
	            }
//
//	            // Add line to the corresponding header's list
	            resultMap.get(headerMap.get(headerId)).add(line);
	        }
	        System.out.println("Below for loop "+LocalDateTime.now());
	        // Printing the map to verify
	        resultMap.forEach((key, value) -> {
	            System.out.println("Header: " + key + " -> Lines: " + value);
	        });
	        
	        List<ResponsePendingOrdersDto> ordersList = new ArrayList<>();
	        for (Map.Entry<SoHeaderDtoPendingOrders, List<SoLineDtoPendingOrders>> entry : resultMap.entrySet()) {
	            SoHeaderDtoPendingOrders header = entry.getKey();
	            List<SoLineDtoPendingOrders> lines = entry.getValue();
	            ResponsePendingOrdersDto responseHeader = new ResponsePendingOrdersDto();
	            responseHeader.setHeaderId(header.getHeaderId());
	            responseHeader.setOrderNumber(header.getOrderNumber());
	            responseHeader.setOrderDate(header.getOrderDate());
	            responseHeader.setStatus(header.getStatus());
	            responseHeader.setStatus1(header.getStatus1());
	            responseHeader.setAddress(header.getAddress());
	            responseHeader.setOutletId(header.getOutletId());
	            responseHeader.setOutletName(header.getOutletName());
	            responseHeader.setOutletCode(header.getOutletCode());
	            responseHeader.setLatitude(header.getLatitude());
	            responseHeader.setUserId(header.getUserId());
	            responseHeader.setOutletAddress(header.getOutletAddress());
	            responseHeader.setProprietorName(header.getProprietorName());
	            responseHeader.setDeliveryDate(header.getDeliveryDate());
	            responseHeader.setInstockFlag(header.getInstockFlag());
	            responseHeader.setBeatId(header.getBeatId());
	            responseHeader.setPriceList(header.getPriceList());;
	            responseHeader.setOrderLinesItems(lines);
	            ordersList.add(responseHeader);
	        }

	        // Print the results
	        for (ResponsePendingOrdersDto order : ordersList) {
	            System.out.println(order);
	        }
 
	        if (responseList.isEmpty()) {
	            status.setCode(RECORD_NOT_FOUND);
	            status.setData("Record not found");
		        System.out.println("Exit from in if getAllPendingOrders at =" + LocalDateTime.now());
	            return status;
	        } else {
	            status.setCode(SUCCESS);
	            status.setData(ordersList);
	            timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetSoHeader111,outQuerygetSoHeader111));
//	    		timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetInStockProductAdmin,outQuerygetInStockProductAdmin));
//	    		timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
//	    		timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
//	    		timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//	    		timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
	    		
	    		long methodOut = System.currentTimeMillis();
	    		System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
	            timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
	            status.setTimeDifference(timeDifference);
		        System.out.println("Exit from in else getAllPendingOrders at =" + LocalDateTime.now());
	            return status;
	        }
	    } catch (Exception e) {
	        logger.error("Error Description :", e);
	        e.printStackTrace();
	    }
	    return null;
	}
	
		private static SoHeaderDtoPendingOrders extractSoHeader(ResponseDto response) {
		    return new SoHeaderDtoPendingOrders(
		        response.getHeaderId(),
		        response.getOrderNumber(),
		        response.getOrderDate(),
		        response.getStatus(),
		        response.getStatus1(),
		        response.getAddress(),
		        response.getOutletId(),
		        response.getOutletName(),
		        response.getOutletCode(),
		        response.getLatitude(),
		        response.getLongitude(),
		        response.getUserId(),
		        response.getOutletAddress(),
		        response.getProprietorName(),
		        response.getDeliveryDate(),
		        response.getInstockFlag(),
		        response.getPriceList(),
		        response.getBeatId()
		    );
		}
		
		private static SoLineDtoPendingOrders extractSoLine(ResponseDto response) {
		    return new SoLineDtoPendingOrders(
		        response.getLineId(),
		        response.getProductId(),
		        response.getQuantity(),
		        response.getProductCode(),
		        response.getProductDesc(),
		        response.getProductName(),
		        response.getListPrice(),
		        response.getPriceList(),
		        response.getPtrPrice(),
		        response.getDeliveryDate1(),
		        response.getLinelistPrice(),
		        response.getLinePtrPrice(),
		        response.getInventoryQuantity(),
		        response.getOrgId(),
		        response.getCategoryId(),
		        response.getProductType(),
		        response.getCategory(),
		        response.getSubCategory(),
		        response.getPrimaryUom(),
		        response.getSecondaryUom(),
		        response.getSecondaryUomValue(),
		        response.getUnitsPerCase(),
		        response.getSegment(),
		        response.getBrand(),
		        response.getCasePack(),
		        response.getHsnCode(),
		        response.getBeatId()
		    );
		}
	
	
	@Override
	public Status removingPendingOrdersFromGetOrderV2(RequestDto requestDto) throws ServiceException, IOException {
		Map<String,String> timeDifference = new HashMap<>();
		long methodIn = System.currentTimeMillis();
		long inQuerygetSoHeaderRemovingPendingOrdersFromGetOrderV2 = 0;
		long outQuerygetSoHeaderRemovingPendingOrdersFromGetOrderV2 = 0;
		long inQuerygetOrderV2RemovingPendingOrdersFromGetOrderV2 = 0;
		long outQuerygetOrderV2RemovingPendingOrdersFromGetOrderV2 = 0;
//		long inQuerygetInStockProductCountForAdmin = 0;
//		long outQuerygetInStockProductCountForAdmin = 0;
//		long inQuerygetMultipleMrpForProduct = 0;
//		long outQuerygetMultipleMrpForProduct = 0;
//		long inQuerygetInStockProductWithInventory = 0;
//		long outQuerygetInStockProductWithInventory = 0;
//		long inQuerygetInStockProductCountWithInventory = 0;
//		long outQuerygetInStockProductCountWithInventory = 0;
			try {
				Status status = new Status();
				//System.out.println("Login Id is ="+requestDto.getLoginId());
				inQuerygetSoHeaderRemovingPendingOrdersFromGetOrderV2 = System.currentTimeMillis();
				List<Long> headerIdsList = ltSoHeadersDao.getSoHeaderRemovingPendingOrdersFromGetOrderV2(requestDto);
				outQuerygetSoHeaderRemovingPendingOrdersFromGetOrderV2 = System.currentTimeMillis();
				System.out.print("headerIdsList is ====" +headerIdsList);
				System.out.print("requestDto data is =="+requestDto);
				//Long recordCount = ltSoHeadersDao.getRecordCountRemovingPendingOrdersFromGetOrderV2(requestDto);
				//Long recordCount = (long) headerIdsList.size() + 1;
				
				System.out.println("headerIdsList====>"+headerIdsList.size());
				//System.out.println("recordCount====>"+recordCount);
				
				//status.setTotalCount(recordCount);
				//status.setRecordCount(recordCount);
				if(headerIdsList.isEmpty()) {
					status.setCode(RECORD_NOT_FOUND);
					status.setData("Record not found");
					return status;
				}
				
				// code for not showing pending_approval to himself
				
//				List<Long> headerIdsListUpdated = new ArrayList<Long>();
//						if(!headerIdsList.isEmpty()) {
//							for(Long headerId:headerIdsList) {
//								SoHeaderDto list = ltSoHeadersDao.getheaderByHeaderId(headerId);
//								System.out.println("listlistlist=="+list);
//								System.out.println("list.getCreatedBy() is="+list.getCreatedBy());
//								System.out.println("requestDto.getUserId() is ="+requestDto.getLoginId());
//								System.out.println("list.getStatus() is="+list.getStatus());
//								
//								if (!(list.getCreatedBy() == requestDto.getLoginId()) && !list.getStatus().equalsIgnoreCase("PENDING_APPROVAL")) {
//											headerIdsListUpdated.add(headerId);
//											System.out.println("header Ids List Updated is="+headerIdsListUpdated);
//										}
//							}
//						}
				
				
				List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
				
				inQuerygetOrderV2RemovingPendingOrdersFromGetOrderV2 = System.currentTimeMillis();
				responseDtoList = ltSoHeadersDao.getOrderV2RemovingPendingOrdersFromGetOrderV2(headerIdsList);
				outQuerygetOrderV2RemovingPendingOrdersFromGetOrderV2 = System.currentTimeMillis();
				
			//	responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsListUpdated);
				
				Map<Long, SoHeaderDto> soHeaderDtoMap = new LinkedHashMap<Long, SoHeaderDto>();
				Map<Long, List<SoLineDto>> soLineDtoMap = new LinkedHashMap<Long, List<SoLineDto>>();

//				System.out.println("responseDto befoe filter is ===" +responseDtoList1);
//				System.out.println("UserId is ===="+requestDto.getUserId());
//				List<ResponseDto> responseDtoList = responseDtoList1.stream().filter(X->X.getUserId()!=requestDto.getUserId()).collect(Collectors.toList());
//				System.out.println("responseDto after filter is ===" +responseDtoList);
				
				for (Iterator iterator = responseDtoList.iterator(); iterator.hasNext();) {
					ResponseDto responseDto = (ResponseDto) iterator.next();
	                						
					System.out.println("responseDto is ===" +responseDto);
					
					SoLineDto soLineDto = new SoLineDto();

					if (responseDto.getLineId() != null) {
						soLineDto.setLineId(responseDto.getLineId());
					}
					if (responseDto.getProductId() != null) {
						soLineDto.setProductId(responseDto.getProductId());
					}
					if (responseDto.getQuantity() != null) {
						soLineDto.setQuantity(responseDto.getQuantity());
					}
					if (responseDto.getProductCode() != null) {
						soLineDto.setProductCode(responseDto.getProductCode());
					}
					if (responseDto.getProductDesc() != null) {
						soLineDto.setProductDesc(responseDto.getProductDesc());
					}
					if (responseDto.getProductName() != null) {
						soLineDto.setProductName(responseDto.getProductName());
					}
					if (responseDto.getPriceList() != null) {
						soLineDto.setPriceList(responseDto.getPriceList());
						
					}
				
					if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) 
						|| responseDto.getStatus().equals("APPROVED")) {
						if (responseDto.getListPrice() != null) {
							soLineDto.setListPrice(responseDto.getListPrice());
						}
					}else {
						if (responseDto.getLinelistPrice() != null) {
							soLineDto.setListPrice(responseDto.getLinelistPrice());
						}
					}
									
					if (responseDto.getPtrPrice() != null) {
						if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
							soLineDto.setPtrPrice(responseDto.getListPrice());
						}else {
							soLineDto.setPtrPrice(responseDto.getPtrPrice());
						}
					}
				
					if (responseDto.getPtrPrice() != null) {
						//System.out.println("IF responseDto.getPtrPrice() != null "+responseDto.getPtrPrice());
						if(responseDto.getPtrFlag()!= null && responseDto.getPtrFlag().equalsIgnoreCase("Y")) {
							//soLineDto.setPtrPrice(responseDto.getListPrice());
							if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
									responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
								if (responseDto.getListPrice() != null) {
									soLineDto.setListPrice(responseDto.getListPrice());
								}
							}else {
								if (responseDto.getLinelistPrice() != null) {
									soLineDto.setListPrice(responseDto.getLinelistPrice());
								}
							}
						}else {
							//soLineDto.setPtrPrice(responseDto.getPtrPrice());
							if (responseDto.getStatus().equalsIgnoreCase(DRAFT) || responseDto.getStatus().equalsIgnoreCase(PENDINGAPPROVAL) ||
									responseDto.getStatus().equalsIgnoreCase("APPROVED")) {
								if (responseDto.getPtrPrice() != null) {
									soLineDto.setPtrPrice(responseDto.getPtrPrice());
								}
							}else {
								if (responseDto.getLinePtrPrice() != null) {
									soLineDto.setPtrPrice(responseDto.getLinePtrPrice());
								}
							}
						}
					}
					
					if (responseDto.getInventoryQuantity() != null) {
						soLineDto.setInventoryQuantity(responseDto.getInventoryQuantity());
					}
					if (responseDto.getDeliveryDate() != null) {
						soLineDto.setDeliveryDate(responseDto.getDeliveryDate1());
					}
					if (responseDto.getOrgId() != null) {
						soLineDto.setOrgId(responseDto.getOrgId());
					}
					if (responseDto.getProductType() != null) {
						soLineDto.setProductType(responseDto.getProductType());
					}
					if (responseDto.getPrimaryUom() != null) {
						soLineDto.setPrimaryUom(responseDto.getPrimaryUom());
					}
					if (responseDto.getSecondaryUom() != null) {
						soLineDto.setSecondaryUom(responseDto.getSecondaryUom());
					}
					if (responseDto.getSecondaryUomValue() != null) {
						soLineDto.setSecondaryUomValue(responseDto.getSecondaryUomValue());
					}
					if (responseDto.getUnitsPerCase() != null) {
						soLineDto.setUnitsPerCase(responseDto.getUnitsPerCase());
					}
					if (responseDto.getProductImage() != null) {
						soLineDto.setProductImage(responseDto.getProductImage());
					}
					if (responseDto.getBrand() != null) {
						soLineDto.setBrand(responseDto.getBrand());
					}
					if (responseDto.getSubBrand() != null) {
						soLineDto.setSubBrand(responseDto.getSubBrand());
					}
					if (responseDto.getSegment() != null) {
						soLineDto.setSegment(responseDto.getSegment());
					}
					if (responseDto.getCasePack() != null) {
						soLineDto.setCasePack(responseDto.getCasePack());
					}
					if (responseDto.getHsnCode() != null) {
						soLineDto.setHsnCode(responseDto.getHsnCode());
					}
					if (responseDto.getThumbnailImage() != null) {
						soLineDto.setThumbnailImage(responseDto.getThumbnailImage());
					}

					if (soLineDtoMap.get(responseDto.getHeaderId()) != null) {
						// already exost
						List<SoLineDto> soLineDtoList = soLineDtoMap.get(responseDto.getHeaderId());
						if(soLineDto.getLineId() != null) {
							soLineDtoList.add(soLineDto);
						}
						soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
					} else {
						// add data into soheader map
						SoHeaderDto soHeaderDto = new SoHeaderDto();
						soHeaderDto.setHeaderId(responseDto.getHeaderId());
						soHeaderDto.setOrderNumber(responseDto.getOrderNumber());
						
						//FOR DEV UTC TIME ZONE
						//soHeaderDto.setOrderDate(responseDto.getOrderDate().toString());
						
						//FOR DEV IST TIME ZONE
						final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
						final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
						final TimeZone utc = TimeZone.getTimeZone("UTC");
						sdf.setTimeZone(utc);
						soHeaderDto.setOrderDate(sdf.format(responseDto.getOrderDate()));
						
						soHeaderDto.setStatus(responseDto.getStatus());
//						if(soHeaderDto.getStatus().equalsIgnoreCase("APPROVED")) {
//							saveOrderIntoSiebel(ltSoHeader, soHeaderDto);
//						}
						
						soHeaderDto.setAddress(responseDto.getAddress());
						soHeaderDto.setOutletName(responseDto.getOutletName());
						soHeaderDto.setOutletId(responseDto.getOutletId());
						soHeaderDto.setOutletCode(responseDto.getOutletCode());
						soHeaderDto.setLatitude(responseDto.getLatitude());
						soHeaderDto.setLongitude(responseDto.getLongitude());
						
						soHeaderDto.setInstockFlag(responseDto.getInstockFlag());
						soHeaderDto.setPriceList(responseDto.getPriceList());
						//soHeaderDto.setBeatId(responseDto.getBeatId());
						try {
						System.out.println("Hi Siebel in get order v2 try = SiebelStatus"+responseDto.getSiebelStatus());
						//System.out.println("SiebelRemark"+responseDto.getSiebelRemark());
						//System.out.println(responseDto.getSiebelInvoicenumber());
						//System.out.println("SiebelJsonpayload"+responseDto.getSiebelJsonpayload());
						
						//soHeaderDto.setSiebelStatus(responseDto.getSiebelStatus());
						soHeaderDto.setSiebelRemark(responseDto.getSiebelRemark());
						//soHeaderDto.setSiebelInvoicenumber(responseDto.getSiebelInvoicenumber());
						//soHeaderDto.setSiebelJsonpayload(responseDto.getSiebelJsonpayload());
						
						/*System.out.println("Hi Siebel 123= SiebelStatus"+soHeaderDto.getSiebelStatus());
						System.out.println("SiebelRemark"+soHeaderDto.getSiebelRemark());
						System.out.println(soHeaderDto.getSiebelInvoicenumber());
						System.out.println("SiebelJsonpayload"+responseDto.getSiebelJsonpayload());*/
						}catch(Exception e) {
							logger.error("Error Description :", e);
							e.printStackTrace();
						}
						if(responseDto.getBeatId()!= null) {
						soHeaderDto.setBeatId(responseDto.getBeatId());
						}
						if(responseDto.getHeaderPriceList()!= null) {
						soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
						}
						
						if (responseDto.getProprietorName() != null) {
							soHeaderDto.setProprietorName(responseDto.getProprietorName());
						}
						if (responseDto.getRemark() != null) {
							soHeaderDto.setRemark(responseDto.getRemark());
						}
						if (responseDto.getDeliveryDate() != null) {
							soHeaderDto.setDeliveryDate(responseDto.getDeliveryDate());
						}
						if (responseDto.getUserId() != null) {
							soHeaderDto.setUserId(responseDto.getUserId());
						}
						if (responseDto.getOutletAddress() != null) {
							soHeaderDto.setOutletAddress(responseDto.getOutletAddress());
						}
						if (responseDto.getCustomerId() != null) {
							soHeaderDto.setCustomerId(responseDto.getCustomerId());
						}
						if (responseDto.getAddress() != null) {
							soHeaderDto.setAddress(responseDto.getAddress());
						}
						
						if (responseDto.getCity() != null) { soHeaderDto.setCity(responseDto.getCity()); }
						
						if(responseDto.getBeatId()!= null) {
							soHeaderDto.setBeatId(responseDto.getBeatId());
						}
						if(responseDto.getHeaderPriceList()!= null) {
						soHeaderDto.setPriceList(responseDto.getHeaderPriceList());
						}
						
						soHeaderDtoMap.put(responseDto.getHeaderId(), soHeaderDto);

						List<SoLineDto> soLineDtoList = new ArrayList<SoLineDto>();
						if(soLineDto.getLineId() != null) {
							soLineDtoList.add(soLineDto);
						}
						soLineDtoMap.put(responseDto.getHeaderId(), soLineDtoList);
					}

				}
				OrderDetailsDto orderDetailsDto = new OrderDetailsDto();

				List<SoHeaderDto> soHeaderDtoList = new ArrayList<SoHeaderDto>();

				for (Map.Entry<Long, SoHeaderDto> entry : soHeaderDtoMap.entrySet()) {
					SoHeaderDto soHeaderDto = entry.getValue();

					List<SoLineDto> soLineDtoList = soLineDtoMap.get(entry.getKey());
					soHeaderDto.setSoLineDtoList(soLineDtoList);

					soHeaderDtoList.add(soHeaderDto);
				}
				orderDetailsDto.setSoHeaderDto(soHeaderDtoList);

				if (!responseDtoList.isEmpty()) {
					status.setCode(RECORD_FOUND);// = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
					status.setData(orderDetailsDto);
					
					timeDifference.put("QuerygetUserTypeByUserId", timeDiff(inQuerygetSoHeaderRemovingPendingOrdersFromGetOrderV2,outQuerygetSoHeaderRemovingPendingOrdersFromGetOrderV2));
					timeDifference.put("QuerygetInStockProductAdmin", timeDiff(inQuerygetOrderV2RemovingPendingOrdersFromGetOrderV2,outQuerygetOrderV2RemovingPendingOrdersFromGetOrderV2));
//					timeDifference.put("QuerygetInStockProductCountForAdmin", timeDiff(inQuerygetInStockProductCountForAdmin, outQuerygetInStockProductCountForAdmin));
//					timeDifference.put("QuerygetMultipleMrpForProduct",timeDiff(inQuerygetMultipleMrpForProduct, outQuerygetMultipleMrpForProduct));
//					timeDifference.put("QuerygetInStockProductWithInventory", timeDiff(inQuerygetInStockProductWithInventory,outQuerygetInStockProductWithInventory));
//					timeDifference.put("QuerygetInStockProductCountWithInventory", timeDiff(inQuerygetInStockProductCountWithInventory,outQuerygetInStockProductCountWithInventory));
					
					long methodOut = System.currentTimeMillis();
					System.out.println("Exit from method getInStockProduct at "+LocalDateTime.now());
			        timeDifference.put("durationofMethodInOut", timeDiff(methodIn,methodOut));
			        status.setTimeDifference(timeDifference);
			        
					return status;
				}
				status.setCode(RECORD_NOT_FOUND); // status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
				status.setData(null);
				return status;
			} catch (Exception e) {
				logger.error("Error Description :", e);
				e.printStackTrace();
			} return null;
		
	}

	
}
