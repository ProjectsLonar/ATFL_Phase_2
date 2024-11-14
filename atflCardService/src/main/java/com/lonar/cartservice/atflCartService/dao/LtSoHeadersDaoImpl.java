package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.rmi.ServerException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.DistributorDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtOrderLineDataGt;
import com.lonar.cartservice.atflCartService.dto.QuantityCheck;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.UserDetailsDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtMastOutles;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtOrderCancellationReason;
import com.lonar.cartservice.atflCartService.model.LtSalesPersonLocation;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.LtSoLines;
import com.lonar.cartservice.atflCartService.repository.LtSalesPersonLocationRepository;
import com.lonar.cartservice.atflCartService.repository.LtSoLinesRepository;
import java.util.ArrayList;
import com.lonar.cartservice.atflCartService.service.ConsumeApiService;


@Repository
@PropertySource(value = "classpath:queries/cartMasterQueries.properties", ignoreResourceNotFound = true)
//@Transactional(propagation=Propagation.MANDATORY)
public class LtSoHeadersDaoImpl implements LtSoHeadersDao,CodeMaster {

	@Autowired
	private Environment env;
	
	@Autowired
	LtSoLinesRepository  ltSoLinesRepository;
	
	@Autowired
	LtSalesPersonLocationRepository ltSalesPersonLocationRepository;

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(LtSoHeadersDaoImpl.class);
	
	@Override
	public LtSoHeaders checkHeaderStatusIsDraft(String outletId) throws ServiceException, IOException {
		String query = env.getProperty("getStatusByOutletId");
		List<LtSoHeaders> ltSoHeaderslist = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtSoHeaders> ltSoHeaderslist = jdbcTemplate.query(query, new Object[] { outletId },
//				new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));

		try {
			ltSoHeaderslist = consumeApiService.consumeApi(query, 
					new Object[] { outletId }, LtSoHeaders.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!ltSoHeaderslist.isEmpty()) {
			return ltSoHeaderslist.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteLineDataByHeaderId(Long header_id) throws ServiceException, IOException {
		String query = env.getProperty("deleteLineDataByHeaderId");
		Object[] person = new Object[] { header_id };
		jdbcTemplate.update(query, person);
	}

	@Override
	public LtSoHeaders checkOrderStatus(String orderNumber) throws ServiceException, IOException {
		String query = env.getProperty("checkOrderStatus");
		List<LtSoHeaders> ltSoHeaderslist = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();
		
//		List<LtSoHeaders> ltSoHeaderslist = jdbcTemplate.query(query, new Object[] { orderNumber.toUpperCase() },
//				new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));

		try {
			ltSoHeaderslist = consumeApiService.consumeApi(query, 
					new Object[] { orderNumber.toUpperCase() }, 
					LtSoHeaders.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!ltSoHeaderslist.isEmpty()) {
			return ltSoHeaderslist.get(0);
		}
		return null;
	}

	@Override
	public List<ResponseDto> getAllOrderInprocess() throws ServiceException, IOException {
		String query = env.getProperty("getAllOrderInprocess");
		try {
			List<ResponseDto> headerDtolist = jdbcTemplate.query(query, new Object[] {},
					new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
			return headerDtolist;
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Long> getSoHeader(RequestDto requestDto) throws ServiceException, IOException {
		List<Long> headerIdslist = new ArrayList<>();
		try {
			System.out.println("in method getSoHeaderv3 dao line 113 ="+ LocalDateTime.now());
		String query = env.getProperty("getOrderHeaderV1");
       
		if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
		
 String headerId = null;
		if(requestDto.getHeaderId() !=null) {
			headerId = requestDto.getHeaderId().toString();
				}
		
		
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			
			String searchStr = requestDto.getSearchField().toUpperCase();
			
			String dayStr = "";
		    String yearStr = "";
			if(searchStr.contains("JAN")) {
				 String[] arr = searchStr.trim().split("JAN"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-01-"+dayStr+"%";
				
			}else if (searchStr.contains("FEB")) {
				 String[] arr = searchStr.trim().split("FEB"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-02-"+dayStr+"%";
				
			}else if (searchStr.contains("MAR")) {
				 String[] arr = searchStr.trim().split("MAR"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-03-"+dayStr+"%";
				
			}else if (searchStr.contains("APR")) {
				 String[] arr = searchStr.trim().split("APR"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-04-"+dayStr+"%";
				
			}else if (searchStr.contains("MAY")) {
				 String[] arr = searchStr.trim().split("MAY"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-05-"+dayStr+"%";
				
			}else if (searchStr.contains("JUN")) {
				 String[] arr = searchStr.trim().split("JUN"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-06-"+dayStr+"%";
				
			}else if (searchStr.contains("JUL")) {
				 String[] arr = searchStr.trim().split("JUL"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-07-"+dayStr+"%";
				
			}else if (searchStr.contains("AUG")) {
				 String[] arr = searchStr.trim().split("AUG"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-08-"+dayStr+"%";
				
			}else if (searchStr.contains("SEP")) {
				 String[] arr = searchStr.trim().split("SEP"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-09-"+dayStr+"%";
			}else if (searchStr.contains("OCT")) {
				 String[] arr = searchStr.trim().split("OCT"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-10-"+dayStr+"%";
				
			}else if (searchStr.contains("NOV")) {
				 String[] arr = searchStr.trim().split("NOV"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-11-"+dayStr+"%";
			}else if (searchStr.contains("DEC")) {
				 String[] arr = searchStr.trim().split("DEC"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-12-"+dayStr+"%";
			}else {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
		}
		System.out.println("searchField :: "+searchField);
		
		//List<Long> headerIdslist = new ArrayList<>();		
		//Long headerId =0l;
		System.out.println("Above getUserTypeAndDisId query call method at = "+LocalDateTime.now());
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		System.out.println("Below getUserTypeAndDisId query call method at = "+LocalDateTime.now());
		
		System.out.println("AlluserDetailsDto"+userDetailsDto);
		if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			//get userList by distributorID
			
	/*Keyur		if(userDetailsDto != null) {
				query = query + " and lsh.created_by not in ( " + requestDto.getUserId() + ")";
				System.out.println("New AlluserDetailsDto"+query);
			}
			query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			System.out.println("New123 AlluserDetailsDto"+query);
	*/		
			System.out.println("Above getUsersByDistributorId query call method at = "+LocalDateTime.now());
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			System.out.println("Below getUsersByDistributorId query call method at = "+LocalDateTime.now());
			
			//System.out.println("AlluserListDto"+userList);
			
			if(!userList.isEmpty() && userList != null) {
				query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					//	+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b where rownum BETWEEN ? AND ? ";
				        + ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
				//query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  where rownum BETWEEN ? AND ? ";
				query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}
			
			
			
			
			/*
			 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getStatus(),
			 * requestDto.getOrderNumber(),requestDto.getDistributorId(), headerId,
			 * searchField, requestDto.getOutletId(), requestDto.getLimit(),
			 * requestDto.getOffset());
			 */
			
//			headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
//					requestDto.getOrderNumber(),requestDto.getDistributorId(),  searchField,
//					requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset());

/*			headerIdslist = jdbcTemplate.queryForList(query, Long.class, 
					requestDto.getDistributorId(),
					requestDto.getStatus(),
					requestDto.getOrderNumber(),
					searchField, requestDto.getOutletId(),
					requestDto.getLimit(), requestDto.getOffset());
*/					
			ConsumeApiService consumeApiService = new ConsumeApiService();

			try {
			List<RequestDto> headerIds = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { 
								requestDto.getDistributorId(),
								requestDto.getStatus(),
								requestDto.getOrderNumber(),
								searchField, requestDto.getOutletId(),
								requestDto.getLimit(), requestDto.getOffset()
						}, 
						RequestDto.class);
				for(int i=0; i<headerIds.size(); i++) {
					headerIdslist.add(headerIds.get(i).getHeaderId());	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			//System.out.println("headerIdslist in Dao"+headerIdslist);
			return headerIdslist;
			
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			//String positionId = getPositionIdByUserId(requestDto.getUserId());
			//List<Long> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId()); //comment on 12-March-24 vaibhav
			System.out.println("Above getOutletIdsByPositionId query call method at = "+LocalDateTime.now());
			List<String> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
			System.out.println("Below getOutletIdsByPositionId query call method at = "+LocalDateTime.now());
			
			if(requestDto.getOutletId()!= null) {
				query = query + " and lsh.outlet_id in (" + "'"+requestDto.getOutletId() +"'"+
						 ")  ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
						
			if(!outletList.isEmpty() && outletList != null) {
				//query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//		+ " ) ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";

				//				String outList = null;   comment on 13-march-2024 vaibhav 320-333
//				String newList = null;
//				for(int i=1; i< outletList.size(); i++) { 
//					if(i!=outletList.size()) {
//					 outList= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'"+",";
//					 //outList.charAt(outList.length() - 1);
//					 outList.substring(0, outList.length() - 1);
//					 //outList.replace(",", "");
//					 System.out.println("Issue for outList =\n"+newList);}
//					if(i==outletList.size()-1) {
//						String outList1= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'";
//						 System.out.print("2nd outList =\n"+outList+outList1);}
//					
//				}
				if(requestDto.getOutletId()!= null) {
				outletList.add(requestDto.getOutletId());
				}
//				System.out.println(outletList);
				String outletListString = outletList.stream()
                        .map(id -> "'" + id + "'")
                        .collect(Collectors.joining(", "));
				System.out.println("In if of Sales Person");
				query = query + " and lsh.outlet_id in (" + outletListString +
				 ")  ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				
			//	 query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
					//	+ " ) ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
			//	+  ") ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
//				query = query +" ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
			//	query = query +" ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ?";
				query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			}}
			
//			System.out.println("Issue for query ="+query);
					    
/*			headerIdslist = jdbcTemplate.queryForList(query, Long.class, 
					requestDto.getDistributorId(),
					requestDto.getStatus(),
					requestDto.getOrderNumber(),
					searchField,
					requestDto.getLimit(), requestDto.getOffset());
*/			
			ConsumeApiService consumeApiService = new ConsumeApiService();
		//	System.out.println("input For sales requestDto "+ requestDto);
			try {
			List<RequestDto> headerIds = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { 
								requestDto.getDistributorId(),
								requestDto.getStatus(),
								requestDto.getOrderNumber(),
								searchField,
								requestDto.getLimit(), requestDto.getOffset()
						}, 
						RequestDto.class);
				for(int i=0; i<headerIds.size(); i++) {
					headerIdslist.add(headerIds.get(i).getHeaderId());	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println(requestDto.getStatus());
			//System.out.println(requestDto.getOrderNumber());

			//System.out.println(requestDto.getDistributorId());

			//System.out.println(searchField);

			//System.out.println(requestDto.getLimit());
			
			//System.out.println(requestDto.getOffset() +"\n"+ requestDto);

		//	System.out.println("Sales outletList query" + query);
			
		//	System.out.println("headerIdslist for Sales query = "+headerIdslist);
			/*
			 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getStatus(), requestDto.getOrderNumber(),
			 * requestDto.getDistributorId(), headerId, searchField, requestDto.getLimit(),
			 * requestDto.getOffset());
			 */
			return headerIdslist;
			
		}
		
		else {
		//	query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
			query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			/*
			 * if(requestDto.getHeaderId() !=null) { headerId = requestDto.getHeaderId();
			 * }else { headerId = null; }
			 */
			
			System.out.println(requestDto.getHeaderId()); 
			System.out.println(query);  
			
			/*
			 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getDistributorId(),requestDto.getStatus(),
			 * requestDto.getOrderNumber(), searchField
			 * ,headerId,requestDto.getOutletId(),requestDto.getLimit(),
			 * requestDto.getOffset() );
			 */
           
           
           System.out.println("Status: " + requestDto.getStatus() + " (Type: " + (requestDto.getStatus() != null ? requestDto.getStatus().getClass().getName() : "null") + ")");
           System.out.println("Order Number: " + requestDto.getOrderNumber() + " (Type: " + (requestDto.getOrderNumber() != null ? requestDto.getOrderNumber().getClass().getName() : "null") + ")");
           System.out.println("Distributor ID: " + requestDto.getDistributorId() + " (Type: " + (requestDto.getDistributorId() != null ? requestDto.getDistributorId().getClass().getName() : "null") + ")");
           System.out.println("Search Field: " + searchField + " (Type: " + (searchField != null ? searchField.getClass().getName() : "null") + ")");
           //System.out.println("Limit: " + requestDto.getLimit() + " (Type: " + (requestDto.getLimit() != null ? requestDto.getLimit().getClass().getName() : "null") + ")");
           //System.out.println("Offset: " + requestDto.getOffset() + " (Type: " + (requestDto.getOffset() != null ? requestDto.getOffset().getClass().getName() : "null") + ")");
           System.out.println("Request DTO: " + requestDto + " (Type: " + requestDto.getClass().getName() + ")");


			System.out.println("Sales outletList query" + query);
			
/*			headerIdslist = jdbcTemplate.queryForList(query, Long.class,
					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
					searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
					);
*/
			
			ConsumeApiService consumeApiService = new ConsumeApiService();

			try {
			List<RequestDto> headerIds = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { 
								requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
								searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
						}, 
						RequestDto.class);
				for(int i=0; i<headerIds.size(); i++) {
					headerIdslist.add(headerIds.get(i).getHeaderId());	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println("headerIdslist for Other user query = "+headerIdslist);

			/*
			 * headerIdslist = jdbcTemplate.query(query, new Object[] { }, new
			 * BeanPropertyRowMapper<Long>(Long.class));
			 */
           System.out.println("out method getSoHeaderV3 dao ="+ LocalDateTime.now());
System.out.println("headerIdsSSSS "+headerIdslist);
			return headerIdslist;
		}}
		catch(Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}return headerIdslist;
	}

	
	@Override
	public List<ResponseDto> getSoHeader11(RequestDto requestDto) throws ServiceException, IOException {
		try {
		String query = env.getProperty("getOrderHeaderV11");
       
		if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
		
String headerId = null;
		if(requestDto.getHeaderId() !=null) {
			headerId = requestDto.getHeaderId().toString();
				}
		
		
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			
			String searchStr = requestDto.getSearchField().toUpperCase();
			
			String dayStr = "";
		    String yearStr = "";
			if(searchStr.contains("JAN")) {
				 String[] arr = searchStr.trim().split("JAN");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-01-"+dayStr+"%";
				
			}else if (searchStr.contains("FEB")) {
				 String[] arr = searchStr.trim().split("FEB");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-02-"+dayStr+"%";
				
			}else if (searchStr.contains("MAR")) {
				 String[] arr = searchStr.trim().split("MAR");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-03-"+dayStr+"%";
				
			}else if (searchStr.contains("APR")) {
				 String[] arr = searchStr.trim().split("APR");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-04-"+dayStr+"%";
				
			}else if (searchStr.contains("MAY")) {
				 String[] arr = searchStr.trim().split("MAY");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-05-"+dayStr+"%";
				
			}else if (searchStr.contains("JUN")) {
				 String[] arr = searchStr.trim().split("JUN");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-06-"+dayStr+"%";
				
			}else if (searchStr.contains("JUL")) {
				 String[] arr = searchStr.trim().split("JUL");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-07-"+dayStr+"%";
				
			}else if (searchStr.contains("AUG")) {
				 String[] arr = searchStr.trim().split("AUG");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-08-"+dayStr+"%";
				
			}else if (searchStr.contains("SEP")) {
				 String[] arr = searchStr.trim().split("SEP");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-09-"+dayStr+"%";
			}else if (searchStr.contains("OCT")) {
				 String[] arr = searchStr.trim().split("OCT");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-10-"+dayStr+"%";
				
			}else if (searchStr.contains("NOV")) {
				 String[] arr = searchStr.trim().split("NOV");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-11-"+dayStr+"%";
			}else if (searchStr.contains("DEC")) {
				 String[] arr = searchStr.trim().split("DEC");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-12-"+dayStr+"%";
			}else {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
		}
		System.out.println("searchField :: "+searchField);
		
		List<ResponseDto> headerIdslist = null;		
		//Long headerId =0l;
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		
		System.out.println("AlluserDetailsDto"+userDetailsDto);
		if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			//get userList by distributorID
			
	/*Keyur		if(userDetailsDto != null) {
				query = query + " and lsh.created_by not in ( " + requestDto.getUserId() + ")";
				System.out.println("New AlluserDetailsDto"+query);
			}
			query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			System.out.println("New123 AlluserDetailsDto"+query);
	*/		
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			
			System.out.println("AlluserListDto"+userList);
			
			if(!userList.isEmpty() && userList != null) {
				query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
					//	+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b where rownum BETWEEN ? AND ? ";
				        + ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.CDATE desc  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
				//query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  where rownum BETWEEN ? AND ? ";
				query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.CDATE desc  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}
			
			
			
			
			/*
			 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getStatus(),
			 * requestDto.getOrderNumber(),requestDto.getDistributorId(), headerId,
			 * searchField, requestDto.getOutletId(), requestDto.getLimit(),
			 * requestDto.getOffset());
			 */
			
			headerIdslist = jdbcTemplate.query(query, new Object[] {
					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
					searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
           },new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
 
			System.out.println("headerIdslist in Dao"+headerIdslist);
			return headerIdslist;
			
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			//String positionId = getPositionIdByUserId(requestDto.getUserId());
			//List<Long> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId()); //comment on 12-March-24 vaibhav
			List<String> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
			
			if(!outletList.isEmpty() && outletList != null) {
				//query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//		+ " ) ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
 
				//				String outList = null;   comment on 13-march-2024 vaibhav 320-333
//				String newList = null;
//				for(int i=1; i< outletList.size(); i++) {
//					if(i!=outletList.size()) {
//					 outList= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'"+",";
//					 //outList.charAt(outList.length() - 1);
//					 outList.substring(0, outList.length() - 1);
//					 //outList.replace(",", "");
//					 System.out.println("Issue for outList =\n"+newList);}
//					if(i==outletList.size()-1) {
//						String outList1= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'";
//						 System.out.print("2nd outList =\n"+outList+outList1);}
//					
//				}
				
				query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
					//	+ " ) ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
				+  ") ) a order by a.status_o, a.CDATE desc  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
//				query = query +" ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
			//	query = query +" ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ?";
				query = query +" ) a order by a.status_o, a.CDATE desc  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			}
			
			System.out.print("Issue for query ="+query);
			
			headerIdslist = jdbcTemplate.query(query, new Object[] {
					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
					searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
           },new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
			
			
			/*
			 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getStatus(), requestDto.getOrderNumber(),
			 * requestDto.getDistributorId(), headerId, searchField, requestDto.getLimit(),
			 * requestDto.getOffset());
			 */
			return headerIdslist;
			
		}else {
		//	query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
			query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.CDATE desc OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			/*
			 * if(requestDto.getHeaderId() !=null) { headerId = requestDto.getHeaderId();
			 * }else { headerId = null; }
			 */
			
			System.out.println(requestDto.getHeaderId());
			System.out.println(query);  
			
			/*
			 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getDistributorId(),requestDto.getStatus(),
			 * requestDto.getOrderNumber(), searchField
			 * ,headerId,requestDto.getOutletId(),requestDto.getLimit(),
			 * requestDto.getOffset() );
			 */
           
           headerIdslist = jdbcTemplate.query(query, new Object[] {
					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
					searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
           },new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
 
 
			/*
			 * headerIdslist = jdbcTemplate.query(query, new Object[] { }, new
			 * BeanPropertyRowMapper<Long>(Long.class));
			 */
System.out.println("headerIdsSSSS "+headerIdslist);
System.out.println("Query is "+query);
			return headerIdslist;
		}}catch(Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}return null;
	}
	
	
	@Override
	public List<ResponseDto> getSoHeader111(RequestDto requestDto) throws ServiceException, IOException {
		//System.out.println("In getSoHeader111 dao method at = "+LocalDateTime.now());
		try {
		String query = env.getProperty("getOrderHeaderV13");
		String query1 = env.getProperty("getOrderHeaderV133");
		String query11 = env.getProperty("getOrderHeaderV13AreaHead");
       
		if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
		
		String headerId = null;
		if(requestDto.getHeaderId() !=null) {
			headerId = requestDto.getHeaderId().toString();
				}
		
		
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			
			String searchStr = requestDto.getSearchField().toUpperCase();
			
			String dayStr = "";
		    String yearStr = "";
			if(searchStr.contains("JAN")) {
				 String[] arr = searchStr.trim().split("JAN");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-01-"+dayStr+"%";
				
			}else if (searchStr.contains("FEB")) {
				 String[] arr = searchStr.trim().split("FEB");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-02-"+dayStr+"%";
				
			}else if (searchStr.contains("MAR")) {
				 String[] arr = searchStr.trim().split("MAR");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-03-"+dayStr+"%";
				
			}else if (searchStr.contains("APR")) {
				 String[] arr = searchStr.trim().split("APR");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-04-"+dayStr+"%";
				
			}else if (searchStr.contains("MAY")) {
				 String[] arr = searchStr.trim().split("MAY");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-05-"+dayStr+"%";
				
			}else if (searchStr.contains("JUN")) {
				 String[] arr = searchStr.trim().split("JUN");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-06-"+dayStr+"%";
				
			}else if (searchStr.contains("JUL")) {
				 String[] arr = searchStr.trim().split("JUL");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-07-"+dayStr+"%";
				
			}else if (searchStr.contains("AUG")) {
				 String[] arr = searchStr.trim().split("AUG");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-08-"+dayStr+"%";
				
			}else if (searchStr.contains("SEP")) {
				 String[] arr = searchStr.trim().split("SEP");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-09-"+dayStr+"%";
			}else if (searchStr.contains("OCT")) {
				 String[] arr = searchStr.trim().split("OCT");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-10-"+dayStr+"%";
				
			}else if (searchStr.contains("NOV")) {
				 String[] arr = searchStr.trim().split("NOV");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-11-"+dayStr+"%";
			}else if (searchStr.contains("DEC")) {
				 String[] arr = searchStr.trim().split("DEC");
			        for (String st : arr)
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-12-"+dayStr+"%";
			}else {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
		}
		System.out.println("searchField :: "+searchField);
		
		List<ResponseDto> headerIdslist = new ArrayList<>();;	
		ConsumeApiService consumeApiService = new ConsumeApiService();

		//Long headerId =0l;
		//System.out.println("Above getUserTypeAndDisId dao method at = "+LocalDateTime.now());
		//UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getLoginId());
		//String userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		//System.out.println("Below getUserTypeAndDisId dao method at = "+LocalDateTime.now());
		
//		System.out.println("userDetailsDto = "+userDetailsDto);
		
		if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			System.out.println("In distributor");		
			//System.out.println("Above getUsersByDistributorId dao method at = "+LocalDateTime.now());
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			//System.out.println("Below getUsersByDistributorId dao method at = "+LocalDateTime.now());
			System.out.println("userList = "+userList);
			
			if(!userList.isEmpty() && userList != null) {
				System.out.println("In if distributor");
				query1 = query1 + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
				        + ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx'))  order by CDATE desc OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
				System.out.println("In else distributor");
				//query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  where rownum BETWEEN ? AND ? ";
				query1 = query1 +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx'))  order by status_o, CDATE desc  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY  ";
			}
			
			System.out.println("Query = " +query);
			
			//System.out.println("Above query dao method at = "+LocalDateTime.now());
//			headerIdslist = jdbcTemplate.query(query, new Object[] {
//					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
//					searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
//           },new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
//			//System.out.println("Below query dao method at = "+LocalDateTime.now());
			
			try {
				headerIdslist = consumeApiService.consumeApiWithRequestBody(query1, 
						new Object[] { requestDto.getDistributorId(), requestDto.getLoginId(), requestDto.getStatus(), requestDto.getOrderNumber(),
								searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset() }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("headerIdslist in Dao = "+headerIdslist);
			return headerIdslist;
			
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			System.out.println("In Sales Person");
			List<String> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
			
			if(!outletList.isEmpty() && outletList != null) {
				String outletListString = outletList.stream()
                        .map(id -> "'" + id + "'")
                        .collect(Collectors.joining(", "));
				System.out.println("In if of Sales Person");
				query1 = query1 + " and lsh.outlet_id in (" + outletListString +
				 ")  order by CDATE desc OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
			}else {
				System.out.println("In else of Sales Person");
				query1 = query1 +"  order by status_o, CDATE desc  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			}
			
//			System.out.print("Query = "+query);
			//System.out.println("Above query dao method at = "+LocalDateTime.now());
 
//			headerIdslist = jdbcTemplate.query(query, new Object[] {
//					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
//					searchField,requestDto.getLimit(), requestDto.getOffset()
//           },new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
			//System.out.println("Below query dao method at = "+LocalDateTime.now());
 
			try {
				headerIdslist = consumeApiService.consumeApiWithRequestBody(query1, 
						new Object[] { requestDto.getDistributorId(),requestDto.getLoginId(), requestDto.getStatus(), requestDto.getOrderNumber(),
								searchField,requestDto.getLimit(), requestDto.getOffset() }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return headerIdslist;
			
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase("AREAHEAD")) {
			System.out.println("in AREAHEAD else");
		
			List<String> distId= getDistributorIdFromAreaHead(userDetailsDto.getEmployeeCode());
			String distIdList = null;
			if(!distId.isEmpty() && distId != null) {
				      distIdList = distId.stream()
		                .map(id -> "'" + id + "'")
		                .collect(Collectors.joining(", "));
				
			}
//			System.out.println("distId are= "+distId);
			query11 = query11 + "and COALESCE(lsh.outlet_id ,'xx') =  COALESCE(? ,COALESCE(lsh.outlet_id,'xx')) ORDER BY CDATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//			query11 = query11 + "and COALESCE(lsh.outlet_id ,'xx') =  COALESCE(? ,COALESCE(lsh.outlet_id,'xx')) ORDER BY STATUS_O, CDATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
					
			//			System.out.println("query11 == "+query11);
//			System.out.println("requestDto data is == "+requestDto+ searchField);
			try {
				headerIdslist = consumeApiService.consumeApiWithRequestBody(query11, 
						new Object[] { distIdList, requestDto.getStatus(), requestDto.getOrderNumber(),
								searchField, requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset() }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return headerIdslist;
		}
		
		else {
			System.out.println("in else");
			query = query + "and COALESCE(lsh.outlet_id ,'xx') =  COALESCE(? ,COALESCE(lsh.outlet_id,'xx')) ORDER BY STATUS_O, CDATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			
////			System.out.println(requestDto.getHeaderId());
//			System.out.println(query);  
 
          // System.out.println("Above else dao query at = "+LocalDateTime.now());
           String result = String.join("  ", requestDto.getDistributorId(), requestDto.getStatus(),requestDto.getOrderNumber(),searchField,requestDto.getOutletId());
//           System.out.println("result = "+result);
//           System.out.println(requestDto.getLimit());
//           System.out.println(requestDto.getOffset());
		//	System.out.println("Above query dao method at = "+LocalDateTime.now());
 
//           headerIdslist = jdbcTemplate.query(query, new Object[] {
//					requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
//					searchField,requestDto.getOutletId(),requestDto.getLimit(),requestDto.getOffset()
//           },new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
		//	System.out.println("Below query dao method at = "+LocalDateTime.now());
 
           try {
        	   
        	   headerIdslist = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
								searchField,requestDto.getOutletId(),requestDto.getLimit(),requestDto.getOffset() }, 
						ResponseDto.class);
//			for(int i=0; i<headerIds.size(); i++) {
//				headerIdslist.add(headerIds.get(i));
//			}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
           
//           System.out.println("headerIdslist below = "+headerIdslist);
         //  System.out.println("Below else dao query at = "+LocalDateTime.now());
//           System.out.println("headerIdsSSSS "+headerIdslist);
//           System.out.println("Query is "+query);
//        //   System.out.println("Exit from dao method at = "+LocalDateTime.now());
           return headerIdslist;
		
		}
		
//		 NamedParameterJdbcTemplate namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
//         
//         MapSqlParameterSource parameters = new MapSqlParameterSource();
//         
//         parameters.addValue("distributorId", requestDto.getDistributorId());
//         parameters.addValue("status", requestDto.getStatus());
//         parameters.addValue("orderNumber", requestDto.getOrderNumber());
//         parameters.addValue("searchField", searchField);
//         parameters.addValue("outletId", requestDto.getOutletId());
//         parameters.addValue("limit", requestDto.getLimit());
//         parameters.addValue("offset", requestDto.getOffset());
//         
//         System.out.println("headerIdslist = "+headerIdslist);
//         
//         headerIdslist = namedParameterJdbcTemplate.query(query, parameters, new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
//         return headerIdslist;
		}catch(Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}return null;
	}
	
	@Override
	public List<ResponseDto> getOrderV1(List<Long> headerIdList) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getOrderLineV1");
			query = query + "  and lsh.header_id IN ( " + headerIdList.toString().replace("[", "").replace("]", "")
					+ " ) ) a order by a.status_o, a.cdate desc, a.header_id ";
			List<ResponseDto> headerDtolist = jdbcTemplate.query(query, new Object[] {},
					new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
			
			System.out.println("in method getAllPendingOrders service line 425 ="+ new Date());
			return headerDtolist;
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ResponseDto> getOrderV2(List<Long> headerIdList) throws ServiceException, IOException {
		try {
			System.out.println("in method getOrderV3 dao = "+ LocalDateTime.now());
			String query = env.getProperty("getOrderLineV1");
		//	System.out.print("headerIdList =" +headerIdList);
			if(headerIdList.size()>0) {
			//query = query + "  and lsh.header_id IN ( 164) ) a order by a.status_o, a.cdate desc, a.header_id ";
			query = query + "  and lsh.header_id IN ( " + headerIdList.toString().replace("[", "").replace("]", "")
				//	+ " ) ) a order by a.status_o, a.cdate desc, a.header_id "; comment by milind sir on 16-Oct2024 for shuffle issue
					+ " ) ) a order by a.header_id, a.line_id ";
			}
			else {
				System.out.println("in else");
			    query = query + "  and lsh.header_id IN (NULL) ) a order by a.status_o, a.cdate desc, a.header_id ";
				//System.out.println("Concated query is "+query);
 
			}
/*			List<ResponseDto> headerDtolist = jdbcTemplate.query(query, new Object[] {},
					new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
*/
			ConsumeApiService consumeApiService = new ConsumeApiService();
			List<ResponseDto> headerDtolist = new ArrayList<>();

			try {
				 headerDtolist = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("out method getOrderV2 dao = "+ LocalDateTime.now());
			return headerDtolist;
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Long getSequancesValue() throws ServiceException, IOException {
		Long seq;
		String sql = env.getProperty("genrate_order_number_seq");
		seq = jdbcTemplate.queryForObject(sql, new Object[] {}, Long.class);
		return seq;
	}

	@Override
	public String getDistributorCode(String outletId) throws ServiceException, IOException {
		String distCode;
		String sql = env.getProperty("getDistributorCode");
		distCode = jdbcTemplate.queryForObject(sql, new Object[] { outletId }, String.class);
		return distCode;
	}

	@Override
	public List<LtMastUsers> getActiveDistUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException {
		String query = env.getProperty("getActiveDistUsersFromHeaderId");
		List<LtMastUsers> userList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		
		try {
			userList = consumeApiService.consumeApi(query, 
					new Object[] { headerId, orderNumber }, 
					LtMastUsers.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userList;
	}

	@Override
	public List<LtMastUsers> getActiveSalesUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException {
		String query = env.getProperty("getActiveSalesUsersFromHeaderId"); 
		List<LtMastUsers> userList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		
		try {
			userList = consumeApiService.consumeApi(query, 
					new Object[] { headerId, orderNumber }, 
					LtMastUsers.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userList;
	}

	@Override
	public Long getRecordCount(RequestDto requestDto) throws ServiceException, IOException {
		
		System.out.println("in method getRecordCountV3 dao = "+ LocalDateTime.now());
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			
			String searchStr = requestDto.getSearchField().toUpperCase();
			
			String dayStr = "";
		    String yearStr = "";
			if(searchStr.contains("JAN")) {
				 String[] arr = searchStr.trim().split("JAN"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-01-"+dayStr+"%";
				
			}else if (searchStr.contains("FEB")) {
				 String[] arr = searchStr.trim().split("FEB"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-02-"+dayStr+"%";
				
			}else if (searchStr.contains("MAR")) {
				 String[] arr = searchStr.trim().split("MAR"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-03-"+dayStr+"%";
				
			}else if (searchStr.contains("APR")) {
				 String[] arr = searchStr.trim().split("APR"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-04-"+dayStr+"%";
				
			}else if (searchStr.contains("MAY")) {
				 String[] arr = searchStr.trim().split("MAY"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-05-"+dayStr+"%";
				
			}else if (searchStr.contains("JUN")) {
				 String[] arr = searchStr.trim().split("JUN"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-06-"+dayStr+"%";
				
			}else if (searchStr.contains("JUL")) {
				 String[] arr = searchStr.trim().split("JUL"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-07-"+dayStr+"%";
				
			}else if (searchStr.contains("AUG")) {
				 String[] arr = searchStr.trim().split("AUG"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-08-"+dayStr+"%";
				
			}else if (searchStr.contains("SEP")) {
				 String[] arr = searchStr.trim().split("SEP"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-09-"+dayStr+"%";
			}else if (searchStr.contains("OCT")) {
				 String[] arr = searchStr.trim().split("OCT"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-10-"+dayStr+"%";
				
			}else if (searchStr.contains("NOV")) {
				 String[] arr = searchStr.trim().split("NOV"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-11-"+dayStr+"%";
			}else if (searchStr.contains("DEC")) {
				 String[] arr = searchStr.trim().split("DEC"); 
			        for (String st : arr) 
			        {
			        	 if(st.trim().length() == 2) {
			        		 dayStr = st.trim();
			        	 }else {
			        		 yearStr = st.trim();
			        	 }
			        }
				searchField = "%"+yearStr+"-12-"+dayStr+"%";
			}else {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
			System.out.println("searchField :: "+searchField);
		}
		
		Long recordCount = 0L;
		
		String query = env.getProperty("getOrderHeaderCount");
		
		String headerId = null;
		if(requestDto.getHeaderId() !=null) {
			headerId = requestDto.getHeaderId().toString(); 
		}
		System.out.println("Above getUserTypeAndDisId query call method at = "+LocalDateTime.now());
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		System.out.println("Below getUserTypeAndDisId query call method at = "+LocalDateTime.now());
		
		if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			//get userList by distributorID
			System.out.println("Above getUsersByDistributorId query call method at = "+LocalDateTime.now());
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			System.out.println("Below getUsersByDistributorId query call method at = "+LocalDateTime.now());
			
			if(!userList.isEmpty() && userList != null) {
				query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") and COALESCE (lsh.outlet_id, 'xx')= COALESCE( ? ,  COALESCE (lsh.outlet_id, 'xx'))";
			}else {
				query = query +" COALESCE (lsh.outlet_id, 'xx')= COALESCE( ? ,  COALESCE (lsh.outlet_id, 'xx')) ";
			}
			
			/*
			 * recordCount = jdbcTemplate.queryForObject(query, new Object[] {
			 * requestDto.getStatus(), requestDto.getOrderNumber(),
			 * requestDto.getDistributorId(), headerId, searchField,requestDto.getOutletId()
			 * }, Long.class);
			 */
			
//			recordCount = jdbcTemplate.queryForObject(query, new Object[] { 
//					requestDto.getStatus(), requestDto.getOrderNumber(),  
//					requestDto.getDistributorId(), 
//					searchField,requestDto.getOutletId()
//			}, Long.class);
			ConsumeApiService consumeApiService = new ConsumeApiService();

			try {
				recordCount = consumeApiService.consumeApiForCountWithRequestBody(query, 
						new Object[] { 
								requestDto.getStatus(), requestDto.getOrderNumber(),  
								requestDto.getDistributorId(), 
								searchField,requestDto.getOutletId()
						});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return recordCount;
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
	        System.out.println("Above getOutletIdsByPositionId query call method at = "+LocalDateTime.now());
			List<String> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
	        System.out.println("Above getOutletIdsByPositionId query call method at = "+LocalDateTime.now());
			if(!outletList.isEmpty() && outletList != null) {
				//query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//		+ ") ";
				String outletListString = outletList.stream()
                        .map(id -> "'" + id + "'")
                        .collect(Collectors.joining(", "));
				System.out.println("In if of Sales Person Count");
				query = query + " and lsh.outlet_id in (" + outletListString +")";
				
			}else {
				//query = query +" and COALESCE(lsh.outlet_id ,-99) =  COALESCE( ? ,COALESCE(lsh.outlet_id),-99) ";
			}
			
			/*
			 * recordCount = jdbcTemplate.queryForObject(query, new Object[] {
			 * requestDto.getStatus(), requestDto.getOrderNumber(),
			 * requestDto.getDistributorId(), headerId, searchField }, Long.class);
			 */
			
//			recordCount = jdbcTemplate.queryForObject(query, new Object[] { 
//					requestDto.getStatus(), requestDto.getOrderNumber(),  
//					requestDto.getDistributorId(), 
//					searchField
//			}, Long.class);
			
			ConsumeApiService consumeApiService = new ConsumeApiService();
			System.out.println("getOrderV2 query for count = " + query);
			try {
				recordCount = consumeApiService.consumeApiForCountWithRequestBody(query, 
						new Object[] { 
								requestDto.getStatus(), requestDto.getOrderNumber(),  
								requestDto.getDistributorId(), 
								searchField
						});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return recordCount;
		}else {
			query = query +"and  COALESCE (lsh.outlet_id, 'xx')= COALESCE( ? ,  COALESCE (lsh.outlet_id, 'xx'))";
			
//			recordCount = jdbcTemplate.queryForObject(query, new Object[] { 
//					requestDto.getStatus(), requestDto.getOrderNumber(),  
//					requestDto.getDistributorId(), 
//					searchField,requestDto.getOutletId()
//			}, Long.class);
			
			ConsumeApiService consumeApiService = new ConsumeApiService();

			try {
				recordCount = consumeApiService.consumeApiForCountWithRequestBody(query, 
						new Object[] { 
								requestDto.getStatus(), requestDto.getOrderNumber(),  
								requestDto.getDistributorId(), 
								searchField,requestDto.getOutletId()
						});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("out method getRecordCountV3 dao ="+ LocalDateTime.now());
			/*
			 * recordCount = jdbcTemplate.queryForObject(query, new Object[] {
			 * requestDto.getStatus(), requestDto.getOrderNumber(),
			 * requestDto.getDistributorId(), headerId, searchField,requestDto.getOutletId()
			 * }, Long.class);
			 */
			return recordCount;
			//return 2L;
		}

	}

	@Override
	public DistributorDetailsDto getDistributorDetailsByOutletId(String outletId) throws ServiceException, IOException {
		String query = env.getProperty("getDistributorDetailsByOutletId");
		List<DistributorDetailsDto> distributorDetailsDtoList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<DistributorDetailsDto> distributorDetailsDtoList = jdbcTemplate.query(query, new Object[] { outletId },
//				new BeanPropertyRowMapper<DistributorDetailsDto>(DistributorDetailsDto.class));

		try {
			distributorDetailsDtoList = consumeApiService.consumeApi(query, 
					new Object[] { outletId }, 
					DistributorDetailsDto.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!distributorDetailsDtoList.isEmpty()) {
			return distributorDetailsDtoList.get(0);
		}
		return null;
	}

	@Override
	public void updateDistributorSequance(String distId, Long distSequance) throws ServiceException, IOException {
		String query = env.getProperty("updateDistributorSequance");
		this.jdbcTemplate.update(query, distSequance, distId);
	}

	@Override
	public String getMobileNumber(Long userId) throws ServiceException, IOException {
		//String mobileNumber;
		String sql = env.getProperty("getMobileNumber");
		//mobileNumber = jdbcTemplate.queryForObject(sql, new Object[] { userId }, String.class);
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String mobileNumber = consumeApiService.consumeApiForString(sql, 
					new Object[] { userId });
			return mobileNumber;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public UserDetailsDto getUserTypeAndDisId(Long userId) throws ServiceException {
		String query = env.getProperty("getUserTypeAndDisId");
		List<UserDetailsDto> list = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();
//		List<UserDetailsDto> list = jdbcTemplate.query(query, new Object[] { userId },
//				new BeanPropertyRowMapper<UserDetailsDto>(UserDetailsDto.class));
		
		try {
			list = consumeApiService.consumeApi(query, 
					new Object[] { userId }, UserDetailsDto.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//if (!list.isEmpty()) {
		if(list.size()>0) {
			return list.get(0);
			}
		else {
			return null;
		}
//		if(list!= null) {
//			return (UserDetailsDto) list;
//		}return null;
	}
	
	private List<Long> getUsersByDistributorId(String distributorId) throws ServiceException {
		String query = env.getProperty("getUsersByDistributorId");
		List<Long> userIdList = new ArrayList<>();
		//List<UserDetailsDto> list = new ArrayList<>();

		ConsumeApiService consumeApiService =  new ConsumeApiService();
		//List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
		
		try {
			List<UserDetailsDto> list = consumeApiService.consumeApi(query, 
					new Object[] { distributorId }, 
					UserDetailsDto.class);
			for(int i=0; i<list.size(); i++ ) {
				userIdList.add(list.get(i).getUserId());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}
	
	@Override
	public String getPositionIdByUserId(Long userId) throws ServiceException, IOException {
		String positionId;
		String sql = env.getProperty("getPositionIdByUserId");
		positionId = jdbcTemplate.queryForObject(sql, new Object[] { userId }, String.class);
		return positionId;
	}
	
	private List<String> getOutletIdsByPositionId(String positionId) throws ServiceException {
		String query = env.getProperty("getOutletIdsByPositionId");
		ConsumeApiService consumeApiService = new ConsumeApiService();
		List<String> userIdList = new ArrayList<>();
		
//		List<String> userIdList = jdbcTemplate.queryForList(query, String.class, positionId);
		
		try {
			List<ResponseDto> list = consumeApiService.consumeApi(query, 
					new Object[] { positionId }, 
					ResponseDto.class);
			
			
			for(int i =0; i < list.size();i++) {
				
				userIdList.add(list.get(i).getOutletId());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		if (!userIdList.isEmpty())
			return userIdList;                //userIdList;
		else
			return null;
	}

	@Override
	public int deleteLineDataByHeaderIdAndReturnStatus(Long heaaderId) throws ServiceException, IOException {
		String query = env.getProperty("deleteLineDataByHeaderId");
		Object[] person = new Object[] { heaaderId };
		int status = jdbcTemplate.update(query, person);
//		ConsumeApiService consumeApiService = new ConsumeApiService();
//		int i=0;
//		try {
//			Long status = consumeApiService.consumeApiForCount(query, 
//					new Object[] { person });
//			i= status.intValue();
//			return i;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return status;
	}
	
	@Override
	public int insertLine(String query) throws ServiceException, IOException {
		  return jdbcTemplate.update(query);
	}
	
	@Override
	public List<LtOrderCancellationReason> getOrderCancellationReport() throws ServiceException, IOException{
		String query = env.getProperty("getOrderCancellationReport");
		

		List<LtOrderCancellationReason> list = jdbcTemplate.query(query,
				new Object[] { },
				new BeanPropertyRowMapper<LtOrderCancellationReason>(LtOrderCancellationReason.class));

		if (!list.isEmpty()) {
			return list;
		}

		return null;
	}
	
	@Override
	public List<LtMastUsers> getActiveAreaHeadeUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException {
		String query = env.getProperty("getActiveAreaHeadUsersFromHeaderId");
		System.out.println("This is areaHead User query in Dao \n" + query+ "\n"+ headerId+"\n"+ orderNumber);
		List<LtMastUsers> userList =  new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
//		System.out.println("This is areaHead User List in Dao " + userList);
		
		try {
			userList = consumeApiService.consumeApi(query, 
					new Object[] {  headerId, orderNumber }, 
					LtMastUsers.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return userList;
	}
	
	@Override
	public LtSalesPersonLocation locationSaveOnNoOrder(LtSalesPersonLocation ltSalesPersonLocation)throws ServiceException, IOException{
		/*
		 * String query = env.getProperty("locationSaveOnNoOrder");
		 * List<LtSalesPersonLocation> noOrderDetails = jdbcTemplate.query(query, new
		 * Object[] { ltSalesPersonLocation }, new
		 * BeanPropertyRowMapper<LtSalesPersonLocation>(LtSalesPersonLocation.class));
		 * 
		 * if(noOrderDetails !=null) { return noOrderDetails.get(0); }else { return
		 * null; }
		 */
		return ltSalesPersonLocationRepository.save(ltSalesPersonLocation);
	}

	@Override
	public List<LtMastOutles> getOutletDetailsById(String outletId) throws ServiceException, IOException {
		String query = env.getProperty("getOutletDetailsByOutletId");
		List<LtMastOutles> outlet = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
//		List<LtMastOutles> outlet = jdbcTemplate.query(query, new Object[] {outletId}, 
//				new BeanPropertyRowMapper<LtMastOutles>(LtMastOutles.class));
		//System.out.print("outlet =" +outlet);
		
			try {
				outlet = consumeApiService.consumeApi(query, 
						new Object[] { outletId }, 
						LtMastOutles.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!outlet.isEmpty() ) {
			return outlet;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		//return Optional.empty();
	}

//	@Override
//	public String getUserTypeAgainsUserId(Long userId) throws ServiceException, IOException {
//		String query= env.getProperty("getuserTypeAgainsUserId");
//		String userType = jdbcTemplate.queryForObject(query, new Object[] {userId}, String.class);
//		return userType;
//	}

	@Override
	public LtMastUsers getUserDetailsAgainsUserId(Long userId) throws ServiceException, IOException {
		String query= env.getProperty("getuserTypeAgainsUserId");
		List<LtMastUsers> userList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] {userId}, 
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		try {
			userList = consumeApiService.consumeApi(query, 
					new Object[] { userId }, 
					LtMastUsers.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!userList.isEmpty()) {
		return userList.get(0);
		}
		return null;
	}

	@Override
	public String getDefaultPriceListAgainstOutletId(String outletId) throws ServiceException, IOException {
		String query= env.getProperty("getDefaultPriceListAgainstOutletId");
		//String priceList= jdbcTemplate.queryForObject(query, new Object[] {outletId}, String.class);
		//String priceList;
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String priceList = consumeApiService.consumeApiForString(query, 
					new Object[] { outletId });
			return priceList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


	@Override
	public String getOrderSequence() throws ServiceException, IOException {
		String query= env.getProperty("getOrderSequence");
		//String sequenceNumber= jdbcTemplate.queryForObject(query, new Object[] {}, String.class);
		
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String sequenceNumber = consumeApiService.consumeApiForString(query, 
					new Object[] {});
			return sequenceNumber;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<LtMastUsers> getActiveSysAdminUsersFromHeaderId(Long headerId, String orderNumber)
			throws ServiceException, IOException {
		String query = env.getProperty("getActiveSysAdminUsersFromHeaderId");
		List<LtMastUsers> userList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
//				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		
		try {
			userList = consumeApiService.consumeApi(query, 
					new Object[] { headerId, orderNumber }, 
					LtMastUsers.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userList;
	}

	@Override
	public String getUserTypeAgainsUserId(Long createdBy) throws ServiceException, IOException {
		String query= env.getProperty("getUserTypeAgainsUserId");
		//String userType= jdbcTemplate.queryForObject(query, new Object[] {createdBy}, String.class);
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String userType = consumeApiService.consumeApiForString(query, 
					new Object[] { createdBy });
			return userType;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public SoHeaderDto getheaderByHeaderId(Long headerId) throws ServiceException, IOException {
		String query= env.getProperty("getHeaderDetailsByHeaderId");
		List<SoHeaderDto> soHeaderDto= jdbcTemplate.query(query, new Object[] {headerId}, 
				new BeanPropertyRowMapper<SoHeaderDto>(SoHeaderDto.class));
		if (!soHeaderDto.isEmpty())
			return soHeaderDto.get(0);
		else
			return null;
	}

	@Override
	public String getEmailBody(String subject) throws ServiceException, IOException {
		String query= env.getProperty("getEmailBody");
		//String body= jdbcTemplate.queryForObject(query, new Object[] {subject}, String.class);
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String body = consumeApiService.consumeApiForString(query, 
					new Object[] { subject });
			return body;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getUserNameAgainsUserId(Long createdBy) throws ServiceException, IOException {
		String query= env.getProperty("getUserNameAgainsUserId");
		//String userName= jdbcTemplate.queryForObject(query, new Object[] {createdBy}, String.class);
		
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String userName = consumeApiService.consumeApiForString(query, 
					new Object[] { createdBy });
			return userName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Double> getTotalAmount(Long headerId) throws ServiceException, IOException {
		String query= env.getProperty("getTotalAmount");
		List<Double> amount= jdbcTemplate.queryForList(query, new Object[] {headerId}, double.class);
		System.out.println("amount is........" + amount);
		return amount;
	}

	@Override
	public List<String> getTotalAmount1(Long headerId) throws ServiceException, IOException {
		String query= env.getProperty("getTotalAmount");
		List<String> amount= new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		List<String> amount= jdbcTemplate.queryForList(query, new Object[] {headerId}, String.class);
//		System.out.println("amount is........" + amount);
		
		try {
			List<LtSoLines>amount1 = consumeApiService.consumeApi(query, 
					new Object[] { headerId},LtSoLines.class);
			
			for(int i=0; i<amount1.size(); i++) {
				amount.add(amount1.get(i).getPtrPrice());
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!amount.isEmpty()) {
			return amount;
			}
		return null;
	}
	
	@Override
	public LtSoHeaders getSiebelDataById(Long headerId) throws ServiceException, IOException {
		String query= env.getProperty("getSiebelDataById");
		//List<LtSoHeaders> siebelData= new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

		List<LtSoHeaders> siebelData= jdbcTemplate.query(query, new Object[] {headerId}, 
				new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));
		
//		try {
//			siebelData = consumeApiService.consumeApi(query, 
//					new Object[] { headerId }, 
//					LtSoHeaders.class);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (!siebelData.isEmpty())
			return siebelData.get(0);
		else
			return null;	
	}

	@Override
	public List<Long> getSoHeaderRemovingPendingOrdersFromGetOrderV2(RequestDto requestDto)
			throws ServiceException, IOException {
			try {
			String query = env.getProperty("getOrderHeaderV1RemovingPendingOrders");
			String query1 = env.getProperty("getSoHeaderRemovingPendingOrdersFromGetOrderV2ForAreaHead");
	       
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
	 String headerId = null;
			if(requestDto.getHeaderId() !=null) {
				headerId = requestDto.getHeaderId().toString();
					}
			
			
			String searchField = null;
			if (requestDto.getSearchField() != null) {
				
				String searchStr = requestDto.getSearchField().toUpperCase();
				
				String dayStr = "";
			    String yearStr = "";
				if(searchStr.contains("JAN")) {
					 String[] arr = searchStr.trim().split("JAN"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-01-"+dayStr+"%";
					
				}else if (searchStr.contains("FEB")) {
					 String[] arr = searchStr.trim().split("FEB"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-02-"+dayStr+"%";
					
				}else if (searchStr.contains("MAR")) {
					 String[] arr = searchStr.trim().split("MAR"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-03-"+dayStr+"%";
					
				}else if (searchStr.contains("APR")) {
					 String[] arr = searchStr.trim().split("APR"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-04-"+dayStr+"%";
					
				}else if (searchStr.contains("MAY")) {
					 String[] arr = searchStr.trim().split("MAY"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-05-"+dayStr+"%";
					
				}else if (searchStr.contains("JUN")) {
					 String[] arr = searchStr.trim().split("JUN"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-06-"+dayStr+"%";
					
				}else if (searchStr.contains("JUL")) {
					 String[] arr = searchStr.trim().split("JUL"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-07-"+dayStr+"%";
					
				}else if (searchStr.contains("AUG")) {
					 String[] arr = searchStr.trim().split("AUG"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-08-"+dayStr+"%";
					
				}else if (searchStr.contains("SEP")) {
					 String[] arr = searchStr.trim().split("SEP"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-09-"+dayStr+"%";
				}else if (searchStr.contains("OCT")) {
					 String[] arr = searchStr.trim().split("OCT"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-10-"+dayStr+"%";
					
				}else if (searchStr.contains("NOV")) {
					 String[] arr = searchStr.trim().split("NOV"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-11-"+dayStr+"%";
				}else if (searchStr.contains("DEC")) {
					 String[] arr = searchStr.trim().split("DEC"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-12-"+dayStr+"%";
				}else {
					searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
				}
			}
			System.out.println("searchField :: "+searchField);
			
			ConsumeApiService consumeApiService = new ConsumeApiService();
			List<Long> headerIdslist = new ArrayList<>();	
			//List<Long> headerIdslist = null;
			//Long headerId =0l;
			Long userId = 0L;
			//UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
			UserDetailsDto userDetailsDto = getUserTypeAndDisId(userId);
			
			System.out.println("AlluserDetailsDto"+userDetailsDto);
			if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				//get userList by distributorID
				
		/*Keyur		if(userDetailsDto != null) {
					query = query + " and lsh.created_by not in ( " + requestDto.getUserId() + ")";
					System.out.println("New AlluserDetailsDto"+query);
				}
				query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
				System.out.println("New123 AlluserDetailsDto"+query);
		*/		
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				
				System.out.println("AlluserListDto"+userList);
				
				if(!userList.isEmpty() && userList != null) {
					query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
						//	+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b where rownum BETWEEN ? AND ? ";
				// on 7-May-24	        + ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
							+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				}else {
					//query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  where rownum BETWEEN ? AND ? ";
			//on 7-May-24		query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
					query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.last_update_date desc ) b  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				}
				
				
				
				
				/*
				 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
				 * requestDto.getStatus(),
				 * requestDto.getOrderNumber(),requestDto.getDistributorId(), headerId,
				 * searchField, requestDto.getOutletId(), requestDto.getLimit(),
				 * requestDto.getOffset());
				 */
				
//				headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
//						requestDto.getOrderNumber(),requestDto.getDistributorId(),  searchField,
//						requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset());

				try {
					headerIdslist = consumeApiService.consumeApi(query, 
							new Object[] { requestDto.getStatus(),
									requestDto.getOrderNumber(),requestDto.getDistributorId(),  searchField,
									requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() }, 
							Long.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("headerIdslist in Dao"+headerIdslist);
				return headerIdslist;
				
			}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
				System.out.println("else if SALES = ");
				//String positionId = getPositionIdByUserId(requestDto.getUserId());
				//List<Long> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId()); //comment on 12-March-24 vaibhav
				List<String> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
				
				if(!outletList.isEmpty() && outletList != null) {
					//query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
					//query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
					//		+ " ) ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";

					//				String outList = null;   comment on 13-march-2024 vaibhav 320-333
//					String newList = null;
//					for(int i=1; i< outletList.size(); i++) { 
//						if(i!=outletList.size()) {
//						 outList= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'"+",";
//						 //outList.charAt(outList.length() - 1);
//						 outList.substring(0, outList.length() - 1);
//						 //outList.replace(",", "");
						 System.out.println("Issue for outList = "+outletList);//}
//						if(i==outletList.size()-1) {
//							String outList1= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'";
//							 System.out.print("2nd outList =\n"+outList+outList1);}
//						
//					}
					
					query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
						//	+ " ) ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
					//7-May-24      +  ") ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
							+  ") ) a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				}else {
					System.out.println("else for outList = ");//}
//					query = query +" ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
				//	query = query +" ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ?";
		// 7-may-24	query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
					query = query +" ) a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
				}
				
				System.out.print("Issue for query ="+query);
				
//				headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
//						requestDto.getOrderNumber(), requestDto.getDistributorId(), searchField,
//						requestDto.getLimit(), requestDto.getOffset());
				
				try {
					headerIdslist = consumeApiService.consumeApi(query, 
							new Object[] { requestDto.getStatus(),
									requestDto.getOrderNumber(), requestDto.getDistributorId(), searchField,
									requestDto.getLimit(), requestDto.getOffset() }, 
							Long.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*
				 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
				 * requestDto.getStatus(), requestDto.getOrderNumber(),
				 * requestDto.getDistributorId(), headerId, searchField, requestDto.getLimit(),
				 * requestDto.getOffset());
				 */
				return headerIdslist;
				
			}
//				else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase("AREAHEAD")){
//				
//				List<String> distId= getDistributorIdFromAreaHead(userDetailsDto.getEmployeeCode());
//				String distIdList = null;
//				if(!distId.isEmpty() && distId != null) {
//					      distIdList = distId.stream()
//			                .map(id -> "'" + id + "'")
//			                .collect(Collectors.joining(", "));
//				}
//				
//						//	query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
//		//07-May-24			query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
//					      query1 = query1 +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
//					
//					/*
//					 * if(requestDto.getHeaderId() !=null) { headerId = requestDto.getHeaderId();
//					 * }else { headerId = null; }
//					 */
//					
//					System.out.println(requestDto.getHeaderId()); 
//					System.out.println("In else AREAHEAD ="+query1);  
//					
//					/*
//					 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
//					 * requestDto.getDistributorId(),requestDto.getStatus(),
//					 * requestDto.getOrderNumber(), searchField
//					 * ,headerId,requestDto.getOutletId(),requestDto.getLimit(),
//					 * requestDto.getOffset() );
//					 */
//		           
////		           headerIdslist = jdbcTemplate.queryForList(query, Long.class,
////							requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
////							searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
////							);
//					List<RequestDto>headerIdslist1 = new ArrayList<>();
//					try {
//						headerIdslist1 = consumeApiService.consumeApi(query1, 
//								new Object[] { distIdList,requestDto.getStatus(), requestDto.getOrderNumber(),
//										searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset() }, 
//								RequestDto.class);
//						for(int i =0; i<headerIdslist1.size(); i++) {
//							headerIdslist.add(headerIdslist1.get(i).getHeaderId());
//						}
//						
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					/*
//					 * headerIdslist = jdbcTemplate.query(query, new Object[] { }, new
//					 * BeanPropertyRowMapper<Long>(Long.class));
//					 */
//		System.out.println("headerIdsSSSS "+headerIdslist);
//					return headerIdslist;
//				
//			}
			
			else {
			//	query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
	//07-May-24			query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				
				/*
				 * if(requestDto.getHeaderId() !=null) { headerId = requestDto.getHeaderId();
				 * }else { headerId = null; }
				 */
				
				System.out.println(requestDto.getHeaderId()); 
				System.out.println(query);  
				
				/*
				 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
				 * requestDto.getDistributorId(),requestDto.getStatus(),
				 * requestDto.getOrderNumber(), searchField
				 * ,headerId,requestDto.getOutletId(),requestDto.getLimit(),
				 * requestDto.getOffset() );
				 */
	           
//	           headerIdslist = jdbcTemplate.queryForList(query, Long.class,
//						requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
//						searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
//						);
				List<RequestDto>headerIdslist1 = new ArrayList<>();
				try {
					headerIdslist1 = consumeApiService.consumeApi(query, 
							new Object[] { requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
									searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset() }, 
							RequestDto.class);
					for(int i =0; i<headerIdslist1.size(); i++) {
						headerIdslist.add(headerIdslist1.get(i).getHeaderId());
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*
				 * headerIdslist = jdbcTemplate.query(query, new Object[] { }, new
				 * BeanPropertyRowMapper<Long>(Long.class));
				 */
	System.out.println("headerIdsSSSS "+headerIdslist);
				return headerIdslist;
			}}catch(Exception e) {
				logger.error("Error Description :", e);
				e.printStackTrace();
			}return null;
		
	}

	/*@Override
	public Long getRecordCountRemovingPendingOrdersFromGetOrderV2(RequestDto requestDto)
			throws ServiceException, IOException {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ResponseDto> getOrderV2RemovingPendingOrdersFromGetOrderV2(List<Long> headerIdList)
			throws ServiceException, IOException {	
		List<ResponseDto> headerDtolist = new ArrayList<>();
			try {
				String query = env.getProperty("getOrderLineV1RemovingPendingOrders");
				System.out.print("headerIdList =" +headerIdList);
				ConsumeApiService consumeApiService = new ConsumeApiService();
				
				
				//query = query + "  and lsh.header_id IN ( 164) ) a order by a.status_o, a.cdate desc, a.header_id ";
				query = query + "  and lsh.header_id IN ( " + headerIdList.toString().replace("[", "").replace("]", "")
				//07-May-24		+ " ) ) a order by a.status_o, a.cdate desc, a.header_id ";
				+ " ) ) a order by a.last_update_date desc";//, a.header_id ";
				
				System.out.println("headerIdList line data query = " +query);
//				List<ResponseDto> headerDtolist = jdbcTemplate.query(query, new Object[] {},
//						new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));

				try {
					headerDtolist = consumeApiService.consumeApiWithRequestBody(query, 
							new Object[] { }, 
							ResponseDto.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return headerDtolist;
			} catch (Exception e) {
				logger.error("Error Description :", e);
				e.printStackTrace();
			}
			return headerDtolist;
	
	}

	@Override
	public List<SoHeaderDto> getheaderByHeaderIdNew(List<Long> headerIdsList) throws ServiceException, IOException {
		System.out.println("in method getAllPendingOrders dao line 1302 ="+ new Date());
		String query = env.getProperty("getheaderByHeaderIdNew");
		try {
			query = query + " and lsh.header_id IN ( " + headerIdsList.toString().replace("[", "").replace("]", "")
					+ " ) ORDER BY lsh.HEADER_ID desc";
		List<SoHeaderDto> SoHeaderData = jdbcTemplate.query(query,new Object[] {} , 
				new BeanPropertyRowMapper<SoHeaderDto>(SoHeaderDto.class));
	
	if(SoHeaderData.size()>0) {
		return SoHeaderData;
	}
	System.out.println("in method getAllPendingOrders service line 1313 ="+ new Date());
	}catch(Exception e) 
	    {
		 e.printStackTrace();
		}
	return null;
	}
	
	
	public int[] batchInsert(List<String> sqlQueries) {
        return jdbcTemplate.batchUpdate(sqlQueries.toArray(new String[0]));
    }
	
	@Override
	public List<QuantityCheck> quantityCheck(String distributorId,List<String> productIdList) throws ServiceException, IOException{
//		String query = env.getProperty("quantityCheck");
//		query = query + "and lmiv.PRODUCT_ID in ("+ "'"+ productIdList.toString().replace("[", "").replace("]", "") +  "'"+")";
//		System.out.println("Query is "+query);
//		List<QuantityCheck> productList= jdbcTemplate.query(query, new Object[] {distributorId},
//				new BeanPropertyRowMapper<QuantityCheck>(QuantityCheck.class));
//		return productList;
		List<QuantityCheck> productList = new ArrayList<>(); 
		try {
		String query = env.getProperty("quantityCheck");
	    
	    // Dynamically construct the placeholders for the IN clause
	    String placeholders = productIdList.stream()
	        .map(productId -> "?")
	        .collect(Collectors.joining(", "));
	    
	    query = query + " AND lmiv.PRODUCT_ID IN (" + placeholders + ")";
	    System.out.println("Query is " + query);
	    
	    // Create a new list to hold all the parameters for the query
	    List<Object> params = new ArrayList<>();
	    params.add(distributorId);
	    params.addAll(productIdList);
	    
	    // Execute the query with the parameters
	     productList = jdbcTemplate.query(
	        query,
	        params.toArray(),
	        new BeanPropertyRowMapper<>(QuantityCheck.class)
	    );
	     	     
	    return productList;
	}
	  catch(Exception e) {
			e.printStackTrace();
		}
		return productList;
	}

	@Override
	public String getUserNameFromSiebel(String mobileNumber) throws ServiceException, IOException {
		String query = env.getProperty("getUserNameFromSiebel");
		String userName = jdbcTemplate.queryForObject(query, new Object[] {mobileNumber}, String.class);
		return userName;
	}

	@Override
	public List<Long> getSoHeaderFromProcedure() throws ServiceException, IOException {
		String query = env.getProperty("getSoHeaderFromProcedure");
		List<Long> headerIdList = jdbcTemplate.queryForList(query, new Object[] {}, Long.class);
		return headerIdList;
	}

	@Override
	public List<ResponseDto> getOrderV2FromProcedure(List<Long> headerIdsList) throws ServiceException, IOException {
		try {
			System.out.println("in method getAllPendingOrders getOrderV2 dao line 438 ="+ LocalDateTime.now());
			String query = env.getProperty("getOrderV2FromProcedure");
			System.out.print("headerIdList =" +headerIdsList);
			if(headerIdsList.size()>0) {
			//query = query + "  and lsh.header_id IN ( 164) ) a order by a.status_o, a.cdate desc, a.header_id ";
			query = query + "  and lsh.header_id IN ( " + headerIdsList.toString().replace("[", "").replace("]", "")
					+ " ) ) a order by a.status_o, a.cdate desc, a.header_id ";
			}
			else {
				System.out.println("in else");
			    query = query + "  and lsh.header_id IN (NULL) ) a order by a.status_o, a.cdate desc, a.header_id ";
				System.out.println("Concated query is "+query);
 
			}
			List<ResponseDto> headerDtolist = jdbcTemplate.query(query, new Object[] {},
					new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));

			System.out.println("in method getAllPendingOrders dao line 448 ="+ LocalDateTime.now());
			return headerDtolist;
		} catch (Exception e) {
			logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<LtOrderLineDataGt> getOrderV2DataFromProcedure() throws ServiceException, IOException {
		String query = env.getProperty("getOrderV2DataFromProcedure");
		
		List<LtOrderLineDataGt> data = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtOrderLineDataGt>(LtOrderLineDataGt.class));
		return data;
		
	}

	@Override
	public List<LtOrderLineDataGt> getDataFromProcedure() throws ServiceException, IOException {
             String query = env.getProperty("getOrderV2DataFromProcedure");
		
		List<LtOrderLineDataGt> data = jdbcTemplate.query(query, new Object[] {},
				new BeanPropertyRowMapper<LtOrderLineDataGt>(LtOrderLineDataGt.class));
		return data;
	}

	
	@Override
	public List<ResponseDto> getMultipleMrpForProductV1(String prodId, String distributorId)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ResponseDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
//				System.out.println("prodId & distId & query = " + prodId +"/t"+ distributorId + query);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						//new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId },
						new Object[] { distributorId, distributorId, prodId,distributorId,prodId,prodId,distributorId,prodId },
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("productList o/p = "+ productList);
		   if(productList!= null) 
		    {
			  return productList;
		    }
		}catch (Exception e) 
		    {
			  e.printStackTrace();
		    }
		return null;
	}

	@Override
	public String checkOrderNoInSiebel(String orderNumber) throws ServiceException, IOException {
		
		String query = env.getProperty("checkOrderNoInSiebel");
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		String	siebelOrderNoList = null;
		try {
			siebelOrderNoList = consumeApiService.consumeApiForString(query, new Object[] {orderNumber});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return siebelOrderNoList;
	}

	@Override
	public List<ResponseDto> getMultipleMrpForOutofStockProductV1(String prodId, String distributorId)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ResponseDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForOutofStockProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				System.out.println("prodId & distId = " + prodId +"/t"+ distributorId);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("productList o/p = "+ productList);
		   if(productList!= null) 
		    {
			  return productList;
		    }
		}catch (Exception e) 
		    {
			  e.printStackTrace();
		    }
		return null;
	}

	@Override
	public List<String> getPriceListAgainstHeaderId(String headerIdList) throws ServiceException, IOException {
		ConsumeApiService consumeApiService = new ConsumeApiService();
		List<String> priceList = new ArrayList<>();
		String query = env.getProperty("getPriceListAgainstHeaderId");
//		priceList = jdbcTemplate.queryForList(query, new Object[] {headerIdList}, String.class);
		System.out.println("Query getPriceListAgainstHeaderId = "+ query);
		
		try {
			List<ResponseDto> list = consumeApiService.consumeApi(query, 
					new Object[] { headerIdList }, 
					ResponseDto.class);
			for(int i =0; i < list.size();i++) {
				
				priceList.add(list.get(i).getPriceList());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("price List = "+ priceList);		
		if (!priceList.isEmpty())
			return priceList;   
		else
			return null;
	}

	@Override
	public List<ResponseDto> getMultipleMrpForInstockProductV1(String prodId, String distributorId, String priceList)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ResponseDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForInstockProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				System.out.println("prodId & distId & query = " + prodId +"/t"+ distributorId + query);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId,priceList }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("productList o/p = "+ productList);
		   if(productList!= null) 
		    {
			  return productList;
		    }
		}catch (Exception e) 
		    {
			  e.printStackTrace();
		    }
		return null;
	}

	@Override
	public List<ResponseDto> getOpenOrderWithNewStatusFromSiebel(String prod, String distributorId, String outletId)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ResponseDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getOpenOrderWithNewStatusFromSiebel");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				System.out.println("prodId & distId & query = " + prod +"/t"+ distributorId + outletId + query);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prod,distributorId,outletId }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("productList o/p = "+ productList);
		   if(productList!= null) 
		    {
			  return productList;
		    }
		}catch (Exception e) 
		    {
			  e.printStackTrace();
		    }
		return null;
	}

	@Override
	public List<ResponseDto> getMrpForMultipleProductV1(String prodId, String distributorId)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ResponseDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMrpForMultipleProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				//System.out.println("prodId & distId & query = " + prodId +"/t"+ distributorId + query);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						//new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId },
						new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId },
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("productList o/p = "+ productList);
		   if(productList!= null) 
		    {
			  return productList;
		    }
		}catch (Exception e) 
		    {
			  e.printStackTrace();
		    }
		return null;
	}

	@Override
	public List<ResponseDto> getMultiMrpAndInventQtyForProd(String prodId, String distributorId) throws ServiceException, IOException {
		
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ResponseDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultiMrpAndInventQtyForProd");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				//System.out.println("prodId & distId & query = " + prodId +"/t"+ distributorId + query);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						//new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId },
						new Object[] { prodId,distributorId,prodId,prodId,distributorId,prodId },
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("productList o/p = "+ productList);
		   if(productList!= null) 
		    {
			  return productList;
		    }
		}catch (Exception e) 
		    {
			  e.printStackTrace();
		    }
		return null;
	}

	@Override
	public String getPriceListId(String priceList) throws ServiceException, IOException {
		String query = env.getProperty("getPriceListId");
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		String	priceListId = null;
		try {
			priceListId = consumeApiService.consumeApiForString(query, new Object[] {priceList});
		} catch (Exception e) {
			e.printStackTrace();
		} //if(priceListId!=null)
		return priceListId;
		
	}
	
	@Override
	public String findDistributorIdAgainstOutletId(String outletId) throws ServiceException, IOException {
		String distId;
		String sql = env.getProperty("findDistributorIdAgainstOutletId");
		distId = jdbcTemplate.queryForObject(sql, new Object[] { outletId }, String.class);
		return distId;
	}
	
	@Override
	public List<LtMastUsers> getAllAreaHeadAgainstDist(String distributorId)throws ServiceException, IOException{
		String query = env.getProperty("getAllAreaHeadAgainstDist");
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {distributorId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		System.out.println("getAllAreaHeadAgainstDist query& distributorId"+query+"\t"+distributorId);
		System.out.println("getAllAreaHeadAgainstDist query o/p =" +ltMastUserslist);
		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}
	
	@Override
	public List<LtMastUsers> getSystemAdministartorsDetails(String orgId) throws ServiceException, IOException{
		String query = env.getProperty("getSystemAdministartorDetails");
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] { orgId},
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}
	
	@Override
	public List<LtMastUsers> getAllSalesOfficersAgainstDist(String distributorId)throws ServiceException, IOException{
		String query = env.getProperty("getAllSalesOfficersAgainstDist");
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {distributorId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}
	
	@Override
	public Long getUserIdFromMobileNo(String mobileNumber) throws ServiceException, IOException {
		
		String query = env.getProperty("getUserIdFromMobileNo");
		ConsumeApiService consumeApiService = new ConsumeApiService();
//		Long userId =0L;
		if(mobileNumber!= null) {
		Long userId = jdbcTemplate.queryForObject(query,new Object[] {mobileNumber}, Long.class);
		if(userId!= null) {
			return userId;
			}
		}
		System.out.println("Notification query == " +query+mobileNumber);
//		if(mobileNumber!= null) {
//		try {
//			userId = consumeApiService.consumeApiForCount(query, 
//					new Object[] { mobileNumber });
//			if(userId!= null) {
//			return userId;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		}
		
		return null;
	}
	
	@Override
	public String getMobileNoFromOrderNo(String orderNo) throws ServerException {
		String query = env.getProperty("getMobileNoFromOrderNo");
		String mobileNo = jdbcTemplate.queryForObject(query, new Object[] {orderNo}, String.class);
		return mobileNo;
	}
	
//	@Override
//	public String getDistIdFromOutletCode(String outletCode) throws ServerException {
//		String query = env.getProperty("getDistIdFromOutletCode");
//		String distId = jdbcTemplate.queryForObject(query, new Object[] {outletCode}, String.class);
//		return distId;
//	}
	
		public List<String> getDistributorIdFromAreaHead(String employeeCodse) throws ServiceException, IOException {
			
			String query = env.getProperty("getDistributorIdFromAreaHead");
			System.out.println("ltMastUsers employeeCode is ="+employeeCodse);
			List<String> distIds = jdbcTemplate.queryForList(query, new Object[] {employeeCodse}, String.class);
			System.out.println("distId is ="+distIds +query+employeeCodse);
			 if (!distIds.isEmpty()) {
					return distIds;
				}else {
					return null;
				}		
		}	
		
		public List<String> getEmpCodeFromDistributorId(String distCode) throws ServiceException, IOException {
			
			String query = env.getProperty("getEmpCodeFromDistributorId");
			System.out.println("ltMastUsers distCode is ="+distCode);
			List<String> empCode = jdbcTemplate.queryForList(query, new Object[] {distCode}, String.class);
			 if (!empCode.isEmpty()) {
					return empCode;
				}else {
					return null;
				}		
		}

public List<String> getDistributorIdFromAreaHeadNew(String employeeCodse) throws ServiceException, IOException {
			
			String query = env.getProperty("getDistributorIdFromAreaHeadNew");
			System.out.println("ltMastUsers employeeCode is ="+employeeCodse);
			List<String> distIds = jdbcTemplate.queryForList(query, new Object[] {}, String.class);
			System.out.println("distId is ="+distIds +query+employeeCodse);
			 if (!distIds.isEmpty()) {
					return distIds;
				}else {
					return null;
				}		
		}	
		

		@Override
		public List<Long> getSoHeaderRemovingPendingOrdersFromGetOrderV2ForAreaHead(RequestDto requestDto,
				String distIdList)throws ServiceException, IOException {
			try {
			String query = env.getProperty("getSoHeaderRemovingPendingOrdersFromGetOrderV2ForAreaHead");
	       
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
	 String headerId = null;
			if(requestDto.getHeaderId() !=null) {
				headerId = requestDto.getHeaderId().toString();
					}
			
			
			String searchField = null;
			if (requestDto.getSearchField() != null) {
				
				String searchStr = requestDto.getSearchField().toUpperCase();
				
				String dayStr = "";
			    String yearStr = "";
				if(searchStr.contains("JAN")) {
					 String[] arr = searchStr.trim().split("JAN"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-01-"+dayStr+"%";
					
				}else if (searchStr.contains("FEB")) {
					 String[] arr = searchStr.trim().split("FEB"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-02-"+dayStr+"%";
					
				}else if (searchStr.contains("MAR")) {
					 String[] arr = searchStr.trim().split("MAR"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-03-"+dayStr+"%";
					
				}else if (searchStr.contains("APR")) {
					 String[] arr = searchStr.trim().split("APR"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-04-"+dayStr+"%";
					
				}else if (searchStr.contains("MAY")) {
					 String[] arr = searchStr.trim().split("MAY"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-05-"+dayStr+"%";
					
				}else if (searchStr.contains("JUN")) {
					 String[] arr = searchStr.trim().split("JUN"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-06-"+dayStr+"%";
					
				}else if (searchStr.contains("JUL")) {
					 String[] arr = searchStr.trim().split("JUL"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-07-"+dayStr+"%";
					
				}else if (searchStr.contains("AUG")) {
					 String[] arr = searchStr.trim().split("AUG"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-08-"+dayStr+"%";
					
				}else if (searchStr.contains("SEP")) {
					 String[] arr = searchStr.trim().split("SEP"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-09-"+dayStr+"%";
				}else if (searchStr.contains("OCT")) {
					 String[] arr = searchStr.trim().split("OCT"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-10-"+dayStr+"%";
					
				}else if (searchStr.contains("NOV")) {
					 String[] arr = searchStr.trim().split("NOV"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-11-"+dayStr+"%";
				}else if (searchStr.contains("DEC")) {
					 String[] arr = searchStr.trim().split("DEC"); 
				        for (String st : arr) 
				        {
				        	 if(st.trim().length() == 2) {
				        		 dayStr = st.trim();
				        	 }else {
				        		 yearStr = st.trim();
				        	 }
				        }
					searchField = "%"+yearStr+"-12-"+dayStr+"%";
				}else {
					searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
				}
			}
			System.out.println("searchField :: "+searchField);
			
			ConsumeApiService consumeApiService = new ConsumeApiService();
			List<Long> headerIdslist = new ArrayList<>();	
			//List<Long> headerIdslist = null;
			//Long headerId =0l;
			UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
			
			System.out.println("AlluserDetailsDto"+userDetailsDto);
			if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
				//get userList by distributorID
				
		/*Keyur		if(userDetailsDto != null) {
					query = query + " and lsh.created_by not in ( " + requestDto.getUserId() + ")";
					System.out.println("New AlluserDetailsDto"+query);
				}
				query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
				System.out.println("New123 AlluserDetailsDto"+query);
		*/		
				List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
				
				System.out.println("AlluserListDto"+userList);
				
				if(!userList.isEmpty() && userList != null) {
					query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
						//	+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b where rownum BETWEEN ? AND ? ";
				// on 7-May-24	        + ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
							+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				}else {
					//query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  where rownum BETWEEN ? AND ? ";
			//on 7-May-24		query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
					query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.last_update_date desc ) b  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				}
				
				
				
				
				/*
				 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
				 * requestDto.getStatus(),
				 * requestDto.getOrderNumber(),requestDto.getDistributorId(), headerId,
				 * searchField, requestDto.getOutletId(), requestDto.getLimit(),
				 * requestDto.getOffset());
				 */
				
//				headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
//						requestDto.getOrderNumber(),requestDto.getDistributorId(),  searchField,
//						requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset());

				try {
					headerIdslist = consumeApiService.consumeApi(query, 
							new Object[] { requestDto.getStatus(),
									requestDto.getOrderNumber(),requestDto.getDistributorId(),  searchField,
									requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() }, 
							Long.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("headerIdslist in Dao"+headerIdslist);
				return headerIdslist;
				
			}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
				//String positionId = getPositionIdByUserId(requestDto.getUserId());
				//List<Long> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId()); //comment on 12-March-24 vaibhav
				List<String> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
				
				if(!outletList.isEmpty() && outletList != null) {
					//query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
					//query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
					//		+ " ) ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";

					//				String outList = null;   comment on 13-march-2024 vaibhav 320-333
//					String newList = null;
//					for(int i=1; i< outletList.size(); i++) { 
//						if(i!=outletList.size()) {
//						 outList= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'"+",";
//						 //outList.charAt(outList.length() - 1);
//						 outList.substring(0, outList.length() - 1);
//						 //outList.replace(",", "");
//						 System.out.println("Issue for outList =\n"+newList);}
//						if(i==outletList.size()-1) {
//							String outList1= "'" + outletList.get(i).toString().replace("[", "").replace("]", "") + "'";
//							 System.out.print("2nd outList =\n"+outList+outList1);}
//						
//					}
					
					query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
						//	+ " ) ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
					//7-May-24      +  ") ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
							+  ") ) a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				}else {
//					query = query +" ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
				//	query = query +" ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ?";
		// 7-may-24	query = query +" ) a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
					query = query +" ) a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
				}
				
				System.out.print("Issue for query ="+query);
				
//				headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
//						requestDto.getOrderNumber(), requestDto.getDistributorId(), searchField,
//						requestDto.getLimit(), requestDto.getOffset());
				
				try {
					headerIdslist = consumeApiService.consumeApi(query, 
							new Object[] { requestDto.getStatus(),
									requestDto.getOrderNumber(), requestDto.getDistributorId(), searchField,
									requestDto.getLimit(), requestDto.getOffset() }, 
							Long.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*
				 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
				 * requestDto.getStatus(), requestDto.getOrderNumber(),
				 * requestDto.getDistributorId(), headerId, searchField, requestDto.getLimit(),
				 * requestDto.getOffset());
				 */
				return headerIdslist;
				
			}else {
			//	query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
	//07-May-24			query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.last_update_date desc ) b OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
				
				/*
				 * if(requestDto.getHeaderId() !=null) { headerId = requestDto.getHeaderId();
				 * }else { headerId = null; }
				 */
				
				System.out.println(requestDto.getHeaderId()); 
				System.out.println(query);  
				
				/*
				 * headerIdslist = jdbcTemplate.queryForList(query, Long.class,
				 * requestDto.getDistributorId(),requestDto.getStatus(),
				 * requestDto.getOrderNumber(), searchField
				 * ,headerId,requestDto.getOutletId(),requestDto.getLimit(),
				 * requestDto.getOffset() );
				 */
	           
//	           headerIdslist = jdbcTemplate.queryForList(query, Long.class,
//						requestDto.getDistributorId(),requestDto.getStatus(), requestDto.getOrderNumber(),
//						searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset()
//						);
				List<RequestDto>headerIdslist1 = new ArrayList<>();
				try {
					headerIdslist1 = consumeApiService.consumeApi(query, 
							new Object[] { distIdList,requestDto.getStatus(), requestDto.getOrderNumber(),
									searchField,requestDto.getOutletId(),requestDto.getLimit(), requestDto.getOffset() }, 
							RequestDto.class);
					for(int i =0; i<headerIdslist1.size(); i++) {
						headerIdslist.add(headerIdslist1.get(i).getHeaderId());
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*
				 * headerIdslist = jdbcTemplate.query(query, new Object[] { }, new
				 * BeanPropertyRowMapper<Long>(Long.class));
				 */
	System.out.println("headerIdsSSSS "+headerIdslist);
				return headerIdslist;
			}}catch(Exception e) {
				logger.error("Error Description :", e);
				e.printStackTrace();
			}return null;
		
		}
		
		
		@Override
		public String getDistIdFromOrderNo(String orderNo) throws ServiceException, IOException {
			String distCode;
			String sql = env.getProperty("getDistIdFromOrderNo");
			distCode = jdbcTemplate.queryForObject(sql, new Object[] { orderNo }, String.class);
			//if(distCode!= null) {
			return distCode;
			//}
			//return null;
		}
}
	
