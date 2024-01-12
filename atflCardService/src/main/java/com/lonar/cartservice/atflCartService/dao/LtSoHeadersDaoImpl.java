package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.UserDetailsDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtMastOutles;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtOrderCancellationReason;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.model.LtSoLines;
import com.lonar.cartservice.atflCartService.repository.LtSoLinesRepository;


@Repository
@PropertySource(value = "classpath:queries/cartMasterQueries.properties", ignoreResourceNotFound = true)
//@Transactional(propagation=Propagation.MANDATORY)
public class LtSoHeadersDaoImpl implements LtSoHeadersDao,CodeMaster {

	@Autowired
	private Environment env;
	
	@Autowired
	LtSoLinesRepository  ltSoLinesRepository;

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

		List<LtSoHeaders> ltSoHeaderslist = jdbcTemplate.query(query, new Object[] { outletId },
				new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));

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

		List<LtSoHeaders> ltSoHeaderslist = jdbcTemplate.query(query, new Object[] { orderNumber.toUpperCase() },
				new BeanPropertyRowMapper<LtSoHeaders>(LtSoHeaders.class));

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
		String query = env.getProperty("getOrderHeaderV1");

		if (requestDto.getLimit() == 0) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
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
		
		List<Long> headerIdslist = null;
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			//get userList by distributorID
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			
			if(!userList.isEmpty() && userList != null) {
				query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx')) )a order by a.status_o, a.creation_date desc ) b where rownum<= ? ";
			}else {
				query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b  where rownum<= ? ";
			}
			headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
					requestDto.getOrderNumber(),requestDto.getDistributorId(), requestDto.getHeaderId(), searchField,
					requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset());

			return headerIdslist;
			
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			//String positionId = getPositionIdByUserId(requestDto.getUserId());
			List<Long> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
			
			if(!outletList.isEmpty() && outletList != null) {
				//query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
				//		+ " ) ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
				query = query + " and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
						+ " ) ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
			}else {
//				query = query +" ) a order by a.status_o, a.creation_date desc ) b LIMIT ?  OFFSET ? ";
				query = query +" ) a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ?";
			}
			headerIdslist = jdbcTemplate.queryForList(query, Long.class, requestDto.getStatus(),
					requestDto.getOrderNumber(), requestDto.getDistributorId(), requestDto.getHeaderId(), searchField,
					requestDto.getLimit(), requestDto.getOffset());
			return headerIdslist;
			
		}else {
			query = query +" and COALESCE(lsh.outlet_id ,'xx') =  COALESCE( ? ,COALESCE(lsh.outlet_id,'xx') ) )a order by a.status_o, a.creation_date desc ) b WHERE rownum BETWEEN ? AND ? ";
			/*
			 * List<Long> headerIds = jdbcTemplate.queryForList(query, Long.class,
			 * requestDto.getStatus(),
			 * requestDto.getOrderNumber(),requestDto.getDistributorId(),
			 * requestDto.getHeaderId(), searchField, requestDto.getOutletId(),
			 * requestDto.getLimit(), requestDto.getOffset());
			 */
			
			 headerIdslist = jdbcTemplate.query(query, new Object[] {requestDto.getStatus(),
					requestDto.getOrderNumber(),requestDto.getDistributorId(), requestDto.getHeaderId(), searchField,
					requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() },
					new BeanPropertyRowMapper<Long>(Long.class));
System.out.println("headerIds"+headerIdslist);
			return headerIdslist;
		}
	}

	@Override
	public List<ResponseDto> getOrderV1(List<Long> headerIdList) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getOrderLineV1");
			query = query + "  and lsh.header_id IN ( " + headerIdList.toString().replace("[", "").replace("]", "")
					+ " ) ) a order by a.status_o, a.cdate desc, a.header_id ";
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
	public List<ResponseDto> getOrderV2(List<Long> headerIdList) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getOrderLineV1");
			query = query + "  and lsh.header_id IN ( " + headerIdList.toString().replace("[", "").replace("]", "")
					+ " ) ) a order by a.status_o, a.cdate desc, a.header_id ";
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
		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return userList;
	}

	@Override
	public List<LtMastUsers> getActiveSalesUsersFromHeaderId(Long headerId, String orderNumber) throws ServiceException, IOException {
		String query = env.getProperty("getActiveSalesUsersFromHeaderId");
		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return userList;
	}

	@Override
	public Long getRecordCount(RequestDto requestDto) throws ServiceException, IOException {
		
				
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
		
		Long recordCount;
		
		String query = env.getProperty("getOrderHeaderCount");
		
		UserDetailsDto userDetailsDto = getUserTypeAndDisId(requestDto.getUserId());
		if (userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(DISTRIBUTOR)) {
			//get userList by distributorID
			List<Long> userList = getUsersByDistributorId(userDetailsDto.getDistributorId());
			
			if(!userList.isEmpty() && userList != null) {
				query = query + " and lsh.created_by in (" + userList.toString().replace("[", "").replace("]", "")
						+ ") and lsh.outlet_id = COALESCE( ? ,lsh.outlet_id)";
			}else {
				query = query +" and lsh.outlet_id =  COALESCE( ? ,lsh.outlet_id) ";
			}
			
			recordCount = jdbcTemplate.queryForObject(query, new Object[] { 
					requestDto.getStatus(), requestDto.getOrderNumber(),  
					requestDto.getDistributorId(), requestDto.getHeaderId(), 
					searchField,requestDto.getOutletId()
			}, Long.class);
			return recordCount;
		}else if(userDetailsDto!= null && userDetailsDto.getUserType().equalsIgnoreCase(SALES)) {
			List<Long> outletList = getOutletIdsByPositionId(userDetailsDto.getPositionId());
			
			if(!outletList.isEmpty() && outletList != null) {
				query = query +" and lsh.outlet_id in (" + outletList.toString().replace("[", "").replace("]", "")
						+ ") ";
			}else {
				//query = query +" and COALESCE(lsh.outlet_id ,-99) =  COALESCE( ? ,COALESCE(lsh.outlet_id),-99) ";
			}
			
			recordCount = jdbcTemplate.queryForObject(query, new Object[] { 
					requestDto.getStatus(), requestDto.getOrderNumber(),  
					requestDto.getDistributorId(), requestDto.getHeaderId(), 
					searchField
			}, Long.class);
			return recordCount;
		}else {
			query = query +" and lsh.outlet_id = COALESCE( ? ,lsh.outlet_id)";
			
			recordCount = jdbcTemplate.queryForObject(query, new Object[] { 
					requestDto.getStatus(), requestDto.getOrderNumber(),  
					requestDto.getDistributorId(), requestDto.getHeaderId(), 
					searchField,requestDto.getOutletId()
			}, Long.class);
			return recordCount;
		}

	}

	@Override
	public DistributorDetailsDto getDistributorDetailsByOutletId(String outletId) throws ServiceException, IOException {
		String query = env.getProperty("getDistributorDetailsByOutletId");

		List<DistributorDetailsDto> distributorDetailsDtoList = jdbcTemplate.query(query, new Object[] { outletId },
				new BeanPropertyRowMapper<DistributorDetailsDto>(DistributorDetailsDto.class));

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
		String mobileNumber;
		String sql = env.getProperty("getMobileNumber");
		mobileNumber = jdbcTemplate.queryForObject(sql, new Object[] { userId }, String.class);
		return mobileNumber;
	}
	
	private UserDetailsDto getUserTypeAndDisId(Long userId) throws ServiceException {
		String query = env.getProperty("getUserTypeAndDisId");
		List<UserDetailsDto> list = jdbcTemplate.query(query, new Object[] { userId },
				new BeanPropertyRowMapper<UserDetailsDto>(UserDetailsDto.class));
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}
	
	private List<Long> getUsersByDistributorId(String distributorId) throws ServiceException {
		String query = env.getProperty("getUsersByDistributorId");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, distributorId);
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
	
	private List<Long> getOutletIdsByPositionId(String positionId) throws ServiceException {
		String query = env.getProperty("getOutletIdsByPositionId");
		List<Long> userIdList = jdbcTemplate.queryForList(query, Long.class, positionId);
		if (!userIdList.isEmpty())
			return userIdList;
		else
			return null;
	}

	@Override
	public int deleteLineDataByHeaderIdAndReturnStatus(Long heaaderId) throws ServiceException, IOException {
		String query = env.getProperty("deleteLineDataByHeaderId");
		Object[] person = new Object[] { heaaderId };
		int status = jdbcTemplate.update(query, person);
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
		List<LtMastUsers> userList = jdbcTemplate.query(query, new Object[] { headerId, orderNumber },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		return userList;
	}

	@Override
	public List<LtMastOutles> getOutletDetailsById(String outletId) throws ServiceException, IOException {
		String query = env.getProperty("getOutletDetailsByOutletId");
		try {
		List<LtMastOutles> outlet = jdbcTemplate.query(query, new Object[] {outletId}, 
				new BeanPropertyRowMapper<LtMastOutles>(LtMastOutles.class));
		
		if(!outlet.isEmpty() ) {
			return outlet;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		//return Optional.empty();
	}

}
