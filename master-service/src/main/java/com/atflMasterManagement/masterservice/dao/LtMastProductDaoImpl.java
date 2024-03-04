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
import com.atflMasterManagement.masterservice.model.CodeMaster;
//import com.atflMasterManagement.masterservice.model.LtMastProducts;
import com.atflMasterManagement.masterservice.model.LtMastProducts;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.repository.LtMastProductsRepository;

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
		try {
			String query = env.getProperty("getUserTypeByUserId");
			List<RequestDto> dataList = jdbcTemplate.query(query, new Object[] { userId },
					new BeanPropertyRowMapper<RequestDto>(RequestDto.class));
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
	public Long getInStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getInStockProductCountForAdmin");
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
				 searchField,requestDto.getOrgId() }, Long.class);
		return count;
	}
	
	@Override
	public List<ProductDto> getOutOfStockProductForAdmin(RequestDto requestDto) throws ServiceException, IOException {
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
	public Long getOutOfStockProductCountForAdmin(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getOutOfStockProductCountForAdmin");
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getProductId(), requestDto.getCategoryId(),
				 searchField,requestDto.getOrgId() }, Long.class);
		return count;
	}
	
	@Override
	public List<ProductDto> getInStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		//try {
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
			}
			System.out.print("Input requestDto= "+requestDto);
              //System.out.print(query);
//			List<ProductDto> productsList = jdbcTemplate.query(query,
//					new Object[] { requestDto.getOrgId(), requestDto.getProductId(), requestDto.getCategoryId(),
//							searchField,requestDto.getOutletId(), requestDto.getLimit(), requestDto.getOffset() },
//					new BeanPropertyRowMapper<ProductDto>(ProductDto.class));
			
			 return jdbcTemplate.query(query,new Object[] { requestDto.getOrgId(), requestDto.getProductId(), 
					 requestDto.getCategoryId(),searchField,requestDto.getOutletId(), requestDto.getLimit(), 
					 requestDto.getOffset()},
					new BeanPropertyRowMapper<ProductDto>(){
				 public ProductDto mapRow(ResultSet rs, int row) throws SQLException {    
					ProductDto e=new ProductDto();  
					
					 e.setInventoryCode(rs.getString("INVENTORY_CODE"));
					 System.out.println(rs.getString("INVENTORY_CODE"));
					 e.setProductId(rs.getString("PRODUCT_ID"));
					 System.out.println("1234="+rs.getString("productId"));
					 System.out.println("123="+rs.getString("PRODUCT_ID"));
					 System.out.print("ProductList " + e);
					 e.setOrgId(rs.getString("ORG_ID"));
					 e.setCategoryId(rs.getString("CATEGORY_ID"));
					 return e;
					 
			        }    
			    });    
			 
					
//			if (!productsList.isEmpty()) {
//				return productsList;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	@Override
	public Long getInStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getInStockProductCountWithInventory");
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
		Long count = getJdbcTemplate().queryForObject(query, new Object[] {requestDto.getOrgId(), requestDto.getProductId(), requestDto.getCategoryId(),
				searchField,requestDto.getOutletId() }, Long.class);
		return count;
	}

	@Override
	public List<ProductDto> getOutOfStockProductWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		try {
			String query = env.getProperty("getOutOfStockProductWithInventory");
			
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
	public Long getOutOfStockProductCountWithInventory(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getOutOfStockProductCountWithInventory");
		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}
		Long count = getJdbcTemplate().queryForObject(query, new Object[] { requestDto.getOrgId(), requestDto.getProductId(), requestDto.getCategoryId(),
				searchField,requestDto.getOutletId() }, Long.class);
		return count;
	}

	
}


