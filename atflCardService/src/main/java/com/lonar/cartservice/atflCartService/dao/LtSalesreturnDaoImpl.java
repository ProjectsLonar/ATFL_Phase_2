package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsDto;
import com.lonar.cartservice.atflCartService.dto.LtInvoiceDetailsResponseDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnHeaderDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnLineDto;
import com.lonar.cartservice.atflCartService.dto.LtSalesReturnResponseDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.dto.SalesReturnApproval;
import com.lonar.cartservice.atflCartService.dto.SoHeaderDto;
import com.lonar.cartservice.atflCartService.dto.SoHeaderLineDto;
import com.lonar.cartservice.atflCartService.dto.SoLineDto;
import com.lonar.cartservice.atflCartService.dto.SalesReturnResponse;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtMastUsers;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnLines;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;
import com.lonar.cartservice.atflCartService.model.LtSoHeaders;
import com.lonar.cartservice.atflCartService.repository.LtSalesRetrunLinesRepository;
import com.lonar.cartservice.atflCartService.repository.LtSalesReturnRepository;
import com.lonar.cartservice.atflCartService.service.ConsumeApiService;

@Repository
@PropertySource(value = "classpath:queries/cartmasterqueries.properties", ignoreResourceNotFound = true)
//@Transactional(propagation=Propagation.MANDATORY)
public class LtSalesreturnDaoImpl implements LtSalesreturnDao,CodeMaster{

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	LtSalesReturnRepository ltSalesReturnRepository;
	
	@Autowired
	LtSalesRetrunLinesRepository ltSalesRetrunLinesRepository;
	

	@Override
	public List<LtSalesReturnStatus> getStatusForSalesReturn() throws ServerException{
		try {
		String query = env.getProperty("getStatusForSalesReturn");
		

		List<LtSalesReturnStatus> list = jdbcTemplate.query(query,
				new Object[] { },
				new BeanPropertyRowMapper<LtSalesReturnStatus>(LtSalesReturnStatus.class));

		if (!list.isEmpty()) {
			return list;
		}

	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<LtSalesReturnAvailability> getAvailabilityForSalesReturn() throws ServerException{
		try {
		String query = env.getProperty("getAvailabilityForSalesReturn");
		

		List<LtSalesReturnAvailability> list = jdbcTemplate.query(query,
				new Object[] { },
				new BeanPropertyRowMapper<LtSalesReturnAvailability>(LtSalesReturnAvailability.class));

		if (!list.isEmpty()) {
			return list;
		}

	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<LtSalesReturnAvailability> getLocationForSalesReturn(String distributorCode) throws ServerException{
		try {
		String query = env.getProperty("getLocationForSalesReturn");
		//System.out.println("invoiceNumber = "+invoiceNumber);
		List<LtSalesReturnAvailability> list = new ArrayList<>();
//		List<LtSalesReturnAvailability> list = jdbcTemplate.query(query,
//				new Object[] { distributorCode },
//				new BeanPropertyRowMapper<LtSalesReturnAvailability>(LtSalesReturnAvailability.class));
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			list = consumeApiService.consumeApi(query, 
					new Object[] { distributorCode },LtSalesReturnAvailability.class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("list = "+location);
		if (!list.isEmpty()) {
			return list;
		}
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public String getDefaultLocationForSalesReturn(String distributorCode){
		String query= env.getProperty("getLocationForSalesReturn");
		
		//String location= jdbcTemplate.queryForObject(query, new Object[] {distributorCode}, String.class);
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String location = consumeApiService.consumeApiForString(query, 
					new Object[] { distributorCode });
			return location;
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
	public List<Long> getSalesReturnHeader(RequestDto requestDto)throws ServerException{
		List<Long> list = new ArrayList<Long>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
		String query = env.getProperty("getSalesReturnHeader");
		
		String status = null;
		
		String searchField = null;
		if (requestDto.getSearchField()!= null) {
			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
		}
		
		if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
		
		//if(requestDto.getReturnStatus() !=null)
		//{
		//	status = requestDto.getReturnStatus().toUpperCase();
		//}

		if(requestDto.getStatus() !=null)
		{
			status = requestDto.getStatus().toUpperCase();
		}
		
//		list = jdbcTemplate.queryForList(query,
//				new Object[] {requestDto.getOutletId(), status, requestDto.getInvoiceNumber(), requestDto.getSalesReturnNumber(),
//						searchField, requestDto.getLimit(), requestDto.getOffset()},Long.class);

		try {
			List<RequestDto>list1 = consumeApiService.consumeApi(query, 
					new Object[] {requestDto.getOutletId(), status, requestDto.getInvoiceNumber(), requestDto.getSalesReturnNumber(),
							searchField, requestDto.getLimit(), requestDto.getOffset()}, 
					RequestDto.class);
			for(int i =0; i<list1.size(); i++) {
				list.add(list1.get(i).getSalesReturnHeaderId());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!list.isEmpty()) {
			return list;
		}

	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public Long getSalesReturnRecordCount(RequestDto requestDto)throws ServerException{
		try {
		String query = env.getProperty("getSalesReturnRecordCount");
		
		ConsumeApiService consumeApiService = new ConsumeApiService();

		String status = null;
		if(requestDto.getReturnStatus() !=null)
		{
			status = requestDto.getReturnStatus().toUpperCase();
		}
		Long count=0L;
//		Long count = jdbcTemplate.queryForObject(query,
//				new Object[] { status, requestDto.getInvoiceNumber(),requestDto.getSalesReturnNumber()},Long.class);

		try {
			count = consumeApiService.consumeApiForCount(query, 
					new Object[] { status, requestDto.getInvoiceNumber(),requestDto.getSalesReturnNumber()});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public List<ResponseDto> getSalesReturn(List<Long> IdsList) throws ServerException{
		try {
			String query = env.getProperty("getSalesReturnline");
			query = query + "  AND lsrl.SALES_RETURN_HEADER_ID IN ( " + IdsList.toString().replace("[", "").replace("]", "")
					+ " ) ";
			List<ResponseDto> salesreturnlist = jdbcTemplate.query(query, new Object[] {},
					new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));

			return salesreturnlist;
		} catch (Exception e) {
			//logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@Transactional
	public void deleteSalesReturnLinesByHeaderId(Long HeaderId)throws ServerException{
		String query = env.getProperty("deleteSalesReturnLinesByHeaderId");
		Object[] person = new Object[] { HeaderId };
		jdbcTemplate.update(query, person);
	}
	
	@Override
	public LtSalesReturnHeader updateSalesReturnHeader(LtSalesReturnHeader ltSalesreturnHeader)throws ServerException{
		if (ltSalesreturnHeader != null) {
			return ltSalesReturnRepository.save(ltSalesreturnHeader);
		}
		return null;
	}
	
	@Override
	public LtSalesReturnLines updateLines(LtSalesReturnLines ltSalesreturnlines) throws ServerException{
		if(ltSalesreturnlines !=null) {
			return ltSalesRetrunLinesRepository.save(ltSalesreturnlines);
		}
		return null;
	}
	
	@Override
	public List<LtInvoiceDetailsResponseDto> getInvoiceDetails( RequestDto requestDto) throws ServerException{
		try {
			String query = env.getProperty("getInvoiceDetails");
			
			String searchField = null;
			if (requestDto.getSearchField()!= null) {
				searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
			}
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
			
			List<LtInvoiceDetailsResponseDto> list = jdbcTemplate.query(query,
					new Object[] { requestDto.getDistributorId(), requestDto.getInvoiceNumber(),
							searchField, requestDto.getLimit(), requestDto.getOffset()},
					new BeanPropertyRowMapper<LtInvoiceDetailsResponseDto>(LtInvoiceDetailsResponseDto.class));

			System.out.println("invoice list"+list );
			if (!list.isEmpty()) {
				return list;
			}

		
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
	}	
	
	@Override
	public String getSalesReturnSequence(){
		String query= env.getProperty("getSalesReturnSequence");
		String sequenceNumber= jdbcTemplate.queryForObject(query, new Object[] {}, String.class);
		return sequenceNumber;
	}

	@Override
	public List<SoHeaderDto> getBeatNameAgainstInvoiceNo(String invoiceNo) throws ServerException {
		List<SoHeaderDto> beatName = new ArrayList<SoHeaderDto>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
		    String query = env.getProperty("getBeatNameAgainstInvoiceNoNew");
		    System.out.print(query);
		   // System.out.print(invoiceNo);
//		     beatName = jdbcTemplate.query(query, new Object[] {//invoiceNo
//		    		},new BeanPropertyRowMapper<SoHeaderDto>(SoHeaderDto.class));
		    //}
		    
		    try {
		    	beatName = consumeApiService.consumeApi(query, 
						new Object[] { }, 
						SoHeaderDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		     System.out.print(" in query beatName ==" + beatName);
	        if(beatName.size()>0) {
		    return beatName;
	        
		    
		    /*List<String> list = jdbcTemplate.query(query,
					new Object[] {invoiceNo},
					new BeanPropertyRowMapper<String>(String.class));
		    
		    System.out.println("list"+list.get(0));
		    if(! list.isEmpty()) {
		    return list.get(0);*/
	        }else {
	        	return null;
	        }
	        }
	  	 catch(Exception e) 
	  	   {e.printStackTrace();
	  	   }
	  	   
	  	   return beatName;

	}

	@Override
	public List<LtSalesReturnAvailability> getLotNumber(String prodId, String inventId) throws ServerException {
		 	
		try {
			String query = env.getProperty("getLotNumber");
			
			List<LtSalesReturnAvailability>	lotNumber = jdbcTemplate.query(query, new Object[] {prodId,inventId},
					new BeanPropertyRowMapper<LtSalesReturnAvailability>(LtSalesReturnAvailability.class));
			if(!lotNumber.isEmpty()) {
				return lotNumber;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}return null;
	}

	@Override
	public List<LtMastUsers> getAreaHeadDetails(String outletId) throws ServerException {
		String query = env.getProperty("getAreaHeadDetails");
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {outletId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}

	@Override
	public LtSalesReturnHeader getSalesReturnSiebelDataById(Long salesReturnHeaderId) throws ServerException {
		String query= env.getProperty("getSalesReturnSiebelDataById");
		List<LtSalesReturnHeader> siebelData= jdbcTemplate.query(query, new Object[] {salesReturnHeaderId}, 
				new BeanPropertyRowMapper<LtSalesReturnHeader>(LtSalesReturnHeader.class));
		if (!siebelData.isEmpty())
			return siebelData.get(0);
		else
			return null;
	}

	@Override
	public List<LtSalesReturnResponseDto> getSalesReturnForPendingAprroval(List<Long> salesReturnHeaderId) throws ServerException {
		try{String query = env.getProperty("getSalesReturnForPendingAprroval");
		List<LtSalesReturnResponseDto> salesReturnList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
//		if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
//			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
//		}
//		
//		if(requestDto.getOffset() == 0 ) {
//			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
//		}

		query = query + "  AND lsrh.SALES_RETURN_HEADER_ID IN ( " + salesReturnHeaderId.toString().replace("[", "").replace("]", "")
				+ " ) order by lsrh.SALES_RETURN_DATE desc" ;
		
//		List<LtSalesReturnResponseDto> salesReturnList = jdbcTemplate.query(query,new Object[] {},
//				new BeanPropertyRowMapper<LtSalesReturnResponseDto>(LtSalesReturnResponseDto.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		
		try {
			salesReturnList = consumeApiService.consumeApi(query, 
					new Object[] {}, 
					LtSalesReturnResponseDto.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!salesReturnList.isEmpty()) {
			return salesReturnList;
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	}

	@Override
	public List<Long> getSalesReturnHeaderId(RequestDto requestDto) throws ServerException {
		List<Long> salesReturnHeaderId = new ArrayList<Long>();
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			String query = env.getProperty("getSalesReturnHeaderId");
			
			//System.out.println("request in Dao == " + requestDto);
			String searchField = null;
			if (requestDto.getSearchField()!= null) {
				searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
			}
			//System.out.println("searchField in Dao == " + searchField);
			if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0 ) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			//System.out.println("rquest = "+searchField+ requestDto.getLimit()+ requestDto.getOffset()+requestDto.getOutletId());	
			//System.out.println("query = " + query);
//			salesReturnHeaderId = jdbcTemplate.queryForList(query, new Object[] 
//					{requestDto.getOutletId(), searchField, requestDto.getLimit(), requestDto.getOffset()},(Long.class));
			//System.out.println("salesReturnHeaderId result = " + salesReturnHeaderId +"Size will be =="+salesReturnHeaderId.size());   
			
			try {
				List<RequestDto>salesReturnHeaderId1 = consumeApiService.consumeApi(query, 
						new Object[] 								
								{requestDto.getOutletId(), searchField, requestDto.getLimit(), requestDto.getOffset()}, 
								RequestDto.class);
			for(int i=0; i<salesReturnHeaderId1.size(); i++ ) {
				salesReturnHeaderId.add(salesReturnHeaderId1.get(i).getSalesReturnHeaderId());
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			if(!salesReturnHeaderId.isEmpty()) {
			return salesReturnHeaderId;
               }
               } catch (Exception e) {
            //	   System.out.println("I'm catch exception"+e.getMessage());
			//logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return salesReturnHeaderId;
	}

	@Override
	public List<LtSalesReturnLineDto> getSalesReturnLineData(Long long1, RequestDto requestDto) throws ServerException {
		
		try{String query = env.getProperty("getSalesReturnLineData");
//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
		if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}

		//query = query + "  AND lsrh.SALES_RETURN_HEADER_ID IN ( " + salesReturnHeaderId.toString().replace("[", "").replace("]", "")
		//		+ " ) order by lsrh.SALES_RETURN_DATE desc" ;
		
		List<LtSalesReturnLineDto> salesReturnData = jdbcTemplate.query(query,new Object[] {long1
				//requestDto.getLimit(), requestDto.getOffset()
				}, 
				new BeanPropertyRowMapper<LtSalesReturnLineDto>(LtSalesReturnLineDto.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		if(!salesReturnData.isEmpty()) {
			return salesReturnData;
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
		
	}


	@Override
	public List<LtSalesReturnLineDto> getSalesReturnLineData(List<Long> long1, RequestDto requestDto) throws ServerException {
		
		try{String query = env.getProperty("getSalesReturnLineData");
		ConsumeApiService consumeApiService = new ConsumeApiService();
		List<LtSalesReturnLineDto> salesReturnData  = new ArrayList<>();

//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
		if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}

		query = query + "  AND lsrh.SALES_RETURN_HEADER_ID IN ( " + long1.toString().replace("[", "").replace("]", "")
				+ " ) order by lsrh.SALES_RETURN_DATE desc" ;
		
//		List<LtSalesReturnLineDto> salesReturnData = jdbcTemplate.query(query,new Object[] {//long1
//				//requestDto.getLimit(), requestDto.getOffset()
//				}, 
//				new BeanPropertyRowMapper<LtSalesReturnLineDto>(LtSalesReturnLineDto.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		
		try {
			salesReturnData = consumeApiService.consumeApi(query, 
					new Object[] { }, 
					LtSalesReturnLineDto.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!salesReturnData.isEmpty()) {
			return salesReturnData;
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	}
	
	@Override
	public LtSalesReturnResponseDto getSalesReturnForPendingAprroval1(Long salesReturnHeaderId, RequestDto requestDto)
			throws ServerException {
		try{
			String query = env.getProperty("getSalesReturnForPendingAprroval1");

			if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0 ) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
		List<LtSalesReturnResponseDto> salesReturnList = jdbcTemplate.query(query,new Object[] {salesReturnHeaderId
				//requestDto.getLimit(), requestDto.getOffset()
				}, new BeanPropertyRowMapper<LtSalesReturnResponseDto>(LtSalesReturnResponseDto.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		if(!salesReturnList.isEmpty()) {
			return salesReturnList.get(0);
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	}

	@Override
	public List<String> getSalesOrderInvoiceNo(RequestDto requestDto) throws ServerException {
		try {
			String query = env.getProperty("getSalesOrderInvoiceNo");
			//System.out.println("requestDto"+requestDto);
			String searchField = null;
			if (requestDto.getSearchField()!= null) {
				searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
			}
			
			if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0 ) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			//System.out.println("requestDto"+searchField+ requestDto.getLimit()+ requestDto.getOffset());
			//System.out.println("query is" +query);
			List<SoHeaderDto> invoiceNoList = new ArrayList<>();
			ConsumeApiService consumeApiService = new ConsumeApiService();
//			List<String> invoiceNoList = jdbcTemplate.queryForList(query, new Object[] 
//					{requestDto.getOutletId(), searchField, requestDto.getLimit(), requestDto.getOffset()},(String.class));
               
			try {
				invoiceNoList = consumeApiService.consumeApi(query, 
						new Object[] 
								{requestDto.getOutletId(), searchField, requestDto.getLimit(), requestDto.getOffset()}, 
								SoHeaderDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> retrunstring = new ArrayList<>();
			for(int i =0; i < invoiceNoList.size();i++) {
				
				retrunstring.add(invoiceNoList.get(i).getSiebelInvoicenumber());
			}
			
			if(!invoiceNoList.isEmpty()) {
			return retrunstring;
               }
               } catch (Exception e) {
			//logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SoHeaderDto> getSoHeaderDataFromInvoiceNo(List<String> salesReturnInvoice, RequestDto requestDto)
			throws ServerException {
		try {
			String query = env.getProperty("getSoHeaderDataFromInvoiceNo");
			
			if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0 ) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
						
			List<SoHeaderDto> soHeaderData = jdbcTemplate.query(query, new Object[] 
					{salesReturnInvoice, requestDto.getLimit(), requestDto.getOffset()}, new BeanPropertyRowMapper<SoHeaderDto>(SoHeaderDto.class));
               if(!soHeaderData.isEmpty()) {
			return soHeaderData;
               }
               } catch (Exception e) {
			//logger.error("Error Description :", e);
			e.printStackTrace();
		}
		return null;
		
		
	}

	@Override
	public List<SoLineDto> getSoLineData(String siebelInvoicenumber, RequestDto requestDto)
			throws ServerException {
		try{
			String query = env.getProperty("getSoLineData");
			List<SoLineDto> SoLineData = new ArrayList<>();
			ConsumeApiService consumeApiService = new ConsumeApiService();
//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
/*		if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
*/
//		List<SoLineDto> SoLineData = jdbcTemplate.query(query,new Object[] {//siebelInvoicenumber, siebelInvoicenumber 
//				//requestDto.getLimit(), requestDto.getOffset()
//				}, new BeanPropertyRowMapper<SoLineDto>(SoLineDto.class));
		
			try {
				SoLineData = consumeApiService.consumeApi(query, 
						new Object[] {}, 
						SoLineDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		if(!SoLineData.isEmpty()) {
			return SoLineData;
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	
	}

	@Override
	public SoHeaderLineDto getSoHeaderData(String salesReturnInvoice, RequestDto requestDto) throws ServerException {
		try{
			String query = env.getProperty("getSoHeaderData");
//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
		/*if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
*/
		List<SoHeaderLineDto> SoHeaderData = jdbcTemplate.query(query,new Object[] {salesReturnInvoice 
				//requestDto.getLimit(), requestDto.getOffset()
				}, new BeanPropertyRowMapper<SoHeaderLineDto>(SoHeaderLineDto.class));
		
		if(!SoHeaderData.isEmpty()) {
			return SoHeaderData.get(0);
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	}

	@Override
	public List<SoHeaderDto> getSoHeaderDataFromInvoiceNo12(String string, RequestDto requestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SoHeaderDto getSoHeaderData12(String string, RequestDto requestDto) throws ServerException {
		try{
			String query = env.getProperty("getSoHeaderData");
//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
		if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}

		List<SoHeaderDto> SoHeaderData = jdbcTemplate.query(query,new Object[] {string, requestDto.getLimit(), 
				requestDto.getOffset()}, new BeanPropertyRowMapper<SoHeaderDto>(SoHeaderDto.class));
		
		if(!SoHeaderData.isEmpty()) {
			return SoHeaderData.get(0);
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;

	}

	@Override
	public List<LtSalesReturnLines> getSalesReturnLineDataForApproval(Long long1, RequestDto requestDto)
			throws ServerException {
		try{String query = env.getProperty("getSalesReturnLineDataForApproval");
		
		if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}

		//query = query + "  AND lsrh.SALES_RETURN_HEADER_ID IN ( " + salesReturnHeaderId.toString().replace("[", "").replace("]", "")
		//		+ " ) order by lsrh.SALES_RETURN_DATE desc" ;
		
		List<LtSalesReturnLines> salesReturnLines = jdbcTemplate.query(query,new Object[] {long1, 
				requestDto.getLimit(), requestDto.getOffset()
				}, new BeanPropertyRowMapper<LtSalesReturnLines>(LtSalesReturnLines.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		if(!salesReturnLines.isEmpty()) {
			return salesReturnLines;
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
		
	}


	@Override
	public ResponseDto getSalesReturnForAprroval1(Long salesReturnHeaderId, RequestDto requestDto) throws ServerException {
		try{
			String query = env.getProperty("getSalesReturnForAprroval1");

			if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0 ) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
			
		List<ResponseDto> salesReturnHeaderList = jdbcTemplate.query(query,new Object[] {salesReturnHeaderId,
				requestDto.getLimit(), requestDto.getOffset()
				}, new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		if(!salesReturnHeaderList.isEmpty()) {
			return salesReturnHeaderList.get(0);
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	}

	@Override
	public String getInvoiceNumber(String orderNumber) throws ServerException {
		String query= env.getProperty("getInvoiceNumber");
		String invoiceNumber= jdbcTemplate.queryForObject(query, new Object[] {orderNumber}, String.class);
//		List<ResponseDto> siebelInvoicenumber= jdbcTemplate.query(query, new Object[] {orderNumber}, 
//				new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
		System.out.println("invoiceNumber is = "+invoiceNumber);
		//if (siebelInvoicenumber!= null) {
		//return siebelInvoicenumber.get(0).getSiebelInvoicenumber();
	//	}//else {
//			return "";
//		}
		return invoiceNumber;
	}

	@Override
	public String getOrderType(String orderNumber) throws ServerException {
		String query= env.getProperty("getOrderType");
		String orderType= jdbcTemplate.queryForObject(query, new Object[] {orderNumber}, String.class);
		System.out.println("invoiceNumber is = "+orderType);
		return orderType;
	}
	
	@Override
	public String getUserNameAgainsUserId(Long userId) throws ServerException {
		String query= env.getProperty("getUserNameAgainsUserIdForSalesReturn");
		String userName= jdbcTemplate.queryForObject(query, new Object[] {userId}, String.class);
//		if(userName.equalsIgnoreCase("")||userName.equalsIgnoreCase(null) ) {
//			userName ="Test Name";
//		}
		System.out.println("userName"+userName);
		return userName;
	}

	@Override
	public String getOrderNoFromInvoiceNo(String invoiceNumber) throws ServerException {
		String query= env.getProperty("getOrderNoFromInvoiceNoForSalesReturn");
		String orderNumber= jdbcTemplate.queryForObject(query, new Object[] {invoiceNumber}, String.class);
		return orderNumber;
	}

	@Override
	public String getDistIdFromOutletCode(String outletId) throws ServerException {
		String query= env.getProperty("getDistCRMCodeFromOutletCode");
		String distId= jdbcTemplate.queryForObject(query, new Object[] {outletId}, String.class);
		return distId;
	}

	@Override
	public String getProdNameFromProdId(String productId) throws ServerException {
		String query= env.getProperty("getProdNameFromProdId");
		ConsumeApiService consumeApiService = new ConsumeApiService();
		String prodName = "";
		//String prodName= jdbcTemplate.queryForObject(query, new Object[] {productId}, String.class);
		try {
			prodName = consumeApiService.consumeApiForString(query, 
					new Object[] { productId });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prodName;
	}

	
	@Override
	public String getProdDescFromProdId(String productId) throws ServerException {
		String query= env.getProperty("getProdDescFromProdId");
		ConsumeApiService consumeApiService = new ConsumeApiService();
		String ProdDesc = "";
		//String prodName= jdbcTemplate.queryForObject(query, new Object[] {productId}, String.class);
		try {
			ProdDesc = consumeApiService.consumeApiForString(query, 
					new Object[] { productId });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ProdDesc;
	}
	
	
	@Override
	public SoLineDto getProductId(String siebelInvoicenumber) throws ServerException {
		String query= env.getProperty("getProductId");
		List<SoLineDto> prodId= jdbcTemplate.query(query, new Object[] {siebelInvoicenumber}, 
				new BeanPropertyRowMapper<SoLineDto>(SoLineDto.class));
		return prodId.get(0);
	}

	@Override
	public StringBuilder getInvoiceErrorMSg(String orderNumber) throws ServerException {
		String query= env.getProperty("getInvoiceErrorMSg");
		StringBuilder errorMsg= jdbcTemplate.queryForObject(query, new Object[] {orderNumber}, StringBuilder.class);
		return errorMsg;
	}

	@Override
	public LtSalesReturnHeaderDto getSalesReturnOrderAgainstReturnOrderNo(String returnOrderNo) throws ServerException {
		
		String query = env.getProperty("getSalesReturnOrderAgainstReturnOrderNo");
		System.out.println("query is"+ query);
		List<LtSalesReturnHeaderDto> salesReturnList = jdbcTemplate.query(query, new Object[] {returnOrderNo},
				new BeanPropertyRowMapper<LtSalesReturnHeaderDto>(LtSalesReturnHeaderDto.class));
		System.out.println("salesReturnList is"+ salesReturnList);
		if(salesReturnList!= null) {
			return salesReturnList.get(0);
		}
		return null;
	}

	@Override
	public List<LtSalesReturnLineDto> getSalesReturnOrderLineData(Long salesReturnHeaderId) throws ServerException {
		String query = env.getProperty("getSalesReturnOrderLineData");
		System.out.println("query is"+ query);
		List<LtSalesReturnLineDto> salesReturnLineData = jdbcTemplate.query(query, new Object[] {salesReturnHeaderId},
				new BeanPropertyRowMapper<LtSalesReturnLineDto>(LtSalesReturnLineDto.class));
		System.out.println("salesReturnList is"+ salesReturnLineData);
		if(salesReturnLineData.size()>0) {
			return salesReturnLineData;
		}
		return null;
	}

	@Override
	public List<LtMastUsers> getSalesOfficersDetails(String outletId) throws ServerException {
			String query = env.getProperty("getSalesOfficersDetails");
			List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
					new Object[] {outletId },
					new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

			if (!ltMastUserslist.isEmpty()) {
				return ltMastUserslist;
			}else {
				return null;
			}
	}
	

	@Override
	public List<LtMastUsers> getSysAdminDetails(String outletId) throws ServerException {
//		String query = env.getProperty("getSysAdminDetails");
		String query = env.getProperty("getSysAdminDetailsForSalesReturn");
		outletId="1";
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {outletId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}

	@Override
	public List<SoHeaderDto> getSoHeaderDataNew(List<String> salesReturnInvoice, RequestDto requestDto)
			throws ServerException {
		try{
			String query = env.getProperty("getSoHeaderDataNew");
			List<SoHeaderDto> SoHeaderData = new ArrayList<>();
			ConsumeApiService consumeApiService = new ConsumeApiService();
//		String searchField = null;
//		if (requestDto.getSearchField()!= null) {
//			searchField= "%" +requestDto.getSearchField().toUpperCase()+ "%";
//		}
		
		/*if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}
		
		if(requestDto.getOffset() == 0 ) {
			requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
		}
*/
			
			//query = query + "  AND lsh.SIEBEL_INVOICENUMBER in ( " + salesReturnInvoice("[", "").replace("]", "")
				//	+ " ) ";
//			List<SoHeaderDto> SoHeaderData = jdbcTemplate.query(query,new Object[] {} , 
//					new BeanPropertyRowMapper<SoHeaderDto>(SoHeaderDto.class));
		
			try {
				SoHeaderData = consumeApiService.consumeApi(query, 
						new Object[] {}, 
						SoHeaderDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		if(SoHeaderData.size()>0) {
			return SoHeaderData;
		}
		}catch(Exception e) 
		    {
			 e.printStackTrace();
			}
		return null;
	}

	@Override
	public String getUserNameFromSiebel(String mobileNumber) throws ServerException {
		String query = env.getProperty("getUserNameFromSiebel");
		String userName = jdbcTemplate.queryForObject(query, new Object[] {mobileNumber}, String.class);
		return userName;
	}

	
	@Override
	public List<ResponseDto> getSalesReturnForAprroval_Opt(List<Long> salesReturnHeaderId, RequestDto requestDto) throws ServerException {
		try{
			List<ResponseDto> salesReturnHeaderList = new ArrayList<>();
			ConsumeApiService consumeApiService = new ConsumeApiService();
			
			String query = env.getProperty("getSalesReturnForAprroval1");
 
			if(requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0 ) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
			// Construct the IN clause with the list of IDs
	        String inClause = salesReturnHeaderId.toString().replace("[", "").replace("]", "");
 
	        query = query + " AND lsrh.SALES_RETURN_HEADER_ID IN (" + inClause + ") " +
	                      "ORDER BY SALES_RETURN_DATE  DESC " +
	                      "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			
//		List<ResponseDto> salesReturnHeaderList = jdbcTemplate.query(query,new Object[] {
//				requestDto.getLimit(), requestDto.getOffset()
//				}, new BeanPropertyRowMapper<ResponseDto>(ResponseDto.class));
		//System.out.println("salesReturnList == "+salesReturnList);
		
	        try {
	        	salesReturnHeaderList = consumeApiService.consumeApi(query, 
	        			new Object[] {
	        					requestDto.getLimit(), requestDto.getOffset()
	        					 }, 
						ResponseDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(!salesReturnHeaderList.isEmpty()) {
			return salesReturnHeaderList;
		}
		}catch(Exception e)
		    {
			 e.printStackTrace();
			}
		return null;
	}
	
	
	@Override
	public Map<Long, List<LtSalesReturnLines>> getSalesReturnLinesForApproval_Opt(List<Long> long1, RequestDto requestDto) throws ServerException {
	    Map<Long, List<LtSalesReturnLines>> salesReturnLinesMap = new HashMap<>();
	    try {
	        String query = env.getProperty("getSalesReturnLineDataForApproval");
	        List<LtSalesReturnLines> salesReturnLines = new ArrayList<>();
	        ConsumeApiService consumeApiService = new ConsumeApiService();
 
	        if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
	            requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
	        }
 
	        if (requestDto.getOffset() == 0) {
	            requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
	        }
 
	        // Construct the IN clause with the list of IDs
	        String inClause = long1.toString().replace("[", "").replace("]", "");
 
	        query = query + " AND lsrh.SALES_RETURN_HEADER_ID IN (" + inClause + ") " +
	                      "ORDER BY lsrh.SALES_RETURN_DATE DESC " +
	                      "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	        System.out.println(requestDto.getOffset());
	        System.out.println(requestDto.getLimit());
//	        List<LtSalesReturnLines> salesReturnLines = jdbcTemplate.query(
//	            query,
//	            new Object[]{requestDto.getLimit(),requestDto.getOffset() },
//	            new BeanPropertyRowMapper<LtSalesReturnLines>(LtSalesReturnLines.class)
//	        );
 
	        try {
	        	salesReturnLines = consumeApiService.consumeApi(query, 
	        			new Object[]{requestDto.getLimit(),requestDto.getOffset() }, 
						LtSalesReturnLines.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Query = "+query);
	        System.out.println("salesReturnLines = "+salesReturnLines);
	        System.out.println("salesReturnLines size is = "+salesReturnLines.size());
	        for (LtSalesReturnLines line : salesReturnLines) {
	            salesReturnLinesMap.computeIfAbsent(line.getSalesReturnHeaderId(), k -> new ArrayList<>()).add(line);
	        }
 
	        if (!salesReturnLinesMap.isEmpty()) {
	            return salesReturnLinesMap;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ServerException("Error fetching sales return lines", e);
	    }
	    return salesReturnLinesMap;
	}
	
	
	@Override
	public List<LtMastUsers> getAllUsersForEmail(String outletId) throws ServerException {
			String query = env.getProperty("getAllUsersForEmail");
			List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
					new Object[] {outletId },
					new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

			if (!ltMastUserslist.isEmpty()) {
				return ltMastUserslist;
			}else {
				return null;
			}
	}
	
	@Override
	public String getMobileNoFromOrderNo(String orderNo) throws ServerException {
		String query = env.getProperty("getMobileNoFromOrderNo");
		String mobileNo = jdbcTemplate.queryForObject(query, new Object[] {orderNo}, String.class);
		return mobileNo;
	}

	@Override
	public String findDistributorIdAgainstUser(Long createdBy) throws ServiceException, IOException {
		String distId;
		String sql = env.getProperty("findDistributorIdAgainstUser");
		distId = jdbcTemplate.queryForObject(sql, new Object[] { createdBy }, String.class);
		return distId;
	}
	
	@Override
	public List<LtMastUsers> getAllAreaHeadAgainstDist(String distributorId) throws ServiceException, IOException {
		String query = env.getProperty("getAllAreaHeadAgainstDist");
		System.out.println("getAllAreaHeadAgainstDist query=="+ query);
		System.out.println("distributorId ---"+distributorId);
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {distributorId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		System.out.println("ltMastUserslist --"+ltMastUserslist);
		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}
	
	@Override
	public List<LtMastUsers> getAllSalesOfficersAgainstDist(String distributorId)throws ServiceException, IOException{
		String query = env.getProperty("getAllSalesOfficersAgainstDist");
		System.out.println("getAllSalesOfficersAgainstDist query=="+ query);
		System.out.println("distributorId ---"+distributorId);
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {distributorId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));
		System.out.println("ltMastUserslist --"+ltMastUserslist);
		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}
	
	@Override
	public String getMobileNoFromCreatedBy(Long createdBy) throws ServerException {
		String query = env.getProperty("getMobileNoFromCreatedBy");
		String mobileNo = jdbcTemplate.queryForObject(query, new Object[] {createdBy}, String.class);
		return mobileNo;
	}
}

