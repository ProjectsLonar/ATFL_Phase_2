package com.lonar.cartservice.atflCartService.dao;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.model.LtTemplateHeaders;
import com.lonar.cartservice.atflCartService.model.LtTemplateLines;
import com.lonar.cartservice.atflCartService.repository.LtTemplateHeadersRepository;
import com.lonar.cartservice.atflCartService.repository.LtTemplateLinesRepository;



@Repository
@PropertySource(value = "classpath:queries/cartmasterqueries.properties", ignoreResourceNotFound = true)
@Transactional(propagation=Propagation.MANDATORY)
public class LtTemplateDaoImpl implements LtTemplateDao,CodeMaster{
	@Autowired
	private Environment env;

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	@Autowired
	LtTemplateHeadersRepository ltTemplateHeadersRepository;
	
	@Autowired
	LtTemplateLinesRepository ltTemplateLinesRepository;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public LtTemplateHeaders getTemplateAgainstDistributor(String distributorId,Long templateHeaderId)throws ServerException{
	String query = env.getProperty("getTemplateAgainstDistributor");
	try {
		
		List<LtTemplateHeaders> templateHeader = jdbcTemplate.query(query,
				new Object[] { distributorId, templateHeaderId },
				new BeanPropertyRowMapper<LtTemplateHeaders>(LtTemplateHeaders.class));
		 
		
		//LtTemplateHeaders templateHeader= jdbcTemplate.queryForObject(query, new Object[] {distributorId, templateHeaderId }, LtTemplateHeaders.class);
	System.out.println("templateHeader"+templateHeader);
	if (!templateHeader.isEmpty()) {
		return templateHeader.get(0);
	}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	
	@Override
	public LtTemplateHeaders getTemplateAgainstDistributors(String distributorId)throws ServerException{
	String query = env.getProperty("getTemplateAgainstDistributors");
	try {
		
		List<LtTemplateHeaders> templateHeader = jdbcTemplate.query(query,
				new Object[] { distributorId },
				new BeanPropertyRowMapper<LtTemplateHeaders>(LtTemplateHeaders.class));
		 
		
		//LtTemplateHeaders templateHeader= jdbcTemplate.queryForObject(query, new Object[] {distributorId, templateHeaderId }, LtTemplateHeaders.class);
	System.out.println("templateHeader"+templateHeader);
	if (!templateHeader.isEmpty()) {
		return templateHeader.get(0);
	}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return null;
	}

	@Override
	public List<LtTemplateLines> getProductDetailsAgainstheaderId(Long templateHeaderId)throws ServerException{
		String query = env.getProperty("getProductDetailsAgainstheaderId");
		List<LtTemplateLines> templateproductlist = jdbcTemplate.query(query,
				new Object[] {templateHeaderId},
				new BeanPropertyRowMapper<LtTemplateLines>(LtTemplateLines.class));
		if (!templateproductlist.isEmpty()) {
			return templateproductlist;
		}
		return null;
	}
	
	@Override
	@Transactional
	public void deletelinedetailsbytemplateid(Long templateHeaderId) throws ServiceException {
		String query = env.getProperty("deletelinedetailsbytemplateid");
		Object[] person = new Object[] { templateHeaderId };
		jdbcTemplate.update(query, person);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtTemplateHeaders saveheaderData(LtTemplateHeaders ltTemplateHeaders) throws ServiceException{
		return ltTemplateHeadersRepository.save(ltTemplateHeaders);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LtTemplateLines saveLineData (LtTemplateLines ltTemplateLines)throws ServiceException{
		return ltTemplateLinesRepository.save(ltTemplateLines);
	}
	
	@Override
	@Transactional
	public void deleteHeaderdetailsbytemplateid(String distributorId) throws ServiceException {
		String query = env.getProperty("deleteHeaderdetailsbytemplateid");
		Object[] person = new Object[] { distributorId };
		jdbcTemplate.update(query, person);
	}
	@Override
	public LtTemplateHeaders getTemplateAgainstDistributor12(String distributorId) throws ServiceException {
		String query = env.getProperty("getTemplateAgainstDistributor12");
		List<LtTemplateHeaders> templateHeadersList = jdbcTemplate.query(query,
				new Object[] {distributorId},
				new BeanPropertyRowMapper<LtTemplateHeaders>(LtTemplateHeaders.class));
		System.out.println("Hi I'm in query error..... "+templateHeadersList);
		if (templateHeadersList.size()>0) {
			return templateHeadersList.get(0);
		}
		return null;
	}
	
	@Override
	public List<LtTemplateLines> getProductDetailsAgainstheaderId(Long templateHeaderId, String priceList)throws ServerException{
		String query = env.getProperty("getProductDetailsAgainstheaderId1");
		List<LtTemplateLines> templateproductlist = jdbcTemplate.query(query,
				new Object[] {templateHeaderId, priceList},
				new BeanPropertyRowMapper<LtTemplateLines>(LtTemplateLines.class));
		if (!templateproductlist.isEmpty()) {
			return templateproductlist;
		}
		return null;
	}
	@Override
	public LtTemplateHeaders getAllTemplateAgainstDistributors(String distId) throws ServerException {
		String query = env.getProperty("getTemplateAgainstDistributors");
		List<LtTemplateHeaders> templateHeadersList = jdbcTemplate.query(query, new Object[] {distId},
				new BeanPropertyRowMapper<LtTemplateHeaders>(LtTemplateHeaders.class));
		System.out.println("All tempalte query"+query);
		System.out.println("All Template HeadersList =="+templateHeadersList);
		if(!templateHeadersList.isEmpty()) {
			return templateHeadersList.get(0);
		}
		return null;
	}
}
