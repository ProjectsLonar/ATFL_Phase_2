package com.lonar.cartservice.atflCartService.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.json.JSONObject;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.common.BusinessException;
import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.controller.WebController;
import com.lonar.cartservice.atflCartService.dao.LtSoHeadersDao;

import com.lonar.cartservice.atflCartService.dto.DistributorDetailsDto;
import com.lonar.cartservice.atflCartService.dto.OrderDetailsDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoLineDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtMastOutles;
import com.lonar.cartservice.atflCartService.model.LtSoLines;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;

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
	private Environment env;
	
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
		List<LtMastUsers> distUsersList  = ltSoHeadersDao.getActiveDistUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		List<LtMastUsers>salesUsersList  = ltSoHeadersDao.getActiveSalesUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		
		List<LtMastUsers> areaHeadUserList = ltSoHeadersDao.getActiveAreaHeadeUsersFromHeaderId(ltSoHeader.getHeaderId(), ltSoHeader.getOrderNumber());
		
		//	Optional<LtMastOutles> outletsObj =ltMastOutletRepository.findById(ltSoHeader.getOutletId());
		//List<LtMastOutles> ltMastOutle = new ArrayList<LtMastOutles>();
		List<LtMastOutles>ltMastOutle = ltSoHeadersDao.getOutletDetailsById(ltSoHeader.getOutletId());
		
			String outletCode = "";
			String outletName = "";
			//if (outletsObj.isPresent()) {
			if (!ltMastOutle.isEmpty()) {
				//LtMastOutles ltMastOutlets = outletsObj.get();
				
				LtMastOutles ltMastOutlets = new LtMastOutles();
						
				if(ltMastOutlets.getOutletCode() != null) {
					outletCode = ltMastOutlets.getOutletCode() ; 
				}
				
				if(ltMastOutlets.getOutletName() != null) {
					outletName = ltMastOutlets.getOutletName() ;
				}
			}
		
			if(!areaHeadUserList.isEmpty()) {System.out.println("THis is 1 if");}
			if(ltSoHeader.getInStockFlag()=="N") {System.out.println("THis is 2 if");}
			if(ltSoHeader.getStatus()=="DRAFT") {System.out.println("THis is 3 if");}
			if(ltSoHeader.getOrderNumber()!= null) {System.out.println("THis is 4 if");}
			
			System.out.println("Thisss ISSSS ISSSUEEEE"+ ltSoHeader.getInStockFlag());
			
			// send salesOrder approval notification to areHead if order is outOfStock
			if(!areaHeadUserList.isEmpty() && ltSoHeader.getInStockFlag()!="Y" && 
					ltSoHeader.getStatus()=="DRAFT" && ltSoHeader.getOrderNumber()!= null) 
			{	
					for(Iterator iterator = areaHeadUserList.iterator(); iterator.hasNext();) {
						LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
						System.out.print(ltMastUsers.getTokenData());
						if(ltMastUsers.getTokenData() != null) {
							webController.send(ltMastUsers, ltSoHeader, outletCode, outletName);
						}
					}
			}// send salesOrder approval notification to areHead if order is inStock & priceList is not default
			// here considering ALL_INDIA_RDS as default price list
			else if(!areaHeadUserList.isEmpty() && ltSoHeader.getInStockFlag()!="N" && ltSoHeader.getStatus()=="DRAFT" 
					&& ltSoHeader.getOrderNumber()!= null && ltSoHeader.getPriceList() != "ALL_INDIA_RDS") 
			      {			
						for(Iterator iterator = areaHeadUserList.iterator(); iterator.hasNext();) {
							LtMastUsers ltMastUsers = (LtMastUsers) iterator.next();
							System.out.print(ltMastUsers.getTokenData());
							if(ltMastUsers.getTokenData() != null) {
								webController.send(ltMastUsers, ltSoHeader, outletCode, outletName);
							}
						}
			}
			else {
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
	      }
	}

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
		 	String seqNoSixDigit = "1"+seqNofiveDigit;
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
		try {
			if (soHeaderDto.getOutletId() != null) {
				LtSoHeaders ltSoHeader = ltSoHeadersDao.checkHeaderStatusIsDraft(soHeaderDto.getOutletId());
				LtSoHeaders checkOrder = null;
				if (soHeaderDto.getOrderNumber() != null && soHeaderDto.getOrderNumber() != "") {
					checkOrder = ltSoHeadersDao.checkOrderStatus(soHeaderDto.getOrderNumber());
				}
                  
				if (ltSoHeader == null && checkOrder == null && !soHeaderDto.getSoLineDtoList().isEmpty()) {
					// Save header in draft status with line and Line data is not empty
					status = saveSoHeadeLineInDraftV2(soHeaderDto);
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
						status = updateSoHeadeLineInDraftV2(soHeaderDto, checkOrder.getHeaderId(), checkOrder.getCreationDate(), checkOrder.getCreatedBy());
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

	
	private Status saveSoHeadeLineInDraftV2(SoHeaderDto soHeaderDto) throws ServiceException, IOException {
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
//						if (soHeaderDto.getUserId() != null) {
//							ltSoHeader.setLastUpdateLogin(soHeaderDto.getUserId());
//						}
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
							
							SoLineDto soLineDto = (SoLineDto) iterator.next();
							StringBuffer strQuery =  new StringBuffer();
							strQuery.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");

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
							strQuery.append(")");
							
							//ltSoLines = ltSoLinesRepository.save(ltSoLines);
														
							String query = strQuery.toString();
							
							int n = ltSoHeadersDao.insertLine(query);

						}
				//		strQuery.deleteCharAt(strQuery.length()-1); 
				//		strQuery1 = strQuery1.append(strQuery);
						
				//		String query = strQuery1.toString();
						
				//		int n = ltSoHeadersDao.insertLine(query);
						
				//		if (n > 0) {
							System.out.println("Line insert successfully");
							RequestDto requestDto = new RequestDto();
							requestDto.setHeaderId(ltSoHeader.getHeaderId());
							requestDto.setOffset(0);
							status = getOrderV2(requestDto);
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");

							return status;
				//		}
						
					} catch (Exception e) {
						logger.error("Error Description :", e);
						e.printStackTrace();
					}
					return null;
				  				  
				  
			  }else  if(user.getUserType().equalsIgnoreCase(SALES) || user.getUserType().equalsIgnoreCase(DISTRIBUTOR))
			    {
				// need to copy here below instock-outOfstock code 
				// for Instock products order
					if(soHeaderDto.getInStockFlag().equals("Y")) {
						
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

					if(soHeaderDto.getInStockFlag()!= null) {
						ltSoHeader.setInStockFlag(soHeaderDto.getInStockFlag());
					}
					if(soHeaderDto.getPriceList()!= null) {
						ltSoHeader.setPriceList(soHeaderDto.getPriceList());
					}
					
				//	considering ALL_INDIA_RDS as default priceList
					if(!soHeaderDto.getPriceList().equals("ALL_INDIA_RDS")) {
				 	          ltSoHeader.setStatus(DRAFT);
					}else {
						ltSoHeader.setStatus("APPROVED");
					}
//					if(soHeaderDto.getBeatId()!= null) {
//						ltSoHeader.setBeatId(soHeaderDto.getBeatId());
//					}
					
					ltSoHeader = updateSoHeader(ltSoHeader);
					
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
							
//							DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//							Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
//							SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//							String deliveryDate =outputFormat.format(date);
//						//	System.out.println("formatedDate : " + deliveryDate); 
												
						//	strQuery.append("'"+deliveryDate+"',");
							strQuery.append("'"+"',");
							
						 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
						}
						
						//strQuery.append("'"+new Date()+"',");//Created Date
						strQuery.append("'"+"',");  // set null for demo
						
						//strQuery.append("'"+new Date()+"'");//Last update date
						strQuery.append("'"+"'"); // set null for demo
						
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
			//		strQuery.deleteCharAt(strQuery.length()-1); 
			//		strQuery1 = strQuery1.append(strQuery);
					
					//String query = strQuery.toString();
								
					//int n = ltSoHeadersDao.insertLine(query);
					
				//	if (n > 0) {
						System.out.println("Line insert successfully");
						try {
						if(ltSoHeader.getOrderNumber()!= null && ltSoHeader.getStatus().equals("APPROVED")  && 
								ltSoHeader.getInStockFlag().equals("Y") && ltSoHeader.getPriceList().equals("ALL_INDIA_RDS")) {
							// considering ALL_INDIA_RDS as default priceList
							// need to send this reqBody to siebel
							
						///// push the save order data into siebel  
							
							SoLineDto soLineDto = new SoLineDto();
							
							sampleCode();
							System.out.println("Sample Code method call done");
							
							JSONObject lineItemObject = new JSONObject();
							lineItemObject.put("Id", "1");
						//	lineItemObject.put("Product Id", soLineDto.getProductId());
							lineItemObject.put("Product Id", "1-4XBK-2");
						//	lineItemObject.put("Due Date", soLineDto.getDeliveryDate());
							lineItemObject.put("Due Date", "12/06/2023");
						//	lineItemObject.put("Item Price List Id", soLineDto.getPriceListId());
							lineItemObject.put("Item Price List Id", "1-475Z");
							lineItemObject.put("Action Code", "New");
						//	lineItemObject.put("Name", soLineDto.getProductName());
							lineItemObject.put("Name", "P02IAPKP040");
						//	lineItemObject.put("Quantity", soLineDto.getQuantity());
							lineItemObject.put("Quantity", "1");
							
							JSONArray lineItemArray = new JSONArray();
							for (int i =0; i<lineItemObject.length(); i++) {
								lineItemArray.put(lineItemObject);	
							}
							
							JSONObject listOfLineItem = new JSONObject();
							listOfLineItem.put("Line Item", lineItemObject);
							
							SoHeaderDto soHeaders = new SoHeaderDto();
							
							JSONObject header = new JSONObject();
					//		header.put("Requested Ship Date", soHeaderDto.getDeliveryDate());
							header.put("Requested Ship Date", "12/06/2024");
							header.put("Order Type Id", "0-D14G");
					//		header.put("Account Id", soHeaderDto.getOutletId());
							header.put("Account Id", "1-BRWN-27");
							header.put("Status", "New");
							header.put("Order Type", "Service Order");
					//		header.put("Account", soHeaderDto.getOutletName());
							header.put("Account", "SHREE MAHALAXMI KIRANA AND GENERAL STORE");
							header.put("Currency Code", "INR");
					//		header.put("Order Number", soHeaderDto.getOrderNumber());
							header.put("Order Number", "MSO-53623-2324-11");
							header.put("Source Inventory Id", "1-2FPGVLJ");       //"1-2C7QNZG");
							header.put("ListOfLine Item", listOfLineItem);
							
							JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
							ListOfATOrdersIntegrationIO.put("Header", header);
							
							JSONObject siebelMassage = new JSONObject();
							siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
							siebelMassage.put("MessageId", "");
							siebelMassage.put("IntObjectName", "AT Orders Integration IO");
							siebelMassage.put("MessageType", "Integration Object");
							siebelMassage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
							
							JSONObject siebelMassages = new JSONObject();
							siebelMassages.put("SubmitFlag", "");
							siebelMassages.put("InvoiceFlag", "");
							siebelMassages.put("SiebelMessage" , siebelMassage);
							
					//		String apiUrl = env.getProperty("SiebelCreateSaveOrderApi");
					//		String apiUrl = env.getProperty("https://10.245.4.70:9014/siebel/v1.0/service/AT New Order Creation REST BS/CreateOrder?matchrequestformat=y");
							String apiUrl = "https://10.245.4.70:9014/siebel/v1.0/service/AT New Order Creation REST BS/CreateOrder?matchrequestformat=y";
							URL url = new URL(apiUrl);
							
							System.out.print(apiUrl);
							System.out.print(url);
							String UserName="Lonar_Test";
							String Password="Lonar123";
							String credential = UserName +":"+ Password;
							
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("POST");
							connection.setDoOutput(true);
							connection.setRequestProperty("Content-Type", "application/json");
							connection.setRequestProperty("Authorization", "Basic_Auth"+credential);
							
							String jsonPayload =siebelMassages.toString();
							
							System.out.println("jsonPayload"+jsonPayload);
							  try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) 
							  {
								  wr.writeBytes(jsonPayload);
							      wr.flush(); 
							  }
							
							  int responseCode = connection.getResponseCode(); 
							  System.out.println("Response Code: " + responseCode);
							  
							  BufferedReader reader; 
							  if (responseCode ==  HttpURLConnection.HTTP_OK) {
								  reader = new BufferedReader(new
							  InputStreamReader(connection.getInputStream())); 
								  } else { 
									  reader = new BufferedReader(new InputStreamReader(connection.getErrorStream())); }
							  
							  String line; 
							  StringBuilder response = new
							  StringBuilder();
							  
							  while ((line = reader.readLine()) != null) { response.append(line); }
							  reader.close();
							  
							  System.out.println("Response: " + response.toString());

							
						}else {
							
							// inStock order with different priceList need to send for approval to areHead
							sendNotifications(ltSoHeader);
						
						///// push the save order data into siebel  if order is approved
							
							SoLineDto soLineDto = new SoLineDto();
							
							JSONObject lineItemObject = new JSONObject();
							lineItemObject.put("Id", "1");
					//		lineItemObject.put("Product Id", soLineDto.getProductId());
							lineItemObject.put("Product Id", "1-4XBK-2");
					//		lineItemObject.put("Due Date", soLineDto.getDeliveryDate());
							lineItemObject.put("Due Date", "12/06/2023");
					//		lineItemObject.put("Item Price List Id", soLineDto.getPriceListId());
							lineItemObject.put("Item Price List Id", "1-475Z");
							lineItemObject.put("Action Code", "New");
					//		lineItemObject.put("Name", soLineDto.getProductName());
							lineItemObject.put("Name", "P02IAPKP040");
					//		lineItemObject.put("Quantity", soLineDto.getQuantity());
							lineItemObject.put("Quantity", "1");
							
							JSONArray lineItemArray = new JSONArray();
							for (int i =0; i<lineItemObject.length(); i++) {
								lineItemArray.put(lineItemObject);	
							}
							
							JSONObject listOfLineItem = new JSONObject();
							listOfLineItem.put("Line Item", lineItemObject);
							
							SoHeaderDto soHeaders = new SoHeaderDto();
							
							JSONObject header = new JSONObject();
					//		header.put("Requested Ship Date", soHeaderDto.getDeliveryDate());
							header.put("Requested Ship Date", "12/06/2024");
							header.put("Order Type Id", "0-D14G");
				//			header.put("Account Id", soHeaderDto.getOutletId());
							header.put("Account Id", "1-BRWN-27");
							header.put("Status", "New");
							header.put("Order Type", "Service Order");
				//			header.put("Account", soHeaderDto.getOutletName());
							header.put("Account", "SHREE MAHALAXMI KIRANA AND GENERAL STORE");
							header.put("Currency Code", "INR");
				//			header.put("Order Number", soHeaderDto.getOrderNumber());
							header.put("Order Number", "MSO-53623-2324-11");
							header.put("Source Inventory Id", "1-2FPGVLJ");    //"1-2C7QNZG");
							header.put("ListOfLine Item", listOfLineItem);
							
							JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
							ListOfATOrdersIntegrationIO.put("Header", header);
							
							JSONObject siebelMassage = new JSONObject();
							siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
							siebelMassage.put("MessageId", "");
							siebelMassage.put("IntObjectName", "AT Orders Integration IO");
							siebelMassage.put("MessageType", "Integration Object");
							siebelMassage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
							
							JSONObject siebelMassages = new JSONObject();
							siebelMassages.put("SubmitFlag", "");
							siebelMassages.put("InvoiceFlag", "");
							siebelMassages.put("SiebelMessage" , siebelMassage);
							
							String apiUrl = env.getProperty("SiebelCreateOrderApi");
						//	String apiUrl = env.getProperty("https://10.245.4.70:9014/siebel/v1.0/service/AT New Order Creation REST BS/CreateOrder?matchrequestformat=y");
							URL url = new URL(apiUrl);
							System.out.print(apiUrl);
							System.out.print(url);
							String UserName="Lonar_Test";
							String Password="Lonar123";
							String credential = UserName +":"+ Password;
							
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("POST");
							connection.setDoOutput(true);
							connection.setRequestProperty("Content-Type", "application/json");
							connection.setRequestProperty("Authorization", "Basic_Auth"+credential);
							
							String jsonPayload =siebelMassages.toString();
							
							System.out.println("jsonPayload"+jsonPayload);
							  try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) 
							  {
								  wr.writeBytes(jsonPayload);
							      wr.flush(); 
							  }
							
							  int responseCode = connection.getResponseCode(); 
							  System.out.println("Response Code: " + responseCode);
							  
							  BufferedReader reader; 
							  if (responseCode ==  HttpURLConnection.HTTP_OK) {
								  reader = new BufferedReader(new
							  InputStreamReader(connection.getInputStream())); 
								  } else { 
									  reader = new BufferedReader(new InputStreamReader(connection.getErrorStream())); }
							  
							  String line; 
							  StringBuilder response = new
							  StringBuilder();
							  
							  while ((line = reader.readLine()) != null) { response.append(line); }
							  reader.close();
							  
							  System.out.println("Response: " + response.toString());

						}
						
						RequestDto requestDto = new RequestDto();
						requestDto.setHeaderId(ltSoHeader.getHeaderId());
						requestDto.setOffset(0);
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
							e.printStackTrace();
						}
			            status.setCode(INSERT_SUCCESSFULLY);
						status.setMessage("Insert Successfully");

						return status;
					//}
					
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
						ltSoHeader.setStatus(DRAFT);

						if(soHeaderDto.getInStockFlag()!= null) {
							ltSoHeader.setInStockFlag(soHeaderDto.getInStockFlag());
						}
//						if(soHeaderDto.getBeatId()!= null) {
//							ltSoHeader.setBeatId(soHeaderDto.getBeatId());
//						}if(soHeaderDto.getPriceList()!= null) {
//							ltSoHeader.setPriceList(soHeaderDto.getPriceList());
//						}
						
						ltSoHeader = updateSoHeader(ltSoHeader);
							
						// send OutofStock order for approval to areHead
							sendNotifications(ltSoHeader);
						
						List<SoLineDto> soLineDtoList = soHeaderDto.getSoLineDtoList();
						
						StringBuffer strQuery1 =  new StringBuffer();
						
				//		strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");
						
				//		StringBuffer strQuery =  new StringBuffer();
						
						for (Iterator iterator = soLineDtoList.iterator(); iterator.hasNext();) {
												
							SoLineDto soLineDto = (SoLineDto) iterator.next();		
					//		strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,delivery_date,status,created_by,creation_date,last_update_login,last_updated_by,last_update_date,ptr_price) VALUES ");				
							strQuery1.append("insert into lt_so_lines (header_id,product_id,quantity,list_price,status,created_by,last_update_login,last_updated_by,ptr_price,eimstatus,delivery_date,creation_date,last_update_date) VALUES ");
							StringBuffer strQuery =  new StringBuffer();
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
								
//								DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");
//								Date date = (Date)formatter.parse(soLineDto.getDeliveryDate().toString());
//								SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
//								String deliveryDate =outputFormat.format(date);
//							//	System.out.println("formatedDate : " + deliveryDate); 
													
							//	strQuery.append("'"+deliveryDate+"',");
								strQuery.append("'"+"',");
								
							 //strQuery.append("'"+soLineDto.getDeliveryDate().toString()+"',");
							}
							
							//strQuery.append("'"+new Date()+"',");//Created Date
							strQuery.append("'"+"',");  // set null for demo
							
							//strQuery.append("'"+new Date()+"'");//Last update date
							strQuery.append("'"+"'"); // set null for demo
							
							//ltSoLines.setStatus(DRAFT);
							//ltSoLines.setLastUpdateDate(new Date());
							//ltSoLines.setCreationDate(new Date());
//							strQuery.append("),");
							
							strQuery.append(")");
							strQuery1 = strQuery1.append(strQuery);
							//ltSoLines = ltSoLinesRepository.save(ltSoLines);
			       				
						}
						//strQuery.deleteCharAt(strQuery.length()-1); 
						//strQuery1 = strQuery1.append(strQuery);
						
						String query = strQuery1.toString();
						
						int n = ltSoHeadersDao.insertLine(query);
						
						if (n > 0) {
							System.out.println("Line insert successfully");
							RequestDto requestDto = new RequestDto();
							requestDto.setHeaderId(ltSoHeader.getHeaderId());
							requestDto.setOffset(0);
							status = getOrderV2(requestDto);
							
							if(user.getUserType().equalsIgnoreCase(SALES)) {
								
								LtSalesPersonLocation ltSalesPersonLocation = new LtSalesPersonLocation();
								
								ltSalesPersonLocation.setBeatId(ltSoHeader.getBeatId());
								ltSalesPersonLocation.setOutletId(user.getOutletId());
								ltSalesPersonLocation.setOrderName(ltSoHeader.getOrderNumber());
								ltSalesPersonLocation.setAddress(user.getAddress());
								ltSalesPersonLocation.setLatitude(user.getLatitude());
								ltSalesPersonLocation.setLongitude(user.getLongitude());
								ltSalesPersonLocation.setCreatedBy(user.getUserId());
								ltSalesPersonLocation.setLastUpdatedBy(user.getUserId());
								ltSalesPersonLocation.setCreationDate(new Date());
								ltSalesPersonLocation.setLastUpdateDate(new Date());
								
								ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
							}
							
							if(ltSoHeader.getStatus().equalsIgnoreCase(PENDINGAPPROVAL)) {
								/// push the order into siebel and update the order status to NEW 
								SoLineDto soLineDto = new SoLineDto();
								
								JSONObject lineItemObject = new JSONObject();
								lineItemObject.put("Id", "1");
						//		lineItemObject.put("Product Id", soLineDto.getProductId());
								lineItemObject.put("Product Id", "1-4XBK-2");
						//		lineItemObject.put("Due Date", soLineDto.getDeliveryDate());
								lineItemObject.put("Due Date", "12/06/2023");
						//		lineItemObject.put("Item Price List Id", soLineDto.getPriceListId());
								lineItemObject.put("Item Price List Id", "1-475Z");
								lineItemObject.put("Action Code", "New");
						//		lineItemObject.put("Name", soLineDto.getProductName());
								lineItemObject.put("Name", "P02IAPKP040");
						//		lineItemObject.put("Quantity", soLineDto.getQuantity());
								lineItemObject.put("Quantity", "1");
								
								JSONArray lineItemArray = new JSONArray();
								for (int i =0; i<lineItemObject.length(); i++) {
									lineItemArray.put(lineItemObject);	
								}
								
								JSONObject listOfLineItem = new JSONObject();
								listOfLineItem.put("Line Item", lineItemObject);
								
								SoHeaderDto soHeaders = new SoHeaderDto();
								
								JSONObject header = new JSONObject();
					//			header.put("Requested Ship Date", soHeaderDto.getDeliveryDate());
								header.put("Requested Ship Date", "12/06/2024");
								header.put("Order Type Id", "0-D14G");
				//				header.put("Account Id", soHeaderDto.getOutletId());
								header.put("Account Id", "1-BRWN-27");
								header.put("Status", "New");
								header.put("Order Type", "Service Order");
					//			header.put("Account", soHeaderDto.getOutletName());
								header.put("Account", "SHREE MAHALAXMI KIRANA AND GENERAL STORE");
								header.put("Currency Code", "INR");
					//			header.put("Order Number", soHeaderDto.getOrderNumber());
								header.put("Order Number", "MSO-53623-2324-11");
								header.put("Source Inventory Id", "1-2FPGVLJ");   /// "1-2C7QNZG");
								header.put("ListOfLine Item", listOfLineItem);
								
								JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
								ListOfATOrdersIntegrationIO.put("Header", header);
								
								JSONObject siebelMassage = new JSONObject();
								siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
								siebelMassage.put("MessageId", "");
								siebelMassage.put("IntObjectName", "AT Orders Integration IO");
								siebelMassage.put("MessageType", "Integration Object");
								siebelMassage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
								
								JSONObject siebelMassages = new JSONObject();
								siebelMassages.put("SubmitFlag", "");
								siebelMassages.put("InvoiceFlag", "");
								siebelMassages.put("SiebelMessage" , siebelMassage);
								
							//	String apiUrl = env.getProperty("SiebelCreateOrderApi");
								String apiUrl = env.getProperty("https://10.245.4.70:9014/siebel/v1.0/service/AT New Order Creation REST BS/CreateOrder?matchrequestformat=y");
								URL url = new URL(apiUrl);
								System.out.print(apiUrl);
								System.out.print(url);
								String UserName="Lonar_Test";
								String Password="Lonar123";
								String credential = UserName +":"+ Password;
								
								HttpURLConnection connection = (HttpURLConnection) url.openConnection();
								connection.setRequestMethod("POST");
								connection.setDoOutput(true);
								connection.setRequestProperty("Content-Type", "application/json");
								connection.setRequestProperty("Authorization", "Basic_Auth"+credential);
								
								String jsonPayload =siebelMassages.toString();
								
								System.out.println("jsonPayload"+jsonPayload);
								  try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) 
								  {
									  wr.writeBytes(jsonPayload);
								      wr.flush(); 
								  }
								
								  int responseCode = connection.getResponseCode(); 
								  System.out.println("Response Code: " + responseCode);
								  
								  BufferedReader reader; 
								  if (responseCode ==  HttpURLConnection.HTTP_OK) {
									  reader = new BufferedReader(new
								  InputStreamReader(connection.getInputStream())); 
									  } else { 
										  reader = new BufferedReader(new InputStreamReader(connection.getErrorStream())); }
								  
								  String line; 
								  StringBuilder response = new
								  StringBuilder();
								  
								  while ((line = reader.readLine()) != null) { response.append(line); }
								  reader.close();
								
								  System.out.println("Response: " + response.toString());
							}
							
							status.setCode(INSERT_SUCCESSFULLY);
							status.setMessage("Insert Successfully");

							return status;
						}
						
					} 		  
				  
			  }
						
		}catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null; 
	}

	private void sampleCode(){
		String apiUrl = "https://10.245.4.70:9014/siebel/v1.0/service/AT New Order Creation REST BS/CreateOrder?matchrequestformat=y";
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String username = "LONAR_TEST";
        String password = "Lonar123";
       String credentials = username + ":" + password;
        //String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        headers.add("Authorization", "Basic " + credentials);
	
        //String requestBody = "{\"key\": \"value\"}";
        JSONObject lineItemObject = new JSONObject();
		lineItemObject.put("Id", "1");
	//	lineItemObject.put("Product Id", soLineDto.getProductId());
		lineItemObject.put("Product Id", "1-4XBK-2");
	//	lineItemObject.put("Due Date", soLineDto.getDeliveryDate());
		lineItemObject.put("Due Date", "12/06/2023");
	//	lineItemObject.put("Item Price List Id", soLineDto.getPriceListId());
		lineItemObject.put("Item Price List Id", "1-475Z");
		lineItemObject.put("Action Code", "New");
	//	lineItemObject.put("Name", soLineDto.getProductName());
		lineItemObject.put("Name", "P02IAPKP040");
	//	lineItemObject.put("Quantity", soLineDto.getQuantity());
		lineItemObject.put("Quantity", "1");
		
		JSONArray lineItemArray = new JSONArray();
		for (int i =0; i<lineItemObject.length(); i++) {
			lineItemArray.put(lineItemObject);	
		}
		
		JSONObject listOfLineItem = new JSONObject();
		listOfLineItem.put("Line Item", lineItemObject);
		
		SoHeaderDto soHeaders = new SoHeaderDto();
		
		JSONObject header = new JSONObject();
//		header.put("Requested Ship Date", soHeaderDto.getDeliveryDate());
		header.put("Requested Ship Date", "12/06/2024");
		header.put("Order Type Id", "0-D14G");
//		header.put("Account Id", soHeaderDto.getOutletId());
		header.put("Account Id", "1-BRWN-27");
		header.put("Status", "New");
		header.put("Order Type", "Service Order");
//		header.put("Account", soHeaderDto.getOutletName());
		header.put("Account", "SHREE MAHALAXMI KIRANA AND GENERAL STORE");
		header.put("Currency Code", "INR");
//		header.put("Order Number", soHeaderDto.getOrderNumber());
		header.put("Order Number", "MSO-53623-2324-11");
		header.put("Source Inventory Id", "1-2FPGVLJ");       //"1-2C7QNZG");
		header.put("ListOfLine Item", listOfLineItem);
		
		JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
		ListOfATOrdersIntegrationIO.put("Header", header);
		
		JSONObject siebelMassage = new JSONObject();
		siebelMassage.put("IntObjectFormat", "Siebel Hierarchical");
		siebelMassage.put("MessageId", "");
		siebelMassage.put("IntObjectName", "AT Orders Integration IO");
		siebelMassage.put("MessageType", "Integration Object");
		siebelMassage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
		
		JSONObject siebelMassages = new JSONObject();
		siebelMassages.put("SubmitFlag", "");
		siebelMassages.put("InvoiceFlag", "");
		siebelMassages.put("SiebelMessage" , siebelMassage);
		
        HttpEntity<String> requestEntity = new HttpEntity<>(siebelMassages.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        
        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);
                
	}
	
		
	private Status updateSoHeadeLineInDraftV2(SoHeaderDto soHeaderDto, Long headerId, Date creationDate, Long createdBy)
			throws ServiceException, IOException {
		
		int lineDeleteStatus = ltSoHeadersDao.deleteLineDataByHeaderIdAndReturnStatus(headerId);
		
		
		Status status = new Status();
		LtSoHeaders ltSoHeader = new LtSoHeaders();
		
		String mobileNumber = ltSoHeadersDao.getMobileNumber(soHeaderDto.getUserId());

		ltSoHeader.setHeaderId(headerId);
		ltSoHeader.setOutletId(soHeaderDto.getOutletId());
		
		
		ltSoHeader.setInStockFlag(soHeaderDto.getInStockFlag());
		
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
		if(soHeaderDto.getPriceList()!= null) {
			ltSoHeader.setPriceList(soHeaderDto.getPriceList());
		}
		if(soHeaderDto.getInStockFlag()!= null) {
			ltSoHeader.setInStockFlag(soHeaderDto.getInStockFlag());
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
			
			status = getOrderV2(requestDto);
			
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

	
	@Override
	public Status getOrderV2(RequestDto requestDto) throws ServiceException, IOException {
		try {
			Status status = new Status();
			
			List<Long> headerIdsList = ltSoHeadersDao.getSoHeader(requestDto);
			System.out.print("headerIdsList is ====" +headerIdsList);
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
			
			responseDtoList = ltSoHeadersDao.getOrderV2(headerIdsList);
			
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

}
