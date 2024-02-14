package com.lonar.cartservice.atflCartService.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.dao.LtSalesreturnDao;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsLineDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsResponseDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;
import com.lonar.cartservice.atflCartService.model.Status;
import com.lonar.cartservice.atflCartService.repository.LtSalesRetrunLinesRepository;
import com.lonar.cartservice.atflCartService.repository.LtSalesReturnRepository;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;


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
					ltSoLinestosave = updateLines(ltSoLinestosave);
					
					System.out.println("ltSoLinestosave"+ltSoLinestosave);
				}
				
			}else {
				//code for invoice number generation
				String salesReturnNumber = generateSalesreturnNumber();
				
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
					ltSoLinestosave = updateLines(ltSoLinestosave);
				}
				
				
			}
			///siebel json creation
			
			if(ltSalesReturnDto.getReturnStatus().equalsIgnoreCase("APPROVED")) {
			
			String url = "https://10.245.4.70:9014/siebel/v1.0/service/AT%20New%20Order%20Creation%20REST%20BS/CreateReturnOrder?matchrequestformat=y";
	        String method = "POST";
	        String contentType = "Content-Type: application/json";
	        String authorization = "Authorization: Basic TE9OQVJfVEVTVDpMb25hcjEyMw==";
			
	        
			JSONObject salesReturnDetail = new JSONObject();
			JSONObject SiebelMessage = new JSONObject();
			JSONObject ListOfATOrdersIntegrationIO = new JSONObject();
			JSONObject Header = new JSONObject();
			JSONObject ListOfLineItem = new JSONObject();
			JSONArray LineItem = new JSONArray();
			JSONObject lineData = new JSONObject();
			JSONObject ListOfXA = new JSONObject();
			JSONObject XA = new JSONObject();
			
			salesReturnDetail.put("InvoiceNumber","INV-29686-2223-000218");
			salesReturnDetail.put("ReturnReason", "SHORT RECEIVED BY CUSTOMER");  
			salesReturnDetail.put("SiebelMessage", SiebelMessage);
			
			
			SiebelMessage.put("IntObjectFormat", "Siebel Hierarchical");
			SiebelMessage.put("MessageId", "");
			SiebelMessage.put("IntObjectName", "AT Orders Integration IO");
			SiebelMessage.put("MessageType", "Integration Object");
			SiebelMessage.put("ListOfAT Orders Integration IO", ListOfATOrdersIntegrationIO);
			
			ListOfATOrdersIntegrationIO.put("Header", Header);
			
			Header.put("Account Id", "1-EEWE-478");
			Header.put("Account", "3M CAR CARE");
			Header.put("Order Number", "RMA-29686-2223-000078");
			Header.put("Source Inventory Id", "1-2C7QNZG");
			Header.put("ListOfLine Item", ListOfLineItem);
			
			ListOfLineItem.put("Line Item", LineItem);
			
			lineData.put("Product Id", "1-MZZR-1");
			lineData.put("Name", "P02IAPKP040");
			lineData.put("Quantity", "10");
			lineData.put("ListOfXA", ListOfXA);
			
			ListOfXA.put("XA", XA);
			
			XA.put("Qty", "10");
			XA.put("Location", "20602-29686-HFS");
			XA.put("Availability", "On Hand");
			XA.put("Status", "Good");
			XA.put("Lot Number", "");
			
			List<LtSalesReturnLines>lineItem= ltSalesReturnDto.getLtSalesReturnLines();
     		    for(int i=0; i<=lineItem.size(); i++) 
     		    {
     			  //  LtSalesReturnLines ltSalesReturnLines= new LtSalesReturnLines();
     			    String prodId= lineItem.get(i).getProductId();
     			    String prodName= lineItem.get(i).getProductName();
     			    String qty=   Long.toString(lineItem.get(i).getReturnQuantity());    			   
     			    
     			    //data for listOfXA
     			    String qty1 = Long.toString(lineItem.get(i).getReturnQuantity()); 
     			    String location = lineItem.get(i).getLocation();
     			    String availability = lineItem.get(i).getAvailability();
     			    String status1= lineItem.get(i).getStatus();
     			    String lotNo= lineItem.get(i).getLotNumber();
     			    
     			 //  lineItem.add(ltSalesReturnLines);
     			
     			    lineData.put("Product Id", prodId);
     			    lineData.put("Name", prodName );
     			    lineData.put("Quantity", qty);
     			
     			    XA.put("Qty", qty1);
     			    XA.put("Location",location);
     			    XA.put("Availability", availability);
     			    XA.put("Status",status1);
     			    XA.put("Lot Number",lotNo);
     			    
     			   ListOfXA.put("XA", XA);
     			   lineData.put("ListOfXA", ListOfXA);
     			   LineItem.put(lineData);
     					
     		}
     		
			
			  String jsonPayload =salesReturnDetail.toString();

		        System.out.println(jsonPayload);     //(requestBody);
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
		        con.setRequestProperty("Authorization", "Basic TE9OQVJfVEVTVDpMb25hcjEyMw==");

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
		        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		        StringBuilder response = new StringBuilder();
		        String line;
		        while ((line = reader.readLine()) != null) {
		            response.append(line);
		        }
		        reader.close();

		        // Show the response
		        System.out.println("Response Body: " + response.toString());

			  			
			}
			//get sales return response
			RequestDto requestDto = new RequestDto();
			requestDto.setSalesReturnNumber(ltSalesReturnHeader.getSalesReturnNumber());
			requestDto.setLimit(1);
			requestDto.setOffset(2);
			
			status = getSalesReturn(requestDto);
			if(status.getData() !=null) {
				status.setCode(SUCCESS);
				status.setMessage("Done");
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
	
	String generateSalesreturnNumber() throws ServerException{
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
		return "MSR-" + finYear + "-" + seqNoSixDigit;
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
		System.out.println("IdsList"+IdsList +" "+recordCount);
		status.setTotalCount(recordCount);
		status.setRecordCount(recordCount);
		if(IdsList.isEmpty()) {
			status.setCode(RECORD_NOT_FOUND);
			status.setData("Record not found"); 
			return status;
		}
		
		List<ResponseDto> responseDtoList = new ArrayList<ResponseDto>();
		
		Double totalReturnAmount = (double) 0;
		 responseDtoList = ltSalesreturnDao.getSalesReturn(IdsList);
		 
		 System.out.println("responseDtoList"+responseDtoList);
		
		Map<Long, LtSalesReturnDto> salesReturnHeaderDtoMap = new LinkedHashMap<Long, LtSalesReturnDto>();
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
				soHeaderDto.setSalesReturnDate(responseDto.getSalesReturnDate());
				soHeaderDto.setTotalSalesreturnAmount(totalReturnAmount);
				soHeaderDto.setOutletName(responseDto.getOutletName());
				soHeaderDto.setBeatName(responseDto.getBeatName());
				
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
		
		if (responseDtoList != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(orderDetailsDto);
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
		try {
			List<LtInvoiceDetailsResponseDto> ltInvoiceDetailsResponseDto = new ArrayList<LtInvoiceDetailsResponseDto>();
		ltInvoiceDetailsResponseDto = ltSalesreturnDao.getInvoiceDetails(requestDto);
		
		Map<String, LtInvoiceDetailsDto> invoiceDetailsHeaderDtoMap = new LinkedHashMap<String, LtInvoiceDetailsDto>();
		Map<String, List<LtInvoiceDetailsLineDto>> invoiceDetailsLineDtoMap = new LinkedHashMap<String, List<LtInvoiceDetailsLineDto>>();
		
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
				if(responseDto.getInvoiveNumber() !=null) {
					invoiceNo = responseDto.getInvoiveNumber();
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
				invoiceHeaderDto.setInvoiveNumber(responseDto.getInvoiveNumber());
				invoiceHeaderDto.setInvoiceDate(responseDto.getInvoiceDate());
				invoiceHeaderDto.setLocation(responseDto.getLocation());
				invoiceHeaderDto.setTotalAmount(responseDto.getTotalAmount());
				invoiceHeaderDto.setPriceListId(responseDto.getPriceListId());
				invoiceHeaderDto.setPriceListName(responseDto.getPriceListName());
				if(beatName!= null) {
				invoiceHeaderDto.setBeatName(beatName);;
				}				
				
				invoiceDetailsHeaderDtoMap.put(responseDto.getInvoiveNumber(), invoiceHeaderDto);

				List<LtInvoiceDetailsLineDto> invoiceLineDtoList = new ArrayList<LtInvoiceDetailsLineDto>();
				
				//List<LtInvoiceDetailsLineDto> invoiceLineDtoList = invoiceDetailsLineDtoMap.get(responseDto.getProductCode());
				if(invoiceDetailsLineDto.getProductCode() != null) {
					invoiceLineDtoList.add(invoiceDetailsLineDto);
				}
				invoiceDetailsLineDtoMap.put(responseDto.getInvoiveNumber(), invoiceLineDtoList);
				
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
		
		
		if (ltInvoiceDetailsResponseDto != null) {
			status.setCode(SUCCESS);
			status.setMessage("RECORD FOUND SUCCESSFULLY");
			status.setData(invoiceDetailsDto);
			return status;
		} 
			else {
			status.setCode(FAIL);
			status.setMessage("RECORD NOT FOUND");
			return status;
		}
		}catch(Exception e) {e.printStackTrace();}
		return status;
	}
}
