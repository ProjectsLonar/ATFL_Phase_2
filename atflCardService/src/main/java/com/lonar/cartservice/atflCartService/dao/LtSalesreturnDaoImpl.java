package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.dto.LtSalesReturnDto;
import com.lonar.cartservice.atflCartService.dto.RequestDto;
import com.lonar.cartservice.atflCartService.dto.ResponseDto;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnAvailability;
import com.lonar.cartservice.atflCartService.model.LtSalesReturnStatus;


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
		

		List<LtSalesReturnAvailability> list = jdbcTemplate.query(query,
				new Object[] { distributorCode },
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
	public List<Long> getSalesReturnHeader(RequestDto requestDto)throws ServerException{
		try {
		String query = env.getProperty("getSalesReturnHeader");
		

		List<Long> list = jdbcTemplate.query(query,
				new Object[] { requestDto.getReturnStatus(), requestDto.getInvoiceNumber(), requestDto.getSalesReturnHeaderId(),
						requestDto.getLimit(), requestDto.getOffset()},
				new BeanPropertyRowMapper<Long>(Long.class));

		if (!list.isEmpty()) {
			return list;
		}

	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Long getSalesReturnRecordCount(RequestDto requestDto)throws ServerException{
		try {
		String query = env.getProperty("getSalesReturnRecordCount");
		

		Long count = jdbcTemplate.queryForObject(query,
				new Object[] { requestDto.getReturnStatus(), requestDto.getInvoiceNumber(), requestDto.getSalesReturnHeaderId()},
				new BeanPropertyRowMapper<Long>(Long.class));

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
}
