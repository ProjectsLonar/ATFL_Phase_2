package com.atflMasterManagement.masterservice.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dto.ProductDto;
import com.atflMasterManagement.masterservice.dto.TlEtlDto;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastProductCat;
//import com.atflMasterManagement.masterservice.model.LtMastProducts;
import com.atflMasterManagement.masterservice.model.LtMastProducts;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.repository.LtMastProductsRepository;
import com.atflMasterManagement.masterservice.servcies.ConsumeApiService;

@Repository
@PropertySource(value = "classpath:queries/ltMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastProductDaoImpl implements LtMastProductDao, CodeMaster {
	static final Logger logger = LoggerFactory.getLogger(LtMastProductDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;
	
	@Autowired
	private LtMastProductsRepository ltMastProductsRepository;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<ProductDto> getProduct(RequestDto requestDto) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getProduct");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
             System.out.print(query);
			List<ProductDto> productsList = jdbcTemplate.query(query,
					new Object[] { requestDto.getOrgId(), requestDto.getProductId(), requestDto.getCategoryId(),
							requestDto.getOutletId(), searchField, requestDto.getLimit(), requestDto.getOffset() },
					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Long getProductCount(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getProductCount");
		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getOrgId(),
				requestDto.getProductId(), requestDto.getCategoryId(), requestDto.getOutletId() }, Long.class);
		return count;
	}

	@Override
	public List<LtMastProducts> getAllProduct() throws ServiceException, IOException {
		
		List<LtMastProducts> productList = new ArrayList<LtMastProducts>();
		Iterable<LtMastProducts> list = ltMastProductsRepository.findAll();
		
		for (LtMastProducts LtMastProducts : list) {
			productList.add(LtMastProducts);
		}
		if (productList.isEmpty()) {
			return productList;
		} else {
			return productList;
		}
	}
	
	@Override
	public List<ProductDto> getProductForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getProductForAdmin");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
                 System.out.print(query);
			List<ProductDto> productsList = jdbcTemplate.query(query,
					new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
							 searchField,requestDto.getOrgId(), requestDto.getLimit(), requestDto.getOffset() },
					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Long getProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getProductCountForAdmin");
		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getOrgId(),
				requestDto.getProductId(), requestDto.getCategoryId() }, Long.class);
		return count;
	}

	@Override
	public String getUserTypeByUserId(String userId) throws ServiceException, IOException {
		List<RequestDto> dataList = new ArrayList<>();
		ConsumeApiService consumeApiService = new ConsumeApiService();
		try {
			String query = env.getProperty("getUserTypeByUserId");
//			List<RequestDto> dataList = jdbcTemplate.query(query, new Object[] { userId },
//					new BeanPropertyRowMapper<RequestDto>(RequestDto.class));
			
			try {
				dataList = consumeApiService.consumeApi(query, 
						new Object[] { userId }, RequestDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			if (!dataList.isEmpty()) {
				return dataList.get(0).getUserType();
			}else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ProductDto> getProductWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getProductWithInventory");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}
			
			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
              //System.out.print(query);
			List<ProductDto> productsList = jdbcTemplate.query(query,
					new Object[] { requestDto.getOrgId(), requestDto.getProductId(), requestDto.getCategoryId(),
							searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() },
					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Long getProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getProductCountWithInventory");
		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getOrgId(),
				requestDto.getProductId(), requestDto.getCategoryId(), requestDto.getOutletId() }, Long.class);
		return count;
	}
	
	@Override
	public List<ProductDto> getInStockProductAdmin(RequestDto requestDto) throws ServiceException, IOException {
		try {
			System.out.println("In dao getInStockProduct");
			String query = env.getProperty("getInStockProductAdmin");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}else {
				searchField="";
			}
                 System.out.println(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
//							 searchField,//requestDto.getOrgId(), 
//							 requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
                
                 List<ProductDto> productsList = new ArrayList<>();
     			ConsumeApiService consumeApiService = new ConsumeApiService();

                 try {
                	 productsList = consumeApiService.consumeApi(query, 
         					new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
       							 searchField,//requestDto.getOrgId(), 
       							 requestDto.getLimit(), requestDto.getOffset() }, 
         					ProductDto.class);
         		} catch (IOException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		} catch (InterruptedException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
         	     
                 if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<ProductDto> getInStockProductForAreaHead(RequestDto requestDto) throws ServiceException, IOException {
		try {
			System.out.println("In dao getInStockProduct");
			String query = env.getProperty("getInStockProductForAreaHead");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}else {
				searchField="";
			}
                 System.out.println(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
//							 searchField,//requestDto.getOrgId(), 
//							 requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
                
                 List<ProductDto> productsList = new ArrayList<>();
     			ConsumeApiService consumeApiService = new ConsumeApiService();

                 try {
                	 productsList = consumeApiService.consumeApi(query, 
         					new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
       							 searchField,//requestDto.getOrgId(), 
       							 requestDto.getLimit(), requestDto.getOffset() }, 
         					ProductDto.class);
         		} catch (IOException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		} catch (InterruptedException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
         	     
                 if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Long getInStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getInStockProductCountForAdmin");
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
//		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getProductId(), 
//				requestDto.getCategoryId(), searchField//requestDto.getOrgId() 
//				}, Long.class);
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			Long count = consumeApiService.consumeApiForCount(query, 
					new Object[] { requestDto.getProductId(), 
							requestDto.getCategoryId(), searchField//requestDto.getOrgId() 
							});
			return count;
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
	public List<ProductDto> getOutOfStockProductForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		List<ProductDto> productsList =  new ArrayList<>();
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		try {
			String query = env.getProperty("getOutOfStockProductForAdmin");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
                 System.out.println(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
//							 searchField,//requestDto.getOrgId(), 
//							 requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
                 
                 try {
                	 productsList = consumeApiService.consumeApi(query, 
                			 new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
        							 searchField,//requestDto.getOrgId(), 
        							 requestDto.getLimit(), requestDto.getOffset() }, 
                					 ProductDto.class);
         		} catch (IOException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		} catch (InterruptedException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
			if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Long getOutOfStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getOutOfStockProductCountForAdmin");
		String searchField = null;
		Long count= 0L;
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
//		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
//				 searchField,requestDto.getOrgId() }, Long.class);
		
		try {
			count = consumeApiService.consumeApiForCount(query, 
					new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
							 searchField,requestDto.getOrgId()});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	@Override
	public List<ProductDto> getInStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		try {
			//System.out.println("In daoooo getInStockProduct");
			String query = env.getProperty("getInStockProductWithInventory");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}else {
				searchField="";
			}
			
			//System.out.println("Input requestDto= "+requestDto);
             // System.out.println(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] { //requestDto.getOrgId(), 
//							requestDto.getProductId(), 
//							requestDto.getCategoryId(),
//							searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
              List<ProductDto> productsList = new ArrayList<>();
  			ConsumeApiService consumeApiService = new ConsumeApiService();

              try {
            	  productsList = consumeApiService.consumeApiWithRequestBody(query, 
      					new Object[] { //requestDto.getOrgId(), 
    							requestDto.getProductId(), 
    							requestDto.getCategoryId(),
    							searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() }, 
      					ProductDto.class);
      		} catch (IOException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		} catch (InterruptedException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
			
			System.out.println("products dao is ======" +productsList);
//			 return jdbcTemplate.query(query,new Object[] { requestDto.getOrgId(), requestDto.getProductId(), 
//					 requestDto.getCategoryId(),searchField,requestDto.getOutletId(), requestDto.getLimit(), 
//					 requestDto.getOffset()},
//					new BeanPropertyRowMapper<ProductDto>(){
//				 public ProductDto mapRow(ResultSet rs, int row) throws SQLException {    
//					ProductDto e=new ProductDto();  
//					
//					 e.setInventoryCode(rs.getString("INVENTORY_CODE"));
//					 System.out.println(rs.getString("INVENTORY_CODE"));
//					 e.setProductId(rs.getString("PRODUCT_ID"));
//					 System.out.println("1234="+rs.getString("productId"));
//					 System.out.println("123="+rs.getString("PRODUCT_ID"));
//					 System.out.print("ProductList " + e);
//					 e.setOrgId(rs.getString("ORG_ID"));
//					 e.setCategoryId(rs.getString("CATEGORY_ID"));
//					 return e;
//					 
//			        }    
//			    });    
//			 
					
			if (!productsList.isEmpty()) {				return productsList;
			}
			System.out.println("products List is ======" +productsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Long getInStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getInStockProductCountWithInventory");
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
//		Long count = getJdbcTemplate().queryForObject(query, new Object[] {//requestDto.getOrgId(), 
//				requestDto.getProductId(), requestDto.getCategoryId(),
//				searchField,requestDto.getOutletId() }, Long.class);
		
		ConsumeApiService consumeApiService = new ConsumeApiService();

		try {
			Long count = consumeApiService.consumeApiForCountWithRequestBody(query, 
					new Object[] { //requestDto.getOrgId(), 
							requestDto.getProductId(), requestDto.getCategoryId(),
							searchField,requestDto.getOutletId()  });
			return count;
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
	public List<ProductDto> getOutOfStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getOutOfStockProductWithInventory");
			List<ProductDto> productsList = new ArrayList<>();
			ConsumeApiService consumeApiService =  new ConsumeApiService();
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
              System.out.print("New request Dto = "+ requestDto);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] { requestDto.getOrgId(), 
//							requestDto.getProductId(), requestDto.getCategoryId(),
//							searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				productsList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { requestDto.getOrgId(), 
							requestDto.getProductId(), requestDto.getCategoryId(),
								searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() }, 
						ProductDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Long getOutOfStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getOutOfStockProductCountWithInventory");
		String searchField = null;
		Long count = 0L;
		ConsumeApiService consumeApiService = new ConsumeApiService();
		
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
//		Long count = getJdbcTemplate().queryForObject(query, new Object[] { //requestDto.getOrgId(), 
//				requestDto.getProductId(), requestDto.getCategoryId(),
//				searchField,requestDto.getOutletId() }, Long.class);
		
		try {
			count = consumeApiService.consumeApiForCountWithRequestBody(query, 
					new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
						searchField,requestDto.getOutletId() });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}

	@Override
	public List<ProductDto> getMultipleMrpForProduct(String distId, String outId, String prodId, String priceList)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ProductDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForProduct");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				productList = consumeApiService.consumeApi(query, 
						new Object[] { distId, outId, prodId, priceList}, 
						ProductDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	public List<ProductDto> getMultipleMrpForProductV1(String prodId, String distId)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ProductDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				System.out.println("prodId & distId = " + prodId +"/t"+ distId);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distId,prodId,prodId,distId,prodId }, 
						ProductDto.class);
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
	public List<ProductDto> getMultipleMrpForInStockProductV3(String prodId, String distId, String priceList)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ProductDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForInStockProductV3");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				System.out.println("prodId & distId = " + prodId +"/t"+ distId);
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distId,prodId,prodId,distId,prodId,priceList }, 
						ProductDto.class);
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
	public List<ProductDto> getMultipleMrpForProductV2(List<String> prodId, String distId)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ProductDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distId,prodId,prodId,distId,prodId }, 
						ProductDto.class);
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
	public List<ProductDto> getMultipleMrpForOutofStockProductV1(String prodId, String distId, String priceList)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ProductDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForOutofStockProductV1");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distId,prodId,prodId,distId,prodId }, 
						ProductDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	public List<ProductDto> getMultipleMrpForOutofStockProductV2(String prodId, String distId, String priceList)
			throws ServiceException, IOException {
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		List<ProductDto> productList = new ArrayList<>();
		try{
			String query = env.getProperty("getMultipleMrpForOutofStockProductV2");
//		    List<ProductDto> productList = jdbcTemplate.query(query,new Object[] { distId, outId, prodId, priceList },
//				new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			try {
				productList = consumeApiService.consumeApiWithRequestBody(query, 
						new Object[] { prodId,distId,prodId,prodId,distId,prodId,priceList}, 
						ProductDto.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	public List<TlEtlDto> getTlForProductDescription(String priceList, String productId)
			throws ServiceException, IOException {
		   
		List<TlEtlDto> tlList = new ArrayList<TlEtlDto>();		
		try {
			String query = env.getProperty("getTlForProductDescription");
			
			tlList = jdbcTemplate.query(query, new Object[] {priceList,productId},
					new BeanPropertyRowMapper<TlEtlDto>(TlEtlDto.class));
			if(!tlList.isEmpty()) {
				return tlList;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}return tlList;
	}

	@Override
	public List<TlEtlDto> getEtlForProductDescription(String priceList, String productId)
			throws ServiceException, IOException {
		List<TlEtlDto> etlList = new ArrayList<TlEtlDto>();		
		try {
			String query = env.getProperty("getEtlForProductDescription");
			
			etlList = jdbcTemplate.query(query, new Object[] {priceList,productId},
					new BeanPropertyRowMapper<TlEtlDto>(TlEtlDto.class));
			if(!etlList.isEmpty()) {
				return etlList;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}return etlList;
	}
	
	@Override
	public List<ProductDto> getProductListFromProcedure() throws ServiceException, IOException {
	
		String query = env.getProperty("getProductListFromProcedure");
		List<ProductDto> productList = new ArrayList<>();
//		List<ProductDto> productList = jdbcTemplate.query(query, new Object[] {},
//				new BeanPropertyRowMapper<>(ProductDto.class));
		
		ConsumeApiService consumeApiService = new ConsumeApiService();
		try {
			productList = consumeApiService.consumeApi(query, 
					new Object[] {}, ProductDto.class);
			return productList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productList;
	}
 
	@Override
	public List<ProductDto> getMultipleMrpFromProcedure() throws ServiceException, IOException {
		
		String query = env.getProperty("getMultipleMrpFromProcedure");
		List<ProductDto> multipleMrpList = new ArrayList<>();
//		List<ProductDto> multipleMrpList = jdbcTemplate.query(query, new Object[] {},
//				new BeanPropertyRowMapper<>(ProductDto.class));
		
		ConsumeApiService consumeApiService = new ConsumeApiService();
		try {
			multipleMrpList = consumeApiService.consumeApi(query, 
					new Object[] {}, ProductDto.class);
			return multipleMrpList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return multipleMrpList;
	
	}

	@Override
	public Long getProductCountFromProcedure() throws ServiceException, IOException {
		
		String query = env.getProperty("getProductCountFromProcedure");
		//Long productCount = jdbcTemplate.queryForObject(query, new Object[] {}, Long.class); 
		Long productCount = 0L;	
		ConsumeApiService consumeApiService = new ConsumeApiService();
		try {
			productCount = consumeApiService.consumeApiForCount(query, 
					new Object[] {});
			return productCount;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productCount;
	}

	@Override
	public List<ProductDto> getInstockProductsForSysAdminTemplate(RequestDto requestDto)
			throws ServiceException, IOException {
		try {
			System.out.println("In dao getInStockProduct");
			String query = env.getProperty("getInstockProductsForSysAdminTemplate");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}else {
				searchField="";
			}
                 System.out.println(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
//							 searchField,//requestDto.getOrgId(), 
//							 requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
                
                 List<ProductDto> productsList = new ArrayList<>();
     			ConsumeApiService consumeApiService = new ConsumeApiService();

                 try {
                	 productsList = consumeApiService.consumeApi(query, 
         					new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
       							 searchField,//requestDto.getOrgId(), 
       							 requestDto.getLimit(), requestDto.getOffset() }, 
         					ProductDto.class);
         		} catch (IOException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		} catch (InterruptedException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
         	     
                 if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ProductDto> getOutOfStockProductsForSysAdminTemplate(RequestDto requestDto)
			throws ServiceException, IOException {
		List<ProductDto> productsList =  new ArrayList<>();
		ConsumeApiService consumeApiService =  new ConsumeApiService();
		try {
			String query = env.getProperty("getOutOfStockProductsForSysAdminTemplate");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}
                 System.out.println(query);                 
                 try {
                	 productsList = consumeApiService.consumeApi(query, 
                			 new Object[] {requestDto.getProductId(), requestDto.getCategoryId(),
        							 searchField, 
        							 requestDto.getLimit(), requestDto.getOffset() }, 
                					 ProductDto.class);
         		} catch (IOException e) {
         			e.printStackTrace();
         		} catch (InterruptedException e) {
         			e.printStackTrace();
         		}
			if (!productsList.isEmpty()) {
				return productsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ProductDto> getInStockProductWithInventoryHardCode(RequestDto requestDto) throws ServiceException, IOException {
		try {
			//System.out.println("In daoooo getInStockProduct");
			String query = env.getProperty("getInStockProductWithInventoryHardCode");
			
			if (requestDto.getLimit() == 0 || requestDto.getLimit() == 1) {
				requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
			}
			
			if(requestDto.getOffset() == 0) {
				requestDto.setOffset(Integer.parseInt(env.getProperty("offset_value")));
			}

			String searchField = null;
			if (requestDto.getSearchField() != null) {
				searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
			}else {
				searchField="";
			}
			
			//System.out.println("Input requestDto= "+requestDto);
             // System.out.println(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] { //requestDto.getOrgId(), 
//							requestDto.getProductId(), 
//							requestDto.getCategoryId(),
//							searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
              List<ProductDto> productsList = new ArrayList<>();
  			ConsumeApiService consumeApiService = new ConsumeApiService();
  			String outletId="";
  			if(requestDto.getOutletId().equalsIgnoreCase("") || requestDto.getOutletId()==null) {
  				outletId = "1-EEWE-159";
  			}else {
  				outletId = requestDto.getOutletId();
  			} 
                 System.out.println("query are = "+query+outletId);
              try {
            	  productsList = consumeApiService.consumeApiWithRequestBody(query, 
      					new Object[] { //requestDto.getOrgId(), 
    							requestDto.getProductId(), 
    							requestDto.getCategoryId(),
    							searchField, outletId, //requestDto.getOutletId(), 
    							requestDto.getLimit(), requestDto.getOffset() }, 
      					ProductDto.class);
      		} catch (IOException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		} catch (InterruptedException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
			
			System.out.println("products dao is ======" +productsList);
//			 return jdbcTemplate.query(query,new Object[] { requestDto.getOrgId(), requestDto.getProductId(), 
//					 requestDto.getCategoryId(),searchField,requestDto.getOutletId(), requestDto.getLimit(), 
//					 requestDto.getOffset()},
//					new BeanPropertyRowMapper<ProductDto>(){
//				 public ProductDto mapRow(ResultSet rs, int row) throws SQLException {    
//					ProductDto e=new ProductDto();  
//					
//					 e.setInventoryCode(rs.getString("INVENTORY_CODE"));
//					 System.out.println(rs.getString("INVENTORY_CODE"));
//					 e.setProductId(rs.getString("PRODUCT_ID"));
//					 System.out.println("1234="+rs.getString("productId"));
//					 System.out.println("123="+rs.getString("PRODUCT_ID"));
//					 System.out.print("ProductList " + e);
//					 e.setOrgId(rs.getString("ORG_ID"));
//					 e.setCategoryId(rs.getString("CATEGORY_ID"));
//					 return e;
//					 
//			        }    
//			    });    
//			 
					
			if (!productsList.isEmpty()) {				return productsList;
			}
			System.out.println("products List is ======" +productsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}


